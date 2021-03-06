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
import java.io.OutputStream;
import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.karora.cooee.app.filetransfer.Download;
import org.karora.cooee.app.filetransfer.DownloadProvider;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webrender.Connection;
import org.karora.cooee.webrender.ContentType;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.WebRenderServlet;

/**
 * A service that outputs a <code>Download</code> component's file.
 */
class DownloadService implements Service, Serializable {

    private static final String SERVICE_ID = "Echo.Download"; 
    
    private static final String PARAMETER_DOWNLOAD_UID = "downloaduid"; 
    private static final String[] URL_PARAMETERS = new String[]{PARAMETER_DOWNLOAD_UID}; 
    
    public static final DownloadService INSTANCE = new DownloadService();
    
    static {
        WebRenderServlet.getServiceRegistry().add(INSTANCE);
    }
    
    /**
     * Creates a URI to execute a specific <code>DownloadProvider</code>
     * 
     * @param containerInstance the relevant application container instance.
     * @param downloadId the unique id to retrieve the download from the
     *        <code>ContainerInstance</code>
     */
    public String createUri(ContainerInstance containerInstance, String downloadId) {
        return containerInstance.getServiceUri(this, URL_PARAMETERS, new String[]{downloadId});
    }
    
    /**
     * @see org.karora.cooee.webrender.Service#getId()
     */
    public String getId() {
        return SERVICE_ID;
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
        String downloadId = conn.getRequest().getParameter(PARAMETER_DOWNLOAD_UID);
        if (downloadId == null) {
            serviceBadRequest(conn, "Download UID not specified.");
            return;
        }
        Download download = DownloadPeer.getDownload(downloadId);
        
        if (download == null) {
            serviceBadRequest(conn, "Download UID is not valid.");
            return;
        }
        service(conn,download);
        
        DownloadPeer.removeDownload(downloadId);
    }
    
    public void service(Connection conn, Download download) throws IOException {
        OutputStream out = conn.getOutputStream();
        DownloadProvider provider = download.getProvider();
        HttpServletResponse response = conn.getResponse();
        
        String header = "";
        if (download.getStreamInline() == true)
        {
            header = "inline";
        }
        else
        {
            header = "attachment";
        }
        
        
        if (provider.getFileName() != null)
        {
            header += "; filename=\"" + provider.getFileName() + "\"";
        }
        
        response.setHeader("Content-Disposition", header);
        
        if (provider.getSize() > 0) {
            response.setIntHeader("Content-Length", provider.getSize());
        }
        String contentType = provider.getContentType();
        if (contentType == null) {
            response.setContentType("application/octet-stream");
        } else {
            response.setContentType(provider.getContentType());
        }
        response.setHeader("Cache-Control", "");
        response.setHeader("Pragma", "");
        provider.writeFile(out);
    }
    
    public void serviceBadRequest(Connection conn, String message) {
        conn.getResponse().setStatus(HttpServletResponse.SC_BAD_REQUEST);
        conn.setContentType(ContentType.TEXT_PLAIN);
        conn.getWriter().write(message);
    }

}
