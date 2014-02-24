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

package org.geomajas.gwt2.client.widget.control.scalebar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.widget.AbstractMapWidget;

/**
 * Map widget that shows a scale bar on the map. This widget is meant to be added to the map's widget pane (see {@link
 * MapPresenter#getWidgetPane()}).
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class Scalebar extends AbstractMapWidget {

	/**
	 * Unit type to use on the scale bar.
	 *
	 * @author Pieter De Graef
	 */
	public enum UnitType {
		/**
		 * Metric Units. Meters(m) -- Kilometers(km)
		 */
		METRIC,

		/**
		 * English Units. Yards(yd) -- Miles(mi)
		 */
		ENGLISH,

		/**
		 * English Units. Feet(ft) -- Miles(mi)
		 */
		ENGLISH_FOOT,

		/**
		 * Coordinate Reference System Units. Units(u)
		 */
		CRS
	}

	/**
	 * UI binder definition for the {@link Scalebar} widget.
	 *
	 * @author Pieter De Graef
	 */
	interface ScalebarUiBinder extends UiBinder<Widget, Scalebar> {
	}

	private static final ScalebarUiBinder UI_BINDER = GWT.create(ScalebarUiBinder.class);

	private static final double METERS_IN_MILE = 1609.344d;

	private static final double METERS_IN_YARD = 0.9144d;

	private static final double FEET_IN_METER = 3.2808399d;

	private static final int MAX_SIZE_IN_PIXELS = 125;

	private int[] lengths = new int[] { 1, 2, 5, 10, 25, 50, 100, 250, 500, 750, 1000, 2000, 5000, 10000, 25000, 50000,
			75000, 100000, 250000, 500000, 750000, 1000000, 2000000, 5000000, 10000000 };

	// position in lengths array up to where to test for yards (larger values is for miles)
	private static final int YARD_STARTING_POINT = 11;

	private UnitType unitType = UnitType.METRIC;

	// -- for internal use, holds the last calculated best value
	private int widthInUnits;

	// -- for internal use, holds the last calculated best value
	private int widthInPixels;

	// -- for internal use, for UnitType.ENGLISH only
	private boolean widthInUnitsIsMiles;

	@UiField
	protected DivElement scaleBarElement;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new instance for the given map.
	 *
	 * @param mapPresenter The map presenter.
	 */
	public Scalebar(MapPresenter mapPresenter) {
		this(mapPresenter, (ScalebarResource) GWT.create(ScalebarResource.class));
	}

	/**
	 * Create a new instance for the given map.
	 *
	 * @param mapPresenter The map presenter.
	 * @param resource     Custom resource bundle in case you want to provide your own style for this widget.
	 */
	public Scalebar(MapPresenter mapPresenter, ScalebarResource resource) {
		super(mapPresenter);
		resource.css().ensureInjected();

		mapPresenter.getEventBus().addViewPortChangedHandler(new ViewPortChangedHandler() {

			public void onViewPortChanged(ViewPortChangedEvent event) {
				redrawScale();
			}
		});

		initWidget(UI_BINDER.createAndBindUi(this));
		redrawScale();
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the unit type used in this widget.
	 *
	 * @return The unit type in which scales are displayed.
	 */
	public UnitType getUnitType() {
		return unitType;
	}

	/**
	 * Set the unit type in which scales are displayed.
	 *
	 * @param unitType The new unit type.
	 */
	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void redrawScale() {
		calculateBestFit(1 / mapPresenter.getViewPort().getResolution());
		scaleBarElement.setInnerText(formatUnits(widthInUnits));
		scaleBarElement.getStyle().setWidth(widthInPixels, Style.Unit.PX);
		getElement().getStyle().setWidth(widthInPixels + 10, Style.Unit.PX);
	}

	/**
	 * Find the rounded value (from the lengths array) which fits the closest into the maxSizeInPixels for the given
	 * scale.
	 *
	 * @param scale
	 * @return closest fit in units (will be miles or yards for English, m for metric, unit for CRS)
	 */
	private void calculateBestFit(double scale) {
		double unitLength = mapPresenter.getConfiguration().getUnitLength();
		int len = 0;
		long px = 0;
		if (UnitType.ENGLISH.equals(unitType)) {
			// try miles.
			for (int i = lengths.length - 1; i > -1; i--) {
				len = this.lengths[i];
				px = Math.round((len * scale / unitLength) * METERS_IN_MILE);
				if (px < MAX_SIZE_IN_PIXELS) {
					break;
				}
			}
			// try yards.
			if (px > MAX_SIZE_IN_PIXELS) {
				for (int i = YARD_STARTING_POINT; i > -1; i--) {
					len = this.lengths[i];
					px = Math.round((len * scale / unitLength) * METERS_IN_YARD);
					if (px < MAX_SIZE_IN_PIXELS) {
						break;
					}
				}
				widthInUnitsIsMiles = false;
			} else {
				widthInUnitsIsMiles = true;
			}
		} else if (UnitType.ENGLISH_FOOT.equals(unitType)) {
			// try miles.
			for (int i = lengths.length - 1; i > -1; i--) {
				len = this.lengths[i];
				px = Math.round((len * scale / unitLength) * METERS_IN_MILE);
				if (px < MAX_SIZE_IN_PIXELS) {
					break;
				}
			}
			// try feet.
			if (px > MAX_SIZE_IN_PIXELS) {
				for (int i = YARD_STARTING_POINT; i > -1; i--) {
					len = this.lengths[i];
					px = Math.round((len * scale / unitLength) / FEET_IN_METER);
					if (px < MAX_SIZE_IN_PIXELS) {
						break;
					}
				}
				widthInUnitsIsMiles = false;
			} else {
				widthInUnitsIsMiles = true;
			}
		} else {
			for (int i = lengths.length - 1; i > -1; i--) {
				len = this.lengths[i];
				px = Math.round(len * scale / unitLength);
				if (px < MAX_SIZE_IN_PIXELS) {
					break;
				}
			}
		}

		widthInUnits = len;
		widthInPixels = (int) px;
	}

	/**
	 * format to human readable string converting to unit type.
	 *
	 * @param units
	 * @return
	 */
	private String formatUnits(int units) {
		switch (unitType) {
			case ENGLISH:
				return NumberFormat.getDecimalFormat().format(units) + (widthInUnitsIsMiles ? " mi" : " yd");
			case ENGLISH_FOOT:
				return NumberFormat.getDecimalFormat().format(units) + (widthInUnitsIsMiles ? " mi" : " ft");
			case METRIC:
				if (units < 10000) {
					return NumberFormat.getDecimalFormat().format(units) + " m";
				} else {
					return NumberFormat.getDecimalFormat().format((double) units / 1000) + " km";
				}

			case CRS:
				return NumberFormat.getDecimalFormat().format(units) + " u";

			default:
				return "??";
		}
	}
}