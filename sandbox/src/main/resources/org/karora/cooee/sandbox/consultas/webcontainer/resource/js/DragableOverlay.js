
//_______________________
// Object EchoDragableOverlay

/**
 * Static object/namespace for Overlay support.
 * This object/namespace should not be used externally.
 */
EchoDragableOverlay = function(elementId,containerElementId) {
    this.elementId = elementId;
    this.containerElementId = containerElementId;
    
    this.resizingBorderElementId = null;
    
    this.dragOriginX = null;
    this.dragOriginY = null;
    this.dragInitPositionX = null;
    this.dragInitPositionY = null;
    this.dragInitWidth = null;
    this.dragInitHeight = null;
    this.resizeX = 0;
    this.resizeY = 0;
    this.restoreX = 0;
    this.restoreY = 0;
    this.restoreW = 0;
    this.restoreH = 0;
    
    this.positionX = null;
    this.positionY = null;

    this.width = EchoDragableOverlay.DEFAULT_WIDTH;
    this.height = EchoDragableOverlay.DEFAULT_HEIGHT;
    
};

EchoDragableOverlay.DEFAULT_WIDTH = 100;
EchoDragableOverlay.DEFAULT_HEIGHT = 100;

EchoDragableOverlay.prototype.dispose = function() {
    this.removeListeners();

    EchoEventProcessor.removeHandler(this.titleBarDivElement, "mousedown");
    EchoEventProcessor.removeHandler(this.overlayDivElement, "mousedown");
    EchoEventProcessor.removeHandler(this.borderTLDivElement, "mousedown");
    EchoEventProcessor.removeHandler(this.borderTDivElement, "mousedown");
    EchoEventProcessor.removeHandler(this.borderTRDivElement, "mousedown");
    EchoEventProcessor.removeHandler(this.borderLDivElement, "mousedown");
    EchoEventProcessor.removeHandler(this.borderRDivElement, "mousedown");
    EchoEventProcessor.removeHandler(this.borderBLDivElement, "mousedown");
    EchoEventProcessor.removeHandler(this.borderBDivElement, "mousedown");
    EchoEventProcessor.removeHandler(this.borderBRDivElement, "mousedown");

    EchoDragableOverlay.ZIndexManager.remove(this.containerComponentElementId, this.elementId);
    
    EchoDomPropertyStore.dispose(this.overlayDivElement);

    this.overlayDivElement = undefined;
};

EchoDragableOverlay.prototype.getContainerHeight = function() {
    var containerElement = document.getElementById(this.containerElementId);
    var height = containerElement.offsetHeight;
    if (height == 0) {
        height = containerElement.parentNode.offsetHeight;
    }
    return height;
};

EchoDragableOverlay.prototype.getContainerWidth = function() {
    var containerElement = document.getElementById(this.containerElementId);
    var width = containerElement.offsetWidth;
    if (width == 0) {
        width = containerElement.parentNode.offsetWidth;
    }
    return width;
};

EchoDragableOverlay.prototype.processBorderMouseDown = function(echoEvent) {
//    alert("enabled : " + this.enabled);
//    alert("enabled : " + EchoClientEngine.verifyInput(this.elementId));
    
    if (!this.enabled || !EchoClientEngine.verifyInput(this.elementId)) {
        return;
    }
    EchoDomUtil.preventEventDefault(echoEvent);
    this.raise();
    EchoDragableOverlay.activeInstance = this;
    this.resizingBorderElementId = echoEvent.registeredTarget.id;
    this.dragInitPositionX = this.positionX;
    this.dragInitPositionY = this.positionY;
    this.dragInitWidth = this.width;
    this.dragInitHeight = this.height;
    this.dragOriginX = echoEvent.clientX;
    this.dragOriginY = echoEvent.clientY;
    
    var directionId = this.resizingBorderElementId.substring(this.resizingBorderElementId.lastIndexOf("_") + 1);
    switch(directionId) {
    case "tl": this.resizeX = -1; this.resizeY = -1; break;
    case "t":  this.resizeX =  0; this.resizeY = -1; break;
    case "tr": this.resizeX =  1; this.resizeY = -1; break;
    case "l":  this.resizeX = -1; this.resizeY =  0; break;
    case "r":  this.resizeX =  1; this.resizeY =  0; break;
    case "bl": this.resizeX = -1; this.resizeY =  1; break;
    case "b":  this.resizeX =  0; this.resizeY =  1; break;
    case "br": this.resizeX =  1; this.resizeY =  1; break;
    }

    // Remove all listeners to avoid possible retention issues in IE.
    this.removeListeners();
    
    EchoDomUtil.addEventListener(document, "mousemove", EchoDragableOverlay.processBorderMouseMove, false);
    EchoDomUtil.addEventListener(document, "mouseup", EchoDragableOverlay.processBorderMouseUp, false);
    if (EchoClientProperties.get("browserInternetExplorer")) {
        EchoDomUtil.addEventListener(document, "selectstart", EchoDragableOverlay.selectStart, false);
    }
};

