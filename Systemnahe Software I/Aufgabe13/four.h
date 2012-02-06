//Wenn keine Argumente uebergeben werden spielt computer vs. computer
//Wenn nur ein Argument uebergeben wurde. nur human -> human vs. human, nur karl -> karl vs. karl
//Wenn zwei Argumente uebergeben dann wird ganz "normal" wie vorgesehen gespielt
//Wenn ein falsches Argument uebergeben wurde z.B. falscher bot, werden die zu verfuegung stehenden Bots augelistet.


#ifndef FOUR_H
#define FOUR_H

int spielfeld [7][7];
 

void init();							//initalisert nötige Variablen und Felder
void print_spielfeld();					//printet das aktuelle Spielfeld an die Konsole
void check_player(char * player);		//kontrolliert welche Spieler spielen und ob der Bot verfuegbar ist der gewuenscht wurde
int check_who_win(int player);			//kontroliert ob und wer gewonnen hat

#endif
