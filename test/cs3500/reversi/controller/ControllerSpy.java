package cs3500.reversi.controller;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.view.ReversiView;

/**
 * Spy controller, intercepting and logging notifications from the players and model.
 * Useful for testing.
 */
public class ControllerSpy implements Controller {
  // the log
  StringBuilder sb = new StringBuilder();

  /**
   * Constructs a new spy, and adds itself to the listener list of the model and view.
   * @param m the model to subscribe to.
   * @param v the view to subscribe to.
   */
  public ControllerSpy(ReversiModel m, ReversiView v) {
    m.addListener(this);
    v.addListener(this);
    sb.append("Eavesdropping...\n");
  }

  @Override
  public void notifyTurn(boolean isBlackTurn) {
    sb.append("Model Notification: turn change, black to play: ").append(isBlackTurn).append("\n");
  }

  @Override
  public void requestMove(Coordinate coord) {
    sb.append("Player Notification, request move at ").append(coord).append("\n");
  }

  @Override
  public void requestPass() {
    sb.append("Player Notification, request pass.\n");
  }

  /**
   * Prints the spy's log of notifications.
   * @return all the notifications received from the model and view thus far.
   */
  public String printLog() {
    return sb.toString();
  }
}
