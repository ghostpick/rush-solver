package api;

public abstract class Global {
	
	public final static String LINE_BREAK	   		= "\n";
	public final static char BFS_MYCAR	   		    = '>';
	public final static char BFS_H3	   		        = '2';
	public final static char BFS_H2	   		        = '1';
	public final static char BFS_V2	   		        = 'A';
	public final static char BFS_V3	   		  		= 'B';
	
    public final static String SRC_MAPS		    	= "files/puzzle.txt";
    public final static String SRC_BFS		    	= "files/results/bfs_solver.txt";
    public final static String PRINT_DURATION		= "Duration";
    public final static String PRINT_DURATION_01	= "Duration: ";
    public final static String PRINT_EXPNODES		= "Nodes exapanded";
    public final static String PRINT_EXPNODES_01	= "Nodes exapanded: ";
    public final static String PRINT_STEPS	    	= "Steps";
    public final static String PRINT_STEPS_01    	= " >> Steps: ";
    public final static String PRINT_SEPARATOR    	= ", ";
    public final static String PRINT_HEURISTIC   	= " >> Heuristic: ";
    public final static String PRINT_BFS   			= " >> BFS(optimized)";
    public final static String PRINT_PUZZLE     	= "\n  PUZZLE: ";
    

    public final static String PRINT_RESULT_TBL_00 	= " > RESULTS:\n";
    		
    public final static String PRINT_RESULT_TBL_01 	= 
    "|            |     A* (only g(n))    |A* - BLOCKING HEURISTIC|A* - ADVANCED HEURISTIC|   BFS(not optimized)  |";
    
    public final static String PRINT_RESULT_TBL_02 	= 
    "| Name       |   steps   dur.  nodes |   steps   dur.  nodes |   steps   dur.  nodes |   steps   dur.  nodes |";
    
    public final static String PRINT_RESULT_TBL_03 	= 
    "|------------+-----------------------+-----------------------+-----------------------+-----------------------|";
    
    public final static String PRINT_RESULT_TBL_04 	= 
    "\n==============================================================================================================";
    
    public final static String PRINT_RESULT_TBL_05 	= 
    "\n--------------------------------------------------------------------------------------------------------------";
}
