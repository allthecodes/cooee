/* 
 * This file is part of the Echo Web Application Framework (hereinafter "Echo").
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

package org.karora.cooee.webcontainer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.karora.cooee.app.ApplicationInstance;
import org.karora.cooee.app.Command;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Window;
import org.karora.cooee.app.update.PropertyUpdate;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.app.update.ServerUpdateManager;
import org.karora.cooee.app.update.UpdateManager;
import org.karora.cooee.webcontainer.syncpeer.WindowPeer;
import org.karora.cooee.webrender.Connection;
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.UserInstance;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.servermessage.WindowUpdate;
import org.karora.cooee.webrender.service.JavaScriptService;
import org.karora.cooee.webrender.service.SynchronizeService;
import org.karora.cooee.webrender.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;



/**
 * A service which synchronizes the state of the client with that of the
 * server.  Requests made to this service are in the form of "ClientMessage"
 * XML documents which describe the users actions since the last 
 * synchronization, e.g., input typed into text fields and the action taken
 * (e.g., a button press) which caused the server interaction.
 * The service then communicates these changes to the server-side application,
 * and then generates an output "ServerMessage" containing instructions to 
 * update the client-side state of the application to the updated server-side
 * state.
 * <p>
 * This class is derived from the base class <code>SynchronizeService</code>
 * of the web renderer, which handles the lower-level work.
 */
public class ContainerSynchronizeService extends SynchronizeService {
    
    private static final Logger classLog = Logger.getLogger(ContainerSynchronizeService.class);

    /**
     * Flag indicating whether client/server messages should be dumped to console.
     */
    public static final boolean DEBUG_PRINT_MESSAGES_TO_CONSOLE;
    static {
        boolean value;
        try {
            value = "true".equals(System.getProperty("cooee.syncdump"));
        } catch (SecurityException ex) {
            value = false;
        }
        DEBUG_PRINT_MESSAGES_TO_CONSOLE = value;
    }
    
    /**
     * Service to provide supporting JavaScript library.
     */
    public static final Service WEB_CONTAINER_SERVICE = JavaScriptService.forResource("Echo.WebContainer",
            "/org/karora/cooee/webcontainer/resource/js/WebContainer.js");

    static {
        WebRenderServlet.getServiceRegistry().add(WEB_CONTAINER_SERVICE);
    }

    /**
     * A single shared instance of this stateless service.
     */
    public static final ContainerSynchronizeService INSTANCE = new ContainerSynchronizeService();

    /**
     * Determines if any of the <code>Component</code> object in the provided 
     * set of "potential" ancestors is in fact an ancestor of 
     * <code>component</code>.
     * 
     * @param potentialAncestors a set containing <code>Component</code>s
     * @param component the <code>Component</code> to evaluate
     * @return true if any component in <code>potentialAncestors</code> is an
     *         ancestor of <code>component</code>
     */
    private static boolean isAncestor(Set potentialAncestors, Component component) {
        component = component.getParent();
        while (component != null) {
            if (potentialAncestors.contains(component)) {
                return true;
            }
            component = component.getParent();
        }
        return false;
    }

