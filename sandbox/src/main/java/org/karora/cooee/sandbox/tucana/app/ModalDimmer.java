/* 
 * This file is part of the Tucana Echo2 Library.
 * Copyright (C) 2006.
 *
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 */

package org.karora.cooee.sandbox.tucana.app;

import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.FloatingPane;
import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.ResourceImageReference;

/**
 * The ModalDimmer component a non-standard component intended to be added to an
 * application only once.  Once added, it will dim everything under to modal
 * component (usually a windowpane).
 * 
 * @author Jeremy Volkman
 *
 */
public class ModalDimmer extends Component implements FloatingPane {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The dimming type
     * 
     * @see #DIM_TYPE_OPACITY
     * @see #DIM_TYPE_IMAGE
     */
    public static final String PROPERTY_DIM_TYPE = "dimType";
    
    /**
     * The overlay image to use
     * 
     * @see #DIM_TYPE_IMAGE
     */
    public static final String PROPERTY_DIM_IMAGE = "dimImage";
    
    /**
     * The final dim opacity
     * 
     * @see #DIM_TYPE_OPACITY
     */
    public static final String PROPERTY_DIM_OPACITY = "dimOpacity";
    
    /**
     * The color to use for opacity dimming.
     * 
     * @see #DIM_TYPE_OPACITY
     */
    public static final String PROPERTY_DIM_COLOR = "dimColor";
    
    /**
     * Whether or not dimming should be animated
     * 
     * @see #DIM_TYPE_OPACITY
     * @see #PROPERTY_DIM_ANIMATION_SPEED
     */
    public static final String PROPERTY_DIM_ANIMATED = "dimAnimated";

    /**
     * The dimming speed.  Higher numbers are faster.
     * 
     * @see #DIM_TYPE_OPACITY
     * @see #PROPERTY_DIM_ANIMATED
     */
    public static final String PROPERTY_DIM_ANIMATION_SPEED = "dimAnimationSpeed";
    
    /**
     * Opacity dimming.  Use a colored DIV and set an opacity value on it.  This
     * mode supports animated dimming.
     */
    public static final int DIM_TYPE_OPACITY = 0;
    
    /**
     * Image dimming.  Overlay an alpha-supporting image over the window.  This
     * mode does not support animated dimming.
     */
    public static final int DIM_TYPE_IMAGE = 1;
    
    /**
     * Auto selection.  This mode favors opacity dimming, but falls back to
     * image based on browser support.  This is probably the desired option.
     */
    public static final int DIM_TYPE_AUTO = 2;
    
    
    public ModalDimmer() {
        setDimType(DIM_TYPE_AUTO);
        setDimOpacity(0.6f);
        setDimColor(Color.BLACK);
        setDimAnimated(true);
        setDimAnimationSpeed(0.38f);
        setDimImage(new ResourceImageReference("org/karora/cooee/sandbox/tucana/resource/image/translucent_60_percent.png"));
    }
    

    /**
     * Set the dimming type.
     * @param type The new dimming type.
     * 
     * @see #DIM_TYPE_IMAGE
     * @see #DIM_TYPE_OPACITY
     */
    public void setDimType(int type) {
        if (type != DIM_TYPE_OPACITY && type != DIM_TYPE_IMAGE && type != DIM_TYPE_AUTO) {
            throw new IllegalArgumentException("Invalid dim type: " + type);
        }
        
        setProperty(PROPERTY_DIM_TYPE, new Integer(type));
    }

    /**
     * Return the current dim type.
     * @return The current dim type, or <code>-1</code> if one is not set.
     * 
     * @see #DIM_TYPE_IMAGE
     * @see #DIM_TYPE_OPACITY
     */
    public int getDimType() {
        Integer type = (Integer) getProperty(PROPERTY_DIM_TYPE);
        if (type == null) {
            return -1;
        }
        
        return type.intValue();
    }
    
    /**
     * Set the dim image to use.
     * @param dimImage The dim image to use
     * 
     * @see #DIM_TYPE_IMAGE
     */
    public void setDimImage(ImageReference dimImage) {
        setProperty(PROPERTY_DIM_IMAGE, dimImage);
    }

    /**
     * Get the current dim image.
     * @return The current dim image, or <code>null</code> if one is not set.
     */
    public ImageReference getDimImage() {
        return (ImageReference) getProperty(PROPERTY_DIM_IMAGE);
    }
    
    /**
     * Set the dim opacity. Valid values are between 0 and 1.
     * @param opacity The dim opacity.
     * 
     * @see #DIM_TYPE_OPACITY
     */
    public void setDimOpacity(float opacity) {
        if (opacity < 0.0f || opacity > 1.0f) {
            throw new IllegalArgumentException("Invalid opacity value: " + opacity);
        }
        setProperty(PROPERTY_DIM_OPACITY, new Float(opacity));
    }
    
    /**
     * Get the current dim opacity.
     * @return The current opacity, or <code>-1.0f</code> if it is not set.
     */
    public float getDimOpacity() {
        Float opacity = (Float) getProperty(PROPERTY_DIM_OPACITY);
        if (opacity == null) {
            return -1.0f;
        }
        return opacity.floatValue();
    }

    /**
     * Set the opacity dimming color
     * @param color The opacity dimming color
     * 
     * @see #DIM_TYPE_OPACITY
     */
    public void setDimColor(Color color) {
        setProperty(PROPERTY_DIM_COLOR, color);
    }
    
    /**
     * Get the opacity dimming color
     * @return The opacity dimming color, or <code>null</code> if none is set.
     */
    public Color getDimColor() {
        return (Color) getProperty(PROPERTY_DIM_COLOR);
    }
    
    /**
     * Set the animation status
     * @param animated Animation on/off status
     * 
     * @see #DIM_TYPE_OPACITY
     */ 
    public void setDimAnimated(boolean animated) {
        setProperty(PROPERTY_DIM_ANIMATED, new Boolean(animated));
    }
    
    /**
     * Return the current animation on/off status.
     * @return The current animation on/off status, or <code>false</code> 
     * if it is not set.
     *
     * @see #DIM_TYPE_OPACITY
     */
    public boolean isDimAnimated() {
        Boolean animated = (Boolean) getProperty(PROPERTY_DIM_ANIMATED);
        if (animated == null) {
            return false;
        }
        return animated.booleanValue();
    }
    
    /**
     * Set the dimming animation speed
     * @param speed The speed
     *
     * @see #DIM_TYPE_OPACITY
     */
    public void setDimAnimationSpeed(float speed) {
        setProperty(PROPERTY_DIM_ANIMATION_SPEED, new Float(speed));
    }
    
    /**
     * Return the current dim animation speed
     * @return The current animation speed, or <code>-1.0f</code> if it is not set.
     *
     * @see #DIM_TYPE_OPACITY
     */
    public float getDimAnimationSpeed() {
        Float speed = (Float) getProperty(PROPERTY_DIM_ANIMATION_SPEED);
        if (speed == null) {
            return -1.0f;
        }
        return speed.floatValue();
    }
    
    /**
     * Disallow the addition of children.
     */
    public void add(Component child, int n) {
        throw new UnsupportedOperationException(
                "Can't add a child to a ModalDimmer component");
    }
}
