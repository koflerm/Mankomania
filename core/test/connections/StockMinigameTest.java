package connections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mankomania.game.connections.StockMinigame;

import org.junit.Before;
import org.junit.Test;


public class StockMinigameTest {

    private StockMinigame stock;

    @Before
    public void init() {
        stock = new StockMinigame("TestStock", true);
    }

    @Test
    public void InitStockMinigameTest() {
        assertNotNull(stock);
    }

    @Test
    public void StockMinigameGETSET() {
        stock.setStock("Testing2");
        stock.setBlack(false);

        assertEquals("Testing2", stock.getStock());
        assertEquals(false, stock.isBlack());
    }
}
