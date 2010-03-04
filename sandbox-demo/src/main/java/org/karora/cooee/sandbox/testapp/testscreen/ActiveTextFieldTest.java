package org.karora.cooee.sandbox.testapp.testscreen;

import org.karora.cooee.app.ApplicationInstance;
import org.karora.cooee.app.AccordionPane;
import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Border;
import org.karora.cooee.app.Button;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.ColorSelect;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.Row;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.TextField;
import org.karora.cooee.app.WindowPane;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.event.DocumentEvent;
import org.karora.cooee.app.event.DocumentListener;
import org.karora.cooee.app.event.WindowPaneListener;
import org.karora.cooee.app.event.WindowPaneEvent;
import org.karora.cooee.app.layout.SplitPaneLayoutData;
import org.karora.cooee.app.layout.AccordionPaneLayoutData;
import org.karora.cooee.app.text.Document;
import org.karora.cooee.app.text.TextComponent;

import org.karora.cooee.ng.LabelEx;

import org.karora.cooee.webcontainer.propertyrender.BorderRender;
import org.karora.cooee.webcontainer.propertyrender.ColorRender;

import org.karora.cooee.sandbox.informagen.app.ActiveTextField;
import org.karora.cooee.sandbox.testapp.ButtonColumn;
import org.karora.cooee.sandbox.testapp.InteractiveApp;
import org.karora.cooee.sandbox.testapp.StyleUtil;


