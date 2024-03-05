package cs3500.reversi.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;
import cs3500.reversi.provider.model.Disc;
import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.ReversiReadOnlyModel;

/**
 * Adapts calls to the model.
 */
public class ModelAdapter implements ReversiReadOnlyModel {
  ObservableReversiModel model;

  public ModelAdapter(ObservableReversiModel m) {
    this.model = m;
  }

  /**
   * Determines if the game is over. A game is over if either player is unable to place a disc.
   *
   * @return true if the game is over, otherwise false.
   * @throws IllegalStateException if the game has not been started yet.
   */
  @Override
  public boolean isGameOver() throws IllegalStateException {
    return model.isGameOver();
  }

  /**
   * Determines which color won the game once a game is over.
   * If a game is not over return a none disc.
   *
   * @return The disc of the winner.
   * @throws IllegalStateException if the game has not been started yet.
   */
  @Override
  public Disc gameWinner() throws IllegalStateException {
    if (!model.isGameOver()) {
      return Disc.NONE;
    }
    if (model.getWhiteScore() > model.getBlackScore()) {
      return Disc.WHITE;
    }
    return Disc.BLACK;
  }

  /**
   * Gets a disc of the current turn. (i.e. the disc that will be placed next)
   *
   * @return a disc of the current turn.
   * @throws IllegalStateException if the game has not been started yet.
   */
  @Override
  public Disc getCurrentTurn() throws IllegalStateException {
    if (model.isBlackTurn()) {
      return Disc.BLACK;
    }
    return Disc.WHITE;
  }

  /**
   * Gets the size of the board.
   * In the case of hexagon Reversi, it gets the number of layers around
   * the center hexagon.
   *
   * @return the size of the board.
   * @throws IllegalStateException if the game has not been started yet.
   */
  @Override
  public int getBoardSize() throws IllegalStateException {
    return model.getBoardWidth() / 2;
  }

  /**
   * Gets the disc at the specified column and row from a 0-based index.
   *
   * @param q the column
   * @param r the row
   * @return the disc at the given position
   * @throws IllegalStateException    if the game has not been started yet.
   * @throws IllegalArgumentException if column or row is invalid.
   */
  @Override
  public Disc getDiscAt(int q, int r) throws IllegalStateException, IllegalArgumentException {
    Coordinate coord = convertCoord(q, r);
    if (!model.hasPiece(coord)) {
      return Disc.NONE;
    }
    if (model.isTileBlack(coord)) {
      return Disc.BLACK;
    }
    return Disc.WHITE;
  }

  /**
   * Determines if the current player's move is valid at the specified column and row.
   *
   * @param q the column
   * @param r the row
   * @return true if the move is valid false otherwise.
   * @throws IllegalStateException    if the game has not been started yet.
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  @Override
  public boolean validMove(int q, int r) throws IllegalStateException, IllegalArgumentException {
    return model.isLegal(convertCoord(q, r));
  }

  /**
   * Determines if the current player has any valid moves.
   *
   * @param player the disc of the player
   * @return true if there is a valid move, false otherwise.
   * @throws IllegalStateException if the game has not been started yet.
   */
  @Override
  public boolean hasValidMove(Disc player) throws IllegalStateException {
    // why is the player given here? checking if there's any valid moves
    // when it's not that player's turn seems pointless...
    return model.anyValidMoves();
  }

  /**
   * Determines the score of the given disc.
   * The score is the total number of discs of the given disc on the board.
   *
   * @param player disc of player
   * @return score as an integer
   * @throws IllegalStateException if the game has not been started yet.
   */
  @Override
  public int getScore(Disc player) throws IllegalStateException {
    if (player == Disc.WHITE) {
      return model.getWhiteScore();
    }
    if (player == Disc.BLACK) {
      return model.getBlackScore();
    }
    return 0;
  }

  /**
   * Gets the current board state and returns it as a copy.
   *
   * @return current board state
   * @throws IllegalStateException if the game has not been started yet.
   */
  @Override
  public Map<Hex, Disc> getBoard() throws IllegalStateException {
    Map<Hex, Disc> ans = new HashMap<>();
    Coordinate coord;
    Hex provcoord;
    for (int r = 0; r < model.getBoardHeight(); r++) {
      for (int q = 0; q < model.getBoardWidth(); q++) {
        coord = new Coordinate(q, r);
        provcoord = toProvHex(coord);
        if (model.isInBounds(coord)) {
          if (!model.hasPiece(coord)) {
            ans.put(provcoord, Disc.NONE);
          }
          else if (model.isTileBlack(coord)) {
            ans.put(provcoord, Disc.BLACK);
          }
          else {
            ans.put(provcoord, Disc.WHITE);
          }
        }
      }
    }
    return ans;
  }

  /**
   * Gets a list of all valid moves for the given disc.
   *
   * @param player disc of player
   * @return a list of all valid moves for the current colors turn.
   * @throws IllegalStateException if the game has not been started yet.
   */
  @Override
  public List<Hex> getValidMoves(Disc player) throws IllegalStateException {
    ArrayList<Hex> moves = new ArrayList<>();
    Coordinate coord;
    for (int r = 0; r < model.getBoardHeight(); r++) {
      for (int q = 0; q < model.getBoardWidth(); q++) {
        coord = new Coordinate(q, r);
        if (model.isLegal(coord)) {
          moves.add(toProvHex(coord));
        }
      }
    }
    return moves;
  }

  /**
   * Gets get number of discs that will be captured given coordinates and a disc.
   *
   * @param q      the q coordinate
   * @param r      the r coordinate
   * @param player disc of move
   * @return number of discs that will be captured from the given move
   */
  @Override
  public int discsCaptured(int q, int r, Disc player) {
    return model.potentialScore(convertCoord(q, r));
  }

  /**
   * Converts a provider's coordinate to ours for calls to our model impl.
   * @param q the q in axial where 0,0 is the center of the board.
   * @param r the r in axial where 0,0 is the center of the board.
   * @return the new HexaCoord where 0,0 doesn't exist because I'm evil.
   */
  private Coordinate convertCoord(int q, int r) {
    int size = model.getBoardHeight() / 2;
    return new Coordinate(q + size, r + size);
  }

  /**
   * Converts from our coordinate system to the provider's coordinate system.
   * @param c a coordinate from our system (stupid)
   * @return the provider Hex coord (axial where 0,0 is the center)
   */
  private Hex toProvHex(Coordinate c) {
    int q = c.c - model.getBoardWidth() / 2;
    int r = c.r - model.getBoardHeight() / 2;
    return new Hex(q, r, -q - r);
  }
}
