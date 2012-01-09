package de.tdreher.morris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.tdreher.morris.strategies.Beginning;
import de.tdreher.morris.strategies.Flying;
import de.tdreher.morris.strategies.Moving;

/**
 * A artificial intelligence to play "Nine Men's Morris". The programm is very
 * specifized to this problem, because reusability is no requirement.
 * @author tdreher
 * @date 2012-01-09
 */
public class Main {

	/**
	 * @param args optinal: max time for one move
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws IOException {
		
		//TODO max time for one move
		
		// init reader
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		    
		// color
		int color = Integer.parseInt(reader.readLine()); // 0 == white, 1 == black
		if(color != 0 && color != 1) {
			System.err.println("Error: No color choosen: " + color);
		}
		
		// name
		reader.readLine(); // thank you for the name ;)
				
		// play
		while(true) { // only break if an error occur or the game server kill this programm
			oneMove(color);
		}
	}

	private static void oneMove(int color) throws IOException {		
		// state
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String strState = reader.readLine();
		State state = State.interpretState(strState, color);
		
		// think
		Move move = new Move();
		switch(state.getState()) {
		case State.BEGINNING:
			Beginning strategyBeg = new Beginning();
			move = strategyBeg.run(state);
			break;
		case State.MOVING:
			Moving strategyMov = new Moving();
			move = strategyMov.run(state);
			break;
		case State.FLYING:
			Flying strategyFly = new Flying();
			move = strategyFly.run(state);
			break;
		}
		
		// move
		System.out.println(move.toString());
		System.err.println(move.toString());
	}
}
