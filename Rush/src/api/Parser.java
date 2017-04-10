package api;

import java.util.ArrayList;
import java.util.List;

public class Parser {
	
	private List<List<Character>> arrMap;

	Parser(){}
	
	public List<List<Character>> setArrMap (int rows, int cols, String map){
		
		this.arrMap = new ArrayList<List<Character>>();
		
		for(int i = 0; i < rows; i++){
			List<Character>  lv_array = new ArrayList<Character>();
			
			for(int j = 0; j < cols; j++){
				lv_array.add(map.charAt(matrixToString(rows,i,j)));
			}
			this.arrMap.add(lv_array);
		}
				
		return this.arrMap;
	}

    private int matrixToString(int totalRows, int currentRow, int col) {
        return currentRow * totalRows + col;
    }

    
    
}
