/**SYSTEMNAHE SOFTWARE BLATT 6 AUFGABE 9******/

/**
 * @author Heiko Golavsek
 * @date 2011-11-29
 */

#include <string.h> 
#include <time.h>
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>

#define DURCHLAUF_MAX 33554432			//Maximale Feldgroesse	32MB  64MB = 67108864
#define START 1024						//Feld Startgroesse
#define DURCHLAUF 16				    //Durchlauf von Ringteilstücke
#define SCHRITT 524288					//Wachstumsschritt von Feld 0.5MB

void swap(int * input,int position, int rand_postion)
{
	int tmp;
	tmp = input[position]; 
	input[position] = input[rand_postion];
	input[rand_postion] = tmp;
}

void KnuthShuffle(int* pArr, int n)
{
	int i, random;

    for(i=n-1;i>0;i--)
    {
		srand ( time(NULL) +i );
		random = rand() % i;	
        swap(pArr,i,random);
    }
}
/*
MAIN PROGRAMM
*/

int main(void)
{

	int *pArr;
	int i,step;
	clock_t start,stop;			//timer Variablen, zu zeit stoppen der durchfuehrdauer
	FILE *datei;				//Datei zum plotten

	//struct zum verketten 
	struct ring {
		struct ring *next;		//pointer zum verketten
	};

	struct ring *ring_array;	//Ring
	struct ring* laufen;		//"Durchlauf Pointer" 

	datei = fopen ("daten.txt", "w");						//Datei anlegen bzw. oeffnen wenn vorhanden
	if (datei == NULL)
	{
		printf("Fehler beim oeffnen der Datei.");
		return 1;
	 }

	for(step=START;step<DURCHLAUF_MAX;step = step+ SCHRITT)
	{
		//Speicher variabel allocieren
		pArr = (int*)malloc(sizeof(int)*SCHRITT);							//Feldgroesse anlegen
		ring_array = (struct ring*)malloc(sizeof(struct ring)* SCHRITT);	//Ringgroesse anlegen
		laufen = &(ring_array[0]);											//"Durchlauf Pointer" an Stelle im Ring zeigen
		
		for(i =0; i < SCHRITT;i++)
		{
			pArr[i]=i;
		}

		//Knuth Shuffle Algorithm
		KnuthShuffle(pArr, SCHRITT);


		//Ring erstellen
		for(i=0; i< SCHRITT;i++)
		{
			if(i+1 == SCHRITT)
			{
				ring_array[pArr[i]].next = &(ring_array[pArr[0]]);
			}
			else
			{
				ring_array[pArr[i]].next = &(ring_array[pArr[i+1]]);
			}
		}

		start =clock();						//Zeitstoppen starten
		//Ring durchlaufen
		for(i = 0; i < DURCHLAUF*step; i++)
		{
			laufen = laufen->next;
		}
		stop = clock();						//Zeitstoppen ende

		//Zeit ausbeben
		printf("Feldgroesse: %i Zugriffszeit: %.3f\n",step,((float)(stop-start)/CLOCKS_PER_SEC));	
		fprintf (datei, "%i\t%.3f\n",step,((float)(stop-start)/CLOCKS_PER_SEC));					//schreiben in Datei

		//Speicher freigeben
		free(pArr);
		free(ring_array);
	}
	fclose (datei);				//plotdatei schliessen
	//printf("Daten wurden in Datei: daten.txt gespeichert\n");
	return 0;
}
