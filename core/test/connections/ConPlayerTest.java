package connections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mankomania.game.connections.ConPlayer;

import org.junit.Before;
import org.junit.Test;

public class ConPlayerTest {

    private ConPlayer p;

    @Before
    public void init() {
        p = new ConPlayer();
    }

    @Test
    public void InitConPlayerTest() {
        assertNotNull(p);
    }

    @Test
    public void ConPlayerMethodsTest() {
        p.setDice1(1);
        p.setDice2(2);
        p.setPlayerIndex(4);
        p.setDiceCount(1+2);
        p.setMoney(100);
        p.setPosition(5);
        p.setSocket("randomSocketID");
        p.setYouTurn(true);
        p.setDryOilPlc(1);
        p.setHardSteelPlc(1);
        p.setShortCircuitPlc(0);

        assertEquals(4, p.getPlayerIndex());
        assertEquals(100, p.getMoney());
        assertEquals(5, p.getPosition());
        assertEquals("randomSocketID", p.getSocket());
        assertEquals(true, p.isYouTurn());
        assertEquals(1, p.getDryOilPlc());
        assertEquals(1, p.getHardSteelPlc());
        assertEquals(0, p.getShortCircuitPlc());

    }




}
