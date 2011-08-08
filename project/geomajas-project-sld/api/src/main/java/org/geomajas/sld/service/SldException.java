/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.service;

/**
 * Generic exception for SLD package.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SldException extends Exception {

	public SldException() {
		super();
	}

	public SldException(String message, Throwable cause) {
		super(message, cause);
	}

	public SldException(String message) {
		super(message);
	}

	public SldException(Throwable cause) {
		super(cause);
	}

}
