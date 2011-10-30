import java.util.ArrayList;


public class Kachel {

	private String name = null;
	
	/**
	 * Verbindungen zu anderen Kacheln. Verbindungen wurde noch nicht mit dem
	 * Algorithmus ausprobiert.
	 * Maximal 6 Verbindungen gibt es: oben, unten, linksOben, rechtsOben, 
	 * linksUnten, rechtsUnten. In welcher Himmelsrichtung die verbundenen 
	 * Kacheln liegen, ist für den allerdings Algorithmus unrelevant und wird
	 * deshalb auch nicht gespeichert.  Ebenfalls werden keine Wände (null)
	 * gespeichert.
	 */
	private ArrayList<Kachel> offeneVersuche = new ArrayList<Kachel>();
	
	/**
	 * Zuletzt bzw. aktuell versuchte Kachel
	 */
	private Kachel aktuellerVersuch = null;
	
	/**
	 * Die veralteten Versuche - diese werden evtl. wieder benoetigt, wenn eine
	 * Sackgasse nach einem Zyklus erkannt wurde
	 */
	private ArrayList<Kachel> letztenVersuche = new ArrayList<Kachel>();
	
	/**
	 * Konstruktor um den Namen einer Kachel zu speichern
	 * 
	 * @param name
	 */
	Kachel(String name) {
		this.name = name;
	}
	
	/**
	 * Speichere Verbindungen zu anderen Kacheln. Himmelsrichtungen werden
	 * dabei nicht gespeichert, da diese fuer den Algorithmus unrelevant sind. 
	 * Ebenfalls werden keine Wände (null) gespeichert.
	 * 
	 * @param oben
	 * @param unten
	 * @param linksOben
	 * @param rechtsOben
	 * @param linksUnten
	 * @param rechtsUnten
	 */
	void erstelleVerbindungen(Kachel oben, Kachel unten, Kachel linksOben,
			Kachel rechtsOben, Kachel linksUnten, Kachel rechtsUnten) {
		if(oben != null) {
			offeneVersuche.add(oben);
		}
		if(unten != null) {
			offeneVersuche.add(unten);
		}
		if(linksOben != null) {
			offeneVersuche.add(linksOben);
		}
		if(rechtsOben != null) {
			offeneVersuche.add(rechtsOben);
		}
		if(linksUnten != null) {
			offeneVersuche.add(linksUnten);
		}
		if(rechtsUnten != null) {
			offeneVersuche.add(rechtsUnten);
		}
	}
	
	/**
	 * Backtrack-Algorithmus um einen Weg zum Ziel zu finden. Bei einer
	 * Sackgasse wird eine Exception geschmiessen und ein neuer Versuch 
	 * gestartet. Ist der Algorithmus am Ziel angekommen, wird die Methode
	 * ohne Rückgabeparameter zurückgegeben.
	 * @param ziel Die Zielkachel
	 * @throws SackgasseException Es wurde keine 
	 */
	void findSolution(Kachel ziel, Kachel vorgaenger) throws SackgasseException {
		// Loesche Vorgaenger aus offeneVersuche, damit nicht zurueck gegangen wird
		offeneVersuche.remove(vorgaenger);
		
		// Wenn am Ziel angekommen, dann beende ab
		if(this == ziel) {
			return;
		}
		
		// Alle Versuche aufgebraucht == Sackgasse
		if(offeneVersuche.isEmpty()) {
			throw new SackgasseException();
		}
		
		// Erster oder naechster Versuch starten
		if(aktuellerVersuch != null) {
			letztenVersuche.add(0, aktuellerVersuch); // letzten Versuche zwischenspeichern
		}
		aktuellerVersuch = offeneVersuche.get(0);
		offeneVersuche.remove(0);
		try {
			aktuellerVersuch.findSolution(ziel, this); // Gehe weiter
		} catch (SackgasseException e) {
			// aktueller Versuch durch letzten ersetzen (relevant bei Sackgasse in Zyklus)
			if(!letztenVersuche.isEmpty()) {
				aktuellerVersuch = letztenVersuche.get(0);
				letztenVersuche.remove(0);
			}
			this.findSolution(ziel, vorgaenger); // Neuer Versuch einleiten
		}
	}
	
	/**
	 * Es wird von Start nach Ziel durchgegangen und die Pfadnamen aneinander 
	 * gehaengt.
	 * 
	 * @param pfad Pfadname von Start bis zu dieser Kachel
	 * @return Gesamter Pfadname
	 */
	String erstellePfad(String pfad) {
		if(aktuellerVersuch == null) {
			return pfad.concat(" - " + this.name);
		} else {
			return aktuellerVersuch.erstellePfad(pfad + " - " + this.name);
		}
	}
}