    /**
     * <code>ClientMessagePartProcessor</code> to process user-interface 
     * component input message parts.
     */
    private ClientMessagePartProcessor propertyUpdateProcessor = new ClientMessagePartProcessor() {
        
        /**
         * @see org.karora.cooee.webrender.service.SynchronizeService.ClientMessagePartProcessor#getName()
         */
        public String getName() {
            return "EchoPropertyUpdate";
        }
        
        /**
         * @see org.karora.cooee.webrender.service.SynchronizeService.ClientMessagePartProcessor#process(
         *      org.karora.cooee.webrender.UserInstance, org.w3c.dom.Element)
         */
        public void process(UserInstance userInstance, Element messagePartElement) {
            ContainerInstance ci = (ContainerInstance) userInstance;
            Element[] propertyElements = DomUtil.getChildElementsByTagName(messagePartElement, "property");
            for (int i = 0; i < propertyElements.length; ++i) {
                String componentId = propertyElements[i].getAttribute("component-id");
                Component component = ci.getComponentByElementId(componentId);
                if (component == null) {
                    // Component removed.  This should not frequently occur, however in certain cases,
                    // e.g., dragging a window during an during before, during, after a server pushed update
                    // can result in the condition where input is received from a component which no longer
                    // is registered.
                    continue;
                }
                ComponentSynchronizePeer syncPeer = SynchronizePeerFactory.getPeerForComponent(component.getClass());
                if (!(syncPeer instanceof PropertyUpdateProcessor)) {
                    throw new IllegalStateException("Target peer is not an PropertyUpdateProcessor.");
                }
                ((PropertyUpdateProcessor) syncPeer).processPropertyUpdate(ci, component, propertyElements[i]);
            }
        }
    };

    /**
     * <code>ClientMessagePartProcessor</code> to process user-interface 
     * component action message parts.
     */
    private ClientMessagePartProcessor actionProcessor = new ClientMessagePartProcessor() {
        
        /**
         * @see org.karora.cooee.webrender.service.SynchronizeService.ClientMessagePartProcessor#getName()
         */
        public String getName() {
            return "EchoAction";
        }
        
        /**
         * @see org.karora.cooee.webrender.service.SynchronizeService.ClientMessagePartProcessor#process(
         *      org.karora.cooee.webrender.UserInstance, org.w3c.dom.Element)
         */
        public void process(UserInstance userInstance, Element messagePartElement) {
            ContainerInstance ci = (ContainerInstance) userInstance;
            Element actionElement = DomUtil.getChildElementByTagName(messagePartElement, "action");
            String componentId = actionElement.getAttribute("component-id");
            Component component = ci.getComponentByElementId(componentId);
            if (component == null) {
                // Component removed.  This should not frequently occur, however in certain cases,
                // e.g., dragging a window during an during before, during, after a server pushed update
                // can result in the condition where input is received from a component which no longer
                // is registered.
                return;
            }
            ComponentSynchronizePeer syncPeer = SynchronizePeerFactory.getPeerForComponent(component.getClass());
            if (!(syncPeer instanceof ActionProcessor)) {
                throw new IllegalStateException("Target peer is not an ActionProcessor.");
            }
            ((ActionProcessor) syncPeer).processAction(ci, component, actionElement);
        }
    };
    
    /**
     * Creates a new <code>ContainerSynchronizeService</code>.
     * Installs "ClientMessage" part processors.
     */
    private ContainerSynchronizeService() {
        super();
        registerClientMessagePartProcessor(propertyUpdateProcessor);
        registerClientMessagePartProcessor(actionProcessor);
    }
    
    /**
     * Performs disposal operations on components which have been removed from
     * the hierarchy. Removes any <code>RenderState</code> objects being
     * stored in the <code>ContainerInstance</code> for the disposed
     * components. Invokes <code>ComponentSynchronizePeer.renderDispose()</code>
     * such that the peers of the components can dispose of resources on the
     * client.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param componentUpdate the <code>ServerComponentUpdate</code> causing
     *        components to be disposed.
     * @param disposedComponents the components to dispose
     */
    private void disposeComponents(RenderContext rc, ServerComponentUpdate componentUpdate, 
            Component[] disposedComponents) {
        ContainerInstance ci = rc.getContainerInstance();
        for (int i = 0; i < disposedComponents.length; ++i) {
            ComponentSynchronizePeer disposedSyncPeer = SynchronizePeerFactory.getPeerForComponent(
                    disposedComponents[i].getClass());
            disposedSyncPeer.renderDispose(rc, componentUpdate, disposedComponents[i]);
            ci.removeRenderState(disposedComponents[i]);
        }
    }
    
