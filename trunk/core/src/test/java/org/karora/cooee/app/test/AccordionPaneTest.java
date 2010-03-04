package org.karora.cooee.app.test;

// This package for developer reference only
import org.karora.cooee.app.test.Constants;


import org.karora.cooee.app.AccordionPane;
import org.karora.cooee.app.BorderPane;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Row;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.Table;
import org.karora.cooee.app.TabPane;
import org.karora.cooee.app.TransitionPane;
import org.karora.cooee.app.Window;
import org.karora.cooee.app.WindowPane;

import org.karora.cooee.ng.ContainerEx;
import org.karora.cooee.ng.ContentPaneEx;
import org.karora.cooee.ng.LightBox;
import org.karora.cooee.ng.HttpPaneEx;
import org.karora.cooee.ng.StackedPaneEx;
import org.karora.cooee.ng.TabbedPane;
import org.karora.cooee.ng.TemplatePanel;

// Unit Testing Framework
import junit.framework.TestCase;

// Java Framework Beans
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Unit tests for AccordionPane
 */

public class AccordionPaneTest extends TestCase {
   
    AccordionPane accordionPane = new AccordionPane();
    boolean listenerCalled = false;

    // PropertyChangeListener are involked synchronously
    public PropertyChangeListener tabChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            listenerCalled = true;
        }
    };
    
    public void setUp(){
        accordionPane = new AccordionPane();
        listenerCalled = false;
    }

    public void testDefaultConstructor() {
    
        // Class defaults
        assertEquals(-1, accordionPane.getActiveTabIndex());
        assertNull(accordionPane.getDefaultContentInsets());
        assertNull(accordionPane.getTabBackground());
        assertNull(accordionPane.getTabBackgroundImage());
        assertNull(accordionPane.getTabBorder());
        assertNull(accordionPane.getTabForeground());
        assertNull(accordionPane.getTabInsets());
        assertNull(accordionPane.getTabRolloverBackgroundImage());
        assertNull(accordionPane.getTabRolloverBorder());
        assertTrue(accordionPane.isTabRolloverEnabled());
        assertNull(accordionPane.getTabRolloverForeground());
    }

    public void testProperties() {
        
        accordionPane.setDefaultContentInsets(Constants.INSETS_SIMPLE);
        accordionPane.setTabBackground(Color.ORANGE);
        accordionPane.setTabBackgroundImage(Constants.BACKGROUND_IMAGE);
        accordionPane.setTabBorder(Constants.BORDER_THIN_YELLOW);
        accordionPane.setTabForeground(Color.BLUE);
        accordionPane.setTabInsets(Constants.INSETS_1234);
        accordionPane.setTabRolloverBackgroundImage(Constants.BACKGROUND_IMAGE);
        accordionPane.setTabRolloverBorder(Constants.BORDER_THICK_ORANGE);
        accordionPane.setTabRolloverForeground(Color.RED);

        assertEquals(Constants.INSETS_SIMPLE, accordionPane.getDefaultContentInsets());
        assertEquals(Color.ORANGE, accordionPane.getTabBackground());
        assertEquals(Constants.BACKGROUND_IMAGE, accordionPane.getTabBackgroundImage());
        assertEquals(Constants.BORDER_THIN_YELLOW, accordionPane.getTabBorder());
        assertEquals(Color.BLUE, accordionPane.getTabForeground());
        assertEquals(Constants.INSETS_1234, accordionPane.getTabInsets());
        assertEquals(Constants.BORDER_THICK_ORANGE, accordionPane.getTabRolloverBorder());
        assertEquals(Color.RED, accordionPane.getTabRolloverForeground());
         
    }
    
    public void testTabRolloverEnabled() {

        // Enabled by default
        assertTrue(accordionPane.isTabRolloverEnabled());

        // Toggle enabled 'false' then back to 'true'
        accordionPane.setTabRolloverEnabled(false);
        assertFalse(accordionPane.isTabRolloverEnabled());
        accordionPane.setTabRolloverEnabled(true);
        assertTrue(accordionPane.isTabRolloverEnabled());

    }
    
    public void testIsValidParent() {
    
        // Cooee Core PaneContainers
        assertTrue(accordionPane.isValidParent(new AccordionPane()));
        assertTrue(accordionPane.isValidParent(new BorderPane()));
        assertTrue(accordionPane.isValidParent(new SplitPane()));
        assertTrue(accordionPane.isValidParent(new TabPane()));
        assertTrue(accordionPane.isValidParent(new TransitionPane()));
        assertTrue(accordionPane.isValidParent(new WindowPane()));

        // Cooee EPNG PaneContainers
        assertTrue(accordionPane.isValidParent(new ContainerEx()));
        assertTrue(accordionPane.isValidParent(new ContentPaneEx()));
        assertTrue(accordionPane.isValidParent(new HttpPaneEx()));
        assertTrue(accordionPane.isValidParent(new StackedPaneEx()));
        assertTrue(accordionPane.isValidParent(new TabbedPane()));
        assertTrue(accordionPane.isValidParent(new TemplatePanel()));

        // Cooee Core Components which act as containers
        assertFalse(accordionPane.isValidParent(new Row()));
        assertFalse(accordionPane.isValidParent(new Column()));
        assertFalse(accordionPane.isValidParent(new Table()));
        assertFalse(accordionPane.isValidParent(new Window()));
        
        // Cooee EPNG Components which act as containers
        assertFalse(accordionPane.isValidParent(new LightBox()));
    }



    public void testGetActiveTabIndex() {
    
        assertFalse(listenerCalled);
    
        // Add a listener, change the tab index, verify that the listener was called
        accordionPane.addPropertyChangeListener(tabChangeListener);
        accordionPane.setActiveTabIndex(0);
        
        assertTrue(listenerCalled);
        assertEquals(0, accordionPane.getActiveTabIndex());

        // Remove the listener, change the tab index, verify that the listener was not called
        accordionPane.removePropertyChangeListener(tabChangeListener);
        listenerCalled = false;
        accordionPane.setActiveTabIndex(1);
        
        assertFalse(listenerCalled);
        assertEquals(1, accordionPane.getActiveTabIndex());
   }



    public void testProcessInput() {

        assertFalse(listenerCalled);
    
        // Add a listener, change the tab index, verify that the listener was called
        accordionPane.addPropertyChangeListener(tabChangeListener);
        accordionPane.processInput(AccordionPane.INPUT_TAB_INDEX, new Integer(0));
        
        assertTrue(listenerCalled);
        assertEquals(0, accordionPane.getActiveTabIndex());

        // Remove the listener, change the tab index, verify that the listener was not called
        accordionPane.removePropertyChangeListener(tabChangeListener);
        listenerCalled = false;
        accordionPane.processInput(AccordionPane.INPUT_TAB_INDEX, new Integer(1));
        
        assertFalse(listenerCalled);
        assertEquals(1, accordionPane.getActiveTabIndex());
    }
}
