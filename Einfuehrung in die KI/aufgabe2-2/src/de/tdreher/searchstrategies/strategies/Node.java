package de.tdreher.searchstrategies.strategies;

import java.util.LinkedList;

import de.tdreher.searchstrategies.states.State;

/**
 * The class to safe a node of a search tree
 * @author tdreher
 * @date 2011-11-01
 */
public class Node {

	/** 
	 * One state from the hole state space
	 */
	private State state = null;
	
	/**
	 * The parent node of this node
	 */
	private Node parentNode = null;
	
	/**
	 * The action which causes this node
	 */
	private String action = "";
	
	/**
	 * The depth in the search tree
	 */
	private int depth = 0;
	
	/**
	 * The path cost which causes the action of this node
	 */
	private int pathCost = 0;
	
	/**
	 * All children of this node. This list only get filled when the method
	 * expand() is used.
	 */
	private LinkedList<Node> childrenNodes = new LinkedList<Node>();
	
	/**
	 * Constructor of this class
	 * @param s The state of this node
	 * @param parent The parent node of this node
	 * @param action The action which causes this node
	 * @param depth The depth in the search tree
	 * @param cost The path cost which causes the action of this node
	 */
	public Node(State s, Node parent, String action, int depth, int cost) {
		this.state = s;
		this.parentNode = parent;
		this.action = action;
		this.depth = depth;
		this.pathCost = cost;
	}

	/**
	 * Create and return children nodes for all possible actions
	 * @param actions All possible actions in a linked list
	 * @return children of this node
	 */
	LinkedList<Node> expand(LinkedList<String> actions) {
		if(state != null) {
			for(String action : actions) {
				State newState = state.expand(action);
				Node child = new Node(newState,this,action,depth+1,pathCost);
				childrenNodes.add(child);
			}
		}
		return childrenNodes;
	}
}
