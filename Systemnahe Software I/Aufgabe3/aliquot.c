/**
 * The Aliquot Game is a game for two players. In this implementation plays an 
 * human versus a bot. The game begins with a random natural number. The first 
 * player have to choose an aliquot divider, which become subtracted from the
 * random number. On the next move the other player has to find an aliquot
 * divider to the solution of the subtraction and so on... The winner is the
 * player who get the number 1 presented - because this number has no aliquot 
 * divider.
 */

/**
 * @author Heiko Golavsek, Andra Herta, Tobias Dreher
 * @date 2011-10-31
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

/**
 * Computer player (bot), which uses random numbers to solve his part of the game
 */
int bot(int number)
{
	int divider = 0; // value to save the (potential) solution
	int counterForRand = 0; // counter to get more random values in one second

	// endless loop - it breaks only with a correct divider
	while(1 == 1)
	{
		// random number is addicted to the time (in sec) and an counter
		// the counter is needed to get more random values in one second
		srand((unsigned)time(NULL) + counterForRand++);  
		divider = rand() % (number/2+1) + 1;
		
		// check if the random number is a solution
		if(number % divider == 0)
		{
			printf("Bot: %d\n", divider);
			number = number - divider;
			break;
		}
	}
	
	// if the number is 1, the bot won and the player lost
	if(number == 1)
	{
		printf("You Lost! :-(\n");
		exit(0);
	}

	return number;
}

/**
 * The player at the keyboard
 */
int player(int number)
{
	int divider = 0; // value to save the (potential) solution
	
	// endless loop - it breaks only with a correct divider
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
	
	// if the number is 1, the player won and the bot lost
	if(number == 1)
	{
		printf("You win! ;-)\n");
		exit(0);
	}
	return number;
}

/**
 * Main function
 */
int main()
{
	int number = 0; // start number of the game, init with a random number between 10..50
	srand((unsigned)time(NULL));
	number = rand() % 50 + 10;			

 	printf("*** Aliquot Game ***\n");

	// play till number is 1
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
