package de.tdreher.searchstrategies.strategies;

import java.util.LinkedList;

import de.tdreher.searchstrategies.states.State;

public class BreadthFirstTreeSearch implements Strategy {
	
	private LinkedList<Node> queue = new LinkedList<Node>(); 
	LinkedList<State> targetStates = null;
	LinkedList<String> actions = null;

	@Override
	public void setStartState(State s) {
		this.queue.add(new Node(s,null,"",0,0));
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
		return; // this search strategy doesn't use a limit
	}

	@Override
	public String run() {
		if(queue.isEmpty() || targetStates == null) {
			throw new IllegalArgumentException("No start and target states defined");
		}
		
		// search
		while(!queue.isEmpty()) {
			Node n = queue.getFirst(); // get first element of the queue
			queue.removeFirst(); // remove this first element of the queue
			for(State targetState : targetStates) {
				if(n.isEqual(targetState)) {
					return "juhuu"; // TODO solution found - concat a string
				}
			}
			// expand for all possible actions
			for(String action : actions) {
				Node child = n.expand(action);
				if(child != null) {
					queue.add(child);
				}
			}
			System.out.println(queue.size());
		}
		return "No solution found";
	}
}
