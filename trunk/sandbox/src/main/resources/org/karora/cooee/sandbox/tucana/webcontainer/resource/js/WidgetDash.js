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

// Static methods

/**
 * Static namespace/object for the WidgetDash.  The WidgetDash is the
 * main component in the container component which holds widgets.
 * 
 * @author Jeremy Volkman
 * 
 * <p>Construct a WidgetDash instance</p>
 * 
 * @param elementId The id of the WidgetDash DOM element.
 * @param containerId The id of the containing DOM element.
 */
TucanaWidgetDash = function(elementId, containerId) {
    this.widgetDashElementId = elementId;
    this.containerId = containerId;
    this.widgetSpacing = "20px";
};

/**
 * Static method to return the WidgetDash instance for the given DOM element.
 * 
 * @param component The DOM element or element id.
 * @return The WidgetDash instance associated with the element, or <code>null</code>
 * if there is no associated instance.
 */
TucanaWidgetDash.getInstance = function(element) {
    return EchoDomPropertyStore.getPropertyValue(element, "instance");
};

// Prototype methods

/**
 * Initialize the WidgetDash.  Create the core DOM elements associated with
 * a WidgetDash instance.
 */
TucanaWidgetDash.prototype.create = function() {
    var containerElement = document.getElementById(this.containerId);    
    this.widgetDashElement = document.createElement("div");
    this.widgetDashElement.id = this.widgetDashElementId;
    this.widgetDashElement.style.width = "100%";
    this.widgetDashElement.style.height = "100%";
    containerElement.appendChild(this.widgetDashElement);

    EchoDomPropertyStore.setPropertyValue(this.widgetDashElement, "instance", this);

    // Create element	
//    this.columnContainer = document.createElement("div");
//    this.columnContainer.id = this.widgetDashElementId + "_columnContainer";
//    this.widgetDashElement.appendChild(this.columnContainer);
   
    var columnTable = document.createElement("table");
    columnTable.id = this.widgetDashElementId + "_table";
    columnTable.style.tableLayout = "fixed";
    columnTable.style.width = "100%";
    columnTable.style.height = "100%";
    var columnTableBody = document.createElement("tbody");
    columnTableBody.id = this.widgetDashElementId + "_tableBody";
    this.columnContainer = document.createElement("tr");
    this.columnContainer.id = this.widgetDashElementId + "_columnContainer";
    
    columnTableBody.appendChild(this.columnContainer);
    columnTable.appendChild(columnTableBody);
    this.widgetDashElement.appendChild(columnTable);
    
	this.onDeck = document.createElement("div");
	this.onDeck.style.visibility = "hidden";
	this.onDeck.id = this.widgetDashElementId + "_onDeck";
	this.widgetDashElement.appendChild(this.onDeck);
};

/**
 * Add a widget to this WidgetDash (called by the widget being added).
 * If the widget has a defined position, it will be placed there.  Otherwise,
 * the widget will be placed in the shortest column at the time of addition.
 * 
 * @param widget The WidgetContainer element to add
 */
TucanaWidgetDash.prototype.addWidget = function(widget) {
    var instance = TucanaWidgetContainer.getInstance(widget);
    var position = instance.getWidgetPosition();
    var column = null;
    var columnPosition = 0;
    var positionsNeedUpdating = false;
    if (position) {
        column = this.getColumn(position.column);
        columnPosition = position.columnPosition;
        if (!column) {
            column = this.getBestColumn(position.column + 1 > this.getColumnCount());
            columnPosition = -1;
        }
    } else {
        // Determine automatic position
        column = this.getBestColumn(false);
        columnPosition = -1;
        positionsNeedUpdating = true;
    }
    
    var otherContainer = column.firstChild;
    while (!TucanaWidgetDash.isColumnTerminator(otherContainer)) {
        var otherInstance = TucanaWidgetContainer.getInstance(otherContainer);
        var otherPosition = otherInstance.getWidgetPosition();
        if (columnPosition <= otherPosition.columnPosition) break;
        otherContainer = otherContainer.nextSibling;
    }
    var oldVisibility = widget.style.visibility;
	
	// Pre-init widget spacing.
	widget.style.marginBottom = this.widgetSpacing;
    otherContainer.parentNode.insertBefore(widget, otherContainer);
    this.updateWidgetSpacing();

    if (positionsNeedUpdating) {
        this.updatePositions(false);
    }
};

