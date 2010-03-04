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

import java.io.IOException;

import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.sandbox.tucana.app.ModalDimmer;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webcontainer.image.ImageRenderSupport;
import org.karora.cooee.webcontainer.image.ImageTools;
import org.karora.cooee.webcontainer.propertyrender.ColorRender;
import org.karora.cooee.webrender.Connection;
import org.karora.cooee.webrender.ContentType;
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.servermessage.DomUpdate;
import org.karora.cooee.webrender.service.JavaScriptService;
import org.w3c.dom.Element;


/**
 * WebRender peer class for {@link ModalDimmer}.
 * @author Jeremy Volkman
 *
 */
public class ModalDimmerPeer implements ComponentSynchronizePeer, ImageRenderSupport {

    /**
     * Service to provide supporting JavaScript library.
     */
    private static final Service MODAL_DIMMER_SERVICE = JavaScriptService.forResource("Tucana.ModalDimmer",
            "/org/karora/cooee/sandbox/tucana/webcontainer/resource/js/ModalDimmer.js");

    private static final String BLANK_HTML_STRING = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
        + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
        + "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title></title><body></body></html>";

    private static final Service BLANK_HTML_SERVICE = new Service() {

        /**
         * @see nextapp.echo2.webrender.Service#getId()
         */
        public String getId() {
            return "Tucana.ModalDimmer.IFrame";
        }
    
        /**
         * @see nextapp.echo2.webrender.Service#getVersion()
         */
        public int getVersion() {
            return 0;
        }
    
        /**
         * @see nextapp.echo2.webrender.Service#service(nextapp.echo2.webrender.Connection)
         */
        public void service(Connection conn) throws IOException {
            conn.setContentType(ContentType.TEXT_HTML);
            conn.getWriter().write(BLANK_HTML_STRING);
        }
    };
    
    static {
        WebRenderServlet.getServiceRegistry().add(MODAL_DIMMER_SERVICE);
        WebRenderServlet.getServiceRegistry().add(BLANK_HTML_SERVICE);
    }
    
    public String getContainerId(Component child) {
        return null;
    }

    public void renderAdd(RenderContext rc, ServerComponentUpdate update, String targetId, Component component) {
        rc.getServerMessage().addLibrary(MODAL_DIMMER_SERVICE.getId());
        renderInitDirective(rc, component);
    }

    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {
        rc.getServerMessage().addLibrary(MODAL_DIMMER_SERVICE.getId());
        renderDisposeDirective(rc, component);
    }

    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
        DomUpdate.renderElementRemove(rc.getServerMessage(), ContainerInstance.getElementId(update.getParent()));
        renderAdd(rc, update, targetId, update.getParent());
        return false;
    }
    
    private void renderToStringProperty(Element propertiesElement, Component component, String propertyName) {
        Object property = component.getProperty(propertyName);
        if (property != null) {
            Element propertyElement = propertiesElement.getOwnerDocument().createElement("property");
            propertyElement.setAttribute("name", propertyName);
            propertyElement.setAttribute("value", property.toString());
            propertiesElement.appendChild(propertyElement);
        }
    }

    private void renderInitDirective(RenderContext rc, Component component) {
        ServerMessage serverMessage = rc.getServerMessage();
        Element partElement = serverMessage.addPart(ServerMessage.GROUP_ID_UPDATE, "TucanaModalDimmer.MessageProcessor");
        Element initElement = serverMessage.getDocument().createElement("init");
        String elementId = ContainerInstance.getElementId(component);
        initElement.setAttribute("eid", elementId);
        Element propertiesElement = serverMessage.getDocument().createElement("properties");
        initElement.appendChild(propertiesElement);

        Integer type = (Integer) component.getProperty(ModalDimmer.PROPERTY_DIM_TYPE);
        if (type != null) {
            String strType = null;
            switch(type.intValue()) {
                case ModalDimmer.DIM_TYPE_OPACITY:
                    strType = "opacity";
                    break;
                case ModalDimmer.DIM_TYPE_IMAGE:
                    strType = "image";
                    break;
                case ModalDimmer.DIM_TYPE_AUTO:
                    strType = "auto";
                    break;
            }
            
            if (strType != null) {
                Element propertyElement = serverMessage.getDocument().createElement("property");
                propertyElement.setAttribute("name", ModalDimmer.PROPERTY_DIM_TYPE);
                propertyElement.setAttribute("value", strType);
                propertiesElement.appendChild(propertyElement);
            }
        }

        renderToStringProperty(propertiesElement, component, ModalDimmer.PROPERTY_DIM_OPACITY);
        renderToStringProperty(propertiesElement, component, ModalDimmer.PROPERTY_DIM_ANIMATED);
        renderToStringProperty(propertiesElement, component, ModalDimmer.PROPERTY_DIM_ANIMATION_SPEED);
        
        Color color = (Color) component.getProperty(ModalDimmer.PROPERTY_DIM_COLOR);
        if (color != null) {
            Element propertyElement = serverMessage.getDocument().createElement("property");
            propertyElement.setAttribute("name", ModalDimmer.PROPERTY_DIM_COLOR);
            propertyElement.setAttribute("value", ColorRender.renderCssAttributeValue(color));
            propertiesElement.appendChild(propertyElement);
        }
        
        String imageUri = ImageTools.getUri(rc, this, component, ModalDimmer.PROPERTY_DIM_IMAGE);
        if (imageUri != null) {
            Element propertyElement = serverMessage.getDocument().createElement("property");
            propertyElement.setAttribute("name", ModalDimmer.PROPERTY_DIM_IMAGE);
            propertyElement.setAttribute("value", imageUri);
            propertiesElement.appendChild(propertyElement);
        }
        
        partElement.appendChild(initElement);
    }

    private void renderDisposeDirective(RenderContext rc, Component component) {
        ServerMessage serverMessage = rc.getServerMessage();
        Element partElement = serverMessage.addPart(ServerMessage.GROUP_ID_UPDATE, "TucanaModalDimmer.MessageProcessor");
        Element disposeElement = serverMessage.getDocument().createElement("dispose");
        String elementId = ContainerInstance.getElementId(component);
        disposeElement.setAttribute("eid", elementId);
        partElement.appendChild(disposeElement);
    }

    public ImageReference getImage(Component component, String imageId) {
        if (ModalDimmer.PROPERTY_DIM_IMAGE.equals(imageId)) {
            return (ImageReference) component.getProperty(imageId);
        } else {
            return null;
        }
    }
}
