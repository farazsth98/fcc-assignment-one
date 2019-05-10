/*	Author: Syed Faraz Abrar (19126296)
 *	Purpose: Tests SDES using user provided input
 *	Last modified: 18/04/2019
 */

import java.util.Scanner;

public class TestUser
{
	public static void main(String[] args)
	{
		String keyString = Helper.inputKey();
		Key key = new Key(keyString);
		Scanner sc = new Scanner(System.in);
		String input;

		System.out.print("Please enter text to encrypt: ");
		input = sc.nextLine();

		SDES test = new SDES(input, key);

		test.printEncrypted();
		test.printDecrypted();
	}
}
