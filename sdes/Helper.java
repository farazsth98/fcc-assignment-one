/*	Author: Syed Faraz Abrar (19126296)
 *	Purpose: Contains Helper functions related to
 *			 SDES encryption/decryption
 *	Last modified: 18/04/2019
 */

import java.util.Scanner;

public class Helper
{
	// Helper function for inputting a valid key from the user
	// it checks to make sure that the length of the key is 10 bits
	// as well as making sure that its a valid binary number
	public static String inputKey()
	{
		Scanner sc = new Scanner(System.in);
		String input = "";
		boolean validKey = false;
		
		while (!validKey)
		{
			System.out.println("Please enter a key to use (must be 10 bit long and binary): ");
			input = sc.nextLine();
			
			// Set validKey to true by default and only set it to false if
			// a character other than '0' or '1' is found in the input
			validKey = true;
			
			// First ensure the key is 10 bits long
			if (input.length() == 10)
			{
				// Then check every character to ensure it is either a 0 or a 1
				for (int i = 0; i < input.length(); i++)
				{
					// If a character other than a 0 or a 1 is found, set
					// validKey to equal to false
					if (input.charAt(i) != '0' && input.charAt(i) != '1')
						validKey = false;
				}
			}

			else validKey = false; // Input has to be 10 bits
		}

		return input;
	}
}
