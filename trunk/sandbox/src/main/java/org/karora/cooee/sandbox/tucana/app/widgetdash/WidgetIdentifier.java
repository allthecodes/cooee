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

/**
 * A widget identifier's purpose is to identify a for lookup in some catalog
 * of available widgets. This is mainly used for repopulating a WidgetPane from
 * a WidgetPaneState object.
 * 
 * @author Jeremy Volkman
 */
public class WidgetIdentifier {
    
    private String identifier;
    
    /**
     * Construct a WidgetIdentifier
     * @param identifier The string identifier
     */
    public WidgetIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    /**
     * Return this WidgetIdentifier's string identifier.  This identifier should
     * be unique to the corresponding widget.
     * @return The string identifier.
     */
    public String getIdentifier() {
        return identifier;
    }
    
    public boolean equals(Object that) {
        if (that.getClass().equals(this.getClass())) {
            return ((WidgetIdentifier)that).identifier.equals(this.identifier);
        }
        return false;
    }
    
    public int hashCode() {
        return identifier.hashCode();
    }
}
