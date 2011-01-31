/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.widget.featureinfo.client.widget;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.widget.featureinfo.client.widget.attribute.AttributeFormFactory;
import org.geomajas.widget.featureinfo.client.widget.attribute.DefaultAttributeFormFactory;
import org.geomajas.widget.featureinfo.client.widget.attribute.EditableAttributeForm;
import org.geomajas.widget.featureinfo.client.widget.attribute.SimpleAttributeForm;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.events.HasItemChangedHandlers;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p> Started from {@link org.geomajas.gwt.client.widget.FeatureAttributeEditor}.
 * moved here because  the used EditableAttributeForm and SimpleAttributeForm classes have been copied 
 * and changed locally. 
 * Note that it has only been tested in read-only attribute window mode (no editing). 
 *
 * <p> An attribute form widget, that may possibly be edited. Actually, depending on the value of the "disabled" flag,
 * this widget use a different type of attribute form to display the feature's attribute values in.
 * </p>
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author An Buyle
 */
public class FeatureAttributeEditor extends Canvas implements HasItemChangedHandlers {

	private Feature feature;

	private Feature original;

	private VectorLayer layer;

	private EditableAttributeForm editableForm;

	private SimpleAttributeForm simpleForm;

	private AttributeFormFactory formFactory;

	private VLayout layout; /* Attribute window will be a member of this */

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Constructs an attribute form, that may possibly be edited. Actually, depending on the value of the "disabled"
	 * flag, will this widget use a different type of attribute form to display the feature's attribute values in.
	 *
	 * @param layer
	 *            The vector layer that holds all the attribute definitions for the type of feature to display.
	 * @param disabled
	 *            Should the form initially be disabled or not? When disabled, editing is not possible.
	 */
	public FeatureAttributeEditor(VectorLayer layer, boolean disabled) {
		this.layer = layer;
		layout = new VLayout();
		layout.setMembersMargin(0);
		layout.setWidth100();

		// layout.setAutoHeight();
		// layout.setOverflow(Overflow.VISIBLE); // is NOK

		addChild(layout);
		formFactory = new DefaultAttributeFormFactory();
		editableForm = formFactory.createEditableForm(layer.getLayerInfo().getFeatureInfo().getAttributes());
		simpleForm = formFactory.createSimpleForm(layer.getLayerInfo().getFeatureInfo().getAttributes());
		setDisabled(disabled);
		setWidth100();
		setHeight100();
	}

	// -------------------------------------------------------------------------
	// HasItemChangedHandlers implementation:
	// -------------------------------------------------------------------------

	/**
	 * Add a handler to the change events of the attribute values in the form. Note that editing is only possible when
	 * this widget is not disabled.
	 */
	public HandlerRegistration addItemChangedHandler(ItemChangedHandler handler) {
		return editableForm.addItemChangedHandler(handler);
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Overrides the normal setDisabled method, to not only disable or enable this widget, but to change the actual
	 * attribute form that's being displayed. When disabled is set to true, a {@link SimpleAttributeForm} is used,
	 * otherwise a {@link EditableAttributeForm}.
	 */
	public void setDisabled(boolean disabled) {
		if (disabled) {
			if (editableForm != null && layout.hasMember(editableForm.getWidget())) {
				layout.removeMember(editableForm.getWidget());
			}
			layout.addMember(simpleForm.getWidget());
		} else {
			if (simpleForm != null && layout.hasMember(simpleForm.getWidget())) {
				layout.removeMember(simpleForm.getWidget());
			}
			layout.addMember(editableForm.getWidget());
		}
		super.setDisabled(disabled);
		if (feature != null) {
			setFeature(feature);
		}
	}

	/**
	 * Validate the form. This only makes sense when the widget is not disabled. Because only then is it possible for a
	 * user to alter the attribute values.
	 *
	 * @return
	 */
	public boolean validate() {
		return editableForm.validate();
	}

	/** Resets the original values of the feature. */
	public void reset() {
		if (original != null) {
			feature = (Feature) original.clone();
			copyToForm(feature);
		}
	}

	/**
	 * Return the feature, with the current values in the "editable" form. This feature will not necessarily contain
	 * validated attribute values, so it is recommended to call the <code>validate</code> method first.
	 *
	 * @return
	 */
	public Feature getFeature() {
		for (AttributeInfo info : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
			try {
				editableForm.fromForm(info.getName(), feature.getAttributes().get(info.getName()));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return feature;
	}

	/**
	 * Apply a new feature onto this widget. The feature will be immediately shown on the attribute form.
	 *
	 * @param feature
	 *            feature
	 */
	public void setFeature(Feature feature) {
		if (feature != null) {
			// TODO why do these need to be cloned? document or fix
			this.original = feature.clone();
			this.feature = feature.clone();
			copyToForm(this.feature);
		} else {
			original = null;
			feature = null;
			editableForm.clear();
			simpleForm.clear();
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void copyToForm(Feature feature) {
		for (AttributeInfo info : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
			try {
				editableForm.toForm(info.getName(), feature.getAttributes().get(info.getName()));
				simpleForm.toForm(info.getName(), feature.getAttributes().get(info.getName()));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
