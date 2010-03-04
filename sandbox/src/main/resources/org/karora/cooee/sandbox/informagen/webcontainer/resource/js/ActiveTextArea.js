/*
 *  ActiveTextArea is actually a proxy object for an HTML 'input'.  This object
 *    supports a good and bad icon as well as an error message.
 *
 *  The icon can be positioned on the left or right, while the error message can
 *    be positioned top, right, bottom or left. This position is controled in the
 *    Java class 'ActiveTextAreaPeer.java'.  The behavior is controlled with this
 *    JavaScript.
 *  
 */


/*
 *  Constructor
*/

sandbox_informagen_ActiveTextArea = function(elementId) {
    this.elementId = elementId;
};

/*
 *  Initialize:
 *      - wire up event listeners
*/


sandbox_informagen_ActiveTextArea.prototype.init = function() {

    var element = this.getElement();
    var textArea = this.getTextArea();
    
    textArea.style.color = this.foreground;
    textArea.style.backgroundColor = this.background;
    textArea.style.border = this.textAreaBorder;
    
    if (!this.enabled) 
        element.readOnly = true;

    
    if (this.text)
        textArea.value = this.text;

    this.valid = undefined;
    
    if (this.horizontalScroll != 0) 
        textArea.scrollLeft = this.horizontalScroll;
        
    
    if (this.verticalScroll != 0) {

        if (EchoClientProperties.get("quirkIERepaint")) {

            // Avoid IE quirk where browser will fail to set scroll bar position.

            var originalWidth = textArea.style.width;
            var temporaryWidth = parseInt(textArea.clientWidth) - 1;

            textArea.style.width = temporaryWidth + "px";
            textArea.style.width = originalWidth;
        }

        textArea.scrollTop = this.verticalScroll;
    }

    
    if (EchoClientProperties.get("quirkMozillaTextInputRepaint")) {

        // Avoid Mozilla quirk where text will be rendered outside of text field
        // (this appears to be a Mozilla bug).

        var noValue = !textArea.value;
        if (noValue) {
            textArea.value = "-";
        }

        var currentWidth = textArea.style.width;
        textArea.style.width = "20px";
        textArea.style.width = currentWidth;

        if (noValue) {
            textArea.value = "";
        }
    }
    
    
    // Event handlers: 'sandbox_informagen_ActiveTextArea.processXXX' refers to a static JS function
        
    EchoEventProcessor.addHandler(textArea, "blur",   "sandbox_informagen_ActiveTextArea.processBlur");
    EchoEventProcessor.addHandler(textArea, "focus",  "sandbox_informagen_ActiveTextArea.processFocus");
    EchoEventProcessor.addHandler(textArea, "keyup",  "sandbox_informagen_ActiveTextArea.processKeyUp");
    
    EchoDomUtil.addEventListener(textArea, "keypress", sandbox_informagen_ActiveTextArea.processKeyPress, false);
    
    EchoDomPropertyStore.setPropertyValue(textArea, "component", this);
    EchoDomPropertyStore.setPropertyValue(element, "component", this);


    if(this.text)
        this.checkTextLength();

};




/*
 *  Dispose - disconnects event handlers & listeners; removes it's
 *    properties, then deletes the element
*/


sandbox_informagen_ActiveTextArea.prototype.dispose = function() {

    var element = this.getElement();
    var textArea = this.getTextArea();
    

    EchoEventProcessor.removeHandler(textArea, "blur");
    EchoEventProcessor.removeHandler(textArea, "focus");
    EchoEventProcessor.removeHandler(textArea, "keyup");

    EchoDomUtil.removeEventListener(textArea, "keypress", sandbox_informagen_ActiveTextArea.processKeyPress, false);    

    // Remove any updates to text component that occurred during client/server transaction.

    EchoClientMessage.removePropertyElement(element.id, "text");
    
    EchoDomPropertyStore.dispose(textArea);
    EchoDomPropertyStore.dispose(element);
};


/*
 *  Element accessor
*/

sandbox_informagen_ActiveTextArea.prototype.getElement = function() {
    return document.getElementById(this.elementId);
};

sandbox_informagen_ActiveTextArea.prototype.getTextArea = function() {
    return document.getElementById(this.elementId + ".textarea");
};