EchoDragableOverlay.prototype.processBorderMouseMove = function(e) {
    var width = this.width;
    var height = this.height;
    var positionX = this.positionX;
    var positionY = this.positionY;
    
    if (this.resizeX == -1) {
        width = this.dragInitWidth - (e.clientX - this.dragOriginX);
    } else if (this.resizeX ==1 ) {
        width = this.dragInitWidth + e.clientX - this.dragOriginX;
    }
    if (this.resizeY == -1) {
        height = this.dragInitHeight - (e.clientY - this.dragOriginY);
    } else if (this.resizeY ==1) {
        height = this.dragInitHeight + e.clientY - this.dragOriginY;
    }

    this.setSize(width, height);
    
    // If Resizing Up or Left, calculate new position based on new width/height such that
    // bottom right corner remains stationary.  This is done with this.width/this.height
    // in case width or height setting was bounded by setSize().
    if (this.resizeX == -1) {
        positionX = this.dragInitPositionX + this.dragInitWidth - this.width;
    }
    if (this.resizeY == -1) {
        positionY = this.dragInitPositionY + this.dragInitHeight - this.height;
    }
    
    this.setPosition(positionX, positionY);
    
    this.redraw();
};

EchoDragableOverlay.prototype.processBorderMouseUp = function(e) {
    this.removeListeners();
    
    this.resizingBorderElementId = null;
    EchoDragableOverlay.activeInstance = null;
    
    EchoClientMessage.setPropertyValue(this.elementId, "positionX", this.positionX + "px");
    EchoClientMessage.setPropertyValue(this.elementId, "positionY", this.positionY + "px");
    EchoClientMessage.setPropertyValue(this.elementId, "width", this.width + "px");
    EchoClientMessage.setPropertyValue(this.elementId, "height", this.height + "px");

    EchoVirtualPosition.redraw();
};

EchoDragableOverlay.prototype.processTitleBarMouseDown = function(echoEvent) {
    if (!this.enabled || !EchoClientEngine.verifyInput(this.elementId)) {
        return;
    }
    EchoDomUtil.preventEventDefault(echoEvent);
    this.raise();
    EchoDragableOverlay.activeInstance = this;
    this.dragInitPositionX = this.positionX;
    this.dragInitPositionY = this.positionY;
    this.dragOriginX = echoEvent.clientX;
    this.dragOriginY = echoEvent.clientY;
    
    // Remove all listeners to avoid possible retention issues in IE.
    this.removeListeners();
    
    EchoDomUtil.addEventListener(document, "mousemove", EchoDragableOverlay.processTitleBarMouseMove, false);
    EchoDomUtil.addEventListener(document, "mouseup", EchoDragableOverlay.processTitleBarMouseUp, false);
    if (EchoClientProperties.get("browserInternetExplorer")) {
        EchoDomUtil.addEventListener(document, "selectstart", EchoDragableOverlay.selectStart, false);
    }
};

EchoDragableOverlay.prototype.processTitleBarMouseMove = function(e) {
    this.setPosition(this.dragInitPositionX + e.clientX - this.dragOriginX,
            this.dragInitPositionY + e.clientY - this.dragOriginY);
    this.redraw();
};

EchoDragableOverlay.prototype.processTitleBarMouseUp = function(e) {
    this.removeListeners();
    
    EchoDragableOverlay.activeInstance = null;
    
    EchoClientMessage.setPropertyValue(this.elementId, "positionX", this.positionX + "px");
    EchoClientMessage.setPropertyValue(this.elementId, "positionY", this.positionY + "px");
    
    EchoVirtualPosition.redraw();
};

EchoDragableOverlay.prototype.raise = function() {
    var zIndex = EchoDragableOverlay.ZIndexManager.raise(this.containerComponentElementId, this.elementId);
    EchoClientMessage.setPropertyValue(this.elementId, "zIndex",  zIndex);
};

