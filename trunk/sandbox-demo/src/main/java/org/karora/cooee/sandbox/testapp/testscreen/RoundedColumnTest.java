package org.karora.cooee.sandbox.testapp.testscreen;

import org.karora.cooee.app.Color;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.sandbox.consultas.app.RoundedColumn;


public class RoundedColumnTest extends SplitPane {
	
		public RoundedColumnTest() {
//			super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));
//			setStyleName("DefaultResizable");

			RoundedColumn column = new RoundedColumn();
			column.setStyleName(null);
			column.add(new Label("Hello"));
 			column.setBorderOn(true);
			column.setBackground(Color.CYAN);

			add(column);

		}

}
