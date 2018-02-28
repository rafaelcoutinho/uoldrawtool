package edu.uol.drawing.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;

public class OurRectangle implements OurShape, OutlineColorable, FillColorable {

	private Color fill;
	private Color outline;

	public OurRectangle() {
	}

	@Override
	public void setFillColor(Color c) {
		this.fill = c;
	}

	@Override
	public Color getFillColor() {
		return fill;
	}

	@Override
	public void setOutlineColor(Color c) {
		this.outline = c;
	}

	@Override
	public Color getOutlineColor() {
		return outline;
	}

	@Override
	public int getMinNumberOfPoints() {
		return 4;
	}

	@Override
	public int getMaxNumberOfPoints() {
		return 4;
	}

	@Override
	public void drawIt(Graphics g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startingPoint(Point point) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSize(Point point) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void packShape(Point point) {
		// TODO Auto-generated method stub
		
	}

}
