package boardlogic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fieldlogic.Field;

public class BoardFieldsTest {
    private Field[] fields;

    @Before
    public void initBoardFields() {
        fields = BoardFields.getFields(20, 20);
    }

    @Test
    public void testGetFields() {
        assertEquals(69, fields.length);
    }

    @Test
    public void testConnectFields() {
        BoardFields.connectFields(fields);
        for (Field field: fields) {
            assertNotEquals(null, field.getNextField());
        }
    }
}