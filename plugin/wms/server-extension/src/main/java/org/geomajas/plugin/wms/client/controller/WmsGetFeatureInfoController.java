/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.wms.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.controller.AbstractMapController;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.plugin.wms.client.layer.FeaturesSupportedWmsLayer;
import org.geomajas.plugin.wms.client.service.WmsService.GetFeatureInfoFormat;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * Default map controller that executes WMS GetFeatureInfo requests on the registered layers.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class WmsGetFeatureInfoController extends AbstractMapController {

	private final List<FeaturesSupportedWmsLayer> layers;

	private Callback<List<Feature>, String> gmlCallback;

	private Callback<Object, String> htmlCallback;

	private GetFeatureInfoFormat format = GetFeatureInfoFormat.GML2;

	private int maxCoordsPerFeature = -1;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new GetFeatureInfoController. Don't forget to register layers.
	 */
	public WmsGetFeatureInfoController() {
		this(null);
	}

	/**
	 * Create a new GetFeatureInfoController.
	 *
	 * @param layer Immediately add a layer onto which to execute GetFeatureInfo requests.
	 */
	public WmsGetFeatureInfoController(FeaturesSupportedWmsLayer layer) {
		super(false);
		this.layers = new ArrayList<FeaturesSupportedWmsLayer>();
		if (layer != null) {
			addLayer(layer);
		}
	}

	// ------------------------------------------------------------------------
	// MapController implementation:
	// ------------------------------------------------------------------------

	/**
	 * Execute a GetFeatureInfo on mouse up.
	 */
	public void onMouseUp(MouseUpEvent event) {
		// Do not interfere with default behaviour:
		super.onMouseUp(event);

		// Get the event location in world space:
		Coordinate worldLocation = getLocation(event, RenderSpace.WORLD);

		// Now execute the GetFeatureInfo for each layer:
		for (FeaturesSupportedWmsLayer layer : layers) {
			switch (format) {
				case GML2:
				case GML3:
					if (gmlCallback == null) {
						throw new IllegalStateException("No callback has been set on the WmsGetFeatureInfoController");
					}
					layer.getFeatureInfo(worldLocation, gmlCallback);
					break;
				default:
					if (htmlCallback == null) {
						throw new IllegalStateException("No callback has been set on the WmsGetFeatureInfoController");
					}
					layer.getFeatureInfo(worldLocation, format, htmlCallback);
			}
		}
	}

	/**
	 * Add a layer for which a GetFeatureRequest should be fired on click.
	 *
	 * @param layer The layer to add.
	 */
	public void addLayer(FeaturesSupportedWmsLayer layer) {
		layers.add(layer);
	}

	/**
	 * Remove a layer for which a GetFeatureInfoRequest should no longer be fired on click.
	 *
	 * @param layer The layer to remove again.
	 */
	public void removeLayer(FeaturesSupportedWmsLayer layer) {
		layers.remove(layer);
	}

	/**
	 * Get the default GetFeatureInfoFormat. By default this is {@link GetFeatureInfoFormat#GML2}.
	 *
	 * @return the GetFeatureInfoFormat used.
	 */
	public GetFeatureInfoFormat getFormat() {
		return format;
	}

	/**
	 * Set a new GetFeatureInfoFormat to use in the GetFeatureInfoRequest.
	 *
	 * @param format The new GetFeatureInfoFormat.
	 */
	public void setFormat(GetFeatureInfoFormat format) {
		this.format = format;
	}

	/**
	 * Set the callback to use in case the GetFeatureInfoFormat is {@link GetFeatureInfoFormat#GML2}.
	 *
	 * @param gmlCallback The callback to execute when the response returns. This response already contains a list of
	 *                    features, and should not be parsed anymore.
	 */
	public void setGmlCallback(Callback<List<Feature>, String> gmlCallback) {
		this.gmlCallback = gmlCallback;
	}

	/**
	 * Set the callback to use in case the GetFeatureInfoFormat is NOT {@link GetFeatureInfoFormat#GML2}.
	 *
	 * @param htmlCallback The callback to execute when the response returns. Note that the response is the bare boned
	 *                     WMS. GetFeatureInfo. It is up to you to parse it.
	 */
	public void setHtmlCallback(Callback<Object, String> htmlCallback) {
		this.htmlCallback = htmlCallback;
	}

	/**
	 * Get the maximum number of coordinates geometries may contain. This is only applied when the GetFeatureInfo format
	 * is GML.
	 *
	 * @return The maximum number of coordinates per geometry.
	 */
	public int getMaxCoordsPerFeature() {
		return maxCoordsPerFeature;
	}

	/**
	 * Set the maximum number of coordinates geometries may contain. This is only applied when the GetFeatureInfo format
	 * is GML.
	 *
	 * @param maxCoordsPerFeature The maximum number of coordinates per geometry.
	 */
	public void setMaxCoordsPerFeature(int maxCoordsPerFeature) {
		this.maxCoordsPerFeature = maxCoordsPerFeature;
	}
}
