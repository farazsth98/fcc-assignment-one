/*	Author: Syed Faraz Abrar (19126296)
 *	Purpose: Handles the creation of an SDES key
 *			 Uses int[] array indexes to represent
 *			 each bit of the key
 *	Last modified: 18/04/2019
 */

public class Key
{
	private int[] key; // The original input key
	private int[] k1; // First 8 bit key
	private int[] k2; // Second 8 bit key

	public Key(String k)
	{
		key = new int[10]; // 10 bit original key
		k1 = new int[8]; // 8 bit subkey 1
		k2 = new int[8]; // 8 bit subkey 1

		this.makeKey(k);
	}

	// Returns subkey 1 as an int[]
	public int[] getK1()
	{
		// Create a copy of k1
		int[] temp = new int[k1.length];

		for (int i = 0; i < k1.length; i++)
		{
			temp[i] = k1[i];
		}

		return temp;
	}

	// Returns subkey 2 as an int[]
	public int[] getK2()
	{
		// Create a copy of k2
		int[] temp = new int[k2.length];

		for (int i = 0; i < k2.length; i++)
		{
			temp[i] = k2[i];
		}

		return temp;
	}

	// Calls all the function in order to create the key
	private void makeKey(String k)
	{
		// Creates the key from the string input of the user and stores
		// it in the private key variable
		this.getKey(k);

		// Permutates the key using P10
		this.P10();

		// Bit shifts the key to the left by 1
		this.firstLS();

		// Permutates the key using P8 to produce subkey 1 and stores it
		// in the private k1 variable
		this.firstP8();

		// Bit shifts the key to the left by 2
		this.secondLS();

		// Permutates the key using P8 to produce subkey 2 and stores it
		// in the private k2 variable
		this.secondP8();
	}

	// This function transforms the key
	// into the two key parts
	//
	// Assumption: String key is a string of 1's and 0's
	// of length 10
	// i.e "1000100101"
	private void getKey(String k)
	{
		for (int i = 0; i < 10; i++)
		{
			key[i] = Character.getNumericValue(k.charAt(i));
		}
	}

	// Permutates the key to the P10 orientation, defined as
	// [3, 5, 2, 7, 4, 10, 1, 9, 8, 6]
	//
	// i.e it moves the bits around so they match the layout
	// as shown on the above
	private void P10()
	{
		int[] temp = new int[key.length];

		// Copy key into temp
		for (int i = 0; i < key.length; i++)
		{
			temp[i] = key[i];
		}
		
		// Set each permutated index in the key array to the
		// corresponding index of temp
		//
		// Note: each permutated index has to be subtracted by
		// 1
		key[0] = temp[2];
		key[1] = temp[4];
		key[2] = temp[1];
		key[3] = temp[6];
		key[4] = temp[3];
		key[5] = temp[9];
		key[6] = temp[0];
		key[7] = temp[8];
		key[8] = temp[7];
		key[9] = temp[5];
	}

	// Permutates the key to the P8 orientation, defined as
	// [6, 3, 7, 4, 8, 5, 10, 9]
	//
	// Stores the permutated key in k1 as this results in
	// the first subkey
	private void firstP8()
	{
		// Set each permutated index in the k1 array to the
		// corresponding index of key and store it in k1
		//
		// Note: each permutated index has to be subtracted by
		// 1
		k1[0] = key[5];
		k1[1] = key[2];
		k1[2] = key[6];
		k1[3] = key[3];
		k1[4] = key[7];
		k1[5] = key[4];
		k1[6] = key[9];
		k1[7] = key[8];
	}

	// Permutates the key to the P8 orientation, defined as
	// [6, 3, 7, 4, 8, 5, 10, 9]
	//
	// Stores the permutated key in k2 as this results in
	// the second subkey
	private void secondP8()
	{
		// Set each permutated index in the k2 array to the
		// corresponding index of key and store it in k2
		//
		// Note: each permutated index has to be subtracted by
		// 1

		k2[0] = key[5];
		k2[1] = key[2];
		k2[2] = key[6];
		k2[3] = key[3];
		k2[4] = key[7];
		k2[5] = key[4];
		k2[6] = key[9];
		k2[7] = key[8];
	}

	// Shift each half of the key to the left by 1
	private void firstLS()
	{
		int[] temp = new int[key.length];

		// Copy key into temp
		for (int i = 0; i < key.length; i++)
		{
			temp[i] = key[i];
		}

		// Shift left half of key values to the left by 1
		key[4] = temp[0]; // Manually set due to circular shift

		for (int i = 0; i < 4; i++)
		{
			key[i] = temp[i+1];
		}

		// Shift right half of key values to the left by 1
		key[9] = temp[5];

		for (int i = 5; i < 9; i++)
		{
			key[i] = temp[i+1];
		}
	}

	// Shift each half of the key to the left by 2
	private void secondLS()
	{
		int[] temp = new int[key.length];

		// Copy key into temp
		for (int i = 0; i < key.length; i++)
		{
			temp[i] = key[i];
		}

		// Shift left half of key values to the left by 1
		key[3] = key[0]; // Manually set due to circular shift
		key[4] = key[1]; // Manually set due to circular shift

		for (int i = 0; i < 3; i++)
		{
			key[i] = temp[i+2];
		}

		// Shift right half of key values to the left by 1
		key[8] = key[5]; // Manually set due to circular shift
		key[9] = key[6]; // Manually set due to circular shift

		for (int i = 5; i < 8; i++)
		{
			key[i] = temp[i+2];
		}
	}
}
