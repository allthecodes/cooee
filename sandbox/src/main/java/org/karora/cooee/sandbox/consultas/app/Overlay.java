package org.karora.cooee.sandbox.consultas.app;

import java.util.EventListener;

import org.karora.cooee.app.ContentPane;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.FloatingPane;
import org.karora.cooee.app.button.ButtonModel;
import org.karora.cooee.app.button.DefaultButtonModel;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;

public class Overlay extends ContentPane implements FloatingPane {

	public static final String ACTION_LISTENERS_CHANGED_PROPERTY = "actionListeners";

	public static final String INPUT_CLICK											 = "input_click";

	public static final String PROPERTY_MODEL										= "model";

	public static final String PROPERTY_HEIGHT									 = "height";

	public static final String PROPERTY_OPACITY									= "opacity";

	public static final String PROPERTY_POSITION_X							 = "positionX";

	public static final String PROPERTY_POSITION_Y							 = "positionY";

	public static final String PROPERTY_WIDTH										= "width";

	public Overlay() {
		setModel(new DefaultButtonModel());
	}

	public Extent getHeight() {
		return (Extent) getProperty(PROPERTY_HEIGHT);
	}

	public int getOpacity() {
		return ((Integer) getProperty(PROPERTY_OPACITY)).intValue();
	}

	public Extent getPositionX() {
		return (Extent) getProperty(PROPERTY_POSITION_X);
	}

	public Extent getPositionY() {
		return (Extent) getProperty(PROPERTY_POSITION_Y);
	}

	public Extent getWidth() {
		return (Extent) getProperty(PROPERTY_WIDTH);
	}

	public void setHeight(Extent newValue) {
		setProperty(PROPERTY_HEIGHT, newValue);
	}

	public void setOpacity(int newValue) {
		setProperty(PROPERTY_OPACITY, Integer.valueOf(newValue));
	}

	public void setPositionX(Extent newValue) {
		Extent.validate(newValue, Extent.PX);
		setProperty(PROPERTY_POSITION_X, newValue);
	}

	public void setPositionY(Extent newValue) {
		Extent.validate(newValue, Extent.PX);
		setProperty(PROPERTY_POSITION_Y, newValue);
	}

	public void setWidth(Extent newValue) {
		setProperty(PROPERTY_WIDTH, newValue);
	}

	public ButtonModel getModel() {
		return (ButtonModel) getProperty(PROPERTY_MODEL);
	}

	private ActionListener actionForwarder = new ActionListener() {

																					 /**
                                             * @see org.karora.cooee.app.event.ActionListener#actionPerformed(org.karora.cooee.app.event.ActionEvent)
                                             */
																					 public void actionPerformed(ActionEvent modelEvent) {
																						 ActionEvent buttonEvent = new ActionEvent(this,
																								 modelEvent.getActionCommand());
																						 fireActionPerformed(buttonEvent);
																					 }
																				 };

	/**
   * Adds an <code>ActionListener</code> to receive notification of user
   * actions, i.e., button presses.
   * 
   * @param l
   *          the listener to add
   */
	public void addActionListener(ActionListener l) {
		getEventListenerList().addListener(ActionListener.class, l);
		// Notification of action listener changes is provided due to
		// existence of hasActionListeners() method.
		firePropertyChange(ACTION_LISTENERS_CHANGED_PROPERTY, null, l);
	}

	/**
   * Programmatically performs a click/activation of the button.
   */
	public void doAction() {
		getModel().doAction();
	}

	/**
   * Notifies all listeners that have registered for this event type.
   * 
   * @param e
   *          the <code>ActionEvent</code> to send
   */
	public void fireActionPerformed(ActionEvent e) {
		if (!hasEventListenerList()) {
			return;
		}
		EventListener[] listeners = getEventListenerList().getListeners(ActionListener.class);
		for (int index = 0; index < listeners.length; ++index) {
			((ActionListener) listeners[index]).actionPerformed(e);
		}
	}

	public void processInput(String name, Object value) {
		super.processInput(name, value);
		if (INPUT_CLICK.equals(name)) {
			doAction();
		}
	}

	public String getActionCommand() {
		return getModel().getActionCommand();
	}

	public boolean hasActionListeners() {
		return hasEventListenerList()
				&& getEventListenerList().getListenerCount(ActionListener.class) != 0;
	}

	public void removeActionListener(ActionListener l) {
		if (!hasEventListenerList()) {
			return;
		}
		getEventListenerList().removeListener(ActionListener.class, l);
		// Notification of action listener changes is provided due to
		// existence of hasActionListeners() method.
		firePropertyChange(ACTION_LISTENERS_CHANGED_PROPERTY, l, null);
	}

	public void setActionCommand(String newValue) {
		getModel().setActionCommand(newValue);
	}

	public void setModel(ButtonModel newValue) {
		if (newValue == null) {
			throw new IllegalArgumentException("Model may not be null.");
		}

		ButtonModel oldValue = getModel();

		if (oldValue != null) {
			oldValue.removeActionListener(actionForwarder);
		}

		newValue.addActionListener(actionForwarder);

		setProperty(PROPERTY_MODEL, newValue);
	}

}
