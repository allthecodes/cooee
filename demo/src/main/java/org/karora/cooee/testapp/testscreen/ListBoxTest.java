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

package org.karora.cooee.testapp.testscreen;

import org.karora.cooee.testapp.ButtonColumn;
import org.karora.cooee.testapp.InteractiveApp;
import org.karora.cooee.testapp.StyleUtil;

import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Font;
import org.karora.cooee.app.Grid;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.ListBox;
import org.karora.cooee.app.SelectField;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.event.ChangeEvent;
import org.karora.cooee.app.event.ChangeListener;
import org.karora.cooee.app.event.ListDataEvent;
import org.karora.cooee.app.event.ListDataListener;
import org.karora.cooee.app.layout.SplitPaneLayoutData;
import org.karora.cooee.app.list.AbstractListComponent;
import org.karora.cooee.app.list.DefaultListModel;
import org.karora.cooee.app.list.ListCellRenderer;
import org.karora.cooee.app.list.ListSelectionModel;
import org.karora.cooee.app.list.StyledListCell;

/**
 * An interactive test for <code>ListBox</code>es.
 */
public class ListBoxTest extends SplitPane {
    
    public static final String[] NUMBERS = new String[] { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
                "Nine", "Ten" };

    /**
     * Interface used to apply style information to all test components.
     */
    private interface Applicator {
        
        /**
         * Applies style information.
         * 
         * @param listComponent the target list component.
         */
        public void apply(AbstractListComponent listComponent);
    }
    
    /**
     * Writes <code>ActionEvent</code>s to console.
     */
    private ActionListener actionListener = new ActionListener() {

        /**
         * @see org.karora.cooee.app.event.ActionListener#actionPerformed(org.karora.cooee.app.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            ((InteractiveApp) getApplicationInstance()).consoleWrite(e.toString());
        }
    };
    
    /**
     * Writes <code>ChangeEvent</code>s to console.
     */
    private ChangeListener changeListener = new ChangeListener() {

        /**
         * @see org.karora.cooee.app.event.ChangeListener#stateChanged(org.karora.cooee.app.event.ChangeEvent)
         */
        public void stateChanged(ChangeEvent e) {
            ((InteractiveApp) getApplicationInstance()).consoleWrite(e.toString());
        }
    };
    
    /**
     * Writes <code>ListDataListener</code>s to console.
     */
    private ListDataListener listDataListener = new ListDataListener() {
        
        /**
         * @see org.karora.cooee.app.event.ListDataListener#contentsChanged(org.karora.cooee.app.event.ListDataEvent)
         */
        public void contentsChanged(ListDataEvent e) {
            ((InteractiveApp) getApplicationInstance()).consoleWrite(e.toString());
        }

        /**
         * @see org.karora.cooee.app.event.ListDataListener#intervalAdded(org.karora.cooee.app.event.ListDataEvent)
         */
        public void intervalAdded(ListDataEvent e) {
            ((InteractiveApp) getApplicationInstance()).consoleWrite(e.toString());
        }

        /**
         * @see org.karora.cooee.app.event.ListDataListener#intervalRemoved(org.karora.cooee.app.event.ListDataEvent)
         */
        public void intervalRemoved(ListDataEvent e) {
            ((InteractiveApp) getApplicationInstance()).consoleWrite(e.toString());
        }
    };
    
    private ListCellRenderer evenOddListCellRenderer = new ListCellRenderer(){
    
        private Color foreground1 = new Color(0x007f00);
        private Color background1 = new Color(0xafffaf);
        private Color foreground2 = new Color(0x7f0000);
        private Color background2 = new Color(0xffafaf);
        private Font font1 = new Font(Font.MONOSPACE, Font.BOLD, null);
        
        /**
         * @see org.karora.cooee.app.list.ListCellRenderer#getListCellRendererComponent(org.karora.cooee.app.Component, 
         *      java.lang.Object, int)
         */
        public Object getListCellRendererComponent(Component list, final Object value, final int index) {
            return new StyledListCell() {
            
                public Color getForeground() {
                    return index % 2 == 0 ? foreground1 : foreground2;
                }
            
                public Font getFont() {
                    return index % 2 == 0 ? font1 : null;
                }
            
                public Color getBackground() {
                    return index % 2 == 0 ? background1 : background2;
                }
                
                public String toString() {
                    return value == null ? null : value.toString();
                }
            };
        }
    };
    
    private Column testColumn;
    private ListBox listBox1, listBox2;
    private SelectField selectField1, selectField2;

    public ListBoxTest() {
        super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));
        setStyleName("DefaultResizable");

        SplitPaneLayoutData splitPaneLayoutData;

        ButtonColumn controlsColumn = new ButtonColumn();
        controlsColumn.setStyleName("TestControlsColumn");
        add(controlsColumn);

