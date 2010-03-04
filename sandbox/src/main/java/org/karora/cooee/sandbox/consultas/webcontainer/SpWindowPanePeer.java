/* 
 * Copyright (c) 2007, Karora and others 
 * Version: MPL 1.1
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
 */

package org.karora.cooee.sandbox.consultas.webcontainer;

import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.FillImage;
import org.karora.cooee.app.FillImageBorder;
import org.karora.cooee.app.Font;
import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.ResourceImageReference;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.sandbox.consultas.app.SpWindowPane;
import org.karora.cooee.webcontainer.ActionProcessor;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.PartialUpdateManager;
import org.karora.cooee.webcontainer.PropertyUpdateProcessor;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webcontainer.SynchronizePeerFactory;
import org.karora.cooee.webcontainer.image.ImageRenderSupport;
import org.karora.cooee.webcontainer.image.ImageTools;
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
import org.karora.cooee.webrender.service.JavaScriptService;
import org.w3c.dom.Element;


/**
 * Synchronization peer for <code>nextapp.echo2.app.WindowPane</code>
 * components.
 * <p>
 * This class should not be extended or used by classes outside of the Echo
 * framework.
 */
public class SpWindowPanePeer implements ActionProcessor, ImageRenderSupport,
        PropertyUpdateProcessor, ComponentSynchronizePeer {

    /**
     * A boolean property which may be assigned to <code>SpWindowPane</code>s
     * in order to enable the proprietary Internet Explorer transparent PNG
     * alpha renderer for rendering the <code>border</code> property of the
     * window pane.
     */
    public static final String PROPERTY_IE_ALPHA_RENDER_BORDER
            = "org.karora.cooee.sandbox.consultas.webcontainer.SpWindowPanePeer.ieAlphaRenderBorder";
    
    private static final String IMAGE_ID_TITLE_BACKGROUND = "titleBackground";
    private static final String IMAGE_ID_CLOSE_ICON = "close";
    private static final String IMAGE_ID_MAXIMIZE_ICON = "maximize";
    private static final String IMAGE_ID_MINIMIZE_ICON = "minimize";
    private static final String IMAGE_ID_ICON = "icon";
    private static final String IMAGE_ID_BACKGROUND = "background";
    private static final String IMAGE_ID_BORDER_TOP_LEFT = "borderTopLeft";
    private static final String IMAGE_ID_BORDER_TOP = "borderTop";
    private static final String IMAGE_ID_BORDER_TOP_RIGHT = "borderTopRight";
    private static final String IMAGE_ID_BORDER_LEFT = "borderLeft";
    private static final String IMAGE_ID_BORDER_RIGHT = "borderRight";
    private static final String IMAGE_ID_BORDER_BOTTOM_LEFT = "borderBottomLeft";
    private static final String IMAGE_ID_BORDER_BOTTOM = "borderBottom";
    private static final String IMAGE_ID_BORDER_BOTTOM_RIGHT = "borderBottomRight";

    private static final ImageReference DEFAULT_CLOSE_ICON = new ResourceImageReference(
            "/org/karora/cooee/sandbox/consultas/webcontainer/resource/image/DefaultCloseButton.gif");
    private static final ImageReference DEFAULT_MAXIMIZE_ICON = new ResourceImageReference(
    		"/org/karora/cooee/sandbox/consultas/webcontainer/resource/image/DefaultMaximizeButton.gif");
    private static final ImageReference DEFAULT_MINIMIZE_ICON = new ResourceImageReference(
    		"/org/karora/cooee/sandbox/consultas/webcontainer/resource/image/DefaultMinimizeButton.gif");
    
    private static final String[] FILL_IMAGE_NAMES = {"tl", "t", "tr", "l", "r", "bl", "b", "br"};
    private static final String[] FILL_IMAGE_IDS = {
            IMAGE_ID_BORDER_TOP_LEFT, IMAGE_ID_BORDER_TOP, IMAGE_ID_BORDER_TOP_RIGHT, IMAGE_ID_BORDER_LEFT,
            IMAGE_ID_BORDER_RIGHT, IMAGE_ID_BORDER_BOTTOM_LEFT, IMAGE_ID_BORDER_BOTTOM, IMAGE_ID_BORDER_BOTTOM_RIGHT };
    
    /**
     * Service to provide supporting JavaScript library.
     */
    public static final Service SPWINDOW_PANE_SERVICE = JavaScriptService.forResource("Echo.SpWindowPane",
            "/org/karora/cooee/sandbox/consultas/webcontainer/resource/js/SpWindowPane.js");

    static {
        WebRenderServlet.getServiceRegistry().add(SPWINDOW_PANE_SERVICE);
    }

    private static void renderPixelProperty(SpWindowPane windowPane, String propertyName, Element element, String attributeName) {
        String pixelValue;
        Extent extent = (Extent) windowPane.getRenderProperty(propertyName);
        if (extent != null) {
            pixelValue = ExtentRender.renderCssAttributePixelValue(extent);
            if (pixelValue != null) {
                element.setAttribute(attributeName, pixelValue);
            }
        }
    }
    
    private PartialUpdateManager partialUpdateManager;

    /**
     * Default constructor.
     */
    public SpWindowPanePeer() {
        super();
        partialUpdateManager = new PartialUpdateManager();    }
    
    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#getContainerId(nextapp.echo2.app.Component)
     */
    public String getContainerId(Component child) {
        return ContainerInstance.getElementId(child.getParent()) + "_content";
    }
    
    /**
     * @see nextapp.echo2.webcontainer.image.ImageRenderSupport#getImage(nextapp.echo2.app.Component,
     *      java.lang.String)
     */
    public ImageReference getImage(Component component, String imageId) {
        if (IMAGE_ID_TITLE_BACKGROUND.equals(imageId)) {
            FillImage backgroundImage = (FillImage) component.getRenderProperty(SpWindowPane.PROPERTY_TITLE_BACKGROUND_IMAGE);
            return backgroundImage == null ? null : backgroundImage.getImage();
        } else if (IMAGE_ID_BACKGROUND.equals(imageId)) {
            FillImage backgroundImage = (FillImage) component.getRenderProperty(SpWindowPane.PROPERTY_BACKGROUND_IMAGE);
            return backgroundImage == null ? null : backgroundImage.getImage();
        } else if (IMAGE_ID_ICON.equals(imageId)) {
            return (ImageReference) component.getRenderProperty(SpWindowPane.PROPERTY_ICON);
        } else if (IMAGE_ID_CLOSE_ICON.equals(imageId)) {
            return (ImageReference) component.getRenderProperty(SpWindowPane.PROPERTY_CLOSE_ICON, DEFAULT_CLOSE_ICON);
        } else if (IMAGE_ID_MAXIMIZE_ICON.equals(imageId)) {
            return (ImageReference) component.getRenderProperty(SpWindowPane.PROPERTY_MAXIMIZE_ICON, DEFAULT_MAXIMIZE_ICON);
        } else if (IMAGE_ID_MINIMIZE_ICON.equals(imageId)) {
            return (ImageReference) component.getRenderProperty(SpWindowPane.PROPERTY_MINIMIZE_ICON, DEFAULT_MINIMIZE_ICON);
        } else if (IMAGE_ID_BORDER_TOP_LEFT.equals(imageId)) {
            FillImageBorder fillImageBorder = ((FillImageBorder) component.getRenderProperty(SpWindowPane.PROPERTY_BORDER));
            FillImage fillImage = fillImageBorder == null ? null : fillImageBorder.getFillImage(FillImageBorder.TOP_LEFT);
            return fillImage == null ? null : fillImage.getImage();
        } else if (IMAGE_ID_BORDER_TOP.equals(imageId)) {
            FillImageBorder fillImageBorder = ((FillImageBorder) component.getRenderProperty(SpWindowPane.PROPERTY_BORDER));
            FillImage fillImage = fillImageBorder == null ? null : fillImageBorder.getFillImage(FillImageBorder.TOP);
            return fillImage == null ? null : fillImage.getImage();
        } else if (IMAGE_ID_BORDER_TOP_RIGHT.equals(imageId)) {
            FillImageBorder fillImageBorder = ((FillImageBorder) component.getRenderProperty(SpWindowPane.PROPERTY_BORDER));
            FillImage fillImage = fillImageBorder == null ? null : fillImageBorder.getFillImage(FillImageBorder.TOP_RIGHT);
            return fillImage == null ? null : fillImage.getImage();
        } else if (IMAGE_ID_BORDER_LEFT.equals(imageId)) {
            FillImageBorder fillImageBorder = ((FillImageBorder) component.getRenderProperty(SpWindowPane.PROPERTY_BORDER));
            FillImage fillImage = fillImageBorder == null ? null : fillImageBorder.getFillImage(FillImageBorder.LEFT);
            return fillImage == null ? null : fillImage.getImage();
        } else if (IMAGE_ID_BORDER_RIGHT.equals(imageId)) {
            FillImageBorder fillImageBorder = ((FillImageBorder) component.getRenderProperty(SpWindowPane.PROPERTY_BORDER));
            FillImage fillImage = fillImageBorder == null ? null : fillImageBorder.getFillImage(FillImageBorder.RIGHT);
            return fillImage == null ? null : fillImage.getImage();
        } else if (IMAGE_ID_BORDER_BOTTOM_LEFT.equals(imageId)) {
            FillImageBorder fillImageBorder = ((FillImageBorder) component.getRenderProperty(SpWindowPane.PROPERTY_BORDER));
            FillImage fillImage = fillImageBorder == null ? null : fillImageBorder.getFillImage(FillImageBorder.BOTTOM_LEFT);
            return fillImage == null ? null : fillImage.getImage();
        } else if (IMAGE_ID_BORDER_BOTTOM.equals(imageId)) {
            FillImageBorder fillImageBorder = ((FillImageBorder) component.getRenderProperty(SpWindowPane.PROPERTY_BORDER));
            FillImage fillImage = fillImageBorder == null ? null : fillImageBorder.getFillImage(FillImageBorder.BOTTOM);
            return fillImage == null ? null : fillImage.getImage();
        } else if (IMAGE_ID_BORDER_BOTTOM_RIGHT.equals(imageId)) {
            FillImageBorder fillImageBorder = ((FillImageBorder) component.getRenderProperty(SpWindowPane.PROPERTY_BORDER));
            FillImage fillImage = fillImageBorder == null ? null : fillImageBorder.getFillImage(FillImageBorder.BOTTOM_RIGHT);
            return fillImage == null ? null : fillImage.getImage();
        } else {
            return null;
        }
    }

    /**
     * @see nextapp.echo2.webcontainer.ActionProcessor#processAction(nextapp.echo2.webcontainer.ContainerInstance,
     *      nextapp.echo2.app.Component, org.w3c.dom.Element)
     */
    public void processAction(ContainerInstance ci, Component component, Element actionElement) {
    	SpWindowPane windowPane = (SpWindowPane) component;
        boolean closable = ((Boolean) windowPane.getRenderProperty(SpWindowPane.PROPERTY_CLOSABLE, Boolean.TRUE)).booleanValue();
        if (closable) {
            ci.getUpdateManager().getClientUpdateManager().setComponentAction(component, SpWindowPane.INPUT_CLOSE, null);
        }
    }

    /**
     * @see nextapp.echo2.webcontainer.PropertyUpdateProcessor#processPropertyUpdate(
     *      nextapp.echo2.webcontainer.ContainerInstance,
     *      nextapp.echo2.app.Component, org.w3c.dom.Element)
     */
    public void processPropertyUpdate(ContainerInstance ci, Component component, Element propertyElement) {
        String propertyName = propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_NAME);
        SpWindowPane windowPane = (SpWindowPane) component;
        boolean movable = ((Boolean) windowPane.getRenderProperty(SpWindowPane.PROPERTY_MOVABLE, Boolean.TRUE)).booleanValue();
        boolean resizable = ((Boolean) windowPane.getRenderProperty(SpWindowPane.PROPERTY_RESIZABLE, Boolean.TRUE)).booleanValue();
        if (SpWindowPane.PROPERTY_POSITION_X.equals(propertyName)) {
            if (movable) {
                ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, SpWindowPane.PROPERTY_POSITION_X,
                        ExtentRender.toExtent(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
            }
        } else if (SpWindowPane.PROPERTY_POSITION_Y.equals(propertyName)) {
            if (movable) {
                ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, SpWindowPane.PROPERTY_POSITION_Y,
                        ExtentRender.toExtent(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
            }
        } else if (SpWindowPane.PROPERTY_WIDTH.equals(propertyName)) {
            if (resizable) {
                ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, SpWindowPane.PROPERTY_WIDTH,
                        ExtentRender.toExtent(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
            }
        } else if (SpWindowPane.PROPERTY_HEIGHT.equals(propertyName)) {
            if (resizable) {
                ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, SpWindowPane.PROPERTY_HEIGHT,
                        ExtentRender.toExtent(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
            }
        } else if (SpWindowPane.Z_INDEX_CHANGED_PROPERTY.equals(propertyName)) {
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, SpWindowPane.Z_INDEX_CHANGED_PROPERTY,
                    new Integer(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
        }
    }


    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderAdd(nextapp.echo2.webcontainer.RenderContext,
     *      nextapp.echo2.app.update.ServerComponentUpdate, java.lang.String,
     *      nextapp.echo2.app.Component)
     */
    public void renderAdd(RenderContext rc, ServerComponentUpdate update,
            String targetId, Component component) {
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(SPWINDOW_PANE_SERVICE.getId());
        SpWindowPane windowPane = (SpWindowPane) component;
        renderInitDirective(rc, windowPane, targetId);
        Component[] children = windowPane.getVisibleComponents();
        if (children.length != 0) {
            ComponentSynchronizePeer syncPeer = SynchronizePeerFactory.getPeerForComponent(children[0].getClass());
            syncPeer.renderAdd(rc, update, getContainerId(children[0]), children[0]);
        }
    }

    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderDispose(nextapp.echo2.webcontainer.RenderContext,
     *      nextapp.echo2.app.update.ServerComponentUpdate,
     *      nextapp.echo2.app.Component)
     */
    public void renderDispose(RenderContext rc, ServerComponentUpdate update,
            Component component) {
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(SPWINDOW_PANE_SERVICE.getId());
        renderDisposeDirective(rc, (SpWindowPane) component);
    }

    /**
     * Renders a directive to the outgoing <code>ServerMessage</code> to 
     * dispose the state of a <code>SpWindowPane</code>, performing tasks such as
     * unregistering event listeners on the client.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param windowPane the <code>SpWindowPane</code>
     */
    private void renderDisposeDirective(RenderContext rc, SpWindowPane windowPane) {
        String elementId = ContainerInstance.getElementId(windowPane);
        ServerMessage serverMessage = rc.getServerMessage();
        Element initElement = serverMessage.appendPartDirective(ServerMessage.GROUP_ID_PREREMOVE, 
                "EchoSpWindowPane.MessageProcessor", "dispose");
        initElement.setAttribute("eid", elementId);
    }

    /**
     * Renders a directive to the outgoing <code>ServerMessage</code> to 
     * render and intialize the state of a <code>SpWindowPane</code>.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param windowPane the <code>SpWindowPane</code>
     * @param targetId the id of the container element
     */
    private void renderInitDirective(RenderContext rc, SpWindowPane windowPane, String targetId) {
        String elementId = ContainerInstance.getElementId(windowPane);
        ServerMessage serverMessage = rc.getServerMessage();
        Element partElement = serverMessage.addPart(ServerMessage.GROUP_ID_UPDATE, "EchoSpWindowPane.MessageProcessor");
        Element initElement = serverMessage.getDocument().createElement("init");
        initElement.setAttribute("container-eid", targetId);
        initElement.setAttribute("eid", elementId);

        if (!windowPane.isRenderEnabled()) {
            initElement.setAttribute("enabled", "false");
        }
        
        // Content Appearance
        Insets insets = (Insets) windowPane.getRenderProperty(SpWindowPane.PROPERTY_INSETS);
        if (insets != null) {
            initElement.setAttribute("insets", InsetsRender.renderCssAttributeValue(insets));
        }
        Color background = (Color) windowPane.getRenderProperty(SpWindowPane.PROPERTY_BACKGROUND);
        if (background != null) {
            initElement.setAttribute("background", ColorRender.renderCssAttributeValue(background));
        }
        FillImage backgroundImage = (FillImage) windowPane.getRenderProperty(SpWindowPane.PROPERTY_BACKGROUND_IMAGE);
        if (backgroundImage != null) {
            CssStyle backgroundImageCssStyle = new CssStyle();
            FillImageRender.renderToStyle(backgroundImageCssStyle, rc, this, windowPane, IMAGE_ID_BACKGROUND, 
                    backgroundImage, 0);
            initElement.setAttribute("background-image", backgroundImageCssStyle.renderInline());
        }
        Color foreground = (Color) windowPane.getRenderProperty(SpWindowPane.PROPERTY_FOREGROUND);
        if (foreground != null) {
            initElement.setAttribute("foreground", ColorRender.renderCssAttributeValue(foreground));
        }
        Font font = (Font) windowPane.getRenderProperty(SpWindowPane.PROPERTY_FONT);
        if (font != null) {
            CssStyle fontCssStyle = new CssStyle();
            FontRender.renderToStyle(fontCssStyle, font);
            initElement.setAttribute("font", fontCssStyle.renderInline());
        }

        // Positioning
        renderPixelProperty(windowPane, SpWindowPane.PROPERTY_POSITION_X, initElement, "position-x");
        renderPixelProperty(windowPane, SpWindowPane.PROPERTY_POSITION_Y, initElement, "position-y");
        renderPixelProperty(windowPane, SpWindowPane.PROPERTY_WIDTH, initElement, "width");
        renderPixelProperty(windowPane, SpWindowPane.PROPERTY_HEIGHT, initElement, "height");
        renderPixelProperty(windowPane, SpWindowPane.PROPERTY_MINIMUM_WIDTH, initElement, "minimum-width");
        renderPixelProperty(windowPane, SpWindowPane.PROPERTY_MINIMUM_HEIGHT, initElement, "minimum-height");
        renderPixelProperty(windowPane, SpWindowPane.PROPERTY_MAXIMUM_WIDTH, initElement, "maximum-width");
        renderPixelProperty(windowPane, SpWindowPane.PROPERTY_MAXIMUM_HEIGHT, initElement, "maximum-height");
        
        // Title-related
        if (windowPane.getRenderProperty(SpWindowPane.PROPERTY_ICON) != null) {
            initElement.setAttribute("icon", ImageTools.getUri(rc, this, windowPane, IMAGE_ID_ICON));
            Insets iconInsets = (Insets) windowPane.getRenderProperty(SpWindowPane.PROPERTY_ICON_INSETS);
            if (iconInsets != null) {
                initElement.setAttribute("icon-insets", InsetsRender.renderCssAttributeValue(iconInsets));
            }
        }
        String title = (String) windowPane.getRenderProperty(SpWindowPane.PROPERTY_TITLE);
        if (title != null) {
            initElement.setAttribute("title", title);
            Insets titleInsets = (Insets) windowPane.getRenderProperty(SpWindowPane.PROPERTY_TITLE_INSETS);
            Color titleForeground = (Color) windowPane.getRenderProperty(SpWindowPane.PROPERTY_TITLE_FOREGROUND);
            if (titleForeground != null) {
                initElement.setAttribute("title-foreground", ColorRender.renderCssAttributeValue(titleForeground));
            }
            if (titleInsets != null) {
                initElement.setAttribute("title-insets", InsetsRender.renderCssAttributeValue(titleInsets));
            }
            Font titleFont = (Font) windowPane.getRenderProperty(SpWindowPane.PROPERTY_TITLE_FONT);
            if (titleFont != null) {
                CssStyle fontCssStyle = new CssStyle();
                FontRender.renderToStyle(fontCssStyle, titleFont);
                initElement.setAttribute("title-font", fontCssStyle.renderInline());
            }
        }
        renderPixelProperty(windowPane, SpWindowPane.PROPERTY_TITLE_HEIGHT, initElement, "title-height");
        Color titleBackground = (Color) windowPane.getRenderProperty(SpWindowPane.PROPERTY_TITLE_BACKGROUND);
        if (titleBackground != null) {
            initElement.setAttribute("title-background", ColorRender.renderCssAttributeValue(titleBackground));
        }
        FillImage titleBackgroundImage = (FillImage) windowPane.getRenderProperty(SpWindowPane.PROPERTY_TITLE_BACKGROUND_IMAGE);
        if (titleBackgroundImage != null) {
            CssStyle titleBackgroundImageCssStyle = new CssStyle();
            FillImageRender.renderToStyle(titleBackgroundImageCssStyle, rc, this, windowPane, IMAGE_ID_TITLE_BACKGROUND, 
                    titleBackgroundImage, 0);
            initElement.setAttribute("title-background-image", titleBackgroundImageCssStyle.renderInline());
        }
        
        // Move/Close/Resize
        Boolean resizableBoolean = (Boolean) windowPane.getRenderProperty(SpWindowPane.PROPERTY_RESIZABLE);
        boolean resizable = resizableBoolean == null ? true : resizableBoolean.booleanValue();
        initElement.setAttribute("resizable", resizable ? "true" : "false");
        Boolean closableBoolean = (Boolean) windowPane.getRenderProperty(SpWindowPane.PROPERTY_CLOSABLE);
        boolean closable = closableBoolean == null ? true : closableBoolean.booleanValue();
        initElement.setAttribute("closable", closable ? "true" : "false");
        if (closable) {
            if (getImage(windowPane, IMAGE_ID_CLOSE_ICON) != null) {
                initElement.setAttribute("close-icon", ImageTools.getUri(rc, this, windowPane, IMAGE_ID_CLOSE_ICON));
                Insets closeIconInsets = (Insets) windowPane.getRenderProperty(SpWindowPane.PROPERTY_CLOSE_ICON_INSETS);
                if (closeIconInsets != null) {
                    initElement.setAttribute("close-icon-insets", InsetsRender.renderCssAttributeValue(closeIconInsets));
                }
            }
        }
        Boolean maximizableBoolean = (Boolean) windowPane.getRenderProperty(SpWindowPane.PROPERTY_MAXIMIZABLE);
        boolean maximizable = maximizableBoolean == null ? true : maximizableBoolean.booleanValue();
        initElement.setAttribute("maximizable", maximizable ? "true" : "false");
        if (maximizable) {
            if (getImage(windowPane, IMAGE_ID_MAXIMIZE_ICON) != null) {
                initElement.setAttribute("maximize-icon", ImageTools.getUri(rc, this, windowPane, IMAGE_ID_MAXIMIZE_ICON));
                Insets maximizeIconInsets = (Insets) windowPane.getRenderProperty(SpWindowPane.PROPERTY_MAXIMIZE_ICON_INSETS);
                if (maximizeIconInsets != null) {
                    initElement.setAttribute("maximize-icon-insets", InsetsRender.renderCssAttributeValue(maximizeIconInsets));
                }
            }
        }
        Boolean minimizableBoolean = (Boolean) windowPane.getRenderProperty(SpWindowPane.PROPERTY_MINIMIZABLE);
        boolean minimizable = minimizableBoolean == null ? true : minimizableBoolean.booleanValue();
        initElement.setAttribute("minimizable", minimizable ? "true" : "false");
        if (minimizable) {
            if (getImage(windowPane, IMAGE_ID_MINIMIZE_ICON) != null) {
                initElement.setAttribute("minimize-icon", ImageTools.getUri(rc, this, windowPane, IMAGE_ID_MINIMIZE_ICON));
                Insets minimizeIconInsets = (Insets) windowPane.getRenderProperty(SpWindowPane.PROPERTY_MINIMIZE_ICON_INSETS);
                if (minimizeIconInsets != null) {
                    initElement.setAttribute("minimize-icon-insets", InsetsRender.renderCssAttributeValue(minimizeIconInsets));
                }
            }
        }
        Boolean minimizedBoolean = (Boolean) windowPane.getRenderProperty(SpWindowPane.PROPERTY_MINIMIZED);
        boolean minimized = minimizedBoolean == null ? false : minimizedBoolean.booleanValue();
        initElement.setAttribute("minimized", minimized ? "true" : "false");
        
        Boolean maximizedBoolean = (Boolean) windowPane.getRenderProperty(SpWindowPane.PROPERTY_MAXIMIZED);
        boolean maximized = maximizedBoolean == null ? false : maximizedBoolean.booleanValue();
        initElement.setAttribute("maximized", maximized ? "true" : "false");
        
        Boolean movableBoolean = (Boolean) windowPane.getRenderProperty(SpWindowPane.PROPERTY_MOVABLE);
        boolean movable = movableBoolean == null ? true : movableBoolean.booleanValue();
        initElement.setAttribute("movable", movable ? "true" : "false");

        // Border
        FillImageBorder border = (FillImageBorder) windowPane.getRenderProperty(SpWindowPane.PROPERTY_BORDER);
        if (border != null && border.getBorderInsets() != null && border.getContentInsets() != null) {
            Element borderElement = serverMessage.getDocument().createElement("border");
            if (border.getColor() != null) {
                borderElement.setAttribute("color", ColorRender.renderCssAttributeValue(border.getColor()));
            }
            borderElement.setAttribute("border-insets", InsetsRender.renderCssAttributeValue(border.getBorderInsets()));
            borderElement.setAttribute("content-insets", InsetsRender.renderCssAttributeValue(border.getContentInsets()));
            int fillImageRenderFlags = ((Boolean) windowPane.getRenderProperty(PROPERTY_IE_ALPHA_RENDER_BORDER, 
                    Boolean.FALSE)).booleanValue() ? FillImageRender.FLAG_ENABLE_IE_PNG_ALPHA_FILTER : 0;
            for (int i = 0; i < 8; ++i) {
                FillImage fillImage = border.getFillImage(i);
                if (fillImage != null) {
                    Element imageElement = serverMessage.getDocument().createElement("image");
                    imageElement.setAttribute("name", FILL_IMAGE_NAMES[i]);
                    CssStyle fillImageCssStyle = new CssStyle();
                    FillImageRender.renderToStyle(fillImageCssStyle, rc, this, windowPane, FILL_IMAGE_IDS[i], fillImage, 
                            fillImageRenderFlags);
                    imageElement.setAttribute("value", fillImageCssStyle.renderInline());
                    borderElement.appendChild(imageElement);
                }
            }
            initElement.appendChild(borderElement);
        }
        
        partElement.appendChild(initElement);
    }
    
    private void renderSetContent(RenderContext rc, ServerComponentUpdate update) {
        //TODO. implement
    }
    
    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderUpdate(nextapp.echo2.webcontainer.RenderContext,
     *      nextapp.echo2.app.update.ServerComponentUpdate, java.lang.String)
     */
    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update,
            String targetId) {
        boolean fullReplace = false;
        if (update.hasUpdatedLayoutDataChildren()) {
            fullReplace = true;
        } else if (update.hasUpdatedProperties()) {
            if (!partialUpdateManager.canProcess(rc, update)) {
                fullReplace = true;
            }
        }

        //TODO. Temp fix. 
        fullReplace = true;
        
        if (fullReplace) {
            // Perform full update.
            renderDisposeDirective(rc, (SpWindowPane) update.getParent());
            DomUpdate.renderElementRemove(rc.getServerMessage(), ContainerInstance.getElementId(update.getParent()));
            renderAdd(rc, update, targetId, update.getParent());
        } else {
            partialUpdateManager.process(rc, update);
            if (update.hasAddedChildren() || update.hasRemovedChildren()) {
                renderSetContent(rc, update);
            }
        }
        
        return fullReplace;
    }
}
