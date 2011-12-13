#ifndef MY_SYSTEM_H
#define MY_SYSTEM_H

#define BLOCKSIZE	8192

/* Get a 1024-Byte aligned Block of Memory from the System. The return
 * value is 0 if no more memory is availiable. Otherwise it points to
 * the newly allocated block of memory.
 */
void * get_block_from_system();

/*
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/* Internal Functions and data structures for the tester. */
int get_sys_blockcount ();
int valid_area (unsigned long start, unsigned long len);

struct avl_node {
	struct avl_node * next, * prev;
	struct avl_node * left, * right;
	struct avl_node * parent;
	unsigned long start, len;
	int height;
};

struct avl_node * create_avl (void);
struct avl_node * find_avl (struct avl_node * root, unsigned long start);
void insert_avl (struct avl_node ** root, unsigned long start, unsigned long len);
void remove_avl (struct avl_node ** root, struct avl_node * node);
void __my_assert (char * text);
#define my_assert(B,T) do { if(!(B)) __my_assert(T); } while (0)

#endif
