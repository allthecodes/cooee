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

package org.karora.cooee.sandbox.tucana.webcontainer;

import org.karora.cooee.app.Border;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.sandbox.tucana.app.WidgetDash;
import org.karora.cooee.sandbox.tucana.app.widgetdash.WidgetContainer;
import org.karora.cooee.webcontainer.ActionProcessor;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.PropertyUpdateProcessor;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webcontainer.SynchronizePeerFactory;
import org.karora.cooee.webcontainer.propertyrender.BorderRender;
import org.karora.cooee.webcontainer.propertyrender.ExtentRender;
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.service.JavaScriptService;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WidgetDashPeer implements ComponentSynchronizePeer,
		ActionProcessor, PropertyUpdateProcessor {

	/**
	 * Service to provide supporting JavaScript library.
	 */
	private static final Service WIDGET_DASH_SERVICE = JavaScriptService
			.forResource("Tucana.WidgetDash",
					"/org/karora/cooee/sandbox/tucana/webcontainer/resource/js/WidgetDash.js");

	static {
		WebRenderServlet.getServiceRegistry().add(WIDGET_DASH_SERVICE);
	}

	public String getContainerId(Component child) {
		return ContainerInstance.getElementId(child.getParent()) + "_onDeck";
	}

	public void renderAdd(RenderContext rc, ServerComponentUpdate update,
			String targetId, Component component) {
		rc.getServerMessage().addLibrary(WIDGET_DASH_SERVICE.getId());
		renderInitDirective(rc, (WidgetDash) component, targetId);
		Component[] children = component.getComponents();
		for (int i = 0; i < children.length; i++) {
			Component child = children[i];
			ComponentSynchronizePeer syncPeer = SynchronizePeerFactory
					.getPeerForComponent(child.getClass());
			syncPeer.renderAdd(rc, update, getContainerId(child), child);
		}
	}

	public void renderDispose(RenderContext rc, ServerComponentUpdate update,
			Component component) {
		ServerMessage serverMessage = rc.getServerMessage();
		serverMessage.addLibrary(WIDGET_DASH_SERVICE.getId());
		Element partElement = serverMessage.addPart(
				ServerMessage.GROUP_ID_UPDATE,
				"TucanaWidgetDash.MessageProcessor");
		Element disposeElement = serverMessage.getDocument().createElement(
				"dispose");

		String elementId = ContainerInstance.getElementId(component);

		disposeElement.setAttribute("eid", elementId);

		partElement.appendChild(disposeElement);
	}

	public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update,
			String targetId) {
		ServerMessage serverMessage = rc.getServerMessage();
		Element partElement = serverMessage.addPart(
				ServerMessage.GROUP_ID_UPDATE,
				"TucanaWidgetDash.MessageProcessor");
		Element updateElement = serverMessage.getDocument().createElement(
				"update");
		String componentId = ContainerInstance.getElementId(update.getParent());
		updateElement.setAttribute("eid", componentId);
		partElement.appendChild(updateElement);

		String[] updatedPropertyNames = update.getUpdatedPropertyNames();
		if (updatedPropertyNames.length > 0) {
			Element propertiesElement = serverMessage.getDocument()
					.createElement("properties");
			updateElement.appendChild(propertiesElement);
			WidgetDash widgetDash = (WidgetDash) update.getParent();
			for (int i = 0; i < updatedPropertyNames.length; i++) {
				String propertyName = updatedPropertyNames[i];
				if (WidgetDash.PROPERTY_COLUMN_COUNT.equals(propertyName)) {
					addToStringProperty(propertiesElement, widgetDash,
							WidgetDash.PROPERTY_COLUMN_COUNT);
				} else if (WidgetDash.PROPERTY_COLUMN_SPACING
						.equals(propertyName)) {
					addExtentProperty(propertiesElement, widgetDash,
							WidgetDash.PROPERTY_COLUMN_SPACING);
				} else if (WidgetDash.PROPERTY_DRAG_CONTAINER
						.equals(propertyName)) {
					addDragContainerProperty(propertiesElement, widgetDash);
				} else if (WidgetDash.PROPERTY_DRAG_IN_BODY
						.equals(propertyName)) {
					addToStringProperty(propertiesElement, widgetDash,
							WidgetDash.PROPERTY_DRAG_IN_BODY);
				} else if (WidgetDash.PROPERTY_DRAGGED_WIDGET_OPACITY
						.equals(propertyName)) {
					addToStringProperty(propertiesElement, widgetDash,
							WidgetDash.PROPERTY_DRAGGED_WIDGET_OPACITY);
				} else if (WidgetDash.PROPERTY_SHADOW_BORDER
						.equals(propertyName)) {
					addShadowBorderProperty(propertiesElement, widgetDash);
				} else if (WidgetDash.PROPERTY_SHADOW_OPACITY
						.equals(propertyName)) {
					addToStringProperty(propertiesElement, widgetDash,
							WidgetDash.PROPERTY_SHADOW_OPACITY);
				} else if (WidgetDash.PROPERTY_SHADOW_TYPE.equals(propertyName)) {
					addShadowTypeProperty(propertiesElement, widgetDash);
				} else if (WidgetDash.PROPERTY_WIDGET_SPACING
						.equals(propertyName)) {
					addExtentProperty(propertiesElement, widgetDash,
							WidgetDash.PROPERTY_WIDGET_SPACING);
				} else if (WidgetDash.PROPERTY_RETURN_METHOD
						.equals(propertyName)) {
					addReturnMethodProperty(propertiesElement, widgetDash);
				} else if (WidgetDash.PROPERTY_DRIFT_STEP.equals(propertyName)) {
					addToStringProperty(propertiesElement, widgetDash,
							WidgetDash.PROPERTY_DRIFT_STEP);
				} else if (WidgetDash.PROPERTY_DRIFT_INTERVAL
						.equals(propertyName)) {
					addToStringProperty(propertiesElement, widgetDash,
							WidgetDash.PROPERTY_DRIFT_INTERVAL);
				}
			}
		}

		Component[] addedChildren = update.getAddedChildren();
		for (int i = 0; i < addedChildren.length; i++) {
			Component child = addedChildren[i];
			ComponentSynchronizePeer syncPeer = SynchronizePeerFactory
					.getPeerForComponent(child.getClass());
			syncPeer.renderAdd(rc, update, getContainerId(child), child);
		}

		return false;
	}

	/**
	 * Renders a directive to the outgoing <code>ServerMessage</code> to
	 * render and intialize the state of a <code>WidgetPane</code>.
	 * 
	 * @param rc
	 *            the relevant <code>RenderContext</code>
	 * @param windowPane
	 *            the <code>Widget</code>
	 * @param targetId
	 *            the id of the container element
	 */
	private void renderInitDirective(RenderContext rc, WidgetDash widgetDash,
			String targetId) {
		String elementId = ContainerInstance.getElementId(widgetDash);
		ServerMessage serverMessage = rc.getServerMessage();
		Element partElement = serverMessage.addPart(
				ServerMessage.GROUP_ID_UPDATE,
				"TucanaWidgetDash.MessageProcessor");
		Element initElement = serverMessage.getDocument().createElement("init");
		initElement.setAttribute("eid", elementId);
		initElement.setAttribute("container-eid", targetId);
		Element propertiesElement = serverMessage.getDocument().createElement(
				"properties");
		initElement.appendChild(propertiesElement);

		addToStringProperty(propertiesElement, widgetDash,
				WidgetDash.PROPERTY_COLUMN_COUNT);
		addExtentProperty(propertiesElement, widgetDash,
				WidgetDash.PROPERTY_COLUMN_SPACING);
		addExtentProperty(propertiesElement, widgetDash,
				WidgetDash.PROPERTY_WIDGET_SPACING);

		addShadowTypeProperty(propertiesElement, widgetDash);
		addShadowBorderProperty(propertiesElement, widgetDash);
		addDragContainerProperty(propertiesElement, widgetDash);
		addToStringProperty(propertiesElement, widgetDash,
				WidgetDash.PROPERTY_SHADOW_OPACITY);
		addToStringProperty(propertiesElement, widgetDash,
				WidgetDash.PROPERTY_DRAGGED_WIDGET_OPACITY);
		addToStringProperty(propertiesElement, widgetDash,
				WidgetDash.PROPERTY_DRAG_IN_BODY);

		addReturnMethodProperty(propertiesElement, widgetDash);
		addToStringProperty(propertiesElement, widgetDash,
				WidgetDash.PROPERTY_DRIFT_STEP);
		addToStringProperty(propertiesElement, widgetDash,
				WidgetDash.PROPERTY_DRIFT_INTERVAL);

		partElement.appendChild(initElement);
	}

	private Element createPropertyElement(Node parentNode, String propertyName,
			String propertyValue) {
		Element propertyElement = parentNode.getOwnerDocument().createElement(
				"property");
		parentNode.appendChild(propertyElement);

		propertyElement.setAttribute("name", propertyName);
		if (propertyValue != null) {
			propertyElement.setAttribute("value", propertyValue);
		}

		return propertyElement;
	}

	private void addToStringProperty(Node parentNode, WidgetDash widgetDash,
			String propertyName) {
		Object obj = widgetDash.getProperty(propertyName);
		if (obj != null) {
			createPropertyElement(parentNode, propertyName, obj.toString());
		}
	}

	private void addExtentProperty(Node parentNode, WidgetDash widgetDash,
			String propertyName) {
		Extent extent = (Extent) widgetDash.getProperty(propertyName);
		if (extent != null) {
			String extentString = ExtentRender.renderCssAttributeValue(extent);
			createPropertyElement(parentNode, propertyName, extentString);
		}
	}

	private void addShadowTypeProperty(Node parentNode, WidgetDash widgetDash) {
		Integer type = (Integer) widgetDash
				.getProperty(WidgetDash.PROPERTY_SHADOW_TYPE);
		if (type != null) {
			int typeVal = type.intValue();
			String typeString;
			if (typeVal == WidgetDash.SHADOW_TYPE_BORDER) {
				typeString = "border";
			} else {
				typeString = "clone";
			}
			createPropertyElement(parentNode, WidgetDash.PROPERTY_SHADOW_TYPE,
					typeString);
		}
	}

	private void addReturnMethodProperty(Node parentNode, WidgetDash widgetDash) {
		Integer type = (Integer) widgetDash
				.getProperty(WidgetDash.PROPERTY_RETURN_METHOD);
		if (type != null) {
			int typeVal = type.intValue();
			String typeString;
			if (typeVal == WidgetDash.RETURN_METHOD_DRIFT) {
				typeString = "drift";
			} else {
				typeString = "snap";
			}
			createPropertyElement(parentNode,
					WidgetDash.PROPERTY_RETURN_METHOD, typeString);
		}
	}

	private void addShadowBorderProperty(Node parentNode, WidgetDash widgetDash) {
		Border border = (Border) widgetDash
				.getProperty(WidgetDash.PROPERTY_SHADOW_BORDER);
		if (border != null) {
			String borderStyle = BorderRender.renderCssAttributeValue(border);
			createPropertyElement(parentNode,
					WidgetDash.PROPERTY_SHADOW_BORDER, borderStyle);
		}
	}

	private void addDragContainerProperty(Node parentNode, WidgetDash widgetDash) {
		Component dragContainer = (Component) widgetDash
				.getProperty(WidgetDash.PROPERTY_DRAG_CONTAINER);
		if (dragContainer != null) {
			String containerId = ContainerInstance.getElementId(dragContainer);
			if (containerId != null) {
				createPropertyElement(parentNode,
						WidgetDash.PROPERTY_DRAG_CONTAINER, containerId);
			}
		}
	}

	public void processAction(ContainerInstance ci, Component component,
			Element actionElement) {
		String actionName = actionElement
				.getAttribute(ActionProcessor.ACTION_NAME);
		if (WidgetDash.ACTION_POSITIONS_UPDATED.equals(actionName)) {
			ci.getUpdateManager().getClientUpdateManager().setComponentAction(
					component, WidgetDash.ACTION_POSITIONS_UPDATED, null);
		}
	}

	public void processPropertyUpdate(ContainerInstance ci,
			Component component, Element propertyElement) {
		if (WidgetDash.PROPERTY_ACTIVE_WIDGET_CONTAINER.equals(propertyElement
				.getAttribute(PROPERTY_NAME))) {
			String containerId = propertyElement.getAttribute(PROPERTY_VALUE);
			WidgetDash widgetDash = (WidgetDash) component;
			boolean foundComponent = false;
			for (Component widgetContainer : widgetDash.getComponents()) {
				if (ContainerInstance.getElementId(widgetContainer).equals(
						containerId)) {
					widgetDash
							.setActiveWidgetContainer((WidgetContainer) widgetContainer);
					foundComponent = true;
					break;
				}
			}
			if (!foundComponent)
				widgetDash.setActiveWidgetContainer(null);
		}
	}
}
