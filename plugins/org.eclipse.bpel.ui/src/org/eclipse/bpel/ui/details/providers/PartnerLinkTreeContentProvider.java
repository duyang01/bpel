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
package org.eclipse.bpel.ui.details.providers;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.bpel.model.PartnerLink;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.ui.details.tree.PartnerLinkTreeNode;


/**
 * Provides a tree of model objects representing some expansion of the underlying graph
 * of model objects whose roots are the PartnerLinks of a Process. 
 */
public class PartnerLinkTreeContentProvider extends ModelTreeContentProvider {

	public PartnerLinkTreeContentProvider(boolean isCondensed) {
		super(isCondensed);
	}

	public Object[] primGetElements(Object inputElement) {
		Vector v = new Vector();
		if (inputElement instanceof Process) {
			for (Iterator it = ((Process)inputElement).getPartnerLinks().getChildren().iterator(); it.hasNext(); ) {
				v.add(new PartnerLinkTreeNode((PartnerLink)it.next(), isCondensed));
			}
		}
		return v.toArray();
	}
}
