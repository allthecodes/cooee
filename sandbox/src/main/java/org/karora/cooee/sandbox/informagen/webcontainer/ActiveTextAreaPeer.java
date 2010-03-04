package org.karora.cooee.sandbox.informagen.webcontainer;


// This peer's Cooee component

import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.FillImage;
import org.karora.cooee.app.Font;
import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.TextArea;
//import org.karora.cooee.app.TextField;
import org.karora.cooee.app.text.TextComponent;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.sandbox.informagen.app.ActiveTextArea;
import org.karora.cooee.sandbox.informagen.partialupdate.BackgroundUpdate;
import org.karora.cooee.sandbox.informagen.partialupdate.EnabledUpdate;
import org.karora.cooee.sandbox.informagen.partialupdate.ForegroundUpdate;
import org.karora.cooee.sandbox.informagen.partialupdate.TextUpdate;
import org.karora.cooee.webcontainer.ActionProcessor;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.DomUpdateSupport;
import org.karora.cooee.webcontainer.FocusSupport;
import org.karora.cooee.webcontainer.PartialUpdateManager;
import org.karora.cooee.webcontainer.PropertyUpdateProcessor;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webcontainer.image.ImageRenderSupport;
import org.karora.cooee.webcontainer.partialupdate.BorderUpdate;
import org.karora.cooee.webcontainer.partialupdate.InsetsUpdate;
import org.karora.cooee.webcontainer.propertyrender.AlignmentRender;
import org.karora.cooee.webcontainer.propertyrender.BorderRender;
import org.karora.cooee.webcontainer.propertyrender.ColorRender;
import org.karora.cooee.webcontainer.propertyrender.ExtentRender;
import org.karora.cooee.webcontainer.propertyrender.FontRender;
import org.karora.cooee.webcontainer.propertyrender.InsetsRender;
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.ServiceRegistry;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.output.CssStyle;
import org.karora.cooee.webrender.servermessage.DomUpdate;
import org.karora.cooee.webrender.servermessage.WindowUpdate;
import org.karora.cooee.webrender.service.JavaScriptService;
import org.karora.cooee.webrender.util.DomUtil;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


/**
 * Abstract base synchronization peer for the built-in
 * <code>org.karora.cooee.app.text.TextComponent</code> -derived components.
 * <p>
 * This class should not be extended or used by classes outside of the Cooee
 * framework.
 */
