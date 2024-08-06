// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int maxRow = -1, maxCol = -1;
		int minRow = Integer.MAX_VALUE;
		int minCol = Integer.MAX_VALUE;

		boolean found = false;
		for(int row = 0; row < grid.length; row++){
			for(int col = 0; col < grid[row].length; col++){
				char currChar = grid[row][col];
				if(currChar == ch){
					found = true;
					if(minRow > row){
						minRow = row;
					}
					if(minCol > col){
						minCol = col;
					}
					if(maxRow < row + 1){
						maxRow = row + 1;
					}
					if(maxCol < col + 1){
						maxCol = col + 1;
					}
				}
			}
		}
		if(!found) return 0;

		return (maxRow - minRow) * (maxCol - minCol);
	}
	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int result = 0;
		for(int row = 0; row < grid.length; row++){
			for(int col = 0; col < grid[row].length; col++){
				if(isPlus(row, col)){
					result++;
				}
			}
		}
		return result;
	}

	private boolean isValidCell(int row, int col){
		return row >= 0 &&
				col >= 0 &&
				row < grid.length &&
				col < grid[row].length;
	}

	private boolean isPlus(int row, int col) {
		char mainCharacter = grid[row][col];
		int up = countMainCharacters(row, col, mainCharacter, -1, 0);
		int down = countMainCharacters(row, col, mainCharacter, 1, 0);
		int left = countMainCharacters(row, col, mainCharacter, 0, -1);
		int right = countMainCharacters(row, col, mainCharacter, 0, 1);
		return up > 0 && up == down && up == left && up == right;
	}

	private int countMainCharacters(int row, int col, char mainCharacter, int rowShift, int colShift) {
		int nextRow = row + rowShift;
		int nextCol = col + colShift;
		if(!isValidCell(nextRow, nextCol)){
			return 0;
		}
		if(grid[nextRow][nextCol] != mainCharacter){
			return 0;
		}else{
			return 1 + countMainCharacters(nextRow, nextCol, mainCharacter, rowShift, colShift);
		}
	}


}
