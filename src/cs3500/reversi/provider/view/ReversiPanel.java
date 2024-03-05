package cs3500.reversi.provider.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import cs3500.reversi.provider.model.Disc;
import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.ReversiReadOnlyModel;

/**
 * To represent a Reversi Panel, a subclass of JPanel. This class
 * creates a visual display of a hexagon reversi board,
 * and handles mouse clicks and key presses.
 */
public class ReversiPanel extends JPanel {
  private final ReversiReadOnlyModel model;
  private final Map<Hex, Point2D> hexCenters;
  private final List<ViewFeatures> featuresListeners;
  private Optional<Hex> selectedHex;
  private boolean allowInput = false;

  /**
   * Constructor used to create an instead of a Reversi Panel.
   * @param model read only model
   */
  public ReversiPanel(ReversiReadOnlyModel model) {
    this.model = model;
    this.hexCenters = new HashMap<>();
    this.selectedHex = Optional.empty();
    this.featuresListeners = new ArrayList<>();

    MouseEventsListener mouseListener = new MouseEventsListener();
    this.addMouseListener(mouseListener);

    KeyEventsListener keyListener = new KeyEventsListener();
    this.addKeyListener(keyListener);
    this.setFocusable(true);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    this.setBackground(Color.DARK_GRAY);

    Graphics2D g2d = (Graphics2D) g;
    double hexSize = this.getHexSize();

    // saves the old color and transform
    Color oldColor = g2d.getColor();
    AffineTransform oldTransform = g2d.getTransform();

    // translates the panel so the center is (0,0)
    g2d.translate(this.getWidth() / 2, this.getHeight() / 2);


    this.drawBoard(hexSize, g2d);

    // sets the color and transform back to the original
    g2d.setColor(oldColor);
    g2d.setTransform(oldTransform);
    this.initHexCenters();
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(570, 510);
  }

  private void drawBoard(double hexSize, Graphics2D g2d) {
    Set<Hex> board = model.getBoard().keySet();
    for (Hex hex : board) {

      // calculates the center of the hex
      Point2D center = hex.hexToPixel(hexSize);

      // draws a path of the hex
      StandardHexagon drawnHex = new StandardHexagon(center.getX(), center.getY(), hexSize);

      if (selectedHex.isPresent() && hex.equals(selectedHex.get())) {
        g2d.setColor(Color.CYAN); // sets the color of a selected hex to cyan if it exists
      } else {
        g2d.setColor(Color.LIGHT_GRAY); // sets all other hexes to light gray
      }
      g2d.fill(drawnHex);

      // outlines the hex in black
      g2d.setColor(Color.BLACK);
      g2d.draw(drawnHex);

      this.drawDisc(hex, g2d, center, hexSize);
    }
  }

  // draws a disc
  private void drawDisc(Hex hex, Graphics2D g2d, Point2D center, double hexSize) {
    if (model.getBoard().get(hex) == Disc.BLACK) {
      // draw black oval of radius hexSize / 2
      g2d.setColor(Color.BLACK);
      g2d.fillOval((int)(center.getX() - hexSize / 2),
              (int)(center.getY() - hexSize / 2),
              (int) hexSize,
              (int) hexSize);
    } else if (model.getBoard().get(hex) == Disc.WHITE) {
      // draw white oval of radius hexSize / 2
      g2d.setColor(Color.WHITE);
      g2d.fillOval((int)(center.getX() - hexSize / 2),
              (int)(center.getY() - hexSize / 2),
              (int) hexSize,
              (int) hexSize);
    }
  }

  private double getHexSize() {
    // calculates the maximum height of a hexagon that will fit the panel
    double maxHexHeight = this.getHeight()
            / (2 * model.getBoardSize() + (model.getBoardSize()) + 2.0);

    // calculates the maximum hex width of a hexagon that will fit the panel
    double maxHexWidth = this.getWidth() / (Math.sqrt(3) * (model.getBoardSize() * 2 + 1));

    // uses the min of the max height and max width so the board will always fit the panel

    return Math.min(maxHexWidth,
            maxHexHeight);
  }

  // initializes a map of a hex coordinate to the physical hex center coordinate.
  private void initHexCenters() {
    double hexSize = this.getHexSize();
    Set<Hex> board = model.getBoard().keySet();
    for (Hex hex : board) {
      Point2D center = hex.hexToPixel(hexSize);
      this.hexCenters.put(hex, center);

    }
  }

  // transforms a physical point to its logical point
  private Point2D transformPhysicalToLogical(Point2D p) {
    AffineTransform inverseTransform = new AffineTransform();
    inverseTransform.translate(-this.getWidth() / 2., -this.getHeight() / 2.);

    Point2D transformedPoint = new Point2D.Double();
    inverseTransform.transform(p, transformedPoint);
    return transformedPoint;
  }

  /**
   * Adds the view features to a features listener list.
   *
   * @param features given feature
   */
  public void addFeaturesListener(ViewFeatures features) {
    this.featuresListeners.add(Objects.requireNonNull(features));
  }

  // handles the mouse clicks, highlighting and deselecting a hexagon
  private class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      if (!allowInput) {
        return;
      }

      Point2D clickedPoint = e.getPoint();
      Point2D transformedPoint = transformPhysicalToLogical(clickedPoint);

      boolean hexagonPressed;
      hexagonPressed = this.isHexagonPressed(transformedPoint);

      if (!hexagonPressed) {
        selectedHex = Optional.empty();
      }

      repaint();
    }

    // determines if a hexagon has been pressed given a point clicked
    private boolean isHexagonPressed(Point2D transformedPoint) {
      boolean isHexagonPressed = false;
      double hexSize = getHexSize();

      for (Map.Entry<Hex, Point2D> entry : hexCenters.entrySet()) {
        Hex hex = entry.getKey();
        Point2D center = entry.getValue();

        StandardHexagon hexClicked = new StandardHexagon(center.getX(), center.getY(), hexSize);

        if (hexClicked.contains(transformedPoint)) {
          isHexagonPressed = true;
          if (selectedHex.isPresent() && hex.equals(selectedHex.get())) {
            selectedHex = Optional.empty();
          } else {
            selectedHex = Optional.of(hex);
          }
          break;
        }
      }
      return isHexagonPressed;
    }

  }

  /**
   * Enables user interaction with this panel.
   */
  public void enableInput() {
    allowInput = true;
  }

  /**
   * Disables any user interaction with this panel.
   */
  public void disableInput() {
    allowInput = false;
  }


  // handles key events, passing, moving, and quitting
  private class KeyEventsListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      if (!allowInput) {
        return;
      }

      switch (e.getKeyCode()) {
        case KeyEvent.VK_P:
          for (ViewFeatures listener : featuresListeners) {
            listener.pass();
          }
          break;

        case KeyEvent.VK_ENTER:
          if (selectedHex.isPresent()) {
            Hex hex = selectedHex.get();
            for (ViewFeatures listener : featuresListeners) {
              listener.makeMove(hex);
            }
          }
          break;

        case KeyEvent.VK_Q:
          for (ViewFeatures listener : featuresListeners) {
            listener.quit();
          }
          break;
        default:
          break;
      }
    }

  }

}