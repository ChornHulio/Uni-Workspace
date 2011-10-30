import java.util.ArrayList;

/**
 * Ein Feld besteht aus mehreren Kacheln und ist wie folgt aufgebaut und nummeriert:
 *      ___     ___
 *  ___/ 1 \___/ 2 \___
 * / 3 \___/ 4 \___/ 5 \
 * \___/ 6 \___/ 7 \___/
 *     \___/   \___/
 */

public class Feld {
	
	private static final int ANZAHL_KACHELN = 15;
	private ArrayList<Kachel> kacheln = new ArrayList<Kachel>();
	
	/**
	 * Initalisiere das gesamte Feld mit allen Kacheln und deren Verbindungen
	 */
	void erstelleFeld() {
		// Erstelle Kacheln
		for(int i = 0; i < ANZAHL_KACHELN; i++) {
			kacheln.add(new Kachel(String.valueOf(i+1)));
		}
		// Erstelle Verbindungen (oben, unten, linksOben, rechtsOben, linksUnten, rechtsUnten)
		kacheln.get(0).erstelleVerbindungen(null, null, null, null, null, kacheln.get(3));
		kacheln.get(1).erstelleVerbindungen(null, null, null, null, kacheln.get(3), kacheln.get(4));
		kacheln.get(2).erstelleVerbindungen(null, null, null, null, null, kacheln.get(5));
		kacheln.get(3).erstelleVerbindungen(null, kacheln.get(8), kacheln.get(0), kacheln.get(1), kacheln.get(5), null);
		kacheln.get(4).erstelleVerbindungen(null, kacheln.get(9), kacheln.get(1), null, null, null);
		kacheln.get(5).erstelleVerbindungen(null, kacheln.get(10), kacheln.get(2), kacheln.get(3), null, null);
		kacheln.get(6).erstelleVerbindungen(null, kacheln.get(11), null, null, null, kacheln.get(9));
		kacheln.get(7).erstelleVerbindungen(null, null, null, null, null, null);
		kacheln.get(8).erstelleVerbindungen(kacheln.get(3), kacheln.get(13), null, null, null, kacheln.get(11));
		kacheln.get(9).erstelleVerbindungen(kacheln.get(4), kacheln.get(14), kacheln.get(6), null, null, null);
		kacheln.get(10).erstelleVerbindungen(kacheln.get(5), null, null, null, kacheln.get(12), null);
		kacheln.get(11).erstelleVerbindungen(kacheln.get(6), null, kacheln.get(8), null, kacheln.get(13), null);
		kacheln.get(12).erstelleVerbindungen(null, null, null, kacheln.get(10), null, null);
		kacheln.get(13).erstelleVerbindungen(kacheln.get(8), null, null, kacheln.get(11), null, null);
		kacheln.get(14).erstelleVerbindungen(kacheln.get(9), null, null, null, null, null);
	}
	
	/**
	 * Finde eine Loesung von start nach ziel. Wenn es eine Loesung gibt, gebe
	 * den Pfad aus.
	 * @param start Nummer der Start-Kachel ( 1 .. ANZAHL_KACHELN )
	 * @param ziel Nummer der Ziel-Kachel ( 1 .. ANZAHL_KACHELN )
	 * @return Pfad oder Fehlerausgabe
	 */
	String findSolution(int start, int ziel) {
		Kachel startKachel, zielKachel;
		try {
			startKachel = kacheln.get(start-1);
			zielKachel = kacheln.get(ziel-1);
		} catch (IndexOutOfBoundsException e) {
			return "Keine gueltige Start oder Ziel Kachel";
		}
		try {
			// Starte Backtrack-Algorithmus
			startKachel.findSolution(zielKachel, null);
		} catch (SackgasseException e) {
			// keine Loesung gefunden
			return "Es gibt keinen Pfad von Kachel " + start + " zu Kachel " + ziel;
		}
		return startKachel.erstellePfad("Pfad: ");
	}

}
