package de.tdreher.cspsolver.sudoku;

/**
 * Sudoku-Problem from the newspaper "Die Zeit", date of issue 2011-11-21
 * @author tdreher
 * @date 2011-12-01
 */
public class SudokuProblemA {
	private int field[][] = {
			{2,0,0,9,5,6,4,0,0},
			{0,0,0,1,7,0,0,8,3},
			{0,0,0,8,0,2,9,0,0},
			{4,0,0,0,0,0,6,0,0},
			{1,0,3,0,0,0,5,4,0},
			{0,0,2,0,0,9,7,0,0},
			{3,0,0,0,0,0,0,7,0},
			{9,0,0,0,0,7,0,2,0},
			{8,0,7,6,0,0,1,0,0}};
	
	public int[][] getStartState() {
		return field;
	}
}
