package cs3500.reversi.view;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import cs3500.reversi.controller.PlayerActions;

/**
 * an abstract panel supporting JPanel functions that allows a list of PlayerActions to subscribe.
 */
public class AbstractPanel extends JPanel implements ReversiPanel {
  Set<PlayerActions> subscribers = new HashSet<>();

  /**
   * Adds a listener to the actions performed on this panel.
   *
   * @param s the subscriber being added.
   */
  public void addListener(PlayerActions s) {
    this.subscribers.add(s);
  }
}
