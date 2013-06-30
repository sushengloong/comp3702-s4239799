/**
 * @author SU Sheng Loong 42397997
 * @author TEE Lip Jian 42430942
 */
package search.fifteen;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.lang.Math;

import search.Action;
import search.Node;

/**
 * This class has been modified so that the puzzle will have twenty-four tiles
 * and one blank space rather than the original fifteen-tile state.
 * The entry point of the program, main method is written in this class.
 * In addition, methods for solving the puzzle by using the 8 combinations
 * of different heuristic functions and search algorithms are also included
 * in this class.
 * The program basically generate two puzzle states and use them to run 
 * different searches and then the results including solutions, EBF, etc
 * will be displayed.
 */
public class FifteenSearchApp {

    /**
     * Test program for search procedures
     * @param args none interpreted as yet
     */
    public static void main(String[] args) {

        // Create a random puzzle and memorise the puzzle state.
    	int shuffle = 32; // Number of shuffle is fixed here
        PuzzleState myState= randomPuzzle(shuffle);
        PuzzleState myState2 = new PuzzleState(myState);
    	
        // now perform the search from the "shuffled" initial state (fringe is empty), and
        // pull out the actions that were used to generate this goal state from the initial state
        
        double[] ebf = new double[8]; // for storing EBF
        Action[] actions1G = solveH1G(new PuzzleState(myState));
        ebf[0] = Node.effectiveBranchingFactor(
        		Node.getNodesExpandedInLastSearch(), shuffle);
        Action[] actions1A = solveH1A(new PuzzleState(myState));
        ebf[1] = Node.effectiveBranchingFactor(
        		Node.getNodesExpandedInLastSearch(), shuffle);
        Action[] actions2G = solveH2G(new PuzzleState(myState));
        ebf[2] = Node.effectiveBranchingFactor(
        		Node.getNodesExpandedInLastSearch(), shuffle);
        Action[] actions2A = solveH2A(new PuzzleState(myState));
        ebf[3] = Node.effectiveBranchingFactor(
        		Node.getNodesExpandedInLastSearch(), shuffle);
        Action[] actions3G = solveH3G(new PuzzleState(myState));
        ebf[4] = Node.effectiveBranchingFactor(
        		Node.getNodesExpandedInLastSearch(), shuffle);
        Action[] actions3A = solveH3A(new PuzzleState(myState));
        ebf[5] = Node.effectiveBranchingFactor(
        		Node.getNodesExpandedInLastSearch(), shuffle);
        Action[] actions4G = solveH4G(new PuzzleState(myState));
        ebf[6] = Node.effectiveBranchingFactor(
        		Node.getNodesExpandedInLastSearch(), shuffle);
        Action[] actions4A = solveH4A(new PuzzleState(myState));
        ebf[7] = Node.effectiveBranchingFactor(
        		Node.getNodesExpandedInLastSearch(), shuffle);
        
        // List the initial state and results of actions performed.
        System.out.println("Initial state:");
        System.out.println(myState2.toString());
       
        System.out.println("Solution via H1 with Greedy:-------------");
        for (int i=0; i<actions1G.length; i++) {
            System.out.println((i+1)+": "+actions1G[actions1G.length-1-i]);
            PuzzleState.performAction(myState2,actions1G[actions1G.length-1-i]);
            System.out.println(myState2.toString());
        }

        System.out.println("Solution via H1 with A*:-------------");
        myState2 = new PuzzleState(myState);
        for (int i=0; i<actions1A.length; i++) {
            System.out.println((i+1)+": "+actions1A[actions1A.length-1-i]);
            PuzzleState.performAction(myState2,actions1A[actions1A.length-1-i]);
            System.out.println(myState2.toString());
        }
		
        System.out.println("Solution via H2 with Greedy:-------------");
        myState2 = new PuzzleState(myState);
        for (int i=0; i<actions2G.length; i++) {
            System.out.println((i+1)+": "+actions2G[actions2G.length-1-i]);
            PuzzleState.performAction(myState2,actions2G[actions2G.length-1-i]);
            System.out.println(myState2.toString());
        }

        System.out.println("Solution via H2 with A*:-------------");
        myState2 = new PuzzleState(myState);
        for (int i=0; i<actions2A.length; i++) {
            System.out.println((i+1)+": "+actions2A[actions2A.length-1-i]);
            PuzzleState.performAction(myState2,actions2A[actions2A.length-1-i]);
            System.out.println(myState2.toString());
       }
      
        System.out.println("Solution via H3 with Greedy:-------------");
        myState2 = new PuzzleState(myState);
        for (int i=0; i<actions3G.length; i++) {
            System.out.println((i+1)+": "+actions3G[actions3G.length-1-i]);
            PuzzleState.performAction(myState2,actions3G[actions3G.length-1-i]);
            System.out.println(myState2.toString());
        }

        System.out.println("Solution via H3 with A*:-------------");
        myState2 = new PuzzleState(myState);
        for (int i=0; i<actions3A.length; i++) {
            System.out.println((i+1)+": "+actions3A[actions3A.length-1-i]);
            PuzzleState.performAction(myState2,actions3A[actions3A.length-1-i]);
            System.out.println(myState2.toString());
        }
        
        System.out.println("Solution via H4 with Greedy:-------------");
        myState2 = new PuzzleState(myState);
        for (int i=0; i<actions4G.length; i++) {
            System.out.println((i+1)+": "+actions4G[actions4G.length-1-i]);
            PuzzleState.performAction(myState2,actions4G[actions4G.length-1-i]);
            System.out.println(myState2.toString());
        }
		
        System.out.println("Solution via H4 with A*:-------------");
        myState2 = new PuzzleState(myState);
        for (int i=0; i<actions4A.length; i++) {
            System.out.println((i+1)+": "+actions4A[actions4A.length-1-i]);
            PuzzleState.performAction(myState2,actions4A[actions4A.length-1-i]);
            System.out.println(myState2.toString());
        }
       
        System.out.println("H1");
        System.out.println("Greedy: " + actions1G.length);
        System.out.printf("EBF: %.3f\n", ebf[0]);
        System.out.println("A*: " + actions1A.length);
        System.out.printf("EBF: %.3f\n", ebf[1]);
        
        System.out.println();
        
        System.out.println("H2");
        System.out.println("Greedy: " + actions2G.length);
        System.out.printf("EBF: %.3f\n", ebf[2]);
        System.out.println("A*: " + actions2A.length);
        System.out.printf("EBF: %.3f\n", ebf[3]);
        
        System.out.println();
        
        System.out.println("H3");
        System.out.println("Greedy: " + actions3G.length);
        System.out.printf("EBF: %.3f\n", ebf[4]);
        System.out.println("A*: " + actions3A.length);
        System.out.printf("EBF: %.3f\n", ebf[5]);
        
        System.out.println();
        
        System.out.println("H4");
        System.out.println("Greedy: " + actions4G.length);
        System.out.printf("EBF: %.3f\n", ebf[6]);
        System.out.println("A*: " + actions4A.length);
        System.out.printf("EBF: %.3f\n", ebf[7]);
    }