abstract public class ActiveTextFieldTest extends SplitPane {

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
			((InteractiveApp) getApplicationInstance()).consoleWrite(activeTextField.getText());
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
			((InteractiveApp) getApplicationInstance()).consoleWrite(e
					.toString());
			((InteractiveApp) getApplicationInstance())
					.consoleWrite(((Document) e.getSource()).getText());
		}
	};

	protected ActiveTextField activeTextField;
	protected LabelEx styleSheetLbl;
    protected AccordionPane controlGroupsColumn;

    protected WindowPane colorSelectWindow;
    protected ColorSelect colorSelect;

    final TextField textField = new TextField();    

	public ActiveTextFieldTest(ActiveTextField activeTextField) {
		super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));

		this.activeTextField = activeTextField;

		setStyleName("DefaultResizable");

        colorSelectWindow = new WindowPane("", new Extent(235), new Extent(255));
        colorSelectWindow.setModal(true);
        colorSelectWindow.setDefaultCloseOperation(WindowPane.HIDE_ON_CLOSE);
        
        colorSelectWindow.setPositionX(new Extent(262));
        colorSelectWindow.setPositionY(new Extent(277));
        colorSelectWindow.setInsets(new Insets(1, 1));
        
        colorSelect = new ColorSelect();
        colorSelect.setDisplayValue(true);
        
        colorSelectWindow.add(colorSelect);
        
        colorSelectWindow.setVisible(false);

        ApplicationInstance.getActive().getDefaultWindow().getContent().add(colorSelectWindow);


		// Build Test Control Column as an AccordionPane, subclass will fill it
		controlGroupsColumn = new AccordionPane();
		add(controlGroupsColumn);

		// Build the testing panel in the splitpane's right side
		add(buildTestingPanel());

        writeStyleSheet();
	}

	protected void fillTestControlsColumn() {

		// Control Groups in the splitpane's left side

		// Build the test control buttons as small groups (controlsColumn)
		// placed within a larger group (controlGroupsColumn)
		ButtonColumn controlsColumn;
        AccordionPaneLayoutData layoutData;

                
        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Set/Get Text");
        controlsColumn.setLayoutData(layoutData);
        

		controlsColumn.addButton("Text -> 10", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                boolean enabled = !activeTextField.isMessageVisible();
				activeTextField.setText("10");
				ApplicationInstance.getActive().setFocusedComponent(activeTextField);
			}
		});

		controlsColumn.addButton("Text -> 10.0", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                boolean enabled = !activeTextField.isMessageVisible();
				activeTextField.setText("10.0");
				ApplicationInstance.getActive().setFocusedComponent(activeTextField);
			}
		});

		controlsColumn.addButton("Empty String", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setText("");
				ApplicationInstance.getActive().setFocusedComponent(activeTextField);
			}
		});

		controlsColumn.addButton("null", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setText(null);
				ApplicationInstance.getActive().setFocusedComponent(activeTextField);
			}
		});

        controlsColumn.addButton("Get Text", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = activeTextField.getText();
                ((InteractiveApp) getApplicationInstance()).consoleWrite(text);
            }
        });

        controlsColumn.addButton("isValid()", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String valid = Boolean.toString(activeTextField.isValid());
            ((InteractiveApp) getApplicationInstance()).consoleWrite(valid);
            }
        });



        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Icons/Messages");
        controlsColumn.setLayoutData(layoutData);
        

        controlsColumn.addButton("Toggle Message Visible", new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						boolean enabled = !activeTextField.isMessageVisible();
						activeTextField.setMessageVisible(enabled);
                        writeStyleSheet();
					}
				});

		controlsColumn.addButton("Toggle Icon Visible", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean enabled = !activeTextField.isIconVisible();
				activeTextField.setIconVisible(enabled);
                writeStyleSheet();
			}
		});

        controlsColumn.addButton("Toggle Message Always Visible", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean enabled = !activeTextField.isMessageAlwaysVisible();
						activeTextField.setMessageAlwaysVisible(enabled);
                        writeStyleSheet();
					}
				});

        controlsColumn.addButton("Toggle Icon Always Visible", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean enabled = !activeTextField.isIconAlwaysVisible();
						activeTextField.setIconAlwaysVisible(enabled);
                        writeStyleSheet();
					}
				});

		controlsColumn.addButton("Toggle Required", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean required = !activeTextField.isRequired();
				activeTextField.setRequired(required);
                writeStyleSheet();
			}
		});

        controlsColumn.addButton("Toggle Required Message", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String requiredMsg = activeTextField.getRequiredMessage();
                String newMsg = (requiredMsg == null) ? "Input is absolutely required!!!!" : null;
						activeTextField.setRequiredMessage(newMsg);
                        writeStyleSheet();
					}
				});



        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Width");
        controlsColumn.setLayoutData(layoutData);

		controlsColumn.addButton("Width -> null", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setWidth(null);
                writeStyleSheet();
			}
		});

		controlsColumn.addButton("Width -> 200px", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setWidth(new Extent(200, Extent.PX));
                writeStyleSheet();
			}
		});

		controlsColumn.addButton("Width -> 100%", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setWidth(new Extent(100, Extent.PERCENT));
                writeStyleSheet();
			}
		});



        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Listeners");
        controlsColumn.setLayoutData(layoutData);

		controlsColumn.addButton("Add ActionListener", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.addActionListener(actionListener);
			}
		});

		controlsColumn.addButton("Remove ActionListener", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.removeActionListener(actionListener);
			}
		});

		controlsColumn.addButton("Add DocumentListener", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                activeTextField.getDocument().addDocumentListener(documentListener);
            }
        });
        
        controlsColumn.addButton("Remove DocumentListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.getDocument().removeDocumentListener(documentListener);
            }
        });


        controlsColumn.addButton("Toggle ActionOnChange", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setActionCausedOnChange(!activeTextField.getActionCausedOnChange());
            }
        });


