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
package org.geomajas.gwt2.widget.client.feature.featureselectbox.resource;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.widget.client.CoreWidget;

import com.google.gwt.resources.client.ClientBundle;

/**
 * Client resource bundle interface for pure GWT widgets.
 * 
 * @author Dosi Bingov
 * 
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface FeatureSelectBoxResource extends ClientBundle {
	
	/**
	 * Instance for use outside UIBinder.
	 */
	FeatureSelectBoxResource INSTANCE = CoreWidget.getInstance().getClientBundleFactory()
			.createFeatureSelectBoxResource();	

	/**
	 * Get the css resource.
	 * @return the css resource
	 */
	@Source("org/geomajas/gwt2/widget/client/resource/CoreWidget.css")
	FeatureSelectBoxCssResource css();	
	
}