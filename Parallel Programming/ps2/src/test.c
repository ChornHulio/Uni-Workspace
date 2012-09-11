#include <stdlib.h>
#include <stdio.h>
#include <mpi.h>

#include "bmp.h"

#define ITERATIONS 1000  //Number of iterations
#define BORDER 1         //Border thickness

//Indexing macros
#define F(s,i,j) local_image[((s)%2)][((i)+BORDER)*(local_image_size[1]+2*BORDER) + ((j)+BORDER)]
#define G(i,j) local_image_orig[(i)*local_image_size[1] + (j)]

int rank,                       //My rank
    size,                       //Number of processes
    dims[2],                    //Dimensions of processor grid
    coords[2],                  //My coordinates in processor grid
    periods[2] = {0,0},         //No wrap around
    north,south,east,west,      //Neighbours in processor grid
    image_size[2] = {512,512},  //Image size. Change this if you use another image
    local_image_size[2];        //Size of local part of image

//Bluring filter
float filter[3][3] = {
    {0.111,0.111,0.111},
    {0.111,0.111,0.111},
    {0.111,0.111,0.111} };


float lambda = 0.02;

MPI_Comm cart_comm;

MPI_Datatype local_image_orig_t,
             image_t,
             border_row_t,
             border_col_t;


unsigned char *image,            //Glocal g
              *local_image_orig, //Local part of g
              *local_image[2];   //Local part of f, two buffers

void create_types(){
    MPI_Type_contiguous(local_image_size[0]*local_image_size[1],
            MPI_UNSIGNED_CHAR, &local_image_orig_t);
    MPI_Type_commit(&local_image_orig_t);

    MPI_Type_vector(local_image_size[0],
            local_image_size[1], image_size[1], MPI_UNSIGNED_CHAR, &image_t);
    MPI_Type_commit(&image_t);

    // MPI type for border exchange (row)
    MPI_Type_vector(BORDER,local_image_size[1],local_image_size[1]+2*BORDER,MPI_UNSIGNED_CHAR,&border_row_t);
    MPI_Type_commit(&border_row_t);
    
    // MPI type for border exchange (col)
    MPI_Type_vector(local_image_size[0],BORDER,local_image_size[1]+2*BORDER,MPI_UNSIGNED_CHAR,&border_col_t);
    MPI_Type_commit(&border_col_t);
}

void distribute_image(){

    MPI_Request req;
    MPI_Irecv(local_image_orig, 1, local_image_orig_t, 0, 1, cart_comm, &req);

    if(rank == 0){
        int co[2];
        for(int i = 0; i < size; i++){
            MPI_Cart_coords(cart_comm, i, 2, co);
            int index = co[0]*local_image_size[0]*image_size[1] + co[1]*local_image_size[1];
            MPI_Send(&image[index], 1, image_t, i, 1, cart_comm);
        }
    }

    MPI_Wait(&req, MPI_STATUS_IGNORE);
}

void initialilze_guess(){
    // copy local_image_orig (g) to local_image[0] (f0)
    for(int i = -BORDER; i < local_image_size[0]+BORDER; i++){
    	for(int j = -BORDER; j < local_image_size[1]+BORDER; j++){
    		if(i < 0 || j < 0 || i >= local_image_size[0] || j >= local_image_size[1]){
    			F(0,i,j) = 0;
    		} else {
    			F(0,i,j) = G(i,j);
    		}
    	}
    }
}

