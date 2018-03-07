package edu.uol.drawing.shapes;

import java.awt.Color;
import java.awt.Graphics;
/**
 * A shape that is an arc
 * 
 * @author coutinho
 *
 */
public class OurArc extends OpenedShape {

	public OurArc(Color outline) {
		super(outline);
	}

	@Override
	public void drawIt(Graphics g) {
		setColors(g);
		int height = to.y - from.y;
		int width = to.y - from.y;
		g.drawArc(from.x, from.y, Math.abs(width), Math.abs(height), 0, height < 0 ? -90 : 90);
	}

}
