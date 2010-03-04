package org.karora.cooee.testapp.testscreen;

import org.karora.cooee.app.ApplicationInstance;
import org.karora.cooee.app.AccordionPane;
import org.karora.cooee.app.FillImage;
import org.karora.cooee.app.Border;
import org.karora.cooee.app.Button;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.ColorSelect;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.WindowPane;
import org.karora.cooee.app.layout.SplitPaneLayoutData;
import org.karora.cooee.app.layout.AccordionPaneLayoutData;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.event.DocumentEvent;
import org.karora.cooee.app.event.DocumentListener;
import org.karora.cooee.app.event.WindowPaneListener;
import org.karora.cooee.app.event.WindowPaneEvent;
import org.karora.cooee.app.layout.SplitPaneLayoutData;

import org.karora.cooee.app.ActiveTextArea;

import org.karora.cooee.testapp.ButtonColumn;
import org.karora.cooee.testapp.InteractiveApp;
import org.karora.cooee.testapp.StyleUtil;
import org.karora.cooee.testapp.Styles;



// This test component

public class ActiveTextAreaTest extends SplitPane {
    
    /**
     * Writes <code>ActionEvent</code>s to console.
     */
    private ActionListener actionListener = new ActionListener() {

        /**
         * @see org.karora.cooee.app.event.ActionListener#actionPerformed(org.karora.cooee.app.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();                
            ((InteractiveApp) getApplicationInstance()).consoleWrite(action);
        }
    };
    
    /**
     * Writes <code>ActionEvent</code>s to console.
     */
    private DocumentListener documentListener = new DocumentListener() {
        
        /**
         * @see org.karora.cooee.app.event.DocumentListener#documentUpdate(org.karora.cooee.app.event.DocumentEvent)
         */
        public void documentUpdate(DocumentEvent e) {
            ;//((InteractiveApp) getApplicationInstance()).consoleWrite(e.toString());
        }
    };

    final ActiveTextArea activeTextArea = new ActiveTextArea();

    
    public ActiveTextAreaTest() {
        super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));
        
        //activeTextArea.setBorder(new Border(2,Color.LIGHTGRAY, Border.STYLE_INSET));
        
        setStyleName("DefaultResizable");

        SplitPaneLayoutData splitPaneLayoutData;
        
        // Control Groups in the splitpane's left side
        AccordionPane controlGroupsColumn = new AccordionPane();
		controlGroupsColumn.setStyleName("TestControlAccordion"); 
        add(controlGroupsColumn);

        // Build the testing panel in the splitpane's right side
        add(buildTestingPanel());


        // Build the test control buttons as small groups (controlsColumn) 
        //   placed within a larger group (controlGroupsColumn) 
        ButtonColumn controlsColumn;
        AccordionPaneLayoutData layoutData;

                
        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("ActiveTextArea Properties");
        controlsColumn.setLayoutData(layoutData);

        controlsColumn.addButton("MaxLength = 20", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                 activeTextArea.setMaxLength(20);
            }
        });

        controlsColumn.addButton("MaxLength = 60", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                 activeTextArea.setMaxLength(60);
            }
        });

        controlsColumn.addButton("MaxLength = 100", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                 activeTextArea.setMaxLength(100);
            }
        });

        controlsColumn.addButton("MaxLength = -1 (no limit)", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                 activeTextArea.setMaxLength(-1);
            }
        });

        controlsColumn.addButton("Toggle Status Visible", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                boolean enabled = !activeTextArea.isMessageVisible();
                 activeTextArea.setMessageVisible(enabled);
            }
        });



        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Set Text");
        controlsColumn.setLayoutData(layoutData);

        controlsColumn.addButton("Hello, World!", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setText("Hello, World!");
            }
        });

        controlsColumn.addButton("Empty String", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setText("");
            }
        });
        
        controlsColumn.addButton("null", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setText(null);
            }
        });

        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Width");
        controlsColumn.setLayoutData(layoutData);

        controlsColumn.addButton("null", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setWidth(null);
            }
        });

        controlsColumn.addButton("35 em", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setWidth(new Extent(35, Extent.EM));
            }
        });

        controlsColumn.addButton("100%", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setWidth(new Extent(100, Extent.PERCENT));
            }
        });



        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Listeners");
        controlsColumn.setLayoutData(layoutData);

        controlsColumn.addButton("Get Text", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = activeTextArea.getText();
                ((InteractiveApp) getApplicationInstance()).consoleWrite(text);
            }
        });
        

        controlsColumn.addButton("Add ActionListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.addActionListener(actionListener);
            }
        });
        controlsColumn.addButton("Remove ActionListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.removeActionListener(actionListener);
            }
        });
        
