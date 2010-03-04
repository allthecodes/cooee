package org.karora.cooee.sandbox.informagen.app;

import java.util.EventListener;

import org.karora.cooee.app.Color;
import org.karora.cooee.app.Border;
import org.karora.cooee.app.TextArea;
import org.karora.cooee.app.text.Document;

import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;

/**
 * A single-line text input field with pre keystroke regular expression testing
 */

public class ActiveTextArea extends TextArea {

    // Focus border and background behavior 
    public static final String PROPERTY_FOCUS_BORDER = "focus-border";
    
    public static final String PROPERTY_TOP_FOCUS_BORDER = "top-focus-border";
    public static final String PROPERTY_RIGHT_FOCUS_BORDER = "right-focus-border";
    public static final String PROPERTY_BOTTOM_FOCUS_BORDER = "bottom-focus-border";
    public static final String PROPERTY_LEFT_FOCUS_BORDER = "left-focus-border";
    
    public static final String PROPERTY_FOCUS_FOREGROUND = "focus-foreground";
    public static final String PROPERTY_FOCUS_BACKGROUND = "focus-background";


    public static final String ACTION_VALIDATION_TRANSACTION = "validation-transition";
    public static final String INPUT_VALID = "inputValid";
    public static final String INPUT_INVALID = "inputInvalid";


    public static final String PROPERTY_MAXIMUM_LENGTH = "maxLength";
    public static final String PROPERTY_MSG_VISIBLE = "messageVisible";

    /**
     * Creates a new <code>ActiveTextArea</code> with an empty 
     * <code>StringDocument</code> as its model, and default width
     * setting.
    */
    public ActiveTextArea() {}

    
    /**
     * Creates a new <code>ActiveTextArea</code> with the specified
     * <code>Document</code> model.
     * 
     * @param document the document
    */

    public ActiveTextArea(Document document) {
        super(document);
    }

    
    /**
     * Creates a new <code>ActiveTextArea</code> with the specified
     * <code>Document</code> model, initial text, and column width.
     * 
     * @param document the document
     * @param text the initial text (may be null)
     * @param columns the number of columns to display
    */
     
    public ActiveTextArea(Document document, String text, int columns, int rows) {
        super(document, text, columns, rows);
    }

