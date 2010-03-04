package org.karora.cooee.sandbox.testapp.testscreen;

import org.karora.cooee.app.Color;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.sandbox.consultas.app.RoundedPanel;

public class RoundedPanelTest extends SplitPane {
	
		public RoundedPanelTest() {
//			super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));
//			setStyleName("DefaultResizable");

			RoundedPanel column = new RoundedPanel();
			column.setStyleName(null);
			column.add(new Label("Hello"));
			column.setTitle("title");
			column.setTitleForeground(Color.BLUE);
			column.setBorderOn(true);
			column.setBackground(Color.RED);
//			column.setBottomRoundedCorner(true);
//			column.setTopRoundedCorner(true);

			add(column);

		}

}
