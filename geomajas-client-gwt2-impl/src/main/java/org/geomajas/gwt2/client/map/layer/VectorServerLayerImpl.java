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

package org.geomajas.gwt2.client.map.layer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.dto.RegisterNamedStyleInfoRequest;
import org.geomajas.command.dto.RegisterNamedStyleInfoResponse;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.event.FeatureDeselectedEvent;
import org.geomajas.gwt2.client.event.FeatureSelectedEvent;
import org.geomajas.gwt2.client.event.LayerLabelHideEvent;
import org.geomajas.gwt2.client.event.LayerLabelShowEvent;
import org.geomajas.gwt2.client.event.LayerStyleChangedEvent;
import org.geomajas.gwt2.client.event.LayerStyleChangedHandler;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedHandler;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.dom.FixedScaleLayerRenderer;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.RuleInfo;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Vector layer representation.
 * 
 * @author Pieter De Graef
 */
public class VectorServerLayerImpl extends AbstractServerLayer<ClientVectorLayerInfo> implements VectorServerLayer {

	private final LayerRenderer renderer;

	private final Map<String, Feature> selection;

	private String filter;

	private boolean labeled;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public VectorServerLayerImpl(ClientVectorLayerInfo layerInfo, ViewPort viewPort, MapEventBus eventBus) {
		super(layerInfo, viewPort, eventBus);
		this.selection = new HashMap<String, Feature>();
		this.renderer = new FixedScaleLayerRenderer(viewPort, this, eventBus);
	}

	// ------------------------------------------------------------------------
	// Layer implementation:
	// ------------------------------------------------------------------------

	@Override
	public LayerRenderer getRenderer() {
		return renderer;
	}

	// ------------------------------------------------------------------------
	// FeaturesSupported implementation:
	// ------------------------------------------------------------------------

	@Override
	public void setFilter(String filter) {
		this.filter = filter;
		refresh();
	}

	@Override
	public String getFilter() {
		return filter;
	}

	@Override
	public boolean isFeatureSelected(String featureId) {
		return selection.containsKey(featureId);
	}

	@Override
	public boolean selectFeature(Feature feature) {
		if (!selection.containsValue(feature) && feature.getLayer() == this) {
			selection.put(feature.getId(), feature);
			eventBus.fireEvent(new FeatureSelectedEvent(this, feature));
		}
		return false;
	}

	@Override
	public boolean deselectFeature(Feature feature) {
		if (selection.containsKey(feature.getId())) {
			selection.remove(feature.getId());
			eventBus.fireEvent(new FeatureDeselectedEvent(this, feature));
			return true;
		}
		return false;
	}

	@Override
	public void clearSelectedFeatures() {
		for (Feature feature : selection.values()) {
			eventBus.fireEvent(new FeatureDeselectedEvent(this, feature));
		}
		selection.clear();
	}

	@Override
	public Collection<Feature> getSelectedFeatures() {
		return selection.values();
	}

	// ------------------------------------------------------------------------
	// LabelsSupported implementation:
	// ------------------------------------------------------------------------

	@Override
	public void setLabeled(boolean labeled) {
		this.labeled = labeled;
		if (labeled) {
			eventBus.fireEvent(new LayerLabelShowEvent(this));
		} else {
			eventBus.fireEvent(new LayerLabelHideEvent(this));
		}
	}

	@Override
	public boolean isLabeled() {
		return labeled;
	}

	// ------------------------------------------------------------------------
	// VectorServerLayer implementation:
	// ------------------------------------------------------------------------

	@Override
	public void updateStyle(NamedStyleInfo nsi) {
		getLayerInfo().setNamedStyleInfo(nsi);
		GwtCommand commandRequest = new GwtCommand(RegisterNamedStyleInfoRequest.COMMAND);
		RegisterNamedStyleInfoRequest request = new RegisterNamedStyleInfoRequest();
		request.setLayerId(getServerLayerId());
		request.setNamedStyleInfo(getLayerInfo().getNamedStyleInfo());
		commandRequest.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(commandRequest,
				new AbstractCommandCallback<RegisterNamedStyleInfoResponse>() {

					@Override
					public void execute(RegisterNamedStyleInfoResponse response) {
						getLayerInfo().getNamedStyleInfo().setName(response.getStyleName());
						eventBus.fireEvent(new LayerStyleChangedEvent(VectorServerLayerImpl.this));
					}
				});
	}

