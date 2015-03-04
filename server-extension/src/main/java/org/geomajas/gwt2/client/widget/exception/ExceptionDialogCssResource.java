/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.widget.exception;

import com.google.gwt.resources.client.CssResource;

/**
 * CSS resource bundle that contains all generic styles used in pure gwt geomajas widgets.
 * 
 * @author Dosi Bingov
 * @author Jan De Moerloose
 */
public interface ExceptionDialogCssResource extends CssResource {

	/**
	 * Get a CSS style class.
	 * 
	 * @return
	 */
	String exceptionDialogButtonPanel();

	/**
	 * Get a CSS style class.
	 * 
	 * @return
	 */
	String exceptionDialogStackTracePanel();

	/**
	 * Get a CSS style class.
	 * 
	 * @return
	 */
	String exceptionDialogMessageLabel();

	/**
	 * Get a CSS style class.
	 * 
	 * @return
	 */
	String exceptionDialogCloseIcon();

	/**
	 * Get a CSS style class.
	 * 
	 * @return
	 */
	String exceptionDialogTitle();

}