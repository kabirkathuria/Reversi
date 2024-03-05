package cs3500.reversi;

import org.junit.Before;
import org.junit.Test;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.HexagonalReversiModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the public interface methods of the HexagonalReversi class.
 * Validates the behavior of the game model's core functionalities.
 */
public class ReversiModelInterfaceTests {

  private HexagonalReversiModel model;

  /**
   * Sets up the game model before each test.
   * Initializes the model with a board of size 7.
   */
  @Before
  public void setUp() {
    model = new HexagonalReversiModel(4);
  }

  @Test
  public void testGameIsOver() {
    assertTrue(model.anyValidMoves());
    assertTrue(model.isLegal(new Coordinate(2, 2)));
    assertFalse(model.isGameOver());
    model.pass();
    model.pass();
    assertTrue(model.isGameOver());
    assertFalse(model.anyValidMoves());
  }

  @Test
  public void testGameNotOver() {
    assertFalse(model.isGameOver());
    model.pass();
    model.move(new Coordinate(2, 2));
    model.pass();
    assertFalse(model.isGameOver());
  }

  @Test
  public void testGetPlayerAt() {
    // center of the board, no piece placed here, but it's a valid tile.
    assertFalse(model.hasPiece(new Coordinate(3, 3)));
    // the pieces in the starting positions, going clockwise around the circle
    // starting at the leftmost piece.
    assertFalse(model.isTileBlack(new Coordinate(2, 3)));
    assertTrue(model.isTileBlack(new Coordinate(3, 2)));
    assertFalse(model.isTileBlack(new Coordinate(4, 2)));
    assertTrue(model.isTileBlack(new Coordinate(4, 3)));
    assertFalse(model.isTileBlack(new Coordinate(3, 4)));
    assertTrue(model.isTileBlack(new Coordinate(2, 4)));
    // this is not on the board! it's an invalid coordinate.
    assertThrows(IllegalArgumentException.class,
        () -> model.isTileBlack(new Coordinate(0, 0)));
  }

  @Test
  public void testGetBoardSize() {
    assertEquals(7, model.getBoardHeight());
  }

  /**
   * Tests that a couple of simple moves are valid or invalid, and that getPlayerAt works properly.
   */
  @Test
  public void testMove() {
    // there shouldn't be anything at this position on the board.
    assertFalse(model.hasPiece(new Coordinate(2, 2)));
    // there should be a white piece at 2,3. This is important later, it gets flipped!
    assertFalse(model.isTileBlack(new Coordinate(2, 3)));
    /* This is the current board state here.
     *    _ _ _ _
     *   _ _ _ _ _
     *  _ _ ○ ● _ _
     * _ _ ● _ ○ _ _
     *  _ _ ○ ● _ _
     *   _ _ _ _ _
     *    _ _ _ _
     */
    // this is a legal move, so a piece should be placed without throwing anything.
    model.move(new Coordinate(2, 2));
    /* It has now changed to this:
     *    _ _ _ _
     *   _ _ _ _ _
     *  _ ○ ○ ● _ _
     * _ _ ○ _ ○ _ _
     *  _ _ ○ ● _ _
     *   _ _ _ _ _
     *    _ _ _ _
     */
    // the piece placed should be black, as black is the first to move.
    assertTrue(model.isTileBlack(new Coordinate(2, 2)));
    // the piece next to it should be black also, as we just flipped it.
    assertTrue(model.isTileBlack(new Coordinate(2, 3)));
    // there shouldn't be anything at this position on the board (the center)
    assertFalse(model.hasPiece(new Coordinate(3, 3)));
    // this move is NOT legal for white because there is no enemy tile being "jumped".
    assertThrows(IllegalStateException.class, () -> model.move(new Coordinate(3, 3)));
  }

  @Test
  public void testPass() {
    // we should be allowed to pass at any time.
    model.pass();
    // it is now white's turn, so this move should place a white tile.
    model.move(new Coordinate(2, 2));
    // note that this is WHITE, whereas in the other test, it was black.
    assertFalse(model.isTileBlack(new Coordinate(2, 2)));
  }

  @Test
  public void testGetScore() {
    // at the start of the game, there's the same number of white and black tiles.
    assertEquals(3, model.getBlackScore());
    assertEquals(0, model.getBlackScore() - model.getWhiteScore());
    model.move(new Coordinate(2, 2));
    /* It has now changed to this:
     *    _ _ _ _
     *   _ _ _ _ _
     *  _ ○ ○ ● _ _
     * _ _ ○ _ ○ _ _
     *  _ _ ○ ● _ _
     *   _ _ _ _ _
     *    _ _ _ _
     * 2 white tiles, 5 black tiles.
     */
    // the score is now +3 (black) from this move.
    assertEquals(5, model.getBlackScore());
    assertEquals(3, model.getBlackScore() - model.getWhiteScore());
    model.move(new Coordinate(1, 2));
    /* It has now changed to this:
     *    _ _ _ _
     *   _ _ _ _ _
     *  ● ● ● ● _ _
     * _ _ ○ _ ○ _ _
     *  _ _ ○ ● _ _
     *   _ _ _ _ _
     *    _ _ _ _
     * 5 white tiles, 3 black tiles.
     */
    // the score is now -2, meaning that white has two more tiles than black.
    assertEquals(3, model.getBlackScore());
    assertEquals(-2, model.getBlackScore() - model.getWhiteScore());
  }

  @Test
  public void testIllegalMove() {
    assertThrows(IllegalArgumentException.class, () -> model.move(new Coordinate(-1, -1)));
  }

  @Test
  public void testInvalidSize() {
    assertThrows(IllegalArgumentException.class, () -> new HexagonalReversiModel(1));
  }
}