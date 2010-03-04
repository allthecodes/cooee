
package org.karora.cooee.sandbox.consultas.webcontainer;

import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.FillImage;
import org.karora.cooee.app.Font;
import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.app.util.DomUtil;
import org.karora.cooee.sandbox.consultas.app.LiveTextField;
import org.karora.cooee.webcontainer.ActionProcessor;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.DomUpdateSupport;
import org.karora.cooee.webcontainer.FocusSupport;
import org.karora.cooee.webcontainer.PartialUpdateManager;
import org.karora.cooee.webcontainer.PartialUpdateParticipant;
import org.karora.cooee.webcontainer.PropertyUpdateProcessor;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webcontainer.image.ImageRenderSupport;
import org.karora.cooee.webcontainer.partialupdate.BorderUpdate;
import org.karora.cooee.webcontainer.partialupdate.ColorUpdate;
import org.karora.cooee.webcontainer.partialupdate.InsetsUpdate;
import org.karora.cooee.webcontainer.propertyrender.AlignmentRender;
import org.karora.cooee.webcontainer.propertyrender.BorderRender;
import org.karora.cooee.webcontainer.propertyrender.ColorRender;
import org.karora.cooee.webcontainer.propertyrender.ExtentRender;
import org.karora.cooee.webcontainer.propertyrender.FillImageRender;
import org.karora.cooee.webcontainer.propertyrender.FontRender;
import org.karora.cooee.webcontainer.propertyrender.InsetsRender;
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.output.CssStyle;
import org.karora.cooee.webrender.servermessage.DomUpdate;
import org.karora.cooee.webrender.servermessage.WindowUpdate;
import org.karora.cooee.webrender.service.JavaScriptService;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * Abstract base synchronization peer for the built-in
 * <code>org.karora.cooee.app.text.TextComponent</code> -derived components.
 * <p>
 * This class should not be extended or used by classes outside of the Echo
 * framework.
 */
