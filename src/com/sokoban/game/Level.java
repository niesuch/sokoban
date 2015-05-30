package com.sokoban.game;

import java.util.ArrayList;

public class Level {
	public int numberOfBoxes = 0; 	// Number of boxes
	public int numberOfGoals = 0; 	// Number of goals
	public int numberOfRows = 0; 	// Number of rows
	public int numberOfCols = 0; 	// Number of cols
	public int pusherM = 0; 		// Coordinate M of pusher
	public int pusherN = 0; 		// Coordinate N of pusher
	private ArrayList<Character> origGrid; 	// Original grid of the level
	private ArrayList<Character> grid; 		// Grid of the level

	public Level(String line, int width, int height) {
		this.numberOfRows 	= height;
		this.numberOfCols 	= width;
		this.origGrid 		= new ArrayList<Character>();
		this.grid 			= new ArrayList<Character>();

		for (int i=0; i<height*width; i++)
			origGrid.add(Character.valueOf(line.charAt(i)));

		grid = (ArrayList<Character>) origGrid.clone();

		initializePusher();
		makeFloor();

		for (int i=0; i<height*width; i++) {
			char pos = grid.get(i);
			if (pos == '*' || pos == '$')
				numberOfBoxes += 1;
			if (pos == '+' || pos == '*' || pos == '.')
				numberOfGoals += 1;
		}
	}

	public void restart() {
		grid = (ArrayList<Character>) origGrid.clone();
		initializePusher();
		makeFloor();
	}

	// Read position
	public char readPosition(int i, int j) {
		if (i<numberOfRows && j<numberOfCols && i>=0 && j>=0)
			return grid.get(numberOfCols * i + j);
		else
			return 'E';
	}

	// Write position
	public void writePosition(int i, int j, char letter) {
		if (i<numberOfRows && j<numberOfCols && i>=0 && j>=0)
			grid.set(numberOfCols*i+j, letter);
	}

	// Check if pusher can move
	public boolean pusherMove(char direction) {
		char move1 = ' ';
		char move2 = ' ';
		int i= pusherM;
		int j = pusherN;

		if (direction == 'u') {
			move1 = readPosition(i-1, j);
			move2 = readPosition(i-2, j);
		} else if (direction == 'd') {
			move1 = readPosition(i+1, j);
			move2 = readPosition(i+2, j);
		} else if (direction == 'l') {
			move1 = readPosition(i, j-1);
			move2 = readPosition(i, j-2);
		} else if (direction == 'r') {
			move1 = readPosition(i, j+1);
			move2 = readPosition(i, j+2);
		}

		if (move1 == '#' || ((move1 == '*' || move1 == '$') && (move2 == '*' || move2 == '$' || move2 == '#')))
			return false;
		else
			return true;
	}

	// Move of pusher
	public int move(char direction) {
		int action = 1;
		int i = pusherM;
		int j = pusherN;
		int i1=0, i2=0, j1=0, j2=0;
		int state=0;

		direction = Character.toLowerCase(direction);

		if (direction == 'u' && pusherMove('u')) {
			i1 = i-1;
			i2 = i-2;
			j1 = j2 = j;
			pusherM--;
		} else if (direction == 'd' && pusherMove('d')) {
			i1 = i+1;
			i2 = i+2;
			j1 = j2 = j;
			pusherM++;
		} else if (direction == 'l' && pusherMove('l')) {
			j1 = j-1;
			j2 = j-2;
			i1 = i2 = i;
			pusherN--;
		} else if (direction == 'r' && pusherMove('r')) {
			j1 = j+1;
			j2 = j+2;
			i1 = i2 = i;
			pusherN++;
		} else {
			action = 0;
			state = 0;
		}

		if (action == 1) {
			state = 1;

			if (readPosition(i, j) == '+')
				writePosition(i, j, '.');
			else
				writePosition(i, j, 's');

			if (readPosition(i1, j1) == '$' || readPosition(i1, j1) == '*') {
				if (readPosition(i2, j2) == '.')
					writePosition(i2, j2, '*');
				else
					writePosition(i2, j2, '$');

				state = 2;
			}

			if (readPosition(i1, j1) == '.' || readPosition(i1, j1) == '*')
				writePosition(i1, j1, '+');
			else
				writePosition(i1, j1, '@');
		}

		return state;
	}

	// Init start position of pusher
	private void initializePusher() {
		boolean find = false;

		for (int i=0; i<numberOfRows*numberOfCols; i++) {
			if (!find && (grid.get(i) == '@' || grid.get(i) == '+')) {
				pusherN = i%numberOfCols;
				pusherM = i/numberOfCols;
				find = true;
			}
		}
	}

	// Make floor part 1
	private void makeFloor() {
		makeFloorPart(pusherM, pusherN);

		for (int i=0; i<numberOfRows*numberOfCols; i++) {
			char pos = grid.get(i);
			if (pos == 'p')
				grid.set(i,'.');
			else if (pos == 'd')
				grid.set(i,'$');
			else if (pos == 'a')
				grid.set(i,'*');
		}
	}

	// Make floor part 2
	private void makeFloorPart(int i, int j) {
		char a = readPosition(i,j);

		if (a == ' ')
			writePosition(i,j,'s');
		else if (a == '.')
			writePosition(i,j,'p');
		else if (a == '$')
			writePosition(i,j,'d');
		else if (a == '*')
			writePosition(i,j,'a');

		if (a!='#' && a!='s' && a!='p' && a!='d' && a!='a') {
			makeFloorPart(i+1, j);
			makeFloorPart(i-1, j);
			makeFloorPart(i, j+1);
			makeFloorPart(i, j-1);
		}
	}
	
	// Check if pusher won
	public boolean is_won() {
		for (int i=0; i<numberOfRows*numberOfCols; i++) {
			if (grid.get(i) == '$')
				return false;
		}
		return true;
	}
}
