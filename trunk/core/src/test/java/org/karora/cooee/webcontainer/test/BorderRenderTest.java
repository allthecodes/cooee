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

package org.karora.cooee.webcontainer.test;

import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Extent;
import org.karora.cooee.webcontainer.propertyrender.BorderRender;
import org.karora.cooee.webrender.output.CssStyle;

import junit.framework.TestCase;

/**
 * 
 */
public class BorderRenderTest extends TestCase {
    
    public void testNull() {
        CssStyle cssStyle = new CssStyle();
        Border border = new Border(null, null, Border.STYLE_NONE);
        BorderRender.renderToStyle(cssStyle, border);
        assertEquals("none", cssStyle.getAttribute("border"));
    }
    
    public void testAll() {
        CssStyle cssStyle = new CssStyle();
        Border border = new Border(new Extent(20, Extent.PX), Color.GREEN, Border.STYLE_SOLID);
        BorderRender.renderToStyle(cssStyle, border);
        assertEquals("20px solid #00ff00", cssStyle.getAttribute("border"));
    }
}
