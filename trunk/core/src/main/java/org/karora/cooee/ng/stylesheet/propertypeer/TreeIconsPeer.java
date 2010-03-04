package org.karora.cooee.ng.stylesheet.propertypeer;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.Style;
import org.karora.cooee.app.componentxml.ComponentIntrospector;
import org.karora.cooee.app.componentxml.ComponentXmlException;
import org.karora.cooee.app.componentxml.InvalidPropertyException;
import org.karora.cooee.app.componentxml.PropertyLoader;
import org.karora.cooee.app.componentxml.PropertyXmlPeer;
import org.karora.cooee.app.util.DomUtil;
import org.karora.cooee.ng.tree.DefaultTreeIcons;
import org.karora.cooee.ng.tree.TreeIcons;
import org.w3c.dom.Element;

public class TreeIconsPeer implements PropertyXmlPeer {

    /**
     * @see org.karora.cooee.app.componentxml.PropertyXmlPeer#getValue(java.lang.ClassLoader, java.lang.Class, org.w3c.dom.Element)
     */
    public Object getValue(ClassLoader classLoader, Class objectClass, Element propertyElement) 
    throws InvalidPropertyException {
        try {
	        // Load properties from XML into Style.
	        PropertyLoader propertyLoader = PropertyLoader.forClassLoader(classLoader);
	        Element propertiesElement = DomUtil.getChildElementByTagName(propertyElement, "properties");
	        String type = TreeIcons.class.getName();
	        Style propertyStyle = propertyLoader.createStyle(propertiesElement, type);
	        
	        // Instantiate DefaultTreeIcons instance.
	    	DefaultTreeIcons treeIcons = new DefaultTreeIcons();
	        
	        // Set property values of LayoutData instance.
	        Iterator it = propertyStyle.getPropertyNames();
	        while (it.hasNext()) {
	            String propertyName = (String) it.next();
	            treeIcons.setIcon(propertyName, (ImageReference)propertyStyle.getProperty(propertyName));
	        }
	    	return treeIcons;
        } catch (ComponentXmlException ex) {
            throw new InvalidPropertyException("Unable to process properties.", ex);
        } catch (IllegalArgumentException ex) {
            throw new InvalidPropertyException("Unable to process properties.", ex);
        }
    }
}
