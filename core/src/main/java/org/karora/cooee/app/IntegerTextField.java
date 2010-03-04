package org.karora.cooee.app;


/**
 * A single-line text input field with pre keystroke regular expression testing
 */

public class IntegerTextField extends ActiveTextField {

    public static final String PROPERTY_MINVALUE = "minimumValue";
    public static final String PROPERTY_MAXVALUE = "maximumValue";

    public boolean isValid() {
        
        int value = getValue();
        int min = getMinValue();
        int max = getMaxValue();
    
        return (min <= value) && (value <= max);
    }


    /**
     * Set the integer value as an int
     * 
     * @param value the int value
    */
       
    public void setValue(int value) {
        setText(Integer.toString(value));
    }


    /**
     * Get the value as an int
     * 
     * @return the int value. It returns 0 if the value that had been set invokes a 
     * NumberFormatException.
    */
       
    public int getValue() {
        int value;
        try {
            value = Integer.parseInt(getText());
        } catch (NumberFormatException nfe) {
            value = 0;
        }
        
        return value;
    }

    
    /**
     * Sets the maximum allowed value.
     * 
     * @param maxValue the maximum value to be set.
    */
       
    public void setMaxValue(int maxValue) {
        setProperty(PROPERTY_MAXVALUE, new Integer(maxValue));
    }


    /**
     * Gets the maximum value for this integer field
     * 
     * @return the maximum value as an int.
    */
       
    public int getMaxValue() {
        Integer object = (Integer)getProperty(PROPERTY_MAXVALUE);
        return (object != null) ? object.intValue() : Integer.MAX_VALUE;
    }

    
    /**
     * Set the minimum allowed value. 
     * 
     * @param minValue the minimum value
    */
       
    public void setMinValue(int minValue) {
        setProperty(PROPERTY_MINVALUE, new Integer(minValue));
    }


    /**
     * Get the minimum value for this integer field
     * 
     * @return int the minimum value
    */
       
    public int getMinValue() {
        Integer object = (Integer)getProperty(PROPERTY_MINVALUE);
        return (object != null) ? object.intValue() : Integer.MIN_VALUE;
    }

}

