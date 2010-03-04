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
 * Represents a widget's position in the WidgetPane. A WidgetPane is made up of
 * N number of columns, with some number of widgets in each column.  The WidgetPane
 * is not square grid, since widgets can vary in height.  All numbers are 0-based.
 * 
 * @author Jeremy Volkman
 *
 */
public class WidgetPosition {

    /**
     * The column that the widget is in.
     */
    private int column;
    
    /**
     * The position (index) in the column
     */
    private int columnPosition;
    
    /**
     * Constructor
     * @param column the column
     * @param columnPosition the column position
     */
    public WidgetPosition(int column, int columnPosition) {
        this.column = column;
        this.columnPosition = columnPosition;
    }
    
    /**
     * Get the column
     * @return the column
     */
    public int getColumn() {
        return column;
    }
    
    /**
     * Get the column position
     * @return the column position
     */
    public int getColumnPosition() {
        return columnPosition;
    }
    
    /**
     * Some silly hashcode algorithm
     */
    public int hashCode() {
        
        return 31 * ((17 * column) + (23 * columnPosition));
    }

    public boolean equals(Object thatObj) {
        if (thatObj.getClass().equals(getClass())) {
            WidgetPosition that = (WidgetPosition) thatObj;
            return this.column == that.column 
                && this.columnPosition == that.columnPosition;
        }
        return false;
    }
}
