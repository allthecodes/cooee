package org.karora.cooee.webcontainer.syncpeer;

import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.FillImage;
import org.karora.cooee.app.Font;
import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.TextField;
import org.karora.cooee.app.text.TextComponent;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.app.ActiveTextField;
import org.karora.cooee.webcontainer.partialupdate.BackgroundUpdate;
import org.karora.cooee.webcontainer.partialupdate.EnabledUpdate;
import org.karora.cooee.webcontainer.partialupdate.ForegroundUpdate;
import org.karora.cooee.webcontainer.partialupdate.TextUpdate;
import org.karora.cooee.webcontainer.ActionProcessor;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.FocusSupport;
import org.karora.cooee.webcontainer.PartialUpdateManager;
import org.karora.cooee.webcontainer.PropertyUpdateProcessor;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webcontainer.image.ImageRenderSupport;
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
import org.karora.cooee.webrender.service.StaticBinaryService;
import org.karora.cooee.webrender.util.DomUtil;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * Abstract base synchronization peer for the built-in
 * <code>org.karora.cooee.informagen.app.ActiveTextField</code>
 */

public abstract class ActiveTextFieldPeer implements ActionProcessor, 
                                                     ComponentSynchronizePeer, 
                                                     FocusSupport, 
                                                     ImageRenderSupport, 
                                                     PropertyUpdateProcessor {


    protected static final Alignment DEFAULT_MSG_POSITION = new Alignment(Alignment.TRAILING, Alignment.DEFAULT);
    protected static final Alignment DEFAULT_ICON_POSITION = new Alignment(Alignment.TRAILING, Alignment.DEFAULT);

    private static final String IMAGE_ID_BACKGROUND = "background";

    /**
     * Service to provide supporting images
     */

    /**
     * Service to provide supporting JavaScript library; icons are loaded
     *  and registered in the superclass "ActiveTextFieldPeer"
     */

    static final Service ACTIVE_TEXTFIELD_SERVICE = JavaScriptService.forResource(
                    "Echo.ActiveTextField",
                    "/org/karora/cooee/webcontainer/resource/js/ActiveTextField.js");


	private static final Service GOOD_ICON_IMAGE = StaticBinaryService.forResource(
	                "Echo.images.goodIcon", 
	                "image/png",
			        "/org/karora/cooee/webcontainer/resource/image/good.icon.png");

	private static final Service WARNING_ICON_IMAGE = StaticBinaryService.forResource(
	                "Echo.images.warningIcon", 
	                "image/png",
			        "/org/karora/cooee/webcontainer/resource/image/warning.icon.png");

	private static final Service ERROR_ICON_IMAGE = StaticBinaryService.forResource(
	                "Echo.images.errorIcon", 
	                "image/png",
			        "/org/karora/cooee/webcontainer/resource/image/error.icon.png");



    static {
		ServiceRegistry registery = WebRenderServlet.getServiceRegistry();
		registery.add(ACTIVE_TEXTFIELD_SERVICE);
		registery.add(GOOD_ICON_IMAGE);
		registery.add(WARNING_ICON_IMAGE);
		registery.add(ERROR_ICON_IMAGE);
    }
 

    ///////////////////////////////////////////////////////////////////////////////////////////

    private PartialUpdateManager partialUpdateManager;

    private String messageProcessor;
    /**
     * Default constructor.
     */

    public ActiveTextFieldPeer(String messageProcessor) {
    
        this.messageProcessor = messageProcessor;
    
        partialUpdateManager = new PartialUpdateManager();

        partialUpdateManager.add(Component.PROPERTY_FOREGROUND, new ForegroundUpdate(messageProcessor));
        partialUpdateManager.add(Component.PROPERTY_BACKGROUND, new BackgroundUpdate(messageProcessor));
        partialUpdateManager.add(Component.ENABLED_CHANGED_PROPERTY, new EnabledUpdate(messageProcessor));
        partialUpdateManager.add(TextField.TEXT_CHANGED_PROPERTY, new TextUpdate(messageProcessor));
        
    }


    //====  ImageRenderSupport interface =========================================================

    /**
     * @see org.karora.cooee.webcontainer.image.ImageRenderSupport#getImage(org.karora.cooee.app.Component,
     *      java.lang.String)
     */

    public ImageReference getImage(Component component, String imageId) {
        if (IMAGE_ID_BACKGROUND.equals(imageId)) {
            FillImage backgroundImage;
            if (component.isRenderEnabled()) {
                backgroundImage = (FillImage) component.getRenderProperty(TextField.PROPERTY_BACKGROUND_IMAGE);
            } else {
                backgroundImage = (FillImage) component.getRenderProperty(TextField.PROPERTY_DISABLED_BACKGROUND_IMAGE);
                if (backgroundImage == null) {
                    backgroundImage = (FillImage) component.getRenderProperty(TextField.PROPERTY_BACKGROUND_IMAGE);
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


    //====  ActionProcessor interface =========================================================

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



    //====  PropertyUpdateProcessor interface =================================================

    /**
     * @see org.karora.cooee.webcontainer.PropertyUpdateProcessor#processPropertyUpdate(
     *      org.karora.cooee.webcontainer.ContainerInstance,
     *      org.karora.cooee.app.Component, org.w3c.dom.Element)
     */
    public void processPropertyUpdate(ContainerInstance ci, Component component, Element propertyElement) {
    
        String propertyName = propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_NAME);

        if (TextField.TEXT_CHANGED_PROPERTY.equals(propertyName)) {

            String propertyValue = DomUtil.getElementText(propertyElement);
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, 
                    TextField.TEXT_CHANGED_PROPERTY, propertyValue);

        } else if (TextField.PROPERTY_HORIZONTAL_SCROLL.equals(propertyName)) {

            Extent propertyValue = new Extent(Integer.parseInt(
                    propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, 
                    TextField.PROPERTY_HORIZONTAL_SCROLL, propertyValue);

        } else if (TextField.PROPERTY_VERTICAL_SCROLL.equals(propertyName)) {

            Extent propertyValue = new Extent(Integer.parseInt(
                    propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, 
                    TextField.PROPERTY_VERTICAL_SCROLL, propertyValue);
        }
    }

    //====  FocusSupport interface ============================================================

    /**
     * @see org.karora.cooee.webcontainer.FocusSupport#renderSetFocus(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.Component)
     */
    public void renderSetFocus(RenderContext rc, Component component) {
        WindowUpdate.renderSetFocus(rc.getServerMessage(), ContainerInstance.getElementId(component) + ".input");
    }



    //==== ComponentSychronizePeer interface ==================================================
    //  - getContainerId
    //  - renderAdd
    //  - renderUpdate
    //  - renderDispose
    
    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#getContainerId(org.karora.cooee.app.Component)
     */
    public String getContainerId(Component child) {
        throw new UnsupportedOperationException("Component does not support children.");
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



    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {

        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(ACTIVE_TEXTFIELD_SERVICE.getId());

        renderDisposeDirective(rc, component);
    }

    protected void renderDisposeDirective(RenderContext rc, Component component) {
    
        String elementId = ContainerInstance.getElementId(component);
        
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

    protected Element renderInitDirective(RenderContext rc, ActiveTextField activeTextField) {
    
        String elementId = ContainerInstance.getElementId(activeTextField);
        ServerMessage serverMessage = rc.getServerMessage();

        Element itemizedUpdateElement = serverMessage.getItemizedDirective(ServerMessage.GROUP_ID_POSTUPDATE,
                messageProcessor, "init", new String[0], new String[0]);
 
  
        Element itemElement = serverMessage.getDocument().createElement("item");
        itemElement.setAttribute("eid", elementId);

        Extent horizontalScroll = (Extent) activeTextField.getRenderProperty(ActiveTextField.PROPERTY_HORIZONTAL_SCROLL);
        Extent verticalScroll = (Extent) activeTextField.getRenderProperty(ActiveTextField.PROPERTY_VERTICAL_SCROLL);
        
        if (horizontalScroll != null && horizontalScroll.getValue() != 0) {
            itemElement.setAttribute("horizontal-scroll", ExtentRender.renderCssAttributePixelValue(horizontalScroll, "0"));
        }
        if (verticalScroll != null && verticalScroll.getValue() != 0) {
            itemElement.setAttribute("vertical-scroll", ExtentRender.renderCssAttributePixelValue(verticalScroll, "0"));
        }
        
        if (!activeTextField.isRenderEnabled()) {
            itemElement.setAttribute("enabled", "false");
        }

        String message = activeTextField.getMessage();
        if (message != null) 
            itemElement.setAttribute("status-msg", message);

        String text = activeTextField.getText();
        if (text != null) 
            itemElement.setAttribute("text", text);

		itemElement.setAttribute("actionCausedOnChange",  activeTextField.getActionCausedOnChange() ? "true" : "false");

        itemElement.setAttribute("msg-visible", activeTextField.isMessageVisible() ? "true" : "false");
        itemElement.setAttribute("icon-visible", activeTextField.isIconVisible() ? "true" : "false");
        
        itemElement.setAttribute("msg-always-visible", activeTextField.isMessageAlwaysVisible() ? "true" : "false");
        itemElement.setAttribute("icon-always-visible", activeTextField.isIconAlwaysVisible() ? "true" : "false");


        itemElement.setAttribute("foreground", ColorRender.renderCssAttributeValue((Color)activeTextField.getRenderProperty(Component.PROPERTY_FOREGROUND)));
        itemElement.setAttribute("background", ColorRender.renderCssAttributeValue((Color)activeTextField.getRenderProperty(Component.PROPERTY_BACKGROUND)));

        itemElement.setAttribute("disabled-foreground", ColorRender.renderCssAttributeValue((Color)activeTextField.getRenderProperty(TextComponent.PROPERTY_DISABLED_FOREGROUND)));
        itemElement.setAttribute("disabled-background", ColorRender.renderCssAttributeValue((Color)activeTextField.getRenderProperty(TextComponent.PROPERTY_DISABLED_BACKGROUND)));


        if (activeTextField.getErrorForeground() != null && !activeTextField.getErrorForeground().equals("")) 
            itemElement.setAttribute("error-foreground", ColorRender.renderCssAttributeValue(activeTextField.getErrorForeground()));

        if (activeTextField.getErrorBackground() != null && !activeTextField.getErrorBackground().equals("")) 
            itemElement.setAttribute("error-background", ColorRender.renderCssAttributeValue(activeTextField.getErrorBackground()));


        // Input field border
        
        Border border = (Border) activeTextField.getRenderProperty(TextComponent.PROPERTY_BORDER);
        itemElement.setAttribute("input-border", (border != null) ? BorderRender.renderCssAttributeValue(border) : "");
        
        // Focus borders and focus background color
        
        // Focus borders (top, right, bottom, left) and focus background color
        border = (Border) activeTextField.getRenderProperty(ActiveTextField.PROPERTY_FOCUS_BORDER);
        if (border != null) {
            itemElement.setAttribute("top-focus-border", BorderRender.renderCssAttributeValue(border));
            itemElement.setAttribute("right-focus-border", BorderRender.renderCssAttributeValue(border));
            itemElement.setAttribute("bottom-focus-border", BorderRender.renderCssAttributeValue(border));
            itemElement.setAttribute("left-focus-border", BorderRender.renderCssAttributeValue(border));
        }
        
        border = (Border) activeTextField.getRenderProperty(ActiveTextField.PROPERTY_TOP_FOCUS_BORDER);
        if (border != null)
            itemElement.setAttribute("top-focus-border", BorderRender.renderCssAttributeValue(border));
            
        border = (Border) activeTextField.getRenderProperty(ActiveTextField.PROPERTY_RIGHT_FOCUS_BORDER);
        if (border != null)
            itemElement.setAttribute("right-focus-border", BorderRender.renderCssAttributeValue(border));

        border = (Border) activeTextField.getRenderProperty(ActiveTextField.PROPERTY_BOTTOM_FOCUS_BORDER);
        if (border != null)
            itemElement.setAttribute("bottom-focus-border", BorderRender.renderCssAttributeValue(border));

        border = (Border) activeTextField.getRenderProperty(ActiveTextField.PROPERTY_LEFT_FOCUS_BORDER);
        if (border != null)
            itemElement.setAttribute("left-focus-border", BorderRender.renderCssAttributeValue(border));
            
        Color focusForeground = (Color)activeTextField.getRenderProperty(ActiveTextField.PROPERTY_FOCUS_FOREGROUND);
        if (focusForeground != null)
            itemElement.setAttribute("focus-foreground", ColorRender.renderCssAttributeValue(focusForeground));
            
        Color focusBackground = (Color)activeTextField.getRenderProperty(ActiveTextField.PROPERTY_FOCUS_BACKGROUND);
        if (focusBackground != null)
            itemElement.setAttribute("focus-background", ColorRender.renderCssAttributeValue(focusBackground));


        if (activeTextField.hasActionListeners())
            itemElement.setAttribute("server-notify", "true");

        if (activeTextField.isRequired())
            itemElement.setAttribute("required", "true");

        String requiredMessage = activeTextField.getRequiredMessage();
        if (requiredMessage != null) 
            itemElement.setAttribute("required-msg", requiredMessage);

        itemizedUpdateElement.appendChild(itemElement);
        
        return itemElement;
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



    /*====  DomUpdateSupport interface ========================================================

    
    /**
     * @see org.karora.cooee.webcontainer.DomUpdateSupport#renderHtml(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, org.w3c.dom.Node, org.karora.cooee.app.Component)
     */
    public void renderHtml(RenderContext rc, ServerComponentUpdate addUpdate, Node parentNode, Component component) {
        
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(ACTIVE_TEXTFIELD_SERVICE.getId());

    
        ActiveTextField textField = (ActiveTextField) component;
        String elementId = ContainerInstance.getElementId(textField);

        // Create the outer <div> with element id
        
 		Element divOuter = parentNode.getOwnerDocument().createElement("div");
		divOuter.setAttribute("id", elementId);

        // Create the component parts
        Element inputElement = parentNode.getOwnerDocument().createElement("input");
		inputElement.setAttribute("id", elementId + ".input");
        inputElement.setAttribute("type", "text");


        String text = textField.getText();
        if (text != null) 
            inputElement.setAttribute("value", text);

        
        if (textField.isFocusTraversalParticipant()) {
            inputElement.setAttribute("tabindex", Integer.toString(textField.getFocusTraversalIndex()));
        } else {
            inputElement.setAttribute("tabindex", "-1");
        }
        
        String toolTipText = (String) textField.getRenderProperty(TextField.PROPERTY_TOOL_TIP_TEXT);
        if (toolTipText != null) {
            inputElement.setAttribute("title", toolTipText);
        }
        
        Integer maximumLength = (Integer) textField.getRenderProperty(TextField.PROPERTY_MAXIMUM_LENGTH);
        if (maximumLength != null) {
            inputElement.setAttribute("maxlength", maximumLength.toString());
        }

        CssStyle cssStyle = createBaseCssStyle(rc, textField);
        
        if (cssStyle.hasAttributes())
            inputElement.setAttribute("style", cssStyle.renderInline());
        

        // The 'OK' icon
        String srcURI = rc.getContainerInstance().getServiceUri(GOOD_ICON_IMAGE);
		Element goodIconElement = parentNode.getOwnerDocument().createElement("img");
		goodIconElement.setAttribute("id", elementId + ".good");
		goodIconElement.setAttribute("src", srcURI);
		goodIconElement.setAttribute("width", "13");
		goodIconElement.setAttribute("height", "13");
		goodIconElement.setAttribute("alt", "OK");

		cssStyle = new CssStyle();
		cssStyle.setAttribute("vertical-align", "middle");
		cssStyle.setAttribute("padding-left", "3px");
		cssStyle.setAttribute("padding-right", "3px");
		cssStyle.setAttribute("display", (textField.isIconAlwaysVisible() ? "" : "none"));
		goodIconElement.setAttribute("style", cssStyle.renderInline());


        // The 'error' icon
        srcURI = rc.getContainerInstance().getServiceUri(ERROR_ICON_IMAGE);
		Element errorIconElement = parentNode.getOwnerDocument().createElement("img");
		errorIconElement.setAttribute("id", elementId + ".error");
		errorIconElement.setAttribute("src", srcURI);
		errorIconElement.setAttribute("width", "13");
		errorIconElement.setAttribute("height", "13");
		errorIconElement.setAttribute("alt", "Error");
		
		cssStyle = new CssStyle();
		cssStyle.setAttribute("vertical-align", "middle");
		cssStyle.setAttribute("padding-left", "3px");
		cssStyle.setAttribute("padding-right", "3px");
		cssStyle.setAttribute("display", "none");
		errorIconElement.setAttribute("style", cssStyle.renderInline());

        // The 'warning' icon
        srcURI = rc.getContainerInstance().getServiceUri(WARNING_ICON_IMAGE);
		Element warningIconElement = parentNode.getOwnerDocument().createElement("img");
		warningIconElement.setAttribute("id", elementId + ".warning");
		warningIconElement.setAttribute("src", srcURI);
		warningIconElement.setAttribute("width", "13");
		warningIconElement.setAttribute("height", "13");
		warningIconElement.setAttribute("alt", "Warning");
		
		cssStyle = new CssStyle();
		cssStyle.setAttribute("vertical-align", "middle");
		cssStyle.setAttribute("padding-left", "3px");
		cssStyle.setAttribute("padding-right", "3px");
		cssStyle.setAttribute("display", "none");
		warningIconElement.setAttribute("style", cssStyle.renderInline());

        
        Element messageSpan = parentNode.getOwnerDocument().createElement("span");
		messageSpan.setAttribute("id", elementId + ".msg");
		
		cssStyle = new CssStyle();
		cssStyle.setAttribute("vertical-align", "middle");
		cssStyle.setAttribute("padding-left", "3px");
		cssStyle.setAttribute("padding-right", "3px");
		cssStyle.setAttribute("font-family", "sans-serif");
		cssStyle.setAttribute("font-size", "x-small");
		cssStyle.setAttribute("display", (textField.isMessageAlwaysVisible() ? "" : "none"));
		messageSpan.setAttribute("style", cssStyle.renderInline());


        // Assemble the component parts starting with the <input>

		parentNode.appendChild(divOuter);

 		Element divInner = parentNode.getOwnerDocument().createElement("div");
        divOuter.appendChild(divInner);
        divInner.appendChild(inputElement);


        Alignment iconPosition = (Alignment) textField.getRenderProperty(ActiveTextField.PROPERTY_ICON_POSITION, 
                                                                         DEFAULT_ICON_POSITION);
        int iconOrientation = convertAlignmentToPosition(iconPosition, textField);

        // Add the icons
        switch(iconOrientation) {
            
            case Alignment.TOP:
                divOuter.insertBefore(warningIconElement, divInner);
                divOuter.insertBefore(goodIconElement, divInner);
                divOuter.insertBefore(errorIconElement, divInner);
                break;
                
            case Alignment.RIGHT:
                divInner.appendChild(warningIconElement);
                divInner.appendChild(goodIconElement);
                divInner.appendChild(errorIconElement);
                break;
                
            case Alignment.BOTTOM:
                divOuter.appendChild(warningIconElement);
                divOuter.appendChild(goodIconElement);
                divOuter.appendChild(errorIconElement);
                break;
                
            case Alignment.LEFT:
                divInner.insertBefore(warningIconElement, inputElement);
                divInner.insertBefore(goodIconElement, inputElement);
                divInner.insertBefore(errorIconElement, inputElement);
                break;
                
        }

        // Add the status message
        Alignment textPosition = (Alignment) textField.getRenderProperty(ActiveTextField.PROPERTY_MSG_POSITION, 
                                                                             DEFAULT_MSG_POSITION);
        int textOrientation = convertAlignmentToPosition(textPosition, textField);

        switch(textOrientation) {
            
            case Alignment.TOP:
                divOuter.insertBefore(messageSpan, divInner);
                break;

            case Alignment.RIGHT:
                divInner.appendChild(messageSpan);
                break;

            case Alignment.BOTTOM:
                divOuter.appendChild(messageSpan);
                break;
                
            case Alignment.LEFT:
                divInner.insertBefore(messageSpan, inputElement);
                break;
        }
         
    }


    /**
     * Creates a base <code>CssStyle</code> for properties common to text
     * components.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param textComponent the text component
     * @return the style
     */

    protected CssStyle createBaseCssStyle(RenderContext rc, TextComponent textComponent) {
    
        CssStyle cssStyle = new CssStyle();

        boolean renderEnabled = textComponent.isRenderEnabled();

        //Border border;
        Color foreground, background;
        Font font;
        FillImage backgroundImage;
        if (!renderEnabled) {
            // Retrieve disabled style information.
            background = (Color) textComponent.getRenderProperty(TextComponent.PROPERTY_DISABLED_BACKGROUND);
            backgroundImage = (FillImage) textComponent.getRenderProperty(TextComponent.PROPERTY_DISABLED_BACKGROUND_IMAGE);
            //border = (Border) textComponent.getRenderProperty(TextComponent.PROPERTY_DISABLED_BORDER);
            font = (Font) textComponent.getRenderProperty(TextComponent.PROPERTY_DISABLED_FONT);
            foreground = (Color) textComponent.getRenderProperty(TextComponent.PROPERTY_DISABLED_FOREGROUND);

            // Fallback to normal styles.
            if (background == null) {
                background = (Color) textComponent.getRenderProperty(Component.PROPERTY_BACKGROUND);
                if (backgroundImage == null) {
                    // Special case:
                    // Disabled background without disabled background image will render disabled background instead of
                    // normal background image.
                    backgroundImage = (FillImage) textComponent.getRenderProperty(TextComponent.PROPERTY_BACKGROUND_IMAGE);
                }
            }
            //if (border == null) {
            //    border = (Border) textComponent.getRenderProperty(TextComponent.PROPERTY_BORDER);
            //}
            if (font == null) {
                font = (Font) textComponent.getRenderProperty(TextComponent.PROPERTY_FONT);
            }
            if (foreground == null) {
                foreground = (Color) textComponent.getRenderProperty(Component.PROPERTY_FOREGROUND);
            }
        } else {
            //border = (Border) textComponent.getRenderProperty(TextComponent.PROPERTY_BORDER);
            foreground = (Color) textComponent.getRenderProperty(Component.PROPERTY_FOREGROUND);
            background = (Color) textComponent.getRenderProperty(Component.PROPERTY_BACKGROUND);
            font = (Font) textComponent.getRenderProperty(Component.PROPERTY_FONT);
        }
        
        Alignment alignment = (Alignment) textComponent.getRenderProperty(TextComponent.PROPERTY_ALIGNMENT);
        
        if (alignment != null) {
            int horizontalAlignment = AlignmentRender.getRenderedHorizontal(alignment, textComponent);
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
        
        
        InsetsRender.renderToStyle(cssStyle, "padding", (Insets) textComponent.getRenderProperty(TextComponent.PROPERTY_INSETS));
        
        Extent width = (Extent) textComponent.getRenderProperty(TextComponent.PROPERTY_WIDTH);
        Extent height = (Extent) textComponent.getRenderProperty(TextComponent.PROPERTY_HEIGHT);

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
