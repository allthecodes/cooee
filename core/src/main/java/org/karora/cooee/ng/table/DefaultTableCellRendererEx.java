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
package org.karora.cooee.ng.table;

import org.karora.cooee.app.Component;
import org.karora.cooee.app.Table;
import org.karora.cooee.app.table.DefaultTableCellRenderer;
import org.karora.cooee.ng.xhtml.XhtmlFragment;

/**
 * A default implementation of <code>TableCellRendererEx</code>.
  */
public class DefaultTableCellRendererEx extends DefaultTableCellRenderer implements TableCellRendererEx {

	/**
	 * Constructs a <code>DefaultTableCellRendererEx</code>
	 */
	public DefaultTableCellRendererEx() {
		super();
	}
	/**
	 * Always returns null in this implementation.
	 *
	 * @see org.karora.cooee.app.table.DefaultTableCellRenderer#getTableCellRendererComponent(org.karora.cooee.app.Table, java.lang.Object, int, int)
	 */
	public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
		return null;
	}
	/**
	 * @see org.karora.cooee.ng.table.TableCellRendererEx#getTableCellRendererContent(Table, Object, int, int)
	 */
	public XhtmlFragment getTableCellRendererContent(Table table, Object value, int column, int row) {
		if (value == null) {
			return null;
		}
		return new XhtmlFragment(String.valueOf(value));
	}

	/**
	 * @see org.karora.cooee.ng.table.TableCellRendererEx#isActionCausingCells(org.karora.cooee.app.Table, int, int)
	 */
	public boolean isActionCausingCell(Table table, int column, int row) {
		return true;
	}
	
	/**
	 * @see org.karora.cooee.ng.table.TableCellRendererEx#isSelectionCausingCell(org.karora.cooee.app.Table, int, int)
	 */
	public boolean isSelectionCausingCell(Table table, int column, int row) {
		return true;
	}
}
