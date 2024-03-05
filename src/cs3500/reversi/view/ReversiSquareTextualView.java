package cs3500.reversi.view;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;

/**
 * Renders a square reversi model with text!
 */
public class ReversiSquareTextualView extends ReversiTextualView {
  /**
   * Constructs a ReversiTextualView given an ObservableReversiModel and output target.
   *
   * @param m   the game model
   * @param out the output stream.
   */
  public ReversiSquareTextualView(ObservableReversiModel m, Appendable out) {
    super(m, out);
  }

  /**
   * Renders the current game state as a string.
   *
   * <p>- Black pieces are represented by "○"
   *
   * <p>- White pieces are represented by "●"
   *
   * <p>- Empty cells are represented by "_"
   */
  @Override
  public void render() {
    for (int r = 0; r < m.getBoardHeight(); r++) {
      for (int q = 0; q < m.getBoardWidth(); q++) {
        try {
          if (!m.hasPiece(new Coordinate(q, r))) {
            transmit("_");
          } else if (m.isTileBlack(new Coordinate(q, r))) {
            transmit("X");
          } else {
            transmit("O");
          }
        } catch (IllegalArgumentException ignored) {
          // ignore and just indent, the model does the bounds checking for us.
          // this will result in trailing spaces instead of just leading ones, which is fine.
        }
      }
      transmit("\n");
    }
  }
}
