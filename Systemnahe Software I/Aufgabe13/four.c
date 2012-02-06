#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "four.h"
#include "human.h"
#include "bot_karl.h"

//Wenn keine Argumente uebergeben werden spielt computer vs. computer
//Wenn nur ein Argument uebergeben wurde. nur human -> human vs. human, nur karl -> karl vs. karl
//Wenn zwei Argumente uebergeben dann wird ganz "normal" wie vorgesehen gespielt
//Wenn ein falsches Argument uebergeben wurde z.B. falscher bot, werden die zu verfuegung stehenden Bots augelistet
//KI wird in bot_karl.h erklaert


//Variablen
int human_first = 0;
int player_first = 0;
int two_player = 0;				//=2 wenn zwei bots gegeneinander
int two_human = 0;				//=2 wenn zwei Menschen gegeneinander

//Initialisert für das Spiel notwendige Variablen und Felder
void init()
{
	int i,y;
	//init spielfeld
	for(i=0;i<7;i++)
	{
		for(y=0;y<7;y++)
		{
			spielfeld[i][y]=0;
		}
	}

	human_first = 0;
	player_first = 0;
}

//Gibt aktuelles Spielfeld auf der Konsole aus
void print_spielfeld()
{
	int i,y;

	printf("\n");

	for(i=0;i<7;i++)
	{
		for(y=0;y<7;y++)
		{
			if(y == 0 && spielfeld[i][y]==0)
			{
				printf("|  ");
			}else if(spielfeld[i][y]==0)
			{
				printf("  ");
			}
			if(spielfeld[i][y] == 0)
			{
				printf(" |");
			}
			else if(spielfeld[i][y] == 1 && y==0)  //X
			{
				printf("| X |");
			}
			else if(spielfeld[i][y] == 1) //X
			{
				printf(" X |");
			}
			else if(spielfeld[i][y] == 2 && y==0) //O
			{
				printf("| O |");
			}
			else							//O
			{
				printf(" O |");
			}

			if(y==6)				//neue Zeile
			{
			    printf("\n");
			}
		}

		if(i==6)
		{
			printf("+---+---+---+---+---+---+---+\n");
			printf("  0   1   2   3   4   5   6  \n\n");
		}
	}
}

//kontrolliert, welcher spieler als erstes anfaengt
void check_player(char * player)
{
	if(strstr(player, "human")!= NULL)
	{
		if(player_first == 0)				//kontrolliert welcher player zuerst anfaengt, in diesem fall menschlicher player
		{
			human_first = 1;
			player_first = 2;
		}
		two_human++;			//Spieler werden gezaehlt
	}
	else if(strstr(player, "karl")!= NULL)
	{
		if(player_first == 0)				//kontrolliert welcher player zuerst anfaengt, in diesem fall bot
		{
			player_first = 1;
		}
		two_player++;			//Bots werden gezaehlt
	}
	else	//Wenn ein falsches Argument uebergeben wurde z.B. falscher bot, werden die zu verfuegung stehenden Bots augelistet
	{
		printf("Falscher Bot wurder angegeben! Momentan kann nur gegen karl gespielt werden!\n");
	}

}

