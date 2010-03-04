/*
 *  NumericTextField is actually a proxy object for an HTML 'input'.  This object
 *    supports a good and bad icon as well as an error message.
 *
 *  The icon can be positioned on the left or right, while the error message can
 *    be positioned top, right, bottom or left. This position is controled in the
 *    Java class 'IntegerTextFieldPeer.java'.  The behavior is controlled with this
 *    JavaScript.
 *  
 */


/*
 *  Constructor
 */

sandbox_informagen_NumericTextField = function(elementId) {
    this.elementId = elementId;
};



/*
 *  Initialize:
 *      - wire up event listeners
*/

sandbox_informagen_NumericTextField.prototype.init = function() {

    var element = this.getElement();
    var input = this.getInput();
    
    if (!this.enabled) {
        element.readOnly = true;
    }
    
    input.style.color = this.foreground;
    input.style.backgroundColor = this.background;
    input.style.border = this.inputBorder;

    var infocus = false;

    this.valid = undefined;
    
    if (this.text) {
        input.value = this.text;
    } else
        input.value="";

    var warningIcon = document.getElementById(this.elementId + ".warning");
    var messageSpan = document.getElementById(this.elementId + ".msg");

    if(this.required) {
        warningIcon.style.display =  "";
        messageSpan.innerHTML = this.requiredMsg;
        messageSpan.style.display = "";
    }


    if (this.horizontalScroll != 0) {
        input.scrollLeft = this.horizontalScroll;
    }

    this.regexp = new RegExp("^-?[0-9]*\\.?[0-9]*$");


    if((this.statusMsg != null) && (this.required == false)) {
        messageSpan.innerHTML = this.statusMsg;
    }
        
    
    if (this.verticalScroll != 0) {

        if (EchoClientProperties.get("quirkIERepaint")) {

            // Avoid IE quirk where browser will fail to set scroll bar position.

            var originalWidth = element.style.width;
            var temporaryWidth = parseInt(element.clientWidth) - 1;

            input.style.width = temporaryWidth + "px";
            input.style.width = originalWidth;
        }

        input.scrollTop = this.verticalScroll;
    }

    
    if (EchoClientProperties.get("quirkMozillaTextInputRepaint")) {

        // Avoid Mozilla quirk where text will be rendered outside of text field
        // (this appears to be a Mozilla bug).

        var noValue = !input.value;
        if (noValue) {
            input.value = "-";
        }

        var currentWidth = input.style.width;
        input.style.width = "20px";
        input.style.width = currentWidth;

        if (noValue) {
            input.value = "";
        }
    }

    // Event handlers: 'sandbox_informagen_ActiveTextField.processXXX' refers to a static JS function
    
    EchoEventProcessor.addHandler(input, "blur", "sandbox_informagen_ActiveTextField.processBlur");
    EchoEventProcessor.addHandler(input, "focus", "sandbox_informagen_ActiveTextField.processFocus");
    EchoEventProcessor.addHandler(input, "keyup", "sandbox_informagen_ActiveTextField.processKeyUp");
	EchoEventProcessor.addHandler(input, "change", "sandbox_informagen_ActiveTextField.processChange");
    
    //EchoDomUtil.addEventListener(input, "keypress", sandbox_informagen_ActiveTextField.processKeyPress, false);
    
    EchoDomPropertyStore.setPropertyValue(input, "component", this);
    EchoDomPropertyStore.setPropertyValue(element, "component", this);


    if(this.text)
        this.validateText();

};




/*
 *  Dispose - disconnects event handlers & listeners; removes it's
 *    properties, then deletes the element
*/


sandbox_informagen_NumericTextField.prototype.dispose = function() {

    var element = this.getElement();
    var input = this.getInput();
    
    EchoEventProcessor.removeHandler(input, "blur");
    EchoEventProcessor.removeHandler(input, "focus");
    EchoEventProcessor.removeHandler(input, "keyup");
	EchoEventProcessor.removeHandler(input, "change");

    //EchoDomUtil.removeEventListener(input, "keypress", sandbox_informagen_ActiveTextField.processKeyPress, false);    

    // Remove any updates to text component that occurred during client/server transaction.

    EchoClientMessage.removePropertyElement(element.id, "text");
    
    EchoDomPropertyStore.dispose(input);
    EchoDomPropertyStore.dispose(element);
};


/*
 *  Element accessor
*/

sandbox_informagen_NumericTextField.prototype.getElement = function() {
    return document.getElementById(this.elementId);
};

sandbox_informagen_NumericTextField.prototype.getInput = function() {
    return document.getElementById(this.elementId + ".input");
};


