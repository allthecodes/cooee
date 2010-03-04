
EchoLiveTextField = function(elementId) {
    this.elementId = elementId;
};

EchoLiveTextField.prototype.dispose = function() {
    var element = this.getElement();
    EchoEventProcessor.removeHandler(element, "blur");
    EchoEventProcessor.removeHandler(element, "focus");
    EchoEventProcessor.removeHandler(element, "keyup");
    EchoDomUtil.removeEventListener(element, "keypress", EchoLiveTextField.processKeyPress, false);    

    // Remove any updates to text component that occurred during client/server transaction.
    EchoClientMessage.removePropertyElement(element.id, "text");
    
    EchoDomPropertyStore.dispose(element);
};

/**
 * Processes a user "action request" on the text component i.e., the pressing
 * of the ENTER key when the the component is focused.
 * If any server-side <code>ActionListener</code>s are registered, an action
 * will be set in the ClientMessage and a client-server connection initiated.
 */
EchoLiveTextField.prototype.doAction = function() {
    if (!this.serverNotify) {
        return;
    }
    
    if (!this.enabled || !EchoClientEngine.verifyInput(this.getElement(), false)) {
        // Don't process actions when client/server transaction in progress.
        EchoDomUtil.preventEventDefault(echoEvent);
        return;
    }
    
    this.updateClientMessage();
    EchoClientMessage.setActionValue(this.elementId, "action");
    EchoServerTransaction.connect();
};

EchoLiveTextField.prototype.getElement = function() {
    return document.getElementById(this.elementId);
};

EchoLiveTextField.prototype.init = function() {
    var element = this.getElement();
    
    if (!this.enabled) {
        element.readOnly = true;
    }
    if (this.text) {
        element.value = this.text;
    }

    if (this.horizontalScroll != 0) {
        element.scrollLeft = this.horizontalScroll;
    }
    
    if (this.verticalScroll != 0) {
        if (EchoClientProperties.get("quirkIERepaint")) {
            // Avoid IE quirk where browser will fail to set scroll bar position.
            var originalWidth = element.style.width;
            var temporaryWidth = parseInt(element.clientWidth) - 1;
            element.style.width = temporaryWidth + "px";
            element.style.width = originalWidth;
        }
        element.scrollTop = this.verticalScroll;
    }
    
    if (EchoClientProperties.get("quirkMozillaTextInputRepaint")) {
        // Avoid Mozilla quirk where text will be rendered outside of text field
        // (this appears to be a Mozilla bug).
        var noValue = !element.value;
        if (noValue) {
            element.value = "-";
        }
        var currentWidth = element.style.width;
        element.style.width = "20px";
        element.style.width = currentWidth;
        if (noValue) {
            element.value = "";
        }
    }
    
    EchoEventProcessor.addHandler(element, "blur", "EchoLiveTextField.processBlur");
    EchoEventProcessor.addHandler(element, "focus", "EchoLiveTextField.processFocus");
    EchoEventProcessor.addHandler(element, "keyup", "EchoLiveTextField.processKeyUp");
    
    EchoDomUtil.addEventListener(element, "keypress", EchoLiveTextField.processKeyPress, false);
    
    EchoDomPropertyStore.setPropertyValue(element, "component", this);
};

/**
 * Processes a focus blur event:
 * Records the current state of the text field to the ClientMessage.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoLiveTextField.prototype.processBlur = function(echoEvent) {
    if (!this.enabled || !EchoClientEngine.verifyInput(this.getElement())) {
        return;
    }
    this.validateText();
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
EchoLiveTextField.prototype.processFocus = function(echoEvent) {
    if (!this.enabled || !EchoClientEngine.verifyInput(this.getElement())) {
        return;
    }
    
    EchoFocusManager.setFocusedState(this.elementId, true);
};

/**
 * Processes a key press event:
 * Initiates an action in the event that the key pressed was the
 * ENTER key.
 *
 * @param e the DOM Level 2 event
 */
EchoLiveTextField.prototype.processKeyPress = function(e) {
    if (!this.enabled || !EchoClientEngine.verifyInput(this.getElement(), true)) {
        EchoDomUtil.preventEventDefault(e);
        return;
    }
    if (e.keyCode == 13) {
        this.doAction();
    }
};

/**
 * Processes a key up event:
 * Records the current state of the text field to the ClientMessage.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoLiveTextField.prototype.processKeyUp = function(echoEvent) {
    var element = this.getElement();
    if (!this.enabled || !EchoClientEngine.verifyInput(element, true)) {
        EchoDomUtil.preventEventDefault(echoEvent);
        return;
    }
    this.validateText();
    if (this.maximumLength >= 0) {
        if (element.value && element.value.length > this.maximumLength) {
            element.value = element.value.substring(0, this.maximumLength);
        }
    }
    
    this.updateClientMessage();
};

EchoLiveTextField.prototype.validateText = function ()
  {
    if (this.regexp != null)
    {
      var element = this.getElement();
      var newval = element.value;
      var cleanval = "";
      
      
      for (i = 0; i < newval.length; i++)
      {
        if (this.regexp.test(cleanval + newval.substr(i,1)))
        {
          cleanval = cleanval + newval.substr(i,1);
        }
      }
      element.value = cleanval;
    }
  };

/**
 * Updates the component state in the outgoing <code>ClientMessage</code>.
 */
