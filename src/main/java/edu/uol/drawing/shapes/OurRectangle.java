package edu.uol.drawing.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.RectangularShape;

public class OurRectangle extends ClosedShape implements Selectable, Rotatable {

	private double angle;

	public OurRectangle(Color outline, Color fillcolor) {
		super(outline, fillcolor);
	}

	protected void drawFilledshape(Graphics g, Point topLeft, int width, int height) {
		g.fillRect(getTopLeft().x, getTopLeft().y, width, height);

	}

	protected void drawOutline(Graphics g, Point topLeft, int width, int height) {
		Graphics2D g2d = (Graphics2D) g;
		double centerX = getTopLeft().getX() + getWidth() / 2;
		double centerY = getTopLeft().getY() + getHeight() / 2;
		g2d.rotate(Math.toRadians(angle), centerX, centerY);
		g2d.drawRect(getTopLeft().x, getTopLeft().y, width, height);
		g2d.rotate(Math.toRadians(-angle), centerX, centerY);
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

	@Override
	public void rotateCounterClockwise() {
		angle -= 45;
	}

}
