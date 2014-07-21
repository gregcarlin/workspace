package com.gregorycarlin.pathfinder.search.fringe;

import com.gregorycarlin.pathfinder.nodes.Node;

public interface Fringe {
	public Node select();
	
	public void add(Node n);
	
	public boolean isEmpty();
}
