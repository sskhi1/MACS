import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.

	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
			"1 6 4 0 0 0 0 0 2",
			"2 0 0 4 0 3 9 1 0",
			"0 0 5 0 8 0 4 0 7",
			"0 9 0 0 0 6 5 0 0",
			"5 0 0 1 0 2 0 0 8",
			"0 0 8 9 0 0 0 3 0",
			"8 0 9 0 4 0 2 0 0",
			"0 7 3 5 0 9 0 0 1",
			"4 0 0 0 0 0 6 7 9");


	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
			"530070000",
			"600195000",
			"098000060",
			"800060003",
			"400803001",
			"700020006",
			"060000280",
			"000419005",
			"000080079");

	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
			"3 7 0 0 0 0 0 8 0",
			"0 0 1 0 9 3 0 0 0",
			"0 4 0 7 8 0 0 0 3",
			"0 9 3 8 0 0 0 1 2",
			"0 0 0 0 4 0 0 0 0",
			"5 2 0 0 0 6 7 9 0",
			"6 0 0 0 2 1 0 4 0",
			"0 0 0 5 3 0 9 0 0",
			"0 3 0 0 0 0 0 5 1");


	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;

	// Provided various static utility methods to
	// convert data formats to int[][] grid.

	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}


	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}

		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}


	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);

		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}

	private long elapseTime;
	private int[][] grid;
	private int[][] solution;
	private boolean solved;
	private List<Spot> spots;
	private int totalSolutions;


	@Override
	public String toString() {
		return gridToString(grid);
	}

	/**
	 * Given 9x9 grid, return a string containing 81 numbers.
	 * @param grid 9x9 grid
	 * @return string
	 */
	private String gridToString(int[][] grid){
		assert(grid.length == SIZE && grid[0].length == SIZE);

		String res = "";
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				res += grid[i][j] + " ";
			}
			if(i != SIZE - 1) res += '\n';
		}
		return res;
	}

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		this.elapseTime = 0;
		this.grid = ints;
		this.solution = new int[SIZE][SIZE];
		this.solved = false;
		generateSpotsList();
	}

	private void generateSpotsList() {
		spots = new ArrayList<>();
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				if(grid[i][j] == 0){
					spots.add(new Spot(i, j));
				}
			}
		}
		Collections.sort(spots);
	}


	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		if(incorrectGrid(grid)){
			elapseTime = 0;
			return -1;
		}
		this.totalSolutions = 0;
		long start = System.currentTimeMillis();
		solveSudokuRec(0);
		long end   = System.currentTimeMillis();
		elapseTime = end - start;
		return totalSolutions;
	}

	/**
	 * checks if the input grid is correct or not
	 * @param grid
	 * @return true if grid is incorrect (false otherwise)
	 */
	public boolean incorrectGrid(int[][] grid) {
		Set<Integer> grid_nums_horizontal = new HashSet<>();
		Set<Integer> grid_nums_vertical = new HashSet<>();
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				if(grid[i][j] < 0 || grid[i][j] > 9){
					return true;
				}
				if(grid[i][j] != 0){
					if(grid_nums_horizontal.contains(grid[i][j])){
						return true;
					}
					grid_nums_horizontal.add(grid[i][j]);
				}
			}
			grid_nums_horizontal.clear();
		}

		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				if(grid[j][i] != 0){
					if(grid_nums_vertical.contains(grid[j][i])){
						return true;
					}
					grid_nums_vertical.add(grid[j][i]);
				}
			}
			grid_nums_vertical.clear();
		}

		Set<Integer> grid_nums_squares =  new HashSet<>();
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				for(int row = i * 3; row < i * 3 + 3; row++){
					for(int col = j * 3; col < j * 3 + 3; col++){
						if(grid[row][col] != 0){
							if(grid_nums_squares.contains(grid[row][col])){
								return true;
							}
							grid_nums_squares.add(grid[row][col]);
						}
					}
				}
				grid_nums_squares.clear();
			}
		}
		return false;
	}

	/**
	 * Given position, solve sudoku and count number of solutions.
	 * @param spotPos current position
	 */
	private void solveSudokuRec(int spotPos) {
		if(totalSolutions >= MAX_SOLUTIONS){
			return;
		}
		if(spotPos >= spots.size()){
			if(!solved){
				for(int i = 0; i < SIZE; i++){
					System.arraycopy(grid[i], 0, solution[i], 0, SIZE);
				}
				solved = true;
			}
			totalSolutions++;
			return;
		}

		Spot currSpot = spots.get(spotPos);
		for(int variant : currSpot.totalVariants()){
			currSpot.set(variant);
			solveSudokuRec(spotPos + 1);
			currSpot.set(0);
		}
	}

	public String getSolutionText() {
		// assert(!incorrectGrid(solution));
		return gridToString(solution);
	}

	public long getElapsed() {
		return elapseTime;
	}


	// spot - inner class
	private class Spot implements Comparable{
		private int row, col;
		private int variantsNum;

		/**
		 * Sets up based on current position.
		 */
		public Spot(int row, int col){
			this.row = row;
			this.col = col;
			this.variantsNum = totalVariants().size();
		}

		/**
		 * @return set of variants available for current position.
		 */
		private HashSet<Integer> totalVariants() {
			HashSet<Integer> variants = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
			for(int i = 0; i < SIZE; i++){
				variants.remove(grid[i][col]);
				variants.remove(grid[row][i]);
				variants.remove(grid[row/PART * PART + i/PART][col/PART * PART + i % PART]);
			}
			return variants;
		}

		/**
		 * Make current position - val.
		 */
		public void set(int val){
			grid[row][col] = val;
		}

		@Override
		public int compareTo(Object o) {
			return (this.variantsNum - ((Spot) o).variantsNum);
		}
	}

}