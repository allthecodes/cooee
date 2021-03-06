/* 
 * This file is part of the Echo2 Chart Library.
 * Copyright (C) 2005 NextApp, Inc.
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

//__________________
// Object Echo2Chart

/**
 * Static object/namespace for ChartDisplay support.
 * This object/namespace should not be used externally.
 */
Echo2Chart = function() { };

/**
 * Static object/namespace for ChartDisplay MessageProcessor 
 * implementation.
 */
Echo2Chart.MessageProcessor = function() { };

/**
 * MessageProcessor process() implementation 
 * (invoked by ServerMessage processor).
 *
 * @param messagePartElement the <code>message-part</code> element to process.
 */
Echo2Chart.MessageProcessor.process = function(messagePartElement) {
    for (var i = 0; i < messagePartElement.childNodes.length; ++i) {
        if (messagePartElement.childNodes[i].nodeType == 1) {
            switch (messagePartElement.childNodes[i].tagName) {
            case "set-image":
                Echo2Chart.MessageProcessor.processSetImage(messagePartElement.childNodes[i]);
                break;
            }
        }
    }
};

Echo2Chart.MessageProcessor.processSetImage = function(setImageElement) {
    var chartId = setImageElement.getAttribute("eid");
    var newImage = setImageElement.getAttribute("new-image");
    var imgElement = document.getElementById(chartId + "_image");
    imgElement.setAttribute("src", newImage);
};