/**
 * Remove a widget from the panel.
 * 
 * @param widget The widget to remove.
 */
TucanaWidgetDash.prototype.removeWidget = function(widget) {
    // widget should be in graveyard
    // ?? this.getGraveyard().removeChild(widget);
    widget.parentNode.removeChild(widget);
    this.updatePositions(false);
    this.updateWidgetSpacing();
};

/**
 * Return the widget OnDeck area.  This is an invisible area where
 * not-yet-positioned widgets are placed.
 * 
 * @return the OnDeck area element.
 */
TucanaWidgetDash.prototype.getOnDeck = function() {
	return this.onDeck;
};

/**
 * Return the immediate parent of column elements.  In the current 
 * implementation, this should be a &lt;td&gt; element.
 * 
 * @return The column container element.
 */
TucanaWidgetDash.prototype.getColumnContainer = function() {
    return this.columnContainer;
};

/**
 * Return the column at the specified index.
 * 
 * @return the specified column, or <code>null</code> if it does not exist.
 */
TucanaWidgetDash.prototype.getColumn = function(columnNum) {
    return document.getElementById(this.widgetDashElementId + "_column_" + columnNum);
};

/**
 * Return the current number of columns in the WidgetDash.
 * 
 * @return The current column count.
 */
TucanaWidgetDash.prototype.getColumnCount = function() {
    var count = 0;
    var columnContainer = this.getColumnContainer();
    for (var i = 0; i < columnContainer.childNodes.length; i++) {
        if (columnContainer.childNodes[i].className == "column") {
            count++;
        }
    }
    return count;
};

/**
 * Return all WidgetContainers contained in this WidgetDash's columns.  Optionally,
 * column terminators can also be returned.
 * 
 * @param includeTerminators Whether or not to return column terminators.  Terminators
 * are empty DIV elements at the end of each column used for positioning while dragging.
 * 
 * @return An array of all WidgetContainers positioned in columns, and optionally the 
 * column terminators.
 */
TucanaWidgetDash.prototype.getWidgetContainers = function(includeTerminators) {

    var widgets = new Array();
    for (var col = 0; col < this.getColumnCount(); col++) {
        var column = this.getColumn(col);
        var child = column.firstChild;
        var colWidgets = new Array();
        while (child) {
            if (TucanaWidgetDash.isColumnTerminator(child)) {
                if (includeTerminators) {
                    colWidgets[colWidgets.length] = child;
                }
            } else {
                if(child.id != this.widgetDashElementId + "_widgetShadow") {
                    colWidgets[colWidgets.length] = child;
                }
            }
            child = child.nextSibling;
        }
        for (var i = 0; i < colWidgets.length; i++) {
            widgets[widgets.length] = colWidgets[i];
        }
    }
    return widgets;
};

/**
 * Determine the actual positions of widgets in the panel and inform them of their
 * current positions.
 * 
 * @param report Whether or not to report new positioning to the server
 */
TucanaWidgetDash.prototype.updatePositions = function(report) {
    var needsReporting = false;
    
    for (var column = 0; column < this.getColumnCount(); column++) {
        var columnElement = this.getColumn(column);
        var child = columnElement.firstChild;
        var columnPosition = 0;
        while (child && !TucanaWidgetDash.isColumnTerminator(child)) {
            var position = new TucanaWidgetDash.WidgetPosition(column, columnPosition++);
            var id = EchoDomUtil.getComponentId(child.id);
            var widgetInstance = TucanaWidgetContainer.getInstance(id);
            if (widgetInstance.updateWidgetPosition(position)) {
                needsReporting = true;
            }
            child = child.nextSibling;
        }
    }
    if (/*needsReporting && */report) {
       EchoClientMessage.setActionValue(this.widgetDashElementId, "positionsUpdated");
       EchoServerTransaction.connect();
    }    
};

/**
 * Instruct each widget to update the vertical spacing between itself and the one below
 * it.  The vertical spacing is determined by the value set in this WidgetDash instance.
 */
TucanaWidgetDash.prototype.updateWidgetSpacing = function() {
    var containers = this.getWidgetContainers(false);
    for (i in containers) {
        var containerInstance = TucanaWidgetContainer.getInstance(containers[i]);
        containerInstance.updateWidgetSpacing(this);
    }
};

/**
 * Get the DOM element in which widgets should be moved to while dragging. This
 * defaults to the WidgetDash itself, but can be set to any ancestor of the panel.
 * 
 * @return The DOM element which will contain a widget being dragged
 */
