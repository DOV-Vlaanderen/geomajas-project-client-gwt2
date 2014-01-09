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

package org.geomajas.plugin.wms.client.layer;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.HasLegendWidget;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.LegendUrlSupported;
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.plugin.wms.client.layer.config.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.layer.config.WmsTileConfiguration;

/**
 * <p>
 * Base client-side WMS layer definition. Note that a WMS service can use either a raster data set or a vector data set,
 * we too make that distinction here. This layer definition does not support interaction with the WMS service. If you
 * need support for features or a GetFeatureInfo request, have a look at the {@link FeaturesSupportedWmsLayer}.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface WmsLayer extends Layer, LegendUrlSupported, HasLegendWidget {

	/**
	 * Get the main WMS options. These options are translated into HTTP GET parameters for the WMS calls.
	 * 
	 * @return Get the main WMS options object.
	 */
	WmsLayerConfiguration getConfig();

	/**
	 * Get this layers tile configuration object.
	 * 
	 * @return
	 */
	WmsTileConfiguration getTileConfig();

	/**
	 * Returns the view port CRS. This layer should always have the same CRS as the map!
	 * 
	 * @return The layer CRS (=map CRS).
	 */
	String getCrs();

	/**
	 * Get the view port for this layer.
	 * 
	 * @return The ViewPort, or null if the layer has not been added to a map.
	 */
	ViewPort getViewPort();

	/**
	 * Get the tiles for the specified scale and world bounds.
	 * 
	 * @param scale
	 * @param worldBounds
	 * @return
	 */
	List<Tile> getTiles(double scale, Bbox worldBounds);
}