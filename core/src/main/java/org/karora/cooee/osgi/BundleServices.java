package org.karora.cooee.osgi;

import org.osgi.framework.BundleContext;

/**
 * Provides access to the bundle context when running in
 * an OSGI environment.
 * 
 * If the Cooee bundle has not been started, the bundle context
 * will not have been set and will not be available.
 * 
 * @author dmurley
 * @since Cooee 1.0.0
 */
public class BundleServices
{
    private static BundleContext bundleContext;
    
    /**
     * Provides access to the bundle context when in an OSGI
     * environment.  Returns null if no context is available.
     * 
     * @return The BundleContext for the Cooee bundle
     */
    public static BundleContext getBundleContext()
    {
        return bundleContext;
    }
   
    protected static void setBundleContext(BundleContext bundleContext)
    {
        BundleServices.bundleContext = bundleContext;
    }
    
    
}
