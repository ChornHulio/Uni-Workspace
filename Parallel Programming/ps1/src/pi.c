#include <stdio.h>
#include <stdlib.h>
#include <mpi.h>

#define ITER 1000

int main(int argc, char **argv) {
	int rank, size;
	int i;
	MPI_Status status;

	double local_result;
	double result = 0;

	/* Insert appropriate code for MPI initialization here, as well as:
	   - obtaining the rank (process ID) which should be put in the variable
	     rank
	   - obtaining the number of processes, which should be put in the
	     variable size.
	*/
	MPI_Init(&argc,&argv);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);


	/* Each program instance calculates part of the approximation of pi
	   using the formula pi/4 = 1 - 1/3 + 1/5 - 1/7 + 1/9 - ... */
	/* Rank 0 calculates the elements 0 - (ITER-1) of the sum,
	   rank 1 calculates the elements ITER - (2*ITER-1) of the sum etc. */	   
	local_result = 0;
	for(i=rank*ITER; i<rank*ITER+ITER; i++) {
		if(i & 1)
			local_result -= 1.0 / (i*2+1);
		else
			local_result += 1.0 / (i*2+1);
	}

	/* Insert your own code here. Here you should use MPI functions
	   (send and receive) to collect the result in the process with
	   rank 0. */
	MPI_Send(&local_result, 1, MPI_DOUBLE, 0, 99, MPI_COMM_WORLD);
	
	if(rank == 0) {
		for(i=0; i<size; i++) {
			MPI_Recv(&local_result, 1, MPI_DOUBLE, i, 99, MPI_COMM_WORLD, &status);		
			result += local_result;
		}
	}

	if(rank==0){
		printf("pi is approximately equal to %f\n", 4 * result);
  }

	/*	Insert appropriate code here for de-initializing MPI */
	MPI_Finalize();

	return 0;
}
