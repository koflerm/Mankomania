import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import fieldLogic.Field;
import fieldLogic.FieldColor;
import playerLogic.Player;

public class FieldTest {
    public Field optional = null;
    public Field previous = null;
    public Field next = null;
    public Field field = new Field(0,"testField", FieldColor.WHITE);
    public Player p1 = new Player(field,0);
    public Player p2 = new Player(next,1);
    @Test
    public void runner(){
        field.setPreviousField(previous);
        field.setNextField(next);
        field.setOptionalNextField(optional);
    }
    @Test
    public void hasIntersection_returnFalse(){
        assertFalse(field.isIntersection());
    }
    @Test
    public void fieldsTest_returnTrue(){
        assertNull(field.getNextField());
        assertNull(field.getPreviousField());
        assertNull(field.getOptionalNextField());
    }
    @Test
    public void descriptionTest_returnTrue(){
        assertEquals("testField",field.getFieldDescription());
    }
    @Test
    public void colorTest_returnTrue(){
        assertEquals(FieldColor.WHITE,field.getColor());
    }
    @Test
    public void twoPlayersOnFieldTest_returnTrue(){
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        assertTrue(field.hasPlayer(players));
    }
}
