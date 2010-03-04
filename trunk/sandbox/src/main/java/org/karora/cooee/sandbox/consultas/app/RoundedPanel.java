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

package org.karora.cooee.sandbox.consultas.app;

import org.karora.cooee.app.Color;
import org.karora.cooee.app.Font;

public class RoundedPanel extends RoundedColumn {

	private String title = "";

	private Font titleFont;

	private Color titleForeground;

	public RoundedPanel() {
		super();
		setBorderOn(true);
		setBorderColor(Color.BLACK);
		setBackground(Color.WHITE);
	}

	public RoundedPanel(String title) {
		this();
		setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Font getTitleFont() {
		return titleFont;
	}

	public void setTitleFont(Font titleFont) {
		this.titleFont = titleFont;
	}

	public Color getTitleForeground() {
		return titleForeground;
	}

	public void setTitleForeground(Color titleForeground) {
		this.titleForeground = titleForeground;
	}

}
