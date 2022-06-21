package fieldlogic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FieldTest {

    private Field field;
    private Field secondField;
    private Field thirdField;
    private Field fourthField;

    @Before
    public void init() {
        field = new Field(1, "Test Field", 0, 0);
        secondField = new Field(2, "Test Field 2", 1, 1);
        thirdField = new Field(3, "Test Field 3", 2, 2);
        fourthField = new Field(4, "Test Field 4", 3, 3);
        field.setNextField(secondField);
        field.setOptionalNextField(thirdField);
        field.setPreviousField(fourthField);
    }

    @Test
    public void isIntersection() {
        assertEquals(true, field.isIntersection());
    }

    @Test
    public void getFieldIndex() {
        int fieldIndex = field.getFieldIndex();

        assertEquals(1, fieldIndex);
    }

    @Test
    public void getFieldDescription() {
        String description = field.getFieldDescription();

        assertEquals("Test Field", description);
    }

    @Test
    public void getNextField() {
        Field nextField = field.getNextField();

        assertEquals(secondField, nextField);
    }

    @Test
    public void getOptionalNextField() {
        Field nextField = field.getOptionalNextField();

        assertEquals(thirdField, nextField);
    }

    @Test
    public void getPreviousField() {
        Field prevField = field.getPreviousField();

        assertEquals(fourthField, prevField);
    }

    @Test
    public void getX() {
        float xValue = field.getX();

        assertEquals(0f, xValue, 0);
    }

    @Test
    public void getY() {
        float yValue = field.getY();

        assertEquals(0f, yValue, 0);
    }
}