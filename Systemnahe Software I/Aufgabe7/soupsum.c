#include <stdio.h>
#include <math.h>

// global variables
float exponent = 0;

float s0();
float s1();
float s2(float x);
float s3();
float s4();
float s5();
float s6();
float s7(int x);

int main() {
	int c = 0;
	float sum = 0;
	while((c = getchar()) != EOF) 
	{
		ungetc(c,stdin); // push the back - s0 can read it itself
		float newNumber = s0(); // start state machine
		if(exponent == 0) 
		{
			sum += newNumber;
		}
		else
		{
			sum += newNumber*exponent;
			exponent = 0;
		}
	}
	printf("%.4f\n", sum);
	return 0;
}

float s0() {
	int c = getchar();
	if(c >= '0' && c <= '9') // digit
	{
		return s2(c-'0');
	} 
	else if(c == '+' || c == '-') // + or -
	{
		return (c == '+' ? s1() : (-1) * s1());
	} 
	else if(c == '.') // .
	{
		float fp = s3();
		return (fabs(fp) > 0.00001 ? fp/10 : 0);
	}
	return 0;
}

float s1() {
	int c = getchar();
	if(c >= '0' && c <= '9') // digit
	{
		return s2(c-'0');
	} 
	else if(c == '.') // .
	{
		float fp = s3();
		return (fabs(fp) > 0.00001 ? fp/10 : 0);
	}
	ungetc(c,stdin);
	return 0;
}

float s2(float x) {
	int c = getchar();
	if(c >= '0' && c <= '9') // digit
	{
		return s2(x * 10 + (c-'0'));
	}
	else if(c == '.') // .
	{
		float fp = s4();
		return x + (fabs(fp) > 0.00001 ? fp/10 : 0);
	} 
	else if(c == 'e' || c == 'E') // e or E
	{
		exponent = pow(10,s5()); // create the exponent
		return x; // return the number without the exponent
	}
	ungetc(c,stdin);
	return x;
}

float s3() {
	int c = getchar();
	if(c >= '0' && c <= '9') // digit
	{
		float fp = s4();
		return (c-'0') + (fabs(fp) > 0.00001 ? fp/10 : 0);
	}
	ungetc(c,stdin);
	return 0;
}

float s4() {
	int c = getchar();
	if(c >= '0' && c <= '9') // digit
	{
		float fp = s4();
		return (c-'0') + (fabs(fp) > 0.00001 ? fp/10 : 0);
	} 
	else if(c == 'e' || c == 'E') // e or E
	{
		exponent = pow(10,s5());
		return 0; // return the number without the exponent
	}
	ungetc(c,stdin);
	return 0;
}

float s5() {
	int c = getchar();
	if(c >= '0' && c <= '9') // digit
	{
		return s7(c-'0');
	} 
	else if(c == '+' || c == '-') // + or -
	{	
		return (c == '+' ? s6() : (-1) * s6());;
	} 
	ungetc(c,stdin);
	return 0;
}

float s6() {
	int c = getchar();
	if(c >= '0' && c <= '9') // digit
	{
		return s7(c-'0');
	}
	ungetc(c,stdin);
	return 0;
}

float s7(int x) {
	int c = getchar();
	if(c >= '0' && c <= '9') // digit
	{
		return x * 10 + s7(c-'0');
	}
	ungetc(c,stdin);
	return x;
}
