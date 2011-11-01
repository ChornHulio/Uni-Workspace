package de.tdreher.searchstrategies.states;

/**
 * Interface for different states in the state space
 * @author tdreher
 * @date 2011-11-01
 */
public interface State {

	/**
	 * Check if this state is equal to another state
	 * @param s The other state to check
	 * @return true or false
	 */
	boolean isEqual(Object state);
	
	/**
	 * Expand the given node for one iteration with one action
	 * @param action A string to show which action is used
	 * @return the subsequent state for the given action
	 */
	State expand(String action);
}
