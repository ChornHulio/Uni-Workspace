package de.tdreher.searchstrategies.strategies;

import java.util.LinkedList;

import de.tdreher.searchstrategies.heuristics.Heuristic;
import de.tdreher.searchstrategies.states.State;

public class AStarTreeSearch implements Strategy {
	
	private LinkedList<Node> queue = new LinkedList<Node>(); 
	private LinkedList<State> targetStates = null;
	private LinkedList<String> actions = null;
	private int limit = -1; // on default this strategy has no depth-limit
	private Heuristic heuristic = null;

	@Override
	public void setStartState(State s) {
		this.queue.add(new Node(s,null,"",0,0,1));
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
		// on default this strategy has no depth-limit - but if you want, you get one
		this.limit = limit;
	}
	
	@Override
	public void setHeuristic(Heuristic heuristic) {
		this.heuristic = heuristic;
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
					// check heuristic
					int newH = heuristic.calcHeuristic(child.getState(), targetStates);
					int newF = newH + child.getCosts();
					for(int i = 0; i <= queue.size(); i++) {
						// if it's the worst heuristic value
						if(i == queue.size()) {
							queue.addLast(child);
							break;
						}
						int iH = heuristic.calcHeuristic(queue.get(i).getState(), targetStates);
						int iF = iH + queue.get(i).getCosts();
						if(newF <= iF) {
							queue.add(i, child); // add the new node at this place
							break;
						}
					}
				}
			}
			
			// test
			/*if(queue.size() < 20) {
				for(int i = 0; i < queue.size(); i++) {
					
					System.out.print(heuristic.calcHeuristic(queue.get(i).getState(), targetStates) + "." + queue.get(i).getCosts() + " - ");
				}
				System.out.println(" ");
			} else {
				throw new RuntimeException("Guten Tag");
			}*/
		}
		return "No solution found";
	}
}

