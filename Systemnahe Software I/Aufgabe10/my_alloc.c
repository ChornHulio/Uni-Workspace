#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include "my_alloc.h"
#include "my_system.h"

#define WASTE_RATE 0.1
#define MAX_BLOCKS 25

unsigned char offset_block = 0;
unsigned short offset_internal = 0;
unsigned int mem_size = 0;
unsigned int used_mem = 0;
unsigned char used_blocks = 1;
unsigned char found_something = 0;
unsigned char * datablocks[MAX_BLOCKS];

void init_my_alloc ()
{
	datablocks[0] = get_block_from_system();
	mem_size = BLOCKSIZE;
	// mark the memory as free
	for(int i = 0; i < BLOCKSIZE-100; i += 1017)
	{
		*(datablocks[0] + i) = 0b11111110;
	}
	*(datablocks[0] + 8136) = 0b01101110;
}

void * my_alloc (int size)
{
	do
	{
		do 
		{
			unsigned char status = (unsigned char) * (datablocks[offset_block] + offset_internal); // status information at the beginning of an internal block
			printf("status: %d\n",status);
			if(!(status % 2) && (status/2 > size/8)) // internal block is unused and big enough
			{
				found_something = 1;
				used_mem += size;
				unsigned char * new_status = datablocks[offset_block] + offset_internal;
				unsigned char * ret = new_status + 1;
				*new_status = size/4 + 1; // beginning of internal block
				*(ret + 1) = (status/2 - size/8) + 1; // end of internal block
				return ret;
			}
			else
			{
				offset_internal += 1 + ((status/2)*8);
			}
		} while (offset_internal <= (BLOCKSIZE + size));
		offset_block++; // next block
		offset_internal = 0; // at the beginning
	} while(offset_block <= used_blocks);
	
	// no internal block of this size found
	if(found_something == 0 || mem_size == 0 || (used_mem / mem_size) < WASTE_RATE)
	{
		if(used_blocks >= MAX_BLOCKS)
		{
			return NULL; // too much memory blocks are used
		}
		// get block from system
		found_something = 0;
		offset_internal = 0;
		offset_block = used_blocks;
		datablocks[used_blocks] = get_block_from_system();
		if(datablocks[used_blocks] == NULL)
		{
			return NULL; // get no memory
		}
		mem_size += BLOCKSIZE;
		// mark the memory as free
		for(int i = 0; i < BLOCKSIZE-100; i += 1017)
		{
			*(datablocks[used_blocks] + i) = 0b11111110;
		}
		*(datablocks[0] + 8136) = 0b01101110;
		used_blocks++;
		return my_alloc(size);
	}
	else // there should be an internal block for this size - start at the beginning
	{
		found_something = 0;
		offset_block = 0;
		offset_internal = 0;
		return my_alloc(size);
	}
}

void my_free (void * ptr)
{
}
