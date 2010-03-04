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
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Font;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.LayoutData;
import org.karora.cooee.app.layout.ColumnLayoutData;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.sandbox.consultas.app.RoundedColumn;
import org.karora.cooee.sandbox.consultas.app.RoundedPanel;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.DomUpdateSupport;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webcontainer.RenderState;
import org.karora.cooee.webcontainer.SynchronizePeerFactory;
import org.karora.cooee.webcontainer.propertyrender.CellLayoutDataRender;
import org.karora.cooee.webcontainer.propertyrender.ColorRender;
import org.karora.cooee.webcontainer.propertyrender.ExtentRender;
import org.karora.cooee.webcontainer.propertyrender.FontRender;
import org.karora.cooee.webcontainer.propertyrender.InsetsRender;
import org.karora.cooee.webcontainer.syncpeer.ColumnPeer;
import org.karora.cooee.webrender.output.CssStyle;
import org.karora.cooee.webrender.servermessage.DomUpdate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class RoundedColumnPeer extends ColumnPeer {
	
		
    private static class RoundedColumnPeerRenderState 
    implements RenderState {
        
        /**
         * The child <code>Component</code> which had the highest index during 
         * the last rendering.  This information is necessary when rendering 
         * cell spacing, as the last component will not have a "spacing" row
         * beneath it.  Thus, if it is no longer the last component due to an
         * add, one will need to be added beneath it. 
         */
        public Component lastChild;
    }
    public void renderHtml(RenderContext rc, ServerComponentUpdate update, Node parentNode, Component component) {
        RoundedColumn column = (RoundedColumn) component;
        
        Document document = parentNode.getOwnerDocument();
        Element divElement = document.createElement("div");
        divElement.setAttribute("id", ContainerInstance.getElementId(column));
        
        if (column.isTopRoundedCorner())
        { 
            if (column.isBorderOn())
            {
                Element rtopElement= document.createElement("b");
                rtopElement.setAttribute("style", "display:block;background:transparent");
                
                if (column instanceof RoundedPanel && ((RoundedPanel)column).getTitle() != null && !((RoundedPanel)column).getTitle().equals(""))
                {
	                Element rspaceElement = document.createElement("b");
	                rspaceElement.setAttribute("style", "display: block; height: 6px; overflow: hidden; margin: 0 0px;background:transparent");
	                rtopElement.appendChild(rspaceElement);
                }
	              Element r1Element = document.createElement("b");
                r1Element.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 5px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(RoundedColumn.PROPERTY_BORDERCOLOR)));
                rtopElement.appendChild(r1Element);
                Element r2Element = document.createElement("b");
                r2Element.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 3px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(RoundedColumn.PROPERTY_BORDERCOLOR)));
                rtopElement.appendChild(r2Element);
                Element r2ElementBis = document.createElement("b");
                r2ElementBis.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 2px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                r2Element.appendChild(r2ElementBis);
                Element r3Element = document.createElement("b");
                r3Element.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 2px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(RoundedColumn.PROPERTY_BORDERCOLOR)));
                rtopElement.appendChild(r3Element);
                Element r3ElementBis = document.createElement("b");
                r3ElementBis.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 1px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                r3Element.appendChild(r3ElementBis);
                Element r4Element = document.createElement("b");
                r4Element.setAttribute("style", "display: block; height: 2px; overflow: hidden; margin: 0 1px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(RoundedColumn.PROPERTY_BORDERCOLOR)));
                rtopElement.appendChild(r4Element);
                Element r4ElementBis = document.createElement("b");
                r4ElementBis.setAttribute("style", "display: block; height: 2px; overflow: hidden; margin: 0 1px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                r4Element.appendChild(r4ElementBis);
                Element r5Element = document.createElement("b");
                r5Element.setAttribute("style", "display: block; height: 2px; overflow: hidden; margin: 0 0px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(RoundedColumn.PROPERTY_BORDERCOLOR)));
                rtopElement.appendChild(r5Element);
                Element r5ElementBis = document.createElement("b");
                r5ElementBis.setAttribute("style", "display: block; height: 2px; overflow: hidden; margin: 0 1px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                r5Element.appendChild(r5ElementBis);
                divElement.appendChild(rtopElement);
            }
            else
            {
                Element rtopElement= document.createElement("b");
                rtopElement.setAttribute("style", "display:block;background:transparent");
                Element r1Element = document.createElement("b");
                r1Element.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 5px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                rtopElement.appendChild(r1Element);
                Element r2Element = document.createElement("b");
                r2Element.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 3px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                rtopElement.appendChild(r2Element);
                Element r3Element = document.createElement("b");
                r3Element.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 2px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                rtopElement.appendChild(r3Element);
                Element r4Element = document.createElement("b");
                r4Element.setAttribute("style", "display: block; height: 2px; overflow: hidden; margin: 0 1px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                rtopElement.appendChild(r4Element);
                Element r5Element = document.createElement("b");
                r5Element.setAttribute("style", "display: block; height: 2px; overflow: hidden; margin: 0 0px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                rtopElement.appendChild(r5Element);
                divElement.appendChild(rtopElement);
            }
        }
        
        if (column instanceof RoundedPanel && ((RoundedPanel)column).getTitle() != null && !((RoundedPanel)column).getTitle().equals(""))
        {
	        Element titleElement = document.createElement("span");
	        String titleStyle = "";
	        if (((RoundedPanel)column).getTitleForeground() != null)
	        	titleStyle += "color:" + ColorRender.renderCssAttributeValue((Color) ((RoundedPanel)column).getTitleForeground()) + ";";
	        if (((RoundedPanel)column).getTitleFont() != null)
	       	{
	        	titleStyle += "font-style:" + FontRender.renderFontStyleCssAttributeValue((Font) ((RoundedPanel)column).getTitleFont())+ ";";
	        	titleStyle += "font-family:" + FontRender.renderFontFamilyCssAttributeValue(((Font) ((RoundedPanel)column).getTitleFont()).getTypeface())+ ";";
	        	titleStyle += "font-weight:" + FontRender.renderFontWeightCssAttributeValue((Font) ((RoundedPanel)column).getTitleFont())+ ";";
	        	
	        	if (((RoundedPanel)column).getTitleFont().getSize() != null) {
	        		titleStyle += "font-size:" + ExtentRender.renderCssAttributeValue(((RoundedPanel)column).getTitleFont().getSize()) + ";";
	        	}
	       	}
        
	        if (column.isBorderOn())
	        {	
	        	
	        	titleElement.setAttribute("style", "position:absolute;top:-3px;left:15px;background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)) + ";" +titleStyle);
	        }
	        else
	        {
	        	titleElement.setAttribute("style", "position:absolute;top:-3px;left:15px;background:transparent;"+ titleStyle);
	        }
	        titleElement.appendChild(document.createTextNode("\u00A0"+ ((RoundedPanel)column).getTitle() + "\u00A0"));
	        divElement.appendChild(titleElement);
        }
        Element contentElement = document.createElement("div");
        
        CssStyle divCssStyle = new CssStyle();
        ColorRender.renderToStyle(divCssStyle, (Color) column.getRenderProperty(Column.PROPERTY_FOREGROUND), 
                (Color) column.getRenderProperty(Column.PROPERTY_BACKGROUND));
        FontRender.renderToStyle(divCssStyle, (Font) column.getRenderProperty(Column.PROPERTY_FONT));
        Insets insets = (Insets) column.getRenderProperty(Column.PROPERTY_INSETS);
        if (insets == null) {
            divCssStyle.setAttribute("padding", "0px");
        } else {
            InsetsRender.renderToStyle(divCssStyle, "padding", insets);
        }
        
        if (column.isBorderOn())
        {
            divCssStyle.setAttribute("margin", "0 1px");
            contentElement.setAttribute("style", divCssStyle.renderInline());
            Element contentElementBis = document.createElement("div");
            contentElementBis.setAttribute("style", "overflow: hidden; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(RoundedColumn.PROPERTY_BORDERCOLOR)));
            contentElementBis.appendChild(contentElement);
            divElement.appendChild(contentElementBis);
        }
        else
        {
            contentElement.setAttribute("style", divCssStyle.renderInline());
            divElement.appendChild(contentElement);
        }
        
        parentNode.appendChild(divElement);
        
        
        
        Component[] children = column.getVisibleComponents();
        for (int i = 0; i < children.length; ++i) {
            renderChild(rc, update, contentElement, component, children[i]);
        }
        if (column.isBottomRoundedCorner())
        {
            if (column.isBorderOn())
            {
                Element rbotElement = document.createElement("b");
                rbotElement.setAttribute("style", "display:block;background:transparent");
                Element r5Element = document.createElement("b");
                r5Element.setAttribute("style", "display: block; height: 2px; overflow: hidden; margin: 0 0px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(RoundedColumn.PROPERTY_BORDERCOLOR)));
                Element r5ElementBis = document.createElement("b");
                r5ElementBis.setAttribute("style", "display: block; height: 2px; overflow: hidden; margin: 0 1px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                r5Element.appendChild(r5ElementBis);
                rbotElement.appendChild(r5Element);
                Element r4Element = document.createElement("b");
                r4Element.setAttribute("style", "display: block; height: 2px; overflow: hidden; margin: 0 1px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(RoundedColumn.PROPERTY_BORDERCOLOR)));
                Element r4ElementBis = document.createElement("b");
                r4ElementBis.setAttribute("style", "display: block; height: 2px; overflow: hidden; margin: 0 1px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                r4Element.appendChild(r4ElementBis);
                rbotElement.appendChild(r4Element);
                Element r3Element = document.createElement("b");
                r3Element.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 2px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(RoundedColumn.PROPERTY_BORDERCOLOR)));
                Element r3ElementBis = document.createElement("b");
                r3ElementBis.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 1px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                r3Element.appendChild(r3ElementBis);
                rbotElement.appendChild(r3Element);
                Element r2Element = document.createElement("b");
                r2Element.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 3px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(RoundedColumn.PROPERTY_BORDERCOLOR)));
                Element r2ElementBis = document.createElement("b");
                r2ElementBis.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 2px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                r2Element.appendChild(r2ElementBis);
                rbotElement.appendChild(r2Element);
                Element r1Element = document.createElement("b");
                r1Element.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 5px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(RoundedColumn.PROPERTY_BORDERCOLOR)));
                rbotElement.appendChild(r1Element);
                divElement.appendChild(rbotElement);
            }
            else
            {
                Element rbotElement = document.createElement("b");
                rbotElement.setAttribute("style", "display:block;background:transparent");
                Element r5Element = document.createElement("b");
                r5Element.setAttribute("style", "display: block; height: 2px; overflow: hidden; margin: 0 0px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                rbotElement.appendChild(r5Element);
                Element r4Element = document.createElement("b");
                r4Element.setAttribute("style", "display: block; height: 2px; overflow: hidden; margin: 0 1px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                rbotElement.appendChild(r4Element);
                Element r3Element = document.createElement("b");
                r3Element.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 2px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                rbotElement.appendChild(r3Element);
                Element r2Element = document.createElement("b");
                r2Element.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 3px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                rbotElement.appendChild(r2Element);
                Element r1Element = document.createElement("b");
                r1Element.setAttribute("style", "display: block; height: 1px; overflow: hidden; margin: 0 5px; background:" + ColorRender.renderCssAttributeValue((Color)column.getRenderProperty(Column.PROPERTY_BACKGROUND)));
                rbotElement.appendChild(r1Element);
                divElement.appendChild(rbotElement);
            }
        }
        storeRenderState(rc, column);
    }
    
    private void renderChild(RenderContext rc, ServerComponentUpdate update, Node parentNode, 
            Component component, Component child) {
        Document document = parentNode.getOwnerDocument();
        String childId = ContainerInstance.getElementId(child);
        Element divElement = document.createElement("div");
        String cellId = ContainerInstance.getElementId(component) + "_cell_" + childId;
        divElement.setAttribute("id", cellId);
        
        // Configure cell style.
        CssStyle cssStyle = new CssStyle();
        ColumnLayoutData layoutData = getLayoutData(child);
        CellLayoutDataRender.renderToElementAndStyle(divElement, cssStyle, child, layoutData, "0px");
        CellLayoutDataRender.renderBackgroundImageToStyle(cssStyle, rc, this, component, child);
        if (layoutData != null) {
            ExtentRender.renderToStyle(cssStyle, "height", layoutData.getHeight());
        }
        divElement.setAttribute("style", cssStyle.renderInline());
        
        parentNode.appendChild(divElement);
        
        renderSpacingCell(parentNode, (RoundedColumn) component, child);
        
        renderAddChild(rc, update, divElement, child);
    }
    
    private ColumnLayoutData getLayoutData(Component child) {
        LayoutData layoutData = (LayoutData) child.getRenderProperty(Component.PROPERTY_LAYOUT_DATA);
        if (layoutData == null) {
            return null;
        } else if (layoutData instanceof ColumnLayoutData) {
            return (ColumnLayoutData) layoutData;
        } else {
            throw new RuntimeException("Invalid LayoutData for Column Child: " + layoutData.getClass().getName());
        }
    }

    private void renderSpacingCell(Node parentNode, RoundedColumn column, Component child) {
        Extent cellSpacing = (Extent) column.getRenderProperty(Column.PROPERTY_CELL_SPACING);
        if (!ExtentRender.isZeroLength(cellSpacing) && column.visibleIndexOf(child) != column.getVisibleComponentCount() - 1) {
            Element spacingElement = parentNode.getOwnerDocument().createElement("div");
            spacingElement.setAttribute("id", ContainerInstance.getElementId(column) + "_spacing_" 
                    + ContainerInstance.getElementId(child));
            CssStyle spacingCssStyle = new CssStyle();
            spacingCssStyle.setAttribute("height", ExtentRender.renderCssAttributeValue(cellSpacing));
            spacingCssStyle.setAttribute("font-size", "1px");
            spacingCssStyle.setAttribute("line-height", "0px");
            spacingElement.setAttribute("style", spacingCssStyle.renderInline());
            parentNode.appendChild(spacingElement);
        }
    }
    
    private void renderAddChild(RenderContext rc, ServerComponentUpdate update, Element parentElement, Component child) {
        ComponentSynchronizePeer syncPeer = SynchronizePeerFactory.getPeerForComponent(child.getClass());
        if (syncPeer instanceof DomUpdateSupport) {
            ((DomUpdateSupport) syncPeer).renderHtml(rc, update, parentElement, child);
        } else {
            syncPeer.renderAdd(rc, update, getContainerId(child), child);
        }
    }
    
    private void storeRenderState(RenderContext rc, Component component) {
        int componentCount = component.getVisibleComponentCount();
        RoundedColumnPeerRenderState renderState = new RoundedColumnPeerRenderState();
        if (componentCount > 0) {
            renderState.lastChild = component.getVisibleComponent(componentCount - 1);
        }
        rc.getContainerInstance().setRenderState(component, renderState);
    }
    
    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
      DomUpdate.renderElementRemove(rc.getServerMessage(), ContainerInstance.getElementId(update.getParent()));
      renderAdd(rc, update, targetId, update.getParent());
    
      storeRenderState(rc, update.getParent());
      return true;
  }
}
