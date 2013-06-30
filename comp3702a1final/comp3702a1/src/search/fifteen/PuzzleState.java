/**
 * @author SU Sheng Loong 42397997
 * @author TEE Lip Jian 42430942
 */
package search.fifteen;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import search.Action;
import search.ActionStatePair;
import search.State;

/**
 * The State that implements the Fifteen-Puzzle.
 */
public final class PuzzleState implements State {

	/** The board configuration of this state */
	public final int[][] tiles;
	/** The action "to slide the empty space to the left" */
	public static Action MOVE_LEFT  = new Action("LEFT");
	/** The action "to slide the empty space to the right" */
	public static Action MOVE_RIGHT = new Action("RIGHT");
	/** The action "to slide the empty space upwards" */
	public static Action MOVE_UP    = new Action("UP");
	/** The action "to slide the empty space downwards" */
	public static Action MOVE_DOWN  = new Action("DOWN");
	/** The order in which the actions are tested */
	private static Action[] actionSequence={MOVE_LEFT, MOVE_RIGHT, MOVE_UP, MOVE_DOWN};
	/**
	 * The constructor initialises the board to the goal configuration.
	 * From there all sequences of legal actions will render a puzzle that can be solved. 
	 */
	public PuzzleState() {
		this.tiles=new int[5][5];
		for (int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				this.tiles[i][j]=i*5+j+1;
			}
		}
		tiles[4][4]=0; // empty
	}

	/**
	 * A new state is created but the board configuration of the supplied state is copied.
	 * @param state the state to copy
	 */
	public PuzzleState(PuzzleState state) {
		this.tiles=new int[5][5];
		for (int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				this.tiles[i][j]=state.tiles[i][j];
			}
		}
	}

	/**
	 * A new state is created but the board configuration of the supplied state is copied.
	 * @param state the 4x4 representation of state to copy
	 */
	public PuzzleState(int[][] state) {
		this.tiles=new int[5][5];
		for (int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				this.tiles[i][j]=state[i][j];
			}
		}
	}

	/**
	 * Create a new state by looking at another state and performing an action.
	 * @param origin the original state
	 * @param action the action which is taken
	 * @throws RuntimeException if the action is invalid
	 */
	public PuzzleState(PuzzleState origin, Action action) {
		this(origin);
		performAction(this,action);
	}
	
	/**
	 * This method accepts a puzzle state and then transform it by applying 
	 * the action.
	 * 
	 * @param origin original puzzle state
	 * @param action action taken to transform the state
	 */
	public static void performAction(PuzzleState origin, Action action) {
		int rowEmpty=0, colEmpty=0;
		for (int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				if (origin.tiles[i][j]==0) {
					rowEmpty=i;
					colEmpty=j;
					break;
				}
			}
		}
		// Check which action is taken, check if it is "valid", if so carry out the action (change the board)
		if (action==MOVE_UP && rowEmpty!=0) { 
			origin.tiles[rowEmpty][colEmpty]=origin.tiles[rowEmpty-1][colEmpty]; // fill empty spot with moved tile
			origin.tiles[rowEmpty-1][colEmpty]=0; // new empty spot
		} else if (action==MOVE_DOWN && rowEmpty!=4) {
			origin.tiles[rowEmpty][colEmpty]=origin.tiles[rowEmpty+1][colEmpty];
			origin.tiles[rowEmpty+1][colEmpty]=0;
		} else if (action==MOVE_LEFT && colEmpty!=0) {
			origin.tiles[rowEmpty][colEmpty]=origin.tiles[rowEmpty][colEmpty-1];
			origin.tiles[rowEmpty][colEmpty-1]=0;
		} else if (action==MOVE_RIGHT && colEmpty!=4) {
			origin.tiles[rowEmpty][colEmpty]=origin.tiles[rowEmpty][colEmpty+1];
			origin.tiles[rowEmpty][colEmpty+1]=0;
		} else {
			throw new RuntimeException("Illegal move"); // illegal move
		}
	}

	/**
	 * Check if this state is a goal state:
	 * |01 02 03 04 05|
	 * |06 07 08 09 10|
	 * |11 12 13 14 15|
	 * |16 17 18 19 20|
	 * |21 22 23 24 00|
	 * @return true if the state is a goal, false otherwise
	 */
	public boolean goal() {
		for (int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				if (tiles[i][j]!=i*5+j+1) {
					if (i==4 && j==4)
						return true;
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * The successor function that generates all valid ActionStatePairs from the current state.
	 * @return an array of all valid Action State pairs
	 */
	public ActionStatePair[] successor() {
		//ArrayList list=new ArrayList<ActionStatePair>();
		ArrayList list=new ArrayList();
		for (int a=0; a<actionSequence.length; a++) {
			try {
				State state=new PuzzleState(this, actionSequence[a]);
				list.add(new ActionStatePair(actionSequence[a], state));
			} catch (RuntimeException e) {
				;	// illegal move
			} 
		}
		ActionStatePair[] pairs=new ActionStatePair[list.size()];
		Iterator iter=list.iterator();
		for (int i=0; iter.hasNext(); i++) {
			pairs[i]=(ActionStatePair)iter.next();
		}
		return pairs;
	}

	/**
	 * Determine the cost of taking the specified move from this state.
	 * @return the path cost
	 */
	public double pathcost(Action action) {
		return 1;
	}

	/**
	 * Heuristic function 1:
	 * This method accepts tiles of a state in the two-
	 * dimensional array form and loops through each of every 
	 * tile to check if it is misplaced. If it is, the counter
	 * will be incremented. At the end of the method, the 
	 * counter will be returned. 
	 *  
	 * @param tiles Tiles of a state
	 * @return number of misplaced tiles
	 */
	public static int misplacedTiles(int[][] tiles) {
		int num = 0; // initialise counter to zero
		// For each row
		for (int i=0; i<5; i++) {
			// for each column in the particular row
			for (int j=0; j<5; j++) {
				// if tile is not zero and is misplaced
				if (tiles[i][j]!=0 && tiles[i][j]!=i*5+j+1) {
					num++; // increment counter
				}
			}
		}
		return num;
	}

	/**
	 * Heuristic function 2:
	 * The function loops through each row and column to check if the tile is 
	 * misplaced. If the tile is misplaced, the function will calculate the 
	 * distance of the current tile to its desired position, excluding the 
	 * blank tile.
	 * @param states	tiles of current state
	 * @return int		the total Manhattan Distance
	 */
	public static int mDistance(int[][] states) {
		int totalDist = 0,dRow,dCol;
		for(int row = 0; row < 5; row++) {
			for(int col = 0; col < 5; col++) {
				//If current tile is not a blank tile
				if(states[row][col] != 0) {
					//Determine Row Distance (dRow)
					totalDist += Math.abs(row - states[row][col] / 6);
					//Determine Column distance (dCol)
					if((dCol = states[row][col] % 5) != 0) {
						//for value that is a factor of 1 to 4
						totalDist += Math.abs(col - (dCol - 1));
					} else {
						//for value that is factor of 5
						totalDist += Math.abs(col - 4);
					}
				} //End if
			}//End nested for loop
		}//End for loop
		return totalDist;
	}
	
	/**
	 * Heuristic function 3:
	 * The function loops through each row to check each tile in current are
	 * correctly adjacent to its above tile. In addition, since first row has no
	 * above adjacent tiles, they will check against the goal state values.
	 * @param states	tiles of current state
	 * @return int		the total number wrongAboveAdjacentTiles
	 */
	public static int wrongAboveAdjacent(int[][] states) {
		int wrongAboveAdjacentTiles = 0;
		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 5; col++) {
				//Ignore blank tiles
				if (states[row][col] == 0)
					continue;
				if (row == 0) {
					//Check first row tiles against goal state value
					if (states[row][col] != row * 5 + col + 1)
						wrongAboveAdjacentTiles++;
					//other rows' tile checks against its above adjacent tile
				} else if (states[row-1][col] + 5 != states[row][col])
					wrongAboveAdjacentTiles++;
			}
		}
		return wrongAboveAdjacentTiles;
	}
	
	/**
	 * Heuristic function 4:
	 * The function loops through each row to check whether the tiles in the 
	 * row are arranged orderly, and is currently sitting at correct row 
	 * position. If it violates the rule above, it will be counted as a piece 
	 * in the row. Finally, function will return the number of pieces of the 
	 * board.
	 * @param states	tiles of current state
	 * @return int		the total number of pieces
	 */
    public static int countPieces(int[][] states) {
    	int numOfPieces = 0;
    	for(int row = 0; row < 5; row++) { //For each row
    		int previousNumber = states[row][0];
    		for(int col = 0; col < 5; col++) { //For each column
    			if (col == 0) { //First number in the row
    				//previousNumber check against goalstate value
    				if(previousNumber != (row * 5 + 1))
    					numOfPieces++;
    			} else if (col == 4 && states[row][col] != 0) { //Last number in the row
    				//current examine number check against goalstate value
    				if(states[row][col] != (row * 5 + 5))
    					numOfPieces++;
    			} else {
    				//If is a blank tile 
    				if(states[row][col]== 0)
    					if(previousNumber != 24) {
    						numOfPieces++;
    						continue;
    					}
    				//Second, Third and fourth number in the row
    				if(states[row][col] != previousNumber + 1)
    					numOfPieces++;
    			}
    			//update previous number
    			previousNumber = states[row][col];
    		}
    	}
    	return numOfPieces; 
    }

	/**
	 * This method is called if states are checked for equality on basis of the tile configuration 
	 * (e.g. if checking for previous instances in the queue)
	 * @param obj the object to compare to
	 * @return true if the states are equal, false otherwise
	 */
	public boolean equals(Object obj) {
		if (obj.getClass().equals(this.getClass())) {
			PuzzleState state=(PuzzleState)obj;
			for (int r=0; r<tiles.length; r++) {    
				for (int c=0; c<tiles[r].length; c++) {
					if (state.tiles[r][c]!=tiles[r][c])
						return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * This method returns the tiles of the state in a two-dimensional array.
	 * @return tiles of the state
	 */
	public int[][] getTiles() {
		return tiles;
	}
	
	/**
	 * A printable string of the state - displays the tile configuration using newlines.
	 * @return a string displaying the tile configuration of the state
	 */
	public String toString() {
		DecimalFormat nf=new DecimalFormat("00");
		StringBuffer sb=new StringBuffer();
		for (int r=0; r<tiles.length; r++) {
			for (int c=0; c<tiles[r].length; c++) {
				sb.append(" "+nf.format(tiles[r][c]));
			}
			sb.append("\n");
		}
		return sb.toString();
	}


}