//Funktion schaut, ob das Spiel beendet ist oder nicht.
int check_who_win(int player)
{
	int i,y,x,i_tmp,y_tmp,zaehler;
	if(player == 1)						//X
	{
		for(i = 0; i<7; i++)
		{
			for(y=0; y<7; y++)
			{
				if(spielfeld[i][y]==1)
				{
					i_tmp = i;
					y_tmp = y;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 oben
					{
						if(i>6)							//wenn i < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							i--;
							if(spielfeld[i][y]==1 && i>-1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  3)
							{
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 links
					{
						if(y<0)							//wenn y < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							y--;
							if(spielfeld[i][y]==1 && y>-1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  3)
							{
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 schraeg rechts
					{
						if(y>6 || i<0)							//wenn i < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							i--;
							y++;
							if(spielfeld[i][y]==1 && i>-1 && y<7)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  3)
							{
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 schraeg links
					{
						if(y<0 || i<0)							//wenn i < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							i--;
							y--;
							if(spielfeld[i][y]==1 && i>-1 && y>-1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  3)
							{
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
				}
			}
		}
	}
	else					//O
	{
		for(i = 0; i<7; i++)
		{
			for(y=0; y<7; y++)
			{
				if(spielfeld[i][y]==2)
				{
					i_tmp = i;
					y_tmp = y;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 oben
					{
						if(i>6)							//wenn i < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							i--;
							if(spielfeld[i][y]==2 && i>-1 )
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  3)
							{
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 links
					{
						if(y<0)							//wenn y < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							y--;
							if(spielfeld[i][y]==2 && y >=0)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  3)
							{
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 schraeg rechts
					{
						if(y>6 || i<0)							//wenn i < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							i--;
							y++;
							if(spielfeld[i][y]==2 && i>-1 && y<7)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  3)
							{
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 schraeg links
					{
						if(y<0 || i<0)							//wenn i < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							i--;
							y--;
							if(spielfeld[i][y]==2 && y >= 0 && i>-1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  3)
							{
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
				}
			}
		}
	}
	return 0;
}

int main(int argc, char *argv[])
{
	int ready = 0;					//Variable die das ende des Spiels signalisiert 
	char *player;
	player = *argv++;

	init();
	printf("*********VIER GEWINNT***********\n\n");

	player = *argv++;					
	
	if(player != NULL)					//checken ob Argumente uebergeben wurde
	{
		check_player(player);

		player = *argv++;

		if(player != NULL)					//checken ob 2. Argument angeben wurde
		{
			check_player(player);
		}
	}
	

	while(ready != 1)
	{
		print_spielfeld();
		printf("Player ");

		if(two_human == 1 && two_player == 1)				//player vs. bot
		{
			if(human_first == 1)
			{
				if(player_first == 2)
				{
					human(1);					//mensch faengt an
					if(check_who_win(1)==1)
					{
						print_spielfeld();
						printf("Human Player 'X' hat gewonnen!\n");
						ready = 1;
					}
				}
				else
				{
					human(2);					//player faengt an
					if(check_who_win(1)==1)
					{
						print_spielfeld();
						printf("Human Player 'O' hat gewonnen!\n");
						ready = 1;
					}
				}
				human_first = 0;
			}else
			{
				karl(player_first,spielfeld);

				if(check_who_win(player_first)==1)
				{
					print_spielfeld();
					if(player_first == 2)
					{
						printf("Bot 'O' hat gewonnen!\n");
					}
					else
					{
						printf("Bot Player 'X' hat gewonnen!\n");
					}
					ready = 1;
				}
				human_first = 1;

			}
		}
		else if(two_human == 1)								//player vs. player
		{
			if(human_first == 1)
			{
				human(1);
				human_first = 0;
				if(check_who_win(1)==1)
				{
					print_spielfeld();
					printf("Human Player 1 'X' hat gewonnen!\n");
					ready = 1;
				}
			}else
			{
				human(2);
				human_first = 1;
				if(check_who_win(2)==1)
				{
					print_spielfeld();
					printf("Human Player 2 'O' hat gewonnen!\n");
					ready = 1;
				}
			}
		}
		else												//bot vs. bot
		{
			if(human_first == 1)
			{
				karl(1,spielfeld);
				if(check_who_win(1)==1)
				{
					print_spielfeld();
					printf("Karl Player 1 'X' hat gewonnen!\n");
					ready = 1;
				}
				human_first = 0;
			}else
			{
				karl(2,spielfeld);
				if(check_who_win(2)==1)
				{
					print_spielfeld();
					printf("Karl Player 2 'O' hat gewonnen!\n");
					ready = 1;
				}
				human_first = 1;
			}
		}
	}

	return 0;
}
