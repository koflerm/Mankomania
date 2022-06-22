package connections;

import static org.junit.Assert.assertNotNull;

import com.mankomania.game.connections.ConStock;

import org.junit.Before;
import org.junit.Test;

public class ConStockTest {

    private ConStock stock;

    @Before
    public void init() {
        stock = new ConStock(3,2,1);
    }

    @Test
    public void InitConStockTest() {
        assertNotNull(stock);
    }

}
