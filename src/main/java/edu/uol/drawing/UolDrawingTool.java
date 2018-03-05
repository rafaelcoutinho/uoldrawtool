
package edu.uol.drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JColorChooser;

import edu.uol.drawing.shapes.ClosedShape;
import edu.uol.drawing.shapes.FillColorable;
import edu.uol.drawing.shapes.OpenedShape;
import edu.uol.drawing.shapes.OurArc;
import edu.uol.drawing.shapes.OurLine;
import edu.uol.drawing.shapes.OurRectangle;
import edu.uol.drawing.shapes.OurRoundRectangle;
import edu.uol.drawing.shapes.OurShape;
import edu.uol.drawing.shapes.OutlineColorable;
import edu.uol.drawing.shapes.Selectable;

public class UolDrawingTool extends Frame {

	private static final String EXIT_MENU_LABEL = "Exit";
	private static final String SET_OUTLINE_COLOR_MENU_LABEL = "Set Outline Color...";
	private static final String SET_FILL_COLOR_MENU_LABEL = "Set Fill Color...";
	private DrawingPanel panel;
	private Color fillColor = Color.GREEN;
	private Color outlineColor = Color.BLACK;

	public UolDrawingTool() {
		// set frame's title
		super("UolDrawingTool");
		// add menu
		addMenu();
		// add drawing panel
		addPanel();
		// add window listener
		this.addWindowListener(new WindowHandler());
		// set frame size
		this.setSize(400, 400);
		// make this frame visible
		this.setVisible(true);
	}

	/**
	 * This method creates menu bar and menu items and then attach the menu bar with
	 * the frame of this drawing tool.
	 */
	private void addMenu() {
		// Add menu bar to our frame
		MenuBar menuBar = new MenuBar();
		Menu file = new Menu("File");
		Menu colors = new Menu("Colors");
		Menu shape = new Menu("Our Shapes");
		file.add(new MenuItem(EXIT_MENU_LABEL)).addActionListener(new WindowHandler());
		colors.add(new MenuItem(SET_OUTLINE_COLOR_MENU_LABEL)).addActionListener(new WindowHandler());
		colors.add(new MenuItem(SET_FILL_COLOR_MENU_LABEL)).addActionListener(new WindowHandler());

		shape.add(new MenuItem(OurLine.class.getSimpleName())).addActionListener(new WindowHandler());
		shape.add(new MenuItem(OurArc.class.getSimpleName())).addActionListener(new WindowHandler());
		shape.add(new MenuItem(OurRectangle.class.getSimpleName())).addActionListener(new WindowHandler());
		shape.add(new MenuItem(OurRoundRectangle.class.getSimpleName())).addActionListener(new WindowHandler());

		menuBar.add(file);
		menuBar.add(colors);
		menuBar.add(shape);
		if (null == this.getMenuBar()) {
			this.setMenuBar(menuBar);
		}
	}// addMenu()

	/**
	 * This method adds a panel to SimpleDrawingTool for drawing shapes.
	 */
	private void addPanel() {
		panel = new DrawingPanel();
		Dimension d = this.getSize();
		// get insets of frame
		Insets ins = this.insets();
		// exclude insets from the size of the panel
		d.height = d.height - ins.top - ins.bottom;
		d.width = d.width - ins.left - ins.right;
		panel.setSize(d);
		panel.setLocation(ins.left, ins.top);
		panel.setBackground(Color.white);
		// add mouse listener. Panel itself will be handling mouse events
		panel.addMouseListener(panel);
		panel.addMouseMotionListener(panel);
		this.add(panel);
	}

	private class WindowHandler extends WindowAdapter implements ActionListener {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}

