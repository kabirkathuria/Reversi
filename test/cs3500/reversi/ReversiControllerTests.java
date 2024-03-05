package cs3500.reversi;

import org.junit.Before;
import org.junit.Test;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import cs3500.reversi.controller.Controller;
import cs3500.reversi.controller.ControllerSpy;
import cs3500.reversi.controller.HumanPlayer;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.model.HexagonalReversiModel;
import cs3500.reversi.view.ReversiGuiView;
import cs3500.reversi.view.ReversiView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the controller.
 */
public class ReversiControllerTests {
  private HexagonalReversiModel model;

  @Before
  public void setUp() {
    model = new HexagonalReversiModel(6);
  }

  /**
   * Tests the controller by making legitimate mouse clicks on the view.
   * Don't move your mouse! Might not be reproducible on other systems.
   * I'm not really sure how robot works.
   */
  @Test
  public void testClicksTransmitted() throws AWTException, InterruptedException {
    HumanPlayer p = new HumanPlayer(true);
    ReversiView v = new ReversiGuiView(model);
    Controller controller = new ReversiController(model, p, v);
    ControllerSpy spy = new ControllerSpy(model, v);
    Robot robot = new Robot();
    model.startGame();
    robot.mouseMove(210, 230);
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    Thread.sleep(40);
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    Thread.sleep(180);
    robot.keyPress(KeyEvent.VK_M);
    Thread.sleep(40);
    robot.keyRelease(KeyEvent.VK_M);
    Thread.sleep(180);
    // not the player's turn! they should not be able to move.
    robot.mouseMove(300, 170);
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    Thread.sleep(40);
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    Thread.sleep(200);
    robot.keyPress(KeyEvent.VK_M);
    Thread.sleep(40);
    robot.keyRelease(KeyEvent.VK_M);
    Thread.sleep(300);
    // clicking the "illegal move" dialog ok.
    robot.mouseMove(285, 293);
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    Thread.sleep(40);
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    Thread.sleep(180);
    // passing
    robot.keyPress(KeyEvent.VK_P);
    Thread.sleep(40);
    robot.keyRelease(KeyEvent.VK_P);
    Thread.sleep(180);
    String log = spy.printLog();
    assertTrue(log.contains("request move at Coordinate: c: 4, r: 4"));
    assertTrue(log.contains("black to play: true"));
    assertTrue(log.contains("request move at Coordinate: c: 6, r: 3"));
    // the turn should be notified
    // once at the start of the game
    // and once with the first move, as it is legal.
    // the following moves should not do anything.
    int count = 0;
    for (String s : log.split("\\n")) {
      if (s.contains("black to play")) {
        count += 1;
      }
    }
    assertEquals(count, 2);
  }
}
