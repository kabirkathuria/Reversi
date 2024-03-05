package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A game of reversi on a square board, where a player can capture along 4 axes (8 directions)
 * instead of 3 axes (6 directions) like in hexagonal reversi.
 */
public class SquareReversiModel extends AbstractReversiModel {
  // the possible move direction vectors in a game of square reversi.
  private static final List<Coordinate> directions = new ArrayList<>(Arrays.asList(
          new Coordinate(0, -1),
          new Coordinate(1, -1),
          new Coordinate(1, 0),
          new Coordinate(1, 1),
          new Coordinate(0, 1),
          new Coordinate(-1, 1),
          new Coordinate(-1, 0),
          new Coordinate(-1, -1)));

  /**
   * Constructs a new Square reversi board of given sidelength.
   *
   * @param sideLength the side length of the board. Must be even and at least 2!
   */
  public SquareReversiModel(int sideLength) {
    super(directions);
    if (sideLength < 2) {
      throw new IllegalArgumentException("Cannot have a board that small!");
    }
    if (sideLength % 2 != 0) {
      throw new IllegalArgumentException("Cannot have a board with odd side length");
    }
    board = new Player[sideLength][sideLength];
    initBoard();
  }

  /**
   * Creates a deep copy of a given model.
   *
   * @param m the model to be copied.
   */
  public SquareReversiModel(SquareReversiModel m) {
    super(directions);
    int height = m.getBoardHeight();
    int width = m.getBoardWidth();
    board = new Player[width][height];
    turn = m.isBlackTurn() ? Player.BLACK : Player.WHITE;
    gameOver = m.isGameOver();
    // currently not exposing passed. todo maybeee???
    Coordinate coord;
    for (int r = 0; r < height; r++) {
      for (int q = 0; q < width; q++) {
        coord = new Coordinate(q, r);
        try {
          if (m.hasPiece(coord)) {
            // yes this coord.q/r is redundant, but it makes it easier to change later
            board[coord.c][coord.r] = m.isTileBlack(coord) ? Player.BLACK : Player.WHITE;
          }
        } catch (IllegalArgumentException ignored) {
        }
      }
    }
  }

  /**
   * Initializes the game board with default positions for Black and White players.
   */
  private void initBoard() {
    Coordinate center = new Coordinate(board.length / 2 - 1, board.length / 2 - 1);
    board[center.c][center.r] = Player.BLACK;
    board[center.c + 1][center.r] = Player.WHITE;
    board[center.c + 1][center.r + 1] = Player.BLACK;
    board[center.c][center.r + 1] = Player.WHITE;
  }

  /**
   * Returns whether a hexagonal coordinate is in bounds for the board.
   *
   * @param coordinate the coordinate being checked.
   * @return true, iff the coordinate is a valid tile in the bounds of the board.
   */
  @Override
  public boolean isInBounds(Coordinate coordinate) {
    return (coordinate.c < board.length && coordinate.c >= 0
            && coordinate.r < board.length && coordinate.r >= 0);
  }

  /**
   * Returns the resulting score of making a move at a given position.
   *
   * @return the number of tiles flipped by the move.
   */
  @Override
  public int potentialScore(Coordinate coord) {
    SquareReversiModel temp = new SquareReversiModel(this);
    temp.move(coord);
    if (this.isBlackTurn()) {
      return Math.abs(this.getWhiteScore() - temp.getWhiteScore());
    } else {
      return Math.abs(this.getBlackScore() - temp.getBlackScore());
    }
  }

  /**
   * Gets corners.
   *
   * @return a list of corner coordinates.
   */
  public List<Coordinate> getCorners() {
    int sideLen = getBoardWidth();
    return new ArrayList<>(Arrays.asList(
            new Coordinate(0, 0), // top left
            new Coordinate(sideLen, 0), // top right
            new Coordinate(0, sideLen), // bot left
            new Coordinate(sideLen, sideLen))); // bot right
  }

  /**
   * Returns the possible move direction vectors for the board.
   *
   * @return the move (unit) vectors.
   */
  @Override
  public List<Coordinate> getDirections() {
    return directions;
  }
}
