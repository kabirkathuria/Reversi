package cs3500.reversi.adapter;

import cs3500.reversi.controller.PlayerActions;
import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;
import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.view.ViewFeatures;

/**
 * Adapts the calls to the controller.
 */
public class ControllerAdapter implements ViewFeatures {
  PlayerActions s;
  ObservableReversiModel self;

  public ControllerAdapter(PlayerActions s, ObservableReversiModel m1) {
    this.s = s;
    this.self = m1;
  }

  /**
   * Notifies the subscriber that a move is made at the given hex.
   *
   * @param hex coordinate.
   */
  @Override
  public void makeMove(Hex hex) {
    s.requestMove(convertCoord(hex.getQ(), hex.getR()));
  }

  /**
   * Notifies the subscriber to pass/skip the turn.
   */
  @Override
  public void pass() {
    s.requestPass();
  }

  /**
   * Notifies the subscriber to end the game.
   */
  @Override
  public void quit() {
    // what is this actually supposed to do?
    pass();
  }

  private Coordinate convertCoord(int q, int r) {
    int size = self.getBoardHeight() / 2;
    return new Coordinate(q + size, r + size);
  }
}
