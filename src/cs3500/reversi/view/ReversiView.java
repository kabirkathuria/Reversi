package cs3500.reversi.view;

import cs3500.reversi.controller.PlayerActions;

/**
 * Represents the view for the Reversi game.
 * Provides methods to render the game state.
 */
public interface ReversiView {

  /**
   * Renders the state of the reversi game.
   */
  void render();

  /**
   * Updates the display, re-rendering the state of the game.
   */
  void update();

  /**
   * Indicates that a requested move was not valid.
   * To be displayed to the user!
   */
  void invalidMove();

  void notYourTurn();

  /**
   * Adds a listener to player actions made through the view.
   * @param s the listener being added to the list of subscribers.
   */
  void addListener(PlayerActions s);
}