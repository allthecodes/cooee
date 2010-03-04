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
 * Static namespace for the ModalDimmer.  Since there can only be one dimmer 
 * per Application, there is no prototype here.
 */
TucanaModalDimmer = function() { };

TucanaModalDimmer.dimmerComponentId = null;
TucanaModalDimmer.dimType = null;
TucanaModalDimmer.dimOpacity = null;
TucanaModalDimmer.dimColor = null;
TucanaModalDimmer.dimAnimated = null;
TucanaModalDimmer.dimAnimationSpeed = null;
TucanaModalDimmer.dimImage = null;
TucanaModalDimmer.currentModalElement = null;

/**
 * Initialize the ModalDimmer.  This method hijacks Echo2's EchoServerMessage.processApplicationProperties
 * in order to get events related to modal windows.
 */
TucanaModalDimmer.init = function() {
    TucanaModalDimmer.realProcessApplicationProperties = EchoServerMessage.processApplicationProperties;  
    EchoServerMessage.processApplicationProperties = function() {
        TucanaModalDimmer.realProcessApplicationProperties();
        if (EchoModalManager.modalElementId) {
            TucanaModalDimmer.dim();
        } else {
            TucanaModalDimmer.undim();
        }
    };

    var dimElement = document.createElement("div");
    dimElement.id = TucanaModalDimmer.dimmerComponentId;
    dimElement.style.position = "absolute";
    dimElement.style.left = "0px";
    dimElement.style.top = "0px";
    dimElement.style.margin = "0px";
    dimElement.style.padding = "0px";
    dimElement.style.width = "100%";
    dimElement.style.height = "100%";
    dimElement.style.cursor = "wait";
    
    TucanaModalDimmer.outerElement = dimElement;
    
    // If we're using image dimming, precache the image.
    if (!TucanaModalDimmer.isOpacityDimming()) {
        TucanaModalDimmer.dimImageObject = new Image();
        TucanaModalDimmer.dimImageObject.src = TucanaModalDimmer.dimImage;
    }
};

/**
 * Dispose of the ModalDimmer.  This resets the processApplicationProperties
 * method to what it was before the ModalDimmer was added.
 */
TucanaModalDimmer.dispose = function() {
    if (TucanaModalDimmer.realProcessApplicationProperties) {
        EchoServerMessage.processApplicationProperties = TucanaModalDimmer.realProcessApplicationProperties;
        TucanaModalDimmer.realProcessApplicationProperties = null;
    }

    TucanaModalDimmer.undim();

    TucanaModalDimmer.dimType = null;
    TucanaModalDimmer.dimOpacity = null;
    TucanaModalDimmer.dimColor = null;
    TucanaModalDimmer.dimAnimated = null;
    TucanaModalDimmer.dimAnimationSpeed = null;
    TucanaModalDimmer.dimImage = null;

    TucanaModalDimmer.outerElement = null;
};

TucanaModalDimmer.alertFunc = function() { alert("Hello!"); };


/**
 * Dim the screen.  If the dim type is "opacity" and animation is enabled, this
 * will kick off the animation.
 */
