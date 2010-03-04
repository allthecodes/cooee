package org.karora.cooee.sandbox.informagen.app;



/**
 * A single-line text input field with pre keystroke regular expression testing
 */

public class NumericTextField extends ActiveTextField {

    public static final String PROPERTY_MINVALUE = "minimumValue";
    public static final String PROPERTY_MAXVALUE = "maximumValue";

    public boolean isValid() {
        
        double value = getValue();
        double min = getMinValue();
        double max = getMaxValue();
    
        return (min <= value) && (value <= max);
    }


    /**
     * Sets the value as a double.
     * 
     * @param value the value to be set as a double.
    */
       
    public void setValue(double value) {
        setText(Double.toString(value));
    }


    /**
     * Get the value as a double.
     * 
     * @return the value as a double.
    */
       
    public double getValue() {
        double value;
        try {
            value = Double.parseDouble(getText());
        } catch (NumberFormatException nfe) {
            value = 0;
        }
        
        return value;
    }

    
    /**
     * Sets the maximum allowed value for this NumericTextField.
     * 
     * @param maxValue the maximum value to be set.
    */
       
    public void setMaxValue(double maxValue) {
        setProperty(PROPERTY_MAXVALUE, new Double(maxValue));
    }


    /**
     * Gets the maximum value for this NumericTextField.
     * 
     * @return the maximum value as an int.
    */
       
    public double getMaxValue() {
        Double object = (Double)getProperty(PROPERTY_MAXVALUE);
        return (object != null) ? object.doubleValue() : Double.MAX_VALUE;
    }

    
    /**
     * Sets the minimum allowed value for this NumericTextField as a double. 
     * 
     * @param minValue the minimum value to be set.
    */
       
    public void setMinValue(double minValue) {
        setProperty(PROPERTY_MINVALUE, new Double(minValue));
    }


    /**
     * Gets the minimum value for this NumericTextField as a double.
     * 
     * @return the minimum value as a double
    */
       
    public double getMinValue() {
        Double object = (Double)getProperty(PROPERTY_MINVALUE);
        return (object != null) ? object.doubleValue() : -Double.MAX_VALUE;
    }

}