    /**
     * @see org.karora.cooee.app.Component#processInput(java.lang.String, java.lang.Object)
     */

    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);


        if (!hasEventListenerList()) {
            return;
        }

        if(inputName.equals(ACTION_VALIDATION_TRANSACTION))
            processValidationTransition(inputValue);
    }


    private void processValidationTransition(Object inputValue) {
    
        EventListener[] listeners = getEventListenerList().getListeners(ActionListener.class);
 
        ActionEvent e = null;
        
        boolean valid = Boolean.valueOf((String)inputValue).booleanValue();
        
        
        for (int i = 0; i < listeners.length; ++i) {
            // Lazy instanciation
            if (e == null) 
                e = new ActionEvent(this, (valid) ? INPUT_VALID : INPUT_INVALID);
            
            ((ActionListener) listeners[i]).actionPerformed(e);
        }
    
    }


    
    /**
     * Sets the maximum length for this text area.
     * 
     * @param maxLength the maximum length set to this text area.
    */
       
    public void setMaxLength(int maxLength) {
        setProperty(PROPERTY_MAXIMUM_LENGTH, new Integer(maxLength));
    }


    /**
     * Gets maximum length for this text area.
     * 
     * @return the maximum length as an int
    */
       
    public int getMaxLength() {
        Integer value = (Integer)getProperty(PROPERTY_MAXIMUM_LENGTH);
        return (value != null) ? value.intValue() : -1;
    }


    /**
     * Sets the status message to be always visible
     * 
     * @param isVisible boolean value set to determine
    */
       
    public void setMessageVisible(boolean isVisible) {
        setProperty(PROPERTY_MSG_VISIBLE, new Boolean(isVisible));
    }


    /**
     * Returns whether the message is visible or not.
     * 
     * @returns true if it is visible or false if it is not.
    */
       
    public boolean isMessageVisible() {
        Boolean object = (Boolean)getProperty(PROPERTY_MSG_VISIBLE);
        return (object != null) ? object.booleanValue() : true;
    }


    /*
     * Set the border to be used when this component comes into focus. Set the
     *   individual border properties to null.
     * NB: Different browser use different border widths as default.  To prevent
     *   the text component from "jumping" around when focused, set the normal 
     *   border and the focus border to the same size.
     *
     * @param border 
     */

    public void setFocusBorder(Border border) {
        setProperty(PROPERTY_FOCUS_BORDER, border);
        setProperty(PROPERTY_TOP_FOCUS_BORDER, null);
        setProperty(PROPERTY_RIGHT_FOCUS_BORDER, null);
        setProperty(PROPERTY_BOTTOM_FOCUS_BORDER, null);
        setProperty(PROPERTY_LEFT_FOCUS_BORDER, null);
    }

    /**
     * Returns the current focus border
     * 
     * @returns null if not set
    */

    public Border getFocusBorder() {
        return (Border)getProperty(PROPERTY_FOCUS_BORDER);
    }

    /*
     * Convenience method to specify four seperate borders
     * 
     * @param topBorder 
     * @param rightBorder 
     * @param bottomBorder 
     * @param leftBorder 
     */

    public void setFocusBorder(Border topBorder, 
                               Border rightBorder, 
                               Border bottomBorder, 
                               Border leftBorder) {
        setProperty(PROPERTY_TOP_FOCUS_BORDER, topBorder);
        setProperty(PROPERTY_RIGHT_FOCUS_BORDER, rightBorder);
        setProperty(PROPERTY_BOTTOM_FOCUS_BORDER, bottomBorder);
        setProperty(PROPERTY_LEFT_FOCUS_BORDER, leftBorder);
    }

    /*
     * Convenience method to specify a shadowed borders where the
     *   top/left border is ususaly darker than the right/bottom border
     *  Used with setRightBottomFocusBorder
     * 
     * @param border 
     */


    public void setTopLeftFocusBorder(Border border) {
        setProperty(PROPERTY_TOP_FOCUS_BORDER, border);
        setProperty(PROPERTY_LEFT_FOCUS_BORDER, border);
    }

    /*
     * Convenience method to specify a shadowed borders where the
     *   top/left border is ususaly darker than the right/bottom border
     *  Used with setTopLeftFocusBorder
     * 
     * @param border 
     */

    public void setRightBottomFocusBorder(Border border) {
        setProperty(PROPERTY_RIGHT_FOCUS_BORDER, border);
        setProperty(PROPERTY_BOTTOM_FOCUS_BORDER, border);
    }

    // Top border
    public void setTopFocusBorder(Border border) {
        setProperty(PROPERTY_TOP_FOCUS_BORDER, border);
    }
    
    public Border getTopFocusBorder() {
        return (Border) getProperty(PROPERTY_TOP_FOCUS_BORDER);
    }

    // Right border
    public void setRightFocusBorder(Border border) {
        setProperty(PROPERTY_RIGHT_FOCUS_BORDER, border);
    }
    
    public Border getRightFocusBorder() {
        return (Border) getProperty(PROPERTY_RIGHT_FOCUS_BORDER);
    }

    // Bottom border
    public void setBottomFocusBorder(Border border) {
        setProperty(PROPERTY_BOTTOM_FOCUS_BORDER, border);
    }
    
    public Border getBottomFocusBorder() {
        return (Border) getProperty(PROPERTY_BOTTOM_FOCUS_BORDER);
    }

    // Left border
    public void setLeftFocusBorder(Border border) {
        setProperty(PROPERTY_LEFT_FOCUS_BORDER, border);
    }
    
    public Border getLeftFocusBorder() {
        return (Border) getProperty(PROPERTY_LEFT_FOCUS_BORDER);
    }
    



    /*
     *  When set, this color will be used as the background when this component
     *   is brought into focus. NB: the overdrawn background color will 
     *   override this setting if too many characters are entered.  IF
     *   this value is 'null' then the component background will be used.
     * 
     * @param color 
     */


    public void setFocusBackground(Color color) {
        setProperty(PROPERTY_FOCUS_BACKGROUND, color);
    }

    /**
     * Returns the current focus background color
     * 
     * @returns null if not set
    */

    public Color getFocusBackground() {
        return (Color)getProperty(PROPERTY_FOCUS_BACKGROUND);
    }



    public void setFocusForeground(Color color) {
        setProperty(PROPERTY_FOCUS_FOREGROUND, color);
    }

    /**
     * Returns the current focus background color
     * 
     * @returns null if not set
    */

    public Color getFocusForeground() {
        return (Color)getProperty(PROPERTY_FOCUS_FOREGROUND);
    }

}

