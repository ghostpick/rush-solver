package api;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import api.AStar.BranchingFactor;
import api.AStar.Puzzle;

public class GeneralFeatures {
	
    //log
    public void log(String current, boolean state){

    	String currentState = current + Global.LINE_BREAK;

        try(FileWriter fw = new FileWriter(Global.SRC_BFS , true);
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
    
    
    public void log_tableResults(int num_heuristics, 
    							 String[] heuristic_names,
    							 boolean showTime,
    							 int num_puzzles,
    							 int[][] soln_depth,
    							 int[][] num_expanded,
    							 Puzzle[] puzzles,
    							 long[][] duration){
    	
		// print the results in a table
		System.out.println();
		System.out.println();
		System.out.println();

		System.out.print("          ");
		for (int h = 0; h < num_heuristics; h++)
			if (showTime) {
				System.out.print(" |    " + this.log_tableResults_right(heuristic_names[h], 24));
			} else {
				System.out.print(" |    " + this.log_tableResults_right(heuristic_names[h], 18));
			}
		System.out.println();

		System.out.print("name      ");
		for (int h = 0; h < num_heuristics; h++)
			if (showTime) {
				System.out.print(" |    nodes dpth  br.fac  dur.");
			} else {
				System.out.print(" |    nodes dpth  br.fac");
			}
		System.out.println();

		System.out.print("----------");
		for (int h = 0; h < num_heuristics; h++)
			if (showTime) {
				System.out.print("-+----------------------------");
			} else {
				System.out.print("-+----------------------");
			}
		System.out.println();

		NumberFormat brfac_nf = new DecimalFormat("##0.000");

		for (int i = 0; i < num_puzzles; i++) {
			System.out.print(this.log_tableResults_right(puzzles[i].getName(), 10));

			for (int h = 0; h < num_heuristics; h++) {
				if (soln_depth[i][h] < 0) {
					System.out.print(" |  ** search failed ** ");
				} else {
					System.out.print(" | " + 
					this.log_tableResults_left(Integer.toString(num_expanded[i][h]), 8)  + " " + 
					this.log_tableResults_left(Integer.toString(soln_depth[i][h]), 4)    + " " + 
							  this.log_tableResults_left(
									brfac_nf.format(BranchingFactor.compute(num_expanded[i][h], soln_depth[i][h])), 7) + 
							  			(showTime ? log_tableResults_left(Long.toString(duration[i][h]), 6) : "") );
				}
			}
			System.out.println();
		}
    }
    
    
	private  String log_tableResults_left(String s, int n) {
		for (int i = n - s.length(); i > 0; i--)
			s = " " + s;
		return s;
	}

	private String log_tableResults_right(String s, int n) {
		for (int i = n - s.length(); i > 0; i--)
			s = s + " ";
		return s.substring(0, n);
	}
    

}
