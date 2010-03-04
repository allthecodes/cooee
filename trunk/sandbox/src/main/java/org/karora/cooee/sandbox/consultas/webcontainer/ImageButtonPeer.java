package org.karora.cooee.sandbox.consultas.webcontainer;

import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Border;
import org.karora.cooee.app.CheckBox;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.FillImage;
import org.karora.cooee.app.Font;
import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.RadioButton;
import org.karora.cooee.app.ResourceImageReference;
import org.karora.cooee.app.button.AbstractButton;
import org.karora.cooee.app.button.ButtonGroup;
import org.karora.cooee.app.button.ToggleButton;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.sandbox.consultas.app.ImageButton;
import org.karora.cooee.webcontainer.ActionProcessor;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.DomUpdateSupport;
import org.karora.cooee.webcontainer.PropertyUpdateProcessor;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webcontainer.image.ImageRenderSupport;
import org.karora.cooee.webcontainer.image.ImageTools;
import org.karora.cooee.webcontainer.propertyrender.AlignmentRender;
import org.karora.cooee.webcontainer.propertyrender.BorderRender;
import org.karora.cooee.webcontainer.propertyrender.ColorRender;
import org.karora.cooee.webcontainer.propertyrender.ExtentRender;
import org.karora.cooee.webcontainer.propertyrender.FillImageRender;
import org.karora.cooee.webcontainer.propertyrender.FontRender;
import org.karora.cooee.webcontainer.propertyrender.ImageReferenceRender;
import org.karora.cooee.webcontainer.propertyrender.InsetsRender;
import org.karora.cooee.webcontainer.propertyrender.LayoutDirectionRender;
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.output.CssStyle;
import org.karora.cooee.webrender.servermessage.DomUpdate;
import org.karora.cooee.webrender.service.JavaScriptService;
import org.karora.cooee.sandbox.consultas.webcontainer.TriCellTable;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Synchronization peer for 
 * <code>nextapp.echo2.app.AbstractButton</code>-derived components.
 * <p>
 * This class should not be extended or used by classes outside of the
 * Echo framework.
 */