    /**
     * This method is Greedy search implementation with heuristic function 1.
     * This method accepts a puzzle state and then return an array
     * of actions which can move from the initial state to the goal state.
     * 
     * @param state initial puzzle state
     * @return array of actions to solve the puzzle  
     */
    public static Action[] solveH1G(PuzzleState state){
    	/* call generic search by given initial state and priority queue with 
 	   		h(n1)-h(n2) comparator */
    	// The generic search function is used by passing in an object of an anonymous class
    	//     which is the Comparator for comparing two nodes in terms the number of 
    	//     misplaced tiles
    	Node goal = Node.genericSearch(state, new PriorityQueue<Node>(100, new Comparator<Node>() {
    		//priority queue with comparator( h(a) - h(b) )	
    		public int compare(Node lhs,Node rhs) {
        		int lhsH2 = PuzzleState.misplacedTiles(lhs.getState().getTiles());
        		int rhsH2 = PuzzleState.misplacedTiles(rhs.getState().getTiles());
        		return lhsH2 - rhsH2 ;
        	} 
    	}));
        Action[] actions=goal.getActions();
        
        return actions;
    }
    
    /**
     * This method is A* search implementation with heuristic function 1.
     * This method accepts a puzzle state and then return an array
     * of actions which can move from the initial state to the goal state.
     * 
     * @param state initial puzzle state
     * @return array of actions to solve the puzzle  
     */
    public static Action[] solveH1A(PuzzleState state){
    	/* call generic search by given initial state and priority queue with 
 	   		h(n1)-h(n2) comparator */
    	// The generic search function is used by passing in an object of an anonymous class
    	//     which is the Comparator for comparing two nodes in terms of the sum of 
    	//     the number of misplaced tiles and the path cost to the node.
    	Node goal = Node.genericSearch(state, new PriorityQueue<Node>(11, new Comparator<Node>() {
    		public int compare(Node lhs,Node rhs) {
    			//priority queue with comparator( [h(a) + g(a)] - [h(b) + g(b)] )	
        		int lhsH2 = PuzzleState.misplacedTiles(lhs.getState().getTiles());
        		int rhsH2 = PuzzleState.misplacedTiles(rhs.getState().getTiles());
        		return (lhsH2 + (int)lhs.getCost()) - (rhsH2 + (int)rhs.getCost());
        	} 
    	}));
        Action[] actions=goal.getActions();
        
        return actions;
    }

