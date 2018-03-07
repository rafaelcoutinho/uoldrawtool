package edu.uol.drawing.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Shapes that cannot be filled because they are open circuit shapes.
 * 
 * @author coutinho
 *
 */
public abstract class OpenedShape implements OurShape, OutlineColorable {
	protected Color outLineColor;
	protected Point from;
	protected Point to;

	public OpenedShape(Color outline) {
		this.outLineColor = outline;
	}

	@Override
	public void startingPoint(Point point) {
		from = point;

	}

	protected void setColors(Graphics g) {
		g.setColor(outLineColor);
	}

	@Override
	public void updateSize(Point to) {
		this.to = to;
	}

	public abstract void drawIt(Graphics g);

	@Override
	public void setOutlineColor(Color c) {
		this.outLineColor = c;
	}

	@Override
	public Color getOutlineColor() {
		return outLineColor;
	}

}