EchoDragableOverlay.prototype.create = function() {
    var containerElement = document.getElementById(this.containerElementId);
    
    this.overlayDivElement = document.createElement("div");
    this.overlayDivElement.id = this.elementId;
    this.overlayDivElement.style.position = "absolute";
    this.overlayDivElement.style.zIndex = "1";
    
    var containerWidth = this.getContainerWidth();
    var containerHeight = this.getContainerHeight();
    
    if (this.positionX == null) {
        if (containerWidth && containerHeight) {
            // Only center window if valid data exist for container width and height.
          this.positionX = Math.round((containerWidth - this.width) / 2);
          if (this.positionX < 0) {
              this.positionX = 0;
          }
        } else {
            this.positionX = 0;
        }
    }
    if (this.positionY == null) {
        if (containerWidth && containerHeight) {
            // Only center window if valid data exist for container width and height.
          this.positionY = Math.round((containerHeight - this.height) / 2);
          if (this.positionY < 0) {
              this.positionY = 0;
          }
        } else {
            this.positionY = 0;
        }
    }
    this.overlayDivElement.style.left = this.positionX + "px";
    this.overlayDivElement.style.top = this.positionY + "px";
    this.overlayDivElement.style.width = this.width + "px";
    this.overlayDivElement.style.height = this.height + "px";
    this.overlayDivElement.style.backgroundColor = this.background;
    if (this.movable)
    {
      this.overlayDivElement.style.cursor = "move";
    }
    if (this.opacity != null)
    {
      this.overlayDivElement.style.filter = 'alpha(opacity='+ this.opacity+')';
      this.overlayDivElement.style.opacity = '0.' + this.opacity;
    }
    
    var borderSideWidth = this.width - 6;
    var borderSideHeight = this.height - 6;
    
      // Render top left corner
      this.borderTLDivElement = document.createElement("div");
      this.borderTLDivElement.id = this.elementId + "_border_tl";
      this.borderTLDivElement.style.position = "absolute";
      this.borderTLDivElement.style.left = "0px";
      this.borderTLDivElement.style.top = "0px";
      this.borderTLDivElement.style.width = 3 + "px";
      this.borderTLDivElement.style.height = 3 + "px";
      /*if (this.border.color != null) {
          this.borderTLDivElement.style.backgroundColor = this.border.color;
      }*/
      this.borderTLDivElement.style.backgroundColor = "#000000";
      
      if (this.resizable)
      {
        this.borderTLDivElement.style.cursor = "nw-resize";
      }
      this.overlayDivElement.appendChild(this.borderTLDivElement);
      
      
      // Render top side
      this.borderTDivElement = document.createElement("div");
      this.borderTDivElement.id = this.elementId + "_border_t";
      this.borderTDivElement.style.position = "absolute";
      this.borderTDivElement.style.left = 3 + "px";
      this.borderTDivElement.style.top = "0px";
      this.borderTDivElement.style.width = borderSideWidth + "px";
      this.borderTDivElement.style.height = 3 + "px";
      /*if (this.border.color != null) {
          this.borderTDivElement.style.backgroundColor = this.border.color;
      }*/
      this.borderTDivElement.style.backgroundColor = "#000000";
      if (this.resizable)
      {
        this.borderTDivElement.style.cursor = "n-resize";
      }
      this.overlayDivElement.appendChild(this.borderTDivElement);

      // Render top right corner
      
      this.borderTRDivElement = document.createElement("div");
      this.borderTRDivElement.id = this.elementId + "_border_tr";
      this.borderTRDivElement.style.position = "absolute";
      this.borderTRDivElement.style.right = "0px";
      this.borderTRDivElement.style.top = "0px";
      this.borderTRDivElement.style.width = 3 + "px";
      this.borderTRDivElement.style.height = 3 + "px";
      /*if (this.border.color != null) {
          this.borderTRDivElement.style.backgroundColor = this.border.color;
      }*/
      this.borderTRDivElement.style.backgroundColor = "#000000";
      if (this.resizable)
      {
        this.borderTRDivElement.style.cursor = "ne-resize";
      }
      
      this.overlayDivElement.appendChild(this.borderTRDivElement);
      
    // Render left side
      this.borderLDivElement = document.createElement("div");
      this.borderLDivElement.id = this.elementId + "_border_l";
      this.borderLDivElement.style.position = "absolute";
      this.borderLDivElement.style.left = "0px";
      this.borderLDivElement.style.top = 3 + "px";
      this.borderLDivElement.style.width = 3 + "px";
      this.borderLDivElement.style.height = borderSideHeight + "px";
      /*if (this.border.color != null) {
          this.borderLDivElement.style.backgroundColor = this.border.color;
      }*/
      this.borderLDivElement.style.backgroundColor = "#000000";
      if (this.resizable)
      {
        this.borderLDivElement.style.cursor = "w-resize";
      }
      this.overlayDivElement.appendChild(this.borderLDivElement);
    
    // Render right side
      this.borderRDivElement = document.createElement("div");
      this.borderRDivElement.id = this.elementId + "_border_r";
      this.borderRDivElement.style.position = "absolute";
      this.borderRDivElement.style.right = "0px";
      this.borderRDivElement.style.top = 3 + "px";
      this.borderRDivElement.style.width = 3 + "px";
      this.borderRDivElement.style.height = borderSideHeight + "px";
      /*if (this.border.color != null) {
          this.borderRDivElement.style.backgroundColor = this.border.color;
      }*/
      this.borderRDivElement.style.backgroundColor = "#000000";
      this.borderRDivElement.style.cursor = "e-resize";
      this.overlayDivElement.appendChild(this.borderRDivElement);
    
    
        // Render bottom left corner
      this.borderBLDivElement = document.createElement("div");
      this.borderBLDivElement.id = this.elementId + "_border_bl";
      this.borderBLDivElement.style.position = "absolute";
      this.borderBLDivElement.style.left = "0px";
      this.borderBLDivElement.style.bottom = "0px";
      this.borderBLDivElement.style.width = 3 + "px";
      this.borderBLDivElement.style.height = 3 + "px";
      /*if (this.border.color != null) {
          this.borderBLDivElement.style.backgroundColor = this.border.color;
      }*/
      this.borderBLDivElement.style.backgroundColor = "#000000";
      if (this.resizable)
      {
        this.borderBLDivElement.style.cursor = "sw-resize";
      }
      this.overlayDivElement.appendChild(this.borderBLDivElement);
  

        // Render bottom side
      this.borderBDivElement = document.createElement("div");
      this.borderBDivElement.id = this.elementId + "_border_b";
      this.borderBDivElement.style.position = "absolute";
      this.borderBDivElement.style.left = 3 + "px";
      this.borderBDivElement.style.bottom = "0px";
      this.borderBDivElement.style.width = borderSideWidth + "px";
      this.borderBDivElement.style.height = 3 + "px";
      /*if (this.border.color != null) {
          this.borderBDivElement.style.backgroundColor = this.border.color;
      }*/
      this.borderBDivElement.style.backgroundColor = "#000000";
      if (this.resizable)
      {
        this.borderBDivElement.style.cursor = "s-resize";
      }
      this.overlayDivElement.appendChild(this.borderBDivElement);
      
        // Render bottom right corner
      this.borderBRDivElement = document.createElement("div");
      this.borderBRDivElement.id = this.elementId + "_border_br";
      this.borderBRDivElement.style.position = "absolute";
      this.borderBRDivElement.style.right = "0px";
      this.borderBRDivElement.style.bottom = "0px";
      this.borderBRDivElement.style.width = 3 + "px";
      this.borderBRDivElement.style.height = 3 + "px";
      /*if (this.border.color != null) {
          this.borderBRDivElement.style.backgroundColor = this.border.color;
      }*/
      this.borderBRDivElement.style.backgroundColor = "#000000";
      if (this.resizable)
      {
        this.borderBRDivElement.style.cursor = "se-resize";
      }
      this.overlayDivElement.appendChild(this.borderBRDivElement);
      
    // Render Title Bar
    this.titleBarDivElement = document.createElement("div");
    this.titleBarDivElement.id = this.elementId + "_titlebar";
    this.titleBarDivElement.style.position = "absolute";
    this.titleBarDivElement.style.zIndex = 3;
    
    this.titleBarDivElement.style.color = this.titleForeground;
    this.titleBarDivElement.style.top = 3 + "px";
    this.titleBarDivElement.style.left = 3 + "px";
    this.titleBarDivElement.style.width = (this.width - 6) + "px";
    this.titleBarDivElement.style.height = 3 + "px";
    this.titleBarDivElement.style.overflow = "hidden";
    if (this.movable)
    {
      this.titleBarDivElement.style.cursor = "move";
    }
  
    this.overlayDivElement.appendChild(this.titleBarDivElement);
    // Render Content Area
    
    this.contentDivElement = document.createElement("div");
    this.contentDivElement.id = this.elementId + "_content";
    this.contentDivElement.style.position = "absolute";
    this.contentDivElement.style.zIndex = 2;
    
    this.contentDivElement.style.top = 6 + "px";
    this.contentDivElement.style.left = 3 + "px";
    this.contentDivElement.style.right = 3 + "px";
    this.contentDivElement.style.bottom = 3 + "px";
    
    EchoVirtualPosition.register(this.contentDivElement.id);
    
    this.contentDivElement.style.overflow = "auto";
    /*if (this.insets != null) {
        this.contentDivElement.style.padding = this.insets;
    }*/
    this.overlayDivElement.appendChild(this.contentDivElement);
/*
    if (EchoClientProperties.get("quirkIESelectZIndex")) {
        // Render Select Field Masking IFRAME.
        this.maskDivElement = document.createElement("div");
        this.maskDivElement.id = this.elementId + "_mask";
        this.maskDivElement.style.zIndex = 1;
        this.maskDivElement.style.position = "absolute";
      this.maskDivElement.style.top = this.border.contentInsets.top + "px";
      this.maskDivElement.style.left = this.border.contentInsets.left + "px";
      this.maskDivElement.style.right = this.border.contentInsets.right + "px";
      this.maskDivElement.style.bottom = this.border.contentInsets.bottom + "px";
        this.maskDivElement.style.borderWidth = 0;
        
        var maskIFrameElement = document.createElement("iframe");
        maskIFrameElement.style.width = "100%";
        maskIFrameElement.style.height = "100%";
        this.maskDivElement.appendChild(maskIFrameElement);
        
      EchoVirtualPosition.register(this.maskDivElement.id);
      this.overlayDivElement.appendChild(this.maskDivElement);
    }
  */  
    containerElement.appendChild(this.overlayDivElement);

    EchoDomPropertyStore.setPropertyValue(this.overlayDivElement, "component", this);
    
    if (this.movable)
    {
    
        EchoEventProcessor.addHandler(this.titleBarDivElement, "mousedown", 
                "EchoDragableOverlay.processTitleBarMouseDown");
        
        EchoEventProcessor.addHandler(this.overlayDivElement, "mousedown", 
                "EchoDragableOverlay.processTitleBarMouseDown");
    }
    
    if (this.resizable)
    {
      EchoEventProcessor.addHandler(this.borderTLDivElement, "mousedown", "EchoDragableOverlay.processBorderMouseDown");
      EchoEventProcessor.addHandler(this.borderTDivElement, "mousedown", "EchoDragableOverlay.processBorderMouseDown");
      EchoEventProcessor.addHandler(this.borderTRDivElement, "mousedown", "EchoDragableOverlay.processBorderMouseDown");
      EchoEventProcessor.addHandler(this.borderLDivElement, "mousedown", "EchoDragableOverlay.processBorderMouseDown");
      EchoEventProcessor.addHandler(this.borderRDivElement, "mousedown", "EchoDragableOverlay.processBorderMouseDown");
      EchoEventProcessor.addHandler(this.borderBLDivElement, "mousedown", "EchoDragableOverlay.processBorderMouseDown");
      EchoEventProcessor.addHandler(this.borderBDivElement, "mousedown", "EchoDragableOverlay.processBorderMouseDown");
      EchoEventProcessor.addHandler(this.borderBRDivElement, "mousedown", "EchoDragableOverlay.processBorderMouseDown");
    }
    
    EchoDragableOverlay.ZIndexManager.add(this.containerComponentElementId, this.elementId);
    
};


