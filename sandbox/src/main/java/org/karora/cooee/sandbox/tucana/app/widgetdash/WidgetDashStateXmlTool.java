/* 
 * This file is part of the Tucana Echo2 Library.
 * Copyright (C) 2006.
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

package org.karora.cooee.sandbox.tucana.app.widgetdash;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * A utility to marshal a {@link WidgetDashState} object to XML, and to unmarshal
 * XML to a WidgetPaneState object.
 * 
 * The format is as follows
 * <p><code>
 * &lt;widgetpanestate column-count="3"&gt;<br>
 * &nbsp;&nbsp;&lt;widget identifier="widget1"&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&ltposition column="0" column-position="0"/&gt;<br>
 * &nbsp;&nbsp;&lt/widget&gt;<br>
 * &nbsp;&nbsp;&lt;widget identifier="widget2"&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&ltposition column="1" column-position="0"/&gt;<br>
 * &nbsp;&nbsp;&lt/widget&gt;<br>
 * &ltwidgetpanestate&gt;<br>
 * </code></p>
 * @author Jeremy Volkman
 */
public class WidgetDashStateXmlTool {
    
    /**
     * Marshal a WidgetPaneState object to XMl
     * @param state The WidgetPaneState object
     * @return The constructed W3C Document 
     */
    public Document toXmlDocument(WidgetDashState state) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            throw new RuntimeException(pce);
        }
        Document stateDoc = builder.newDocument();

        Element root = stateDoc.createElement("widgetpanestate");
        root.setAttribute("column-count", Integer.toString(state.getColumnCount()));
        
        WidgetIdentifier[] identifiers = state.getWidgetIdentifiers();
        for (int i = 0; i < identifiers.length; i++) {
            WidgetIdentifier identifier = identifiers[i];
            WidgetPosition position = state.getWidgetPosition(identifier);
            Element widgetElement = stateDoc.createElement("widget");
            widgetElement.setAttribute("identifier", identifier.getIdentifier());
            if (position != null) {
                Element positionElement = stateDoc.createElement("position");
                positionElement.setAttribute("column", Integer.toString(position.getColumn()));
                positionElement.setAttribute("column-position", Integer.toString(position.getColumnPosition()));
                widgetElement.appendChild(positionElement);
            }
            root.appendChild(widgetElement);
        }
        
        stateDoc.appendChild(root);

        return stateDoc;
    }
    
    /**
     * Unmarshal a WidgetPaneState XML representation into a WidgetPaneState object
     * @param document The XML document
     * @return a WidgetPaneState object from the document.
     */
    public WidgetDashState fromXmlDocument(Document document) {
        Element rootElement = (Element) document.getFirstChild();
        int columnCount = WidgetDashState.COLUMN_COUNT_DEFAULT;
        try {
            columnCount = Integer.parseInt(rootElement.getAttribute("column-count"));
        } catch (NumberFormatException e) {
        }

        Map widgetsToPositions = new HashMap();
        
        NodeList elements = rootElement.getElementsByTagName("widget");
        
        for (int i = 0; i < elements.getLength(); i++) {
            Element widgetElement = (Element) elements.item(i);
            String identifier = widgetElement.getAttribute("identifier");
            Element positionElement = (Element) widgetElement.getFirstChild();
            WidgetPosition position = null;
            if (positionElement != null) {
                String column = positionElement.getAttribute("column");
                String columnPosition = positionElement.getAttribute("column-position");
                try {
                    position = new WidgetPosition(Integer.parseInt(column), 
                            Integer.parseInt(columnPosition));
                } catch (NumberFormatException e) {
                    
                }
            }
            widgetsToPositions.put(new WidgetIdentifier(identifier), position);
        }
        
        return new WidgetPaneStateImpl(columnCount, widgetsToPositions);
    }
    
    /**
     * Marshal a WidgetPaneState object to an XML string
     * @param state The WidgetPaneState object
     * @return The constructed XML as a string
     */
    public String toXmlString(WidgetDashState state) {
        Document document = toXmlDocument(state);
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
    
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(document);
    
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            
            transformer.transform(source, result);        
            return writer.getBuffer().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unmarshal a WidgetPaneState XML representation into a WidgetPaneState object
     * @param stateXml The XML document as astring
     * @return a WidgetPaneState object from the document.
     */
    public WidgetDashState fromXmlString(String stateXml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document stateDoc = builder.parse(new InputSource(new StringReader(stateXml)));
            return fromXmlDocument(stateDoc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Implementation of WidgetPaneState.
     * @author Jeremy Volkman
     */
    private static class WidgetPaneStateImpl implements WidgetDashState {

        private int columnCount;
        private Map widgetMap;
        
        public WidgetPaneStateImpl(int columnCount, Map widgetMap) {
            this.columnCount = columnCount;
            this.widgetMap = widgetMap;
        }
        
        
        public int getColumnCount() {
            return columnCount;
        }

        public WidgetIdentifier[] getWidgetIdentifiers() {
            return (WidgetIdentifier[])
                widgetMap.keySet().toArray(new WidgetIdentifier[widgetMap.size()]);
        }

        public WidgetPosition getWidgetPosition(WidgetIdentifier widget) {
            return (WidgetPosition) widgetMap.get(widget);
        }
        
    }
}