    /**
     * Invokes <code>renderDispose()</code> on 
     * <code>ComponentSynchronizePeer</code>s in a hierarchy of Components that is
     * be re-rendered on the client.  That is, this hierarchy of components exist on 
     * the client, are being removed, and will be re-rendered due to a container 
     * component NOT being capable of rendering a partial update.
     * This method is invoked recursively.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param update the update
     * @param parent the <code>Component</code> whose descendants should be disposed
     */
    private void disposeReplacedDescendants(RenderContext rc, ServerComponentUpdate update, Component parent) {
        Component[] replacedComponents = parent.getVisibleComponents();
        boolean isRoot = parent == update.getParent();
        for (int i = 0; i < replacedComponents.length; ++i) {
            // Verify that component was not added on this synchronization.
            if (isRoot && update.hasAddedChild(replacedComponents[i])) {
                // Component was added as a child on this synchronization:
                // There is no reason to dispose of it as it does not yet exist on the client.
                continue;
            }
            
            // Recursively dispose child components.
            disposeReplacedDescendants(rc, update, replacedComponents[i]);
            
            // Dispose component.
            ComponentSynchronizePeer syncPeer = SynchronizePeerFactory.getPeerForComponent(replacedComponents[i].getClass());
            syncPeer.renderDispose(rc, update, replacedComponents[i]);
        }
    }
    
    /**
     * Determines if the specified <code>component</code> has been rendered to
     * the client by determining if it is a descendant of any
     * <code>LazyRenderContainer</code>s and if so querying them to determine
     * the hierarchy's render state. This method is recursively invoked.
     * 
     * @param ci the relevant <code>ContainerInstance</code>
     * @param component the <code>Component</code> to analyze
     * @return <code>true</code> if the <code>Component</code> has been
     *         rendered to the client
     */
    private boolean isRendered(ContainerInstance ci, Component component) {
        Component parent = component.getParent();
        if (parent == null) {
            return true;
        }
        ComponentSynchronizePeer syncPeer = SynchronizePeerFactory.getPeerForComponent(parent.getClass());
        if (syncPeer instanceof LazyRenderContainer) {
            boolean rendered = ((LazyRenderContainer) syncPeer).isRendered(ci, parent, component);
            if (!rendered) {
                return false;
            }
        }
        return isRendered(ci, parent);
    }
    
    /**
     * Retrieves information about the current focused component on the client,
     * if provided, and in such case notifies the 
     * <code>ApplicationInstance</code> of the focus.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param clientMessageDocument the ClientMessage <code>Document</code> to 
     *        retrieve focus information from
     */
    private void processClientFocusedComponent(RenderContext rc, Document clientMessageDocument) {
        if (clientMessageDocument.getDocumentElement().hasAttribute("focus")) {
            String focusedComponentId = clientMessageDocument.getDocumentElement().getAttribute("focus");
            Component component = null;
            if (focusedComponentId.length() > 2) {
                // Valid component id.
                component = rc.getContainerInstance().getComponentByElementId(focusedComponentId);
            }
            ApplicationInstance applicationInstance = rc.getContainerInstance().getApplicationInstance();
            applicationInstance.getUpdateManager().getClientUpdateManager().setApplicationProperty(
                    ApplicationInstance.FOCUSED_COMPONENT_CHANGED_PROPERTY, component);
        }
    }
    
    /**
     * Handles an invalid transaction id scenario, reinitializing the entire 
     * state of the client.
     * 
     * @param rc the relevant <code>RenderContex</code>
     */
    private void processInvalidTransaction(RenderContext rc) {
        WindowUpdate.renderReload(rc.getServerMessage());
    }
    
