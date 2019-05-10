/*	Author: Syed Faraz Abrar (19126296)
 *	Purpose: This class contains a function used to print out a
 *			 histogram that shows the distribution of how often
 *			 each character appears in the given input file
 *	Last modified: 18/04/2019
 */

import java.io.*;
import java.util.Scanner;

public class Frequency
{
	// This function plots a graph that shows how often each character
	// shows up in a test file with the given imported filename
	public static void plot(String filename)
	{
		File file;
		Scanner sc;
		String line;
		int[] frequency = new int[26]; // 26 letters
		int charIndex; // indexes for each character

		// Initialize all indexes of frequency to 0
		for (int i = 0; i < frequency.length; i++)
		{
			frequency[i] = 0;
		}
	
		try
		{
			file = new File(filename);
			sc = new Scanner(file);

			while (sc.hasNextLine())
			{
				line = sc.nextLine().toLowerCase();

				for (int i = 0; i < line.length(); i++)
				{
					if (line.charAt(i) >= 'a' &&
						line.charAt(i) <= 'z')
					{
						// charIndex: 0 for 'a', 1 for 'b'
						//    ...     25 for 'z'
						charIndex = line.charAt(i) - 'a';

						// Increment the corresponding index
						(frequency[charIndex])++;
					}
				}
			}
		}

		catch (IOException e) { System.out.println(e.getMessage()); }

		char c;

		// Plot *'s to show frequency for each character
		for (int i = 0; i < frequency.length; i++)
		{
			// Converts each index to its corresponding
			// character
			c = (char) (i + 'a');

			System.out.print(c + ": ");
			for (int j = 0; j < frequency[i]; j++)
			{
				System.out.print("*");
			}
			System.out.println();
		}
	}
}
