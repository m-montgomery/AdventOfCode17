"""
AUTHOR: M. Montgomery (github.com/m-montgomery)
DATE:   1/10/18
FILE:   day1.py

PROMPT:

--- Day 1: Inverse Captcha ---

The captcha requires you to review a sequence of digits (your puzzle input) and find the sum of all digits that match the next digit in the list. The list is circular, so the digit after the last digit is the first digit in the list.

What is the solution to your captcha?

--- Part Two ---

Now, instead of considering the next digit, it wants you to consider the digit halfway around the circular list. That is, if your list contains 10 items, only include a digit in your sum if the digit 10/2 = 5 steps forward matches it. Fortunately, your list has an even number of elements.

What is the solution to your new captcha?

"""
import sys

# get captcha as one string
captcha = sys.stdin.read().strip()

# PART ONE

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
print("Part One:", total)


# PART TWO

# save vals to reduce runtime calculations
total = 0
length = len(captcha)
step = length//2

# compare each character to character halfway around list
for index in range(length):
    currentOne = captcha[index]
    nextOne = captcha[(index+step)%length]  # mod by length to wrap around list
    if currentOne == nextOne:
        total += int(currentOne)            # convert to integer for sum

# output result
print("Part Two:", total)
