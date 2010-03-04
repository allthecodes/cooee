
package org.karora.cooee.sandbox.consultas.webcontainer;

import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.ContentPane;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.FillImage;
import org.karora.cooee.app.FloatingPane;
import org.karora.cooee.app.Font;
import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.button.AbstractButton;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.sandbox.consultas.app.Overlay;
import org.karora.cooee.webcontainer.ActionProcessor;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.DomUpdateSupport;
import org.karora.cooee.webcontainer.PartialUpdateManager;
import org.karora.cooee.webcontainer.PartialUpdateParticipant;
import org.karora.cooee.webcontainer.PropertyUpdateProcessor;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webcontainer.SynchronizePeerFactory;
import org.karora.cooee.webcontainer.image.ImageRenderSupport;
import org.karora.cooee.webcontainer.propertyrender.ColorRender;
import org.karora.cooee.webcontainer.propertyrender.ExtentRender;
import org.karora.cooee.webcontainer.propertyrender.FillImageRender;
import org.karora.cooee.webcontainer.propertyrender.FontRender;
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
 * Synchronization peer for <code>org.karora.cooee.app.ContentPane</code> components.
 * <p>
 * This class should not be extended or used by classes outside of the
 * Echo framework.
 */
public class OverlayPeer 
implements ActionProcessor, ComponentSynchronizePeer, DomUpdateSupport, ImageRenderSupport, PropertyUpdateProcessor {

    private static final Extent EXTENT_0 = new Extent(0);
    
    private static final String IMAGE_ID_BACKGROUND = "background";

    /**
     * Service to provide supporting JavaScript library.
     */
    private static final Service OVERLAY_SERVICE = JavaScriptService.forResource("Echo.Overlay",
            "/org/karora/cooee/sandbox/consultas/webcontainer/resource/js/Overlay.js");

    static {
        WebRenderServlet.getServiceRegistry().add(OVERLAY_SERVICE);
    }
    
    private PartialUpdateManager partialUpdateManager; 
    
    /**
     * Default constructor.
     */
    public OverlayPeer() {
        super();
        partialUpdateManager = new PartialUpdateManager();
        partialUpdateManager.add(Overlay.PROPERTY_HORIZONTAL_SCROLL, new PartialUpdateParticipant() {
        
            public void renderProperty(RenderContext rc, ServerComponentUpdate update) {
                renderScrollDirective(rc, (Overlay) update.getParent(), true);
            }
        
            public boolean canRenderProperty(RenderContext rc, ServerComponentUpdate update) {
                return true;
            }
        });
        partialUpdateManager.add(Overlay.PROPERTY_VERTICAL_SCROLL, new PartialUpdateParticipant() {
        
            public void renderProperty(RenderContext rc, ServerComponentUpdate update) {
                renderScrollDirective(rc, (Overlay) update.getParent(), false);
            }
        
            public boolean canRenderProperty(RenderContext rc, ServerComponentUpdate update) {
                return true;
            }
        });
    }
    
    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#getContainerId(org.karora.cooee.app.Component)
     */
    public String getContainerId(Component child) {
        String parentId = ContainerInstance.getElementId(child.getParent());
        return parentId + "_container_" + ContainerInstance.getElementId(child);
    }
    
    /**
     * @see org.karora.cooee.webcontainer.image.ImageRenderSupport#getImage(org.karora.cooee.app.Component, java.lang.String)
     */
    public ImageReference getImage(Component component, String imageId) {
        if (IMAGE_ID_BACKGROUND.equals(imageId)) {
            FillImage backgroundImage 
                    = (FillImage) component.getRenderProperty(AbstractButton.PROPERTY_BACKGROUND_IMAGE);
            if (backgroundImage == null) {
                return null;
            } else {
                return backgroundImage.getImage();
            }
        } else {
            return null;
        }
    }

    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderAdd(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, java.lang.String, org.karora.cooee.app.Component)
     */
    public void renderAdd(RenderContext rc, ServerComponentUpdate update, String targetId, Component component) {
        Element domAddElement = DomUpdate.renderElementAdd(rc.getServerMessage());
        DocumentFragment htmlFragment = rc.getServerMessage().getDocument().createDocumentFragment();
        renderHtml(rc, update, htmlFragment, component);
        DomUpdate.renderElementAddContent(rc.getServerMessage(), domAddElement, targetId, htmlFragment);
    }

    /**
     * Renders child components which were added to a 
     * <code>ContentPane</code>, as described in the provided 
     * <code>ServerComponentUpdate</code>.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param update the update
     */
    private void renderAddChildren(RenderContext rc, ServerComponentUpdate update) {
        Component component = update.getParent();
        String elementId = ContainerInstance.getElementId(component);
        Component[] components = update.getParent().getVisibleComponents();
        Component[] addedChildren = update.getAddedChildren();
        
        for (int componentIndex = components.length - 1; componentIndex >= 0; --componentIndex) {
            boolean childFound = false;
            for (int addedChildrenIndex = 0; !childFound && addedChildrenIndex < addedChildren.length; ++addedChildrenIndex) {
                if (addedChildren[addedChildrenIndex] == components[componentIndex]) {
                    Element domAddElement = DomUpdate.renderElementAdd(rc.getServerMessage());
                    DocumentFragment htmlFragment = rc.getServerMessage().getDocument().createDocumentFragment();
                    renderChild(rc, update, htmlFragment, component, components[componentIndex]);
                    
                    if (componentIndex == components.length - 1) {
                        DomUpdate.renderElementAddContent(rc.getServerMessage(), domAddElement, elementId, htmlFragment);
                    } else {
                        DomUpdate.renderElementAddContent(rc.getServerMessage(), domAddElement, 
                                elementId, getContainerId(components[componentIndex + 1]), htmlFragment);
                    }

                    childFound = true;
                }
            }
        }
    }
    
    /**
     * Renders an individual child component of the <code>ContentPane</code>.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param update the <code>ServerComponentUpdate</code> being performed
     * @param parentNode the outer &lt;div&gt; element of the 
     *        <code>ContentPane</code>
     * @param child the child <code>Component</code> to be rendered
     */
    private void renderChild(RenderContext rc, ServerComponentUpdate update, Node parentNode, 
            Component component, Component child) {
        Element containerDivElement = parentNode.getOwnerDocument().createElement("div");
        String containerId = getContainerId(child);
        containerDivElement.setAttribute("id", containerId);
        if (!(child instanceof FloatingPane)) {
            containerDivElement.setAttribute("style", "position:absolute;overflow:hidden;");
        }
        parentNode.appendChild(containerDivElement);
        ComponentSynchronizePeer syncPeer = SynchronizePeerFactory.getPeerForComponent(child.getClass());
        if (syncPeer instanceof DomUpdateSupport) {
            ((DomUpdateSupport) syncPeer).renderHtml(rc, update, containerDivElement, child);
        } else {
            syncPeer.renderAdd(rc, update, containerId, child);
        }
    }
    
    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderDispose(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, org.karora.cooee.app.Component)
     */
    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {
        rc.getServerMessage().addLibrary(OVERLAY_SERVICE.getId());
        renderDisposeDirective(rc, (Overlay) component);
    }
    
    /**
     * Renders a directive to the outgoing <code>ServerMessage</code> to 
     * dispose the state of a <code>ContentPane</code>, performing tasks such as
     * unregistering event listeners on the client.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param contentPane the <code>ContentPane</code>
     */
    private void renderDisposeDirective(RenderContext rc, Overlay overlay) {
        ServerMessage serverMessage = rc.getServerMessage();
        Element itemizedUpdateElement = serverMessage.getItemizedDirective(ServerMessage.GROUP_ID_PREREMOVE,
                "EchoOverlay.MessageProcessor", "dispose",  new String[0], new String[0]);
        Element itemElement = serverMessage.getDocument().createElement("item");
        itemElement.setAttribute("eid", ContainerInstance.getElementId(overlay));
        itemizedUpdateElement.appendChild(itemElement);
    }
    
    /**
     * @see org.karora.cooee.webcontainer.DomUpdateSupport#renderHtml(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, org.w3c.dom.Node, org.karora.cooee.app.Component)
     */
    public void renderHtml(RenderContext rc, ServerComponentUpdate update, Node parentNode, Component component) {
        Overlay overlay = (Overlay) component;
        
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(OVERLAY_SERVICE.getId());

        Document document = parentNode.getOwnerDocument();
        Element divElement = document.createElement("div");
        divElement.setAttribute("id", ContainerInstance.getElementId(component));
        
        CssStyle cssStyle = new CssStyle();
        cssStyle.setAttribute("position", "absolute");
        ExtentRender.renderToStyle(cssStyle, Overlay.PROPERTY_WIDTH,(Extent)overlay.getRenderProperty(Overlay.PROPERTY_WIDTH));
        ExtentRender.renderToStyle(cssStyle, Overlay.PROPERTY_HEIGHT,(Extent)overlay.getRenderProperty(Overlay.PROPERTY_HEIGHT));
        ExtentRender.renderToStyle(cssStyle, "top",(Extent)overlay.getRenderProperty(Overlay.PROPERTY_POSITION_X));
        ExtentRender.renderToStyle(cssStyle, "left",(Extent)overlay.getRenderProperty(Overlay.PROPERTY_POSITION_Y));
        
        if (overlay.getRenderProperty(Overlay.PROPERTY_OPACITY) != null && ((Integer)overlay.getRenderProperty(Overlay.PROPERTY_OPACITY)).intValue() > 0)
        {
        	cssStyle.setAttribute("filter", "alpha(opacity="+overlay.getRenderProperty(Overlay.PROPERTY_OPACITY)+")");
        	cssStyle.setAttribute("-moz-opacity", "0."+overlay.getRenderProperty(Overlay.PROPERTY_OPACITY));
        }
                
        cssStyle.setAttribute("overflow", "auto");
        if (overlay.hasActionListeners()) {
        	cssStyle.setAttribute("cursor", "pointer");
        }
        ColorRender.renderToStyle(cssStyle, (Color) overlay.getRenderProperty(Overlay.PROPERTY_FOREGROUND),
                (Color) overlay.getRenderProperty(Overlay.PROPERTY_BACKGROUND));
        FontRender.renderToStyle(cssStyle, (Font) overlay.getRenderProperty(Overlay.PROPERTY_FONT));
        FillImageRender.renderToStyle(cssStyle, rc, this, overlay, IMAGE_ID_BACKGROUND, 
                (FillImage) overlay.getRenderProperty(Overlay.PROPERTY_BACKGROUND_IMAGE), 0);
        divElement.setAttribute("style", cssStyle.renderInline());
        
        parentNode.appendChild(divElement);

        // Render initialization directive.
        renderInitDirective(rc, overlay);
        
        Component[] children = overlay.getVisibleComponents();
        for (int i = 0; i < children.length; ++i) {
            renderChild(rc, update, divElement, component, children[i]);
        }
    }

    /**
     * Renders a directive to the outgoing <code>ServerMessage</code> to 
     * initialize the state of a <code>ContentPane</code>, performing tasks 
     * such as registering event listeners on the client.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param contentPane the <code>ContentPane</code>
     */
    private void renderInitDirective(RenderContext rc, Overlay overlay) {
        String elementId = ContainerInstance.getElementId(overlay);
        ServerMessage serverMessage = rc.getServerMessage();

        Element itemizedUpdateElement = serverMessage.getItemizedDirective(ServerMessage.GROUP_ID_POSTUPDATE,
                "EchoOverlay.MessageProcessor", "init", new String[0], new String[0]);
        Element itemElement = serverMessage.getDocument().createElement("item");
        itemElement.setAttribute("eid", elementId);
        Extent horizontalScroll = (Extent) overlay.getRenderProperty(ContentPane.PROPERTY_HORIZONTAL_SCROLL);
        if (horizontalScroll != null && horizontalScroll.getValue() != 0) {
            itemElement.setAttribute("horizontal-scroll", ExtentRender.renderCssAttributeValue(horizontalScroll));
        }
        Extent verticalScroll = (Extent) overlay.getRenderProperty(ContentPane.PROPERTY_VERTICAL_SCROLL);
        if (verticalScroll != null && verticalScroll.getValue() != 0) {
            itemElement.setAttribute("vertical-scroll", ExtentRender.renderCssAttributeValue(verticalScroll));
        }
        if (!overlay.hasActionListeners()) {
            itemElement.setAttribute("server-notify", "false");
        }
        itemizedUpdateElement.appendChild(itemElement);
    }
    
    /**
     * Renders a directive to the outgoing <code>ServerMessage</code> to update
     * the scroll bar positions of a <code>ContentPane</code>.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param contentPane the <code>ContentPane</code>
     * @param horizontal a flag indicating whether the horizontal (true) or 
     *        vertical (false) scroll bar position should be updated
     */
    private void renderScrollDirective(RenderContext rc, Overlay overlay, boolean horizontal) {
        ServerMessage serverMessage = rc.getServerMessage();
        Element scrollElement = 
                serverMessage.appendPartDirective(ServerMessage.GROUP_ID_POSTUPDATE, "EchoOverlay.MessageProcessor",
                horizontal ? "scroll-horizontal" : "scroll-vertical");
        Extent position = (Extent) overlay.getRenderProperty(
                horizontal ? Overlay.PROPERTY_HORIZONTAL_SCROLL : Overlay.PROPERTY_VERTICAL_SCROLL, EXTENT_0);
        scrollElement.setAttribute("eid", ContainerInstance.getElementId(overlay));
        scrollElement.setAttribute("position", ExtentRender.renderCssAttributeValue(position));
    }

    /**
     * Renders removal operations for child components which were removed from 
     * a <code>ContentPane</code>, as described in the provided 
     * <code>ServerComponentUpdate</code>.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param update the update
     */
    private void renderRemoveChildren(RenderContext rc, ServerComponentUpdate update) {
        Component[] removedChildren = update.getRemovedChildren();
        for (int i = 0; i < removedChildren.length; ++i) {
            DomUpdate.renderElementRemove(rc.getServerMessage(), 
                    ContainerInstance.getElementId(update.getParent()) + "_container_" +
                    ContainerInstance.getElementId(removedChildren[i]));
        }
    }
    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderUpdate(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, java.lang.String)
     */
    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
        boolean fullReplace = false;
        if (update.hasUpdatedLayoutDataChildren()) {
            fullReplace = true;
        } else if (update.hasUpdatedProperties()) {
            if (!partialUpdateManager.canProcess(rc, update)) {
                fullReplace = true;
            }
        }
        
        if (fullReplace) {
            // Perform full update.
            DomUpdate.renderElementRemove(rc.getServerMessage(), 
                    ContainerInstance.getElementId(update.getParent()));
            renderAdd(rc, update, targetId, update.getParent());
        } else {
            partialUpdateManager.process(rc, update);
            renderRemoveChildren(rc, update);
            renderAddChildren(rc, update);
        }
        return fullReplace;
    }

    /**
     * @see org.karora.cooee.webcontainer.PropertyUpdateProcessor#processPropertyUpdate(
     *      org.karora.cooee.webcontainer.ContainerInstance,
     *      org.karora.cooee.app.Component, org.w3c.dom.Element)
     */
    public void processPropertyUpdate(ContainerInstance ci, Component component, Element propertyElement) {
        if ("horizontalScroll".equals(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_NAME))) {
            Extent newValue = ExtentRender.toExtent(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)); 
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, 
                    ContentPane.PROPERTY_HORIZONTAL_SCROLL, newValue);
        } else if ("verticalScroll".equals(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_NAME))) {
            Extent newValue = ExtentRender.toExtent(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)); 
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, 
                    ContentPane.PROPERTY_VERTICAL_SCROLL, newValue);
        } 
    }
    
    public void processAction(ContainerInstance ci, Component component, Element actionElement) {
        ci.getUpdateManager().getClientUpdateManager().setComponentAction(component, AbstractButton.INPUT_CLICK, null);
    }
}
