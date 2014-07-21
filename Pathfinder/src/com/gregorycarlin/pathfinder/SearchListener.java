package com.gregorycarlin.pathfinder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.gregorycarlin.pathfinder.nodes.Node;
import com.gregorycarlin.pathfinder.search.Search;

public class SearchListener implements ActionListener {
	private final Pathfinder pathfinder;
	private final Search search;
	
	public SearchListener(Pathfinder pathfinder, Search search) {
		this.pathfinder = pathfinder;
		this.search = search;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		pathfinder.revertColors();
		pathfinder.search = this;
	}
	
	protected List<Node> search(Node start, Node end, List<Node> allNodes) {
		return search.findPath(start, end, allNodes);
	}
}
