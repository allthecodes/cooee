package org.karora.cooee.ng.testapp;

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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.karora.cooee.ng.testapp.testcases.TestButton;
import org.karora.cooee.ng.testapp.testcases.TestCalculator;
import org.karora.cooee.ng.testapp.testcases.TestCaseBaseNG;
import org.karora.cooee.ng.testapp.testcases.TestColorChooser;
import org.karora.cooee.ng.testapp.testcases.TestCommands;
import org.karora.cooee.ng.testapp.testcases.TestContainerEx;
import org.karora.cooee.ng.testapp.testcases.TestContentPaneEx;
import org.karora.cooee.ng.testapp.testcases.TestDateChooser;
import org.karora.cooee.ng.testapp.testcases.TestDateField;
import org.karora.cooee.ng.testapp.testcases.TestDirectHtml;
import org.karora.cooee.ng.testapp.testcases.TestExpandableSection;
import org.karora.cooee.ng.testapp.testcases.TestHistory;
import org.karora.cooee.ng.testapp.testcases.TestLabel;
import org.karora.cooee.ng.testapp.testcases.TestLiveTable;
import org.karora.cooee.ng.testapp.testcases.TestMenu;
import org.karora.cooee.ng.testapp.testcases.TestNativeWindow;
import org.karora.cooee.ng.testapp.testcases.TestPopUp;
import org.karora.cooee.ng.testapp.testcases.TestProgressBar;
import org.karora.cooee.ng.testapp.testcases.TestRichTextArea;
import org.karora.cooee.ng.testapp.testcases.TestSlider;
import org.karora.cooee.ng.testapp.testcases.TestStackedPaneEx;
import org.karora.cooee.ng.testapp.testcases.TestStyleSheet;
import org.karora.cooee.ng.testapp.testcases.TestTabbedPane;
import org.karora.cooee.ng.testapp.testcases.TestTable;
import org.karora.cooee.ng.testapp.testcases.TestTemplate;
import org.karora.cooee.ng.testapp.testcases.TestTextFieldEx;
import org.karora.cooee.ng.testapp.testcases.TestTree;
import org.karora.cooee.ng.testapp.testcases.TestXtra;

import org.karora.cooee.app.ApplicationInstance;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Component;
import org.karora.cooee.app.ContentPane;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Grid;
import org.karora.cooee.app.HttpImageReference;
import org.karora.cooee.app.ImageReference;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.Window;
import org.karora.cooee.app.WindowPane;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.layout.SplitPaneLayoutData;
import org.karora.cooee.webcontainer.ContainerContext;
import org.karora.cooee.webrender.WebRenderServlet;
import org.karora.cooee.ng.BorderEx;
import org.karora.cooee.ng.ButtonEx;
import org.karora.cooee.ng.ContainerEx;
import org.karora.cooee.ng.ContentPaneEx;
import org.karora.cooee.ng.DirectHtml;
import org.karora.cooee.ng.ExpandableSection;
import org.karora.cooee.ng.ExtentEx;
import org.karora.cooee.ng.GroupBox;
import org.karora.cooee.ng.HistoryMonitor;
import org.karora.cooee.ng.ImageIcon;
import org.karora.cooee.ng.LabelEx;
import org.karora.cooee.ng.Tree;
import org.karora.cooee.ng.history.HistoryState;
import org.karora.cooee.ng.model.ExpansionGroup;
import org.karora.cooee.ng.tree.DefaultMutableTreeNode;
import org.karora.cooee.ng.tree.EmptyTreeSelectionModel;
import org.karora.cooee.ng.util.ColorKit;
import org.karora.cooee.ng.util.reflect.ReflectionKit;

/**
 * <code>TestInstance</code>
 */
public class TestInstanceNG extends ApplicationInstance implements ActionListener {

	private List testCaseList = new ArrayList();

	private Map invokerMap = new HashMap();

	private ContentPane containingPane;

	private ContentPane testContentArea;
	
	private ContentPane testTestingArea;

	private Component currentTestComponent;

	private HistoryMonitor historyMonitor;

	private ImageReference smallFireWorks = new HttpImageReference("images/t_firework.jpg", new Extent(10), new Extent(10));

	private ImageReference smallAstronaut = new HttpImageReference("images/t_astronaut.jpg", new Extent(10), new Extent(10));

