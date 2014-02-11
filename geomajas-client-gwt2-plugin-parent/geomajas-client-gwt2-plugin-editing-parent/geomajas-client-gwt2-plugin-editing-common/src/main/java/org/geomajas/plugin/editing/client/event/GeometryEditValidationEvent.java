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

package org.geomajas.plugin.editing.client.event;

import java.util.List;

import org.geomajas.annotation.FutureApi;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndex;

/**
 * Event which is passed when the geometry would become invalid during geometry editing. The operation is undone in this
 * case.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public class GeometryEditValidationEvent extends AbstractGeometryEditEvent<GeometryEditValidationHandler> {

	/**
	 * Main constructor.
	 * 
	 * @param geometry geometry
	 * @param indices indices
	 */
	public GeometryEditValidationEvent(Geometry geometry, List<GeometryIndex> indices) {
		super(geometry, indices);
	}

	/**
	 * Get the current editing state.
	 * 
	 * @return Returns the current editing state.
	 */
	public Type<GeometryEditValidationHandler> getAssociatedType() {
		return GeometryEditValidationHandler.TYPE;
	}

	protected void dispatch(GeometryEditValidationHandler handler) {
		handler.onGeometryEditValidation(this);
	}
}