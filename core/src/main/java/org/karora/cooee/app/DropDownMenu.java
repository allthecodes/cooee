package org.karora.cooee.app;

import org.karora.cooee.app.menu.AbstractMenuComponent;
import org.karora.cooee.app.menu.MenuModel;
import org.karora.cooee.app.menu.MenuSelectionModel;
import org.karora.cooee.app.menu.MenuStateModel;

import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.FillImage;
import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.event.ChangeEvent;
import org.karora.cooee.app.event.ChangeListener;

/**
 * EXPERIMENTAL: Under Development, API may change.
 */
public class DropDownMenu extends AbstractMenuComponent {
    
    public static final String PROPERTY_BACKGROUND_IMAGE = "backgroundImage";
    public static final String PROPERTY_BORDER = "border";
    public static final String PROPERTY_DISABLED_BACKGROUND = "disabledBackground";
    public static final String PROPERTY_DISABLED_BACKGROUND_IMAGE = "disabledBackgroundImage";
    public static final String PROPERTY_DISABLED_FOREGROUND = "disabledForeground";
    public static final String PROPERTY_DISABLED_EXPAND_ICON = "disabledExpandIcon";
    public static final String PROPERTY_EXPAND_ICON = "expandIcon";
    public static final String PROPERTY_HEIGHT = "height";
    public static final String PROPERTY_MENU_BACKGROUND = "menuBackground";
    public static final String PROPERTY_MENU_BACKGROUND_IMAGE = "menuBackgroundImage";
    public static final String PROPERTY_MENU_BORDER = "menuBorder";
    public static final String PROPERTY_MENU_FOREGROUND = "menuForeground";
    public static final String PROPERTY_SELECTION_BACKGROUND = "selectionBackground";
    public static final String PROPERTY_SELECTION_BACKGROUND_IMAGE = "selectionBackgroundImage";
    public static final String PROPERTY_SELECTION_FOREGROUND = "selectionForeground";
    public static final String PROPERTY_WIDTH = "width";
    
    public static final String SELECTION_CHANGED_PROPERTY = "selection";
    public static final String SELECTION_MODEL_CHANGED_PROPERTY = "selectionModel";
    
    private MenuSelectionModel selectionModel;
    
    private String defaultMenuId = null;
    
    private ChangeListener changeListener = new ChangeListener(){
    
        public void stateChanged(ChangeEvent e) {
            firePropertyChange(SELECTION_CHANGED_PROPERTY, null, null);
        }
    };
    
    /**
     * Creates a new <code>DropDownMenu</code> with an empty
     * <code>DefaultMenuModel</code> as its model and a.
     * <code>DefaultMenuStateModel</code> to provide state information.
     */
    public DropDownMenu() {
        this(null);
    }
    
    /**
     * Creates a new <code>DropDownMenu</code> displaying the specified 
     * <code>MenuModel</code> and using a 
     * <code>DefaultMenuStateModel</code> to provide state information.
     * 
     * @param model the model
     */
    public DropDownMenu(MenuModel model) {
        this(model, (MenuStateModel)null, null);
    }
    
    /**
     * Creates a new <code>DropDownMenu</code> displaying the specified 
     * <code>MenuModel</code> with a default menu Id.
     * 
     * @param model the model
     */
    public DropDownMenu(MenuModel model, String defaultMenuId) {
        this(model, (MenuStateModel)null, defaultMenuId);
    }

    /**
     * Creates a new <code>DropDownMenu</code> displaying the specified 
     * <code>MenuModel</code> and using the specified 
     * <code>MenuStateModel</code> to provide state information.
     * 
     * @param model the model
     * @param stateModel the selection model
     */
    public DropDownMenu(MenuModel model, MenuStateModel stateModel) {
        this(model, stateModel, null);
    }
    
    public DropDownMenu(MenuModel model, MenuSelectionModel selectionModel) {
        this(model, selectionModel, null);
    }
    
