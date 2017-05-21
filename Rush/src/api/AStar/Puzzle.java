package api.AStar;

import java.io.*;
import java.util.*;
import api.Global;

public class Puzzle {

	private String name;
	private Node initNode;
	private int searchCount;
	private int numCars;
	private int fixedPos[];
	private int carSize[];
	private boolean carOrient[];
	private int gridSize;
	private static ArrayList<String> arr_bfsMaps = new ArrayList<String> ();
	
	public static ArrayList<String> get_arr_bfsMaps() 	{ return arr_bfsMaps;  }
	
	public int getNumCars() 							{ return numCars;      }

	public int getFixedPosition(int v)					{ return fixedPos[v];  }

	public int getCarSize(int v) 						{ return carSize[v];   }

	public boolean getCarOrient(int v) 					{ return carOrient[v]; }

	public void incrementSearchCount(int d) 			{ searchCount += d;    }

	public int getSearchCount() 						{ return searchCount;  }
	
	public void resetSearchCount() 						{ searchCount = 1;     }

	public String getName() 							{ return name;		   }

	public int getGridSize() 							{ return gridSize;     }

	public Node getInitNode() 							{ return initNode;     }


	public Puzzle(String name, int gridSize, int numCars, boolean orient[], int size[], int x[], int y[]) {
		this.name 		= name;
		this.numCars 	= numCars;
		this.gridSize	= gridSize;
		
		// Check positive number of cars
		if (numCars <= 0) { System.exit(0); }
		
		carOrient = new boolean[numCars];
		carSize = new int[numCars];
		fixedPos = new int[numCars];
		int varPos[] = new int[numCars];

		boolean grid[][] = new boolean[gridSize][gridSize];

		for (int v = 0; v < numCars; v++) {
			carOrient[v] = orient[v];
			carSize[v] = size[v];
			
			// Check positive number of cars
			if (size[v] <= 0)
				 System.exit(0);
			
			// Check cars in the of grid"
			if (x[v] < 0 || y[v] < 0 || (orient[v] && y[v] + size[v] > gridSize)
					|| (!orient[v] && x[v] + size[v] > gridSize))
				System.exit(0);

			for (int d = 0; d < size[v]; d++) {
				int xv = x[v], yv = y[v];
				if (orient[v])
					yv += d;
				else
					xv += d;
				
				// check overlap cars
				if (grid[xv][yv])
					 System.exit(0);
				
				grid[xv][yv] = true;
			}

			if (orient[v]) {
				fixedPos[v] = x[v];
				varPos[v] = y[v];
			} else {
				fixedPos[v] = y[v];
				varPos[v] = x[v];
			}
		}
		initNode = new Node(new State(this, varPos), 0, null);
		resetSearchCount();
	}

	// Puzzle reader
	public static Puzzle[] readPuzzlesFromFile(String filename) throws FileNotFoundException, IOException {

		@SuppressWarnings("resource")
		BufferedReader in 			 = new BufferedReader(new FileReader(filename));
		ArrayList<Puzzle> puzzles  	 = new ArrayList<Puzzle> ();
		ArrayList<Obstacle> car_list = null;
		String name 				 = null;
		String line					 = "";
		String[] words 				 = null;
		int read_mode 				 = 0;
		int gridsize 				 = 0;

		while ((line = in.readLine()) != null) {
			line  = line.trim();
			words = line.split("\\s+");
			
			if (line.equals(""))
				continue;
			
			// name
			if (read_mode == 0) { 
				name = line;
				car_list = new ArrayList<Obstacle>();
				read_mode = 1;
				
			}
			
			// grid size
			else if (read_mode == 1) { 	
				try {
					gridsize = Integer.parseInt(words[0]);
				} 
		    	catch (Exception e) { e.printStackTrace(); }
				read_mode = 2;

			} 
			
			// end of puzzle
			else if (line.equals(".")) { 
				int numcars = car_list.size();
				boolean orient[] = new boolean[numcars];
				int size[] = new int[numcars];
				int x[] = new int[numcars];
				int y[] = new int[numcars];

				for (int v = 0; v < numcars; v++) {
					Obstacle carrec = (Obstacle) car_list.get(v);
					orient[v] = carrec.orient;
					size[v] = carrec.size;
					x[v] = carrec.x;
					y[v] = carrec.y;
					
				}

				puzzles.add(new Puzzle(name, gridsize, numcars, orient, size, x, y));
				import_toBFS(gridsize, numcars, orient, size, x, y);   
				read_mode = 0;
			
			} 
			
			else {
				Obstacle carrec = new Obstacle();

				try {
					carrec.x 			= Integer.parseInt(words[0]);
					carrec.y 			= Integer.parseInt(words[1]);
					String orientation  = words[2].toLowerCase();
					carrec.orient 		= orientation.equals("v");
					carrec.size 		= Integer.parseInt(words[3]);
				} 
		    	catch (Exception e) { e.printStackTrace(); }
				car_list.add(carrec);
			}
		}
		return (Puzzle[]) puzzles.toArray(new Puzzle[0]);
	}

	
	public static void import_toBFS(int gridsize, int car_total, boolean car_orient[], int size[], int x[], int y[]){
			
		StringBuilder	lv_map_builder	= new StringBuilder();
		char[][] 		lv_arrMap		= new char [gridsize][gridsize];
		int 			lv_interations 	= 0;
		boolean 		lv_orient		= false;
		int 			lv_x			= 0;
		int 			lv_y			= 0;
		int 			lv_carSize		= 0;
		int 			lv_increment	= 0;;
		char 			lv_val 			= '-';			
		
		
		for(int a=0; a < gridsize; a++)
			for(int b=0; b < gridsize; b++)
				lv_arrMap[a][b] = '-';
						

	    while(lv_interations != car_total){
	    	lv_orient   =  car_orient[lv_interations];
	    	lv_x		=  x[lv_interations];
	    	lv_y		=  y[lv_interations];
	    	lv_carSize  =  size[lv_interations];
	    	
	    	if(lv_interations == 0){
	    		lv_val = Global.BFS_MYCAR;
	    		lv_increment =  lv_x;
	    	}
	    	else if (lv_carSize == 2 && !lv_orient){
	    		lv_val = Global.BFS_H2;
	    		lv_increment =  lv_x;
	    	}
	    	else if (lv_carSize == 3 && !lv_orient){
	    		lv_val = Global.BFS_H3;
	    		lv_increment =  lv_x;	
	    	}
	    	else if (lv_carSize == 2 && lv_orient){
	    		lv_val = Global.BFS_V2;
	    		lv_increment =  lv_y;
	    	}
	    	else if (lv_carSize == 3 && lv_orient){
	    		lv_val = Global.BFS_V3;
	    		lv_increment =  lv_y;
	    	}
	    	    	
	    	while(lv_carSize > 0){
	    		if(!lv_orient){
	    			lv_arrMap[lv_y][lv_increment] = lv_val;
	    			lv_increment = lv_increment + 1;
	    		}
	    		else{
	    			lv_arrMap[lv_increment][lv_x] = lv_val;
	    			lv_increment = lv_increment + 1;
	    		}
	    		lv_carSize = lv_carSize - 1;
		
			}
	    	
	    	lv_interations+=1;
	    }
	    
	    //Mapping to string
		for(int a=0; a < gridsize; a++){
			for(int b=0; b < gridsize; b++)
				lv_map_builder.append(lv_arrMap[a][b]);
		}
		arr_bfsMaps.add(lv_map_builder.toString());   
	}
	
	
	private static class Obstacle {
		boolean orient;
		int size;
		int x;
		int y;
	}
}
