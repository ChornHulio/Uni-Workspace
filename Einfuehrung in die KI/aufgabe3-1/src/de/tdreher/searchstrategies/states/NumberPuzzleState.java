package de.tdreher.searchstrategies.states;

import java.util.Arrays;

/**
 * A state of the Number Puzzle. It represents a 2D-field with numbers from 1 
 * to 9 and one free cell. This 2D-field is intern represented as a 1D-field. 
 * 
 * @author tdreher
 * @date 2011-11-15
 */
public class NumberPuzzleState implements State {
	
	public final static int FREE = 0;
	
	/**
	 * 2D-field with numbers from 1 to 9 and one free cell 
	 * 
	 *  [A] [B] [C]
	 *  [D] [E] [F]      --> [A] [B] [C] [D] [E] [F] [G] [H] [I]
	 *  [G] [H] [I]
	 *  
	 *  Target:
	 *  [ ] [1] [2]
	 *  [3] [4] [5]      --> [FREE] [1] [2] [3] [4] [5] [6] [7] [8] [9]
	 *  [6] [8] [9]
	 *   
	 */
	private int[] field = null;

	/**
	 * Length of the 2D-Field 
	 */
	private int length = 0;
	
	/**
	 * Constructor of this class
	 * @param field The current field of the puzzle
	 */
	public NumberPuzzleState(int[] field) {
		// don't use the given field but copy it
		this.field = field.clone();
		this.length = (int) Math.sqrt(field.length);
	}


	@Override
	public boolean isEqual(Object state) {
		NumberPuzzleState sState = null;
		try {
			sState = (NumberPuzzleState) state;
		} catch(Exception e) {
			return false; // The given state is not even a SokobanState
		}

		// compare fields
		int sField[] = sState.field;
		return Arrays.equals(field,sField);
	}

	@Override
	public State expand(String action) {
		// position of free cell
		int x = -1;
		for(int i = 0; i < field.length; i++) {
			if(field[i] == FREE) {
				x = i;
				break; // break for-loop
			}
		}
		if(x < 0) {
			throw new RuntimeException("No free cell is found in the hole field");
		}
		
		// create new state
		NumberPuzzleState newState = new NumberPuzzleState(field);
		
		// interpret action (action means: move the free field up, down, right or left)
		if(action.equals("up") && x >= length) {
			newState.swapCells(x, x-length);
			return newState;
		} else if(action.equals("right") && ((x+1)%length != 0)) {	
			newState.swapCells(x, x+1);
			return newState;
		} else if(action.equals("down") && (x < (length*(length-1)))) {	
			newState.swapCells(x, x+length);
			return newState;
		} else if(action.equals("left") && ((x%length) != 0)) {
			newState.swapCells(x, x-1);
			return newState;
		}
		return null; // This action is not possible
	}

	@Override
	public void printState() {
		for(int i = 0; i < field.length; i+=length) {
			for(int j = 0; j < length; j++) {
				System.out.print(field[i]+"\t");
			}
			System.out.println();
		}

	}
	
	/**
	 * Swap the values of two cells
	 * @param x1 Cell to swap with x2
	 * @param x2 Cell to swap with x1
	 */
	public void swapCells(int x1, int x2) {
		int tmp = field[x1];
		field[x1] = field[x2];
		field[x2] = tmp;
	}
	
	public int[] getField() {
		return field;
	}
}
