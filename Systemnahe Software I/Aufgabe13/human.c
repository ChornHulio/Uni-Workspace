#include <stdio.h>

#include "human.h"
#include "four.h"

void human(int spielstein)
{
	int spielsteinstelle = 0;
	int steinvorhanden = 0;
	int i = 0;

	while(steinvorhanden !=1)
	{
		if(spielstein == 1)						//X
		{
			while("")				//Spielstein kontrolle
			{
				printf("'X': ");
				scanf("%i",&spielsteinstelle);
				if(spielsteinstelle < 0 || spielsteinstelle >6)
				{
					printf("Falsche Spielstelle angeben! Wiederholen!\n");
				}else
				{
					break;
				}
			}

			for(i = 6; i>=0; i--)
			{
				if(spielfeld[i][spielsteinstelle]==0)
				{
					spielfeld[i][spielsteinstelle] = 1;
					steinvorhanden = 1;
					break;
				}
			}
			if(steinvorhanden != 1)
			{
				printf("Es existiert bereits ein Spielstein an dieser Stelle! Bitte zug wiederholen!\n");
			}
		}
		else					//O
		{
			
			while("")
			{
				printf("'O': ");
				scanf("%i",&spielsteinstelle);
				if(spielsteinstelle < 0 || spielsteinstelle >6)
				{
					printf("Falsche Spielstelle angeben! Wiederholen!\n");
				}else
				{
					break;
				}
			}

			for(i = 6; i>=0; i--)
			{
				if(spielfeld[i][spielsteinstelle]==0)
				{
					spielfeld[i][spielsteinstelle] = 2;
					steinvorhanden = 1;
					break;
				}
			}
			if(steinvorhanden != 1)
			{
				printf("Es existiert bereits ein Spielstein an dieser Stelle! Bitte zug wiederholen!\n");
			}
		}
	}
}