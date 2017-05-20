import java.io.IOException;
import java.util.ArrayList;

import api.GeneralFeatures;
import api.Global;
import api.BFS.*;
import api.AStar.*;
import api.AStar.Heuristics.*;

public class Main  {
	static ArrayList<String> lv_bfs_statistics = new ArrayList<String>();

    public static void main(String[] args){	


    	String map = "------" +
			         "-222A-" +
			         ">>-BA-" +
			         "---B--" +
			         "---B--" +
			         "------";
    	
    	BFS bfs_solver    = new BFS(map,6,6, true);
    	lv_bfs_statistics = bfs_solver.applyAlgorithm();
    	
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
    	
    		String[] heuristic_names = null;
    		int num_puzzles = puzzles.length; // puzzles.length;
    		int num_heuristics = 0;

    		int[][] num_expanded = null;
    		int[][] steps = null;
    		long[][] duration = null;

    		// run each heuristic on each puzzle
    		for (int i = 0; i < num_puzzles; i++) {
    			System.out.println(Global.PRINT_RESULT_TBL_04);
    			System.out.println(Global.PRINT_PUZZLE + puzzles[i].getName());
    			
    			// h(x) used in A*
    			Heuristic[] heuristics = {
    					new DFS(puzzles[i]),
    					new BlockingHeuristic(puzzles[i]),
    					new AdvancedHeuristic(puzzles[i])
    				};

    			if (i == 0) {
    				num_heuristics  = heuristics.length;
    				num_expanded    = new int[num_puzzles][num_heuristics];
    				steps           = new int[num_puzzles][num_heuristics];
    				duration        = new long[num_puzzles][num_heuristics];
    				heuristic_names = new String[num_heuristics];
    				
    				for (int h = 0; h < num_heuristics; h++)
    					heuristic_names[h] = heuristics[h].getHeuristicName();
    			}

    			for (int h = 0; h < num_heuristics; h++) {
    			
    				System.out.println(Global.PRINT_RESULT_TBL_05);
    				System.out.println(Global.PRINT_HEURISTIC + heuristic_names[h] + Global.LINE_BREAK);

    				puzzles[i].resetSearchCount();
    				
    				long startTime = System.currentTimeMillis();
    				AStar search = new AStar(puzzles[i], heuristics[h]);
    				long endTime   = System.currentTimeMillis();

    				if (search.path == null) {
    					System.out.println("NO SOLUTION FOUND.");
    					steps[i][h] = -1;
    				} 
    				else {

    					for (int j = 0; j < search.path.length; j++) {
    						search.path[j].print();
    						System.out.println();
    					}

    					num_expanded[i][h] = puzzles[i].getSearchCount();
    					steps[i][h] = search.path.length - 1;
    					duration[i][h] = endTime - startTime;

    					System.out.println(Global.PRINT_STEPS_01    + steps[i][h]    + Global.PRINT_SEPARATOR +
    									   Global.PRINT_DURATION_01 + duration[i][h] + Global.PRINT_SEPARATOR  +
    									   Global.PRINT_EXPNODES_01 + num_expanded[i][h] );

    				}
    			}
    		}
    		
    		GeneralFeatures generalFeatures = new GeneralFeatures();
    		generalFeatures.log_tableResults(num_heuristics,
    										 heuristic_names, 
    										 num_puzzles,
    										 steps, 
    										 num_expanded, 
    										 puzzles, 
    										 duration);
    }
}