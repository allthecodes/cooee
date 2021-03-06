/* 
 * This file is part of the Echo2 Extras Project.
 * Copyright (C) 2005-2006 NextApp, Inc.
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

package org.karora.cooee.testapp.testscreen;

import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Border;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.DragSource;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.Row;
import org.karora.cooee.app.event.DropEvent;
import org.karora.cooee.app.event.DropListener;
import org.karora.cooee.testapp.StyleUtil;
import org.karora.cooee.testapp.Styles;

/**
 * Interactive test module for <code>ColorSelect</code>s.
 */
public class DragSourceTest extends AbstractTest {
    
    // TODO: Experimental support buttons (functionality/tests currently disabled)

    public DragSourceTest() {
        super("DragSource", Styles.ICON_16_DRAG_SOURCE);

        Row row = new Row();
        row.setAlignment(new Alignment(Alignment.LEFT, Alignment.TOP));
        row.setCellSpacing(new Extent(10, Extent.PX));
        add(row);
        setTestComponent(this, row);

        final Column labelColumn = new Column();
        labelColumn.setBorder(new Border(2, Color.BLUE, Border.STYLE_INSET));
        labelColumn.add(new Label("Drag Sources: Labels"));

        /*
        final Column buttonColumn = new Column();
        buttonColumn.setBorder(new Border(2, Color.BLUE, Border.STYLE_INSET));
        buttonColumn.add(new Label("Drag Sources: Buttons"));
        */
        
        final Column dropTarget1 = new Column();
        dropTarget1.setBorder(new Border(2, Color.RED, Border.STYLE_OUTSET));
        dropTarget1.add(new Label("Drop Target 1"));

        final Column dropTarget2 = new Column();
        dropTarget2.setBorder(new Border(2, Color.RED, Border.STYLE_OUTSET));
        dropTarget2.add(new Label("Drop Target 2"));

        row.add(labelColumn);
        // row.add(buttonColumn);
        row.add(dropTarget1);
        row.add(dropTarget2);

        for (int i = 0; i < 6; i++) {
            Label label = new Label("Draggable Label " + i);
            label.setBackground(StyleUtil.randomBrightColor());
            DragSource ds = new DragSource(label);
            ds.addDropTarget(dropTarget1);
            ds.addDropTarget(dropTarget2);
            ds.addDropTargetListener(new DropListener() {
                public void dropPerformed(DropEvent event) {
                    DragSource dragged = (DragSource) event.getSource();
                    labelColumn.remove(dragged);
                    Component dropTarget = (Component) event.getTarget();
                    dropTarget.add(dragged.getComponent(0));
                }
            });
            labelColumn.add(ds);
        }

        /*
        for (int i = 0; i < 6; i++) {
            Button button = new Button("Draggable Button " + i);
            button.setBackground(StyleUtil.randomBrightColor());
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    WindowPane alertWindow = new WindowPane();
                    alertWindow.setModal(true);
                    alertWindow.setTitle("Button Clicked");
                    alertWindow.setInsets(new Insets(10));
                    alertWindow.add(new Label("A draggable button was clicked."));
                    InteractiveApp.getApp().getDefaultWindow().getContent().add(alertWindow);
                }
            });
            DragSource ds = new DragSource(button);
            ds.addDropTarget(dropTarget1);
            ds.addDropTarget(dropTarget2);
            ds.addDropTargetListener(new DropListener() {
                public void dropPerformed(DropEvent event) {
                    DragSource dragged = (DragSource) event.getSource();
                    buttonColumn.remove(dragged);
                    Component dropTarget = (Component) event.getTarget();
                    dropTarget.add(dragged.getComponent(0));
                }
            });
            buttonColumn.add(ds);
        }
        */

        addStandardIntegrationTests();
    }

}
