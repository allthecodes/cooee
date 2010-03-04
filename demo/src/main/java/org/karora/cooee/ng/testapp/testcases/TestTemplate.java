/* 
 * This file is part of the Echo Point Project.  This project is a collection
 * of Components that have extended the Echo Web Application Framework.
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
package org.karora.cooee.ng.testapp.testcases;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.karora.cooee.app.ApplicationInstance;
import org.karora.cooee.app.Button;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.MutableStyle;
import org.karora.cooee.app.MutableStyleSheet;
import org.karora.cooee.app.SelectField;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.TextField;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.ng.BorderEx;
import org.karora.cooee.ng.ButtonEx;
import org.karora.cooee.ng.DirectHtml;
import org.karora.cooee.ng.ExtentEx;
import org.karora.cooee.ng.LabelEx;
import org.karora.cooee.ng.PushButton;
import org.karora.cooee.ng.TemplatePanel;
import org.karora.cooee.ng.able.Positionable;
import org.karora.cooee.ng.layout.DisplayLayoutData;
import org.karora.cooee.ng.template.FileTemplateDataSource;
import org.karora.cooee.ng.template.JspTemplateDataSource;
import org.karora.cooee.ng.template.ResourceTemplateDataSource;
import org.karora.cooee.ng.template.SimpleTemplateCachingHints;
import org.karora.cooee.ng.template.SimpleTemplateCompilerHints;
import org.karora.cooee.ng.template.SimpleTemplateTextSubstitution;
import org.karora.cooee.ng.template.StringTemplateDataSource;
import org.karora.cooee.ng.util.ColorKit;
import org.karora.cooee.ng.util.RandKit;
import org.karora.cooee.ng.util.reflect.ReflectionKit;

/**
 * <code>TestTemplate</code>
 */

public class TestTemplate extends TestCaseBaseNG implements ActionListener {

	SimpleTemplateTextSubstitution substitution;

	/**
	 * Constructs a <code>TestTemplate</code>
	 */
	public TestTemplate() {
		substitution = new SimpleTemplateTextSubstitution();
		substitution.put("text1", "Lechorous Lothario");
		substitution.put("text2", "Tess Tickler");
	}

	public String getTestCategory() {
		return "TemplatePanel";
	}

