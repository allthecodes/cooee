
package org.karora.cooee.sandbox.informagen.partialupdate;


// Cooee Framework
import org.karora.cooee.app.text.TextComponent;
import org.karora.cooee.app.update.ServerComponentUpdate;


// Cooee WebContainer Sub-Framework
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.PartialUpdateParticipant;
import org.karora.cooee.webcontainer.RenderContext;

// Cooee WebRenderer Sub-Framework
import org.karora.cooee.webrender.ServerMessage;

// W3C DOM
import org.w3c.dom.Element;

 
/**
 * A <code>PartialUpdateParticipant</code> to update the text any
 * subclass of 'TextComponent'
 */

public class TextUpdate implements PartialUpdateParticipant {

    private String messageProcessor;


    /**
     * Will update the only text of a TextComponent with creating a new cleint-side
     *  component.  This is know as a partial update.
     * 
     * @param messageProcessor the name of the client-side JavaScript message processor
     */


    public TextUpdate(String messageProcessor) {
        this.messageProcessor = messageProcessor;
    }


    /**
     * @see org.karora.cooee.webcontainer.PartialUpdateParticipant#canRenderProperty(org.karora.cooee.webcontainer.RenderContext, 
     *      org.karora.cooee.app.update.ServerComponentUpdate)
     */
    public boolean canRenderProperty(RenderContext rc, ServerComponentUpdate update) {
        return true;
    }

    /**
     * @see org.karora.cooee.webcontainer.PartialUpdateParticipant#renderProperty(
     *      org.karora.cooee.webcontainer.RenderContext, org.karora.cooee.app.update.ServerComponentUpdate)
     */

    public void renderProperty(RenderContext rc, ServerComponentUpdate update) {
    
        TextComponent textComponent = (TextComponent) update.getParent();
        
        String elementId = ContainerInstance.getElementId(textComponent);
        ServerMessage serverMessage = rc.getServerMessage();
        
        Element itemizedUpdateElement = serverMessage.getItemizedDirective(
                ServerMessage.GROUP_ID_POSTUPDATE,
                messageProcessor, 
                "set-text", 
                new String[0], 
                new String[0]);
                
        Element itemElement = serverMessage.getDocument().createElement("item");
        itemElement.setAttribute("eid", elementId);
        itemElement.setAttribute("text", textComponent.getText());
        
        itemizedUpdateElement.appendChild(itemElement);
        
    }
}

