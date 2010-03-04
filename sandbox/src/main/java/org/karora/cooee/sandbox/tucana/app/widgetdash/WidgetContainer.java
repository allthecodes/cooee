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

package org.karora.cooee.sandbox.tucana.app.widgetdash;

import org.karora.cooee.app.Component;



/**
 * A {@link Component} that wraps around a Widget.  The WidgetContainer provides
 * a title bar for the widget, and things such as close buttons and maximize buttons.
 * This abstract class provides no UI.
 * 
 * @author Jeremy Volkman
 *
 */
public abstract class WidgetContainer extends Component {

    /**
     * Property key for this container's WidgetPosition
     */
    public static final String PROPERTY_POSITION = "widgetPosition";
	
    /**
     * This container's WidgetIdentifier
     */
    protected WidgetIdentifier widgetIdentifier;
    
    /**
     * Constructor. 
     * @param widgetIdentifier The WidgetIdentifier for this WidgetContainer. 
     * Must not be null.
     */
    public WidgetContainer(WidgetIdentifier widgetIdentifier) {
        if (widgetIdentifier == null) {
            throw new NullPointerException("widgetIdentifier cannot be null");
        }
        this.widgetIdentifier = widgetIdentifier;
    }
    
    /**
     * Return the body of this widget. This is the top layer component directly
     * under the WidgetContainer
     * @return The widget body.
     */
    public abstract Component getWidgetBody();
    
    /**
     * Return the WidgetIdentifier associated with this widget
     * @return The WidgetIdentifier
     */
    public WidgetIdentifier getWidgetIdentifier() {
        return widgetIdentifier;
    }
    
    /**
     * Return this widget's position
     * @return This widget's WidgetPosition, or null if it doesn't have one
     */
    public WidgetPosition getWidgetPosition() {
        return (WidgetPosition) getProperty(PROPERTY_POSITION);
    }
    
    /**
     * Set this widget's position
     * @param widgetPosition the new position
     */
    public void setWidgetPosition(WidgetPosition widgetPosition) {
        setProperty(PROPERTY_POSITION, widgetPosition);
    }
    
    
    public void processInput(String inputName, Object inputValue) {
        if (PROPERTY_POSITION.equals(inputName)) {
            setWidgetPosition((WidgetPosition) inputValue);
        }
    }
}
