/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map;

import java.util.Collection;
import java.util.Set;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.puregwt.client.controller.MapController;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.feature.FeatureService;
import org.geomajas.puregwt.client.map.render.MapRenderer;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Central map definition.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface MapPresenter extends IsWidget {

	/**
	 * Initialize the map. This method will try to fetch the associated map configuration from the server and apply it
	 * on return. A special {@link org.geomajas.puregwt.client.event.MapInitializationEvent} will be fired once
	 * initialization is done.
	 */
	void initialize(String applicationId, String id);

	/**
	 * Get the event bus for this map. All events regarding this map, it's layers and it's features will originate from
	 * this event bus. In other words, this is where you need to register your handlers.
	 * 
	 * @return The event bus for this map.
	 */
	MapEventBus getEventBus();

	/**
	 * Return the widget that displays the map in the HTML page.
	 */
	Widget asWidget();

	/**
	 * Set a new renderer on the map. This renderer is responsible for making sure the map is always renderer correctly.
	 * 
	 * @param mapRenderer
	 *            The new map renderer responsible for rendering the map.
	 */
	void setMapRenderer(MapRenderer mapRenderer);

	/**
	 * Set the number of milliseconds an animated navigation sequence should last.
	 * 
	 * @param animationMillis
	 *            The number of milliseconds an animated navigation sequence should last.
	 */
	void setAnimationMillis(int animationMillis);

	/**
	 * Apply a new width and height on the map. Both parameters are expressed in pixels.
	 * 
	 * @param width
	 *            The new pixel width for the map.
	 * @param height
	 *            The new pixel height for the map.
	 */
	void setSize(int width, int height);

	/**
	 * <p>
	 * Create a new container in world space wherein one can render new vector objects and return it. Note that all
	 * objects drawn into such a container should be expressed in world coordinates (the CRS of the map). These objects
	 * will also be automatically redrawn when the view port on the map changes.<br/>
	 * New containers are automatically drawn on top of all other containers - that includes both world and screen
	 * containers.
	 * </p>
	 * <p>
	 * WARNING: adding subgroups to the returned result does not work in IE !
	 * </p>
	 * 
	 * @return Returns the world vector container.
	 */
	VectorContainer addWorldContainer();

	/**
	 * Create a new container in screen space wherein one can render new vector objects and return it. Note that all
	 * objects drawn into such a container should be expressed in pixel coordinates. No matter how much the map moves or
	 * zooms, these objects will always remain on the same fixed position.<br/>
	 * New containers are automatically drawn on top of all other containers - that includes both world and screen
	 * containers.
	 * 
	 * @return Returns the screen vector container.
	 */
	VectorContainer addScreenContainer();

	/**
	 * Remove an existing vector container from the map. This can be either a world or a screen container.
	 * 
	 * @param container
	 *            The identifier of the container. If no such container exists, false will be returned.
	 * @return Was the removal successful or not?
	 */
	boolean removeVectorContainer(VectorContainer container);

	/**
	 * Bring an existing vector container to the front. This container must be a registered world or screen container.
	 * 
	 * @param container
	 *            The vector container to bring to the front. This container must be acquired either through the
	 *            <code>addScreenContainer</code> or the <code>addWorldContainer</code> methods.
	 * @return Could the container be successfully brought to the front or not?
	 */
	boolean bringToFront(VectorContainer container);

	/**
	 * Returns the layers model for this presenter. This model is the central layer handler for the map, with methods
	 * for getting layers, moving them up and down, adding or removing layers, ..
	 * 
	 * @return The layers model.
	 */
	LayersModel getLayersModel();

	/**
	 * Returns the {@link ViewPort} associated with this map. The view port regulates zooming and panning around the
	 * map, but also presents transformation methods for transforming vector objects between the different render
	 * spaces.
	 * 
	 * @return Returns the view port.
	 */
	ViewPort getViewPort();

	/**
	 * Get a service for feature searching/manipulation, specific for this map.
	 * 
	 * @return The feature service.
	 */
	FeatureService getFeatureService();

	/**
	 * Get the full set of currently active map gadgets.
	 * 
	 * @return The full set of currently active map gadgets.
	 */
	Set<MapGadget> getMapGadgets();

	/**
	 * Add a new gadget to the map. These gadgets are autonomous entities that can draw themselves in screen space, and
	 * react to certain events from the view port.
	 * 
	 * @param mapGadget
	 *            The new gadget to add to the map.
	 */
	void addMapGadget(MapGadget mapGadget);

	/**
	 * Remove a gadget from the map. These gadgets are autonomous entities that can draw themselves in screen space, and
	 * react to certain events from the view port.
	 * 
	 * @param mapGadget
	 *            The gadget to remove from the map.
	 * @return Was the removal successful or not?
	 */
	boolean removeMapGadget(MapGadget mapGadget);

	/**
	 * Apply a new {@link MapController} on the map. This controller will handle all mouse-events that are global for
	 * the map. Only one controller can be set at any given time. When a controller is active on the map, using this
	 * method, any fall-back controller is automatically disabled.
	 * 
	 * @param controller
	 *            The new {@link MapController} object. If null is passed, then the active controller is again disabled.
	 *            At that time the fall-back controller is again activated.
	 */
	void setMapController(MapController controller);

	/**
	 * Return the currently active controller on the map.
	 * 
	 * @return The currently active controller.
	 */
	MapController getMapController();

	/**
	 * Add a new listener to the map. These listeners passively listen to mouse events on the map, without actually
	 * interfering with these events. It is up to the developer to make sure these listeners do not interfere with the
	 * primary controller on the map. In this regard it is considered bad practice to let these listeners interfere with
	 * the mouse events.
	 * 
	 * @param mapListener
	 *            The listener to try and remove again.
	 * @return Returns true of removal was successful, false otherwise (i.e. if the listener could not be found).
	 */
	boolean addMapListener(MapController mapListener);

	/**
	 * Remove one of the currently active listeners on the map. These listeners passively listen to mouse events on the
	 * map, without actually interfering with these events. It is up to the developer to make sure these listeners do
	 * not interfere with the primary controller on the map. In this regard it is considered bad practice to let these
	 * listeners interfere with the mouse events.
	 * 
	 * @param mapListener
	 *            The listener to try and remove again.
	 * @return Returns true of removal was successful, false otherwise (i.e. if the listener could not be found).
	 */
	boolean removeMapListener(MapController mapListener);

	/**
	 * Get the currently active set of listeners on the map. These listeners passively listen to mouse events on the
	 * map, without actually interfering with these events. It is up to the developer to make sure these listeners do
	 * not interfere with the primary controller on the map. In this regard it is considered bad practice to let these
	 * listeners interfere with the mouse events.
	 * 
	 * @return Returns the full collection of currently active listeners.
	 */
	Collection<MapController> getMapListeners();

	/**
	 * Apply a new mouse cursor when hovering above the map.
	 * 
	 * @param cursor
	 *            The new cursor to apply.
	 */
	void setCursor(String cursor);

	/**
	 * Get the renderer for the map. This object will always make sure the map is correctly rendered.
	 * 
	 * @return The renderer for the map.
	 */
	MapRenderer getMapRenderer();

	/**
	 * Get an event parser specific for this map. This object can derive locations from mouse or touch events etc.
	 * 
	 * @return The event parser specific for this map.
	 */
	MapEventParser getMapEventParser();

	/**
	 * <p>
	 * Get the server-side configuration object associated with this map. This configuration only becomes available
	 * after the map has been successfully initialized, as this object first needs to be fetched from the server.
	 * </p>
	 * <p>
	 * Never change the values within this object!
	 * </p>
	 * 
	 * @return The server-side configuration object associated with this map.
	 */
	ClientMapInfo getConfiguration();
}