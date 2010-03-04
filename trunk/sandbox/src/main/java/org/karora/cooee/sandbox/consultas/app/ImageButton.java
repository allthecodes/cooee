package org.karora.cooee.sandbox.consultas.app;

import org.karora.cooee.app.Extent;
import org.karora.cooee.app.FillImage;
import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.button.AbstractButton;
import org.karora.cooee.app.button.DefaultButtonModel;

/**
 * An implementation of a &quot;push&quot; button where
 * the background image can consist of three images:
 * a left and a right border part, and a center part
 */
public class ImageButton extends AbstractButton {

	/**
   * Creates a button with no text or icon.
   */
	public ImageButton() {
		this(null, null);
	}

	/**
   * Creates a button with text.
   * 
   * @param text
   *          the text to be displayed in the button
   */
	public ImageButton(String text) {
		this(text, null);
	}

	/**
   * Creates a button with an icon.
   * 
   * @param icon
   *          the icon to be displayed in the button
   */
	public ImageButton(ImageReference icon) {
		this(null, icon);
	}

	/**
   * Creates a button with text and an icon.
   * 
   * @param text
   *          the text to be displayed in the button
   * @param icon
   *          the icon to be displayed in the button
   */
	public ImageButton(String text, ImageReference icon) {
		super();

		setModel(new DefaultButtonModel());

		setIcon(icon);
		setText(text);
	}

	public static final String PROPERTY_BACKGROUND_IMAGE_LEFT					 = "backgroundImageLeft";

	public static final String PROPERTY_BACKGROUND_IMAGE_RIGHT					= "backgroundImageRight";

	public static final String PROPERTY_BACKGROUND_IMAGE_LEFT_WIDTH		 = "backgroundImageLeftWidth";

	public static final String PROPERTY_BACKGROUND_IMAGE_RIGHT_WIDTH		= "backgroundImageRightWidth";

	public static final String PROPERTY_DISABLED_BACKGROUND_IMAGE_LEFT	= "disabledBackgroundImageLeft";

	public static final String PROPERTY_PRESSED_BACKGROUND_IMAGE_LEFT	 = "pressedBackgroundImageLeft";

	public static final String PROPERTY_ROLLOVER_BACKGROUND_IMAGE_LEFT	= "rolloverBackgroundImageLeft";

	public static final String PROPERTY_DISABLED_BACKGROUND_IMAGE_RIGHT = "disabledBackgroundImageRight";

	public static final String PROPERTY_PRESSED_BACKGROUND_IMAGE_RIGHT	= "pressedBackgroundImageRight";

	public static final String PROPERTY_ROLLOVER_BACKGROUND_IMAGE_RIGHT = "rolloverBackgroundImageRight";

	/**
     * Returns the left part of the background image 
     * displayed when the button is disabled.
     * 
     * @return the background image
     */
	public FillImage getDisabledBackgroundImageLeft() {
		return (FillImage) getProperty(PROPERTY_DISABLED_BACKGROUND_IMAGE_LEFT);
	}
	
	/**
     * Returns the right part of the background image 
     * displayed when the button is disabled.
     * 
     * @return the background image
     */
	public FillImage getDisabledBackgroundImageRight() {
		return (FillImage) getProperty(PROPERTY_DISABLED_BACKGROUND_IMAGE_RIGHT);
	}
	
	/**
     * Returns the left part of the background image displayed 
     * when the button is pressed. 
     * 
     * @return the background image
     */
	public FillImage getPressedBackgroundImageLeft() {
		return (FillImage) getProperty(PROPERTY_PRESSED_BACKGROUND_IMAGE_RIGHT);
	}

	/**
     * Returns the right part of the background image displayed 
     * when the button is pressed. 
     * 
     * @return the background image
     */
	public FillImage getPressedBackgroundImageRight() {
		return (FillImage) getProperty(PROPERTY_PRESSED_BACKGROUND_IMAGE_RIGHT);
	}

	/**
     * Returns the left part of the background image 
     * displayed when the mouse cursor is inside
     * the button's bounds.
     * 
     * @return the background image
     */
	public FillImage getRolloverBackgroundImageLeft() {
		return (FillImage) getProperty(PROPERTY_ROLLOVER_BACKGROUND_IMAGE_LEFT);
	}