TucanaModalDimmer.dim = function() {
    // Check to see if the dimmer element is already created.
    var modalElement = document.getElementById(EchoModalManager.modalElementId);
    if (TucanaModalDimmer.currentModalElement != null) {
        if (TucanaModalDimmer.currentModalElement != modalElement) {
            // This happens if the modal element is updated requiring a redraw,
            // but is not yet removed server-side
            TucanaModalDimmer.currentModalElement = modalElement;
            var modalZIndex = parseInt(TucanaModalDimmer.getStyle(modalElement, "z-index"));
            modalElement.style.zIndex = modalZIndex + 2;
            TucanaModalDimmer.outerElement.style.zIndex = modalZIndex + 1;
            modalElement.parentNode.appendChild(TucanaModalDimmer.outerElement);
            return;
        } else {
            return;
        }
    }

    TucanaModalDimmer.currentModalElement = modalElement;    
    var bodyElement = document.getElementsByTagName("body")[0];
    var innerType = "div";
    var iframeElement;
    var innerElement;  

    innerElement = document.createElement("div");
    innerElement.style.width = "100%";
    innerElement.style.height = "100%";
    innerElement.style.zIndex = 100;
    innerElement.style.position = "absolute";
    innerElement.style.left = "0px";
    innerElement.style.top = "0px";
    TucanaModalDimmer.outerElement.appendChild(innerElement);

    if (TucanaModalDimmer.isOpacityDimming()) {
        innerElement.style.backgroundColor = TucanaModalDimmer.dimColor;
        if (TucanaModalDimmer.dimAnimated) {
            EchoDebugManager.consoleWrite("Animating");
            TucanaModalDimmer.currentAnimation = new Object();
            TucanaModalDimmer.currentAnimation.element = innerElement;
            TucanaModalDimmer.currentAnimation.startTime = (new Date()).getTime();
            TucanaModalDimmer.currentAnimation.interval = setInterval("TucanaModalDimmer.animate()", 50);          
            TucanaModalDimmer.setOpacity(innerElement, 0.0);
        } else {
            TucanaModalDimmer.setOpacity(innerElement, TucanaModalDimmer.dimOpacity);
        }
    } else {
        innerElement.style.backgroundImage = "url(" + TucanaModalDimmer.dimImage + ")";
    }
    
    if (EchoClientProperties.get("quirkIESelectZIndex")) {
        var iframeElement = document.createElement("iframe");
        iframeElement.hspace = 0;
        iframeElement.vspace = 0;
        iframeElement.frameBorder = "0";
        iframeElement.style.position = "absolute";
        iframeElement.style.top = "0px";
        iframeElement.style.left = "0px";
        iframeElement.style.width = "100%";
        iframeElement.style.height = "100%";
        iframeElement.style.filter = "alpha(opacity=0)";
        iframeElement.style.zIndex = 10;
        iframeElement.src = EchoClientEngine.baseServerUri + "?serviceId=Tucana.ModalDimmer.IFrame";
        TucanaModalDimmer.outerElement.appendChild(iframeElement);
    }
   
    var modalZIndex = parseInt(TucanaModalDimmer.getStyle(modalElement, "z-index"));
    modalElement.style.zIndex = modalZIndex + 2;
    TucanaModalDimmer.outerElement.style.zIndex = modalZIndex + 1;
    modalElement.parentNode.appendChild(TucanaModalDimmer.outerElement);
};

/**
 * Undim the screen.
 */
TucanaModalDimmer.undim = function() {
    if (TucanaModalDimmer.outerElement) {
        if (TucanaModalDimmer.outerElement.parentNode) {
            TucanaModalDimmer.outerElement.parentNode.removeChild(TucanaModalDimmer.outerElement);
        }
        while (TucanaModalDimmer.outerElement.firstChild) {
            TucanaModalDimmer.outerElement.removeChild(TucanaModalDimmer.outerElement.firstChild);
        }
    }
    TucanaModalDimmer.currentModalElement = null;
};

/**
 * Animation step.  Calculates the current opacity value based on how long the
 * animation has been running.  If the opacity value has reached the final (set)
 * value, the animation is stopped.
 */
TucanaModalDimmer.animate = function() {
    var duration = (new Date()).getTime() - TucanaModalDimmer.currentAnimation.startTime;
    var opacityChange = duration / 1000.0 * TucanaModalDimmer.dimAnimationSpeed;
    var currentOpacity = 0.0 + opacityChange;
    if (currentOpacity >= TucanaModalDimmer.dimOpacity) {
        clearInterval(TucanaModalDimmer.currentAnimation.interval);
        TucanaModalDimmer.setOpacity(TucanaModalDimmer.currentAnimation.element, TucanaModalDimmer.dimOpacity);
        TucanaModalDimmer.currentAnimation = null;
    } else {
        TucanaModalDimmer.setOpacity(TucanaModalDimmer.currentAnimation.element, currentOpacity);
    }
};

/**
 * Determine which dimming mode to use.  If the dimming mode is explicitly specified 
 * (opacity or image), use that.  If the dimming mode is set to auto, determine
 * based on browser type.
 * 
 * @return true if the dimming mode is opacity, false if it is image.
 */
