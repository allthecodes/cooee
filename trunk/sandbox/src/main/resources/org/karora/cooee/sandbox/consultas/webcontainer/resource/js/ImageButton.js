/* 
 * This file is part of the Cooee Application Framework 
 *
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
 *
 */

//__________________
// Object EchoImageButton

/**
 * Static object/namespace for Button support.
 * This object/namespace should not be used externally.
 * <p>
 * Creates a new Button data object.
 */
EchoImageButton = function(elementId) { 
    this.elementId = elementId;
};

/** State constant indicating default button state. */
EchoImageButton.STATE_DEFAULT = 0;

/** State constant indicating rollover button state. */
EchoImageButton.STATE_ROLLOVER = 1;

/** State constant indicating pressed button state. */
EchoImageButton.STATE_PRESSED = 2;

/**
 * Disposes of a button data object, unregistering event listeners
 * and cleaning up resources of underlying element.
 */
EchoImageButton.prototype.dispose = function() {
    var element = this.getElement();
    EchoEventProcessor.removeHandler(element, "click");
    EchoEventProcessor.removeHandler(element, "keypress");
    EchoEventProcessor.removeHandler(element, "mousedown");
    EchoEventProcessor.removeHandler(element, "mouseup");
    if (EchoClientProperties.get("proprietaryEventMouseEnterLeaveSupported")) {
        EchoEventProcessor.removeHandler(element, "mouseenter");
        EchoEventProcessor.removeHandler(element, "mouseleave");
    } else {
        EchoEventProcessor.removeHandler(element, "mouseout");
        EchoEventProcessor.removeHandler(element, "mouseover");
    }
    
    if (this.groupId) {
        EchoImageButton.Group.remove(this.groupId, this.elementId);
    }

    EchoDomPropertyStore.dispose(element);
};

/**
 * Programmatically invokes a button's action.
 * Togglebuttons will have their state toggled as a result.
 * The server will be notified of the action in the event
 * the button has server-side <code>ActionListener</code>s.
 *
 * @param elementId the button element id
 */
EchoImageButton.prototype.doAction = function() {
    if (this.toggle) {
        this.doToggle();
    }
    
    if (!this.serverNotify) {
        return;
    }
    
    if (document.selection && document.selection.empty) {
        document.selection.empty();
    }
    
    EchoClientMessage.setActionValue(this.elementId, "click");
    
    EchoServerTransaction.connect();
};

/**
 * Toggles the state of a toggle button.
 */
EchoImageButton.prototype.doToggle = function() {
    var newState = !this.selected;
    if (this.groupId) {
        if (!newState) {
            // Clicking on selected radio button: do nothing.
            return;
        }
        EchoImageButton.Group.deselect(this.groupId);
    }
    this.setSelected(newState);
};

EchoImageButton.prototype.getElement = function() {
    return document.getElementById(this.elementId);
};

/**
 * Initializes the state of a configured button data object.
 * Registers event listeners for the underlying element.
 */
EchoImageButton.prototype.init = function() {
    if (this.groupId) {
        EchoImageButton.Group.add(this.groupId, this.elementId);
    }
    
    var element = this.getElement();
    
    EchoEventProcessor.addHandler(element, "click", "EchoImageButton.processClick");
    EchoEventProcessor.addHandler(element, "keypress", "EchoImageButton.processKeyPressed");
    EchoEventProcessor.addHandler(element, "mousedown", "EchoImageButton.processPressed");
    EchoEventProcessor.addHandler(element, "mouseup", "EchoImageButton.processReleased");
    if (EchoClientProperties.get("proprietaryEventMouseEnterLeaveSupported")) {
        EchoEventProcessor.addHandler(element, "mouseenter", "EchoImageButton.processRolloverEnter");
        EchoEventProcessor.addHandler(element, "mouseleave", "EchoImageButton.processRolloverExit");
    } else {
        EchoEventProcessor.addHandler(element, "mouseout", "EchoImageButton.processRolloverExit");
        EchoEventProcessor.addHandler(element, "mouseover", "EchoImageButton.processRolloverEnter");
    }
    
    element.onselectstart = function () {
    	return false;
	};
    
    EchoDomPropertyStore.setPropertyValue(element, "component", this);
};

