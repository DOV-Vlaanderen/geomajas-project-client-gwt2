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
package org.geomajas.gwt2.client.service;

import org.geomajas.annotation.Api;

/**
 * Service that provides management of a number of interesting end point URLs.
 * 
 * @author Jan De Moerloose
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface EndPointService {

	/**
	 * Get the Geomajas RPC service end point URL.
	 * 
	 * @return The URL to the Geomajas RPC service.
	 */
	String getCommandServiceUrl();

	/**
	 * Set the Geomajas RPC service end point URL to a different value. If pointing to a different context, make sure
	 * the GeomajasController of that context supports this.
	 * 
	 * @see org.geomajas.gwt.server.mvc.GeomajasController
	 * 
	 * @param url
	 *            the new URL
	 */
	void setCommandServiceUrl(String commandServiceUrl);

	/**
	 * Get the URL to the Geomajas Legend service.
	 * 
	 * @return The URL to the Geomajas Legend service.
	 */
	String getLegendServiceUrl();

	/**
	 * Set the URL to the Geomajas Legend service.
	 * 
	 * @param legendServiceUrl
	 *            The new URL to the Geomajas Legend service.
	 */
	void setLegendServiceUrl(String legendServiceUrl);

	/**
	 * Return the Spring dispatcher URL for the web application. The URL ends with a slash.
	 * 
	 * @return The URL to the Spring Dispatcher.
	 */
	String getDispatcherUrl();

	/**
	 * Set the Spring dispatcher URL for the web application.
	 * 
	 * @param dispatcherUrl
	 *            The Spring dispatcher URL for the web application.
	 */
	void setDispatcherUrl(String dispatcherUrl);
}