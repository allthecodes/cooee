
package org.karora.cooee.sandbox.consultas.webcontainer;

import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.FillImage;
import org.karora.cooee.app.FillImageBorder;
import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.button.AbstractButton;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.sandbox.consultas.app.DragableOverlay;
import org.karora.cooee.webcontainer.ActionProcessor;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.PartialUpdateManager;
import org.karora.cooee.webcontainer.PropertyUpdateProcessor;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webcontainer.SynchronizePeerFactory;
import org.karora.cooee.webcontainer.image.ImageRenderSupport;
import org.karora.cooee.webcontainer.propertyrender.ColorRender;
import org.karora.cooee.webcontainer.propertyrender.ExtentRender;
import org.karora.cooee.webcontainer.propertyrender.InsetsRender;
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.servermessage.DomUpdate;
import org.karora.cooee.webrender.service.JavaScriptService;
import org.w3c.dom.Element;


/**
 * Synchronization peer for <code>org.karora.cooee.app.ContentPane</code> components.
 * <p>
 * This class should not be extended or used by classes outside of the
 * Echo framework.
 */
public class DragableOverlayPeer 
implements ActionProcessor, ComponentSynchronizePeer, ImageRenderSupport, PropertyUpdateProcessor {

    private static final Extent EXTENT_0 = new Extent(0);
    
    private static final String IMAGE_ID_BACKGROUND = "background";

    public static final String PROPERTY_IE_ALPHA_RENDER_BORDER
    = "org.karora.cooee.sandbox.consultas.webcontainer.SpWindowPanePeer.ieAlphaRenderBorder";


    /**
     * Service to provide supporting JavaScript library.
     */
    private static final Service DRAGABLEOVERLAY_SERVICE = JavaScriptService.forResource("Echo.DragableOverlay",
            "/org/karora/cooee/sandbox/consultas/webcontainer/resource/js/DragableOverlay.js");

    static {
        WebRenderServlet.getServiceRegistry().add(DRAGABLEOVERLAY_SERVICE);
    }
    
    private PartialUpdateManager partialUpdateManager; 
    
    /**
     * Default constructor.
     */
    public DragableOverlayPeer() {
        super();
        partialUpdateManager = new PartialUpdateManager();
        /*partialUpdateManager.add(DragableOverlay.PROPERTY_HORIZONTAL_SCROLL, new PartialUpdateParticipant() {
        
            public void renderProperty(RenderContext rc, ServerComponentUpdate update) {
                renderScrollDirective(rc, (DragableOverlay) update.getParent(), true);
            }
        
            public boolean canRenderProperty(RenderContext rc, ServerComponentUpdate update) {
                return true;
            }
        });
        partialUpdateManager.add(DragableOverlay.PROPERTY_VERTICAL_SCROLL, new PartialUpdateParticipant() {
        
            public void renderProperty(RenderContext rc, ServerComponentUpdate update) {
                renderScrollDirective(rc, (DragableOverlay) update.getParent(), false);
            }
        
            public boolean canRenderProperty(RenderContext rc, ServerComponentUpdate update) {
                return true;
            }
        });*/
    }
    
    private static void renderPixelProperty(DragableOverlay overlay, String propertyName, Element element, String attributeName) {
        String pixelValue;
        Extent extent = (Extent) overlay.getRenderProperty(propertyName);
        if (extent != null) {
            pixelValue = ExtentRender.renderCssAttributePixelValue(extent);
            if (pixelValue != null) {
                element.setAttribute(attributeName, pixelValue);
            }
        }
    }
    
    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#getContainerId(org.karora.cooee.app.Component)
     */
    public String getContainerId(Component child) {
        return ContainerInstance.getElementId(child.getParent()) + "_content";
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
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(DRAGABLEOVERLAY_SERVICE.getId());
        DragableOverlay overlay = (DragableOverlay) component;
        renderInitDirective(rc, overlay, targetId);
        Component[] children = overlay.getVisibleComponents();
        if (children.length != 0) {
            ComponentSynchronizePeer syncPeer = SynchronizePeerFactory.getPeerForComponent(children[0].getClass());
            syncPeer.renderAdd(rc, update, getContainerId(children[0]), children[0]);
        }
        
    }

    private void renderInitDirective(RenderContext rc, DragableOverlay overlay, String targetId) {
        String elementId = ContainerInstance.getElementId(overlay);
        ServerMessage serverMessage = rc.getServerMessage();
        Element partElement = serverMessage.addPart(ServerMessage.GROUP_ID_UPDATE, "EchoDragableOverlay.MessageProcessor");
        Element initElement = serverMessage.getDocument().createElement("init");
        initElement.setAttribute("container-eid", targetId);
        initElement.setAttribute("eid", elementId);

        if (!overlay.isRenderEnabled()) {
            initElement.setAttribute("enabled", "false");
        }
        
        if (!overlay.isResizable()) {
            initElement.setAttribute("resizable", "false");
        }
        if (!overlay.isMovable()) {
            initElement.setAttribute("movable", "false");
        }
        
        Color background = (Color) overlay.getRenderProperty(DragableOverlay.PROPERTY_BACKGROUND);
        if (background != null) {
            initElement.setAttribute("background", ColorRender.renderCssAttributeValue(background));
        }
        
        // Positioning
        renderPixelProperty(overlay, DragableOverlay.PROPERTY_POSITION_X, initElement, "position-x");
        renderPixelProperty(overlay, DragableOverlay.PROPERTY_POSITION_Y, initElement, "position-y");
        renderPixelProperty(overlay, DragableOverlay.PROPERTY_WIDTH, initElement, "width");
        renderPixelProperty(overlay, DragableOverlay.PROPERTY_HEIGHT, initElement, "height");
        
        int opacity = overlay.getOpacity();
        if (opacity != 0) {
            initElement.setAttribute("opacity", String.valueOf(opacity));
        }
        // Border
        FillImageBorder border = (FillImageBorder) overlay.getRenderProperty(DragableOverlay.PROPERTY_BORDER);
        if (border != null && border.getBorderInsets() != null && border.getContentInsets() != null) {
            Element borderElement = serverMessage.getDocument().createElement("border");
            if (border.getColor() != null) {
                borderElement.setAttribute("color", ColorRender.renderCssAttributeValue(border.getColor()));
            }
            borderElement.setAttribute("border-insets", InsetsRender.renderCssAttributeValue(border.getBorderInsets()));
            borderElement.setAttribute("content-insets", InsetsRender.renderCssAttributeValue(border.getContentInsets()));
            initElement.appendChild(borderElement);
        }
        
        partElement.appendChild(initElement);
    }
    /**
     * Renders child components which were added to a 
     * <code>ContentPane</code>, as described in the provided 
     * <code>ServerComponentUpdate</code>.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param update the update
     */
    /*private void renderAddChildren(RenderContext rc, ServerComponentUpdate update) {
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
    */
    /**
     * Renders an individual child component of the <code>ContentPane</code>.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param update the <code>ServerComponentUpdate</code> being performed
     * @param parentNode the outer &lt;div&gt; element of the 
     *        <code>ContentPane</code>
     * @param child the child <code>Component</code> to be rendered
     */
    /*
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
    */
    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderDispose(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, org.karora.cooee.app.Component)
     */
    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {
        rc.getServerMessage().addLibrary(DRAGABLEOVERLAY_SERVICE.getId());
        renderDisposeDirective(rc, (DragableOverlay) component);
    }
    
    /**
     * Renders a directive to the outgoing <code>ServerMessage</code> to 
     * dispose the state of a <code>ContentPane</code>, performing tasks such as
     * unregistering event listeners on the client.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param contentPane the <code>ContentPane</code>
     */
    private void renderDisposeDirective(RenderContext rc, DragableOverlay overlay) {
        ServerMessage serverMessage = rc.getServerMessage();
        Element initElement = serverMessage.appendPartDirective(ServerMessage.GROUP_ID_PREREMOVE, 
                "EchoDragableOverlay.MessageProcessor", "dispose");
        initElement.setAttribute("eid", ContainerInstance.getElementId(overlay));
        
    }
    
    /**
     * @see org.karora.cooee.webcontainer.DomUpdateSupport#renderHtml(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, org.w3c.dom.Node, org.karora.cooee.app.Component)
     */
    /*
    public void renderHtml(RenderContext rc, ServerComponentUpdate update, Node parentNode, Component component) {
        DragableOverlay overlay = (DragableOverlay) component;
        
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(DRAGABLEOVERLAY_SERVICE.getId());

        Document document = parentNode.getOwnerDocument();
        Element divElement = document.createElement("div");
        divElement.setAttribute("id", ContainerInstance.getElementId(component));
        
        CssStyle cssStyle = new CssStyle();
        cssStyle.setAttribute("position", "absolute");
        ExtentRender.renderToStyle(cssStyle, DragableOverlay.PROPERTY_WIDTH,(Extent)overlay.getRenderProperty(DragableOverlay.PROPERTY_WIDTH));
        ExtentRender.renderToStyle(cssStyle, DragableOverlay.PROPERTY_HEIGHT,(Extent)overlay.getRenderProperty(DragableOverlay.PROPERTY_HEIGHT));
        ExtentRender.renderToStyle(cssStyle, "top",(Extent)overlay.getRenderProperty(DragableOverlay.PROPERTY_POSITION_X));
        ExtentRender.renderToStyle(cssStyle, "left",(Extent)overlay.getRenderProperty(DragableOverlay.PROPERTY_POSITION_Y));
        
        if (overlay.getRenderProperty(DragableOverlay.PROPERTY_OPACITY) != null && ((Integer)overlay.getRenderProperty(DragableOverlay.PROPERTY_OPACITY)).intValue() > 0)
        {
        	cssStyle.setAttribute("filter", "alpha(opacity="+overlay.getRenderProperty(DragableOverlay.PROPERTY_OPACITY)+")");
        	cssStyle.setAttribute("-moz-opacity", "0."+overlay.getRenderProperty(DragableOverlay.PROPERTY_OPACITY));
        }
                
        cssStyle.setAttribute("overflow", "auto");
        if (overlay.hasActionListeners()) {
        	cssStyle.setAttribute("cursor", "pointer");
        }
        ColorRender.renderToStyle(cssStyle, (Color) overlay.getRenderProperty(DragableOverlay.PROPERTY_FOREGROUND),
                (Color) overlay.getRenderProperty(DragableOverlay.PROPERTY_BACKGROUND));
        FontRender.renderToStyle(cssStyle, (Font) overlay.getRenderProperty(DragableOverlay.PROPERTY_FONT));
        FillImageRender.renderToStyle(cssStyle, rc, this, overlay, IMAGE_ID_BACKGROUND, 
                (FillImage) overlay.getRenderProperty(DragableOverlay.PROPERTY_BACKGROUND_IMAGE), 0);
        divElement.setAttribute("style", cssStyle.renderInline());
        
        parentNode.appendChild(divElement);

        // Render initialization directive.
        renderInitDirective(rc, overlay);
        
        Component[] children = overlay.getVisibleComponents();
        for (int i = 0; i < children.length; ++i) {
            renderChild(rc, update, divElement, component, children[i]);
        }
    }
*/
    /**
     * Renders a directive to the outgoing <code>ServerMessage</code> to 
     * initialize the state of a <code>ContentPane</code>, performing tasks 
     * such as registering event listeners on the client.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param contentPane the <code>ContentPane</code>
     */
    /*
    private void renderInitDirective(RenderContext rc, DragableOverlay overlay) {
        String elementId = ContainerInstance.getElementId(overlay);
        ServerMessage serverMessage = rc.getServerMessage();

        Element itemizedUpdateElement = serverMessage.getItemizedDirective(ServerMessage.GROUP_ID_POSTUPDATE,
                "EchoDragableOverlay.MessageProcessor", "init", new String[0], new String[0]);
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
    */
    
    /**
     * Renders removal operations for child components which were removed from 
     * a <code>ContentPane</code>, as described in the provided 
     * <code>ServerComponentUpdate</code>.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param update the update
     */
    /*
    private void renderRemoveChildren(RenderContext rc, ServerComponentUpdate update) {
        Component[] removedChildren = update.getRemovedChildren();
        for (int i = 0; i < removedChildren.length; ++i) {
            DomUpdate.renderElementRemove(rc.getServerMessage(), 
                    ContainerInstance.getElementId(update.getParent()) + "_container_" +
                    ContainerInstance.getElementId(removedChildren[i]));
        }
    }
    */
    private void renderSetContent(RenderContext rc, ServerComponentUpdate update) {
        //TODO. implement
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
            renderDisposeDirective(rc, (DragableOverlay) update.getParent());
            
            DomUpdate.renderElementRemove(rc.getServerMessage(), 
                    ContainerInstance.getElementId(update.getParent()));
            renderAdd(rc, update, targetId, update.getParent());
        } else {
            partialUpdateManager.process(rc, update);
            if (update.hasAddedChildren() || update.hasRemovedChildren()) {
                renderSetContent(rc, update);
            }
        }
        return fullReplace;
    }

    /**
     * @see org.karora.cooee.webcontainer.PropertyUpdateProcessor#processPropertyUpdate(
     *      org.karora.cooee.webcontainer.ContainerInstance,
     *      org.karora.cooee.app.Component, org.w3c.dom.Element)
     */
    public void processPropertyUpdate(ContainerInstance ci, Component component, Element propertyElement) {
        String propertyName = propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_NAME);
        
        if (DragableOverlay.PROPERTY_POSITION_X.equals(propertyName)) {
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, DragableOverlay.PROPERTY_POSITION_X,
                        ExtentRender.toExtent(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
        } else if (DragableOverlay.PROPERTY_POSITION_Y.equals(propertyName)) {
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, DragableOverlay.PROPERTY_POSITION_Y,
                        ExtentRender.toExtent(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
            
        } else if (DragableOverlay.PROPERTY_WIDTH.equals(propertyName)) {
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, DragableOverlay.PROPERTY_WIDTH,
                        ExtentRender.toExtent(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
            
        } else if (DragableOverlay.PROPERTY_HEIGHT.equals(propertyName)) {
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, DragableOverlay.PROPERTY_HEIGHT,
                        ExtentRender.toExtent(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
            
        } else if (DragableOverlay.Z_INDEX_CHANGED_PROPERTY.equals(propertyName)) {
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, DragableOverlay.Z_INDEX_CHANGED_PROPERTY,
                    new Integer(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
        }
    }
    
    public void processAction(ContainerInstance ci, Component component, Element actionElement) {
        ci.getUpdateManager().getClientUpdateManager().setComponentAction(component, AbstractButton.INPUT_CLICK, null);
    }
}
