package de.tdreher.cspsolver;

import de.tdreher.cspsolver.sudoku.SudokuCSPPlainBacktracking;
import de.tdreher.cspsolver.sudoku.SudokuProblemA;
import de.tdreher.cspsolver.sudoku.SudokuState;

/**
 * CSP-Solver for Sudoku
 * @author tdreher
 * @date 2011-12-01
 */
public class Main {

	public static void main(String[] args) {
		long ms = 0; // milliseconds for stopwatch
		
		// Problem A | Plain Backtracking
		SudokuProblemA p = new SudokuProblemA();
		SudokuState s = new SudokuState(p.getStartState());
		SudokuCSPPlainBacktracking solver = new SudokuCSPPlainBacktracking();
		ms = System.currentTimeMillis();
		SudokuState solution = solver.run(s);
		System.out.println("Problem A | Plain Backtracking");
		System.out.println("Lead time: " + (System.currentTimeMillis() - ms) + " ms");
		if(solution == null) {
			System.out.println("No solution found");
		} else {
			System.out.println("Solution:\n" + solution.toString());
		}
	}

}