TucanaWidgetDash.prototype.getDraggingContainer = function() {
   if (this.dragContainer) {
       var containerElement = document.getElementById(this.dragContainer);
       if (containerElement) return containerElement;
   }
   return this.widgetDashElement;
};

/**
 * Return the current mouse cursor position
 * 
 * @return The current mouse cursor position
 */
TucanaWidgetDash.prototype.getCursorPosition = function(e){
	var pageX;
	var pageY;
	
	if(e.pageX) {
		pageX = e.pageX;
		pageY = e.pageY;
	}
	else {
        var draggingContainer = this.getDraggingContainer();
		pageX = e.clientX + draggingContainer.scrollLeft - draggingContainer.clientLeft;
		pageY = e.clientY + draggingContainer.scrollTop - draggingContainer.clientTop;
	}
	return {x:pageX,y:pageY};
};

/**
 * Return the position for an element, relative to the dragging container
 * and adjusted for scrolling.
 * 
 * @param element The element to locate.
 * @return The position of the given element.
 */
TucanaWidgetDash.prototype.getElementPosition = function(element) {
	var offsetLeft = element.offsetLeft;
	var offsetTop = element.offsetTop;
	
	if (element != this.getDraggingContainer()) {
    	element = element.parentNode;
    	while (element){
    		offsetLeft += element.offsetLeft - element.scrollLeft;
    		offsetTop += element.offsetTop - element.scrollTop;
    		if (element == this.getDraggingContainer()) break;
    		element = element.offsetParent;
    	}	
    }
	return {left:offsetLeft,top:offsetTop};
};

/**
 * Get the "best" column to add a new widget to. Currently, the "best" column
 * is simply the shortest column at the time.
 * 
 * @param searchBackwards Search for the shortest column starting with the last
 * column and moving towards the first.
 * @return The shortest column.
 */
TucanaWidgetDash.prototype.getBestColumn = function(searchBackwards) {
    var shortestColumn = null;
    var columnCount = this.getColumnCount();
    for (var i = 0; i < columnCount; i++) {
        var colNum = searchBackwards ? columnCount - i - 1 : i;
        var column = this.getColumn(colNum);
        if (shortestColumn == null || column.lastChild.offsetTop < shortestColumn.lastChild.offsetTop) {
            shortestColumn = column;
        }
    }    
    return shortestColumn;
};

/**
 * Process a &lt;properties&gt; element from a server message. The &lt;properties&gt;
 * element contains updates to WidgetDash properties and can be applied at 
 * runtime.  Some properties will be delegated to specific handlers.
 * 
 * @param the properties element from the server message.
 */
TucanaWidgetDash.prototype.processProperties = function(propertiesElement) {
    for (var i = 0; i < propertiesElement.childNodes.length; i++) {
        var property = propertiesElement.childNodes[i];
        var propertyName = property.getAttribute("name");
        var propertyValue = property.getAttribute("value");
        switch (propertyName) {
            case "columnCount":
                var columnCount = parseInt(propertyValue);
                this.setColumnCount(columnCount);
                break;
            
            case "columnSpacing": 
                this.columnSpacing = propertyValue;
                this.updateColumnSpacing();
                break;
                
            case "widgetSpacing":
                this.widgetSpacing = propertyValue;
                this.updateWidgetSpacing();
            break;

            // String values
            case "shadowBorder":
            case "shadowType":
            case "dragContainer":
            case "returnMethod":
                this[propertyName] = propertyValue;  
                break;

            // Float values
            case "shadowOpacity":
            case "widgetOpacity":
                this[propertyName] = parseFloat(propertyValue);
                break;     

            // Int values
            case "driftStep":
            case "driftInterval":
                this[propertyName] = parseInt(propertyValue);
                break;
                
            // Boolean values            
            case "dragInBody":
            case "driftBack":
                this[propertyName] = (propertyValue == "true");
                break;
        } 
    }
};

/**
 * Handle the <code>columnCount</code> property.  If the new columnCount is less than the
 * current one, columns will be removed and widgets will be shifted to the available columns
 * automatically.  If the new columnCount is greater than the current one, new columns will be 
 * added but no widgets will be shifted.
 * 
 * @param the new <code>columnCount</code> value.
 */
