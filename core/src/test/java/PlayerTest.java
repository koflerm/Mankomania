import org.junit.Test;

import static org.junit.Assert.assertEquals;


import fieldLogic.Field;
import fieldLogic.FieldColor;
import playerLogic.Player;

public class PlayerTest {
    public Field fieldP1 = new Field(0,"P1 Field", FieldColor.RED);
    public Field fieldP2 = new Field(0,"P2 Field", FieldColor.RED);
    public Player p1 = new Player(fieldP1,0);
    public Player p2 = new Player(fieldP2,1);
    public int amount = 50000;

    @Test
    public void currentPositionTest_returnTrue(){
        assertEquals(fieldP1,p1.getCurrentPosition());
        assertEquals(fieldP2,p2.getCurrentPosition());
    }
    @Test
    public void paymentTest_returnTrue(){
        p1.payToPlayer(p2,amount);
        assertEquals(p1.getMoney(),p1.getMoney());
        assertEquals(p2.getMoney(),p2.getMoney());
    }
    @Test
    public void add_lose_MoneyTest_returnTrue(){
        p1.addMoney(amount);
        assertEquals(p1.getMoney(),p1.getMoney());
        p1.loseMoney(amount);
        assertEquals(p1.getMoney(),p1.getMoney());
    }
    @Test
    public void setMoneyTest_returnTrue(){
        p1.setMoney(amount);
        assertEquals(amount, p1.getMoney());
    }
    @Test
    public void setFieldTest_returnTrue(){
        p1.setCurrentFieldPosition(fieldP2);
        assertEquals(fieldP2,p1.getCurrentPosition());
    }
    @Test
    public void getPlayerIndexTest_returnTrue(){
        assertEquals(0,p1.getPlayerIndex());
        assertEquals(1,p2.getPlayerIndex());
        p1.setPlayerIndex(3);
        p2.setPlayerIndex(4);
        assertEquals(3,p1.getPlayerIndex());
        assertEquals(4,p2.getPlayerIndex());
    }
}
