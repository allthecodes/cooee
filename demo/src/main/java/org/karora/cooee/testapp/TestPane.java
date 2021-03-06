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

package org.karora.cooee.testapp;

import org.karora.cooee.app.Button;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.ContentPane;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;

/**
 * Main InteractiveTest <code>ContentPane</code> which displays a menu of
 * available tests.
 */
public class TestPane extends ContentPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5589476062325452414L;

	private SplitPane horizontalPane;

	private ActionListener commandActionListener = new ActionListener() {

		private Button activeButton = null;

		/**
		 * @see org.karora.cooee.app.event.ActionListener#actionPerformed(org.karora.cooee.app.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			try {
				if (activeButton != null) {
					activeButton.setStyleName("Default");
				}
				Button button = (Button) e.getSource();
				button.setStyleName("Selected");
				activeButton = button;
				String screenClassName = "org.karora.cooee.testapp.testscreen."
						+ e.getActionCommand();
				Class screenClass = Class.forName(screenClassName);
				Component content = (Component) screenClass.newInstance();
				if (horizontalPane.getComponentCount() > 1) {
					horizontalPane.remove(1);
				}
				horizontalPane.add(content);
			} catch (ClassNotFoundException ex) {
				throw new RuntimeException(ex.toString());
			} catch (InstantiationException ex) {
				throw new RuntimeException(ex.toString());
			} catch (IllegalAccessException ex) {
				throw new RuntimeException(ex.toString());
			}
		}
	};

	private Column testLaunchButtonsColumn;

	public TestPane() {
		super();

		SplitPane verticalPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL);
		verticalPane.setStyleName("TestPane");
		add(verticalPane);

		Label titleLabel = new Label("Karora Cooee - Test Application");
		titleLabel.setStyleName("TitleLabel");
		verticalPane.add(titleLabel);

		horizontalPane = new SplitPane(SplitPane.ORIENTATION_HORIZONTAL,
				new Extent(215));
		horizontalPane.setStyleName("DefaultResizable");
		verticalPane.add(horizontalPane);

		Column controlsColumn = new Column();
		controlsColumn.setStyleName("ApplicationControlsColumn");
		controlsColumn.setCellSpacing(new Extent(5));

		horizontalPane.add(controlsColumn);

		testLaunchButtonsColumn = new Column();
		controlsColumn.add(testLaunchButtonsColumn);

		addTest("Accordion Pane", "AccordionPaneTest");
		addTest("Active TextArea", "ActiveTextAreaTest");
		addTest("Border Pane", "BorderPaneTest");
		addTest("Button", "ButtonTest");
		addTest("Calendar Select", "CalendarSelectTest");
		addTest("Client Configuration", "ClientConfigurationTest");
		addTest("Client Exception", "ClientExceptionTest");
		addTest("Color Select ", "ColorSelectTest");
		addTest("Column", "ColumnTest");
		addTest("Command", "CommandTest");
		addTest("Composite", "CompositeTest");
		addTest("Container Context", "ContainerContextTest");
		addTest("Content Pane", "ContentPaneTest");
		addTest("Delay", "DelayTest");
		addTest("Drag Source", "DragSourceTest");
		addTest("Drop Down Menu", "DropDownMenuTest");
		addTest("Exception", "ExceptionTest");
		addTest("Grid", "GridTest");
		addTest("Hierarchy", "HierarchyTest");
		addTest("Image", "ImageReferenceTest");
		addTest("Integer TextField", "IntegerTextFieldTest");
		addTest("Label", "LabelTest");
		addTest("List Box", "ListBoxTest");
		addTest("List Box (Large Quantity)", "ListRenderTableTest");
		addTest("Localization", "LocalizationTest");
		addTest("Menu Bar Pane", "MenuBarPaneTest");
		addTest("Numeric TextField", "NumericTextFieldTest");
		addTest("Panel", "PanelTest");
		addTest("Push (Basic)", "PushTest");
		addTest("Push (Ghost Test)", "PushGhostTest");
		addTest("Random Click", "RandomClickTest");
		addTest("RegEx TextField", "RegExTextFieldTest");
		addTest("Row", "RowTest");
		addTest("Split Pane (Basic)", "SplitPaneTest");
		addTest("Split Pane (Nested)", "SplitPaneNestedTest");
		addTest("Style Sheet", "StyleSheetTest");
		addTest("Tab Pane", "TabPaneTest");
		addTest("Table", "TableTest");
		addTest("Table (Performance)", "TablePerformanceTest");
		addTest("Text Component", "TextComponentTest");
		addTest("Text Sync", "TextSyncTest");
		addTest("Transition Pane", "TransitionPaneTest");
		addTest("Visibility", "VisibilityTest");
		addTest("Window", "WindowTest");
		addTest("Window Pane", "WindowPaneTest");
		addTest("Window Pane Examples", "WindowPaneExamplesTest");

		Column applicationControlsColumn = new Column();
		controlsColumn.add(applicationControlsColumn);

		Button button = new Button("Exit");
		button.setRenderId("Exit");
		button.setId("ExitTestApplication");
		button.setStyleName("Default");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InteractiveApp.getApp().displayWelcomePane();
			}
		});
		applicationControlsColumn.add(button);
	}

	private void addTest(String name, String action) {
		Button button = new Button(name);
		button.setRenderId("StartTest" + action);
		button.setId("StartTest:" + action);
		button.setActionCommand(action);
		button.setStyleName("Default");
		button.addActionListener(commandActionListener);
		testLaunchButtonsColumn.add(button);
	}
}
