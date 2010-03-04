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

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.sandbox.tucana.app.widgetdash.WidgetContainer;
import org.karora.cooee.sandbox.tucana.app.widgetdash.WidgetContainerFactory;
import org.karora.cooee.sandbox.tucana.app.widgetdash.WidgetDashState;
import org.karora.cooee.sandbox.tucana.app.widgetdash.WidgetIdentifier;
import org.karora.cooee.sandbox.tucana.app.widgetdash.WidgetPosition;

/**
 * A Widget Panel is a container organized in columns in which
 * {@link WidgetContainer} components can be placed. The Widgets
 * (WidgetContainers) can be repositioned in the panel such that they all line
 * up in columns. The state of the panel (the user's layout preference) can be
 * extracted and stored server-side, to be used later to recreate the panel for
 * a user.
 * 
 * @author Jeremy Volkman
 * 
 */
public class WidgetDash extends Component {

    public static final String ACTION_POSITIONS_UPDATED = "positionsUpdated";
    
    public static final String ACTION_LISTENERS_CHANGED_PROPERTY = "actionListeners";

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Horizontal spacing between columns.
     */
    public static final String PROPERTY_ACTIVE_WIDGET_CONTAINER = "activeWidgetContainer";

    /**
     * Horizontal spacing between columns.
     */
    public static final String PROPERTY_COLUMN_SPACING = "columnSpacing";

    /**
     * Number of columns in panel.
     */
    public static final String PROPERTY_COLUMN_COUNT = "columnCount";

    /**
     * Vertical spacing betwen widgets.
     */
    public static final String PROPERTY_WIDGET_SPACING = "widgetSpacing";

    /**
     * Type of widget shadow to use.
     */
    public static final String PROPERTY_SHADOW_TYPE = "shadowType";

    /**
     * Opacity of widget shadow (clone mode).
     */
    public static final String PROPERTY_SHADOW_OPACITY = "shadowOpacity";

    /**
     * Opacity of dragged widget.
     */
    public static final String PROPERTY_DRAGGED_WIDGET_OPACITY = "widgetOpacity";

    /**
     * Widget shadow border (border mode).
     */
    public static final String PROPERTY_SHADOW_BORDER = "shadowBorder";

    /**
     * Container to be used for widget dragging.
     */
    public static final String PROPERTY_DRAG_CONTAINER = "dragContainer";

    /**
     * Whether or not to drag widget in the HTML body element.
     */
    public static final String PROPERTY_DRAG_IN_BODY = "dragInBody";

    /**
     * The type of dragged widget return to use (snap or drift (animated)).
     */
    public static final String PROPERTY_RETURN_METHOD = "returnMethod";

    /**
     * The distance in PX of each drift animation step.
     */
    public static final String PROPERTY_DRIFT_STEP = "driftStep";

    /**
     * The amount of time (ms) between drift animation steps.
     */
    public static final String PROPERTY_DRIFT_INTERVAL = "driftInterval";

    /**
     * A cloned node widget shadow
     */
    public static final int SHADOW_TYPE_CLONE = 0;

    /**
     * A border widget shadow
     */
    public static final int SHADOW_TYPE_BORDER = 1;

    /**
     * Snap into position after dropping widget
     */
    public static final int RETURN_METHOD_SNAP = 0;

    /**
     * Drift into position after dropping widget
     */
    public static final int RETURN_METHOD_DRIFT = 1;

    public static final float DEFAULT_SHADOW_OPACITY = 6.0f;

    public static final float DEFAULT_DRAGGED_WIDGET_OPACITY = 9.0f;

    public static final int DEFAULT_SHADOW_TYPE = SHADOW_TYPE_CLONE;

    public static final Border DEFAULT_SHADOW_BORDER = new Border(2,
            Color.BLACK, Border.STYLE_DASHED);

    public static final int DEFAULT_COLUMN_COUNT = 3;

