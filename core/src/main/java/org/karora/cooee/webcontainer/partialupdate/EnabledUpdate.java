
package org.karora.cooee.webcontainer.partialupdate;


// Cooee Framework
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.update.ServerComponentUpdate;


// Cooee WebContainer Sub-Framework
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webcontainer.PartialUpdateParticipant;
import org.karora.cooee.webcontainer.RenderContext;

// Cooee WebRenderer Sub-Framework
import org.karora.cooee.webrender.ServerMessage;
import org.karora.cooee.webcontainer.propertyrender.ColorRender;

// W3C DOM
import org.w3c.dom.Element;

 
/**
 * A <code>PartialUpdateParticipant</code> to update the text any
 * subclass of 'TextComponent'
 */

public class EnabledUpdate implements PartialUpdateParticipant {

    private String messageProcessor;

    public EnabledUpdate(String messageProcessor) {
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
    
        Component component = (Component) update.getParent();
        
        String elementId = ContainerInstance.getElementId(component);
        ServerMessage serverMessage = rc.getServerMessage();
        
        Element itemizedUpdateElement = serverMessage.getItemizedDirective(
                ServerMessage.GROUP_ID_POSTUPDATE,
                messageProcessor, 
                "set-enabled", 
                new String[0], 
                new String[0]);
                
        Element itemElement = serverMessage.getDocument().createElement("item");
        itemElement.setAttribute("eid", elementId);
        
        String enabled = (String)component.getRenderProperty(Component.ENABLED_CHANGED_PROPERTY);
        
        itemElement.setAttribute("enabled", Boolean.toString(component.isRenderEnabled()));
        
        itemizedUpdateElement.appendChild(itemElement);
        
    }
}

