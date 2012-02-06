#include "bot_karl.h"

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int check_if_win(int player,int spielfeld[7][7])
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
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 && spielfeld[i-1][y] == 0 && spielfeld[i-1][y-1]!=0 && i > -1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y] = 1;
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
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 &&((spielfeld[i][y-1] == 0 && spielfeld[i+1][y-1]!=0) || (spielfeld[i][y-1] == 0 && i+1>=7 && y >-1)))				//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y-1] = 1;
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 rechts
					{
						if(y>6)							//wenn y < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							y++;
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 &&((spielfeld[i][y+1] == 0 && spielfeld[i+1][y+1]!=0) || (spielfeld[i][y+1] == 0 && i+1<=7 && y+1<7)))				//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y+1] = 1;
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
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 && spielfeld[i-1][y+1] == 0 && spielfeld[i][y+1]!=0 && y+1 <7 && i-1 >-1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y+1] = 1;
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
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 && spielfeld[i-1][y-1] == 0 && spielfeld[i][y-1]!=0 && y-1 > -1 && x-1>-1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y-1] = 1;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 && spielfeld[i-1][y] == 0 && i-1 >-1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y] = 2;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 &&((spielfeld[i][y-1] == 0 && spielfeld[i+1][y-1]!=0) || (spielfeld[i][y-1] == 0  && i+1>=7 && y-1 >-1)))				//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y-1] = 2;
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 rechts
					{
						if(y>6)							//wenn y < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							y++;
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 &&((spielfeld[i][y+1] == 0 && spielfeld[i+1][y+1]!=0) || (spielfeld[i][y+1] == 0 && i+1<=7 && y+1 < 7)))				//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y+1] = 2;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 && spielfeld[i-1][y+1] == 0 && spielfeld[i][y+1]!=0 && i-1 >-1 && y+1 <7)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y+1] = 2;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 && spielfeld[i-1][y-1] == 0 && spielfeld[i][y-1]!=0 && i-1 >-1 && y-1 > -1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y-1] = 2;
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

int check_if_player_can_win(int player,int spielfeld[7][7])
{
	int i,y,x,i_tmp,y_tmp,zaehler;
	if(player == 2)						//X
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
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 && spielfeld[i-1][y] == 0 && i-1 > -1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y] = 2;
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 links
					{
						if(y<0)							//wenn y < 0 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							y--;
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 &&((spielfeld[i][y-1] == 0 && spielfeld[i+1][y-1]!=0) || (spielfeld[i][y-1] == 0 && i+1>=7 && y >-1)))			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y-1] = 2;
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 rechts
					{
						if(y>6)							//wenn y < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							y++;
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 &&((spielfeld[i][y+1] == 0 && spielfeld[i+1][y+1]!=0) || (spielfeld[i][y+1] == 0 && i+1<=7 && y+1<7)))				//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y+1] = 2;
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
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 && spielfeld[i-1][y+1] == 0 && spielfeld[i][y+1] != 0 && i-1 >-1 && y+1 <7)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y+1] = 2;
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
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 && spielfeld[i-1][y-1] == 0 && spielfeld[i][y-1]!=0 && y-1 > -1 && x-1>-1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y-1] = 2;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 && spielfeld[i-1][y] == 0 && i-1 > -1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y] = 1;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 &&((spielfeld[i][y-1] == 0 && spielfeld[i+1][y-1]!=0) || (spielfeld[i][y-1] == 0 && i+1>=7 && y >-1)))				//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y-1] = 1;
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 rechts
					{
						if(y>6)							//wenn y < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							y++;
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 &&((spielfeld[i][y+1] == 0 && spielfeld[i+1][y+1]!=0) || (spielfeld[i][y+1] == 0 && i+1<=7 && y+1<7)))				//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y+1] = 1;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 && spielfeld[i-1][y+1] == 0 && spielfeld[i][y+1] != 0 && i-1 >-1 && y+1 <7)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y+1] = 1;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  2 && spielfeld[i-1][y-1] == 0 && spielfeld[i][y-1]!=0 && y-1 > -1 && x-1>-1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y-1] = 1;
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

void lege_random(int spielstein, int spielfeld[7][7])
{
	int i = 0;
	int ready = 0;
	int stelle = 0;

	while(ready != 1)
	{
		srand ( time(NULL) +i );

		stelle = rand() % (6 - 0+1);			//ki

		for(i = 6; i>=0; i--)
			{
				if(spielfeld[i][stelle]==0)
				{
					spielfeld[i][stelle] = spielstein;
					ready = 1;
					break;
				}
			}
		i++;
	}

}

