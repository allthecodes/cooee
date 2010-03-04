/* 
 * This file is part of the Echo Web Application Framework (hereinafter "Echo").
 * Copyright (C) 2002-2005 NextApp, Inc.
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

package org.karora.cooee.sandbox.testapp.testscreen;

import java.io.IOException;
import java.io.InputStream;

import org.karora.cooee.app.Color;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.SplitPane;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.sandbox.filetransfer.app.UploadEvent;
import org.karora.cooee.sandbox.filetransfer.app.UploadListener;
import org.karora.cooee.sandbox.filetransfer.app.UploadSelect;
import org.karora.cooee.sandbox.testapp.ButtonColumn;
import org.karora.cooee.sandbox.testapp.InteractiveApp;
import org.karora.cooee.sandbox.testapp.StyleUtil;

/**
 * Interactive test module for <code>ContentPane</code>s.
 */
public class FileUploadTest extends SplitPane {

	public FileUploadTest() {
		super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));
		setStyleName("DefaultResizable");

		ButtonColumn controlsColumn = new ButtonColumn();
		controlsColumn.setStyleName("TestControlsColumn");
		add(controlsColumn);

		final UploadSelect uploadSelect = new UploadSelect();
		uploadSelect.setUploadListener(new UploadListener() {

			public void invalidFileUpload(UploadEvent e) {
				InteractiveApp app = InteractiveApp.getApp();
				app.consoleWrite("-----------------------------");
				app.consoleWrite("Invalid File Upload");
			}

			public void fileUpload(UploadEvent e) {
				InteractiveApp app = InteractiveApp.getApp();
				app.consoleWrite("-----------------------------");
				app.consoleWrite("File Upload");
				app.consoleWrite("ContentType = " + e.getContentType());
				app.consoleWrite("FileName = " + e.getFileName());
				app.consoleWrite("Size = " + e.getSize());

				int totalBytesRead = 1; // offset -1 returned when no more bytes
										// available.
				InputStream in = e.getInputStream();
				try {
					byte[] data = new byte[16];
					int bytesRead = in.read(data);
					totalBytesRead += bytesRead;
					while (bytesRead != -1) {
						if (totalBytesRead < 1024) {
							StringBuffer out = new StringBuffer();
							for (int i = 0; i < bytesRead; ++i) {
								int value = data[i] & 0xff;
								if (value < 0x10) {
									out.append("0");
								}
								out.append(Integer.toString(value, 16));
								out.append(' ');
							}
							for (int i = bytesRead; i < 16; ++i) {
								out.append("   ");
							}
							out.append(" | ");
							for (int i = 0; i < bytesRead; ++i) {
								if (data[i] >= 32 && data[i] <= 126) {
									out.append((char) data[i]);
								} else {
									out.append(' ');
								}
							}
							app.consoleWrite(out.toString());
						}
						bytesRead = in.read(data);
						totalBytesRead += bytesRead;
					}
				} catch (IOException ex) {
					app.consoleWrite(ex.toString());
				} finally {
					try {
						in.close();
					} catch (IOException ex) {
						app.consoleWrite(ex.toString());
					}
				}
				app.consoleWrite("InputStream Bytes Read = " + totalBytesRead);
			}
		});
		add(uploadSelect);

		controlsColumn.addButton("Set Foreground", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = StyleUtil.randomColor();
				uploadSelect.setForeground(color);
			}
		});

		controlsColumn.addButton("Set Background", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = StyleUtil.randomColor();
				uploadSelect.setBackground(color);
			}
		});

		controlsColumn.addButton("Set Width = Default", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uploadSelect.setWidth(null);
			}
		});

		controlsColumn.addButton("Set Width = 500px", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uploadSelect.setWidth(new Extent(500));
			}
		});

		controlsColumn.addButton("Set Width = 100%", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uploadSelect.setWidth(new Extent(100, Extent.PERCENT));
			}
		});
	}
}
