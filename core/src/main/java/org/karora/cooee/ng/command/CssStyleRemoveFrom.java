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
package org.karora.cooee.ng.command;

import org.karora.cooee.app.Command;
import org.karora.cooee.app.Component;

/**
 * <code>CssStyleRemoveFrom</code> is used to remove a given CSS className
 * from the XHTML of a specified <code>targetComponent</code>.
 */
public class CssStyleRemoveFrom implements Command {

	private String className;

	private Component targetComponent;

	/**
	 * Constructs a <code>CssStyleRemoveFrom</code> Command that will
	 * remove a the given className to the components top level XHTML.
	 * @param className -
	 *            the CSS className to use
	 * @param targetComponent -
	 *            the targetComponent to remove this to on the client
	 */
	public CssStyleRemoveFrom(String className, Component targetComponent) {
		this.className = className;
		this.targetComponent = targetComponent;
	}

	/**
	 * @return Returns the className.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return Returns the targetComponent.
	 */
	public Component getTargetComponent() {
		return targetComponent;
	}
}
