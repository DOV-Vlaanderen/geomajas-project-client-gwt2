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
package org.geomajas.gwt2.client.animation;

import java.util.logging.Logger;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.View;

/**
 * Trajectory for kinetic effect after panning.
 * 
 * @author Jan De Moerloose
 *
 */
public class KineticTrajectory implements Trajectory {

	private static Logger logger = Logger.getLogger("MapPresenterImpl");

	private View beginView;

	private double duration;

	private double initialSpeed;

	private double distance;

	private Coordinate direction;

	private double decay = -0.005;

	private double minVelocity = 0.05;

	private double delay = 100;

	/**
	 * Constructs a trajectory.
	 * 
	 * @param beginView
	 * @param direction
	 * @param initialSpeed
	 */
	public KineticTrajectory(View beginView, Coordinate direction, double initialSpeed) {
		this.initialSpeed = initialSpeed;
		this.duration = Math.log(minVelocity / this.initialSpeed) / this.decay;
		this.distance = (this.minVelocity - this.initialSpeed) / this.decay;
		this.beginView = beginView;
		this.direction = normalize(direction);
	}

	@Override
	public View getView(double progress) {
		logger.info("kinetics initialSpeed=" + initialSpeed);
		double easing = initialSpeed * (Math.exp((decay * progress) * duration) - 1) / (minVelocity - initialSpeed);
		double ds = distance * easing;
		if (Math.abs(((int) (progress * 10) - progress * 10)) < 0.01) {
			logger.info("kinetics " + progress + ",ds=" + ds);
		}
		Coordinate c = add(beginView.getPosition(), mult(direction, ds));
		return new View(c, beginView.getResolution());
	}

	public double getDuration() {
		return duration;
	}

	private Coordinate add(Coordinate c1, Coordinate c2) {
		return new Coordinate(c2.getX() + c1.getX(), c2.getY() + c1.getY());
	}

	private Coordinate mult(Coordinate c, double factor) {
		return new Coordinate(factor * c.getX(), factor * c.getY());
	}

	private Coordinate normalize(Coordinate c) {
		double d = Math.sqrt(c.getX() * c.getX() + c.getY() * c.getY());
		return new Coordinate(c.getX() / d, c.getY() / d);
	}

}
