/**
 * @author SU Sheng Loong 42397997
 * @author TEE Lip Jian 42430942
 */
package search;

import java.util.*;

import search.fifteen.PuzzleState;


/**
 * The Node is the main class for the search tree.
 * To use ordering, use the Comparator class. 
 */
public class Node{
    private final State  state;
    private final Node   parent;
    private final Action action;
    private final double cost;
    private final int    depth;
    private static int nodesExpandedInLastSearch;
    // keep track of the number of nodes expanded in the last search

    protected Node() {
        this(null, null, null, 0);
    }
    /**
     * Create a search node with no parent node, i.e. a root node.
     * @param state the state which this node refers to, usually the initial state
     */
    public Node(State state) {
        this(state, null, null, 0);
    }
    
    /**
     * Initialise a search node.
     * @param state the state it contains
     * @param parent the parent node it derives from
     * @param action the action that we took to get here
     * @param cost the cost that has been accumulated getting here
     */
    public Node(State state, Node parent, Action action, double cost) {
        this.state=state;
        this.parent=parent;
        this.action=action;
        this.cost=cost;
        if (parent!=null)
            this.depth=parent.getDepth()+1;
        else
            this.depth=0;
    }
    
    /**
     * Expand the node into sub-nodes.
     * It uses the successor-function of the state to determine all Action/State pairs, and generates
     * a node for each.
     * @return all nodes that can be reached from this node
     */
    public Node[] expand() {
        ActionStatePair[] successors=state.successor();
        Node[] descendants=new Node[successors.length];
        for (int a=0; a<successors.length; a++) {
            Action action=successors[a].getAction();
            State child=successors[a].getState();
            descendants[a]=new Node(child, this, action, this.getCost()+state.pathcost(action));
        }
        return descendants;
    }

    /**
     * Determine all actions that were used to get to this node.
     * @return all actions that took us to this node
     */
    public Action[] getActions() {
        Action[] steps=new Action[depth];
        Node node=this;
        for (int i=0; i<depth; i++) {
            steps[i]=node.getAction();
            node=node.getParent();
        }
        return steps;
    }
    
    /**
     * @return the (last) action that took us here (from parent)
     */
    public Action getAction() {
        return action;
    }

    /**
     * @return the accumulated cost of getting here
     */
    public double getCost() {
        return cost;
    }

    /**
     * @return the tree depth of this node
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @return the parent node of this node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * @return the state this node contains
     */
    public State getState() {
        return state;
    }
    
    /**
     * Getter method for static variable which is the total number of nodes
     * expanded in the last search.
     * @return total number of nodes expanded in the last search
     */
    public static int getNodesExpandedInLastSearch() {
    	return nodesExpandedInLastSearch;
    }
    
    /**
     * This method compares and returns whether this object is the same as the 
     * argument object. The criteria used is whether both objects have the same
     * state. 
     * @param obj another object to be compared
     * @return whether this object has the same state as the argument object
     */
    public boolean equals(Object obj) {
        return this.getState().equals(((Node)obj).getState());
    } 
    
    /**
     * Calculate the effective branching factor for a tree search.
     * Adapted from ATILLA DEMIRAY's ANSI C CODE CALCULATING EFFECTIVE BRANCHING FACTOR
     * http://utopia.poly.edu/~ademir02/soft/sources/aisx/ebfx.htm (was available 30 Aug 2005)
     * @param nNodes number of nodes
     * @param depth depth where solution was found
     * @return the estimated effective branching factor
     */
    public static double effectiveBranchingFactor(int nNodes, int depth) {
        double maxError=0.01; // the maximum error we accept from a solution 
        double delta=0.01;    // how much we change our tested ebf each iteration   
        int signOfError=0;    // the sign of the difference between N+1 and 1+b+b^2+......+b^d
        double b=0;           // search for the optimum b will start from minimum possible b
        while (true) {        // search for b starts here
            // compute the expression 1+b+b^2+......+b^d
            double sum=1;
            for (int i=1; i<depth+1; i++) {
                sum+=Math.pow(b, (double)i);
            }
            // now the tricky bit, we compute the difference between 
            // N+1 and 1+b+b^2+......+b^d, remember that we should have N+1=1+b+b^2+......+b^d
            double error=sum-(1+(double)nNodes);
            // save previous sign of error
            int signOfPreviousError=signOfError;
            // determine the new sign of error
            if (error<0.0) // negative
                signOfError=-1;
            else // positive
                signOfError=+1;
            /* if the error calculated above is greater than the maximum acceptable error, then check if sign of error
               was changed, if so that means loop missed the root that we are looking for, then decrease b by delta and 
               decrease delta to catch root in next search if sign of error wasnt change then increase 'b' by delta
               otherwise if error is smaller than the limit return the effective branch factor */
            if (Math.abs(error)>maxError) { // error is big
                if (signOfPreviousError==signOfError || signOfPreviousError==0) {  
                    b+=delta;   // taking another step towards solution
                } else {        // change of sign which means that we jumped over the minima
                    b-=delta;   // go back
                    delta/=2;   // take smaller steps
                    signOfError=signOfPreviousError;  // undo change of sign
                }
            } else // error is small, let's return current estimate
                return(b);
        }
    }
  
    /**
     * This method is a generic search method for performing search algorithm.
     * This method accepts the initial state and a priority queue and return the 
     * node which is in the goal state. The Comparator object passed into the
     * function will decide which search algorithm and heuristic to be used.
     * 
     * @param initial The initial state
     * @param fringe A priority queue which stores the nodes in decreasing desirability
     * @return The node of the goal state
     */
    public static Node genericSearch(State initial, PriorityQueue<Node> fringe) {
    	// reset the total number of nodes expanded to zero
    	nodesExpandedInLastSearch = 0;
    	// add the initial state node to the fringe
    	fringe.add(new Node(initial));
    	// have a list to keep track of all nodes generated
    	ArrayList<Node> allNode = new ArrayList<Node>();
    	allNode.add(new Node(initial));
    	while(!fringe.isEmpty()) {
    		// remove and return the most desirable node from the fringe
    		Node head = fringe.poll();
    		//check for not repeating with ancestor nodes and previous node 
    		State state = head.getState();
    		// if goal state, return the node
    		if(state.goal()){
    			return head;
    		}
    		// Generating child nodes and add to the fringe and list
    		for (Object child:Arrays.asList(head.expand())) {
    			if(!allNode.contains((Node)child)){
    				fringe.add((Node)child);
    				allNode.add((Node)child);
    			}		
    		}
    		// increment the number of nodes expanded in this search
    		nodesExpandedInLastSearch++;
    	}
    	return null;
    }
    
    /**
     * Check if a node already has existed in a particular list.
     * @param list a list of nodes
     * @param nodeToExamine node that is to be examined against the list
     * @return whether the node exists in the list
     */
    private static boolean isRepeated(Collection<Node> list, Node nodeToExamine) {
    	return list.contains(nodeToExamine);
    }
    
    /**
     * Check if a node is already repeated by checking it against the previous node.
     * @param previousNode previous node
     * @param nodeToExamine the node to be examined
     * @return whether the node is a repeated node
     */
    private static boolean isRepeated(Node previousNode, Node nodeToExamine) {
    	if(previousNode == null)
    		return false;
    	return previousNode.equals(nodeToExamine);
    }
}
