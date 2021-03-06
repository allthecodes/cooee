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
import org.karora.cooee.testapp.StyleUtil;
import org.karora.cooee.testapp.Styles;

import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.FillImage;
import org.karora.cooee.app.Button;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Row;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.layout.SplitPaneLayoutData;

/**
 * Interactive test module for <code>SplitPane</code>s.
 */
public class SplitPaneTest extends SplitPane {
    
    private class PaneControlsColumn extends ButtonColumn {
        
        private PaneControlsColumn(final int paneNumber) {
            add(new Label("Configure Pane #" + paneNumber));
    
            addButton("Fill With Text", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    if (testPane.getComponent(paneNumber) instanceof Label) {
                        Label label = (Label) testPane.getComponent(paneNumber);
                        label.setText(StyleUtil.QUASI_LATIN_TEXT_1);
                    }
                }
            });
            addButton("Change Background Color", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setBackground(StyleUtil.randomBrightColor());
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("MIN Size = Default", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setMinimumSize(null);
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("MIN Size = 30", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setMinimumSize(new Extent(30));
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("MAX Size = Default", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setMaximumSize(null);
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("MAX Size = 120", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setMaximumSize(new Extent(120));
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("Toggle Background Image", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    FillImage backgroundImage = splitPaneLayoutData.getBackgroundImage();
                    if (backgroundImage == null) {
                        splitPaneLayoutData.setBackgroundImage(Styles.BG_SHADOW_LIGHT_BLUE);
                    } else {
                        splitPaneLayoutData.setBackgroundImage(null);
                    }
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("Insets = null", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setInsets(null);
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("Insets = 0px", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setInsets(new Insets(0));
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("Insets = 5px", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setInsets(new Insets(5));
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("Insets = 10/20/30/40px", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setInsets(new Insets(10, 20, 30, 40));
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("Overflow = Auto", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setOverflow(SplitPaneLayoutData.OVERFLOW_AUTO);
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("Overflow = Hidden", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setOverflow(SplitPaneLayoutData.OVERFLOW_HIDDEN);
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("Overflow = Scroll", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setOverflow(SplitPaneLayoutData.OVERFLOW_SCROLL);
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("Alignment = Left", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setAlignment(new Alignment(Alignment.LEFT, Alignment.DEFAULT));
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("Alignment = Center", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setAlignment(new Alignment(Alignment.CENTER, Alignment.DEFAULT));
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("Alignment = Right", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setAlignment(new Alignment(Alignment.RIGHT, Alignment.DEFAULT));
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
            addButton("Alignment = Default", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (testPane.getComponentCount() < paneNumber + 1) {
                        return;
                    }
                    SplitPaneLayoutData splitPaneLayoutData = getLayoutData(paneNumber);
                    splitPaneLayoutData.setAlignment(new Alignment(Alignment.DEFAULT, Alignment.DEFAULT));
                    testPane.getComponent(paneNumber).setLayoutData(splitPaneLayoutData);
                }
            });
        }
        
        private SplitPaneLayoutData getLayoutData(int paneNumber) {
            SplitPaneLayoutData splitPaneLayoutData = (SplitPaneLayoutData) testPane.getComponent(paneNumber).getLayoutData();
            if (splitPaneLayoutData == null) {
                splitPaneLayoutData = new SplitPaneLayoutData();
            }
            return splitPaneLayoutData;
        }
    }

    private SplitPane testPane;
    
    public SplitPaneTest() {
        super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));
        setStyleName("DefaultResizable");
        
        Column groupContainerColumn = new Column();
        groupContainerColumn.setCellSpacing(new Extent(5));
        groupContainerColumn.setStyleName("TestControlsColumn");
        add(groupContainerColumn);

        ButtonColumn controlsColumn;
        
        controlsColumn = new ButtonColumn();
        controlsColumn.add(new Label("Add / Remove Panes"));
        groupContainerColumn.add(controlsColumn);
        
        controlsColumn.addButton("Remove Pane 0", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testPane.getComponentCount() >= 1) {
                    testPane.remove(0);
                }
            }
        });
        controlsColumn.addButton("Remove Pane 1", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testPane.getComponentCount() >= 2) {
                    testPane.remove(1);
                }
            }
        });
        controlsColumn.addButton("Replace Pane 0", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testPane.getComponentCount() >= 1) {
                    testPane.remove(0);
                }
                testPane.add(createPaneLabel("Replacement for Pane 0"), 0);
            }
        });
        controlsColumn.addButton("Replace Pane 1", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testPane.getComponentCount() >= 2) {
                    testPane.remove(1);
                }
                testPane.add(createPaneLabel("Replacement for Pane 1"));
            }
        });
        controlsColumn.addButton("Add at Beginning", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testPane.getComponentCount() < 2) {
                    testPane.add(createPaneLabel("Added at Beginning"), 0);
                }
            }
        });
        controlsColumn.addButton("Add at End", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testPane.getComponentCount() < 2) {
                    testPane.add(createPaneLabel("Added at End"));
                }
            }
        });
        controlsColumn.addButton("Add Row", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testPane.getComponentCount() < 2) {
                    Row row = new Row();
                    row.setBorder(new Border(new Extent(1), Color.BLACK, Border.STYLE_SOLID));
                    row.setCellSpacing(new Extent(5));
                    row.setInsets(new Insets(10, 5));
                    row.add(new Label("Alpha"));
                    row.add(new Label("Bravo"));
                    row.add(new Label("Charlie"));
                    testPane.add(row);
                }
            }
        });
        controlsColumn.addButton("Add-Remove-Add", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testPane.getComponentCount() < 2) {
                    Label label = createPaneLabel("Added at End, Removed, Re-Added");
                    testPane.add(label);
                    testPane.remove(label);
                    testPane.add(label);
                }
            }
        });
        controlsColumn.addButton("Add Button", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testPane.getComponentCount() < 2) {
                    Button testButton = new Button("Test Button");
                    SplitPaneLayoutData layoutData = new SplitPaneLayoutData();
                    layoutData.setInsets(new Insets(10));
                    testButton.setLayoutData(layoutData);
                    testButton.setStyleName("Default");
                    testPane.add(testButton);
                }
            }
        });
        
        controlsColumn = new ButtonColumn();
        controlsColumn.add(new Label("Configure SplitPane"));
        groupContainerColumn.add(controlsColumn);
        
        controlsColumn.addButton("Set Separator Position = null", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testPane.setSeparatorPosition(null);
            }
        });
        controlsColumn.addButton("Set Separator Position = 300px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testPane.setSeparatorPosition(new Extent(300));
            }
        });
        controlsColumn.addButton("Set Orientation = Leading/Trailing", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testPane.setOrientation(SplitPane.ORIENTATION_HORIZONTAL_LEADING_TRAILING);
            }
        });
        controlsColumn.addButton("Set Orientation = Trailing/Leading", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testPane.setOrientation(SplitPane.ORIENTATION_HORIZONTAL_TRAILING_LEADING);
            }
        });
        controlsColumn.addButton("Set Orientation = Left/Right", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testPane.setOrientation(SplitPane.ORIENTATION_HORIZONTAL_LEFT_RIGHT);
            }
        });
        controlsColumn.addButton("Set Orientation = Right/Left", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testPane.setOrientation(SplitPane.ORIENTATION_HORIZONTAL_RIGHT_LEFT);
            }
        });
        controlsColumn.addButton("Set Orientation = Top/Bottom", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testPane.setOrientation(SplitPane.ORIENTATION_VERTICAL_TOP_BOTTOM);
            }
        });
        controlsColumn.addButton("Set Orientation = Bottom/Top", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testPane.setOrientation(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP);
            }
        });
        controlsColumn.addButton("Disable Resize", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testPane.setResizable(!testPane.isResizable());
                ((Button) e.getSource()).setText(testPane.isResizable() ? "Disable Resize" : "Enable Resize");
            }
        });
        
        groupContainerColumn.add(new PaneControlsColumn(0));
        groupContainerColumn.add(new PaneControlsColumn(1));

        testPane = new SplitPane(ORIENTATION_VERTICAL, new Extent(200, Extent.PX));
        testPane.setStyleName("DefaultResizable");
        add(testPane);
    }
    
    private Label createPaneLabel(String text) {
        Label label = new Label(text);
        SplitPaneLayoutData splitPaneLayoutData = new SplitPaneLayoutData();
        splitPaneLayoutData.setBackground(StyleUtil.randomBrightColor());
        label.setLayoutData(splitPaneLayoutData);
        return label;
    }
}
