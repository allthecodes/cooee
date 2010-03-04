package org.karora.cooee.sandbox.testapp.testscreen;

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

import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.ContentPane;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.FillImage;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.ResourceImageReference;
import org.karora.cooee.app.Row;
import org.karora.cooee.ng.ExtentEx;
import org.karora.cooee.sandbox.consultas.app.ImageButton;

public class ImageButtonTest extends ContentPane {

	public ImageButtonTest() {

       Column column = new Column();
       add(column);
       
       column.setInsets(new Insets(10));
       column.setCellSpacing(new Extent(5));

	   column.add(createXPButton("Test"));
	   column.add(createVistaButton("Test"));
	   column.add(createBigVistaButton("Test"));
	   
	   Row row = new Row();
	   column.add(row);
	   row.setCellSpacing(new Extent(5));
	   ImageButton button = createXPButton("Ok");
	   button.setWidth(new Extent(100));
	   row.add(button);
	   button = createXPButton("Cancel");
	   button.setEnabled(false);
	   button.setWidth(new Extent(100));
	   row.add(button);

	   row = new Row();
	   column.add(row);
	   row.setCellSpacing(new Extent(5));
	   button = createVistaButton("Ok");
	   button.setWidth(new Extent(100));
	   row.add(button);
	   button = createVistaButton("Cancel");
	   button.setEnabled(false);
	   button.setWidth(new Extent(100));
	   row.add(button);
	}
	
	private ImageButton createVistaButton(String Title) {
		ImageButton button = new ImageButton();
	    button.setStyleName(""); // Don't want Default styling
		button.setText(Title);
		ResourceImageReference buttonImage = new ResourceImageReference(
				"/org/karora/cooee/sandbox/testapp/resource/image/button_vista.gif");

		button.setBackgroundImage(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-54), FillImage.REPEAT_HORIZONTAL));
		button.setBackgroundImageLeft(new FillImage(buttonImage));
		button.setBackgroundImageRight(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-27), FillImage.REPEAT_HORIZONTAL));
		button.setRolloverBackgroundImage(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-135), FillImage.REPEAT_HORIZONTAL));
		button.setRolloverBackgroundImageLeft(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-81), FillImage.REPEAT_HORIZONTAL));
		button.setRolloverBackgroundImageRight(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-108), FillImage.REPEAT_HORIZONTAL));
		button.setPressedBackgroundImage(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-216), FillImage.REPEAT_HORIZONTAL));
		button.setPressedBackgroundImageLeft(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-162), FillImage.REPEAT_HORIZONTAL));
		button.setPressedBackgroundImageRight(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-189), FillImage.REPEAT_HORIZONTAL));
		
		button.setBackgroundImageLeftWidth(new ExtentEx(34));
		button.setBackgroundImageRightWidth(new ExtentEx(34));
		button.setRolloverEnabled(true);
		button.setPressedEnabled(true);
		button.setHeight(new ExtentEx(27));
		button.setWidth(new ExtentEx("100%"));
		button.setForeground(Color.WHITE);
		button.setDisabledForeground(Color.LIGHTGRAY);
		button.setAlignment(new Alignment(Alignment.CENTER, 0));

		return button;
	}

	private ImageButton createXPButton(String Title) {
		ImageButton button = new ImageButton();
	    button.setStyleName(""); // Don't want Default styling

		button.setText(Title);

		ResourceImageReference buttonImage = new ResourceImageReference(
				"/org/karora/cooee/sandbox/testapp/resource/image/button_extjs.gif");
		button.setBackgroundImage(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-42), FillImage.REPEAT_HORIZONTAL));
		button.setBackgroundImageLeft(new FillImage(buttonImage));
		
		button.setBackgroundImageRight(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-21), FillImage.REPEAT_HORIZONTAL));
		button.setRolloverBackgroundImage(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-105), FillImage.REPEAT_HORIZONTAL));
		button.setRolloverBackgroundImageLeft(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-63), FillImage.REPEAT_HORIZONTAL));
		button.setRolloverBackgroundImageRight(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-84), FillImage.REPEAT_HORIZONTAL));
		button.setPressedBackgroundImage(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-126), FillImage.REPEAT_HORIZONTAL));
		button.setPressedBackgroundImageLeft(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-63), FillImage.REPEAT_HORIZONTAL));
		button.setPressedBackgroundImageRight(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-84), FillImage.REPEAT_HORIZONTAL));
		
		
		button.setBackgroundImageLeftWidth(new ExtentEx(3));
		button.setBackgroundImageRightWidth(new ExtentEx(3));
		button.setRolloverEnabled(true);
		button.setPressedEnabled(true);
		
		button.setAlignment(new Alignment(Alignment.CENTER, 0));
		button.setWidth(new ExtentEx("100%"));
		
		button.setHeight(new ExtentEx(21));
		button.setDisabledForeground(Color.LIGHTGRAY);
		button.setForeground(Color.BLACK);

		return button;
	}
	
	private ImageButton createBigVistaButton(String Title) {
		ImageButton button = new ImageButton();
	    button.setStyleName(""); // Don't want Default styling
		button.setText(Title);
		ResourceImageReference buttonImage = new ResourceImageReference(
				"/org/karora/cooee/sandbox/testapp/resource/image/button_vista_big.gif");

		button.setBackgroundImage(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-104), FillImage.REPEAT_HORIZONTAL));
		button.setBackgroundImageLeft(new FillImage(buttonImage));
		button.setBackgroundImageRight(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-52), FillImage.REPEAT_HORIZONTAL));
		button.setRolloverBackgroundImage(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-260), FillImage.REPEAT_HORIZONTAL));
		button.setRolloverBackgroundImageLeft(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-156), FillImage.REPEAT_HORIZONTAL));
		button.setRolloverBackgroundImageRight(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-208), FillImage.REPEAT_HORIZONTAL));
		button.setPressedBackgroundImage(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-416), FillImage.REPEAT_HORIZONTAL));
		button.setPressedBackgroundImageLeft(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-312), FillImage.REPEAT_HORIZONTAL));
		button.setPressedBackgroundImageRight(new FillImage(buttonImage, new ExtentEx(0), new ExtentEx(-364), FillImage.REPEAT_HORIZONTAL));
		
		button.setBackgroundImageLeftWidth(new ExtentEx(34));
		button.setBackgroundImageRightWidth(new ExtentEx(34));
		button.setRolloverEnabled(true);
		button.setPressedEnabled(true);
		button.setHeight(new ExtentEx(52));
		button.setWidth(new ExtentEx("100%"));
		button.setForeground(Color.WHITE);
		button.setDisabledForeground(Color.LIGHTGRAY);
		button.setAlignment(new Alignment(Alignment.CENTER, 0));

		return button;
	}
}
