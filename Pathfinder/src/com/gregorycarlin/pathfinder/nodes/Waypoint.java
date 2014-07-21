package com.gregorycarlin.pathfinder.nodes;

import java.awt.Color;

public class Waypoint extends Node {
	private static final long serialVersionUID = -5887050162539426741L;

	public Waypoint(double x, double y) {
		super(x, y, 3);
	}

	@Override
	public Color getDefaultColor() {
		return Color.GREEN;
	}
}