EchoDragableOverlay.prototype.redraw = function() {
    var borderSideWidth = this.width - 6;
    var borderSideHeight = 0;
    
    borderSideHeight = this.height - 6;
    
    this.overlayDivElement.style.left = this.positionX + "px";
    this.overlayDivElement.style.top = this.positionY + "px";
    this.overlayDivElement.style.width = this.width + "px";
    this.overlayDivElement.style.height = this.height + "px";

    this.titleBarDivElement.style.width = (this.width - 6) + "px";

    this.borderTDivElement.style.width = borderSideWidth + "px";
    this.borderBDivElement.style.width = borderSideWidth + "px";
    this.borderLDivElement.style.height = borderSideHeight + "px";
    this.borderRDivElement.style.height = borderSideHeight + "px";
    
    var contentElement = document.getElementById(this.elementId + "_content");
    
    EchoVirtualPosition.redraw(contentElement);
    
};

EchoDragableOverlay.getComponent = function(element) {
    return EchoDomPropertyStore.getPropertyValue(element, "component");
};

EchoDragableOverlay.prototype.removeListeners = function() {
    EchoDomUtil.removeEventListener(document, "mousemove", EchoDragableOverlay.processTitleBarMouseMove, false);
    EchoDomUtil.removeEventListener(document, "mouseup", EchoDragableOverlay.processTitleBarMouseUp, false);
    EchoDomUtil.removeEventListener(document, "mousemove", EchoDragableOverlay.processBorderMouseMove, false);
    EchoDomUtil.removeEventListener(document, "mouseup", EchoDragableOverlay.processBorderMouseUp, false);
    if (EchoClientProperties.get("browserInternetExplorer")) {
        EchoDomUtil.removeEventListener(document, "selectstart", EchoDragableOverlay.selectStart, false);
    }
};


