package minesweeper.BoardSquare;

import static org.junit.Assert.assertEquals;
import minesweeper.BoardSquare.Square.boomException;

import org.junit.Test;

/**
 * 
 * Test Strategy:
 * 
 * 1. Test that the Board can load a text file with legal format
 * 2. Test that the Board throws FileException if the file is illegal
 *     A. The x and y dimensions of the file are different
 *     B. The file contain numbers other than 0 and 1
 * 3. Test the boolean constructor of the Board
 * 4. Test that 'flag' and 'deflag' lead to the expected behavior of the Board state
 * 5. Test the Board state following a 'dig' message
 *     A. single digging
 *     B. recursive digging
 *     C. correct state update after a BOOM message
 *
 */

public class BoardTest {

    @Test
    public void testgoodBoard() {
        Board board = new Board("src/minesweeper/server/goodBoardtest1.txt");
        String expected = "- - - - -\n- - - - -\n- - - - -\n- - - - -\n- - - - -\n";
        assertEquals(5, board.size());
        assertEquals(expected, board.toString());
        String expected1 = "1 B 2 2 B\n1 1 3 B 3\n0 0 3 B 3\n0 0 2 B 2\n0 0 1 1 1\n";
        assertEquals(expected1, board.bombDistri());
        board.dig(0, 0);
        String expected2 = "1 - - - -\n- - - - -\n- - - - -\n- - - - -\n- - - - -\n";
        assertEquals(expected2, board.toString());
        board.dig(3, 4);
        String expected3 = "1 - - - -\n- - - - -\n- - - - -\n- - - - -\n- - - 1 -\n";
        assertEquals(expected3, board.toString());
        board.dig(2, 4);
        String expected4 = "1 - - - -\n- - - - -\n- - - - -\n- - - - -\n- - 1 1 -\n";
        assertEquals(expected4, board.toString());

    }

    @Test
    public void testBooleanConstructor() {
        boolean[][] input = { { false, true, false }, { true, false, true },
                { false, false, false } };
        Board b = new Board(input);
        assertEquals("- - -\n- - -\n- - -\n", b.toString());
        assertEquals("2 B 2\nB 3 B\n1 2 1\n", b.bombDistri());

    }

    @Test(expected = Exception.class)
    public void testBadBoard1() throws Exception {
    	// expected size error
        new Board("src/minesweeper/server/badBoardtest1.txt");
    }
    
    
    
    @Test(expected = Exception.class)
    public void testBadBoard2() throws Exception {
    	// board file incorrectly contains a "2"
        new Board("src/minesweeper/server/badBoardtest2.txt");
    }
    
    @Test
    public void testFlag() {
        boolean[][] input = {{true}};
        Board b = new Board(input);
        b.flag(0, 0);
        assertEquals("F\n", b.toString());

    }

    @Test
    public void testDeflag() {
        boolean[][] input = {{true}};
        Board b = new Board(input);
        b.flag(0, 0);
        b.deflag(0, 0);
        assertEquals("-\n", b.toString());
    }

    @Test
    public void testDigNoRecursive() {
    	Board board = new Board("src/minesweeper/server/goodBoardtest1.txt");
        board.dig(2, 0);
        String expected = "- - 2 - -\n- - - - -\n- - - - -\n- - - - -\n- - - - -\n";
        assertEquals(expected, board.toString());
    }

    @Test
    public void testDigRecursive() {
        boolean[][] input = { { true, true, false }, { true, false, false },
                { false, false, false } };
        Board board = new Board(input);
        board.dig(2, 2);
        assertEquals("- - -\n- 3 1\n- 1  \n", board.toString());
    }


    @Test
    public void testBoomMessage() {
    	Board board = new Board("src/minesweeper/server/goodBoardtest1.txt");
        assertEquals(boomException.message, board.dig(1, 0));
        String expected = "-   1 - -\n- - - - -\n- - - - -\n- - - - -\n- - - - -\n";
        assertEquals(expected, board.dig(2, 0));

    }

}