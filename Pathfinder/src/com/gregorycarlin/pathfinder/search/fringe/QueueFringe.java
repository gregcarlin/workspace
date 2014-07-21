package com.gregorycarlin.pathfinder.search.fringe;

import java.util.LinkedList;
import java.util.Queue;

import com.gregorycarlin.pathfinder.nodes.Node;

public class QueueFringe implements Fringe {
	private final Queue<Node> queue = new LinkedList<Node>();
	
	@Override
	public Node select() {
		return queue.poll();
	}

	@Override
	public void add(Node n) {
		queue.add(n);
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