    public static final Extent DEFAULT_COLUMN_SPACING = new Extent(10,
            Extent.PX);

    public static final Extent DEFAULT_WIDGET_SPACING = new Extent(20,
            Extent.PX);

    public static final int DEFAULT_DRIFT_STEP = 20;

    public static final long DEFAULT_DRIFT_INTERVAL = 20;

    /**
     * Default contstructor. Sets default values for all of the properties.
     */
    public WidgetDash() {
        setColumnCount(DEFAULT_COLUMN_COUNT);
        setColumnSpacing(DEFAULT_COLUMN_SPACING);
        setWidgetSpacing(DEFAULT_WIDGET_SPACING);
        setShadowBorder(DEFAULT_SHADOW_BORDER);
        setShadowType(SHADOW_TYPE_CLONE);
        setShadowOpacity(DEFAULT_SHADOW_OPACITY);
        setDraggedWidgetOpacity(DEFAULT_DRAGGED_WIDGET_OPACITY);
        setDragContainer(this);
        setDragInBody(false);
        setReturnMethod(RETURN_METHOD_SNAP);
        setDriftStep(DEFAULT_DRIFT_STEP);
        setDriftInterval(DEFAULT_DRIFT_INTERVAL);
    }

    /**
     * Construct a WidgetPane given the previously stored
     * {@link WidgetDashState} and a {@link WidgetContainerFactory}. Widgets
     * listed in the WidgetPaneState will be added to this WidgetPane.
     * 
     * @param state
     *            The WidgetPaneState to construct from
     * @param factory
     *            The WidgetFactory to use when building Widgets
     */
    public WidgetDash(WidgetDashState state, WidgetContainerFactory factory) {
        this();
        restore(state, factory);
    }

    /**
     * Populate the WidgetPane given the previously stored
     * {@link WidgetDashState} and a {@link WidgetContainerFactory}. Widgets
     * listed in the WidgetPaneState will be added to this WidgetPane.  Any widgets
     * in the dash before this call will be removed.
     * 
     * @param state
     *            The WidgetPaneState to construct from
     * @param factory
     *            The WidgetFactory to use when building Widgets
     */
    public void restore(WidgetDashState state, WidgetContainerFactory factory) {
        removeAll();
        setColumnCount(state.getColumnCount());
        WidgetIdentifier[] identifiers = state.getWidgetIdentifiers();
        for (int i = 0; i < identifiers.length; i++) {
            WidgetContainer container = factory
                    .createWidgetContainer(identifiers[i]);
            if (container != null) {
                WidgetPosition position = state
                        .getWidgetPosition(identifiers[i]);
                if (position != null) {
                    container.setWidgetPosition(position);
                }
                add(container);
            }
        }
    }
    
    /**
     * Set the vertical spacing between widgets in a column
     * 
     * @param spacing
     *            The vertical spacing
     */
    public void setWidgetSpacing(Extent spacing) {
        setProperty(PROPERTY_WIDGET_SPACING, spacing);
    }

    /**
     * Get the vertical spacing between widgets in a column
     * 
     * @return The vertical spacing, or <code>null</code> if it is not set.
     */
    public Extent getWidgetSpacing() {
        return (Extent) getProperty(PROPERTY_WIDGET_SPACING);
    }

    /**
     * Only allow WidgetContainer objects to be added to the WidgetPane
     */
    public boolean isValidChild(Component child) {
        return child instanceof WidgetContainer;
    }

    /**
     * Return the number of columns in this WidgetPane
     * 
     * @return column count, or <code>-1</code> if it is not set.
     */
    public int getColumnCount() {
        Integer columnCount = (Integer) getProperty(PROPERTY_COLUMN_COUNT);
        if (columnCount != null) {
            return columnCount.intValue();
        }

        return -1;
    }