/**
 * Processes a user "action request" on the text component i.e., the pressing
 * of the ENTER key when the the component is focused.
 * If any server-side <code>ActionListener</code>s are registered, an action
 * will be set in the ClientMessage and a client-server connection initiated.
 */

sandbox_informagen_NumericTextField.prototype.doAction = function() {

    if (!this.serverNotify) {
        return;
    }
    
    if (!this.enabled || !EchoClientEngine.verifyInput(this.getInput(), false)) {
        // Don't process actions when client/server transaction in progress.
        EchoDomUtil.preventEventDefault(echoEvent);
        return;
    }
    
    this.updateClientMessage();

    EchoClientMessage.setActionValue(this.elementId, "action");
    EchoServerTransaction.connect();
};



sandbox_informagen_NumericTextField.prototype.doValidationTransition = function() {

    if (!this.serverNotify) {
        return;
    }
    
    if (this.valid == undefined || this.valid == null) {
        return;
    }
    
    
    //this.updateClientMessage();

    //EchoClientMessage.setActionValue(this.elementId, "validation-transition", (this.valid) ? "true" : "false");
    //EchoServerTransaction.connect();
};





// Prototype Event Handlers ===================================================================


/**
 * Processes a focus blur event:
 * Records the current state of the text field to the ClientMessage.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */

