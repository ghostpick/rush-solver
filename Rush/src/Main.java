import java.io.IOException;
import api.GeneralFeatures;
import api.Global;
import api.BFS.*;
import api.AStar.*;
import api.AStar.Heuristics.*;

public class Main  {
	
    public static void main(String[] args){	


    	String map = "------" +
			         "-222A-" +
			         ">>-BA-" +
			         "---B--" +
			         "---B--" +
			         "------";
    	
    	BFS bfs_solver = new BFS(map,6,6, true);
    	bfs_solver.applyAlgorithm();
    	
    	astar_solver_applyAlgorithm();
	}
    
    
    
    static void astar_solver_applyAlgorithm(){
    	Puzzle[] puzzles = null;
    	try {
    		puzzles = Puzzle.readPuzzlesFromFile(Global.SRC_MAPS);
    	} 
    	catch (IOException e) {
    		e.printStackTrace();
    	}

    		boolean showTime = false;
    	
    		String[] heuristic_names = null;
    		int num_puzzles = puzzles.length; // puzzles.length;
    		int num_heuristics = 0;

    		int[][] num_expanded = null;
    		int[][] soln_depth = null;
    		long[][] duration = null;

    		// run each heuristic on each puzzle
    		for (int i = 0; i < num_puzzles; i++) {
    			System.out.println(Global.PRINT_LINE);
    			System.out.println("puzzle = " + puzzles[i].getName());
    			
    			// h(x) used in A*
    			Heuristic[] heuristics = {
    					new ZeroHeuristic(puzzles[i]),
    					new BlockingHeuristic(puzzles[i]),
    					new AdvancedHeuristic(puzzles[i])
    				};

    			if (i == 0) {
    				num_heuristics = heuristics.length;
    				num_expanded = new int[num_puzzles][num_heuristics];
    				soln_depth = new int[num_puzzles][num_heuristics];
    				duration = new long[num_puzzles][num_heuristics];

    				heuristic_names = new String[num_heuristics];
    				for (int h = 0; h < num_heuristics; h++)
    					heuristic_names[h] = heuristics[h].getClass().getName();
    			}

    			for (int h = 0; h < num_heuristics; h++) {
    				System.out.println();
    				System.out.println("------------------------------------");
    				System.out.println();
    				System.out.println("heuristic = " + heuristic_names[h]);

    				puzzles[i].resetSearchCount();
    				
    				long startTime = System.currentTimeMillis();
    				AStar search = new AStar(puzzles[i], heuristics[h]);
    				long endTime   = System.currentTimeMillis();

    				if (search.path == null) {
    					System.out.println("NO SOLUTION FOUND.");
    					soln_depth[i][h] = -1;
    				} 
    				else {

    					for (int j = 0; j < search.path.length; j++) {
    						search.path[j].print();
    						System.out.println();
    					}

    					num_expanded[i][h] = puzzles[i].getSearchCount();
    					soln_depth[i][h] = search.path.length - 1;
    					duration[i][h] = endTime - startTime;

    					System.out.println("nodes expanded: " + num_expanded[i][h] + ", soln depth: " + soln_depth[i][h] + ", duration: " + duration[i][h]);

    				}
    			}
    		}
    		
    		GeneralFeatures generalFeatures = new GeneralFeatures();
    		generalFeatures.log_tableResults(num_heuristics,
    										 heuristic_names, 
    										 showTime, 
    										 num_puzzles,
    										 soln_depth, 
    										 num_expanded, 
    										 puzzles, 
    										 duration);
    }
}