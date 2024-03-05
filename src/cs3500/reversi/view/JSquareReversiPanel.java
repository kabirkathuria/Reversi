package cs3500.reversi.view;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Optional;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;

/**
 * Draws the panel for a square reversi board.
 */
public class JSquareReversiPanel extends JReversiPanel {

  /**
   * Constructs a JReversiPanel to visualize a reversi board.
   *
   * @param m the model of the board
   */
  public JSquareReversiPanel(ObservableReversiModel m) {
    super(m);
  }

  @Override
  public void drawTile(Coordinate coord, int size, Graphics2D g2d) {
    size *= 2;
    AffineTransform t = g2d.getTransform();
    Color old = g2d.getColor();
    g2d.setColor(Color.LIGHT_GRAY);
    if (super.getSelected().isPresent() && coord.equals(super.getSelected().get())) {
      g2d.setColor(Color.CYAN);
    }

    Point c = logicalToPixel(coord);
    g2d.translate(c.getX() ,c.getY());
    g2d.fillRect(1, 1, size - 1, size - 1);
    g2d.setColor(Color.BLACK);
    g2d.drawRect(0, 0, size, size);

    if (model.hasPiece(coord)) {
      g2d.translate(size / 2, size / 2);
      g2d.setColor(model.isTileBlack(coord) ? Color.BLACK : Color.WHITE);
      g2d.fillOval(-size / 3, -size / 3, 2 * size / 3, 2 * size / 3);
    }

    g2d.setColor(old);
    g2d.setTransform(t);
  }

  @Override
  public Dimension getPreferredSize() {
    int width = size * model.getBoardWidth();
    int height = size * model.getBoardHeight();

    return new Dimension(width, height);
  }

  @Override
  protected Optional<Coordinate> pixelToLogical(int x, int y, int size) {
    return Optional.of(new Coordinate(x / size, y / size));
  }

  @Override
  public Point logicalToPixel(Coordinate c) {
    return new Point(size * c.c, size * c.r);
  }
}
