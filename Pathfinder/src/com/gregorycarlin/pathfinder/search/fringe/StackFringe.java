package com.gregorycarlin.pathfinder.search.fringe;

import java.util.Stack;

import com.gregorycarlin.pathfinder.nodes.Node;

public class StackFringe implements Fringe {
	private final Stack<Node> stack = new Stack<Node>();

	@Override
	public Node select() {
		return stack.pop();
	}

	@Override
	public void add(Node n) {
		stack.add(n);
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}
}
