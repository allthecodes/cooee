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
package org.karora.cooee.ng.testapp.helpers;

import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.event.ChangeEvent;
import org.karora.cooee.app.event.ChangeListener;
import org.karora.cooee.ng.ColorChooser;
import org.karora.cooee.ng.DropDown;
import org.karora.cooee.ng.LabelEx;
import org.karora.cooee.ng.PopUp;
import org.karora.cooee.ng.util.FontKit;

/**
 * 
 */
public class TestColorPopup extends TestAbstractHelper {

	private PopUp popUp;
	private ColorChooser colorChooser;
	private LabelEx sampleColor;
	
	/**
	 * @see org.karora.cooee.ng.testapp.helpers.TestHelper#getUI()
	 */
	public Component getUI() {
		return popUp;
	}
	
	/**
	 * @see org.karora.cooee.ng.testapp.helpers.TestHelper#getTestValue()
	 */
	public Object getTestValue() {
		return colorChooser.getSelectedColor();
	}
	
	/**
	 * @see org.karora.cooee.ng.testapp.helpers.TestHelper#applyAsynchPropertyValue()
	 */
	public void applyAsynchPropertyValue() {
		if (getPropertyName() != null) {
			Color newValue = colorChooser.getSelectedColor();
			setTestProperty(Color.class, newValue);			
		}
	}
	
	/**
	 * @see org.karora.cooee.ng.testapp.helpers.TestHelper#applyInitialPropertyValue()
	 */
	public void applyInitialPropertyValue() {
		Color currentValue = (Color) getTestProperty(Color.class);
		colorChooser.setSelectedColor(currentValue);
	}
	
	/**
	 * @see org.karora.cooee.ng.testapp.helpers.TestHelper#setTestValue(java.lang.Object)
	 */
	public void setTestValue(Object value) {
		colorChooser.setSelectedColor((Color) value);
	}

	public void init() {
		
		popUp = new DropDown();
		popUp.setPopUpOnRollover(false);
		
		sampleColor = new LabelEx();
		sampleColor.setText(getTestDescription());
		sampleColor.setToolTipText(getTestDescription());
		sampleColor.setFont(FontKit.font("Verdana,plain,7pt"));

		colorChooser = new ColorChooser();
		// init the initial value based on current value
		colorChooser.getSelectionModel().addListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Color newValue = colorChooser.getSelectedColor();
				sampleColor.setBackground(newValue);
				popUp.setExpanded(false);
				if (isDirectUpdate()) {
					setTestProperty(Color.class, newValue);
				}
			}
		});
		popUp.setTarget(sampleColor);
		popUp.setPopUp(colorChooser);
	}

}
