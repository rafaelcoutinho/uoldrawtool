
package edu.uol.drawing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;

import edu.uol.drawing.ShapeFactory.ShapeTypes;
import edu.uol.drawing.shapes.Erasable;
import edu.uol.drawing.shapes.FillColorable;
import edu.uol.drawing.shapes.OurShape;
import edu.uol.drawing.shapes.OutlineColorable;
import edu.uol.drawing.shapes.Rotatable;
import edu.uol.drawing.shapes.Selectable;

/**
 * JFrame with the drawing pane. It also packs the top menu and the toolbar
 * 
 * @author coutinho
 *
 */
public class UolDrawingTool extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String EXIT_MENU_LABEL = "Exit";
	private static final String SET_OUTLINE_COLOR_MENU_LABEL = "Set Outline Color...";
	private static final String SET_FILL_COLOR_MENU_LABEL = "Set Fill Color...";

	private DrawingPanel panel;
	private Color fillColor = Color.GREEN;
	private Color outlineColor = Color.BLACK;

	private JButton fillColorButton;
	private JButton outlineColorButton;

	private JToolBar colorToolBar;

	public UolDrawingTool() {
		super("UolDrawingTool - Chambers and Coutinho");
		addMenu();
		addPanel();
		addToolBar();

		// add window listener
		this.addWindowListener(new WindowHandler());
		this.setSize(600, 600);
		this.setLocationRelativeTo(null);

		this.setVisible(true);
	}

	private void setFillColor(Color color) {
		this.fillColor = color;

		// set button color too.
		updateColorButton(fillColorButton, color);
	}

	private void updateColorButton(JButton btn, Color color) {
		btn.setBackground(color);
		if (isColorDark(color)) {
			btn.setForeground(Color.WHITE);
		} else {
			btn.setForeground(Color.BLACK);
		}
	}

	public boolean isColorDark(Color color) {

		double darkness = 1 - (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
		if (darkness < 0.5) {
			return false; // It's a light color
		} else {
			return true; // It's a dark color
		}
	}

	private void setOutlineColor(Color color) {
		this.outlineColor = color;
		updateColorButton(outlineColorButton, color);
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

		// set default colors.
		setFillColor(fillColor);
		setOutlineColor(outlineColor);

		colorToolBar.add(fillColorButton);
		colorToolBar.add(outlineColorButton);

		// add toolbar to the top of window.
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

	JPopupMenu jpopup = new JPopupMenu();

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
		panel.setCurrentOutlineColor(outlineColor);
		panel.setCurrentFillColor(fillColor);
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
					panel.setCurrentFillColor(newColor);
				}
			} else if (SET_OUTLINE_COLOR_MENU_LABEL.equals(e.getActionCommand())) {
				Color newColor = JColorChooser.showDialog(panel, "Choose Outline Color", outlineColor);
				if (newColor != null) {
					setOutlineColor(newColor);
					panel.setCurrentOutlineColor(newColor);
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
	private static final String UPDATE_FILL_COLOR = "Update fill color";
	private static final String UPDATE_OUTLINE_COLOR = "Update outline color";
	private static final String ROTATE_CLOCKWISE = "Rotate clockwise";
	private static final String DELETE_SHAPE = "Delete shape";
	private Color currentOutlineColor;
	private Color currentFillColor;
	private JPopupMenu popup;
	private JMenuItem deleteitem;
	private JMenuItem rotateitem;
	private JMenuItem updateOutlineitem;
	private JMenuItem updateFillitem;
	private OurShape selectedShape;

	public DrawingPanel() {
		popup = new JPopupMenu();
		/**
		 * Action Listener for the popup menu. It handles the actions made to the shapes
		 */
		ActionListener menuListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (selectedShape != null) {
					if (DELETE_SHAPE.equals(event.getActionCommand())) {
						// going to delete selected
						if (JOptionPane.showOptionDialog(null, "Delete this shape?", "Delete Shape",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) != 1) {
							boolean a = drawedShapes.remove(selectedShape);
							RectangularShape bounds = ((Selectable) selectedShape).getBounds();
							repaint((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth() + 1,
									(int) bounds.getHeight() + 1);
						}
					} else if (ROTATE_CLOCKWISE.equals(event.getActionCommand())) {

						((Rotatable) selectedShape).rotateClockwise();
						repaint();// repaint the rotated section

					} else if (UPDATE_FILL_COLOR.equals(event.getActionCommand())) {
						((FillColorable) selectedShape).setFillColor(currentFillColor);

					} else if (UPDATE_OUTLINE_COLOR.equals(event.getActionCommand())) {
						((OutlineColorable) selectedShape).setOutlineColor(currentOutlineColor);

					}
					RectangularShape bounds = ((Selectable) selectedShape).getBounds();
					repaint((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth() + 1,
							(int) bounds.getHeight() + 1);
					selectedShape = null;
				}
			}
		};

		updateOutlineitem = new JMenuItem(UPDATE_OUTLINE_COLOR);
		updateOutlineitem.setHorizontalTextPosition(JMenuItem.RIGHT);
		popup.add(updateOutlineitem);
		updateOutlineitem.addActionListener(menuListener);

		updateFillitem = new JMenuItem(UPDATE_FILL_COLOR);
		updateFillitem.setHorizontalTextPosition(JMenuItem.RIGHT);
		popup.add(updateFillitem);
		updateFillitem.addActionListener(menuListener);

		deleteitem = new JMenuItem(DELETE_SHAPE);
		deleteitem.setHorizontalTextPosition(JMenuItem.RIGHT);
		popup.add(deleteitem);
		deleteitem.addActionListener(menuListener);

		popup.add(rotateitem = new JMenuItem(ROTATE_CLOCKWISE));
		rotateitem.setHorizontalTextPosition(JMenuItem.RIGHT);
		rotateitem.addActionListener(menuListener);

		popup.addSeparator();

		popup.setLabel("Justification");
		popup.setBorder(new BevelBorder(BevelBorder.RAISED));
		// popup.addPopupMenuListener(new PopupPrintListener());

		addMouseListener(this);
	}

	public void setCurrentOutlineColor(Color newColor) {
		currentOutlineColor = newColor;

	}

	public void setCurrentFillColor(Color newColor) {
		currentFillColor = newColor;

	}

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
			currentShape.drawIt(g);
		}

	}

	OurShape currentShape = null;

	public void startDrawing(OurShape shape) {
		currentShape = shape;

	}

	// define mouse handler
	public void mouseClicked(MouseEvent e) {
		if (currentShape == null) {
			checkShowpopup(e);

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
		} else {
			checkShowpopup(e);
		}
	}// mousePressed

	private void checkShowpopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			selectedShape = null;

			for (Iterator iterator = drawedShapes.iterator(); iterator.hasNext();) {
				OurShape ourShape = (OurShape) iterator.next();
				if (ourShape instanceof Selectable) {

					if (((Selectable) ourShape).getBounds().contains(e.getPoint())) {
						selectedShape = ourShape;
						break;
					}
				}
			}

			if (selectedShape != null) {
				if (selectedShape instanceof OutlineColorable) {
					updateOutlineitem.setEnabled(true);
				} else {
					updateOutlineitem.setEnabled(false);
				}
				if (selectedShape instanceof FillColorable) {
					updateFillitem.setEnabled(true);
				} else {
					updateFillitem.setEnabled(false);
				}
				if (selectedShape instanceof Erasable) {
					deleteitem.setEnabled(true);
				} else {
					deleteitem.setEnabled(false);
				}
				if (selectedShape instanceof Rotatable) {
					rotateitem.setEnabled(true);
				} else {
					rotateitem.setEnabled(false);
				}
				popup.show(DrawingPanel.this, e.getX(), e.getY());
			}
		}

	}

	public void mouseReleased(MouseEvent e) {

		if (startedDrawing) {
			startedDrawing = false;
			drawedShapes.add(currentShape);
			repaint();
			currentShape = null;
		} else {
			checkShowpopup(e);
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
				repaint();// watch out, repaint may be heavy

			}

		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// no need, only draw when mouse is dragged
	}
}
