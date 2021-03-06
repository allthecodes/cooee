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

package org.karora.cooee.app.test;

import org.karora.cooee.app.Button;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.MutableStyle;
import org.karora.cooee.app.MutableStyleSheet;

import junit.framework.TestCase;

/**
 * Unit test(s) for <code>org.karora.cooee.app.StyleSheet</code>.
 */
public class StyleSheetTest extends TestCase {
    
    public void testBasicOperation() {
        MutableStyleSheet styleSheet = new MutableStyleSheet();
        
        MutableStyle alphaButtonStyle = new MutableStyle();
        alphaButtonStyle.setProperty(Button.PROPERTY_BORDER, Constants.BORDER_THICK_ORANGE);
        alphaButtonStyle.setProperty(Button.PROPERTY_BACKGROUND, Color.YELLOW);
        styleSheet.addStyle(Button.class, "alpha", alphaButtonStyle);
        
        assertEquals(alphaButtonStyle, styleSheet.getStyle(Button.class, "alpha"));
        assertNull(styleSheet.getStyle(Button.class, "bravo"));
        
        MutableStyle bravoButtonStyle = new MutableStyle();
        bravoButtonStyle.setProperty(Button.PROPERTY_BORDER, Constants.BORDER_THIN_YELLOW);
        bravoButtonStyle.setProperty(Button.PROPERTY_BACKGROUND, Color.GREEN);
        styleSheet.addStyle(Button.class, "bravo", bravoButtonStyle);

        assertEquals(alphaButtonStyle, styleSheet.getStyle(Button.class, "alpha"));
        assertEquals(bravoButtonStyle, styleSheet.getStyle(Button.class, "bravo"));
        
        MutableStyle bravoLabelStyle = new MutableStyle();
        bravoLabelStyle.setProperty(Label.PROPERTY_FOREGROUND, Color.RED);
        styleSheet.addStyle(Label.class, "bravo", bravoLabelStyle);

        assertEquals(alphaButtonStyle, styleSheet.getStyle(Button.class, "alpha"));
        assertEquals(bravoButtonStyle, styleSheet.getStyle(Button.class, "bravo"));
        assertEquals(bravoLabelStyle, styleSheet.getStyle(Label.class, "bravo"));
    }
}
