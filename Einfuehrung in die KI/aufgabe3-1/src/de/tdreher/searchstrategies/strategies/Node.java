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
	 * The path cost which causes the action of this node from the parent node 
	 * to this node
	 */
	private int pathCost = 0;
	
	/**
	 * All children of this node. This list only get filled when the method
	 * expand() is used.
	 */
	private LinkedList<Node> childrenNodes = new LinkedList<Node>();
	
	/**
	 * Node ID - this id is only unique in combinition
	 * with the depth of a node
	 * 
	 *            (depth / id)
	 *                  0/1
	 *         1/1              1/2
	 *     2/1     2/2      2/3     2/4
	 * 
	 */
	private int nodeID = 0;
	
	/**
	 * Constructor of this class
	 * @param s The state of this node
	 * @param parent The parent node of this node
	 * @param action The action which causes this node
	 * @param depth The depth in the search tree
	 * @param cost The path cost which causes the action of this node
	 * @param nodeID The id of this node
	 */
	public Node(State s, Node parent, String action, int depth, int cost, int nodeID) {
		this.state = s;
		this.parentNode = parent;
		this.action = action;
		this.depth = depth;
		this.pathCost = cost;
		this.nodeID = nodeID;
	}

	/**
	 * Create and return children nodes for one possible actions
	 * @param action One possible actions in a linked list
	 * @return children of this node
	 */
	Node expand(String action) {
		Node child = null;
		if(state != null) {
			State newState = state.expand(action);
			if(newState == null) {
				return null; // action not possible
			}
			// if this is a root change the pathCost for the child
			if(pathCost == 0 && parentNode == null) {
				// create child
				child = new Node(newState,this,action,depth+1,1,childrenNodes.size());
			} else {
				// create child
				child = new Node(newState,this,action,depth+1,pathCost,childrenNodes.size());
			}
			childrenNodes.add(child);
		}
		return child;
	}
	
	/**
	 * Check if the state if this node is equal to another state
	 * @param s The other state to check
	 * @return true or false
	 */
	public boolean isEqual(State s) {
		return state.isEqual(s);
	}
	
	public String getAction() {
		return action;
	}
	
	public void printState() {
		System.out.println("------- " + depth + "/" + nodeID + " -------");
		state.printState();
	}
	
	public int getDepth() {
		return depth;
	}

	/**
	 * Concat the path to the solution (backwards)
	 * @param path The path from the target backwards to this node
	 * @return the complete path (recursive)
	 */
	public String concatPath(String path) {
		if(parentNode != null && path.isEmpty()) {
			return parentNode.concatPath(action);
		} else if(parentNode != null) {
			return parentNode.concatPath(action + " / " + path);
		} else {
			return "Solution: " + path;
		}
	}
	
	public State getState() {
		return state;
	}
	
	/**
	 * Summarize the costs of the path from the root till this node
	 * @return the costs
	 */
	public int getCosts() {
		if(parentNode == null) {
			return pathCost;
		} else {
			return pathCost + parentNode.getCosts();
		}
	}
}
