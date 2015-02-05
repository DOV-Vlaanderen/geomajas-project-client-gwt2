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

package org.geomajas.gwt2.plugin.corewidget.example.client.sample.dialog;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.plugin.corewidget.example.client.sample.dialog.resource.CloseableDialogBoxWidgetResource;

/**
 * Factory for creating the client bundle.
 *
 * @author David Debuck
 */
public class CloseableDialogBoxWidgetClientBundleFactory {

	/**
	 * Get the CloseableDialogBox resources.
	 *
	 * @return CloseableDialogBoxWidgetResource.
	 */
	public CloseableDialogBoxWidgetResource closeableDialogBoxWidgetResource() {
		return GWT.create(CloseableDialogBoxWidgetResource.class);
	}

}
