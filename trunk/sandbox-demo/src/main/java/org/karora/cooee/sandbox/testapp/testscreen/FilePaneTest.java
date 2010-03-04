package org.karora.cooee.sandbox.testapp.testscreen;

import org.karora.cooee.app.Extent;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.sandbox.filetransfer.app.FilePane;
import org.karora.cooee.sandbox.filetransfer.app.ResourceDownloadProvider;
import org.karora.cooee.sandbox.testapp.ButtonColumn;

public class FilePaneTest extends SplitPane {

    public FilePaneTest() {
        super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));
        setStyleName("DefaultResizable");
        
        final FilePane filePane = new FilePane();

        ButtonColumn controlsColumn = new ButtonColumn();
        controlsColumn.setStyleName("TestControlsColumn");
        
        controlsColumn.addButton("Provider: Null", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filePane.setProvider(null);
            }
        });
        
        controlsColumn.addButton("Provider: Resource PDF", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filePane.setProvider(new ResourceDownloadProvider(
                        "/org/karora/cooee/sandbox/testapp/resource/testfile/Test.pdf", "application/pdf"));
            }
        });
        
        add(controlsColumn);
        
        filePane.setProvider(new ResourceDownloadProvider(
                "/org/karora/cooee/sandbox/testapp/resource/testfile/Test.pdf", "application/pdf"));
        add(filePane);
    }
}
