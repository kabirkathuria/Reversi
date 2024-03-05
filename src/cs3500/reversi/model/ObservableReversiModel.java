package cs3500.reversi.model;

import java.util.List;

import cs3500.reversi.controller.ModelStatus;

/**
 * Represents an observable version of the Reversi model.
 * This interface provides methods to access information about the game, such as the state of the
 * game (playing, over), the player at a certain coordinate, the size (diameter) of the board,
 * and the current score of the game.
 */
public interface ObservableReversiModel {

  /**
   * Checks if the game is over, which happens if both players pass consecutively.
   *
   * @return true if the game is over, false otherwise
   */
  boolean isGameOver();

  /**
   * Returns whether a hexagonal coordinate is in bounds for the board.
   * @return true, iff the coordinate is a valid tile in the bounds of the board.
   */
  boolean isInBounds(Coordinate coordinate);

  /**
   * Retrieves the color of the piece at a given coordinate.
   *
   * @param coordinate the position on the board
   * @return the player at the given position
   * @throws IllegalArgumentException if the coordinate is out of bounds
   */
  boolean isTileBlack(Coordinate coordinate);

  /**
   * Checks if the coordinate has a piece on it.
   * @param coordinate the coordinate on the board being checked
   * @return false if empty, true if there's a piece.
   */
  boolean hasPiece(Coordinate coordinate);

  /**
   * Gets the length, i.e. the vertical dimension, of the game board.
   *
   * @return the board's length.
   */
  int getBoardHeight();

  /**
   * Gets the width, i.e. the horizontal direction, of the game board.
   *
   * @return the board's width
   */
  int getBoardWidth();

  /**
   * Gives black's score, in # of tiles on the board.
   *
   * @return a whole number, indicating black's score.
   */
  int getBlackScore();

  /**
   * Checks if the current player has any valid moves on the board (brute force).
   * @return true iff there is a valid move on the board.
   */
  boolean anyValidMoves();

  /**
   * Gives white's score, in # of tiles on the board.
   *
   * @return a whole number, indicating white's score.
   */
  int getWhiteScore();

  /**
   * Hides the Player enum from the interface.
   * @return whether it's the black's turn or not.
   */
  boolean isBlackTurn();

  /**
   * Checks if the current player would be able to play a move at the given coordinate.
   * @param coordinate the coordinate of the potential move.
   * @return true iff the current player would be able to play their turn at the given coordinate.
   */
  boolean isLegal(Coordinate coordinate);

  /**
   * Notifies the modelStatus listener when the turn changes.
   * @param s one of the subscribers to be notified.
   */
  void addListener(ModelStatus s);

  /**
   * Returns the resulting score of making a move at a given position.
   * @return the number of tiles flipped by the move.
   */
  int potentialScore(Coordinate coord);

  /**
   * Returns the coordinates of the corners on this board.
   * @return a list of corner coordinates.
   */
  List<Coordinate> getCorners();

  /**
   * Returns the possible move direction vectors for the board.
   * @return the move (unit) vectors.
   */
  List<Coordinate> getDirections();
}