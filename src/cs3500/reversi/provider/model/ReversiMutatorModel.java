package cs3500.reversi.provider.model;

/**
 * Represents the primary mutator model interface for playing a game of Reversi.
 */
public interface ReversiMutatorModel extends ReversiReadOnlyModel {

  /**
   * Starts a new game of Reversi.
   *
   * <p>This method should have no side effects other than configuring this model
   * instance, and should work for any valid arguments.</p>
   *
   * @throws IllegalStateException if the game has already started
   * @throws IllegalArgumentException if the size is invalid
   */
  void startGame() throws IllegalStateException, IllegalArgumentException;

  /**
   * Places the current turn's disc to the column and row,
   * if the move is allowable by the rules of the game.
   *
   * @param col the 0-based index of the column for a disk to be placed in.
   * @param row the 0-based index of the row for a disk to be placed in.
   * @throws IllegalArgumentException if the column or row is invalid.
   * @throws IllegalStateException if the move is not allowable by the rules of the game
   *                  or if the game has not been started yet.
   */
  void placeDisc(int col, int row)
          throws IllegalArgumentException, IllegalStateException;

  /**
   * Skips the current player's turn.
   *
   * @throws IllegalStateException if the game has not been started yet.
   */
  void skipTurn() throws IllegalStateException;




  void addModelNotificationListener(ModelNotifications listener);

}
