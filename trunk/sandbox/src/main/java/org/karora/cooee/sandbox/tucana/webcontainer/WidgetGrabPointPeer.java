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
import org.karora.cooee.sandbox.tucana.app.widgetdash.WidgetGrabPoint;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.DomUpdateSupport;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webcontainer.SynchronizePeerFactory;
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.output.CssStyle;
import org.karora.cooee.webrender.servermessage.DomUpdate;
import org.karora.cooee.webrender.service.JavaScriptService;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Synchronization peer for <code>nextapp.echo2.contrib.dnd.DragSource</code> components.
 * <p>
 */
public class WidgetGrabPointPeer 
implements ComponentSynchronizePeer, DomUpdateSupport {
    
    /**
     * Service to provide supporting JavaScript library.
     */
    private static final Service WIDGET_GRAB_SERVICE = JavaScriptService.forResource("Tucana.WidgetGrabPoint",
            "/org/karora/cooee/sandbox/tucana/webcontainer/resource/js/WidgetGrabPoint.js");

    static {
        WebRenderServlet.getServiceRegistry().add(WIDGET_GRAB_SERVICE);
    }

	
    /**
     * Default constructor.
     */
    public WidgetGrabPointPeer() {
    }
    
    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#getContainerId(nextapp.echo2.app.Component)
     */
    public String getContainerId(Component child) {
        return ContainerInstance.getElementId(child.getParent());
    }


    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderAdd(nextapp.echo2.webcontainer.RenderContext, 
     *      nextapp.echo2.app.update.ServerComponentUpdate, java.lang.String, nextapp.echo2.app.Component)
     */
    public void renderAdd(RenderContext rc, ServerComponentUpdate update, String targetId, Component component) {
        Element domAddElement = DomUpdate.renderElementAdd(rc.getServerMessage());
        DocumentFragment htmlFragment = rc.getServerMessage().getDocument().createDocumentFragment();
        renderHtml(rc, update, htmlFragment, component);
        DomUpdate.renderElementAddContent(rc.getServerMessage(), domAddElement, targetId, htmlFragment);
    }
    
    /**
     * Renders a child component.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param update the update
     * @param parentElement the HTML element which should contain the child
     * @param child the child component to render
     */
    private void renderAddChild(RenderContext rc, ServerComponentUpdate update, Element parentElement, Component child) {
        ComponentSynchronizePeer syncPeer = SynchronizePeerFactory.getPeerForComponent(child.getClass());
        if (syncPeer instanceof DomUpdateSupport) {
            ((DomUpdateSupport) syncPeer).renderHtml(rc, update, parentElement, child);
        } else {
            syncPeer.renderAdd(rc, update, getContainerId(child), child);
        }
    }
    
    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderDispose(nextapp.echo2.webcontainer.RenderContext,
     *      nextapp.echo2.app.update.ServerComponentUpdate,
     *      nextapp.echo2.app.Component)
     */
    public void renderDispose(RenderContext rc, ServerComponentUpdate update,
            Component component) {
    	WidgetGrabPoint grabPoint = (WidgetGrabPoint) component;
    	
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(WIDGET_GRAB_SERVICE.getId());

        Element partElement = serverMessage.addPart(ServerMessage.GROUP_ID_UPDATE, "TucanaWidgetGrabPoint.MessageProcessor");
        Element initElement = serverMessage.getDocument().createElement("dispose");

        String elementId = ContainerInstance.getElementId(grabPoint);
        
        initElement.setAttribute("eid", elementId);
        
        partElement.appendChild(initElement);
    }

    /**
     * @see nextapp.echo2.webcontainer.DomUpdateSupport#renderHtml(nextapp.echo2.webcontainer.RenderContext, 
     *      nextapp.echo2.app.update.ServerComponentUpdate, org.w3c.dom.Node, nextapp.echo2.app.Component)
     */
    public void renderHtml(RenderContext rc, ServerComponentUpdate update, Node parentNode, Component component) {
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(WIDGET_GRAB_SERVICE.getId());

    	WidgetGrabPoint grabPoint = (WidgetGrabPoint) component;
    	
    	String elementId = ContainerInstance.getElementId(grabPoint);
        Document document = parentNode.getOwnerDocument();
        
        Element divElement = document.createElement("div");
        divElement.setAttribute("id", elementId);
        divElement.setAttribute("class", "widgetGrabPoint");
        CssStyle style = new CssStyle();
        style.setAttribute("cursor", (String) component.getProperty(WidgetGrabPoint.PROPERTY_CURSOR));
        divElement.setAttribute("style", style.renderInline());
        
        parentNode.appendChild(divElement);

        renderInitDirective(rc, component);
        
        renderAddChild(rc, update, divElement, grabPoint.getGrabbable());
    }
    
    private void renderInitDirective(RenderContext rc, Component component) {
    	WidgetGrabPoint grabPoint = (WidgetGrabPoint) component;
    	
        ServerMessage serverMessage = rc.getServerMessage();
        Element partElement = serverMessage.addPart(ServerMessage.GROUP_ID_UPDATE, "TucanaWidgetGrabPoint.MessageProcessor");
        Element initElement = serverMessage.getDocument().createElement("init");

        String elementId = ContainerInstance.getElementId(grabPoint);
        String targetId = ContainerInstance.getElementId(grabPoint.getWidgetContainer());
        
        initElement.setAttribute("eid", elementId);
        initElement.setAttribute("widgetcontainer-eid", targetId);
        
        partElement.appendChild(initElement);
    }
    
    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderUpdate(nextapp.echo2.webcontainer.RenderContext, 
     *      nextapp.echo2.app.update.ServerComponentUpdate, java.lang.String)
     */
    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
        DomUpdate.renderElementRemove(rc.getServerMessage(), ContainerInstance.getElementId(update.getParent()));
        renderAdd(rc, update, targetId, update.getParent());
        return true;
    }
}