TucanaWidgetDash.prototype.setColumnCount = function(newColumnCount) {
    var currentColumnCount = this.getColumnCount();
    var columnContainer = this.getColumnContainer();
    var orphanedWidgets = new Array();
    
    if (newColumnCount > currentColumnCount) {
        for (var i = currentColumnCount; i < newColumnCount; i++) {
            if (i > 0) {
                // add spacer first if this isn't the first column
               var spacerElement = document.createElement("td");
               spacerElement.id = this.widgetDashElementId + "_columnSpacer_" + (i - 1);
               spacerElement.className = "columnSpacer";
               columnContainer.appendChild(spacerElement);
            }
            var columnElement = document.createElement("td");
            columnElement.className = "column";
            columnElement.id = this.widgetDashElementId + "_column_" + i;
//          columnElement.setAttribute("nowrap", "");
            columnElement.style.verticalAlign = "top";
            columnElement.style.textAlign = "left";
            columnContainer.appendChild(columnElement);
            var terminatorElement = document.createElement("div");
            terminatorElement.className = "term";
            terminatorElement.id = columnElement.id + "_terminator";
            columnElement.appendChild(terminatorElement);
        }
    } else if (newColumnCount < currentColumnCount) {
        for (var i = currentColumnCount - 1; i >= newColumnCount; i--) {
            var columnElement = this.getColumn(i);
            var child = columnElement.firstChild;
            while (child && !TucanaWidgetDash.isColumnTerminator(child)) {
                orphanedWidgets[orphanedWidgets.length] = child;
                child = child.nextSibling;
            }
            // If there is a spacer before this column, remove it
            if (columnElement.previousSibling && columnElement.previousSibling.className == "columnSpacer") {
                columnContainer.removeChild(columnElement.previousSibling);
            }
            columnContainer.removeChild(columnElement);
        }
    }

    this.updateColumnSpacing();    
    
    for (var i = 0; i < orphanedWidgets.length; i++) {
        var column = this.getBestColumn(true);
        var term = column.lastChild;
        column.insertBefore(orphanedWidgets[i], term);
    }
    
    this.updatePositions(false);
    this.updateWidgetSpacing();
};

/**
 * Update the horizontal spacing between columns.  The spacing is determined
 * from this WidgetDash instance's <code>columnSpacing</code> value.
 */
TucanaWidgetDash.prototype.updateColumnSpacing = function() {
    if (!this.columnSpacing) return;
    var child = this.getColumnContainer().firstChild;
    while (child) {
        if (child.className == "columnSpacer") {
            child.style.width = this.columnSpacing;
        }
        child = child.nextSibling;
    }
};

// Utilities

/**
 * Set the opacity of an element.
 * 
 * @param element The element whose opacity to change.
 * @param value The opacity value.
 */
TucanaWidgetDash.setOpacity = function(element, value) {
	if (EchoClientProperties.get("browserInternetExplorer")) {
		element.style.zoom = 1;
		element.style.filter = "alpha(opacity=" + value*10 + ")";
	} else {
		element.style.opacity = value/10;
	}
};

/**
 * Copy a style element from one place to another.  Actually, this
 * method will copy the contents of any oject from one place to another
 * (not deeply).
 * 
 * @param source The source object whose contents to copy
 * @param dest the destination, or null.
 * 
 * @return The destination that the source contents were copied to.  If 
 * no destination object was provided, one will be created.
 */
TucanaWidgetDash.copyStyle = function(source, dest) {
    if (dest == null) {
        dest = new Object();
    }

    for (prop in source) {
        try {
            dest[prop] = source[prop];
        } catch (err) { 
            /* do nothing */
        }
    }
    return dest;
};

/**
 * Determine if the given element is a column terminator element.
 * 
 * @element The element in question.
 * @return true if the element is a column terminator.
 */
TucanaWidgetDash.isColumnTerminator = function(element) {
    return (element && element.className == "term");
};

/**
 * Start a widget drag session.  This is called with a WidgetGrabPoint
 * is clicked.
 * 
 * @param widgetContainerId The id of the widget being dragged
 * @param event The mousedown event that initiated the drag.
 */
TucanaWidgetDash.prototype.startDrag = function(widgetContainerId, event) {
    var dragSession = new TucanaWidgetDash.DragSession(this.widgetDashElementId, widgetContainerId);
    EchoClientMessage.setPropertyValue(this.widgetDashElementId, "activeWidgetContainer", widgetContainerId);
    dragSession.initSession(event);
};

// Object TucanaWidgetDash.WidgetPosition

