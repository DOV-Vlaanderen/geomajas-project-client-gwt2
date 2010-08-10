/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.plugin.geocoder.service;

import com.vividsolutions.jts.geom.Coordinate;
import org.geomajas.global.Api;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.geocoder.api.GeocoderService;
import org.geomajas.plugin.geocoder.api.GetLocationResult;
import org.geomajas.plugin.geocoder.api.StaticRegexMatchInfo;
import org.geomajas.plugin.geocoder.api.StaticRegexMatchLocationInfo;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Geocoder service which reads the configuration from the application context.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class StaticRegexMatchService implements GeocoderService {

	@Autowired
	private StaticRegexMatchInfo matchInfo;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService dtoConverterService;

	private CoordinateReferenceSystem crs;

	@PostConstruct
	private void initCrs() throws GeomajasException {
		crs = geoService.getCrs(matchInfo.getCrs());
	}

	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	public GetLocationResult getLocation(List<String> location) {
		GetLocationResult result = null;
		for (StaticRegexMatchLocationInfo test : matchInfo.getLocations()) {
			result = getLocation(test, location);
			if (null != result) {
				break;
			}
		}
		return result;
	}

	GetLocationResult getLocation(StaticRegexMatchLocationInfo test, List<String> location) {
		int index = 0;
		boolean skipMoreDetailed = false;
		for (String toMatch : test.getToMatch()) {
			if ("**".equals(toMatch)) {
				// all smaller areas are allowed
				skipMoreDetailed = true;
				break;
			}
			String regex = toMatch;
			if (index >= location.size()) {
				return null; // not found, not enough parts to match
			}
			if (null != regex && regex.length() > 0) {
				boolean optional = false;
				if (regex.charAt(0) == '?') {
					optional = true;
					regex = regex.substring(1);
				}
				if (!regex.startsWith("^")) {
					regex = "^" + regex;
				}
				if (!regex.endsWith("$")) {
					regex = regex + "$";
				}
				Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
				if (pattern.matcher(location.get(index)).matches()) {
					index++;
				} else {
					if (!optional) {
						return null; // unmatched required part
					}
				}
			}
		}
		if (index < location.size() && !skipMoreDetailed) {
			return null; // remaining unmatched bits
		}
		GetLocationResult result = new GetLocationResult();
		if (null != test.getBbox()) {
			result.setEnvelope(dtoConverterService.toInternal(test.getBbox()));
		} else {
			result.setCoordinate(new Coordinate(test.getX(), test.getY()));
		}
		result.setMatchingStrings(test.getPreferred());
		return result;
	}
}
