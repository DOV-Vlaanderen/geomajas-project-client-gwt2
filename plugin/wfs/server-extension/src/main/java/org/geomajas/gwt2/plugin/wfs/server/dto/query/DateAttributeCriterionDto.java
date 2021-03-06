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
package org.geomajas.gwt2.plugin.wfs.server.dto.query;

import java.util.Date;

/**
 * {@link AttributeCriterionDto} of type {@link java.util.Date}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DateAttributeCriterionDto extends AttributeCriterionDto<Date> {

	private static final long serialVersionUID = 221L;

	@SuppressWarnings("unused")
	private DateAttributeCriterionDto() {
		super();
	}
	
	@Override
	public DateAttributeCriterionDto clone() {
		return new DateAttributeCriterionDto(getAttributeName(), getOperation(), getValue());
	}

	/**
	 * Create a date criterion for this attribute, operation and value.
	 * 
	 * @param attributeName
	 * @param operation
	 * @param value
	 */
	public DateAttributeCriterionDto(String attributeName, String operation, Date value) {
		super(attributeName, operation, value);
	}

}