public class ActiveTextAreaPeer implements ActionProcessor, 
                                           ComponentSynchronizePeer, 
                                           DomUpdateSupport, 
                                           FocusSupport, 
                                           ImageRenderSupport, 
                                           PropertyUpdateProcessor {


    private static final String IMAGE_ID_BACKGROUND = "background";

    /**
     * Service to provide supporting JavaScript library.
     */
    static final Service ACTIVE_TEXTAREA_SERVICE = JavaScriptService.forResource(
                    "sandbox.informagen.ActiveTextArea",
                    "/org/karora/cooee/sandbox/informagen/webcontainer/resource/js/ActiveTextArea.js");


    static {
		ServiceRegistry registery = WebRenderServlet.getServiceRegistry();
        registery.add(ACTIVE_TEXTAREA_SERVICE);
    }
 
 
    private PartialUpdateManager partialUpdateManager;
    
    private final String messageProcessor = "sandbox_informagen_ActiveTextArea.MessageProcessor";

    /**
     * Default constructor.
     */

    public ActiveTextAreaPeer() {
        
        partialUpdateManager = new PartialUpdateManager();
        
        //partialUpdateManager.add(Component.PROPERTY_FOREGROUND,
        //        new ColorUpdate(Component.PROPERTY_FOREGROUND, ".textarea", ColorUpdate.CSS_COLOR));
                
        //partialUpdateManager.add(Component.PROPERTY_BACKGROUND,
        //        new ColorUpdate(Component.PROPERTY_BACKGROUND, ".textarea", ColorUpdate.CSS_BACKGROUND_COLOR));
                
        //partialUpdateManager.add(TextComponent.PROPERTY_BORDER,
        //        new BorderUpdate(TextComponent.PROPERTY_BORDER, ".textarea", BorderUpdate.CSS_BORDER));
        
        partialUpdateManager.add(Component.PROPERTY_FOREGROUND, new ForegroundUpdate(messageProcessor));
        partialUpdateManager.add(Component.PROPERTY_BACKGROUND, new BackgroundUpdate(messageProcessor));
        partialUpdateManager.add(Component.ENABLED_CHANGED_PROPERTY, new EnabledUpdate(messageProcessor));
                
        partialUpdateManager.add(TextComponent.PROPERTY_INSETS,
                new InsetsUpdate(TextComponent.PROPERTY_INSETS, ".textarea", InsetsUpdate.CSS_PADDING));
                
        partialUpdateManager.add(TextComponent.TEXT_CHANGED_PROPERTY, new TextUpdate(messageProcessor));
        
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
                backgroundImage = (FillImage) component.getRenderProperty(TextComponent.PROPERTY_BACKGROUND_IMAGE);
            } else {
                backgroundImage = (FillImage) component.getRenderProperty(TextComponent.PROPERTY_DISABLED_BACKGROUND_IMAGE);
                if (backgroundImage == null) {
                    backgroundImage = (FillImage) component.getRenderProperty(TextComponent.PROPERTY_BACKGROUND_IMAGE);
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
    
        String name = actionElement.getAttribute("name");
        String value = actionElement.getAttribute("value");
        
        ci.getUpdateManager().getClientUpdateManager().setComponentAction(component, name, value);

        //ci.getUpdateManager().getClientUpdateManager().setComponentAction(component, TextField.INPUT_ACTION, null);
    }

    /**
     * @see org.karora.cooee.webcontainer.PropertyUpdateProcessor#processPropertyUpdate(
     *      org.karora.cooee.webcontainer.ContainerInstance,
     *      org.karora.cooee.app.Component, org.w3c.dom.Element)
     */
    public void processPropertyUpdate(ContainerInstance ci, Component component, Element propertyElement) {
    
        String propertyName = propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_NAME);
        
        if (TextComponent.TEXT_CHANGED_PROPERTY.equals(propertyName)) {
            String propertyValue = DomUtil.getElementText(propertyElement);
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, 
                    TextComponent.TEXT_CHANGED_PROPERTY, propertyValue);
        } else if (TextComponent.PROPERTY_HORIZONTAL_SCROLL.equals(propertyName)) {
            Extent propertyValue = new Extent(Integer.parseInt(
                    propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, 
                    TextComponent.PROPERTY_HORIZONTAL_SCROLL, propertyValue);
        } else if (TextComponent.PROPERTY_VERTICAL_SCROLL.equals(propertyName)) {
            Extent propertyValue = new Extent(Integer.parseInt(
                    propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, 
                    TextComponent.PROPERTY_VERTICAL_SCROLL, propertyValue);
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
        rc.getServerMessage().addLibrary(ACTIVE_TEXTAREA_SERVICE.getId());
        renderDisposeDirective(rc, (ActiveTextArea) component);
    }

    /**
     * Renders a directive to the outgoing <code>ServerMessage</code> to
     * dispose the state of a text component, performing tasks such as
     * registering event listeners on the client.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param integerTextField the <code>LiveTextField<code>
     */
    public void renderDisposeDirective(RenderContext rc, ActiveTextArea integerTextField) {
        String elementId = ContainerInstance.getElementId(integerTextField);
        ServerMessage serverMessage = rc.getServerMessage();
        Element itemizedUpdateElement = serverMessage.getItemizedDirective(ServerMessage.GROUP_ID_PREREMOVE,
                messageProcessor, "dispose", new String[0], new String[0]);
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
     * @param integerTextField the <code>LiveTextField<code>
     */
    public void renderInitDirective(RenderContext rc, ActiveTextArea activeTextArea) {
    
        Extent horizontalScroll = (Extent) activeTextArea.getRenderProperty(ActiveTextArea.PROPERTY_HORIZONTAL_SCROLL);
        Extent verticalScroll = (Extent) activeTextArea.getRenderProperty(ActiveTextArea.PROPERTY_VERTICAL_SCROLL);
        
        String elementId = ContainerInstance.getElementId(activeTextArea);
        ServerMessage serverMessage = rc.getServerMessage();

        Element itemizedUpdateElement = serverMessage.getItemizedDirective(ServerMessage.GROUP_ID_POSTUPDATE,
                messageProcessor, "init", new String[0], new String[0]);
                
        Element itemElement = serverMessage.getDocument().createElement("item");
        
        itemElement.setAttribute("eid", elementId);
        if (horizontalScroll != null && horizontalScroll.getValue() != 0) {
            itemElement.setAttribute("horizontal-scroll", ExtentRender.renderCssAttributePixelValue(horizontalScroll, "0"));
        }
        if (verticalScroll != null && verticalScroll.getValue() != 0) {
            itemElement.setAttribute("vertical-scroll", ExtentRender.renderCssAttributePixelValue(verticalScroll, "0"));
        }
        
        if (!activeTextArea.isRenderEnabled()) {
            itemElement.setAttribute("enabled", "false");
        }

        String text = activeTextArea.getText();
        if (text != null) 
            itemElement.setAttribute("text", text);

        itemElement.setAttribute("foreground", ColorRender.renderCssAttributeValue((Color)activeTextArea.getRenderProperty(Component.PROPERTY_FOREGROUND)));
        itemElement.setAttribute("background", ColorRender.renderCssAttributeValue((Color)activeTextArea.getRenderProperty(Component.PROPERTY_BACKGROUND)));


        itemElement.setAttribute("maximum-length", Integer.toString(activeTextArea.getMaxLength()));
        itemElement.setAttribute("msg-visible", activeTextArea.isMessageVisible() ? "true" : "false");

        // TextArea Border
        
        Border border = (Border) activeTextArea.getRenderProperty(TextComponent.PROPERTY_BORDER);
        itemElement.setAttribute("textarea-border", (border != null) ? BorderRender.renderCssAttributeValue(border) : "");
        
        // Focus borders (top, right, bottom, left) and focus background color

        // Single border property sets other
        border = (Border) activeTextArea.getRenderProperty(ActiveTextArea.PROPERTY_FOCUS_BORDER);
        if (border != null) {
            itemElement.setAttribute("top-focus-border", BorderRender.renderCssAttributeValue(border));
            itemElement.setAttribute("right-focus-border", BorderRender.renderCssAttributeValue(border));
            itemElement.setAttribute("bottom-focus-border", BorderRender.renderCssAttributeValue(border));
            itemElement.setAttribute("left-focus-border", BorderRender.renderCssAttributeValue(border));
        }


        // Individual borders can override single border        
        border = (Border) activeTextArea.getRenderProperty(ActiveTextArea.PROPERTY_TOP_FOCUS_BORDER);
        if (border != null)
            itemElement.setAttribute("top-focus-border", BorderRender.renderCssAttributeValue(border));
            
        border = (Border) activeTextArea.getRenderProperty(ActiveTextArea.PROPERTY_RIGHT_FOCUS_BORDER);
        if (border != null)
            itemElement.setAttribute("right-focus-border", BorderRender.renderCssAttributeValue(border));

        border = (Border) activeTextArea.getRenderProperty(ActiveTextArea.PROPERTY_BOTTOM_FOCUS_BORDER);
        if (border != null)
            itemElement.setAttribute("bottom-focus-border", BorderRender.renderCssAttributeValue(border));

        border = (Border) activeTextArea.getRenderProperty(ActiveTextArea.PROPERTY_LEFT_FOCUS_BORDER);
        if (border != null)
            itemElement.setAttribute("left-focus-border", BorderRender.renderCssAttributeValue(border));
            
        Color focusForeground = (Color)activeTextArea.getRenderProperty(ActiveTextArea.PROPERTY_FOCUS_FOREGROUND);
        if (focusForeground != null)
            itemElement.setAttribute("focus-foreground", ColorRender.renderCssAttributeValue(focusForeground));
            
        Color focusBackground = (Color)activeTextArea.getRenderProperty(ActiveTextArea.PROPERTY_FOCUS_BACKGROUND);
        if (focusBackground != null)
            itemElement.setAttribute("focus-background", ColorRender.renderCssAttributeValue(focusBackground));

        
        if (activeTextArea.hasActionListeners())
            itemElement.setAttribute("server-notify", "true");

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
    
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(ACTIVE_TEXTAREA_SERVICE.getId());
        
        ActiveTextArea activeTextArea = (ActiveTextArea) component;
        String elementId = ContainerInstance.getElementId(activeTextArea);


        // Create the outer <div> with element id
        
 		Element divOuter = parentNode.getOwnerDocument().createElement("div");
		divOuter.setAttribute("id", elementId);

        // Create the component parts
        Element textAreaElement = parentNode.getOwnerDocument().createElement("textarea");
		textAreaElement.setAttribute("id", elementId + ".textarea");

        Extent width = (Extent) activeTextArea.getRenderProperty(TextComponent.PROPERTY_WIDTH);
        Extent height = (Extent) activeTextArea.getRenderProperty(TextComponent.PROPERTY_HEIGHT);

        if (width != null) 
            textAreaElement.setAttribute("cols", ExtentRender.renderCssAttributeValue(width));

        if (height != null) 
            textAreaElement.setAttribute("rows", ExtentRender.renderCssAttributeValue(height));
  
  
        String text = activeTextArea.getText();
        if (text != null) {
            Text textNode = parentNode.getOwnerDocument().createTextNode(text);
            textAreaElement.appendChild(textNode);
        }
        
        if (activeTextArea.isFocusTraversalParticipant()) {
            textAreaElement.setAttribute("tabindex", Integer.toString(activeTextArea.getFocusTraversalIndex()));
        } else {
            textAreaElement.setAttribute("tabindex", "-1");
        }
        
        String toolTipText = (String) activeTextArea.getRenderProperty(TextArea.PROPERTY_TOOL_TIP_TEXT);
        if (toolTipText != null) {
            textAreaElement.setAttribute("title", toolTipText);
        }

        CssStyle cssStyle = createBaseCssStyle(rc, activeTextArea);
        
        if (cssStyle.hasAttributes())
            textAreaElement.setAttribute("style", cssStyle.renderInline());

        // 'length' span      
        Element lengthSpan = parentNode.getOwnerDocument().createElement("span");
		lengthSpan.setAttribute("id", elementId + ".length");
		cssStyle = new CssStyle();
		cssStyle.setAttribute("font-family", "sans-serif");
		cssStyle.setAttribute("font-size", "x-small");
		cssStyle.setAttribute("display", (activeTextArea.isMessageVisible() ? "" : "none"));
		lengthSpan.setAttribute("style", cssStyle.renderInline());

        // 'remaining' span      
        Element remainingSpan = parentNode.getOwnerDocument().createElement("span");
		remainingSpan.setAttribute("id", elementId + ".remaining");
		cssStyle = new CssStyle();
		cssStyle.setAttribute("padding-left", "1em");
		cssStyle.setAttribute("font-family", "sans-serif");
		cssStyle.setAttribute("font-size", "x-small");
		cssStyle.setAttribute("display", (activeTextArea.isMessageVisible() ? "" : "none"));
		remainingSpan.setAttribute("style", cssStyle.renderInline());

        // 'overdrawn' span      
        Element overdrawnSpan = parentNode.getOwnerDocument().createElement("span");
		overdrawnSpan.setAttribute("id", elementId + ".overdrawn");
		cssStyle = new CssStyle();
		cssStyle.setAttribute("padding-left", "1em");
		cssStyle.setAttribute("font-family", "sans-serif");
		cssStyle.setAttribute("font-size", "x-small");
		cssStyle.setAttribute("color", "#ff0000");
		cssStyle.setAttribute("font-weight", "bold");
		cssStyle.setAttribute("display", (activeTextArea.isMessageVisible() ? "" : "none"));
		overdrawnSpan.setAttribute("style", cssStyle.renderInline());


        // Assemble the component parts starting with the <input>

 		Element divTextArea = parentNode.getOwnerDocument().createElement("div");

		parentNode.appendChild(divOuter);
		divOuter.appendChild(divTextArea);
		divTextArea.appendChild(textAreaElement);

 		Element divMessage = parentNode.getOwnerDocument().createElement("div");
		cssStyle = new CssStyle();
		cssStyle.setAttribute("margin-top", "3px");
		divMessage.setAttribute("style", cssStyle.renderInline());
 		
		divMessage.appendChild(lengthSpan);
		divMessage.appendChild(remainingSpan);
		divMessage.appendChild(overdrawnSpan);
		divOuter.appendChild(divMessage);

        renderInitDirective(rc, (ActiveTextArea)activeTextArea);
    }


    /**
     * Creates a base <code>CssStyle</code> for properties common to text
     * components.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param integerTextField the text component
     * @return the style
     */

    protected CssStyle createBaseCssStyle(RenderContext rc, ActiveTextArea activeTextArea) {
        CssStyle cssStyle = new CssStyle();

        boolean renderEnabled = activeTextArea.isRenderEnabled();

        //Border border;
        Color foreground, background;
        Font font;
        FillImage backgroundImage;

        if (!renderEnabled) {
            // Retrieve disabled style information.
            background = (Color) activeTextArea.getRenderProperty(TextComponent.PROPERTY_DISABLED_BACKGROUND);
            backgroundImage = (FillImage) activeTextArea.getRenderProperty(TextComponent.PROPERTY_DISABLED_BACKGROUND_IMAGE);
            //border = (Border) activeTextArea.getRenderProperty(TextComponent.PROPERTY_DISABLED_BORDER);
            font = (Font) activeTextArea.getRenderProperty(TextComponent.PROPERTY_DISABLED_FONT);
            foreground = (Color) activeTextArea.getRenderProperty(TextComponent.PROPERTY_DISABLED_FOREGROUND);

            // Fallback to normal styles.
            if (background == null) {
                background = (Color) activeTextArea.getRenderProperty(Component.PROPERTY_BACKGROUND);
                if (backgroundImage == null) {
                    // Special case:
                    // Disabled background without disabled background image will render disabled background instead of
                    // normal background image.
                    backgroundImage = (FillImage) activeTextArea.getRenderProperty(TextComponent.PROPERTY_BACKGROUND_IMAGE);
                }
            }
            //if (border == null) {
            //    border = (Border) activeTextArea.getRenderProperty(TextComponent.PROPERTY_BORDER);
            //}
            if (font == null) {
                font = (Font) activeTextArea.getRenderProperty(TextComponent.PROPERTY_FONT);
            }
            if (foreground == null) {
                foreground = (Color) activeTextArea.getRenderProperty(Component.PROPERTY_FOREGROUND);
            }
        } else {
            //border = (Border) activeTextArea.getRenderProperty(TextComponent.PROPERTY_BORDER);
            foreground = (Color) activeTextArea.getRenderProperty(Component.PROPERTY_FOREGROUND);
            background = (Color) activeTextArea.getRenderProperty(Component.PROPERTY_BACKGROUND);
            font = (Font) activeTextArea.getRenderProperty(TextComponent.PROPERTY_FONT);
        }
 
 
        Alignment alignment = (Alignment) activeTextArea.getRenderProperty(TextComponent.PROPERTY_ALIGNMENT);
        
        if (alignment != null) {
            int horizontalAlignment = AlignmentRender.getRenderedHorizontal(alignment, activeTextArea);
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
        
        //BorderRender.renderToStyle(cssStyle, border);
        ColorRender.renderToStyle(cssStyle, foreground, background);
        FontRender.renderToStyle(cssStyle, font);
        
        
        InsetsRender.renderToStyle(cssStyle, "padding", (Insets) activeTextArea.getRenderProperty(TextComponent.PROPERTY_INSETS));
        
        Extent width = (Extent) activeTextArea.getRenderProperty(TextArea.PROPERTY_WIDTH);
        Extent height = (Extent) activeTextArea.getRenderProperty(TextArea.PROPERTY_HEIGHT);

        if (width != null) {
            cssStyle.setAttribute("width", ExtentRender.renderCssAttributeValue(width));
        }

        if (height != null) {
            cssStyle.setAttribute("height", ExtentRender.renderCssAttributeValue(height));
        }
        
        return cssStyle;
    }


    static int convertAlignmentToPosition(Alignment position, Component component) {

        if (position.getVertical() == Alignment.DEFAULT) {
        
            switch (position.getHorizontal()) {
                case Alignment.LEFT:
                    return Alignment.LEFT;
                case Alignment.RIGHT:
                    return Alignment.RIGHT;
                case Alignment.LEADING:
                    return component.getRenderLayoutDirection().isLeftToRight() 
                        ? Alignment.LEFT : Alignment.RIGHT;
                case Alignment.TRAILING:
                    return component.getRenderLayoutDirection().isLeftToRight() 
                        ? Alignment.RIGHT : Alignment.LEFT;
                default:
                    // Invalid, return value for TRAILING (default).
                    return Alignment.RIGHT;
            }
        } else {
            if (position.getVertical() == Alignment.TOP) {
                return Alignment.TOP;
            } else {
                return Alignment.BOTTOM;
            }
        }
    }


}
