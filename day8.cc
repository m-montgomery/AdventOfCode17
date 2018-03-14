/*
AUTHOR: M. Montgomery 
DATE:   03/14/2018
FILE:   day8.cc 

PROMPT: 
--- Day 8: I Heard You Like Registers ---

You receive a signal directly from the CPU. Because of your recent assistance with jump instructions, it would like you to compute the result of a series of unusual register instructions.

Each instruction consists of several parts: the register to modify, whether to increase or decrease that register's value, the amount by which to increase or decrease it, and a condition. If the condition fails, skip the instruction without modifying the register. The registers all start at 0. The instructions look like this:

b inc 5 if a > 1
a inc 1 if b < 5
c dec -10 if a >= 1
c inc -20 if c == 10

These instructions would be processed as follows:

Because a starts at 0, it is not greater than 1, and so b is not modified. a is increased by 1 (to 1) because b is less than 5 (it is 0). c is decreased by -10 (to 10) because a is now greater than or equal to 1 (it is 1). c is increased by -20 (to -10) because c is equal to 10. After this process, the largest value in any register is 1.

You might also encounter <= (less than or equal to) or != (not equal to). However, the CPU doesn't have the bandwidth to tell you what all the registers are named, and leaves that to you to determine.

What is the largest value in any register after completing the instructions in your puzzle input?

--- Part Two ---

To be safe, the CPU also needs to know the highest value held in any register during this process so that it can decide how much memory to allocate to these operations. For example, in the above instructions, the highest value ever held was 10 (in register c after the third instruction was evaluated).
*/

#include <iostream>
#include <unordered_map>
using namespace std;

unordered_map<string, int> registers;            // hash map of registers
int largestEver = 0;                             // all registers start at 0

bool have_register(string name) {                // check if register exists
  return registers.count(name) > 0;
}

void add_register(string name) {                 // initialize register
  registers[name] = 0;
}

void update_largest(string name) {               // track largest value seen
  if (registers[name] > largestEver)
    largestEver = registers[name];
}

void inc_register(string name, int increment) {  // increment register
  registers[name] += increment;
  update_largest(name);
}

void dec_register(string name, int decrement) {  // decrement register
  registers[name] -= decrement;
  update_largest(name);
}

int get_register(string name) {                  // return register value
  return registers[name];
}

bool evaluate(int num1, string op, int num2) {   // evaluate int comparison
  return
    (op == "<" && num1 < num2)   ||
    (op == "<=" && num1 <= num2) ||
    (op == ">" && num1 > num2)   ||
    (op == ">=" && num1 >= num2) ||
    (op == "!=" && num1 != num2) ||
    (op == "==" && num1 == num2);
}

int main() {

  // vars for each component of one line of instructions
  //   format: register inc/dec number if register comparison_operator number
  //   ex:     b inc 5 if a > 1
  std::string reg1, cmd, if_, reg2, op;
  int cmdNum, opNum;

  // iterate through instructions
  while (cin >> reg1 >> cmd >> cmdNum >> if_ >> reg2 >> op >> opNum) {

    // initialize registers if first time seeing them
    if (!have_register(reg1))
      add_register(reg1);
    if (!have_register(reg2))
      add_register(reg2);
    
    // evaluate conditional
    if (evaluate(get_register(reg2), op, opNum)) {

      // increment or decrement register
      if (cmd == "inc")
	inc_register(reg1, cmdNum);
      else
	dec_register(reg1, cmdNum);
    }
  }

  // report largest value in registers at end
  int largest = 0;
  for (unordered_map<string, int>::iterator it = registers.begin();
       it != registers.end(); it++) {
    if (it->second > largest)
      largest = it->second;
  }
  cout << "The largest value is " << largest << "." << endl;

  // PART TWO
  // report largest value in registers at any point during process
  cout << "The largest value ever seen was " << largestEver << "." << endl;
  
  return 0;
}
