/* 
 * This file is part of the Echo File Transfer Library (hereinafter "EFTL").
 * Copyright (C) 2002-2005 NextApp, Inc.
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

package org.karora.cooee.webcontainer.filetransfer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.karora.cooee.app.ApplicationInstance;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.filetransfer.UploadSelect;
import org.karora.cooee.app.util.Messages;
import org.karora.cooee.webcontainer.ContainerContext;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.propertyrender.ColorRender;
import org.karora.cooee.webrender.ClientProperties;
import org.karora.cooee.webrender.Connection;
import org.karora.cooee.webrender.ContentType;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.output.CssStyle;
import org.karora.cooee.webrender.output.XmlDocument;
import org.karora.cooee.webrender.service.JavaScriptService;
import org.w3c.dom.Element;

/**
 * Renders an HTML page containing a form with a single file upload element 
 * and an optional Send button, based on the <code>UploadSelect</code> that 
 * is given.
 */
public class UploadFormService implements Serializable, Service {

    private static final String SERVICE_ID = "Echo.UploadForm"; 
    public static final String FILE_PARAMETER_NAME = SERVICE_ID + ".File";
    
    private static final String PARAMETER_UPLOAD_FORM_UID = "uploadformUid"; 
    private static final String[] URL_PARAMETERS = new String[]{PARAMETER_UPLOAD_FORM_UID}; 
    
    private static final String RESOURCE_BUNDLE_BASE_NAME 
            = "org.karora.cooee.filetransfer.webcontainer.resource.bundle.UploadSelectMessages";
    private static final String SEND_TEXT_KEY = "upload.send.default";
    private static final String WAIT_TEXT_KEY = "upload.send.wait";
    
    
    public static final UploadFormService INSTANCE = new UploadFormService();
    
    /**
     * Service to provide supporting JavaScript library.
     */
    public static final Service UPLOAD_SERVICE = JavaScriptService.forResource("Echo.UploadComponent", 
            "/org/karora/cooee/filetransfer/webcontainer/resource/js/Upload.js");

    static {
        WebRenderServlet.getServiceRegistry().add(UPLOAD_SERVICE);
        WebRenderServlet.getServiceRegistry().add(INSTANCE);
    }
    
    /**
     * Creates a URI to execute a specific <code>UploadFormService</code>
     * 
     * @param containerInstance the relevant application container instance.
     * @param imageId the unique id to retrieve the image from the
     *        <code>ContainerInstance</code>
     */
    public String createUri(ContainerInstance containerInstance, String imageId) {
        return containerInstance.getServiceUri(this, URL_PARAMETERS, new String[]{imageId});
    }

    /**
     * @see org.karora.cooee.webrender.Service#getId()
     */
    public String getId() {
        return PARAMETER_UPLOAD_FORM_UID;
    }
    
    /**
     * @see org.karora.cooee.webrender.Service#getVersion()
     */
    public int getVersion() {
        return DO_NOT_CACHE;
    }
    
    /**
     * @see org.karora.cooee.webrender.Service#service(Connection)
     */
    public void service(Connection conn) throws IOException {
        ContainerInstance containerInstance = (ContainerInstance)conn.getUserInstance();
        if (containerInstance == null) {
            serviceBadRequest(conn, "No container available.");
            return;
        }
        String uploadId = conn.getRequest().getParameter(PARAMETER_UPLOAD_FORM_UID);
        if (uploadId == null) {
            serviceBadRequest(conn, "Upload UID not specified.");
            return;
        }
        UploadSelect upload = (UploadSelect) containerInstance.getIdTable().getObject(uploadId);
        
        if (upload == null) {
            serviceBadRequest(conn, "Upload UID is not valid.");
            return;
        }
        service(conn,upload);
    }
    
    protected void service(Connection conn, UploadSelect uploadSelect) throws IOException {
        
        ContainerInstance ci = (ContainerInstance)conn.getUserInstance();
        ApplicationInstance app = uploadSelect.getApplicationInstance();
        
        XmlDocument doc = new XmlDocument("html",null,null,"http://www.w3.org/TR/xhtml1");
        
        Element head = doc.getDocument().createElement("head");
        doc.getDocument().getDocumentElement().appendChild(head);

        Element body = doc.getDocument().createElement("body");
        doc.getDocument().getDocumentElement().appendChild(body);
        body.setAttribute("onload","EchoUploadComponent.synchronize('" + ContainerInstance.getElementId(uploadSelect) + "');");
               
        Element script = doc.getDocument().createElement("script");
        head.appendChild(script);
        String uploadScriptUrl = ci.getServiceUri(UPLOAD_SERVICE);
        script.setAttribute("language","JavaScript");        
        script.setAttribute("src",uploadScriptUrl);        
        // IE has issues with "<script ... />" (no end tag) 
        // The following whitespace will force the DOM to render "</script>"
        script.appendChild(doc.getDocument().createTextNode(" "));
        
        CssStyle style = new CssStyle();
        //style.setAttribute("text-align", "center");
        style.setAttribute("vertical-align", "center");
        style.setAttribute("border","none");
        
        ColorRender.renderToStyle(style,(Color)uploadSelect.getRenderProperty(UploadSelect.PROPERTY_FOREGROUND),(Color)uploadSelect.getRenderProperty(UploadSelect.PROPERTY_BACKGROUND));
        body.setAttribute("style",style.renderInline());
        
        String id = uploadSelect.getRenderId();
        ci.getIdTable().register(uploadSelect);
        String actionUrl = UploadReceiver.INSTANCE.createUri(ci,id);
        
        Element form = doc.getDocument().createElement("form");
        body.appendChild(form);
        form.setAttribute("method", "POST");
        form.setAttribute("enctype", "multipart/form-data");
        form.setAttribute("action", actionUrl);
        
        Element fileInput = doc.getDocument().createElement("input");
        form.appendChild(fileInput);
        fileInput.setAttribute("type", "file");
        fileInput.setAttribute("name", FILE_PARAMETER_NAME);
        fileInput.setAttribute("size", "20");
        if (!uploadSelect.isEnabled()) 
        {
            fileInput.setAttribute("disabled", "true");
        }
        
        // Auto Send if no separate send button is being rendered
        if (uploadSelect.isSendButtonDisplayed() == false) {
            fileInput.setAttribute("onchange", "document.forms[0].submit()");
        }
        
        if (uploadSelect.isSendButtonDisplayed()) {
            String enabledText = ensureValue(uploadSelect.getEnabledSendButtonText(), Messages.getString(SEND_TEXT_KEY));
            String disabledText = ensureValue(uploadSelect.getDisabledSendButtonText(),Messages.getString(WAIT_TEXT_KEY));
            
            Element sendButton = doc.getDocument().createElement("input");
            form.appendChild(sendButton);
            sendButton.setAttribute("name", "sendButton");
            sendButton.setAttribute("onclick", "EchoUploadComponent.upload('" + enabledText + "','" + disabledText + "')");
            sendButton.setAttribute("type", "button");
            sendButton.setAttribute("value", enabledText);
        }
        
        conn.setContentType(ContentType.TEXT_HTML);
        PrintWriter pw = conn.getWriter();
        doc.render(pw);
        pw.close();
    }

    
    protected String ensureValue(String value, String defaultValue){
        if (value == null){
            return defaultValue;
        } else {
            return value;
        }
    }
    
    protected void serviceBadRequest(Connection conn, String message) {
        conn.getResponse().setStatus(HttpServletResponse.SC_BAD_REQUEST);
        conn.setContentType(ContentType.TEXT_PLAIN);
        conn.getWriter().write(message);
    }
    
   
}
