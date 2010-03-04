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
package org.karora.cooee.ng.model;

import org.karora.cooee.app.button.ButtonModel;

/**
 * ButtonModelEx is an extension of ButtonModel that raises
 * <code>ActionEventEx</code> event objects. The contain the meta key
 * information that was present when the button was pressed.
 * 
 */
public interface ButtonModelEx extends ButtonModel {

	/**
	 * This raise an <code>ActionEventEx</code> that containes meta key
	 * information that was in play when the event raised.
	 * 
	 * @param metaKeyInfo -
	 *            The metaKey information in play when the event was raised.
	 *            This can be one of the following
	 *            <ul>
	 *            <li>ActionEventEx.METAKEY_NONE - no meta keys were pressed
	 *            during the action event</li>
	 *            <li>ActionEventEx.METAKEY_ALT - the 'alt' key was pressed
	 *            during the action event</li>
	 *            <li>ActionEventEx.METAKEY_SHIFT - the 'shift' key was pressed
	 *            during the action event</li>
	 *            <li>ActionEventEx.METAKEY_CONTROL - the 'control' key was
	 *            pressed during the action event</li>
	 *            <li>ActionEventEx.METAKEY_META - the 'meta' key was pressed
	 *            during the action event</li>
	 *            </ul>
	 * @see org.karora.cooee.app.button.DefaultButtonModel#doAction()
	 * @see ActionEventEx
	 */
	public void doAction(int metaKeyInfo);

}