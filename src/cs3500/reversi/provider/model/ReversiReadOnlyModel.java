package cs3500.reversi.provider.model;

import java.util.Map;
import java.util.List;


/**
 * Represents the primary read only model interface for playing a game of Reversi.
 */
public interface ReversiReadOnlyModel {
  /**
   * Determines if the game is over. A game is over if either player is unable to place a disc.
   *
   * @return true if the game is over, otherwise false.
   * @throws IllegalStateException if the game has not been started yet.
   */
  boolean isGameOver() throws IllegalStateException;

  /**
   * Determines which color won the game once a game is over.
   * If a game is not over return a none disc.
   *
   *
   * @return The disc of the winner.
   * @throws IllegalStateException if the game has not been started yet.
   */
  Disc gameWinner() throws IllegalStateException;

  /**
   * Gets a disc of the current turn. (i.e. the disc that will be placed next)
   *
   * @return a disc of the current turn.
   * @throws IllegalStateException if the game has not been started yet.
   */
  Disc getCurrentTurn() throws IllegalStateException;

  /**
   * Gets the size of the board.
   * In the case of hexagon Reversi, it gets the number of layers around
   * the center hexagon.
   *
   * @return the size of the board.
   * @throws IllegalStateException if the game has not been started yet.
   */
  int getBoardSize() throws IllegalStateException;

  /**
   * Gets the disc at the specified column and row from a 0-based index.
   *
   * @param q the column
   * @param r the row
   * @return the disc at the given position
   * @throws IllegalStateException if the game has not been started yet.
   * @throws IllegalArgumentException if column or row is invalid.
   */
  Disc getDiscAt(int q, int r) throws IllegalStateException, IllegalArgumentException;

  /**
   * Determines if the current player's move is valid at the specified column and row.
   *
   * @param q the column
   * @param r the row
   * @return true if the move is valid false otherwise.
   * @throws IllegalStateException if the game has not been started yet.
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  boolean validMove(int q, int r) throws IllegalStateException, IllegalArgumentException;

  /**
   * Determines if the current player has any valid moves.
   *
   * @param player the disc of the player
   * @return true if there is a valid move, false otherwise.
   * @throws IllegalStateException if the game has not been started yet.
   */
  boolean hasValidMove(Disc player) throws IllegalStateException;

  /**
   * Determines the score of the given disc.
   * The score is the total number of discs of the given disc on the board.
   *
   * @param player disc of player
   * @return score as an integer
   * @throws IllegalStateException if the game has not been started yet.
   */
  int getScore(Disc player) throws IllegalStateException;

  /**
   * Gets the current board state and returns it as a copy.
   *
   * @return current board state
   * @throws IllegalStateException if the game has not been started yet.
   */
  Map<Hex, Disc> getBoard() throws IllegalStateException;


  /**
   * Gets a list of all valid moves for the given disc.
   *
   * @param player disc of player
   * @return a list of all valid moves for the current colors turn.
   * @throws IllegalStateException if the game has not been started yet.
   */
  List<Hex> getValidMoves(Disc player) throws IllegalStateException;

  /**
   * Gets get number of discs that will be captured given coordinates and a disc.
   *
   * @param q the q coordinate
   * @param r the r coordinate
   * @param player disc of move
   * @return number of discs that will be captured from the given move
   */
  int discsCaptured(int q, int r, Disc player);
}
