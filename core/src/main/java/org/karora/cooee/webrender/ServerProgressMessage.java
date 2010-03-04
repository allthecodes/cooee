package org.karora.cooee.webrender;

import java.io.IOException;

import org.karora.cooee.webcontainer.ContainerContext;
import org.w3c.dom.Element;

public abstract class ServerProgressMessage {

	ContainerContext containerContext;
	
	
	/**
	 * Framework method, ensures container context is available prior
	 * to calling getMessage().  Framework will NEVER directly call
	 * getMessage.  
	 * 
	 * Designed to prevent any issues with passing in the container
	 * context on object construction.
	 * 
	 * @param containerContext
	 * @return
	 */
	/* package */ 
	final Element internalGetMessage(ContainerContext containerContext)
	{
		this.containerContext = containerContext;
		return getMessage();
	}
	
    public abstract Element getMessage();
	
    public final ContainerContext getContainerContext()
    {
    	return containerContext;
    }
    
	protected final String getImageServiceURI(ImageService imageService)
	{
		if (containerContext != null)
		{
			return containerContext.getServiceUri(imageService);
		}
		return "";
	}
	
	protected final String getServiceURI(Service service)
	{
		if (containerContext != null)
		{
			return containerContext.getServiceUri(service);
		}
		return "";
	}
	
	protected final String getImageServiceURI(String resourceName)
	{

		ImageService imageService = ImageServiceFactory.findImageServiceInCache(resourceName);
		
		if (imageService != null)
			return getImageServiceURI(imageService);
		
		return "";
		
	}
	
	
}