/*
        
        controlsColumn.addButton("Change Border (All Attributes)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = StyleUtil.randomBorder();
                activeTextField.setBorder(border);
            }
        });

        controlsColumn.addButton("Change Border Color", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = activeTextField.getBorder();
                if (border == null) {
                    return;
                }
                border = new Border(border.getSize(), StyleUtil.randomColor(), border.getStyle());
                activeTextField.setBorder(border);
            }
        });

        controlsColumn.addButton("Change Border Size", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = StyleUtil.nextBorderSize(activeTextField.getBorder());
                if (border == null) {
                    return;
                }
                activeTextField.setBorder(border);
            }
        });

        controlsColumn.addButton("Change Border Style", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = StyleUtil.nextBorderStyle(activeTextField.getBorder());
                if (border == null) {
                    return;
                }
                activeTextField.setBorder(border);
            }
        });




        controlsColumn.addButton("Toggle Background Image", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FillImage backgroundImage = activeTextField.getBackgroundImage();
                if (backgroundImage == null) {
                    activeTextField.setBackgroundImage(Styles.BG_SHADOW_LIGHT_BLUE);
                } else {
                    activeTextField.setBackgroundImage(null);
                }
            }
        });



*/
        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Foreground & Background");
        controlsColumn.setLayoutData(layoutData);

		controlsColumn.addButton("Set Foreground", new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Color color = activeTextField.getForeground();
                if (color == null)
                    color = Color.BLACK;
                colorSelect.setColor(color);       
                
                // Window Pane closing Listener
                colorSelectWindow.setTitle("Foreground");
                colorSelectWindow.addWindowPaneListener(new WindowPaneListener() {
                    public void windowPaneClosing(WindowPaneEvent evt) {
                        Color color = colorSelect.getColor();
                        Button button = (Button) e.getSource();
                        button.setText("Foreground = " + ColorRender.renderCssAttributeValue(color));

                        activeTextField.setForeground(color);
                        colorSelectWindow.removeWindowPaneListener(this);
                        writeStyleSheet();
                    }
                });
                
                colorSelectWindow.setVisible(true);
            }
        });

		controlsColumn.addButton("Set Foreground to null", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setForeground(null);
                writeStyleSheet();
			}
		});

		controlsColumn.addButton("Set Background", new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Color color = activeTextField.getBackground();
                if (color == null)
                    color = Color.WHITE;
                colorSelect.setColor(color);       
                
                // Window Pane closing Listener
                colorSelectWindow.setTitle("Background");
                colorSelectWindow.addWindowPaneListener(new WindowPaneListener() {
                    public void windowPaneClosing(WindowPaneEvent evt) {
                        Color color = colorSelect.getColor();
                        Button button = (Button) e.getSource();
                        button.setText("Background = " + ColorRender.renderCssAttributeValue(color));

                        activeTextField.setBackground(color);
                        colorSelectWindow.removeWindowPaneListener(this);
                        writeStyleSheet();
                    }
                });

                colorSelectWindow.setVisible(true);
                    
            }
        });

		controlsColumn.addButton("Set Background to null", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setBackground(null);
                writeStyleSheet();
			}
		});


        
        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Focus Border & Background");
        controlsColumn.setLayoutData(layoutData);
        
        controlsColumn.addButton("Red TL/RB Focus Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setTopLeftFocusBorder(new Border(1, new Color(188,0,0), Border.STYLE_SOLID));
                activeTextField.setRightBottomFocusBorder(new Border(1, new Color(255,101,92), Border.STYLE_SOLID));
                activeTextField.setFocusBackground(Color.WHITE);
            }
        });
              
         controlsColumn.addButton("Green Inset Focus Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setFocusBorder(new Border(1, Color.GREEN, Border.STYLE_INSET));
                activeTextField.setFocusBackground(Color.WHITE);
                writeStyleSheet();
            }
        });
       
       

        controlsColumn.addButton("Set Border Color", new ActionListener() {
             public void actionPerformed(final ActionEvent e) {
                Border border = activeTextField.getFocusBorder();
                
                if (border == null) 
                    border = new Border(1, Color.LIGHTGRAY, Border.STYLE_INSET);
             
             
                colorSelect.setColor(border.getColor());
                
                final Extent thickness = border.getSize();
                
                // Window Pane closing Listener
                colorSelectWindow.setTitle("Border");
                colorSelectWindow.addWindowPaneListener(new WindowPaneListener() {
                    public void windowPaneClosing(WindowPaneEvent evt) {
                        Color color = colorSelect.getColor();
                        Button button = (Button) e.getSource();
                        button.setText("Border Color = " + ColorRender.renderCssAttributeValue(color));
                        activeTextField.setFocusBorder(new Border(thickness, color, Border.STYLE_INSET));
                        colorSelectWindow.removeWindowPaneListener(this);
                        writeStyleSheet();
                    }
                });

                colorSelectWindow.setVisible(true);
            }
        });

        controlsColumn.addButton("Set Border Size", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = activeTextField.getFocusBorder();
                
                int size;
                Color color;
                int style;
                if (border != null) {
                    color = border.getColor();
                    size = border.getSize().getValue();
                    style = border.getStyle();
                } else {
                    color = Color.LIGHTGRAY;
                    size = 0;
                    style = Border.STYLE_INSET;
                }

                // Increment the border thickness   
                size = (size > 10) ? 1 : size+1;
                
                Button button = (Button) e.getSource();
                button.setText("Border Size = " + size + "px");
                
                activeTextField.setFocusBorder(new Border(size, color, style));
                writeStyleSheet();
            }
        });
        
        controlsColumn.addButton("Random Top/Left Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setTopLeftFocusBorder(StyleUtil.randomBorder());
            }
        });
        
        controlsColumn.addButton("Random Right/Bottom Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setRightBottomFocusBorder(StyleUtil.randomBorder());
            }
        });
                
        controlsColumn.addButton("Random Four Borders", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setFocusBorder(StyleUtil.randomBorder(),
                                               StyleUtil.randomBorder(),
                                               StyleUtil.randomBorder(),
                                               StyleUtil.randomBorder());
                activeTextField.setFocusBackground(Color.WHITE);
            }
        });
                
        controlsColumn.addButton("Set Focus Border to NULL", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setFocusBorder(null);
                writeStyleSheet();
            }
        });


        controlsColumn.addButton("Set Focus Foreground", new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Color color = activeTextField.getFocusForeground();
                if (color == null)
                    color = Color.BLACK;
                colorSelect.setColor(color);       
                
                // Window Pane closing Listener
                colorSelectWindow.setTitle("Focus Foreground");
                colorSelectWindow.addWindowPaneListener(new WindowPaneListener() {
                    public void windowPaneClosing(WindowPaneEvent evt) {
                        Color color = colorSelect.getColor();
                        Button button = (Button) e.getSource();
                        button.setText("Focus Foreground = " + ColorRender.renderCssAttributeValue(color));

                        activeTextField.setFocusForeground(color);
                        colorSelectWindow.removeWindowPaneListener(this);
                        writeStyleSheet();
                    }
                });

                colorSelectWindow.setVisible(true);
            }
        });

        controlsColumn.addButton("Set Focus Foreground to NULL", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setFocusForeground(null);
                writeStyleSheet();
            }
        });

        controlsColumn.addButton("Set Focus Background", new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Color color = activeTextField.getFocusBackground();
                if (color == null)
                    color = Color.WHITE;
                colorSelect.setColor(color);       
                
                // Window Pane closing Listener
                colorSelectWindow.setTitle("Focus Background");
                colorSelectWindow.addWindowPaneListener(new WindowPaneListener() {
                    public void windowPaneClosing(WindowPaneEvent evt) {
                        Color color = colorSelect.getColor();
                        Button button = (Button) e.getSource();
                        button.setText("Focus Background = " + ColorRender.renderCssAttributeValue(color));

                        activeTextField.setFocusBackground(color);
                        colorSelectWindow.removeWindowPaneListener(this);
                        writeStyleSheet();
                    }
                });

                colorSelectWindow.setVisible(true);
                    
            }
        });

        controlsColumn.addButton("Set Focus Background to NULL", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setFocusBackground(null);
            }
        });





        
        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Border");
        controlsColumn.setLayoutData(layoutData);
        
        controlsColumn.addButton("Increase Size", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = activeTextField.getBorder();
                
                if (border == null) 
                    border = new Border(1, Color.LIGHTGRAY, Border.STYLE_INSET);
                    
                int size = border.getSize().getValue();
                size = (size > 10) ? 1 : size+1;
                
                activeTextField.setBorder(new Border(size, border.getColor(), border.getStyle()));
                writeStyleSheet();
            }
        });

        controlsColumn.addButton("Set Color", new ActionListener() {
             public void actionPerformed(final ActionEvent e) {
                Border border = activeTextField.getBorder();
                
                if (border == null) 
                    border = new Border(1, Color.LIGHTGRAY, Border.STYLE_INSET);
             
             
                colorSelect.setColor(border.getColor());
                
                final Extent thickness = border.getSize();
                
                // Window Pane closing Listener
                colorSelectWindow.setTitle("Border");
                colorSelectWindow.addWindowPaneListener(new WindowPaneListener() {
                    public void windowPaneClosing(WindowPaneEvent evt) {
                        Color color = colorSelect.getColor();
                        Button button = (Button) e.getSource();
                        button.setText("Border Color = " + ColorRender.renderCssAttributeValue(color));
                        activeTextField.setBorder(new Border(thickness, color, Border.STYLE_INSET));
                        colorSelectWindow.removeWindowPaneListener(this);
                        writeStyleSheet();
                    }
                });

                colorSelectWindow.setVisible(true);
                    
            }
        });


        controlsColumn.addButton("Reset Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setBorder(null);
                writeStyleSheet();
            }
        });





        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Error Foreground & Background");
        controlsColumn.setLayoutData(layoutData);

		controlsColumn.addButton("Set Error Foreground", new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Color color = activeTextField.getErrorForeground();
                if (color == null)
                    color = Color.BLACK;
                colorSelect.setColor(color);       
                
                // Window Pane closing Listener
                colorSelectWindow.setTitle("Error Foreground");
                colorSelectWindow.addWindowPaneListener(new WindowPaneListener() {
                    public void windowPaneClosing(WindowPaneEvent evt) {
                        Color color = colorSelect.getColor();
                        Button button = (Button) e.getSource();
                        button.setText("Error Foreground = " + ColorRender.renderCssAttributeValue(color));

                        activeTextField.setErrorForeground(color);
                        colorSelectWindow.removeWindowPaneListener(this);
                        writeStyleSheet();
                    }
                });
                
                colorSelectWindow.setVisible(true);
            }
        });

		controlsColumn.addButton("Set Error Foreground = null", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setErrorForeground(null);
                writeStyleSheet();
			}
		});


		controlsColumn.addButton("Set Error Background", new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Color color = activeTextField.getErrorBackground();
                if (color == null)
                    color = Color.YELLOW;
                colorSelect.setColor(color);       
                
                // Window Pane closing Listener
                colorSelectWindow.setTitle("Error Background");
                colorSelectWindow.addWindowPaneListener(new WindowPaneListener() {
                    public void windowPaneClosing(WindowPaneEvent evt) {
                        Color color = colorSelect.getColor();
                        Button button = (Button) e.getSource();
                        button.setText("Error Background = " + ColorRender.renderCssAttributeValue(color));

                        activeTextField.setErrorBackground(color);
                        colorSelectWindow.removeWindowPaneListener(this);
                        writeStyleSheet();
                    }
                });
                
                colorSelectWindow.setVisible(true);
            }
        });

		controlsColumn.addButton("Set Error Background = null", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setErrorBackground(null);
                writeStyleSheet();
			}
		});



        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Disabled Appearance");
        controlsColumn.setLayoutData(layoutData);

        controlsColumn.addButton("Disable Component", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean enabled = !activeTextField.isEnabled();
                
                Button button = (Button) e.getSource();
                button.setText(enabled ? "Disable Component" : "Enable Component");

                activeTextField.setEnabled(enabled);
            }
        });