	public TestInstanceNG() {
		addTest(new TestButton());

		addTest(new TestCalculator());
		addTest(new TestColorChooser());
		addTest(new TestContainerEx());
		addTest(new TestContentPaneEx());
		addTest(new TestCommands());

		addTest(new TestDateChooser());
		addTest(new TestDateField());
		addTest(new TestDirectHtml());

		addTest(new TestExpandableSection());
		
		addTest(new TestHistory());

		addTest(new TestLabel());
		addTest(new TestLiveTable());

		addTest(new TestMenu());

		addTest(new TestNativeWindow());

		addTest(new TestProgressBar());
		addTest(new TestPopUp());

		addTest(new TestRichTextArea());

		addTest(new TestSlider());
		addTest(new TestStackedPaneEx());
		addTest(new TestStyleSheet());

		addTest(new TestTable());
		addTest(new TestTabbedPane());
		addTest(new TestTemplate());
		addTest(new TestTextFieldEx());
		addTest(new TestTree());

		addTest(new TestXtra());
	}

	/** allows test cases to be added to all instances */
	public void addTest(TestCaseBaseNG testCase) {
		testCaseList.add(testCase);
	}

	/**
	 * @see org.karora.cooee.app.ApplicationInstance#init()
	 */
	public Window init() {
		Window w = new Window();
		w.setTitle("EchoPointNG Testing Harness - running on " + ID_STRING);

		containingPane = new ContentPane();
		recreatePane(containingPane);
		w.setContent(containingPane);
		return w;
	}

