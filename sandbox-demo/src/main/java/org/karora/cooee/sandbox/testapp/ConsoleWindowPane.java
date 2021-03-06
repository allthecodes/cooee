/* 
 * This file is part of the Echo2 Chart Library.
 * Copyright (C) 2005 NextApp, Inc.
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

package org.karora.cooee.sandbox.testapp;

import org.karora.cooee.app.Button;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Font;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.Row;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.WindowPane;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.layout.SplitPaneLayoutData;

/**
 * A <code>WindowPane</code> which contains an event console.
 */
public class ConsoleWindowPane extends WindowPane {
    
    private Column column;
    
    public ConsoleWindowPane() {
        super();
        setTitle("Console");
        setStyleName("Default");
        
        SplitPane splitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, new Extent(40));
        splitPane.setSeparatorHeight(new Extent(1));
        add(splitPane);
        
        SplitPaneLayoutData splitPaneLayoutData;
        
        Row controlRow = new Row();
        splitPaneLayoutData = new SplitPaneLayoutData();
        splitPaneLayoutData.setBackground(new Color(0xafafbf));
        splitPaneLayoutData.setInsets(new Insets(5));
        controlRow.setLayoutData(splitPaneLayoutData);
        splitPane.add(controlRow);
        
        Button clearButton = new Button("Clear");
        clearButton.setStyleName("Default");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                column.removeAll();
            }
        });
        controlRow.add(clearButton);
        
        column = new Column();
        column.setFont(new Font(Font.MONOSPACE, Font.PLAIN, new Extent(10)));
        column.setForeground(Color.GREEN);
        splitPaneLayoutData = new SplitPaneLayoutData();
        splitPaneLayoutData.setBackground(Color.BLACK);
        splitPaneLayoutData.setInsets(new Insets(5));
        column.setLayoutData(splitPaneLayoutData);
        splitPane.add(column);
    }
    
    public void writeMessage(String message) {
        column.add(new Label(message));
    }
}
