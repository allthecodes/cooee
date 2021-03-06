package org.karora.cooee.app.filetransfer;

import org.karora.cooee.app.Component;
import org.karora.cooee.app.Pane;

public class FilePane extends Component 
implements Pane {
    
    public static final String PROVIDER_CHANGED_PROPERTY = "provider";
    
    private DownloadProvider provider;

    /**
     * Creates a new <code>FilePane</code> nd with no download
     * provider.
     */
    public FilePane() {
        this(null);
    }
    
    /**
     * Creates a new <code>Download</code> command with the specified 
     * prodcuer and active state.
     *
     * @param provider The <code>DownloadProvider</code> that will provide the
     *        file download.
     * @param active True if the file should be immediately downloaded by the 
     *        client.
     */
    public FilePane(DownloadProvider provider) {
        super();
        this.provider = provider;
    }

    /** 
     * Returns the <code>DownloadProvider</code> that will provide the file
     * download.
     *
     * @return The <code>DownloadProvider</code> that will provide the file
     *         download.
     */
    public DownloadProvider getProvider() {
        return provider;
    }
    
    /**    
     * Sets the <code>DownloadProvider</code> that will provide the file
     * download.
     *
     * @param newValue A <code>DownloadProvider</code> that will provide the file
     * download.
     */
    public void setProvider(DownloadProvider newValue) {
        DownloadProvider oldValue = provider;
        provider = newValue;
        firePropertyChange(PROVIDER_CHANGED_PROPERTY, oldValue, newValue);
    }
}
