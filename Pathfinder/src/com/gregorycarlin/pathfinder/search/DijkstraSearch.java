package com.gregorycarlin.pathfinder.search;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import com.gregorycarlin.pathfinder.Edge;
import com.gregorycarlin.pathfinder.nodes.Node;

public class DijkstraSearch implements Search {

	@Override
	public List<Node> findPath(Node start, Node end, List<Node> allNodes) {
		final Map<Node,Double> dists = new HashMap<Node,Double>();
		final Map<Node,Node> traversals = new HashMap<Node,Node>();
		final Queue<Node> fringe = new PriorityQueue<Node>(allNodes.size()-1, new Comparator<Node>() {
			@Override
			public int compare(Node arg0, Node arg1) {
				return dists.get(arg0).compareTo(dists.get(arg1));
			}});
		
		dists.put(start, 0.0);
		for(Node n : allNodes) {
			if(!n.equals(start)) {
				dists.put(n, Double.MAX_VALUE);
			}
			fringe.add(n);
		}
		
		while(!fringe.isEmpty()) {
			Node u = fringe.poll();
			
			for(Edge e : u.getEdges()) {
				Node v = e.getNodeThatsNot(u);
				double alt = dists.get(u) + e.getCost();
				System.out.printf("cost to %s is %f%n", v, alt);
				if(alt < dists.get(v)) {
					System.out.printf("adding %s with dist %f%n", v, alt);
					dists.put(v, alt);
					traversals.put(v, u);
				}
			}
		}
		
		if(dists.get(end) == Double.MAX_VALUE) return null;
		
		List<Node> path = new LinkedList<Node>();
		Node n = end;
		path.add(n);
		while((n = traversals.get(n)) != null) {
			path.add(0, n);
		}
		return path;
	}
}
