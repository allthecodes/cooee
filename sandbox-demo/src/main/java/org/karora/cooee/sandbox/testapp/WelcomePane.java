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
import org.karora.cooee.app.ContentPane;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Font;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Row;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.WindowPane;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;

/**
 * <code>ContentPane</code> which displays a welcome/instruction message to
 * users when they initially visit the application.
 */
public class WelcomePane extends ContentPane {

	/**
	 * Default constructor.
	 */
	public WelcomePane() {
		super();
		setStyleName("WelcomePane");

		Label label;

		Column column = new Column();
		column.setStyleName("WelcomePane.Column");
		add(column);

		label = new Label("Karora - Cooee Sandbox");
		column.add(label);

		label = new Label(Styles.ECHO2_IMAGE);
		column.add(label);

		label = new Label(Styles.INTERACTIVE_TEST_APPLICATION_IMAGE);
		column.add(label);

		WindowPane loginWindow = new WindowPane();
		loginWindow.setTitle("Welcome to the Cooee Sandbox Demo");
		loginWindow.setStyleName("WelcomePane");
		loginWindow.setClosable(false);
		add(loginWindow);

		SplitPane splitPane = new SplitPane(
				SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, new Extent(32));
		loginWindow.add(splitPane);

		Row controlRow = new Row();
		controlRow.setStyleName("ControlPane");
		splitPane.add(controlRow);

		Button button = new Button("Continue", Styles.ICON_24_YES);
		button.setStyleName("ControlPane.Button");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InteractiveApp.getApp().displayTestPane();
			}
		});
		controlRow.add(button);

		Column infoColumn = new Column();
		infoColumn.setInsets(new Insets(20, 5));
		infoColumn.setCellSpacing(new Extent(10));
		splitPane.add(infoColumn);

		label = new Label("About this application:");
		label.setFont(new Font(null, Font.BOLD, null));
		infoColumn.add(label);

		StringBuilder sb = new StringBuilder(
				"This application provides an interactive test for Cooee Sandbox Components.");
		sb.append("Most of Sandbox components are contributions from the community,");
		sb.append(" check  http://www.karora.org/wiki/display/COO/About+the+sandbox for more information");

		label = new Label(sb.toString());
		infoColumn.add(label);

	}
}
