package com.gregorycarlin.pathfinder.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gregorycarlin.pathfinder.Edge;
import com.gregorycarlin.pathfinder.nodes.Destination;
import com.gregorycarlin.pathfinder.nodes.Node;
import com.gregorycarlin.pathfinder.nodes.Waypoint;

public class IO {
	private static final String NODE_LOC = "nodes.txt";
	private static final String EDGE_LOC = "edges.txt";
	private static final String SEP = ":";
	
	public static void save(List<Node> nodes) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(NODE_LOC));
			BufferedWriter edges = new BufferedWriter(new FileWriter(EDGE_LOC));
			List<Edge> written = new ArrayList<Edge>();
			for(Node n : nodes) {
				bw.write(n.getX() + SEP + n.getY() + SEP + n.getRadius());
				bw.newLine();
				for(Edge e : n.getEdges()) {
					if(!written.contains(e)) {
						Node start = e.getNode1();
						Node end = e.getNode2();
						edges.write(start.getX() + SEP + start.getY() + SEP + end.getX() + SEP + end.getY());
						edges.newLine();
						written.add(e);
					}
				}
			}
			bw.close();
			edges.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Node> load() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(NODE_LOC));
			String line;
			List<Node> nodes = new ArrayList<Node>();
			while((line = br.readLine()) != null) {
				line = line.trim();
				String[] split = line.split(SEP);
				if(split.length != 3) continue;
				double x = Double.parseDouble(split[0]);
				double y = Double.parseDouble(split[1]);
				int r = Integer.parseInt(split[2]);
				if(r == 5) {
					nodes.add(new Destination(x, y));
				} else if(r == 3) {
					nodes.add(new Waypoint(x, y));
				}
			}
			br.close();
			
			br = new BufferedReader(new FileReader(EDGE_LOC));
			while((line = br.readLine()) != null) {
				line = line.trim();
				String[] split = line.split(SEP);
				if(split.length != 4) continue;
				double sX = Double.parseDouble(split[0]);
				double sY = Double.parseDouble(split[1]);
				double eX = Double.parseDouble(split[2]);
				double eY = Double.parseDouble(split[3]);
				Node s = null;
				Node e = null;
				for(Node n : nodes) {
					if(s == null && n.contains(sX, sY)) s = n;
					if(e == null && n.contains(eX, eY)) e = n;
					if(s != null && e != null) break;
				}
				if(s == null || e == null) continue;
				new Edge(s, e);
			}
			
			return nodes;
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
}
