package api.AStar.Heuristics;

import api.AStar.Puzzle;
import api.AStar.State;

 // Trivial heuristic function that always returns 0.
public class Simple_H implements Heuristic {
	
	private String name = "SIMPLE";

	public Simple_H(Puzzle puzzle) {}

	// Returns the value of the heuristic function at the given state.
	@Override
	public int getValue(State state) { return 0; }

	@Override
	public String getHeuristicName() { return this.name; }
}
