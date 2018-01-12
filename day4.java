/*
AUTHOR: M. Montgomery (github.com/m-montgomery)
DATE:   1/12/18
FILE:   day4.java

PROMPT:

--- Day 4: High-Entropy Passphrases ---

A new system policy has been put in place that requires all accounts to use a passphrase instead of simply a password. A passphrase consists of a series of words (lowercase letters) separated by spaces. To ensure security, a valid passphrase must contain no duplicate words.

For example: aa bb cc dd ee is valid. aa bb cc dd aa is not valid - the word aa appears more than once. aa bb cc dd aaa is valid - aa and aaa count as different words.

The system's full passphrase list is available as your puzzle input. How many passphrases are valid?

--- Part Two ---

For added security, yet another system policy has been put in place. Now, a valid passphrase must contain no two words that are anagrams of each other - that is, a passphrase is invalid if any word's letters can be rearranged to form any other word in the passphrase.

For example: abcde fghij is a valid passphrase. abcde xyz ecdab is not valid - the letters from the third word can be rearranged to form the first word. a ab abc abd abf abj is a valid passphrase, because all letters need to be used when forming another word. iiii oiii ooii oooi oooo is valid. oiii ioii iioi iiio is not valid - any of these words can be rearranged to form any other word.

Under this new system policy, how many passphrases are valid?
*/

import java.util.Scanner;
import java.util.HashSet;
import java.util.Arrays;

public class day4 {
    public static void main(String[] args) {

	// set up
	int total;       // total number of phrases
	int totalValid;  // total number of valid phrases
	boolean checkAnagrams = true;        // true for part two
	Scanner in = new Scanner(System.in);

	// check each line (phrase) for validity
	for (total = 0, totalValid = 0; in.hasNext(); total++)
	    if (isValid(in.nextLine(), checkAnagrams))
		totalValid++;

	// report result
	System.out.println("Total valid: " + totalValid + " / " + total);
    }

    // IS VALID
    // Determines phrase validity (each word must be unique)
    static boolean isValid(String phrase, boolean checkAnagrams) {

	// separate phrase into words
	String[] splitPhrase = phrase.split(" ");

	// add each word to set to ensure unique
	HashSet<String> seenWords = new HashSet<String>(splitPhrase.length);
	for (String word : splitPhrase) {

	    // sort word first to avoid anagrams (part two)
	    if (checkAnagrams) {
		char[] wordChars = word.toCharArray();
		Arrays.sort(wordChars);
		word = new String(wordChars);
	    }

	    // check for uniqueness
	    if (seenWords.contains(word))   // word was repeated
		    return false;
	    seenWords.add(word);            // word is unique
	}
	return true;
    }
}
