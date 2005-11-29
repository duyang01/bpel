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
package org.eclipse.bpel.ui.adapters;

import java.util.List;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Sequence;
import org.eclipse.bpel.ui.BPELEditor;
import org.eclipse.bpel.ui.BPELUIPlugin;
import org.eclipse.bpel.ui.IBPELUIConstants;
import org.eclipse.bpel.ui.Messages;
import org.eclipse.bpel.ui.adapters.delegates.ImplicitSequenceContainer;
import org.eclipse.bpel.ui.editparts.CaseEditPart;
import org.eclipse.bpel.ui.editparts.OutlineTreeEditPart;
import org.eclipse.bpel.ui.uiextensionmodel.UiextensionmodelFactory;
import org.eclipse.bpel.ui.util.BPELUtil;
import org.eclipse.bpel.ui.util.ModelHelper;
import org.eclipse.core.resources.IMarker;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.swt.graphics.Image;


public class CaseAdapter extends ContainerAdapter implements ILabeledElement,
	EditPartFactory, IOutlineEditPartFactory, IMarkerHolder, IExtensionFactory
{

	/* IContainer delegate */
	
	public IContainer createContainerDelegate() {
		// TODO: SwitchAdapter ensures the Case always has an activity inside it,
		// by creating an ImplicitSequence if the Case was empty.  We then make
		// sure we don't delete the last activity from the Case.
		// See also SwitchAdapter. Rethink this in the future.
		return new ImplicitSequenceContainer(BPELPackage.eINSTANCE.getCase_Activity()) {
			public boolean removeChild(Object object, Object child) {
				Object currentChild = getSingleChild(object);
				if (currentChild == null)  return false;
				if (!isImplicitSequence(currentChild)) {
					// TODO: should we check that child is actually the correct object??
					if (ModelHelper.isSpecCompliant((EObject)object)) {
						// spec-compliant mode: just remove it.
						setSingleChild(object, null);
						return true;
					} else {
						// we have a child, but not an implicit sequence.  just replace it
						// with a new (empty) implicit sequence.
						BPELEditor bpelEditor = ModelHelper.getBPELEditor(object);
						Sequence impSeq = BPELUtil.createImplicitSequence(
							bpelEditor.getProcess(), bpelEditor.getExtensionMap());
						setSingleChild(object, impSeq);
						return true;
					}
				}
				// We have an implicit sequence.  Remove the child from it.
				// Since Case must have an activity inside it (for runtime marker
				// purposes), we won't remove the implicit sequence even if there is
				// less than two children in it.
				List list = getChildList(object);
				return list.remove(child);
			}
		};
	}

	/* ILabeledElement */
	
	public Image getSmallImage(Object object) {
		return BPELUIPlugin.getPlugin().getImage(IBPELUIConstants.ICON_CASE_16);
	}
	
	public Image getLargeImage(Object object) {
		return BPELUIPlugin.getPlugin().getImage(IBPELUIConstants.ICON_CASE_32);
	}	
	
	public String getTypeLabel(Object object) {
		return Messages.CaseAdapter_Case_1; 
	}	
	public String getLabel(Object object) {
		String s = ModelHelper.getDisplayName(object);
		if (s != null && !("".equals(s))) return s; //$NON-NLS-1$
		return getTypeLabel(object);
	}
	
	/* EditPartFactory */
	
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart result = new CaseEditPart();
		result.setModel(model);
		return result;
	}
	
	/* IOutlineEditPartFactory */

	public EditPart createOutlineEditPart(EditPart context, Object model) {
		EditPart result = new OutlineTreeEditPart();
		result.setModel(model);
		return result;
	}

	/* IMarkerHolder */
	
	public IMarker[] getMarkers(Object object) {
		return BPELUtil.getMarkers(object);
	}

	/* IExtensionFactory */
	
	public EObject createExtension(EObject object) {
		return UiextensionmodelFactory.eINSTANCE.createCaseExtension();
	}
}
