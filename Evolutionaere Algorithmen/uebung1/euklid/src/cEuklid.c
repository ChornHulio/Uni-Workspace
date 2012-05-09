#include "math.h"

void cEuklid(int *a, int *b, int *na, int *erg) {
	double sum = 0.0;
	for(int i = 0; i < *na; i++) {
		sum += pow((a[i] - b[i]),2);
	}
	*erg = sqrt(sum);
}