    /**
     * Creates a new <code>DropDownMenu</code> displaying the specified 
     * <code>MenuModel</code> and using the specified 
     * <code>MenuStateModel</code> to provide state information with
     * a default menu Id.
     * 
     * @param model the model
     * @param stateModel the selection model
     */
    public DropDownMenu(MenuModel model, MenuStateModel stateModel, String defaultMenuId) {
        super(model, stateModel);
        setDefaultMenuId(defaultMenuId);
    }
    
    /**
     * Creates a new <code>DropDownMenu</code> displaying the specified 
     * <code>MenuModel</code> and using the specified 
     * <code>MenuSelectionModel</code> with a default menu Id.
     * 
     * @param model the model
     * @param stateModel the selection model
     */
    public DropDownMenu(MenuModel model, MenuSelectionModel selectionModel, String defaultMenuId) {
        super(model, null);
        setSelectionModel(selectionModel);
        setDefaultMenuId(defaultMenuId);
    }

    public MenuSelectionModel getSelectionModel() {
        return selectionModel;
    }
    
    /**
     * Returns the background image that will be displayed in the 
     * <code>DropDownMenu</code>.  This background image will also be 
     * used around pull-down menus in the event that a menu 
     * background image is not specified.
     * 
     * @return the default background image
     */
    public FillImage getBackgroundImage() {
        return (FillImage) getProperty(PROPERTY_BACKGROUND_IMAGE);
    }
    
    /**
     * Returns the border that will be displayed around the 
     * <code>DropDownMenu</code>.  This border will also be used around
     * pull-down menus in the event that a menu border is not specified.
     * 
     * @return the default border
     */
    public Border getBorder() {
        return (Border) getProperty(PROPERTY_BORDER);
    }

    /**
     * Returns the background color used to render disabled menu items.
     * 
     * @return the disabled background
     */
    public Color getDisabledBackground() {
        return (Color) getProperty(PROPERTY_DISABLED_BACKGROUND);
    }
    
    /**
     * Returns the background image used to render disabled menu items.
     * 
     * @return the disabled background image
     */
    public FillImage getDisabledBackgroundImage() {
        return (FillImage) getProperty(PROPERTY_DISABLED_BACKGROUND_IMAGE);
    }
    
    /**
     * Returns the disabled expand icon.
     * 
     * @return the disabled expand icon
     */
    public ImageReference getDisabledExpandIcon() {
        return (ImageReference) getProperty(PROPERTY_DISABLED_EXPAND_ICON);
    }
    
    /**
     * Returns the foreground color used to render disabled menu items.
     * 
     * @return the disabled foreground
     */
    public Color getDisabledForeground() {
        return (Color) getProperty(PROPERTY_DISABLED_FOREGROUND);
    }
    
    /**
     * Returns the icon used to expand the drop down menu.
     * 
     * @return the expand icon
     */
    public ImageReference getExpandIcon() {
        return (ImageReference) getProperty(PROPERTY_EXPAND_ICON);
    }
    
    /**
     * Returns the height of the drop down menu.
     * 
     * @return the height
     */
    public Extent getHeight() {
        return (Extent) getProperty(PROPERTY_HEIGHT);
    }
    
    /**
     * Returns the background color that will be displayed in pull-down
     * menus.  Use this property only if a color different from
     * the one used for the menu bar is desired for pull-down menus
     * (otherwise use only the "background" property").
     * 
     * @return the menu background
     */
    public Color getMenuBackground() {
        return (Color) getProperty(PROPERTY_MENU_BACKGROUND);
    }
    
    /**
     * Returns the background image that will be displayed in pull-down
     * menus.  Use this property only if an image different from
     * the one used for the menu bar is desired for pull-down menus
     * (otherwise use only the "backgroundImage" property").
     * 
     * @return the menu background image
     */
    public FillImage getMenuBackgroundImage() {
        return (FillImage) getProperty(PROPERTY_MENU_BACKGROUND_IMAGE);
    }
    
