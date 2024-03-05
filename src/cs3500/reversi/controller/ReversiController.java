package cs3500.reversi.controller;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.view.ReversiView;

/**
 * Represents a normal controller.
 */
public class ReversiController implements Controller {
  ReversiModel model;
  ReversiView view;
  GenericPlayer player;
  boolean started = false;
  boolean blackTurn = true;

  /**
   * Constructs a controller connected to the given model, listening to notifications from the
   * provided view and player, and serving the player and view updates.
   * @param m the model
   * @param p the player
   * @param v the view
   */
  public ReversiController(ReversiModel m, GenericPlayer p, ReversiView v) {
    this.model = m;
    this.view = v;
    this.player = p;
    model.addListener(this);
    view.addListener(this);
    player.addListener(this);
    view.render();
  }

  @Override
  public void notifyTurn(boolean isBlackTurn) {
    started = true;
    blackTurn = isBlackTurn;
    if (blackTurn == player.isBlack() && !model.isGameOver()) {
      view.update();
      player.yourTurn();
    }
  }

  @Override
  public void requestMove(Coordinate coord) {
    if (started && player.isBlack() != blackTurn && !model.isGameOver()) {
      view.notYourTurn();
    } else if (started) {
      try {
        model.move(coord);
      } catch (IllegalStateException e) {
        view.invalidMove();
      }
    }
    view.update();
  }

  @Override
  public void requestPass() {
    if (started && player.isBlack() != blackTurn && !model.isGameOver()) {
      view.notYourTurn();
    } else if (started && !model.isGameOver()) {
      model.pass();
      view.update();
    }
  }
}
