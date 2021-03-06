/* 
 * This file is part of the Echo Web Application Framework (hereinafter "Echo").
 * Copyright (C) 2002-2005 NextApp, Inc.
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

package org.karora.cooee.webcontainer.syncpeer;

import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.FillImage;
import org.karora.cooee.app.Font;
import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.LayoutData;
import org.karora.cooee.app.Table;
import org.karora.cooee.app.layout.TableLayoutData;
import org.karora.cooee.app.list.ListSelectionModel;
import org.karora.cooee.app.table.TableColumnModel;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.webcontainer.ActionProcessor;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.DomUpdateSupport;
import org.karora.cooee.webcontainer.PartialUpdateManager;
import org.karora.cooee.webcontainer.PropertyUpdateProcessor;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webcontainer.SynchronizePeerFactory;
import org.karora.cooee.webcontainer.image.ImageRenderSupport;
import org.karora.cooee.webcontainer.propertyrender.BorderRender;
import org.karora.cooee.webcontainer.propertyrender.CellLayoutDataRender;
import org.karora.cooee.webcontainer.propertyrender.ColorRender;
import org.karora.cooee.webcontainer.propertyrender.ExtentRender;
import org.karora.cooee.webcontainer.propertyrender.FillImageRender;
import org.karora.cooee.webcontainer.propertyrender.FontRender;
import org.karora.cooee.webcontainer.propertyrender.InsetsRender;
import org.karora.cooee.webrender.ClientProperties;
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.output.CssStyle;
import org.karora.cooee.webrender.servermessage.DomUpdate;
import org.karora.cooee.webrender.service.JavaScriptService;
import org.karora.cooee.webrender.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 * Synchronization peer for <code>org.karora.cooee.app.Table</code> components.
 * <p>
 * This class should not be extended or used by classes outside of the
 * Echo framework.
 */
