/* 
 * This file is part of the Tucana Echo2 Library.
 * Copyright (C) 2006.
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

package org.karora.cooee.sandbox.tucana.webcontainer;

import org.karora.cooee.app.Component;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.sandbox.tucana.app.widgetdash.WidgetContainer;
import org.karora.cooee.sandbox.tucana.app.widgetdash.WidgetPosition;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.DomUpdateSupport;
import org.karora.cooee.webcontainer.PropertyUpdateProcessor;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webcontainer.SynchronizePeerFactory;
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.servermessage.DomUpdate;
import org.karora.cooee.webrender.service.JavaScriptService;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



public class WidgetContainerPeer implements ComponentSynchronizePeer, DomUpdateSupport, PropertyUpdateProcessor {

    public static final String PROPERTY_POSITION = "widgetPosition";

    /**
     * Service to provide supporting JavaScript library.
     */
    private static final Service WIDGET_CONTAINER_SERVICE = JavaScriptService.forResource("Tucana.WidgetContainer",
            "/org/karora/cooee/sandbox/tucana/webcontainer/resource/js/WidgetContainer.js");

    static {
        WebRenderServlet.getServiceRegistry().add(WIDGET_CONTAINER_SERVICE);
    }

    
    public String getContainerId(Component child) {
        return ContainerInstance.getElementId(child.getParent());
    }

    public void renderAdd(RenderContext rc, ServerComponentUpdate update, String targetId, Component component) {
        Element domAddElement = DomUpdate.renderElementAdd(rc.getServerMessage());
        DocumentFragment htmlFragment = rc.getServerMessage().getDocument().createDocumentFragment();
        renderHtml(rc, update, htmlFragment, component);
        DomUpdate.renderElementAddContent(rc.getServerMessage(), domAddElement, targetId, htmlFragment);
    }

    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(WIDGET_CONTAINER_SERVICE.getId());
        Element partElement = serverMessage.addPart(ServerMessage.GROUP_ID_UPDATE, "TucanaWidgetContainer.MessageProcessor");
        Element disposeElement = serverMessage.getDocument().createElement("dispose");

        String elementId = ContainerInstance.getElementId(component);
        
        disposeElement.setAttribute("eid", elementId);
        
        partElement.appendChild(disposeElement);
    }

    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
        DomUpdate.renderElementRemove(rc.getServerMessage(), ContainerInstance.getElementId(update.getParent()));
        renderAdd(rc, update, targetId, update.getParent());
        return false;
    }

    public void renderHtml(RenderContext rc, ServerComponentUpdate update, Node parentNode, Component component) {
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(WIDGET_CONTAINER_SERVICE.getId());
        
        Element divElement = parentNode.getOwnerDocument().createElement("div");
        parentNode.appendChild(divElement);
        
        String id = ContainerInstance.getElementId(component);
        divElement.setAttribute("id", id);
        divElement.setAttribute("class", "widgetContainer");
        divElement.setAttribute("style", "overflow: hidden;");
        
        renderInitDirective(rc, component);

        Component[] visibleChildren = component.getVisibleComponents();
        for (int i = 0; i < visibleChildren.length; i++) {
            renderAddChild(rc, update, divElement, visibleChildren[i]);
        }
    }

    /**
     * Renders a child component.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param update the update
     * @param parentNode the HTML element which should contain the child
     * @param child the child component to render
     */
    private void renderAddChild(RenderContext rc, ServerComponentUpdate update, Node parentNode, Component child) {
        ComponentSynchronizePeer syncPeer = SynchronizePeerFactory.getPeerForComponent(child.getClass());
        if (syncPeer instanceof DomUpdateSupport) {
            ((DomUpdateSupport) syncPeer).renderHtml(rc, update, parentNode, child);
        } else {
            syncPeer.renderAdd(rc, update, getContainerId(child), child);
        }
    }
    
    private void renderInitDirective(RenderContext rc, Component component) {
        ServerMessage serverMessage = rc.getServerMessage();
        Element partElement = serverMessage.addPart(ServerMessage.GROUP_ID_UPDATE, "TucanaWidgetContainer.MessageProcessor");
        Element initElement = serverMessage.getDocument().createElement("init");

        String elementId = ContainerInstance.getElementId(component);
        String targetId = ContainerInstance.getElementId(component.getParent());
        
        initElement.setAttribute("eid", elementId);
        initElement.setAttribute("widgetdash-eid", targetId);
        
        WidgetPosition position = (WidgetPosition) component.getProperty(WidgetContainer.PROPERTY_POSITION);
        if (position != null) {
            Element positionElement = serverMessage.getDocument().createElement("position");
            positionElement.setAttribute("column", Integer.toString(position.getColumn()));
            positionElement.setAttribute("column-position", Integer.toString(position.getColumnPosition()));
            initElement.appendChild(positionElement);
        }

        partElement.appendChild(initElement);
    }

    public void processPropertyUpdate(ContainerInstance ci, Component component, Element propertyElement) {
        String propertyName = propertyElement.getAttribute(PROPERTY_NAME);
        if (propertyName.equals(PROPERTY_POSITION)) {
            Element positionElement = (Element)propertyElement.getFirstChild();
            String column = positionElement.getAttribute("column");
            String columnPosition = positionElement.getAttribute("column-position");
            WidgetPosition position = null;
            try {
                position = new WidgetPosition(Integer.parseInt(column),
                        Integer.parseInt(columnPosition));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Cannot parse position update", e);
            }
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, 
                    WidgetContainer.PROPERTY_POSITION, position);
        }
    }

}
