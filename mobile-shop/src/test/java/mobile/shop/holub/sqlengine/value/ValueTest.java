package mobile.shop.holub.sqlengine.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import org.junit.jupiter.api.Test;

public class ValueTest {


    String dummyTableName = "dummy table name";
    String dummyColumnName = "dummy column name";

    @Test
    void testGetBooleanValue() {
        assertTrue(ValueFactory.getBooleanValue(true).getBooleanValue());

        assertThrows(UnsupportedOperationException.class, () -> {
            ValueFactory.getNumericValue(3).getBooleanValue();
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            ValueFactory.getNullValue().getBooleanValue();
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            ValueFactory.getStringValue("3").getBooleanValue();
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            ValueFactory.getIdValue(dummyTableName, dummyColumnName).getBooleanValue();
        });
    }

    @Test
    void testGetNumericValue() throws ParseException {
        assertEquals(3, ValueFactory.getNumericValue(3).getNumericValue());
        assertEquals(3, ValueFactory.getNumericValue("3").getNumericValue());

        assertThrows(UnsupportedOperationException.class, () -> {
            ValueFactory.getStringValue("3").getNumericValue();
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            ValueFactory.getBooleanValue(false).getNumericValue();
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            ValueFactory.getNullValue().getNumericValue();
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            ValueFactory.getIdValue(dummyTableName, dummyColumnName).getNumericValue();
        });
    }


    @Test
    void testGetStringValue() {
        assertEquals("design pattern", ValueFactory.getStringValue("design pattern").getStringValue());

        assertThrows(UnsupportedOperationException.class, () -> {
            ValueFactory.getNumericValue("3").getStringValue();
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            ValueFactory.getBooleanValue(false).getStringValue();
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            ValueFactory.getNullValue().getStringValue();
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            ValueFactory.getIdValue(dummyTableName, dummyColumnName).getStringValue();
        });
    }


}
