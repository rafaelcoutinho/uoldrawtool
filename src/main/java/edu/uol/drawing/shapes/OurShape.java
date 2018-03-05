package edu.uol.drawing.shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

public interface OurShape {

	public abstract void startingPoint(Point point);

	public abstract void updateSize(Graphics g, Point point);

	public abstract void drawIt(Graphics g);

}