    /**
     * Executes queued <code>Command</code>s.
     * 
     * @param rc the relevant <code>RenderContext</code>
     */
    private void processQueuedCommands(RenderContext rc) {
        ServerUpdateManager serverUpdateManager = rc.getContainerInstance().getUpdateManager().getServerUpdateManager();
        Command[] commands = serverUpdateManager.getCommands();
        for (int i = 0; i < commands.length; i++) {
            CommandSynchronizePeer peer = SynchronizePeerFactory.getPeerForCommand(commands[i].getClass()); 
            peer.render(rc, commands[i]);
        }
    }
    
    /**
     * Processes updates from the application, generating an outgoing
     * <code>ServerMessage</code>.
     * 
     * @param rc the relevant <code>RenderContext</code>
     */
    private void processServerUpdates(RenderContext rc) {
        ContainerInstance ci = rc.getContainerInstance();
        UpdateManager updateManager = ci.getUpdateManager();
        ServerUpdateManager serverUpdateManager = updateManager.getServerUpdateManager();
        ServerComponentUpdate[] componentUpdates = updateManager.getServerUpdateManager().getComponentUpdates();
        
        if (serverUpdateManager.isFullRefreshRequired()) {
            Window window = rc.getContainerInstance().getApplicationInstance().getDefaultWindow();
            ServerComponentUpdate fullRefreshUpdate = componentUpdates[0];
            
            // Dispose of removed descendants.
            Component[] removedDescendants = fullRefreshUpdate.getRemovedDescendants();
            disposeComponents(rc, fullRefreshUpdate, removedDescendants);
            
            // Perform full refresh.
            RootSynchronizePeer rootSyncPeer 
                    = (RootSynchronizePeer) SynchronizePeerFactory.getPeerForComponent(window.getClass());
            rootSyncPeer.renderRefresh(rc, fullRefreshUpdate, window);
            
            setRootLayoutDirection(rc);
        } else {
            // Remove any updates whose updates are descendants of components which have not been rendered to the
            // client yet due to lazy-loading containers.
            for (int i = 0; i < componentUpdates.length; ++i) {
                if (!isRendered(ci, componentUpdates[i].getParent())) {
                    componentUpdates[i] = null;
                }
            }
            
            // Set of Components whose HTML was entirely re-rendered, negating the need
            // for updates of their children to be processed.
            Set fullyReplacedHierarchies = new HashSet();
    
            for (int i = 0; i < componentUpdates.length; ++i) {
                if (componentUpdates[i] == null) {
                    // Update removed, do nothing.
                    continue;
                }
                
                // Dispose of removed children.
                Component[] removedChildren = componentUpdates[i].getRemovedChildren();
                disposeComponents(rc, componentUpdates[i], removedChildren);

                // Dispose of removed descendants.
                Component[] removedDescendants = componentUpdates[i].getRemovedDescendants();
                disposeComponents(rc, componentUpdates[i], removedDescendants);
    
                // Perform update.
                Component parentComponent = componentUpdates[i].getParent();
                if (!isAncestor(fullyReplacedHierarchies, parentComponent)) {
                    // Only perform update if ancestor of updated component is NOT contained in
                    // the set of components whose descendants were fully replaced.
                    ComponentSynchronizePeer syncPeer = SynchronizePeerFactory.getPeerForComponent(parentComponent.getClass());
                    String targetId;
                    if (parentComponent.getParent() == null) {
                        targetId = null;
                    } else {
                        ComponentSynchronizePeer parentSyncPeer 
                                = SynchronizePeerFactory.getPeerForComponent(parentComponent.getParent().getClass());
                        targetId = parentSyncPeer.getContainerId(parentComponent);
                    }
                    boolean fullReplacement = syncPeer.renderUpdate(rc, componentUpdates[i], targetId);
                    if (fullReplacement) {
                        // Invoke renderDispose() on hierarchy of components destroyed by
                        // the complete replacement.
                        disposeReplacedDescendants(rc, componentUpdates[i], parentComponent);
                        fullyReplacedHierarchies.add(parentComponent);
                    }
                }
            }
        }
    }
    
