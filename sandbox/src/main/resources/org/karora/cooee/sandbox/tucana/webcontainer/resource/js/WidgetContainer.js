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

/**
 * Static object/namespace for the WidgetContainer.  A WidgetContainer is an idividual
 * positionable element in the WidgetDash.
 * 
 * <p>Constructs a WidgetContainer</p>
 * 
 * @param componentId The id of the WidgetContainer DOM element
 * @param widgetDashId The id of the parent WidgetDash DOM element
 * @param widgetPosition The initial position of this WidgetContainer in the 
 * WidgetDash, if any.  A position consits of the widget's column, and position in that
 * column.
 */
TucanaWidgetContainer = function(componentId, widgetDashId, widgetPosition) {
    this.componentId = componentId;
    this.widgetDashId = widgetDashId;
    this.widgetPosition = widgetPosition;
};

// Static methods

/**
 * Static method to return the WidgetContainer instance for the given DOM element.
 * 
 * @param component The DOM element or element id.
 * @return The WidgetContainer instance associated with the element, or <code>null</code>
 * if there is no associated instance.
 */
TucanaWidgetContainer.getInstance = function(component) {
    var instance = EchoDomPropertyStore.getPropertyValue(component, "WidgetContainer.instance");
    return instance;
};

// Prototype methods

/**
 * Initialize the WidgetContainer.  Add this widget to the parent WidgetDash.
 */
TucanaWidgetContainer.prototype.create = function() {
    this.componentElement = document.getElementById(this.componentId);
    EchoDomPropertyStore.setPropertyValue(this.componentElement, "WidgetContainer.instance", this);
    var widgetDash = TucanaWidgetDash.getInstance(this.widgetDashId);
    widgetDash.addWidget(this.componentElement);
};

/**
 * Dispose of this WidgetContainer
 */
TucanaWidgetContainer.prototype.dispose = function() {
    // Do nothing?
};

/**
 * Start dragging a WidgetContainer.  This method is called by a WidgetGrabPoint
 * contained within this container.
 * 
 * @param event The mousedown event that initialized the drag
 */
TucanaWidgetContainer.prototype.startDrag = function(event) {
    TucanaWidgetDash.getInstance(this.widgetDashId).startDrag(this.componentId, event);
};

/**
 * Return the current WidgetPosition for this widget.
 * 
 * @return The current WidgetPosition, or <code>null</code> if there is none.
 */
TucanaWidgetContainer.prototype.getWidgetPosition = function() {
    return this.widgetPosition;
};

/**
 * Update this widget's WidgetPosition.  If the position has not changed,
 * do nothing.
 * 
 * @param position The (possibly) new position.
 */
TucanaWidgetContainer.prototype.updateWidgetPosition = function(position) {
    if (!this.widgetPosition || !this.widgetPosition.equals(position)) {
        // Changed
        this.widgetPosition = position;
        var propertyElement = EchoClientMessage.createPropertyElement(this.componentId, "widgetPosition");
        var positionElement = propertyElement.ownerDocument.createElement("position");
        positionElement.setAttribute("column", position.column);
        positionElement.setAttribute("column-position", position.columnPosition);

        while(propertyElement.firstChild) { propertyElement.removeChild(propertyElement.firstChild); }
        propertyElement.appendChild(positionElement);
        return true;
    } 
    return false;
};

/**
 * Update the spacing between this widget and the one below it.  If there is no
 * widget below this one, the spacing should be invisible.
 * 
 * @param widgetPaneInstance the parent WidgetDash instance
 */
TucanaWidgetContainer.prototype.updateWidgetSpacing = function(widgetDashInstance) {
   var spacing = widgetDashInstance.widgetSpacing;
   // Check to see if this is the last widget in a column, and if not, enable
   // the lower margin.
   
   if (!TucanaWidgetDash.isColumnTerminator(this.componentElement.nextSibling)) {
       this.componentElement.style.marginBottom = widgetDashInstance.widgetSpacing;
   } else {
       this.componentElement.style.marginBottom = null;
   }
};

// Message Processor

/**
 * Namespace for server->client message processor
 */
TucanaWidgetContainer.MessageProcessor = function() { };

/**
 * Static message processing method.  This method delegates to specific handlers.
 * 
 * @param messagePartElement message element to process
 */
TucanaWidgetContainer.MessageProcessor.process = function(messagePartElement) {
    for (var i = 0; i < messagePartElement.childNodes.length; ++i) {
        if (messagePartElement.childNodes[i].nodeType == 1) {
            switch (messagePartElement.childNodes[i].tagName) {
            case "init":
                TucanaWidgetContainer.MessageProcessor.processInit(messagePartElement.childNodes[i]);
                break;
            case "dispose":
                TucanaWidgetContainer.MessageProcessor.processDispose(messagePartElement.childNodes[i]);
                break;
            }
        }
    }
};

/**
 * Processes a <code>dispose</code> message to finalize the state of a
 * selection component that is being removed.
 *
 * @param disposeMessageElement the <code>dispose</code> element to process
 */
TucanaWidgetContainer.MessageProcessor.processDispose = function(disposeMessageElement) {
    var componentId = disposeMessageElement.getAttribute("eid");
    var widgetElement = document.getElementById(componentId);
    if (!widgetElement) return;
    var widgetInstance = TucanaWidgetContainer.getInstance(widgetElement);
    if (!widgetInstance) return;
    
    var widgetDashInstance = TucanaWidgetDash.getInstance(widgetInstance.widgetDashId)
    widgetDashInstance.removeWidget(widgetElement);
};

/**
 * Processes an <code>init</code> message to initialize the state of a 
 * selection component that is being added.
 *
 * @param initMessageElement the <code>init</code> element to process
 */
TucanaWidgetContainer.MessageProcessor.processInit = function(initMessageElement) {
    var componentId = initMessageElement.getAttribute("eid");
	
	// The WidgetContainer that this grab point will move
    var widgetDashId = initMessageElement.getAttribute("widgetdash-eid");

    var position = null;
    var child = initMessageElement.firstChild;
    if (child && child.tagName == "position") {
        var column = child.getAttribute("column");
        var columnPosition = child.getAttribute("column-position");
        position = new TucanaWidgetDash.WidgetPosition(column, columnPosition);
    }
    
    var component = new TucanaWidgetContainer(componentId, widgetDashId, position);
    component.create();
};

