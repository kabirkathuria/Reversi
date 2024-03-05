package cs3500.reversi.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cs3500.reversi.controller.ModelStatus;

/**
 * Represents the model for a Reversi game played on a hexagonal grid.
 * Implements the MutableReversiModel to offer core game functionalities such as making moves,
 * checking game state, and accessing board details. View the README for more in depth information
 * on how to actually play reversi or use the model!
 */
public abstract class AbstractReversiModel implements ReversiModel {
  // represented in coordinates C,R where C is the slanted column of tiles running
  // from the top left to the bottom right, and R is the horizontal row of tiles.
  // not all coordinates C,R are valid, as each row of the board
  // does not have an equal number of tiles. See isInBounds for more info.
  protected Player[][] board;
  // if one player has called for surrender
  protected boolean passed = false;
  protected boolean gameOver = false;
  // Black moves first. This changes every move or pass.
  protected Player turn = Player.BLACK;
  // the offsets in the board 2d-array that represent a tile's neighbors.
  // starts clockwise from the top left.
  protected List<Coordinate> directions;

  /**
   * No-arg constructor; Creates a default game of reversi with board size 7.
   */
  public AbstractReversiModel(List<Coordinate> d) {
    this.directions = d;
  }

  /**
   * let players know the game has started, and it's black's turn to make a move.
   */
  public void startGame() {
    notifySubscribers();
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
    throwIfGameOver();
    validateMove(turn, coordinate);
    directions.forEach(s -> flipAdjacentIfLegal(turn, coordinate, s));
    board[coordinate.c][coordinate.r] = turn;
    passed = false;
    changeTurn();
  }

  /**
   * The game is over if both players pass consecutively. Both players might be forced to pass if
   * there are no legal moves for either player.
   */
  @Override
  public void pass() {
    throwIfGameOver();
    if (passed) {
      gameOver = true;
    } else {
      passed = true;
    }
    changeTurn();
  }

  /**
   * Prevents the player from doing anything if the game is already over (i.e, both players passed)
   */
  protected void throwIfGameOver() {
    if (gameOver) {
      throw new IllegalStateException("The game is over knucklehead! You chose this...");
    }
  }

  /**
   * Checks if playing a move at a given coordinate is valid for a given player.
   *
   * @throws IllegalArgumentException if the coordinates are not within the board's bounds
   * @throws IllegalStateException    if the move is not logically possible, or there is already
   *                                  a piece on that tile.
   */
  protected void validateMove(Player player, Coordinate coordinate) {
    // handles out of bounds moves
    if (!isInBounds(coordinate)) {
      throw new IllegalArgumentException("Coordinate out of bounds!");
    }
    // handles non-empty tile
    if (hasPiece(coordinate)) {
      throw new IllegalStateException("There is already a piece on that tile!");
    }
    if (directions.stream()
            .noneMatch(s -> hasMoveInDirection(player, coordinate, s, false))) {
      throw new IllegalStateException("There are no valid moves in any direction");
    }
  }

  /**
   * Checks if the current player would be able to play a move at the given coordinate.
   * @param coordinate the coordinate of the potential move.
   * @return true iff the current player would be able to play their turn at the given coordinate.
   */
  public boolean isLegal(Coordinate coordinate) {
    try {
      validateMove(turn, coordinate);
      return true;
    } catch (IllegalArgumentException | IllegalStateException ignored) {
      return false;
    }
  }

  /**
   * Attempts to flip the adjacent pieces if the move is legal at the given coordinate.
   *
   * @param player     the player trying to flip tiles.
   * @param coordinate the coordinate of the new piece.
   * @param direction  the direction that is being checked, one of six. See {@link #directions}
   */
  protected void flipAdjacentIfLegal(Player player, Coordinate coordinate, Coordinate direction) {
    if (hasMoveInDirection(player, coordinate, direction, false)) {
      hasMoveInDirection(player, coordinate, direction, true);
    }
  }

