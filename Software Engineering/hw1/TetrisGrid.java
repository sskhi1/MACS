//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

public class TetrisGrid {
	private boolean[][] grid;
	
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}
	
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		boolean[][] newGrid = new boolean[grid.length][grid[0].length];
		int currCol = 0;
		for(int col = 0; col < grid[0].length; col++){
			if(!isFull(col)){
				for(int row = 0; row < grid.length; row++){
					newGrid[row][currCol] = grid[row][col];
				}
				currCol++;
			}
		}

		for(int col = currCol; col < grid[0].length; col++){
			for(int row = 0; row < grid.length; row++){
				newGrid[row][col] = false;
			}
		}
		grid = newGrid;
	}

	private boolean isFull(int col) {
		for (boolean[] booleans : grid) {
			if (!booleans[col]) {
				return false;
			}
		}
		return true;
	}


	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return grid;
	}
}
