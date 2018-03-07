package edu.uol.drawing.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Abstract class for shapes that can be filled and are closed circuit
 * 
 * @author coutinho
 *
 */
public abstract class ClosedShape implements OurShape, OutlineColorable, FillColorable {
	protected Color outLineColor;
	protected Color fillColor;
	protected Point from;
	protected Point to;

	public ClosedShape(Color outline, Color fillcolor) {
		this.outLineColor = outline;
		this.fillColor = fillcolor;
	}

	@Override
	public void startingPoint(Point point) {
		from = point;
	}

	@Override
	public void updateSize(Point point) {
		this.to = point;
	}

	@Override
	public void drawIt(Graphics g) {
		int height = getHeight();
		int width = getWidth();
		Point topLeft = getTopLeft();
		g.setColor(fillColor);
		drawFilledshape(g, topLeft, width, height);
		g.setColor(outLineColor);
		drawOutline(g, topLeft, width, height);

	}

	protected int getWidth() {
		return Math.abs(to.x - from.x);
	}

	protected int getHeight() {
		return Math.abs(to.y - from.y);
	}

	protected Point getTopLeft() {
		Point topLeft = new Point(from.x < to.x ? from.x : to.x, from.y < to.y ? from.y : to.y);
		return topLeft;
	}

	protected abstract void drawOutline(Graphics g, Point topLeft, int width, int height);

	protected abstract void drawFilledshape(Graphics g, Point topLeft, int width, int height);

	@Override
	public void setOutlineColor(Color c) {
		this.outLineColor = c;
	}

	@Override
	public Color getOutlineColor() {
		return outLineColor;
	}

	@Override
	public void setFillColor(Color c) {
		this.fillColor = c;
	}

	@Override
	public Color getFillColor() {
		return fillColor;
	}

}
