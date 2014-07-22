package com.gregorycarlin.pathfinder;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import com.gregorycarlin.pathfinder.nodes.Node;

import acm.graphics.GLine;
import acm.graphics.GPoint;

/**
 * A representation of a graph node.
 * 
 * @author Lekan Wang (lekan@lekanwang.com)
 *
 */
public class Edge extends GLine {
	private static final long serialVersionUID = -4714862974467962357L;
	
	private final Node n1, n2;
	private final double cost;
	
	/**
	 * Creates an edge that connects nodes n1 and n2.
	 */
	public Edge(Node n1, Node n2) {
		super(n1.getX() + n1.getRadius(), n1.getY() + n1.getRadius(), n2.getX() + n2.getRadius(), n2.getY() + n2.getRadius());
		n1.addEdge(this);
		n2.addEdge(this);
		this.n1 = n1;
		this.n2 = n2;
		this.cost = n1.distanceTo(n2);
	}
	
	/**
	 * Gets both nodes as a Pair<Node> object. You can access
	 * the individual Nodes by saying pair.left, and pair.right
	 * once you have the pair.
	 * 
	 * This method does not guarantee any ordering on the nodes.
	 * Hence, this could be useful when working with undirected
	 * graphs. (*hint hint*)
	 * 
	 * @return
	 */
	public Pair<Node> getNodes() {
		return new Pair<Node>(n1, n2);
	}
	
	/**
	 * Returns the first node. Because this guarantees an order,
	 * it could be useful when working with directed graphs.
	 * 
	 * @return
	 */
	public Node getNode1() {
		return this.n1;
	}
	
	/**
	 * Returns the second node. Because this guarantees an order,
	 * it could be useful when working with directed graphs.
	 * 
	 * @return
	 */
	public Node getNode2() {
		return this.n2;
	}
	
	/**
	 * Assuming that n is one of the nodes connected with this edge, returns the node that is not n.
	 * 
	 * @param n
	 * @return
	 */
	public Node getNodeThatsNot(Node n) {
		if(getNode1().equals(n)) return getNode2();
		else if(getNode2().equals(n)) return getNode1();
		else return null;
	}
	
	public double getCost() {
		return cost;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Edge) {
			Edge other = (Edge) obj;
			return other.getNode1().equals(getNode1()) && other.getNode2().equals(getNode2());
		}
		return false;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		GPoint start = getStartPoint();
		GPoint end = getEndPoint();
		String text = Long.toString(Math.round(cost));
		FontMetrics fm = g.getFontMetrics();
		int w = fm.stringWidth(text);
		int h = fm.getHeight();
		int x = (int) ((start.getX() + end.getX()) / 2 - w / 2);
		int y = (int) ((start.getY() + end.getY()) / 2 - h / 2);
		
		g.setColor(Color.BLACK);
		g.fillRect(x - 1, y, w + 2, h);
		g.setColor(Color.WHITE);
		g.drawString(text, x, (int) (y + h * 0.8));
		g.setColor(Color.BLACK);
	}
}
