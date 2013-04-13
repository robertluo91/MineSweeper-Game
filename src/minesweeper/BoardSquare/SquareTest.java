package minesweeper.BoardSquare;

import static org.junit.Assert.assertEquals;
import minesweeper.BoardSquare.Square.boomException;

import org.junit.Test;

/**
 * 
 * Testing for an instance of the Square class, which is essentially a 1-by-1 mine field
 * 
 * Test Strategy:
 * 
 * A. Test 'dig' behavior for a square 1.without bomb   2. with bomb
 * B. Test for expected 'flag' and 'deflag' actions
 */

public class SquareTest {
    
	
	@Test
    public void bombTest1() throws boomException{
        Square singleSquare = new Square(false);
        singleSquare.dig();
        assertEquals(" ",singleSquare.toString());
	}
	
	
    @Test(expected=boomException.class)
    public void bombTest2() throws boomException{
        Square singleSquare = new Square(true);
        singleSquare.dig();
    }
    
      
    @Test
    public void FlagAndDeflag(){
        Square onesquare = new Square(false);
        onesquare.flag();
        assertEquals("F",onesquare.toString());
        onesquare.deflag();
        assertEquals("-",onesquare.toString());

    } 
}