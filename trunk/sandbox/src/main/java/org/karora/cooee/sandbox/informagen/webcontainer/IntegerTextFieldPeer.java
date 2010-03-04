package org.karora.cooee.sandbox.informagen.webcontainer;


// This peer's component
import org.karora.cooee.app.Component;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.sandbox.informagen.app.IntegerTextField;
import org.karora.cooee.webcontainer.DomUpdateSupport;
import org.karora.cooee.webcontainer.RenderContext;
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.ServiceRegistry;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.service.JavaScriptService;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * Synchronization peer for the 
 * <code>org.karora.cooee.sandbox.informagen.app.IntegerTextField</code> component
 */


public class IntegerTextFieldPeer extends ActiveTextFieldPeer
                                  implements DomUpdateSupport {

    /**
     * Service to provide supporting JavaScript library; icons are loaded
     *  and registered in the superclass "ActiveTextFieldPeer"
     */

    static final Service INTEGER_TEXTFIELD_SERVICE = JavaScriptService.forResource(
                    "sandbox.informagen.IntegerTextField",
                    "/org/karora/cooee/sandbox/informagen/webcontainer/resource/js/IntegerTextField.js");


    static {
		ServiceRegistry registery = WebRenderServlet.getServiceRegistry();
		registery.add(INTEGER_TEXTFIELD_SERVICE);
    }
 

    ///////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Default constructor.
     */

    public IntegerTextFieldPeer() {
        super("sandbox_informagen_IntegerTextField.MessageProcessor");
    }


    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderDispose(org.karora.cooee.webcontainer.RenderContext,
     *      org.karora.cooee.app.update.ServerComponentUpdate,
     *      org.karora.cooee.app.Component)
     */
    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {

        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(INTEGER_TEXTFIELD_SERVICE.getId());

        super.renderDispose(rc, update, component);
    }


    
    /**
     * @see org.karora.cooee.webcontainer.DomUpdateSupport#renderHtml(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, org.w3c.dom.Node, org.karora.cooee.app.Component)
     */

    public void renderHtml(RenderContext rc, ServerComponentUpdate addUpdate, Node parentNode, Component component) {
    
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(INTEGER_TEXTFIELD_SERVICE.getId());

        IntegerTextField integerTextField = (IntegerTextField)component;
        
        super.renderHtml(rc, addUpdate, parentNode, integerTextField);


        // Now build a generic 'init' message directive, then add this subclasses's
        //   specific attributes
        
        Element itemElement = renderInitDirective(rc, integerTextField);

        itemElement.setAttribute("minimum-value", Integer.toString(integerTextField.getMinValue()));
        itemElement.setAttribute("maximum-value", Integer.toString(integerTextField.getMaxValue()));

    }


}