/**
 * Processes a user "action request" on the text component i.e., the pressing
 * of the ENTER key when the the component is focused.
 * If any server-side <code>ActionListener</code>s are registered, an action
 * will be set in the ClientMessage and a client-server connection initiated.
 */

sandbox_informagen_ActiveTextArea.prototype.doAction = function() {

    if (!this.serverNotify) {
        return;
    }
    
    if (!this.enabled || !EchoClientEngine.verifyInput(this.getTextArea(), false)) {
        // Don't process actions when client/server transaction in progress.
        EchoDomUtil.preventEventDefault(echoEvent);
        return;
    }
    
    this.updateClientMessage();

    EchoClientMessage.setActionValue(this.elementId, "action");
    EchoServerTransaction.connect();
};


sandbox_informagen_ActiveTextArea.prototype.doValidationTransition = function() {

    if (!this.serverNotify) {
        return;
    }
    
    if (this.valid == undefined || this.valid == null) {
        return;
    }
    
    
    //this.updateClientMessage();

    EchoClientMessage.setActionValue(this.elementId, "validation-transition", (this.valid) ? "true" : "false");
    EchoServerTransaction.connect();
};



// Event listeners =============================================================
// Event listeners: 'ActiveTextArea.processXXX' refers to a JS function
//   defined as a static method. Each of these static method forward
//   the event to the component's event handler


// Static event handlers ===================================================================


/**
 * Processes a focus blur event:
 * Records the current state of the text field to the ClientMessage.
 * Delegates to data object method.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
 
sandbox_informagen_ActiveTextArea.processBlur = function(echoEvent) {
    var textComponent = sandbox_informagen_ActiveTextArea.getComponent(echoEvent.registeredTarget);
    textComponent.processBlur(echoEvent);
};

/**
 * Processes a focus event:
 * Notes focus state in ClientMessage.
 * Delegates to data object method.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
 
sandbox_informagen_ActiveTextArea.processFocus = function(echoEvent) {
    var textComponent = sandbox_informagen_ActiveTextArea.getComponent(echoEvent.registeredTarget);
    textComponent.processFocus(echoEvent);
};

/**
 * Processes a key press event:
 * Initiates an action in the event that the key pressed was the
 * ENTER key.
 * Delegates to data object method.
 *
 * @param e the DOM Level 2 event, if avaialable
 */
 
sandbox_informagen_ActiveTextArea.processKeyPress = function(e) {
    e = e ? e : window.event;
    var target = EchoDomUtil.getEventTarget(e);
    var textComponent = sandbox_informagen_ActiveTextArea.getComponent(target);
    textComponent.processKeyPress(e);
};

/**
 * Processes a key up event:
 * Records the current state of the text field to the ClientMessage.
 * Delegates to data object method.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
sandbox_informagen_ActiveTextArea.processKeyUp = function(echoEvent) {
    var textComponent = sandbox_informagen_ActiveTextArea.getComponent(echoEvent.registeredTarget);
    textComponent.processKeyUp(echoEvent);
};


// Prototype event handlers ===================================================================

/**
 * Processes a focus blur event:
 * Records the current state of the text field to the ClientMessage.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */

sandbox_informagen_ActiveTextArea.prototype.processBlur = function(echoEvent) {

    if (!this.enabled || !EchoClientEngine.verifyInput(this.getElement())) {
        return;
    }

    var textArea = this.getTextArea();
    
    // Return the background and border to 'normal'
    textArea.style.border = this.textAreaBorder;
    textArea.style.color = this.foreground;
    textArea.style.backgroundColor = this.background;

    // Do this check here because 'paste' operations don't fire any events
    // At least this way we get an update eventually
    this.checkTextLength();
    this.updateClientMessage();

    EchoFocusManager.setFocusedState(this.elementId, false);
};


/**
 * Processes a focus event:
 * Notes focus state in ClientMessage.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */

