import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import fieldLogic.Field;
import fieldLogic.FieldColor;

public class FieldTest {

    public Field field = new Field(0, null, null,null,"testField", FieldColor.WHITE);

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
}
