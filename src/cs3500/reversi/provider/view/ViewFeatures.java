package cs3500.reversi.provider.view;

import cs3500.reversi.provider.model.Hex;

/**
 * To represent a features interface of Reversi. This is everything a player should be able to do
 * when interacting with the view.
 */
public interface ViewFeatures {
  /**
   * Notifies the subscriber that a move is made at the given hex.
   * @param hex coordinate.
   */
  void makeMove(Hex hex);

  /**
   * Notifies the subscriber to pass/skip the turn.
   */
  void pass();

  /**
   * Notifies the subscriber to end the game.
   */
  void quit();

}
