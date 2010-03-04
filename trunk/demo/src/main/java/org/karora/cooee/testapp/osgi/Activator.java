package org.karora.cooee.testapp.osgi;

import org.karora.cooee.ng.testapp.TestErrorPageServletNG;
import org.karora.cooee.ng.testapp.TestServletNG;
import org.karora.cooee.testapp.InteractiveServlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator{

	   private ServiceTracker httpServiceTracker;

       private static BundleContext bundleContext;
 
	    public void start(BundleContext context) throws Exception
	    {
	    	Activator.bundleContext = context;
	        registerServletService();
	    }

	    public void stop(BundleContext context) throws Exception
	    {
	        httpServiceTracker.close();
	        httpServiceTracker = null;
	    }

	    private class HttpServiceTracker extends ServiceTracker
	    {

	        public HttpServiceTracker(BundleContext context)
	        {
	            super(context, HttpService.class.getName(), null);
	        }

	        public Object addingService(ServiceReference reference)
	        {
	            HttpService httpService = (HttpService) context
	                    .getService(reference);
	            try
	            {

	            	
	            	InteractiveServlet interactiveServlet = new InteractiveServlet();
	            	httpService.registerServlet("/app", interactiveServlet, null, null); //$NON-NLS-1$
	            	
	            	TestServletNG testNGServlet = new TestServletNG();
	            	httpService.registerServlet("/ng", testNGServlet, null, null); //$NON-NLS-1$

	            	TestErrorPageServletNG errorPageServlet = new TestErrorPageServletNG();
	            	httpService.registerServlet("/testngerrorpage", errorPageServlet, null, null); //$NON-NLS-1$
	            	
	                
	            }
	            catch (Exception e)
	            {
	                e.printStackTrace();
	            }
	            return httpService;
	        }

	        public void removedService(ServiceReference reference, Object service)
	        {
	            HttpService httpService = (HttpService) service;
	            httpService.unregister("/app"); //$NON-NLS-1$
	            httpService.unregister("/ng"); //$NON-NLS-1$
	            httpService.unregister("/testngerrorpage"); //$NON-NLS-1$
	            super.removedService(reference, service);
	        }
	    }

	    public static BundleContext getContext()
	    {
	        return bundleContext;
	    }
	    
	    private void registerServletService()
	    {
	        httpServiceTracker = new HttpServiceTracker(bundleContext);
	        httpServiceTracker.open();
	    }
	    
}