  /**
   * checks if there is a valid move in the given direction from the given coordinate.
   * modifies the board if called with modify == true.
   * @param player the player placing a piece down
   * @param coord  the coordinate of the new piece
   * @param dir    the direction being checked for a valid move
   * @param modify whether the pieces should be flipped when iterating, assumes
   *               that there is already a valid move! If true, modifies the board.
   * @return if there is a valid move in that direction.
   */
  protected boolean hasMoveInDirection(Player player, Coordinate coord, Coordinate dir,
                                       boolean modify) {
    // we can start at the neighbor tile in the given direction
    Coordinate currCoordinate = new Coordinate(coord.c + dir.c, coord.r + dir.r);
    // for a move to be valid, a capture has to happen, so we have to see
    // some number of enemy pieces followed by a friendly piece.
    boolean jumpsOpponentPiece = false;

    while (isInBounds(currCoordinate)) {
      if (!hasPiece(currCoordinate) && !currCoordinate.equals(coord)) {
        return false;
      }
      // if the piece's color matches the player's piece
      if ((isTileBlack(currCoordinate) == (player == Player.BLACK))
              && !currCoordinate.equals(coord)) {
        return jumpsOpponentPiece;
      }
      if (modify) {
        board[currCoordinate.c][currCoordinate.r] = player;
      }
      jumpsOpponentPiece = true;
      currCoordinate = new Coordinate(
              currCoordinate.c + dir.c,
              currCoordinate.r + dir.r);
    }
    return false;
  }

  /**
   * Checks if the game is over, which happens if both players pass consecutively.
   *
   * @return true if the game is over, false otherwise
   */
  @Override
  public boolean isGameOver() {
    return gameOver;
  }

  /**
   * Checks if the current player has any valid moves on the board (brute force).
   * @return true iff there is a valid move on the board and the game isn't over.
   */
  public boolean anyValidMoves() {
    if (isGameOver()) {
      return false;
    }
    for (int r = 0; r < getBoardHeight(); r++) {
      for (int c = 0; c < getBoardWidth(); c++) {
        try {
          validateMove(turn, new Coordinate(c, r));
          return true;
        } catch (IllegalArgumentException | IllegalStateException ignored) {
          // do nothing
        }
      }
    }
    return false;
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
    if (isInBounds(coordinate)) {
      return board[coordinate.c][coordinate.r] == Player.BLACK;
    }
    throw new IllegalArgumentException("Invalid coordinate!");
  }

  /**
   * Checks if the coordinate has a piece on it.
   * @param coordinate the coordinate on the board being checked
   * @return false if empty, true if there's a piece.
   */
  public boolean hasPiece(Coordinate coordinate) {
    if (isInBounds(coordinate)) {
      return board[coordinate.c][coordinate.r] != null;
    }
    throw new IllegalArgumentException("Invalid coordinate!");
  }

  /**
   * Hides the Player enum from the interface.
   * @return whether it's the first player's turn or not.
   */
  public boolean isBlackTurn() {
    return turn == Player.BLACK;
  }

  /**
   * Gets the length, i.e. the vertical dimension, of the game board.
   *
   * @return the board's length.
   */
  @Override
  public int getBoardHeight() {
    return this.board[0].length;
  }

  /**
   * Gets the width, i.e. the horizontal direction, of the game board.
   *
   * @return the board's width
   */
  @Override
  public int getBoardWidth() {
    return this.board.length;
  }

  /**
   * Changes the turn from white to black or vice versa.
   */
  protected void changeTurn() {
    if (turn == Player.BLACK) {
      turn = Player.WHITE;
    } else {
      turn = Player.BLACK;
    }
    // now we are notifying any modelStatus listeners!
    notifySubscribers();
  }

  /**
   * Gives black's score, in # of tiles on the board.
   *
   * @return a whole number, indicating black's score.
   */
  @Override
  public int getBlackScore() {
    int count = 0;
    for (Player[] row : board) {
      for (Player tile : row) {
        if (tile == Player.BLACK) {
          count += 1;
        }
      }
    }
    return count;
  }

  /**
   * Gives white's score, in # of tiles on the board.
   *
   * @return a whole number, indicating white's score.
   */
  @Override
  public int getWhiteScore() {
    int count = 0;
    for (Player[] row : board) {
      for (Player tile : row) {
        if (tile == Player.WHITE) {
          count += 1;
        }
      }
    }
    return count;
  }

  // the modelStatus listeners that want notifications when the turn changes
  protected final Set<ModelStatus> subscribers = new HashSet<>();

  /**
   * notifies everyone in the subscription list when the turn changes.
   */
  void notifySubscribers() {
    for (ModelStatus s : subscribers) {
      s.notifyTurn(isBlackTurn());
    }
  }

  /**
   * Adds a modelStatus listener to the list of subscribers to be notified when the turn changes.
   * @param s one of the subscribers to be notified.
   */
  public void addListener(ModelStatus s) {
    this.subscribers.add(s);
  }
}