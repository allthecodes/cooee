package org.karora.cooee.sandbox.informagen.app;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * A single-line text input field with pre keystroke regular expression testing.
 */

public class RegExTextField extends ActiveTextField {

    public static final String PROPERTY_REGEXP = "regex";
    public static final String PROPERTY_REGEXP_FILTER = "regexFilter";

    /**
    * Tests whether this RegExTextField is valid based upon whether its reguler expression 
    * can be compiled.
    * @return True if it is valid and false if it is invalid.
    */
    public boolean isValid() {
    
        String text = getText();
        String regex = getRegEx();
        
        if( isEmpty(text) || isEmpty(regex) )
            return false;
    
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        
        // Must use 'find' and not 'matches' so that the Java validation
        //   behavior is the same as the JavaScript valication behavior
        //   This change fixes issue SBX-30
        
        return m.find();
    }

    
    /**
     * Set the regular expression which will filter each keystroke.
     * 
     * @param regex the regular expression to be set.
    */
       
    public void setRegExFilter(String regex) {
        setProperty(PROPERTY_REGEXP_FILTER, regex);
    }


    /**
     * Get the regular expression for input into this text field
     * 
     * @return the regular expression set to this RegExTextField.
    */
       
    public String getRegExFilter() {
        return (String)getProperty(PROPERTY_REGEXP_FILTER);
    }



    /**
     * Set the regular expression which will be checked after each keystroke
     * 
     * @param regex the regular expression
    */
       
    public void setRegEx(String regex) {
        setProperty(PROPERTY_REGEXP, regex);
    }


    /**
     * Get the regular expression for this text field
     * 
     * @returns regex the regular expression
    */
       
    public String getRegEx() {
        return (String)getProperty(PROPERTY_REGEXP);
    }



}

