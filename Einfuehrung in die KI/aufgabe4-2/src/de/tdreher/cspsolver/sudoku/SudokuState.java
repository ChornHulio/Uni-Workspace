package de.tdreher.cspsolver.sudoku;

/**
 * Represents the state of a Sudoku incl. empty cells (== 0)
 * @author tdreher
 * @date 2011-12-01
 */
public class SudokuState {
	
	/**
	 * The field represents the complete state. An empty cell is represented as
	 * a zero.
	 */
	public int field[][] = null;
	
	/**
	 * Constructor. Copies the given field.
	 * @param field A Sudoku field (9x9)
	 */
	public SudokuState(int[][] field) {
		this.field = new int[field.length][field[0].length];
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				this.field[i][j] = field[i][j];
			}
		}
	}
	
	@Override
	public String toString() {
		String str = "";
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				str = str.concat(" " + field[i][j]);
			}
			str = str.concat("\n"); 
		}
		return str;
	}
	
	/**
	 * Checks if the given state is complete and correct
	 * @return true or false
	 */
	public boolean isSolution() {
		// is complete?
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				if(field[i][j] <= 0) {
					return false; // there is one or more empty cells
				}
			}
		}
		// is correct?
		return isCorrect();
	}

	/**
	 * Checks if the given state is correct (not complete!). In this method you
	 * find all constraints of Sudoku.
	 * @return true or false
	 */
	public boolean isCorrect() {
		if(field.length != field[0].length) {
			throw new RuntimeException("The field is not quadratically");
		}
		// row constraints
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				if(field[i][j] <= 0) {
					continue; // empty cell
				}
				for(int k = j+1; k < field[i].length; k++) {
					if(field[i][j] == field[i][k]) {
						return false; // there is one number two times in one row
					}
				}
			}
		}
		// column constraints
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				if(field[j][i] <= 0) {
					continue; // empty cell
				}
				for(int k = j+1; k < field[i].length; k++) {
					if(field[j][i] == field[k][i]) {
						return false; // there is one number two times in one column
					}
				}
			}
		}
		// box constraints
		for(int a = 0; a < 3; a++) {
			for(int b = 0; b < 3; b++) {
				for(int i = a*3; i < a*3+3; i++) {
					for(int j = b*3; j < b*3+3; j++) {
						if(field[j][i] <= 0) {
							continue; // empty cell
						}
						for(int k = i; k < a*3+3; k++) {
							for(int l = j+1; l < b*3+3; l++) {
								if(field[j][i] == field[l][k]) {
									return false; // there is one number two times in one box
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
}
