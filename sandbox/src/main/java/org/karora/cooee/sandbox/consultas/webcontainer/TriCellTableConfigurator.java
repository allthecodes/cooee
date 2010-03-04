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
 */

package org.karora.cooee.sandbox.consultas.webcontainer;

import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Component;

/**
 * Provides utility methods to configure <code>TriCellTable</code> for
 * text/state positions specified by buttons/labels.
 */
class TriCellTableConfigurator {
    
    /**
     * Converts the value of a <code>textPosition</code> property into a value
     * suitable to be passed to a <code>TriCellTable</code> as an
     * <code>orientation</code> constructor parameter.
     * This method assumes that the <code>TriCellTable</code> is being rendered
     * with text at index 0 and icon at position 1.
     * 
     * @param textPosition the <code>Alignment</code>
     * @param component the component being rendered
     * @return the <code>orientation</code> value
     */
    static int convertIconTextPositionToOrientation(Alignment textPosition, Component component) {
        if (textPosition.getVertical() == Alignment.DEFAULT) {
            switch (textPosition.getHorizontal()) {
            case Alignment.LEFT:
                return component.getRenderLayoutDirection().isLeftToRight() 
                        ? TriCellTable.LEADING_TRAILING : TriCellTable.TRAILING_LEADING;
            case Alignment.RIGHT:
                return component.getRenderLayoutDirection().isLeftToRight() 
                        ? TriCellTable.TRAILING_LEADING : TriCellTable.LEADING_TRAILING;
            case Alignment.LEADING:
                return TriCellTable.LEADING_TRAILING;
            case Alignment.TRAILING:
                return TriCellTable.TRAILING_LEADING;
            default:
                // Invalid, return value for TRAILING (default).
                return TriCellTable.TRAILING_LEADING;
            }
        } else {
            if (textPosition.getVertical() == Alignment.TOP) {
                return TriCellTable.TOP_BOTTOM;
            } else {
                return TriCellTable.BOTTOM_TOP;
            }
        }
    }
    
    /**
     * Converts the value of the <code>stateAlignment</code> property of a 
     * <code>ToggleButton</code> into a value suitable to be passed to a
     * <code>TriCellTable</code> as an <code>orientation</code> constructor
     * parameter.
     * This method assumes that the <code>TriCellTable</code> is rendered with
     * text/icon at lower indices than the state.
     * 
     * @param statePosition the state position <code>Alignment</code>
     * @param component the button being rendered
     * @return the <code>orientation</code> value
     */
    static int convertStatePositionToOrientation(Alignment statePosition, Component component) {
        if (statePosition.getVertical() == Alignment.DEFAULT) {
            switch (statePosition.getHorizontal()) {
            case Alignment.LEFT:
                return component.getRenderLayoutDirection().isLeftToRight() 
                        ? TriCellTable.TRAILING_LEADING : TriCellTable.LEADING_TRAILING;
            case Alignment.RIGHT:
                return component.getRenderLayoutDirection().isLeftToRight() 
                        ? TriCellTable.LEADING_TRAILING : TriCellTable.TRAILING_LEADING;
            case Alignment.LEADING:
                return TriCellTable.TRAILING_LEADING;
            case Alignment.TRAILING:
                return TriCellTable.LEADING_TRAILING;
            default:
                // Invalid, return value for LEADING (default).
                return TriCellTable.TRAILING_LEADING;
            }
        } else {
            if (statePosition.getVertical() == Alignment.TOP) {
                return TriCellTable.BOTTOM_TOP;
            } else {
                return TriCellTable.TOP_BOTTOM;
            }
        }
    }
}
