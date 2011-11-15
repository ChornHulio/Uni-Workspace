/*Gegeben ist eine Reihe nebeneinander stehender Kegel. Zwei Spieler (Mensch gegen
Computer) sind abwechselnd dran. Jeder Spieler kann entweder einen oder zwei (-)
benachbarte Kegel herausschiessen. Wer den/die letzten Kegel raus schiesst, gewinnt.*/

/**
 * @author Heiko Golavsek, Andra Herta, Tobias Dreher
 * @date 2011-11-09
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

//set the bit on "bit array" to 0.
unsigned long set_bit(unsigned long bits, int bit_index, int state)
{
	if(bits & (1 << (bit_index -1))) //check if the cone is away
	{
		if(state == 0)
		{
			bits &=  ~(1 << (bit_index-1));
		}
		else
		{
			bits |= (1 << (bit_index-1));
		}
	}
	else
	{
		printf("Pin %d is not longer standing.\n",bit_index);
	}

	return bits; 
}

//print the cones on console
void print_kegel(unsigned long bits)
{
	int i;
	for(i =0; i <10 ; i++)
	{
		if(bits & 1 << i) //check if the bit is one
			printf("/  \\ ");
		else
			printf("     ");
	}

	printf("\n");

	for(i= 0; i <10 ; i++)
	{
		if(bits & 1 << i && i == 9)
			printf(" 10 ");

		if(bits & 1 << i && i != 9)
			printf(" 0%d  ",i+1);
		else
			printf("     ");
	}

	printf("\n");
}

//check an simulate the players parameter
unsigned long player(unsigned long bits)
{
	int kegel_index;
	int bits_alt = bits;
	while("")
	{
		printf("Your move: ");
		scanf("%d",&kegel_index);
		
		if(kegel_index < -10 || kegel_index > 10 || kegel_index == 0)
		{
			printf("Invalid pin number\n");
		}
		else
		{
			if(kegel_index == -10)	//spezialfall -10
			{
				bits = set_bit(bits, -(kegel_index), 0);
			}
			else if(kegel_index < 0 ) //If the player want get two cones
			{
				if(bits & ( 1 << -((kegel_index)-1))) //check if the first cone is  empty
				{
					if(bits & ( 1 << -((kegel_index-1)-1)))//check if the second cone right behind the fist cone is empty
					{
						//set the bits
						bits = set_bit(bits, -(kegel_index), 0);
						bits = set_bit(bits, -(kegel_index-1), 0);
					}
					else
					{
						printf("Pin %d is not longer standing.\n",kegel_index-1);
					}
				}
				else
				{
					printf("Pin %d is not longer standing.\n",kegel_index);
				}
			}
			else
			{
				bits = set_bit(bits, kegel_index, 0);
			}

			if(bits_alt != bits)
			{
				return bits;
			}
		}
	}
}


//simulate the bot 
unsigned long bot(unsigned long bits)
{
	int i = 0;
	int bit;
	while("")
	{
		srand ( time(NULL) +i );

		bit = rand() % 21 - 10;			//ki

		if(bit < 0)
		{
			if(bits & ( 1 << -(bit -1))&& bits & ( 1 << -((bit-1)-1)))		//check if the two cones are empty
			{
				printf("Bot move: %d\n",bit);
				bits = set_bit(bits,-(bit),0);
				bits = set_bit(bits,-(bit-1),0);
				return bits;
			}	
		}
		else if(bit >0)
		{
			if(bits & ( 1 << (bit -1)))
			{
				printf("Bot move: %d\n",bit);
				bits = set_bit(bits,bit,0);
				return bits;
			}
		}
		
		i++;
	}
}

// Bowling Game
int main()
{
	unsigned long kegel = 1023;			//10bit every bit is one cone

	printf("*** Dawson's Bowling Game ***\n");

	print_kegel(kegel);

	while("")
	{
		//Player
		kegel = player(kegel);

		//If the "bit array" is empty the Bot have lost now.
		if(kegel == 0)
		{
			printf("*** Bot lose! ***\n");
			printf("*** You win! ***\n");
			break;
		}

		//Print the cones to the console
		print_kegel(kegel);

		//Bot
		kegel = bot(kegel);

		//If the "bit array" is empty the Player have won now.
		if(kegel == 0)
		{
			printf("*** You lose! ***\n");
			break;
		}

		//Print the cones to the console
		print_kegel(kegel);

	}
	return 0;
}
