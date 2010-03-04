
//_______________________
// Object EchoOverlay

/**
 * Static object/namespace for Overlay support.
 * This object/namespace should not be used externally.
 */
EchoOverlay = function(elementId) {
	
	this.element = document.getElementById(elementId);
};

EchoOverlay.getComponent = function(element) {
    return EchoDomPropertyStore.getPropertyValue(element, "component");
};



EchoOverlay.processClick = function(echoEvent) {
        
    if (!EchoClientEngine.verifyInput(echoEvent.registeredTarget)) {
    	return;
    }
    var overlay = EchoOverlay.getComponent(echoEvent.registeredTarget);

	if (!overlay.serverNotify) {
		return;
    }
    
    if (document.selection && document.selection.empty) {
        document.selection.empty();
    }
    
    EchoClientMessage.setActionValue(overlay.element.id, "click");
    
    EchoServerTransaction.connect();
};


/**
 * Processes a scrollbar adjustment event.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoOverlay.processScroll = function(echoEvent) {
    if (!EchoClientEngine.verifyInput(echoEvent.registeredTarget)) {
        return;
    }
    EchoClientMessage.setPropertyValue(echoEvent.registeredTarget.id, "horizontalScroll",  
            echoEvent.registeredTarget.scrollLeft + "px");
    EchoClientMessage.setPropertyValue(echoEvent.registeredTarget.id, "verticalScroll",  
            echoEvent.registeredTarget.scrollTop + "px");
};

/**
 * Static object/namespace for Overlay MessageProcessor 
 * implementation.
 */
EchoOverlay.MessageProcessor = function() { };

/**
 * MessageProcessor process() implementation 
 * (invoked by ServerMessage processor).
 *
 * @param messagePartElement the <code>message-part</code> element to process.
 */
EchoOverlay.MessageProcessor.process = function(messagePartElement) {
    for (var i = 0; i < messagePartElement.childNodes.length; ++i) {
        if (messagePartElement.childNodes[i].nodeType == 1) {
            switch (messagePartElement.childNodes[i].tagName) {
            case "init":
                EchoOverlay.MessageProcessor.processInit(messagePartElement.childNodes[i]);
                break;
            case "dispose":
                EchoOverlay.MessageProcessor.processDispose(messagePartElement.childNodes[i]);
                break;
            case "scroll-horizontal":
                EchoOverlay.MessageProcessor.processScroll(messagePartElement.childNodes[i]);
                break;
            case "scroll-vertical":
                EchoOverlay.MessageProcessor.processScroll(messagePartElement.childNodes[i]);
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
EchoOverlay.MessageProcessor.processDispose = function(disposeMessageElement) {
    for (var item = disposeMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        EchoEventProcessor.removeHandler(elementId, "scroll");
    	var overlay = EchoOverlay.getComponent(elementId);        
        if (overlay && overlay.serverNotify)
		{        
			EchoEventProcessor.removeHandler(elementId, "click", "EchoOverlay.processClick");
    	}

    	if (overlay)
    	{
    		EchoDomPropertyStore.dispose(overlay.element);
    		overlay.element = undefined;
    	}
    	
    }
};

/**
 * Processes an <code>init</code> message to initialize the state of a 
 * selection component that is being added.
 *
 * @param initMessageElement the <code>init</code> element to process
 */
EchoOverlay.MessageProcessor.processInit = function(initMessageElement) {
	 
        
    for (var item = initMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        var overlay = new EchoOverlay(elementId);
	    overlay.enabled = item.getAttribute("enabled") != "false";
	    overlay.serverNotify = item.getAttribute("server-notify") != "false";
        var divElement = document.getElementById(elementId);

        var horizontalScroll = item.getAttribute("horizontal-scroll");
        var verticalScroll = item.getAttribute("vertical-scroll");

        EchoEventProcessor.addHandler(divElement, "scroll", "EchoOverlay.processScroll");
		
		if (overlay.serverNotify)
		{
        	EchoEventProcessor.addHandler(divElement, "click", "EchoOverlay.processClick");
    	}
    	EchoDomPropertyStore.setPropertyValue(overlay.element, "component", overlay);
	    
        if (horizontalScroll) {
            divElement.scrollLeft = parseInt(horizontalScroll);
        }
        if (verticalScroll) {
            divElement.scrollTop = parseInt(verticalScroll);
        }
    }
    
};

/**
 * Processes a <code>scroll</code> directive to update the scrollbar positions
 * of the component.
 *
 * @param scrollMessageElement the <code>scroll</code> element to process
 */
EchoOverlay.MessageProcessor.processScroll = function(scrollMessageElement) {
    var elementId = scrollMessageElement.getAttribute("eid");
    var position = parseInt(scrollMessageElement.getAttribute("position"));

    var divElement = document.getElementById(elementId);
    
    if (position < 0) {
        position = 1000000;
    }
    
    if (scrollMessageElement.nodeName == "scroll-horizontal") {
        divElement.scrollLeft = position;
    } else if (scrollMessageElement.nodeName == "scroll-vertical") {
        divElement.scrollTop = position;
    }
};