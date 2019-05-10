/*	Author: Syed Faraz Abrar (19126296)
 *	Purpose: This class contains the functions required to
 *			 get the modular inverse of a variable a with
 *			 regards to a variable m
 *	Last modified: 18/04/2019
 */

public class ModInv
{
	// finds the greatest common denominator between a and b
	// code adapted from pseudocode found in lecture 6 slide 19
	public static int gcd(int a, int b)
	{
		int temp = 0;

		while (b != 0)
		{
			temp = b;
			b = a % b;
			a = temp;
		}

		return a;
	}

	// Finds x such that (a*x) mod m = 1
	// In our case, m = 26 by default
	public static int inverseMod(int a, int m)
	{
		// Ensuring that a and m are co-prime is done in the
		// TestKeys.java file, thus there is guaranteed to
		// be a multiplicative inverse of a
	
		// Set the initial value of x to 0, and only
		// modify it when the multiplicative inverse is found
		int x = 0;

		int i = 1; // Used as a loop index

		// Brute force to find x, the multiplicative inverse of a such
		// that (a*x) mod m = 1
		while (x == 0)
		{
			if ((a*i) % m == 1)
				x = i;

			i++;
		}

		return x;
	}
}
