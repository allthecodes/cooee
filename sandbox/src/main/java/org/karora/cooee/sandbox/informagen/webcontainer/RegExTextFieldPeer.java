package org.karora.cooee.sandbox.informagen.webcontainer;


import org.karora.cooee.app.Component;
import org.karora.cooee.app.update.ServerComponentUpdate;
import org.karora.cooee.sandbox.informagen.app.RegExTextField;
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


public class RegExTextFieldPeer extends ActiveTextFieldPeer
                                implements DomUpdateSupport {

    /**
     * Service to provide supporting JavaScript library.
     */

    static final Service REGEX_TEXTFIELD_SERVICE = JavaScriptService.forResource(
            "sandbox.informagen.RegExTextField",
            "/org/karora/cooee/sandbox/informagen/webcontainer/resource/js/RegExTextField.js");


    static {
		ServiceRegistry registery = WebRenderServlet.getServiceRegistry();
        registery.add(REGEX_TEXTFIELD_SERVICE);
    }
 

    /**
     * Default constructor.
     */

    public RegExTextFieldPeer() {
        super("sandbox_informagen_RegExTextField.MessageProcessor");
    }

    /**
     * @see org.karora.cooee.webcontainer.ComponentSynchronizePeer#renderDispose(org.karora.cooee.webcontainer.RenderContext,
     *      org.karora.cooee.app.update.ServerComponentUpdate,
     *      org.karora.cooee.app.Component)
     */

    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {

        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(REGEX_TEXTFIELD_SERVICE.getId());

        super.renderDispose(rc, update, component);
    }


//         if (regexTextField.getRegExFilter() != null && !regexTextField.getRegExFilter().equals("")) 
//             itemElement.setAttribute("regexp-filter", regexTextField.getRegExFilter());
//         
//         if (regexTextField.getRegEx() != null && !regexTextField.getRegEx().equals("")) 
//             itemElement.setAttribute("regexp", regexTextField.getRegEx());




    
    /**
     * @see org.karora.cooee.webcontainer.DomUpdateSupport#renderHtml(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate, org.w3c.dom.Node, org.karora.cooee.app.Component)
     */
    public void renderHtml(RenderContext rc, ServerComponentUpdate addUpdate, Node parentNode, Component component) {
    
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(REGEX_TEXTFIELD_SERVICE.getId());
        
        RegExTextField regexTextField = (RegExTextField)component;
        
        super.renderHtml(rc, addUpdate, parentNode, regexTextField);


        Element itemElement = renderInitDirective(rc, regexTextField);
        
        if (regexTextField.getRegExFilter() != null && !regexTextField.getRegExFilter().equals("")) 
            itemElement.setAttribute("regexp-filter", regexTextField.getRegExFilter());
        
        if (regexTextField.getRegEx() != null && !regexTextField.getRegEx().equals("")) 
            itemElement.setAttribute("regexp", regexTextField.getRegEx());

    }



}
