package org.karora.cooee.app.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.karora.cooee.app.ApplicationInstance;
import org.karora.cooee.webcontainer.ContainerContext;
import org.karora.cooee.webrender.ClientProperties;

/**
*  A class for creating a resource bundle instance and returning string values
*
*/
public class Messages
{
	// Map of bundles for each required locale
	private static Map<Locale, ResourceBundle> bundles = new HashMap<Locale, ResourceBundle>();

	private Messages() {} // Stop instantiation
	
	/**
	* Returns a value for the given key
	*/
	public final static String getString(String key)
	{
		// We have to determine locale for each request
		Locale locale = getClientLocale();
		ResourceBundle bundle = bundles.get(locale);
		
		// No previously created bundle for the required locale, so create one and cache
		if(bundle == null)
		{
			bundle = ResourceBundle.getBundle("org/karora/cooee/Messages", locale);
			bundles.put(locale, bundle);
		}
		
		// Return the bundle property or the key if no property was found
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}
	
	// Return the first found client locale if there is an ApplicationInstance, 
	// otherwise just return the default one as a fallback
	private static Locale getClientLocale()
	{
		ApplicationInstance app = ApplicationInstance.getActive();
		Locale locale = Locale.getDefault();
		if(app != null) {
			ContainerContext context = (ContainerContext) app.getContextProperty(ContainerContext.CONTEXT_PROPERTY_NAME);
			ClientProperties clientProperties = context.getClientProperties();
			Locale locales[] = (Locale[]) clientProperties.get(ClientProperties.LOCALES);
			if(locales.length != 0)
				locale = locales[0];
		}

		return locale;
	}
}