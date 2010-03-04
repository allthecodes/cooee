package org.karora.cooee.sandbox.informagen.webcontainer;


// This peer's Cooee component
import org.karora.cooee.app.Component;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.sandbox.informagen.app.NumericTextField;
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


public class NumericTextFieldPeer extends ActiveTextFieldPeer
                                  implements DomUpdateSupport {

    /**
     * Service to provide supporting JavaScript library; icons are loaded
     *  and registered in the superclass "ActiveTextFieldPeer"
     */

    static final Service NUMERIC_TEXTFIELD_SERVICE = JavaScriptService.forResource(
                    "sandbox.informagen.NumericTextField",
                    "/org/karora/cooee/sandbox/informagen/webcontainer/resource/js/NumericTextField.js");


    static {
		ServiceRegistry registery = WebRenderServlet.getServiceRegistry();
		registery.add(NUMERIC_TEXTFIELD_SERVICE);
    }
 

    ///////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Default constructor.
     */

    public NumericTextFieldPeer() {
        super("sandbox_informagen_NumericTextField.MessageProcessor");
    }


    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderDispose(org.karora.cooee.webcontainer.RenderContext,
     *      org.karora.cooee.app.update.ServerComponentUpdate,
     *      org.karora.cooee.app.Component)
     */
    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {

        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(NUMERIC_TEXTFIELD_SERVICE.getId());

        super.renderDispose(rc, update, component);
    }


    
    /**
     * @see org.karora.cooee.webcontainer.DomUpdateSupport#renderHtml(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, org.w3c.dom.Node, org.karora.cooee.app.Component)
     */

    public void renderHtml(RenderContext rc, ServerComponentUpdate addUpdate, Node parentNode, Component component) {
    
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(NUMERIC_TEXTFIELD_SERVICE.getId());

        NumericTextField numericTextField = (NumericTextField)component;
        
        super.renderHtml(rc, addUpdate, parentNode, numericTextField);


        // Now build a generic 'init' message directive, then add this subclasses's
        //   specific attributes
        
        Element itemElement = renderInitDirective(rc, numericTextField);

        itemElement.setAttribute("minimum-value", Double.toString(numericTextField.getMinValue()));
        itemElement.setAttribute("maximum-value", Double.toString(numericTextField.getMaxValue()));

    }


}