/*        
        controlsColumn.addButton("Change Disabled Border (All Attributes)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = StyleUtil.randomBorder();
                activeTextField.setDisabledBorder(border);
            }
        });

        controlsColumn.addButton("Change Disabled Border Color", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = activeTextField.getDisabledBorder();
                if (border == null) {
                    return;
                }
                border = new Border(border.getSize(), StyleUtil.randomColor(), border.getStyle());
                activeTextField.setDisabledBorder(border);
            }
        });
        controlsColumn.addButton("Change Disabled Border Size", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = StyleUtil.nextBorderSize(activeTextField.getDisabledBorder());
                if (border == null) {
                    return;
                }
                activeTextField.setDisabledBorder(border);
            }
        });
        controlsColumn.addButton("Change Disabled Border Style", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = StyleUtil.nextBorderStyle(activeTextField.getDisabledBorder());
                if (border == null) {
                    return;
                }
                activeTextField.setDisabledBorder(border);
            }
        });

        controlsColumn.addButton("Toggle Disabled Background Image", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FillImage backgroundImage = activeTextField.getDisabledBackgroundImage();
                if (backgroundImage == null) {
                    activeTextField.setDisabledBackgroundImage(Styles.BG_SHADOW_LIGHT_BLUE);
                } else {
                    activeTextField.setDisabledBackgroundImage(null);
                }
            }
        });
*/
        controlsColumn.addButton("Set Disabled Foreground", new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Color color = activeTextField.getDisabledForeground();
                if (color == null)
                    color = Color.DARKGRAY;
                colorSelect.setColor(color);       
                
                // Window Pane closing Listener
                colorSelectWindow.setTitle("Disabled Foreground");
                colorSelectWindow.addWindowPaneListener(new WindowPaneListener() {
                    public void windowPaneClosing(WindowPaneEvent evt) {
                        Color color = colorSelect.getColor();
                        Button button = (Button) e.getSource();
                        button.setText("Disabled Foreground = " + ColorRender.renderCssAttributeValue(color));
                        activeTextField.setDisabledForeground(color);
                        colorSelectWindow.removeWindowPaneListener(this);
                        writeStyleSheet();
                    }
                });
                
                colorSelectWindow.setVisible(true);
            }
        });


        controlsColumn.addButton("Disabled Foreground to null", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setDisabledForeground(null);
                writeStyleSheet();
            }
        });
        