	/**
     * Returns the right part of the background image 
     * displayed when the mouse cursor is inside
     * the button's bounds.
     * 
     * @return the background image
     */
	public FillImage getRolloverBackgroundImageRight() {
		return (FillImage) getProperty(PROPERTY_ROLLOVER_BACKGROUND_IMAGE_RIGHT);
	}

	/**
     * Returns the right part of the background image. 
     * 
     * @return the background image
     */
	public FillImage getBackgroundImageRight() {
		return (FillImage) getProperty(PROPERTY_BACKGROUND_IMAGE_RIGHT);
	}

	/**
     * Returns the left part of the background image .
     * 
     * @return the background image
     */
	public FillImage getBackgroundImageLeft() {
		return (FillImage) getProperty(PROPERTY_BACKGROUND_IMAGE_LEFT);
	}

	/**
     * Returns the width of the right part of the 
	 * background image.
     * 
     * @return <code>Extent</code> containing the width
     */
	public Extent getBackgroundImageRightWidth() {
		return (Extent) getProperty(PROPERTY_BACKGROUND_IMAGE_RIGHT_WIDTH);
	}

	/**
     * Returns the width of the left part of the 
	 * background image.
     * 
     * @return <code>Extent</code> containing the width
     */
	public Extent getBackgroundImageLeftWidth() {
		return (Extent) getProperty(PROPERTY_BACKGROUND_IMAGE_LEFT_WIDTH);
	}

    /**
     * Sets the left part of the background image 
     * when the button is disabled.
     * 
     * @param newValue the new background image
     */	
	public void setDisabledBackgroundImageLeft(FillImage newValue) {
		setProperty(PROPERTY_DISABLED_BACKGROUND_IMAGE_LEFT, newValue);
	}

    /**
     * Sets the right part of the background image 
     * when the button is disabled.
     * 
     * @param newValue the new background image
     */	
	public void setDisabledBackgroundImageRight(FillImage newValue) {
		setProperty(PROPERTY_DISABLED_BACKGROUND_IMAGE_RIGHT, newValue);
	}

    /**
     * Sets the left part of the background image.
     * 
     * @param newValue the new background image
     */	
	public void setBackgroundImageLeft(FillImage newValue) {
		setProperty(PROPERTY_BACKGROUND_IMAGE_LEFT, newValue);
	}

    /**
     * Sets the right part of the background image. 
     * 
     * @param newValue the new background image
     */	
	public void setBackgroundImageRight(FillImage newValue) {
		setProperty(PROPERTY_BACKGROUND_IMAGE_RIGHT, newValue);
	}

    /**
     * Sets the left part of the background image 
     * displayed when the button is pressed.
     * 
     * @param newValue the new background image
     */	
	public void setPressedBackgroundImageLeft(FillImage newValue) {
		setProperty(PROPERTY_PRESSED_BACKGROUND_IMAGE_LEFT, newValue);
	}

    /**
     * Sets the right part of the background image 
     * displayed when the button is pressed.
     * 
     * @param newValue the new background image
     */	
	public void setPressedBackgroundImageRight(FillImage newValue) {
		setProperty(PROPERTY_PRESSED_BACKGROUND_IMAGE_RIGHT, newValue);
	}

    /**
     * Sets the left part of the background image 
     * displayed when the mouse cursor is inside the
     * button's bounds.
     * 
     * @param newValue the new background image
     */	
	public void setRolloverBackgroundImageLeft(FillImage newValue) {
		setProperty(PROPERTY_ROLLOVER_BACKGROUND_IMAGE_LEFT, newValue);
	}

    /**
     * Sets the right part of the background image 
     * displayed when the mouse cursor is inside the
     * button's bounds.
     * 
     * @param newValue the new background image
     */	
	public void setRolloverBackgroundImageRight(FillImage newValue) {
		setProperty(PROPERTY_ROLLOVER_BACKGROUND_IMAGE_RIGHT, newValue);
	}

	/**
     * Sets the width of the right part of the 
	 * background image.
     * 
     * @param newValue the width
     */
	public void setBackgroundImageRightWidth(Extent newValue) {
		setProperty(PROPERTY_BACKGROUND_IMAGE_RIGHT_WIDTH, newValue);
	}

	/**
     * Sets the width of the left part of the 
	 * background image.
     * 
     * @param newValue the width
     */
	public void setBackgroundImageLeftWidth(Extent newValue) {
		setProperty(PROPERTY_BACKGROUND_IMAGE_LEFT_WIDTH, newValue);
	}

}
