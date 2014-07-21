package com.gregorycarlin.pathfinder.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gregorycarlin.pathfinder.Edge;
import com.gregorycarlin.pathfinder.nodes.Node;
import com.gregorycarlin.pathfinder.search.fringe.Fringe;

public abstract class BasicSearch implements Search {
	
	@Override
	public final List<Node> findPath(Node start, Node end, List<Node> allNodes) {
		Fringe fringe = getFringe();
		Set<Node> visited = new HashSet<Node>();
		Map<Node,Node> traversals = new HashMap<Node,Node>();
		
		visited.add(start);
		fringe.add(start);
		
		while(!fringe.isEmpty()) {
			Node t = fringe.select();
			if(t.equals(end)) {
				List<Node> path = new LinkedList<Node>();
				Node n = t;
				path.add(n);
				while((n = traversals.get(n)) != null) {
					path.add(0, n); // add to front
				}
				return path;
			}
			for(Edge e : t.getEdges()) {
				Node u = e.getNodeThatsNot(t);
				if(!visited.contains(u)) {
					visited.add(u);
					traversals.put(u, t);
					fringe.add(u);
				}
			}
		}
		
		return null; // end node not found
	}
	
	abstract Fringe getFringe();
}