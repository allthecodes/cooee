package org.karora.cooee.sandbox.informagen.app;

import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;

/** 
 * <code>HorizontalRule</code> is a very simple component which implements the
 *  HTML 'hr' tag
 */

public class HorizontalRule extends Component {

    public static final String PROPERTY_BORDER = "border";
    public static final String PROPERTY_DISABLED_BORDER = "disabledBorder";

    public static final String PROPERTY_DISABLED_BACKGROUND = "disabledBackground";

    public static final String PROPERTY_HEIGHT = "height";
    public static final String PROPERTY_WIDTH = "width";

    
    /**
     * Returns the border of the text component.
     * 
     * @return the border
     */
    public Border getBorder() {
        return (Border) getProperty(PROPERTY_BORDER);
    }
    

    /**
     * Returns the background color displayed when the text component is 
     * disabled.
     * 
     * @return the color
     */
    public Color getDisabledBackground() {
        return (Color) getProperty(PROPERTY_DISABLED_BACKGROUND);
    }

    /**
     * Returns the border displayed when the text component is 
     * disabled.
     * 
     * @return the border
     */
    public Border getDisabledBorder() {
        return (Border) getProperty(PROPERTY_DISABLED_BORDER);
    }


    /**
     * Returns the height of the text component.
     * This property only supports <code>Extent</code>s with
     * fixed (i.e., not percent) units.
     * 
     * @return the height
     */
    public Extent getHeight() {
        return (Extent) getProperty(PROPERTY_HEIGHT);
    }
    
    /**
     * Returns the width of the text component.
     * This property supports <code>Extent</code>s with
     * either fixed or percentage-based units.
     * 
     * @return the width
     */
    public Extent getWidth() {
        return (Extent) getProperty(PROPERTY_WIDTH);
    }


    
    /**
     * Sets the border of the text component.
     * 
     * @param newValue the new border
     */
    public void setBorder(Border newValue) {
        setProperty(PROPERTY_BORDER, newValue);
    }

    
    /**
     * Sets the background color displayed when the component is disabled.
     * 
     * @param newValue the new <code>Color</code>
     */
    public void setDisabledBackground(Color newValue) {
        setProperty(PROPERTY_DISABLED_BACKGROUND, newValue);
    }


    /**
     * Sets the border displayed when the component is disabled.
     * 
     * @param newValue the new border
     */
    public void setDisabledBorder(Border newValue) {
        setProperty(PROPERTY_DISABLED_BORDER, newValue);
    }


    /**
     * Sets the height of the text component.
     * This property only supports <code>Extent</code>s with
     * fixed (i.e., not percent) units.
     * 
     * @param newValue the new height
     */
    public void setHeight(Extent newValue) {
        setProperty(PROPERTY_HEIGHT, newValue);
    }
        
    /**
     * Sets the width of the text component.
     * This property supports <code>Extent</code>s with
     * either fixed or percentage-based units.
     * 
     * @param newValue the new width
     */
    public void setWidth(Extent newValue) {
        setProperty(PROPERTY_WIDTH, newValue);
    }


}