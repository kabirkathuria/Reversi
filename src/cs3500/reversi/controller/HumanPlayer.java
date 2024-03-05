package cs3500.reversi.controller;

/**
 * Represents a human player, who will probably be using a GUI view and does not
 * need to act on the notifications, or provide listeners with notifications.
 */
public class HumanPlayer implements GenericPlayer {
  private final boolean isBlack;

  /**
   * Constructs a human player, which doesn't do much as the human player interacts with the GUI.
   * @param black the color the player is going to play.
   */
  public HumanPlayer(boolean black) {
    this.isBlack = black;
  }

  /**
   * Notifies the player when it's their turn... which they will see in the view.
   */
  @Override
  public void yourTurn() {
    // don't really have to do anything
  }

  /**
   * The color of this player.
   * @return true if this player is playing with the black pieces. False otherwise.
   */
  @Override
  public boolean isBlack() {
    return isBlack;
  }

  /**
   * Adds a listener to the... whatever. are you really reading this? it does nothing.
   * @param s like and subscribe
   */
  @Override
  public void addListener(PlayerActions s) {
    // again, this is only really useful for machines. the player will use the GUI.
  }
}
