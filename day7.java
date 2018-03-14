/*
  AUTHOR: M. Montgomery 
  DATE:   03/14/2018
  FILE:   day7.java 

  PROMPT: 

  --- Day 7: Recursive Circus ---

  Wandering further through the circuits of the computer, you come upon a tower of programs that have gotten themselves into a bit of trouble. A recursive algorithm has gotten out of hand, and now they're balanced precariously in a large tower.

  One program at the bottom supports the entire tower. It's holding a large disc, and on the disc are balanced several more sub-towers. At the bottom of these sub-towers, standing on the bottom disc, are other programs, each holding their own disc, and so on. At the very tops of these sub-sub-sub-...-towers, many programs stand simply keeping the disc below them balanced but with no disc of their own.

  You offer to help, but first you need to understand the structure of these towers. You ask each program to yell out their name, their weight, and (if they're holding a disc) the names of the programs immediately above them balancing on that disc. You write this information down (your puzzle input). Unfortunately, in their panic, they don't do this in an orderly fashion; by the time you're done, you're not sure which program gave which information.

  For example, if your list is the following:

  pbga (66)
  xhth (57)
  ebii (61)
  havc (66)
  ktlj (57)
  fwft (72) -> ktlj, cntj, xhth
  qoyq (66)
  padx (45) -> pbga, havc, qoyq
  tknk (41) -> ugml, padx, fwft
  jptl (61)
  ugml (68) -> gyxo, ebii, jptl
  gyxo (61)
  cntj (57)

  ...then you would be able to recreate the structure of the towers that looks like this:

  gyxo
  /     
  ugml - ebii
  /      \     
  |         jptl
  |        
  |         pbga
  /        /
  tknk --- padx - havc
  \        \
  |         qoyq
  |             
  |         ktlj
  \      /     
  fwft - cntj
  \     
  xhth

  In this example, tknk is at the bottom of the tower (the bottom program), and is holding up ugml, padx, and fwft. Those programs are, in turn, holding up other programs; in this example, none of those programs are holding up any other programs, and are all the tops of their own towers. (The actual tower balancing in front of you is much larger.)

  Before you're ready to help them, you need to make sure your information is correct. What is the name of the bottom program?

  --- Part Two ---

  The programs explain the situation: they can't get down. Rather, they could get down, if they weren't expending all of their energy trying to keep the tower balanced. Apparently, one program has the wrong weight, and until it's fixed, they're stuck here.

  For any program holding a disc, each program standing on that disc forms a sub-tower. Each of those sub-towers are supposed to be the same weight, or the disc itself isn't balanced. The weight of a tower is the sum of the weights of the programs in that tower.

  In the example above, this means that for ugml's disc to be balanced, gyxo, ebii, and jptl must all have the same weight, and they do: 61.

  However, for tknk to be balanced, each of the programs standing on its disc and all programs above it must each match. This means that the following sums must all be the same:

  ugml + (gyxo + ebii + jptl) = 68 + (61 + 61 + 61) = 251
  padx + (pbga + havc + qoyq) = 45 + (66 + 66 + 66) = 243
  fwft + (ktlj + cntj + xhth) = 72 + (57 + 57 + 57) = 243

  As you can see, tknk's disc is unbalanced: ugml's stack is heavier than the other two. Even though the nodes above ugml are balanced, ugml itself is too heavy: it needs to be 8 units lighter for its stack to weigh 243 and keep the towers balanced. If this change were made, its weight would be 60.

  Given that exactly one program is the wrong weight, what would its weight need to be to balance the entire tower?
*/

import java.util.ArrayList;
import java.util.Scanner;

