/*
AUTHOR: M. Montgomery (github.com/m-montgomery)
DATE:   1/10/18
FILE:   day2.java

PROMPT:

--- Day 2: Corruption Checksum ---

As you walk through the door, a glowing humanoid shape yells in your direction. "You there! Your state appears to be idle. Come help us repair the corruption in this spreadsheet - if we take another millisecond, we'll have to display an hourglass cursor!"

The spreadsheet consists of rows of apparently-random numbers. To make sure the recovery process is on the right track, they need you to calculate the spreadsheet's checksum. For each row, determine the difference between the largest value and the smallest value; the checksum is the sum of all of these differences.

For example, given the following spreadsheet:

5 1 9 5
7 5 3
2 4 6 8

The first row's largest and smallest values are 9 and 1, and their difference is 8. The second row's largest and smallest values are 7 and 3, and their difference is 4. The third row's difference is 6. In this example, the spreadsheet's checksum would be 8 + 4 + 6 = 18.

What is the checksum for the spreadsheet in your puzzle input?

--- Part Two ---

"Great work; looks like we're on the right track after all. Here's a star for your effort." However, the program seems a little worried. Can programs be worried?

"Based on what we're seeing, it looks like all the User wanted is some information about the evenly divisible values in the spreadsheet. Unfortunately, none of us are equipped for that kind of calculation - most of us specialize in bitwise operations."

It sounds like the goal is to find the only two numbers in each row where one evenly divides the other - that is, where the result of the division operation is a whole number. They would like you to find those numbers on each line, divide them, and add up each line's result.

For example, given the following spreadsheet:

5 9 2 8
9 4 7 3
3 8 6 5

In the first row, the only two numbers that evenly divide are 8 and 2; the result of this division is 4. In the second row, the two numbers are 9 and 3; the result is 3. In the third row, the result is 2. In this example, the sum of the results would be 4 + 3 + 2 = 9.

What is the sum of each row's result in your puzzle input?
*/

import java.util.Scanner;
import java.util.Arrays;

public class day2 {
    public static void main(String[] args) {

	// initialize
	int checkSum = 0;  // part one answer
	int divSum = 0;    // part two answer
	Scanner in = new Scanner(System.in);

	// for every row of numbers
	while (in.hasNext()) {

	    // gather each character (number)
	    String[] row = in.nextLine().split("\\s"); // for part one
	    int[] nums = new int[row.length];          // for part two

	    // PART ONE
	    // find min and max
	    int max = Integer.parseInt(row[0]);
	    int min = Integer.parseInt(row[0]);
	    for (int i = 0; i < row.length; i++) {
		int num = Integer.parseInt(row[i]);
		if (num > max)      // check for new maximum
		    max = num;
		else if (num < min) // check for new minimum
		    min = num;
		nums[i] = num;      // save num for part two
	    }

	    // add to check sum
	    checkSum += max - min;

	    
	    // PART TWO
	    Arrays.sort(nums);  // arrange smallest to largest

	    // for all i,j in row (where i <= j)
	    for (int i = 0; i < nums.length-1; i++) {
		for (int j = i+1; j < nums.length; j++) {
		    if (nums[j] % nums[i] == 0)
			divSum += nums[j] / nums[i];  // add result of even div
		}
	    }
	}

	// report results
	System.out.println("Part One: " + checkSum);
	System.out.println("Part Two: " + divSum);
    }
}
