/**
 * @author Heiko Golavsek
 * @date 2011-11-22
 */

#include <stdio.h>
#include <string.h> 
#include <stdlib.h>
#include <time.h>
#include <ctype.h>

#define DATA "Ulm.hm"
#define BUFLEN 2147483647


/*
read random line from textual and write it into the buffer.
In this function is the reservoir algorithm 
*/
void reservoir(char* buffer)
{
	int i;
	int lines = 448; //number of lines in textual
	int j = 0;
	char tmpBuffer[350];
	

	//open file
	FILE* fp = fopen(DATA,"r");

	if(fp == NULL){
		printf("Opening Error from %s\n",DATA);
		buffer[0] = -1;
		return;
	}

	//reservoir algorithm
	if(fgets(buffer,BUFLEN,fp)== NULL)
	{
		printf("Reading Error from %s\n",DATA);
		buffer[0] = -1;
		return;
	}
	
	for (i = 1; i < lines; i++)
	{
		srand((unsigned)time(NULL) + i);
		j = rand() % lines + 1;
		if(j <= 1) {
			if(fgets(buffer,BUFLEN,fp)== NULL)
			{
				printf("Reading Error from %s\n",DATA);
				buffer[0] = -1;
				return;
			}
		} else {
			if(fgets(tmpBuffer,BUFLEN,fp)== NULL) // similar to /dev/null
			{
				printf("Reading Error from %s\n",DATA);
				buffer[0] = -1;
				return;
			}
		}
	}
	
	//close file
	fclose(fp);
}


/*print the game to the console
Input Parameter: person_laenge = the length of the person's name
				 buffer = the searcher person
				 versuche = the trails (mistakes)
				 input_buchstaben = the whole input letters from the player
*/
void print_output(int personen_laenge, char* buffer, int versuche,char* input_buchstaben)
{
	int i;
	printf("%d\t",versuche);
	for(i=0;i<26; i++)
	{
		printf("%c",input_buchstaben[i]);
	}
	for(i=0;i<personen_laenge; i++)
	{
		printf("%c",buffer[i]);
	}
	printf("\n");
}

/*
The Hangman function, ist the main function who looks if the letter is in the word or not. This function decide if you have won or not.
Input Parameter: person_laenge = the length of the person's name
				 buffer = the searcher person
				 versuche = the trails (mistakes)
				 input_buchstaben = the whole input letters from the player
*/
void hangman(int personen_laenge, char*buffer,char* outputbuffer,char* input_buchstaben)
{
	int versuche = 7;
	int richtig = 1;					//temp variable, if the letter is existing in the word or not. 0 yes, 1 no
	char buchstabe;
	int i=0;
	int buchstabe_vorhanden = 1;		//it check if you have input the letter before. 1 = yes 0 = no
	int ready = 0;						//checkt if the playder win the match

	//initialize the input letter array
	for(i=0; i<26;i++)
	{
		input_buchstaben[i]= ' ';
	}

	while("")
	{
		buchstabe = (char)getchar();			//User input
	
		if((int)buchstabe != 10)				//checkt if the input letter are empty
		{
			//Save the letter in the array if they are not enter before
			for(i = 0 ; i <26; i++)
			{
				if(input_buchstaben[i] == (char) tolower((int) buchstabe))				//letter was enter before
				{
					i=26;
					buchstabe_vorhanden = 0;
				}
				else if(input_buchstaben[i] != (char) tolower((int) buchstabe) && input_buchstaben[i] == ' ')	//put the letter into the array
				{
					input_buchstaben[i] = (char) tolower((int) buchstabe);
					i = 26;
					buchstabe_vorhanden = 0;
				}
			}
			if(buchstabe_vorhanden == 0)
			{
				for(i = 0; i < personen_laenge; i++)
				{
					if(buffer[i] == buchstabe)
					{
						outputbuffer[i] = buchstabe;
						richtig =0;							//the input letter is right
						ready++;							//if ready the same as the length of the searched word. The Player have win wenn
					}
					//checke if the upper letter is in the searchd word
					if(buffer[i] == (char)toupper((int)buchstabe))
					{
						outputbuffer[i] =(char)toupper((int)buchstabe);
						richtig =0;							//the input letter is right
						ready++;							//if ready the same as the length of the searched word. The Player have win wennn
					}
				}
			}

			if(ready >= personen_laenge-1)					//if ready same as the length of the searched word, the player win.
			{
				print_output(personen_laenge,buffer,versuche,input_buchstaben);
				printf("Congratulations!\n");
				break;
			}

			if(richtig != 0)
			{
				versuche--;										//the player have seven trials. Every time if the letter is incorrect. This variable will subtract about one. If the variable versuch == 0 the player have lost
				if(versuche == 0)
				{
					printf("YOU LOSE!\nSearched person: ");
					for(i = 0; i < personen_laenge;i++)
					{
						printf("%c",buffer[i]);
					}
					printf("\n");
					break;
				}
			}

			print_output(personen_laenge,outputbuffer,versuche,input_buchstaben);	//Hangmanoutput

			buchstabe_vorhanden = 1;												//default value
			richtig = 1;															//default value
		}
	}
}

/*
writes the inital game to the console.  
*/
void init_output(int personen_laenge, char* buffer, char* outputbuffer)
{
	int i;

	printf("\n7\t\t\t\t  ");
	for(i = 0; i< personen_laenge; i++)
	{
		if(buffer[i] != ' ')
		{
			outputbuffer[i]='_';
			printf("_");
		}
		else
		{
			outputbuffer[i]=' ';
			printf(" ");
		}
	}
	printf("\n");
}


/*
MAIN PROGRAMM
*/

int main(void)
{
	//variables
	int i;
	int y = 0;
	char buffer[350];
	char person[100];
	char outputbuffer[100];
	char input_buchstaben[26];
	int ratehilfe_laenge = 0;
	int person_laenge = 0;
	int ratehilfe_ready = 0;
	
	//initial buffers
	for(i =0; i < 100; i++)
	{
		buffer[i]=0;
		person[i]=0;
		outputbuffer[i]=0;
	}
	
	reservoir(buffer);
	//If is an error till opening or writen of data ->breakup
	if(buffer[0] == -1)
	{
		printf("Programm breakup!\n");
		return 0;
	}


	//cut the input line from the textual into the searched person and description
	for(i = 0; i < 350; i++)
	{
		if((int)buffer[i] != 9 && ratehilfe_ready == 0)		//description 
		{
			printf("%c",buffer[i]);							//give the description to the console
			ratehilfe_laenge = i;
		}
		else if((int)buffer[i] != 10)						//searched person
		{
			if((int)buffer[i] == 9)
			{
				ratehilfe_ready = 1;
				i++;
			}
			person[y] = buffer[i];							//copie the searched person into the person buffer
			y++;
			person_laenge = i-ratehilfe_laenge-1;
		}else
		{
			i = 350;
		}
	}

	//Start the game hangman
	init_output(person_laenge,person,outputbuffer);
	hangman(person_laenge,person,outputbuffer,input_buchstaben);
	
	return 0;
}
