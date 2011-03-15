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

package org.geomajas.plugin.rasterizing.api;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.global.Api;
import org.geomajas.global.ExpectAlternatives;
import org.geomajas.global.GeomajasException;
import org.geomajas.global.UserImplemented;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;

/**
 * Factory that creates a renderable layer based on the metadata supplied. Typically, each layer class will have its own
 * factory. Use the {@link LayerFactoryService} component as an entry point for iterating over all configured factories.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
@ExpectAlternatives
public interface LayerFactory {

	String USERDATA_KEY_SHOWING = "geomajas.rasterizing.showing";

	/**
	 * Returns true if this factory is capable of creating layer instances for the specified metadata.
	 * 
	 * @param mapContext
	 *            the map context
	 * @param clientLayerInfo
	 *            the client layer metadata
	 * @return true if we can create layer instances
	 */
	boolean canCreateLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo);

	/**
	 * Creates a layer for the specified metadata.
	 * 
	 * @param mapContext
	 *            the map context
	 * @param clientLayerInfo
	 *            the client layer metadata
	 * @return layer ready for rendering
	 * @throws GeomajasException
	 *             something went wrong
	 */
	Layer createLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) throws GeomajasException;
}
