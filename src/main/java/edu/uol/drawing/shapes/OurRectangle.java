package edu.uol.drawing.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.RectangularShape;

/**
 * A shape that is a rectangle. It implements rotation
 * 
 * @author coutinho
 *
 */
public class OurRectangle extends ClosedShape implements Selectable, Rotatable {

	private double angle;

	public OurRectangle(Color outline, Color fillcolor) {
		super(outline, fillcolor);
	}

	protected void drawFilledshape(Graphics g, Point topLeft, int width, int height) {

		Graphics2D g2d = rotateGraphics(g, topLeft, width, height, angle);
		g2d.fillRect(getTopLeft().x, getTopLeft().y, width, height);
		rotateGraphics(g, topLeft, width, height, -angle);
	}

	protected void drawOutline(Graphics g, Point topLeft, int width, int height) {
		Graphics2D g2d = rotateGraphics(g, topLeft, width, height, angle);
		g2d.drawRect(getTopLeft().x, getTopLeft().y, width, height);
		rotateGraphics(g, topLeft, width, height, -angle);
	}

	private Graphics2D rotateGraphics(Graphics g, Point topLeft, int width, int height, double toAngle) {

		Graphics2D g2d = (Graphics2D) g;
		double centerX = topLeft.getX() + width / 2;
		double centerY = topLeft.getY() + height / 2;
		g2d.rotate(Math.toRadians(toAngle), centerX, centerY);
		return g2d;

	}

	@Override
	public RectangularShape getBounds() {
		RectangularShape shape = new Rectangle(getTopLeft().x, getTopLeft().y, getWidth(), getHeight());

		return shape;
	}

	@Override
	public void rotateClockwise() {
		angle += 45;
	}

}
