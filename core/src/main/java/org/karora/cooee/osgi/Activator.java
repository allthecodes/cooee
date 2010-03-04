package org.karora.cooee.osgi;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Activator for Cooee when running in an OSGI environment
 * 
 * This activator simply gets a reference to the bundle context
 * and makes it available via the "BundleServices" class
 * 
 * @author dmurley
 * @since Cooee 1.0
 *
 */
public class Activator implements BundleActivator
{

    public void start(BundleContext context) throws Exception
    {
        BundleServices.setBundleContext(context);
        
    }

    public void stop(BundleContext context) throws Exception
    {
        
    }
    
}
