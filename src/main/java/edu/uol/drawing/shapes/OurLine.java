package edu.uol.drawing.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.RectangularShape;

public class OurLine extends OpenedShape {

	public OurLine(Color outline) {
		super(outline);
	}

	@Override
	public void drawIt(Graphics g) {
		setColors(g);
		g.drawLine(from.x, from.y, to.x, to.y);
	}
}