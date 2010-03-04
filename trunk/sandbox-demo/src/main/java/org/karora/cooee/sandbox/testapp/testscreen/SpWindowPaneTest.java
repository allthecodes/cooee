package org.karora.cooee.sandbox.testapp.testscreen;

import org.karora.cooee.app.Column;
import org.karora.cooee.app.ContentPane;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.sandbox.consultas.app.SpWindowPane;
import org.karora.cooee.sandbox.testapp.ButtonColumn;
import org.karora.cooee.sandbox.testapp.StyleUtil;

public class SpWindowPaneTest extends SplitPane {

	private SpWindowPane windowPane;
	private ContentPane contentPane;

	public SpWindowPaneTest() {
		super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));
		setStyleName("DefaultResizable");

		Column groupContainerColumn = new Column();
		groupContainerColumn.setCellSpacing(new Extent(5));
		groupContainerColumn.setStyleName("TestControlsColumn");
		add(groupContainerColumn);

		contentPane = new ContentPane();
		add(contentPane);
		windowPane = new SpWindowPane();
		contentPane.add(windowPane);

		 ButtonColumn controlsColumn;
		
		   // Content
	        
	        controlsColumn = new ButtonColumn();
	        controlsColumn.add(new Label("Content"));
	        groupContainerColumn.add(controlsColumn);
	        
	        controlsColumn.addButton("Set Content = Small Label", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.removeAll();
	                windowPane.add(new Label("Hello, World!"));
	            }
	        });
	        
	        controlsColumn.addButton("Set Content = Big Label", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.removeAll();
	                windowPane.add(new Label(StyleUtil.QUASI_LATIN_TEXT_1));
	            }
	        });
	        
	        
	        controlsColumn.addButton("Set Content = Nothing", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.removeAll();
	            }
	        });
	        
	        controlsColumn.addButton("Add-Remove-Add", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.removeAll();
	                Label label = new Label("Hello, World!");
	                windowPane.add(label);
	                windowPane.remove(label);
	                windowPane.add(label);
	            }
	        });
	        
	        // Properties
	        
	        controlsColumn = new ButtonColumn();
	        controlsColumn.add(new Label("Properties"));
	        groupContainerColumn.add(controlsColumn);
	        
	        controlsColumn.addButton("Set Style Name = Default", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setStyleName("Default");
	            }
	        });
	        controlsColumn.addButton("Clear Style Name", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setStyleName(null);
	            }
	        });

	        controlsColumn.addButton("Set Foreground", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setForeground(StyleUtil.randomColor());
	            }
	        });
	        controlsColumn.addButton("Clear Foreground", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setForeground(null);
	            }
	        });
	        controlsColumn.addButton("Set Background", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setBackground(StyleUtil.randomColor());
	            }
	        });
	        controlsColumn.addButton("Clear Background", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setBackground(null);
	            }
	        });
	        controlsColumn.addButton("Set Font", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setFont(StyleUtil.randomFont());
	            }
	        });
	        controlsColumn.addButton("Clear Font", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setFont(null);
	            }
	        });
	        controlsColumn.addButton("Set Content Insets to 0", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setInsets(new Insets(0));
	            }
	        });
	        controlsColumn.addButton("Set Content Insets to 5", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setInsets(new Insets(5));
	            }
	        });
	        controlsColumn.addButton("Set Content Insets to 10/20/40/80", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setInsets(new Insets(10, 20, 40, 80));
	            }
	        });
	        controlsColumn.addButton("Clear Content Insets", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setInsets(null);
	            }
	        });

	        controlsColumn.addButton("Set Position Random", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setPositionX(new Extent((int) (Math.random() * 600)));
	                windowPane.setPositionY(new Extent((int) (Math.random() * 500)));
	            }
	        });
	        controlsColumn.addButton("Set Size Random", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setWidth(new Extent(100 + (int) (Math.random() * 400)));
	                windowPane.setHeight(new Extent(100 + (int) (Math.random() * 300)));
	            }
	        });
	        controlsColumn.addButton("Set Position&Size Random", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setPositionX(new Extent((int) (Math.random() * 600)));
	                windowPane.setPositionY(new Extent((int) (Math.random() * 500)));
	                windowPane.setWidth(new Extent(100 + (int) (Math.random() * 400)));
	                windowPane.setHeight(new Extent(100 + (int) (Math.random() * 300)));
	            }
	        });        
	        
	        // Title-Related Properties
	        
	        controlsColumn = new ButtonColumn();
	        controlsColumn.add(new Label("Properties"));
	        groupContainerColumn.add(controlsColumn);
	        
	        controlsColumn.addButton("Set Title", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitle("Window Title");
	            }
	        });
	        controlsColumn.addButton("Set Title Long", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitle("This is a fairly long window title that goes on for a little to long to see if " +
	                        "wrapping is handled properly and such.");
	            }
	        });
	        controlsColumn.addButton("Clear Title", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitle(null);
	            }
	        });
	        controlsColumn.addButton("Set Title Height", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitleHeight(new Extent(((int) (Math.random() * 24)) + 24));
	            }
	        });
	        controlsColumn.addButton("Clear Title Height", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitleHeight(null);
	            }
	        });
	        controlsColumn.addButton("Set Title Insets to 0", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitleInsets(new Insets(0));
	            }
	        });
	        controlsColumn.addButton("Set Title Insets to 5", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitleInsets(new Insets(5));
	            }
	        });
	        controlsColumn.addButton("Set Title Insets to 10/20/40/80", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitleInsets(new Insets(10, 20, 40, 80));
	            }
	        });
	        controlsColumn.addButton("Clear Title Insets", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitleInsets(null);
	            }
	        });
	        controlsColumn.addButton("Set Title Foreground", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitleForeground(StyleUtil.randomColor());
	            }
	        });
	        controlsColumn.addButton("Clear Title Foreground", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitleForeground(null);
	            }
	        });
	        controlsColumn.addButton("Set Title Background", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitleBackground(StyleUtil.randomColor());
	            }
	        });
	        controlsColumn.addButton("Clear Title Background", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitleBackground(null);
	            }
	        });
	        controlsColumn.addButton("Set Title Font", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitleFont(StyleUtil.randomFont());
	            }
	        });
	        controlsColumn.addButton("Clear Title Font", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setTitleFont(null);
	            }
	        });

	        // Integration Tests
	        
	        controlsColumn = new ButtonColumn();
	        controlsColumn.add(new Label("Content"));
	        groupContainerColumn.add(controlsColumn);

	        controlsColumn.addButton("Add Component", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                if (contentPane.getComponentCount() == 0) {
	                    contentPane.add(windowPane);
	                }
	            }
	        });

	        controlsColumn.addButton("Remove Component", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                contentPane.remove(windowPane);
	            }
	        });

	        controlsColumn.addButton("Enable Component", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setEnabled(true);
	            }
	        });

	        controlsColumn.addButton("Disable Component", new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                windowPane.setEnabled(false);
	            }
	        });

	       
	    }
}
