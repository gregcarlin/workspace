package com.gregorycarlin.pathfinder.search;

import com.gregorycarlin.pathfinder.search.fringe.Fringe;
import com.gregorycarlin.pathfinder.search.fringe.StackFringe;

public class DepthFirstSearch extends BasicSearch {

	@Override
	Fringe getFringe() {
		return new StackFringe();
	}
}
