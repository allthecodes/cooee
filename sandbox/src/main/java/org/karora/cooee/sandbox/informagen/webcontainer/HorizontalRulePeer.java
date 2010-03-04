package org.karora.cooee.sandbox.informagen.webcontainer;

// This peer's Cooee component
import org.karora.cooee.sandbox.informagen.app.HorizontalRule;

import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Insets;

import org.karora.cooee.app.update.ServerComponentUpdate;

import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.DomUpdateSupport;
import org.karora.cooee.webcontainer.RenderContext;

// CSS property renderers
import org.karora.cooee.webcontainer.propertyrender.BorderRender;
import org.karora.cooee.webcontainer.propertyrender.ColorRender;
import org.karora.cooee.webcontainer.propertyrender.ExtentRender;
import org.karora.cooee.webcontainer.propertyrender.InsetsRender;

import org.karora.cooee.webrender.output.CssStyle;
import org.karora.cooee.webrender.servermessage.DomUpdate;

// W3C
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <code>HorizontalRulePeer</code>
 */

public class HorizontalRulePeer implements ComponentSynchronizePeer, DomUpdateSupport {


    /**
    * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#getContainerId(org.karora.cooee.app.Component)
    */
    
    public String getContainerId(Component child) {
        throw new IllegalStateException("HorizontalRulePeer does not work as a DOM container");
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
        // we dont have anything to dispose of per see
    }
    
    
    /**
    * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderUpdate(org.karora.cooee.webcontainer.RenderContext,
    *      org.karora.cooee.app.update.ServerComponentUpdate, java.lang.String)
    */
    
    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
    
        DomUpdate.renderElementRemove(rc.getServerMessage(), ContainerInstance.getElementId(update.getParent()));
        renderAdd(rc, update, targetId, update.getParent());
        
        return false;
    }


    /**
    * @see org.karora.cooee.webcontainer.DomUpdateSupport#renderHtml(org.karora.cooee.webcontainer.RenderContext,
    *      org.karora.cooee.app.update.ServerComponentUpdate, org.w3c.dom.Node,
    *      org.karora.cooee.app.Component)
    */
    
    public void renderHtml(RenderContext rc, ServerComponentUpdate update, Node parentNode, Component component) {
    
        Document doc = rc.getServerMessage().getDocument();
        Element element = doc.createElement("hr");
        element.setAttribute("id",ContainerInstance.getElementId(component));
        
        CssStyle cssStyle = createStyle(component);
        
        if (cssStyle.hasAttributes()) 
            element.setAttribute("style", cssStyle.renderInline());
        
        parentNode.appendChild(element);
    }
        
        
    private CssStyle createStyle(Component component) {
    
        CssStyle cssStyle = new CssStyle();
        
        // 'width' & 'height' ==============================================================
        
        Extent width = (Extent) component.getRenderProperty(HorizontalRule.PROPERTY_WIDTH);
        Extent height = (Extent) component.getRenderProperty(HorizontalRule.PROPERTY_HEIGHT);

        if (width != null) 
            cssStyle.setAttribute("width", ExtentRender.renderCssAttributeValue(width));
        
        if (height != null) 
            cssStyle.setAttribute("height", ExtentRender.renderCssAttributeValue(height));


        // 'background-color' ==============================================================
        
        Color color = (Color) component.getRenderProperty(Component.PROPERTY_BACKGROUND);

        if(color == null)
            color = (Color) component.getRenderProperty(Component.PROPERTY_FOREGROUND);

        if( component.isRenderEnabled() == false)
            color = (Color) component.getRenderProperty(HorizontalRule.PROPERTY_DISABLED_BACKGROUND);

        if(color != null)        
            ColorRender.renderToStyle(cssStyle, color, color);

        // 'border' ========================================================================
        
        Border border = (Border)component.getRenderProperty(HorizontalRule.PROPERTY_BORDER);

        if( component.isRenderEnabled() == false)
            border = (Border) component.getRenderProperty(HorizontalRule.PROPERTY_DISABLED_BORDER);

        if(border != null)
            BorderRender.renderToStyle(cssStyle, border);
        
        return cssStyle;
    }
    
        
}