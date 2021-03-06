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

package org.geomajas.gwt2.client.widget.control.zoomtorect;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.event.PointerEvents;
import org.geomajas.gwt.client.event.PointerTouchEndEvent;
import org.geomajas.gwt.client.event.PointerTouchEndHandler;
import org.geomajas.gwt.client.event.PointerTouchMoveEvent;
import org.geomajas.gwt.client.event.PointerTouchMoveHandler;
import org.geomajas.gwt.client.event.PointerTouchStartEvent;
import org.geomajas.gwt.client.event.PointerTouchStartHandler;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.animation.NavigationAnimationFactory;
import org.geomajas.gwt2.client.controller.MapEventParserImpl;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedHandler;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.ZoomOption;
import org.geomajas.gwt2.client.widget.AbstractMapWidget;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.path.ClosePath;
import org.vaadin.gwtgraphics.client.shape.path.LineTo;
import org.vaadin.gwtgraphics.client.shape.path.MoveTo;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Map widget that displays a button for zooming in to a rectangle on the map. The user is supposed to drag the
 * rectangle after hitting this button. This widget is meant to be added to the map's widget pane (see
 * {@link MapPresenter#getWidgetPane()}).
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class ZoomToRectangleControl extends AbstractMapWidget {

	private final ZoomToRectangleControlResource resource;

	private VectorContainer container;

	private ZoomToRectGroup zoomToRectangleGroup;

	private HandlerRegistration escapeHandler;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new instance for the given map.
	 * 
	 * @param mapPresenter The map presenter.
	 */
	public ZoomToRectangleControl(MapPresenter mapPresenter) {
		this(mapPresenter, GeomajasImpl.getClientBundleFactory().createZoomToRectangleControlResource());
	}

	/**
	 * Create a new instance for the given map.
	 * 
	 * @param mapPresenter The map presenter.
	 */
	public ZoomToRectangleControl(MapPresenter mapPresenter, ZoomToRectangleControlResource resource) {
		super(mapPresenter);
		this.resource = resource;
		this.resource.css().ensureInjected();
		buildGui();
		mapPresenter.getEventBus().addViewPortChangedHandler(new ViewPortChangedHandler() {

			public void onViewPortChanged(ViewPortChangedEvent event) {
				cleanup();
			}
		});
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void cleanup() {
		if (zoomToRectangleGroup != null) {
			container.remove(zoomToRectangleGroup);
			zoomToRectangleGroup = null;
		}
		if (escapeHandler != null) {
			escapeHandler.removeHandler();
			escapeHandler = null;
		}
		mapPresenter.getContainerManager().removeVectorContainer(container);
	}

	private void buildGui() {
		initWidget(new SimplePanel());
		getElement().getStyle().setPosition(Position.ABSOLUTE);
		getElement().getStyle().setTop(5, Unit.PX);
		getElement().getStyle().setLeft(60, Unit.PX);
		setStyleName(resource.css().zoomToRectangle());
		StopPropagationHandler preventWeirdBehaviourHandler = new StopPropagationHandler();
		addDomHandler(preventWeirdBehaviourHandler, MouseDownEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, MouseUpEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, ClickEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, DoubleClickEvent.getType());

		// Create TOP button:
		if (PointerEvents.isSupported()) {
			addDomHandler(new PointerTouchStartHandler() {

				@Override
				public void onPointerTouchStart(PointerTouchStartEvent event) {
					startRectangle();

				}
			}, PointerTouchStartEvent.getType());
		} else {
			addDomHandler(new MouseUpHandler() {

				public void onMouseUp(MouseUpEvent event) {
					startRectangle();
				}

			}, MouseUpEvent.getType());
		}
	}

	private void startRectangle() {
		cleanup();
		container = mapPresenter.getContainerManager().addScreenContainer();
		zoomToRectangleGroup = new ZoomToRectGroup(mapPresenter.getViewPort());
		escapeHandler = Event.addNativePreviewHandler(new NativePreviewHandler() {

			public void onPreviewNativeEvent(NativePreviewEvent event) {
				if (event.getTypeInt() == Event.ONKEYDOWN || event.getTypeInt() == Event.ONKEYPRESS) {
					if (KeyCodes.KEY_ESCAPE == event.getNativeEvent().getKeyCode()) {
						cleanup();
					}
				}
			}
		});

		container.add(zoomToRectangleGroup);
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Vector group that lets the user zoom to a rectangle.
	 * 
	 * @author Pieter De Graef
	 */
	private class ZoomToRectGroup extends Group {

		private Rectangle eventCatcher;

		private Path zoomInRect;

		private boolean dragging;

		private Coordinate xy;

		private Bbox screenBounds;

		private MapEventParser eventParser = new MapEventParserImpl(mapPresenter);

		public ZoomToRectGroup(final ViewPort viewPort) {
			eventCatcher = new Rectangle(0, 0, mapPresenter.getViewPort().getMapWidth(), mapPresenter.getViewPort()
					.getMapHeight());
			eventCatcher.setFillOpacity(0);
			eventCatcher.setStrokeOpacity(0);

			zoomInRect = new Path(0, 0);
			zoomInRect.setFillColor("#000000");
			zoomInRect.setFillOpacity(0.2);
			zoomInRect.setStrokeColor("#000000");
			zoomInRect.setStrokeWidth(1);
			zoomInRect.setStrokeOpacity(1);
			DOM.setElementAttribute(zoomInRect.getElement(), "fill-rule", "evenodd");
			zoomInRect.lineTo(viewPort.getMapWidth(), 0);
			zoomInRect.lineTo(viewPort.getMapWidth(), viewPort.getMapHeight());
			zoomInRect.lineTo(0, viewPort.getMapHeight());
			zoomInRect.close();
			zoomInRect.moveTo(0, 0);
			zoomInRect.lineTo(0, 0);
			zoomInRect.lineTo(0, 0);
			zoomInRect.lineTo(0, 0);
			zoomInRect.close();

			add(zoomInRect);
			add(eventCatcher);

			if (PointerEvents.isSupported()) {
				eventCatcher.addDomHandler(new PointerTouchStartHandler() {

					@Override
					public void onPointerTouchStart(PointerTouchStartEvent event) {
						startDragging(event);

					}
				}, PointerTouchStartEvent.getType());

				eventCatcher.addDomHandler(new PointerTouchEndHandler() {

					@Override
					public void onPointerTouchEnd(PointerTouchEndEvent event) {
						stopDragging(viewPort, event);
					}
				}, PointerTouchEndEvent.getType());

				eventCatcher.addDomHandler(new PointerTouchMoveHandler() {

					@Override
					public void onPointerTouchMove(PointerTouchMoveEvent event) {
						continueDragging(event);
					}
				}, PointerTouchMoveEvent.getType());
			} else {
				eventCatcher.addMouseDownHandler(new MouseDownHandler() {

					public void onMouseDown(MouseDownEvent event) {
						startDragging(event);
					}

				});

				eventCatcher.addMouseUpHandler(new MouseUpHandler() {

					public void onMouseUp(MouseUpEvent event) {
						stopDragging(viewPort, event);
					}
				});

				eventCatcher.addMouseMoveHandler(new MouseMoveHandler() {

					public void onMouseMove(MouseMoveEvent event) {
						continueDragging(event);
					}
				});

				eventCatcher.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {
						event.stopPropagation();
					}
				});

				eventCatcher.addDoubleClickHandler(new DoubleClickHandler() {

					public void onDoubleClick(DoubleClickEvent event) {
						event.stopPropagation();
					}
				});
			}

		}

		private void updateRectangle(HumanInputEvent<?> event) {
			int beginX = (int) xy.getX();
			int beginY = (int) xy.getY();
			int endX = (int) eventParser.getLocation(event, RenderSpace.SCREEN).getX();
			int endY = (int) eventParser.getLocation(event, RenderSpace.SCREEN).getY();

			// Check if begin and end need to be reversed:
			if (beginX > endX) {
				int temp = endX;
				endX = beginX;
				beginX = temp;
			}
			if (beginY > endY) {
				int temp = endY;
				endY = beginY;
				beginY = temp;
			}

			int width = endX - beginX;
			int height = endY - beginY;
			if (height != 0 && width != 0) {
				zoomInRect.setStep(5, new MoveTo(false, beginX, beginY));
				zoomInRect.setStep(6, new LineTo(false, endX, beginY));
				zoomInRect.setStep(7, new LineTo(false, endX, endY));
				zoomInRect.setStep(8, new LineTo(false, beginX, endY));
				zoomInRect.setStep(9, new ClosePath());
				screenBounds = new Bbox(beginX, beginY, width, height);
			}
		}

		private void startDragging(HumanInputEvent<?> event) {
			dragging = true;
			xy = eventParser.getLocation(event, RenderSpace.SCREEN);
			updateRectangle(event);
			event.stopPropagation();
			event.preventDefault();
		}

		private void stopDragging(final ViewPort viewPort, HumanInputEvent<?> event) {
			if (dragging) {
				dragging = false;
				if (screenBounds != null) {
					Bbox worldBounds = viewPort.getTransformationService().transform(screenBounds, RenderSpace.SCREEN,
							RenderSpace.WORLD);
					View endView = viewPort.asView(worldBounds, ZoomOption.LEVEL_CLOSEST);
					viewPort.registerAnimation(NavigationAnimationFactory.createZoomIn(mapPresenter, endView));
				}
			}
			event.stopPropagation();
		}

		private void continueDragging(HumanInputEvent<?> event) {
			if (dragging) {
				updateRectangle(event);
			}
			event.stopPropagation();
		}
	}
}