sandbox_informagen_NumericTextField.prototype.processBlur = function(echoEvent) {

//     if (!this.enabled || !EchoClientEngine.verifyInput(this.getElement())) {
//         return;
//     }
    var input = this.getInput();
    
    // Return the background and border to 'normal'
    input.style.border = this.inputBorder;
    input.style.color = this.foreground;
    input.style.backgroundColor = this.background;

    // Do this check here because 'paste' operations don't fire any events
    // At least this way we get an update eventually

    this.infocus = false;
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

sandbox_informagen_NumericTextField.prototype.processFocus = function(echoEvent) {

//     if (!this.enabled || !EchoClientEngine.verifyInput(this.getInput())) {
//         return;
//     }
    
    var input = this.getInput();
    this.infocus = true;
    
    if(this.topFocusBorder != null)
        input.style.borderTop = this.topFocusBorder;

    if(this.rightFocusBorder != null)
        input.style.borderRight = this.rightFocusBorder;
    
    if(this.bottomFocusBorder != null)
        input.style.borderBottom = this.bottomFocusBorder;
        
    if(this.leftFocusBorder != null)
        input.style.borderLeft = this.leftFocusBorder;

    if(this.focusForeground != null)
        input.style.color = this.focusForeground;

    if(this.focusBackground != null)
        input.style.backgroundColor = this.focusBackground;

    
    EchoFocusManager.setFocusedState(this.elementId + ".input", true);
};


/**
 * Processes a key press event:
 * Initiates an action in the event that the key pressed was the
 * ENTER key.
 *
 * @param e the DOM Level 2 event
 */

sandbox_informagen_NumericTextField.prototype.processKeyPress = function(e) {
//     if (!this.enabled || !EchoClientEngine.verifyInput(this.getElement(), true)) {
//         EchoDomUtil.preventEventDefault(e);
//         return;
//     }
//     if (e.keyCode == 13) {
//         this.doAction();
//     }
};

/**
 * Processes a key up event:
 * Records the current state of the text field to the ClientMessage.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
sandbox_informagen_NumericTextField.prototype.processKeyUp = function(echoEvent) {

    var input = this.getInput();
    
    if (!this.enabled || !EchoClientEngine.verifyInput(input, true)) {
        EchoDomUtil.preventEventDefault(echoEvent);
        return;
    }
    
    this.validateText();
    
    if (this.maximumLength >= 0) {
        if (input.value && input.value.length > this.maximumLength) {
            input.value = input.value.substring(0, this.maximumLength);
        }
    }
    
    this.updateClientMessage();
};


sandbox_informagen_NumericTextField.prototype.processChange = function(echoEvent) {

    // Don't report 'change' events to the server
    if(this.actionCausedOnChange == false)
        return;

    // Value have not been validated
    if (this.valid == undefined || this.valid == null) {
        return;
    }


    if (!this.enabled || !EchoClientEngine.verifyInput(this.getElement(), true)) {
        EchoDomUtil.preventEventDefault(echoEvent);
        return;
    }

    
    if(this.valid == true)
        this.doAction(echoEvent);
};

// End: Event Hanlders ========================================================================


sandbox_informagen_NumericTextField.prototype.validateText = function () {

    var input = this.getInput();
      
    if (this.regexp != null) {
    
      var newval = input.value;
      var cleanval = "";
      
      for (i = 0; i < newval.length; i++) {
        if (this.regexp.test(cleanval + newval.substr(i,1))) {
          cleanval = cleanval + newval.substr(i,1);
        }
      }
      input.value = cleanval;
    }

    this.validateRange();
};


sandbox_informagen_NumericTextField.prototype.validateRange = function () {

    var input = this.getInput();
    var newval = input.value;

    if(newval == "-")
        newval = -1;

    var warningIcon = document.getElementById(this.elementId + ".warning");
    var goodIcon = document.getElementById(this.elementId + ".good");
    var errorIcon = document.getElementById(this.elementId + ".error");
    var messageSpan = document.getElementById(this.elementId + ".msg");

    if(newval.length == 0) {
        input.style.color = (this.infocus == true) ? this.focusForeground :this.foreground;
        input.style.backgroundColor = (this.infocus == true) ? this.focusBackground : this.background;
        goodIcon.style.display = (this.isIconAlwaysVisible) ? "" : "none";
        errorIcon.style.display =  "none";
        if(this.required) {
            errorIcon.style.display = "none";
            goodIcon.style.display = "none";
            warningIcon.style.display =  "";
            messageSpan.innerHTML = this.requiredMsg;
            messageSpan.style.display =  "";
        } else
            messageSpan.style.display = (this.isMessageAlwaysVisible) ? "" : "none";

        return;
    }

    newval = parseFloat(newval); 
 
    // First condition means value is in range
    if((newval >= this.minimumValue) && (newval <= this.maximumValue)) {
        input.style.color = (this.infocus == true) ? this.focusForeground :this.foreground;
        input.style.backgroundColor = (this.infocus == true) ? this.focusBackground : this.background;
        goodIcon.style.display = (this.isIconVisible) ? "" : "none";
        errorIcon.style.display = "none";
        warningIcon.style.display = "none";
        messageSpan.innerHTML = this.statusMsg;
        messageSpan.style.display = (this.isMessageAlwaysVisible) ? "" : "none";
        
        if(this.valid == undefined || this.valid == false) {
            this.valid = true;
            this.doValidationTransition();
        }
        
    } else {
        input.style.color = this.errorForeground;
        input.style.backgroundColor = this.errorBackground;
        goodIcon.style.display = "none";
        errorIcon.style.display = (this.isIconVisible) ? "" : "none";
        warningIcon.style.display = "none";
        messageSpan.innerHTML = this.statusMsg;
        messageSpan.style.display = (this.isMessageAlwaysVisible || this.isMsgVisible) ? "" : "none";
        
        if(this.valid == undefined || this.valid == true) {
            this.valid = false;
            this.doValidationTransition();
        }
    }
    
  };


/**
 * Updates the component state in the outgoing <code>ClientMessage</code>.
 */

sandbox_informagen_NumericTextField.prototype.updateClientMessage = function() {
    var input = this.getInput();
    var textPropertyElement = EchoClientMessage.createPropertyElement(this.elementId, "text");
    
    if (textPropertyElement.firstChild) {
        textPropertyElement.firstChild.nodeValue = input.value;
    } else {
        textPropertyElement.appendChild(EchoClientMessage.messageDocument.createTextNode(input.value));
    }
    
    //EchoClientMessage.setPropertyValue(this.elementId, "horizontalScroll", element.scrollLeft);
    //EchoClientMessage.setPropertyValue(this.elementId, "verticalScroll", element.scrollTop);
};

///////////////////////////////////////////////////////////////////////////////////////////////
// MessageProcessor composition object and methods
//
//
// Static object/namespace for Text Component MessageProcessor implementation.
//
//

/*
 *  Constructor
 */

sandbox_informagen_NumericTextField.MessageProcessor = function() { };


/**
 * MessageProcessor process() implementation 
 * (invoked by ServerMessage processor).
 *
 * @param messagePartElement the <code>message-part</code> element to process.
 */

sandbox_informagen_NumericTextField.MessageProcessor.process = function(messagePartElement) {

    for (var i = 0; i<messagePartElement.childNodes.length; ++i) {
    
        if (messagePartElement.childNodes[i].nodeType == 1) {  // Node.ELEMENT_NODE = 1
        
            switch (messagePartElement.childNodes[i].tagName) {
            case "init":
                sandbox_informagen_NumericTextField.MessageProcessor.processInit(messagePartElement.childNodes[i]);
                break;
                
            case "dispose":
                sandbox_informagen_ActiveTextField.MessageProcessor.processDispose(messagePartElement.childNodes[i]);
                break;
                
            case "set-text":
                sandbox_informagen_ActiveTextField.MessageProcessor.processSetText(messagePartElement.childNodes[i]);
                break;
                
            case "set-foreground":
                sandbox_informagen_ActiveTextField.MessageProcessor.processSetForeground(messagePartElement.childNodes[i]);
                break;
                
            case "set-background":
                sandbox_informagen_ActiveTextField.MessageProcessor.processSetBackground(messagePartElement.childNodes[i]);
                break;
                
            case "set-enabled":
                sandbox_informagen_ActiveTextField.MessageProcessor.processSetEnabled(messagePartElement.childNodes[i]);
                break;
                
            }
        }
    }
};



/**
 * Processes an <code>init</code> message to initialize the state of a 
 * Text Component that is being added.
 *
 * @param initMessageElement the <code>init</code> element to process
 */

sandbox_informagen_NumericTextField.MessageProcessor.processInit = function(initMessageElement) {

    for (var item = initMessageElement.firstChild; item; item = item.nextSibling) {
    
        var elementId = item.getAttribute("eid");        
        var textComponent = new sandbox_informagen_NumericTextField(elementId);
        
        textComponent.enabled       = item.getAttribute("enabled") != "false";
        textComponent.text          = item.getAttribute("text") ? item.getAttribute("text") : "";
 
        textComponent.serverNotify  = item.getAttribute("server-notify") == "true";
        textComponent.maximumLength = item.getAttribute("maximum-length") ? item.getAttribute("maximum-length") : -1;
        
        textComponent.horizontalScroll = item.getAttribute("horizontal-scroll") 
                                         ? parseInt(item.getAttribute("horizontal-scroll")) : 0;
        textComponent.verticalScroll = item.getAttribute("vertical-scroll") 
                                         ? parseInt(item.getAttribute("vertical-scroll")) : 0;
        
        textComponent.isIconVisible = (item.getAttribute("icon-visible") == "true") ? true : false;
        textComponent.isMsgVisible =  (item.getAttribute("msg-visible")  == "true") ? true : false;
        textComponent.isMessageAlwaysVisible = (item.getAttribute("msg-always-visible") == "true") ? true : false;
        textComponent.isIconAlwaysVisible = (item.getAttribute("icon-always-visible") == "true") ? true : false;

        textComponent.statusMsg = item.getAttribute("status-msg");

        textComponent.required = (item.getAttribute("required") == "true") ? true : false;
        textComponent.requiredMsg = item.getAttribute("required-msg") ? item.getAttribute("required-msg") : "Required";

        textComponent.foreground = item.getAttribute("foreground") ? item.getAttribute("foreground") : "";
        textComponent.background = item.getAttribute("background") ? item.getAttribute("background") : "";

        textComponent.disabledForeground = item.getAttribute("disabled-foreground") ? item.getAttribute("disabled-foreground") : "";
        textComponent.disabledBackground = item.getAttribute("disabled-background") ? item.getAttribute("disabled-background") : "";

        textComponent.errorForeground = item.getAttribute("error-foreground") ? item.getAttribute("error-foreground") : "#ff0000";
        textComponent.errorBackground = item.getAttribute("error-background") ? item.getAttribute("error-background") : "#ffff99";

        // TextField border
        textComponent.inputBorder = item.getAttribute("input-border") ? item.getAttribute("input-border") : "";

        // TextField focus border and focus background
        textComponent.topFocusBorder = item.getAttribute("top-focus-border") ? item.getAttribute("top-focus-border") : null;
        textComponent.rightFocusBorder = item.getAttribute("right-focus-border") ? item.getAttribute("right-focus-border") : null;
        textComponent.bottomFocusBorder = item.getAttribute("bottom-focus-border") ? item.getAttribute("bottom-focus-border") : null;
        textComponent.leftFocusBorder = item.getAttribute("left-focus-border") ? item.getAttribute("left-focus-border") : null;

        textComponent.focusForeground = item.getAttribute("focus-foreground") ? item.getAttribute("focus-foreground") : textComponent.foreground;
        textComponent.focusBackground = item.getAttribute("focus-background") ? item.getAttribute("focus-background") : textComponent.background;

        // Respond to change event
        textComponent.actionCausedOnChange 	= (item.getAttribute("actionCausedOnChange") == "true") ? true : false;
    
        // NumericTextField specific values
        textComponent.minimumValue = item.getAttribute("minimum-value") ? item.getAttribute("minimum-value") : Number.MIN_VALUE;
        textComponent.maximumValue = item.getAttribute("maximum-value") ? item.getAttribute("maximum-value") : Number.MAX_VALUE;
                
        textComponent.init();
    }
};