    /**
     * This method is Greedy search implementation with heuristic function 2.
     * This method accepts a puzzle state and then return an array
     * of actions which can move from the initial state to the goal state.
     * 
     * @param state initial puzzle state
     * @return array of actions to solve the puzzle  
     */
    public static Action[] solveH2G(PuzzleState state){
    	/* call generic search by given initial state and priority queue with 
    	   h(n1)-h(n2) comparator */
    	// The generic search function is used by passing in an object of an anonymous class
    	//     which is the Comparator for comparing two nodes in terms of the total of 
    	// 	   Manhattan distance of all tiles 
    	Node goal = Node.genericSearch(state, 
    		new PriorityQueue<Node>(11, new Comparator<Node>() {
    				//priority queue with comparator( h(a) - h(b) )	
    				public int compare(Node lhs,Node rhs) {
    					int lhsH2 = PuzzleState.mDistance(lhs.getState().getTiles());
    					int rhsH2 = PuzzleState.mDistance(rhs.getState().getTiles());
    					return lhsH2 - rhsH2;
    				} 
    			}
    		));
        Action[] actions = goal.getActions();
        
        return actions;
    }
    
    /**
     * This method is A* search implementation with heuristic function 2.
     * This method accepts a puzzle state and then return an array
     * of actions which can move from the initial state to the goal state.
     * 
     * @param state initial puzzle state
     * @return array of actions to solve the puzzle  
     */
    public static Action[] solveH2A(PuzzleState state){
    	/* call generic search by given initial state and priority queue with 
 	   	   [h(n1) + g(n1)] - [h(n2) + g(n2)] comparator. */
    	// The generic search function is used by passing in an object of an anonymous class
    	//     which is the Comparator for comparing two nodes in terms of the sum of 
    	//     the total of Manhattan distance of all tiles and the path cost to the node.
    	Node goal = Node.genericSearch(state, new PriorityQueue<Node>(11, 
    		new Comparator<Node>() {
    			//priority queue with comparator( [h(a) + g(a)] - [h(b) + g(b)] )	
    			public int compare(Node lhs,Node rhs) {
    				int lhsH2 = PuzzleState.mDistance(lhs.getState().getTiles());
    				int rhsH2 = PuzzleState.mDistance(rhs.getState().getTiles());
    				return (lhsH2 + (int)lhs.getCost()) - (rhsH2 + (int)rhs.getCost());
    			} 
    		}
    	));
    	Action[] actions = goal.getActions();
    	return actions;
    }
    
    /**
     * This method is Greedy search implementation with heuristic function 3.
     * This method accepts a puzzle state and then return an array
     * of actions which can move from the initial state to the goal state.
     * 
     * @param state initial puzzle state
     * @return array of actions to solve the puzzle  
     */
    public static Action[] solveH3G(PuzzleState state){
    	/* call generic search by given initial state and priority queue with 
 	   	   h(n1)-h(n2) comparator */
    	Node goal = Node.genericSearch(state, new PriorityQueue<Node>(11, new Comparator<Node>() {
    		public int compare(Node lhs,Node rhs) {
    			//priority queue with comparator( h(a) - h(b) )	
        		int lhsH3 = PuzzleState.wrongAboveAdjacent(lhs.getState().getTiles());
        		int rhsH3 = PuzzleState.wrongAboveAdjacent(rhs.getState().getTiles());
        		return lhsH3  - rhsH3;
        	} 
    	}));
    	Action[] actions = goal.getActions();
    
    	return actions;
    }
    
    /**
     * This method is A* search implementation with heuristic function 3.
     * This method accepts a puzzle state and then return an array
     * of actions which can move from the initial state to the goal state.
     * 
     * @param state initial puzzle state
     * @return array of actions to solve the puzzle  
     */
    public static Action[] solveH3A(PuzzleState state){
    	/* call generic search by given initial state and priority queue with 
	   	   [h(n1) + g(n1)] - [h(n2) + g(n2)] comparator. */
    	Node goal = Node.genericSearch(state, new PriorityQueue<Node>(11, new Comparator<Node>() {
    		public int compare(Node lhs,Node rhs) {
    			//priority queue with comparator( [h(a) + g(a)] - [h(b) + g(b)] )	
        		int lhsH3 = PuzzleState.wrongAboveAdjacent(lhs.getState().getTiles());
        		int rhsH3 = PuzzleState.wrongAboveAdjacent(rhs.getState().getTiles());
        		return (lhsH3 + (int)lhs.getCost()) - (rhsH3 + (int)rhs.getCost());
        	} 
    	}));
    	Action[] actions = goal.getActions();
    
    	return actions;
    }
    
