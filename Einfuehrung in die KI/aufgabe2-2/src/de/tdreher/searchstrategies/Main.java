package de.tdreher.searchstrategies;

import de.tdreher.searchstrategies.problems.ProblemSolver;
import de.tdreher.searchstrategies.problems.SokobanProblemA;
import de.tdreher.searchstrategies.problems.SokobanProblemB;
import de.tdreher.searchstrategies.problems.SokobanProblemC;
import de.tdreher.searchstrategies.strategies.BreadthFirstTreeSearch;
import de.tdreher.searchstrategies.strategies.DepthTreeSearch;
import de.tdreher.searchstrategies.strategies.IterativeDepthTreeSearch;

public class Main {

	public static void main(String[] args) {
		
		long ms = 0; // milliseconds for stopwatch
		
		// Problem A - Breadth First - TreeSearch
		ProblemSolver ps = new ProblemSolver(new SokobanProblemA(), new BreadthFirstTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(A / Breadth First) " + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms));
		
		// Problem A - Depth First - TreeSearch
		// limited on a depth on 20 - otherwise it is endless
		ps = new ProblemSolver(new SokobanProblemA(), new DepthTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(A / Depth First) " + ps.run(20));
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms));
		
		// Problem A - Depth Limited - TreeSearch
		ps = new ProblemSolver(new SokobanProblemA(), new DepthTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(A / Depth Limited) " + ps.run(10));
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms));
		
		// Problem A - Iterative Depth - TreeSearch
		ps = new ProblemSolver(new SokobanProblemA(), new IterativeDepthTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(A / Iterative Depth) " + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms));
		
		// Problem B - Breadth First - TreeSearch
		ps = new ProblemSolver(new SokobanProblemB(), new BreadthFirstTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(B / Breadth First) " + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms));
		
		// Problem B - Depth First - TreeSearch
		// limited on a depth on 18 - otherwise it is endless
		ps = new ProblemSolver(new SokobanProblemB(), new DepthTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(B / Depth First) " + ps.run(18));
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms));

		// Problem B - Depth Limited - TreeSearch
		ps = new ProblemSolver(new SokobanProblemB(), new DepthTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(B / Depth Limited) " + ps.run(17));
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms));
		
		// Problem B - Iterative Depth - TreeSearch
		ps = new ProblemSolver(new SokobanProblemB(), new IterativeDepthTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(B / Iterative Depth) " + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms));
		
		// Problem C - Breadth First - TreeSearch
		/* CAUSES OUT OF MEMORY ERROR
		ps = new ProblemSolver(new SokobanProblemC(), new BreadthFirstTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(C / Breadth First) " + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms));
		*/
		
		// Problem C - Depth First - TreeSearch
		/* CAUSES OUT OF MEMORY ERROR
		ps = new ProblemSolver(new SokobanProblemC(), new DepthTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(C / Depth First) " + ps.run(18));
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms));
		*/

		// Problem C - Depth Limited - TreeSearch
		/* CAUSES OUT OF MEMORY ERROR
		ps = new ProblemSolver(new SokobanProblemC(), new DepthTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(C / Depth Limited) " + ps.run(17));
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms));
		*/
		
		// Problem C - Iterative Depth - TreeSearch
		/* CAUSES OUT OF MEMORY ERROR
		ps = new ProblemSolver(new SokobanProblemC(), new IterativeDepthTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(C / Iterative Depth) " + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms));
		*/
	}

}
