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
package org.geomajas.jsapi;

import org.geomajas.jsapi.map.Map;


/**
 * MapRegistry provides a registry where {@link org.geomajas.jsapi.map.Map} components can be registered 
 * from GWT to be retrieved from plain Javascript. This will most probably be implemented as a singleton.
 * 
 * @author Oliver May
 *
 */
public interface MapRegistry {

	/**
	 * Return the {@link Map} that is registered with the given application and map ID.
	 * 
	 * @param applicationId the application id.
	 * @param mapId the map id.
	 * @return the map.
	 */
	Map getMap(String applicationId, String mapId);
	
	/**
	 * Register the given {@link Map} with applicationId and mapId. 
	 * 
	 * @param applicationId the application id.
	 * @param mapId the map id.
	 * @param map the map to register.
	 */
	void registerMap(String applicationId, String mapId, Map map);
}