    /**
     * Returns the border that will be displayed around pull-down
     * menus.  Use this property only if a border different from
     * the one used for the menu bar is desired for pull-down menus
     * (otherwise use only the "border" property").
     * 
     * @return the menu border
     */
    public Border getMenuBorder() {
        return (Border) getProperty(PROPERTY_MENU_BORDER);
    }
    
    /**
     * Returns the foreground color that will be displayed in pull-down
     * menus.  Use this property only if a color different from
     * the one used for the menu bar is desired for pull-down menus
     * (otherwise use only the "foreground" property").
     * 
     * @return the menu foreground
     */
    public Color getMenuForeground() {
        return (Color) getProperty(PROPERTY_MENU_FOREGROUND);
    }
    
    /**
     * Returns the background color used to highlight the currently 
     * selected menu item.
     * 
     * @return the selection background
     */
    public Color getSelectionBackground() {
        return (Color) getProperty(PROPERTY_SELECTION_BACKGROUND);
    }
    
    /**
     * Returns the background image used to highlight the currently 
     * selected menu item.
     * 
     * @return the selection background image
     */
    public FillImage getSelectionBackgroundImage() {
        return (FillImage) getProperty(PROPERTY_SELECTION_BACKGROUND_IMAGE);
    }
    
    /**
     * Returns the foreground color used to highlight the currently 
     * selected menu item.
     * 
     * @return the selection foreground
     */
    public Color getSelectionForeground() {
        return (Color) getProperty(PROPERTY_SELECTION_FOREGROUND);
    }
    
    /**
     * Returns the width of the drop down menu.
     * 
     * @return the width
     */
    public Extent getWidth() {
        return (Extent) getProperty(PROPERTY_WIDTH);
    }
    
    /**
     * Returns the menu item to be displayed on the dropdown.
     * 
     * @return the default menu Id
     */
    public String getDefaultMenuId() {
        return defaultMenuId;
    }

    /**
     * Sets the background image that will be displayed in the 
     * <code>DropDownMenu</code>.  This background image will also be 
     * used around pull-down menus in the event that a menu 
     * background image is not specified.
     * 
     * @param newValue the new default background image
     */
    public void setBackgroundImage(FillImage newValue) {
        setProperty(PROPERTY_BACKGROUND_IMAGE, newValue);
    }
    /**
     * Sets the border that will be displayed around the 
     * <code>DropDownMenu</code>.  This border will also be used around
     * pull-down menus in the event that a menu border is not specified.
     * 
     * @param newValue the new default border
     */
    public void setBorder(Border newValue) {
        setProperty(PROPERTY_BORDER, newValue);
    }
    
    /**
     * Sets the background color used to render disabled menu items.
     * 
     * @param newValue the new disabled background
     */
    public void setDisabledBackground(Color newValue) {
        setProperty(PROPERTY_DISABLED_BACKGROUND, newValue);
    }
    
    /**
     * Sets the background image used to render disabled menu items.
     * 
     * @param newValue the new disabled background image
     */
    public void setDisabledBackgroundImage(FillImage newValue) {
        setProperty(PROPERTY_DISABLED_BACKGROUND_IMAGE, newValue);
    }
    
    /**
     * Sets the disabled expand icon.
     * 
     * @param newValue the new disabled expand icon
     */
    public void setDisabledExpandIcon(ImageReference newValue) {
        setProperty(PROPERTY_DISABLED_EXPAND_ICON, newValue);
    }
    
    /**
     * Sets the foreground color used to render disabled menu items.
     * 
     * @param newValue the new disabled foreground
     */
    public void setDisabledForeground(Color newValue) {
        setProperty(PROPERTY_DISABLED_FOREGROUND, newValue);
    }
    
