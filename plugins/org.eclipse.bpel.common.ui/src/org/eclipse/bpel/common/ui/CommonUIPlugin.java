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
package org.eclipse.bpel.common.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.bpel.common.ui.details.IDetailsColors;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class CommonUIPlugin extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "org.eclipse.bpel.common.ui"; //$NON-NLS-1$

	//The shared instance.
	private static CommonUIPlugin plugin;
	
	private ColorRegistry colorRegistry;
	protected boolean imagesAndColorsInitialized;
	
	/**
	 * The constructor.
	 */
	public CommonUIPlugin() {
		plugin = this;
		imagesAndColorsInitialized = false;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static CommonUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.bpel.common.ui", path);
	}
	
	public ImageRegistry getImageRegistry() {
		ImageRegistry result = super.getImageRegistry();
		initialize();
		return result;
	}
	
	public ColorRegistry getColorRegistry() {
		if (colorRegistry == null) {
			colorRegistry = new ColorRegistry();
			initialize();
		}
		return colorRegistry;
	}

	/**
	 * Creates an image and places it in the image registry.
	 */
	private void createImageDescriptor(String id, URL baseURL) {
		URL url = null;
		try {
			url = new URL(baseURL, ICommonUIConstants.ICON_PATH + id);
		} catch (MalformedURLException e) {
		}
		ImageDescriptor desc = ImageDescriptor.createFromURL(url);
		getImageRegistry().put(id, desc);
	}

	/**
	 * Initializes the table of images used in this plugin.
	 */
	private void initializeImages(Display display) {
		URL baseURL = getBundle().getEntry("/"); //$NON-NLS-1$

		// edit part icons
		createImageDescriptor(ICommonUIConstants.ICON_TRAY_CATEGORY_ADD_BUTTON, baseURL);
		createImageDescriptor(ICommonUIConstants.ICON_TRAY_CATEGORY_REMOVE_BUTTON, baseURL);
		
		// message icons
		createImageDescriptor(ICommonUIConstants.ICON_SM_BLANK, baseURL);
		createImageDescriptor(ICommonUIConstants.ICON_SM_INFO, baseURL);
		createImageDescriptor(ICommonUIConstants.ICON_SM_WARN, baseURL);
		createImageDescriptor(ICommonUIConstants.ICON_SM_ERROR, baseURL);
		
		// marker icons
		createImageDescriptor(ICommonUIConstants.ICON_ERROR, baseURL);		
		createImageDescriptor(ICommonUIConstants.ICON_WARNING, baseURL);		
		createImageDescriptor(ICommonUIConstants.ICON_INFO, baseURL);
		
		// tray buttons
		createImageDescriptor(ICommonUIConstants.ICON_TRAY_EXPAND_ARROW, baseURL);
		ImageRegistry registry = getImageRegistry();
		ImageDescriptor desc = registry.getDescriptor(ICommonUIConstants.ICON_TRAY_EXPAND_ARROW);
		registry.put(ICommonUIConstants.ICON_KEY_TRAY_COLLAPSE_BUTTON, desc.createImage());
		ImageData data = ImageUtils.flip(desc.getImageData());
		registry.put(ICommonUIConstants.ICON_KEY_TRAY_EXPAND_BUTTON, new Image(display, data));
		
		// actions
		createImageDescriptor(ICommonUIConstants.ICON_SHOW_PROP_VIEW_D, baseURL);		
		createImageDescriptor(ICommonUIConstants.ICON_SHOW_PROP_VIEW_E, baseURL);
		
		// tools icons
		createImageDescriptor(ICommonUIConstants.ICON_ZOOM_IN_TOOL, baseURL);
		createImageDescriptor(ICommonUIConstants.ICON_ZOOM_IN_TOOL_DISABLED, baseURL);
		createImageDescriptor(ICommonUIConstants.ICON_ZOOM_OUT_TOOL, baseURL);
		createImageDescriptor(ICommonUIConstants.ICON_ZOOM_OUT_TOOL_DISABLED, baseURL);
	}

	/**
	 * Register the colors used by the details editor and its parts. 
	 */
	private void registerColors(Display display) {
		RGB light = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).getRGB();
		RGB dark = ColorUtils.getShadeRGB(light, -0.09019605f);
		RGB shadow = ColorUtils.getShadeRGB(light, -0.20f);
		RGB scrollBtn = ColorUtils.getShadeRGB(light, -0.1921568f);

		ColorRegistry registry = getColorRegistry();
		registry.put(IDetailsColors.COLOR_LIGHT_BACKGROUND, light);
		registry.put(IDetailsColors.COLOR_DARK_BACKGROUND, dark);
		registry.put(IDetailsColors.COLOR_DARK_SHADOW, shadow);
		registry.put(IDetailsColors.COLOR_TOOL_SELECTED_1, light);
		registry.put(IDetailsColors.COLOR_TOOL_SELECTED_2, dark);
		registry.put(IDetailsColors.COLOR_SCROLL_BUTTON, scrollBtn);
		registry.put(IDetailsColors.COLOR_TOOL_SELECTED_BORDER, display.getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB());
		RGB canvas = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB();
		registry.put(IDetailsColors.COLOR_CANVAS, canvas);
		registry.put(IDetailsColors.COLOR_TEXT, display.getSystemColor(SWT.COLOR_LIST_FOREGROUND).getRGB());
		
		// tray
		registry.put(IDetailsColors.COLOR_TRAY_BACKGROUND, ColorUtils.getLightShade(light, 2, 3));
		
		// selection handler
		Color selectionColor = Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION);
		colorRegistry.put(ICommonUIConstants.COLOR_SELECTION_HANDLE_CORNER, ColorUtils.getLightShade(selectionColor.getRGB(), 2, 3));
	}

	protected void initialize() {
		if (!imagesAndColorsInitialized) {
			imagesAndColorsInitialized = true;
			Display display = Display.getCurrent();
			initializeImages(display);
			registerColors(display);
		}
	}

	/**
	 * Method to create an error status object and write an error message to the log
	 * based on the passed boolean value.  
	 * Return the IStatus that is created.
	 * 
	 * @param message message that describes the error 
	 * @param e Exception
	 * @param writeToLog boolean, whether or not to write the exception and message to the log
	 * @return IStatus
	 */
	public IStatus createErrorStatus(String message, Exception e, boolean writeToLog){
		IStatus status = new Status(
			IStatus.ERROR,
			CommonUIPlugin.PLUGIN_ID, 
			0, 
			message, 
			e);
		
		if (writeToLog)
			CommonUIPlugin.plugin.getLog().log(status);
		return status;
	}
	
	/**
	 * Utility methods for logging exceptions.
	 */
	public static void log(Exception e, int severity) {
		IStatus status = null;
		if (e instanceof CoreException) {
			status = ((CoreException)e).getStatus();
		} else {
			String m = e.getMessage();
			status = new Status(severity, PLUGIN_ID, 0, m==null? "<no message>" : m, e);
		}
		System.out.println(e.getClass().getName()+": "+status);
		CommonUIPlugin.getDefault().getLog().log(status);
	}

	public static void log(Exception e) { log(e, IStatus.ERROR); }
}
