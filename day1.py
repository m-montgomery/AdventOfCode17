"""
AUTHOR: M. Montgomery (github.com/m-montgomery)
DATE:   1/10/18
FILE:   day1.py

PROMPT:

You're standing in a room with "digitization quarantine" written in LEDs along one wall. The only door is locked, but it includes a small interface. "Restricted Area - Strictly No Digitized Users Allowed."

It goes on to explain that you may only leave by solving a captcha to prove you're not a human. Apparently, you only get one millisecond to solve the captcha: too fast for a normal human, but it feels like hours to you.

The captcha requires you to review a sequence of digits (your puzzle input) and find the sum of all digits that match the next digit in the list. The list is circular, so the digit after the last digit is the first digit in the list.

For example:

    1122 produces a sum of 3 (1 + 2) because the first digit (1) matches the second digit and the third digit (2) matches the fourth digit.
    1111 produces 4 because each digit (all 1) matches the next.
    1234 produces 0 because no digit matches the next.
    91212129 produces 9 because the only digit that matches the next one is the last digit, 9.

What is the solution to your captcha?
"""
import sys

# get captcha as one string
captcha = sys.stdin.read().strip()

# compare each character to next
total = 0
for index in range(len(captcha)-1):
    currentOne = captcha[index]      # no need to convert to integers here
    nextOne = captcha[index+1]
    if currentOne == nextOne:
        total += int(currentOne)     # convert to integer for sum

# check first and last (circular list)
if captcha[0] == captcha[-1]:
    total += int(captcha[0])

# output result
print(total)
