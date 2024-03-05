package cs3500.reversi.controller;

/**
 * Represents a player in reversi.
 */
public interface GenericPlayer {
  /**
   * Tells the player it's their turn to make a move.
   */
  void yourTurn();

  /**
   * Whether this player is using the black pieces or not.
   * @return true iff they are using the black pieces.
   */
  boolean isBlack();

  /**
   * adds a listener for moves or passes made by this player.
   * @param p the subscriber to receive notifications.
   */
  void addListener(PlayerActions p);
}
