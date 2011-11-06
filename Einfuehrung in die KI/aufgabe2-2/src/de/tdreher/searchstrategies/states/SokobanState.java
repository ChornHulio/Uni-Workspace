package de.tdreher.searchstrategies.states;

import de.tdreher.searchstrategies.exceptions.ExceptionSokobanNoFigur;

/**
 * A state of the Sokoban Game. It represents a 2D-field with walls, the figur,
 * boxes, free fields and target fields
 * @author tdreher
 * @date 2011-11-01
 */
public class SokobanState implements State {
	
	public final static int FREE_FIELD = 0;
	public final static int FIGUR = 1;
	public final static int BOX = 2;
	public final static int TARGET_FIELD = 3;
	public final static int FIGUR_ON_TARGET_FIELD = 4;
	public final static int BOX_ON_TARGET = 5;
	public final static int WALL = 6; // only used for graphical presentation
	public final static int DONT_CARE = 9; // used for target state
	
	/**
	 * 2D-field with walls, the figur, boxes, free fields and target fields 
	 * 
	 *  +-----------------> y
	 *  |[0/0] [1/0] [2/0]
	 *  |[0/1] [1/1] [2/1]      --> field[y][x]
	 *  |[0/2] [1/2] [2/2]
	 *  V
	 *  x 
	 */
	private int[][] field = null;
	
	/**
	 * Constructor of this class
	 * @param field The current field of the Sokoban Game
	 */
	public SokobanState(int[][] field) {
		// don't use the given field but copy it
		this.field = new int[field.length][field[0].length];
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				this.field[i][j] = field[i][j];
			}
		}
	}

	@Override
	public boolean isEqual(Object state) {
		SokobanState sState = null;
		try {
			sState = (SokobanState) state;
		} catch(Exception e) {
			return false; // The given state is not even a SokobanState
		}
		
		// get field
		int[][] sField = sState.field;
		
		// compare lengths
		if(field.length != sField.length || field[0].length != sField[0].length) {
			return false; // Not even the same lengths
		}
		
		// compare cell for cell
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				try {
					if(field[i][j] != sField[i][j]) {
						if(field[i][j] != DONT_CARE && sField[i][j] != DONT_CARE) {
							return false;
						}
					}
				} catch(IndexOutOfBoundsException e) {
					return false; // sField has not everytime the same length as field
				}
			}
		}
		
		// ok, the fields are equal
		return true;
		
	}

	@Override
	public State expand(String action) {
		// position of figur
		int x = -1;
		int y = -1;
		for(int j = 0; j < field.length; j++) {
			for(int i = 0; i < field[j].length; i++) {
				if(field[j][i] == FIGUR || field[j][i] == FIGUR_ON_TARGET_FIELD) {
					x = i;
					y = j;
					// break for-loops
					j = field.length;
					break;
				}
			}
		}
		if(x < 0 || y < 0) {
			throw new ExceptionSokobanNoFigur("No figur is found in the hole field");
		}
		
		// interpret action
		if(action.equals("up")) {
			return doAction(x, y, x, y-1, x, y-2);			
		} else if(action.equals("right")) {
			return doAction(x, y, x+1, y, x+2, y);	
		} else if(action.equals("down")) {
			return doAction(x, y, x, y+1, x, y+2);	
		} else if(action.equals("left")) {
			return doAction(x, y, x-1, y, x-2, y);	
		} else {
			return null;
		}
	}
	
	/**
	 * Check if the figur can go one step 
	 * @param x The x-coordinate of the figur
	 * @param y The y-coordinate of the figur
	 * @param x1 First step towards the direction of the action (x-coordinate)
	 * @param y1 First step towards the direction of the action (y-coordinate)
	 * @param x2 Second step towards the direction of the action (x-coordinate)
	 * @param y2 Second step towards the direction of the action (y-coordinate)
	 * @return The new state (copied and changed this object)
	 */
	private State doAction(int x, int y, int x1, int y1, int x2, int y2) {
		// init a new state with the field of this state
		SokobanState newState = new SokobanState(field);
		
		// if there is enough space for one step
		if( x1 >= 0 && x1 < field[0].length && y1 >= 0 && y1 < field.length ) { 
			// if there is a free field or a target field -> move
			if(field[y1][x1] == FREE_FIELD || field[y1][x1] == TARGET_FIELD) {
				// set new value for [y][x]
				if(field[y][x] == FIGUR_ON_TARGET_FIELD) {
					newState.changeField(x, y, TARGET_FIELD);
				} else {
					newState.changeField(x, y, FREE_FIELD);
				}
				// set new value for [y1][x1]
				if(field[y1][x1] == TARGET_FIELD) {
					newState.changeField(x1, y1, FIGUR_ON_TARGET_FIELD);
				} else {
					newState.changeField(x1, y1, FIGUR);
				}
				return newState;
			}
		} 
		
		// if there is enough space for two steps
		if( x2 >= 0 && x2 < field[0].length && y2 >= 0 && y2 < field.length ) { 
			// if there is a box
			if(field[y1][x1] == BOX || field[y1][x1] == BOX_ON_TARGET) {
				// if there is a free field or target field -> push it and move
				if(field[y2][x2] == FREE_FIELD || field[y2][x2] == TARGET_FIELD) {
					// set new value for [y][x]
					if(field[y][x] == FIGUR_ON_TARGET_FIELD) {
						newState.changeField(x, y, TARGET_FIELD);
					} else {
						newState.changeField(x, y, FREE_FIELD);
					}
					// set new value for [x1][y1]
					if(field[y1][x1] == BOX_ON_TARGET) {
						newState.changeField(x1, y1, FIGUR_ON_TARGET_FIELD);
					} else {
						newState.changeField(x1, y1, FIGUR);
					}
					// set new value for [y2][x2]
					if(field[y2][x2] == TARGET_FIELD) {
						newState.changeField(x2, y2, BOX_ON_TARGET);
					} else {
						newState.changeField(x2, y2, BOX);
					}
					return newState;
				}
			}
		} 
		return null; // There is no step possible (e.g. in cause of a wall)
	}
	
	/**
	 * Change value of one field
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param fieldState the new state of the field
	 */
	public void changeField(int x, int y, int fieldState) {
		field[y][x] = fieldState;
	}
	
	@Override
	public void printState() {
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				System.out.print(field[i][j]);
			}
			System.out.println();
		}
	}
}
