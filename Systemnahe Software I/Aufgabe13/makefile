a.out:	bot_karl.o human.o four.o
	gcc bot_karl.o human.o four.o -o four
bot_karl.o:	bot_karl.h bot_karl.c
	gcc -c -Wall -std=c99 bot_karl.c
human.o:	human.h four.h human.c
	gcc -c -Wall -std=c99 human.c
four.o:	four.h human.h bot_karl.h four.c
	gcc -c -Wall -std=c99 four.c

