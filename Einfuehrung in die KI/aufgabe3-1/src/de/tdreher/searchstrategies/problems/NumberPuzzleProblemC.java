package de.tdreher.searchstrategies.problems;

import java.util.LinkedList;

import de.tdreher.searchstrategies.states.NumberPuzzleState;
import de.tdreher.searchstrategies.states.State;

public class NumberPuzzleProblemC implements Problem {

	@Override
	public State getStartState() {
		int[] field = {7,4,NumberPuzzleState.FREE,6,3,1,5,2,8 };
		return new NumberPuzzleState(field);
	}

	@Override
	public LinkedList<State> getTargetStates() {
		int[] field = {	1,2,3,4,5,6,7,8,NumberPuzzleState.FREE };
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
