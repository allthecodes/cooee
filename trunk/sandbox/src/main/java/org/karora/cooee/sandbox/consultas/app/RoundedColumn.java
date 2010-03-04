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
import org.karora.cooee.app.Column;

public class RoundedColumn extends Column {

	public static final String PROPERTY_BORDERCOLOR = "borderColor";

	public static final String PROPERTY_BORDERON		= "borderOn";

	private boolean						topRoundedCorner		 = true;

	private boolean						bottomRoundedCorner	= true;

	private boolean						borderOn						 = false;
	
		public RoundedColumn() {
		setProperty(PROPERTY_BORDERCOLOR, Color.BLACK);
	}

	public boolean isBottomRoundedCorner() {
		return bottomRoundedCorner;
	}

	/**
   * Set if the Bottom has to be rounded
   * 
   * @param bottomRoundedCorner
   *          true, false
   */
	public void setBottomRoundedCorner(boolean bottomRoundedCorner) {
		this.bottomRoundedCorner = bottomRoundedCorner;
	}

	public boolean isTopRoundedCorner() {
		return topRoundedCorner;
	}

	/**
   * Set if the Top has to be rounded
   * 
   * @param topRoundedCorner
   *          true, false
   */
	public void setTopRoundedCorner(boolean topRoundedCorner) {
		this.topRoundedCorner = topRoundedCorner;
	}

	public void setBorderColor(Color borderColor) {
		setProperty(PROPERTY_BORDERCOLOR, borderColor);
	}

	public Color getBorderColor() {
		return (Color) getProperty(PROPERTY_BORDERCOLOR);
	}

	public boolean isBorderOn() {
		return borderOn;
	}

	public void setBorderOn(boolean borderOn) {
		this.borderOn = borderOn;
	}

	
}
