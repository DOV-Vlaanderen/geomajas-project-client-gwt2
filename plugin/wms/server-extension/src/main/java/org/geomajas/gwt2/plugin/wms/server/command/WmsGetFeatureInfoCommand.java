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

package org.geomajas.gwt2.plugin.wms.server.command;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.geomajas.command.CommandHasRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.converter.FeatureConverter;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService.GetFeatureInfoFormat;
import org.geomajas.gwt2.plugin.wms.server.command.dto.WmsGetFeatureInfoRequest;
import org.geomajas.gwt2.plugin.wms.server.command.dto.WmsGetFeatureInfoResponse;
import org.geomajas.gwt2.plugin.wms.server.command.factory.WmsHttpClientFactory;
import org.geomajas.gwt2.plugin.wms.server.command.factory.impl.DefaultWmsHttpClientFactory;
import org.geomajas.layer.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Parser;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

/**
 * Command that executes a WMS GetFeatureInfo request.
 * <p/>
 * This command is not part of the API and shouldn't be used directly.
 *
 * @author Pieter De Graef
 * @author An Buyle
 * @author Jan De Moerloose
 */
@Component
public class WmsGetFeatureInfoCommand implements
		CommandHasRequest<WmsGetFeatureInfoRequest, WmsGetFeatureInfoResponse> {

	private final Logger log = LoggerFactory.getLogger(WmsGetFeatureInfoCommand.class);

	private static final String PARAM_FORMAT = "info_format";

	private WmsHttpClientFactory httpClientFactory = new DefaultWmsHttpClientFactory();

	@Override
	public void execute(WmsGetFeatureInfoRequest request, WmsGetFeatureInfoResponse response) throws Exception {
		HttpClient client = httpClientFactory.create(request.getUrl());
		URL url = httpClientFactory.getTargetUrl(request.getUrl());

		GetFeatureInfoFormat format = getFormatFromUrl(request.getUrl());
		switch (format) {
			case GML2:
				response.setFeatures(getFeaturesFromUrl(client, url, new org.geotools.gml2.GMLConfiguration(), request.getMaxCoordsPerFeature()));
				break;
			case GML3:
				response.setFeatures(getFeaturesFromUrl(client, url, new org.geotools.gml3.GMLConfiguration(), request.getMaxCoordsPerFeature()));
				break;
			case JSON:
				response.setFeatures(getFeaturesFromJson(client, request, url, request.getMaxCoordsPerFeature()));
				break;
			default:
				String content = readUrl(client, url);
				response.setWmsResponse(content);
		}
	}

	public WmsHttpClientFactory getHttpClientFactory() {
		return httpClientFactory;
	}

	public void setHttpClientFactory(WmsHttpClientFactory httpClientFactory) {
		this.httpClientFactory = httpClientFactory;
	}

	@Override
	public WmsGetFeatureInfoRequest getEmptyCommandRequest() {
		return new WmsGetFeatureInfoRequest();
	}

	@Override
	public WmsGetFeatureInfoResponse getEmptyCommandResponse() {
		return new WmsGetFeatureInfoResponse();
	}

	private List<Feature> getFeaturesFromJson(HttpClient client, WmsGetFeatureInfoRequest request, URL url,
			int maxCoordsPerFeature) throws IOException {
		FeatureConverter converter = new FeatureConverter();
		List<Feature> dtoFeatures = new ArrayList<Feature>();
		HttpGet get = new HttpGet(url.toExternalForm());
		HttpResponse response = client.execute(get);
		if (200 != response.getStatusLine().getStatusCode()) {
			get.releaseConnection();
			throw new IOException("Server returned " + response.getStatusLine() + " for URL " + url.toExternalForm());
		}
		FeatureIterator<SimpleFeature> it = new FeatureJSON()
				.streamFeatureCollection(response.getEntity().getContent());
		while (it.hasNext()) {
			SimpleFeature feature = it.next();
			try {
				dtoFeatures.add(converter.toDto(feature, maxCoordsPerFeature));
			} catch (Exception e) {
				log.error("Error parsing Feature information: " + e.getMessage());
			}
		}
		it.close();
		return dtoFeatures;
	}

	private List<Feature> getFeaturesFromUrl(HttpClient client, URL url, Configuration gml, int maxCoordsPerFeature)
			throws IOException, SAXException, ParserConfigurationException {
		HttpGet get = new HttpGet(url.toExternalForm());
		HttpResponse response = client.execute(get);
		if (200 != response.getStatusLine().getStatusCode()) {
			get.releaseConnection();
			throw new IOException("Server returned " + response.getStatusLine() + " for URL " + url.toExternalForm());
		}

        Parser parser = new Parser(gml);
        parser.setStrict(false);
        FeatureCollection<?, SimpleFeature> collection = (FeatureCollection<?, SimpleFeature>) parser.parse(response.getEntity().getContent());

        List<Feature> dtoFeatures = new ArrayList<Feature>();
		if (null == collection) {
			return dtoFeatures; // empty list
		}
		FeatureConverter converter = new FeatureConverter();
		FeatureIterator<SimpleFeature> it = collection.features();
		if (it.hasNext()) {
			SimpleFeature feature = it.next();
			try {
				dtoFeatures.add(converter.toDto(feature, maxCoordsPerFeature));
			} catch (Exception e) {
				log.error("Error parsing Feature information: " + e.getMessage());
			}
		}
		while (it.hasNext()) {
			SimpleFeature feature = it.next();
			try {
				dtoFeatures.add(converter.toDto(feature, maxCoordsPerFeature));
			} catch (Exception e) {
				// Do nothing...
			}
		}
		return dtoFeatures;
	}

	private GetFeatureInfoFormat getFormatFromUrl(String url) {
		try {
			int index = url.toLowerCase().indexOf(PARAM_FORMAT) + PARAM_FORMAT.length() + 1;
			String format = url.substring(index);
			index = format.indexOf('&');
			if (index > 0) {
				format = format.substring(0, index);
			}
			for (GetFeatureInfoFormat enumValue : GetFeatureInfoFormat.values()) {
				if (enumValue.toString().equalsIgnoreCase(format)) {
					return enumValue;
				}
			}
		} catch (Exception e) {
			log.error("WMS GetFeatureInfo - Cannot understand which format to request... "
					+ "We'll take HTML format as a fallback." + e.getMessage());
		}
		return GetFeatureInfoFormat.HTML;
	}

	private String readUrl(HttpClient client, URL url) throws Exception {
		HttpGet get = new HttpGet(url.toExternalForm());
		HttpResponse response = client.execute(get);
		if (200 != response.getStatusLine().getStatusCode()) {
			get.releaseConnection();
			throw new IOException("Server returned " + response.getStatusLine() + " for URL " + url.toExternalForm());
		}
		return EntityUtils.toString(response.getEntity());
	}
}
