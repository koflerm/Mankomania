package connections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import com.mankomania.game.connections.ConAuction;

import org.junit.Before;
import org.junit.Test;

public class ConAuctionTest {

    private ConAuction a;

    private float itemPrice = 125;
    private float multiplicator = 2;
    private float moneyFromBank = 100;
    private int difference = 200;
    private int moneyToSet = 800;


    @Before
    public void init() {
        a = new ConAuction(itemPrice, multiplicator, moneyFromBank, difference, moneyToSet);
    }

    @Test
    public void InitConAuctionTest() {
        assertNotNull(a);
    }

    @Test
    public void ConAuctionMethodTest() {
        a.setItemprice(1.1f);
        a.setMultiplicator(2.3f);
        a.setMoneyFromBank(500);
        a.setDifference(1200);
        a.setMoneyToSet(300);

        assertEquals(1.1f, a.getItemprice(), 0.0001);
        assertEquals(2.3f, a.getMultiplicator(), 0.0001);
        assertEquals(500, a.getMoneyFromBank(), 0.0001);
        assertEquals(1200, a.getDifference());
        assertEquals(300, a.getMoneyToSet());
    }

}
