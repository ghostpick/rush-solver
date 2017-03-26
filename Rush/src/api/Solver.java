package api;

import java.util.*;

public class Solver {

    // static parameters
    final int 						POSITION_INITIAL= 2;
    final int 						POSITION_GOAL 	= 5;
    final char 						MY_CAR 			= '#';
    final String 					H_CAR			= "23" + MY_CAR;  // horizontal-sliding cars
    final String 					V_CAR 			= "AB";   		  // vertical-sliding cars
    final String 					CAR_BIG 		= "3B";   		  // length 3 cars
    final String 					CAR_SMALL 		= "2A" + MY_CAR;  // length 2 cars
    final char						EMPTY 			= '-';    		  // empty space, movable into
    final char 						OUT_OF_BOUND	= '@';    		  // represents everything out of bound
    final String 					LINE_BREAK 		= "\n";
    
    // Variables
	private int 					gv_boardLines   = 0;
	private int 					gv_boarColumns 	= 0;
    private String					gv_initialMap 	= "";
    private HashMap<String,String>	gt_stateMap		= null; // <currentState,previousState> 
    private Queue<String> 			gv_queue 		= null; // the breadth first search queue
    private boolean 				gv_trace 		= false;
    
    
    /////////////////////////////////////////////////////////////////////////////// PUBLIC SECTION
    public Solver(String map, int lines,int columns, boolean trace){
    	
    	// initialize variables
    	this.gv_boardLines 	= lines;
    	this.gv_boarColumns = columns;
    	this.gv_initialMap	= map;
    	this.gt_stateMap	= new HashMap<String,String>();
    	this.gv_queue		= new LinkedList<String>();
    	this.gv_trace		= trace;
    	
    	// initial state
    	this.saveState(this.gv_initialMap, null);
    }
    
    // solve rush problem
    public void applyAlgorithm(){
    	String lv_currentSate = "";
    	
        while (!this.gv_queue.isEmpty()) {
        	lv_currentSate = this.gv_queue.remove();
        	
        	// GOAL
        	if (this.getPosition(lv_currentSate, POSITION_INITIAL, POSITION_GOAL) == MY_CAR){      	
            	
            	if(this.gv_trace){
            		this.printSteps(lv_currentSate);
                	System.out.println(this.gt_stateMap.size() + " expanded");	
            	}
            	
            	/* we don't need to continue in the while, even the 
            	  queue is not empty because we find the solution */
            	break;
            }
            else
            	this.expand(lv_currentSate);
        }        
    }
        
    /////////////////////////////////////////////////////////////////////////////// PRIVATE SECTION

    // using BFS if we haven't reached the objective
    // Map the given state and add to queue
    private void saveState(String nextState, String prevState) {
        if (!this.gt_stateMap.containsKey(nextState)) {
        	this.gt_stateMap.put(nextState, prevState);
            this.gv_queue.add(nextState);
        }
    }

    
    // expands a given state; searches for next level states in the breadth first search
    //
    // Let (r,c) be the intersection point of this cross:
    //
    //     @       nU = 3     '@' is not a car, 'B' and 'X' are of the wrong type;
    //     -       nD = 1     only '2' can slide to the right up to 5 spaces
    //   2-----B   nL = 2
    //     X       nR = 4
    //
    // The n? counts how many spaces are there in a given direction, origin inclusive.
    // Cars matching the type will then slide on these "alleys".
    //
    private void expand(String current) {
        for (int r = 0; r < this.gv_boarColumns; r++) {
            for (int c = 0; c < this.gv_boardLines; c++) {
                if (getPosition(current, r, c) != EMPTY) continue;
                int nU = countSpaces(current, r, c, -1, 0);
                int nD = countSpaces(current, r, c, +1, 0);
                int nL = countSpaces(current, r, c, 0, -1);
                int nR = countSpaces(current, r, c, 0, +1);
                slide(current, r, c, V_CAR, nU, -1, 0, nU + nD - 1);
                slide(current, r, c, V_CAR, nD, +1, 0, nU + nD - 1);
                slide(current, r, c, H_CAR, nL, 0, -1, nL + nR - 1);
                slide(current, r, c, H_CAR, nR, 0, +1, nL + nR - 1);
            }
        }
    }
    
    // in a given state, starting from given coordinate, toward the given direction,
    // counts how many empty spaces there are (origin inclusive)
    private int countSpaces(String state, int r, int c, int dr, int dc) {
        int k = 0;
        while (getPosition(state, r + k * dr, c + k * dc) == EMPTY) {
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
    private void slide(String current, int r, int c, String type, int distance, int dr, int dc, int n) {
        r += distance * dr;
        c += distance * dc;
        char carPosition = getPosition(current, r, c);
        
        if ((type.indexOf(carPosition) == -1))
        	return;
        
        
        final int L = getLengthCar(carPosition);
        StringBuilder sb = new StringBuilder(current);
        for (int i = 0; i < n; i++) {
            r -= dr;
            c -= dc;
            sb.setCharAt(rc2i(r, c), carPosition);
            sb.setCharAt(rc2i(r + L * dr, c + L * dc), EMPTY);
            saveState(sb.toString(), current);
            current = sb.toString(); // comment to combo as one step
        }
    }
        
    // conventional row major 2D-1D index transformation
    private int rc2i(int r, int c) {
        return r * this.gv_boardLines + c;
    }

    // get lengthCar
    private int getLengthCar(char car) {
    	
        if ((CAR_BIG.indexOf(car) != -1))
        	return 3;   
        else if ((CAR_SMALL.indexOf(car) != -1))
        	return 2;
        else 
        	return 0/0;
    }

    // in given state, returns the entity at a given coordinate, possibly out of bound
    private char getPosition(String state, int r, int c) {
    	
    	if((r >=0 && r < this.gv_boarColumns) && (c >=0 && c < this.gv_boardLines))
    		return state.charAt(rc2i(r, c));
    	else
    		return OUT_OF_BOUND;    
    }
    

    // trace recursion
    private int printSteps(String current) {
        String prev = this.gt_stateMap.get(current);
        int    step = 0;
    	String currentState = LINE_BREAK;

        // get step
        if(prev == null)
        	step = 0;
        else
        	step = printSteps(prev) + 1;
        
    	for(int i = 0; i < current.length(); i++){
    		currentState += current.charAt(i);
    		
    		if ((i+1)%this.gv_boardLines == 0)
    			currentState += LINE_BREAK;
    	}
    	
    	System.out.println("STEP: " + step); 
        System.out.println(currentState);
       
        return step;
    }
}