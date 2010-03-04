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

import java.util.EventListener;

import org.karora.cooee.app.event.ChangeEvent;
import org.karora.cooee.app.event.ChangeListener;



/**
 * Default <code>ToggleButtonModel</code> implementation.
 */
public class DefaultToggleButtonModel extends DefaultButtonModel
implements ToggleButtonModel {

    private boolean selected;

    /**
     * @see org.karora.cooee.app.button.ToggleButtonModel#addChangeListener(org.karora.cooee.app.event.ChangeListener)
     */
    public void addChangeListener(ChangeListener l) {
        getEventListenerList().addListener(ChangeListener.class, l);
    }
    
    /**
     * Notifies all listeners that have registered for this event type.
     */
    public void fireStateChanged() {
        EventListener[] listeners = getEventListenerList().getListeners(ChangeListener.class);
        ChangeEvent e = new ChangeEvent(this);
        for (int index = 0; index < listeners.length; ++index) {
            ((ChangeListener) listeners[index]).stateChanged(e);
        }
    }

    /**
     * @see org.karora.cooee.app.button.ToggleButtonModel#isSelected()
     */
    public boolean isSelected() {
        return selected;
    }
    
    /**
     * @see org.karora.cooee.app.button.ToggleButtonModel#removeChangeListener(org.karora.cooee.app.event.ChangeListener)
     */
    public void removeChangeListener(ChangeListener l) {
        getEventListenerList().removeListener(ChangeListener.class, l);
    }
    
    /**
     * @see org.karora.cooee.app.button.ToggleButtonModel#setSelected(boolean)
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        fireStateChanged();
    }
}
