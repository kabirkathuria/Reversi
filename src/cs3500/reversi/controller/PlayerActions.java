package cs3500.reversi.controller;

import cs3500.reversi.model.Coordinate;

/**
 * Requirements to be a listener for player inputs.
 */
public interface PlayerActions {

  /**
   * Notification served when a move is being requested.
   * @param coord The coordinate of the move.
   */
  void requestMove(Coordinate coord);

  /**
   * Notification served when a pass is being requested.
   */
  void requestPass();
}
