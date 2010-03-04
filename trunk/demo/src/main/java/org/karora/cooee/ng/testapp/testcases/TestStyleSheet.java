/* 
 * This file is part of the Echo Point Project.  This project is a collection
 * of Components that have extended the Echo Web Application Framework.
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
package org.karora.cooee.ng.testapp.testcases;

import java.io.InputStream;

import org.karora.cooee.app.ApplicationInstance;
import org.karora.cooee.app.CheckBox;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.RadioButton;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.StyleSheet;
import org.karora.cooee.app.TextArea;
import org.karora.cooee.app.TextField;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.ng.ContainerEx;
import org.karora.cooee.ng.DateChooser;
import org.karora.cooee.ng.ExtentEx;
import org.karora.cooee.ng.GroupBox;
import org.karora.cooee.ng.LabelEx;
import org.karora.cooee.ng.PushButton;
import org.karora.cooee.ng.stylesheet.CssStyleSheetException;
import org.karora.cooee.ng.stylesheet.CssStyleSheetLoader;
import org.karora.cooee.ng.ui.util.io.StringInputStream;

/** 
 * <code>TestStyleSheet</code> 
 */

public class TestStyleSheet extends TestCaseBaseNG {

	private StyleSheet styleSheet;
	private SplitPane splitPane;
	private Column styleSheetArea;
	private Column componentArea;
	/**
	 * @see org.karora.cooee.ng.testapp.TestCaseNG#getTestCategory()
	 */
	public String getTestCategory() {
		return "CSS StyleSheet";
	}
	
	public Component testStyleSheet() {
		// set up stylehseet area where they can enter in style sheet text
		final TextArea textArea = new TextArea();
		textArea.setText("Enter style sheet data here");
		
		final PushButton buttonApply = new PushButton("Apply");
		final LabelEx labelErrors = new LabelEx();
		buttonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					labelErrors.setText((String) null);
					InputStream styleStream = new StringInputStream(textArea.getText()); 
					styleSheet = CssStyleSheetLoader.load(styleStream,Thread.currentThread().getContextClassLoader());
					ApplicationInstance.getActive().setStyleSheet(styleSheet);
				} catch (CssStyleSheetException e1) {
					labelErrors.setText(e1.getMessage());
				}
			}
		});
		
		
		styleSheetArea = new Column();
		styleSheetArea.add(textArea);
		styleSheetArea.add(buttonApply);
		styleSheetArea.add(labelErrors);

		// build up some components here to style
		componentArea = new Column();
		componentArea.add(new TextField());
		componentArea.add(new RadioButton());
		componentArea.add(new CheckBox());
		componentArea.add(new DateChooser());
		
		
		GroupBox groupBox = new GroupBox("Components");
		groupBox.add(componentArea);
		
		splitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP,new ExtentEx("100px"));
		splitPane.add(styleSheetArea);
		splitPane.add(groupBox);
		splitPane.setResizable(true);
		
		ContainerEx containerEx = new ContainerEx();
		containerEx.add(splitPane);
		
		return containerEx;
	}
	

}