sandbox_informagen_ActiveTextArea.prototype.processFocus = function(echoEvent) {

    if (!this.enabled || !EchoClientEngine.verifyInput(this.getTextArea())) {
        return;
    }

    var textArea = this.getTextArea();
    
    if(this.topFocusBorder != null)
        textArea.style.borderTop = this.topFocusBorder;

    if(this.rightFocusBorder != null)
        textArea.style.borderRight = this.rightFocusBorder;
    
    if(this.bottomFocusBorder != null)
        textArea.style.borderBottom = this.bottomFocusBorder;
        
    if(this.leftFocusBorder != null)
        textArea.style.borderLeft = this.leftFocusBorder;

    if(this.focusForeground != null)
        textArea.style.color = this.focusForeground;

    if(this.focusBackground != null)
        textArea.style.backgroundColor = this.focusBackground;
    
    EchoFocusManager.setFocusedState(this.elementId + ".input", true);
};


/**
 * Processes a key press event:
 * Initiates an action in the event that the key pressed was the
 * ENTER key.
 *
 * @param e the DOM Level 2 event
 */

sandbox_informagen_ActiveTextArea.prototype.processKeyPress = function(e) {
    if (!this.enabled || !EchoClientEngine.verifyInput(this.getElement(), true)) {
        EchoDomUtil.preventEventDefault(e);
        return;
    }
    if (e.keyCode == 13) {
        ;//this.doAction();
    }
};

/**
 * Processes a key up event:
 * Records the current state of the text field to the ClientMessage.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
sandbox_informagen_ActiveTextArea.prototype.processKeyUp = function(echoEvent) {

    var textArea = this.getTextArea();
    
    if (!this.enabled || !EchoClientEngine.verifyInput(textArea, true)) {
        EchoDomUtil.preventEventDefault(echoEvent);
        return;
    }
    
    this.checkTextLength();
        
    this.updateClientMessage();
};

// End: Event Hanlders ========================================================================


sandbox_informagen_ActiveTextArea.prototype.checkTextLength = function () {

    var maximumLength = this.maximumLength;
    
    var textArea = this.getTextArea();
    var length = textArea.value.length;
    var lengthSpan = document.getElementById(this.elementId + ".length");

    lengthSpan.innerHTML = "Length: " + length;
    lengthSpan.style.display = (this.isMsgVisible) ? "" : "none";

    if(maximumLength == -1)
        return;
        
    var remainingSpan = document.getElementById(this.elementId + ".remaining");
    var overdrawnSpan = document.getElementById(this.elementId + ".overdrawn");
    
    var remaining = maximumLength - length;
    var overdrawn = (remaining < 0) ? -remaining : 0
    remaining = (remaining > 0) ? remaining : 0;
    
    remainingSpan.innerHTML = "Remaining: " + remaining;
    remainingSpan.style.display = (this.isMsgVisible) ? "" : "none";
    

    if(overdrawn > 0) {
        textArea.style.color = "#ff0000";
        textArea.style.background = "#ffff99";
        overdrawnSpan.innerHTML = "Over: " + overdrawn;
        overdrawnSpan.style.display = (this.isMsgVisible) ? "" : "none";
        
        if(this.valid == undefined || this.valid == true) {
            this.valid = false;
            this.doValidationTransition();
        }
        
    } else {
        overdrawnSpan.style.display = "none";
        textArea.style.color = this.foreground;
        textArea.style.background = this.background;
        
        if(this.valid == undefined || this.valid == false) {
            this.valid = true;
            this.doValidationTransition();
        }
    }

  };


/**
 * Updates the component state in the outgoing <code>ClientMessage</code>.
 */

sandbox_informagen_ActiveTextArea.prototype.updateClientMessage = function() {

    var textArea = this.getTextArea();
    
    var textPropertyElement = EchoClientMessage.createPropertyElement(this.elementId, "text");
    
    if (textPropertyElement.firstChild) {
        textPropertyElement.firstChild.nodeValue = textArea.value;
    } else {
        textPropertyElement.appendChild(EchoClientMessage.messageDocument.createTextNode(textArea.value));
    }
    
    EchoClientMessage.setPropertyValue(this.elementId, "horizontalScroll", textArea.scrollLeft);
    EchoClientMessage.setPropertyValue(this.elementId, "verticalScroll", textArea.scrollTop);
};


/**
 * Returns the TextComponent data object instance based on the root element id
 * of the TextComponent.
 *
 * @param element the root element or element id of the TextComponent
 * @return the relevant TextComponent instance
 */

