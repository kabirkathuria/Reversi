package cs3500.reversi.provider.view;

import java.awt.geom.Path2D;

/**
 *  To represent a standard pointy top hexagon, a subclass of Path2D.
 */
final class StandardHexagon extends Path2D.Double {
  /**
   * Constructor used to path the lines of a hexagon.
   * @param centerX center x coordinate
   * @param centerY center y coordinate
   * @param size size of hexagon
   */
  public StandardHexagon(double centerX, double centerY, double size) {
    this.pathHexagon(centerX, centerY, size);
  }

  private void pathHexagon(double centerX, double centerY, double size) {

    double startX = centerX + size * Math.cos(Math.PI / 6);
    double startY = centerY + size * Math.sin(Math.PI / 6);
    this.moveTo(startX, startY);

    for (int side = 1; side < 6; side++) {
      double angle = side * (Math.PI / 3) + (Math.PI / 6);
      double x = centerX + size * Math.cos(angle);
      double y = centerY + size * Math.sin(angle);
      this.lineTo(x, y);
    }

    this.closePath();
  }

}
