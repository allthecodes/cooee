package org.karora.cooee.sandbox.testapp.testscreen;


import org.karora.cooee.app.AccordionPane;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.layout.AccordionPaneLayoutData;
import org.karora.cooee.sandbox.informagen.app.NumericTextField;
import org.karora.cooee.sandbox.testapp.ButtonColumn;
import org.karora.cooee.sandbox.testapp.InteractiveApp;


public class NumericTextFieldTest extends ActiveTextFieldTest {
    
    private final NumericTextField numericTextField;
    
    public NumericTextFieldTest() {
        super(new NumericTextField());
        
        numericTextField = (NumericTextField)activeTextField;

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
        layoutData.setTitle("NumericTextField Properties");
        controlsColumn.setLayoutData(layoutData);
        
        controlsColumn.addButton("Range: 0:10", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                numericTextField.setMinValue(0.0);
                numericTextField.setMaxValue(10.0);
                numericTextField.setMessage("Range: 0:10");
            }
        });

        controlsColumn.addButton("Range: -100:100", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                numericTextField.setMinValue(-100.);
                numericTextField.setMaxValue(100.);
                numericTextField.setMessage("Range: -100:100");
            }
        });
        
        controlsColumn.addButton("Range: default", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                numericTextField.setMinValue(Double.MIN_VALUE);
                numericTextField.setMaxValue(Double.MAX_VALUE);
                numericTextField.setMessage("Range: all numbers");
            }
        });

        controlsColumn.addButton("Value -> 10.0", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                numericTextField.setValue(10.0);
            }
        });

        controlsColumn.addButton("Value -> 10.00000001", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                numericTextField.setValue(10.00000001);
            }
        });


        controlsColumn.addButton("getValue()", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double value = numericTextField.getValue();
            ((InteractiveApp) getApplicationInstance()).consoleWrite(Double.toString(value));
            }
        });


    }


}
