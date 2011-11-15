package de.tdreher.searchstrategies.problems;

import java.util.LinkedList;

import de.tdreher.searchstrategies.states.State;

public interface Problem {

	/**
	 * Get the start state of the problem
	 * @return start state
	 */
	public State getStartState();
	
	/**
	 * Get one or more target states of the problem
	 * @return target states in a LinkedList
	 */
	public LinkedList<State> getTargetStates();
	
	/**
	 * Get all possible actions of this problem (e.g. "go up", "go down", ...)
	 * @return A list of strings with all possible actions
	 */
	public LinkedList<String> getActions();
}
