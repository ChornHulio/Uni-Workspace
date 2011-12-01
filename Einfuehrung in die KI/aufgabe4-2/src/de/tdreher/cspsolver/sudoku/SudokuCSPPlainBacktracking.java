package de.tdreher.cspsolver.sudoku;

/**
 * Backtracking-Algorithm for the CSP. The algorithm is a plain backtracking-
 * algorithm without any heurisitics or other high sophisticated things. Stupid
 * like me ;)
 * @author tdreher
 * @date 2011-12-01
 */
public class SudokuCSPPlainBacktracking {
	
	public SudokuState run(SudokuState start) {
		return algorithm(start);		
	}
	
	private SudokuState algorithm(SudokuState oS) {
		if(!oS.isCorrect()) {
			return null;
		}
		if(oS.isSolution()) {
			return oS;
		}
		// Search for the first empty cell and overwrite it with an variable		 
		for(int i = 0; i < oS.field.length; i++) {
			for(int j = 0; j < oS.field[i].length; j++) {
				if(oS.field[i][j] != 0) {
					continue; // cell has already a value
				}
				// Create a new state for the next iteration (deep clone)
				SudokuState nS = new SudokuState(oS.field);
				for(int v = 1; v <= 9; v++) { // values
					nS.field[i][j] = v; 
					SudokuState possibleSol = algorithm(nS); // Backtracking
					if(possibleSol != null) {
						return possibleSol; // its the solution
					}
					// check with the next value
				}
				return null; // no solution found - go one step back
			}
		}
		return null; // no solution found
	}
}
