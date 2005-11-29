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
package org.eclipse.bpel.ui.commands;

import java.util.List;

import org.eclipse.bpel.model.CorrelationSet;
import org.eclipse.bpel.model.messageproperties.Property;
import org.eclipse.bpel.ui.IBPELUIConstants;


public class ReplacePropertyCommand extends ReplaceInListCommand {

	public ReplacePropertyCommand(CorrelationSet target, Property oldProperty,
		Property newProperty)
	{
		super(target, oldProperty, newProperty, IBPELUIConstants.CMD_EDIT_PROPERTY);
	}

	protected List getList() {
		return ((CorrelationSet)target).getProperties();
	}
}