	/**
	 * @see org.karora.cooee.app.event.ActionListener#actionPerformed(org.karora.cooee.app.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			String text = "The time is : " + sdf.format(new Date()) + " - click me again!";
			ReflectionKit.invokeIfPresent("setText", new Class[] { String.class }, null, e.getSource(), new Object[] { text });
		}
	}

	private void addNamedComponents(TemplatePanel tp) {
		Component comp;
		Button btn = new Button("Component 1 - Click me to see the time");
		btn.addActionListener(this);
		btn.setBackground(Color.YELLOW);
		btn.setRolloverEnabled(true);
		btn.setRolloverBackground(Color.ORANGE);

		tp.addNamedComponent(btn, "comp1");

		comp = new Label("Included Component 2");
		tp.addNamedComponent(comp, "comp2");

		comp = new Label("Included Component 3");
		tp.addNamedComponent(comp, "comp3");

		comp = new TextField();
		tp.addNamedComponent(comp, "comp4");

		comp = new PushButton("Included Component 5");
		tp.addNamedComponent(comp, "comp5");

		SelectField sf = new SelectField(new String[] { "Alpha", "Beta", "Delta" });
		tp.addNamedComponent(sf, "comp6");
	}

	public Component testDisplayLayoutData() {
		TemplatePanel templatePanel = new TemplatePanel();
		templatePanel.setTemplateTextSubstitution(substitution);

		DisplayLayoutData layoutData = new DisplayLayoutData();
		layoutData.setPosition(Positionable.RELATIVE);
		layoutData.setWidth(new ExtentEx("450px"));
		layoutData.setHeight(new ExtentEx("450px"));

		SplitPane splitPane1 = new SplitPane();
		splitPane1.setResizable(true);
		splitPane1.add(genRandomTextContent());
		splitPane1.add(genRandomTextContent());
		splitPane1.setLayoutData(layoutData);

		templatePanel.addNamedComponent(splitPane1, "comp1");
		templatePanel.addNamedComponent(newLabelAt(10, 600), "comp2");
		templatePanel.addNamedComponent(newLabelAt(20, 650), "comp3");
		templatePanel.addNamedComponent(newLabelAt(30, 700), "comp4");
		templatePanel.addNamedComponent(newLabelAt(40, 750), "comp5");

		String s = "<div style=\"border : thin dotted red; width:500px;height:1000px;\">"
				+ "<p>Here we have a SplitPane with some other components </p>" + "<component name=\"comp1\"/>" + "<component name=\"comp2\"/>"
				+ "<component name=\"comp3\"/>" + "<component name=\"comp4\"/>" + "<component name=\"comp5\"/>" + "</div>";

		StringTemplateDataSource stds = new StringTemplateDataSource(s);
		templatePanel.setTemplateDataSource(stds);
		return templatePanel;
	}
	
	public Component testFlashEmbed() {
		String markup = "<embed src=\"http://www.clocklink.com/clocks/0005-Red.swf?TimeZone=AEST\" width=\"250\" height=\"250\" wmode=\"transparent\" type=\"application/x-shockwave-flash\"/>";
		DirectHtml directHtml = new DirectHtml(markup);
		
		StringTemplateDataSource stds = new StringTemplateDataSource(markup);
		TemplatePanel templatePanel = new TemplatePanel();
		templatePanel.setTemplateDataSource(stds);
		
		Column cell = new Column();
		cell.setCellSpacing(new ExtentEx(10));
		cell.add(directHtml);
		cell.add(templatePanel);
		return cell;
		
	}

	private Label newLabelAt(int left, int top) {
		Label l;
		DisplayLayoutData dld;

		dld = new DisplayLayoutData();
		dld.setPosition(Positionable.ABSOLUTE);
		dld.setLeft(new ExtentEx(left));
		dld.setTop(new ExtentEx(top));

		l = new Label("Positioned at (" + left + "," + top + ")");
		l.setLayoutData(dld);
		l.setBackground(Color.ORANGE);
		return l;
	}

	public Component testFileTemplate() {
		TemplatePanel templatePanel = new TemplatePanel();
		templatePanel.setTemplateTextSubstitution(substitution);
		addNamedComponents(templatePanel);

		templatePanel.setInvokeSetText(true);

		String fileName = "/java/org.karora.cooee.ng/projects/test/src/org.karora.cooee.ng/test/res/test1.html";
		FileTemplateDataSource fileTDS = new FileTemplateDataSource(fileName);
		templatePanel.setTemplateDataSource(fileTDS);
		return templatePanel;
	}

	
	public Component testFileTemplateNoCaching() {
		TemplatePanel templatePanel = new TemplatePanel();
		templatePanel.setTemplateTextSubstitution(substitution);
		addNamedComponents(templatePanel);

		templatePanel.setInvokeSetText(true);
		
		SimpleTemplateCachingHints cachingHints =  null;

		String fileName = "/java/org.karora.cooee.ng/projects/test/src/org.karora.cooee.ng/test/res/test1_no_doctype.html";
		FileTemplateDataSource fileTDS = new FileTemplateDataSource(fileName);
		fileTDS.setCachingHints(cachingHints);
		templatePanel.setTemplateDataSource(fileTDS);
		return templatePanel;
	}

	public Component testFileTemplateWithValidation() {
		final SimpleTemplateCachingHints cachingHints =  null;
		final SimpleTemplateCompilerHints compilerHints = new SimpleTemplateCompilerHints();
		compilerHints.setValidating(true);
		
		final TemplatePanel templatePanel = new TemplatePanel();
		templatePanel.setTemplateTextSubstitution(substitution);
		addNamedComponents(templatePanel);

		templatePanel.setInvokeSetText(true);
		
		//compilerHints.setAttributeValue("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.valueOf(false));


		String fileName = "/java/org.karora.cooee.ng/projects/test/src/org.karora.cooee.ng/test/res/test1.html";
		final FileTemplateDataSource fileTDS = new FileTemplateDataSource(fileName);
		fileTDS.setCachingHints(cachingHints);
		fileTDS.setCompilerHints(compilerHints);
		templatePanel.setTemplateDataSource(fileTDS);

		final SimpleDateFormat sdf = new SimpleDateFormat("'Compiled started around : 'hh:mm:ss");
		final LabelEx compiledLabel = new LabelEx(sdf.format(new Date()));
		final ButtonEx button = new ButtonEx("Set to Non Validating / Dont Fetch DTD");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				compilerHints.setValidating( ! compilerHints.isValidating());
				if (compilerHints.isValidating()) {
					compilerHints.setAttributeValue("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.valueOf(true));
					button.setText("Set to Non Validating / Dont Fetch DTD");
				} else {
					compilerHints.setAttributeValue("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.valueOf(false));
					button.setText("Set to Validating / Fetch DTD");
				}
				// cause a property update
				templatePanel.setTemplateDataSource(null);
				templatePanel.setTemplateDataSource(fileTDS);
				
				compiledLabel.setText(sdf.format(new Date()));
			}
		});
		
		Column cell = new Column();
		cell.add(button);
		cell.add(compiledLabel);
		cell.add(templatePanel);
		return cell;
	}
	
	public Component testJspTemplate() {
		TemplatePanel templatePanel = new TemplatePanel();
		templatePanel.setTemplateTextSubstitution(substitution);
		addNamedComponents(templatePanel);

		JspTemplateDataSource jspTDS = new JspTemplateDataSource("/jsp/test1.jsp");

		Date dt = new Date() {
			/**
			 * @see java.util.Date#toString()
			 */
			public String toString() {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
				return sdf.format(this);
			}
		};
		jspTDS.setAttribute("attr1", "The current date and time is : " + dt);

		templatePanel.setTemplateDataSource(jspTDS);
		return templatePanel;
	}

	public Component testJspTemplateEncoding() {
		Column cell = new Column();
		
		TemplatePanel templatePanel = new TemplatePanel();
		JspTemplateDataSource jspTDS = new JspTemplateDataSource("/jsp/chinese_chars.jsp");
		jspTDS.setCharacterEncoding("GBK");
		templatePanel.setTemplateDataSource(jspTDS);
		cell.add(templatePanel);

		templatePanel = new TemplatePanel();
		jspTDS = new JspTemplateDataSource("/jsp/chinese_chars_with_meta.jsp");
		jspTDS.setCharacterEncoding("GBK");
		templatePanel.setTemplateDataSource(jspTDS);
		cell.add(templatePanel);

		templatePanel = new TemplatePanel();
		jspTDS = new JspTemplateDataSource("/jsp/chinese_chars.jsp");
		jspTDS.setCharacterEncoding(null);
		templatePanel.setTemplateDataSource(jspTDS);
		cell.add(templatePanel);

		templatePanel = new TemplatePanel();
		jspTDS = new JspTemplateDataSource("/jsp/chinese_chars_with_meta.jsp");
		jspTDS.setCharacterEncoding(null);
		templatePanel.setTemplateDataSource(jspTDS);
		cell.add(templatePanel);
		
		return cell;
	}

	public Component testResourceTemplate() {
		TemplatePanel templatePanel = new TemplatePanel();
		templatePanel.setTemplateTextSubstitution(substitution);
		addNamedComponents(templatePanel);

		ResourceTemplateDataSource resTDS = new ResourceTemplateDataSource("org.karora.cooee.ng/test/res/test1.html");
		templatePanel.setTemplateDataSource(resTDS);
		return templatePanel;
	}

	public Component testIframeTemplate() {

		String s = "<iframe src=\"http://www.smh.com.au\"></iframe>";

		StringTemplateDataSource stds = new StringTemplateDataSource(s);
		TemplatePanel templatePanel = new TemplatePanel();
		templatePanel.setTemplateDataSource(stds);
		return templatePanel;
	}

	public Component testComponentReplacement() {
		String s = "<?xml version=\"1.0\"?>" + "<html><body>" + "<div id=\"marker\">" + "<component name=\"content1\" />" + "<br />"
				+ "<component name=\"content2\" />" + "</div>" + "<component name=\"button\"/>" + "</body></html>";

		StringTemplateDataSource source = new StringTemplateDataSource(s);

		final TemplatePanel panel = new TemplatePanel(source);
		panel.setBackground(Color.ORANGE);

		Button button = new ButtonEx("Change Content");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.removeNamedComponent("content1");
				Label label = new Label(String.valueOf(new Date()));
				panel.addNamedComponent(label, "content1");

				panel.removeNamedComponent("content2");
				panel.addNamedComponent(genRandomTextContent(), "content2");

			}
		});
		Button buttonChangeProperties = new ButtonEx("Change Properties");
		buttonChangeProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setBackground((Color) RandKit.roll(new Color[] { Color.BLUE, Color.CYAN, Color.PINK }));
				panel.setBorder((BorderEx) RandKit.roll(new BorderEx[] { new BorderEx(Color.YELLOW), new BorderEx(Color.RED),
						new BorderEx(Color.GREEN), }));
				panel.setInsets(new Insets(RandKit.rand(1, 5)));
				panel.setOutsets(new Insets(RandKit.rand(1, 5)));
			}
		});

		panel.addNamedComponent(new Label("Starting Content1"), "content1");
		panel.addNamedComponent(new Label("Starting Content2"), "content2");
		panel.addNamedComponent(button, "button");

		Column cell = new Column();
		cell.setCellSpacing(new ExtentEx("25em"));
		cell.add(panel);
		cell.add(buttonChangeProperties);
		return cell;
	}

	public Component testNameSpace() {
		TemplatePanel tp = new TemplatePanel();
		tp.setTemplateDataSource(new StringTemplateDataSource("<b>bold and <u>underlined</u> and <i>italic</i> all inside bold text</b>"));
		return tp;
	}
	
	public Component testStyleName() {
		
		MutableStyle myStyle = new MutableStyle();
		myStyle.setProperty(Label.PROPERTY_BACKGROUND,ColorKit.clr("#ff0000"));
		
		MutableStyleSheet styleSheet = new MutableStyleSheet();
		styleSheet.addStyle(Label.class,"myStyleName",myStyle);
		
		ApplicationInstance.getActive().setStyleSheet(styleSheet);

		String s = "<p>Here is a red Label ==> <component id=\"c1\" styleName=\"myStyleName\" /></p>";

		StringTemplateDataSource stds = new StringTemplateDataSource(s);
		TemplatePanel templatePanel = new TemplatePanel();
		templatePanel.setTemplateDataSource(stds);
		templatePanel.addNamedComponent(new Label("A Label"),"c1");
		return templatePanel;
	}
	
	public Component testStrictXhtml() {
		String markup = "<html><body><b>123</b><br/><i>123</i><br/>123</body></html>";
		DirectHtml directHtml = new DirectHtml(markup);

		StringTemplateDataSource stds = new StringTemplateDataSource(markup);
		TemplatePanel templatePanel = new TemplatePanel();
		templatePanel.setTemplateDataSource(stds);

		Column col = new Column();
		col.setCellSpacing(new ExtentEx(10));
		col.add(directHtml);
		col.add(templatePanel);
		
		return col;
		
	}
	
}
