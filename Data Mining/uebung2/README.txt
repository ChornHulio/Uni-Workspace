Data Mining - 2.Aufgabenblatt

Bearbeiter:
	Andra Herta
	Tobias Dreher

################################################
# Aufgabe 5

Alle geforderten Ausgaben werden direkt ausgegeben.


################################################
# Aufgabe 6

Die Ausgabe der Funktion ist eine Matrix. Die Position in der Matrix gibt den/die 
Punkt/e der ersten Partion an. Die Partition besteht aus einem oder zwei Punkten.
Die zweite Partition besteht aus den verbleibenden Elementen. Der Wert an dieser 
Position in der Matrix gibt den Mittelwert der Durchmesser der einzelnen Partitionen an.

Die Matrix stellt also wie folgt, die Punkte der ersten Partition dar:
	(1) (1 2) (1 3) (1 4) (1 5) 
	     (2)  (2 3) (2 4) (2 5)
	           (3)  (3 4) (3 5)
	                 (4)  (4 5)
	                       (5)
	                       
Ergebnis der Berechnungen ist, dass alle Clusterungen, bei denen eine Partition 
aus lediglich einem Element bestehen, optimal sind.