/**
 * Processes a button mouse click event.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoImageButton.prototype.processClick = function(echoEvent) {
    if (!this.enabled || !EchoClientEngine.verifyInput(this.elementId)) {
        return;
    }
    this.doAction();
};

/**
 * Processes a button key press event.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoImageButton.prototype.processKeyPressed = function(echoEvent) {
    if (!this.enabled || !EchoClientEngine.verifyInput(this.elementId)) {
        return;
    }
    if (echoEvent.keyCode == 13 || echoEvent.keyCode == 32) {
        this.doAction();
    }
};

/**
 * Processes a button mouse press event.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoImageButton.prototype.processPressed = function(echoEvent) {
    if (!this.enabled || !EchoClientEngine.verifyInput(this.elementId)) {
        return;
    }
    EchoDomUtil.preventEventDefault(echoEvent);
    this.setVisualState(EchoImageButton.STATE_PRESSED);
};

/**
 * Processes a button mouse release event.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoImageButton.prototype.processReleased = function(echoEvent) {
    this.setVisualState(EchoImageButton.STATE_DEFAULT);
};

/**
 * Processes a button mouse rollover enter event.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoImageButton.prototype.processRolloverEnter = function(echoEvent) {
    if (!this.enabled || !EchoClientEngine.verifyInput(this.elementId)) {
        return;
    }
    this.setVisualState(EchoImageButton.STATE_ROLLOVER);
};

/**
 * Processes a button mouse rollover exit event.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoImageButton.prototype.processRolloverExit = function(echoEvent) {
    this.setVisualState(EchoImageButton.STATE_DEFAULT);
};

/**
 * Sets the selection state of a toggle button.
 * @param newState a boolean flag indicating the new selection state
 */
EchoImageButton.prototype.setSelected = function(newState) {
    this.selected = newState;

    var stateIconUri = newState ? this.selectedStateIcon : this.stateIcon;
    if (!stateIconUri) {
        throw new Error("State icon not specified for selection state: " + newState);
    }
    var stateIconElement = document.getElementById(this.elementId + "_stateicon");
    stateIconElement.src = stateIconUri;
    
    EchoClientMessage.setPropertyValue(this.elementId, "selected", newState ? "true" : "false");
};

/**
 * Sets the visual state of a specific button.
 *
 * @param newState the new visual state, one of the following values:
 *        <ul>
 *         <li><code>EchoImageButton.STATE_DEFFAULT</code></li>
 *         <li><code>EchoImageButton.STATE_PRESSED</code></li>
 *         <li><code>EchoImageButton.STATE_ROLLOVER</code></li>
 *        </ul>
 */
EchoImageButton.prototype.setVisualState = function(newState) {
    var newStyle, newIcon, newStateIcon, newStyleLeft, newStyleCenter, newStyleRight;
    
    switch (newState) {
    case EchoImageButton.STATE_ROLLOVER:
        newStyle = this.rolloverStyle;
        newStyleLeft = this.rolloverStyleLeft;
        newStyleCenter = this.rolloverStyleCenter;
        newStyleRight = this.rolloverStyleRight;
        newIcon = this.rolloverIcon;
        newStateIcon = this.selected ? this.rolloverSelectedStateIcon : this.rolloverStateIcon;
        break;
    case EchoImageButton.STATE_PRESSED:
        newStyle = this.pressedStyle;
        newStyleLeft = this.pressedStyleLeft;
        newStyleCenter = this.pressedStyleCenter;
        newStyleRight = this.pressedStyleRight;   
        newIcon = this.pressedIcon;
        newStateIcon = this.selected ? this.pressedSelectedStateIcon : this.pressedStateIcon;
        break;
    default:
    	if(this.previousState && this.previousState == EchoImageButton.STATE_PRESSED){
	    	newStyle = this.rolloverStyle;
	        newStyleLeft = this.rolloverStyleLeft;
	        newStyleCenter = this.rolloverStyleCenter;
	        newStyleRight = this.rolloverStyleRight;
	        newIcon = this.rolloverIcon;
	        newStateIcon = this.selected ? this.rolloverSelectedStateIcon : this.rolloverStateIcon;
    	}
    	
        newIcon = this.defaultIcon;
        newStateIcon = this.selected ? this.selectedStateIcon : this.stateIcon;
    }
    
    if (newIcon) {
        var iconElement = document.getElementById(this.elementId + "_icon");
        iconElement.src = newIcon;
    }
    if (newStateIcon) {
        var stateIconElement = document.getElementById(this.elementId + "_stateicon");
        stateIconElement.src = newStateIcon;
    }
    
    var element = this.getElement();
    EchoCssUtil.restoreOriginalStyle(element);
    EchoCssUtil.restoreOriginalStyle(document.getElementById(element.id + "_outer_td_0"));
    EchoCssUtil.restoreOriginalStyle(document.getElementById(element.id + "_outer_td_1"));
    EchoCssUtil.restoreOriginalStyle(document.getElementById(element.id + "_outer_td_2"));
    if (newStyle) {
    	EchoCssUtil.applyTemporaryStyle(element, newStyle);
    }
	if (newStyleLeft) {
        EchoCssUtil.applyTemporaryStyle(document.getElementById(element.id + "_outer_td_0"), newStyleLeft);
    }
	if (newStyleCenter) {
        EchoCssUtil.applyTemporaryStyle(document.getElementById(element.id + "_outer_td_1"), newStyleCenter);
    }
    if (newStyleRight) {
        EchoCssUtil.applyTemporaryStyle(document.getElementById(element.id + "_outer_td_2"), newStyleRight);
    }
    EchoImageButton.ieRepaint(element);
    
    this.previousState = newState;
};

