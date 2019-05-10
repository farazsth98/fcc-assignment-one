/*	Author: Syed Faraz Abrar (19126296)
 *	Purpose: This class contains the required
 *			 functions for SDES encryption
 *			 and decryption. It also stores the
 *			 encrypted and decrypted string
 *	Last modified: 18/04/2019
 */

import java.io.*;
import java.util.Scanner;

public class SDES
{
	// Using StringBuilder objects for the
	// encrypted and decrypted strings so it is
	// easy to append each character to the String
	private StringBuilder encrypted;
	private StringBuilder decrypted;

	private Key key; // The key generation is done in Key.java

	// SBoxes 0 and 1, as seen on lecture 4 slide 29
	private final static int[][] S0 = new int[][] 
	{
		{1,0,3,2},
		{3,2,1,0},
		{0,2,1,3},
		{3,1,3,2}
	};

	private final static int[][] S1 = new int[][]
	{
		{0,1,2,3},
		{2,0,1,3},
		{3,0,1,0},
		{2,1,0,3}
	};

	// Default constructor to encrypt and decrypt a String
	public SDES(String text, Key key)
	{
		encrypted = new StringBuilder("");
		decrypted = new StringBuilder("");
		this.key = key;

		// Encrypt the text and store in encrypted
		this.encrypt(text);

		// Decrypt the text and store in decrypted
		this.decrypt(encrypted.toString());
	}

	// Alternate constructor to encrypt and decrypt a txt file
	public SDES(File file, Key key)
	{
		this.key = key;

		// fileIO handles the encryption and decryption
		this.fileIO(file);
	}	

	// Returns the encrypted string
	public String getEncrypted()
	{
		return this.encrypted.toString();
	}

	// Prints the encrypted string
	public void printEncrypted()
	{
		System.out.println(encrypted.toString());
	}

	// Returns the decrypted string
	public String getDecrypted()
	{
		return this.decrypted.toString();
	}

	// Prints the decrypted string
	public void printDecrypted()
	{
		System.out.println(decrypted.toString());
	}

	private void fileIO(File file)
	{
		// Scanner used for file input
		Scanner sc;

		// The files to write the encrypted and decrypted strings
		// to
		File encryptedOutput, decryptedOutput;
		PrintWriter pwEncrypted, pwDecrypted;

		try
		{
			// Set encrypted and decrypted output files
			encryptedOutput = new File("encrypted.txt");
			decryptedOutput = new File("decrypted.txt");
			sc = new Scanner(file);
			pwEncrypted = new PrintWriter(encryptedOutput);
			pwDecrypted = new PrintWriter(decryptedOutput);
			
			String line;

			// Read each line in the file
			while (sc.hasNextLine())
			{
				encrypted = new StringBuilder("");
				decrypted = new StringBuilder("");

				// Read each line from the file, and encrypt it
				line = sc.nextLine();
				this.encrypt(line);

				// Decrypt the encrypted line immediately
				this.decrypt(encrypted.toString());
				
				// Write the encrypted and decrypted lines to
				// each of the corresponding files
				pwEncrypted.println(encrypted.toString());
				pwDecrypted.println(decrypted.toString());
			}

			pwEncrypted.close();
			pwDecrypted.close();
		}

		catch (IOException e) { System.out.println(e.getMessage()); }
	}

	// The encryption function. It encrypts the text and stores it in
	// the private encrypted variable
	private void encrypt(String text)
	{
		char c;
		char e; // encrypted version of c
		int[] k1 = key.getK1();
		int[] k2 = key.getK2();

		// The binary representation of each char
		// in String format
		String binary;

		// StringBuilder used to ensure the binary
		// representation is padded to 8 bits
		StringBuilder padded;

		// Used to represent each char as 8 bits
		int[] bits;

		// The left half and right half of the plaintext
		// bits
		int[] L = new int[4];
		int[] R = new int[4];

		for (int i = 0; i < text.length(); i++)
		{
			c = text.charAt(i);
			binary = Integer.toBinaryString(c);
			binary = this.pad(binary, 8); // Pad to 8 bits
			bits = this.convertToBinary(binary);

			// The actual encryption steps starts here

			// First, each plaintext character's bits has 
			// the initial permutation applied to it
			// IP = [2, 6, 3, 1, 4, 8, 5, 7]
			this.IP(bits);

			// Then, the bits are divided into two halves
			// L and R
			this.divideBits(bits, L, R);
			
			// Then, fk1 is performed using
			// f(L, R, k1)
			this.f(L, R, k1);

			// Then, L and R are switched around
			this.SW(L, R);

			// Then, fk2 is performed using
			// f(L, R, k2)
			this.f(L, R, k2);

			// Combine L and R into an array
			int[] combined = this.combineArrays(L, R);

			// Run the inverse IP function over it
			// IP^-1 = [4, 1, 3, 5, 7, 2, 8, 6]
			this.inverseIP(combined);

			// Convert int[] array into a binary string of bits
			binary = this.binaryToString(combined);
			
			// Convert this binary string of bits to a character
			e = (char) (Integer.parseInt(binary, 2));

			// Append to encrypted
			encrypted.append(e);
		}
	}

