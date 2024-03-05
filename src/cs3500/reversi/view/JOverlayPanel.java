package cs3500.reversi.view;



import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;

/**
 * An overlay panel that displays the number of captures on top of the selected tile
 * when there is no piece on that tile. Enabled/disabled by pressing H.
 */
public class JOverlayPanel extends JReversiPanel {
  boolean show;

  /**
   * Constructs a JOverlayPanel that draws hints on top of a hexagonal panel.
   * @param m the hexagonal model.
   */
  public JOverlayPanel(ObservableReversiModel m) {
    super(m);
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_H) {
          show = !show;
          repaint();
        }
      }
    });
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    if (super.getSelected().isPresent() && show) {
      Coordinate coord = super.getSelected().get();
      int score = 0;
      if (model.isLegal(coord)) {
        score = model.potentialScore(coord);
      }
      if (!model.hasPiece(coord)) {
        AffineTransform old = g2d.getTransform();
        Color c = g2d.getColor();
        g2d.setColor(new Color(0, 64, 64, 255));
        g2d.translate(logicalToPixel(coord).getX() + 26,
                logicalToPixel(coord).getY() + 26);
        g2d.scale(2.5, 2.5);
        g2d.drawString("" + score, -3, 6);
        g2d.setTransform(old);
        g2d.setColor(c);
      }
    }
  }
}
