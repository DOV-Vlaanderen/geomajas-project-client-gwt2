/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt2.widget.client.other.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

/**
 * Client resource bundle interface for pure GWT widgets.
 *
 * @author Dosi Bingov
 * @author Jan De Moerloose
 */
public interface CloseableDialogBoxResource extends ClientBundle {

	/**
	 * Instance for use outside UIBinder.
	 */
	CloseableDialogBoxResource INSTANCE = GWT.create(CloseableDialogBoxResource.class);

	/**
	 * Get the css resource.
	 * @return the css resource
	 */
	@Source("closeableDialogBox.css")
	CloseableDialogBoxCssResource css();

	/**
	 * Image sprite.
	 * @return
	 */
	@Source("image/close.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource closeableDialogCloseIcon();

	/**
	 * Image sprite.
	 * @return
	 */
	@Source("image/close_hover.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource closeableDialogCloseIconHover();


	/**
	 * Image sprite.
	 * @return
	 *
	 */
	@Source("image/hborder.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal)
	ImageResource closeableDialogHborder();

	/**
	 * Image sprite.
	 * @return
	 */
	@Source("image/vborder.png")
	@ImageOptions(repeatStyle = RepeatStyle.Vertical)
	ImageResource closeableDialogVborder();

	/**
	 * Image sprite.
	 * @return
	 */
	@Source("image/corner.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	ImageResource closeableDialogCorner();
}