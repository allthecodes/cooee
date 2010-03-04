/* 
 * Copyright (c) 2007, Karora and others 
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 */
 
package ${groupId};

import org.karora.cooee.app.StyleSheet;
import org.karora.cooee.app.componentxml.ComponentXmlException;
import org.karora.cooee.app.componentxml.StyleSheetLoader;

/**
 * 
 */
public class Resources {
    
    public static final String IMAGE_PATH = "/image/";
    public static final String STYLE_PATH = "/style/";
   

    public static final StyleSheet DEFAULT_STYLE_SHEET;
    static {
        try {
            DEFAULT_STYLE_SHEET = StyleSheetLoader.load(STYLE_PATH + "Default.stylesheet", 
                    Thread.currentThread().getContextClassLoader());
        } catch (ComponentXmlException ex) {
            throw new RuntimeException(ex);
        }
    }
}
