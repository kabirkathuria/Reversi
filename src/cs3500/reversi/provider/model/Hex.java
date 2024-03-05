package cs3500.reversi.provider.model;

import java.awt.geom.Point2D;
import java.util.Objects;

// NOTE: some of this code was referenced from https://www.redblobgames.com/grids/hexagons/

/**
 *  To represent a hex coordinate.
 */
public final class Hex {
  private final int q;
  private final int r;
  private final int s;

  /**
   * Used to construct a hex coordinate.
   *
   * @param q the q value of the q-axis.
   * @param r the r value of the r-axis.
   * @param s the s value of the s-axis.
   */
  public Hex(int q, int r, int s) {
    this.q = q;
    this.r = r;
    this.s = s;

    if (q + r + s != 0) {
      throw new IllegalArgumentException("q + r + s must be 0");
    }
  }

  /**
   * Converts hex coordinates to pixel coordinates.
   * @param size of a hex
   * @return pixel coordinates
   */
  public Point2D hexToPixel(double size) {
    double x = size * (Math.sqrt(3) * this.q + Math.sqrt(3) / 2 * this.r);
    double y = size * (3.0 / 2 * this.r);

    return new Point2D.Double(x, y);
  }



  /**
   * Gets the q coordinate of this hexagon.
   *
   * @return q coordinate
   */
  public int getQ() {
    return this.q;
  }

  /**
   * Gets the r coordinate of this hexagon.
   *
   * @return r coordinate
   */
  public int getR() {
    return this.r;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Hex)) {
      return false;
    }
    Hex that = (Hex) other;
    return this.q == that.q
            && this.r == that.r
            && this.s == that.s;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.q, this.r, this.s);
  }
}
