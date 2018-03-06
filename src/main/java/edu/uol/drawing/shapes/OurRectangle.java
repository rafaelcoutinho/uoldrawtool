package edu.uol.drawing.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.RectangularShape;

public class OurRectangle extends ClosedShape implements Selectable {

	public OurRectangle(Color outline, Color fillcolor) {
		super(outline, fillcolor);
	}

	protected void drawFilledshape(Graphics g, Point topLeft, int width, int height) {
		g.fillRect(topLeft.x, topLeft.y, width, height);

	}

	protected void drawOutline(Graphics g, Point topLeft, int width, int height) {
		g.drawRect(topLeft.x, topLeft.y, width, height);

	}

	@Override
	public RectangularShape getBounds() {
		RectangularShape shape = new Rectangle(getTopLeft().x, getTopLeft().y, getWidth(), getHeight());
		return shape;
	}

}
