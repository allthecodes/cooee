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

import org.karora.cooee.app.Color;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.IllegalChildException;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.SplitPane;

import junit.framework.TestCase;

/**
 * Unit test(s) for the <code>org.karora.cooee.app.SplitPane</code> component.
 */
public class SplitPaneTest extends TestCase {

    /**
     * Test empty constructor and verify default state.
     */
    public void testEmptyConstructor() {
        SplitPane splitPane = new SplitPane();
        assertNull(splitPane.getSeparatorPosition());
        assertFalse(splitPane.isResizable());
    }
    
    /**
     * Test receiving input from client.
     */
    public void testInput() {
        SplitPane splitPane = new SplitPane();
        splitPane.add(new Label("one label"));
        splitPane.add(new Label("one more label"));
        splitPane.setSeparatorPosition(new Extent(80));
        splitPane.processInput(SplitPane.PROPERTY_SEPARATOR_POSITION, new Extent(212));
        assertEquals(new Extent(212), splitPane.getSeparatorPosition());
    }
    
    /**
     * Attempt to illegally add more than two children, tests for failure.
     */
    public void testOverload() {
        SplitPane splitPane = new SplitPane();
        splitPane.add(new Label("one label"));
        splitPane.add(new Label("one more label"));
        boolean exceptionThrown = false;
        try {
            splitPane.add(new Label("one label too many"));
        } catch (IllegalChildException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    /**
     * Test primary constructor.
     */
    public void testPrimaryConstructor() {
        SplitPane splitPane;
        splitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL, Constants.EXTENT_200_PX);
        assertEquals(SplitPane.ORIENTATION_VERTICAL, splitPane.getOrientation());
        assertEquals(Constants.EXTENT_200_PX, splitPane.getSeparatorPosition());
        
        splitPane = new SplitPane(SplitPane.ORIENTATION_HORIZONTAL, Constants.EXTENT_500_PX); 
        assertEquals(SplitPane.ORIENTATION_HORIZONTAL, splitPane.getOrientation());
        assertEquals(Constants.EXTENT_500_PX, splitPane.getSeparatorPosition());
    }
    
    /**
     * Test property accessors and mutators.
     */
    public void testProperties() {
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(SplitPane.ORIENTATION_VERTICAL);
        assertEquals(SplitPane.ORIENTATION_VERTICAL, splitPane.getOrientation());
        splitPane.setSeparatorPosition(Constants.EXTENT_200_PX);
        assertEquals(Constants.EXTENT_200_PX, splitPane.getSeparatorPosition());
        splitPane.setSeparatorHeight(Constants.EXTENT_30_PX);
        assertEquals(Constants.EXTENT_30_PX, splitPane.getSeparatorHeight());
        splitPane.setSeparatorWidth(Constants.EXTENT_100_PX);
        assertEquals(Constants.EXTENT_100_PX, splitPane.getSeparatorWidth());
        splitPane.setSeparatorColor(Color.RED);
        assertEquals(Color.RED, splitPane.getSeparatorColor());
        splitPane.setResizable(true);
        assertEquals(true, splitPane.isResizable());
        splitPane.setSeparatorHorizontalImage(Constants.BACKGROUND_IMAGE);
        assertEquals(Constants.BACKGROUND_IMAGE, splitPane.getSeparatorHorizontalImage());
        splitPane.setSeparatorVerticalImage(Constants.BACKGROUND_IMAGE);
        splitPane.setSeparatorHorizontalImage(null);
        assertEquals(Constants.BACKGROUND_IMAGE, splitPane.getSeparatorVerticalImage());
    }
}