public class TablePeer 
implements ActionProcessor, ComponentSynchronizePeer, ImageRenderSupport, PropertyUpdateProcessor  {

    //TODO: Add full support for partial rendering on row insertions/deletions.

    /**
     * A string of periods used for the IE 100% Table Width workaround.
     */
    private static final String SIZING_DOTS = ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . "
            + ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ";
    
    private static final String[] TABLE_INIT_KEYS = new String[]{"rollover-style", "selection-style"};
    
    private static final String PROPERTY_SELECTION = "selection";
    
    private static final String IMAGE_ID_ROLLOVER_BACKGROUND = "rolloverBackground";
    private static final String IMAGE_ID_SELECTION_BACKGROUND = "selectionBackground";
    
    /**
     * Service to provide supporting JavaScript library.
     */
    private static final Service TABLE_SERVICE = JavaScriptService.forResource("Echo.Table", 
            "/org/karora/cooee/webcontainer/resource/js/Table.js");

    static {
        WebRenderServlet.getServiceRegistry().add(TABLE_SERVICE);
    }

    protected PartialUpdateManager propertyRenderRegistry;
    
    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#getContainerId(org.karora.cooee.app.Component)
     */
    public String getContainerId(Component child) {
        return ContainerInstance.getElementId(child.getParent()) + "_cell_" + child.getRenderId();
    }
    
    /**
     * @see org.karora.cooee.webcontainer.image.ImageRenderSupport#getImage(org.karora.cooee.app.Component, java.lang.String)
     */
    public ImageReference getImage(Component component, String imageId) {
        if (IMAGE_ID_ROLLOVER_BACKGROUND.equals(imageId)) {
            FillImage backgroundImage 
                    = (FillImage) component.getRenderProperty(Table.PROPERTY_ROLLOVER_BACKGROUND_IMAGE);
            if (backgroundImage == null) {
                return null;
            } else {
                return backgroundImage.getImage();
            }
        } else if (IMAGE_ID_SELECTION_BACKGROUND.equals(imageId)) {
            FillImage backgroundImage 
                    = (FillImage) component.getRenderProperty(Table.PROPERTY_SELECTION_BACKGROUND_IMAGE);
            if (backgroundImage == null) {
                return null;
            } else {
                return backgroundImage.getImage();
            }
        } else {
            // Retrieve CellLayoutData background image if applicable.
            return CellLayoutDataRender.getCellLayoutDataBackgroundImage(component, imageId);
        }
    }

    /**
     * Returns the <code>TableLayoutData</code> of the given child,
     * or null if it does not provide layout data.
     *
     * @param child the child component
     * @return the layout data
     * @throws java.lang.RuntimeException if the the provided
     *         <code>LayoutData</code> is not a <code>TableLayoutData</code>
     */
    private TableLayoutData getLayoutData(Component child) {
        LayoutData layoutData = (LayoutData) child.getRenderProperty(Component.PROPERTY_LAYOUT_DATA);
        if (layoutData == null) {
            return null;
        } else if (layoutData instanceof TableLayoutData) {
            return (TableLayoutData) layoutData;
        } else {
            throw new RuntimeException("Invalid LayoutData for Table Child: " + layoutData.getClass().getName());
        }
    }
    
    /**
     * @see org.karora.cooee.webcontainer.ActionProcessor#processAction(org.karora.cooee.webcontainer.ContainerInstance, 
     *      org.karora.cooee.app.Component, org.w3c.dom.Element)
     */
    public void processAction(ContainerInstance ci, Component component, Element actionElement) {
        ci.getUpdateManager().getClientUpdateManager().setComponentAction(component, Table.INPUT_ACTION, null);
    }
    
    /**
     * @see org.karora.cooee.webcontainer.PropertyUpdateProcessor#processPropertyUpdate(
     *      org.karora.cooee.webcontainer.ContainerInstance, org.karora.cooee.app.Component, org.w3c.dom.Element)
     */
    public void processPropertyUpdate(ContainerInstance ci, Component component, Element propertyElement) {
        String propertyName = propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_NAME);
        if (PROPERTY_SELECTION.equals(propertyName)) {
            Element[] optionElements = DomUtil.getChildElementsByTagName(propertyElement, "row");
            int[] selectedIndices = new int[optionElements.length];
            for (int i = 0; i < optionElements.length; ++i) {
                selectedIndices[i] = Integer.parseInt(optionElements[i].getAttribute("index"));
            }
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, 
                    Table.SELECTION_CHANGED_PROPERTY, selectedIndices);
        }
    }
    
    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderAdd(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, java.lang.String, org.karora.cooee.app.Component)
     */
    public void renderAdd(RenderContext rc, ServerComponentUpdate update, String targetId, Component component) {
        Table table = (Table) component;
        Border border = (Border) table.getRenderProperty(Table.PROPERTY_BORDER);
        Insets tableInsets = (Insets) table.getRenderProperty(Table.PROPERTY_INSETS);
        String defaultInsetsAttributeValue = tableInsets == null 
                ? "0px" : InsetsRender.renderCssAttributeValue(tableInsets);
        CssStyle styleCss = new CssStyle();
        styleCss.setAttribute("padding", defaultInsetsAttributeValue);
        BorderRender.renderToStyle(styleCss, border);
        DomUpdate.renderStyleSheetAddRule(rc.getServerMessage(), "TD.c-" + component.getRenderId(), styleCss.renderInline());
        
        Element domAddTableElement = DomUpdate.renderElementAdd(rc.getServerMessage());
        DocumentFragment htmlFragment = rc.getServerMessage().getDocument().createDocumentFragment();
        renderHtml(rc, update, htmlFragment, component);
        DomUpdate.renderElementAddContent(rc.getServerMessage(), domAddTableElement, targetId, htmlFragment);
    }
    
    /**
     * Renders a child component.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param update the update
     * @param parentElement the HTML element which should contain the child
     * @param child the child component to render
     */
    private void renderAddChild(RenderContext rc, ServerComponentUpdate update, Element parentElement, Component child) {
        if (!child.isVisible()) {
            // Do nothing.
            return;
        }
        ComponentSynchronizePeer syncPeer = SynchronizePeerFactory.getPeerForComponent(child.getClass());
        if (syncPeer instanceof DomUpdateSupport) {
            ((DomUpdateSupport) syncPeer).renderHtml(rc, update, parentElement, child);
        } else {
            syncPeer.renderAdd(rc, update, getContainerId(child), child);
        }
    }
    
    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderDispose(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, org.karora.cooee.app.Component)
     */
    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {
        rc.getServerMessage().addLibrary(TABLE_SERVICE.getId());
        renderDisposeDirective(rc, (Table) component);
    }
    
    /**
     * Renders a directive to the outgoing <code>ServerMessage</code> to 
     * dispose the state of a table, performing tasks such as unregistering
     * event listeners on the client.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param table the table
     */
    private void renderDisposeDirective(RenderContext rc, Table table) {
 //       DomUpdate.renderElementRemove(rc.getServerMessage(), ContainerInstance.getElementId(table) + "_style");
       DomUpdate.renderStyleSheetRemoveRule(rc.getServerMessage(), "TD.c-" + table.getRenderId());
 
        ServerMessage serverMessage = rc.getServerMessage();
        Element itemizedUpdateElement = serverMessage.getItemizedDirective(ServerMessage.GROUP_ID_PREREMOVE,
                "EchoTable.MessageProcessor", "dispose",  new String[0], new String[0]);
        Element itemElement = serverMessage.getDocument().createElement("item");
        itemElement.setAttribute("eid", ContainerInstance.getElementId(table));
        itemizedUpdateElement.appendChild(itemElement);
    }

    /**
     * @see org.karora.cooee.webcontainer.DomUpdateSupport#renderHtml(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, org.w3c.dom.Node, org.karora.cooee.app.Component)
     */
    public void renderHtml(RenderContext rc, ServerComponentUpdate update, Node parentNode, Component component) {
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(TABLE_SERVICE.getId());
        Table table = (Table) component;
        
        renderInitDirective(rc, table);
        
        Border border = (Border) table.getRenderProperty(Table.PROPERTY_BORDER);
        Extent borderSize = border == null ? null : border.getSize();

        String elementId = ContainerInstance.getElementId(table);
        
        Document document = parentNode.getOwnerDocument();
        Element tableElement = document.createElement("table");
        tableElement.setAttribute("id", elementId);

        CssStyle tableCssStyle = new CssStyle();
        tableCssStyle.setAttribute("border-collapse", "collapse");
        
        if (((Boolean) table.getRenderProperty(Table.PROPERTY_SELECTION_ENABLED, Boolean.FALSE)).booleanValue()) {
            tableCssStyle.setAttribute("cursor", "pointer");
        }
        
        Insets tableInsets = (Insets) table.getRenderProperty(Table.PROPERTY_INSETS);
        
        String defaultInsetsAttributeValue = tableInsets == null ? "0px" : InsetsRender.renderCssAttributeValue(tableInsets);
        
        ColorRender.renderToStyle(tableCssStyle, component);
        FontRender.renderToStyle(tableCssStyle, component);
        BorderRender.renderToStyle(tableCssStyle, border);
        if (borderSize != null) {
            if (!rc.getContainerInstance().getClientProperties().getBoolean(
                    ClientProperties.QUIRK_CSS_BORDER_COLLAPSE_INSIDE)) {
                tableCssStyle.setAttribute("margin", ExtentRender.renderCssAttributeValueHalf(borderSize));
            }
        }
        
        Extent width = (Extent) table.getRenderProperty(Table.PROPERTY_WIDTH);
        boolean render100PercentWidthWorkaround = false;
        if (rc.getContainerInstance().getClientProperties().getBoolean(
                ClientProperties.QUIRK_IE_TABLE_PERCENT_WIDTH_SCROLLBAR_ERROR)) {
            if (width != null && width.getUnits() == Extent.PERCENT && width.getValue() == 100) {
                width = null;
                render100PercentWidthWorkaround = true;
            }
        }
        ExtentRender.renderToStyle(tableCssStyle, "width", width);
        
        tableElement.setAttribute("style", tableCssStyle.renderInline());
        
        parentNode.appendChild(tableElement);
        
        TableColumnModel columnModel = table.getColumnModel();
        int columnCount = columnModel.getColumnCount();
        
        boolean someColumnsHaveWidths = false;
        for (int i = 0; i < columnCount; ++i) {
            if (columnModel.getColumn(i).getWidth() != null) {
                someColumnsHaveWidths = true;
            }
        }
        if (someColumnsHaveWidths) {
            Element colGroupElement = document.createElement("colgroup");
            tableElement.appendChild(colGroupElement);
            
            for (int i = 0; i < columnCount; ++i) {
                Element colElement = document.createElement("col");
                Extent columnWidth = columnModel.getColumn(i).getWidth();
                if (columnWidth != null) {
                    colElement.setAttribute("width", ExtentRender.renderCssAttributeValue(columnWidth));
                }
                colGroupElement.appendChild(colElement);
            }
        }
        
        Element tbodyElement = document.createElement("tbody");
        tbodyElement.setAttribute("id", elementId + "_tbody");
        tableElement.appendChild(tbodyElement);
        
        Element firstTrElement = null;
        
        if (table.isHeaderVisible()) {
            firstTrElement = renderRow(rc, update, tbodyElement, table, Table.HEADER_ROW, defaultInsetsAttributeValue);
        }
        
        int rows = table.getModel().getRowCount();
        for (int rowIndex = 0; rowIndex < rows; ++rowIndex) {
            if (firstTrElement == null && rowIndex == 0) {
                firstTrElement = renderRow(rc, update, tbodyElement, table, rowIndex, defaultInsetsAttributeValue);
            } else {
                renderRow(rc, update, tbodyElement, table, rowIndex, defaultInsetsAttributeValue);
            }
        }
        
        if (render100PercentWidthWorkaround && firstTrElement != null) {
            // Render string of "sizing dots" in first row of cells.
            NodeList childNodes = firstTrElement.getChildNodes();
            int length = childNodes.getLength();
            for (int i = 0; i < length; ++i) {
                if (!"td".equals(childNodes.item(i).getNodeName())) {
                    continue;
                }
                Element tdElement = (Element) childNodes.item(i);
                Element sizingDivElement = document.createElement("div");
                sizingDivElement.setAttribute("style", "font-size:50px;height:0px;overflow:hidden;");
                sizingDivElement.appendChild(document.createTextNode(SIZING_DOTS));
                tdElement.appendChild(sizingDivElement);
            }
        }
    }
    
    /**
     * Renders a directive to the outgoing <code>ServerMessage</code> to 
     * initialize the state of a <code>Table</code>, performing tasks such as 
     * registering event listeners on the client.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param table the table
     */
    private void renderInitDirective(RenderContext rc, Table table) {
        String elementId = ContainerInstance.getElementId(table);
        ServerMessage serverMessage = rc.getServerMessage();
        Document document = serverMessage.getDocument();
        
        boolean rolloverEnabled = ((Boolean) table.getRenderProperty(Table.PROPERTY_ROLLOVER_ENABLED, 
                Boolean.FALSE)).booleanValue();
        boolean selectionEnabled = ((Boolean) table.getRenderProperty(Table.PROPERTY_SELECTION_ENABLED, 
                Boolean.FALSE)).booleanValue();
        
        String rolloverStyle = "";
        if (rolloverEnabled) {
            CssStyle rolloverCssStyle = new CssStyle();
            ColorRender.renderToStyle(rolloverCssStyle, 
                    (Color) table.getRenderProperty(Table.PROPERTY_ROLLOVER_FOREGROUND),
                    (Color) table.getRenderProperty(Table.PROPERTY_ROLLOVER_BACKGROUND));
            FontRender.renderToStyle(rolloverCssStyle, 
                    (Font) table.getRenderProperty(Table.PROPERTY_ROLLOVER_FONT));
            FillImageRender.renderToStyle(rolloverCssStyle, rc, this, table, IMAGE_ID_ROLLOVER_BACKGROUND,
                    (FillImage) table.getRenderProperty(Table.PROPERTY_ROLLOVER_BACKGROUND_IMAGE),
                    FillImageRender.FLAG_DISABLE_FIXED_MODE);
            if (rolloverCssStyle.hasAttributes()) {
                rolloverStyle = rolloverCssStyle.renderInline();
            }
        }
        
        String selectionStyle = "";
        if (selectionEnabled) {
            CssStyle selectionCssStyle = new CssStyle();
            ColorRender.renderToStyle(selectionCssStyle, 
                    (Color) table.getRenderProperty(Table.PROPERTY_SELECTION_FOREGROUND),
                    (Color) table.getRenderProperty(Table.PROPERTY_SELECTION_BACKGROUND));
            FontRender.renderToStyle(selectionCssStyle, 
                    (Font) table.getRenderProperty(Table.PROPERTY_SELECTION_FONT));
            FillImageRender.renderToStyle(selectionCssStyle, rc, this, table, IMAGE_ID_SELECTION_BACKGROUND,
                    (FillImage) table.getRenderProperty(Table.PROPERTY_SELECTION_BACKGROUND_IMAGE), 
                    FillImageRender.FLAG_DISABLE_FIXED_MODE);
            if (selectionCssStyle.hasAttributes()) {
                selectionStyle = selectionCssStyle.renderInline();
            }
        }
        
        Element itemizedUpdateElement = serverMessage.getItemizedDirective(ServerMessage.GROUP_ID_POSTUPDATE,
                "EchoTable.MessageProcessor", "init",  TABLE_INIT_KEYS, 
                new String[]{rolloverStyle, selectionStyle});
        Element itemElement = document.createElement("item");
        itemElement.setAttribute("eid", elementId);
        if (table.isHeaderVisible()) {
            itemElement.setAttribute("header-visible", "true");
        }
        
        if (table.hasActionListeners()) {
            itemElement.setAttribute("server-notify", "true");
        }
        
        if (rolloverEnabled) {
            itemElement.setAttribute("rollover-enabled", "true");
        }
        
        if (selectionEnabled) {
            itemElement.setAttribute("selection-enabled", "true");
            ListSelectionModel selectionModel = table.getSelectionModel();
            if (selectionModel.getSelectionMode() == ListSelectionModel.MULTIPLE_SELECTION) {
                itemElement.setAttribute("selection-mode", "multiple");
            }
            if (selectionModel.getMinSelectedIndex() != -1) {
                Element selectionElement = document.createElement("selection");
                int minimumIndex = selectionModel.getMinSelectedIndex();
                int maximumIndex = selectionModel.getMaxSelectedIndex();
                if (maximumIndex > table.getModel().getRowCount() - 1) {
                    maximumIndex = table.getModel().getRowCount() - 1;
                }
                for (int i = minimumIndex; i <= maximumIndex; ++i) {
                    if (selectionModel.isSelectedIndex(i)) {
                        Element rowElement = document.createElement("row");
                        rowElement.setAttribute("index", Integer.toString(i));
                        selectionElement.appendChild(rowElement);
                    }
                }
                itemElement.appendChild(selectionElement);
            }
        }
        
        if (!table.isRenderEnabled()) {
            itemElement.setAttribute("enabled", "false");
        }

        itemizedUpdateElement.appendChild(itemElement);
    }
    
    /**
     * Renders a single row of a table.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param update the <code>ServerComponentUpdate</code> being processed
     * @param tbodyElement the <code>tbody</code> element to which to append 
     *        the rendered content
     * @param table the <code>Table</code> being rendered
     * @param rowIndex the row to render
     * @param defaultInsetsAttributeValue the default CSS padding attribute value
     * @return the rendered TR element
     */
    private Element renderRow(RenderContext rc, ServerComponentUpdate update, Element tbodyElement, Table table, int rowIndex,
            String defaultInsetsAttributeValue) {
        Document document = tbodyElement.getOwnerDocument();
        String elementId = ContainerInstance.getElementId(table);
        
        Element trElement = document.createElement("tr");
        if (rowIndex == Table.HEADER_ROW) {
            trElement.setAttribute("id", elementId + "_tr_header");
        } else {
            trElement.setAttribute("id", elementId + "_tr_" + rowIndex); 
        }
        tbodyElement.appendChild(trElement);
        
        String className = "c-" + table.getRenderId();
        
        boolean inlineStyleRequired = rc.getContainerInstance().getClientProperties().getBoolean(
                ClientProperties.NOT_SUPPORTED_CSS_MANIPULATION);
        Border border = null;
        if (inlineStyleRequired) {
            border = (Border) table.getRenderProperty(Table.PROPERTY_BORDER);
        }
        
        int columns = table.getColumnModel().getColumnCount();
        for (int columnIndex = 0; columnIndex < columns; ++columnIndex) {
            Component childComponent = table.getCellComponent(columnIndex, rowIndex);
            Element tdElement = document.createElement("td");
            tdElement.setAttribute("id", elementId + "_cell_" + childComponent.getRenderId());
            
            CssStyle tdCssStyle = new CssStyle();
            
            if (inlineStyleRequired) {
                BorderRender.renderToStyle(tdCssStyle, border);
                CellLayoutDataRender.renderToElementAndStyle(tdElement, tdCssStyle, childComponent, getLayoutData(childComponent), 
                        defaultInsetsAttributeValue);
            } else {
                tdElement.setAttribute("class", className);
                CellLayoutDataRender.renderToElementAndStyle(tdElement, tdCssStyle, childComponent, getLayoutData(childComponent), 
                        null);
            }
            
            CellLayoutDataRender.renderBackgroundImageToStyle(tdCssStyle, rc, this, table, childComponent);
            if (tdCssStyle.hasAttributes()) {
                tdElement.setAttribute("style", tdCssStyle.renderInline());
            }
            
            trElement.appendChild(tdElement);
            
            renderAddChild(rc, update, tdElement, childComponent);
        }
        
        return trElement;
    }
    
    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderUpdate(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, java.lang.String)
     */
    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
        Table table = (Table) update.getParent();
        renderDisposeDirective(rc, table);
        DomUpdate.renderElementRemove(rc.getServerMessage(), ContainerInstance.getElementId(table));
        renderAdd(rc, update, targetId, table);
        return true;
    }
}
