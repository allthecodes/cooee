/*
 *  ActiveTextField - A composite component made up of an HTML input field, a set of 
 *     small image icons which are used to show the valid/invalid state of the current input
 *     and a brief status message which can be used a hint to the user so that valid
 *     input is entered.
 *
 *  Superclass of:
 *
 *      - RegExTextField
 *      - IntegerTextField
 *      - NumericTextField
 *  
 */


/*
 *  Constructor
*/

sandbox_informagen_ActiveTextField = function() { };



/**
 * Returns the TextComponent data object instance based on the root element id
 * of the TextComponent.
 *
 * @param element the root element or element id of the TextComponent
 * @return the relevant TextComponent instance
 */

sandbox_informagen_ActiveTextField.getComponent = function(element) {
    return EchoDomPropertyStore.getPropertyValue(element, "component");
};


// Event listeners =============================================================
// Event listeners: '.processXXX' refers to a JS function
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
 
sandbox_informagen_ActiveTextField.processBlur = function(echoEvent) {
    var textComponent = sandbox_informagen_ActiveTextField.getComponent(echoEvent.registeredTarget);
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
 
sandbox_informagen_ActiveTextField.processFocus = function(echoEvent) {
    var textComponent = sandbox_informagen_ActiveTextField.getComponent(echoEvent.registeredTarget);
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
 
sandbox_informagen_ActiveTextField.processKeyPress = function(e) {
    e = e ? e : window.event;
    var target = EchoDomUtil.getEventTarget(e);
    var textComponent = sandbox_informagen_ActiveTextField.getComponent(target);
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
sandbox_informagen_ActiveTextField.processKeyUp = function(echoEvent) {
    var textComponent = sandbox_informagen_ActiveTextField.getComponent(echoEvent.registeredTarget);
    textComponent.processKeyUp(echoEvent);
};


sandbox_informagen_ActiveTextField.processChange = function(echoEvent) {
    var textComponent = sandbox_informagen_ActiveTextField.getComponent(echoEvent.registeredTarget);
    if (textComponent.actionCausedOnChange) 
        textComponent.processChange(echoEvent);
};


// MessageProcessor composition object and methods ============================================

/**
 * Static object/namespace for Text Component MessageProcessor 
 * implementation.
 */

sandbox_informagen_ActiveTextField.MessageProcessor = function() { };

/**
 * Processes a <code>dispose</code> message to finalize the state of a
 * Text Component that is being removed.
 *
 * @param disposeMessageElement the <code>dispose</code> element to process
 */

sandbox_informagen_ActiveTextField.MessageProcessor.processDispose = function(disposeMessageElement) {
    for (var item = disposeMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var textComponent = sandbox_informagen_ActiveTextField.getComponent(elementId);
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

sandbox_informagen_ActiveTextField.MessageProcessor.processSetText = function(setTextMessageElement) {

    for (var item = setTextMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var text = item.getAttribute("text");
        var element = document.getElementById(elementId);
        var textComponent = sandbox_informagen_ActiveTextField.getComponent(element);

        if(textComponent) {
            textComponent.getInput().value = text;
            textComponent.validateText();
        }
                   
        EchoClientMessage.removePropertyElement(elementId, "text");
    }
};


sandbox_informagen_ActiveTextField.MessageProcessor.processSetForeground = function(setTextMessageElement) {

    for (var item = setTextMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var foreground = item.getAttribute("foreground");

        // Adjust the DOM
        var inputElement = document.getElementById(elementId + ".input");
        inputElement.style.color = foreground;

        // Save the setting in the object
        var componentObject = sandbox_informagen_ActiveTextField.getComponent(inputElement);
        
        if(componentObject.focusForeground == componentObject.foreground)
            componentObject.focusForeground = foreground; 
        
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


sandbox_informagen_ActiveTextField.MessageProcessor.processSetBackground = function(setTextMessageElement) {

    for (var item = setTextMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var background = item.getAttribute("background");

        // Adjust the DOM
        var inputElement = document.getElementById(elementId + ".input");
        inputElement.style.background = background;

        // Save the setting in the object
        var componentObject = sandbox_informagen_ActiveTextField.getComponent(inputElement);
        
        if(componentObject.focusBackground == componentObject.background)
            componentObject.focusBackground = background; 
            
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


sandbox_informagen_ActiveTextField.MessageProcessor.processSetEnabled = function(setTextMessageElement) {

    for (var item = setTextMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var enabled = item.getAttribute("enabled") != "false";

        // Adjust the HTML input element
        var inputElement = document.getElementById(elementId + ".input");
        inputElement.disabled = !enabled;

        // Save the new setting into the composite component
        var componentObject = sandbox_informagen_ActiveTextField.getComponent(inputElement);
        componentObject.enabled = enabled;

        if(enabled == false) {
        
            var warningIcon = document.getElementById(elementId + ".warning");
            var goodIcon = document.getElementById(elementId + ".good");
            var errorIcon = document.getElementById(elementId + ".error");
            var messageSpan = document.getElementById(elementId + ".msg");
        
            warningIcon.style.display = "none";
            goodIcon.style.display = "none";
            errorIcon.style.display = "none";
            messageSpan.style.display = "none";

            inputElement.style.color = componentObject.disabledForeground;
            inputElement.style.backgroundColor = componentObject.disabledBackground;
        
        } else
            componentObject.validateText();
               
                
        // Remove any updates to text component that occurred during client/server transaction.
        EchoClientMessage.removePropertyElement(componentObject.id, "enabled");
    }
};