        testColumn = new Column();
        testColumn.setCellSpacing(new Extent(15));
        splitPaneLayoutData = new SplitPaneLayoutData();
        splitPaneLayoutData.setInsets(new Insets(15));
        testColumn.setLayoutData(splitPaneLayoutData);
        add(testColumn);

        listBox1 = new ListBox(NUMBERS);
        testColumn.add(listBox1);

        selectField1 = new SelectField(NUMBERS);
        testColumn.add(selectField1);
        
        Grid grid = new Grid();
        grid.setBorder(new Border(1, Color.BLACK, Border.STYLE_SOLID));
        testColumn.add(grid);
        
        selectField2 = new SelectField(NUMBERS);
        grid.add(selectField2);
        
        listBox2 = new ListBox(NUMBERS);
        grid.add(listBox2);
        
        controlsColumn.add(new Label("Global"));

        controlsColumn.addButton("Add ActionListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.addActionListener(actionListener);
                    }
                });
            }
        });
        controlsColumn.addButton("Remove ActionListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.removeActionListener(actionListener);
                    }
                });
            }
        });
        controlsColumn.addButton("Add ChangeListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.getSelectionModel().addChangeListener(changeListener);
                    }
                });
            }
        });
        controlsColumn.addButton("Remove ChangeListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.getSelectionModel().removeChangeListener(changeListener);
                    }
                });
            }
        });
        controlsColumn.addButton("Add ListDataListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.getModel().addListDataListener(listDataListener);
                    }
                });
            }
        });
        controlsColumn.addButton("Remove ListDataListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.getModel().removeListDataListener(listDataListener);
                    }
                });
            }
        });
        controlsColumn.addButton("Toggle Enabled State", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setEnabled(!listComponent.isEnabled());
                    }
                });
            }
        });
        controlsColumn.addButton("Set ListCellRenderer", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setCellRenderer(evenOddListCellRenderer);
                    }
                });
            }
        });
        controlsColumn.addButton("Clear ListCellRenderer", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setCellRenderer(AbstractListComponent.DEFAULT_LIST_CELL_RENDERER);
                    }
                });
            }
        });
        controlsColumn.addButton("Set Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Border border = StyleUtil.randomBorder();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setBorder(border);
                    }
                });
            }
        });
        controlsColumn.addButton("Clear Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setBorder(null);
                    }
                });
            }
        });
        controlsColumn.addButton("Set Foreground", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Color color = StyleUtil.randomColor();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setForeground(color);
                    }
                });
            }
        });
        controlsColumn.addButton("Set Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Color color = StyleUtil.randomColor();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setBackground(color);
                    }
                });
            }
        });
        controlsColumn.addButton("Set Font", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Font font = StyleUtil.randomFont();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setFont(font);
                    }
                });
            }
        });
        controlsColumn.addButton("Clear Font", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setFont(null);
                    }
                });
            }
        });
        controlsColumn.addButton("Set Disabled Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Border border = StyleUtil.randomBorder();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setDisabledBorder(border);
                    }
                });
            }
        });
        controlsColumn.addButton("Clear Disabled Border", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setDisabledBorder(null);
                    }
                });
            }
        });
        controlsColumn.addButton("Set Disabled Foreground", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Color color = StyleUtil.randomColor();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setDisabledForeground(color);
                    }
                });
            }
        });
        controlsColumn.addButton("Set Disabled Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Color color = StyleUtil.randomColor();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setDisabledBackground(color);
                    }
                });
            }
        });
        controlsColumn.addButton("Set Disabled Font", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Font font = StyleUtil.randomFont();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setDisabledFont(font);
                    }
                });
            }
        });
        controlsColumn.addButton("Clear Disabled Font", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setDisabledFont(null);
                    }
                });
            }
        });
        controlsColumn.addButton("Toggle ToolTip Text", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        if (listComponent.getToolTipText() == null) {
                            listComponent.setToolTipText("This is a tool tip.");
                        } else {
                            listComponent.setToolTipText(null);
                        }
                    }
                });
            }
        });
        controlsColumn.addButton("Enable Rollover Effects", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setRolloverEnabled(true);
                    }
                });
            }
        });
        controlsColumn.addButton("Disable Rollover Effects", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setRolloverEnabled(false);
                    }
                });
            }
        });
        controlsColumn.addButton("Set Rollover Foreground", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Color color = StyleUtil.randomColor();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setRolloverForeground(color);
                    }
                });
            }
        });
        controlsColumn.addButton("Set Rollover Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Color color = StyleUtil.randomColor();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setRolloverBackground(color);
                    }
                });
            }
        });
        controlsColumn.addButton("Set Rollover Font", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Font font = StyleUtil.randomFont();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setRolloverFont(font);
                    }
                });
            }
        });
        controlsColumn.addButton("Increase Width (15 px)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Extent width = listBox1.getWidth() == null ? new Extent(75) : listBox1.getWidth();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setWidth(Extent.add(width, new Extent(15)));
                    }
                });
            }
        });
        controlsColumn.addButton("Decrease Width (15 px)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Extent width = listBox1.getWidth() == null ? new Extent(75) : listBox1.getWidth();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setWidth(Extent.add(width, new Extent(-15)));
                    }
                });
            }
        });
        controlsColumn.addButton("Increase Height (15 px)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Extent height = listBox1.getHeight() == null ? new Extent(75) : listBox1.getHeight();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setHeight(Extent.add(height, new Extent(15)));
                    }
                });
            }
        });
        controlsColumn.addButton("Decrease Height (15 px)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Extent height = listBox1.getHeight() == null ? new Extent(75) : listBox1.getHeight();
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setHeight(Extent.add(height, new Extent(-15)));
                    }
                });
            }
        });

        controlsColumn.addButton("Select Index 0", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator(){
                    public void apply(AbstractListComponent listComponent) {
                        if (listComponent instanceof ListBox) {
                            ((ListBox) listComponent).setSelectedIndices(new int[] {0});
                        } else if (listComponent instanceof SelectField) {
                            ((SelectField) listComponent).setSelectedIndex(0);
                        }
                    }
                });
            }
        });
        
        controlsColumn.addButton("Select Index 2", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator(){
                    public void apply(AbstractListComponent listComponent) {
                        if (listComponent instanceof ListBox) {
                            ((ListBox) listComponent).setSelectedIndices(new int[] {2});
                        } else if (listComponent instanceof SelectField) {
                            ((SelectField) listComponent).setSelectedIndex(2);
                        }
                    }
                });
            }
        });
        
        controlsColumn.addButton("Select Index 1502", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator(){
                    public void apply(AbstractListComponent listComponent) {
                        if (listComponent instanceof ListBox) {
                            ((ListBox) listComponent).setSelectedIndices(new int[] {1502});
                        } else if (listComponent instanceof SelectField) {
                            ((SelectField) listComponent).setSelectedIndex(1502);
                        }
                    }
                });
            }
        });
        
        controlsColumn.addButton("Clear Selections", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator(){
                    public void apply(AbstractListComponent listComponent) {
                        if (listComponent instanceof ListBox) {
                            ((ListBox) listComponent).setSelectedIndices(new int[] {});
                        } else if (listComponent instanceof SelectField) {
                            ((SelectField) listComponent).setSelectedIndex(-1);
                        }
                    }
                });
            }
        });
        
        controlsColumn.addButton("Empty ListModel", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator(){
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setModel(new DefaultListModel());
                    }
                });
            }
        });
        
        controlsColumn.addButton("Set ListModel = Numbers", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator(){
                    public void apply(AbstractListComponent listComponent) {
                        listComponent.setModel(new DefaultListModel(NUMBERS));
                    }
                });
            }
        });
        controlsColumn.addButton("Focus SelectField1", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getApplicationInstance().setFocusedComponent(selectField1);
            }
        });
        controlsColumn.addButton("Focus SelectField2", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getApplicationInstance().setFocusedComponent(selectField2);
            }
        });
        
        controlsColumn.add(new Label("ListBox-specific"));
        
        controlsColumn.addButton("Toggle Multiple Select", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final int mode = ListSelectionModel.MULTIPLE_SELECTION == listBox1.getSelectionMode()
                        ? ListSelectionModel.SINGLE_SELECTION : ListSelectionModel.MULTIPLE_SELECTION;
                apply(new Applicator() {
                    public void apply(AbstractListComponent listComponent) {
                        if (!(listComponent instanceof ListBox)) {
                            return;
                        }
                        ((ListBox) listComponent).setSelectionMode(mode);
                    }
                });
            }
        });
        controlsColumn.addButton("Select Even Indices", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator(){
                    public void apply(AbstractListComponent listComponent) {
                        if (listComponent instanceof ListBox) {
                            ((ListBox) listComponent).setSelectedIndices(new int[] { 0, 2, 4, 6, 8, 10 });
                        }
                    }
                });
            }
        });
        controlsColumn.addButton("Select Odd Indices", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply(new Applicator(){
                    public void apply(AbstractListComponent listComponent) {
                        if (listComponent instanceof ListBox) {
                            // Note: Unlike certain amplifiers, this selectfield does not "go up to eleven".
                            // Just want to make sure this is handled. 
                            ((ListBox) listComponent).setSelectedIndices(new int[] { 1, 3, 5, 7, 9, 11 });
                        }
                    }
                });
            }
        });
    }
    
    public void apply(Applicator applicator) {
        applicator.apply(selectField1);
        applicator.apply(listBox1);
        applicator.apply(selectField2);
        applicator.apply(listBox2);
    }    
}