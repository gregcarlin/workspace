import java.util.*;

/** An instance represents a Solver that intelligently determines 
 *  Moves using algorithm Minimax. */
public class AI implements Solver {

    private final Board.Player player; // the current player

    /** The depth of the search in the game space when evaluating moves. */
    private final int depth;

    /** Constructor: an instance with player p who searches to depth d
     * when searching the game space for moves. */
    public AI(Board.Player p, int d) {
        player = p;
        depth = d;
    }

    /** See Solver.getMoves for the specification. */
    public @Override Move[] getMoves(Board b) {
        State current = new State(player, b, b.getLastMove());
        createGameTree(current, depth);
        minimax(current);
        //current.writeToFile();
        State[] options = current.getChildren();
        Arrays.sort(options);
        //System.out.println("picking state with value " + options[options.length-1].getValue());
        return new Move[] {options[options.length-1].getLastMove()};
    }

    /** Generate the game tree with root s of depth d.
     * The game tree's nodes are State objects that represent the state of a game
     * and whose children are all possible States that can result from the next move.
     * NOTE: this method runs in exponential time with respect to d.
     * With d around 5 or 6, it is extremely slow and will start to take a very
     * long time to run.
     * Note: If s has a winner (4 in a row), it should be a leaf. */
    public static void createGameTree(State s, int d) {
        if(d == 0) return;
        
        s.initializeChildren();
        for(State child : s.getChildren()) {
        	createGameTree(child, d - 1);
        }
    }
    
    /** Call minimax in ai with state s. */
    public static void minimax(AI ai, State s) {
        ai.minimax(s);
    }

    /** State s is a node of a game tree (i.e. the current State of the game).
     * Use the Minimax algorithm to assign a numerical value to each State of the
     * tree rooted at s, indicating how desirable that State is to this player. */
    public void minimax(State s) {
        Board b = s.getBoard();
        
        Board.Player winner = b.hasConnectFour();
        if(winner == null) {
        	State[] children = s.getChildren();
        	if(children.length == 0) {
        		/*int adj = 0;
        		for(int i=0; i<Board.NUM_ROWS; i++) {
        			for(int j=0; j<Board.NUM_COLS; j++) {
        				Board.Player start = b.getPlayer(i, j);
        				if(start == null) continue;
        				for(int[] delta : Board.DELTAS) {
        					assert delta.length == 2;
        					int row = i + delta[0];
        					int col = j + delta[1];
        					if(b.isValidTile(row, col)) {
        						Board.Player p = b.getPlayer(row, col);
        						if(p != start) continue;
        						if(p == player) {
        							adj++; // increase value for adjacent tiles of ours
        						} else {
        							adj--; // decrease value for adjacent tiles not ours
        						}
        					}
        				}
        			}
        		}
        		s.setValue(adj);*/
        		int val = 0;
        		for(Board.Player[] wins : b.winLocations()) {
        			assert wins.length == 4;
        			int me = 0;
        			int other = 0;
        			for(Board.Player p : wins) {
        				if(p == null) continue;
        				else if(p == player) me++;
        				else other++;
        			}
        			assert me < 4 && other < 4;
        			if(me == 3) val += 3;
        			else if(other == 3) val -= 3;
        			else {
        				if(me == 0) {
        					val -= other;
        				} else if(other == 0) {
        					val += me;
        				}
        			}
        		}
        		s.setValue(val);
        	} else {
        		boolean max = player == s.getPlayer();
        		//System.out.println("looking for " + (max ? "max" : "min"));
        		int extreme = max ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        		for(State child : children) {
        			minimax(child);
        			int val = child.getValue();
        			if(max) {
        				if(val > extreme) extreme = val;
        			} else {
        				if(val < extreme) extreme = val;
        			}
        		}
        		s.setValue(extreme);
        	}
        } else if(player == winner) {
        	s.setValue(Integer.MAX_VALUE);
        } else {
        	s.setValue(Integer.MIN_VALUE);
        }
    }

    /** Evaluate the desirability of Board b for this player
     * Precondition: b is a leaf node of the game tree (because that is most
     * effective when looking several moves into the future). 
     * The desireability is calculated as follows.
     * 1. If the board does not have a winner: */
    public int evaluateBoard(Board b) {
        Board.Player winner= b.hasConnectFour();
        if (winner == null) {
            // Store in sum the value of board b. 
            int sum= 0;
            List<Board.Player[]> locs= b.winLocations();
            for (Board.Player[] loc : locs) {
                for (Board.Player p : loc) {
                    sum= sum + (p == player ? 1 : p != null ? -1 : 0);
                }
            }
            return sum;
        }
        // There is a winner
        int numEmpty= 0;
        for (int r= 0; r < Board.NUM_ROWS; r= r+1) {
            for (int c= 0; c < Board.NUM_COLS; c= c+1) {
                if (b.getTile(r, c) == null) numEmpty += 1;
            }
        }
        return (winner == player ? 1 : -1) * 10000 * numEmpty;

    }

}
