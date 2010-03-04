package org.karora.cooee.sandbox.informagen.app;

import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;

import junit.framework.TestCase;

public class IntegerTextFieldTest extends TestCase{

    IntegerTextField field;
    
    public void setUp(){
        field = new IntegerTextField();
    }
    
    public void testConstants(){
        assertEquals("minimumValue", IntegerTextField.PROPERTY_MINVALUE);
        assertEquals("maximumValue", IntegerTextField.PROPERTY_MAXVALUE);
    }
    
    public void testGetsAndSets(){
        field.setValue(10);
        assertEquals(10, field.getValue());
        
        field.setMaxValue(30);
        assertEquals(30, field.getMaxValue());
        
        field.setMinValue(15);
        assertEquals(15, field.getMinValue());
    }
    
    public void testIsValid(){
        field.setMinValue(10);
        field.setMaxValue(15);
        field.setValue(13);
        assertTrue(field.isValid());
        
        field.setValue(20);
        assertFalse(field.isValid());
        
        field.setValue(0);
        assertFalse(field.isValid());
        
        field.setValue(-10);
        assertFalse(field.isValid());
    }

    public void testFocusBorder() {
    
        Border border   = new Border(2, Color.LIGHTGRAY, Border.STYLE_INSET);
        Border tlBorder = new Border(5, Color.RED, Border.STYLE_GROOVE);
        Border rbBorder = new Border(3, Color.GREEN, Border.STYLE_OUTSET);

        field.setTopLeftFocusBorder(tlBorder);
        field.setRightBottomFocusBorder(rbBorder);
 
        assertNotNull(field.getTopFocusBorder());
        assertNotNull(field.getRightFocusBorder());
        assertNotNull(field.getBottomFocusBorder());
        assertNotNull(field.getLeftFocusBorder());
 
        // Setting this property should in turn set individual
        //  borders to null
        
        field.setFocusBorder(border);
        
        assertEquals(2, field.getFocusBorder().getSize().getValue());
        assertNull(field.getTopFocusBorder());
        assertNull(field.getRightFocusBorder());
        assertNull(field.getBottomFocusBorder());
        assertNull(field.getLeftFocusBorder());
    
    }


}