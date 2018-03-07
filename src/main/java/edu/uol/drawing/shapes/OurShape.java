package edu.uol.drawing.shapes;

import java.awt.Graphics;
import java.awt.Point;

/**
 * Interface for all shape types
 * 
 * @author coutinho
 *
 */
public interface OurShape {

	public abstract void startingPoint(Point point);

	public abstract void updateSize(Point point) throws IllegalStateException;

	public abstract void drawIt(Graphics g);

}
