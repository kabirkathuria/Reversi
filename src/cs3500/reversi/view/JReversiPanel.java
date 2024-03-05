package cs3500.reversi.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import cs3500.reversi.controller.PlayerActions;
import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;

/**
 * Represents the graphical user interface (GUI) view of the Reversi game.
 * Implements the ReversiView interface to provide rendering capabilities for the game state.
 */
public class JReversiPanel extends AbstractPanel {
  // the model being drawn
  protected final ObservableReversiModel model;
  // the currently selected coordinate. Optional.empty() means nothing valid is selected.
  private Optional<Coordinate> selected = Optional.empty();
  private final Map<Point, Coordinate> hexagonCenters = new HashMap<>();
  private static final double rt3 = Math.sqrt(3);
  // the pixel distance from opposing vertices in a hexagon
  protected static final int size = 52;
  private double scaleRatio = 1.0;

  /**
   * Notifies all subscribers of the action.
   */
  // todo: replace with features interface map call?
  private void notifySubscribers(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_M && selected.isPresent()) {
      for (PlayerActions s : subscribers) {
        s.requestMove(selected.get());
      }
      // reset selection
      selected = Optional.empty();
    } else if (e.getKeyCode() == KeyEvent.VK_P) {
      for (PlayerActions s : subscribers) {
        s.requestPass();
      }
    }
  }

  /**
   * Constructs a JReversiPanel to visualize a reversi board.
   *
   * @param m the model of the board
   */
  public JReversiPanel(ObservableReversiModel m) {
    this.model = Objects.requireNonNull(m);
    this.setBackground(Color.DARK_GRAY);
    // this seems to be required for typing...? weird
    this.setFocusable(true);
    this.addMouseListener(new MouseAdapter() {

      @Override
      public void mousePressed(MouseEvent e) {
        // converts the clicked pixel position to a hex coordinate.
        Optional<Coordinate> clickedHexagon = pixelToLogical(
                e.getX(),
                e.getY(),
                JReversiPanel.size);
        // debugging
        // System.out.println("Clicked pixel: " + e.getX() + ", " + e.getY());
        if (clickedHexagon.isPresent() && m.isInBounds(clickedHexagon.get())) {
          // debugging
          // System.out.println("Clicked on: " + clickedHexagon);
          if (selected.isPresent() && selected.get().equals(clickedHexagon)) {
            selected = Optional.empty();
          } else {
            selected = clickedHexagon;
          }
          render();
        } else {
          // debugging
          // System.out.println("Clicked illegal: " + clickedHexagon);
          selected = Optional.empty();
          render();
        }
      }
    });
    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        notifySubscribers(e);
      }
    });
  }

  /**
   * Refreshes the canvas.
   * The canvas is also automatically updated on every mouse click.
   */
  public void render() {
    repaint();
  }

  /**
   * Converts a pixel coordinate to a hexagon coordinate.
   *
   * @param x    the X coordinate of the pixel
   * @param y    the Y coordinate of the pixel
   * @param size the distance from hexagon center to vertex
   * @return the Hexagonal coordinate of the hexagon at the pixel,
   *         or 0, 0 if there is no hexagon at that pixel.
   */
  protected Optional<Coordinate> pixelToLogical(int x, int y, int size) {
    x /= scaleRatio;
    y /= scaleRatio;
    size /= rt3;
    int rely;
    int relx;
    for (Point hexagon : hexagonCenters.keySet()) {
      rely = y - hexagon.y;
      relx = x - hexagon.x;
      if (hexagonContainsPoint(relx, rely, size)) {
        return Optional.of(hexagonCenters.get(hexagon));
      }
    }
    // if they click off the board.
    return Optional.empty();
  }

  /**
   * Checks if a hexagon would contain the given point, assuming 0,0 is the center of the hexagon,
   * and there is a vertex at (0, size).
   *
   * @param x      the X coordinate of the point
   * @param y      the Y coordinate of the point
   * @param radius the length from center to vertex
   * @return true iff the point is in the hexagon.
   */
  private boolean hexagonContainsPoint(int x, int y, int radius) {
    // relative Y position must be below the angled top / above angled bottom
    return (Math.abs(y) + Math.abs(x / rt3) < radius)
            // and relative X position must not be past the walls
            && (Math.abs(x) <= radius * rt3 / 2);
  }

  /**
   * Converts a hexagonal coordinate to a pixel location, where that pixel location would be
   * the center of the hexagon.
   *
   * @param coord the coordinate of the hexagon on the board.
   * @return the pixel coordinate of the center of the hexagon.
   */
  public Point logicalToPixel(Coordinate coord) {
    int boardHeight = model.getBoardHeight();
    int x = Math.abs(coord.r - ((boardHeight - 1) / 2)) * JReversiPanel.size / 2
            + (coord.c - Math.max(
            0,
            ((boardHeight - 1) / 2 - coord.r))) * JReversiPanel.size;
    // translate down by the 'capless' hexagon
    int y = (int) (coord.r * 1.5 * JReversiPanel.size / rt3);
    return new Point(x, y);
  }

  /**
   * Paints the panel with the board.
   *
   * @param g the graphics object.
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g; //downcast lol

    scaleToFit(g2d);

    int boardWidth = model.getBoardWidth();
    int boardHeight = model.getBoardHeight();

    // Draw the board
    Coordinate coord;
    for (int r = 0; r < boardHeight; r++) {
      for (int q = 0; q < boardWidth; q++) {
        coord = new Coordinate(q, r);
        if (model.isInBounds(coord)) {
          // draws a hexagonal tile and a piece on it, if there's one there
          drawTile(coord, JReversiPanel.size / 2, g2d);
        }
      }
    }
  }

  /**
   * Scales and translates the board to be in the center of the screen and
   * as big as possible without cutting off the board.
   *
   * @param g2d the graphics object
   */
  private void scaleToFit(Graphics2D g2d) {
    Dimension pref = getPreferredSize();
    // size ratio compared to the original board graphic
    double xRatio = 1. * getBounds().width / pref.width;
    double yRatio = 1. * getBounds().height / pref.height;
    scaleRatio = Math.min(xRatio, yRatio);

    AffineTransform t = new AffineTransform();
    // scaling to be as big as possible without cropping off the board
    t.scale(scaleRatio, scaleRatio);
    // translating to always be center screen
    if (xRatio > yRatio) {
      t.translate((getBounds().width - pref.width * scaleRatio) / (2. * scaleRatio),
              0);
    } else {
      t.translate(0,
              (getBounds().height - pref.height * scaleRatio) / (2. * scaleRatio));
    }
    // perform the transform.
    g2d.transform(t);
  }

  protected void drawTile(Coordinate coord, int size, Graphics2D g2d) {
    Point canvasLocation = logicalToPixel(coord);
    Color old = g2d.getColor();
    g2d.setColor(Color.LIGHT_GRAY);
    if (selected.isPresent() && coord.equals(selected.get())) {
      g2d.setColor(Color.CYAN);
    }

    // offset for the hexagon
    g2d.translate(size, size * 2 / rt3);
    // offset for the logical->physical
    g2d.translate(canvasLocation.x, canvasLocation.y);
    // filled hexagon
    Path2D.Double hex = createHexagon(size);
    // used later for pixel to hex
    hexagonCenters.put(
            new Point(size + canvasLocation.x, (int) (canvasLocation.y + size * 2 / rt3)),
            coord);

    g2d.draw(hex);
    g2d.fill(hex);
    // outline
    hex = createHexagon(size);
    g2d.setColor(Color.BLACK);
    g2d.draw(hex);
    // the black or white piece
    if (model.hasPiece(coord)) {
      g2d.setColor(model.isTileBlack(coord) ? Color.BLACK : Color.WHITE);
      g2d.fillOval(-size / 2, -size / 2, size, size);
    }
    // undo any changes we made
    g2d.translate(-size, -size * 2 / rt3);
    g2d.translate(-canvasLocation.x, -canvasLocation.y);
    g2d.setColor(old);
  }

  private Path2D.Double createHexagon(int size) {
    size = (int) (size * 2 / rt3);
    Path2D.Double hex = new Path2D.Double();
    double angle_rad = Math.PI / 3;
    double up = Math.PI / 2;
    hex.moveTo(0, size);
    for (int i = 1; i < 6; i++) {
      hex.lineTo(size * Math.cos(up + angle_rad * i),
              size * Math.sin(up + angle_rad * i));
    }

    hex.closePath();
    return hex;
  }

  @Override
  public Dimension getPreferredSize() {
    int width = JReversiPanel.size * model.getBoardWidth();
    // the caps of the hexagons overlap vertically (except for the top row, which we add)
    int height = // top row's cap (the part that would be overlapping on the lower rows)
            (int) (0.5 * (JReversiPanel.size / rt3)
                    // lower rows non-overlapping portions
                    + ((JReversiPanel.size / rt3) * 1.5 * model.getBoardHeight()));

    return new Dimension(width, height);
  }

  public Optional<Coordinate> getSelected() {
    return selected;
  }
}