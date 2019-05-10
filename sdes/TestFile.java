/*	Author: Syed Faraz Abrar (19126296)
 *	Purpose: Tests SDES using the provided test file
 *	Last modified: 18/04/2019
 */

import java.io.*;
import java.util.Scanner;

public class TestFile
{
	public static void main(String[] args)
	{
		String keyString = Helper.inputKey();
		File file;

		file = new File("testfile-SDES.txt");
		Key key = new Key(keyString);
		SDES test = new SDES(file, key);

		System.out.println("Encrypted and decrypted testfile-SDES.txt");
		System.out.println("Files created: encrypted.txt, decrypted.txt");
	}
}