public class LiveTextFieldPeer 
implements ActionProcessor, ComponentSynchronizePeer, DomUpdateSupport, FocusSupport, ImageRenderSupport, PropertyUpdateProcessor {

    private static final String IMAGE_ID_BACKGROUND = "background";

    /**
     * Service to provide supporting JavaScript library.
     */
    static final Service LIVE_TEXTFIELD_SERVICE = JavaScriptService.forResource("Echo.LiveTextField",
            "/org/karora/cooee/sandbox/consultas/webcontainer/resource/js/LiveTextField.js");

    static {
        WebRenderServlet.getServiceRegistry().add(LIVE_TEXTFIELD_SERVICE);
    }
    
    /**
     * A <code>PartialUpdateParticipant</code> to update the text of
     * a text component.
     */
    private class TextUpdate
    implements PartialUpdateParticipant {
    
        /**
         * @see org.karora.cooee.webcontainer.PartialUpdateParticipant#canRenderProperty(org.karora.cooee.webcontainer.RenderContext, 
         *      org.karora.cooee.app.update.ServerComponentUpdate)
         */
        public boolean canRenderProperty(RenderContext rc, ServerComponentUpdate update) {
            return true;
        }
    
        /**
         * @see org.karora.cooee.webcontainer.PartialUpdateParticipant#renderProperty(
         *      org.karora.cooee.webcontainer.RenderContext, org.karora.cooee.app.update.ServerComponentUpdate)
         */
        public void renderProperty(RenderContext rc, ServerComponentUpdate update) {
            LiveTextField liveTextField = (LiveTextField) update.getParent();
            String elementId = ContainerInstance.getElementId(liveTextField);
            ServerMessage serverMessage = rc.getServerMessage();
            Element itemizedUpdateElement = serverMessage.getItemizedDirective(ServerMessage.GROUP_ID_POSTUPDATE,
                    "EchoLiveTextField.MessageProcessor", "set-text", new String[0], new String[0]);
            Element itemElement = serverMessage.getDocument().createElement("item");
            itemElement.setAttribute("eid", elementId);
            itemElement.setAttribute("text", liveTextField.getText());
            itemizedUpdateElement.appendChild(itemElement);
            
        }
    }

    private PartialUpdateManager partialUpdateManager;

    /**
     * Default constructor.
     */
    public LiveTextFieldPeer() {
        partialUpdateManager = new PartialUpdateManager();
        partialUpdateManager.add(LiveTextField.PROPERTY_FOREGROUND,
                new ColorUpdate(LiveTextField.PROPERTY_FOREGROUND, null, ColorUpdate.CSS_COLOR));
        partialUpdateManager.add(LiveTextField.PROPERTY_BACKGROUND,
                new ColorUpdate(LiveTextField.PROPERTY_BACKGROUND, null, ColorUpdate.CSS_BACKGROUND_COLOR));
        partialUpdateManager.add(LiveTextField.PROPERTY_BORDER,
                new BorderUpdate(LiveTextField.PROPERTY_BORDER, null, BorderUpdate.CSS_BORDER));
        partialUpdateManager.add(LiveTextField.PROPERTY_INSETS,
                new InsetsUpdate(LiveTextField.PROPERTY_INSETS, null, InsetsUpdate.CSS_PADDING));
        partialUpdateManager.add(LiveTextField.TEXT_CHANGED_PROPERTY, new TextUpdate());
    }

    /**
     * Creates a base <code>CssStyle</code> for properties common to text
     * components.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param liveTextField the text component
     * @return the style
     */
    protected CssStyle createBaseCssStyle(RenderContext rc, LiveTextField liveTextField) {
        CssStyle cssStyle = new CssStyle();

        boolean renderEnabled = liveTextField.isRenderEnabled();

        Border border;
        Color foreground, background;
        Font font;
        FillImage backgroundImage;
        if (!renderEnabled) {
            // Retrieve disabled style information.
            background = (Color) liveTextField.getRenderProperty(LiveTextField.PROPERTY_DISABLED_BACKGROUND);
            backgroundImage = (FillImage) liveTextField.getRenderProperty(LiveTextField.PROPERTY_DISABLED_BACKGROUND_IMAGE);
            border = (Border) liveTextField.getRenderProperty(LiveTextField.PROPERTY_DISABLED_BORDER);
            font = (Font) liveTextField.getRenderProperty(LiveTextField.PROPERTY_DISABLED_FONT);
            foreground = (Color) liveTextField.getRenderProperty(LiveTextField.PROPERTY_DISABLED_FOREGROUND);

            // Fallback to normal styles.
            if (background == null) {
                background = (Color) liveTextField.getRenderProperty(LiveTextField.PROPERTY_BACKGROUND);
                if (backgroundImage == null) {
                    // Special case:
                    // Disabled background without disabled background image will render disabled background instead of
                    // normal background image.
                    backgroundImage = (FillImage) liveTextField.getRenderProperty(LiveTextField.PROPERTY_BACKGROUND_IMAGE);
                }
            }
            if (border == null) {
                border = (Border) liveTextField.getRenderProperty(LiveTextField.PROPERTY_BORDER);
            }
            if (font == null) {
                font = (Font) liveTextField.getRenderProperty(LiveTextField.PROPERTY_FONT);
            }
            if (foreground == null) {
                foreground = (Color) liveTextField.getRenderProperty(LiveTextField.PROPERTY_FOREGROUND);
            }
        } else {
            border = (Border) liveTextField.getRenderProperty(LiveTextField.PROPERTY_BORDER);
            foreground = (Color) liveTextField.getRenderProperty(LiveTextField.PROPERTY_FOREGROUND);
            background = (Color) liveTextField.getRenderProperty(LiveTextField.PROPERTY_BACKGROUND);
            font = (Font) liveTextField.getRenderProperty(LiveTextField.PROPERTY_FONT);
            backgroundImage = (FillImage) liveTextField.getRenderProperty(LiveTextField.PROPERTY_BACKGROUND_IMAGE);
        }
        
        Alignment alignment = (Alignment) liveTextField.getRenderProperty(LiveTextField.PROPERTY_ALIGNMENT);
        if (alignment != null) {
            int horizontalAlignment = AlignmentRender.getRenderedHorizontal(alignment, liveTextField);
            switch (horizontalAlignment) {
            case Alignment.LEFT:
                cssStyle.setAttribute("text-align", "left");
                break;
            case Alignment.CENTER:
                cssStyle.setAttribute("text-align", "center");
                break;
            case Alignment.RIGHT:
                cssStyle.setAttribute("text-align", "right");
                break;
            }
        }
        
        BorderRender.renderToStyle(cssStyle, border);
        ColorRender.renderToStyle(cssStyle, foreground, background);
        FontRender.renderToStyle(cssStyle, font);
        FillImageRender.renderToStyle(cssStyle, rc, this, liveTextField, IMAGE_ID_BACKGROUND, backgroundImage, 
                FillImageRender.FLAG_DISABLE_FIXED_MODE);
        
        InsetsRender.renderToStyle(cssStyle, "padding", (Insets) liveTextField.getRenderProperty(LiveTextField.PROPERTY_INSETS));
        
        Extent width = (Extent) liveTextField.getRenderProperty(LiveTextField.PROPERTY_WIDTH);
        Extent height = (Extent) liveTextField.getRenderProperty(LiveTextField.PROPERTY_HEIGHT);

        if (width != null) {
            cssStyle.setAttribute("width", ExtentRender.renderCssAttributeValue(width));
        }

        if (height != null) {
            cssStyle.setAttribute("height", ExtentRender.renderCssAttributeValue(height));
        }
        return cssStyle;
    }

    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#getContainerId(org.karora.cooee.app.Component)
     */
    public String getContainerId(Component child) {
        throw new UnsupportedOperationException("Component does not support children.");
    }

    /**
     * @see org.karora.cooee.webcontainer.image.ImageRenderSupport#getImage(org.karora.cooee.app.Component,
     *      java.lang.String)
     */
    public ImageReference getImage(Component component, String imageId) {
        if (IMAGE_ID_BACKGROUND.equals(imageId)) {
            FillImage backgroundImage;
            if (component.isRenderEnabled()) {
                backgroundImage = (FillImage) component.getRenderProperty(LiveTextField.PROPERTY_BACKGROUND_IMAGE);
            } else {
                backgroundImage = (FillImage) component.getRenderProperty(LiveTextField.PROPERTY_DISABLED_BACKGROUND_IMAGE);
                if (backgroundImage == null) {
                    backgroundImage = (FillImage) component.getRenderProperty(LiveTextField.PROPERTY_BACKGROUND_IMAGE);
                }
            }
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
     * @see org.karora.cooee.webcontainer.ActionProcessor#processAction(org.karora.cooee.webcontainer.ContainerInstance, 
     *      org.karora.cooee.app.Component, org.w3c.dom.Element)
     */
    public void processAction(ContainerInstance ci, Component component, Element actionElement) {
        ci.getUpdateManager().getClientUpdateManager().setComponentAction(component, LiveTextField.INPUT_ACTION, null);
    }

    /**
     * @see org.karora.cooee.webcontainer.PropertyUpdateProcessor#processPropertyUpdate(
     *      org.karora.cooee.webcontainer.ContainerInstance,
     *      org.karora.cooee.app.Component, org.w3c.dom.Element)
     */
    public void processPropertyUpdate(ContainerInstance ci, Component component, Element propertyElement) {
        String propertyName = propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_NAME);
        if (LiveTextField.TEXT_CHANGED_PROPERTY.equals(propertyName)) {
            String propertyValue = DomUtil.getElementText(propertyElement);
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, 
                    LiveTextField.TEXT_CHANGED_PROPERTY, propertyValue);
        } else if (LiveTextField.PROPERTY_HORIZONTAL_SCROLL.equals(propertyName)) {
            Extent propertyValue = new Extent(Integer.parseInt(
                    propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, 
                    LiveTextField.PROPERTY_HORIZONTAL_SCROLL, propertyValue);
        } else if (LiveTextField.PROPERTY_VERTICAL_SCROLL.equals(propertyName)) {
            Extent propertyValue = new Extent(Integer.parseInt(
                    propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, 
                    LiveTextField.PROPERTY_VERTICAL_SCROLL, propertyValue);
        }
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
        rc.getServerMessage().addLibrary(LIVE_TEXTFIELD_SERVICE.getId());
        renderDisposeDirective(rc, (LiveTextField) component);
    }

    /**
     * Renders a directive to the outgoing <code>ServerMessage</code> to
     * dispose the state of a text component, performing tasks such as
     * registering event listeners on the client.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param liveTextField the <code>LiveTextField<code>
     */
    public void renderDisposeDirective(RenderContext rc, LiveTextField liveTextField) {
        String elementId = ContainerInstance.getElementId(liveTextField);
        ServerMessage serverMessage = rc.getServerMessage();
        Element itemizedUpdateElement = serverMessage.getItemizedDirective(ServerMessage.GROUP_ID_PREREMOVE,
                "EchoLiveTextField.MessageProcessor", "dispose", new String[0], new String[0]);
        Element itemElement = serverMessage.getDocument().createElement("item");
        itemElement.setAttribute("eid", elementId);
        itemizedUpdateElement.appendChild(itemElement);
    }

    /**
     * Renders a directive to the outgoing <code>ServerMessage</code> to
     * initialize the state of a text component, performing tasks such as
     * registering event listeners on the client.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param liveTextField the <code>LiveTextField<code>
     */
    public void renderInitDirective(RenderContext rc, LiveTextField liveTextField) {
        Extent horizontalScroll = (Extent) liveTextField.getRenderProperty(LiveTextField.PROPERTY_HORIZONTAL_SCROLL);
        Extent verticalScroll = (Extent) liveTextField.getRenderProperty(LiveTextField.PROPERTY_VERTICAL_SCROLL);
        String elementId = ContainerInstance.getElementId(liveTextField);
        ServerMessage serverMessage = rc.getServerMessage();

        Element itemizedUpdateElement = serverMessage.getItemizedDirective(ServerMessage.GROUP_ID_POSTUPDATE,
                "EchoLiveTextField.MessageProcessor", "init", new String[0], new String[0]);
        Element itemElement = serverMessage.getDocument().createElement("item");
        itemElement.setAttribute("eid", elementId);
        if (horizontalScroll != null && horizontalScroll.getValue() != 0) {
            itemElement.setAttribute("horizontal-scroll", ExtentRender.renderCssAttributePixelValue(horizontalScroll, "0"));
        }
        if (verticalScroll != null && verticalScroll.getValue() != 0) {
            itemElement.setAttribute("vertical-scroll", ExtentRender.renderCssAttributePixelValue(verticalScroll, "0"));
        }
        
        if (!liveTextField.isRenderEnabled()) {
            itemElement.setAttribute("enabled", "false");
        }
        if (liveTextField.getRegexp() != null && !liveTextField.getRegexp().equals("")) {
            itemElement.setAttribute("regexp", liveTextField.getRegexp());
        }
        
        if (liveTextField.hasActionListeners()) {
            itemElement.setAttribute("server-notify", "true");
        }

        itemizedUpdateElement.appendChild(itemElement);
    }

    /**
     * @see org.karora.cooee.webcontainer.FocusSupport#renderSetFocus(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.Component)
     */
    public void renderSetFocus(RenderContext rc, Component component) {
        WindowUpdate.renderSetFocus(rc.getServerMessage(), ContainerInstance.getElementId(component));
    }

    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderUpdate(org.karora.cooee.webcontainer.RenderContext,
     *      org.karora.cooee.app.update.ServerComponentUpdate, java.lang.String)
     */
    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
        boolean fullReplace = false;
        if (update.hasUpdatedProperties()) {
            if (!partialUpdateManager.canProcess(rc, update)) {
                fullReplace = true;
            }
        }

        if (fullReplace) {
            // Perform full update.
            DomUpdate.renderElementRemove(rc.getServerMessage(), ContainerInstance.getElementId(update.getParent()));
            renderAdd(rc, update, targetId, update.getParent());
        } else {
            partialUpdateManager.process(rc, update);
        }

        return false;
    }
    
    /**
     * @see org.karora.cooee.webcontainer.DomUpdateSupport#renderHtml(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, org.w3c.dom.Node, org.karora.cooee.app.Component)
     */
    public void renderHtml(RenderContext rc, ServerComponentUpdate addUpdate, Node parentNode, Component component) {
        LiveTextField liveTextField = (LiveTextField) component;
        String elementId = ContainerInstance.getElementId(liveTextField);

        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(LIVE_TEXTFIELD_SERVICE.getId());
        
        Element inputElement = parentNode.getOwnerDocument().createElement("input");
        inputElement.setAttribute("id", elementId);
        inputElement.setAttribute("type", "text");
        
        String value = liveTextField.getText();
        if (value != null) {
            inputElement.setAttribute("value", value);
        }
        
        if (liveTextField.isFocusTraversalParticipant()) {
            inputElement.setAttribute("tabindex", Integer.toString(liveTextField.getFocusTraversalIndex()));
        } else {
            inputElement.setAttribute("tabindex", "-1");
        }
        
        String toolTipText = (String) liveTextField.getRenderProperty(LiveTextField.PROPERTY_TOOL_TIP_TEXT);
        if (toolTipText != null) {
            inputElement.setAttribute("title", toolTipText);
        }
        
        Integer maximumLength = (Integer) liveTextField.getRenderProperty(LiveTextField.PROPERTY_MAXIMUM_LENGTH);
        if (maximumLength != null) {
            inputElement.setAttribute("maxlength", maximumLength.toString());
        }

        CssStyle cssStyle = createBaseCssStyle(rc, liveTextField);
        if (cssStyle.hasAttributes()) {
            inputElement.setAttribute("style", cssStyle.renderInline());
        }
        
        parentNode.appendChild(inputElement);

        renderInitDirective(rc, liveTextField);
    }
}
