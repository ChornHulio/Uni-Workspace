#include <stdlib.h>
#include <assert.h>
#include "my_alloc.h"
#include "my_system.h"


int offset = 0;
char * data = NULL;

void init_my_alloc ()
{
}

void * my_alloc (int size)
{
	char * ret;
	if (!data || offset + size > BLOCKSIZE) {
		offset = 0;
		data = get_block_from_system();
	}
	ret = data + offset;
	offset += size;
	return ret;
}

void my_free (void * ptr)
{
}
