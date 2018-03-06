
package edu.uol.drawing;

import java.awt.*;
import java.awt.event.*;

import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;

import edu.uol.drawing.ShapeFactory.ShapeTypes;
import edu.uol.drawing.shapes.FillColorable;
import edu.uol.drawing.shapes.OurShape;
import edu.uol.drawing.shapes.OutlineColorable;
import edu.uol.drawing.shapes.Selectable;

public class UolDrawingTool extends JFrame {

	private static final String EXIT_MENU_LABEL = "Exit";
	private static final String SET_OUTLINE_COLOR_MENU_LABEL = "Set Outline Color...";
	private static final String SET_FILL_COLOR_MENU_LABEL = "Set Fill Color...";
	
	private static final String FILL_COLOR_LABEL = "Fill Color";
	private static final String OUTLINE_COLOR_LABEL = "Outline Color";
	
	
	private DrawingPanel panel;
	private Color fillColor = Color.GREEN;
	private Color outlineColor = Color.BLACK;
	
	
	private JButton fillColorButton;
	private JButton outlineColorButton;
	
	private JToolBar colorToolBar;

	public UolDrawingTool() {
		// set frame's title
		super("UolDrawingTool");
		// add menu
		addMenu();
		// add drawing panel
		addPanel();
		
		//add toolbar
		addToolBar();
		
		// add window listener
		this.addWindowListener(new WindowHandler());
		// set frame size
		this.setSize(600, 600);
		//center on screen
		this.setLocationRelativeTo(null);
		
		// make this frame visible
		this.setVisible(true);
	}

	private void setFillColor(Color color) {
		this.fillColor = color;
		
		//set button color too.
		fillColorButton.setBackground(color);
	}
	
	private void setOutlineColor(Color color) {
		this.outlineColor = color;
		
		outlineColorButton.setBackground(color);
	}
	
	private void addToolBar() {
		ButtonGroup shapeButtonGroup = new ButtonGroup();		
		
		fillColorButton = new JButton(SET_FILL_COLOR_MENU_LABEL);
		outlineColorButton = new JButton(SET_OUTLINE_COLOR_MENU_LABEL);			
				
		fillColorButton.setActionCommand(SET_FILL_COLOR_MENU_LABEL);
		outlineColorButton.setActionCommand(SET_OUTLINE_COLOR_MENU_LABEL);
		
		fillColorButton.addActionListener(new WindowHandler());
		outlineColorButton.addActionListener(new WindowHandler());
		
		colorToolBar = new JToolBar();
		
		for (int i = 0; i < ShapeTypes.values().length; i++) {				
			
			JRadioButton j1 = new JRadioButton(ShapeTypes.values()[i].name());
			j1.addActionListener(new WindowHandler());
			shapeButtonGroup.add(j1);		
			
			colorToolBar.add(j1);
		}
		
		
		//set default colors.
		setFillColor(Color.RED);
		setOutlineColor(Color.BLACK);
		
		colorToolBar.add(fillColorButton);
		colorToolBar.add(outlineColorButton);
		
		//add toolbar to the top of window.
		this.add(colorToolBar, BorderLayout.NORTH);
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
		for (int i = 0; i < ShapeTypes.values().length; i++) {
			shape.add(new MenuItem(ShapeTypes.values()[i].name())).addActionListener(new WindowHandler());
		}

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
		this.add(panel, BorderLayout.CENTER);
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
					setFillColor(newColor);
				}
			} else if (SET_OUTLINE_COLOR_MENU_LABEL.equals(e.getActionCommand())) {
				Color newColor = JColorChooser.showDialog(panel, "Choose Outline Color", outlineColor);
				if (newColor != null) {
					setOutlineColor(newColor);
					System.out.println(outlineColor.toString());
				}
			} else {

				String classSimpleName = e.getActionCommand();

				try {
					OurShape shape = ShapeFactory.getInstance(ShapeTypes.valueOf(classSimpleName), outlineColor,
							fillColor);

					panel.startDrawing(shape);
					if (shape instanceof OutlineColorable) {
						((OutlineColorable) shape).setOutlineColor(outlineColor);
					}
					if (shape instanceof FillColorable) {
						((FillColorable) shape).setFillColor(fillColor);
					}

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

	}

	OurShape currentShape = null;

	public void startDrawing(OurShape shape) {
		currentShape = shape;

	}

	// define mouse handler
	public void mouseClicked(MouseEvent e) {
		
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
				if (currentShape != null) {
					currentShape.updateSize(lastDraggedPoint);
				}
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