/**
 * Static structure that holds a WidgetPosition (column, column position)
 * 
 * @param column The index of the column that the widget is in
 * @param columnPosition the position of the widget within the column
 */
TucanaWidgetDash.WidgetPosition = function(column, columnPosition) {
    this.column = column;
    this.columnPosition = columnPosition;
    
    this.equals = function(otherPosition) {
        return otherPosition.column == this.column 
        && otherPosition.columnPosition == this.columnPosition;
    }
};

// Object TucanaWidgetDash.DragSession

/**
 * Static object/namespace for a widget DragSession. A new DragSession is created
 * each time a widget is dragged around a WidgetDash.
 * 
 * @param widgetDashId The WidgetDash that this DragSession belongs to.
 * @param widgetContainerId The id of the widget being dragged.
 */
TucanaWidgetDash.DragSession = function(widgetDashId, widgetContainerId) {
    this.widgetDashId = widgetDashId;
    this.widgetContainerId = widgetContainerId;
};

/**
 * Static method to process a mousemove event while a DragSession is active.
 * The event is delegated to the active DragSession instance.
 * 
 * @param event The mousemove event
 */
TucanaWidgetDash.DragSession.processMouseMove = function(event) {
    event = event ? event : window.event;
    if (TucanaWidgetDash.DragSession.activeInstance) {
        TucanaWidgetDash.DragSession.activeInstance.processMouseMove(event);
    }
};

/**
 * Static method to process a mouseup event while a DragSession is active.
 * The event is delegated to the active DragSession instance.
 * 
 * @param event The mouseup event
 */
TucanaWidgetDash.DragSession.processMouseUp = function(event) {
    event = event ? event : window.event;
    if (TucanaWidgetDash.DragSession.activeInstance) {
        TucanaWidgetDash.DragSession.activeInstance.processMouseUp(event);
    }
};

/**
 * Static method to process the selectstart event (IE only).  The event
 * is simply cancelled.
 */
TucanaWidgetDash.DragSession.processSelectStart = function() {
    EchoDomUtil.preventEventDefault(window.event);
};

/**
 * Initialize a DragSession. The result of this method is that the widget to be 
 * dragged has been moved out of the WidgetDash and into the dragging container,
 * and a "shadow" of the widget has been created and placed in the widget's initial
 * position.
 * 
 * @param echoEvent The mousedown event that initiated the drag.
 */
TucanaWidgetDash.DragSession.prototype.initSession = function(echoEvent) {
    if (TucanaWidgetDash.DragSession.activeInstance) {
        return false;
    }

    EchoDomUtil.preventEventDefault(echoEvent);
    
    TucanaWidgetDash.DragSession.activeInstance = this;
    this.widgetContainerInstance = TucanaWidgetContainer.getInstance(this.widgetContainerId);
    this.widgetContainer = document.getElementById(this.widgetContainerId);
    this.widgetDashInstance = TucanaWidgetDash.getInstance(this.widgetDashId);
    
    var position = this.widgetDashInstance.getElementPosition(this.widgetContainer);
    this.originX = position.left;
    this.originY = position.top;

    this.initContainerPositions();
    
    // Detach widget from pane and place a shadow in its place.
    this.copyAndDetachWidget();

    var cursorPosition = this.widgetDashInstance.getCursorPosition(echoEvent);
    this.initialOffsetX = cursorPosition.x - this.originX;
    this.initialOffsetY = cursorPosition.y - this.originY;	
    
    EchoDomUtil.addEventListener(document, "mousemove", TucanaWidgetDash.DragSession.processMouseMove, false);
    EchoDomUtil.addEventListener(document, "mouseup", TucanaWidgetDash.DragSession.processMouseUp, false);
    if (EchoClientProperties.get("browserInternetExplorer")) {
        EchoDomUtil.addEventListener(document, "selectstart", TucanaWidgetDash.DragSession.processSelectStart, false);
    }

    // Manually invoke first MouseMove event
    this.processMouseMove(echoEvent);
};

/**
 * Process a mouseup event for a particular DragSession instance. The mouseup event
 * stops the dragging process and positions the widget in the current position of its
 * shadow.  If the <code>driftBack</code> WidgetDash is configured to have widgets
 * drift back to their positions, that animation is started.
 * 
 * @param e The mouseup event.
 */
