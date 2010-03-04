package org.karora.cooee.testapp.testscreen;

import org.karora.cooee.app.AccordionPane;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.layout.AccordionPaneLayoutData;

import org.karora.cooee.app.IntegerTextField;

import org.karora.cooee.testapp.ButtonColumn;
import org.karora.cooee.testapp.InteractiveApp;
import org.karora.cooee.testapp.StyleUtil;
import org.karora.cooee.testapp.Styles;

public class IntegerTextFieldTest extends ActiveTextFieldTest {
    
    private final IntegerTextField integerTextField;
    
    public IntegerTextFieldTest() {
        super(new IntegerTextField());
        
        integerTextField = (IntegerTextField)activeTextField;

        fillTestControlsColumn(); 
        super.fillTestControlsColumn();
    }

    protected void fillTestControlsColumn() {        

        // Build the test control buttons as small groups (controlsColumn) 
        //   placed within a larger group (controlGroupsColumn) 
        ButtonColumn controlsColumn;
        AccordionPaneLayoutData layoutData;
                
        controlsColumn = new ButtonColumn();
        controlGroupsColumn.add(controlsColumn);
        layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("IntegerTextField Properties");
        controlsColumn.setLayoutData(layoutData);
        
        controlsColumn.addButton("Range: 0:10", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                integerTextField.setText("");
                integerTextField.setMinValue(0);
                integerTextField.setMaxValue(10);
                integerTextField.setMessage("Range: 0:10");
            }
        });

        controlsColumn.addButton("Range: -100:100", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                integerTextField.setText("");
                integerTextField.setMinValue(-100);
                integerTextField.setMaxValue(100);
                integerTextField.setMessage("Range: -100:100");
            }
        });
        
        controlsColumn.addButton("Range: default", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                integerTextField.setText("");
                integerTextField.setMinValue(Integer.MIN_VALUE);
                integerTextField.setMaxValue(Integer.MAX_VALUE);
                integerTextField.setMessage("Range: all integers");
            }
        });

        controlsColumn.addButton("Integer -> 10", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                integerTextField.setValue(10);
            }
        });


        controlsColumn.addButton("getValue()", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int value = integerTextField.getValue();
            ((InteractiveApp) getApplicationInstance()).consoleWrite(Integer.toString(value));
            }
        });

    }


}
