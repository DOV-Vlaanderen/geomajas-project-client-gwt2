/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client;

import org.geomajas.gwt2.client.gfx.GfxUtil;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.service.CommandService;
import org.geomajas.gwt2.client.service.EndPointService;

import com.google.web.bindery.event.shared.EventBus;

/**
 * Geomajas starting point. This class allows you to request singleton services or create new instances.
 * 
 * @author Pieter De Graef
 */
public interface Geomajas {

	/**
	 * Create a new empty map. This map still needs to be initialized (it needs to fetch a configuration from the
	 * server, only then is it initialized, and are any layers available).
	 * 
	 * @return An empty map.
	 */
	MapPresenter getMapPresenter();

	/**
	 * Get the {@link GfxUtil} singleton. Utility service that helps out when rendering custom shapes on the map.
	 * 
	 * @return The {@link GfxUtil} singleton.
	 */
	GfxUtil getGfxUtil();

	/**
	 * Get the {@link EndPointService} singleton. Has pointers to the Geomajas services on the back-end, and allows
	 * those end-points to be altered in case your server is somewhere else (for example behind a proxy).
	 * 
	 * @return The {@link EndPointService} singleton.
	 */
	EndPointService getEndPointService();

	/**
	 * Get the {@link CommandService} singleton. This service allows for executing commands on the back-end. It is the
	 * base for all Geomajas client-server communication.
	 * 
	 * @return The {@link EndPointService} singleton.
	 */
	CommandService getCommandService();

	/**
	 * Get a general EventBus singleton. This EventBus should should be used outside of the map, to catch application
	 * specific events.
	 * 
	 * @return A general EventBus.
	 */
	EventBus getEventBus();
}