    /**
     * Set the number of columns
     * 
     * @param newCount
     *            column count
     */
    public void setColumnCount(int newCount) {
        if (newCount <= 0) {
            throw new IllegalArgumentException("Invalid number of columns: "
                    + newCount);
        }
        setProperty(PROPERTY_COLUMN_COUNT, new Integer(newCount));
    }

    /**
     * Set the horizontal spacing between columns
     * 
     * @param spacing
     *            The column spacing
     */
    public void setColumnSpacing(Extent spacing) {
        setProperty(PROPERTY_COLUMN_SPACING, spacing);
    }

    /**
     * Get the horizontal spacing between columns
     * 
     * @return The column spacing, or <code>null</code> if it is not set.
     */
    public Extent getColumnSpacing() {
        return (Extent) getProperty(PROPERTY_COLUMN_SPACING);
    }

    /**
     * Return the current shadow type
     * 
     * @return The current shadow type, or <code>-1</code> if one is not set.
     * 
     * @see #SHADOW_TYPE_CLONE
     * @see #SHADOW_TYPE_BORDER
     */
    public int getShadowType() {
        Integer typeInteger = (Integer) getProperty(PROPERTY_SHADOW_TYPE);
        if (typeInteger == null) {
            return -1;
        }
        return typeInteger.intValue();
    }

    /**
     * Set the shadow type
     * 
     * @param type
     *            The new shadow type
     * 
     * @see #SHADOW_TYPE_CLONE
     * @see #SHADOW_TYPE_BORDER
     */
    public void setShadowType(int type) {
        if (type != SHADOW_TYPE_BORDER && type != SHADOW_TYPE_CLONE) {
            throw new IllegalArgumentException("Invalid shadow type");
        }
        setProperty(PROPERTY_SHADOW_TYPE, new Integer(type));
    }

    /**
     * Set the shadow border to use.
     * 
     * @param border
     *            The new shadow border to use
     * 
     * @see #SHADOW_TYPE_BORDER
     */
    public void setShadowBorder(Border border) {
        setProperty(PROPERTY_SHADOW_BORDER, border);
    }

    /**
     * Get the current shadow border.
     * 
     * @return The current shadow border, or <code>null</code> if one is not
     *         set.
     * 
     * @see #SHADOW_TYPE_BORDER
     */
    public Border getShadowBorder() {
        Border border = (Border) getProperty(PROPERTY_SHADOW_BORDER);
        return border;
    }

    /**
     * Set the clone shadow opacity value
     * 
     * @param opacity
     *            The new opacity value
     * 
     * @see #SHADOW_TYPE_CLONE
     */
    public void setShadowOpacity(float opacity) {
        setProperty(PROPERTY_SHADOW_OPACITY, new Float(opacity));
    }

    /**
     * Get the clone shadow opacity value
     * 
     * @return The current opacity value, or <code>-1f</code> if one is not
     *         set.
     * 
     * @see #SHADOW_TYPE_CLONE
     */
    public float getShadowOpacity() {
        Float opacityFloat = (Float) getProperty(PROPERTY_SHADOW_OPACITY);
        if (opacityFloat == null) {
            return -1f;
        }
        return opacityFloat.floatValue();
    }

    /**
     * Set the dragged widget opacity value. This value is applied to a widget
     * when it is clicked to be dragged. The widget's original opacity is reset
     * when it is dropped.
     * 
     * @param opacity
     *            The new opacity value.
     */
    public void setDraggedWidgetOpacity(float opacity) {
        setProperty(PROPERTY_DRAGGED_WIDGET_OPACITY, new Float(opacity));
    }

    /**
     * Get the current dragged widget opacity value.
     * 
     * @return The current opacity value, or <code>-1f</code> if one is not
     *         set.
     */
    public float getDraggedWidgetOpacity() {
        Float opacityFloat = (Float) getProperty(PROPERTY_DRAGGED_WIDGET_OPACITY);
        if (opacityFloat == null) {
            return -1f;
        }
        return opacityFloat.floatValue();
    }

