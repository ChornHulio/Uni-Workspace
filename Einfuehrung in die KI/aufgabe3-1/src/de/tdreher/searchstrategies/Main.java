package de.tdreher.searchstrategies;

import de.tdreher.searchstrategies.heuristics.*;
import de.tdreher.searchstrategies.problems.*;
import de.tdreher.searchstrategies.strategies.*;

public class Main {

	public static void main(String[] args) {
		
		long ms = 0; // milliseconds for stopwatch
		ProblemSolver ps = null; // the solver
		
		/* *************************** PROBLEM A *************************** */
		
		// Breadth First - TreeSearch
		ps = new ProblemSolver(	new NumberPuzzleProblemA(), 
								new BreadthFirstTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(Problem A | Breadth First)\n\t\t" + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms) + "\n");
		
		// Iterative Depth - TreeSearch
		ps = new ProblemSolver(	new NumberPuzzleProblemA(), 
								new IterativeDepthTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(Problem A | Iterative Depth)\n\t\t" + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms) + "\n");
		
		// Greedy Search - Heuristic A
		ps = new ProblemSolver(		new NumberPuzzleProblemA(), 
		 							new GreedyTreeSearch(), 
		 							new NumberPuzzleHeuristicA());
		ms = System.currentTimeMillis();
		System.out.println("(Problem A | Greedy Search | Heuristic A)\n\t\t" + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms) + "\n");
		
		// Greedy Search - Heuristic B
		ps = new ProblemSolver(		new NumberPuzzleProblemA(), 
		 							new GreedyTreeSearch(), 
		 							new NumberPuzzleHeuristicB());
		ms = System.currentTimeMillis();
		System.out.println("(Problem A | Greedy Search | Heuristic B)\n\t\t" + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms) + "\n");
		
		// A* Search - Heuristic A
		ps = new ProblemSolver(	new NumberPuzzleProblemA(), 
								new AStarTreeSearch(), 
								new NumberPuzzleHeuristicA());
		ms = System.currentTimeMillis();
		System.out.println("(Problem A | A* Search | Heuristic A)\n\t\t" + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms) + "\n");
		
		// A* Search - Heuristic B
		ps = new ProblemSolver(	new NumberPuzzleProblemA(), 
								new AStarTreeSearch(), 
								new NumberPuzzleHeuristicB());
		ms = System.currentTimeMillis();
		System.out.println("(Problem A | A* Search | Heuristic B)\n\t\t" + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms) + "\n");
	
		/* *************************** PROBLEM B *************************** */
		
		// Breadth First - TreeSearch - needs to much memory on a few systems
		
		// Iterative Depth - TreeSearch 
		ps = new ProblemSolver(	new NumberPuzzleProblemB(), 
								new IterativeDepthTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(Problem B | Iterative Depth)\n\t\t" + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms) + "\n");
		
		// Greedy Search - doesn't solve the problem
		
		// A* Search - Heuristic A
		ps = new ProblemSolver(	new NumberPuzzleProblemB(), 
								new AStarTreeSearch(), 
								new NumberPuzzleHeuristicA());
		ms = System.currentTimeMillis();
		System.out.println("(Problem B | A* Search | Heuristic A)\n\t\t" + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms) + "\n");
		
		// A* Search - Heuristic B
		ps = new ProblemSolver(	new NumberPuzzleProblemB(), 
								new AStarTreeSearch(), 
								new NumberPuzzleHeuristicB());
		ms = System.currentTimeMillis();
		System.out.println("(Problem B | A* Search | Heuristic B)\n\t\t" + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms) + "\n");
		
		/* *************************** PROBLEM C *************************** */
		
		// A* Search - Heuristic B
		ps = new ProblemSolver(	new NumberPuzzleProblemC(), 
								new AStarTreeSearch(), 
								new NumberPuzzleHeuristicB());
		ms = System.currentTimeMillis();
		System.out.println("(Problem C | A* Search | Heuristic B)\n\t\t" + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms) + "\n");
	}

}