	// The decryption function. It decrypts the text and stores it
	// in the private decrypted variable
	private void decrypt(String text)
	{
		char c;
		char d; // decrypted version of c
		int[] k1 = key.getK1();
		int[] k2 = key.getK2();

		// The binary representation of each char
		// in String format
		String binary;

		// StringBuilder used to ensure the binary
		// representation is padded to 8 bits
		StringBuilder padded;

		// Used to represent each char as 8 bits
		int[] bits;

		// The left half and right half of the plaintext
		// bits
		int[] L = new int[4];
		int[] R = new int[4];

		for (int i = 0; i < text.length(); i++)
		{
			c = text.charAt(i);
			binary = Integer.toBinaryString(c);
			binary = this.pad(binary, 8);
			bits = this.convertToBinary(binary);

			// The actual decryption steps starts here

			// First, each plaintext characters' bits has
			// the initial permutation applied to it
			// IP = [2, 6, 3, 1, 4, 8, 5, 7]
			this.IP(bits);

			// Then, the bits are divided into two halves
			// L and R
			this.divideBits(bits, L, R);

			// Then, fk1 is performed using
			// f(L, R, k2) because decryption works backwards
			this.f(L, R, k2);

			// Then, L and R are switched around
			this.SW(L, R);

			// Then, fk2 is performed using
			// f(L, R, k1) because decryption works backwards
			this.f(L, R, k1);

			// Combine L and R into an array backwards
			// because we are decrypting
			int[] combined = this.combineArrays(L, R);

			// Run the inverse IP function over it
			// IP^-1 = [4, 1, 3, 5, 7, 2, 8, 6]
			this.inverseIP(combined);

			// Convert int[] array into a binary string of bits
			binary = this.binaryToString(combined);
			
			// Convert this binary string of bits to a character
			d = (char) (Integer.parseInt(binary, 2));

			// Append to decrypted
			decrypted.append(d);
		}
	}

	// Switches L and R around so L = R and R = L
	private void SW(int[] L, int[] R)
	{
		// Copy L into temp
		int[] temp = new int[L.length];
		for (int i = 0; i < L.length; i++)
		{
			temp[i] = L[i];
		}

		// Copy R into L
		for (int i = 0; i < R.length; i++)
		{
			L[i] = R[i];
		}

		// Copy temp into R
		for (int i = 0; i < temp.length; i++)
		{
			R[i] = temp[i];
		}
	}

	// Performs fk using f(L, R, key)
	// fk is defined as:
	// (L ^ F(R, SK), R) where ^ is the XOR operator
	// SK is a subkey, either k1 or k2
	private void f(int[] L, int[] R, int[] key)
	{
		// Perform the F(R, SK) and store the
		// result in arrF
		int[] arrF = this.F(R, key);

		// Then, XOR L with arrF
		int[] arrXOR = this.XOR(L, arrF);

		// Copy arrXOR into L
		// int[] combined = combineArrays(arrXOR, R);
		for (int i = 0; i < arrXOR.length; i++)
		{
			L[i] = arrXOR[i];
		}
	}

	// The F(R, SK) function, performs the
	// EP function on R, then it XORS the key
	// with the result.
	// It then substitutes the two halves of
	// the latest result using the Sboxes,
	// which is then permuted using P4
	private int[] F(int[] R, int[] key)
	{
		// Expand E using E/P
		// Permutation is [4, 1, 2, 3, 2, 3, 4, 1]
		int[] expanded = this.EP(R);

		// XOR expanded bits with the key
		int[] arrXOR = this.XOR(expanded, key);

		// Substitute the two halves of the XOR'd
		// array based on the SBoxes
		int[] substituted = this.substitute(arrXOR);

		// Perform P4 on the substituted array
		// P4 = [2, 4, 3, 1]
		substituted = this.P4(substituted);

		return substituted;
	}

	// Perform the permutation P4 to the imported array
	// P4 = [2, 4, 3, 1]
	// arr is guaranteed to be 4 bits long
	private int[] P4(int[] arr)
	{
		// Copy arr into temp
		int[] temp = new int[arr.length];

		temp[0] = arr[1];
		temp[1] = arr[3];
		temp[2] = arr[2];
		temp[3] = arr[0];

		return temp;
	}