TucanaWidgetDash.DragSession.prototype.processMouseUp = function(e) {
    EchoDomUtil.removeEventListener(document, "mousemove", TucanaWidgetDash.DragSession.processMouseMove, false);
    EchoDomUtil.removeEventListener(document, "mouseup", TucanaWidgetDash.DragSession.processMouseUp, false);
    if (EchoClientProperties.get("browserInternetExplorer")) {
        EchoDomUtil.removeEventListener(document, "selectstart", TucanaWidgetDash.DragSession.processSelectStart, false);
    }
    
    if (this.widgetDashInstance.returnMethod == "drift" 
    	&& (this.driftFunc = this.createDriftPath())) {
        this.driftTimer = setInterval("TucanaWidgetDash.DragSession.driftBackStep()", this.widgetDashInstance.driftInterval);
    } else {
        this.finalizeSession();
    }
};

/**
 * Create a function that animate a widget along a path from its drop point
 * to its destination
 * 
 * @return The drift function.
 */
TucanaWidgetDash.DragSession.prototype.createDriftPath = function() {
    var widgetPosition = this.widgetDashInstance.getElementPosition(this.widgetContainer);
    var shadowPosition = this.widgetDashInstance.getElementPosition(this.widgetShadow);
    var y1 = shadowPosition.top, y2 = widgetPosition.top;
    var x1 = shadowPosition.left, x2 = widgetPosition.left;
    var stepAmount = this.widgetDashInstance.driftStep;
	
	// Special case: if the widget is less than the step amount from the destination,
	// we can't really drift
	
	if (Math.abs(x2 - x1) <= stepAmount && Math.abs(y2 -y1) <= stepAmount) {
		return null;
	}
	
    var theta = Math.atan((y2 - y1) / (x2 - x1));
	var direction = (x1 - x2 <= 0) ? -1 : 1;
	var stepX = stepAmount * Math.cos(theta) * direction;
	var stepY = stepAmount * Math.sin(theta) * direction;
	
	// Create step function

	var funcWidget = this.widgetContainer;
	
	/**
	 * The drift function.  Each time the function is invoked, the 
	 * widget is positioned one step further along its path.  If the path
	 * is finished, the function returns false.
	 * 
	 * @return false if the path is finished, true if not.
	 */
	var stepFunc = function() {
		var hyp = theta == 0 ? (widgetPosition.left - shadowPosition.left) 
			: (widgetPosition.top - shadowPosition.top) / Math.sin(theta);
		if (Math.abs(hyp) <= stepAmount) {
			funcWidget.style.left = shadowPosition.left + "px";
			funcWidget.style.top = shadowPosition.top + "px";
			return false;
		} else {
			widgetPosition.left += stepX;
			widgetPosition.top += stepY;
			funcWidget.style.left = widgetPosition.left + "px";
			funcWidget.style.top = widgetPosition.top + "px";
			return true;
		}
	};
	
	return stepFunc;
};

/**
 * Static method called by drift interval.  This method invokes the current 
 * drift function set on the active DragSession.
 */
TucanaWidgetDash.DragSession.driftBackStep = function() {
    var instance = TucanaWidgetDash.DragSession.activeInstance;	
	if (!instance.driftFunc()) {
		clearInterval(instance.driftTimer);
		instance.finalizeSession();
	}
};

/**
 * Move the dragged widget to the position held by its shadow, and reset its
 * style to the original settings.  Notify widgets of their new positions.
 */
TucanaWidgetDash.DragSession.prototype.finalizeSession = function() {
    TucanaWidgetDash.DragSession.activeInstance = null;    
    TucanaWidgetDash.copyStyle(this.containerStyleCopy, this.widgetContainer.style);

    this.widgetShadow.parentNode.replaceChild(this.widgetContainer, this.widgetShadow);

    EchoVirtualPosition.redraw();
    this.widgetDashInstance.updatePositions(true);
    this.widgetDashInstance.updateWidgetSpacing();
};

/**
 * Process a mousemove event for a particular DragSession instance. The mousemove event
 * updates the widget shadow position to reflect the closest widget drop point.
 * 
 * @param e The mousemove event.
 */
TucanaWidgetDash.DragSession.prototype.processMouseMove = function(e) {
    var cursorPosition = this.widgetDashInstance.getCursorPosition(e);
    this.widgetContainer.style.left = cursorPosition.x - this.initialOffsetX + "px"; 
    this.widgetContainer.style.top = cursorPosition.y - this.initialOffsetY + "px";
    this.moveShadow();
};

