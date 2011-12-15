/**
 * Systemnahe Software
 * Aufgabenblatt 7
 * Bearbeiter: Heiko Golavsek, Andra Herta, Tobias Dreher
 */

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include "my_alloc.h"
#include "my_system.h"

#define WASTE_RATE 0.03

unsigned int mem_size = 0;
unsigned int used_mem = 0;
unsigned char found_something = 0;
char * data_start;
char * data_current;
char * data_last;
int offset = 0;
int blocks = 1;
int current_block = 1;

void init_my_alloc ()
{
	data_start = get_block_from_system();
	mem_size = BLOCKSIZE;
	// mark the memory as free
	*(data_start) = 0; // unused
	//*(data_start+1) = (short) (BLOCKSIZE - 16); // the next 8176 bytes are unused
	*(data_start+1) = (BLOCKSIZE - 16) / 0xFF; // ...
	*(data_start+2) = (BLOCKSIZE - 16) % 0xFF; // the next 8176 bytes are unused
	data_current = data_start;
	data_last = data_start;
}

void * my_alloc (int size)
{
	do
	{
		while (offset >= 0 && offset < (BLOCKSIZE - 8)) 
		{
			char status = (char) * (data_current + offset);
			unsigned short bytes = (unsigned char)*(data_current + offset + 1) * 0xFF + (unsigned char)*(data_current + offset + 2);
			if(status == 0 && bytes > size)  // block is unused and big enough
			{
				found_something = 1;
				used_mem += size;
				*(data_current + offset) = 1; // used
				*(data_current + offset + 1) = size / 0xFF; // ...
				*(data_current + offset + 2) = size % 0xFF; // the next 'size' bytes are used
				if(bytes != size) 
				{
					*(data_current + offset + 8 + size) = 0; // used
					*(data_current + offset + 9 + size) = (bytes - size) / 0xFF; // ...
					*(data_current + offset +10 + size) = (bytes - size) % 0xFF; // the next 'x' bytes are unsed
				}
				void * ret = data_current + offset + 8;
				offset += 8 + size;
				return ret;
			}
			else // block is used or not big enough
			{
				offset += 8 + bytes;
				printf("bytes: %u\n",bytes);
			}
		}
		if(blocks <= current_block)
		{
			data_current = data_start; // go back to the beginning
			found_something = 0;
			current_block = 0;
		}
		else
		{
			data_current = (void*) data_current + (BLOCKSIZE - 8);
			current_block++;
		}
	} while(found_something == 1 && (used_mem / mem_size) < WASTE_RATE);
	
	// no block of the given size found
	found_something = 0;
	offset = 0;
	blocks++;
	current_block++;
	data_current = get_block_from_system();
	*(data_last + (BLOCKSIZE - 8)) = data_current; //BUG
	mem_size += BLOCKSIZE;
	// mark the memory as free
	*(data_current) = 0; // unused
	*(data_current+1) = (BLOCKSIZE - 16) / 0xFF; // ...
	*(data_current+2) = (BLOCKSIZE - 16) % 0xFF; // the next 8176 bytes are unused
	return my_alloc(size);
}

void my_free (void * ptr)
{
	char * cptr = ptr - 8 * sizeof(char);
	unsigned short bytes = (unsigned char)*(cptr + 1) * 0xFF + (unsigned char)*(cptr + 2);
	*(cptr) = 0; // unused
	
	/* BUG: next_bytes can be the end of a block
	char status = (char)*(cptr + 8 + bytes);
	short next_bytes = (unsigned char)*(cptr + bytes + 9) * 0xFF + (unsigned char)*(cptr + bytes + 10);
	
	if(status == 0 && next_bytes > 0)
	{
		*(cptr + 1) = (bytes + next_bytes) / 0xFF; // ...
		*(cptr + 2) = (bytes + next_bytes) % 0xFF; // next x bytes are unused
	}
	*/
}