    /**
     * This method is Greedy search implementation with heuristic function 4.
     * This method accepts a puzzle state and then return an array
     * of actions which can move from the initial state to the goal state.
     * 
     * @param state initial puzzle state
     * @return array of actions to solve the puzzle  
     */
    public static Action[] solveH4G(PuzzleState state){
    	/* call generic search by given initial state and priority queue with 
 	       h(n1)-h(n2) comparator */
    	Node goal = Node.genericSearch(state, new PriorityQueue<Node>(11, new Comparator<Node>() {
    		public int compare(Node lhs,Node rhs) {
    			//priority queue with comparator( h(a) - h(b) )	
        		int lhsH4 = PuzzleState.countPieces(lhs.getState().getTiles());
        		int rhsH4 = PuzzleState.countPieces(rhs.getState().getTiles());
        		return lhsH4  - rhsH4;
        	} 
    	}));
    	Action[] actions = goal.getActions();
    
    	return actions;
    }
    
    /**
     * This method is A* search implementation with heuristic function 4.
     * This method accepts a puzzle state and then return an array
     * of actions which can move from the initial state to the goal state.
     * 
     * @param state initial puzzle state
     * @return array of actions to solve the puzzle  
     */
    public static Action[] solveH4A(PuzzleState state){
    	/* call generic search by given initial state and priority queue with 
	   	   [h(n1) + g(n1)] - [h(n2) + g(n2)] comparator. */
    	Node goal = Node.genericSearch(state, new PriorityQueue<Node>(11, new Comparator<Node>() {
    		public int compare(Node lhs,Node rhs) {
    			//priority queue with comparator( [h(a) + g(a)] - [h(b) + g(b)] )	
        		int lhsH4 = PuzzleState.countPieces(lhs.getState().getTiles());
        		int rhsH4 = PuzzleState.countPieces(rhs.getState().getTiles());
        		return (lhsH4 + (int)lhs.getCost()) - (rhsH4 + (int)rhs.getCost());
        	} 
    	}));
    	Action[] actions = goal.getActions();
    
    	return actions;
    }
    
    /**
     * Generate a solvable random puzzle. 
     * This method has been modified so that the puzzle will be shuffled in
     * such a way that the blank space will not be moved back to the previous
     * position.
     * @param maxShuffles the number of shuffles to be performed
     */
    public static PuzzleState randomPuzzle(int maxShuffles) {
        PuzzleState myState=new PuzzleState();
        int totalMoves = 0;
        // Keep track of upper and lower bound which the random number
        // generated has to be in between
        double previousMoveUpperBound = -1,previousMoveLowerBound = -1;
        while(totalMoves < maxShuffles){
            double r = Math.random();
            try {
            	while( r < previousMoveUpperBound && r > previousMoveLowerBound){
            		r = Math.random();
            	}
                if(r < 0.25){
                    PuzzleState.performAction(myState, PuzzleState.MOVE_LEFT);
                    previousMoveUpperBound = 0.5;
                    previousMoveLowerBound = 0.25;
                } else if (r < 0.5) {
                    PuzzleState.performAction(myState, PuzzleState.MOVE_RIGHT);
                    previousMoveUpperBound = 0.25;
                    previousMoveLowerBound = 0.0;
                } else if (r < 0.75) {
                    PuzzleState.performAction(myState, PuzzleState.MOVE_UP);
                    previousMoveUpperBound = 1.0;
                    previousMoveLowerBound = 0.75;
                } else {
                    PuzzleState.performAction(myState, PuzzleState.MOVE_DOWN);
                    previousMoveUpperBound = 0.75;
                    previousMoveLowerBound = 0.5;
                }
                totalMoves++;
            }
            catch (RuntimeException e){
                ; // illegal move
            }
        }
        return myState;
    }

    /**
     * Check if the actions solve the given puzzle.
     * @param myState a problem
     * @param actions an array of Action
     */
    public static boolean checkActions(PuzzleState myState, Action[] actions) {
        // create an initial fiteen puzzle state by first generating the goal config
        for (int i=0; i<actions.length; i++) {
            try {
                PuzzleState.performAction(myState, actions[i]);
            }
            catch (RuntimeException e){
                return false; // illegal move
            }
        }

        // check whether myState is the goal state.
        return myState.goal();
    }
 
}