sandbox_informagen_ActiveTextArea.getComponent = function(element) {
    return EchoDomPropertyStore.getPropertyValue(element, "component");
};



// MessageProcessor composition object and methods ============================================

/**
 * Static object/namespace for Text Component MessageProcessor 
 * implementation.
 */

sandbox_informagen_ActiveTextArea.MessageProcessor = function() { };


/**
 * MessageProcessor process() implementation 
 * (invoked by ServerMessage processor).
 *
 * @param messagePartElement the <code>message-part</code> element to process.
 */

sandbox_informagen_ActiveTextArea.MessageProcessor.process = function(messagePartElement) {

    for (var i = 0; i<messagePartElement.childNodes.length; ++i) {
    
        if (messagePartElement.childNodes[i].nodeType == 1) {  // Node.ELEMENT_NODE = 1
        
            switch (messagePartElement.childNodes[i].tagName) {
            case "init":
                sandbox_informagen_ActiveTextArea.MessageProcessor.processInit(messagePartElement.childNodes[i]);
                break;
                
            case "dispose":
                sandbox_informagen_ActiveTextArea.MessageProcessor.processDispose(messagePartElement.childNodes[i]);
                break;
                
            case "set-text":
                sandbox_informagen_ActiveTextArea.MessageProcessor.processSetText(messagePartElement.childNodes[i]);
                break;
                
            case "set-foreground":
                sandbox_informagen_ActiveTextArea.MessageProcessor.processSetForeground(messagePartElement.childNodes[i]);
                break;
                
            case "set-background":
                sandbox_informagen_ActiveTextArea.MessageProcessor.processSetBackground(messagePartElement.childNodes[i]);
                break;
                
            case "set-enabled":
                sandbox_informagen_ActiveTextArea.MessageProcessor.processSetEnabled(messagePartElement.childNodes[i]);
                break;
                
            }
        }
    }
};

/**
 * Processes a <code>dispose</code> message to finalize the state of a
 * Text Component that is being removed.
 *
 * @param disposeMessageElement the <code>dispose</code> element to process
 */

sandbox_informagen_ActiveTextArea.MessageProcessor.processDispose = function(disposeMessageElement) {
    for (var item = disposeMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var textComponent = sandbox_informagen_ActiveTextArea.getComponent(elementId);
        if (textComponent) {
            textComponent.dispose();
        }
    }
};


/**
 * Processes a <code>set-text</code> message to update the text displayed in a
 * Text Component.
 *
 * @param setTextMessageElement the <code>set-text</code> element to process
 */

sandbox_informagen_ActiveTextArea.MessageProcessor.processSetText = function(setTextMessageElement) {

    for (var item = setTextMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var text = item.getAttribute("text");
        var element = document.getElementById(elementId);

        var textComponent = sandbox_informagen_ActiveTextArea.getComponent(element);
        
        if(textComponent) {
            textComponent.getTextArea().value = text;
            textComponent.checkTextLength();   
        }
        
        // Remove any updates to text component that occurred during client/server transaction.
        EchoClientMessage.removePropertyElement(element.id, "text");
    }
};


/**
 * Processes an <code>init</code> message to initialize the state of a 
 * Text Component that is being added.
 *
 * @param initMessageElement the <code>init</code> element to process
 */

