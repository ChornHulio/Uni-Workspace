package de.tdreher.searchstrategies.heuristics;

import java.util.LinkedList;

import de.tdreher.searchstrategies.states.NumberPuzzleState;
import de.tdreher.searchstrategies.states.State;

/**
 * Misplaced-Tiles heuristic. The count of all misplaced numbers.
 * @author tdreher
 * @date 2011-11-16
 */
public class NumberPuzzleHeuristicA implements Heuristic {

	@Override
	public int calcHeuristic(State currentState, LinkedList<State> targetStates) {
		int h = Integer.MAX_VALUE; // heuristic
		// interpret currect state
		NumberPuzzleState cState = (NumberPuzzleState) currentState;		
		// go for all target states
		for(State tStateObject : targetStates) {
			// interpret target state
			NumberPuzzleState tState = (NumberPuzzleState) tStateObject;
			// count the misplaced numbers
			int[] cField = cState.getField();
			int[] tField = tState.getField();
			if(cField.length != cField.length) {
				throw new RuntimeException("invalid fields");
			}
			int tmpH = 0;
			for(int i = 0; i < cField.length && i < tField.length; i++) {
				if(cField[i] != tField[i]) {
					tmpH++;
				}
			}
			// if its lower than before, replace the old
			if(tmpH < h) {
				h = tmpH;
			}
			// test
			/*System.out.println("A: ");
			for(int i = 0; i < cField.length; i++) {
				System.out.print(cField[i] + " ");
			}
			System.out.println("--> " + h);*/
		}
		return h;
	}

}
