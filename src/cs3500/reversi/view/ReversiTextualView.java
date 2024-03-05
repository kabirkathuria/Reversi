package cs3500.reversi.view;

import java.io.IOException;

import cs3500.reversi.controller.PlayerActions;
import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;

/**
 * Represents the textual view of the Reversi game.
 * Implements the ReversiView interface to render the game state as a string.
 * Takes an appendable and ObservableReversiModel-- though this will likely
 * be a MutableReversiModel in practice, we want to limit the scope here.
 */
public class ReversiTextualView implements ReversiView {
  protected final ObservableReversiModel m;
  protected final Appendable out;

  /**
   * Constructs a ReversiTextualView given an ObservableReversiModel and output target.
   *
   * @param m the game model
   */
  public ReversiTextualView(ObservableReversiModel m, Appendable out) {
    this.m = m;
    this.out = out;
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
  public void render() {
    for (int r = 0; r < m.getBoardHeight(); r++) {
      if (r > m.getBoardHeight() / 2) {
        for (int s = 0; s < r - m.getBoardWidth() / 2; s++) {
          transmit(" ");
        }
      }
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
        if ((q + r < (m.getBoardHeight() - 1) * 1.5) && q != m.getBoardHeight() - 1) {
          transmit(" ");
        }
      }
      transmit("\n");
    }
  }

  public void update() {
    this.render();
  }

  /**
   * Indicates that a requested move was not valid.
   * To be displayed to the user!
   */
  @Override
  public void invalidMove() {
    update();
    transmit("Invalid move!");
  }

  @Override
  public void notYourTurn() {
    transmit("Not your turn yet!");
  }

  /**
   * Adds a listener to player actions made through the view.
   *
   * @param s the listener being added to the list of subscribers.
   */
  @Override
  public void addListener(PlayerActions s) {
    // nothing to really do here...
  }

  protected void transmit(String message) {
    try {
      out.append(message);
    } catch (IOException e) {
      System.out.println("Could not transmit!");
      e.printStackTrace();
    }
  }
}
