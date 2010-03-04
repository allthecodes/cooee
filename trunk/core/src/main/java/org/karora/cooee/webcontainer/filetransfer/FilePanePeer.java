package org.karora.cooee.webcontainer.filetransfer;

import org.karora.cooee.app.Component;
import org.karora.cooee.app.filetransfer.DownloadProvider;
import org.karora.cooee.app.filetransfer.FilePane;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.DomUpdateSupport;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webrender.servermessage.DomUpdate;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FilePanePeer 
implements ComponentSynchronizePeer, DomUpdateSupport {
    
    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#getContainerId(Component)
     */
    public String getContainerId(Component child) {
        throw new UnsupportedOperationException("Component does not support children.");
    }
    
    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderAdd(RenderContext,
     *      ServerComponentUpdate, String, Component)
     */
    public void renderAdd(RenderContext rc, ServerComponentUpdate update, String targetId, Component component) {
        Element domAddElement = DomUpdate.renderElementAdd(rc.getServerMessage());
        DocumentFragment htmlFragment = rc.getServerMessage().getDocument().createDocumentFragment();
        renderHtml(rc, update, htmlFragment, component);
        DomUpdate.renderElementAddContent(rc.getServerMessage(), domAddElement, targetId, htmlFragment);
    }

    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderDispose(org.karora.cooee.webcontainer.RenderContext,
     *      org.karora.cooee.app.update.ServerComponentUpdate,
     *      org.karora.cooee.app.Component)
     */
    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {
        // Do nothing.
    }

    public void renderHtml(RenderContext rc, ServerComponentUpdate update, Node parentNode, Component component) {
        // Note: Embedding the file pane in a DIV is necessary for proper rendeirng on Internet Explorer 7.
        // Test all modifications with IE7, it's very finnicky about height/width attributes.
        
        FilePane filePane = (FilePane) component;
        String elementId = ContainerInstance.getElementId(filePane);
        ServerMessage serverMessage = rc.getServerMessage();
        Document document = serverMessage.getDocument();
        Element divElement = document.createElement("div");
        divElement.setAttribute("id", elementId);
        
        divElement.setAttribute("style", "background:white;position:absolute;width:100%;height:100%;overflow:hidden;");
        
        Element filePaneIFrameElement = document.createElement("iframe");
        filePaneIFrameElement.setAttribute("style", "position:absolute;overflow:hidden;");
        filePaneIFrameElement.setAttribute("width", "100%");
        filePaneIFrameElement.setAttribute("height", "100%");
        DownloadProvider provider = filePane.getProvider();
        if (provider == null) {
            filePaneIFrameElement.setAttribute("src", "about:blank");
        } else {
            filePaneIFrameElement.setAttribute("src", FilePaneService.INSTANCE.createUri(rc.getContainerInstance(),
                    ContainerInstance.getElementId(filePane)));
        }

        divElement.appendChild(filePaneIFrameElement);
        
        parentNode.appendChild(divElement);
    }

    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderUpdate(org.karora.cooee.webcontainer.RenderContext,
     *      org.karora.cooee.app.update.ServerComponentUpdate, java.lang.String)
     */
    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
        DomUpdate.renderElementRemove(rc.getServerMessage(), ContainerInstance.getElementId(update.getParent()));
        renderAdd(rc, update, targetId, update.getParent());
        return true;
    }
}
