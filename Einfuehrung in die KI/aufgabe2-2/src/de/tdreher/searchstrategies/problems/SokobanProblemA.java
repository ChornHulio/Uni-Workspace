package de.tdreher.searchstrategies.problems;

import java.util.LinkedList;

import de.tdreher.searchstrategies.states.State;
import de.tdreher.searchstrategies.states.SokobanState;

public class SokobanProblemA implements Problem {

	@Override
	public State getStartState() {
		int[][] field = {	{ SokobanState.WALL, SokobanState.WALL, SokobanState.WALL, SokobanState.WALL, SokobanState.WALL },
							{ SokobanState.WALL, SokobanState.FREE_FIELD, SokobanState.FIGUR, SokobanState.FREE_FIELD, SokobanState.WALL },
							{ SokobanState.WALL, SokobanState.FREE_FIELD, SokobanState.BOX, SokobanState.FREE_FIELD, SokobanState.WALL },
							{ SokobanState.WALL, SokobanState.FREE_FIELD, SokobanState.WALL, SokobanState.FREE_FIELD, SokobanState.WALL },
							{ SokobanState.WALL, SokobanState.FREE_FIELD, SokobanState.FREE_FIELD, SokobanState.TARGET_FIELD, SokobanState.WALL },
							{ SokobanState.WALL, SokobanState.WALL, SokobanState.WALL, SokobanState.WALL, SokobanState.WALL }};
		return new SokobanState(field);
	}

	@Override
	public LinkedList<State> getTargetStates() {
		int[][] field = {	{ SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE },
							{ SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE },
							{ SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE },
							{ SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE },
							{ SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.BOX_ON_TARGET, SokobanState.DONT_CARE },
							{ SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE, SokobanState.DONT_CARE }};
		LinkedList<State> targetStates = new LinkedList<State>();
		targetStates.add(new SokobanState(field));
		return targetStates;
	}

	@Override
	public LinkedList<String> getActions() {
		LinkedList<String> actions = new LinkedList<String>();
		actions.add("up");
		actions.add("right");
		actions.add("down");
		actions.add("left");
		return actions;
	}
}
