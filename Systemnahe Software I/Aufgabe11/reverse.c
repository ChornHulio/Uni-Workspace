// Autor: Heiko G.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* funktion checkt, welche Parameter angeben wurden. Und setzt jeweils Parameter opt_c und opt_x auf true*/
void check_options(int argc, char *argv[], int * opt_c, int* opt_x)
{
	int i;
	char *s;
	for(i = 1; i < argc; i++)
	{
		//Check for arguments
		for(s= argv[i]; *s != '\0';s++)
		{
			//check for options
			switch(*s)
			{
				case '-': s++; switch(*s)
						{
							case 'c': *opt_c = 1; break;					//parameter c is there
							case 'x': *opt_x = 1; break;					//parameter x is there
						}
			}
		}
	}
}

/*Gibt die umgedrehten Zeilen einer Datei aus, wenn keine Parameter angeben wurden*/
void print_argument_no_option(int argc,char *argv[])
{

	int ZeichenAnzahl = 0;
	int ZeichenAnzahl_tmp = 0;
	int i = 0;
	char* string_out;
	char c;
	FILE *fp; // File-Pointer definieren
	if((fp = fopen( argv[1], "r" )) == NULL)
	{
		printf("Fehler beim öffnen der Datei!\n");
		return;
	}
	while("")
	{
		ZeichenAnzahl++;
		c =fgetc(fp);

		//End of data
		if(c == EOF)
		{
			fclose(fp);
			fp = fopen( argv[1], "r" );
			printf("\n");
			string_out = (char*) malloc(sizeof(char*)*ZeichenAnzahl);
			for(i = 0; i < ZeichenAnzahl; i++)
			{
				if(ZeichenAnzahl_tmp <= i)
				{
					string_out[i-ZeichenAnzahl_tmp]=fgetc(fp);
				}else
				{
					fgetc(fp);
				}
			}
			for(i = ZeichenAnzahl-ZeichenAnzahl_tmp-1; i > 0; i--)
			{
					printf("%c",string_out[i-1]);
			}
			ZeichenAnzahl_tmp = ZeichenAnzahl;
			printf("\n");
			break;
		}

		//Zeilen ende
		if(c == 10)
		{
			fclose(fp);
			fp = fopen( argv[1], "r" );

			string_out = (char*) malloc(sizeof(char*)*ZeichenAnzahl);
			for(i = 0; i < ZeichenAnzahl; i++)
			{
				if(ZeichenAnzahl_tmp <= i)
				{
					string_out[i-ZeichenAnzahl_tmp]=fgetc(fp);
				}else
				{
					fgetc(fp);
				}
			}
			for(i = ZeichenAnzahl-ZeichenAnzahl_tmp; i > 0; i--)
			{
					printf("%c",string_out[i-1]);
			}
			ZeichenAnzahl_tmp = ZeichenAnzahl;
		}
	}
	fclose(fp);
}

/*Gibt die Zeilen einer Datei umgekehrt aus, wenn der Parameter c angeben wurde und der Teil String "search_string
in der Zeile vorhanden ist. Parameter position ist dafür da um zu wissen, ob der Teilstring direkt am Parameter dranhängt(-cXX)
oder nicht dranhängt (-c XX).*/
void print_argument_option_c(int argc,char *argv[],char*  search_string,int position)
{
	int ZeichenAnzahl = 0;
	int ZeichenAnzahl_tmp = 0;
	int i = 0;
	char* string_out;
	char c;
	FILE *fp; // File-Pointer definieren
	if((fp = fopen( argv[position], "r" )) == NULL)
	{
		printf("Fehler beim öffnen der Datei!\n");
		return;
	}
	while("")
	{
		ZeichenAnzahl++;
		c =fgetc(fp);

		//End of data
		if(c == EOF)
		{
			fclose(fp);
			fp = fopen( argv[position], "r" );
			printf("\n");
			string_out = (char*) malloc(sizeof(char*)*ZeichenAnzahl);
			for(i = 0; i < ZeichenAnzahl; i++)
			{
				if(ZeichenAnzahl_tmp <= i)
				{
					string_out[i-ZeichenAnzahl_tmp]=fgetc(fp);
				}else
				{
					fgetc(fp);
				}
			}
			//abchecken ob der string in der Zeile enthalten ist. Wenn ja umdrehen
			if(strstr(string_out,search_string)) 
			{
				for(i = ZeichenAnzahl-ZeichenAnzahl_tmp; i > 0; i--)
				{
					printf("%c",string_out[i-1]);
				}
				printf("\n");
			}else
			{
				for(i = 0; i < ZeichenAnzahl-ZeichenAnzahl_tmp; i++)
				{
					printf("%c",string_out[i]);
				}
			}
			ZeichenAnzahl_tmp = ZeichenAnzahl;
			break;
		}

		//Zeilen ende
		if(c == 10)
		{
			fclose(fp);
			fp = fopen( argv[position], "r" );

			string_out = (char*) malloc(sizeof(char*)*ZeichenAnzahl);
			for(i = 0; i < ZeichenAnzahl; i++)
			{
				if(ZeichenAnzahl_tmp <= i)
				{
					string_out[i-ZeichenAnzahl_tmp]=fgetc(fp);
				}else
				{
					fgetc(fp);
				}
			}
			//abchecken ob der string in der Zeile enthalten ist. Wenn ja umdrehen
			if(strstr(string_out,search_string)) 
			{
				for(i = ZeichenAnzahl-ZeichenAnzahl_tmp; i > 0; i--)
				{
					printf("%c",string_out[i-1]);
				}
			}
			else
			{
				for(i = 0; i < ZeichenAnzahl-ZeichenAnzahl_tmp; i++)
				{
					printf("%c",string_out[i]);
				}
			}
			ZeichenAnzahl_tmp = ZeichenAnzahl;
		}
	}
	fclose(fp);
}

