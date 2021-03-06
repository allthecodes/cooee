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
import org.karora.cooee.testapp.Styles;

import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.SelectField;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.Table;
import org.karora.cooee.app.TextField;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.event.ChangeEvent;
import org.karora.cooee.app.event.ChangeListener;
import org.karora.cooee.app.layout.SplitPaneLayoutData;
import org.karora.cooee.app.layout.TableLayoutData;
import org.karora.cooee.app.list.AbstractListModel;
import org.karora.cooee.app.list.ListModel;
import org.karora.cooee.app.list.ListSelectionModel;
import org.karora.cooee.app.table.AbstractTableModel;
import org.karora.cooee.app.table.DefaultTableCellRenderer;
import org.karora.cooee.app.table.DefaultTableModel;
import org.karora.cooee.app.table.TableCellRenderer;
import org.karora.cooee.app.table.TableColumnModel;
import org.karora.cooee.app.table.TableModel;

/**
 * A test for <code>Tables</code>s.
 */
public class TableTest extends SplitPane {
    
    private static class PayGrade {
        
        public int payGrade;
        
        public PayGrade(int payGrade) {
            this.payGrade = payGrade;
        }
        
        public String toString() {
            return "Level " + payGrade;
        }
    }
    
    public static TableModel createEmployeeTableModel() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnCount(4);
        model.insertRow(0, new Object[]{"Bob Johnson", "bob.johnson@test.nextapp.com", "949.555.1234", new PayGrade(10)});
        model.insertRow(0, new Object[]{"Laura Smith", "laura.smith@test.nextapp.com", "217.555.9343", new PayGrade(6)});
        model.insertRow(0, new Object[]{"Jenny Roberts", "jenny.roberts@test.nextapp.com", "630.555.1987", new PayGrade(6)});
        model.insertRow(0, new Object[]{"Thomas Albertson", "thomas.albertson@test.nextapp.com", "619.555.1233", new PayGrade(3)});
        model.insertRow(0, new Object[]{"Albert Thomas", "albert.thomas@test.nextapp.com", "408.555.3232", new PayGrade(11)});
        model.insertRow(0, new Object[]{"Sheila Simmons", "sheila.simmons@test.nextapp.com", "212.555.8700", new PayGrade(6)});
        model.insertRow(0, new Object[]{"Mark Atkinson", "mark.atkinson@test.nextapp.com", "213.555.9456", new PayGrade(3)});
        model.insertRow(0, new Object[]{"Linda Jefferson", "linda.jefferson@test.nextapp.com", "949.555.8925", new PayGrade(4)});
        model.insertRow(0, new Object[]{"Yvonne Adams", "yvonne.adams@test.nextapp.com", "714.555.8543", new PayGrade(5)});
        return model;
    }
    
    private class MultiplicationTableModel extends AbstractTableModel {

        /**
         * @see org.karora.cooee.app.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return 12;
        }
        
        /**
         * @see org.karora.cooee.app.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return 12;
        }
        
        /**
         * @see org.karora.cooee.app.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int column, int row) {
            return new Integer((column + 1) * (row + 1));
        }
    }
    
    private Table testTable;
    
    private TableCellRenderer editingTableCellRenderer = new TableCellRenderer() {
        
        /**
         * @see org.karora.cooee.app.table.TableCellRenderer#getTableCellRendererComponent(org.karora.cooee.app.Table,
         *      java.lang.Object, int, int)
         */
        public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
            if (value instanceof PayGrade) {
                ListModel listModel = new AbstractListModel() {
                    
                    public Object get(int index) {
                        return new Integer(index + 3);
                    }
    
                    public int size() {
                        return 10;
                    }
                };
                final SelectField selectField = new SelectField(listModel);
                selectField.setSelectedIndex(((PayGrade) value).payGrade - 3);
                selectField.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        selectField.setBackground(StyleUtil.randomBrightColor());
                    }
                });
                return selectField;
            } else {
                TextField textField = new TextField();
                if (value != null) {
                    textField.setText(value.toString());
                }
                return textField;
            }
        }
    };
    
    private TableCellRenderer randomizingCellRenderer = new TableCellRenderer() {
        
        /**
         * @see org.karora.cooee.app.table.TableCellRenderer#getTableCellRendererComponent(org.karora.cooee.app.Table,
         *      java.lang.Object, int, int)
         */
        public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
            Label label = new Label(value == null ? null : value.toString());
            TableLayoutData layoutData = new TableLayoutData();
            layoutData.setBackground(StyleUtil.randomBrightColor());
            layoutData.setInsets(new Insets(StyleUtil.randomExtent(12), StyleUtil.randomExtent(12), StyleUtil.randomExtent(12),
                    StyleUtil.randomExtent(12)));
            layoutData.setAlignment(StyleUtil.randomAlignmentHV());
            label.setLayoutData(layoutData);
            return label;
        }
    };
    
    private TableCellRenderer backgroundImageCheckerCellRenderer = new TableCellRenderer() {
        
        /**
         * @see org.karora.cooee.app.table.TableCellRenderer#getTableCellRendererComponent(org.karora.cooee.app.Table,
         *      java.lang.Object, int, int)
         */
        public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
            Label label = new Label(value == null ? null : value.toString());
            TableLayoutData layoutData = new TableLayoutData();
            layoutData.setInsets(new Insets(5));
            if (row % 2 == column % 2) {
                layoutData.setBackgroundImage(Styles.BG_SHADOW_DARK_BLUE);
            } else {
                layoutData.setBackgroundImage(Styles.BG_SHADOW_LIGHT_BLUE);
            }
            label.setLayoutData(layoutData);
            return label;
        }
    };
    
    private TableCellRenderer visibilityCheckerCellRenderer = new TableCellRenderer() {
        
        /**
         * @see org.karora.cooee.app.table.TableCellRenderer#getTableCellRendererComponent(org.karora.cooee.app.Table,
         *      java.lang.Object, int, int)
         */
        public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
            Label label = new Label(value == null ? null : value.toString());
            if (row % 2 == column % 2) {
                label.setVisible(false);
            }
            return label;
        }
    };
    
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
    
    public TableTest() {
        super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));
        setStyleName("DefaultResizable");
        
        Column groupContainerColumn = new Column();
        groupContainerColumn.setCellSpacing(new Extent(5));
        groupContainerColumn.setStyleName("TestControlsColumn");
        add(groupContainerColumn);
        
        Column testColumn = new Column();
        SplitPaneLayoutData splitPaneLayoutData = new SplitPaneLayoutData();
        splitPaneLayoutData.setInsets(new Insets(10, 5));
        testColumn.setLayoutData(splitPaneLayoutData);
        add(testColumn);

        ButtonColumn controlsColumn;
        
        controlsColumn = new ButtonColumn();
        groupContainerColumn.add(controlsColumn);

        controlsColumn.add(new Label("TableModel"));
        
        controlsColumn.addButton("Multiplication Model", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setModel(new MultiplicationTableModel());
            }
        });
        
        controlsColumn.addButton("DefaultTableModel (Empty)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setModel(new DefaultTableModel());
            }
        });
        
        controlsColumn.addButton("DefaultTableModel (Employees)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setModel(createEmployeeTableModel());
            }
        });
        
        testTable = new Table();
        testTable.setBorder(new Border(new Extent(1), Color.BLUE, Border.STYLE_SOLID));
        testColumn.add(testTable);

        controlsColumn.add(new Label("Appearance"));
        
        controlsColumn.addButton("Change Foreground", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setForeground(StyleUtil.randomColor());
            }
        });
        controlsColumn.addButton("Change Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setBackground(StyleUtil.randomColor());
            }
        });
        controlsColumn.addButton("Change Border (All Attributes)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setBorder(StyleUtil.randomBorder());
            }
        });
        controlsColumn.addButton("Change Border Color", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Border border = testTable.getBorder();
                testTable.setBorder(new Border(border.getSize(), StyleUtil.randomColor(), border.getStyle()));
            }
        });
        controlsColumn.addButton("Change Border Size", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setBorder(StyleUtil.nextBorderSize(testTable.getBorder()));
            }
        });
        controlsColumn.addButton("Change Border Style", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setBorder(StyleUtil.nextBorderStyle(testTable.getBorder()));
            }
        });
        
        controlsColumn.addButton("Set Insets 0px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setInsets(new Insets(0));
            }
        });
        controlsColumn.addButton("Set Insets 2px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setInsets(new Insets(2));
            }
        });
        controlsColumn.addButton("Set Insets 10/5px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setInsets(new Insets(10, 5));
            }
        });
        controlsColumn.addButton("Set Insets 10/20/30/40px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setInsets(new Insets(10, 20, 30, 40));
            }
        });
        controlsColumn.addButton("Set Width = null", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setWidth(null);
            }
        });
        controlsColumn.addButton("Set Width = 500px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setWidth(new Extent(500));
            }
        });
        controlsColumn.addButton("Set Width = 100%", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setWidth(new Extent(100, Extent.PERCENT));
            }
        });
        controlsColumn.addButton("Set ColumnWidths Equal", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TableColumnModel columnModel = testTable.getColumnModel();
                int columnCount = columnModel.getColumnCount();
                    if (columnCount > 0) {
                    Extent width = new Extent(100 / columnCount, Extent.PERCENT);
                    for (int i = 0; i < columnCount; ++i) {
                        columnModel.getColumn(i).setWidth(width);
                    }
                }
            }
        });
        controlsColumn.addButton("Toggle Header Visible", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setHeaderVisible(!testTable.isHeaderVisible());
            }
        });
        controlsColumn.addButton("Toggle Enabled State", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setEnabled(!testTable.isEnabled());
            }
        });
        
        // Rollover Effect Settings

        controlsColumn = new ButtonColumn();
        groupContainerColumn.add(controlsColumn);
        
        controlsColumn.add(new Label("Rollover Effects"));

        controlsColumn.addButton("Enable Rollover Effects", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setRolloverEnabled(true);
            }
        });
        controlsColumn.addButton("Disable Rollover Effects", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setRolloverEnabled(false);
            }
        });
        controlsColumn.addButton("Set Rollover Foreground", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setRolloverForeground(StyleUtil.randomColor());
            }
        });
        controlsColumn.addButton("Clear Rollover Foreground", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setRolloverForeground(null);
            }
        });
        controlsColumn.addButton("Set Rollover Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 testTable.setRolloverBackground(StyleUtil.randomColor());
            }
        });
        controlsColumn.addButton("Clear Rollover Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setRolloverBackground(null);
            }
        });
        controlsColumn.addButton("Set Rollover Font", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setRolloverFont(StyleUtil.randomFont());
            }
        });
        controlsColumn.addButton("Clear Rollover Font", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setRolloverFont(null);
            }
        });
        controlsColumn.addButton("Set Rollover Background Image", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setRolloverBackgroundImage(Styles.BG_SHADOW_LIGHT_BLUE);
            }
        });
        controlsColumn.addButton("Clear Rollover Background Image", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setRolloverBackgroundImage(null);
            }
        });
        
        // Selection Settings

        controlsColumn = new ButtonColumn();
        groupContainerColumn.add(controlsColumn);
        
        controlsColumn.add(new Label("Selection"));

        controlsColumn.addButton("Enable Selection", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setSelectionEnabled(true);
            }
        });
        controlsColumn.addButton("Disable Selection", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setSelectionEnabled(false);
            }
        });
        controlsColumn.addButton("Set SelectionMode = Single", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            }
        });
        controlsColumn.addButton("Set SelectionMode = Multiple", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_SELECTION);
            }
        });
        controlsColumn.addButton("Toggle Selection of Row #2", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListSelectionModel selectionModel = testTable.getSelectionModel();
                selectionModel.setSelectedIndex(2, !selectionModel.isSelectedIndex(2));
            }
        });
        controlsColumn.addButton("Toggle Selection of Row #500 (there isn't one)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListSelectionModel selectionModel = testTable.getSelectionModel();
                selectionModel.setSelectedIndex(500, !selectionModel.isSelectedIndex(500));
            }
        });
        controlsColumn.addButton("Set Selection Foreground", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setSelectionForeground(StyleUtil.randomColor());
            }
        });
        controlsColumn.addButton("Clear Selection Foreground", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setSelectionForeground(null);
            }
        });
        controlsColumn.addButton("Set Selection Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 testTable.setSelectionBackground(StyleUtil.randomColor());
            }
        });
        controlsColumn.addButton("Clear Selection Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setSelectionBackground(null);
            }
        });
        controlsColumn.addButton("Set Selection Font", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setSelectionFont(StyleUtil.randomFont());
            }
        });
        controlsColumn.addButton("Clear Selection Font", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setSelectionFont(null);
            }
        });
        controlsColumn.addButton("Set Selection Background Image", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setSelectionBackgroundImage(Styles.BUTTON_PRESSED_BACKGROUND_IMAGE);
            }
        });
        controlsColumn.addButton("Clear Selection Background Image", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setSelectionBackgroundImage(null);
            }
        });
        
        // Listener Settings

        controlsColumn = new ButtonColumn();
        groupContainerColumn.add(controlsColumn);
        
        controlsColumn.add(new Label("Listeners"));

        controlsColumn.addButton("Add ActionListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.addActionListener(actionListener);
            }
        });
        controlsColumn.addButton("Remove ActionListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.removeActionListener(actionListener);
            }
        });
        controlsColumn.addButton("Add ChangeListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.getSelectionModel().addChangeListener(changeListener);
            }
        });
        controlsColumn.addButton("Remove ChangeListener", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.getSelectionModel().removeChangeListener(changeListener);
            }
        });
        
        // Cell Settings

        controlsColumn = new ButtonColumn();
        groupContainerColumn.add(controlsColumn);
        
        controlsColumn.add(new Label("Cell Renderer"));

        controlsColumn.addButton("Default Cell Renderer", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
            }
        });
        controlsColumn.addButton("Randomizing Cell Renderer", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setDefaultRenderer(Object.class, randomizingCellRenderer);
            }
        });
        controlsColumn.addButton("BackgroundImage Checker Cell Renderer", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setDefaultRenderer(Object.class, backgroundImageCheckerCellRenderer);
            }
        });
        controlsColumn.addButton("Visibility Checker Cell Renderer", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setDefaultRenderer(Object.class, visibilityCheckerCellRenderer);
            }
        });
        controlsColumn.addButton("Editing Cell Renderer (not bound to model)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setDefaultRenderer(Object.class, editingTableCellRenderer);
            }
        });
        controlsColumn.addButton("Alignment = Leading/Top", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setDefaultRenderer(Object.class, 
                        createTableCellRenderer(new Alignment(Alignment.LEADING, Alignment.TOP)));
            }
        });
        controlsColumn.addButton("Alignment = Trailing/Bottom", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setDefaultRenderer(Object.class, 
                        createTableCellRenderer(new Alignment(Alignment.TRAILING, Alignment.BOTTOM)));
            }
        });
        controlsColumn.addButton("Alignment = Left/Top", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setDefaultRenderer(Object.class, 
                        createTableCellRenderer(new Alignment(Alignment.LEFT, Alignment.TOP)));
            }
        });
        controlsColumn.addButton("Alignment = Right/Bottom", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testTable.setDefaultRenderer(Object.class, 
                        createTableCellRenderer(new Alignment(Alignment.RIGHT, Alignment.BOTTOM)));
            }
        });
    }

    private TableCellRenderer createTableCellRenderer(final Alignment alignment) {
        return new TableCellRenderer() {        

            /**
             * @see org.karora.cooee.app.table.TableCellRenderer#getTableCellRendererComponent(org.karora.cooee.app.Table,
             *      java.lang.Object, int, int)
             */
            public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
                Label label = new Label(value == null ? null : value.toString());
                TableLayoutData layoutData = new TableLayoutData();
                layoutData.setAlignment(alignment);
                label.setLayoutData(layoutData);
                return label;
            }
        };
    }
    
}
