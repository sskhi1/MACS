import junit.framework.TestCase;


public class BoardTest extends TestCase {
	Board b, myBoard;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated, square;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	
	protected void setUp() throws Exception {
		b = new Board(3, 6);
		myBoard = new Board(10, 10);

		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);

		square = new Piece(Piece.SQUARE_STR);
		myBoard.place(square, 2, 0);
	}
	
	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(10, myBoard.getHeight());
		assertEquals(10, myBoard.getWidth());
		assertEquals(2, myBoard.getColumnHeight(2));
		assertEquals(0, myBoard.getColumnHeight(1));
		myBoard.commit();
		myBoard.place(square, 2, 2);
		assertEquals(4, myBoard.getColumnHeight(2));

	}
	
	// Place sRotated into the board, then check some measures
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}
	
	// Makre  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	public void testGetGrid(){
		assertTrue(myBoard.getGrid(10, 10));
		assertTrue(myBoard.getGrid(12, 5));
		assertFalse(myBoard.getGrid(4, 5));
		assertFalse(myBoard.getGrid(0, 0));
	}

	public void testToString(){
		String board ="|          |" + "\n"
					+ "|          |" + "\n"
					+ "|          |" + "\n"
					+ "|          |" + "\n"
					+ "|          |" + "\n"
					+ "|          |" + "\n"
					+ "|          |" + "\n"
					+ "|          |" + "\n"
					+ "|  ++      |" + "\n"
					+ "|  ++      |" + "\n"
					+ "------------";
		assertEquals(board, myBoard.toString());
	}

	public void testPlaceBadInputs(){
		Piece p = new Piece(Piece.SQUARE_STR);
		myBoard.commit();
		assertEquals(myBoard.place(p, -1, 0), Board.PLACE_OUT_BOUNDS);
		myBoard.commit();
		assertEquals(myBoard.place(p, 1, 0), Board.PLACE_BAD);
	}

	public void testClearRows(){
		myBoard.commit();
		myBoard.place(square, 0, 0);
		myBoard.commit();
		myBoard.place(square, 4, 0);
		myBoard.commit();
		myBoard.place(square, 6, 0);
		myBoard.commit();
		myBoard.place(s, 8, 0);
		// System.out.println(myBoard.toString());
		myBoard.commit();
		myBoard.clearRows();
		/*
		for(int i = 0; i < myBoard.getWidth(); i++){
			System.out.println(myBoard.getColumnHeight(i));
		}
		 */
		assertEquals(myBoard.getColumnHeight(7), 1);
		assertEquals(myBoard.getColumnHeight(8), 0);

		myBoard.commit();
		myBoard.place(square, 0, 1);
		myBoard.commit();
		myBoard.place(square, 2, 1);
		myBoard.commit();
		myBoard.place(square, 4, 1);
		myBoard.commit();
		myBoard.place(square, 6, 1);
		myBoard.commit();
		myBoard.place(square, 8, 1);
		myBoard.commit();
		myBoard.place(square, 0, 3);
		myBoard.commit();
		myBoard.place(square, 2, 3);
		myBoard.commit();
		myBoard.place(square, 4, 3);
		myBoard.commit();
		myBoard.place(square, 6, 3);
		myBoard.commit();
		myBoard.place(square, 8, 3);
		// System.out.println(myBoard.toString());
		myBoard.commit();
		int cleared = myBoard.clearRows();
		assertEquals(cleared, 4);
		assertEquals(myBoard.getColumnHeight(7), 1);
		assertEquals(myBoard.getColumnHeight(8), 0);
	}

	public void testUndo(){
		myBoard.undo();
		for(int i = 0; i < myBoard.getWidth(); i++){
			assertEquals(myBoard.getColumnHeight(i), 0);
		}
	}

	public void testDropHeight(){
		assertEquals(myBoard.dropHeight(square, 1), 2);
	}
}
