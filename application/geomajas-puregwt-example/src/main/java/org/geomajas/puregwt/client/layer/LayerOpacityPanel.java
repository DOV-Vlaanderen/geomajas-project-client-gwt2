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

package org.geomajas.puregwt.client.layer;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.map.layer.OpacitySupported;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates layer refreshing.
 * 
 * @author Pieter De Graef
 */
public class LayerOpacityPanel extends ContentPanel {

	private static final int MAX_OPACITY = 100;

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, LayerOpacityPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	@UiField
	protected TextBox opacityBox;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public LayerOpacityPanel(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	public Widget getContentWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter.setSize(640, 480);

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapOsm");
		return layout;
	}

	@UiHandler("applyBtn")
	public void onApplyClicked(ClickEvent event) {
		if (mapPresenter.getLayersModel().getLayerCount() == 0) {
			return;
		}
		Layer layer = mapPresenter.getLayersModel().getLayer(0);
		if (layer != null && layer instanceof OpacitySupported) {
			OpacitySupported os = (OpacitySupported) layer;

			String opacityTxt = opacityBox.getValue();
			int opacity = MAX_OPACITY;
			try {
				opacity = Integer.parseInt(opacityTxt);
				if (opacity > MAX_OPACITY) {
					opacity = MAX_OPACITY;
					opacityBox.setValue(MAX_OPACITY + "");
				} else if (opacity < 0) {
					opacity = 0;
					opacityBox.setValue("0");
				}
				os.setOpacity((double) opacity / MAX_OPACITY);
			} catch (Exception e) { // NOSONAR
				Window.alert("Could not parse opacity... Default value of " + MAX_OPACITY + " is used");
				os.setOpacity(1.0);
				opacityBox.setValue(MAX_OPACITY + "");
			}
		}
	}

	public String getTitle() {
		return "Layer opacity";
	}

	public String getDescription() {
		return "This showcase demonstrates changing a layer's opacity.";
	}
}