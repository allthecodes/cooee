package org.karora.cooee.testapp.testscreen;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.karora.cooee.app.Button;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.layout.SplitPaneLayoutData;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.app.util.DomUtil;
import org.karora.cooee.webcontainer.ComponentSynchronizePeer;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.DomUpdateSupport;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.servermessage.DomUpdate;
import org.karora.cooee.webrender.service.JavaScriptService;

/**
 * A test to examine failure behavior with client script exceptions.
 */
public class ClientExceptionTest extends Column {

    /**
     * Component that can be configured to throw JavaScript
     * exception on client/server sync.
     */
    public static class ExComponent extends Component {

        public static final int MODE_WORKING = 0;
        public static final int MODE_FAIL_ON_RENDER_ONCE = 1;
        public static final int MODE_FAIL_ON_RENDER_EVERY_TIME = 2;
        public static final int MODE_LOAD_BROKEN_JS_ONCE = 3;
        public static final int MODE_LOAD_BROKEN_JS_EVERY_TIME = 4;
        
        private int mode;
        
        public ExComponent(int mode) {
            super();
            this.mode = mode;
        }
         
        public int getMode() {
            return mode;
        }
        
        public void setMode(int mode) {
            this.mode = mode;
        }
    }

    /**
     * Peer class for <code>ExComponent</code> that will throw a JavaScript 
     * error depending on component state.
     */
    public static class ExPeer implements ComponentSynchronizePeer, DomUpdateSupport {

        /**
         * Service to provide supporting JavaScript library.
         */
        private static final Service EXCOMPONENT_SERVICE = JavaScriptService.forResource("EchoTestApp.ExComponent",
                "/org/karora/cooee/testapp/resource/js/ExComponent.js");
        
        /**
         * Service to provide supporting JavaScript library.
         */
        private static final Service EXCOMPONENT_BROKEN_SERVICE = JavaScriptService.forResource("EchoTestApp.ExComponentBroken",
                "/org/karora/cooee/testapp/resource/js/ExComponentBroken.js");

        static {
            WebRenderServlet.getServiceRegistry().add(EXCOMPONENT_SERVICE);
            WebRenderServlet.getServiceRegistry().add(EXCOMPONENT_BROKEN_SERVICE);
        }
        
        /**
         * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#getContainerId(org.karora.cooee.app.Component)
         */
        public String getContainerId(Component child) {
            throw new UnsupportedOperationException("Component does not support children.");
        }

        /**
         * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderAdd(org.karora.cooee.webcontainer.RenderContext,
         *      org.karora.cooee.app.update.ServerComponentUpdate, java.lang.String, org.karora.cooee.app.Component)
         */
        public void renderAdd(RenderContext rc, ServerComponentUpdate update, String targetId, Component component) {
            Element domAddElement = DomUpdate.renderElementAdd(rc.getServerMessage());
            DocumentFragment htmlFragment = rc.getServerMessage().getDocument().createDocumentFragment();
            renderHtml(rc, update, htmlFragment, component);
            DomUpdate.renderElementAddContent(rc.getServerMessage(), domAddElement, targetId, htmlFragment);
        }
        
        /**
         * Renders a directive to dynamically load a broken script module.
         * 
         * @param rc the relevant <code>RenderContext</code>
         */
        private void renderBrokenScriptModule(RenderContext rc) {
            rc.getServerMessage().addLibrary(EXCOMPONENT_BROKEN_SERVICE.getId());
        }

        /**
         * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderDispose(
         *      org.karora.cooee.webcontainer.RenderContext, org.karora.cooee.app.update.ServerComponentUpdate,
         *      org.karora.cooee.app.Component)
         */
        public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) { }
        
        /**
         * Renders a directive that will fail during processing.
         * 
         * @param rc the relevant <code>RenderContext</code>
         */
        private void renderFailDirective(RenderContext rc) {
            rc.getServerMessage().addLibrary(EXCOMPONENT_SERVICE.getId());
            rc.getServerMessage().appendPartDirective(ServerMessage.GROUP_ID_POSTUPDATE, "ExComponent.MessageProcessor", "fail");
        }

