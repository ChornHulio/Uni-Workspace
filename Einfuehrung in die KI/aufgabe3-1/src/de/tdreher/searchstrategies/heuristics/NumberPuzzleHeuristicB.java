package de.tdreher.searchstrategies.heuristics;

import java.util.LinkedList;

import de.tdreher.searchstrategies.states.NumberPuzzleState;
import de.tdreher.searchstrategies.states.State;

/**
 * Manhatten-Distance heuristic. Total sum of all distances from the number to 
 * its target position.
 * @author tdreher
 * @date 2011-11-16
 */
public class NumberPuzzleHeuristicB implements Heuristic {

	@Override
	public int calcHeuristic(State currentState, LinkedList<State> targetStates) {
		int h = Integer.MAX_VALUE; // heuristic
		// interpret currect state
		NumberPuzzleState cState = (NumberPuzzleState) currentState;		
		// go for all target states
		for(State tStateObject : targetStates) {
			// interpret target state
			NumberPuzzleState tState = (NumberPuzzleState) tStateObject;
			
			int[] cField = cState.getField();
			int[] tField = tState.getField();
			if(cField.length != cField.length) {
				throw new RuntimeException("invalid fields");
			}
			
			/* Calculate the heuristic after this formula:
			 * h(n) = | n%l - a%l | + | n/l - a/l | 	, for a == target cell 
			 * 											  and n € [0..8]
			 * 											  l == length of the edge
			 */
			int tmpH = 0;
			int l = (int) Math.sqrt(cField.length);
			for(int n = 0; n < cField.length; n++) {
				int nMod = n % l;
				int nDiv = n / l;
				for(int a = 0; a < tField.length; a++) {
					if(cField[n] == tField[a]) {
						int aMod = a % l;
						int aDiv = a / l;
						tmpH += Math.abs(nMod-aMod) + Math.abs(nDiv-aDiv);
					}
					
				}
			}
			// if its lower than before, replace the old
			if(tmpH < h) {
				h = tmpH;
			}
		}
		return h;
	}

}
