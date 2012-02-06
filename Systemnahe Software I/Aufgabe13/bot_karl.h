#ifndef BOT_KARL_H
#define BOT_KARL_H

//Funktionsweise von KI bzw. karl
//   ---------------
//   Die Funktion sucht im Array der struktur (spielfeld) zuerst nach 3er-kombinationen
//   vom Spieler bzw. vom PC.
//
//   Wenn eine Kombination gefunden wurde und die Kombination dem PC gehört, wird
//   sie mit einem Stein vom PC vervollstaendigt.
//   ---> PC gewinnt
//
//   Wenn eine Kombination gefunden wurde und die Kombination dem Spieler 2 gehört,
//   wird sie mit einem Stein vom PC vervollständigt.
//   ---> PC verhindert, dass der Spieler die Kombination vervollständigt
//
//	 Wenn Gegner eine 2er kombination hat, wird dies blockiert

//   Wenn keine 3er-Kombination gefunden wurde, wird das Ganze mit 2er-Kombinationen
//   wiederholt. Das wird aber nur mit den nebeneinander / übereinanderliegenden
//   Steinen gemacht. Diagonale werden nur in 3er-Varianten ergänzt / zerstört.
//
//   lege soviele Steine an Ebenen Null damit mindestens jede zweite Stelle der ki gehoert
//
//	 lege in die Mitte solange es geht
//
//   Wenn überhaupt nichts gefunden wurde (keine 3er UND keine 2er), wird der Stein mithilfe von
//   random() an eine zufällige Stelle gesetzt.

//"main"-Funktion von karl -> KI
void karl(int spielstein, int spielfeld [7][7]);				

//Die Funktion sucht im Array der struktur (spielfeld) nach 3er-kombinationen vom bot und setzt wenn eine stelle frei ist den Stein
//um zu gewinnen
int check_if_win(int player,int spielfeld[7][7]);

//Schaut ob der Gegener drei steine aneinander hat und versucht die zu verhindern
int check_if_player_can_win(int player,int spielfeld[7][7]);

//schaut ob zwei Steine der Gegener aneinander hat um den Weg zu verbauen
int check_if_two_stones_player(int player,int spielfeld[7][7]);

//Schaut ob irgednwo zwei Steine aneinander sind von sich um den dritten stein neben an zu legen
int check_if_two_stones(int player,int spielfeld[7][7]);

//funktion legt Steine damit jede zweite Position am Boden der ki gehört
int seat_eben_null(int player,int spielfeld[7][7]);

//legt solange Steine in die Mitte bis kein Platz mehr in der Mitte vorhanden ist
int seat_into_middle(int player,int spielfeld[7][7]);

//liegt random maeßig Steine auf das Spielfeld
void lege_random(int player,int spielfeld[7][7]);
#endif