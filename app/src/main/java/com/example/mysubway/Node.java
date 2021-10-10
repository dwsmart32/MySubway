package com.example.mysubway;

import java.util.LinkedList;

public class Node implements Comparable<Node>{
	String vertex;
	int distance;
	String sameline;
	LinkedList<String> route=new LinkedList<>();;
	
	//constructor
		Node(String vertex, int distance){
			this.vertex=vertex;
			this.distance=distance;
		}
	void setpath(LinkedList<String> l) {
		route=l;
	}
	void setsameline(String s) {
		this.sameline=s;
	}
	String getsameline() {
		return this.sameline;
	}
	LinkedList<String> getroute() {
		return route;
	}
	void addpath(String e) {
		this.route.add(e);
	}
	//compareto 구 g 현
	public int compareTo(Node o) {
		return (int) (distance-o.distance);
	}
}
