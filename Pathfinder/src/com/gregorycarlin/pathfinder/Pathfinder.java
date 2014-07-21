package com.gregorycarlin.pathfinder;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.gregorycarlin.pathfinder.io.IO;
import com.gregorycarlin.pathfinder.nodes.Destination;
import com.gregorycarlin.pathfinder.nodes.Node;
import com.gregorycarlin.pathfinder.nodes.Waypoint;
import com.gregorycarlin.pathfinder.search.*;

import acm.graphics.*;
import acm.program.*;

/**
 * Who needs Google Maps when you have Pathfinder?
 * 
 * @author Greg Carlin
 *
 */
public class Pathfinder extends GraphicsProgram {
	private static final long serialVersionUID = -5846277133335276638L;

	public static void main(String[] args) {
		new Pathfinder().start(args);
	}
	
	private static enum DrawingMode {
		DESTINATION,
		WAYPOINT,
		EDGE;
	}
	
	// Constants
	public static final int APPLICATION_WIDTH = 1000;
	public static final int APPLICATION_HEIGHT = 620;
	private static final String IMAGE_PATH = "Stanfordmap-1000x618.png";
	
	private final List<Node> nodes = new ArrayList<Node>();
	private final List<Edge> edges = new ArrayList<Edge>();
	private GLabel editLabel;
	private DrawingMode mode;
	
	private Node startNode;
	private GLine currentLine;
	
	protected SearchListener search;
	private Node first;
	private boolean colored;
	
	@Override
	public void init() {
		setTitle("Pathfinder BETA!");
		
		editLabel = null;
		mode = null;
		startNode = null;
		currentLine = null;
		search = null;
		first = null;
		colored = false;
		
		try {
			GImage img = new GImage(ImageIO.read(new File(IMAGE_PATH)), 0, 0);
			add(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		final JButton addDestin = new JButton("Add Destination");
		addDestin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				assert editLabel != null;
				editLabel.setLabel("CLICK TO ADD DESTINATION");
				editLabel.setLocation(APPLICATION_WIDTH - editLabel.getWidth(), 10);
				mode = DrawingMode.DESTINATION;
			}});
		addDestin.setEnabled(false);
		
		final JButton addWay = new JButton("Add Waypoint");
		addWay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				assert editLabel != null;
				editLabel.setLabel("CLICK TO ADD WAYPOINT");
				editLabel.setLocation(APPLICATION_WIDTH - editLabel.getWidth(), 10);
				mode = DrawingMode.WAYPOINT;
			}});
		addWay.setEnabled(false);
		
		final JButton bfsButton = new JButton("BFS");
		bfsButton.addActionListener(new SearchListener(this, new BreadthFirstSearch()));
		
		final JButton dfsButton = new JButton("DFS");
		dfsButton.addActionListener(new SearchListener(this, new DepthFirstSearch()));
		
		final JButton dijButton = new JButton("Dijkstra");
		dijButton.addActionListener(new SearchListener(this, new DijkstraSearch()));
		
		final JButton editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				revertColors();
				if(editLabel != null) {
					editButton.setText("Edit");
					Pathfinder.this.remove(editLabel);
					editLabel = null;
					addDestin.setEnabled(false);
					addWay.setEnabled(false);
					bfsButton.setEnabled(true);
					dfsButton.setEnabled(true);
					dijButton.setEnabled(true);
					mode = null;
				} else {
					editButton.setText("Stop Editing");
					editLabel = new GLabel("EDIT MODE");
					editLabel.setLocation(APPLICATION_WIDTH - editLabel.getWidth(), 10);
					editLabel.setColor(Color.RED);
					Pathfinder.this.add(editLabel);
					addDestin.setEnabled(true);
					addWay.setEnabled(true);
					bfsButton.setEnabled(false);
					dfsButton.setEnabled(false);
					dijButton.setEnabled(false);
				}
			}});
		
		add(editButton, NORTH);
		add(addDestin, NORTH);
		add(addWay, NORTH);
		
		add(bfsButton, NORTH);
		add(dfsButton, NORTH);
		add(dijButton, NORTH);
		
		final JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				IO.save(nodes);
			}});
		
		final JButton load = new JButton("Load");
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Node> ns = IO.load();
				if(ns != null) {
					nodes.addAll(ns);
					for(Node n : ns) {
						add(n);
						for(Edge edge : n.getEdges()) {
							if(!edges.contains(edge)) {
								edges.add(edge);
								add(edge);
							}
						}
					}
				}
			}});
		load.getActionListeners()[0].actionPerformed(null); // load data
		
		add(save, EAST);
		add(load, EAST);
		
		addMouseListeners();
	}
	
	@Override
	public void run() {
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if(mode == null) {
			
			if(search == null) return;
			Node n = getNodeAt(x, y);
			if(n == null) return;

			if(first == null) {
				first = n;
				first.setColor(Color.RED);
			} else {
				List<Node> path = search.search(first, n, nodes);
				if(path == null) {
					colored = true;
					revertColors();
					first = null;
					search = null;
					JOptionPane.showMessageDialog(this, "Path not found.");
					return;
				}
				Node last = null;
				for(Node node : path) {
					node.setColor(Color.RED);
					if(last != null) node.getEdgeWith(last).setColor(Color.RED);
					last = node;
				}
				
				first = null;
				search = null;
				colored = true;
			}
			
		} else {
		
			switch(mode) {
			case DESTINATION:
				Destination destin = new Destination(x, y);
				add(destin);
				nodes.add(destin);
				break;
			case WAYPOINT:
				Waypoint way = new Waypoint(x, y);
				add(way);
				nodes.add(way);
				break;
			default:
				break;
			}
			
		}
	}
	
	private Node getNodeAt(int x, int y) {
		for(Node n : nodes) {
			if(n.contains(x, y)) {
				return n;
			}
		}
		return null;
	}
	
	protected boolean revertColors() {
		if(colored) {
			for(Node n : nodes) {
				n.setColor(n.getDefaultColor());
			}
			for(Edge ed : edges) {
				ed.setColor(Color.BLACK);
			}
			colored = false;
			return true;
		}
		return false;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		revertColors();
		
		if(editLabel == null) return;
		
		int x = e.getX();
		int y = e.getY();
		
		startNode = getNodeAt(x, y);
		if(startNode != null) {
			int r = startNode.getRadius();
			double nX = startNode.getX() + r;
			double nY = startNode.getY() + r;
			currentLine = new GLine(nX, nY, nX, nY);
			add(currentLine);
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(editLabel == null || currentLine == null) return;
		
		currentLine.setEndPoint(e.getX(), e.getY());
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(startNode == null) return;
		Node endNode = getNodeAt(e.getX(), e.getY());
		if(endNode == null) {
			if(currentLine != null) remove(currentLine);
		} else {
			if(currentLine != null) remove(currentLine);
			Edge edge = new Edge(startNode, endNode);
			add(edge);
			edges.add(edge);
		}
		currentLine = null;
		startNode = null;
	}
}
