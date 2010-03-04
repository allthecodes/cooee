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

import org.karora.cooee.app.ApplicationInstance;
import org.karora.cooee.webcontainer.WebContainerServlet;

/**
 * Cooee World Application Servlet Implementation.
 */
public class CooeeServlet extends WebContainerServlet {

    /**
     * @see org.karora.cooee.webcontainer.WebContainerServlet#newApplicationInstance()
     */
    public ApplicationInstance newApplicationInstance() {
        return new CooeeApp();
    }
}
