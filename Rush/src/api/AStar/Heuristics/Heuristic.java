package api.AStar.Heuristics;

import api.AStar.State;

//Interface for all heuristics
public interface Heuristic {
	
	// Returns HeuristicName
	public String getHeuristicName();

	// Returns the value of the heuristic function at the given state.
	public int getValue(State state);
}
