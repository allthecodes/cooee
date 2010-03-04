/*******************************************************************************
* Copyright (c) 2007, Karora and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Mozilla Public License Version
* 1.1 which is available at http://www.mozilla.org/MPL
*
* Contributors:
*     Karora organisation - initial implementation
*******************************************************************************/

package ${groupId};

import org.karora.cooee.app.ApplicationInstance;
import org.karora.cooee.app.Window;

/**
 * Hello World Application Instance.
 */
public class CooeeApp extends ApplicationInstance {

    /**
     * @see org.karora.cooee.app.ApplicationInstance#init()
     */
    public Window init() {
    	Window window = new Window();
        window.setTitle("Cooee world!");
        setStyleSheet(Resources.DEFAULT_STYLE_SHEET);
        window.setContent(new MainWindow());
        
        return window;
    }
}
