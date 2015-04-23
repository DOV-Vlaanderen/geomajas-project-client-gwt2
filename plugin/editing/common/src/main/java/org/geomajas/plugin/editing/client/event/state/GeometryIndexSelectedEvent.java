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

package org.geomajas.plugin.editing.client.event.state;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.event.AbstractGeometryEditEvent;
import org.geomajas.plugin.editing.client.service.GeometryIndex;

/**
 * Event which is passed when a part of a geometry is selected during geometry editing.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class GeometryIndexSelectedEvent extends AbstractGeometryEditEvent<GeometryIndexSelectedHandler> {

	/**
	 * Create a new event.
	 * 
	 * @param geometry
	 *            The geometry being edited.
	 * @param indices
	 *            The indices that are selected.
	 */
	public GeometryIndexSelectedEvent(Geometry geometry, List<GeometryIndex> indices) {
		super(geometry, indices);
	}

	@Override
	public Type<GeometryIndexSelectedHandler> getAssociatedType() {
		return GeometryIndexSelectedHandler.TYPE;
	}

	protected void dispatch(GeometryIndexSelectedHandler featureSelectionHandler) {
		featureSelectionHandler.onGeometryIndexSelected(this);
	}
}