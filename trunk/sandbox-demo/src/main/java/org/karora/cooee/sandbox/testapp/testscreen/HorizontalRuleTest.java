package org.karora.cooee.sandbox.testapp.testscreen;

import org.karora.cooee.sandbox.testapp.ButtonColumn;
import org.karora.cooee.sandbox.testapp.InteractiveApp;
import org.karora.cooee.sandbox.testapp.StyleUtil;


import java.util.Locale;

import org.karora.cooee.app.AccordionPane;
import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Font;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.LayoutDirection;
import org.karora.cooee.app.SplitPane;

import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;

import org.karora.cooee.app.layout.SplitPaneLayoutData;
import org.karora.cooee.app.layout.AccordionPaneLayoutData;

// This test component
import org.karora.cooee.sandbox.informagen.app.HorizontalRule;


/**
 * An interactive test for <code>HorizontalRule</code>s.
 */
 
public class HorizontalRuleTest extends SplitPane {
        
    final HorizontalRule horizontalRule = new HorizontalRule();
    
    public HorizontalRuleTest() {
        super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));
		setStyleName("DefaultResizable");
        
        // Control Groups in the splitpane's left side
        AccordionPane controlGroupsColumn = new AccordionPane();
		controlGroupsColumn.setStyleName("Default"); // BUG - Does not default to "Default"
        add(controlGroupsColumn);

        // Build the testing panel in the splitpane's right side
        add(buildTestingPanel());


        // Build the test control buttons as small groups (controlsColumn) 
        //   placed within a larger group (controlGroupsColumn) 
        ButtonColumn controlsColumn;
        AccordionPaneLayoutData layoutData;
        
        // Base Properties

        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("HorizontalRule Properties");
        controlsColumn.setLayoutData(layoutData);

        
        controlsColumn.addButton("Set Height=null", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setHeight(null);
            }
        });
        
        controlsColumn.addButton("Set Height=3px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setHeight(new Extent(3));
            }
        });
        
        
        controlsColumn.addButton("Set Height=10px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setHeight(new Extent(10));
            }
        });
        
        controlsColumn.addButton("Set Width=null", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setWidth(null);
            }
        });
        
        controlsColumn.addButton("Set width=300px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setWidth(new Extent(300));
            }
        });
        
        
        controlsColumn.addButton("Set Height=75%", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setWidth(new Extent(75, Extent.PERCENT));
            }
        });


        // Color
        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Background Color");
        controlsColumn.setLayoutData(layoutData);
       
        controlsColumn.addButton("Random Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Color color = StyleUtil.randomColor();
                horizontalRule.setBackground(color);
            }
        });
        
        controlsColumn.addButton("Clear Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setBackground(null);
            }
        });


        // Borders

        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Borders");
        controlsColumn.setLayoutData(layoutData);
        
        
        controlsColumn.addButton("Random Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setBorder(StyleUtil.randomBorder());
            }
        });
        
        controlsColumn.addButton("Random Border Color", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = horizontalRule.getBorder();
                
                if (border == null)
                    border = new Border(new Extent(1), StyleUtil.randomColor(), Border.STYLE_SOLID);
                
                horizontalRule.setBorder(new Border(border.getSize(), StyleUtil.randomColor(), border.getStyle()));
            }
        });
        
        controlsColumn.addButton("Random Border Size", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setBorder(StyleUtil.nextBorderSize(horizontalRule.getBorder()));
            }
        });
        
        controlsColumn.addButton("Random Border Style", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setBorder(StyleUtil.nextBorderStyle(horizontalRule.getBorder()));
            }
        });
        
        controlsColumn.addButton("Clear Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setBorder(null);
            }
        });
        
        
        // Enabled/Disabled look
 
        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Enabled/Disabled");
        controlsColumn.setLayoutData(layoutData);

        controlsColumn.addButton("Toggle Enabled", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean enabled = !horizontalRule.isEnabled();
                horizontalRule.setEnabled(enabled);
            }
        });

        controlsColumn.addButton("Random Disabled Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setDisabledBackground(StyleUtil.randomColor());
            }
        });

        controlsColumn.addButton("Clear Disabled Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setDisabledBackground(null);
            }
        });
        
        controlsColumn.addButton("Random Disabled Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setDisabledBorder(StyleUtil.randomBorder());
            }
        });

        controlsColumn.addButton("Clear Disabled Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horizontalRule.setDisabledBorder(null);
            }
        });

    }

    private Component buildTestingPanel() {
    
        Column testColumn = new Column();
        testColumn.setStyleName("TestColumn");
        testColumn.add(horizontalRule);
        
        return testColumn;
    }

}
