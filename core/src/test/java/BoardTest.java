import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import boardLogic.Board;

public class BoardTest {
    Board b1 = new Board();
    @Test
    public void runner(){

        currentPlayerTest_returnTrue();
        currentPlayerTest_returnFalse();
        setNextPlayerTest();
        nextPlayerTest_returnTrue();
        nextPlayerTest_returnFalse();
        setPlayerTest_returnTrue();
        setPlayerTest_returnFalse();
    }


    @Test
    public void currentPlayerTest_returnTrue(){
        assertEquals(0, (b1.getCurrentPlayerIndex()));
    }
    @Test
    public void currentPlayerTest_returnFalse(){
        assertNotEquals(1, (b1.getCurrentPlayerIndex()));
    }
    @Test
    public void setNextPlayerTest(){
        b1.setCurrentPlayerIndex(1);
    }
    @Test
    public void nextPlayerTest_returnTrue(){
        assertEquals(1, (b1.getCurrentPlayerIndex()));
    }
    @Test
    public void nextPlayerTest_returnFalse(){
        assertNotEquals(0, (b1.getCurrentPlayerIndex()));
    }
    @Test
    public void setPlayerTest_returnTrue(){
        b1.setCurrentPlayerIndex(0);
        assertEquals(0, (b1.getCurrentPlayerIndex()));
    }
    @Test
    public void setPlayerTest_returnFalse(){
        b1.setCurrentPlayerIndex(0);
        assertNotEquals(1, (b1.getCurrentPlayerIndex()));
    }
}
