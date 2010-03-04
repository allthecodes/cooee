package org.karora.cooee.sandbox.informagen.app;

import java.util.EventListener;

import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.TextField;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.text.Document;


/**
 * A single-line text input field with pre keystroke regular expression testing
 */

abstract public class ActiveTextField extends TextField {


    public static final String INPUT_VALID = "inputValid";
    public static final String INPUT_INVALID = "inputInvalid";

    public static final String PROPERTY_MINVALUE = "minimumValue";
    public static final String PROPERTY_MAXVALUE = "maximumValue";
    public static final String PROPERTY_STATUSMSG = "message";
    
    public static final String PROPERTY_MSG_VISIBLE = "messageVisible";
    public static final String PROPERTY_MSG_ALWAYS_VISIBLE = "messageAlwaysVisible";
    public static final String PROPERTY_MSG_POSITION = "messagePosition";
    
    public static final String PROPERTY_ICON_VISIBLE = "iconVisible";
    public static final String PROPERTY_ICON_ALWAYS_VISIBLE = "iconAlwaysVisible";
    public static final String PROPERTY_ICON_POSITION = "iconPosition";

    public static final String PROPERTY_ERROR_FOREGROUND = "errorForeground";
    public static final String PROPERTY_ERROR_BACKGROUND = "errorBackground";

    public static final String PROPERTY_REQUIRED = "required";
    public static final String PROPERTY_REQUIREDMSG = "requiredMessage";

    public static final String PROPERTY_FOCUS_BORDER = "focusBorder";
    public static final String PROPERTY_TOP_FOCUS_BORDER = "topFocusBorder";
    public static final String PROPERTY_RIGHT_FOCUS_BORDER = "rightFocusBorder";
    public static final String PROPERTY_BOTTOM_FOCUS_BORDER = "bottomFocusBorder";
    public static final String PROPERTY_LEFT_FOCUS_BORDER = "leftFocusBorder";
    
    public static final String PROPERTY_FOCUS_FOREGROUND = "focusForeground";
    public static final String PROPERTY_FOCUS_BACKGROUND = "focusBackground";

	public static final String PROPERTY_ACTION_CAUSED_ON_CHANGE = "actionCausedOnChange";

    /**
     * Creates a new <code>ActiveTextField</code> with an empty 
     * <code>StringDocument</code> as its model, and default width
     * setting.
    */
    public ActiveTextField() {}

    
    /**
     * Creates a new <code>ActiveTextField</code> with the specified
     * <code>Document</code> model.
     * 
     * @param document the document
    */

    public ActiveTextField(Document document) {
        super(document);
    }

    
    public abstract boolean isValid();

    
    /**
     * Creates a new <code>ActiveTextField</code> with the specified
     * <code>Document</code> model, initial text, and column width.
     * 
     * @param document the document
     * @param text the initial text (may be null)
     * @param columns the number of columns to display
    */
     
    public ActiveTextField(Document document, String text, int columns) {
        super(document, text, columns);
    }

    /**
     * @see org.karora.cooee.app.Component#processInput(java.lang.String, java.lang.Object)
     */


/*
    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);


        if (!hasEventListenerList()) {
            return;
        }
        
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

*/

      
    /**
     * Sets the status message 
     * 
     * @param minValue the minimum value
    */
       
    public void setMessage(String statusMsg) {
        setProperty(PROPERTY_STATUSMSG, statusMsg);
    }


    /**
     * Gets the status, out of range, message
     * 
     * @returns String the status message, ie Valid values are...
    */
       
    public String getMessage() {
        return (String)getProperty(PROPERTY_STATUSMSG);
    }


    /**
     * Sets the visibility of the status message as a boolean.
     * 
     * @param isVisible the visibility
    */
       
    public void setMessageVisible(boolean isVisible) {
        setProperty(PROPERTY_MSG_VISIBLE, new Boolean(isVisible));
    }


    /**
     * Gets the visibility of the status message as a boolean.
     * 
     * @return the status message visibility
    */
       
    public boolean isMessageVisible() {
        Boolean object = (Boolean)getProperty(PROPERTY_MSG_VISIBLE);
        return (object != null) ? object.booleanValue() : true;
    }



    /**
     * Sets the status message to be always visible
     * 
     * @param minValue the minimum value
    */
       
    public void setMessageAlwaysVisible(boolean isAlwaysVisible) {
        setProperty(PROPERTY_MSG_ALWAYS_VISIBLE, new Boolean(isAlwaysVisible));
    }


    /**
     * Returns whether the status message is always be visible or not.
     * 
     * @return true if it is always visible or false if it is not.
    */
       
    public boolean isMessageAlwaysVisible() {
        Boolean object = (Boolean)getProperty(PROPERTY_MSG_ALWAYS_VISIBLE);
        return (object != null) ? object.booleanValue() : false;
    }


    /**
     * Sets the position of the message 
     * 
     * @param messagePosition the status message position
    */
       
    public void setMessagePosition(Alignment messagePosition) {
        setProperty(PROPERTY_MSG_POSITION, messagePosition);
    }


    /**
     * Sets the visibility of the status message
     * 
     * @param isVisible the visibility
    */
       
    public void setIconVisible(boolean isVisible) {
        setProperty(PROPERTY_ICON_VISIBLE, new Boolean(isVisible));
    }


    /**
     * Gets the visibility of the status message
     * 
     * @return the status message visibility as a boolean.
    */
       
