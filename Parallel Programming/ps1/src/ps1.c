#include <stdio.h>
#include <stdlib.h>
#include <time.h>

typedef struct {
	int real;
	int imag;
}complex_t;

complex_t multiply_complex(complex_t a, complex_t b){
  complex_t c;
  c.real = a.real*b.real - a.imag*b.imag;
  c.imag = a.imag*b.real + a.real*b.imag;
  return c;
}

void divide_complex(complex_t a, complex_t b, complex_t* r){
  r->real = (a.real*b.real + a.imag*b.imag) / (b.real*b.real + b.imag*b.imag);
  r->imag = (a.imag*b.real - a.real*b.imag) / (b.real*b.real + b.imag*b.imag);
}

complex_t* create_random_complex_array(int size){
	int i;
	srand(time(NULL));
  complex_t* a = malloc(size*sizeof(complex_t));
  if(a == NULL) {
  	return NULL;
  }
  for(i = 0; i < size; i++) {
  	a[i].real = rand() % 1999 - 999;
  	a[i].imag = rand() % 1999 - 999;
  }
  return a;
}

complex_t* multiply_complex_arrays(complex_t* a1, complex_t* a2, int size){
  int i;
  complex_t* a = create_random_complex_array(size);
  for(i = 0; i < size; i++) {
  	a[i] = multiply_complex(a1[i], a2[i]);
  }
  return a;
}

complex_t* divide_complex_arrays(complex_t* a1, complex_t* a2, int size){
  int i;
  complex_t* a = create_random_complex_array(size);
  for(i = 0; i < size; i++) {
  	divide_complex(a1[i], a2[i], &(a[i]));
  }
  return a;
}


int main(int argc, char** argv){

  //Creating two arrays
  complex_t* a = create_random_complex_array(100);
  complex_t* b = create_random_complex_array(100);

  //Multiplying a and b
  complex_t* c = multiply_complex_arrays(a,b,100);
  //Dividing c by b, d and a should now be equal
  complex_t* d = divide_complex_arrays(c,b,100);
  
  //Checking that a and d are equal
  for(int i = 0; i < 100; i++){
    if(d[i].real != a[i].real || d[i].imag != a[i].imag){
      printf("Error at: %d\n", i);
    }
  }
  
  free(a);
  free(b);
  free(c);
  free(d);
}
