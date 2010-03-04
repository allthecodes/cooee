package org.karora.cooee.webcontainer.util;

import org.karora.cooee.app.util.Uid;
import org.karora.cooee.webcontainer.ContainerInstance;
import org.karora.cooee.webrender.ServerDelayMessage;
import org.karora.cooee.webrender.Service;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.webrender.output.HtmlDocument;
import org.karora.cooee.webrender.output.XmlDocument;
import org.karora.cooee.webrender.service.StaticBinaryService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * A Class to create a cooee server delay message based on a image resource.
 * 
 <pre>
		ContainerContext cc = (ContainerContext) ApplicationInstance.getActive().getContextProperty(ContainerContext.CONTEXT_PROPERTY_NAME);
		cc.setServerDelayMessage(new ResourceImageServerDelayMessage("loading.gif", "image/gif", 104, 305));
 </pre>
 
 * @author gmccreath
 *
 */
public class ResourceImageServerDelayMessage extends ServerDelayMessage {

	private static final long serialVersionUID = 603449238442585723L;

	/**
	 * Message content.
	 */
	private Element messageElement;

	/**
	 * Creates the server delay message.
	 * 
	 * @param resourceName the resource name to show as the image within the delay message
	 * @param contentType the content type of the given image resource 
	 * @param height the height in pixels of the image
	 * @param width the width in pixels of the image
	 */
	public ResourceImageServerDelayMessage(String resourceName, String contentType, int height, int width) {
		
		Service loadingImageService = StaticBinaryService.forResource(Uid.generateUidString(), contentType, resourceName);
		WebRenderServlet.getServiceRegistry().add(loadingImageService);
		
		XmlDocument xmlDocument = new XmlDocument("div", null, null, HtmlDocument.XHTML_1_0_NAMESPACE_URI);
		
		Document document = xmlDocument.getDocument();
		Element divElement = document.getDocumentElement();
		
		divElement.setAttribute("id", ELEMENT_ID_MESSAGE);
		divElement.setAttribute("style", "position:absolute;top:0px;left:0px;width:100%;height:100%;cursor:wait;"
				+ "margin:0px;padding:0px;visibility:hidden;z-index:10000;");

		Element tableElement = document.createElement("table");
		tableElement.setAttribute("style", "width:100%;height:100%;border:0px;padding:0px;");
		
		divElement.appendChild(tableElement);

		Element tbodyElement = document.createElement("tbody");
		tableElement.appendChild(tbodyElement);

		Element trElement = document.createElement("tr");
		tbodyElement.appendChild(trElement);

		Element tdElement = document.createElement("td");
		trElement.appendChild(tdElement);

		ContainerInstance containerInstance = (ContainerInstance) WebRenderServlet.getActiveConnection().getUserInstance();
		String si = containerInstance.getServiceUri(loadingImageService);

		Element imgElement = document.createElement("img");
		imgElement.setAttribute("src", si);

		Element longDivElement = document.createElement("div");
		longDivElement.setAttribute("id", ELEMENT_ID_LONG_MESSAGE);

		longDivElement.setAttribute("style", "margin-top:40px;margin-left:auto;margin-right:auto;background-color:#FFFFFF;"
				+ "color:#FFFFFF;width:" + width + "px;height:" + height + "px;"
				+ "font-family:verdana,arial,helvetica,sans-serif;font-size:10pt;text-align:center;");

		longDivElement.appendChild(imgElement);
		tdElement.appendChild(longDivElement);

		messageElement = divElement;
	}

	/**
	 * @see org.karora.cooee.webrender.ServerDelayMessage#getMessage()
	 */
	public Element getMessage() {
		return messageElement;
	}
}