EchoDragableOverlay.prototype.setPosition = function(positionX, positionY) {
    if (positionX < 0) {
        positionX = 0;
    } else {
        var containerWidth = this.getContainerWidth();
        if (containerWidth > 0 && positionX > containerWidth - this.width) {
            positionX = containerWidth - this.width;
        }
    }
    if (positionY < 0) {
        positionY = 0;
    } else {
        var containerHeight = this.getContainerHeight();
        if (containerHeight > 0 && positionY > containerHeight - this.height) {
            positionY = containerHeight - this.height;
        }
    }
    

    this.positionX = positionX;
    this.positionY = positionY;
};

EchoDragableOverlay.prototype.setSize = function(width, height) {
    if (this.minimumWidth != null && width < this.minimumWidth) {
        width = this.minimumWidth;
    } else if (this.maximumWidth != null && width > this.maximumWidth) {
        width = this.maximumWidth;
    }
    if (this.minimumHeight != null && height < this.minimumHeight) {
        height = this.minimumHeight;
    } else if (this.maximumHeight != null && height > this.maximumHeight) {
        height = this.maximumHeight;
    }
    this.width = width;
    this.height = height;
};

EchoDragableOverlay.processClick = function(echoEvent) {
        
    if (!EchoClientEngine.verifyInput(echoEvent.registeredTarget)) {
    	return;
    }
    var overlay = EchoDragableOverlay.getComponent(echoEvent.registeredTarget);

	if (!overlay.serverNotify) {
		return;
    }
    
    if (document.selection && document.selection.empty) {
        document.selection.empty();
    }
    
    EchoClientMessage.setActionValue(overlay.element.id, "click");
    
    EchoServerTransaction.connect();
};