public class ImageButtonPeer 
implements ActionProcessor, DomUpdateSupport, ImageRenderSupport, PropertyUpdateProcessor, ComponentSynchronizePeer {

    private static final Alignment DEFAULT_TEXT_POSITION = new Alignment(Alignment.TRAILING, Alignment.DEFAULT);
    private static final Alignment DEFAULT_STATE_POSITION = new Alignment(Alignment.LEADING, Alignment.DEFAULT);
    private static final Extent DEFAULT_ICON_TEXT_MARGIN = new Extent(5);
    private static final ImageReference DEFAULT_CHECKBOX_ICON
            = new ResourceImageReference("/org/karora/cooee/sandbox/consultas/webcontainer/resource/image/CheckBoxOff.gif");
    private static final ImageReference DEFAULT_SELECTED_CHECKBOX_ICON 
            = new ResourceImageReference("/org/karora/cooee/sandbox/consultas/webcontainer/resource/image/CheckBoxOn.gif");
    private static final ImageReference DEFAULT_RADIOBUTTON_ICON
            = new ResourceImageReference("/org/karora/cooee/sandbox/consultas/webcontainer/resource/image/RadioButtonOff.gif");
    private static final ImageReference DEFAULT_SELECTED_RADIOBUTTON_ICON 
            = new ResourceImageReference("/org/karora/cooee/sandbox/consultas/webcontainer/resource/image/RadioButtonOn.gif");
    
    private static final String[] BUTTON_INIT_KEYS
    		= new String[]{"rollover-style", "rollover-style-left", "rollover-style-center",
    						"rollover-style-right", "pressed-style", "pressed-style-left",
    						"pressed-style-center", "pressed-style-right"};
    private static final String IMAGE_ID_BACKGROUND = "background";
    private static final String IMAGE_ID_BACKGROUND_LEFT = "backgroundLeft";
    private static final String IMAGE_ID_BACKGROUND_RIGHT = "backgroundRight";
    
    private static final String IMAGE_ID_ICON = "icon";
    
    private static final String IMAGE_ID_ROLLOVER_BACKGROUND = "rolloverBackground";
    private static final String IMAGE_ID_ROLLOVER_BACKGROUND_LEFT = "rolloverBackgroundLeft";
    private static final String IMAGE_ID_ROLLOVER_BACKGROUND_RIGHT = "rolloverBackgroundRight";
    
    
    private static final String IMAGE_ID_ROLLOVER_ICON = "rolloverIcon";
    private static final String IMAGE_ID_ROLLOVER_STATE_ICON = "rolloverStateIcon";
    private static final String IMAGE_ID_ROLLOVER_SELECTED_STATE_ICON = "rolloverSelectedStateIcon";
    private static final String IMAGE_ID_PRESSED_BACKGROUND = "pressedBackground";
    private static final String IMAGE_ID_PRESSED_BACKGROUND_LEFT = "pressedBackgroundLeft";
    private static final String IMAGE_ID_PRESSED_BACKGROUND_RIGHT = "pressedBackgroundRight";
    
    private static final String IMAGE_ID_PRESSED_ICON = "pressedIcon";
    private static final String IMAGE_ID_PRESSED_STATE_ICON = "pressedStateIcon";
    private static final String IMAGE_ID_PRESSED_SELECTED_STATE_ICON = "pressedSelectedStateIcon";
    private static final String IMAGE_ID_STATE_ICON = "stateIcon";
    private static final String IMAGE_ID_SELECTED_STATE_ICON = "selectedStateIcon";
    
    private static final String CONTAINER_TABLE_CSS_TEXT_DEFAULT = "border:0px none;border-collapse:collapse;";
    private static final String CONTAINER_TABLE_CSS_TEXT_LEFT = "border:0px none;border-collapse:collapse; margin: 0 auto 0 0";
    private static final String CONTAINER_TABLE_CSS_TEXT_CENTER = "border:0px none;border-collapse:collapse; margin: 0 auto;";
    private static final String CONTAINER_TABLE_CSS_TEXT_RIGHT = "border:0px none;border-collapse:collapse; margin: 0 0 0 auto;";
    
    /**
     * Service to provide supporting JavaScript library.
     */
    private static final Service IMAGEBUTTON_SERVICE = JavaScriptService.forResource("Echo.ImageButton", 
            "/org/karora/cooee/sandbox/consultas/webcontainer/resource/js/ImageButton.js");

    static {
        WebRenderServlet.getServiceRegistry().add(IMAGEBUTTON_SERVICE);
    }
    
    /**
     * Determines the CSS text which should be placed in the 'style' attribute
     * of the button's container TABLE element.
     * 
     * @param button the rendering <code>AbstractButton</code>
     * @return the CSS text
     */
    private static String getContainerTableCssText(AbstractButton button) {
        Alignment alignment = (Alignment) button.getRenderProperty(AbstractButton.PROPERTY_ALIGNMENT);
        if (alignment != null) {
            int horizontal = AlignmentRender.getRenderedHorizontal(alignment, button);
            switch (horizontal) {
            case Alignment.LEFT:
                return CONTAINER_TABLE_CSS_TEXT_LEFT;
            case Alignment.CENTER:
                return CONTAINER_TABLE_CSS_TEXT_CENTER;
            case Alignment.RIGHT:
                return CONTAINER_TABLE_CSS_TEXT_RIGHT;
            }
        }
        return CONTAINER_TABLE_CSS_TEXT_DEFAULT;
    }
    
    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#getContainerId(nextapp.echo2.app.Component)
     */
    public String getContainerId(Component child) {
        throw new UnsupportedOperationException("Component does not support children.");
    }
    
    /**
     * Combines the properties of two <code>Alignment</code> objects together.
     * Properties of the <code>secondary</code> object will override default
     * properties of the <code>primary</code>.
     * 
     * @param primary the first <code>Alignment</code> (may be null)
     * @param secondary the second <code>Alignment</code> (may be null)
     * @return a new <code>Alignment</code> combining the values of both (or 
     *         null if both input <code>Alignment</code>s were null 
     */
    private Alignment combineAlignment(Alignment primary, Alignment secondary) {
        if (primary == null) {
            return secondary;
        } else if (secondary == null) {
            return primary;
        }
        int horizontal = primary.getHorizontal(); 
        int vertical = primary.getVertical();
        Alignment alignment = new Alignment(horizontal == Alignment.DEFAULT ? secondary.getHorizontal() : horizontal,
                vertical == Alignment.DEFAULT ? secondary.getVertical() : vertical);
        return alignment;
    }
    
    /**
     * @see nextapp.echo2.webcontainer.image.ImageRenderSupport#getImage(nextapp.echo2.app.Component, 
     *      java.lang.String)
     */
    public ImageReference getImage(Component component, String imageId) {
        if (IMAGE_ID_ICON.equals(imageId)) {
            if (component.isRenderEnabled()) {
                return (ImageReference) component.getRenderProperty(AbstractButton.PROPERTY_ICON);
            } else {
                ImageReference icon = (ImageReference) component.getRenderProperty(AbstractButton.PROPERTY_DISABLED_ICON);
                if (icon == null) {
                    icon = (ImageReference) component.getRenderProperty(AbstractButton.PROPERTY_ICON);
                }
                return icon;
            }
        } else if (IMAGE_ID_ROLLOVER_ICON.equals(imageId)) {
            return (ImageReference) component.getRenderProperty(AbstractButton.PROPERTY_ROLLOVER_ICON);
        } else if (IMAGE_ID_PRESSED_ICON.equals(imageId)) {
            return (ImageReference) component.getRenderProperty(AbstractButton.PROPERTY_PRESSED_ICON);
        } else if (IMAGE_ID_STATE_ICON.equals(imageId)) {
            return getStateIcon((ToggleButton) component);
        } else if (IMAGE_ID_SELECTED_STATE_ICON.equals(imageId)) {
            return getSelectedStateIcon((ToggleButton) component);
        } else if (IMAGE_ID_BACKGROUND.equals(imageId)) {
            FillImage backgroundImage;
            if (component.isRenderEnabled()) {
                backgroundImage = (FillImage) component.getRenderProperty(AbstractButton.PROPERTY_BACKGROUND_IMAGE);
            } else {
                backgroundImage = (FillImage) component.getRenderProperty(AbstractButton.PROPERTY_DISABLED_BACKGROUND_IMAGE);
                if (backgroundImage == null) {
                    backgroundImage = (FillImage) component.getRenderProperty(AbstractButton.PROPERTY_BACKGROUND_IMAGE);
                }
            }
            if (backgroundImage == null) {
                return null;
            } else {
                return backgroundImage.getImage();
            }
        } else if (IMAGE_ID_BACKGROUND_LEFT.equals(imageId)) {
            FillImage backgroundImage;
            if (component.isRenderEnabled()) {
                backgroundImage = (FillImage) component.getRenderProperty(ImageButton.PROPERTY_BACKGROUND_IMAGE_LEFT);
            } else {
                backgroundImage = (FillImage) component.getRenderProperty(ImageButton.PROPERTY_DISABLED_BACKGROUND_IMAGE_LEFT);
                if (backgroundImage == null) {
                    backgroundImage = (FillImage) component.getRenderProperty(ImageButton.PROPERTY_BACKGROUND_IMAGE_LEFT);
                }
            }
            if (backgroundImage == null) {
                return null;
            } else {
                return backgroundImage.getImage();
            }
        }else if (IMAGE_ID_BACKGROUND_RIGHT.equals(imageId)) {
            FillImage backgroundImage;
            if (component.isRenderEnabled()) {
                backgroundImage = (FillImage) component.getRenderProperty(ImageButton.PROPERTY_BACKGROUND_IMAGE_RIGHT);
            } else {
                backgroundImage = (FillImage) component.getRenderProperty(ImageButton.PROPERTY_DISABLED_BACKGROUND_IMAGE_RIGHT);
                if (backgroundImage == null) {
                    backgroundImage = (FillImage) component.getRenderProperty(ImageButton.PROPERTY_BACKGROUND_IMAGE_RIGHT);
                }
            }
            if (backgroundImage == null) {
                return null;
            } else {
                return backgroundImage.getImage();
            }
        }
        else if (IMAGE_ID_ROLLOVER_BACKGROUND.equals(imageId)) {
            FillImage backgroundImage 
                    = (FillImage) component.getRenderProperty(AbstractButton.PROPERTY_ROLLOVER_BACKGROUND_IMAGE);
            if (backgroundImage == null) {
                return null;
            } else {
                return backgroundImage.getImage();
            }
        } else if (IMAGE_ID_ROLLOVER_BACKGROUND_LEFT.equals(imageId)) {
            FillImage backgroundImage 
                    = (FillImage) component.getRenderProperty(ImageButton.PROPERTY_ROLLOVER_BACKGROUND_IMAGE_LEFT);
            if (backgroundImage == null) {
                return null;
            } else {
                return backgroundImage.getImage();
            }
        } else if (IMAGE_ID_ROLLOVER_BACKGROUND_RIGHT.equals(imageId)) {
            FillImage backgroundImage 
                    = (FillImage) component.getRenderProperty(ImageButton.PROPERTY_ROLLOVER_BACKGROUND_IMAGE_RIGHT);
            if (backgroundImage == null) {
                return null;
            } else {
                return backgroundImage.getImage();
            }
        } else if (IMAGE_ID_PRESSED_BACKGROUND.equals(imageId)) {
            FillImage backgroundImage 
                    = (FillImage) component.getRenderProperty(AbstractButton.PROPERTY_PRESSED_BACKGROUND_IMAGE);
            if (backgroundImage == null) {
                return null;
            } else {
                return backgroundImage.getImage();
            }
        } else if (IMAGE_ID_PRESSED_BACKGROUND_LEFT.equals(imageId)) {
            FillImage backgroundImage 
                    = (FillImage) component.getRenderProperty(ImageButton.PROPERTY_PRESSED_BACKGROUND_IMAGE_LEFT);
            if (backgroundImage == null) {
                return null;
            } else {
                return backgroundImage.getImage();
            }
        }  else if (IMAGE_ID_PRESSED_BACKGROUND_RIGHT.equals(imageId)) {
            FillImage backgroundImage 
                    = (FillImage) component.getRenderProperty(ImageButton.PROPERTY_PRESSED_BACKGROUND_IMAGE_RIGHT);
            if (backgroundImage == null) {
                return null;
            } else {
                return backgroundImage.getImage();
            }
        } else if (IMAGE_ID_ROLLOVER_STATE_ICON.equals(imageId)) {
            ImageReference icon = (ImageReference) component.getRenderProperty(ToggleButton.PROPERTY_ROLLOVER_STATE_ICON);
            return icon == null ? getStateIcon((ToggleButton) component) : icon; 
        } else if (IMAGE_ID_ROLLOVER_SELECTED_STATE_ICON.equals(imageId)) {
            ImageReference icon = (ImageReference) component.getRenderProperty(ToggleButton.PROPERTY_ROLLOVER_SELECTED_STATE_ICON);
            return icon == null ? getSelectedStateIcon((ToggleButton) component) : icon; 
        } else if (IMAGE_ID_PRESSED_STATE_ICON.equals(imageId)) {
            ImageReference icon = (ImageReference) component.getRenderProperty(ToggleButton.PROPERTY_PRESSED_STATE_ICON);
            return icon == null ? getStateIcon((ToggleButton) component) : icon; 
        } else if (IMAGE_ID_PRESSED_SELECTED_STATE_ICON.equals(imageId)) {
            ImageReference icon = (ImageReference) component.getRenderProperty(ToggleButton.PROPERTY_PRESSED_SELECTED_STATE_ICON);
            return icon == null ? getSelectedStateIcon((ToggleButton) component) : icon; 
        } else {
            return null;
        }
    }

    /**
     * Determines the selected state icon of the specified
     * <code>ToggleButton</code>.
     * 
     * @param toggleButton the <code>ToggleButton</code>
     * @return the selected state icon
     */
    private ImageReference getSelectedStateIcon(ToggleButton toggleButton) {
        ImageReference selectedStateIcon 
                = (ImageReference) toggleButton.getRenderProperty(ToggleButton.PROPERTY_SELECTED_STATE_ICON);
        if (selectedStateIcon == null) {
            if (toggleButton instanceof CheckBox) {
                selectedStateIcon = DEFAULT_SELECTED_CHECKBOX_ICON;
            } else if (toggleButton instanceof RadioButton) {
                selectedStateIcon = DEFAULT_SELECTED_RADIOBUTTON_ICON;
            }
        }
        return selectedStateIcon;
    }
    
    /**
     * Determines the default (non-selected) state icon of the specified
     * <code>ToggleButton</code>.
     * 
     * @param toggleButton the <code>ToggleButton</code>
     * @return the state icon
     */
    private ImageReference getStateIcon(ToggleButton toggleButton) {
        ImageReference stateIcon = (ImageReference) toggleButton.getRenderProperty(ToggleButton.PROPERTY_STATE_ICON);
        if (stateIcon == null) {
            if (toggleButton instanceof CheckBox) {
                stateIcon = DEFAULT_CHECKBOX_ICON;
            } else if (toggleButton instanceof RadioButton) {
                stateIcon = DEFAULT_RADIOBUTTON_ICON;
            }
        }
        return stateIcon;
    }
    
    /**
     * @see nextapp.echo2.webcontainer.ActionProcessor#processAction(nextapp.echo2.webcontainer.ContainerInstance, 
     *      nextapp.echo2.app.Component, org.w3c.dom.Element)
     */
    public void processAction(ContainerInstance ci, Component component, Element actionElement) {
        ci.getUpdateManager().getClientUpdateManager().setComponentAction(component, AbstractButton.INPUT_CLICK, null);
    }

    /**
     * @see nextapp.echo2.webcontainer.PropertyUpdateProcessor#processPropertyUpdate(
     *      nextapp.echo2.webcontainer.ContainerInstance, nextapp.echo2.app.Component, org.w3c.dom.Element)
     */
    public void processPropertyUpdate(ContainerInstance ci, Component component, Element propertyElement) {
        
        String propertyName = propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_NAME);
        if (ToggleButton.SELECTED_CHANGED_PROPERTY.equals(propertyName)) {
            Boolean propertyValue = new Boolean("true".equals(propertyElement.getAttribute("value")));
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, 
                    ToggleButton.SELECTED_CHANGED_PROPERTY, propertyValue);
        }
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
     * Renders the containing DIV element of a button.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param parentNode the parent node
     * @param button the <code>AbstractButton</code> being rendered
     * @return the rendered DIV element (note that this element will already 
     *         have been appended to the parent)
     */
    private Element renderButtonContainer(RenderContext rc, Node parentNode, ImageButton button) {
    	Document document = rc.getServerMessage().getDocument();
    	TriCellTable table = new TriCellTable(rc, document, 
    								ContainerInstance.getElementId(button) + "_outer", 
    								TriCellTable.LEADING_TRAILING, null, 
    								TriCellTable.LEADING_TRAILING, null);
    	table.addCellCssText("padding:0px;");
    	
        Element tableElement = table.getTableElement();
        tableElement.setAttribute("id", ContainerInstance.getElementId(button) + "_outer_table");
        tableElement.setAttribute("style", getContainerTableCssText(button));
        tableElement.setAttribute("width", "100%");
       
    	
        Element divElement = parentNode.getOwnerDocument().createElement("div");
        divElement.setAttribute("id", ContainerInstance.getElementId(button));


        
        Element centerElement = parentNode.getOwnerDocument().createElement("div");
        centerElement.setAttribute("id", ContainerInstance.getElementId(button) + "_center");

        table.getTdElement(1).appendChild(centerElement);
        divElement.appendChild(table.getTableElement());
        
        if (button.isFocusTraversalParticipant()) {
            divElement.setAttribute("tabindex", Integer.toString(button.getFocusTraversalIndex()));
        } else {
            divElement.setAttribute("tabindex", "-1");
        }
        
        CssStyle cssStyle = new CssStyle();
        
        CssStyle cssStyleCenter = new CssStyle();
        CssStyle cssStyleLeft = new CssStyle();
        CssStyle cssStyleRight = new CssStyle();
        
        cssStyle.setAttribute("cursor", "pointer");
        cssStyleCenter.setAttribute("-moz-user-select", "none");
        cssStyle.setAttribute("margin", "0px");
        cssStyle.setAttribute("border-spacing", "0px");
        cssStyleLeft.setAttribute("padding", "0px");
        cssStyleCenter.setAttribute("padding", "0px");
        cssStyleRight.setAttribute("padding", "0px");
        cssStyleRight.setAttribute("z-index", "999999");
        LayoutDirectionRender.renderToStyle(cssStyle, button.getLayoutDirection(), button.getLocale());
        ExtentRender.renderToStyle(cssStyle, "width", (Extent) button.getRenderProperty(AbstractButton.PROPERTY_WIDTH));
        Extent height = (Extent) button.getRenderProperty(AbstractButton.PROPERTY_HEIGHT);
        if (height != null) {
            ExtentRender.renderToStyle(cssStyle, "height", height);
            ExtentRender.renderToStyle(cssStyleLeft, "height", height);
            ExtentRender.renderToStyle(cssStyleCenter, "height", height);
            ExtentRender.renderToStyle(cssStyleRight, "height", height);
            
            cssStyle.setAttribute("overflow", "hidden");
        }
        if (Boolean.FALSE.equals(button.getRenderProperty(AbstractButton.PROPERTY_LINE_WRAP))) {
            cssStyle.setAttribute("white-space", "nowrap");
        }
        
        boolean renderEnabled = button.isRenderEnabled();

        Border border;
        Color foreground, background;
        Font font;
        FillImage backgroundImage;
        FillImage backgroundImageLeft;
        FillImage backgroundImageRight;
        
        if (!renderEnabled) {
            // Retrieve disabled style information.
            background = (Color) button.getRenderProperty(AbstractButton.PROPERTY_DISABLED_BACKGROUND);
            backgroundImage = (FillImage) button.getRenderProperty(AbstractButton.PROPERTY_DISABLED_BACKGROUND_IMAGE);
            backgroundImageLeft = (FillImage) button.getRenderProperty(ImageButton.PROPERTY_DISABLED_BACKGROUND_IMAGE_LEFT);
            backgroundImageRight = (FillImage) button.getRenderProperty(ImageButton.PROPERTY_DISABLED_BACKGROUND_IMAGE_RIGHT);
            
            border = (Border) button.getRenderProperty(AbstractButton.PROPERTY_DISABLED_BORDER);
            font = (Font) button.getRenderProperty(AbstractButton.PROPERTY_DISABLED_FONT);
            foreground = (Color) button.getRenderProperty(AbstractButton.PROPERTY_DISABLED_FOREGROUND);

            // Fallback to normal styles.
            if (background == null) {
                background = (Color) button.getRenderProperty(AbstractButton.PROPERTY_BACKGROUND);
                if (backgroundImage == null) {
                    // Special case:
                    // Disabled background without disabled background image will render disabled background instead of
                    // normal background image.
                    backgroundImage = (FillImage) button.getRenderProperty(AbstractButton.PROPERTY_BACKGROUND_IMAGE);
                }
            }
//          Fallback to normal styles.
            if (backgroundImageLeft == null) {
                backgroundImageLeft = (FillImage) button.getRenderProperty(ImageButton.PROPERTY_BACKGROUND_IMAGE_LEFT);
                
            }
            if (backgroundImageRight == null) {
                backgroundImageRight = (FillImage) button.getRenderProperty(ImageButton.PROPERTY_BACKGROUND_IMAGE_RIGHT);
                
            }
            if (border == null) {
                border = (Border) button.getRenderProperty(AbstractButton.PROPERTY_BORDER);
            }
            if (font == null) {
                font = (Font) button.getRenderProperty(AbstractButton.PROPERTY_FONT);
            }
            if (foreground == null) {
                foreground = (Color) button.getRenderProperty(AbstractButton.PROPERTY_FOREGROUND);
            }
        } else {
            border = (Border) button.getRenderProperty(AbstractButton.PROPERTY_BORDER);
            foreground = (Color) button.getRenderProperty(AbstractButton.PROPERTY_FOREGROUND);
            background = (Color) button.getRenderProperty(AbstractButton.PROPERTY_BACKGROUND);
            font = (Font) button.getRenderProperty(AbstractButton.PROPERTY_FONT);
            backgroundImage = (FillImage) button.getRenderProperty(AbstractButton.PROPERTY_BACKGROUND_IMAGE);
            backgroundImageLeft = (FillImage) button.getRenderProperty(ImageButton.PROPERTY_BACKGROUND_IMAGE_LEFT);
            backgroundImageRight = (FillImage) button.getRenderProperty(ImageButton.PROPERTY_BACKGROUND_IMAGE_RIGHT);
        }
        
        BorderRender.renderToStyle(cssStyle, border);
        ColorRender.renderToStyle(cssStyle, foreground, background);
        FontRender.renderToStyle(cssStyle, font);
        
        FillImageRender.renderToStyle(cssStyleCenter, rc, this, button, IMAGE_ID_BACKGROUND, backgroundImage, 
                FillImageRender.FLAG_DISABLE_FIXED_MODE);
        
        FillImageRender.renderToStyle(cssStyleLeft, rc, this, button, IMAGE_ID_BACKGROUND_LEFT, backgroundImageLeft, 
                FillImageRender.FLAG_DISABLE_FIXED_MODE);
        
        FillImageRender.renderToStyle(cssStyleRight, rc, this, button, IMAGE_ID_BACKGROUND_RIGHT, backgroundImageRight, 
                FillImageRender.FLAG_DISABLE_FIXED_MODE);
        
        InsetsRender.renderToStyle(cssStyleCenter, "padding", (Insets) button.getRenderProperty(AbstractButton.PROPERTY_INSETS));
        
        AlignmentRender.renderToStyle(cssStyleCenter,
                (Alignment) button.getRenderProperty(AbstractButton.PROPERTY_ALIGNMENT), button);
        if (button.getBackgroundImageLeftWidth() != null)
        {
            ExtentRender.renderToStyle(cssStyleLeft, "width", (Extent) button.getRenderProperty(ImageButton.PROPERTY_BACKGROUND_IMAGE_LEFT_WIDTH));
        }
        if (button.getBackgroundImageRightWidth() != null)
        {
            ExtentRender.renderToStyle(cssStyleRight, "width", (Extent) button.getRenderProperty(ImageButton.PROPERTY_BACKGROUND_IMAGE_RIGHT_WIDTH));
        }
        String toolTipText = (String) button.getRenderProperty(AbstractButton.PROPERTY_TOOL_TIP_TEXT);
        if (renderEnabled && toolTipText != null) {
            divElement.setAttribute("title", toolTipText);
        }
        cssStyle.setAttribute("position", "relative");
        divElement.setAttribute("style", cssStyle.renderInline());

        
        table.getTdElement(0).setAttribute("style", cssStyleLeft.renderInline());
        table.getTdElement(1).setAttribute("style", cssStyleCenter.renderInline());
        table.getTdElement(2).setAttribute("style", cssStyleRight.renderInline());
        parentNode.appendChild(divElement); 
        
        return divElement;
    }
    
    /**
     * Renders the content of the button, i.e., its text, icon, and/or state icon.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param buttonContainerElement the <code>Element</code> which will 
     *        contain the content
     * @param button the <code>AbstractButton</code> being rendered
     */
    private void renderButtonContent(RenderContext rc, Element buttonContainerElement, AbstractButton button) {
        Node contentNode;
        Document document = rc.getServerMessage().getDocument();
        ToggleButton toggleButton = button instanceof ToggleButton ? (ToggleButton) button : null;
        String elementId = ContainerInstance.getElementId(button);
        
        String text = (String) button.getRenderProperty(AbstractButton.PROPERTY_TEXT);
        ImageReference icon = (ImageReference) button.getRenderProperty(AbstractButton.PROPERTY_ICON);
        
        // Create entities.
        Text textNode = text == null ? null : rc.getServerMessage().getDocument().createTextNode(
                (String) button.getRenderProperty(AbstractButton.PROPERTY_TEXT));
        
        Element iconElement;
        if (icon == null) {
            iconElement = null;
        } else {
            iconElement = ImageReferenceRender.renderImageReferenceElement(rc, ImageButtonPeer.this, button, 
                    IMAGE_ID_ICON);
            iconElement.setAttribute("id", elementId + "_icon");
        }

        Element stateIconElement;
        if (toggleButton == null) {
            stateIconElement = null;
        } else {
            stateIconElement = ImageReferenceRender.renderImageReferenceElement(rc, ImageButtonPeer.this, button, 
                    toggleButton.isSelected() ? IMAGE_ID_SELECTED_STATE_ICON : IMAGE_ID_STATE_ICON);
            stateIconElement.setAttribute("id", elementId + "_stateicon");
        }
        
        int entityCount = (textNode == null ? 0 : 1) + (iconElement == null ? 0 : 1) + (stateIconElement == null ? 0 : 1);
        
        Extent iconTextMargin;
        Alignment textPosition;
        Element tableElement;
        
        switch (entityCount) {
        case 1:
            if (textNode != null) {
                contentNode = textNode;
            } else if (iconElement != null) {
                contentNode = iconElement;
            } else { // stateIconElement must not be null.
                contentNode = stateIconElement;
            }
            break;
        case 2:
            iconTextMargin = (Extent) button.getRenderProperty(AbstractButton.PROPERTY_ICON_TEXT_MARGIN, 
                    DEFAULT_ICON_TEXT_MARGIN);
            TriCellTable tct;
            textPosition = (Alignment) button.getRenderProperty(AbstractButton.PROPERTY_TEXT_POSITION, 
                    DEFAULT_TEXT_POSITION);
            if (stateIconElement == null) {
                // Not rendering a ToggleButton.
                int orientation = TriCellTableConfigurator.convertIconTextPositionToOrientation(textPosition, button);
                tct = new TriCellTable(rc, document, elementId, orientation, iconTextMargin);
                
                renderCellText(tct, textNode, button);
                renderCellIcon(tct, iconElement, 1, button);
            } else {
                 // Rendering a ToggleButton.
                Extent stateMargin = (Extent) button.getRenderProperty(ToggleButton.PROPERTY_STATE_MARGIN, 
                        DEFAULT_ICON_TEXT_MARGIN);
                Alignment statePosition = (Alignment) button.getRenderProperty(ToggleButton.PROPERTY_STATE_POSITION,
                        DEFAULT_STATE_POSITION);
                int orientation = TriCellTableConfigurator.convertStatePositionToOrientation(statePosition, button);
                tct = new TriCellTable(rc, document, elementId, orientation, stateMargin);

                if (textNode == null) {
                    renderCellIcon(tct, iconElement, 0, button);
                } else {
                    renderCellText(tct, textNode, button);
                }
                renderCellState(tct, stateIconElement, 1, button);
            }

            tct.addCellCssText("padding:0px;");
            tableElement = tct.getTableElement();
            tableElement.setAttribute("id", elementId + "_table");
            tableElement.setAttribute("style", getContainerTableCssText(button));
            contentNode = tableElement;
            break;
        case 3:
            iconTextMargin = (Extent) button.getRenderProperty(AbstractButton.PROPERTY_ICON_TEXT_MARGIN, 
                    DEFAULT_ICON_TEXT_MARGIN);
            textPosition = (Alignment) button.getRenderProperty(AbstractButton.PROPERTY_TEXT_POSITION, 
                    DEFAULT_TEXT_POSITION);
            Extent stateMargin = (Extent) button.getRenderProperty(ToggleButton.PROPERTY_STATE_MARGIN, 
                    DEFAULT_ICON_TEXT_MARGIN);
            Alignment statePosition = (Alignment) button.getRenderProperty(ToggleButton.PROPERTY_STATE_POSITION,
                    DEFAULT_STATE_POSITION);
            int stateOrientation = TriCellTableConfigurator.convertStatePositionToOrientation(statePosition, button);
            int orientation = TriCellTableConfigurator.convertIconTextPositionToOrientation(textPosition, button);
            tct = new TriCellTable(rc, document, elementId, orientation, iconTextMargin, stateOrientation, stateMargin);

            renderCellText(tct, textNode, button);
            renderCellIcon(tct, iconElement, 1, button);
            renderCellState(tct, stateIconElement, 2, button);

            tct.addCellCssText("padding:0px;");
            tableElement = tct.getTableElement();
            tableElement.setAttribute("id", elementId + "_table");
            tableElement.setAttribute("style", getContainerTableCssText(button));
            contentNode = tableElement;
            break;
        default:
            // 0 element button.
            contentNode = null;
        }
        
        if (contentNode != null) {
            buttonContainerElement.appendChild(contentNode);
        }
    }
    
    /**
     * Renders the content of the <code>TriCellTable</code> cell which 
     * contains the button's icon.
     * 
     * @param tct the <code>TriCellTable</code> to update
     * @param iconElement the icon element
     * @param cellIndex the index of the cell in the <code>TriCellTable</code>
     *        that should contain the icon
     */
    private void renderCellIcon(TriCellTable tct, Element iconElement, int cellIndex, AbstractButton button) {
        Element iconTdElement = tct.getTdElement(cellIndex);
        Alignment alignment = (Alignment) button.getRenderProperty(AbstractButton.PROPERTY_ALIGNMENT);
        if (alignment != null) {
            CssStyle style = new CssStyle();
            AlignmentRender.renderToStyle(style, alignment, button);
            iconTdElement.setAttribute("style", style.renderInline());
        }
        iconTdElement.appendChild(iconElement);
    }
    
    /**
     * Renders the content of the <code>TriCellTable</code> cell which 
     * contains the button's state icon.
     * 
     * @param tct the <code>TriCellTable</code> to update
     * @param stateIconElement the state icon element
     * @param cellIndex the index of the cell in the <code>TriCellTable</code>
     *        that should contain the state icon
     * @param button the <code>AbstractButton</code> being rendered
     */
    private void renderCellState(TriCellTable tct, Element stateIconElement, int cellIndex, AbstractButton button) {
        Element stateTdElement = tct.getTdElement(cellIndex);
        CssStyle stateTdCssStyle = new CssStyle();
        AlignmentRender.renderToStyle(stateTdCssStyle,  
                (Alignment) button.getRenderProperty(ToggleButton.PROPERTY_STATE_ALIGNMENT), button);
        stateTdElement.setAttribute("style", stateTdCssStyle.renderInline());
        stateTdElement.appendChild(stateIconElement);
    }
    
    /**
     * Renders the content of the <code>TriCellTable</code> cell which 
     * contains the button's text.
     * Text is always rendered in cell #0 of the table.
     * 
     * @param tct the <code>TriCellTable</code> to update
     * @param textNode the text
     * @param button the <code>AbstractButton</code> being rendered
     */
    private void renderCellText(TriCellTable tct, Text textNode, AbstractButton button) {
        Element textTdElement = tct.getTdElement(0);
        CssStyle textTdCssStyle = new CssStyle();
        
        if (Boolean.FALSE.equals(button.getRenderProperty(AbstractButton.PROPERTY_LINE_WRAP))) {
            textTdCssStyle.setAttribute("white-space", "nowrap");
        }
        
        Alignment alignment = combineAlignment((Alignment) button.getRenderProperty(AbstractButton.PROPERTY_TEXT_ALIGNMENT),
                (Alignment) button.getRenderProperty(AbstractButton.PROPERTY_ALIGNMENT));
        AlignmentRender.renderToStyle(textTdCssStyle, alignment, button);
        
        if (textTdCssStyle.hasAttributes()) {
            textTdElement.setAttribute("style", textTdCssStyle.renderInline());
        }
        
        textTdElement.appendChild(textNode);
    }
    
    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderDispose(nextapp.echo2.webcontainer.RenderContext, 
     *      nextapp.echo2.app.update.ServerComponentUpdate, nextapp.echo2.app.Component)
     */
    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {
        rc.getServerMessage().addLibrary(IMAGEBUTTON_SERVICE.getId());
        renderDisposeDirective(rc, (AbstractButton) component);
    }

    /**
     * Renders a directive to the outgoing <code>ServerMessage</code> to 
     * dispose the state of a button, performing tasks such as unregistering
     * event listeners on the client.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param button the button
     */
    private void renderDisposeDirective(RenderContext rc, AbstractButton button) {
        ServerMessage serverMessage = rc.getServerMessage();
        Element itemizedUpdateElement = serverMessage.getItemizedDirective(ServerMessage.GROUP_ID_PREREMOVE,
                "EchoImageButton.MessageProcessor", "dispose",  new String[0], new String[0]);
        Element itemElement = serverMessage.getDocument().createElement("item");
        itemElement.setAttribute("eid", ContainerInstance.getElementId(button));
        itemizedUpdateElement.appendChild(itemElement);
    }
    
    /**
     * @see nextapp.echo2.webcontainer.DomUpdateSupport#renderHtml(nextapp.echo2.webcontainer.RenderContext, 
     *      nextapp.echo2.app.update.ServerComponentUpdate, org.w3c.dom.Node, nextapp.echo2.app.Component)
     */
    public void renderHtml(RenderContext rc, ServerComponentUpdate update, Node parentNode, Component component) {
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(IMAGEBUTTON_SERVICE.getId());
        ImageButton button = (ImageButton) component;
        Element containerDivElement = renderButtonContainer(rc, parentNode, button);
        renderInitDirective(rc, button);
        renderButtonContent(rc, (Element)containerDivElement.getFirstChild().getFirstChild()
        							.getFirstChild().getChildNodes().item(1).getFirstChild(), button);
    }

    /**
     * Renders a directive to the outgoing <code>ServerMessage</code> to 
     * initialize the state of a button, performing tasks such as registering
     * event listeners on the client.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param button the button
     */
    private void renderInitDirective(RenderContext rc, AbstractButton button) {
        String elementId = ContainerInstance.getElementId(button);
        ServerMessage serverMessage = rc.getServerMessage();
        FillImage backgroundImage = (FillImage) button.getRenderProperty(AbstractButton.PROPERTY_BACKGROUND_IMAGE);
        FillImage backgroundImageLeft = (FillImage) button.getRenderProperty(AbstractButton.PROPERTY_BACKGROUND_IMAGE);
        FillImage backgroundImageRight = (FillImage) button.getRenderProperty(AbstractButton.PROPERTY_BACKGROUND_IMAGE);
        
        boolean rolloverEnabled = ((Boolean) button.getRenderProperty(AbstractButton.PROPERTY_ROLLOVER_ENABLED, 
                Boolean.FALSE)).booleanValue();
        boolean pressedEnabled = ((Boolean) button.getRenderProperty(AbstractButton.PROPERTY_PRESSED_ENABLED, 
                Boolean.FALSE)).booleanValue();
        
        String pressedStyle = "";
        String pressedStyleLeft = "";
        String pressedStyleCenter = "";
        String pressedStyleRight = "";
        
        String rolloverStyle = "";
        String rolloverStyleLeft = "";
        String rolloverStyleCenter = "";
        String rolloverStyleRight = "";
        
        
        String defaultIconUri = null;
        String rolloverIconUri = null;
        String pressedIconUri = null;
        
        if (rolloverEnabled || pressedEnabled) {
            boolean hasIcon = button.getRenderProperty(AbstractButton.PROPERTY_ICON) != null;
            if (hasIcon) {
                defaultIconUri = ImageTools.getUri(rc, this, button, IMAGE_ID_ICON);
            }
            
            if (rolloverEnabled) {
                CssStyle rolloverCssStyle = new CssStyle();
                CssStyle rolloverCssStyleLeft = new CssStyle();
                CssStyle rolloverCssStyleRight = new CssStyle();
                CssStyle rolloverCssStyleCenter = new CssStyle();
                
                
                BorderRender.renderToStyle(rolloverCssStyle, 
                        (Border) button.getRenderProperty(AbstractButton.PROPERTY_ROLLOVER_BORDER));
                ColorRender.renderToStyle(rolloverCssStyle, 
                        (Color) button.getRenderProperty(AbstractButton.PROPERTY_ROLLOVER_FOREGROUND),
                        (Color) button.getRenderProperty(AbstractButton.PROPERTY_ROLLOVER_BACKGROUND));
                FontRender.renderToStyle(rolloverCssStyle, 
                        (Font) button.getRenderProperty(AbstractButton.PROPERTY_ROLLOVER_FONT));
                if (backgroundImage != null) {
                    FillImageRender.renderToStyle(rolloverCssStyleCenter, rc, this, button, IMAGE_ID_ROLLOVER_BACKGROUND,
                            (FillImage) button.getRenderProperty(AbstractButton.PROPERTY_ROLLOVER_BACKGROUND_IMAGE), 
                            FillImageRender.FLAG_DISABLE_FIXED_MODE);                   
                }
                if (backgroundImageLeft != null) {
                    FillImageRender.renderToStyle(rolloverCssStyleLeft, rc, this, button, IMAGE_ID_ROLLOVER_BACKGROUND_LEFT,
                            (FillImage) button.getRenderProperty(ImageButton.PROPERTY_ROLLOVER_BACKGROUND_IMAGE_LEFT), 
                            FillImageRender.FLAG_DISABLE_FIXED_MODE);
                }
                if (backgroundImageRight != null) {
                    FillImageRender.renderToStyle(rolloverCssStyleRight, rc, this, button, IMAGE_ID_ROLLOVER_BACKGROUND_RIGHT,
                            (FillImage) button.getRenderProperty(ImageButton.PROPERTY_ROLLOVER_BACKGROUND_IMAGE_RIGHT), 
                            FillImageRender.FLAG_DISABLE_FIXED_MODE);
                }
                if (rolloverCssStyle.hasAttributes()) {
                    rolloverStyle = rolloverCssStyle.renderInline();
                }
                if (rolloverCssStyleLeft.hasAttributes()) {
                    rolloverStyleLeft = rolloverCssStyleLeft.renderInline();
                }
                if (rolloverCssStyleCenter.hasAttributes()) {
                    rolloverStyleCenter = rolloverCssStyleCenter.renderInline();
                }
                if (rolloverCssStyleRight.hasAttributes()) {
                    rolloverStyleRight = rolloverCssStyleRight.renderInline();
                }
                if (hasIcon) {
                    ImageReference rolloverIcon = (ImageReference) button.getRenderProperty(AbstractButton.PROPERTY_ROLLOVER_ICON);
                    if (rolloverIcon != null) {
                        rolloverIconUri = ImageTools.getUri(rc, this, button, IMAGE_ID_ROLLOVER_ICON);
                    }
                }
            }
            
            if (pressedEnabled) {
                CssStyle pressedCssStyle = new CssStyle();
                CssStyle pressedCssStyleLeft = new CssStyle();
                CssStyle pressedCssStyleCenter = new CssStyle();
                CssStyle pressedCssStyleRight = new CssStyle();
                
                BorderRender.renderToStyle(pressedCssStyle, 
                        (Border) button.getRenderProperty(AbstractButton.PROPERTY_PRESSED_BORDER));
                ColorRender.renderToStyle(pressedCssStyle, 
                        (Color) button.getRenderProperty(AbstractButton.PROPERTY_PRESSED_FOREGROUND),
                        (Color) button.getRenderProperty(AbstractButton.PROPERTY_PRESSED_BACKGROUND));
                FontRender.renderToStyle(pressedCssStyle, 
                        (Font) button.getRenderProperty(AbstractButton.PROPERTY_PRESSED_FONT));
                if (backgroundImage != null) {
                    FillImageRender.renderToStyle(pressedCssStyleCenter, rc, this, button, IMAGE_ID_PRESSED_BACKGROUND,
                            (FillImage) button.getRenderProperty(AbstractButton.PROPERTY_PRESSED_BACKGROUND_IMAGE), 
                            FillImageRender.FLAG_DISABLE_FIXED_MODE);
                }
                if (backgroundImageLeft != null) {
                    FillImageRender.renderToStyle(pressedCssStyleLeft, rc, this, button, IMAGE_ID_PRESSED_BACKGROUND_LEFT,
                            (FillImage) button.getRenderProperty(ImageButton.PROPERTY_PRESSED_BACKGROUND_IMAGE_LEFT), 
                            FillImageRender.FLAG_DISABLE_FIXED_MODE);
                }
                if (backgroundImageRight != null) {
                    FillImageRender.renderToStyle(pressedCssStyleRight, rc, this, button, IMAGE_ID_PRESSED_BACKGROUND_RIGHT,
                            (FillImage) button.getRenderProperty(ImageButton.PROPERTY_PRESSED_BACKGROUND_IMAGE_RIGHT), 
                            FillImageRender.FLAG_DISABLE_FIXED_MODE);
                }
                if (pressedCssStyle.hasAttributes()) {
                    pressedStyle = pressedCssStyle.renderInline();
                }
                if (pressedCssStyleLeft.hasAttributes()) {
                    pressedStyleLeft = pressedCssStyleLeft.renderInline();
                }
                if (pressedCssStyleCenter.hasAttributes()) {
                    pressedStyleCenter = pressedCssStyleCenter.renderInline();
                }
                if (pressedCssStyleRight.hasAttributes()) {
                    pressedStyleRight = pressedCssStyleRight.renderInline();
                }
                if (hasIcon) {
                    ImageReference pressedIcon = (ImageReference) button.getRenderProperty(AbstractButton.PROPERTY_PRESSED_ICON);
                    if (pressedIcon != null) {
                        pressedIconUri = ImageTools.getUri(rc, this, button, IMAGE_ID_PRESSED_ICON);
                    }
                }
            }
        }
        
        Element itemizedUpdateElement = serverMessage.getItemizedDirective(ServerMessage.GROUP_ID_POSTUPDATE,
                "EchoImageButton.MessageProcessor", "init",  BUTTON_INIT_KEYS, new String[]{rolloverStyle,rolloverStyleLeft,rolloverStyleCenter,rolloverStyleRight, pressedStyle, pressedStyleLeft, pressedStyleCenter, pressedStyleRight});
        Element itemElement = serverMessage.getDocument().createElement("item");
        itemElement.setAttribute("eid", elementId);
        if (defaultIconUri != null) {
            itemElement.setAttribute("default-icon", defaultIconUri);
        }
        if (rolloverIconUri != null) {
            itemElement.setAttribute("rollover-icon", rolloverIconUri);
        }
        if (pressedIconUri != null) {
            itemElement.setAttribute("pressed-icon", pressedIconUri);
        }
        if (!button.hasActionListeners()) {
            itemElement.setAttribute("server-notify", "false");
        }
        if (!button.isRenderEnabled()) {
            itemElement.setAttribute("enabled", "false");
        }

        if (button instanceof ToggleButton) {
            ToggleButton toggleButton = (ToggleButton) button;
            itemElement.setAttribute("toggle", "true");
            itemElement.setAttribute("selected", toggleButton.isSelected() ? "true" : "false");
            itemElement.setAttribute("state-icon", ImageTools.getUri(rc, this, toggleButton, IMAGE_ID_STATE_ICON));
            itemElement.setAttribute("selected-state-icon", ImageTools.getUri(rc, this, toggleButton, 
                    IMAGE_ID_SELECTED_STATE_ICON));
            
            if (rolloverEnabled && toggleButton.getRenderProperty(ToggleButton.PROPERTY_ROLLOVER_STATE_ICON) != null
                    && toggleButton.getRenderProperty(ToggleButton.PROPERTY_ROLLOVER_SELECTED_STATE_ICON) != null) {
                itemElement.setAttribute("rollover-state-icon", 
                        ImageTools.getUri(rc, this, toggleButton, IMAGE_ID_ROLLOVER_STATE_ICON));
                itemElement.setAttribute("rollover-selected-state-icon", 
                        ImageTools.getUri(rc, this, toggleButton, IMAGE_ID_ROLLOVER_SELECTED_STATE_ICON));
            }
            if (pressedEnabled && toggleButton.getRenderProperty(ToggleButton.PROPERTY_PRESSED_STATE_ICON) != null
                    && toggleButton.getRenderProperty(ToggleButton.PROPERTY_PRESSED_SELECTED_STATE_ICON) != null) {
                itemElement.setAttribute("pressed-state-icon", 
                        ImageTools.getUri(rc, this, toggleButton, IMAGE_ID_PRESSED_STATE_ICON));
                itemElement.setAttribute("pressed-selected-state-icon", 
                        ImageTools.getUri(rc, this, toggleButton, IMAGE_ID_PRESSED_SELECTED_STATE_ICON));
            }
            
            if (button instanceof RadioButton) {
                ButtonGroup buttonGroup = ((RadioButton) toggleButton).getGroup();
                if (buttonGroup != null) {
                    rc.getContainerInstance().getIdTable().register(buttonGroup);
                    itemElement.setAttribute("group", buttonGroup.getRenderId());
                }
            }
        }
        
        itemizedUpdateElement.appendChild(itemElement);
    }
    
    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderUpdate(nextapp.echo2.webcontainer.RenderContext, 
     *      nextapp.echo2.app.update.ServerComponentUpdate, java.lang.String)
     */
    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
        String parentId = ContainerInstance.getElementId(update.getParent());
        DomUpdate.renderElementRemove(rc.getServerMessage(), parentId);
        renderAdd(rc, update, targetId, update.getParent());
        return false;
    }
}