sandbox_informagen_ActiveTextArea.MessageProcessor.processInit = function(initMessageElement) {


    for (var item = initMessageElement.firstChild; item; item = item.nextSibling) {
    
        var elementId = item.getAttribute("eid");        
        var textComponent = new sandbox_informagen_ActiveTextArea(elementId);
        
        textComponent.enabled       = item.getAttribute("enabled") != "false";
        textComponent.text          = item.getAttribute("text") ? item.getAttribute("text") : "";

        textComponent.serverNotify  = item.getAttribute("server-notify") == "true";
        
        textComponent.horizontalScroll = item.getAttribute("horizontal-scroll") 
                                         ? parseInt(item.getAttribute("horizontal-scroll")) : 0;
        textComponent.verticalScroll = item.getAttribute("vertical-scroll") 
                                         ? parseInt(item.getAttribute("vertical-scroll")) : 0;

        textComponent.foreground = item.getAttribute("foreground") ? item.getAttribute("foreground") : "#000000";
        textComponent.background = item.getAttribute("background") ? item.getAttribute("background") : "#ffffff";

        // TextArea border
        textComponent.textAreaBorder = item.getAttribute("textarea-border") ? item.getAttribute("textarea-border") : "";

        // TextArea focus border and focus background
        textComponent.topFocusBorder = item.getAttribute("top-focus-border") ? item.getAttribute("top-focus-border") : null;
        textComponent.rightFocusBorder = item.getAttribute("right-focus-border") ? item.getAttribute("right-focus-border") : null;
        textComponent.bottomFocusBorder = item.getAttribute("bottom-focus-border") ? item.getAttribute("bottom-focus-border") : null;
        textComponent.leftFocusBorder = item.getAttribute("left-focus-border") ? item.getAttribute("left-focus-border") : null;
        
        textComponent.focusForeground = item.getAttribute("focus-foreground") ? item.getAttribute("focus-foreground") : textComponent.foreground;
        textComponent.focusBackground = item.getAttribute("focus-background") ? item.getAttribute("focus-background") : textComponent.background;
       

        // ActiveTextArea behavior values
        
        textComponent.isMsgVisible =  (item.getAttribute("msg-visible")  == "true") ? true : false;
        textComponent.maximumLength = item.getAttribute("maximum-length") ? item.getAttribute("maximum-length") : -1;
                
        textComponent.init();
    }
};



sandbox_informagen_ActiveTextArea.MessageProcessor.processSetForeground = function(setTextMessageElement) {

    for (var item = setTextMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var foreground = item.getAttribute("foreground");

        // Adjust the DOM
        var inputElement = document.getElementById(elementId + ".textarea");
        inputElement.style.color = foreground;

        // Save the setting in the component's object
        var componentObject = sandbox_informagen_ActiveTextArea.getComponent(inputElement);
            componentObject.foreground = foreground;
        
        // Remove any updates to text component that occurred during client/server transaction.
        EchoClientMessage.removePropertyElement(componentObject.id, "foreground");
    }
};



/**
 * Processes a <code>set-background</code> message to update the background color displayed in a
 * Text Component.
 *
 * @param setTextMessageElement the <code>set-background</code> DOM element to process
 */


sandbox_informagen_ActiveTextArea.MessageProcessor.processSetBackground = function(setTextMessageElement) {

    for (var item = setTextMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var background = item.getAttribute("background");

        // Adjust the DOM
        var inputElement = document.getElementById(elementId + ".textarea");
        inputElement.style.background = background;

        // Save the setting in the object
        var componentObject = sandbox_informagen_ActiveTextArea.getComponent(inputElement);
        componentObject.background = background;
        
        // Remove any updates to text component that occurred during client/server transaction.
        EchoClientMessage.removePropertyElement(componentObject.id, "background");
    }
};

/**
 * Processes a <code>set-enabled</code> message to update the enabled state of the 
 * Text Component.
 *
 * @param setTextMessageElement the <code>set-enabled</code> DOM element to process
 */


sandbox_informagen_ActiveTextArea.MessageProcessor.processSetEnabled = function(setTextMessageElement) {

    for (var item = setTextMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var enabled = item.getAttribute("enabled") != "false";

        // Adjust the HTML input element
        var inputElement = document.getElementById(elementId + ".textarea");
        inputElement.disabled = !enabled;

        // Save the new setting into the composite component
        var componentObject = sandbox_informagen_ActiveTextArea.getComponent(inputElement);
        componentObject.enabled = enabled;

        if(enabled == false) {
        
            var lengthSpan = document.getElementById(elementId + ".length");
            var remainingSpan = document.getElementById(elementId + ".remaining");
            var overdrawnSpan = document.getElementById(elementId + ".overdrawn");
            
            lengthSpan.style.display = "none";
            remainingSpan.style.display = "none";
            overdrawnSpan.style.display = "none";
        
        } else
            componentObject.checkTextLength();
               
                
        // Remove any updates to text component that occurred during client/server transaction.
        EchoClientMessage.removePropertyElement(componentObject.id, "enabled");
    }
};