    /**
     * Sets the icon used to expand the drop down menu.
     * 
     * @param newValue the new expand icon
     */
    public void setExpandIcon(ImageReference newValue) {
        setProperty(PROPERTY_EXPAND_ICON, newValue);
    }
    
    /**
     * Sets the height of the drop down menu.
     * 
     * @param newValue the new height
     */
    public void setHeight(Extent newValue) {
        setProperty(PROPERTY_HEIGHT, newValue);
    }
    
    /**
     * Sets the background color that will be displayed in pull-down
     * menus.  Use this property only if a color different from
     * the one used for the menu bar is desired for pull-down menus
     * (otherwise use only the "background" property").
     * 
     * @param newValue the new menu background
     */
    public void setMenuBackground(Color newValue) {
        setProperty(PROPERTY_MENU_BACKGROUND, newValue);
    }
    
    /**
     * Sets the background image that will be displayed in pull-down
     * menus.  Use this property only if an image different from
     * the one used for the menu bar is desired for pull-down menus
     * (otherwise use only the "backgroundImage" property").
     * 
     * @param newValue the new menu background image
     */
    public void setMenuBackgroundImage(FillImage newValue) {
        setProperty(PROPERTY_MENU_BACKGROUND_IMAGE, newValue);
    }
    
    /**
     * Sets the border that will be displayed around pull-down
     * menus.  Use this property only if a border different from
     * the one used for the menu bar is desired for pull-down menus
     * (otherwise use only the "border" property").
     * 
     * @param newValue the new menu border
     */
    public void setMenuBorder(Border newValue) {
        setProperty(PROPERTY_MENU_BORDER, newValue);
    }
    
    /**
     * Sets the foreground color that will be displayed in pull-down
     * menus.  Use this property only if a color different from
     * the one used for the menu bar is desired for pull-down menus
     * (otherwise use only the "foreground" property").
     * 
     * @param newValue the new menu foreground
     */
    public void setMenuForeground(Color newValue) {
        setProperty(PROPERTY_MENU_FOREGROUND, newValue);
    }
    
    /**
     * Sets the background color used to highlight the currently 
     * selected menu item.
     * 
     * @param newValue the new selection background
     */
    public void setSelectionBackground(Color newValue) {
        setProperty(PROPERTY_SELECTION_BACKGROUND, newValue);
    }
    
    /**
     * Sets the background image used to highlight the currently 
     * selected menu item.
     * 
     * @param newValue the new selection background image
     */
    public void setSelectionBackgroundImage(FillImage newValue) {
        setProperty(PROPERTY_SELECTION_BACKGROUND_IMAGE, newValue);
    }
    
    /**
     * Sets the foreground color used to highlight the currently 
     * selected menu item.
     * 
     * @param newValue the new selection foreground
     */
    public void setSelectionForeground(Color newValue) {
        setProperty(PROPERTY_SELECTION_FOREGROUND, newValue);
    }
    
    /**
     * Sets the selection model to use.
     * 
     * @param selectionModel the new selection model
     */
    public void setSelectionModel(MenuSelectionModel newValue) {
        MenuSelectionModel oldValue = selectionModel;
        if (oldValue != null) {
            oldValue.removeChangeListener(changeListener);
        }
        selectionModel = newValue;
        if (newValue != null) {
            newValue.addChangeListener(changeListener);
        }
        firePropertyChange(SELECTION_MODEL_CHANGED_PROPERTY, oldValue, newValue);
    }
    
    /**
     * Sets the width of the drop down menu.
     * 
     * @param newValue the new width
     */
    public void setWidth(Extent newValue) {
        setProperty(PROPERTY_WIDTH, newValue);
    }

    /**
     * Sets the menu item to display on the drop down menu.
     * 
     * @param newValue the new default menu Id.
     */
    public void setDefaultMenuId(String defaultMenuId) {
        this.defaultMenuId = defaultMenuId;
        firePropertyChange(SELECTION_CHANGED_PROPERTY, null, null);
    }
}
