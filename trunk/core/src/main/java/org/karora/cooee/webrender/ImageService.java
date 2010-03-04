package org.karora.cooee.webrender;

import java.io.IOException;
import java.io.OutputStream;

import org.karora.cooee.app.util.Uid;
import org.karora.cooee.webrender.util.Resource;

public class ImageService implements Service {

    private final String SERVICE_ID = Uid.generateUidString();

    private ContentType contentType;
    private String resourceReference;
    
    ImageService(String resourceReference, ContentType resourceType)
    {
    	this.contentType = resourceType;
    	this.resourceReference = resourceReference;
    }
    
    /**
     * @see org.karora.cooee.webrender.Service#getId()
     */
    public String getId() {
        return SERVICE_ID;
    }

    /**
     * @see org.karora.cooee.webrender.Service#getVersion()
     */
    public int getVersion() {
        return 0;
    }

    /**
     * @see org.karora.cooee.webrender.Service#service(org.karora.cooee.webrender.Connection)
     */
    public void service(Connection conn) throws IOException {
     
    	byte[] resource = Resource.getResourceAsByteArray(resourceReference);
    	
    	conn.setContentType(contentType);
        OutputStream out = conn.getOutputStream();
        out.write(resource);

    }
}