    /**
     * @see org.karora.cooee.webrender.service.SynchronizeService#renderInit(org.karora.cooee.webrender.Connection,
     *      org.w3c.dom.Document)
     */
    protected ServerMessage renderInit(Connection conn, Document clientMessageDocument) {
        ServerMessage serverMessage = new ServerMessage();
        RenderContext rc = new RenderContextImpl(conn, serverMessage);
        ContainerInstance containerInstance = rc.getContainerInstance();
        try {
            serverMessage.addLibrary(WEB_CONTAINER_SERVICE.getId());
            
            processClientMessage(conn, clientMessageDocument);

            if (!containerInstance.isInitialized()) {
                containerInstance.init(conn);
            }
            
            ApplicationInstance applicationInstance = rc.getContainerInstance().getApplicationInstance();
            ApplicationInstance.setActive(applicationInstance);

            Window window = applicationInstance.getDefaultWindow();
            
            ServerComponentUpdate componentUpdate = new ServerComponentUpdate(window);
            ComponentSynchronizePeer syncPeer = SynchronizePeerFactory.getPeerForComponent(window.getClass());
            ((WindowPeer) syncPeer).renderRefresh(rc, componentUpdate, window);
            
            //TODO. clean-up how these operations are invoked on init/update.
            setAsynchronousMonitorInterval(rc);
            setFocus(rc, true);
            setModalContextRootId(rc);
            setRootLayoutDirection(rc);

            processQueuedCommands(rc);
            
            applicationInstance.getUpdateManager().purge();
            
            return serverMessage;
        } finally {
            ApplicationInstance.setActive(null);
        }
    }
    