    /**
     * Return whether or not widgets are dragged in the HTML body element
     * 
     * @return Drag-in-body status
     */
    public boolean isDragInBody() {
        Boolean propBoolean = (Boolean) getProperty(PROPERTY_DRAG_IN_BODY);
        if (propBoolean != null) {
            return propBoolean.booleanValue();
        }
        return false;
    }

    /**
     * Sets whether or not widgets are dragged in the HTML body element
     * 
     * @param dragInBody
     *            New drag-in-body status
     * 
     * @see #setDragContainer(Component)
     */
    public void setDragInBody(boolean dragInBody) {
        setProperty(PROPERTY_DRAG_IN_BODY, new Boolean(dragInBody));
    }

    /**
     * Get the current drag container being used.
     * 
     * @return The current drag container, or null if one is not set.
     */
    public Component getDragContainer() {
        Component dragContainer = (Component) getProperty(PROPERTY_DRAG_CONTAINER);
        if (dragContainer == null) {
            return this;
        }
        return dragContainer;
    }

    /**
     * Set the container (component) that widgets should be moved to when being
     * dragged. For example, if the WidgetDash is a child of a SplitPane,
     * dragging works best if the drag container is the parent SplitPane.
     * 
     * @param dragContainer
     *            The new drag container.
     */
    public void setDragContainer(Component dragContainer) {
        setProperty(PROPERTY_DRAG_CONTAINER, dragContainer);
    }
    
    /**
     * Set the method to be used when returning a dropped widget to its shadow's
     * position.
     * 
     * @see #RETURN_METHOD_SNAP
     * @see #RETURN_METHOD_DRIFT
     * 
     * @param returnMethod
     */
    public void setReturnMethod(int returnMethod) {
        if (returnMethod != RETURN_METHOD_SNAP
                && returnMethod != RETURN_METHOD_DRIFT) {
            throw new IllegalArgumentException("Invalid return method type: "
                    + returnMethod);
        }
        setProperty(PROPERTY_RETURN_METHOD, new Integer(returnMethod));
    }

    /**
     * Get the current return method
     * 
     * @return The current return method, or <code>-1</code> if one is not set.
     * 
     * @see #RETURN_METHOD_SNAP
     * @see #RETURN_METHOD_DRIFT
     */
    public int getReturnMethod() {
        Integer returnMethod = (Integer) getProperty(PROPERTY_RETURN_METHOD);
        if (returnMethod != null) {
            return returnMethod.intValue();
        }
        return -1;
    }

    /**
     * Set the drift step amount (px)
     * @param driftStep The step amount.
     * 
     * @see #PROPERTY_DRIFT_STEP
     */
    public void setDriftStep(int driftStep) {
        setProperty(PROPERTY_DRIFT_STEP, new Integer(driftStep));
    }

    /**
     * Get the drift step amount (px)
     * 
     * @return The current step amount
     * 
     * @see #PROPERTY_DRIFT_STEP
     */
    public int getDriftStep() {
        Integer driftStep = (Integer) getProperty(PROPERTY_DRIFT_STEP);
        if (driftStep != null) {
            return driftStep.intValue();
        }
        return -1;
    }

    /**
     * Set the drift step interval (ms).
     * @param driftStep The new step interval.
     * 
     * @see #PROPERTY_DRIFT_INTERVAL
     */
    public void setDriftInterval(long driftStep) {
        setProperty(PROPERTY_DRIFT_INTERVAL, new Long(driftStep));
    }

    /**
     * Get the drift step interval (ms).
     * @return The current step interval.
     * 
     * @see #PROPERTY_DRIFT_INTERVAL
     */
    public long getDriftInterval() {
        Long driftStep = (Long) getProperty(PROPERTY_DRIFT_STEP);
        if (driftStep != null) {
            return driftStep.longValue();
        }
        return -1L;
    }

