package cs3500.reversi.model;

import java.awt.Color;

/**
 * Represents the two players in the Reversi game: BLACK and WHITE.
 * Each player is associated with a specific color.
 */
enum Player {
  BLACK(Color.black), WHITE(Color.white);
  private final Color color;

  /**
   * Constructs a Player with the associated color.
   *
   * @param color the color associated with the player
   */
  Player(Color color) {
    this.color = color;
  }

  /**
   * Provides a string representation of the player.
   *
   * @return "●" for WHITE and "○" for BLACK
   */
  public String toString() {
    if (color == Color.white) {
      return "●";
    }
    return "○";
  }
}