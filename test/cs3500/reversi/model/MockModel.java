package cs3500.reversi.model;

import java.util.List;

import cs3500.reversi.controller.ModelStatus;

/**
 * A mock model for recording the requests made to a real model. Useful for testing.
 */
public class MockModel implements ReversiModel {
  ReversiModel m;
  StringBuilder record = new StringBuilder();

  public MockModel(ReversiModel m) {
    this.m = m;
  }

  /**
   * Returns the record of commands issued to the real model.
   *
   * @return the record of command calls, as a string, from the string builder.
   */
  public String report() {
    return record.toString();
  }

  /**
   * Checks if the game is over, which happens if both players pass consecutively.
   *
   * @return true if the game is over, false otherwise
   */
  @Override
  public boolean isGameOver() {
    record.append("Checked if game over\n");
    return m.isGameOver();
  }

  /**
   * Returns whether a hexagonal coordinate is in bounds for the board.
   *
   * @param coordinate the coordinate of the tile being checked.
   * @return true, iff the coordinate is a valid tile in the bounds of the board.
   */
  @Override
  public boolean isInBounds(Coordinate coordinate) {
    record.append("Checked isInBounds ").append(coordinate).append("\n");
    return m.isInBounds(coordinate);
  }

  /**
   * Retrieves the color of the piece at a given coordinate.
   *
   * @param coordinate the position on the board
   * @return the player at the given position
   * @throws IllegalArgumentException if the coordinate is out of bounds
   */
  @Override
  public boolean isTileBlack(Coordinate coordinate) {
    record.append("Checked isTileBlack ").append(coordinate).append("\n");
    return m.isTileBlack(coordinate);
  }

  /**
   * Checks if the coordinate has a piece on it.
   *
   * @param coordinate the coordinate on the board being checked
   * @return false if empty, true if there's a piece.
   */
  @Override
  public boolean hasPiece(Coordinate coordinate) {
    record.append("Checked hasPiece ").append(coordinate).append("\n");
    return m.hasPiece(coordinate);
  }

  /**
   * Gets the length, i.e. the vertical dimension, of the game board.
   *
   * @return the board's length.
   */
  @Override
  public int getBoardHeight() {
    record.append("Checked board height\n");
    return m.getBoardHeight();
  }

  /**
   * Gets the width, i.e. the horizontal direction, of the game board.
   *
   * @return the board's width
   */
  @Override
  public int getBoardWidth() {
    record.append("Checked board width\n");
    return m.getBoardWidth();
  }

  /**
   * Gives black's score, in # of tiles on the board.
   *
   * @return a whole number, indicating black's score.
   */
  @Override
  public int getBlackScore() {
    record.append("Checked black's score\n");
    return m.getBlackScore();
  }

  /**
   * Checks if the current player has any valid moves on the board (brute force).
   *
   * @return true iff there is a valid move on the board.
   */
  @Override
  public boolean anyValidMoves() {
    record.append("Checked valid moves\n");
    return m.anyValidMoves();
  }

  /**
   * Gives white's score, in # of tiles on the board.
   *
   * @return a whole number, indicating white's score.
   */
  @Override
  public int getWhiteScore() {
    record.append("Checked white score\n");
    return m.getWhiteScore();
  }

  /**
   * Hides the Player enum from the interface.
   *
   * @return whether it's the black's turn or not.
   */
  @Override
  public boolean isBlackTurn() {
    record.append("Checked isBlackTurn\n");
    return m.isBlackTurn();
  }

  /**
   * Checks if the current player would be able to play a move at the given coordinate.
   *
   * @param coordinate the coordinate of the potential move.
   * @return true iff the current player would be able to play their turn at the given coordinate.
   */
  @Override
  public boolean isLegal(Coordinate coordinate) {
    record.append("Checked isLegal ").append(coordinate).append("\n");
    return m.isLegal(coordinate);
  }

  /**
   * Notifies the modelStatus listener when the turn changes.
   *
   * @param s one of the subscribers to be notified.
   */
  @Override
  public void addListener(ModelStatus s) {
    record.append("Listener added:").append(s).append("\n");
  }

  /**
   * Returns the resulting score of making a move at a given position.
   *
   * @param coord the coordinate
   * @return the number of tiles flipped by the move.
   */
  @Override
  public int potentialScore(Coordinate coord) {
    record.append("Checked potentialScore of move at ").append(coord).append("\n");
    return m.potentialScore(coord);
  }

  /**
   * Returns the coordinates of the corners on this board.
   *
   * @return a list of corner coordinates.
   */
  @Override
  public List<Coordinate> getCorners() {
    record.append("Got corners");
    return m.getCorners();
  }

  /**
   * Returns the possible move direction vectors for the board.
   *
   * @return the move (unit) vectors.
   */
  @Override
  public List<Coordinate> getDirections() {
    record.append("Got directions");
    return m.getDirections();
  }

  /**
   * Attempts to place a piece at the given coordinate on the board.
   *
   * @param coordinate The position of the new tile, if valid.
   * @throws IllegalArgumentException if the coordinate of the move is out of bounds.
   * @throws IllegalStateException    if the move is not allowed according to the
   *                                  rules of the game, or is not logically possible.
   */
  @Override
  public void move(Coordinate coordinate) {
    record.append("Made move ").append(coordinate).append("\n");
    m.move(coordinate);
  }

  /**
   * The game is over if both players pass consecutively. Both players might be forced to pass if
   * there are no legal moves for either player.
   */
  @Override
  public void pass() {
    record.append("Passed\n");
    m.pass();
  }

  /**
   * Tells the model to notify its subscribers that the game has started.
   */
  @Override
  public void startGame() {
    m.startGame();
    record.append("Game started");
  }
}