EchoDragableOverlay.selectStart = function() {
    EchoDomUtil.preventEventDefault(window.event);
};
/**
 * Do-nothing event handler.
 */
EchoDragableOverlay.nullEventHandler = function(echoEvent) { };

EchoDragableOverlay.processBorderMouseDown = function(echoEvent) {
    var componentId = EchoDomUtil.getComponentId(echoEvent.registeredTarget.id);
    var overlay = EchoDragableOverlay.getComponent(componentId);
    overlay.processBorderMouseDown(echoEvent);
};

EchoDragableOverlay.processBorderMouseMove = function(e) {
    e = e ? e : window.event;
    if (EchoDragableOverlay.activeInstance) {
      EchoDragableOverlay.activeInstance.processBorderMouseMove(e);
    }
};

EchoDragableOverlay.processBorderMouseUp = function(e) {
    e = e ? e : window.event;
    if (EchoDragableOverlay.activeInstance) {
        EchoDragableOverlay.activeInstance.processBorderMouseUp(e);
    }
};

EchoDragableOverlay.processTitleBarMouseDown = function(echoEvent) {
    var componentId = EchoDomUtil.getComponentId(echoEvent.registeredTarget.id);
    var overlay = EchoDragableOverlay.getComponent(componentId);
    overlay.processTitleBarMouseDown(echoEvent);
};

EchoDragableOverlay.processTitleBarMouseMove = function(e) {
    e = e ? e : window.event;
    if (EchoDragableOverlay.activeInstance) {
        EchoDragableOverlay.activeInstance.processTitleBarMouseMove(e);
    }
};