/**
 * Returns the Button data object instance based on the root element id
 * of the Button.
 *
 * @param element the root element or element id of the Button
 * @return the relevant Button instance
 */
EchoImageButton.getComponent = function(element) {
    return EchoDomPropertyStore.getPropertyValue(element, "component");
};

/**
 * Forces quirky IE clients to repaint immediately for a sometimes very 
 * signficant aesthetic performance improvement.
 * Invoking this method performs no operation/has no effect for other browser 
 * clients that do not suffer this quirk.
 *
 * @param the button element to force repaint
 */
EchoImageButton.ieRepaint = function(buttonElement) {
    if (EchoClientProperties.get("quirkIERepaint")) {
        var originalWidth = buttonElement.style.width;
        var temporaryWidth = parseInt(buttonElement.clientWidth) + 1;
        buttonElement.style.width = temporaryWidth + "px";
        buttonElement.style.width = originalWidth;
    }
};

/**
 * Processes a button mouse click event.
 * This method delegates to corresponding method
 * on EchoImageButton data object instance.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoImageButton.processClick = function(echoEvent) {
    var button = EchoImageButton.getComponent(echoEvent.registeredTarget);
    button.processClick(echoEvent);
};

/**
 * Processes a button key press event.
 * This method delegates to corresponding method
 * on EchoImageButton data object instance.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoImageButton.processKeyPressed = function(echoEvent) {
    var button = EchoImageButton.getComponent(echoEvent.registeredTarget);
    button.processKeyPressed(echoEvent);
};

/**
 * Processes a button mouse press event.
 * This method delegates to corresponding method
 * on EchoImageButton data object instance.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoImageButton.processPressed = function(echoEvent) {
    var button = EchoImageButton.getComponent(echoEvent.registeredTarget);
    button.processPressed(echoEvent);
};

/**
 * Processes a button mouse release event.
 * This method delegates to corresponding method
 * on EchoImageButton data object instance.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoImageButton.processReleased = function(echoEvent) {
    var button = EchoImageButton.getComponent(echoEvent.registeredTarget);
    button.processReleased(echoEvent);
};

/**
 * Processes a button mouse rollover enter event.
 * This method delegates to corresponding method
 * on EchoImageButton data object instance.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoImageButton.processRolloverEnter = function(echoEvent) {
    var button = EchoImageButton.getComponent(echoEvent.registeredTarget);
    button.processRolloverEnter(echoEvent);
};

/**
 * Processes a button mouse rollover exit event.
 * This method delegates to corresponding method
 * on EchoImageButton data object instance.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoImageButton.processRolloverExit = function(echoEvent) {
    var button = EchoImageButton.getComponent(echoEvent.registeredTarget);
    button.processRolloverExit(echoEvent);
};


/**
 * Static object/namespace for RadioButton group management.
 */
EchoImageButton.Group = function() { };

/**
 * Associative arary mapping button group ids to
 * arrays of button element ids.
 */
EchoImageButton.Group.idToButtonArrayMap = new EchoCollectionsMap();

/**
 * Adds a button to a button group.
 *
 * @param groupId the id of the button group
 */
EchoImageButton.Group.add = function(groupId, buttonId) {
    var buttonArray = EchoImageButton.Group.idToButtonArrayMap.get(groupId);
    if (!buttonArray) {
        buttonArray = new Array();
        EchoImageButton.Group.idToButtonArrayMap.put(groupId, buttonArray);
    }
    buttonArray.push(buttonId);
};

EchoImageButton.Group.deselect = function(groupId) {
    var buttonArray = EchoImageButton.Group.idToButtonArrayMap.get(groupId);
    if (!buttonArray) {
        return;
    }
    for (var i = 0; i < buttonArray.length; ++i) {
        var elementId = buttonArray[i];
        var button = EchoImageButton.getComponent(elementId);
        if (button.selected) {
            button.setSelected(false);
        }
    }
};

