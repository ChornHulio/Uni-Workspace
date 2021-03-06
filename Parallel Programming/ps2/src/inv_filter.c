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
    {0.111,0.111,0.111}
};


float lambda = 0.02;

MPI_Comm cart_comm;

MPI_Datatype local_image_orig_t,
             image_t,
             border_row_t,
             border_col_t;


unsigned char *image,            //Glocal g
         *local_image_orig, //Local part of g
         *local_image[2];   //Local part of f, two buffers

void create_types() {
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

void distribute_image() {

    MPI_Request req;
    MPI_Irecv(local_image_orig, 1, local_image_orig_t, 0, 1, cart_comm, &req);

    if(rank == 0) {
        int co[2];
        for(int i = 0; i < size; i++) {
            MPI_Cart_coords(cart_comm, i, 2, co);
            int index = co[0]*local_image_size[0]*image_size[1] + co[1]*local_image_size[1];
            MPI_Send(&image[index], 1, image_t, i, 1, cart_comm);
        }
    }

    MPI_Wait(&req, MPI_STATUS_IGNORE);
}

void initialilze_guess() {
    // copy local_image_orig (g) to local_image[0] (f0)
    for(int i = -BORDER; i < local_image_size[0]+BORDER; i++) {
        for(int j = -BORDER; j < local_image_size[1]+BORDER; j++) {
            if(i < 0 || j < 0 || i >= local_image_size[0] || j >= local_image_size[1]) {
                F(0,i,j) = 0;
            } else {
                F(0,i,j) = G(i,j);
            }
        }
    }
}

void exchange_borders(int step) {
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

void perform_convolution(int step) {
    // go over (current) local image (with border)
    for(int i = 0; i < local_image_size[0]; i++) {
        for(int j = 0; j < local_image_size[1]; j++) {
            // go over filter
            double value = 0.0; // result of filter
            for(int a = -1; a <= 1; a++) { // attention: works only for 3x3 filters
                for(int b = -1; b <= 1; b++) {
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

void gather_image() {
    // MPI type for image gathering
    MPI_Datatype image_gathering_t;
    MPI_Type_vector(local_image_size[0],
                    local_image_size[1], local_image_size[1]+2*BORDER, MPI_UNSIGNED_CHAR, &image_gathering_t);
    MPI_Type_commit(&image_gathering_t);

    MPI_Request req[size];

    // gather image data at rank 0
    if(rank == 0) {
        // receive data from all ranks
        for(int i = 0; i < size; i++) {
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
    if(rank == 0) {
        MPI_Waitall(size, req, MPI_STATUSES_IGNORE);
    }
}

int main(int argc, char** argv) {

    //Initialization
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    //Reading image
    if(rank == 0) {
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
    for(int i = 0; i < ITERATIONS; i++) {
        exchange_borders(i);
        perform_convolution(i);
    }

    gather_image();

    MPI_Finalize();

    //Write image
    if(rank==0) {
        write_bmp(image, image_size[0], image_size[1]);
    }

    exit(0);
}