/*
        controlsColumn.addButton("Add DocumentListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.getDocument().addDocumentListener(documentListener);
                //passwordField.getDocument().addDocumentListener(documentListener);
                //textArea.getDocument().addDocumentListener(documentListener);
            }
        });
        controlsColumn.addButton("Remove DocumentListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.getDocument().removeDocumentListener(documentListener);
                //passwordField.getDocument().removeDocumentListener(documentListener);
                //textArea.getDocument().removeDocumentListener(documentListener);
            }
        });
*/





/*        
        controlsColumn.addButton("Change Border (All Attributes)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = StyleUtil.randomBorder();
                activeTextArea.setBorder(border);
            }
        });

        controlsColumn.addButton("Change Border Color", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = activeTextArea.getBorder();
                if (border == null) {
                    return;
                }
                border = new Border(border.getSize(), StyleUtil.randomColor(), border.getStyle());
                activeTextArea.setBorder(border);
            }
        });

        controlsColumn.addButton("Change Border Size", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = StyleUtil.nextBorderSize(activeTextArea.getBorder());
                if (border == null) {
                    return;
                }
                activeTextArea.setBorder(border);
            }
        });

        controlsColumn.addButton("Change Border Style", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = StyleUtil.nextBorderStyle(activeTextArea.getBorder());
                if (border == null) {
                    return;
                }
                activeTextArea.setBorder(border);
            }
        });
*/


/*
        controlsColumn.addButton("Toggle Background Image", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FillImage backgroundImage = activeTextArea.getBackgroundImage();
                if (backgroundImage == null) {
                    activeTextArea.setBackgroundImage(Styles.BG_SHADOW_LIGHT_BLUE);
                } else {
                    activeTextArea.setBackgroundImage(null);
                }
            }
        });
*/

        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Foreground & Background");
        controlsColumn.setLayoutData(layoutData);

        controlsColumn.addButton("Random Foreground", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = StyleUtil.randomColor();
                activeTextArea.setForeground(color);
            }
        });
        
        
        
        controlsColumn.addButton("Reset Foreground", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setForeground(null);
            }
        });

        controlsColumn.addButton("Random Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = StyleUtil.randomColor();
                activeTextArea.setBackground(color);
            }
        });

        controlsColumn.addButton("Reset Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setBackground(null);
            }
        });


        
        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Focus Border & Background");
        controlsColumn.setLayoutData(layoutData);
        
        controlsColumn.addButton("Red TL/RB Focus Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setTopLeftFocusBorder(new Border(2, new Color(188,0,0), Border.STYLE_SOLID));
                activeTextArea.setRightBottomFocusBorder(new Border(2, new Color(255,101,92), Border.STYLE_SOLID));
                activeTextArea.setFocusBackground(Color.WHITE);
            }
        });
              
         controlsColumn.addButton("Green Inset Focus Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setFocusBorder(new Border(2, Color.GREEN, Border.STYLE_INSET));
                activeTextArea.setFocusBackground(Color.WHITE);
            }
        });
       

        controlsColumn.addButton("Random Single Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setFocusBorder(StyleUtil.randomBorder());
            }
        });
        
        controlsColumn.addButton("Random Top/Left Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setTopLeftFocusBorder(StyleUtil.randomBorder());
            }
        });
        
        controlsColumn.addButton("Random Right/Bottom Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setRightBottomFocusBorder(StyleUtil.randomBorder());
            }
        });
                
        controlsColumn.addButton("Random Four Borders", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setFocusBorder(StyleUtil.randomBorder(),
                                              StyleUtil.randomBorder(),
                                              StyleUtil.randomBorder(),
                                              StyleUtil.randomBorder());
                activeTextArea.setFocusBackground(Color.WHITE);
            }
        });
                
        controlsColumn.addButton("Set Focus Border => NULL", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setFocusBorder(null);
            }
        });


        controlsColumn.addButton("Random Focus Foreground", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = StyleUtil.randomColor();
                activeTextArea.setFocusForeground(color);
            }
        });

        controlsColumn.addButton("Set Focus Foreground => NULL", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setFocusForeground(null);
            }
        });


        controlsColumn.addButton("Random Focus Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = StyleUtil.randomColor();
                activeTextArea.setFocusBackground(color);
            }
        });

        controlsColumn.addButton("Set Focus Background => NULL", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setFocusBackground(null);
            }
        });



        
        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Border");
        controlsColumn.setLayoutData(layoutData);
        
        controlsColumn.addButton("Increase Thickness", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = activeTextArea.getBorder();
                
                if (border == null) 
                    border = new Border(1, Color.LIGHTGRAY, Border.STYLE_INSET);
                    
                int size = border.getSize().getValue();
                size = (size > 10) ? 1 : size+1;

                Button button = (Button) e.getSource();
                button.setText("Thickness = " + size + "px");
                
                activeTextArea.setBorder(new Border(size, border.getColor(), border.getStyle()));
            }
        });

