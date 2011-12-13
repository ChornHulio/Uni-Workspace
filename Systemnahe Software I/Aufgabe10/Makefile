.PHONY:	clean all
all:	testit
my_system.o:	my_system.c my_system.h
	gcc -g -Wall -std=gnu99 -c my_system.c
my_alloc.o:	my_alloc.c my_alloc.h my_system.h
	gcc -g -Wall -std=gnu99 -c my_alloc.c
testit.o:	my_system.h my_alloc.h testit.c
	gcc -g -Wall -std=gnu99 -c testit.c 
testit:	my_system.o testit.o my_alloc.o
	gcc -g -otestit my_system.o testit.o my_alloc.o
clean:
	rm -f *.o core a.out testit