        /**
         * @see org.karora.cooee.webcontainer.DomUpdateSupport#renderHtml(org.karora.cooee.webcontainer.RenderContext,
         *      org.karora.cooee.app.update.ServerComponentUpdate,
         *      org.w3c.dom.Node, org.karora.cooee.app.Component)
         */
        public void renderHtml(RenderContext rc, ServerComponentUpdate update, Node parentNode, Component component) {
            ExComponent exComponent = (ExComponent) component;
            Document document = rc.getServerMessage().getDocument();
            Element spanElement = document.createElement("span");
            spanElement.setAttribute("id", ContainerInstance.getElementId(component));
            parentNode.appendChild(spanElement);
            
            switch (exComponent.getMode()) {
            case ExComponent.MODE_FAIL_ON_RENDER_ONCE:
                DomUtil.setElementText(spanElement, "[fail on render once]");
                renderFailDirective(rc);
                exComponent.setMode(ExComponent.MODE_WORKING);
                break;
            case ExComponent.MODE_FAIL_ON_RENDER_EVERY_TIME:
                DomUtil.setElementText(spanElement, "[fail on render every time]");
                renderFailDirective(rc);
                break;
            case ExComponent.MODE_LOAD_BROKEN_JS_ONCE:
                DomUtil.setElementText(spanElement, "[load broken script module]");
                renderBrokenScriptModule(rc);
                exComponent.setMode(ExComponent.MODE_WORKING);
                break;
            case ExComponent.MODE_LOAD_BROKEN_JS_EVERY_TIME:
                DomUtil.setElementText(spanElement, "[load broken script module]");
                renderBrokenScriptModule(rc);
                break;
            default:
                DomUtil.setElementText(spanElement, "[non-broken]");
            }
        }

        /**
         * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderUpdate(
         *      org.karora.cooee.webcontainer.RenderContext, org.karora.cooee.app.update.ServerComponentUpdate,
         *      java.lang.String)
         */
        public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
            String parentId = ContainerInstance.getElementId(update.getParent());
            DomUpdate.renderElementRemove(rc.getServerMessage(), parentId);
            renderAdd(rc, update, targetId, update.getParent());
            return true;
        }
    }

    /**
     * Creates a new <code>ClientExceptionTest</code>.
     */
    public ClientExceptionTest() {
        super();
        
        SplitPaneLayoutData splitPaneLayoutData = new SplitPaneLayoutData();
        splitPaneLayoutData.setInsets(new Insets(10));
        setLayoutData(splitPaneLayoutData);
        
        Button button;
        
        button = new Button("Add working component (Control Case)");
        button.setStyleName("Default");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add(new ExComponent(ExComponent.MODE_WORKING));
            }
        });
        add(button);
        
        button = new Button("Add broken component that fails to render (ONCE).");
        button.setStyleName("Default");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add(new ExComponent(ExComponent.MODE_FAIL_ON_RENDER_ONCE));
            }
        });
        add(button);
        
        button = new Button("Add broken component that will dynamically load broken JavaScript module (ONCE).");
        button.setStyleName("Default");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add(new ExComponent(ExComponent.MODE_LOAD_BROKEN_JS_ONCE));
            }
        });
        add(button);
        
        button = new Button("Add broken component that fails to render (EVERY TIME).");
        button.setStyleName("Default");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add(new ExComponent(ExComponent.MODE_FAIL_ON_RENDER_EVERY_TIME));
            }
        });
        add(button);
        
        button = new Button("Add broken component that will dynamically load broken JavaScript module (EVERY TIME).");
        button.setStyleName("Default");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add(new ExComponent(ExComponent.MODE_LOAD_BROKEN_JS_EVERY_TIME));
            }
        });
        add(button);
    }
}