	// Returns a new array that has values substituted into it
	// based on the values of the imported arr variable and
	// the two Sboxes defined at the top of this file
	private int[] substitute(int[] arr)
	{
		// r = rows, c = columns
		// 0 and 1 indicate which sbox it is for
		int r0=0, c0=0, r1=0, c1=0;

		// Arrays based on the Sboxes that are
		// then combined
		int[] arr0 = new int[2];
		int[] arr1 = new int[2];
		int[] combined = new int[4];

		// Binary to decimal conversion for each
		// row and column. Remember that for each half of
		// the imported array, the 0th and 3rd bit represent
		// the row number while the 1st and 2nd bit represent
		// the column number
		//
		// We perform binary math for each index
		r0 += arr[0] == 1 ? 2 : 0;
		r0 += arr[3] == 1 ? 1 : 0;
		c0 += arr[1] == 1 ? 2 : 0;
		c0 += arr[2] == 1 ? 1 : 0;

		r1 += arr[4] == 1 ? 2 : 0;
		r1 += arr[7] == 1 ? 1 : 0;
		c1 += arr[5] == 1 ? 2 : 0;
		c1 += arr[6] == 1 ? 1 : 0;

		// Get each value from its corresponding row and column
		// from S0 and S1, pad them to two bits, then convert them
		// to binary and store them into the int[] arrays
		// arr0 and arr1
		arr0 = this.convertToBinary(pad(
			Integer.toBinaryString(S0[r0][c0]), 2));
		arr1 = this.convertToBinary(pad(
			Integer.toBinaryString(S1[r1][c1]), 2));

		// Combine arr0 and arr1 into a single array
		combined = combineArrays(arr0, arr1);

		return combined;
	}

	// Expansion permutation
	// [4, 1, 2, 3, 2, 3, 4, 1]
	private int[] EP(int[] arr)
	{
		// Input is guaranteed to be of size 4
		// Output will be size 8
		int[] expanded = new int[8];

		// Perform permutation
		expanded[0] = arr[3];
		expanded[1] = arr[0];
		expanded[2] = arr[1];
		expanded[3] = arr[2];
		expanded[4] = arr[1];
		expanded[5] = arr[2];
		expanded[6] = arr[3];
		expanded[7] = arr[0];

		return expanded;
	}

	// Divides bits into two halves L and R
	private void divideBits(int[] bits, int[] L, int[] R)
	{
		for (int i = 0; i < 4; i++)
		{
			L[i] = bits[i];
			R[i] = bits[i+4];
		}
	}

	// XOR function that XORs each index with each corresponding
	// index of two arrays
	private int[] XOR(int[] arrOne, int[] arrTwo)
	{
		int[] arrXOR = new int[arrOne.length];

		// arrOne and arrTwo are guaranteed to be the same length
		for (int i = 0; i < arrOne.length; i++)
		{
			if (arrOne[i] != arrTwo[i])
				arrXOR[i] = 1;
			else
				arrXOR[i] = 0;
		}

		return arrXOR;
	}

	// Pads the imported binary string representation to
	// l bits and returns it
	private String pad(String binary, int l)
	{
		StringBuilder padded = new StringBuilder(binary);

		while (padded.length() < l)
		{
			padded.insert(0, '0');
		}

		return padded.toString();
	}

	// Combines two arrays into one
	private int[] combineArrays(int[] arrOne, int[] arrTwo)
	{
		int[] combined = new int[arrOne.length + arrTwo.length];
		
		// Add arrOne's indexes to the new array
		for (int i = 0; i < arrOne.length; i++)
		{
			combined[i] = arrOne[i];
		}

		// Then add arrTwo's indexes
		for (int i = 0; i < arrTwo.length; i++)
		{
			combined[i+arrOne.length] = arrTwo[i];
		}

		return combined;
	}

	// Converts the binary string representation into an
	// int[] array that represents each bit in each index
	private int[] convertToBinary(String binary)
	{
		int[] bits = new int[binary.length()];

		for (int i = 0; i < binary.length(); i++)
		{
			bits[i] = Character.getNumericValue(binary.charAt(i));
		}

		return bits;
	}

	// Convert the binary int[] representation into a string
	private String binaryToString(int[] bits)
	{
		StringBuilder binary = new StringBuilder("");
		for (int i = 0; i < bits.length; i++)
		{
			binary.append(bits[i]);
		}

		return binary.toString();
	}

	// Permutates the bit representation of each character
	// into the orientation [2, 6, 3, 1, 4, 8, 5, 7]
	private void IP(int[] bits)
	{
		int[] temp = new int[bits.length];

		// Copy bits into temp
		for (int i = 0; i < bits.length; i++)
		{
			temp[i] = bits[i];
		}

		// Set each permutated index in the bit array to the
		// corresponding index of temp
		//
		// Note: each permutated index has to be subtracted by
		// 1
		bits[0] = temp[1];
		bits[1] = temp[5];
		bits[2] = temp[2];
		bits[3] = temp[0];
		bits[4] = temp[3];
		bits[5] = temp[7];
		bits[6] = temp[4];
		bits[7] = temp[6];
	}
	
	// The inverse of the IP function above
	// Permutes into [4, 1, 3, 5, 7, 2, 8, 6]
	private void inverseIP(int[] bits)
	{
		int[] temp = new int[bits.length];

		// Copy bits into temp
		for (int i = 0; i < bits.length; i++)
		{
			temp[i] = bits[i];
		}

		// Set each permutated index in the bit array to the
		// corresponding index of temp
		//
		// Note: each permutated index has to be subtracted by
		// 1
		bits[0] = temp[3];
		bits[1] = temp[0];
		bits[2] = temp[2];
		bits[3] = temp[4];
		bits[4] = temp[6];
		bits[5] = temp[1];
		bits[6] = temp[7];
		bits[7] = temp[5];
	}
}
