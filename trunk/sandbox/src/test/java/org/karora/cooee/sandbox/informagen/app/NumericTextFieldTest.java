package org.karora.cooee.sandbox.informagen.app;

import junit.framework.TestCase;

public class NumericTextFieldTest extends TestCase{
    
    NumericTextField field;
    public void setUp(){
        field = new NumericTextField();
    }
    public void testConstants(){
        assertEquals("minimumValue", NumericTextField.PROPERTY_MINVALUE);
        assertEquals("maximumValue", NumericTextField.PROPERTY_MAXVALUE);
    }
    public void testGetsAndSets(){
        field.setValue(13);
        field.setMinValue(5);
        field.setMaxValue(20);

        assertEquals(13.0, field.getValue());
        assertEquals(5.0, field.getMinValue());
        assertEquals(20.0, field.getMaxValue());
    }
    public void testIsValid(){
        field.setMinValue(10);
        field.setMaxValue(20);
        field.setValue(15);
        
        assertTrue(field.isValid());
        
        field.setValue(5);
        assertFalse(field.isValid());
        
        field.setValue(100);
        assertFalse(field.isValid());
        
        field.setValue(-2);
        assertFalse(field.isValid());
    }
}