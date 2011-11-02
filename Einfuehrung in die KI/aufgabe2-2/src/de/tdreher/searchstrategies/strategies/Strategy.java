package de.tdreher.searchstrategies.strategies;

import java.util.LinkedList;

import de.tdreher.searchstrategies.states.State;

/**
 * Interface for every strategy
 * @author tdreher
 * @date 2011-11-02
 */
public interface Strategy {

	/**
	 * Set a start state
	 * @param s The start state
	 */
	public void setStartState(State s);
	
	/**
	 * Set one or more target states
	 * @param s The target states in a LinkedList
	 */
	public void setTargetStates(LinkedList<State> s);
	
	/**
	 * Set all possible actions
	 * @param a A list of strings with all possible actions
	 */
	public void setActions(LinkedList<String> a);
	
	/**
	 * If the strategy uses a limit, you can set it here. E.g., the depth-limited-
	 * search uses one.
	 * @param limit A limit
	 */
	public void setLimit(int limit);
	
	/**
	 * Run the search strategy
	 * @return A string, which describes the path of the solution
	 */
	public String run();
}
