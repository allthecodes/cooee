package org.karora.cooee.sandbox.testapp.testscreen;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.karora.cooee.app.Color;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.ContentPane;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.ng.PushButton;
import org.karora.cooee.sandbox.consultas.app.DragableOverlay;
import org.karora.cooee.sandbox.consultas.app.Overlay;
import org.karora.cooee.sandbox.consultas.app.SpWindowPane;
import org.karora.cooee.sandbox.testapp.ButtonColumn;
import org.karora.cooee.sandbox.testapp.StyleUtil;

public class DragableOverlayTest extends SplitPane {

	private DragableOverlay overlay;
	private ContentPane contentPane;
	private Label msg;

	public DragableOverlayTest() {
		super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));
		setStyleName("DefaultResizable");

		Column groupContainerColumn = new Column();
		groupContainerColumn.setCellSpacing(new Extent(5));
		groupContainerColumn.setStyleName("TestControlsColumn");
		add(groupContainerColumn);

		contentPane = new ContentPane();
		add(contentPane);
		
		overlay = new DragableOverlay();
		
		overlay.setOpacity(50);
		overlay.setWidth(new Extent(300,Extent.PX));
		overlay.setHeight(new Extent(150,Extent.PX));
		overlay.setPositionX(new Extent(100,Extent.PX));
		overlay.setPositionY(new Extent(100,Extent.PX));
		overlay.setBackground(StyleUtil.randomColor());
		
		overlay.addPropertyChangeListener(new PropertyChangeListener(){

			public void propertyChange(PropertyChangeEvent evt) {
				msg.setText("Size : " + overlay.getWidth() + ":" + 
																overlay.getHeight() + " Position : " +
																overlay.getPositionX() + ":" +
																overlay.getPositionY());
				
			}
			
		});
		
		Column col = new Column();
		
		msg = new Label("");
		
		PushButton button = new PushButton("Update...");
		
		button.addActionListener(new ActionListener() {
			
				public void actionPerformed(ActionEvent e) { 	
    }});
		
		col.add(msg);
		col.add(button);
		col.add(new Label(StyleUtil.QUASI_LATIN_TEXT_1));
		
		contentPane.add(col);
		contentPane.add(overlay);

		 ButtonColumn controlsColumn;
		
		   // Content
	        
	        controlsColumn = new ButtonColumn();
	        controlsColumn.add(new Label("Content"));
	        groupContainerColumn.add(controlsColumn);
	        
	        controlsColumn.addButton("Set Content = Small Label", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	overlay.removeAll();
	            	overlay.add(new Label("Hello, World!"));
	            }
	        });
	        
	        controlsColumn.addButton("Set Content = Big Label", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	overlay.removeAll();
	            	overlay.add(new Label(StyleUtil.QUASI_LATIN_TEXT_1));
	            }
	        });
	        
	        controlsColumn.addButton("Set Content = PushButton", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	overlay.removeAll();
            	overlay.add(new PushButton("Push me!"));
            }
	        });
	        controlsColumn.addButton("Set Content = Nothing", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                overlay.removeAll();
	            }
	        });
	        
	        controlsColumn.addButton("Add-Remove-Add", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                overlay.removeAll();
	                Label label = new Label("Hello, World!");
	                overlay.add(label);
	                overlay.remove(label);
	                overlay.add(label);
	            }
	        });
	        
	        // Properties
	        
	        controlsColumn = new ButtonColumn();
	        controlsColumn.add(new Label("Properties"));
	        groupContainerColumn.add(controlsColumn);
	        
	        
	        controlsColumn.addButton("Set Position Random", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                overlay.setPositionX(new Extent((int) (Math.random() * 600)));
	                overlay.setPositionY(new Extent((int) (Math.random() * 500)));
	            }
	        });
	        controlsColumn.addButton("Set Size Random", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                overlay.setWidth(new Extent(100 + (int) (Math.random() * 400)));
	                overlay.setHeight(new Extent(100 + (int) (Math.random() * 300)));
	            }
	        });
	        controlsColumn.addButton("Set Position&Size Random", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                overlay.setPositionX(new Extent((int) (Math.random() * 600)));
	                overlay.setPositionY(new Extent((int) (Math.random() * 500)));
	                overlay.setWidth(new Extent(100 + (int) (Math.random() * 400)));
	                overlay.setHeight(new Extent(100 + (int) (Math.random() * 300)));
	            }
	        });        
	        
	        // Title-Related Properties
	        
	        controlsColumn = new ButtonColumn();
	        controlsColumn.add(new Label("Properties"));
	        groupContainerColumn.add(controlsColumn);
	        
	        controlsColumn.addButton("Increase Opacity", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                overlay.setOpacity(Math.min(100,overlay.getOpacity() + 10));
	            }
	        });
	        controlsColumn.addButton("Reset Opacity", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                overlay.setOpacity(50);
	            }
	        });
	        controlsColumn.addButton("Set Background Color", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                overlay.setBackground(StyleUtil.randomColor());
	            }
	        });
	        controlsColumn.addButton("Toggle Resizable", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                overlay.setResizable(!overlay.isResizable());
            }
	        });
	        controlsColumn.addButton("Toggle Movable", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                overlay.setMovable(!overlay.isMovable());
            }
	        });

	        // Integration Tests
	        
	        controlsColumn = new ButtonColumn();
	        controlsColumn.add(new Label("Content"));
	        groupContainerColumn.add(controlsColumn);

	        controlsColumn.addButton("Add Component", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                if (contentPane.getComponentCount() == 0) {
	                    contentPane.add(overlay);
	                }
	            }
	        });

	        controlsColumn.addButton("Remove Component", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                contentPane.remove(overlay);
	            }
	        });

	        controlsColumn.addButton("Enable Component", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                overlay.setEnabled(true);
	            }
	        });

	        controlsColumn.addButton("Disable Component", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                overlay.setEnabled(false);
	            }
	        });

	       
	    }
}
