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

package org.geomajas.plugin.editing.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Handler for catching edit start events.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface GeometryEditStartHandler extends EventHandler {

	/** The type of the handler. */
	GwtEvent.Type<GeometryEditStartHandler> TYPE = new GwtEvent.Type<GeometryEditStartHandler>();

	/**
	 * Executed when the editing process has begun.
	 * 
	 * @param event
	 *            The geometry edit start event.
	 */
	void onGeometryEditStart(GeometryEditStartEvent event);
}