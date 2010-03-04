/* 
 * This file is part of the Tucana Echo2 Library.
 * Copyright (C) 2006.
 *
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 */

package org.karora.cooee.sandbox.tucana.app.widgetdash;

import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Border;
import org.karora.cooee.app.Button;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Font;
import org.karora.cooee.app.Grid;
import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.ResourceImageReference;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.layout.GridLayoutData;
import org.karora.cooee.sandbox.tucana.app.WidgetDash;


/**
 * Default implementation of {@link WidgetContainer}.  Provides a 
 * box around the widget with a titlebar and a close button.
 * 
 * @author Jeremy Volkman
 *
 */
public class DefaultWidgetContainer extends WidgetContainer {

    private Component widgetBody;
    
    private static ImageReference closeImage;
    
    static {
        closeImage = new ResourceImageReference("org/karora/cooee/webcontainer/resource/image/DefaultCloseButton.gif",
                new Extent(17), new Extent(17));
    }
    
    public DefaultWidgetContainer(WidgetIdentifier identifier, Component widget, String title) {
        super(identifier);
        this.widgetBody = widget;
        
        Column bodyColumn = new Column();
        bodyColumn.setBackground(Color.WHITE);
        Border bodyBorder = new Border(1, Color.LIGHTGRAY, Border.STYLE_RIDGE);
        bodyColumn.setBorder(bodyBorder);
        Color titleColor = new Color(255, 222, 0);
 
        Grid titleGrid = new Grid(2);
        
        titleGrid.setBackground(titleColor);
        titleGrid.setOrientation(Grid.ORIENTATION_HORIZONTAL);
        titleGrid.setInsets(new Insets(2));
        Label label = new Label();
        label.setText(title);
        
        
        Font f = new Font(
            new Font.Typeface("verdana"), Font.PLAIN, new Extent(17)
        );
        label.setFont(f);
        label.setForeground(new Color(242, 26, 26));
        titleGrid.add(label);

        titleGrid.setColumnWidth(1, closeImage.getWidth());
        Button closeButton = new Button(closeImage);
       
        
        GridLayoutData closeLayout = new GridLayoutData();
        closeLayout.setAlignment(new Alignment(Alignment.RIGHT, Alignment.CENTER));
        closeButton.setLayoutData(closeLayout);
        closeButton.setWidth(closeImage.getWidth());
        
        final WidgetContainer container = this;
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// make sure parent is a WidgetDash which has setActiveWidgetContainer() method
            	if (container.getParent() instanceof WidgetDash 
            		&& container == ((WidgetDash)container.getParent()).getActiveWidgetContainer()) {
            			((WidgetDash)container.getParent()).setActiveWidgetContainer(null);
            	}
                container.getParent().remove(container);
            }
        });
        
       
        titleGrid.add(closeButton);
        
       
        
        titleGrid.setWidth(new Extent(100, Extent.PERCENT));
        bodyColumn.add(new WidgetGrabPoint(titleGrid, this));
        
        
        Column widgetWrapper = new Column();
        
        // Set standard widget insets so all widgets start off with the same
        // padding.  Widgets shouldn't have to set insets manually (at least
        // for their top-level component.
        widgetWrapper.setInsets(new Insets(4, 8));
        widgetWrapper.add(widget);
        bodyColumn.add(widgetWrapper);
        add(bodyColumn);
    }

    public Component getWidgetBody() {
        return widgetBody;
    }
    
    
}
