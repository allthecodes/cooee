package ${groupId};

import org.karora.cooee.app.Button;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.ContentPane;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;

public class MainWindow extends ContentPane {

	private SplitPane horizontalPane;

	private ActionListener commandActionListener = new ActionListener() {

		private Button activeButton = null;

		public void actionPerformed(ActionEvent e) {

			if (activeButton != null) {
				activeButton.setStyleName("Default");
			}
			Button button = (Button) e.getSource();
			button.setStyleName("Selected");
			activeButton = button;
			String screenClassName = e.getActionCommand();

			try {

				Class screenClass = Class.forName(screenClassName);
				Component content = (Component) screenClass.newInstance();
				if (horizontalPane.getComponentCount() > 1) {
					horizontalPane.remove(1);
				}
				horizontalPane.add(content);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}

		}
	};

	private Column testLaunchButtonsColumn;

	public MainWindow() {
		super();

		SplitPane verticalPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL);
		verticalPane.setStyleName("TestPane");
		add(verticalPane);

		Label titleLabel = new Label("Cooee Archetype App");
		titleLabel.setStyleName("TitleLabel");

		verticalPane.add(titleLabel);

		horizontalPane = new SplitPane(SplitPane.ORIENTATION_HORIZONTAL,
				new Extent(215));
		horizontalPane.setStyleName("DefaultResizable");
		verticalPane.add(horizontalPane);

		Column controlsColumn = new Column();
		controlsColumn.setStyleName("ApplicationControlsColumn");
		controlsColumn.setCellSpacing(new Extent(5));

		horizontalPane.add(controlsColumn);

		testLaunchButtonsColumn = new Column();
		controlsColumn.add(testLaunchButtonsColumn);

		addTest("Show Hello", "com.demo.HelloPane");

		Column applicationControlsColumn = new Column();
		controlsColumn.add(applicationControlsColumn);

	}

	private void addTest(String name, String action) {
		Button button = new Button(name);
		button.setActionCommand(action);
		button.setStyleName("Default");
		button.addActionListener(commandActionListener);
		testLaunchButtonsColumn.add(button);
	}
}
