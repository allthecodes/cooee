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

package org.karora.cooee.app.list;

import java.util.ArrayList;
import java.util.List;

/**
 * Default <code>ListModel</code> implementation.
 */
public class DefaultListModel extends AbstractListModel implements ModifiableListModel {

    private List items = new ArrayList();
    
    /**
     * Creates a new <code>DefaultSelectListModel</code> with the given 
     * content.
     */
    public DefaultListModel() {
        super(); 
    }
    
    /**
     * Creates a new <code>DefaultSelectListModel</code> containing the 
     * specified items
     *
     * @param itemArray the initial items
     */
    public DefaultListModel(Object[] itemArray) {
        this();
        
        for (int i = 0; i < itemArray.length; ++i) {
            add(itemArray[i]);
        }
    }
    
    /* (non-Javadoc)
	 * @see org.karora.cooee.app.list.ModifiableListModel#add(java.lang.Object)
	 */
    public void add(Object item) {
        items.add(item); 
        int index = items.size() - 1;
        fireIntervalAdded(index, index); 
    }
    
    /* (non-Javadoc)
	 * @see org.karora.cooee.app.list.ModifiableListModel#add(int, java.lang.Object)
	 */
    public void add(int index, Object item) {
        items.add(index, item);
        fireIntervalAdded(index, index); 
    }
    
    /* (non-Javadoc)
	 * @see org.karora.cooee.app.list.ModifiableListModel#get(int)
	 */
    public Object get(int index) {
        return items.get(index);
    }
    
    /* (non-Javadoc)
	 * @see org.karora.cooee.app.list.ModifiableListModel#indexOf(java.lang.Object)
	 */
    public int indexOf(Object item) {
        return items.indexOf(item);
    }
    
    /* (non-Javadoc)
	 * @see org.karora.cooee.app.list.ModifiableListModel#remove(int)
	 */
    public void remove(int index) {
        items.remove(index);
        fireIntervalRemoved(index, index); 
    }

    /* (non-Javadoc)
	 * @see org.karora.cooee.app.list.ModifiableListModel#remove(java.lang.Object)
	 */
    public void remove(Object item) {
        int index = items.indexOf(item);
        items.remove(item);
        fireIntervalRemoved(index, index); 
    }
    
    /* (non-Javadoc)
	 * @see org.karora.cooee.app.list.ModifiableListModel#removeAll()
	 */
    public void removeAll() {
        int size = items.size();
        if (size > 0) {
            items.clear();
            fireIntervalRemoved(0, size - 1);
        }
    }

    /* (non-Javadoc)
	 * @see org.karora.cooee.app.list.ModifiableListModel#size()
	 */
    public int size() {
        return items.size();
    }
}
