/*	Author: Syed Faraz Abrar (19126296)
 *	Purpose: This file contains a function used to test whether
 *			 a given key (a, b) is valid.
 *			 It also contains a function that prints all valid
 *			 keys for m=26
 *	Last modified: 18/04/2019
 */

import java.io.PrintWriter;
import java.io.IOException;

public class TestKeys
{
	public static boolean testKeys(int a, int b)
	{
		boolean valid = true;

		// Ensure a and b are between 1 and 26 inclusive
		if ((a < 1 || a > 26) || (b < 1 || b > 26))
			valid = false;

		// Ensure a and 26 are co-prime
		if (ModInv.gcd(a, 26) != 1)
			valid = false;

		return valid;
	}

	public static void printAllKeys()
	{
		PrintWriter pw;
		try
		{
			pw = new PrintWriter("keys.txt");

			for (int a = 1; a <= 26; a++)
			{
				for (int b = 1; b <= 26; b++)
				{
					if (TestKeys.testKeys(a, b))
						pw.println("(" + a + ", " + b + ")");
				}
			}

			pw.close();
		}

		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
}
