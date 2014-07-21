package com.gregorycarlin.pathfinder.search;

import java.util.List;

import com.gregorycarlin.pathfinder.nodes.Node;

public interface Search {
	public List<Node> findPath(Node start, Node end, List<Node> allNodes);
}
