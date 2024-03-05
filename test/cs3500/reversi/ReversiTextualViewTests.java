package cs3500.reversi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.HexagonalReversiModel;
import cs3500.reversi.view.ReversiTextualView;
import cs3500.reversi.view.ReversiView;

/**
 * Tests render's output in the textual view. Relatively simple.
 */
public class ReversiTextualViewTests {
  private HexagonalReversiModel model;
  private ReversiView view;
  private StringBuilder mockOutput;

  /**
   * Sets up the game model before each test.
   * Initializes the model with a board of size 7.
   */
  @Before
  public void setUp() {
    model = new HexagonalReversiModel(4);
    mockOutput = new StringBuilder();
    view = new ReversiTextualView(model, mockOutput);
  }

  /* our coordinate system:
   *
   *    3 4 5 6     row 0
   *   2 _ _ _ _    row 1
   *  1 _ X O _ _   row 2
   * 0 _ O _ X _ 6  row 3
   *  _ _ X O _ 5   row 4
   *   _ _ _ _ 4    row 5
   *    0 1 2 3     row 6
   */

  @Test
  public void testOutputMatchesExpected() {
    // render the default game state, with just white and black pieces in the center in a circle
    // to the StringBuilder, so we can test it
    view.render();
    Assert.assertEquals(
            "   _ _ _ _" + "\n" +
                    "  _ _ _ _ _" + "\n" +
                    " _ _ X O _ _" + "\n" +
                    "_ _ O _ X _ _" + "\n" +
                    " _ _ X O _ _" + "\n" +
                    "  _ _ _ _ _" + "\n" +
                    "   _ _ _ _" + "\n",
            mockOutput.toString());
  }

  @Test
  public void testMoveDisplayed() {
    // render the default game state, with just white and black pieces in the center in a circle
    // to the StringBuilder, so we can test it
    model.move(new Coordinate(2, 2));
    view.render();
    Assert.assertEquals(
            "   _ _ _ _" + "\n" +
                    "  _ _ _ _ _" + "\n" +
                    " _ X X O _ _" + "\n" +
                    "_ _ X _ X _ _" + "\n" +
                    " _ _ X O _ _" + "\n" +
                    "  _ _ _ _ _" + "\n" +
                    "   _ _ _ _" + "\n",
            mockOutput.toString());
  }
}
