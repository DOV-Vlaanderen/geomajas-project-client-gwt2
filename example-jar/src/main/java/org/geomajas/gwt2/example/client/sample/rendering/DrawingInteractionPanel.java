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

package org.geomajas.gwt2.example.client.sample.rendering;

import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates user interaction on custom drawings.
 *
 * @author Pieter De Graef
 */
public class DrawingInteractionPanel implements SamplePanel {

	private MapPresenter mapPresenter;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	private Rectangle rectangle;

	private Text text;

	public Widget asWidget() {
		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());

		// Initialize the map, and return the layout:
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapOsm");
		return mapDecorator;
	}

	/**
	 * Map initialization handler that draws a rectangle in screen space.
	 *
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			VectorContainer container = mapPresenter.getContainerManager().addScreenContainer();
			rectangle = new Rectangle(60, 40, 200, 80);
			rectangle.setFillColor("#CC9900");
			rectangle.setFillOpacity(0.4);

			text = new Text(70, 60, "Drag me...");
			text.setFontFamily("Arial");
			text.setFontSize(16);
			text.setStrokeOpacity(0);
			text.setFillColor("#000000");
			container.add(text);
			container.add(rectangle);

			DragHandler dragHandler = new DragHandler();
			rectangle.addMouseDownHandler(dragHandler);
			rectangle.addMouseUpHandler(dragHandler);
			rectangle.addMouseMoveHandler(dragHandler);
			rectangle.addMouseOutHandler(dragHandler);
			rectangle.addDomHandler(dragHandler, TouchMoveEvent.getType());
			rectangle.addDomHandler(dragHandler, TouchStartEvent.getType());
			rectangle.addDomHandler(dragHandler, TouchEndEvent.getType());
			text.addMouseDownHandler(dragHandler);
			text.addMouseUpHandler(dragHandler);
			text.addMouseMoveHandler(dragHandler);
			text.addMouseOutHandler(dragHandler);
		}
	}

	/**
	 * A mouse handler that lets the rectangle (and the accompanying text) move around the map by dragging it.
	 *
	 * @author Pieter De Graef
	 */
	private class DragHandler
			implements
				MouseDownHandler,
				MouseUpHandler,
				MouseMoveHandler,
				MouseOutHandler,
				TouchStartHandler,
				TouchMoveHandler,
				TouchEndHandler {

		private boolean dragging;

		private int startX;

		private int startY;

		@Override
		public void onTouchEnd(TouchEndEvent event) {
			dragging = false;
		}

		@Override
		public void onTouchMove(TouchMoveEvent event) {
			if (dragging) {
				if (event.getTouches().length() == 1) {
					Element e = event.getRelativeElement();
					int deltaX = event.getTouches().get(0).getRelativeX(e) - startX;
					int deltaY = event.getTouches().get(0).getRelativeY(e) - startY;
					rectangle.setX(rectangle.getX() + deltaX);
					rectangle.setY(rectangle.getY() + deltaY);
					text.setX(text.getX() + deltaX);
					text.setY(text.getY() + deltaY);
				}
			}
		}

		@Override
		public void onTouchStart(TouchStartEvent event) {
			if (event.getTouches().length() == 1) {
				dragging = true;
				Element e = event.getRelativeElement();
				startX = event.getTouches().get(0).getRelativeX(e);
				startY = event.getTouches().get(0).getRelativeY(e);
			}
			// Stop the event from reaching the map controller: no panning while we're dragging the rectangle.
			event.stopPropagation();
		}

		public void onMouseMove(MouseMoveEvent event) {
			if (dragging) {
				int deltaX = event.getX() - startX;
				int deltaY = event.getY() - startY;
				rectangle.setX(rectangle.getX() + deltaX);
				rectangle.setY(rectangle.getY() + deltaY);
				text.setX(text.getX() + deltaX);
				text.setY(text.getY() + deltaY);
			}
		}

		public void onMouseUp(MouseUpEvent event) {
			dragging = false;
		}

		public void onMouseDown(MouseDownEvent event) {
			dragging = true;
			startX = event.getX();
			startY = event.getY();

			// Stop the event from reaching the map controller: no panning while we're dragging the rectangle.
			event.stopPropagation();
		}

		public void onMouseOut(MouseOutEvent arg0) {
			dragging = false;
		}
	}
}