int check_if_two_stones_player(int player,int spielfeld[7][7])
{
int i,y,x,i_tmp,y_tmp,zaehler;
	if(player == 2)						//X
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
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 && spielfeld[i-1][y] == 0 && i-1 > -1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y] = 2;
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 links
					{
						if(y<0)							//wenn y < 0 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							y--;
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 &&((spielfeld[i][y-1] == 0 && spielfeld[i+1][y-1]!=0) || (spielfeld[i][y-1] == 0 && i+1>=7 && y-1 >-1)))			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y-1] = 2;
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 rechts
					{
						if(y>6)							//wenn y < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							y++;
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 &&((spielfeld[i][y+1] == 0 && spielfeld[i+1][y+1]!=0 && i+1<=7 && y+1<7)))				//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y+1] = 2;
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
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 && spielfeld[i-1][y+1] == 0 && spielfeld[i][y+1] != 0 && i-1 >-1 && y+1 <7)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y+1] = 2;
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
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 && spielfeld[i-1][y-1] == 0 && spielfeld[i][y-1]!=0 && y-1 > -1 && x-1>-1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y-1] = 2;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 && spielfeld[i-1][y] == 0 && i-1 > -1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y] = 1;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 &&((spielfeld[i][y-1] == 0 && spielfeld[i+1][y-1]!=0) || (spielfeld[i][y-1] == 0 && i+1>=7 && y >-1)))				//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y-1] = 1;
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 rechts
					{
						if(y>6)							//wenn y < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							y++;
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 &&((spielfeld[i][y+1] == 0 && spielfeld[i+1][y+1]!=0) && i+1<=7 && y+1<7))				//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y+1] = 1;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 && spielfeld[i-1][y+1] == 0 && spielfeld[i][y+1] != 0 && i-1 >-1 && y+1 <7)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y+1] = 1;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 && spielfeld[i-1][y-1] == 0 && spielfeld[i][y-1]!=0 && y-1 > -1 && x-1>-1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y-1] = 1;
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

int seat_into_middle(int player,int spielfeld[7][7])
{
	int i = 0;
	while("")
	{
		for(i = 6; i>=0; i--)
		{
				if(spielfeld[i][3]==0)
				{
					spielfeld[i][3] = player;
					return 1;
				}
		}
		return 0;
	}
}

int check_if_two_stones(int player,int spielfeld[7][7])
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
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 && spielfeld[i-1][y] == 0 && spielfeld[i-1][y-1]!=0 && i-1 > -1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y] = 1;
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
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 &&((spielfeld[i][y-1] == 0 && spielfeld[i+1][y-1]!=0) || (spielfeld[i][y-1] == 0 && i+1>=7 && y >-1)))				//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y-1] = 1;
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 rechts
					{
						if(y>6)							//wenn y < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							y++;
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 &&((spielfeld[i][y+1] == 0 && spielfeld[i+1][y+1]!=0) || (spielfeld[i][y+1] == 0 && i+1<=7 && y+1<7)))				//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y+1] = 1;
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
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 && spielfeld[i-1][y+1] == 0 && spielfeld[i][y+1]!=0 && i-1 >-1 && y+1 <7)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y+1] = 1;
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
							if(spielfeld[i][y]==1)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 && spielfeld[i-1][y-1] == 0 && spielfeld[i][y-1]!=0 && y-1 > -1 && x-1>-1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y-1] = 1;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 && spielfeld[i-1][y] == 0 && i-1 > -1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y] = 2;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 &&((spielfeld[i][y-1] == 0 && spielfeld[i+1][y-1]!=0) || (spielfeld[i][y-1] == 0 && i+1>=7 && y >-1)))				//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y-1] = 2;
								return 1;
							}
						}
					}
					i = i_tmp;
					y = y_tmp;
					zaehler=0;
					for(x = 0 ; x <4; x++)				//kontrolliere 4 rechts
					{
						if(y>6)							//wenn y < 3 dann spielfeld vorbei
						{
							break;
						}
						else
						{
							y++;
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 &&((spielfeld[i][y+1] == 0 && spielfeld[i+1][y+1]!=0) || (spielfeld[i][y+1] == 0 && i+1<=7 && y+1<7)))				//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i][y+1] = 2;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 && spielfeld[i-1][y+1] == 0 && spielfeld[i][y+1]!=0 && i-1 >-1 && y+1 <7)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y+1] = 2;
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
							if(spielfeld[i][y]==2)
							{
								zaehler++;
							}
							else
							{
								break;
							}
							if(zaehler ==  1 && spielfeld[i-1][y-1] == 0 && spielfeld[i][y-1]!=0 && y-1 > -1 && x-1>-1)			//Freies Feld neben 3 spielsteinen
							{
								spielfeld[i-1][y-1] = 2;
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

int seat_eben_null(int player,int spielfeld[7][7])
{
	int i = 0;
	while("")		//jede zweite stelle sollte uns gehoeren um nicht ausgetrickst zu werden^^
	{
		for(i = 6; i>=0; i=i-2)
		{
				if(spielfeld[6][i]==0)
				{
					spielfeld[6][i] = player;
					return 1;
				}
		}
		for(i = 5; i>=0; i=i-2)
		{
				if(spielfeld[6][i]==0)
				{
					spielfeld[6][i] = player;
					return 1;
				}
		}
		return 0;
	}
}
void karl(int spielstein,int spielfeld[7][7])
{
	printf("karl \n");
	//Schaue ob bot gewinnen kann
	if(!check_if_win(spielstein,spielfeld))
	{
		//Schaue ob der Gegner gewinnen kann und drei Steine aneinander hat
		if(!check_if_player_can_win(spielstein,spielfeld))
		{
			//Schaue ob gegner 2 Steine hat dann blockiere ihn
			if(!check_if_two_stones_player(spielstein,spielfeld))
			{
				//Schaue ob zwei Steine nebeneinander liegen dann lege dritten dazu
				if(!check_if_two_stones(spielstein,spielfeld))
				{
					//schaue das beim boden jedes zweite kästchen dir gehoert
					if(!seat_eben_null(spielstein,spielfeld))
					{
						//lege in die mitte solange es geht
						if(!seat_into_middle(spielstein,spielfeld))
						{
							//lege random maeßig einen einzelnen Stein
							lege_random(spielstein,spielfeld);
						}
					}
				}
			}
		}
	}
}
