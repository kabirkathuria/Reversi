package cs3500.reversi;

import org.junit.Before;
import org.junit.Test;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;
import cs3500.reversi.model.SquareReversiModel;
import cs3500.reversi.strategy.InFallibleStrat;
import cs3500.reversi.strategy.Infallible;
import cs3500.reversi.strategy.MaximizeFlips;
import cs3500.reversi.view.ReversiSquareTextualView;
import cs3500.reversi.view.ReversiView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the square reversi model.
 */
public class ReversiSquareTests {

  private SquareReversiModel model;

  /**
   * Sets up the game model before each test.
   * Initializes the model with a board of size 7.
   */
  @Before
  public void setUp() {
    model = new SquareReversiModel(4);
  }

  @Test
  public void testUniqueMoves() {
    StringBuilder sb = new StringBuilder();
    ReversiView view = new ReversiSquareTextualView(model, sb);
    view.render();

    // initial board state
    assertEquals(
            "____\n" +
                    "_XO_\n" +
                    "_OX_\n" +
                    "____\n",
            sb.toString());

    model.move(new Coordinate(3, 1));
    // doesn't throw, piece places properly.
    assertTrue(model.hasPiece(new Coordinate(3, 1)));

    // allows diagonal moves in +-
    model.move(new Coordinate(3, 0));
    assertTrue(model.hasPiece(new Coordinate(3, 0)));

    sb = new StringBuilder();
    view = new ReversiSquareTextualView(model, sb);
    view.render();

    assertEquals(
            "___O\n" +
                    "_XOX\n" +
                    "_OX_\n" +
                    "____\n",
            sb.toString());

    model.pass();
    model.move(new Coordinate(2, 3));

    // diagonal ++ move in ++ corner, something not possible in hexagonal reversi
    // in both location and direction.
    model.move(new Coordinate(3, 3));

    sb = new StringBuilder();
    view = new ReversiSquareTextualView(model, sb);
    view.render();

    assertEquals(
            "___O\n" +
                    "_XOX\n" +
                    "_OX_\n" +
                    "__OX\n",
            sb.toString());
  }

  @Test
  public void testSquareStrategy() {
    InFallibleStrat strategy = new Infallible(new MaximizeFlips());
    assertEquals(new Coordinate(2, 0), strategy.chooseMove(model));
    model.move(strategy.chooseMove(model));
    assertEquals(new Coordinate(1, 0), strategy.chooseMove(model));
    model.move(strategy.chooseMove(model));
    assertEquals(new Coordinate(0, 0), strategy.chooseMove(model));
    model.move(strategy.chooseMove(model));
    assertEquals(new Coordinate(3, 0), strategy.chooseMove(model));

    StringBuilder sb = new StringBuilder();
    ReversiView view = new ReversiSquareTextualView(model, sb);
    view.render();
    assertEquals(
            "XXX_\n" +
                    "_XX_\n" +
                    "_OX_\n" +
                    "____\n",
            sb.toString());
  }

  @Test
  public void boundsTesting() {
    // no odd
    assertThrows(IllegalArgumentException.class, () -> new SquareReversiModel(3));
    // no 0
    assertThrows(IllegalArgumentException.class, () -> new SquareReversiModel(0));
    // no negative
    assertThrows(IllegalArgumentException.class, () -> new SquareReversiModel(-8));
    // doesn't throw
    ObservableReversiModel m = new SquareReversiModel(8);
    int r;
    int c;
    r = c = 0;
    for (r = 0; r < 8; r++) {
      for (c = 0; c < 8; c++) {
        Coordinate coord = new Coordinate(c, r);
        assertTrue(m.isInBounds(coord));
      }
    }
    r = 8;
    for (c = 0; c < 9; c++) {
      Coordinate coord = new Coordinate(c, r);
      assertFalse(m.isInBounds(coord));
    }
  }
}
