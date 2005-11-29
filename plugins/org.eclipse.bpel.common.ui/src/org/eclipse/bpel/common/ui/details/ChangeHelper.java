/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.common.ui.details;

import org.eclipse.bpel.common.ui.command.ICommandFramework;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * An abstract IOngoingChange that is also a Listener.  ChangeHelper notifies the
 * details area of IOngoingChange lifecycle events on behalf of the widget(s) it
 * listens to.
 */
public abstract class ChangeHelper implements IOngoingChange, Listener {

	protected ICommandFramework commandFramework;
	protected boolean nonUserChange;
	
	/**
	 * Marks the start of a programmatic change to the widget contents.  Clients must
	 * call startNonUserChange() before directly setting the widget contents to avoid
	 * unwanted IOngoingChange lifecycle events.  (Only some widgets/viewers will send the
	 * unwanted notifications, but all clients should use this pattern anyway).
	 * Currently, changes may not be nested.
	 * 
	 * @throws IllegalArgumentException if a programmatic change is already in progress.
	 */
	public void startNonUserChange()  {
		if (nonUserChange)  throw new IllegalStateException();
		nonUserChange = true;
	}
	
	/**
	 * Clients who call startNonUserChange() should call finishNonUserChange() as soon
	 * as possible after the change is done.
	 * 
	 * @throws IllegalArgumentException if no change is in progress.
	 */
	public void finishNonUserChange()  {
		if (!nonUserChange)  throw new IllegalStateException();
		nonUserChange = false;
	}

	/**
	 * Returns true if a programmatic change is in progress.
	 */
	public boolean isNonUserChange() {
		return nonUserChange;
	}
	
	public ChangeHelper(ICommandFramework commandFramework) {
		this.commandFramework = commandFramework;
	}
	
	public void handleEvent(Event event) {
		switch (event.type) {
		case SWT.KeyDown:
			if (event.character == SWT.CR) finish();
			break;
		case SWT.FocusOut:
			finish(); break;
		case SWT.Modify:
		case SWT.Selection:
		case SWT.DefaultSelection:
			modify(); break;
		}
	}
	public void finish() {
		commandFramework.notifyChangeDone(this);
	}
	public void modify() {
		if (!isNonUserChange()) {
			commandFramework.notifyChangeInProgress(this);
		}
	}
	
	protected boolean isModifyBasedControl(Control c) {
		if (c instanceof CCombo) {
			return (c.getStyle() & SWT.READ_ONLY) == 0; // if not read only
		}
		return (c instanceof Text);
	}

	protected boolean isSelectionBasedControl(Control c) {
		return !(c instanceof Text);
	}

	/**
	 * Registers this ChangeHelper with the given control to listen for events
	 * which indicate that a change is in progress (or done).
	 */
	public void startListeningTo(Control control) {
		control.addListener(SWT.FocusOut, this);
		if (isModifyBasedControl(control)) {
			control.addListener(SWT.Modify, this);
		}
		if (isSelectionBasedControl(control)) {
			control.addListener(SWT.Selection, this);
			control.addListener(SWT.DefaultSelection, this);
		}
	}
	
	/**
	 * Registers this ChangeHelper with the given control to listen for the
	 * Enter key.  When Enter is pressed, the change is considered done (this
	 * is appropriate for single-line Text widgets).
	 */
	public void startListeningForEnter(Control control) {
		// NOTE: KeyDown rather than KeyUp, because of similar usage in CCombo. 
		control.addListener(SWT.KeyDown, this);
	}
	
	/**
	 * Unregisters this ChangeHelper from a control previously passed to
	 * startListeningTo() and/or startListeningForEnter().
	 */
	public void stopListeningTo(Control control) {
		control.removeListener(SWT.FocusOut, this);
		if (isModifyBasedControl(control)) {
			control.removeListener(SWT.Modify, this);
		}
		if (isSelectionBasedControl(control)) {
			control.removeListener(SWT.Selection, this);
			control.removeListener(SWT.DefaultSelection, this);
		}
		control.removeListener(SWT.KeyDown, this);
	}
}