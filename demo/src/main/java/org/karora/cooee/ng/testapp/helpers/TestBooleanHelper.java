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

import org.karora.cooee.app.CheckBox;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.event.ChangeEvent;
import org.karora.cooee.app.event.ChangeListener;

/**
 * Does boolean tests
 *
 */
public class TestBooleanHelper extends TestAbstractHelper {
	
	CheckBox checkBox;

	/**
	 * @see org.karora.cooee.ng.testapp.helpers.TestHelper#getUI()
	 */
	public Component getUI() {
		return checkBox;
	}
	/**
	 * @see org.karora.cooee.ng.testapp.helpers.TestHelper#applyAsynchPropertyValue()
	 */
	public void applyAsynchPropertyValue() {
		if (getPropertyName() != null) {
			boolean newValue = checkBox.isSelected();
			setTestProperty(Boolean.TYPE, new Boolean(newValue));			
		}
	}
	
	/**
	 * @see org.karora.cooee.ng.testapp.helpers.TestHelper#applyInitialPropertyValue()
	 */
	public void applyInitialPropertyValue() {
		Boolean currentValue = (Boolean) getTestProperty(Boolean.TYPE);
		if (currentValue != null && currentValue.booleanValue()) {
			checkBox.setSelected(true);
		} else {
			checkBox.setSelected(false);
		}
	}
	

	/**
	 * @see org.karora.cooee.ng.testapp.helpers.TestHelper#init()
	 */
	public void init() {
		checkBox = new CheckBox(getTestDescription());
		//
		// get the intitial value
		if (getPropertyName() != null) {
			Boolean value = (Boolean) getTestProperty(Boolean.TYPE);
			boolean currentValue = (value != null ? value.booleanValue() : false);
			checkBox.setSelected(currentValue);
		}
		//
		// set up listeners
		if (isDirectUpdate()) {
			checkBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean newValue = checkBox.isSelected();
					setTestProperty(Boolean.TYPE, new Boolean(newValue));
				}
			});
		} else {
			checkBox.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					boolean newValue = checkBox.isSelected();
					setTestProperty(Boolean.TYPE, new Boolean(newValue));
				}
			});			
		}
	}

	/**
	 * @see org.karora.cooee.ng.testapp.helpers.TestHelper#getTestValue()
	 */
	public Object getTestValue() {
		return new Boolean(checkBox.isSelected());
	}
	
	public boolean booleanValue() {
		return checkBox.isSelected();
	}

	public void setValue(boolean newValue) {
		checkBox.setSelected(newValue);
	}

	/**
	 * @see org.karora.cooee.ng.testapp.helpers.TestHelper#setTestValue(java.lang.Object)
	 */
	public void setTestValue(Object value) {
		if (value instanceof Boolean) {
			checkBox.setSelected(((Boolean) value).booleanValue());
		} else {
			checkBox.setSelected(false);
		}
	}

}
