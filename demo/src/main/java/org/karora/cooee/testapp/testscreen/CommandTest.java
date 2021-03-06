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

import org.karora.cooee.app.Button;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.layout.SplitPaneLayoutData;
import org.karora.cooee.webcontainer.command.BrowserOpenWindowCommand;
import org.karora.cooee.webcontainer.command.BrowserRedirectCommand;

/**
 * A test for the command infrastructures and browser control commands.
 */
public class CommandTest extends Column {
    
    public CommandTest() {
        super();
        
        SplitPaneLayoutData splitPaneLayoutData = new SplitPaneLayoutData();
        splitPaneLayoutData.setInsets(new Insets(10));
        setLayoutData(splitPaneLayoutData);
        
        Button button;
        
        button = new Button("Enqueue Redirect Command");
        button.setStyleName("Default");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getApplicationInstance().enqueueCommand(new BrowserRedirectCommand("http://www.karora.org/?q=node/8"));
            }
        });
        add(button);
        
        button = new Button("Enqueue Redirect Command to mailto: URL");
        button.setStyleName("Default");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getApplicationInstance().enqueueCommand(new BrowserRedirectCommand("mailto:info@karora.org"));
            }
        });
        add(button);
        
        button = new Button("Enqueue Simple Window Open Command");
        button.setStyleName("Default");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getApplicationInstance().enqueueCommand(
                        new BrowserOpenWindowCommand("http://www.karora.org/?q=node/8", null, null));
            }
        });
        add(button);
        
        button = new Button("Enqueue 640x240 Named Window Open Command");
        button.setStyleName("Default");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getApplicationInstance().enqueueCommand(
                        new BrowserOpenWindowCommand("http://www.karora.org/?q=node/8", 
                        "auxwindow", "width=640,height=240"));
            }
        });
        add(button);
    }
}
