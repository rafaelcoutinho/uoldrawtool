package edu.uol.drawing.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.RectangularShape;

public class OurRoundRectangle extends ClosedShape implements Selectable, Erasable {

	public OurRoundRectangle(Color outline, Color fillcolor) {
		super(outline, fillcolor);
	}

	protected void drawFilledshape(Graphics g, Point topLeft, int width, int height) {
		g.fillRoundRect(topLeft.x, topLeft.y, width, height, (width / 10), (height / 10));

	}

	protected void drawOutline(Graphics g, Point topLeft, int width, int height) {
		g.drawRoundRect(topLeft.x, topLeft.y, width, height, (width / 10), (height / 10));

	}

	@Override
	public RectangularShape getBounds() {
		RectangularShape shape = new Rectangle(getTopLeft().x, getTopLeft().y, getWidth(), getHeight());
		return shape;
	}
}
