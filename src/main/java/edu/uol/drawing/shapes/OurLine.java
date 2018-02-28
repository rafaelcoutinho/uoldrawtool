package edu.uol.drawing.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OurLine implements OurShape, OutlineColorable {
	private Graphics g;
	private Color color;
	private Point from;
	private Point to;

	public OurLine(Graphics g) {
		this.g = g;
	}

	@Override
	public int getMinNumberOfPoints() {
		return 2;
	}

	@Override
	public int getMaxNumberOfPoints() {
		return 2;
	}

	@Override
	public void startingPoint(Point point) {
		from = point;

	}

	@Override
	public void updateSize(Point to) {
		g.setColor(color);
		g.drawLine(from.x, from.y, to.x, to.y);

	}

	@Override
	public void packShape(Point to) {
		this.to = to;

	}

	@Override
	public void drawIt(Graphics g) {
		g.setColor(color);
		g.drawLine(from.x, from.y, to.x, to.y);
	}

	@Override
	public void setOutlineColor(Color c) {
		this.color = c;
	}

	@Override
	public Color getOutlineColor() {
		return color;
	}

}
