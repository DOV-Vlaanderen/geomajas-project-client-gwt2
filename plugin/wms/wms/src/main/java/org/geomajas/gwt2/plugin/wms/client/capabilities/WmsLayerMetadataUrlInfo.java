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

package org.geomajas.gwt2.plugin.wms.client.capabilities;

import org.geomajas.annotation.Api;

import java.io.Serializable;

/**
 * Metadata information for a layer in a WMS Capabilities.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api
public interface WmsLayerMetadataUrlInfo extends Serializable {

	/**
	 * Get the type of metadata.
	 *
	 * @return The metadata type.
	 */
	String getType();

	/**
	 * Get the format that expresses the metadata.
	 *
	 * @return The metadata format.
	 */
	String getFormat();

	/**
	 * Get the online resource where the metadata is located.
	 *
	 * @return The online metadata resource.
	 */
	WmsOnlineResourceInfo getOnlineResource();
}