    /**
     * @see org.karora.cooee.webrender.service.SynchronizeService#renderUpdate(org.karora.cooee.webrender.Connection,
     *      org.w3c.dom.Document)
     */
    protected ServerMessage renderUpdate(Connection conn, Document clientMessageDocument) {
    	
    	/* if we are debugging messages, output the client message */
    	if (DEBUG_PRINT_MESSAGES_TO_CONSOLE) {
	    	try {
				XMLSerializer xs = new XMLSerializer();
				StringWriter sw = new StringWriter();
				xs.setOutputCharStream(sw);
				xs.serialize(clientMessageDocument);
				//System.out.print("Client Sync Message:");
				classLog.debug(sw.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	clientMessageDocument.toString();
    	
        ServerMessage serverMessage = new ServerMessage();
        RenderContext rc = new RenderContextImpl(conn, serverMessage);
        
        ContainerInstance ci = rc.getContainerInstance();
        ApplicationInstance applicationInstance = ci.getApplicationInstance();
        
        try {
            if (!validateTransactionId(ci, clientMessageDocument)) {
                processInvalidTransaction(rc);
                return serverMessage;
            }
            
            // Mark instance as active.
            ApplicationInstance.setActive(applicationInstance);
            
            UpdateManager updateManager = applicationInstance.getUpdateManager();
            
            processClientFocusedComponent(rc, clientMessageDocument);
            
            // Process updates from client.
            processClientMessage(conn, clientMessageDocument);
            
            updateManager.processClientUpdates();
            
            // Process updates from server.
            processServerUpdates(rc);
            
            setAsynchronousMonitorInterval(rc);
            setFocus(rc, false);
            setModalContextRootId(rc);
            
            processQueuedCommands(rc);

            updateManager.purge();
            
        	/* if we are debugging messages, output the server message */
        	if (DEBUG_PRINT_MESSAGES_TO_CONSOLE) {
    	    	try {
    				XMLSerializer xs = new XMLSerializer();
    				StringWriter sw = new StringWriter();
    				xs.setOutputCharStream(sw);
    				xs.serialize(serverMessage.getDocument());
    				classLog.debug(sw.toString());
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
        	}
        	
            return serverMessage;
        } finally {
            // Mark instance as inactive.
            ApplicationInstance.setActive(null);
        }
    }

    /**
     * Sets the interval between asynchronous monitor requests.
     * 
     * @param rc the relevant <code>RenderContext</code>.
     */
    private void setAsynchronousMonitorInterval(RenderContext rc) {
        boolean hasTaskQueues = rc.getContainerInstance().getApplicationInstance().hasTaskQueues();
        if (hasTaskQueues) {
            int interval = rc.getContainerInstance().getCallbackInterval();
            rc.getServerMessage().setAsynchronousMonitorInterval(interval);
        } else {
            rc.getServerMessage().setAsynchronousMonitorInterval(-1);
        }
    }
    
    /**
     * Update the <code>ServerMessage</code> to set the focused component if
     * required.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param initial a flag indicating whether the initial synchronization is
     *        being performed, i.e., whether this method is being invoked from
     *        <code>renderInit()</code>
     */
    private void setFocus(RenderContext rc, boolean initial) {
        ApplicationInstance applicationInstance = rc.getContainerInstance().getApplicationInstance();
        Component focusedComponent = null;
        if (initial) {
            focusedComponent = applicationInstance.getFocusedComponent();
        } else {
            ServerUpdateManager serverUpdateManager = applicationInstance.getUpdateManager().getServerUpdateManager();
            PropertyUpdate focusUpdate = 
                    serverUpdateManager.getApplicationPropertyUpdate(ApplicationInstance.FOCUSED_COMPONENT_CHANGED_PROPERTY);
            if (focusUpdate != null) {
                focusedComponent = (Component) focusUpdate.getNewValue();
            }
        }

        if (focusedComponent != null) {
            ComponentSynchronizePeer componentSyncPeer 
                    = SynchronizePeerFactory.getPeerForComponent(focusedComponent.getClass());
            if (componentSyncPeer instanceof FocusSupport) {
                ((FocusSupport) componentSyncPeer).renderSetFocus(rc, focusedComponent);
            }
        }
    }
    
    /**
     * Update the <code>ServerMessage</code> to describe the current root
     * element of the modal context.
     * 
     * @param rc the relevant <code>RenderContext</code>
     */
    private void setModalContextRootId(RenderContext rc) {
        ApplicationInstance applicationInstance = rc.getContainerInstance().getApplicationInstance();
        Component modalContextRoot = applicationInstance.getModalContextRoot();
        if (modalContextRoot == null) {
            rc.getServerMessage().setModalContextRootId(null);
        } else {
            rc.getServerMessage().setModalContextRootId(ContainerInstance.getElementId(modalContextRoot));
        }
    }
    
    /**
     * Update the <code>ServerMessage</code> to describe the current root
     * layout direction
     * 
     * @param rc the relevant <code>RenderContext</code>
     */
    private void setRootLayoutDirection(RenderContext rc) {
        ApplicationInstance applicationInstance = rc.getContainerInstance().getApplicationInstance();
        rc.getServerMessage().setRootLayoutDirection(applicationInstance.getLayoutDirection().isLeftToRight()
                ? ServerMessage.LEFT_TO_RIGHT : ServerMessage.RIGHT_TO_LEFT);
    }
    
    /**
     * Determines if transaction id retrieved from client matches current transaction id.
     * 
     * @param containerInstance the relevant <code>ContainerInstance</code>
     * @param clientMessageDocument the incoming client message
     * @return true if the transaction id is valid
     */
    private boolean validateTransactionId(ContainerInstance containerInstance, Document clientMessageDocument) {
        try {
            long clientTransactionId = Long.parseLong(clientMessageDocument.getDocumentElement().getAttribute("trans-id"));
            return containerInstance.getCurrentTransactionId() == clientTransactionId;
        } catch (NumberFormatException ex) {
            // Client has not provided a transaction id at all, return true.
            // This should not occur.
            return true;
        }
    }
    
}
