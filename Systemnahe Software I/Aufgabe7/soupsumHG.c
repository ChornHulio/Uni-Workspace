#include <stdio.h>
#include <string.h> 
#include <stdlib.h>
#include <math.h>


// End of frame -> getchar()
#define EOF 10
//defines of cases from the automate
#define ZUSTAND_0 0
#define ZUSTAND_1 1
#define ZUSTAND_2 2
#define ZUSTAND_3 3
#define ZUSTAND_4 4
#define ZUSTAND_5 5
#define ZUSTAND_6 6
#define ZUSTAND_7 7




float make_number(int input)
{
	switch(input)
	{
		case 48:
			return 0;
			break;
		case 49:
			return 1;
			break;
		case 50:
			return 2;
			break;
		case 51:
			return 3;
			break;
		case 52:
			return 4;
			break;
		case 53:
			return 5;
			break;
		case 54:
			return 6;
			break;
		case 55:
			return 7;
			break;
		case 56:
			return 8;
			break;
		case 57:
			return 9;
			break;
	}
}

int main(void)
{
	int input = 0;
	int state = ZUSTAND_0;
	int ready = 1;
	float sum = 0;								//"first" input
	float sum2 = 0;								//"second" input
	float sum3 = 0;
	int vorzeichen = 0;							//vorzeichen == 0 -> + ; vorzeichen == 1 -> - (siehe ZUSTAND_5)
	int potenz = 10;
	int exponent = 0;							//exponent == 0 -> no exponent; exponent == 1 -> exponent

	while(ready)
	{
		input = getchar();
			
		switch (state)
		{	
		case ZUSTAND_0:
			if(input == EOF)
			{
				ready = 0;
				sum3 = sum3 + sum;
			}
			else if(input == 43)									//+
				state = ZUSTAND_1;
			else if(input == 45)									//-
			{
				state = ZUSTAND_1;
				vorzeichen = 1;
			}
			else if(input >= 48 && input <= 57)					//0-9
			{
				state = ZUSTAND_2;
				sum = sum + make_number(input);
			}
			else if(input == 46)									//.********
			{
				state = ZUSTAND_3;
			}
			else
			{
				state = ZUSTAND_0;
				exponent = 0;
				potenz = 10;
				vorzeichen = 0;
			}
			break;

		case ZUSTAND_1:
			if(input == EOF)
			{
				ready = 0;
				sum3 = sum3 + sum;
			}
			else if(input >= 48 && input <= 57)					//0-9
			{
				sum = sum * potenz + make_number(input);
				state= ZUSTAND_2;
			}
			else if(input == 46)									//.**********
			{
				state= ZUSTAND_3;
			}
			else
			{
				state = ZUSTAND_0;
				exponent = 0;
				potenz = 10;
				vorzeichen = 0;
				sum3 = sum3 + sum;
				sum = 0;
			}
			break;

		case ZUSTAND_2:
			if(input == EOF)
			{
				ready = 0;
				sum3 = sum3 + sum;
			}
			else if(input >= 48 && input <= 57)					//0-9
			{
				sum = sum * potenz + make_number(input);
				input = input *10;
			}
			else if(input == 46)									//.**********
			{
				state= ZUSTAND_4;
				potenz = 10;
			}
			else if(input == 69 || input == 101)					//E,e********
			{
				state = ZUSTAND_5;
				if(vorzeichen == 1)
				{
					vorzeichen = 0;
					sum = -sum;
				}
				exponent = 1;
				potenz = 10;
			}
			else
			{
				state = ZUSTAND_0;
				exponent = 0;
				potenz = 10;
				vorzeichen = 0;
				sum3 = sum3 + sum;
				sum = 0;
			}
			break;

		case ZUSTAND_3:
			if(input == EOF)
			{
				ready = 0;
				sum3 = sum3 + sum;
			}
			else if(input >= 48 && input <= 57)					//0-9
			{
				sum = sum  + (make_number(input)/potenz);
				input = input *10;
				state = ZUSTAND_4;
			}
			else
				state = ZUSTAND_0;
			break;

		case ZUSTAND_4:
			if(input == EOF)
			{
				if (vorzeichen == 1)
				{
					sum = -sum;
					sum3 = sum3 + sum;
				}
				else
				{
					sum3 = sum3 + sum;
				}

				ready = 0;
				
			}
			else if(input >= 48 && input <= 57)					//0-9
			{
				sum = sum  + (make_number(input)/potenz);
				potenz = potenz *10;
			}
			else if(input == 69 || input == 101)					//E,e********
			{
				if(vorzeichen == 1)
				{
					vorzeichen = 0;
					sum = -sum;
				}
				state = ZUSTAND_5;
				potenz = 10;
				exponent = 1;
			}
			else
			{
				state = ZUSTAND_0;
				exponent = 0;
				potenz = 10;
				vorzeichen = 0;
				sum3 = sum3 + sum;
				sum = 0;
			}
			break;

		case ZUSTAND_5:
			if(input == EOF)
			{
				ready = 0;
				sum3 = sum3 + sum;
			}
			else if(input == 43)								//+
			{
				state = ZUSTAND_6;
				if(vorzeichen == 1)
				{
					sum = -sum;
					vorzeichen = 0;
				}
			}
			else if(input == 45)								//-
			{
				state = ZUSTAND_6;
				if(vorzeichen == 1)
				{
					sum = -sum;
					vorzeichen = 1;
				}
				vorzeichen = 1;
			}
			else if(input >= 48 && input <= 57)					//0-9
			{
				sum2 = make_number(input);
				state = ZUSTAND_7;
			}
			else
			{
				state = ZUSTAND_0;
				exponent = 0;
				potenz = 10;
				vorzeichen = 0;
				sum3 = sum3 + sum;
				sum = 0;
			}
			break;

		case ZUSTAND_6:
			if(input == EOF)
			{
				if (exponent == 1 && vorzeichen == 1)
				{
					sum2 = -sum2;
					sum = pow((double)sum,(double)sum2);
					sum3 = sum3 + sum;
					sum2 = 0;
				}
				else if (vorzeichen == 1)
				{
					sum2 = -sum2;
					sum3 = sum3 + sum2;
				}
				else if (exponent == 1)
				{
					sum = (int)pow((double)sum,(double)sum2);
					sum3 = sum3 + sum;
					sum2 = 0;
				}
				ready = 0;
			}
			else if(input >= 48 && input <= 57)					//0-9
			{
				sum2 = make_number(input);
				state = ZUSTAND_7;
			}
			else
			{
				state = ZUSTAND_0;
				exponent = 0;
				potenz = 10;
				vorzeichen = 0;
				sum3 = sum3 + sum;
				sum = 0;
				sum2 = 0;
			}
			break;

		case ZUSTAND_7:
			if(input == EOF)
			{
				if (exponent == 1 && vorzeichen == 1)
				{
					sum2 = -sum2;
					sum = pow((double)sum,(double)sum2);
					sum3 = sum3 + sum;
					sum2 = 0;
				}
				else if (vorzeichen == 1)
				{
					sum2 = -sum2;
					sum3 = sum3 + sum2;
				}
				else if (exponent == 1)
				{
					sum = (int)pow((double)sum,(double)sum2);
					sum3 = sum3 + sum;
					sum2 = 0;
				}
				ready = 0;
			}
			else if(input >= 48 && input <= 57)					//0-9
			{
				sum2 = sum2 * potenz + make_number(input);
				potenz = potenz *10;
			}
			else
			{
				if (vorzeichen == 1)
				{
					sum2 = -sum2;
				}
				if(exponent !=1)
				{
					state = ZUSTAND_0;
					exponent = 0;
					potenz = 10;
					vorzeichen = 0;
					sum3 = sum3 + sum;
					sum3 = sum3 +sum2;
					sum = 0;
					sum2 = 0;
				}
				if (exponent == 1)
				{
					sum = (int)pow((double)sum,(double)sum2);
					sum3 = sum3 + sum;
					sum2 = 0;
					sum = 0;
					state = ZUSTAND_0;
					exponent = 0;
					potenz = 10;
					vorzeichen = 0;
				}
				
			}
			break;

		default:
			break;
		}
	}
	printf("%.4f\n",(float) (sum3));								//casten oder gleich float?
}
