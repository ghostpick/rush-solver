package api;

import java.util.ArrayList;
import api.Position;

public class Parser {
	
	private ArrayList<Position> arrMap;
	
	
	Parser(){
		
	}
	
	public ArrayList<Position> setArrMap (int rows, int cols, String map){
		
		arrMap 			= new ArrayList<Position>();
		Position lv_pos = new Position();
		
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < cols; j++){
				lv_pos.content = map.charAt(matrixToString(rows,i,j));
				lv_pos.x	   = i;
				lv_pos.y	   = j;
				arrMap.add(lv_pos);
			}
		
		
		return this.arrMap;
	}
	
    private int matrixToString(int totalRows, int currentRow, int col) {
        return currentRow * totalRows + col;
    }

    
    
}
