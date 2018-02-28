package edu.uol.drawing.shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;

public interface OurShape {
	public abstract int getMinNumberOfPoints();

	public abstract int getMaxNumberOfPoints();

	public abstract void startingPoint(Point point);

	public abstract void updateSize(Point point);

	public abstract void packShape(Point point);

	public abstract void drawIt(Graphics g);

}
