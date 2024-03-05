package cs3500.reversi.adapter;

import cs3500.reversi.controller.PlayerActions;
import cs3500.reversi.model.ObservableReversiModel;
import cs3500.reversi.provider.view.ReversiView;

/**
 * Adapts calls to the view.
 */
public class ViewAdapter implements cs3500.reversi.view.ReversiView {
  ReversiView view;
  ObservableReversiModel self;

  /**
   * Creates a view adapter.
   * @param v the view
   * @param self the model
   */
  public ViewAdapter(ReversiView v, ObservableReversiModel self) {
    this.view = v;
    view.enableInput();
    this.self = self;
  }

  /**
   * Renders the state of the reversi game.
   */
  @Override
  public void render() {
    view.repaint();
  }

  /**
   * Updates the display, re-rendering the state of the game.
   */
  @Override
  public void update() {
    view.repaint();
  }

  /**
   * Indicates that a requested move was not valid.
   * To be displayed to the user!
   */
  @Override
  public void invalidMove() {
    view.showMessageDialog("Invalid Move!");
  }

  @Override
  public void notYourTurn() {
    view.showMessageDialog("Not Your Turn!");
  }

  /**
   * Adds a listener to player actions made through the view.
   *
   * @param s the listener being added to the list of subscribers.
   */
  @Override
  public void addListener(PlayerActions s) {
    view.addFeatureListener(new ControllerAdapter(s, self));
  }
}
