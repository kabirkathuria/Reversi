package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the model for a Reversi game played on a hexagonal grid.
 * Implements the MutableReversiModel to offer core game functionalities such as making moves,
 * checking game state, and accessing board details. View the README for more in depth information
 * on how to actually play reversi or use the model!
 */
public class HexagonalReversiModel extends AbstractReversiModel {
  private static final List<Coordinate> directions = new ArrayList<>(Arrays.asList(
          new Coordinate(0, -1),
          new Coordinate(1, -1),
          new Coordinate(1, 0),
          new Coordinate(0, 1),
          new Coordinate(-1, 1),
          new Coordinate(-1, 0)));

  /**
   * No-arg constructor; Creates a default game of reversi with board size 7.
   */
  public HexagonalReversiModel() {
    this(4);
  }

  /**
   * Creates a game of reversi on a hexagonal board of specified size.
   *
   * @param sideLength the size of the board, in number of tiles, at its widest point.
   *                  Must be odd.
   */
  public HexagonalReversiModel(int sideLength) {
    super(directions);
    if (sideLength < 2) {
      throw new IllegalArgumentException("Cannot have a board that small!");
    }
    // INVARIANT: the board will always have an odd number of 'columns' and rows.
    board = new Player[sideLength * 2 - 1][sideLength * 2 - 1];
    initBoard();
  }

  /**
   * Creates a deep copy of a given model.
   * @param m the model to be copied.
   */
  public HexagonalReversiModel(ObservableReversiModel m) {
    super(directions);
    board = new Player[m.getBoardWidth()][m.getBoardHeight()];
    turn = m.isBlackTurn() ? Player.BLACK : Player.WHITE;
    gameOver = m.isGameOver();
    // currently not exposing passed. todo maybeee???
    int height = m.getBoardHeight();
    int width = m.getBoardWidth();
    Coordinate coord;
    for (int r = 0; r < height; r++) {
      for (int q = 0; q < width; q++) {
        coord = new Coordinate(q, r);
        try {
          if (m.hasPiece(coord)) {
            // yes this coord.q/r is redundant, but it makes it easier to change later
            board[coord.c][coord.r] = m.isTileBlack(coord) ? Player.BLACK : Player.WHITE;
          }
        } catch (IllegalArgumentException ignored) { }
      }
    }
  }

  /**
   * Initializes the game board with default positions for Black and White players.
   */
  private void initBoard() {
    Coordinate center = new Coordinate(board.length / 2, board.length / 2);
    Coordinate offset;
    for (int i = 0; i < directions.size(); i++) {
      offset = directions.get(i);
      board[center.c + offset.c][center.r + offset.r] = turn;
      super.changeTurn();
    }
  }

  /**
   * Checks if the given coordinate is within the bounds of the hexagonal board
   * (not just within the bounds of the 2d-array).
   *
   * @param coordinate the position to check
   * @return true if the coordinate is within bounds, false otherwise
   */
  @Override
  public boolean isInBounds(Coordinate coordinate) {
    return (coordinate.c + coordinate.r <= ((board.length - 1) * 1.5)) &&
            (coordinate.c + coordinate.r >= ((board.length - 1) / 2)) &&
            (0 <= coordinate.c && coordinate.c < board.length) &&
            (0 <= coordinate.r && coordinate.r < board.length);
  }

  /**
   * Returns the resulting score of making a move at a given position.
   * @return the number of tiles flipped by the move.
   */
  @Override
  public int potentialScore(Coordinate coord) {
    AbstractReversiModel temp = new HexagonalReversiModel(this);
    temp.move(coord);
    if (this.isBlackTurn()) {
      return Math.abs(this.getWhiteScore() - temp.getWhiteScore());
    } else {
      return Math.abs(this.getBlackScore() - temp.getBlackScore());
    }
  }

  /**
   * Gets corners.
   * @return a list of corner coordinates.
   */
  public List<Coordinate> getCorners() {
    int sideLen = getBoardWidth() / 2 - 1;
    return new ArrayList<>(Arrays.asList(
            new Coordinate(sideLen - 1, 0), // top left
            new Coordinate(2 * (sideLen - 1), 0), // top right
            new Coordinate(0, sideLen - 1), // mid left
            new Coordinate((2 * sideLen) - 1, sideLen), // mid right
            new Coordinate(0, (2 * sideLen) - 1), // bot left
            new Coordinate((2 * sideLen) - 1, (2 * sideLen) - 1))); // bot right
  }

  @Override
  public List<Coordinate> getDirections() {
    return directions;
  }
}