    /**
     * Get the current state of this WidgetDash
     * 
     * @return A new WidgetDashState object. This object should not change
     *         after being returned from this method (i.e., it should not
     *         reference internal structures)
     */
    public WidgetDashState getWidgetDashState() {
        return new WidgetDashStateImpl(getColumnCount(), getComponents());
    }

    
    public WidgetContainer[] getWidgetContainers()
    {
        Component[] components = getComponents();
        ArrayList<WidgetContainer> widgets = new ArrayList<WidgetContainer>();
        for (Component c: components)
        {
            if (c instanceof WidgetContainer)
            {
                widgets.add((WidgetContainer) c);
            }
        }
        
        return widgets.toArray(new WidgetContainer[0]);
    }
    
    
    
    /**
     * Implementation of WidgetPaneState.
     * 
     * @author Jeremy Volkman
     */
    private static class WidgetDashStateImpl implements WidgetDashState {

        private int columnCount;

        private Map widgetMap;

        public WidgetDashStateImpl(int columnCount,
                Component[] widgetContainers) {
            this.columnCount = columnCount;
            widgetMap = new HashMap();
            for (int i = 0; i < widgetContainers.length; i++) {
                WidgetContainer container = (WidgetContainer) widgetContainers[i];
                widgetMap.put(container.getWidgetIdentifier(), container
                        .getWidgetPosition());
            }
        }

        public int getColumnCount() {
            return columnCount;
        }

        public WidgetIdentifier[] getWidgetIdentifiers() {
            return (WidgetIdentifier[]) widgetMap.keySet().toArray(
                    new WidgetIdentifier[widgetMap.size()]);
        }

        public WidgetPosition getWidgetPosition(WidgetIdentifier widget) {
            return (WidgetPosition) widgetMap.get(widget);
        }

    }
    
    /**
     * Adds an <code>ActionListener</code> to receive notification of user
     * actions, i.e., widget position changes.
     * 
     * @param l the listener to add
     */
    public void addActionListener(ActionListener l) {
        getEventListenerList().addListener(ActionListener.class, l);
        // Notification of action listener changes is provided due to 
        // existence of hasActionListeners() method. 
        firePropertyChange(ACTION_LISTENERS_CHANGED_PROPERTY, null, l);
    }
    
    /**
     * Removes an <code>ActionListener</code> from being notified of user
     * actions, i.e., widget position changes.
     * 
     * @param l the listener to remove
     */
    public void removeActionListener(ActionListener l) {
        if (!hasEventListenerList()) {
            return;
        }
        getEventListenerList().removeListener(ActionListener.class, l);
        // Notification of action listener changes is provided due to 
        // existence of hasActionListeners() method. 
        firePropertyChange(ACTION_LISTENERS_CHANGED_PROPERTY, l, null);
    }
    
    public void processInput(String name, Object value) {
        System.out.println ("Processing input: " + name);
        if (ACTION_POSITIONS_UPDATED.equals(name)) {
            EventListener[] listeners = getEventListenerList().getListeners(ActionListener.class);
            ActionEvent e = new ActionEvent(this, ACTION_POSITIONS_UPDATED);
            for (int i = 0; i < listeners.length; i++) {
                ActionListener actionListener = (ActionListener) listeners[i];
                actionListener.actionPerformed(e);
            }
        }
    }

    /**
     * Get the current active widget container.
     * @return The current active widget container.
     * 
     * @see #PROPERTY_ACTIVE_WIDGET_CONTAINER
     */
    public WidgetContainer getActiveWidgetContainer() {
    	return (WidgetContainer) getProperty(PROPERTY_ACTIVE_WIDGET_CONTAINER);
    }

    /**
     * Set the active widget container.
     * @param newValue The current active widget container.
     * 
     * @see #PROPERTY_ACTIVE_WIDGET_CONTAINER
     */
    public void setActiveWidgetContainer(WidgetContainer newValue) {
    	setProperty(PROPERTY_ACTIVE_WIDGET_CONTAINER, newValue);
    }
}