public class day7 {
    public static void main(String[] args) {
	Scanner in = new Scanner(System.in);
	ArrayList<program> towers = new ArrayList<program>();

	// read in all programs
	while (in.hasNext()) {
	    String[] line = in.nextLine().split(" ");  // split on space
	    String name = line[0];
	    
	    // remove parens & cast to get weight
	    int weight = Integer.parseInt(line[1].substring
					  (1, line[1].length() - 1));
	    program curr = new program(name, weight);

	    // read in children's names
	    for (int i = 3; i < line.length; i++) {    // skip arrow: start at 3
		name = line[i].replace(",", "");       // remove commma
		curr.addChild(name);
	    }
	    towers.add(curr);                          // add program to list
	}

	// set all parent & child references
	for (program parent : towers) {
	    for (String childName : parent.getChildrenNames()) {
		for (program childProgram : towers) {
		    if (childName.equals(childProgram.getName())) {
			childProgram.setParent(parent);
			parent.addChild(childProgram);
		    }
		}
	    }
	}
	
	// find base (the only program without a parent)
	program base = null;
	for (program p : towers) {
	    if (!p.hasParent())
		base = p;
	}
	System.out.println("The base tower is " + base.getName() + ".");

	// PART TWO
	// find program with incorrect weight; report its required weight
	int neededWeight = findNeededWeight(base);
	System.out.println("The needed weight is " + neededWeight + ".");
    }

    static int findNeededWeight(program curr) {

	// if current program has no children, return
	int numChildren = curr.getChildren().size();
	if (numChildren == 0)
	    return -1;

	// vars for tracking which weight is incorrect
	int weight1 = 0;              // save values of weights for comparison
	int weight2 = 0;
	int weight1Count = 0;         // track how many seen of each weight
	int weight2Count = 0;
	program firstProgram = null;  // save in case this program is wrong
	ArrayList<program> children = curr.getChildren();   // the children

	// iterate through children to see if any has unusual weight
	for (int i = 0; i < numChildren; i++) {

	    // have child check its own children
	    program child = children.get(i);
	    int neededWeight = findNeededWeight(child);
	    if (neededWeight != -1)
		return neededWeight;

	    int weight = child.totalWeight();

	    // save first child's weight as weight1
	    if (i == 0) {
		weight1 = weight;
		weight1Count++;
		firstProgram = child;
	    }
	    
	    // saw another weight1; weight1 is the correct weight
	    else if (weight == weight1)
		weight1Count++;

	    // weight2 is the correct weight; first child is wrong
	    else if (weight2Count >= 1) {

		int wrongWeight = firstProgram.totalWeight();
		int diff = wrongWeight > weight2 ?
		    weight2 - wrongWeight :
		    wrongWeight - weight2;
		return firstProgram.getWeight() + diff;
	    }

	    // weight2 is the wrong weight; current child is wrong
	    else if (weight1Count > 1) {
		int wrongWeight = child.totalWeight();
		int diff = wrongWeight > weight1 ?
		    weight1 - wrongWeight :
		    wrongWeight - weight1;
		return child.getWeight() + diff;
	    }

	    // found a weight2 (don't know which weight is correct yet)
	    else {
		weight2 = weight;
		weight2Count++;
	    }
	}

	// all children are the same weight; this program is balanced
	return -1;
    }
}


class program {
    // could have made all these public instead of having get&set methods
    // but I prefer to keep variables private:
    String name;                     // program name (given from input)
    int weight;                      // program weight (given from input)
    program parent;                  // reference to parent, if any
    ArrayList<String> childrenNames; // entered when reading in all programs
    ArrayList<program> children;     // filled in after all programs created

    program(String n, int w) {
	name = n;
	weight = w;
	parent = null;
	children = new ArrayList<program>();
	childrenNames = new ArrayList<String>();
    }

    String getName()                     { return name; }
    
    void addChild(String child)          { childrenNames.add(child); }
    void addChild(program child)         { children.add(child); }
    
    void setParent(program p)            { parent = p; }
    boolean hasParent()                  { return parent != null; }
    program getParent()                  { return parent; }

    ArrayList<program> getChildren()     { return children; }
    ArrayList<String> getChildrenNames() { return childrenNames; }

    int getWeight()                      { return weight; }

    // weight of self and all children
    int totalWeight() {
	int totalWeight = weight;
	for (program child : children)
	    totalWeight += child.totalWeight();
	return totalWeight;
    }
}
