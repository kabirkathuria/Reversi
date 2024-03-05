package cs3500.reversi;

import org.junit.Assert;
import org.junit.Test;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.HexagonalReversiModel;
import cs3500.reversi.model.MockModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.AvoidCornerNeighbors;
import cs3500.reversi.strategy.DynamicStrat;
import cs3500.reversi.strategy.InFallibleStrat;
import cs3500.reversi.strategy.Infallible;
import cs3500.reversi.strategy.MaximizeFlips;
import cs3500.reversi.strategy.PlayCorners;
import cs3500.reversi.strategy.TryN;
import cs3500.reversi.view.ReversiGuiView;
import cs3500.reversi.view.ReversiTextualView;
import cs3500.reversi.view.ReversiView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the player strategy component of reversi.
 */
public class ReversiStrategyTests {

  @Test
  public void testStrategy1Simple() {
    ReversiModel model = new HexagonalReversiModel(6);
    InFallibleStrat strategy = new Infallible(new MaximizeFlips());
    // this is the move we expect the strategy to make
    assertEquals(new Coordinate(6, 3),
            (strategy.chooseMove(model)));
    // make the move
    model.move(strategy.chooseMove(model));
    // now it's white's turn, so we expect it to make this move as it flips 2 black tiles.
    DynamicStrat s = new MaximizeFlips();
    assertEquals(new Coordinate(7, 2),
            strategy.chooseMove(model));
    // make the move
    model.move(strategy.chooseMove(model));
    // piece should be there and be the right color
    assertTrue(model.hasPiece(new Coordinate(7, 2)));
    assertFalse(model.isTileBlack(new Coordinate(7, 2)));
    // so you can understand what moves were just played :)
    StringBuilder sb = new StringBuilder();
    ReversiView view = new ReversiTextualView(model, sb);
    view.render();
    String board =
            "     _ _ _ _ _ _\n" +
                    "    _ _ _ _ _ _ _\n" +
                    "   _ _ _ _ O _ _ _\n" +
                    "  _ _ _ _ O _ _ _ _\n" + // first move (black) was on this row, now captured
                    " _ _ _ _ O X _ _ _ _\n" +
                    "_ _ _ _ O _ X _ _ _ _\n" +
                    " _ _ _ _ X O _ _ _ _\n" +
                    "  _ _ _ _ _ _ _ _ _\n" +
                    "   _ _ _ _ _ _ _ _\n" +
                    "    _ _ _ _ _ _ _\n" +
                    "     _ _ _ _ _ _\n";
    assertEquals(board, sb.toString());
  }

  // don't try this with board size > 10... it takes a while.
  // This strategy is super simple and just uses brute force.
  @Test
  public void testStrategy1Automated() {
    ReversiModel model = new HexagonalReversiModel(6);
    InFallibleStrat strategy = new Infallible(new MaximizeFlips());
    StringBuilder sb = new StringBuilder();
    ReversiView view = new ReversiTextualView(model, sb);
    while (true) {
      Coordinate move;
      try {
        move = strategy.chooseMove(model);
      } catch (IllegalArgumentException e) {
        break;
      }
      model.move(move);
    }
    // when the strategy returns null, that player has no legal moves.
    assertFalse(model.anyValidMoves());
    // play until the game is over, passing when the current player doesn't have a move
    while (!model.isGameOver()) {
      try {
        model.move(strategy.chooseMove(model));
      } catch (IllegalArgumentException e) {
        model.pass();
      }
    }
    assertTrue(model.isGameOver());
    assertFalse(model.anyValidMoves());
    view.render();
    // for viewing pleasure :)
    String board =
            "     X X X X X X\n" +
            "    X _ O _ X _ O\n" +
            "   X X X X X X O O\n" +
            "  X _ O _ O _ X _ O\n" +
            " X O O O O O O X X O\n" +
            "_ _ O _ X _ O _ X _ O\n" +
            " X X X X X X X X O O\n" +
            "  X _ O _ O _ X _ O\n" +
            "   X X X X X X X O\n" +
            "    X _ X _ X _ O\n" +
            "     X X X X X X\n";
    assertEquals(board, sb.toString());
  }

  @Test
  public void proVSnoob() {
    InFallibleStrat pro = new Infallible(new TryN(
            new PlayCorners(),
            new AvoidCornerNeighbors(),
            new MaximizeFlips()));
    InFallibleStrat noob = new Infallible(
            new MaximizeFlips());
    ReversiModel model = new HexagonalReversiModel();
    StringBuilder sb = new StringBuilder();
    ReversiView view = new ReversiTextualView(model, sb);
    InFallibleStrat strategy = noob;
    while (!model.isGameOver()) {
      try {
        Coordinate move = strategy.chooseMove(model);
        model.move(move);
      } catch (IllegalArgumentException e) {
        model.pass();
      }
      // change player
      if (strategy == noob) {
        strategy = pro;
      } else {
        strategy = noob;
      }
    }
    view.render();

    Assert.assertEquals(16, model.getBlackScore());
    Assert.assertEquals(13, model.getWhiteScore());
    ReversiView gui = new ReversiGuiView(model);
    gui.render();
  }

  @Test
  public void strategy1TranscriptOneMoveBaseGame() {
    ReversiModel model = new HexagonalReversiModel(6);
    MockModel mock = new MockModel(model);
    InFallibleStrat strategy = new Infallible(new MaximizeFlips());
    strategy.chooseMove(mock);
    // console spam if you uncomment
    // System.out.println(mock.report());
    Assert.assertEquals(362, mock.report().split("\n").length);
  }
}