TucanaModalDimmer.isOpacityDimming = function() {
    if (TucanaModalDimmer.dimType == "auto") {
        if (EchoClientProperties.get("browserInternetExplorer")) {
            return true;
        } else if (EchoClientProperties.get("browserMozilla")) {
            return true;
        } else if (EchoClientProperties.get("browserMozillaFirefox")) {
            return true;
        } else if (EchoClientProperties.get("browserSafari")) {
            return true;
        } else {
            return false;
        }
    } else {
        return TucanaModalDimmer.dimType == "opacity";
    }
};

/**
 * Set the opacity of an element.
 * 
 * @param element The element whose opacity to change.
 * @param value The opacity value.
 */
TucanaModalDimmer.setOpacity = function(element, value) {
	if (EchoClientProperties.get("browserInternetExplorer")) {
		element.style.zoom = 1;
		element.style.filter = "alpha(opacity=" + value*100 + ")";
	} else {
		element.style.opacity = value;
		element.style.KhtmlOpacity = value;
	}
};

/**
 * Get the rendered style of an element.  
 * 
 * Taken from http://www.robertnyman.com/2006/04/24/get-the-rendered-style-of-an-element/
 * 
 * @param oElm the element
 * @param strCssRule the css rule name to get
 * @return The actual rendered style value
 */
TucanaModalDimmer.getStyle = function(oElm, strCssRule) {
    var strValue = "";
    if(document.defaultView && document.defaultView.getComputedStyle){
        strValue = document.defaultView.getComputedStyle(oElm, "").getPropertyValue(strCssRule);
    }
    else if(oElm.currentStyle){
        strCssRule = strCssRule.replace(/\-(\w)/g, function (strMatch, p1){
            return p1.toUpperCase();
        });
        strValue = oElm.currentStyle[strCssRule];
    }
    return strValue;
}

/**
 * Namespace for server->client message processor
 */
TucanaModalDimmer.MessageProcessor = function() { };

/**
 * Static message processing method.  This method delegates to specific handlers.
 * 
 * @param messagePartElement message element to process
 */
TucanaModalDimmer.MessageProcessor.process = function(messagePartElement) {
    for (var i = 0; i < messagePartElement.childNodes.length; ++i) {
        if (messagePartElement.childNodes[i].nodeType == 1) {
            switch (messagePartElement.childNodes[i].tagName) {
            case "init":
                TucanaModalDimmer.MessageProcessor.processInit(messagePartElement.childNodes[i]);
                break;
            case "dispose":
                TucanaModalDimmer.MessageProcessor.processDispose(messagePartElement.childNodes[i]);
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
TucanaModalDimmer.MessageProcessor.processDispose = function(disposeMessageElement) {
    var dimmerId = disposeMessageElement.getAttribute("eid");
    if (TucanaModalDimmer.dimmerComponentId == dimmerId) {
        TucanaModalDimmer.dispose();
        TucanaModalDimmer.dimmerComponentId = null;
    }
};

/**
 * Processes an <code>init</code> message to initialize the state of a 
 * selection component that is being added.
 *
 * @param initMessageElement the <code>init</code> element to process
 */
TucanaModalDimmer.MessageProcessor.processInit = function(initMessageElement) {
    var dimmerId = initMessageElement.getAttribute("eid");
	
	if (TucanaModalDimmer.dimmerComponentId == null || TucanaModalDimmer.dimmerComponentId == dimmerId) {
		TucanaModalDimmer.dimmerComponentId = dimmerId;
		
		var propertiesElement = initMessageElement.firstChild;
		
		for (var i = 0; i < propertiesElement.childNodes.length; i++) {
	        var property = propertiesElement.childNodes[i];
	        var propertyName = property.getAttribute("name");
	        var propertyValue = property.getAttribute("value");
	        switch (propertyName) {
	            case "dimType":
	            case "dimImage":
	            case "dimColor":
                    TucanaModalDimmer[propertyName] = propertyValue;
                    break;
                                        
	            case "dimOpacity":
	            case "dimAnimationSpeed":
                    TucanaModalDimmer[propertyName] = parseFloat(propertyValue);
                    break;
                    
	            case "dimAnimated":
	               TucanaModalDimmer[propertyName] = (propertyValue == "true");
	               break;
	        } 
    	}
    	TucanaModalDimmer.init();
	} else {
		throw new Error("Multiple instances of ModalDimmer!");
	}
};