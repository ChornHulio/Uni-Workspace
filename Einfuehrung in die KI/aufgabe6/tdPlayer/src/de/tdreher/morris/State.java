package de.tdreher.morris;

import java.io.IOException;

public class State {
	
	// state of one cell
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	public static final int EMPTY = 2;
	
	// state of the game
	public static final int BEGINNING = 0;
	public static final int MOVING = 1;
	public static final int FLYING = 2;
	
	
	private static final int FIELD_LENGTH = 24;
	
	private int whatAmI = WHITE;
	private int[] field = new int[FIELD_LENGTH];
	private int remainingWhite = 0;
	private int remainingBlack = 0;
	private int state = 0;
	
	public State(int whatAmI, int rWhite, int rBlack, int[] field, int state) {
		this.whatAmI = whatAmI;
		this.remainingWhite = rWhite;
		this.remainingBlack = rBlack;
		this.field = field;
		this.state = state;
	}
	
	public static State interpretState(String state, int whatAmI) throws IOException {
		if(state.length() != 28) {
			throw new IOException("State of the game is not valid");
		}
		
		// remaining stones
		int rWhite = (int)state.charAt(25);
		int rBlack = (int)state.charAt(27);
		
		// state of the cells
		int[] field = new int[FIELD_LENGTH];
		for(int i = 0; i < FIELD_LENGTH; i++) {
			switch(state.charAt(i)) {
			case 'W':
				field[i] = WHITE;
				break;
			case 'B':
				field[i] = BLACK;
				break;
			default:
				field[i] = EMPTY;
				break;
			}
		}
		
		// state of the game
		int gState = 0;
		if(rWhite > 0 || rBlack > 0) {
			gState = BEGINNING;
		} else {
			gState = FLYING;
			int stoneSum = 0;
			for(int i = 0; i < FIELD_LENGTH; i++) {
				if(field[i] == whatAmI) {
					stoneSum++;
				}
				if(stoneSum > 3) {
					gState = MOVING;
					break;
				}
			}
		}
		
		// create this state and return it
		State iState = new State(whatAmI, rWhite, rBlack, field, gState);
		return iState;
	}
	
	public int[] getField() {
		return field;
	}

	public void setField(int[] field) {
		this.field = field;
	}

	public int getRemainingWhite() {
		return remainingWhite;
	}

	public void setRemainingWhite(int remainingWhite) {
		this.remainingWhite = remainingWhite;
	}

	public int getRemainingBlack() {
		return remainingBlack;
	}

	public void setRemainingBlack(int remainingBlack) {
		this.remainingBlack = remainingBlack;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getWhatAmI() {
		return whatAmI;
	}

}
