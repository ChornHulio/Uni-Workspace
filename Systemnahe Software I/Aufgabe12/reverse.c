#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char *progname;
char *word = NULL;
int xflag = 0;
int lflag = 0;
int cflag = 0;
int sflag = 0;

void
usage(void)
{
	fprintf(stderr, "Usage: %s [-c WORD [-x]] [FILE...]\n", progname);
	exit(1);
}

char *
readlongline(FILE *fp)
{
	static char *bufp;
	static int size = 8;  /* a realistic value would be 80+2 (LF & NUL) */
	char *cp;
	int offset = 0;

	if (!bufp) {
		if (!(bufp = malloc(size))) {
			fprintf(stderr, "Out of mem\n");
			exit(1);
		}
	}
	*bufp = '\0';
	cp = bufp;
	while (fgets(cp, size-offset, fp)) {
		cp += strlen(cp) - 1;
		if (*cp == '\n') {
			*cp = '\0';
			return bufp;
		}
		size *= 2;
		if (!(bufp = realloc(bufp, size))) {
			fprintf(stderr, "Out of mem\n");
			exit(1);
		}
		offset = strlen(bufp);
		cp = bufp + offset;
	}
	return (*bufp) ? bufp : NULL;  /* possibly incomplete last line */
}

//Funktion für das Wort umdrehen
void
rev_line(char *line)
{
	char *cp = line;
	char *ep = line + strlen(line) - 1;
	char tmp;

	while (cp < ep) {
		tmp = *cp; *cp = *ep; *ep = tmp;
		cp++; ep--;
	}
}

//Funktion zum entscheiden ob Wort umgedreht werden muss oder nicht
void
processfile(FILE *fp)
{
	char *line;

	while ((line = readlongline(fp))) {
		if (!word || (xflag != (strstr(line, word)!=NULL))) {
			rev_line(line);						//Wort wird umgedreht wenn Buchtaben vom Parameter s übereinstimmen
		}
		printf("%s\n", line);					
	}
}


//Funktion findet heraus wieviele Zeilen die jeweilige Datei hat. Funktion wird gebraucht um später weiter arbeiten zu können
int zeilenanzahl(FILE *fp)
{
	static char *bufp;
	static int size = 8;  /* a realistic value would be 80+2 (LF & NUL) */
	char *cp;
	int offset = 0;
	int zaehler = 0;

	if (!bufp) {
		if (!(bufp = malloc(size))) {
			fprintf(stderr, "Out of mem\n");
			exit(1);
		}
	}
	*bufp = '\0';
	cp = bufp;
	while (fgets(cp, size-offset, fp)) {
		cp += strlen(cp) - 1;
		if (*cp == '\n') {				//Variable wird jeweis nach einem '\n'(Ende der Zeile) addiert
			zaehler ++;				
		}
		size *= 2;
		if (!(bufp = realloc(bufp, size))) {
			fprintf(stderr, "Out of mem\n");
			exit(1);
		}
		offset = strlen(bufp);
		cp = bufp + offset;
	}
	return zaehler+1;			//Abzahl der Zeilen der Datei wird zurückgeben
}

//Funktion zum entscheiden ob line umgedreht werden muss oder nicht. Paramter groesse ist die Line die gerade behandelt wird.
void
processfile_FLAG_L(FILE *fp, int groesse)
{
	char *line;
	int i = 0;

	while ((line = readlongline(fp))) {
		i++;
		if(i == groesse)
		{
			if (!word || (xflag != (strstr(line, word)!=NULL))) {
				rev_line(line);
			}
			printf("%s\n", line);
		}
	}
}

//Funktion test ob die Buchstaben vom Paramter s mit einem Wort der zeile übereinstimmten. return 1 = ja , return 0 = nein
int test_vergleich(FILE * fp)
{
	char *line;
	while ((line = readlongline(fp))) {
		
			if (!word || (xflag != (strstr(line, word)!=NULL))) {
				return 1;
			}
	}
return 0;
}


//Funktion zum entscheiden ob line und Wort umgedreht werden muss oder nicht. Paramter groesse ist die Line die gerade behandelt wird.
void
processfile_FLAG_L_C(FILE *fp, int groesse)
{
	char *line;
	int i = 0;

	while ((line = readlongline(fp))) {
		i++;
		if(i == groesse)
		{
			/*rev_line(line);
			printf("%s\n", line);*/
			if(sflag == 1 || xflag == 1)
			{
				if (!word || (xflag != (strstr(line, word)!=NULL))) {
				rev_line(line);
				}
				printf("%s\n", line);
			}
			else
			{
				rev_line(line);
				printf("%s\n", line);
			}
		}
	}
}


//Hauptprogramm
int
main(int argc, char *argv[])
{
	char *cp;
	FILE *fp;
	int zaehler;

	progname = argv[0];
	argc--; argv++;  /* remove program name */

	for (; *argv && **argv=='-'; argc--,argv++) {
		cp = *argv + 1;
		if (!*cp) {
			/* ``-'' is no option but use stdin as first file */
			break;
		}
		switch (*cp) {
		case 'x':
			if (cp[1]) {
				fprintf(stderr, "Unknown option `-%s'\n", cp);
				usage();
			}
			xflag = 1;
			break;
		case 's':
			if (cp[1]) {
				word = cp + 1;
			} else if (*++argv) {
				word = *argv;
				argc--;
			} else {
				usage();
			}
			sflag = 1;
			break;
		case 'l':
			if (cp[1]) {
				word = cp + 1;
			} else if (*argv) {
				word = *argv;
				argc--;
			} else {
				usage();
			}
			lflag = 1;
			break;
		case 'c':
			cflag = 1;
			break;
		default:
			usage();
		}
	}

	if (!*argv) {
		processfile(stdin);
		return 0;
	}
	for (; *argv; argv++,argc--) {
		if (strcmp(*argv, "-") == 0) {
			processfile(stdin);
		} else {
			if (!(fp = fopen(*argv, "r"))) {
				fprintf(stderr,"Could not open `%s'\n", *argv);
				continue;
			}
			
	
			if(lflag == 1 && cflag == 1)					//l flag und c flag sind gesetzt
			{
				if(sflag == 1 && test_vergleich(fp) != 1)		
				{
					fp = fopen(*argv, "r");
					processfile(fp);
				}
				else
				{
					if(xflag != 1)
					{
						fp = fopen(*argv, "r");
						zaehler = zeilenanzahl(fp);
						while(zaehler != 0)			//Zeilen werden umgedreht. Höhere Zeile wird vor Niedrigerre Zeile ausgeben -> umdrehen (zaehler --)
						{
							if (!(fp = fopen(*argv, "r"))) {
							fprintf(stderr,"Could not open `%s'\n", *argv);
							continue;
							}
							processfile_FLAG_L_C(fp, zaehler);
							zaehler--;
						}
					}else
					{
						fp = fopen(*argv, "r");
						processfile(fp);
					}
				}
			}else if(lflag == 1)						// nur l flag ist gesetzt
			{
				if(sflag == 1 && test_vergleich(fp) != 1)
				{
					fp = fopen(*argv, "r");
					processfile(fp);
				}
				else
				{
					fp = fopen(*argv, "r");
					zaehler = zeilenanzahl(fp);						//#########
					while(zaehler != 0)
					{
						if (!(fp = fopen(*argv, "r"))) {
						fprintf(stderr,"Could not open `%s'\n", *argv);
						continue;
						}
						processfile_FLAG_L(fp, zaehler);
						zaehler--;
					}
				}
			
			}
			else
			{
				processfile(fp);
			}
			fclose(fp);
		}
	}

	return 0;
}
