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

import java.util.Collections;

import org.eclipse.bpel.model.PartnerLink;
import org.eclipse.bpel.model.partnerlinktype.PartnerLinkType;
import org.eclipse.bpel.ui.IBPELUIConstants;
import org.eclipse.bpel.ui.util.BPELUtil;
import org.eclipse.wst.wsdl.Definition;

/**
 * Creates a new PartnerLinkType in a WSDL file.
 */
public class CreatePartnerLinkTypeCommand extends CreateWSDLExtensibilityElementCommand {

	protected PartnerLink partner;
	
	// TODO: label wrong?
	public String getDefaultLabel() { return IBPELUIConstants.CMD_ADD_PARTNERLINK; }

	public CreatePartnerLinkTypeCommand(Definition definition, PartnerLinkType plt, PartnerLink partner) {
		super(definition, plt);
		this.partner = partner;
	}

	public void doExecute() {
		getPartnerLinkType().setName(BPELUtil.getUniqueModelName(definition, partner.getName() + "PLT", Collections.singletonList(getPartnerLinkType()))); //$NON-NLS-1$
		super.doExecute();
	}
	
	protected PartnerLinkType getPartnerLinkType() {
		return (PartnerLinkType)element;
	}
}
