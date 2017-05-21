package api;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import api.AStar.Puzzle;

public class GeneralFeatures {
	
    //log
    public void log(String line, boolean lineBreak){
    	String line_1 = "";
    	
    	if (lineBreak)
    		line_1 = line + Global.LINE_BREAK;
    	else
    		line_1 = line;  	

        try(FileWriter fw = new FileWriter(Global.SRC_LOG , true);
    		    BufferedWriter bw = new BufferedWriter(fw);
    		    PrintWriter out = new PrintWriter(bw)){
    		    out.println(line_1);
    		} 
        catch (IOException e) {}
    }
    
    
    public void log_steps(String current, boolean state, int boardRows){
    	String currentState = Global.LINE_BREAK;

    	if (state){       
	    	for(int i = 0; i < current.length(); i++){
	    		currentState += current.charAt(i);
	    		
	    		if ((i+1)%boardRows == 0)
	    			currentState += Global.LINE_BREAK;
	    	}
    	}
    	else
    		currentState = current + Global.LINE_BREAK;

        try(FileWriter fw = new FileWriter(Global.SRC_BFS, true);
    		    BufferedWriter bw 	= new BufferedWriter(fw);
    		    PrintWriter out 	= new PrintWriter(bw)){
        	
    		    out.println(currentState);

    		} catch (IOException e) {}
    }
    
    
    
    public void log_tableResults(int heuristics_mumber, 
    							 String[] heuristic_names,
    							 int num_puzzles,
    							 int[][] steps,
    							 int[][] num_expanded,
    							 Puzzle[] puzzles,
    							 long[][] duration){
    	
    	int i					= 0;
    	int j 				  	= 0;
        GeneralFeatures go_gf	= new GeneralFeatures();
        String line				= "";
			
    	// print the results in a table
        line = 	Global.PRINT_RESULT_TBL_04 + Global.LINE_BREAK +
        		Global.PRINT_RESULT_TBL_00 + Global.LINE_BREAK +
        		Global.PRINT_RESULT_TBL_03 + Global.LINE_BREAK +
        		Global.PRINT_RESULT_TBL_01 + Global.LINE_BREAK +
        		Global.PRINT_RESULT_TBL_03 + Global.LINE_BREAK +
        		Global.PRINT_RESULT_TBL_02 + Global.LINE_BREAK +
        		Global.PRINT_RESULT_TBL_03 + Global.LINE_BREAK;

		for (i = 0; i < num_puzzles; i++) {
			line = line + "| " + this.log_tableResults_dynSpaces(puzzles[i].getName(), 10,true);

			
			for (j = 0; j < heuristics_mumber; j++) {
				if (steps[i][j] >= 0) {
					line =  line + " | " + 
							this.log_tableResults_dynSpaces(Integer.toString(steps[i][j]),        5, false) + " " + 
							this.log_tableResults_dynSpaces(Long.toString(duration[i][j]),        6, false) + " " + 
							this.log_tableResults_dynSpaces(Integer.toString(num_expanded[i][j]), 8, false);
				}
			}
			line = line + " |" + Global.LINE_BREAK;  
		}
		line = line + Global.PRINT_RESULT_TBL_03 + Global.LINE_BREAK +
				      Global.PRINT_RESULT_TBL_04 + Global.LINE_BREAK;

		
		System.out.println(line);
    	go_gf.log(line, false);
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
