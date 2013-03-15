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

package org.geomajas.plugin.wmsclient.client.layer;

import java.io.Serializable;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.wmsclient.client.service.WmsService.WmsVersion;

/**
 * General WMS configuration object. The values herein will be translated into parameters for the WMS service.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class WmsLayerConfiguration implements Serializable {

	private static final long serialVersionUID = 100L;

	private String baseUrl;

	private String format = "image/png";

	private String layers = "";

	private String styles = "";

	private String filter; // CQL in case the WMS server supports it.

	private boolean transparent = true;

	private WmsVersion version = WmsVersion.V1_3_0;

	private int legendWidth = 20;

	private int legendHeight = 20;

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Get the GetMap image format. The default value is "image/png".
	 * 
	 * @return The GetMap image format.
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Set the GetMap image format. The default value is "image/png".
	 * 
	 * @param format
	 *            The GetMap image format.
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Get the layers parameter used in the GetMap requests.
	 * 
	 * @return The GetMap layers parameter.
	 */
	public String getLayers() {
		return layers;
	}

	/**
	 * Set the layers parameter used in the GetMap requests.
	 * 
	 * @param layers
	 *            The GetMap layers parameter.
	 */
	public void setLayers(String layers) {
		this.layers = layers;
	}

	/**
	 * Get the styles parameter used in the GetMap requests.
	 * 
	 * @return The GetMap styles parameter.
	 */
	public String getStyles() {
		return styles;
	}

	/**
	 * Set the styles parameter used in the GetMap requests.
	 * 
	 * @param styles
	 *            The GetMap styles parameter.
	 */
	public void setStyles(String styles) {
		this.styles = styles;
	}

	/**
	 * Get the filter parameter used in GetMap requests. Note this parameter is not a default WMS parameter, and not all
	 * WMS servers may support this.
	 * 
	 * @return The GetMap filter parameter.
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Set the filter parameter used in GetMap requests. Note this parameter is not a default WMS parameter, and not all
	 * WMS servers may support this.
	 * 
	 * @param filter
	 *            The GetMap filter parameter.
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * Get the GetMap transparent parameter. Default value is 'true'.
	 * 
	 * @return The GetMap transparent parameter.
	 */
	public boolean isTransparent() {
		return transparent;
	}

	/**
	 * Set the transparent parameter used in the GetMap requests. Default value is 'true'.
	 * 
	 * @param transparent
	 *            The GetMap transparent parameter.
	 */
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	/**
	 * Get the WMS version used. Default value is '1.3.0'.
	 * 
	 * @return The WMS version.
	 */
	public WmsVersion getVersion() {
		return version;
	}

	/**
	 * Set the WMS version. Default value is '1.3.0'.
	 * 
	 * @param version
	 *            The WMS version.
	 */
	public void setVersion(WmsVersion version) {
		this.version = version;
	}

	/**
	 * Get the base URL to the WMS service. This URL should not contain any WMS parameters.
	 * 
	 * @return The base URL to the WMS service.
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Set the base URL to the WMS service. This URL should not contain any WMS parameters.
	 * 
	 * @param baseUrl
	 *            The base URL to the WMS service.
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Get the image width for the style icons in a GetLegendGraphic request. Default value is '20' pixels.
	 * 
	 * @return The image width for style icons.
	 */
	public int getLegendWidth() {
		return legendWidth;
	}

	/**
	 * Set the image width for the style icons in a GetLegendGraphic request. Default value is '20' pixels.
	 * 
	 * @param legendWidth
	 *            The image width for style icons.
	 */
	public void setLegendWidth(int legendWidth) {
		this.legendWidth = legendWidth;
	}

	/**
	 * Get the image height for the style icons in a GetLegendGraphic request. Default value is '20' pixels.
	 * 
	 * @return The image height for style icons.
	 */
	public int getLegendHeight() {
		return legendHeight;
	}

	/**
	 * Set the image width for the style icons in a GetLegendGraphic request. Default value is '20' pixels.
	 * 
	 * @param legendHeight
	 *            The image height for style icons.
	 */
	public void setLegendHeight(int legendHeight) {
		this.legendHeight = legendHeight;
	}
}