/*
        controlsColumn.addButton("Random  Color", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = activeTextArea.getBorder();
                
                if (border == null) 
                    border = new Border(1, Color.LIGHTGRAY, Border.STYLE_INSET);
                    
                activeTextArea.setBorder(new Border(border.getSize(), StyleUtil.randomColor(), Border.STYLE_INSET));
            }
        });
*/
        controlsColumn.addButton("Select  Color", new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Border border = activeTextArea.getBorder();
                
                
                final Color color = (border == null) ? Color.LIGHTGRAY : border.getColor();
                final Extent thickness = (border == null) ? new Extent(1) : border.getSize();
                final int style = (border == null) ? Border.STYLE_INSET : border.getStyle();

                WindowPane windowPane = new WindowPane("Border Select", new Extent(235), new Extent(255));
                windowPane.setModal(true);
                
                windowPane.setPositionX(new Extent(262));
                windowPane.setPositionY(new Extent(277));
                windowPane.setInsets(new Insets(1, 1));
                
                final ColorSelect colorSelect = new ColorSelect(color);
                colorSelect.setDisplayValue(true);
                
                windowPane.add(colorSelect);
                ApplicationInstance.getActive().getDefaultWindow().getContent().add(windowPane);
                
                
                // Window Pane closing Listener
                windowPane.addWindowPaneListener(new WindowPaneListener() {
                    public void windowPaneClosing(WindowPaneEvent evt) {
                        Color newColor = colorSelect.getColor();
                        Button button = (Button) e.getSource();
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("#");
                        buffer.append(Integer.toHexString(newColor.getRed()));
                        buffer.append(Integer.toHexString(newColor.getGreen()));
                        buffer.append(Integer.toHexString(newColor.getBlue()));
                        
                        button.setText("Selected Color = " + buffer.toString());

                        activeTextArea.setBorder(new Border(thickness, newColor, style));
                    }
                });

                    
            }
        });

        controlsColumn.addButton("Select  Style", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            
                Border border = activeTextArea.getBorder();
                
                
                final Color color = (border == null) ? Color.LIGHTGRAY : border.getColor();
                final Extent thickness = (border == null) ? new Extent(1) : border.getSize();
                int style = (border == null) ? Border.STYLE_INSET : border.getStyle();

                style = (style > 7) ? 0 : style+1;
                
                Button button = (Button) e.getSource();
                
                switch (style) {
                
                    case Border.STYLE_NONE:   button.setText("Selected Style - None"); break;
                    case Border.STYLE_SOLID:  button.setText("Selected Style - Solid"); break;
                    case Border.STYLE_INSET:  button.setText("Selected Style - Inset"); break;
                    case Border.STYLE_OUTSET: button.setText("Selected Style - Outset"); break;
                    case Border.STYLE_GROOVE: button.setText("Selected Style - Groove"); break;
                    case Border.STYLE_RIDGE:  button.setText("Selected Style - Ridge"); break;
                    case Border.STYLE_DOUBLE: button.setText("Selected Style - Double"); break;
                    case Border.STYLE_DOTTED: button.setText("Selected Style - Dotted"); break;
                    case Border.STYLE_DASHED: button.setText("Selected Style - Dashed"); break;
                }
 
                activeTextArea.setBorder(new Border(thickness, color, style));

            }
        });


        controlsColumn.addButton("Set Border = NULL", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setBorder(null);
            }
        });


