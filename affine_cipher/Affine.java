/*	Author: Syed Faraz Abrar (19126296)
 *	Purpose: This class contains all the static functions required
 *			 for Affine cipher encryption and decryption
 *	Last modified: 18/04/2019
 */

import java.lang.StringBuilder;
import java.lang.Math;

public class Affine
{
	// This function takes in the text to encrypt, a, and b
	// It encrypts the string and returns the encrypted text
	public static String encrypt(String text, int a, int b)
	{
		char c;

		// charIdx is the index of each character
		// i.e 'a' = 0, 'b' = 1, etc
		char charIdx;

		StringBuilder encryptedText = new StringBuilder(text);

		// Loop through every character and encrypt them
		for (int i = 0; i < text.length(); i++)
		{
			c = text.charAt(i);

			// Special case for lowercase characters
			if (isLower(c))
			{
				// Change ASCII value to indexes (0 - 25)
				charIdx = (char) (c - 'a');

				// Encryption formula: (ax + b) mod 26
				charIdx = (char) (((a * charIdx) + b) % 26);

				// Change charIdx back to ASCII value of character
				charIdx = (char) (charIdx + 'a');

				// Set the character at the given index to the encrypted char
				encryptedText.setCharAt(i, charIdx);
			}

			// Special case for uppercase characters
			else if (isUpper(c))
			{
				// Change ASCII value to indexes (0 - 25)
				charIdx = (char) (c - 'A');

				// Encryption formula: (ax + b) mod 26
				charIdx = (char) (((a * charIdx) + b) % 26);

				// Change charIdx back to ASCII value of character
				charIdx = (char) (charIdx + 'A');

				// Set the character at the given index to the encrypted char
				encryptedText.setCharAt(i, charIdx);
			}

			// Ignore all other non-letter characters
		}
		
		return encryptedText.toString();
	}

	// This function takes in the text to decrypt, a, and b
	// It decrypts the string and returns the decrypted text
	public static String decrypt(String text, int a, int b)
	{
		char c, charIdx;
		int inverseMod;
		StringBuilder decryptedText = new StringBuilder(text);

		// inverseMod = multiplicative inverse of a with m=26
		inverseMod = ModInv.inverseMod(a, 26);

		int absCharIdx; // required for negative values of (charIdx - b)

		// Loop through every character and decrypt them
		for (int i = 0; i < text.length(); i++)
		{
			c = text.charAt(i);

			// Special case for lowercase characters
			if (isLower(c))
			{
				// Change ASCII value to indexes (0 - 25)
				charIdx = (char) (c - 'a');
				
				// For decryption, if the (x - b) ends up being negative
				// then the formula doesn't work unless 26 is added to it
				// to make it positive
				//
				// In this case, charIdx = x, therefore it is first checked
				// to see whether (charIdx - b) is negative or not, and handled
				// accordingly
				absCharIdx = charIdx - b;
				if (absCharIdx < 0) absCharIdx = 26 + absCharIdx;

				// Decryption formula: (multiplicative inverse of a) * (x - b) mod 26
				charIdx = (char) ((inverseMod * absCharIdx) % 26);

				// Change charIdx back to ASCII value of character
				charIdx = (char) (charIdx + 'a');

				// Set the character at the given index to the decrypted char
				decryptedText.setCharAt(i, charIdx);
			}
			
			// Special case for uppercase characters
			if (isUpper(c))
			{
				// Change ASCII value to indexes (0 - 25)
				charIdx = (char) (c - 'A');
				
				// For decryption, if the (x - b) ends up being negative
				// then the formula doesn't work unless 26 is added to it
				// to make it positive
				//
				// In this case, charIdx = x, therefore it is first checked
				// to see whether (charIdx - b) is negative or not, and handled
				// accordingly
				absCharIdx = charIdx - b;
				if (absCharIdx < 0) absCharIdx = 26 + absCharIdx;

				// Decryption formula: (multiplicative inverse of a) * (x - b) mod 26
				charIdx = (char) ((inverseMod * absCharIdx) % 26);

				// Change charIdx back to ASCII value of character
				charIdx = (char) (charIdx + 'A');

				// Set the character at the given index to the decrypted char
				decryptedText.setCharAt(i, charIdx);
			}

			// Ignore all other characters
		}

		return decryptedText.toString();
	}

	// Returns true if the imported character is lowercase
	private static boolean isLower(char c)
	{
		boolean lower = false;

		if (c >= 'a' && c <= 'z')
			lower = true;

		return lower;
	}

	// Returns true if the imported character is uppercase
	private static boolean isUpper(char c)
	{
		boolean upper = false;

		if (c >= 'A' && c <= 'Z')
			upper = true;

		return upper;
	}
}
