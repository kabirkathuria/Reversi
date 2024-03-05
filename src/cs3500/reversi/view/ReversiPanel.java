package cs3500.reversi.view;

import cs3500.reversi.controller.PlayerActions;

/**
 * Operations a reversi panel must support for adding and notifying subscribers.
 */
public interface ReversiPanel {

  /**
   * adds a subscriber to the list.
   * @param s the subscriber.
   */
  void addListener(PlayerActions s);
}
