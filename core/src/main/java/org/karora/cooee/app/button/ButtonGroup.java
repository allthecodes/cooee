/* 
 * This file is part of the Echo Web Application Framework (hereinafter "Echo").
 * Copyright (C) 2002-2005 NextApp, Inc.
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

package org.karora.cooee.app.button;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.karora.cooee.app.ApplicationInstance;
import org.karora.cooee.app.RadioButton;
import org.karora.cooee.app.RenderIdSupport;

/**
 * A collection of radio buttons which allows the selection of only one
 * radio button at a time.
 */
public class ButtonGroup 
implements RenderIdSupport, Serializable {
    
    private static final ToggleButton[] EMPTY = new ToggleButton[0];
    
    private String id = ApplicationInstance.generateSystemId();
    private Set buttons;
    
    /**
     * Adds a <code>RadioButton</code> to the group.
     * Applications should use <code>RadioButton.setGroup()</code> to add
     * buttons from a group rather than invoking this method.
     * 
     * @param radioButton the <code>RadioButton</code> to add
     * @see RadioButton#setGroup(ButtonGroup)
     */
    public void addButton(ToggleButton radioButton) {
        if (buttons == null) {
            buttons = new HashSet();
        }
        buttons.add(radioButton);
        updateSelection(radioButton);
    }
    
    /**
     * Returns all <code>RadioButton</code>s in the group.
     * 
     * @return the <code>RadioButton</code>
     */
    public ToggleButton[] getButtons() {
        if (buttons == null) {
            return EMPTY;
        } else {
            return (ToggleButton[]) buttons.toArray(new ToggleButton[buttons.size()]);
        }
    }
    
    /**
     * @see org.karora.cooee.app.RenderIdSupport#getRenderId()
     */
    public String getRenderId() {
        return id;
    }
    
    /**
     * Removes a <code>RadioButton</code> from the group.
     * Applications should use <code>RadioButton.setGroup()</code> to remove
     * buttons from a group rather than invoking this method.
     * 
     * @param radioButton the <code>RadioButton</code> to remove
     * @see RadioButton#setGroup(ButtonGroup)
     */
    public void removeButton(ToggleButton radioButton) {
        if (buttons != null) {
            buttons.remove(radioButton);
        }
    }
    
    /**
     * Notifies the <code>ButtonGroup</code> that a <code>RadioButton</code>
     * within its domain may have changed state.
     * 
     * @param changedButton the changed <code>RadioButton</code>
     */
    public void updateSelection(ToggleButton changedButton) {
        if (buttons == null || !changedButton.isSelected()) {
            return;
        }
        Iterator buttonIt = buttons.iterator();
        while (buttonIt.hasNext()) {
            ToggleButton button = (ToggleButton) buttonIt.next();
            if (!button.equals(changedButton)) {
                button.setSelected(false);
            }
        }
    }
}
