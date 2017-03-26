package api;

import java.util.*;

public class Solver {

    // Game parameters
    final int BOARD_LINES   = 6;
    final int BOARD_COLUMNS = 6;
    final int CAR_INITIAL   = 2;
    final int CAR_GOAL      = 5;
    
    final String HORZS = "23X";  // horizontal-sliding cars
    final String VERTS = "AB";   // vertical-sliding cars
    final String LONGS = "3B";   // length 3 cars
    final String SHORTS = "2AX"; // length 2 cars
    final char GOAL_CAR = 'X';
    final char EMPTY = '.';      // empty space, movable into
    final char VOID = '@';       // represents everything out of bound
    
    
    // Variables
    private String map = "";
    private boolean isSolved = false;
    private Map<String,String> pred = null; // the predecessor map, maps currentState => previousState
    private Queue<String> queue = null;     // the breadth first search queue
    
    
    public Solver(String map){
    	
    	// initialize variables
    	this.map   = map;
    	this.pred  = new HashMap<String,String>();
    	this.queue = new LinkedList<String>();
    	
    	// initial state
    	this.propose(this.map, null);
    	
    	// solve rush
        while (!this.queue.isEmpty()) {
            String current = this.queue.remove();
            if (isGoal(current) && !this.isSolved) {
            	this.isSolved = true;
            	this.trace(current);
            }
            this.expand(current);
        }
        System.out.println(pred.size() + " expanded");	
    }
    
    // the breadth first search proposal method: if we haven't reached it yet,
    // (i.e. it has no predecessor), we map the given state and add to queue
    public void propose(String next, String prev) {
        if (!pred.containsKey(next)) {
            pred.put(next, prev);
            queue.add(next);
        }
    }
    
    // checks if a given state is a goal state
    public boolean isGoal(String state) {
    	
    	if (this.at(state, CAR_INITIAL, CAR_GOAL) == GOAL_CAR){
    		return true;
    	}
    	else
    		return false;    	
    }
    
    // expands a given state; searches for next level states in the breadth first search
    //
    // Let (r,c) be the intersection point of this cross:
    //
    //     @       nU = 3     '@' is not a car, 'B' and 'X' are of the wrong type;
    //     .       nD = 1     only '2' can slide to the right up to 5 spaces
    //   2.....B   nL = 2
    //     X       nR = 4
    //
    // The n? counts how many spaces are there in a given direction, origin inclusive.
    // Cars matching the type will then slide on these "alleys".
    //
    public void expand(String current) {
        for (int r = 0; r < BOARD_COLUMNS; r++) {
            for (int c = 0; c < BOARD_LINES; c++) {
                if (at(current, r, c) != EMPTY) continue;
                int nU = countSpaces(current, r, c, -1, 0);
                int nD = countSpaces(current, r, c, +1, 0);
                int nL = countSpaces(current, r, c, 0, -1);
                int nR = countSpaces(current, r, c, 0, +1);
                slide(current, r, c, VERTS, nU, -1, 0, nU + nD - 1);
                slide(current, r, c, VERTS, nD, +1, 0, nU + nD - 1);
                slide(current, r, c, HORZS, nL, 0, -1, nL + nR - 1);
                slide(current, r, c, HORZS, nR, 0, +1, nL + nR - 1);
            }
        }
    }
    
    // in a given state, starting from given coordinate, toward the given direction,
    // counts how many empty spaces there are (origin inclusive)
    int countSpaces(String state, int r, int c, int dr, int dc) {
        int k = 0;
        while (at(state, r + k * dr, c + k * dc) == EMPTY) {
            k++;
        }
        return k;
    }
    
    // in a given state, from a given origin coordinate, attempts to find a car of a given type
    // at a given distance in a given direction; if found, slide it in the opposite direction
    // one spot at a time, exactly n times, proposing those states to the breadth first search
    //
    // e.g.
    //    direction = -->
    //    __n__
    //   /     \
    //   ..o....c
    //      \___/
    //      distance
    //
    void slide(String current, int r, int c, String type, int distance, int dr, int dc, int n) {
        r += distance * dr;
        c += distance * dc;
        char car = at(current, r, c);
        if (!isType(car, type)) return;
        final int L = length(car);
        StringBuilder sb = new StringBuilder(current);
        for (int i = 0; i < n; i++) {
            r -= dr;
            c -= dc;
            sb.setCharAt(rc2i(r, c), car);
            sb.setCharAt(rc2i(r + L * dr, c + L * dc), EMPTY);
            propose(sb.toString(), current);
            current = sb.toString(); // comment to combo as one step
        }
    }
    
    
    // conventional row major 2D-1D index transformation
    int rc2i(int r, int c) {
        return r * BOARD_LINES + c;
    }

    // checks if an entity is of a given type
    boolean isType(char entity, String type) {
        return type.indexOf(entity) != -1;
    }

    // finds the length of a car
    int length(char car) {
    	
        return
            isType(car, LONGS) ? 3 :
            isType(car, SHORTS) ? 2 :
            0/0; // a nasty shortcut for throwing IllegalArgumentException
    }

    // in given state, returns the entity at a given coordinate, possibly out of bound
    char at(String state, int r, int c) {
    
        return (inBound(r, BOARD_COLUMNS) && inBound(c, BOARD_LINES)) ? state.charAt(rc2i(r, c)) : VOID;
    }
    
    boolean inBound(int v, int max) {
        return (v >= 0) && (v < max);
    }


    // the predecessor tracing method, implemented using recursion for brevity;
    // guaranteed no infinite recursion, but may throw StackOverflowError on
    // really long shortest-path trace (which is infeasible in standard Rush Hour)
    public int trace(String current) {
        String prev = pred.get(current);
        int    step = 0;
    	String currentState = "\n";

        // get step
        if(prev == null)
        	step = 0;
        else
        	step = trace(prev) + 1;
        
    	for(int i = 0; i < current.length(); i++){
    		
    		currentState += current.charAt(i);
    		
    		if ((i+1)%BOARD_LINES == 0)
    			currentState += "\n";
    	
    	}
    		
    	System.out.println("STEP: " + step); 
        System.out.println(currentState);
       
        return step;
    }
}