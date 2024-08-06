// Board.java

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private int[] widths;
	private int[] heights;
	private boolean[][] xGrid;
	private int[] xWidths;
	private int[] xHeights;
	private boolean DEBUG = true;
	boolean committed;
	private int maxHeight;
	private int xMaxHeight;

	// Here a few trivial methods are provided:

	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.grid = new boolean[width][height];
		this.xGrid = new boolean[width][height];
		this.committed = true;
		this.widths = new int[height];
		this.xWidths = new int[height];
		this.heights = new int[width];
		this.xHeights = new int[width];
		this.maxHeight = 0;
		this.xMaxHeight = 0;
	}


	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}


	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}


	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		return maxHeight;
	}


	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			int[] currentWidths = new int[height];
			int[] currentHeights = new int[width];
			int currentMaxHeight = 0;

			for(int row = 0; row < grid.length; row++){
				for(int col = 0; col < grid[row].length; col++){
					if(grid[row][col]){
						currentWidths[col]++;
						if(col >= currentHeights[row]){
							currentHeights[row] = col + 1;
						}
						if(currentHeights[row] > currentMaxHeight){
							currentMaxHeight = currentHeights[row];
						}
					}
				}
			}
			if(!Arrays.equals(currentWidths, widths)) {
				throw new RuntimeException("Sanity Check Exception - widths");
			}
			if(!Arrays.equals(currentHeights, heights)){
				throw new RuntimeException("Sanity Check Exception - heights");
			}
			if(currentMaxHeight != maxHeight){
				throw new RuntimeException("Sanity Check Exception - max height");
			}
		}
	}

	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.

	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int res = 0;
		int[] skirt = piece.getSkirt();
		for(int i = 0; i < piece.getWidth(); i++){
			int curr = heights[x + i] - skirt[i];
			res = Math.max(res, curr);
		}
		return res;
	}


	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}


	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y];
	}


	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		return (!isValid(x, y) || grid[x][y]);
	}

	private boolean isValid(int x, int y){
		return (x < width && x >= 0 && y < height && y >= 0);
	}


	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.

	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		committed = false;

		int result = PLACE_OK;

		rememberPreviousBoard();

		for(TPoint point : piece.getBody()){
			int currX = x + point.x;
			int currY = y + point.y;
			if(!isValid(currX, currY)) {
				return PLACE_OUT_BOUNDS;
			}
			if(grid[currX][currY]){
				return PLACE_BAD;
			}
			grid[currX][currY] = true;
			widths[currY]++;
			if(heights[currX] < currY + 1){
				heights[currX] = currY + 1;
			}
			if(heights[currX] > maxHeight){
				maxHeight = heights[currX];
			}

			if(widths[currY] == width){
				result = PLACE_ROW_FILLED;
			}
		}
		sanityCheck();
		return result;
	}

	private void rememberPreviousBoard() {
		System.arraycopy(widths, 0, xWidths, 0, height);
		System.arraycopy(heights, 0, xHeights, 0, width);
		for(int i = 0; i < width; i++) {
			System.arraycopy(grid[i], 0, xGrid[i], 0, height);
		}
		xMaxHeight = maxHeight;
	}


	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		committed = false;
		int row = 0;
		for(int i = 0; i < height; i++){
			if(widths[i] == width){
				rowsCleared++;
			}else if(rowsCleared > 0){
				widths[row] = widths[i];
				for(int j = 0; j < width; j++){
					grid[j][row] = grid[j][i];
				}
				row++;
			}else{
				row++;
			}
		}
		for(; row < height; row++){
			widths[row] = 0;
			maxHeight--;
			for(int i = 0; i < width; i++){
				grid[i][row] = false;
			}
		}
		for(int i = 0; i < heights.length; i++){
			heights[i] = 0;
		}
		for(int x = 0; x < grid.length; x++){
			for(int y = 0; y < grid[x].length; y++){
				if(grid[x][y]){
					if(y >= heights[x]){
						heights[x] = y + 1;
					}
				}
			}
		}
		sanityCheck();
		return rowsCleared;
	}



	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(!committed){
			commit();
			maxHeight = xMaxHeight;
			int[] tmp = widths;
			widths = xWidths;
			xWidths = tmp;
			int[] tmp1 = heights;
			heights = xHeights;
			xHeights = tmp1;
			boolean[][] tmp2 = grid;
			grid = xGrid;
			xGrid = tmp2;
		}
	}


	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}



	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility)
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


