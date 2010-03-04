package org.karora.cooee.webrender;

import java.util.Properties;

import javax.xml.transform.OutputKeys;

import org.karora.cooee.webcontainer.ContainerContext;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webrender.output.HtmlDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Abstract class representing the base client document.
 * 
 * The base client document is the very first piece of HTML which is
 * returned to the client side.  All DOM updates are then made to this document.
 * 
 * This class replaces the <code>BaseHtmlDocument</code> from the Echo framework.
 * 
 * @author Daniel Murley
 * @since Cooee 1.1
 * 
 */
public abstract class BaseClientDocument extends HtmlDocument 
{

	 private static final String BASE_FORM_CSS_STYLING = "padding:0px;margin:0px;";
	 
	 
	 /**
	  * Default text set for the loader.  Usually will be replaced immediately by
	  * the client.
	  * 
	  * Be aware, this MUST be set to something.  A blank string will not be valid
	  * and will cause problems on the client side.
	  * 
	  */
	 private static final String DEFAULT_INITIAL_LOAD_TEXT = "//";
	 
	 private static final Properties OUTPUT_PROPERTIES = new Properties();
	 
	 static 
     {
        // The XML declaration is omitted as Internet Explorer 6 will operate in quirks mode if it is present.
        OUTPUT_PROPERTIES.setProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        
        OUTPUT_PROPERTIES.setProperty(OutputKeys.DOCTYPE_PUBLIC, XHTML_1_0_TRANSITIONAL_PUBLIC_ID);
        OUTPUT_PROPERTIES.setProperty(OutputKeys.DOCTYPE_SYSTEM, XHTML_1_0_TRANSITIONAL_SYSTSEM_ID);
     }
     
     private String contentId;
     
     protected ContainerInstance containerInstance;
     
     /**
      * Creates a new <code>BaseClientDocument</code>.
      * 
      * This constructor will do the following things when called:
      * <ul>
      *   <li>Create a style element</li>
      *   <li>Append the style element to the head node</li>
      *   <li>Create a form element</li>
      *   <li>Create a div element for the loading status</li>
      *   <li>Append form element to the body element</li>
      * </ul>
      * 
      * This class gives you three points at which you should override if you
      * wish to change the behaviour of the initial rendering of a Cooee application.
      * 
      * These are:
      *    <ul>
      *       <li>populateFormElementAttributes</li>
      *       <li>populateStyleElement</li>
      *       <li>createLoadingStatusElement</li>
      *    </ul>
      *    
      * See the Javadocs for the above methods before attempting to override them.
      * 
      * @param contentId The desired id which will be used for the element to 
      *        which content should be added, i.e., the FORM element.
      */
     public BaseClientDocument(ContainerInstance ci, String contentId) {
    	 super(XHTML_1_0_TRANSITIONAL_PUBLIC_ID, XHTML_1_0_TRANSITIONAL_SYSTSEM_ID, XHTML_1_0_NAMESPACE_URI);
         
    	 setOutputProperties(OUTPUT_PROPERTIES);
         
         this.contentId = contentId;
         this.containerInstance = ci;
         
         Document document = getDocument();
         
         Element styleElement = document.createElement("style");
         populateStyleElement(styleElement);
         
         getHeadElement().appendChild(styleElement);
         
         Element formElement = document.createElement("form");
         populateFormElementAttributes(formElement);

         Element loadingDiv = document.createElement("div");
         loadingDiv.setAttribute("id", "loadstatus");
      
         Element loadingStatusElement = createStartupLoadingStatusElement();
         if (loadingStatusElement != null)
         {
        	 try
        	 {   
        		 // Firewalled in the event of rubbish from the client
        		 loadingDiv.setAttribute("customLoadStatus", "true");
        		 Element extLoadingDiv = (Element) document.importNode(loadingStatusElement, true);
        		 loadingDiv.appendChild(extLoadingDiv);
        	 }
        	 catch (Throwable t)
        	 {
        		 // Non critical error -- Can be safely ignored
        		 t.printStackTrace();
        	 }
         }
         else
         {
        	 loadingDiv.appendChild(document.createTextNode(DEFAULT_INITIAL_LOAD_TEXT));
         }
         
         formElement.appendChild(loadingDiv);
         
         getBodyElement().appendChild(formElement);
     }
     
     /**
      * Retrieves the element of the document to which content should be added,
      * i.e., the FORM element.
      * 
      * @return the element to which content should be added.
      */
     public Element getContentElement() {
         return getDocument().getElementById(contentId);
     }
	
     /**
      * 
      * @param formElement
      */
     protected void populateFormElementAttributes(Element formElement)
     {
         formElement.setAttribute("style", BASE_FORM_CSS_STYLING);
         formElement.setAttribute("action", "#");
         formElement.setAttribute("id", contentId);
         formElement.setAttribute("onsubmit", "return false;");
     }
	
     /**
      * Provides a mechanism for customising the initial style tag used as part
      * of the base html document.
      * 
      * Be aware, when overridding this method, if you do not call super() you must
      * provide the style element with a type attribute of "text/css" otherwise it is
      * unlikely the application will start under IE. 
      * 
      * @param styleElement Parent style element ie: <style />
      */
     protected void populateStyleElement(Element styleElement)
     {
    	 // Application will not start under IE if type/css isn't added to style element
    	 styleElement.setAttribute("type", ContentType.TEXT_CSS.toString());
         styleElement.appendChild(getDocument().createTextNode(" "));
     }
     
     /**
      * Method for providing custom implementations of the (startup) loading status
      * element.  Returning null from this method will cause the framework to
      * use a default loader.
      * 
      * @return HTML Element to be used as the loader, otherwise null
      */
     protected Element createStartupLoadingStatusElement()
     {
    	 /*ServerProgressMessage startupMessage = containerInstance.getServerStartupMessage(); 
    	 if (startupMessage != null)
    	 {
    		 return startupMessage.internalGetMessage(containerInstance.getContainerContext());
    	 }
    	 else
    	 {
    		 return null;
    	 }*/

    	 
    	 //XXX: Still not 100% on the api here, as such experimental.
    	 return null;
    	 
     }
     
}
