package org.karora.cooee.sandbox.informagen.app;

import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;

import junit.framework.TestCase;

public class HorizontalRuleTest extends TestCase{
    
    HorizontalRule hr = new HorizontalRule();
    public void testConstants(){
        assertEquals("border", HorizontalRule.PROPERTY_BORDER);
        assertEquals("disabledBorder", HorizontalRule.PROPERTY_DISABLED_BORDER);
        assertEquals("disabledBackground", HorizontalRule.PROPERTY_DISABLED_BACKGROUND);
        assertEquals("height", HorizontalRule.PROPERTY_HEIGHT);
        assertEquals("width", HorizontalRule.PROPERTY_WIDTH);
    }
    
    public void testGetsAndSets(){
        hr.setBorder(new Border(500, new Color(100), 10));
        hr.setDisabledBackground(new Color(150));
        hr.setDisabledBorder(new Border(300, new Color(200), 1));
        hr.setHeight(new Extent(3));
        hr.setWidth(new Extent(100));

        assertEquals(new Border(500, new Color(100), 10), hr.getBorder());        
        assertEquals(new Color(150), hr.getDisabledBackground());        
        assertEquals(new Border(300, new Color(200), 1), hr.getDisabledBorder());        
        assertEquals(new Extent(3), hr.getHeight());        
        assertEquals(new Extent(100), hr.getWidth());        
    }
}