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
	 *  +-----------------> x
	 *  |[0/0] [1/0] [2/0]
	 *  |[0/1] [1/1] [2/1]      --> field[x][y]
	 *  |[0/2] [1/2] [2/2]
	 *  V
	 *  y 
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
		int[][] sField = sState.getField();
		
		// compare lengths
		if(field.length != sField.length || field[0].length != sField[0].length) {
			return false; // Not even the same lengths
		}
		
		// compare cell for cell
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				try {
					if(field[i][j] != sField[i][j]) {
						if(field[i][j] != DONT_CARE || sField[i][j] != DONT_CARE) {
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

	private int[][] getField() {
		return field;
	}

	@Override
	public State expand(String action) {
		if(action.equals("up")) {
			return actionUp();			
		} else if(action.equals("right")) {
			return actionRight();	
		} else if(action.equals("down")) {
			return actionDown();
		} else if(action.equals("left")) {
			return actionLeft();
		} else {
			return null;
		}
	}
	
	/**
	 * Check if the figur can go one step upward
	 * @return The new state (copied and changed this object)
	 */
	private State actionUp() {
		// init a new state with the field of this state
		SokobanState newState = new SokobanState(field);
		
		// position of figur
		int[] figur = givePositionOfFigur();
		int x = figur[0];
		int y = figur[1];
		
		// if there is enough space for one step up
		if( y >= 1 ) { 
			// if there is a free field or a target field -> move up
			if(field[x][y-1] == FREE_FIELD || field[x][y-1] == TARGET_FIELD) {
				// set new value for [x][y]
				if(field[x][y] == FIGUR_ON_TARGET_FIELD) {
					newState.changeField(x, y, TARGET_FIELD);
				} else {
					newState.changeField(x, y, FREE_FIELD);
				}
				// set new value for [x][y-1]
				if(field[x][y-1] == TARGET_FIELD) {
					newState.changeField(x, y-1, FIGUR_ON_TARGET_FIELD);
				} else {
					newState.changeField(x, y-1, FIGUR);
				}
				return newState;
			}
		} 
		
		// if there is enough space for two steps up
		if( y >= 2 ) { 
			// if there is a box
			if(field[x][y-1] == BOX || field[x][y-1] == BOX_ON_TARGET) {
				// if there is a free field or target field -> push it and move up
				if(field[x][y-2] == FREE_FIELD || field[x][y-2] == TARGET_FIELD) {
					// set new value for [x][y]
					if(field[x][y] == FIGUR_ON_TARGET_FIELD) {
						newState.changeField(x, y, TARGET_FIELD);
					} else {
						newState.changeField(x, y, FREE_FIELD);
					}
					// set new value for [x][y-1]
					if(field[x][y-1] == BOX_ON_TARGET) {
						newState.changeField(x, y-1, FIGUR_ON_TARGET_FIELD);
					} else {
						newState.changeField(x, y-1, FIGUR);
					}
					// set new value for [x][y-2]
					if(field[x][y-2] == TARGET_FIELD) {
						newState.changeField(x, y-2, BOX_ON_TARGET);
					} else {
						newState.changeField(x, y-2, BOX);
					}
				}
			}
		} 
		return null; // There is no step up possible (e.g. in cause of a wall)
	}
	
	private State actionRight() {
		// init a new state with the field of this state
		SokobanState newState = new SokobanState(field);
		
		// position of figur
		int[] figur = givePositionOfFigur();
		int x = figur[0];
		int y = figur[1];
		
		// if there is enough space for one step up
		if( x >= field.length-1 ) { 
			// if there is a free field or a target field -> move up
			if(field[x+1][y] == FREE_FIELD || field[x+1][y] == TARGET_FIELD) {
				// set new value for [x][y]
				if(field[x][y] == FIGUR_ON_TARGET_FIELD) {
					newState.changeField(x, y, TARGET_FIELD);
				} else {
					newState.changeField(x, y, FREE_FIELD);
				}
				// set new value for [x+1][y]
				if(field[x+1][y] == TARGET_FIELD) {
					newState.changeField(x+1, y, FIGUR_ON_TARGET_FIELD);
				} else {
					newState.changeField(x+1, y, FIGUR);
				}
				return newState;
			}
		} 
		
		// if there is enough space for two steps up
		if( x >= field.length-2 ) { 
			// if there is a box
			if(field[x+1][y] == BOX || field[x+1][y] == BOX_ON_TARGET) {
				// if there is a free field or target field -> push it and move up
				if(field[x+2][y] == FREE_FIELD || field[x+2][y] == TARGET_FIELD) {
					// set new value for [x][y]
					if(field[x][y] == FIGUR_ON_TARGET_FIELD) {
						newState.changeField(x, y, TARGET_FIELD);
					} else {
						newState.changeField(x, y, FREE_FIELD);
					}
					// set new value for [x+1][y]
					if(field[x+1][y] == BOX_ON_TARGET) {
						newState.changeField(x+1, y, FIGUR_ON_TARGET_FIELD);
					} else {
						newState.changeField(x+1, y, FIGUR);
					}
					// set new value for [x+2][y]
					if(field[x+2][y] == TARGET_FIELD) {
						newState.changeField(x+2, y, BOX_ON_TARGET);
					} else {
						newState.changeField(x+2, y, BOX);
					}
				}
			}
		} 		
		return null; // There is no step up possible (e.g. in cause of a wall)
	}
	
	private State actionDown() {
		// init a new state with the field of this state
		SokobanState newState = new SokobanState(field);
		
		// position of figur
		int[] figur = givePositionOfFigur();
		int x = figur[0];
		int y = figur[1];
		
		// if there is enough space for one step up
		if( y < field[0].length-1 ) { 
			// if there is a free field or a target field -> move up
			if(field[x][y+1] == FREE_FIELD || field[x][y+1] == TARGET_FIELD) {
				// set new value for [x][y]
				if(field[x][y] == FIGUR_ON_TARGET_FIELD) {
					newState.changeField(x, y, TARGET_FIELD);
				} else {
					newState.changeField(x, y, FREE_FIELD);
				}
				// set new value for [x][y+1]
				if(field[x][y+1] == TARGET_FIELD) {
					newState.changeField(x, y+1, FIGUR_ON_TARGET_FIELD);
				} else {
					newState.changeField(x, y+1, FIGUR);
				}
				return newState;
			}
		} 
		
		// if there is enough space for two steps up
		if( y < field[0].length-2 ) { 
			// if there is a box
			if(field[x][y+1] == BOX || field[x][y+1] == BOX_ON_TARGET) {
				// if there is a free field or target field -> push it and move up
				if(field[x][y+2] == FREE_FIELD || field[x][y+2] == TARGET_FIELD) {
					// set new value for [x][y]
					if(field[x][y] == FIGUR_ON_TARGET_FIELD) {
						newState.changeField(x, y, TARGET_FIELD);
					} else {
						newState.changeField(x, y, FREE_FIELD);
					}
					// set new value for [x][y+1]
					if(field[x][y+1] == BOX_ON_TARGET) {
						newState.changeField(x, y+1, FIGUR_ON_TARGET_FIELD);
					} else {
						newState.changeField(x, y+1, FIGUR);
					}
					// set new value for [x][y+2]
					if(field[x][y+2] == TARGET_FIELD) {
						newState.changeField(x, y+2, BOX_ON_TARGET);
					} else {
						newState.changeField(x, y+2, BOX);
					}
				}
			}
		} 		
		return null; // There is no step up possible (e.g. in cause of a wall)
	}
	
	private State actionLeft() {
		// init a new state with the field of this state
		SokobanState newState = new SokobanState(field);
		
		// position of figur
		int[] figur = givePositionOfFigur();
		int x = figur[0];
		int y = figur[1];
		
		// if there is enough space for one step up
		if( x >= 1 ) { 
			// if there is a free field or a target field -> move up
			if(field[x-1][y] == FREE_FIELD || field[x-1][y] == TARGET_FIELD) {
				// set new value for [x][y]
				if(field[x][y] == FIGUR_ON_TARGET_FIELD) {
					newState.changeField(x, y, TARGET_FIELD);
				} else {
					newState.changeField(x, y, FREE_FIELD);
				}
				// set new value for [x-1][y]
				if(field[x-1][y] == TARGET_FIELD) {
					newState.changeField(x-1, y, FIGUR_ON_TARGET_FIELD);
				} else {
					newState.changeField(x-1, y, FIGUR);
				}
				return newState;
			}
		} 
		
		// if there is enough space for two steps up
		if( x >= 2 ) { 
			// if there is a box
			if(field[x-1][y] == BOX || field[x-1][y] == BOX_ON_TARGET) {
				// if there is a free field or target field -> push it and move up
				if(field[x-2][y] == FREE_FIELD || field[x-2][y] == TARGET_FIELD) {
					// set new value for [x][y]
					if(field[x][y] == FIGUR_ON_TARGET_FIELD) {
						newState.changeField(x, y, TARGET_FIELD);
					} else {
						newState.changeField(x, y, FREE_FIELD);
					}
					// set new value for [x-1][y]
					if(field[x-1][y] == BOX_ON_TARGET) {
						newState.changeField(x-1, y, FIGUR_ON_TARGET_FIELD);
					} else {
						newState.changeField(x-1, y, FIGUR);
					}
					// set new value for [x-2][y]
					if(field[x-2][y] == TARGET_FIELD) {
						newState.changeField(x-2, y, BOX_ON_TARGET);
					} else {
						newState.changeField(x-2, y, BOX);
					}
				}
			}
		} 		
		return null; // There is no step up possible (e.g. in cause of a wall)
	}
	
	/**
	 * Change value of one field
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param fieldState the new state of the field
	 */
	public void changeField(int x, int y, int fieldState) {
		field[x][y] = fieldState;
	}
	
	private int[] givePositionOfFigur() {
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				if(field[i][j] == FIGUR || field[i][j] == FIGUR_ON_TARGET_FIELD) {
					int[] position = {i, j};
					return position;
				}
			}
		}
		throw new ExceptionSokobanNoFigur("No figur is found in the hole field");
	}
}