/**
 * Create a "shadow" of the widget to be dragged.  The shadow is an element that
 * is placed where the widget would fall if dropped.  Currently, a shadow can be 
 * either a CSS border, or a clone of the original widget (with optional opacity
 * settings).
 */
TucanaWidgetDash.DragSession.prototype.createShadow = function() {
    var shadow;
    var widgetElement = document.getElementById(this.widgetContainerId);
    if (this.widgetDashInstance.shadowType == "border") {
        // This is a bit of a hack to get a border to be inside a div the same
        // dimensions of the dragged widget.  Naturally, a border around a div
        // would be outside the dimensions of the widget.  However, putting the
        // border around an embedded table cell keeps it inside the dimensions.
        shadow = document.createElement("div");
        shadow.style.height = widgetElement.offsetHeight + "px";

        shadow.id = this.widgetDashId + "_widgetShadow";
        var innerTable = document.createElement("table");
        shadow.appendChild(innerTable);
        innerTable.style.width = "100%";
        innerTable.style.height = "100%";
        innerTable.style.borderSpacing = "0px";
     	if (EchoClientProperties.get("browserInternetExplorer")) {
            innerTable.style.borderCollapse = "collapse";
     	}
     	var innerTbody = document.createElement("tbody");
        innerTable.appendChild(innerTbody);
        var innerTr = document.createElement("tr");
        innerTbody.appendChild(innerTr);
        var innerTd = document.createElement("td");
        innerTr.appendChild(innerTd);
        innerTd.style.border = this.widgetDashInstance.shadowBorder;
    } else {
        shadow = widgetElement.cloneNode(true);
        TucanaWidgetDash.setOpacity(shadow, this.widgetDashInstance.shadowOpacity);
        var shadowChildren = shadow.getElementsByTagName("*");
        for (var i in shadowChildren) {
            shadowChildren[i].id = shadow.id + "_" + shadowChildren[i].id;
        }
    }
    shadow.id = this.widgetDashId + "_widgetShadow";

    return shadow; 
};

/**
 * Move the dragged widget's shadow to the closest possible drop position 
 * in the WidgetDash.
 */
TucanaWidgetDash.DragSession.prototype.moveShadow = function() {
    
    var closestDistance = 10000000;
    var closestContainer = null;
    

    for (var containerId in this.containerPositions) {
        var container = this.containerPositions[containerId].container;
        var position = this.containerPositions[containerId].position;
        var distance = Math.sqrt(Math.pow(this.widgetContainer.offsetLeft - position.left, 2) + Math.pow(this.widgetContainer.offsetTop - position.top, 2));
        if (distance < closestDistance) {
            closestDistance = distance;
            closestContainer = container;
        }
    }
    
    if (closestContainer != null && closestContainer != this.lastClosestContainer) {
        closestContainer.parentNode.insertBefore(this.widgetShadow, closestContainer);
        this.widgetDashInstance.updateWidgetSpacing();
        if (!TucanaWidgetDash.isColumnTerminator(this.widgetShadow.nextSibling)) {
            this.widgetShadow.style.marginBottom = this.widgetDashInstance.widgetSpacing;
        } else {
            this.widgetShadow.style.marginBottom = null;            
        }
        this.lastClosestContainer = closestContainer;
    }
};

/**
 * Create the widget's shadow, detach the widget from the panel (make its positioning
 * absolute), and insert the shadow where the widget was initially.
 */
TucanaWidgetDash.DragSession.prototype.copyAndDetachWidget = function() {

    // Make a copy of the widget's current style.
    this.containerStyleCopy = new Object();
    // this.containerStyleCopy = TucanaWidgetDash.copyStyle(this.widgetContainer.style);
	var localContainerStyleCopy = this.containerStyleCopy;
	var localWidgetContainer = this.widgetContainer;
	var copyStyle = function(styleName) {
		localContainerStyleCopy[styleName] = localWidgetContainer.style[styleName];
	};

	copyStyle("left");
	copyStyle("top");
    // Create the widget shadow.
    this.widgetShadow = this.createShadow();

    var offsetWidth = this.widgetContainer.offsetWidth;
    var offsetHeight = this.widgetContainer.offsetHeight;

    // Set the shadow's margins to be the same as the widget's margins.

    // Place the widget shadow in the starting position of the widget
    this.widgetContainer.parentNode.replaceChild(this.widgetShadow, this.widgetContainer);

    // Move the widget to the body element and position it absolutely
	copyStyle("position");
    this.widgetContainer.style.position = "absolute";
    var containingElement = this.widgetDashInstance.getDraggingContainer();
    containingElement.appendChild(this.widgetContainer);   

    // Set dimensions that were enforced by the widget's column
	copyStyle("width");
	copyStyle("height");
    this.widgetContainer.style.width = offsetWidth + "px";
//    this.widgetContainer.style.height = offsetHeight + "px";

    // lower the widget's opacity
	if (EchoClientProperties.get("browserInternetExplorer")) {
		copyStyle("filter");
		copyStyle("zoom");
	} else {
		copyStyle("opacity");
	}
    TucanaWidgetDash.setOpacity(this.widgetContainer, this.widgetDashInstance.widgetOpacity);
};

