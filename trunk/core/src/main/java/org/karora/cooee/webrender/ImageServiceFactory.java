package org.karora.cooee.webrender;

import java.util.WeakHashMap;


public class ImageServiceFactory {
	
	private static int BUFFER_SIZE = 4096;
	
	private static WeakHashMap cachedImageServices = new WeakHashMap();
	
	
	/*package */ static ImageService findImageServiceInCache(String resource)
	{
		if (cachedImageServices.containsKey(resource))
		{
			return (ImageService) ImageServiceFactory.cachedImageServices.get(resource);
		}
		else
		{
			return null;
		}
	}
	
	
	public static final ImageService createImageService(String imageResourceName, ContentType contentType)
	{
		ImageService imageService;
		
		if (cachedImageServices.containsKey(imageResourceName))
		{
			imageService = (ImageService) ImageServiceFactory.cachedImageServices.get(imageResourceName);
		}
		else
		{
			imageService = new ImageService(imageResourceName, contentType);
			cachedImageServices.put(imageResourceName, imageService);
		}
		
		return imageService;
	}
	
	private ImageServiceFactory()
	{
	}
	
		
	
}