		public void actionPerformed(ActionEvent e) {
			if (EXIT_MENU_LABEL.equals(e.getActionCommand())) {
				System.exit(0);
			} else if (SET_FILL_COLOR_MENU_LABEL.equals(e.getActionCommand())) {
				Color newColor = JColorChooser.showDialog(panel, "Choose Fill Color", fillColor);
				if (newColor != null) {
					fillColor = newColor;
				}
			} else if (SET_OUTLINE_COLOR_MENU_LABEL.equals(e.getActionCommand())) {
				Color newColor = JColorChooser.showDialog(panel, "Choose Outline Color", outlineColor);
				if (newColor != null) {
					outlineColor = newColor;
					System.out.println(outlineColor.toString());
				}
			} else {

				String classSimpleName = e.getActionCommand();

				Class shapeClass = null;
				try {
					shapeClass = Class.forName("edu.uol.drawing.shapes." + classSimpleName);

					OurShape shape = null;
					if (OpenedShape.class.isAssignableFrom(shapeClass)) {// checks if shape class is subclass of
																			// open
																			// shape

						shape = (OurShape) shapeClass.getConstructor(Color.class).newInstance(outlineColor);

					} else if (ClosedShape.class.isAssignableFrom(shapeClass)) {
						shape = (OurShape) shapeClass.getConstructor(Color.class, Color.class).newInstance(outlineColor,
								fillColor);
					} else {
						// TODO show error
					}
					panel.startDrawing(shape);
					if (shape instanceof OutlineColorable) {
						((OutlineColorable) shape).setOutlineColor(outlineColor);
					}
					if (shape instanceof FillColorable) {
						((FillColorable) shape).setFillColor(fillColor);
					}

				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
					// TODO show error
				} catch (Exception e1) {
					e1.printStackTrace();

				}

			}
		}
	}
}

class DrawingPanel extends Panel implements MouseListener, MouseMotionListener {

	private static final int MIN_DISTANCE_TO_REDRAW = 5;
	private List<OurShape> drawedShapes = new ArrayList<>();

	// override panel paint method to draw shapes
	// watch out, repaint is done in separate thread
	public void paint(Graphics g) {

		for (Iterator iterator = drawedShapes.iterator(); iterator.hasNext();) {
			OurShape ourShape = (OurShape) iterator.next();
			ourShape.drawIt(g);
		}
		if (currentShape != null) {
			currentShape.updateSize(g, lastDraggedPoint);
		}
	}

	OurShape currentShape = null;

	public void startDrawing(OurShape shape) {
		currentShape = shape;

	}

	// define mouse handler
	public void mouseClicked(MouseEvent e) {
		if (currentShape == null) {
			OurShape toDelete = null;
			for (Iterator iterator = drawedShapes.iterator(); iterator.hasNext();) {
				OurShape ourShape = (OurShape) iterator.next();
				if (ourShape instanceof Selectable) {
					if (((Selectable) ourShape).getBounds().contains(e.getPoint())) {
						toDelete = ourShape;
						break;
					}
				}
			}
			if (toDelete != null) {
				// TODO Damian please put a confirm option here.
				boolean a = drawedShapes.remove(toDelete);
				RectangularShape bounds = ((Selectable) toDelete).getBounds();
				repaint((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth() + 1,
						(int) bounds.getHeight() + 1);
			}

		}
	}// mouseClicked

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	boolean startedDrawing = false;

	public void mousePressed(MouseEvent e) {
		if (!startedDrawing && currentShape != null) {
			startedDrawing = true;
			startingPoint = e.getPoint();
			currentShape.startingPoint(e.getPoint());
		}
	}// mousePressed

	public void mouseReleased(MouseEvent e) {
		if (startedDrawing) {
			startedDrawing = false;
			drawedShapes.add(currentShape);
			repaint();
			currentShape = null;
		}
	}// mouseReleased

	Point lastDraggedPoint = null;
	Point startingPoint = null;

	@Override
	public void mouseDragged(MouseEvent e) {

		if (startedDrawing && currentShape != null) {
			if (lastDraggedPoint == null || e.getPoint().distance(lastDraggedPoint) > MIN_DISTANCE_TO_REDRAW) {
				lastDraggedPoint = e.getPoint();
				// TODO repaint only the boundary of the current shape
				repaint();// watch out, repaint is done in separate thread

			}

		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// no need, only draw when mouse is dragged
	}
}//