    public boolean isIconVisible() {
        Boolean object = (Boolean)getProperty(PROPERTY_ICON_VISIBLE);
        return (object != null) ? object.booleanValue() : true;
    }



    /**
     * Sets the icon to be always visible
     * 
     * @param isAlwaysVisible boolean telling whether this ActiveTextField to be always visible.
    */
       
    public void setIconAlwaysVisible(boolean isAlwaysVisible) {
        setProperty(PROPERTY_ICON_ALWAYS_VISIBLE, new Boolean(isAlwaysVisible));
    }


    /**
     * Returns whether or not the Icon is always visible or not.
     * 
     * @return true if it is always visible or false if it is not.
    */
       
    public boolean isIconAlwaysVisible() {
        Boolean object = (Boolean)getProperty(PROPERTY_ICON_ALWAYS_VISIBLE);
        return (object != null) ? object.booleanValue() : false;
    }



    /**
     * Gets the icon position
     * 
     * @return the icon position relative to the input field as an <code>Alignment</code>.
    */
       
    public Alignment getMessagePosition() {
        return (Alignment)getProperty(PROPERTY_MSG_POSITION);
    }



    /**
     * Sets the position of the icon 
     * 
     * @param iconPosition the status icon position
    */
       
    public void setIconPosition(Alignment messagePosition) {
        setProperty(PROPERTY_ICON_POSITION, messagePosition);
    }


    /**
     * Gets the icon position
     * 
     * @return the icon position relative to the input field as a <code>Alignment</code>.
    */
       
    public Alignment getIconPosition() {
        return (Alignment)getProperty(PROPERTY_ICON_POSITION);
    }


    /**
     * Sets the foreground color when input is out of range
     * 
     * @param color the error foreground color.
    */
       
    public void setErrorForeground(Color color) {
        setProperty(PROPERTY_ERROR_FOREGROUND, color);
    }


    /**
     * Gets the foreground color when input is out of range
     * 
     * @return the error foreground color
    */
       
    public Color getErrorForeground() {
        return (Color)getProperty(PROPERTY_ERROR_FOREGROUND);
    }



    /**
     * Sets the background color when input is out of range
     * 
     * @param color the error background color.
    */
       
    public void setErrorBackground(Color color) {
        setProperty(PROPERTY_ERROR_BACKGROUND, color);
    }


    /**
     * Gets the background color when input is out of range
     * 
     * @return the error background color
    */
       
    public Color getErrorBackground() {
        return (Color)getProperty(PROPERTY_ERROR_BACKGROUND);
    }


    /**
    * Protected Method that determines whether a string is empty or null.
    * @return true if it is empty or false if it is not empty.
    */
    // Required and Required Message

    protected static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }
    
    /**
    * Sets whether this text field is required to be answered.
    * @param isRequired boolean set to determine whether this field is required.
    */
    public void setRequired(boolean isRequired) {
        setProperty(PROPERTY_REQUIRED, new Boolean(isRequired));
    }
    
    /**
    * Returns if this field is required to be answered.
    * @return true if it is required or false if it is not.
    */
    public boolean isRequired() {
        Boolean object = (Boolean)getProperty(PROPERTY_REQUIRED);
        return (object != null) ? object.booleanValue() : false;
    }

    /**
    * Sets the required message of this field.
    * @param requiredMsg
    */
    public void setRequiredMessage(String requiredMsg) {
        setProperty(PROPERTY_REQUIREDMSG, requiredMsg);
    }

    /**
    * Gets the required message of this field.
    * @return the required message.
    */
    public String getRequiredMessage() {
        return (String)getProperty(PROPERTY_REQUIREDMSG);
    }


    /*
     * Set the border to be used when this component comes into focus. Sets the
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
        setProperty(PROPERTY_FOCUS_BORDER, null);
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
        setProperty(PROPERTY_FOCUS_BORDER, null);
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
        setProperty(PROPERTY_FOCUS_BORDER, null);
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
     *   is brought into focus. NB: the error background color will 
     *   override this setting if error condition is set.  If
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

	/**
	 * @return true if any change to <code>ActiveTextField</code>'s content cause
	 *         the <code>ActionListener</code> to be invoked.
	 */
	public boolean getActionCausedOnChange() {
        Boolean object = (Boolean)getProperty(PROPERTY_ACTION_CAUSED_ON_CHANGE);
        return (object != null) ? object.booleanValue() : true;
	}

	/**
	 * When this flag is set to true, then when the <code>TextFieldEx</code>'s
	 * content changes, then any attached action listener will be informed
	 * immediately. Use this flag with caution as it can degrade the users
	 * experience and UI responsiveness.
	 * <p>
	 * Note also you CANNOT tell the difference between onchange events and
	 * pressing enter within the field. Microsoft documentation defines onchange
	 * for text fields as:
	 * 
	 * <pre>
	 *      &quot;...[the] event is fired when the contents are committed and not while the value is 
	 *       changing. [...] this event is notfired while the user is typing, but rather when the user commits the change by 
	 *       leaving the text box that has focus.&quot;
	 * </pre>
	 * 
	 * @param newValue -
	 *            true if any change to <code>TextFieldEx</code>'s content
	 *            cause the <code>ActionListener</code> to be invoked.
	 */

	public void setActionCausedOnChange(boolean newValue) {
        setProperty(PROPERTY_ACTION_CAUSED_ON_CHANGE, new Boolean(newValue));
	}

}