EchoLiveTextField.prototype.updateClientMessage = function() {
    var element = this.getElement();
    var textPropertyElement = EchoClientMessage.createPropertyElement(this.elementId, "text");
    
    if (textPropertyElement.firstChild) {
        textPropertyElement.firstChild.nodeValue = element.value;
    } else {
        textPropertyElement.appendChild(EchoClientMessage.messageDocument.createTextNode(element.value));
    }
    
    EchoClientMessage.setPropertyValue(this.elementId, "horizontalScroll", element.scrollLeft);
    EchoClientMessage.setPropertyValue(this.elementId, "verticalScroll", element.scrollTop);
};

/**
 * Returns the TextComponent data object instance based on the root element id
 * of the TextComponent.
 *
 * @param element the root element or element id of the TextComponent
 * @return the relevant TextComponent instance
 */
EchoLiveTextField.getComponent = function(element) {
    return EchoDomPropertyStore.getPropertyValue(element, "component");
};

/**
 * Static object/namespace for Text Component MessageProcessor 
 * implementation.
 */
EchoLiveTextField.MessageProcessor = function() { };

/**
 * MessageProcessor process() implementation 
 * (invoked by ServerMessage processor).
 *
 * @param messagePartElement the <code>message-part</code> element to process.
 */
EchoLiveTextField.MessageProcessor.process = function(messagePartElement) {
    for (var i = 0; i < messagePartElement.childNodes.length; ++i) {
        if (messagePartElement.childNodes[i].nodeType == 1) {
            switch (messagePartElement.childNodes[i].tagName) {
            case "init":
                EchoLiveTextField.MessageProcessor.processInit(messagePartElement.childNodes[i]);
                break;
            case "dispose":
                EchoLiveTextField.MessageProcessor.processDispose(messagePartElement.childNodes[i]);
                break;
            case "set-text":
                EchoLiveTextField.MessageProcessor.processSetText(messagePartElement.childNodes[i]);
                break;
            case "set-regexp":
                EchoLiveTextField.MessageProcessor.processSetRegExp(messagePartElement.childNodes[i]);
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
EchoLiveTextField.MessageProcessor.processDispose = function(disposeMessageElement) {
    for (var item = disposeMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var textComponent = EchoLiveTextField.getComponent(elementId);
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
EchoLiveTextField.MessageProcessor.processSetText = function(setTextMessageElement) {

    for (var item = setTextMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var text = item.getAttribute("text");
        var textComponent = document.getElementById(elementId);
        textComponent.value = text;
        
        // Remove any updates to text component that occurred during client/server transaction.
        EchoClientMessage.removePropertyElement(textComponent.id, "text");
    }
};

EchoLiveTextField.MessageProcessor.processSetRegExp = function(setTextMessageElement) {

    for (var item = setTextMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var text = item.getAttribute("regexp");
        var textComponent = EchoLiveTextField.getComponent(elementId);
        textComponent.regexp = new RegExp(text);
        
        
    }
};

/**
 * Processes an <code>init</code> message to initialize the state of a 
 * Text Component that is being added.
 *
 * @param initMessageElement the <code>init</code> element to process
 */
EchoLiveTextField.MessageProcessor.processInit = function(initMessageElement) {
    for (var item = initMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        
        var textComponent = new EchoLiveTextField(elementId);
        textComponent.enabled = item.getAttribute("enabled") != "false";
        textComponent.text =  item.getAttribute("text") ? item.getAttribute("text") : null;
        textComponent.serverNotify = item.getAttribute("server-notify") == "true";
        textComponent.maximumLength = item.getAttribute("maximum-length") ? item.getAttribute("maximum-length") : -1;
        textComponent.regexp = item.getAttribute("regexp") ? new RegExp(item.getAttribute("regexp")) : null;
        textComponent.horizontalScroll = item.getAttribute("horizontal-scroll") 
                ? parseInt(item.getAttribute("horizontal-scroll")) : 0;
        textComponent.verticalScroll = item.getAttribute("vertical-scroll") 
                ? parseInt(item.getAttribute("vertical-scroll")) : 0;
                
        textComponent.init();
    }
};

/**
 * Processes a focus blur event:
 * Records the current state of the text field to the ClientMessage.
 * Delegates to data object method.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoLiveTextField.processBlur = function(echoEvent) {
    var textComponent = EchoLiveTextField.getComponent(echoEvent.registeredTarget);
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
EchoLiveTextField.processFocus = function(echoEvent) {
    var textComponent = EchoLiveTextField.getComponent(echoEvent.registeredTarget);
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
EchoLiveTextField.processKeyPress = function(e) {
    e = e ? e : window.event;
    var target = EchoDomUtil.getEventTarget(e);
    var textComponent = EchoLiveTextField.getComponent(target);
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
EchoLiveTextField.processKeyUp = function(echoEvent) {
    var textComponent = EchoLiveTextField.getComponent(echoEvent.registeredTarget);
    textComponent.processKeyUp(echoEvent);
};
