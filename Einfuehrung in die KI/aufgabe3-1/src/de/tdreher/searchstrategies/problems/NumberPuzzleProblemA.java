package de.tdreher.searchstrategies.problems;

import java.util.LinkedList;

import de.tdreher.searchstrategies.states.NumberPuzzleState;
import de.tdreher.searchstrategies.states.SokobanState;
import de.tdreher.searchstrategies.states.State;

public class NumberPuzzleProblemA implements Problem {

	@Override
	public State getStartState() {
		int[] field = {3,1,2,4,7,5,NumberPuzzleState.FREE,6,8 };
		return new NumberPuzzleState(field);
	}

	@Override
	public LinkedList<State> getTargetStates() {
		int[] field = {	NumberPuzzleState.FREE,1,2,3,4,5,6,7,8 };
		LinkedList<State> targetStates = new LinkedList<State>();
		targetStates.add(new NumberPuzzleState(field));
		return targetStates;
	}

	@Override
	public LinkedList<String> getActions() {
		// action means: move the free field up, down, right or left
		LinkedList<String> actions = new LinkedList<String>();
		actions.add("up");
		actions.add("right");
		actions.add("down");
		actions.add("left");
		return actions;
	}

}