/**
 * Determine the positions of the widgets in the WidgetDash.  For most of the widgets, the
 * position will simply be the value returned by TucanaWidgetDash.getElementPosition().
 * However, for widgets under the dragged widget, the position will be shifted upwards by the
 * height of the dragged widget.
 */
TucanaWidgetDash.DragSession.prototype.initContainerPositions = function() {
    this.containerPositions = new Object();
    
    var containers = TucanaWidgetDash.getInstance(this.widgetDashId).getWidgetContainers(true);
    for (var i = 0; i < containers.length; i++) {
        var container = containers[i];
        if (container == this.widgetContainer) continue;
        var position = this.widgetDashInstance.getElementPosition(container);
        this.containerPositions[container.id] = { container: container, position: position };
    }

    var sibling = this.widgetContainer.nextSibling;
    while (sibling) {
        this.containerPositions[sibling.id].position.top -= this.widgetContainer.offsetHeight;
        sibling = sibling.nextSibling;
    }
};

// Message Processor

/**
 * Namespace for server->client message processor
 */
TucanaWidgetDash.MessageProcessor = function() { };

/** Static message processing method.  This method delegates to specific handlers.
 * 
 * @param messagePartElement message element to process
 */
TucanaWidgetDash.MessageProcessor.process = function(messagePartElement) {
    for (var i = 0; i < messagePartElement.childNodes.length; ++i) {
        if (messagePartElement.childNodes[i].nodeType == 1) {
            switch (messagePartElement.childNodes[i].tagName) {
            case "init":
                TucanaWidgetDash.MessageProcessor.processInit(messagePartElement.childNodes[i]);
                break;
            case "dispose":
                TucanaWidgetDash.MessageProcessor.processDispose(messagePartElement.childNodes[i]);
                break;
            case "update":
                TucanaWidgetDash.MessageProcessor.processUpdate(messagePartElement.childNodes[i]);
                break;
            }
        }
    }
};

/**
 * Processes a <code>dispose</code> message to finalize the state of a
 * widgetpanel component that is being removed.
 *
 * @param disposeMessageElement the <code>dispose</code> element to process
 */
TucanaWidgetDash.MessageProcessor.processDispose = function(disposeMessageElement) {

};

/**
 * Processes an <code>init</code> message to initialize the state of a 
 * widgetpanel component that is being added.
 *
 * @param initMessageElement the <code>init</code> element to process
 */
TucanaWidgetDash.MessageProcessor.processInit = function(initMessageElement) {
    var widgetDashId = initMessageElement.getAttribute("eid");
    var containerId = initMessageElement.getAttribute("container-eid");    
    var widgetDash = new TucanaWidgetDash(widgetDashId, containerId);
    
    var propertiesElements = initMessageElement.getElementsByTagName("properties");
    widgetDash.create();
    if (propertiesElements && propertiesElements.length > 0) {
        widgetDash.processProperties(propertiesElements[0]);
    }
};

/**
 * Processes an <code>update</code> message to update the state of a 
 * widgetpanel component.
 *
 * @param updateMessageElement the <code>init</code> element to process
 */
TucanaWidgetDash.MessageProcessor.processUpdate = function(updateMessageElement) {
    var widgetDashId = updateMessageElement.getAttribute("eid");
    var widgetDashInstance = TucanaWidgetDash.getInstance(widgetDashId);
    var widgetDashElement = document.getElementById(widgetDashId);
    
    var propertiesElement = updateMessageElement.getElementsByTagName("properties")[0];
    if (propertiesElement) {
        widgetDashInstance.processProperties(propertiesElement);
    }
};