/*
        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        controlsColumn.add(new Label("Border"));
        controlsColumn.addButton("Change Disabled Border (All Attributes)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = StyleUtil.randomBorder();
                activeTextArea.setDisabledBorder(border);
            }
        });

        controlsColumn.addButton("Change Disabled Border Color", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = activeTextArea.getDisabledBorder();
                if (border == null) {
                    return;
                }
                border = new Border(border.getSize(), StyleUtil.randomColor(), border.getStyle());
                activeTextArea.setDisabledBorder(border);
            }
        });
        
        controlsColumn.addButton("Change Disabled Border Size", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = StyleUtil.nextBorderSize(activeTextArea.getDisabledBorder());
                if (border == null) {
                    return;
                }
                activeTextArea.setDisabledBorder(border);
            }
        });
        
        controlsColumn.addButton("Change Disabled Border Style", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = StyleUtil.nextBorderStyle(activeTextArea.getDisabledBorder());
                if (border == null) {
                    return;
                }
                activeTextArea.setDisabledBorder(border);
            }
        });
*/

        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Enabled/Disabled");
        controlsColumn.setLayoutData(layoutData);

        controlsColumn.addButton("Toggle Enabled", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean enabled = !activeTextArea.isEnabled();
                activeTextArea.setEnabled(enabled);
            }
        });
/*
        controlsColumn.addButton("Toggle Disabled Background Image", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FillImage backgroundImage = activeTextArea.getDisabledBackgroundImage();
                if (backgroundImage == null) {
                    activeTextArea.setDisabledBackgroundImage(Styles.BG_SHADOW_LIGHT_BLUE);
                } else {
                    activeTextArea.setDisabledBackgroundImage(null);
                }
            }
        });
*/


/*
        controlsColumn.addButton("Set Disabled Foreground", new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color color = StyleUtil.randomColor();
                activeTextArea.setDisabledForeground(color);
            }
        });

        controlsColumn.addButton("Clear Disabled Foreground", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setDisabledForeground(null);
            }
        });
        
        controlsColumn.addButton("Set Disabled Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = StyleUtil.randomColor();
                activeTextArea.setDisabledBackground(color);
            }
        });

        controlsColumn.addButton("Clear Disabled Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setDisabledBackground(null);
            }
        });
*/

/*
        controlsColumn.addButton("Insets -> null", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setInsets(null);
            }
        });

        controlsColumn.addButton("Insets -> 0px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setInsets(new Insets(0));
            }
        });

        controlsColumn.addButton("Insets -> 5px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setInsets(new Insets(5));
            }
        });

        controlsColumn.addButton("Insets -> 10/20/30/40px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextArea.setInsets(new Insets(10, 20, 30, 40));
            }
        });
*/

        controlsColumn.addButton("Toggle ToolTip Enabled", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (activeTextArea.getToolTipText() == null) {
                    activeTextArea.setToolTipText("Active text area");
                } else {
                    activeTextArea.setToolTipText(null);
                }
            }
        });


    }

    private Component buildTestingPanel() {
    
        Column testColumn = new Column();
        testColumn.setStyleName("TestColumn");
        
        activeTextArea.setWidth(new Extent(40, Extent.EM));
        activeTextArea.setHeight(new Extent(20, Extent.EM));

        testColumn.add(activeTextArea);
        
        return testColumn;
    }


}
