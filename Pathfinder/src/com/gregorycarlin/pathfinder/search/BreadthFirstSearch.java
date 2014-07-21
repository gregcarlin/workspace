package com.gregorycarlin.pathfinder.search;
import com.gregorycarlin.pathfinder.search.fringe.Fringe;
import com.gregorycarlin.pathfinder.search.fringe.QueueFringe;

public class BreadthFirstSearch extends BasicSearch {

	@Override
	Fringe getFringe() {
		return new QueueFringe();
	}
}