	@Override
	public List<RuleInfo> getRules() {
		List<RuleInfo> rules = new ArrayList<RuleInfo>();
		for (FeatureTypeStyleInfo sfi : layerInfo.getNamedStyleInfo().getUserStyle().getFeatureTypeStyleList()) {
			rules.addAll(sfi.getRuleList());
		}
		return rules;
	}

	// ------------------------------------------------------------------------
	// HasLegendWidget implementation:
	// ------------------------------------------------------------------------

	@Override
	public IsWidget buildLegendWidget() {
		return new VectorServerLayerLegendWidget(this, eventBus, viewPort);
	}

	// ------------------------------------------------------------------------
	// Private widget class that represents the Legend for this layer:
	// ------------------------------------------------------------------------

	/**
	 * Legend widget for this layer implementation.
	 * 
	 * @author Pieter De Graef
	 */
	private class VectorServerLayerLegendWidget implements IsWidget {

		private final List<ServerLayerStyleWidget> ruleWidgets = new ArrayList<ServerLayerStyleWidget>();

		private VerticalPanel layout;

		protected VectorServerLayerLegendWidget(VectorServerLayer layer, MapEventBus eventBus, ViewPort viewPort) {

			// Zooming in or out may cause some styles to become visible/invisible:
			eventBus.addViewPortChangedHandler(new ViewPortChangedHandler() {

				public void onViewPortChanged(ViewPortChangedEvent event) {
					updateVisibility();
				}
			});

			// Update the legend widget when the style on this layer changes:
			eventBus.addLayerStyleChangedHandler(new LayerStyleChangedHandler() {

				@Override
				public void onLayerStyleChanged(LayerStyleChangedEvent event) {
					layout.clear();
					buildLegend(layout);
				}
			}, VectorServerLayerImpl.this);
		}

		@Override
		public Widget asWidget() {
			if (layout == null) {
				layout = new VerticalPanel();
				buildLegend(layout);
			}
			return layout;
		}

		private void buildLegend(VerticalPanel layout) {
			NamedStyleInfo styleInfo = getLayerInfo().getNamedStyleInfo();
			int i = 0;
			for (FeatureTypeStyleInfo sfi : styleInfo.getUserStyle().getFeatureTypeStyleList()) {
				for (RuleInfo rInfo : sfi.getRuleList()) {
					String url = GeomajasImpl.getInstance().getEndPointService().getLegendServiceUrl();
					addPath(url, getServerLayerId());
					addPath(url, styleInfo.getName());
					addPath(url, i + ".png");
					ServerLayerStyleWidget widget = new ServerLayerStyleWidget(URL.encode(url), rInfo.getName(), rInfo);
					ruleWidgets.add(widget);
					layout.add(widget);
					i++;
				}
			}
		}

		private void updateVisibility() {
			for (ServerLayerStyleWidget ruleWidget : ruleWidgets) {
				ruleWidget.asWidget().setVisible(isVisible(ruleWidget.getRule(), viewPort));
			}
		}

		/** TODO This method might be interesting as a static method in some SLD utility class. */
		private boolean isVisible(RuleInfo rule, ViewPort viewPort) {
			if (rule == null) {
				return true;
			}
			double minScale = Double.MAX_VALUE;
			double maxScale = Double.MIN_VALUE;

			if (rule.getMinScaleDenominator() != null && rule.getMinScaleDenominator().getMinScaleDenominator() != 0) {
				minScale = viewPort.toScale(rule.getMinScaleDenominator().getMinScaleDenominator());
			}

			if (rule.getMaxScaleDenominator() != null && rule.getMaxScaleDenominator().getMaxScaleDenominator() != 0) {
				maxScale = viewPort.toScale(rule.getMaxScaleDenominator().getMaxScaleDenominator());
			}

			return (maxScale <= viewPort.getScale() && viewPort.getScale() < minScale);
		}
	}
}