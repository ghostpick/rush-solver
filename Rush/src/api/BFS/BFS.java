package api.BFS;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class BFS {

    // static parameters
    final int 						POSITION_INITIAL	= 2;
    final int 						POSITION_GOAL 		= 5;
    final char 						MY_CAR 				= '>';
    final String 					H_CAR				= "12" + MY_CAR;  // horizontal-sliding cars
    final String 					V_CAR 				= "AB";   		  // vertical-sliding cars
    final String 					CAR_BIG 			= "2B";   		  // length 3 cars
    final String 					CAR_SMALL 			= "1A" + MY_CAR;  // length 2 cars
    final char						EMPTY 				= '-';    		  // empty space, movable into
    final char 						OUT_OF_BOUND		= 'X';    		  // represents everything out of bound
    final String 					LINE_BREAK 			= "\n";
  
    final int 						MOVE_UP				= -1;
    final int 						MOVE_DOWN			=  1;
    final int 						MOVE_LEFT			= -1;
    final int 						MOVE_RIGHT			=  1;
    final int 						OWN_POSITION		=  1;
    final int 						NULL_ROW		    =  0;
    final int 						NULL_COL		    =  0;
    
    // Variables
	private int 					gv_boardRows		= 0;
	private int 					gv_boarColumns 		= 0;
    private String					gv_initialMap 		= "";
    private HashMap<String,String>	gt_stateMap			= null; // <currentState,previousState> 
    private Queue<String> 			gv_queue 			= null; // the breadth first search queue
    private boolean 				gv_trace 			= false;
    private String 					gv_src				= "files/results/bfs_solver.txt";

    
    /////////////////////////////////////////////////////////////////////////////// PUBLIC SECTION
    public BFS(String map, int lines,int columns, boolean trace){
    	
    	// initialize variables
    	this.gv_boardRows 	= lines;
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
    	String 				lv_currentSate = "";
    	char content = ' ';
    	
        while (!this.gv_queue.isEmpty()) {
        	lv_currentSate = this.gv_queue.remove();
        	content 	   = this.getContent(lv_currentSate, POSITION_INITIAL, POSITION_GOAL);
        	
        	
        	// GOAL
        	if (content == MY_CAR){      	
            	
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
            log(nextState, true);

        }

    }

    
    // expands a given state; searches for next level states in the breadth first search
    //
    // Let (row,col) be the intersection point of this cross:
    //
    //     X       lv_up    = 3     'X' is not a car, 'A' and '>' are of the wrong type;
    //     -       lv_down  = 1     only '1' can slide to the right up to 5 spaces
    //   1-----A   lv_left  = 2
    //     >       lv_right = 4
    //
    // The n? counts how many spaces are there in a given direction, origin inclusive.
    // Cars matching the type will then slide on these "alleys".

    static int c = 0;
    
    private void expand(String current) {
    	int  lv_up, lv_down, lv_left, lv_right,lv_maxHorizontalMove, lv_maxVerticalMove = 0;
    	char content = ' ';
    	
        log(Integer.toString(c), false);
//        log(current, true);

    	
        for (int row = 0; row < this.gv_boardRows; row++) {
            for (int col = 0; col < this.gv_boarColumns; col++) {
            	content = getContent(current, row, col);
            	
                if (content == EMPTY) {
                	lv_up     = countSpaces(current, row, col, MOVE_UP,   NULL_COL);
                	lv_down   = countSpaces(current, row, col, MOVE_DOWN, NULL_COL);
                	lv_left   = countSpaces(current, row, col, NULL_ROW,  MOVE_LEFT);
                	lv_right  = countSpaces(current, row, col, NULL_ROW,  MOVE_RIGHT);
                	
                	lv_maxHorizontalMove = lv_left + lv_right - OWN_POSITION;
           			lv_maxVerticalMove	 = lv_up  + lv_down  - OWN_POSITION;
                	
                	move(current, row, col, V_CAR, lv_up,    MOVE_UP,    NULL_COL,   lv_maxVerticalMove);
                	move(current, row, col, V_CAR, lv_down,  MOVE_DOWN,  NULL_COL,   lv_maxVerticalMove);
                	move(current, row, col, H_CAR, lv_left,  NULL_ROW,   MOVE_LEFT,  lv_maxHorizontalMove);
                	move(current, row, col, H_CAR, lv_right, NULL_ROW,   MOVE_RIGHT, lv_maxHorizontalMove);
                	
                }
            }
        }
        c++;

    }
    
    // in a given state, starting from given coordinate, toward the given direction,
    // counts how many empty spaces there are (origin inclusive)
    private int countSpaces(String state, int row, int col, int dr, int dc) {
        int k  		 = 0;
        char content = getContent(state, row + k * dr, col + k * dc);
        
        while (content == EMPTY) {
            k++;
            content = getContent(state, row + k * dr, col + k * dc);
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
    private void move(String current, int row, int col, String type, int numberSpaces, int distRow, int distCol, int n) {
    	row += numberSpaces * distRow;
    	col += numberSpaces * distCol;
        char content = getContent(current, row, col);
        
        if ((type.indexOf(content) == -1))
        	return;
        
        
        final int L = getLengthCar(content);
        StringBuilder sb = new StringBuilder(current);
        for (int i = 0; i < n; i++) {
        	row -= distRow;
        	col -= distCol;
            sb.setCharAt(matrixToString(row, col), content);
            sb.setCharAt(matrixToString(row + L * distRow, col + L * distCol), EMPTY);
            saveState(sb.toString(), current);
            current = sb.toString(); // comment to combo as one step
        }
    }
        
    // Transform 2D into 1D
    private int matrixToString(int row, int col) {
        return row * this.gv_boardRows + col;
    }

    // get lengthCar
    private int getLengthCar(char car) {
    	
        if ((CAR_BIG.indexOf(car) != -1))
        	return 3;   
        else if ((CAR_SMALL.indexOf(car) != -1))
        	return 2;
        else 
        	return 0;
    }

    // in given state, returns the entity at a given coordinate, possibly out of bound
    private char getContent(String state, int row, int col) {
    	
    	if((row >=0 && row < this.gv_boarColumns) && (col >=0 && col < this.gv_boardRows))
    		return state.charAt(matrixToString(row, col));
    	else
    		return OUT_OF_BOUND;    
    }
    
    
    //log
    private void log(String current, boolean state){
    	String currentState = LINE_BREAK;

    	if (state){
	        
	    	for(int i = 0; i < current.length(); i++){
	    		currentState += current.charAt(i);
	    		
	    		if ((i+1)%this.gv_boardRows == 0)
	    			currentState += LINE_BREAK;
	    	}
    	}
    	else
    		currentState = current + LINE_BREAK;

    	
        //System.out.println("EXPAND :" + c);

        try(FileWriter fw = new FileWriter(gv_src, true);
    		    BufferedWriter bw = new BufferedWriter(fw);
    		    PrintWriter out = new PrintWriter(bw)){
    	   
    		    //more code
    		    out.println(currentState);
    		    //more code
    		} catch (IOException e) {
    		    //exception handling left as an exercise for the reader
    		}
        //c++;
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
    		
    		if ((i+1)%this.gv_boardRows == 0)
    			currentState += LINE_BREAK;
    	}
    	
    	System.out.println("STEP: " + step); 
        System.out.println(currentState);
       
        return step;
    }
}