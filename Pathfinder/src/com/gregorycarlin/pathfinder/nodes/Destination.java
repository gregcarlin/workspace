package com.gregorycarlin.pathfinder.nodes;

import java.awt.Color;

public class Destination extends Node {
	private static final long serialVersionUID = -3759991342736566071L;

	public Destination(double x, double y) {
		super(x, y, 5);
	}

	@Override
	public Color getDefaultColor() {
		return Color.BLUE;
	}
}
