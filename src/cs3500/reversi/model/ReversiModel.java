package cs3500.reversi.model;

/**
 * Represents a mutable version of the Reversi model which allows making moves and passing turns.
 * This interface extends ObservableReversiModel to provide additional methods for game actions.
 */
public interface ReversiModel extends ObservableReversiModel {
  /**
   * Attempts to place a piece at the given coordinate on the board.
   *
   * @param coordinate The position of the new tile, if valid.
   * @throws IllegalArgumentException if the coordinate of the move is out of bounds.
   * @throws IllegalStateException    if the move is not allowed according to the
   *                                  rules of the game, or is not logically possible.
   */
  void move(Coordinate coordinate);

  /**
   * The game is over if both players pass consecutively. Both players might be forced to pass if
   * there are no legal moves for either player.
   */
  void pass();

  /**
   * Tells the model to notify its subscribers that the game has started.
   */
  void startGame();
}