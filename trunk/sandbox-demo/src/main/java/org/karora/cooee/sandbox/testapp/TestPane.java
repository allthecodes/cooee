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
import org.karora.cooee.app.Component;
import org.karora.cooee.app.ContentPane;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;

/**
 * Main InteractiveTest <code>ContentPane</code> which displays a menu of
 * available tests.
 */
public class TestPane extends ContentPane {

	private SplitPane horizontalPane;

	private ActionListener commandActionListener = new ActionListener() {

		private Button activeButton = null;

		/**
		 * @see org.karora.cooee.app.event.ActionListener#actionPerformed(org.karora.cooee.app.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			
				if (activeButton != null) {
					activeButton.setStyleName("Default");
				}
				Button button = (Button) e.getSource();
				button.setStyleName("Selected");
				activeButton = button;
				String screenClassName = "org.karora.cooee.sandbox.testapp.testscreen."
						+ e.getActionCommand();
				
				try {
					
					Class screenClass = Class.forName(screenClassName);
					Component content = (Component) screenClass.newInstance();
					if (horizontalPane.getComponentCount() > 1) {
						horizontalPane.remove(1);
					}
					horizontalPane.add(content);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
		
		}
	};

	private Column testLaunchButtonsColumn;

	public TestPane() {
		super();

        // Create outer SplitPane with title
		SplitPane verticalPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL);
		verticalPane.setStyleName("TestPane");
		add(verticalPane);

		Label titleLabel = new Label("Karora Cooee Sandbox Test Application");
		titleLabel.setStyleName("TitleLabel");
		verticalPane.add(titleLabel);

        // Create inner SplitPane with application control buttons; tests and 'Exit'
		horizontalPane = new SplitPane(SplitPane.ORIENTATION_HORIZONTAL, new Extent(215));
		horizontalPane.setStyleName("DefaultResizable");
		verticalPane.add(horizontalPane);

        // This column hold two columns, one wiht component test buttons and the
        //    the other with applicaiton control buttons
		Column controlsColumn = new Column();
		controlsColumn.setStyleName("ControlsColumn");
		horizontalPane.add(controlsColumn);

        // Create the component test buttons
		testLaunchButtonsColumn = new Column();
		testLaunchButtonsColumn.setStyleName("ComponentsColumn");
		controlsColumn.add(testLaunchButtonsColumn);
		
		Label packageLabel;
		
        packageLabel = new Label("JFreeChart");
        packageLabel.setStyleName("PackageLabel");
		testLaunchButtonsColumn.add(packageLabel);
		addTest("Pie ChartDisplay", "PieChartTest");
		
        packageLabel = new Label("File Transfer");
        packageLabel.setStyleName("PackageLabel");
		testLaunchButtonsColumn.add(packageLabel);
		addTest("File Upload",      "FileUploadTest");
		addTest("File Pane",        "FilePaneTest");

        packageLabel = new Label("Informagen");
        packageLabel.setStyleName("PackageLabel");
		testLaunchButtonsColumn.add(packageLabel);
		addTest("HorizontalRule",   "HorizontalRuleTest");
		addTest("ActiveTextArea",   "ActiveTextAreaTest");
		addTest("RegExTextField",   "RegExTextFieldTest");
		addTest("IntegerTextField", "IntegerTextFieldTest");
		addTest("NumericTextField", "NumericTextFieldTest");
		
        packageLabel = new Label("Consultas");
        packageLabel.setStyleName("PackageLabel");
		testLaunchButtonsColumn.add(packageLabel);
		addTest("SPWindowPane",     "SpWindowPaneTest");
		addTest("RoundedColumn",    "RoundedColumnTest");
		addTest("RoundedPanel",     "RoundedPanelTest");
		addTest("Overlay",          "OverlayTest");
		addTest("DragableOverlay",  "DragableOverlayTest");
		addTest("ImageButton",     "ImageButtonTest");

        // Create application control buttons
		Column applicationControlsColumn = new Column();
		applicationControlsColumn.setStyleName("ApplicationControlsColumn");
		controlsColumn.add(applicationControlsColumn);

        // Create 'Console' button
		Button consoleBtn = new Button("Console");
		consoleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InteractiveApp.getApp().toggleConsoleVisible();
			}
		});
		applicationControlsColumn.add(consoleBtn);


        // Create 'Exit' Button
		Button exitBtn = new Button("Exit");
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InteractiveApp.getApp().displayWelcomePane();
			}
		});
		applicationControlsColumn.add(exitBtn);
	}

	private void addTest(String name, String action) {
		Button button = new Button(name);
		button.setActionCommand(action);
		button.addActionListener(commandActionListener);
		testLaunchButtonsColumn.add(button);
	}
}
