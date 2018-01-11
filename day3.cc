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

--- Part Two ---

As a stress test on the system, the programs here clear the grid and then store the value 1 in square 1. Then, in the same allocation order as shown above, they store the sum of the values in all adjacent squares, including diagonals.

So, the first few squares' values are chosen as follows: Square 1 starts with the value 1. Square 2 has only one adjacent filled square (with value 1), so it also stores 1. Square 3 has both of the above squares as neighbors and stores the sum of their values, 2. Square 4 has all three of the aforementioned squares as neighbors and stores the sum of their values, 4. Square 5 only has the first and fourth squares as neighbors, so it gets the value 5.

Once a square is written, its value does not change. Therefore, the first few squares would receive the following values:

147  142  133  122   59
304    5    4    2   57
330   10    1    1   54
351   11   23   25   26
362  747  806--->   ...

What is the first value written that is larger than your puzzle input?
*/
#include <iostream>
#include <math.h>
#include <unordered_map>


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

// PAIR HASH
// Defined to allow use of pairs as keys in unordered_map
struct pairHash {
public:
  template <typename first, typename second>
  std::size_t operator()(const std::pair<first, second> &val) const {
    return std::hash<first>()(val.first) ^ std::hash<second>()(val.second);
  }
};

// FIND LARGER
// IN:  Integer x
// OUT: First value in sum spiral grid > x
int findLarger(int value) {

  // init grid: key = pair of grid coords; val = value at x,y
  std::unordered_map<std::pair<int, int>, int, pairHash> grid;
  grid[std::make_pair(0,0)] = 1;  // add center value

  // set up movement
  int step = 1;          // steps to next corner
  int curr = 1;          // current value
  int x = 0;             // current coordinates
  int y = 0;

  // set up directions
  // dx, dy: 0,1 (right) -1,0 (up) 0,-1 (left) 1,0 (down)
  int dir = 0;              // begin moving right
  int dx[] = {0, -1, 0, 1};
  int dy[] = {1, 0, -1, 0};
  
  // build the spiral until found larger value
  while (curr <= value) {

    // while not turning a corner
    for (int i = 0; i < step; i++) {

      // take a step
      x += dx[dir];
      y += dy[dir];
      
      // sum surrounding spots
      curr = 0;
      for (int adjX = -1; adjX <= 1; adjX++) {    // for every adjacent x
	for (int adjY = -1; adjY <= 1; adjY++) {  // for every adjacent y
	  if (adjX == 0 && adjY == 0)             // (don't check self)
	    continue;

	  // if grid contains coords (adjX, adjY), add value to sum
	  std::pair<int, int> key = std::make_pair(x+adjX, y+adjY);
	  if (grid.count(key) != 0)
	    curr += grid.find(key)->second;       // get value from iterator
	}
      }
      
      // handle newly calculated spot
      grid[std::make_pair(x,y)] = curr;   // add value to grid
      if (curr > value)                   // check for completion
	return curr;
    }

    // increase step before moving across (right or left)
    if (dir % 2 == 1)
      step++;
    dir = (dir + 1) % 4; // change direction
  }
  return curr;
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


  // PART TWO

  int larger = findLarger(input);
  std::cout << "Larger: " << larger << std::endl;  

  return 0;
}
