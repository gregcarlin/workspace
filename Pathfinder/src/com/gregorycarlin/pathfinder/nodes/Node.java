package com.gregorycarlin.pathfinder.nodes;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gregorycarlin.pathfinder.Edge;

import acm.graphics.GOval;

/**
 * A representation of a graph node.
 * 
 * @author Lekan Wang (lekan@lekanwang.com)
 *
 */
public abstract class Node extends GOval {
	private static final long serialVersionUID = -7105255464531379104L;
	
	private final List<Edge> edges;
	private final int radius;
	
	/**
	 * Creates an empty node with no edges, and a default 0 value;
	 */
	public Node(double x, double y, int radius) {
		super(x - radius, y - radius, radius * 2, radius * 2);
		this.edges = new ArrayList<Edge>();
		this.radius = radius;
		setFilled(true);
		setColor(getDefaultColor());
	}
	
	
	/**
	 * Adds the given edge to this node. The edge must be not null
	 * and valid.
	 * 
	 * @param edge
	 */
	public void addEdge(Edge edge) {
		assert (edge != null);
		edges.add(edge);
	}
	
	/**
	 * Gets the list of edges as an unmodifiable list.
	 */
	public List<Edge> getEdges() {
		return Collections.unmodifiableList(edges);
	}
	
	public Edge getEdgeWith(Node n) {
		for(Edge e : getEdges()) {
			if(e.getNodeThatsNot(this).equals(n)) {
				return e;
			}
		}
		return null;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public abstract Color getDefaultColor();
	
	public double distanceTo(Node other) {
		double dX = other.getX() - getX();
		double dY = other.getY() - getY();
		return Math.sqrt(dX * dX + dY * dY);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Node) {
			Node other = (Node) obj;
			return getX() == other.getX() && getY() == other.getY() && getRadius() == other.getRadius();
		}
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("Node[x=%f,y=%f]", getX(), getY());
	}
}