	private void recreatePane(final ContentPane pane) {
		pane.removeAll();

		/*
		 *   tree           content area
		 *     |                   |
		 *     v                   v
		 * |-------------|----------------------|
		 * |             |                      |
		 * |             |                      |
		 * |             |                      |
		 * |             |                      |
		 * |             |                      |
		 * |-------------|----------------------|
		 * 
		 */
		SplitPane splitPaneHorz = new SplitPane(SplitPane.ORIENTATION_HORIZONTAL, new Extent(300));
		splitPaneHorz.setResizable(true);
		splitPaneHorz.setSeparatorWidth(new Extent(3));

		SplitPaneLayoutData splitPaneLayoutData = new SplitPaneLayoutData();
		splitPaneLayoutData.setInsets(new Insets(15));

		testContentArea = new ContentPaneEx();
		testContentArea.setLayoutData(splitPaneLayoutData);

		splitPaneLayoutData = new SplitPaneLayoutData();
		splitPaneLayoutData.setInsets(new Insets(15));

		ButtonEx recreateButton = new ButtonEx("*Recreate");
		recreateButton.setOutsets(new Insets(0, 20, 0, 0));
		recreateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recreatePane(pane);
			}
		});

		historyMonitor = new HistoryMonitor();

		Component testCases;

		ApplicationInstance app = ApplicationInstance.getActive();
		ContainerContext containerContext = (ContainerContext) app.getContextProperty(ContainerContext.CONTEXT_PROPERTY_NAME);
		Map initialParameters = containerContext.getInitialRequestParameterMap();
		String initialView[] = (String[]) initialParameters.get("view");
		if (initialView != null && initialView.length > 0 && "tree".equals(initialView[0])) {
			testCases = buildTestCaseNamesAsTree();
		} else {
			testCases = buildTestCaseNamesAsButtons();
		}

		Column leftSide = new Column();
		leftSide.add(createLeftServerInteractionComponents());
		leftSide.add(testCases);
		leftSide.add(recreateButton);
		leftSide.add(historyMonitor);
		leftSide.setLayoutData(splitPaneLayoutData);

		splitPaneHorz.add(leftSide);
		splitPaneHorz.add(testContentArea);

		
		testTestingArea = new ContentPaneEx();

		/*
		 * ------------------------------------
		 * |                                  |
		 * |                                  |  <-- general split pane content
		 * |                                  |
		 * |                                  |
		 * ------------------------------------
		 * |                                  | <<-- testing area
		 * |                                  |
		 * ------------------------------------
		 */
		SplitPane splitPaneVertical = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, new Extent(100));
		splitPaneVertical.setResizable(true);
		splitPaneVertical.setSeparatorHeight(new Extent(3));
		
		splitPaneVertical.add(testTestingArea);
		splitPaneVertical.add(splitPaneHorz);
		

		pane.add(splitPaneVertical);

		// ok do we have any history on the URL and can we jump right there
		String currentQueryString = String.valueOf(WebRenderServlet.getActiveConnection().getRequest().getSession()
				.getAttribute("currentQueryString"));
		int fragmentIndex = currentQueryString.indexOf('#');
		if (fragmentIndex != -1) {
			String fragment = currentQueryString.substring(fragmentIndex + 1);
			int commandIndex = fragment.indexOf('-');
			if (commandIndex != -1) {
				String command = fragment.substring(0, commandIndex - 1);
				invokeCommand(command);
			}
		}
	}

	/**
	 * <code>TestCaseInvoker</code> is a simple class to keep track of a test
	 * function, name and a method
	 */
	private class TestCaseInvoker {
		TestCaseBaseNG testCase;

		Method method;

		private TestCaseInvoker(TestCaseBaseNG testCase, Method method) {
			this.testCase = testCase;
			this.method = method;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return method.getName();
		}

		public String getFullName() {
			StringBuffer sb = new StringBuffer(testCase.getTestCategory());
			sb.append("-");
			sb.append(method.getName());
			return sb.toString();
		}
	}

	public void actionPerformed(ActionEvent e) {
		final String actionCommand = e.getActionCommand();

		historyMonitor.addHistory(new HistoryState() {
			/**
			 * @see org.karora.cooee.ng.history.HistoryState#historyHash()
			 */
			public String historyHash() {
				return actionCommand + "-" + System.identityHashCode(new Object());
			}
		});
		invokeCommand(actionCommand);
	}

	private void invokeCommand(String actionCommand) {
		TestCaseInvoker invoker = (TestCaseInvoker) invokerMap.get(actionCommand);
		if (invoker != null) {
			Component testComponent = null;
			Throwable causeException = null;
			testTestingArea.removeAll();
			try {
				
				invoker.testCase.setContainingPane(this.containingPane);
				invoker.testCase.setTestingAreaPane(this.testTestingArea);
				
				invoker.testCase.setUp();
				testComponent = (Component) invoker.method.invoke(invoker.testCase, new Object[] {});
				if (testComponent == null) {
					testComponent = new LabelEx(invoker.method.getName() + " returned null.  Has it been implemented??");
				} else {
					invoker.testCase.tearDown();
				}
			} catch (IllegalAccessException e1) {
				causeException = e1;
			} catch (InvocationTargetException e2) {
				causeException = e2;
				// special case we let through!
				if (causeException.getCause() instanceof UnknownError) {
					throw new RuntimeException(causeException.getCause());
				}
			}
			if (causeException != null) {
				testComponent = buildExceptionReport(causeException);
			}
			// clean up previous test
			if (currentTestComponent != null && currentTestComponent.getParent() != null) {
				currentTestComponent.getParent().remove(currentTestComponent);
			}
			testContentArea.removeAll();

			// set the new test in
			try {
				if (testComponent instanceof WindowPane) {
					containingPane.add(testComponent);
				} else {
					testContentArea.add(testComponent);
				}
			} catch (Exception addException) {
				testComponent = buildExceptionReport(addException);
				testContentArea.removeAll();
				testContentArea.add(testComponent);
			}
			currentTestComponent = testComponent;
		}
	}

	private Component buildExceptionReport(Throwable e) {
		String xhtml = "<div style=\"font-size:8pt;border:1px dashed red;margin-top:5px;\">";
		xhtml += "<span style=\"color:red;font-weight:bold\">";
		xhtml += e.getMessage();
		xhtml += "</span>";
		// print stack trace
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);

		String stackTrace = sw.toString();

		if (stackTrace.length() > 0) {
			xhtml += "<pre>";
			xhtml += stackTrace;
			xhtml += "</pre>";
		}
		xhtml += "</div>";
		DirectHtml exceptionArea = new DirectHtml();
		exceptionArea.setText(xhtml);
		return new ContainerEx(exceptionArea);
	}

	private Component buildTestCaseNamesAsTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("All Test Cases");
		invokerMap = new HashMap();

		Collections.sort(testCaseList, new Comparator() {
			public int compare(Object o1, Object o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});

		for (int i = 0; i < testCaseList.size(); i++) {
			TestCaseBaseNG testCase = (TestCaseBaseNG) testCaseList.get(i);

			DefaultMutableTreeNode catNode = new DefaultMutableTreeNode(testCase.getTestCategory());
			root.add(catNode);

			Method[] testMethods = getTestMethods(testCase);
			Arrays.sort(testMethods, new Comparator() {
				public int compare(Object o1, Object o2) {
					return ((Method) o1).getName().compareTo(((Method) o2).getName());
				}
			});

			for (int j = 0; j < testMethods.length; j++) {
				TestCaseInvoker invoker = new TestCaseInvoker(testCase, testMethods[j]);

				String key = invoker.getFullName();
				invokerMap.put(key, invoker);
				DefaultMutableTreeNode testNode = new DefaultMutableTreeNode(invoker, false);
				testNode.setActionCommand(key);
				catNode.add(testNode);
			}
		}

		Tree testCaseTree = new Tree(root);
		testCaseTree.setSelectionModel(EmptyTreeSelectionModel.getInstance());
		testCaseTree.addActionListener(this);
		return testCaseTree;
	}

	private Component buildTestCaseNamesAsButtons() {
		Grid g = new Grid(1);
		g.setWidth(new ExtentEx("100%"));
		invokerMap = new HashMap();

		Collections.sort(testCaseList, new Comparator() {
			public int compare(Object o1, Object o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});

		ExpansionGroup expansionGroup = new ExpansionGroup();
		expansionGroup.setAccordionMode(true);
		for (int i = 0; i < testCaseList.size(); i++) {
			TestCaseBaseNG testCase = (TestCaseBaseNG) testCaseList.get(i);

			ExpandableSection es = new ExpandableSection(testCase.getTestCategory());
			es.setExpansionGroup(expansionGroup);
			es.setBorder(new BorderEx(1));
			es.setOutsets(new Insets(0, 0, 0, 2));
			g.add(es);

			Grid esContent = new Grid(1);
			esContent.setHeight(new ExtentEx("120px"));
			esContent.setRowHeight(0, new ExtentEx("2em"));
			// esContent.setBackground(ColorKit.makeColor(0xACBCDC));
			es.add(esContent);

			Method[] testMethods = getTestMethods(testCase);
			Arrays.sort(testMethods, new Comparator() {
				public int compare(Object o1, Object o2) {
					return ((Method) o1).getName().compareTo(((Method) o2).getName());
				}
			});

			for (int j = 0; j < testMethods.length; j++) {
				TestCaseInvoker invoker = new TestCaseInvoker(testCase, testMethods[j]);

				String key = invoker.getFullName();
				invokerMap.put(key, invoker);
				ButtonEx button = new ButtonEx(invoker.toString());
				button.setActionCommand(key);
				button.addActionListener(this);
				button.setOutsets(new Insets(20, 2, 0, 2));
				esContent.add(button);
				// es.add(button);
			}
		}
		return g;
	}

	private Component createLeftServerInteractionComponents() {
		final Column container = new Column();
		final ImageIcon icon = new ImageIcon(smallFireWorks, new Extent(25), new Extent(25));

		final ButtonEx serverInteraction = new ButtonEx("ServerInteraction");
		serverInteraction.setBackground(ColorKit.makeColor(0xACBCDC));

		serverInteraction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (icon.getIcon() == smallFireWorks) {
					icon.setIcon(smallAstronaut);
				} else {
					icon.setIcon(smallFireWorks);
				}

			}
		});

		container.add(icon);
		container.add(serverInteraction);
		container.setCellSpacing(new ExtentEx("7px"));

		GroupBox gb = new GroupBox();
		gb.setInsets(new Insets(0, 0, 0, 0));
		gb.setOutsets(new Insets(0, 0, 0, 20));
		gb.add(container);
		return gb;
	}

	private Method[] getTestMethods(TestCaseBaseNG testCase) {
		return ReflectionKit.getMethods(testCase.getClass(), TestCaseBaseNG.class, new ReflectionKit.MethodSearchCriteria() {
			public boolean isMethodOK(Class methodClass, Method method) {
				if (method.getName().startsWith("test")) {
					int mods = method.getModifiers();
					if (Modifier.isPublic(mods) && !Modifier.isStatic(mods)) {
						if (Component.class.isAssignableFrom(method.getReturnType())) {
							if (method.getParameterTypes().length == 0) {
								return true;
							}
						}
					}
				}
				return false;
			}
		});
	}

}