void exchange_borders(int step){
    MPI_Request req[4];

		// receive borders (non blocking to avoid dead locks)
    MPI_Irecv(&F(step,-BORDER,0), 1, border_row_t, north, 0, cart_comm, req);
    MPI_Irecv(&F(step,0,-BORDER), 1, border_col_t, west, 0, cart_comm, req+1);
    MPI_Irecv(&F(step,local_image_size[0],0), 1, border_row_t, south, 0, cart_comm, req+2);
    MPI_Irecv(&F(step,0,local_image_size[1]), 1, border_col_t, east, 0, cart_comm, req+3);

		// send own borders
    MPI_Send(&F(step,0,0), 1, border_row_t, north, 0, cart_comm);
    MPI_Send(&F(step,0,0), 1, border_col_t, west, 0, cart_comm);
    MPI_Send(&F(step,local_image_size[0]-BORDER,0), 1, border_row_t, south, 0, cart_comm);
    MPI_Send(&F(step,0,local_image_size[1]-BORDER), 1, border_col_t, east, 0, cart_comm);

		// wait until all borders are received
    MPI_Waitall(4, req, MPI_STATUSES_IGNORE);
    
    // set corners
    F(step,-1,-1) = 0;
    F(step,-1,local_image_size[1]) = 0;
    F(step,local_image_size[0],-1) = 0;
    F(step,local_image_size[0],local_image_size[1]) = 0;

}

void perform_convolution(int step){
    // go over (current) local image (with border)
    for(int i = 0; i < local_image_size[0]; i++){
    	for(int j = 0; j < local_image_size[1]; j++){
    		// go over filter
    		double value = 0.0; // result of filter	
    		for(int a = -1; a <= 1; a++){ // attention: works only for 3x3 filters
    			for(int b = -1; b <= 1; b++){
    				value += filter[a+1][b+1] * (double) (F(step,i+a,j+b));
    			}
    		}
    		// update pixel (cut if < 0 or > 255)
    		int newValue = F(step,i,j) + lambda * (G(i,j) - value);
    		
    		if(newValue < 0) {
    			F(step+1,i,j) = 0;
    		} else if(newValue > 255) {
    			F(step+1,i,j) = 255;
    		} else {
    			F(step+1,i,j) = newValue;
    		}
    	}
    }
}

void gather_image(){
		// MPI type for image gathering
		MPI_Datatype image_gathering_t;
    MPI_Type_vector(local_image_size[0],
            local_image_size[1], local_image_size[1]+2*BORDER, MPI_UNSIGNED_CHAR, &image_gathering_t);
    MPI_Type_commit(&image_gathering_t);
    
    MPI_Request req[size];

    // gather image data at rank 0
    if(rank == 0){
        // receive data from all ranks
        for(int i = 0; i < size; i++){        	
        	// calc offset of these data
        	int thisCoords[2];
        	MPI_Cart_coords(cart_comm, i, 2, thisCoords ); // coords of this rank
        	int offset = thisCoords[0] * local_image_size[0] * image_size[1] + thisCoords[1] * local_image_size[1];
        	
        	// receive data
        	MPI_Irecv(&image[offset], 1, image_t, i, 99, cart_comm, req+i);
        }
    }
    
  	// send image data to rank 0
  	MPI_Send(&F(ITERATIONS,0,0), 1, image_gathering_t, 0, 99, cart_comm);

		// wait until all borders are received
		if(rank == 0){
    	MPI_Waitall(size, req, MPI_STATUSES_IGNORE);
    }
}