EchoImageButton.Group.remove = function(groupId, buttonId) {
    // Obtain appropriate button group.
    var buttonArray = EchoImageButton.Group.idToButtonArrayMap.get(groupId);
    
    if (!buttonArray) {
        // No such button group exists.
        throw new Error("No such group: " + groupId);
    }

    // Find index of button in array.
    var arrayIndex = -1;
    for (var i = 0; i < buttonArray.length; ++i) {
        if (buttonArray[i] == buttonId) {
            arrayIndex = i;
            break;
        }
    }
    
    if (arrayIndex == -1) {
        // Button does not exist in group.
        throw new Error("No such button: " + buttonId + " in group: " + groupId);
    }
    
    if (buttonArray.length == 1) {
        // Array will now be empty, remove button group entirely.
        EchoImageButton.Group.idToButtonArrayMap.remove(groupId);
    } else {
        // Buttons remain, remove button from button group.
        buttonArray[arrayIndex] = buttonArray[buttonArray.length - 1];
        buttonArray.length = buttonArray.length - 1;
    }
};

/**
 * Static object/namespace for Button MessageProcessor 
 * implementation.
 */
EchoImageButton.MessageProcessor = function() { };

/**
 * MessageProcessor process() implementation 
 * (invoked by ServerMessage processor).
 *
 * @param messagePartElement the <code>message-part</code> element to process.
 */
EchoImageButton.MessageProcessor.process = function(messagePartElement) {
    for (var i = 0; i < messagePartElement.childNodes.length; ++i) {
        if (messagePartElement.childNodes[i].nodeType == 1) {
            switch (messagePartElement.childNodes[i].tagName) {
            case "init":
                EchoImageButton.MessageProcessor.processInit(messagePartElement.childNodes[i]);
                break;
            case "dispose":
                EchoImageButton.MessageProcessor.processDispose(messagePartElement.childNodes[i]);
                break;
            }
        }
    }
};

/**
 * Processes a <code>dispose</code> message to finalize the state of a
 * button component that is being removed.
 *
 * @param disposeMessageElement the <code>dispose</code> element to process
 */
EchoImageButton.MessageProcessor.processDispose = function(disposeMessageElement) {
    for (var item = disposeMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var button = EchoImageButton.getComponent(elementId);
        if (button) {
            button.dispose();
        }
    }
};
    
/**
 * Processes an <code>init</code> message to initialize the state of a 
 * button component that is being added.
 *
 * @param initMessageElement the <code>init</code> element to process
 */
EchoImageButton.MessageProcessor.processInit = function(initMessageElement) {
    var rolloverStyle = initMessageElement.getAttribute("rollover-style");
    var rolloverStyleCenter = initMessageElement.getAttribute("rollover-style-center");
    var rolloverStyleLeft = initMessageElement.getAttribute("rollover-style-left");
    var rolloverStyleRight = initMessageElement.getAttribute("rollover-style-right");
    var pressedStyle = initMessageElement.getAttribute("pressed-style");
    var pressedStyleCenter = initMessageElement.getAttribute("pressed-style-center");
    var pressedStyleLeft = initMessageElement.getAttribute("pressed-style-left");
    var pressedStyleRight = initMessageElement.getAttribute("pressed-style-right");   
    
    for (var item = initMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        
        var button = new EchoImageButton(elementId);
        
        if (rolloverStyle || rolloverStyleCenter) {
            button.rolloverStyle = rolloverStyle;
            button.rolloverStyleCenter = rolloverStyleCenter;
            button.rolloverStyleLeft = rolloverStyleLeft;
            button.rolloverStyleRight = rolloverStyleRight;
        }
        if (pressedStyle || rolloverStyleCenter) {
            button.pressedStyle = pressedStyle;
            button.pressedStyleCenter = pressedStyleCenter;
            button.pressedStyleLeft = pressedStyleLeft;
            button.pressedStyleRight = pressedStyleRight;          
        }
        button.enabled = item.getAttribute("enabled") != "false";
        button.serverNotify = item.getAttribute("server-notify") != "false";
        if (item.getAttribute("default-icon")) {
            button.defaultIcon = item.getAttribute("default-icon");
        }
        if (item.getAttribute("rollover-icon")) {
            button.rolloverIcon = item.getAttribute("rollover-icon");
        }
        if (item.getAttribute("pressed-icon")) {
            button.pressedIcon = item.getAttribute("pressed-icon");
        }
        
        if (item.getAttribute("toggle") == "true") {
            // ToggleButton-specific properties.
            button.toggle = true;
            button.selected = item.getAttribute("selected") == "true";
            button.stateIcon = item.getAttribute("state-icon");
            button.selectedStateIcon = item.getAttribute("selected-state-icon");
            if (item.getAttribute("group")) {
                button.groupId = item.getAttribute("group");
            }
            if (rolloverStyle) {
                button.rolloverStateIcon = item.getAttribute("rollover-state-icon"); 
                button.rolloverSelectedStateIcon = item.getAttribute("rollover-selected-state-icon"); 
            }
            if (pressedStyle) {
                button.pressedStateIcon = item.getAttribute("pressed-state-icon"); 
                button.pressedSelectedStateIcon = item.getAttribute("pressed-selected-state-icon"); 
            }
        }
        
        button.init();
    }
};
