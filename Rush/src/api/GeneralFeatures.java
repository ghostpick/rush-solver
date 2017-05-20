package api;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import api.AStar.Puzzle;

public class GeneralFeatures {
	
    //log
    public void log(String current, boolean state){

    	String currentState = current + Global.LINE_BREAK;

        try(FileWriter fw = new FileWriter(Global.SRC_BFS , true);
    		    BufferedWriter bw = new BufferedWriter(fw);
    		    PrintWriter out = new PrintWriter(bw)){
    		    out.println(currentState);
    		} 
        catch (IOException e) {}
    }
    
    
    public void log_tableResults(int heuristics_mumber, 
    							 String[] heuristic_names,
    							 int num_puzzles,
    							 int[][] steps,
    							 int[][] num_expanded,
    							 Puzzle[] puzzles,
    							 long[][] duration){
    	
    	int i = 0;
    	int j = 0;
    			
    	// print the results in a table
    	System.out.println(Global.PRINT_RESULT_TBL_04);
    	System.out.println(Global.PRINT_RESULT_TBL_00);
		System.out.println(Global.PRINT_RESULT_TBL_03);
		System.out.println(Global.PRINT_RESULT_TBL_01);
		System.out.println(Global.PRINT_RESULT_TBL_03);
		System.out.println(Global.PRINT_RESULT_TBL_02);
		System.out.println(Global.PRINT_RESULT_TBL_03);


		for (i = 0; i < num_puzzles; i++) {
			System.out.print("| " + this.log_tableResults_dynSpaces(puzzles[i].getName(), 10,true));

			for (j = 0; j < heuristics_mumber; j++) {
				if (steps[i][j] >= 0) {
					System.out.print(" | " + 
					this.log_tableResults_dynSpaces(Integer.toString(steps[i][j]),        5, false) + " " + 
					this.log_tableResults_dynSpaces(Long.toString(duration[i][j]),        6, false) + " " + 
					this.log_tableResults_dynSpaces(Integer.toString(num_expanded[i][j]), 8, false)  
					);
				}
			}
			System.out.println(" |");
		}
		System.out.println(Global.PRINT_RESULT_TBL_03);
    	System.out.println(Global.PRINT_RESULT_TBL_04);

    }
    
    

	private String log_tableResults_dynSpaces(String text, int n, boolean floatLeft) {
		
		for (int i = n - text.length(); i > 0; i--){
			if(floatLeft)
				text = text + " ";
			else
				text = " " + text;
		}
		
		if(floatLeft)
			return text.substring(0, n);
		else
			return text;
	}
}
