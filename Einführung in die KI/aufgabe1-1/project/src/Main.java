
public class Main {

	/**
	 * Main-Methode
	 */
	public static void main(String[] args) {
		// Feld erstellen
		Feld feld = new Feld();
		feld.erstelleFeld();
		
		// Start und Zielpunkt festsetzen
		int start = 1;
		int ziel = 15;
		if(args.length == 2) {
			try {
				start = Integer.parseInt(args[0]);
				ziel = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.out.println("Falsches Eingabeformat!\n");	
				System.out.println("Erstes Argument: Startnummer");	
				System.out.println("Zweites Argument: Zielnummer");	
				return;
			}
		}
		
		// Pfad suchen und Ergebnis ausgeben
		System.out.println("Start: " + start);
		System.out.println("Ziel: " + ziel);
		System.out.println(feld.findSolution(start, ziel)); // Von Start nach Ziel
	}

}
