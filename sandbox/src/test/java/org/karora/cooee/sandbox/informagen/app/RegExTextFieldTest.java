package org.karora.cooee.sandbox.informagen.app;


// Unit Testing Framework
import junit.framework.TestCase;


/**
 * Unit tests for AccordionPane
 */

public class RegExTextFieldTest extends TestCase {

    static final String SSN_CHARS = "[0-9-]";
    static final String SSN = "^[0-9]{3}\\-[0-9]{2}\\-[0-9]{4}$";
    static final String SIX_DIGITS = "^[+-]{0,1}[0-9]{1,6}$";
    static final String ODD_DIGITS = "^[13579]*$";

   
    RegExTextField textField;
    
    public void setUp(){
        textField = new RegExTextField();
    }


    public void testDefaultConstructor() {
    
        // Class defaults
        assertNull(textField.getRegEx());
        assertNull(textField.getRegExFilter());
        assertFalse(textField.isValid());
    }

    public void testProperties() {
        
        textField.setRegEx(SSN);
        textField.setRegExFilter(SSN_CHARS);

        assertEquals(SSN, textField.getRegEx());
        assertEquals(SSN_CHARS, textField.getRegExFilter());
         
    }

    public void testIsValid() {
        textField.setText("");
        assertFalse(textField.isValid());
       
        textField.setText("    ");
        assertFalse(textField.isValid());
    }

    public void testIsValid_SSN() {    
        textField.setRegEx(SSN);
        textField.setRegExFilter(SSN_CHARS);
        textField.setText("011-01-0111");
        
        assertTrue(textField.isValid());
    }

    public void testIsValid_6digits() {
    
        textField.setRegEx(SIX_DIGITS);
        
        textField.setText("-");
        assertFalse(textField.isValid());
        
        textField.setText("1");
        assertTrue(textField.isValid());
        
        textField.setText("12");
        assertTrue(textField.isValid());
        
        textField.setText("123");
        assertTrue(textField.isValid());
        
        textField.setText("1234");
        assertTrue(textField.isValid());
        
        textField.setText("123456");
        assertTrue(textField.isValid());
        
        textField.setText("+123456");
        assertTrue(textField.isValid());
        
        textField.setText("-123456");
        assertTrue(textField.isValid());
        
        textField.setText("1234567");
        assertFalse(textField.isValid());
        
    }
    
    public void testIsValid_OddDigits() {
    
        textField.setRegEx(ODD_DIGITS);
        
        textField.setText("1");
        assertTrue(textField.isValid());
        
        textField.setText("12");
        assertFalse(textField.isValid());
        
        textField.setText("13");
        assertTrue(textField.isValid());
        
        textField.setText("134");
        assertFalse(textField.isValid());
        
        textField.setText("135");
        assertTrue(textField.isValid());
        
        textField.setText("13579");
        assertTrue(textField.isValid());
        
        textField.setText("A13579");
        assertFalse(textField.isValid());
        
    }



    public void testSBX30() {
    
        textField.setRegEx("mark");
        
        textField.setText("mark");
        assertTrue(textField.isValid());
        
        textField.setText("marktest");
        assertTrue(textField.isValid());
        
        textField.setText("testmark");
        assertTrue(textField.isValid());
        
        textField.setText("testtesttestmarktesttesttest");
        assertTrue(textField.isValid());
        
        textField.setText("mar k");
        assertFalse(textField.isValid());
    }

}
