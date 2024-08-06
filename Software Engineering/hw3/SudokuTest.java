import junit.framework.TestCase;

public class SudokuTest extends TestCase {
    public void testSolveZeros() {
        String board = "000000000" +
                        "000000000" +
                        "000000000" +
                        "000000000" +
                        "000000000" +
                        "000000000" +
                        "000000000" +
                        "000000000" +
                        "000000000";
        Sudoku sudoku = new Sudoku(Sudoku.textToGrid(board));
        assertTrue(sudoku.getElapsed() >= 0);
        assertEquals(sudoku.solve(), 100);
    }

    public void testSolveWrong1() {
        String board = "001100000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000";
        Sudoku sudoku = new Sudoku(Sudoku.textToGrid(board));
        assertTrue(sudoku.incorrectGrid(Sudoku.textToGrid(board)));
        assertEquals(sudoku.solve(), -1);
    }

    public void testSolveWrong2() {
        String board = "000000000" +
                "100000000" +
                "010000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000";
        Sudoku sudoku = new Sudoku(Sudoku.textToGrid(board));
        assertTrue(sudoku.incorrectGrid(Sudoku.textToGrid(board)));
        assertEquals(sudoku.solve(), -1);
    }

    public void testSolveWrong3() {
        String board = "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000100000" +
                "000000000" +
                "000000000" +
                "000100000" +
                "000000000";
        Sudoku sudoku = new Sudoku(Sudoku.textToGrid(board));
        assertTrue(sudoku.incorrectGrid(Sudoku.textToGrid(board)));
        assertEquals(sudoku.solve(), -1);
    }

    public void testSolve1(){
        String board = "123456789" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000";
        Sudoku sudoku = new Sudoku(Sudoku.textToGrid(board));
        int solutions = sudoku.solve();
        int[][] solution = Sudoku.textToGrid(sudoku.getSolutionText());
        assertFalse(sudoku.incorrectGrid(solution));
    }

    public void testSolve2(){
        Sudoku sudoku = new Sudoku(Sudoku.hardGrid);
        int solutions = sudoku.solve();
        int[][] solution = Sudoku.textToGrid(sudoku.getSolutionText());
        assertTrue(solutions > 0);
        assertFalse(sudoku.incorrectGrid(solution));
    }

    public void testSolve3(){
        Sudoku sudoku = new Sudoku(Sudoku.easyGrid);
        int solutions = sudoku.solve();
        int[][] solution = Sudoku.textToGrid(sudoku.getSolutionText());
        assertTrue(solutions > 0);
        assertFalse(sudoku.incorrectGrid(solution));
    }

    public void testSolve4(){
        Sudoku sudoku = new Sudoku(Sudoku.mediumGrid);
        int solutions = sudoku.solve();
        int[][] solution = Sudoku.textToGrid(sudoku.getSolutionText());
        assertTrue(solutions > 0);
        assertFalse(sudoku.incorrectGrid(solution));
    }
}