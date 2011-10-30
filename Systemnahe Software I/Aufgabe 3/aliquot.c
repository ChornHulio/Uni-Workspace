/**
 * Das ‘‘Aliquote Game’’ ist ein Spiel fuer zwei Teilnehmer (Computer und 
 * Mensch), die abwechselnd ziehen. Das Spiel beginnt mit einer beliebigen 
 * natuerlichen Zahl. Der Spieler, der am Zug ist, waehlt einen der aliquoten 
 * Teiler aus und zieht diesen von der Zahl ab. Der Spieler, der die 1 
 * praesentiert bekommt (und dadurch nicht mehr ziehen kann, weil die 1 keine
 * aliquoten Teiler hat) verliert das Spiel.
 */

/**
 * @author Heiko Golavsek, Andra Herta, Tobias Dreher
 * @date 2011-10-27
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int bot(int number)
{
	int numberTmp = 0;
	int counterForRand = 0;

	while(1 == 1)
	{
		srand((unsigned)time(NULL) + counterForRand++);
		numberTmp = rand() % (number/2+1) + 1;
		
		if(number % numberTmp == 0)
		{
			printf("Bot: %d\n", numberTmp);
			number = number - numberTmp;
			break;
		}
	}
	
	if(number == 1)
	{
		printf("You Lost! :-(\n");
		exit(0);
	}

	return number;
}

int player(int number)
{
	int divider = 0;
	
	while(1==1)
	{
		printf("You: ");
		if(scanf("%d",&divider) != 1) {
			printf("ERROR: No proper number\n");
			getchar(); // workaround for clearing input buffer
			continue;
		}
		
		if(divider == number)
		{
			printf("FALSE: Divisor is same as the number\n");
		}
		else if (divider < 1 || divider > number)
		{
			printf("FALSE: Out of Range\n");
		}
		else if (number % divider != 0)
		{
			printf("FALSE: No proper divisor\n");
		}
		else
		{	
			number = number - divider;
			break;
		}	
	}
	
	if(number == 1)
	{
		printf("You win! ;-)\n");
		exit(0);
	}
	return number;
}

int main()
{
	int number = 0;			

 	printf("*** Aliquot Game ***\n");

	srand((unsigned)time(NULL));
	number = rand() % 50 + 10;

	while(number >= 1)
	{
		// Player
		printf("Number for you: %d\n", number);
		number = player(number);

		// Bot
		printf("Number for bot: %d\n", number);
		number = bot(number);
	}

	return 0;
}
