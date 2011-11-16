package de.tdreher.searchstrategies.strategies;

import java.util.LinkedList;

import de.tdreher.searchstrategies.heuristics.Heuristic;
import de.tdreher.searchstrategies.states.State;

public class IterativeDepthTreeSearch implements Strategy {
	
	private LinkedList<Node> stack = new LinkedList<Node>(); 
	private LinkedList<State> targetStates = null;
	private LinkedList<String> actions = null;
	private int limit = 0;

	@Override
	public void setStartState(State s) {
		this.stack.add(new Node(s,null,"",0,0,1));
	}

	@Override
	public void setTargetStates(LinkedList<State> s) {
		this.targetStates = s;
	}
	
	@Override
	public void setActions(LinkedList<String> a) {
		this.actions = a;
		
	}

	@Override
	public void setLimit(int limit) {
		// this strategy has no depth-limit
	}
	
	@Override
	public void setHeuristic(Heuristic heuristic) {
		// Not needed		
	}

	@Override
	public String run() {
		if(stack.isEmpty() || targetStates == null) {
			throw new IllegalArgumentException("No start and target states defined");
		}
		
		// save the root node
		Node root = stack.getFirst();
		
		// search
		while(true) { // endless till a solution is found
			while(!stack.isEmpty()) {
				Node n = stack.getFirst(); // get first element of the queue
				stack.removeFirst(); // remove this first element of the queue
				for(State targetState : targetStates) {
					if(n.isEqual(targetState)) {
						// solution found - now go back to concat the path
						return n.concatPath("");
					}
				}
				
				// is the depth limit reached, then don't expand the node
				if(limit >= 0 && limit <= n.getDepth()) {
					continue;
				}
				
				// expand for all possible actions
				for(String action : actions) {
					Node child = n.expand(action);
					if(child != null) {
						stack.addFirst(child);
					}
				}
			}
			// increment the limit and try it with the next depth
			limit++;
			stack.add(root);
		}
	}
}
