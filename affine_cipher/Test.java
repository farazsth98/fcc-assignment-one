/*	Author: Syed Faraz Abrar (19126296)
 *	Purpose: This is the test file for the Affine cipher
 *			 The user is given two options, either read the
 *			 test file or type in their own input
 *	Last modified: 18/04/2019
 */

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import java.io.IOException;

public class Test
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		Scanner scFile;
		String original, encryptedText, decryptedText, fileText;
		String choice = "0";
		int a, b;
		File file, output, encrypted;
		PrintWriter pw1, pw2;

		// Print all valid keys to the file 'keys.txt'
		TestKeys.printAllKeys();
		
		// Check whether user wants to use the testfile-Affine.txt file
		// or input their own text
		while (choice.charAt(0) != 'f' && choice.charAt(0) != 't')
		{
			System.out.println("Read test file (f) or type in text (t)?: ");
			choice = sc.nextLine();
		}
		
		/******************************* TEST FILE ******************************/

		if (choice.charAt(0) == 'f')
		{
			try
			{
				// File I/O
				file = new File("testfile-Affine.txt");
				output = new File("output.txt");
				pw1 = new PrintWriter(output);
				encrypted = new File("encrypted.txt");
				pw2 = new PrintWriter(encrypted);
				scFile = new Scanner(file);

				fileText = scFile.nextLine();

				// Input values for a and b
				System.out.print("Enter value for a: ");
				while (!sc.hasNextInt())
				{
					sc.next();
					System.out.print("Enter value for a: ");
				}
				a = sc.nextInt();
				
				System.out.print("Enter value for b: ");
				while (!sc.hasNextInt())
				{
					sc.next();
					System.out.print("Enter value for b: ");
				}
				b = sc.nextInt();

				// Ensure the key (a, b) is valid, otherweise exit program
				if (!TestKeys.testKeys(a, b))
					System.out.println("Error: the key (" + a + ", " + b + ") is not valid");
				else
				{
					// Output all outputs to output.txt
					pw1.println("ORIGINAL: " + fileText);
					encryptedText = Affine.encrypt(fileText, a, b);
					pw1.println("ENCRYPTED: " + encryptedText);
					decryptedText = Affine.decrypt(encryptedText, a, b);
					pw1.println("DECRYPTED: " + decryptedText);

					// Output only the encrypted text to encrypted.txt
					pw2.println(encryptedText);

					// Ensure that decrypted text and the original text are the same
					if (fileText.compareTo(decryptedText) == 0)
						System.out.println("Original and decrypted text are the same");
					else
						System.out.println("Original and decrypted text are NOT the same");

					System.out.println();
					System.out.println("encrypted output written to encrypted.txt");
					System.out.println("all output written to output.txt");
				}

				pw1.close();
				pw2.close();
			}

			catch (IOException e) { System.out.println(e.getMessage()); }
		}
		
		/*************************** INPUT TEXT *****************************/

		else
		{
			// Input text from user
			System.out.print("Enter text to encrypt: ");
			original = sc.nextLine();

			// Input values for a and b
			System.out.print("Enter value for a: ");
			while (!sc.hasNextInt())
			{
				sc.next();
				System.out.print("Enter value for a: ");
			}
			a = sc.nextInt();
			
			System.out.print("Enter value for b: ");
			while (!sc.hasNextInt())
			{
				sc.next();
				System.out.print("Enter value for b: ");
			}
			b = sc.nextInt();

			// Ensure the key (a, b) is valid, else exit program
			if (!TestKeys.testKeys(a, b))
				System.out.println("Error: the key (" + a + ", " + b + ") is not valid");
			else
			{
				// Output everything to the screen
				System.out.println("ORIGINAL: " + original);
				encryptedText = Affine.encrypt(original, a, b);
				System.out.println("ENCRYPTED: " + encryptedText);
				decryptedText = Affine.decrypt(encryptedText, a, b);
				System.out.println("DECRYPTED: " + decryptedText);

				// Ensure that decrypted text and the original text are the same
				if (original.compareTo(decryptedText) == 0)
					System.out.println("Original and decrypted text are the same");
				else
					System.out.println("Original and decrypted text are NOT the same");
			}
		}
	}
}
