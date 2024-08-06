import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4, pyr5;
	private Piece s, sRotated;

	private Piece stick, stick2, stick3,
					l1, l12, l13, l14, l15,
					l2, l22, l23, l24, l25,
					s1, s12, s13,
					s2, s22, s23,
					square, square2;

	protected void setUp() throws Exception {
		super.setUp();

		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		pyr5 = pyr4.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		stick = new Piece(Piece.STICK_STR);
		stick2 = stick.computeNextRotation();
		stick3 = stick2.computeNextRotation();

		l1 = new Piece(Piece.L1_STR);
		l12 = l1.computeNextRotation();
		l13 = l12.computeNextRotation();
		l14 = l13.computeNextRotation();
		l15 = l14.computeNextRotation();

		l2 = new Piece(Piece.L2_STR);
		l22 = l2.computeNextRotation();
		l23 = l22.computeNextRotation();
		l24 = l23.computeNextRotation();
		l25 = l24.computeNextRotation();

		s1 = new Piece(Piece.S1_STR);
		s12 = s1.computeNextRotation();
		s13 = s12.computeNextRotation();

		s2 = new Piece(Piece.S2_STR);
		s22 = s2.computeNextRotation();
		s23 = s22.computeNextRotation();

		square = new Piece(Piece.SQUARE_STR);
		square2 = square.computeNextRotation();
	}
	
	// Here are some sample tests to get you started
	
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());

		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());

		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}
	
	
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));

		assertTrue(Arrays.equals(new int[] {0}, stick.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0, 0}, stick2.getSkirt()));

		assertTrue(Arrays.equals(new int[] {0, 0}, square.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, square2.getSkirt()));

		assertTrue(Arrays.equals(new int[] {0, 0}, l1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, l12.getSkirt()));
		assertTrue(Arrays.equals(new int[] {2, 0}, l13.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 1, 1}, l14.getSkirt()));

	}

	public void testSizes(){
		assertEquals(3, pyr5.getWidth());
		assertEquals(2, pyr5.getHeight());

		assertEquals(1, stick.getWidth());
		assertEquals(4, stick.getHeight());

		assertEquals(4, stick2.getWidth());
		assertEquals(1, stick2.getHeight());

		assertEquals(1, stick3.getWidth());
		assertEquals(4, stick3.getHeight());

		assertEquals(2, square.getWidth());
		assertEquals(2, square.getHeight());

		assertEquals(2, square2.getWidth());
		assertEquals(2, square2.getHeight());

		assertEquals(3, s1.getWidth());
		assertEquals(2, s1.getHeight());

		assertEquals(2, s12.getWidth());
		assertEquals(3, s12.getHeight());

		assertEquals(3, s13.getWidth());
		assertEquals(2, s13.getHeight());

		assertEquals(3, s2.getWidth());
		assertEquals(2, s2.getHeight());

		assertEquals(2, s22.getWidth());
		assertEquals(3, s22.getHeight());

		assertEquals(3, s23.getWidth());
		assertEquals(2, s23.getHeight());

		assertEquals(2, l1.getWidth());
		assertEquals(3, l1.getHeight());

		assertEquals(3, l12.getWidth());
		assertEquals(2, l12.getHeight());

		assertEquals(2, l13.getWidth());
		assertEquals(3, l13.getHeight());

		assertEquals(3, l14.getWidth());
		assertEquals(2, l14.getHeight());

		assertEquals(2, l15.getWidth());
		assertEquals(3, l15.getHeight());

		assertEquals(2, l2.getWidth());
		assertEquals(3, l2.getHeight());

		assertEquals(3, l22.getWidth());
		assertEquals(2, l22.getHeight());

		assertEquals(2, l23.getWidth());
		assertEquals(3, l23.getHeight());

		assertEquals(3, l24.getWidth());
		assertEquals(2, l24.getHeight());

		assertEquals(2, l25.getWidth());
		assertEquals(3, l25.getHeight());

	}

	public void testFastRotations(){
		Piece[] pieces = Piece.getPieces();
		assertTrue(pieces[Piece.STICK].equals(stick));
		assertTrue(pieces[Piece.L1].equals(l1));
		assertTrue(pieces[Piece.L2].equals(l2));
		assertTrue(pieces[Piece.S1].equals(s1));
		assertTrue(pieces[Piece.S2].equals(s2));
		assertTrue(pieces[Piece.SQUARE].equals(square));
		assertTrue(pieces[Piece.PYRAMID].equals(pyr1));

		assertTrue(pieces[Piece.STICK].fastRotation().equals(stick2));
		assertTrue(pieces[Piece.L1].fastRotation().equals(l12));
		assertTrue(pieces[Piece.L2].fastRotation().equals(l22));
		assertTrue(pieces[Piece.S1].fastRotation().equals(s12));
		assertTrue(pieces[Piece.S2].fastRotation().equals(s22));
		assertTrue(pieces[Piece.SQUARE].fastRotation().equals(square2));
		assertTrue(pieces[Piece.PYRAMID].fastRotation().equals(pyr2));

		assertFalse(pieces[Piece.L2].fastRotation().equals(l2));
	}

	public void testException(){
		try {
			Piece p = new Piece("aaa");
		} catch (Exception ex) {
			assertEquals("Could not parse x,y string:aaa", ex.getMessage());
		}

		assertTrue(l1.equals(l15));
		assertTrue(l2.equals(l25));
		assertTrue(pyr1.equals(pyr5));
		assertTrue(s1.equals(s13));
		assertTrue(s2.equals(s23));
		assertTrue(square.equals(square2));
		assertTrue(stick.equals(stick3));
		assertFalse(stick.equals(stick2));
		assertFalse(s1.equals(s12));
		assertFalse(l1.equals(l13));
		assertTrue(l15.computeNextRotation().equals(l12));
	}
}
