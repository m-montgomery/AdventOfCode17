/*
AUTHOR: M. Montgomery (github.com/m-montgomery)
DATE:   1/11/18
FILE:   day3.cc

PROMPT:

--- Day 3: Spiral Memory ---

You come across an experimental new kind of memory stored on an infinite two-dimensional grid. Each square on the grid is allocated in a spiral pattern starting at a location marked 1 and then counting up while spiraling outward. For example, the first few squares are allocated like this:

17  16  15  14  13
18   5   4   3  12
19   6   1   2  11
20   7   8   9  10
21  22  23---> ...

While this is very space-efficient (no squares are skipped), requested data must be carried back to square 1 (the location of the only access port for this memory system) by programs that can only move up, down, left, or right. They always take the shortest path: the Manhattan Distance between the location of the data and square 1.

For example: Data from square 1 is carried 0 steps, since it's at the access port. Data from square 12 is carried 3 steps, such as: down, left, left. Data from square 23 is carried only 2 steps: up twice. Data from square 1024 must be carried 31 steps.

How many steps are required to carry the data from the square identified in your puzzle input all the way to the access port?
*/
#include <iostream>
#include <math.h>

// NEAREST SQUARE
// IN:  Integer z
// OUT: Largest square number <= z
int nearestSquare(int z) {
  if (z < 1)
    return 0;
  return pow(floor(sqrt(z)), 2);
}

// GET SQUARE COORDS
// IN:  Square integer, x and y variables by ref
// OUT: x and y coordinates calculated for square
void getSquareCoords(int sqr, int & x, int & y) {
  if (sqr % 2 == 0) {
    x = - (sqrt(sqr) / 2);
    y = x + 1;
  }
  else {
    x = (sqrt(sqr) - 1) / 2;
    y = x;
  }
}

// GET COORDS
// IN:  Integer value, nearest square <= value, x and y coordinates of square
// OUT: x and y coordinates calculated for value
void getCoords(int value, int sqr, int & x, int & y) {

  // check if coords already accurate
  if (value == sqr)
    return;

  // move to first corner (squares always 1 step from a corner)
  y += (sqr % 2 == 0) ? -1 : 1;     // move left if even, right if odd
  int curr = sqr + 1;

  // set up traversal
  int step = sqrt(sqr);             // length of side == sqrt(sqr)
  int dy = 0;
  int dx = (sqr % 2 == 0) ? 1 : -1; // move down if even, up if odd

  // traverse spiral grid
  while (curr != value) {           // max loops < 2*sqrt(sqr)
    x += dx;
    y += dy;
    curr++;

    // handle 2nd corner (will only ever have two since sqr <= value < next sqr)
    if (curr == sqr+step) { 
      dx = 0;
      dy = (sqr % 2 == 0) ? 1 : -1; // move right if even, left if odd
    }
  }
}

// STEPS TO PORT
// IN:  x and y coordinates
// OUT: Number of steps to port (value 1 in grid)
int stepsToPort(int x, int y) {
  return abs(x) + abs(y);
}

// MAIN
// IN:  Integer value
// OUT: Steps to port (value 1) in spiral grid
int main() {
  
  // get starting value
  int input;
  std::cin >> input;

  // PART ONE
  
  // get grid coords of nearest square
  int sqr = nearestSquare(input);
  int x, y;
  getSquareCoords(sqr, x, y);

  // calculate coords of starting value
  getCoords(input, sqr, x, y);

  // display steps from value to port
  int steps = stepsToPort(x, y);
  std::cout << "Steps: " << steps << std::endl;

  return 0;
}