//         controlsColumn.addButton("Set Disabled Background", new ActionListener() {
//             public void actionPerformed(ActionEvent e) {
//                 Color color = StyleUtil.randomColor();
//                 activeTextField.setDisabledBackground(color);
//             }
//         });

        controlsColumn.addButton("Set Disabled Background", new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                colorSelect.setColor(activeTextField.getDisabledBackground());       
                Color color = activeTextField.getDisabledBackground();
                if (color == null)
                    color = Color.LIGHTGRAY;
                colorSelect.setColor(color);       
                
                // Window Pane closing Listener
                colorSelectWindow.setTitle("Disable Background");
                colorSelectWindow.addWindowPaneListener(new WindowPaneListener() {
                    public void windowPaneClosing(WindowPaneEvent evt) {
                        Color color = colorSelect.getColor();
                        Button button = (Button) e.getSource();
                        button.setText("Disabled Background = " + ColorRender.renderCssAttributeValue(color));

                        activeTextField.setDisabledBackground(color);
                        colorSelectWindow.removeWindowPaneListener(this);
                        writeStyleSheet();
                    }
                });

                colorSelectWindow.setVisible(true);
            }
        });



        controlsColumn.addButton("Disabled Background to null", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setDisabledBackground(null);
                writeStyleSheet();
            }
        });

