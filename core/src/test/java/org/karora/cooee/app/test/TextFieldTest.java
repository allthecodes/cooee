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

import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.IllegalChildException;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.TextField;
import org.karora.cooee.app.text.StringDocument;

import junit.framework.TestCase;

/**
 * Unit test(s) for the <code>org.karora.cooee.app.TextField</code> and
 * <code>cooee.echo.TextComponent</code> components. 
 */
public class TextFieldTest extends TestCase {
    
    /**
     * Test empty constructor and verify defaults.
     */
    public void testEmptyConstructor() {
        TextField textField = new TextField();
        assertNotNull(textField.getDocument());
        assertEquals(StringDocument.class, textField.getDocument().getClass());
    }
    
    /**
     * Attempt to illegally add children, test for failure.
     */
    public void testIllegalChildren() {
        TextField textField = new TextField();
        boolean exceptionThrown = false;
        try {
            textField.add(new Label("you can't add children to this component, right?"));
        } catch (IllegalChildException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    /**
     * Test receiving input from client.
     */
    public void testInput() {
        TextField textField = new TextField();
        textField.processInput(TextField.TEXT_CHANGED_PROPERTY, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", textField.getDocument().getText());
    }

    /**
     * Test primary constructor.
     */
    public void testPrimaryConstructor() {
        TextField textField = new TextField(new StringDocument(), "text", 30);
        assertEquals("text", textField.getDocument().getText());
        assertEquals(new Extent(30, Extent.EX), textField.getWidth());
    }
    
    /**
     * Ensure large text is trimmed if MaximumLength property is et. 
     */
    public void testMaxLengthTrim() {
        TextField textField = new TextField();
        textField.setMaximumLength(5);
        textField.setText("abcdefghijkl");
        assertEquals("abcde", textField.getText());
    }
    
    /**
     * Test property accessors and mutators.
     */
    public void testProperties() {
        TextField textField = new TextField();
        textField.setAlignment(new Alignment(Alignment.LEADING, Alignment.BOTTOM));
        assertEquals(new Alignment(Alignment.LEADING, Alignment.BOTTOM), textField.getAlignment());
        textField.setBorder(Constants.BORDER_THICK_ORANGE);
        assertEquals(Constants.BORDER_THICK_ORANGE, textField.getBorder());
        textField.setHeight(Constants.EXTENT_30_PX);
        assertEquals(Constants.EXTENT_30_PX, textField.getHeight());
        textField.setInsets(Constants.INSETS_1234);
        assertEquals(Constants.INSETS_1234, textField.getInsets());
        textField.setWidth(Constants.EXTENT_100_PX);
        assertEquals(Constants.EXTENT_100_PX, textField.getWidth());
        textField.setBackgroundImage(Constants.BACKGROUND_IMAGE);

        assertEquals(Constants.BACKGROUND_IMAGE, textField.getBackgroundImage());
        assertEquals(-1, textField.getMaximumLength());
        textField.setMaximumLength(20);
        assertEquals(20, textField.getMaximumLength());
        textField.setMaximumLength(-1);
        assertEquals(-1, textField.getMaximumLength());
    }
}
