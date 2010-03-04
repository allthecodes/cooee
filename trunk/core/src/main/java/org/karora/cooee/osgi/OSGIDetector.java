package org.karora.cooee.osgi;

/**
 * Class providing a mechanism for detecting whether or not
 * Cooee is running within an OSGI environemnt.
 * 
 * @author dmurley
 *
 */
public class OSGIDetector
{
    /**
     * This method determines if Cooee has been activated
     * within an OSGI environment.  It is safe to call from any
     * class in Cooee without worrying about a class not found
     * exception
     * 
     * @return True or False depending on whether Cooee is in an OSGI environment
     */
    public static boolean isOSGIEnvironment()
    {
        try
        {
            if (BundleServices.getBundleContext() != null)
            {
                return true;
            }
            return false;
        }
        catch (Throwable t)
        {
            return false;
        }
    }
}