/*Gibt die Zeilen einer Datei umgekehrt aus, wenn der Parameter c und x angeben wurden und der Teil String "search_string
in der Zeile nicht vorhanden ist. Parameter position ist dafür da um zu wissen, ob der Teilstring direkt am Parameter dranhängt(-cXX)
oder nicht dranhängt (-c XX).*/
void print_argument_option_c_and_x(int argc,char *argv[],char*  search_string, int position)
{
	int ZeichenAnzahl = 0;
	int ZeichenAnzahl_tmp = 0;
	int i = 0;
	char* string_out;
	char c;
	FILE *fp; // File-Pointer definieren
	if((fp = fopen( argv[position], "r" )) == NULL)
	{
		printf("Fehler beim öffnen der Datei!\n");
		return;
	}
	while("")
	{
		ZeichenAnzahl++;
		c =fgetc(fp);

		//End of data
		if(c == EOF)
		{
			fclose(fp);
			fp = fopen( argv[position], "r" );
			printf("\n");
			string_out = (char*) malloc(sizeof(char*)*ZeichenAnzahl);
			for(i = 0; i < ZeichenAnzahl; i++)
			{
				if(ZeichenAnzahl_tmp <= i)
				{
					string_out[i-ZeichenAnzahl_tmp]=fgetc(fp);
				}else
				{
					fgetc(fp);
				}
			}
			//abchecken ob der string in der Zeile enthalten ist. Wenn ja dann nicht umdrehen
			if((strstr(string_out,search_string))==NULL) 
			{
				for(i = ZeichenAnzahl-ZeichenAnzahl_tmp; i > 0; i--)
				{
					printf("%c",string_out[i-1]);
				}
				printf("\n");
			}else
			{
				printf("\n");
				for(i = 0; i < ZeichenAnzahl-ZeichenAnzahl_tmp; i++)
				{
					printf("%c",string_out[i]);
				}
			}
			ZeichenAnzahl_tmp = ZeichenAnzahl;
			break;
		}

		//Zeilen ende
		if(c == 10)
		{
			fclose(fp);
			fp = fopen( argv[position], "r" );

			string_out = (char*) malloc(sizeof(char*)*ZeichenAnzahl);
			for(i = 0; i < ZeichenAnzahl; i++)
			{
				if(ZeichenAnzahl_tmp <= i)
				{
					string_out[i-ZeichenAnzahl_tmp]=fgetc(fp);
				}else
				{
					fgetc(fp);
				}
			}
			//abchecken ob der string in der Zeile enthalten ist. Wenn ja dann nichtumdrehen
			if((strstr(string_out,search_string))==NULL) 
			{
				for(i = ZeichenAnzahl-ZeichenAnzahl_tmp; i > 0; i--)
				{
					printf("%c",string_out[i-1]);
				}
			}
			else
			{
				printf("\n");
				for(i = 0; i < ZeichenAnzahl-ZeichenAnzahl_tmp; i++)
				{
					printf("%c",string_out[i]);
				}
			}
			ZeichenAnzahl_tmp = ZeichenAnzahl;
		}
	}
	fclose(fp);
}

int main (int argc, char * argv[])
{
	char *s;
	char *mixed_string;
	int i;
	int groesse = 0;
	int opt_c = 0;													//Option c default == false
	int opt_x = 0;													//Option x default == false

	//check if arguments or Input  are there
	if(argv[1] == '\0')
	{
		printf("No arguments and no Input!\n");
		return 0;
	}
	
	//check if an option is seat
	check_options(argc,argv,&opt_c,&opt_x);

	
	if(opt_c == 0 && opt_x == 0)//no arguments
	{
		print_argument_no_option(argc,argv);
		return 0 ;
	}
	else if(opt_c == 1 && opt_x == 1)//argument x ist gesetzt
	{
		i=0;
		//abchecken ob parameter in dieser Form eingeben wurden -cXX...
		for(s= argv[1]; *s != '\0';s++)
		{
			groesse++;
		}
		//TODO
		if(groesse > 2)		//parameter mit string vermischt!!
		{
			mixed_string = (char*)malloc(sizeof(char*)*groesse-1);
			
			for(s= argv[1]; *s != '\0';s++)
			{	
				//string von parameter trennen
				if(i >1)
				{
					mixed_string[i-2] = *s;
				}
				i++;
			}
				mixed_string[i-2] = '\0';
			print_argument_option_c_and_x(argc,argv,mixed_string,3);
			return 0;
		}
		else
		{

			print_argument_option_c_and_x(argc,argv,argv[2],4);
			return 0;
		}
	}
	else if(opt_c == 1)//argument c ist gesetzt
	{
		i=0;
		//abchecken ob parameter in dieser Form eingeben wurden -cXX...
		for(s= argv[1]; *s != '\0';s++)
		{
			groesse++;
		}
		//TODO
		if(groesse > 2)		//parameter mit string vermischt!!
		{
			mixed_string = (char*)malloc(sizeof(char*)*groesse-2);
			for(s= argv[1]; *s != '\0';s++)
			{	
				//string von parameter trennen
				if(i >1)
				{
					mixed_string[i-2] = *s;
				}
				i++;
			}
		
			mixed_string[i-2] = '\0';
			print_argument_option_c(argc,argv,mixed_string,2);
			return 0;
		}
		else
		{
			print_argument_option_c(argc,argv,argv[2],3);
			return 0;
		}
	}

	return 0;
}
