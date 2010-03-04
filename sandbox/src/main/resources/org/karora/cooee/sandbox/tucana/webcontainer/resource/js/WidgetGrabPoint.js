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
 * Static Object/Namespace for the WidgetGrabPoint component.  This
 * should not be used externally.
 * 
 * @author Jeremy Volkman
 *
 * <p>
 * Constructs a new WidgetGrabPoint object.
 * </p>
 * 
 * @param grabPointId id of the WidgetGrabPoint DOM element
 * @param containerId id of the WidgetContainer DOM element containg this 
 * WidgetGrabPoint.  This element may not be the immediate parent.
 */
TucanaWidgetGrabPoint = function(grabPointId, containerId) {
    this.grabPointId = grabPointId;
    this.containerId = containerId;
}; 

// Static methods

/**
 * Static event handler used to process mousedown events for WidgetGrabPoints
 * 
 * @param event mousedown event
 */
TucanaWidgetGrabPoint.processMouseDown = function(event) {
    var instance = EchoDomPropertyStore.getPropertyValue(event.registeredTarget, "WidgetGrabPoint.instance");
    instance.processMouseDown(event);
};

// Prototype methods

/**
 * Initializes a WidgetGrabPoint. Registers the mousedown event handler and associates
 * the WidgetGrabPoint instance with the DOM object.
 */
TucanaWidgetGrabPoint.prototype.create = function() {
    var grabPointElement = document.getElementById(this.grabPointId);
    EchoDomPropertyStore.setPropertyValue(grabPointElement, "WidgetGrabPoint.instance", this);
    EchoEventProcessor.addHandler(grabPointElement, "mousedown", "TucanaWidgetGrabPoint.processMouseDown");
};

/**
 * Process a mousedown event associated with this WidgetGrabPoint instance.
 * 
 * @param event mousedown event
 */
TucanaWidgetGrabPoint.prototype.processMouseDown = function(event) {
    var containerInstance = TucanaWidgetContainer.getInstance(this.containerId);
    containerInstance.startDrag(event);
};


/**
 * Namespace for server->client message processor
 */
TucanaWidgetGrabPoint.MessageProcessor = function() { };

/**
 * Static message processing method.  This method delegates to specific handlers.
 * 
 * @param messagePartElement message element to process
 */
TucanaWidgetGrabPoint.MessageProcessor.process = function(messagePartElement) {
    for (var i = 0; i < messagePartElement.childNodes.length; ++i) {
        if (messagePartElement.childNodes[i].nodeType == 1) {
            switch (messagePartElement.childNodes[i].tagName) {
            case "init":
                TucanaWidgetGrabPoint.MessageProcessor.processInit(messagePartElement.childNodes[i]);
                break;
            case "dispose":
                TucanaWidgetGrabPoint.MessageProcessor.processDispose(messagePartElement.childNodes[i]);
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
TucanaWidgetGrabPoint.MessageProcessor.processDispose = function(disposeMessageElement) {
    var grabPointId = disposeMessageElement.getAttribute("eid");
    EchoEventProcessor.removeHandler(grabPointId, "mousedown");
};

/**
 * Processes an <code>init</code> message to initialize the state of a 
 * selection component that is being added.
 *
 * @param initMessageElement the <code>init</code> element to process
 */
TucanaWidgetGrabPoint.MessageProcessor.processInit = function(initMessageElement) {
    var grabPointId = initMessageElement.getAttribute("eid");

    // The WidgetContainer that this grab point will move
    var containerId = initMessageElement.getAttribute("widgetcontainer-eid");

    var grabPoint = new TucanaWidgetGrabPoint(grabPointId, containerId);
    grabPoint.create();
};
