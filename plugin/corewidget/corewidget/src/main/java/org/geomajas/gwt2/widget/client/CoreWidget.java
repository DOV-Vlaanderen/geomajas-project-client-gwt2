package org.geomajas.gwt2.widget.client;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.widget.client.featureselectbox.view.FeatureSelectBoxView;
import org.geomajas.gwt2.widget.client.featureselectbox.view.FeatureSelectBoxViewImpl;

/**
 * Starting point for the core widget plugin.
 * 
 * @author Jan De Moerloose
 * @since 2.0.0
 */
@Api(allMethods = true)
public class CoreWidget {

	private static CoreWidget instance;

	private ViewFactory viewFactory;

	private CoreWidget() {
		viewFactory = new ViewFactory() {

			@Override
			public FeatureSelectBoxView createFeatureSelectBox() {
				return new FeatureSelectBoxViewImpl();
			}
		};
	}

	/**
	 * Get a singleton instance.
	 * 
	 * @return Return CoreWidget!
	 */
	public static CoreWidget getInstance() {
		if (instance == null) {
			instance = new CoreWidget();
		}
		return instance;
	}

	public static void setInstance(CoreWidget coreWidget) {
		instance = coreWidget;
	}

	public ViewFactory getViewFactory() {
		return viewFactory;
	}

}
