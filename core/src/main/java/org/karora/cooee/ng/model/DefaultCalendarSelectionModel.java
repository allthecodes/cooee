package org.karora.cooee.ng.model;

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

import java.io.Serializable;
import java.util.Calendar;
import java.util.EventListener;

import org.karora.cooee.app.event.EventListenerList;
import org.karora.cooee.ng.DateChooser;

/**
 * A generic implementation of <code>CalendarSelectionModel</code>.
 * 
 */
public class DefaultCalendarSelectionModel implements CalendarSelectionModel, Serializable {

	private static final long serialVersionUID = 5114495497422592058L;

	private Calendar displayedDate;

	protected EventListenerList listenerList = new EventListenerList();

	private Calendar selectedDate;

	/*
	 * Default behaviour of model now is to always fire the selected event every
	 * time when a date is selected. Turn alwaysFireSelectedEvent to false to
	 * back to previous implementation in which the selection event only come up
	 * when new date is different from current date.  This helps overcome 
	 * a common situation when the user manually blanks the date test field in 
	 * a datefield component.  When they choose a date form the calendar that is 
	 * the same as the one they just blanked out - no selection event is fired.
	 */
	private boolean alwaysFireSelectedEvent = true;

	/*
	 * Copies the provided calendar and hence is another object
	 */
	private Calendar copyCal(Calendar cal) {
		return DateChooser.calendarCopy(cal);
	}

	/**
	 * The creates a CalendarSelectionModel where <b>selectedDate</b> to set to
	 * <code>cal</code> and <b>displayedDate</b> is set to the first day of
	 * the month of <code>cal</code>.
	 * 
	 * @param cal - the Calendar to use.
	 */
	public DefaultCalendarSelectionModel(Calendar cal) {
		super();
		selectedDate = copyCal(cal);
		displayedDate = copyCal(cal);
		displayedDate.set(Calendar.DAY_OF_MONTH, 1);
	}

	/**
	 * Adds a <code>ChangeListener</code> to the model.
	 * 
	 * @param l The <code>ChangeListener</code> to be added.
	 */
	public void addListener(CalendarSelectionListener l) {
		listenerList.addListener(CalendarSelectionListener.class, l);
	}

	private void fireSelectedChanged() {
		EventListener[] listeners = listenerList.getListeners(CalendarSelectionListener.class);
		CalendarEvent calEvent = new CalendarEvent(this, this.selectedDate);
		for (int index = 0; index < listeners.length; ++index) {
			((CalendarSelectionListener) listeners[index]).selectedDateChange(calEvent);
		}
	}

	private void fireDisplayedChanged() {
		EventListener[] listeners = listenerList.getListeners(CalendarSelectionListener.class);
		CalendarEvent calEvent = new CalendarEvent(this, this.displayedDate);
		for (int index = 0; index < listeners.length; ++index) {
			((CalendarSelectionListener) listeners[index]).displayedDateChange(calEvent);
		}
	}

	/**
	 * @see org.karora.cooee.ng.model.CalendarSelectionModel#getDisplayedDate()
	 */
	public Calendar getDisplayedDate() {
		return displayedDate;
	}

	/**
	 * @see org.karora.cooee.ng.model.CalendarSelectionModel#getSelectedDate()
	 */
	public Calendar getSelectedDate() {
		return selectedDate;
	}

	/**
	 * Removes a <code>CalendarSelectionListener</code> from the model.
	 * 
	 * @param l The <code>CalendarSelectionListener</code> to be removed.
	 */
	public void removeListener(CalendarSelectionListener l) {
		listenerList.removeListener(CalendarSelectionListener.class, l);
	}

	/**
	 * @see org.karora.cooee.ng.model.CalendarSelectionModel#setDisplayedDate(java.util.Calendar)
	 */
	public void setDisplayedDate(Calendar cal) {
		// force model to fire up event even same date is selected
		if (alwaysFireSelectedEvent) {
			if (cal != null) {
				displayedDate = copyCal(cal);
				fireDisplayedChanged();
			}
		}
		// Previous behaviour implemetation.
		// cal is compared with current dispalayedDate
		else {
			if (!displayedDate.equals(cal)) {
				displayedDate = copyCal(cal);
				fireDisplayedChanged();
			}
		}
	}

	/**
	 * @see org.karora.cooee.ng.model.CalendarSelectionModel#setSelectedDate(java.util.Calendar)
	 */
	public void setSelectedDate(Calendar cal) {
		// force model to fire up event even same date is selected
		if (alwaysFireSelectedEvent) {
			if (cal != null) {
				selectedDate = copyCal(cal);
				fireSelectedChanged();
			}
		}
		// Previous behaviour implemetation.
		// cal is compared with current selectedDate
		else {
			if ((cal != null && selectedDate == null) || (cal == null && selectedDate != null)
					|| (selectedDate != null && !selectedDate.equals(cal))) {
				selectedDate = copyCal(cal);
				fireSelectedChanged();
			}
		}

	}

	/**
	 * @see org.karora.cooee.ng.model.CalendarSelectionModel#setDates(java.util.Calendar,
	 *      java.util.Calendar)
	 */
	public void setDates(Calendar newSelectedDate, Calendar newDisplayedDate) {
		boolean selectedSet = false;
		boolean displayedSet = false;
		// force model to fire up event even same date is selected
		if (alwaysFireSelectedEvent) {

			if (newSelectedDate != null) {
				selectedDate = copyCal(newSelectedDate);
				selectedSet = true;
			}

			if (newDisplayedDate != null) {
				displayedDate = copyCal(newDisplayedDate);
				displayedSet = true;
			}

		}

		// Previous behaviour implemetation.
		// newSelectedDate is compared with current selectedDate
		// newDisplayedDate is compared with current displayedDate
		else {
			if ((newSelectedDate != null && selectedDate == null) || !selectedDate.equals(newSelectedDate)) {
				selectedDate = copyCal(newSelectedDate);
				selectedSet = true;
			}

			if ((newDisplayedDate != null && displayedDate == null) || !displayedDate.equals(newDisplayedDate)) {
				displayedDate = copyCal(newDisplayedDate);
				displayedSet = true;
			}

		}

		if (selectedSet) {
			fireSelectedChanged();
		}
		if (displayedSet) {
			fireDisplayedChanged();
		}

	}

	public boolean getAlwaysFireSelectedEvent() {
		return alwaysFireSelectedEvent;
	}

	public void setAlwaysFireSelectedEvent(boolean fireSelectedEvent) {
		this.alwaysFireSelectedEvent = fireSelectedEvent;
	}
}
