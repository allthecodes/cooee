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
 * This is a wrapping {@link Component} that makes its child a widget "Grab Point".
 * Essentially, any Component wrapped by this can be used to drag a widget around
 * the screen.  For instance, a title bar in a WidgetContainer might be wrapped
 * in a WidgetGrabPoint.
 * @author Jeremy Volkman
 *
 */
public class WidgetGrabPoint extends Component {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The default mouse cursor
     */
    public static final String DEFAULT_CURSOR = "move";
    
    /**
     * Mouse cursor property key
     */
    public static final String PROPERTY_CURSOR = "cursor";
    
    private WidgetContainer widgetContainer;
    
    /**
     * Creates a WidgetGrabPoint making the given 
     * Component grabbable 
     * @param grabbable The Component. 
     * @param widgetContainer The WidgetContainer that will be dragged by this point
     */
    public WidgetGrabPoint(Component grabbable, WidgetContainer widgetContainer) {
        this.add(grabbable);
        this.widgetContainer = widgetContainer;
        setCursor(DEFAULT_CURSOR);
    }

    /**
     * Checks that no more than one Component is a 
     * child of this DragSource.
     */
    public void add(Component c, int n) {
        if (getComponentCount() == 1) {
            throw new IllegalStateException(
                "Cannot add more than one Component directly to a GrabPoint."
            );
        }
        super.add(c, n);
    }
    
    /**
     * Returns the Component that is to be dragged.
     * @return The draggable Component.
     */
    public Component getGrabbable() {
        return getComponents().length > 0 ? getComponents()[0] : null;
    }
    
    /**
     * Return the container that this grab point should move.
     * @return The WidgetContainer to be moved
     */
    public WidgetContainer getWidgetContainer() {
        return widgetContainer;
    }

    /**
     * Set the mouse cursor icon to be displayed over this GrabPoint.
     * @param cursor The new icon string
     */
    public void setCursor(String cursor) {
        setProperty(PROPERTY_CURSOR, cursor);
    }

    /**
     * Get the mouse cursor to be displayed over this GrabPoint.
     * @return The mouse cursor string
     */
    public String getCursor() {
        return (String) getProperty(PROPERTY_CURSOR);
    }
}
