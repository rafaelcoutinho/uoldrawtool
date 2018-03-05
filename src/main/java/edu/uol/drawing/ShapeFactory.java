package edu.uol.drawing;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;

import edu.uol.drawing.shapes.ClosedShape;
import edu.uol.drawing.shapes.OpenedShape;
import edu.uol.drawing.shapes.OurArc;
import edu.uol.drawing.shapes.OurLine;
import edu.uol.drawing.shapes.OurRectangle;
import edu.uol.drawing.shapes.OurRoundRectangle;
import edu.uol.drawing.shapes.OurShape;

public abstract class ShapeFactory {
	public enum ShapeTypes {
		Line(OurLine.class), Arc(OurArc.class), Rectangle(OurRectangle.class), RoundedRectangle(
				OurRoundRectangle.class);
		private Class clazz;

		private ShapeTypes(Class clazz) {
			this.clazz = clazz;
		}
	}

	public static OurShape getInstance(ShapeTypes valueOf, Color outlineColor, Color fillColor)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		OurShape shape = null;
		Class shapeClass = valueOf.clazz;
		if (OpenedShape.class.isAssignableFrom(shapeClass)) {// checks if shape class is subclass of
																// open
																// shape

			shape = (OurShape) shapeClass.getConstructor(Color.class).newInstance(outlineColor);

		} else if (ClosedShape.class.isAssignableFrom(shapeClass)) {
			shape = (OurShape) shapeClass.getConstructor(Color.class, Color.class).newInstance(outlineColor, fillColor);
		} else {
			throw new IllegalArgumentException("Unknown shape");
		}
		return shape;
	}

}
