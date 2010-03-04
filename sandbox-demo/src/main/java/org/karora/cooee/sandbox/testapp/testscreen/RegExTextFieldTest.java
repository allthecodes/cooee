package org.karora.cooee.sandbox.testapp.testscreen;


import org.karora.cooee.app.Label;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.sandbox.informagen.app.RegExTextField;
import org.karora.cooee.sandbox.testapp.ButtonColumn;

import org.karora.cooee.app.layout.SplitPaneLayoutData;
import org.karora.cooee.app.layout.AccordionPaneLayoutData;


/**
 * An interactive test for <code>RegExTextFieldTest</code>s.
 */
 
public class RegExTextFieldTest extends ActiveTextFieldTest {


    final RegExTextField regexTextField;    

    public RegExTextFieldTest() {
        super(new RegExTextField());
        
        regexTextField = (RegExTextField)activeTextField;

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
        layoutData.setTitle("RegExTextField Properties");
        controlsColumn.setLayoutData(layoutData);

        controlsColumn.addButton("RegEx: +/- 6 digits", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String regEx = "^[+-]{0,1}[0-9]{1,6}$";
                String regExFilter = "[0-9+-]";
                regexTextField.setText("");
                regexTextField.setRegEx(regEx);
                regexTextField.setRegExFilter(regExFilter);
                regexTextField.setMessage("Valid entry is +/- six digits");
                regexTextField.setToolTipText(null);
            }
        });

        controlsColumn.addButton("RegEx: E-mail Address", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                regexTextField.setText("");
                regexTextField.setRegEx("^[_a-z0-9-]+(\\.?[\\+_a-z0-9-]+)*@[a-z0-9-]*(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$");
                regexTextField.setRegExFilter("[\\._a-z0-9@-]");
                regexTextField.setMessage("Incomplete or invalid e-mail address");
                regexTextField.setToolTipText(null);
            }
        });

        controlsColumn.addButton("RegEx: XML Tag", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                regexTextField.setText("");
                regexTextField.setRegEx("^[:_a-zA-Z]+[_a-zA-Z0-9\\.-]*$");
                regexTextField.setRegExFilter("[:_a-zA-Z0-9\\.-]");
                regexTextField.setMessage("Invalid XML tag");
                regexTextField.setToolTipText(null);
            }
        });

        controlsColumn.addButton("RegEx: Social Security Number", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                regexTextField.setText("");
                regexTextField.setRegEx("^[0-9]{3}\\-[0-9]{2}\\-[0-9]{4}$");
                regexTextField.setRegExFilter("[0-9-]");
                regexTextField.setMessage("Invalid SSN");
                regexTextField.setToolTipText("Social Securiry Number");
            }
        });

        controlsColumn.addButton("RegEx: Contains 'xxx'", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                regexTextField.setText("");
                regexTextField.setRegEx("xxx");
                regexTextField.setRegExFilter(null);
                regexTextField.setMessage("Must contain the string 'xxx'");
                regexTextField.setToolTipText("Contains test");
            }
        });

        controlsColumn.addButton("RegExFilter: All digits, [0-9]", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String regExFilter = "[0-9]";
                regexTextField.setText("");
                regexTextField.setRegExFilter(regExFilter);
            }
        });

        controlsColumn.addButton("RegExFilter: Even digits, [02468]", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String regExFilter = "[02468]";
                regexTextField.setText("");
                regexTextField.setRegExFilter(regExFilter);
            }
        });

        controlsColumn.addButton("RegExFilter: Odd digits, [13579]", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String regExFilter = "[13579]";
                regexTextField.setText("");
                regexTextField.setRegExFilter(regExFilter);
            }
        });


        controlsColumn.addButton("RegExFilter: [a-k0-3-]", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String regExFilter = "[a-k0-3-]";
                regexTextField.setText("");
                regexTextField.setRegExFilter(regExFilter);
            }
        });


       
    }
}