/*
        controlsColumn.addButton("Insets -> null", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setInsets(null);
            }
        });

        controlsColumn.addButton("Insets -> 0px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setInsets(new Insets(0));
            }
        });

        controlsColumn.addButton("Insets -> 5px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setInsets(new Insets(5));
            }
        });

        controlsColumn.addButton("Insets -> 10/20/30/40px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setInsets(new Insets(10, 20, 30, 40));
            }
        });
		 */

		// Icon Position

        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Icon Position");
        controlsColumn.setLayoutData(layoutData);

		controlsColumn.addButton("Default", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setIconPosition(null);
                writeStyleSheet();
			}
		});
		controlsColumn.addButton("Top", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setIconPosition(Alignment.ALIGN_TOP);
                writeStyleSheet();
			}
		});
		controlsColumn.addButton("Bottom", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setIconPosition(Alignment.ALIGN_BOTTOM);
                writeStyleSheet();
			}
		});
		controlsColumn.addButton("Left", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setIconPosition(Alignment.ALIGN_LEFT);
                writeStyleSheet();
			}
		});
		controlsColumn.addButton("Right", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setIconPosition(Alignment.ALIGN_RIGHT);
                writeStyleSheet();
			}
		});
		controlsColumn.addButton("Leading", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                activeTextField.setIconPosition(new Alignment(Alignment.LEADING, Alignment.DEFAULT));
                writeStyleSheet();
            }
        });
        controlsColumn.addButton("Trailing", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setIconPosition(new Alignment(Alignment.TRAILING, Alignment.DEFAULT));
                writeStyleSheet();
			}
		});

		// Message Position

		controlsColumn = new ButtonColumn();
		controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Status Message Position");
        controlsColumn.setLayoutData(layoutData);

		controlsColumn.addButton("Default", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setMessagePosition(null);
                writeStyleSheet();
			}
		});
		controlsColumn.addButton("Top", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setMessagePosition(Alignment.ALIGN_TOP);
                writeStyleSheet();
			}
		});
		controlsColumn.addButton("Bottom", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setMessagePosition(Alignment.ALIGN_BOTTOM);
                writeStyleSheet();
			}
		});
		controlsColumn.addButton("Left", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setMessagePosition(Alignment.ALIGN_LEFT);
                writeStyleSheet();
			}
		});
		controlsColumn.addButton("Right", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeTextField.setMessagePosition(Alignment.ALIGN_RIGHT);
                writeStyleSheet();
			}
		});
		controlsColumn.addButton("Leading", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                activeTextField.setMessagePosition(new Alignment(Alignment.LEADING, Alignment.DEFAULT));
                writeStyleSheet();
            }
        });
        controlsColumn.addButton("Trailing", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeTextField.setMessagePosition(new Alignment(Alignment.TRAILING, Alignment.DEFAULT));
                writeStyleSheet();
            }
        });



        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("ToolTip");
        controlsColumn.setLayoutData(layoutData);
        
        controlsColumn.addButton("Toggle ToolTip Enabled", new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if (activeTextField.getToolTipText() == null) {
                    activeTextField.setToolTipText("Integer TextField ");
						} else {
							activeTextField.setToolTipText(null);
						}
					}
				});

	}



	private Component buildTestingPanel() {

		Column testColumn = new Column();
		testColumn.setStyleName("TestColumn");
		testColumn.add(activeTextField);
		
		Row row = new Row();
		row.setStyleName("None");
		row.setCellSpacing(new Extent(5));
		row.add(textField);
		row.add(new Label("TextField component, for comparison"));
		testColumn.add(row);
		
		
		styleSheetLbl = new LabelEx();
		styleSheetLbl.setStyleName("StyleSheetLbl");
		styleSheetLbl.setIntepretNewlines(true);
		
		testColumn.add(styleSheetLbl);

		return testColumn;
	}


    //====  Write Stylesheet - refactor into seperate class and a DOM instead of StringBuffer


    protected void writeStyleSheet() {
    
        StringBuffer buffer = new StringBuffer();
        buffer.append("<!-- Copy and Paste into StyleSheet (incomplete) -->").append("\n");
        buffer.append("<style name=\"Default\" ");
        buffer.append("type=\"");
        buffer.append(activeTextField.getClass().getName());
        buffer.append("\">").append("\n");
        buffer.append("  <properties>").append("\n");
 
        writeExtent(buffer, activeTextField, TextComponent.PROPERTY_WIDTH);
 
        writeBoolean(buffer, activeTextField, ActiveTextField.PROPERTY_ICON_VISIBLE);
        writeBoolean(buffer, activeTextField, ActiveTextField.PROPERTY_ICON_ALWAYS_VISIBLE);
        writeBoolean(buffer, activeTextField, ActiveTextField.PROPERTY_MSG_VISIBLE);
        writeBoolean(buffer, activeTextField, ActiveTextField.PROPERTY_MSG_ALWAYS_VISIBLE);
        writeBoolean(buffer, activeTextField, ActiveTextField.PROPERTY_REQUIRED);
        
        writeAlignment(buffer, activeTextField, ActiveTextField.PROPERTY_MSG_POSITION);
        writeAlignment(buffer, activeTextField, ActiveTextField.PROPERTY_ICON_POSITION);
             
        writeColorProperty(buffer, activeTextField, ActiveTextField.PROPERTY_FOREGROUND);
        writeColorProperty(buffer, activeTextField, ActiveTextField.PROPERTY_BACKGROUND);
        
        writeColorProperty(buffer, activeTextField, ActiveTextField.PROPERTY_FOCUS_FOREGROUND);
        writeColorProperty(buffer, activeTextField, ActiveTextField.PROPERTY_FOCUS_BACKGROUND);
        
        writeColorProperty(buffer, activeTextField, ActiveTextField.PROPERTY_ERROR_FOREGROUND);
        writeColorProperty(buffer, activeTextField, ActiveTextField.PROPERTY_ERROR_BACKGROUND);
        
        writeColorProperty(buffer, activeTextField, ActiveTextField.PROPERTY_DISABLED_FOREGROUND);
        writeColorProperty(buffer, activeTextField, ActiveTextField.PROPERTY_DISABLED_BACKGROUND);
        
        writeBorderProperty(buffer, activeTextField, ActiveTextField.PROPERTY_BORDER);      
        writeBorderProperty(buffer, activeTextField, ActiveTextField.PROPERTY_FOCUS_BORDER);      
        
        buffer.append("</properties>").append("\n");
        buffer.append("</style>");

        styleSheetLbl.setText(buffer.toString());
    }


    private void writeAlignment(StringBuffer buffer, Component component, String propertyName) {

        Alignment alignment = (Alignment)component.getProperty(propertyName);

        if(alignment != null) {
            buffer.append("<property name=\"");
            buffer.append(propertyName);
            buffer.append("\">").append("\n");
            buffer.append("<alignment");
            
            int horizontal = alignment.getHorizontal();
            int vertical = alignment.getVertical();
            
            if (horizontal > 0) {
                buffer.append(" horizontal=\"");
                switch (horizontal) {
                    case Alignment.LEADING : buffer.append("leading"); break;
                    case Alignment.LEFT : buffer.append("left"); break;
                    case Alignment.CENTER : buffer.append("center"); break;
                    case Alignment.RIGHT : buffer.append("right"); break;
                    case Alignment.TRAILING : buffer.append("trailing"); break;
                }            
                buffer.append("\"");
            }
 
            if (vertical > 0) {
                buffer.append(" vertical=\"");
                switch (vertical) {
                    case Alignment.TOP : buffer.append("top"); break;
                    case Alignment.BOTTOM : buffer.append("bottom"); break;
                    case Alignment.CENTER : buffer.append("center"); break;
                }            
                buffer.append("\"");
            }
  
            buffer.append("/>").append("\n");
            buffer.append("</property>").append("\n");
        }
    }
    
    
    private void writeBoolean(StringBuffer buffer, Component component, String propertyName) {
    
        Boolean value = (Boolean)component.getProperty(propertyName);
        
        if(value != null) {
            buffer.append("<property name=\"");
            buffer.append(propertyName);
            buffer.append("\"");
            buffer.append(" value=\"").append(value.toString()).append("\"");
            buffer.append("/>").append("\n");
        }
    }


    private void writeBorderProperty(StringBuffer buffer, Component component, String propertyName) {
    
        Border border = (Border)component.getProperty(propertyName);

        if(border != null) {
            buffer.append("<property name=\"");
            buffer.append(propertyName);
            buffer.append("\">").append("\n");
            buffer.append("    <border");
            buffer.append(" size=\"").append(border.getSize().getValue()).append("px\"");
            buffer.append(" style=\"").append(BorderRender.getStyleValue(border.getStyle())).append("\"");
            buffer.append(" color=\"").append(ColorRender.renderCssAttributeValue(border.getColor())).append("\"/>").append("\n");
            buffer.append("</property>").append("\n");
        }
    
    }


    private void writeColorProperty(StringBuffer buffer, Component component, String propertyName) {
    
        Color color = (Color)component.getProperty(propertyName);
    
        if(color != null) {
            buffer.append("<property name=\"");
            buffer.append(propertyName);
            buffer.append("\" value=\"");
            buffer.append(ColorRender.renderCssAttributeValue(color));
            buffer.append("\"/>").append("\n");
        }
    
    }

    private void writeExtent(StringBuffer buffer, Component component, String propertyName) {
    
        Extent extent = (Extent)component.getProperty(propertyName);

        if(extent != null) {
            buffer.append("<property name=\"");
            buffer.append(propertyName);
            buffer.append("\"");
            buffer.append(" value=\"").append(extent.getValue());
            
            switch (extent.getUnits()) {
                case Extent.CM : buffer.append("cm"); break;
                case Extent.EM : buffer.append("em"); break;
                case Extent.EX : buffer.append("ex"); break;
                case Extent.IN : buffer.append("in"); break;
                case Extent.MM : buffer.append("mm"); break;
                case Extent.PC : buffer.append("pc"); break;
                case Extent.PERCENT : buffer.append("%"); break;
                case Extent.PT : buffer.append("pt"); break;
                case Extent.PX : buffer.append("px"); break;
            
            }
            
            
            buffer.append("\"");
            buffer.append("/>").append("\n");
        }
    
    }

}