int main_replaced(int argc, char** argv){

    //Initialization
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    //Reading image
    if(rank == 0){
        image = read_bmp("Lenna_blur.bmp");
    }

    //Creating cartesian communicator
    MPI_Dims_create(size, 2, dims);
    MPI_Cart_create( MPI_COMM_WORLD, 2, dims, periods, 0, &cart_comm );
    MPI_Cart_coords( cart_comm, rank, 2, coords );

    MPI_Cart_shift( cart_comm, 0, 1, &north, &south );
    MPI_Cart_shift( cart_comm, 1, 1, &west, &east );

    local_image_size[0] = image_size[0]/dims[0];
    local_image_size[1] = image_size[1]/dims[1];

    //Allocating buffers
    int lsize = local_image_size[0]*local_image_size[1];
    int lsize_border = (local_image_size[0] + 2*BORDER)*(local_image_size[1] + 2*BORDER);
    local_image_orig = (unsigned char*)malloc(sizeof(unsigned char)*lsize);
    local_image[0] = (unsigned char*)calloc(lsize_border, sizeof(unsigned char));
    local_image[1] = (unsigned char*)calloc(lsize_border, sizeof(unsigned char));

    create_types();

    distribute_image();

    initialilze_guess();

    //Main loop
    for(int i = 0; i < ITERATIONS; i++){
        exchange_borders(i);
        perform_convolution(i);
    }

    gather_image();

    MPI_Finalize();

    //Write image
    if(rank==0){
        write_bmp(image, image_size[0], image_size[1]);
    }

    exit(0);
}
int main(int argc, char** argv){

    //Initialization
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    //Initializing test pattern
    image_size[0] = 256;
    image_size[1] = 256;
    if(rank == 0){
        image = (unsigned char*)malloc(sizeof(unsigned char) * image_size[0] * image_size[1]);
    }



    //Creating cartesian communicator
    MPI_Dims_create(size, 2, dims);
    MPI_Cart_create( MPI_COMM_WORLD, 2, dims, periods, 0, &cart_comm );
    MPI_Cart_coords( cart_comm, rank, 2, coords );

    MPI_Cart_shift( cart_comm, 0, 1, &north, &south );
    MPI_Cart_shift( cart_comm, 1, 1, &west, &east );

    local_image_size[0] = image_size[0]/dims[0];
    local_image_size[1] = image_size[1]/dims[1];

    //Allocating buffers
    int lsize = local_image_size[0]*local_image_size[1];
    int lsize_border = (local_image_size[0] + 2*BORDER)*(local_image_size[1] + 2*BORDER);
    local_image_orig = (unsigned char*)malloc(sizeof(unsigned char)*lsize);
    local_image[0] = (unsigned char*)calloc(lsize_border, sizeof(unsigned char));
    local_image[1] = (unsigned char*)calloc(lsize_border, sizeof(unsigned char));

    create_types();

    for(int i = 0; i < local_image_size[0]; i++){
        for(int j = 0; j < local_image_size[1]; j++){
            G(i,j) = rank + 1;
        }
    }

    if(rank == 0)
        printf("--- Starting ---\n\n\n");

    //Test initialize guess
    MPI_Barrier(cart_comm);
    int error = 0;

    if(rank == 0)
        printf("--- Testing initialize_guess() ---\n");

    initialilze_guess();

    for(int i = 0; i < local_image_size[0]; i++){
        for(int j = 0; j < local_image_size[1]; j++){
            if(G(i,j) != F(0,i,j)){
                printf("\tinitialize_guess(): error at: %d,%d. Expected: %d, actual: %d. Rank: %d\n", G(i,j), F(0,i,j), i,j,rank);
                error = 1;
                break;
            }
        }
        if(error == 1)
            break;
    }

    MPI_Barrier(cart_comm);

    if(rank == 0)
        printf("--- Done ---\n\n\n");

    //Test border_exchange
    if(rank == 0)
        printf("--- Testing exchange_borders() ---\n");

    exchange_borders(0);

    if(north > 0){
        for(int i = 0; i < local_image_size[0]; i++){
            if(F(0,-1,i) != north + 1){
                printf("\texchange_borders: error from north at: %d. Expected: %d, actual: %d. Rank: %d\n", i, north+1,F(0,-1,i), rank);
                error = 1;
                break;
            }
        }
    }
    if(south > 0 && error == 0){
        for(int i = 0; i < local_image_size[0]; i++){
            if(F(0,local_image_size[1],i) != south + 1){
                printf("\texchange_borders: error from south at: %d. Expected: %d, actual: %d. Rank: %d\n", i, south+1, F(0,local_image_size[1],i), rank);
                error = 1;
                break;
            }
        }
    }
    if(west > 0 && error == 0){
        for(int i = 0; i < local_image_size[0]; i++){
            if(F(0,i,-1) != west + 1){
                printf("\texchange_borders error from west at: %d. Expected: %d, actual: %d. Rank: %d\n", i, west+1, F(0,i,-1), rank);
                error = 1;
                break;
            }
        }
    }
    if(east > 0 && error == 0){
        for(int i = 0; i < local_image_size[0]; i++){
            if(F(0,i,local_image_size[0]) != east + 1){
                printf("\texchange_borders: error from east at: %d. Expected: %d, actual: %d. Rank: %d\n", i, east+1, F(0,i,local_image_size[0]), rank);
                error = 1;
                break;
            }
        }
    }

    MPI_Barrier(cart_comm);

    if(rank == 0)
        printf("--- Done ---\n\n\n");

    //Test perform_convolution
    error = 0;
    if(rank == 0)
        printf("Testing perform_convolution()\n");

    MPI_Barrier(cart_comm);

    for(int i = -1; i < local_image_size[0] + 1; i++){
        for(int j = -1; j < local_image_size[0] + 1; j++){
            F(0,i,j) = ((j%2==0 && i%2==0) || (j%2!=0 && i%2!=0)) ? 10 + rank + 1 : 200 + rank + 1;
        }
    }

    perform_convolution(0);

    unsigned char even = (unsigned char)(10.0 + rank + 1.0 + lambda*(rank + 1.0 - ((850.0 + 9*(rank + 1)) * 0.111)));
    unsigned char odd = (unsigned char)(200.0 + rank + 1.0 - (1040.0/9.0 * lambda));
    for(int i = 0; i < local_image_size[0]; i++){
        for(int j = 0; j < local_image_size[1]; j++){
            if(((j%2==0 && i%2==0) || (j%2==1 && i%2==1))){
                if(F(1,i,j) != even){
                    printf("\tperform_convolution: error at: %d, %d. Expected: %d, actual: %d. Rank: %d\n", i,j,even,F(1,i,j),rank);
                    error = 1;
                    break;
                }
            }
            else{
                if(F(1,i,j) != odd){
                    printf("\tperform_convolution: error at: %d, %d. Expected: %d, actual: %d. Rank: %d\n", i,j,even,F(1,i,j),rank);
                    error = 1;
                    break;
                }
            }
        }
        if(error == 1)
            break;
    }

    MPI_Barrier(cart_comm);
    
    if(rank == 0)
        printf("--- Done ---\n\n\n");

    //Testing gather_image
    error = 0;
    if(rank == 0)
        printf("Testing gather_image()\n");

    gather_image();

    if(rank == 0){

        int co[2];
        for(int r = 0; r < 4; r++){
            MPI_Cart_coords(cart_comm, r, 2, co);

            int x,y;
            for(int i = 0; i < image_size[0]/2; i++){
                for(int j = 0; j < image_size[1]/2; j++){
                    x = i + co[0]*image_size[0]/2;
                    y = j + co[1]*image_size[1]/2;

                    if((x%2==0 && y%2==0) || (x%2==1 && y%2==1)){
                        if(image[x * image_size[0] + y] != 10 + r + 1){
                            printf("\tgather_image: error at: %d, %d. Expected: %d, actual: %d.\n", x,y,10+r+1,image[x*image_size[0]+y]);
                            error = 1;
                            break;
                        }
                    }
                    else{
                        if(image[x * image_size[0] + y] != 200 + r + 1){
                            printf("\tgather_image: error at: %d, %d. Expected: %d, actual: %d.\n", x,y,10+r+1,image[x*image_size[0]+y]);
                            error = 1;
                            break;
                        }
                    }
                }
                if(error == 1)
                    break;
            }
            if(error == 1)
                break;
        }
    }




    MPI_Barrier(cart_comm);
    if(rank == 0)
        printf("--- Done ---\n\n\n");



    MPI_Finalize();

    exit(0);
}
