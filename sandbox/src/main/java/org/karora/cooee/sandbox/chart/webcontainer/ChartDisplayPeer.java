/* 
 * This file is part of the Echo2 Chart Library.
 * Copyright (C) 2005 NextApp, Inc.
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

package org.karora.cooee.sandbox.chart.webcontainer;

import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.sandbox.chart.app.ChartDisplay;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.DomUpdateSupport;
import org.karora.cooee.webcontainer.PartialUpdateManager;
import org.karora.cooee.webcontainer.PartialUpdateParticipant;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webcontainer.RenderState;
import org.karora.cooee.webcontainer.propertyrender.ExtentRender;
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.servermessage.DomUpdate;
import org.karora.cooee.webrender.service.JavaScriptService;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <code>ComponentSynchronizePeer</code> implementation for 
 * <code>ChartDisplay</code> components.
 */
public class ChartDisplayPeer 
implements ComponentSynchronizePeer, DomUpdateSupport {

    protected static final int DEFAULT_WIDTH = 400;
    protected static final int DEFAULT_HEIGHT = 300;
 
    /**
     * Service to provide supporting JavaScript library.
     */
    private static final Service CHART_DISPLAY_SERVICE = JavaScriptService.forResource("Echo2Chart.ChartDisplay", 
            "/org/karora/cooee/sandbox/chart/webcontainer/resource/js/ChartDisplay.js");
    
    static {
        WebRenderServlet.getServiceRegistry().add(CHART_DISPLAY_SERVICE);
    }

    /**
     * <code>RenderState</code> implementation (stores generated image 
     * to be rendered).
     */
    private class ChartRenderState 
    implements RenderState {
        
        private int version;
    }
    
    private PartialUpdateManager partialUpdateManager;
    
    /**
     * <code>PartialUpdateParticipant</code> to handle updates to chart requiring graphic image to be updated.
     */
    private PartialUpdateParticipant chartContentChangedUpdateParticipant = new PartialUpdateParticipant() {

        /**
         * @see org.karora.cooee.webcontainer.PartialUpdateParticipant#renderProperty(org.karora.cooee.webcontainer.RenderContext,
         *      org.karora.cooee.app.update.ServerComponentUpdate)
         */
        public void renderProperty(RenderContext rc, ServerComponentUpdate update) {
            renderSetImageDirective(rc, (ChartDisplay) update.getParent());
        }

        /**
         * @see org.karora.cooee.webcontainer.PartialUpdateParticipant#canRenderProperty(org.karora.cooee.webcontainer.RenderContext,
         *      org.karora.cooee.app.update.ServerComponentUpdate)
         */
        public boolean canRenderProperty(RenderContext rc, ServerComponentUpdate update) {
            return true;
        }
    };
    
    /**
     * Default constructor.
     */
    public ChartDisplayPeer() {
        partialUpdateManager = new PartialUpdateManager();
        partialUpdateManager.add(ChartDisplay.CHART_CONTENT_CHANGED_PROPERTY, chartContentChangedUpdateParticipant);
    }

    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#getContainerId(org.karora.cooee.app.Component)
     */
    public String getContainerId(Component child) {
        throw new UnsupportedOperationException("ChartDisplay component does not support children.");
    }

    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderAdd(org.karora.cooee.webcontainer.RenderContext,
     *      org.karora.cooee.app.update.ServerComponentUpdate, java.lang.String,
     *      org.karora.cooee.app.Component)
     */
    public void renderAdd(RenderContext rc, ServerComponentUpdate update, String targetId, Component component) {
        Element domAddElement = DomUpdate.renderElementAdd(rc.getServerMessage());
        DocumentFragment htmlFragment = rc.getServerMessage().getDocument().createDocumentFragment();
        renderHtml(rc, update, htmlFragment, component);
        DomUpdate.renderElementAddContent(rc.getServerMessage(), domAddElement, targetId, htmlFragment);
    }
    
    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderDispose(org.karora.cooee.webcontainer.RenderContext,
     *      org.karora.cooee.app.update.ServerComponentUpdate,
     *      org.karora.cooee.app.Component)
     */
    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {
        // Do nothing.
    }

    /**
     * @see org.karora.cooee.webcontainer.DomUpdateSupport#renderHtml(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, org.w3c.dom.Node, org.karora.cooee.app.Component)
     */
    public void renderHtml(RenderContext rc, ServerComponentUpdate update, Node parentNode, Component component) {
        ChartDisplay chartDisplay = (ChartDisplay) component;
        synchronized (chartDisplay) {
            Document document = rc.getServerMessage().getDocument();
            String elementId = ContainerInstance.getElementId(chartDisplay);
            
            Element divElement = document.createElement("div");
            divElement.setAttribute("id", elementId);
            
            if (chartDisplay.getChart() != null) {
                int width = ExtentRender.toPixels((Extent) chartDisplay.getRenderProperty(ChartDisplay.PROPERTY_WIDTH), 
                        DEFAULT_WIDTH);
                int height = ExtentRender.toPixels((Extent) chartDisplay.getRenderProperty(ChartDisplay.PROPERTY_HEIGHT), 
                        DEFAULT_HEIGHT);
                int version = incrementImageVersion(rc, chartDisplay);
                Element imgElement = document.createElement("img");
                imgElement.setAttribute("id", elementId + "_image");
                imgElement.setAttribute("src", ChartImageService.getUri(rc, chartDisplay, version));
                imgElement.setAttribute("style", "width:" + width + "px;height:" + height + "px;");
                divElement.appendChild(imgElement);
            }
    
            parentNode.appendChild(divElement);
        }
    }
    
    private int incrementImageVersion(RenderContext rc, ChartDisplay chartDisplay) {
        ChartRenderState renderState = (ChartRenderState) rc.getContainerInstance().getRenderState(chartDisplay);
        if (renderState == null) {
            renderState = new ChartRenderState();
            rc.getContainerInstance().setRenderState(chartDisplay, renderState);
        } else {
            ++renderState.version;
        }
        return renderState.version;
    }
    
    /**
     * Renders a 'set-image' directive to update the displayed chart on the 
     * client.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param chartDisplay the <code>ChartDisplay</code> to update
     */
    private void renderSetImageDirective(RenderContext rc, ChartDisplay chartDisplay) {
        rc.getServerMessage().addLibrary(CHART_DISPLAY_SERVICE.getId());
        int version = incrementImageVersion(rc, chartDisplay);
        Element setImageElement = rc.getServerMessage().appendPartDirective(ServerMessage.GROUP_ID_POSTUPDATE, 
                "Echo2Chart.MessageProcessor", "set-image");
        setImageElement.setAttribute("eid", ContainerInstance.getElementId(chartDisplay));
        setImageElement.setAttribute("new-image", ChartImageService.getUri(rc, chartDisplay, version));
    }

    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderUpdate(org.karora.cooee.webcontainer.RenderContext,
     *      org.karora.cooee.app.update.ServerComponentUpdate, java.lang.String)
     */
    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
        // Determine if fully replacing the component is required.
        if (partialUpdateManager.canProcess(rc, update)) {
            partialUpdateManager.process(rc, update);
        } else {
            DomUpdate.renderElementRemove(rc.getServerMessage(), ContainerInstance.getElementId(update.getParent()));
            renderAdd(rc, update, targetId, update.getParent());
        }
        return true;
    }
}
