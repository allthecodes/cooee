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

package org.karora.cooee.webcontainer;

import org.karora.cooee.app.Component;
import org.w3c.dom.Element;



/**
 * An optional interface for <code>ComponentSynchronizePeer</code>s that provides
 * the capability to receive <strong>IMMEDIATE</strong> notification of
 * state updates from the client. 
 * This interface must be implemented by any component that creates
 * "EchoAction" message parts on the client in a ClientMessage.
 * 
 * @see org.karora.cooee.webcontainer.PropertyUpdateProcessor
 */
public interface ActionProcessor {
    
    /**
     * The attribute name of XML 'property' elements that specifies
     * the name of the property.
     */
    public static final String ACTION_NAME = "name";

    /**
     * The attribute name of XML 'property' elements that specifies
     * the value of the property.  Property elements may instead
     * pass property data in child XML elements, and in such cases
     * should not have a 'value' attribute.
     */
    public static final String ACTION_VALUE = "value";

    /**
     * Notifies the <code>ComponentSynchronizePeer</code> that a client action 
     * has occurred.
     * 
     * @param ci the relevant <code>ContainerInstance</code>
     * @param component the target <code>Component</code>
     * @param actionElement the XML element describing the action
     *        (the name and value of the action may be obtained
     *        by querying the <code>ACTION_NAME</code> and 
     *        <code>ACTION_VALUE</code> attribute values. 
     */
    public void processAction(ContainerInstance ci, Component component, Element actionElement);
}