EchoDragableOverlay.processTitleBarMouseUp = function(e) {
    e = e ? e : window.event;
    if (EchoDragableOverlay.activeInstance) {
        EchoDragableOverlay.activeInstance.processTitleBarMouseUp(e);
    }
};

/**
 * Processes a scrollbar adjustment event.
 *
 * @param echoEvent the event, preprocessed by the 
 *        <code>EchoEventProcessor</code>
 */
EchoDragableOverlay.processScroll = function(echoEvent) {
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
EchoDragableOverlay.MessageProcessor = function() { };

/**
 * MessageProcessor process() implementation 
 * (invoked by ServerMessage processor).
 *
 * @param messagePartElement the <code>message-part</code> element to process.
 */
EchoDragableOverlay.MessageProcessor.process = function(messagePartElement) {
    for (var i = 0; i < messagePartElement.childNodes.length; ++i) {
        if (messagePartElement.childNodes[i].nodeType == 1) {
            switch (messagePartElement.childNodes[i].tagName) {
            case "init":
                EchoDragableOverlay.MessageProcessor.processInit(messagePartElement.childNodes[i]);
                break;
            case "dispose":
                EchoDragableOverlay.MessageProcessor.processDispose(messagePartElement.childNodes[i]);
                break;
            case "status":
                EchoDragableOverlay.MessageProcessor.processStatus(messagePartElement.childNodes[i]);
                break;
            case "position":
                EchoDragableOverlay.MessageProcessor.processStatus(messagePartElement.childNodes[i]);
                break;
            case "size":
                EchoDragableOverlay.MessageProcessor.processStatus(messagePartElement.childNodes[i]);
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
EchoDragableOverlay.MessageProcessor.processDispose = function(disposeMessageElement) {
/*
        EchoEventProcessor.removeHandler(this, "mousedown");
        EchoEventProcessor.removeHandler(this.borderTLDivElement, "mousedown");
        EchoEventProcessor.removeHandler(this.borderTDivElement, "mousedown");
        EchoEventProcessor.removeHandler(this.borderTRDivElement, "mousedown");
        EchoEventProcessor.removeHandler(this.borderLDivElement, "mousedown");
        EchoEventProcessor.removeHandler(this.borderRDivElement, "mousedown");
        EchoEventProcessor.removeHandler(this.borderBLDivElement, "mousedown");
        EchoEventProcessor.removeHandler(this.borderBDivElement, "mousedown");
        EchoEventProcessor.removeHandler(this.borderBRDivElement, "mousedown");
        
    for (var item = disposeMessageElement.firstChild; item; item = item.nextSibling) {
        var elementId = item.getAttribute("eid");
        EchoEventProcessor.removeHandler(elementId, "scroll");
    	var overlay = EchoDragableOverlay.getComponent(elementId);        
        if (overlay && overlay.serverNotify)
		{        
			EchoEventProcessor.removeHandler(elementId, "click", "EchoDragableOverlay.processClick");
    	}

    	if (overlay)
    	{
    		EchoDomPropertyStore.dispose(overlayDivElement);
    		//overlay.element = undefined;
    	}
    	
    }
    EchoDragableOverlay.ZIndexManager.remove(this.containerComponentElementId, this.elementId);
    */
    var elementId = disposeMessageElement.getAttribute("eid");
    var overlay = EchoDragableOverlay.getComponent(elementId);
    if (overlay) {
        overlay.dispose();
    }
};

/**
 * Processes an <code>init</code> message to initialize the state of a 
 * selection component that is being added.
 *
 * @param initMessageElement the <code>init</code> element to process
 */
EchoDragableOverlay.MessageProcessor.processInit = function(initElement) {
	 

    var elementId = initElement.getAttribute("eid");
    var containerElementId = initElement.getAttribute("container-eid");
    
    var overlay = new EchoDragableOverlay(elementId, containerElementId);
    
    overlay.enabled = initElement.getAttribute("enabled") != "false";

    if (initElement.getAttribute("background")) {
        overlay.background = initElement.getAttribute("background");
    }
    if (initElement.getAttribute("opacity")) {
        overlay.opacity = initElement.getAttribute("opacity");
    }
    if (initElement.getAttribute("height")) {
        overlay.height = parseInt(initElement.getAttribute("height"));
    }
    if (initElement.getAttribute("insets")) {
        overlay.insets = initElement.getAttribute("insets");
    }
    if (initElement.getAttribute("position-x")) {
        overlay.positionX = parseInt(initElement.getAttribute("position-x"));
    }
    if (initElement.getAttribute("position-y")) {
        overlay.positionY = parseInt(initElement.getAttribute("position-y"));
    }
    if (initElement.getAttribute("width")) {
        overlay.width = parseInt(initElement.getAttribute("width"));
    }
    overlay.resizable = initElement.getAttribute("resizable") != "false";
    overlay.movable = initElement.getAttribute("movable") != "false";

    overlay.create();
    
    
};

/**
 * Processes a <code>scroll</code> directive to update the scrollbar positions
 * of the component.
 *
 * @param scrollMessageElement the <code>scroll</code> element to process
 */
EchoDragableOverlay.MessageProcessor.processScroll = function(scrollMessageElement) {
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

/**
 * Static object/namespace to manage z-index ordering of multiple WindowPanes
 * with the same parent component.
 */
EchoDragableOverlay.ZIndexManager = function() { };

/**
 * Associative array mapping container ids to arrays of element ids.
 */
EchoDragableOverlay.ZIndexManager.containerIdToElementIdArrayMap = new EchoCollectionsMap();

/**
 * Adds a WindowPane to be managed by the ZIndexManager.
 *
 * @param containerId the id of the Element containing the WindowPane
 * @param elementId the id of the WindowPane
 */
EchoDragableOverlay.ZIndexManager.add = function(containerId, elementId) {
    var elementIdArray = EchoDragableOverlay.ZIndexManager.containerIdToElementIdArrayMap.get(containerId);
    if (!elementIdArray) {
        elementIdArray = new Array();
        EchoDragableOverlay.ZIndexManager.containerIdToElementIdArrayMap.put(containerId, elementIdArray);
    }
    var containsElement = false;
    for (var i = 0; i < elementIdArray.length; ++i) {
        if (elementIdArray[i] == elementId) {
            // Do nothing if re-rendering.
            return;
        }
    }
    elementIdArray.push(elementId);
    EchoDragableOverlay.ZIndexManager.raise(containerId, elementId);
};

/**
 * Raises a WindowPane being managed by the ZIndexManager to the above all 
 * other WindowPanes within its container.
 *
 * @param containerId the id of the Element containing the WindowPane
 * @param elementId the id of the WindowPane
 */
EchoDragableOverlay.ZIndexManager.raise = function(containerId, elementId) {
    var windowElement = document.getElementById(elementId);

    var elementIdArray = EchoDragableOverlay.ZIndexManager.containerIdToElementIdArrayMap.get(containerId);
    if (!elementIdArray) {
        throw new Error("Invalid container id.");
    }

    var raiseIndex = 0;
    
    for (var i = 0; i < elementIdArray.length; ++i) {
        var testWindowElement = document.getElementById(elementIdArray[i]);
        var zIndex = parseInt(testWindowElement.style.zIndex);
        if (!isNaN(zIndex) && zIndex >= raiseIndex) {
            if (elementIdArray[i] == elementId) {
                raiseIndex = zIndex;
            } else {
                raiseIndex = zIndex + 1;
            }
        }
    }

    windowElement.style.zIndex = raiseIndex;
    
    return raiseIndex;
};

/**
 * Removes a WindowPane from being managed by the ZIndexManager.
 *
 * @param containerId the id of the Element containing the WindowPane
 * @param elementId the id of the WindowPane
 */
EchoDragableOverlay.ZIndexManager.remove = function(containerId, elementId) {
    var elementIdArray = EchoDragableOverlay.ZIndexManager.containerIdToElementIdArrayMap.get(containerId);
    if (!elementIdArray) {
        throw new Error("ZIndexManager.remove: no data for container with id \"" + containerId + "\".");
    }
    for (var i = 0; i < elementIdArray.length; ++i) {
        if (elementIdArray[i] == elementId) {
            if (elementIdArray.length == 1) {
                EchoDragableOverlay.ZIndexManager.containerIdToElementIdArrayMap.remove(containerId);
            } else {
                if (i < elementIdArray.length - 1) {
                    elementIdArray[i] = elementIdArray[elementIdArray.length - 1];
                }
                --elementIdArray.length;
            }
            return;
        }
    }
    throw new Error("ZIndexManager.remove: Element with id \"" + elementId + 
            "\" does not exist in container with id \"" + containerId + "\".");
};
