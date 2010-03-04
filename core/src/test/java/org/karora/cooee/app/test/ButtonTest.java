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
import org.karora.cooee.app.Button;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.IllegalChildException;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.button.ButtonModel;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;

import junit.framework.TestCase;


/**
 * Unit tests for the <code>org.karora.cooee.app.button.AbstractButton</code>-based 
 * components.
 */
public class ButtonTest extends TestCase {
    
    /**
     * <code>ActionListener</code> that retains last fired event and counts
     * events received.
     */
    private static class ActionHandler 
    implements ActionListener {
        
        int eventCount = 0;
        ActionEvent lastEvent;
        
        /**
         * @see org.karora.cooee.app.event.ActionListener#actionPerformed(org.karora.cooee.app.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            lastEvent = e;
            ++eventCount;
        }
    }
    
    /**
     * Test behavior of <code>ActionListener</code>s.
     */
    public void testActionListener() {
        ActionHandler buttonActionListener = new ActionHandler();
        ActionHandler modelActionListener = new ActionHandler();
        Button button = new Button("Test");
        ButtonModel model = button.getModel();
        button.addActionListener(buttonActionListener);
        model.addActionListener(modelActionListener);
        assertEquals(0, buttonActionListener.eventCount);
        assertEquals(0, modelActionListener.eventCount);
        button.doAction();
        assertEquals(1, buttonActionListener.eventCount);
        assertEquals(1, modelActionListener.eventCount);
        assertEquals(button, buttonActionListener.lastEvent.getSource());
        assertEquals(model, modelActionListener.lastEvent.getSource());

        buttonActionListener.lastEvent = null;
        modelActionListener.lastEvent = null;
        assertEquals(null, buttonActionListener.lastEvent);
        assertEquals(null, modelActionListener.lastEvent);

        model.doAction();
        
        assertEquals(2, buttonActionListener.eventCount);
        assertEquals(2, modelActionListener.eventCount);
        assertEquals(button, buttonActionListener.lastEvent.getSource());
        assertEquals(model, modelActionListener.lastEvent.getSource());
    }
    
    /**
     * Test default button values.
     */
    public void testDefaults() {
        Button button = new Button();
        assertTrue(button.isLineWrap());
        assertFalse(button.isPressedEnabled());
        assertFalse(button.isRolloverEnabled());
        assertNull(button.getActionCommand());
    }
    
    /**
     * Attempt to illegally add children, test for failure.
     */
    public void testIllegalChildren() {
        Button button = new Button();
        boolean exceptionThrown = false;
        try {
            button.add(new Label("you can't add children to this component, right?"));
        } catch (IllegalChildException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    /**
     * Ensure setting model to null fails.
     */
    public void testNullModelException() {
        boolean exceptionThrown = false;
        Button button = new Button();
        try {
            button.setModel(null);
        } catch (IllegalArgumentException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    /**
     * Test pressed-state-related property accessors and mutators.
     */
    public void testPressedProperties() {
        Button button = new Button();
        
        button.setPressedBackground(Color.GREEN);
        button.setPressedBackgroundImage(Constants.BACKGROUND_IMAGE);
        button.setPressedBorder(Constants.BORDER_THICK_ORANGE);
        button.setPressedEnabled(true);
        button.setPressedFont(Constants.TIMES_72);
        button.setPressedForeground(Color.YELLOW);
        button.setPressedIcon(Constants.PRESSED_ICON);
        
        assertEquals(Color.GREEN, button.getPressedBackground());
        assertEquals(Constants.BACKGROUND_IMAGE, button.getPressedBackgroundImage());
        assertEquals(Constants.BORDER_THICK_ORANGE, button.getPressedBorder());
        assertTrue(button.isPressedEnabled());
        assertEquals(Constants.TIMES_72, button.getPressedFont());
        assertEquals(Color.YELLOW, button.getPressedForeground());
        assertEquals(Constants.PRESSED_ICON, button.getPressedIcon());
    }
    
    /**
     * Test property accessors and mutators.
     */
    public void testProperties() {
        Button button = new Button();
        
        button.setText("Alpha");
        assertEquals("Alpha", button.getText());
        
        button.setBackgroundImage(Constants.BACKGROUND_IMAGE);
        assertEquals(Constants.BACKGROUND_IMAGE, button.getBackgroundImage());
        
        button.setIcon(Constants.ICON);
        assertEquals(Constants.ICON, button.getIcon());
        button.setIconTextMargin(Constants.EXTENT_100_PX);
        assertEquals(Constants.EXTENT_100_PX, button.getIconTextMargin());
        
        button.setBorder(Constants.BORDER_THIN_YELLOW);
        assertEquals(Constants.BORDER_THIN_YELLOW, button.getBorder());
        
        button.setHeight(Constants.EXTENT_500_PX);
        assertEquals(Constants.EXTENT_500_PX, button.getHeight());
        button.setWidth(Constants.EXTENT_200_PX);
        assertEquals(Constants.EXTENT_200_PX, button.getWidth());
        
        button.setTextAlignment(new Alignment(Alignment.LEADING, Alignment.BOTTOM));
        assertEquals(new Alignment(Alignment.LEADING, Alignment.BOTTOM), button.getTextAlignment());
        
        button.setTextPosition(new Alignment(Alignment.DEFAULT, Alignment.TOP));
        assertEquals(new Alignment(Alignment.DEFAULT, Alignment.TOP), button.getTextPosition());
    }
    
    /**
     * Test rollover-state-related property accessors and mutators.
     */
    public void testRolloverProperties() {
        Button button = new Button();
        
        button.setRolloverEnabled(true);
        button.setRolloverBackgroundImage(Constants.BACKGROUND_IMAGE);
        button.setRolloverBackground(Color.RED);
        button.setRolloverForeground(Color.BLUE);
        button.setRolloverFont(Constants.MONOSPACE_12);
        button.setRolloverBorder(Constants.BORDER_THIN_YELLOW);
        button.setRolloverIcon(Constants.ROLLOVER_ICON);
        
        assertTrue(button.isRolloverEnabled());
        assertEquals(Constants.BACKGROUND_IMAGE, button.getRolloverBackgroundImage());
        assertEquals(Color.RED, button.getRolloverBackground());
        assertEquals(Color.BLUE, button.getRolloverForeground());
        assertEquals(Constants.MONOSPACE_12, button.getRolloverFont());
        assertEquals(Constants.BORDER_THIN_YELLOW, button.getRolloverBorder());
        assertEquals(Constants.ROLLOVER_ICON, button.getRolloverIcon());

        button.setRolloverEnabled(false);
        assertFalse(button.isRolloverEnabled());
    }
}
