package cs3500.reversi.controller;

import java.util.HashSet;
import java.util.Set;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;
import cs3500.reversi.strategy.InFallibleStrat;

/**
 * Represents a Machine player for a game of reversi, using a customizable strategy and playing
 * with a set color against an opponent through a controller.
 */
public class MachinePlayer implements GenericPlayer {
  // the strategy to be used.
  private final InFallibleStrat strategy;
  // what color this robot is playing as.
  private final boolean isBlack;
  // the model the robot is using to calculate its moves.
  private final ObservableReversiModel model;
  // the list of subscribers to be notified when the robot decides on a move.
  Set<PlayerActions> subscribers = new HashSet<>();

  /**
   * Constructs a machine player.
   * @param black the color the machine will play for.
   * @param s the strategy the machine will use.
   * @param m the model the machine will make decisions on.
   */
  public MachinePlayer(boolean black, InFallibleStrat s, ObservableReversiModel m) {
    this.isBlack = black;
    this.strategy = s;
    this.model = m;
  }

  /**
   * Picks a move for the current board using the strategy in the constructor and asks
   * the controller to make the move.
   */
  @Override
  public void yourTurn() {
    try {
      Coordinate move = strategy.chooseMove(model);
      for (PlayerActions s : subscribers) {
        s.requestMove(move);
      }
    } catch (IllegalArgumentException e) {
      for (PlayerActions s : subscribers) {
        s.requestPass();
      }
    }
  }

  /**
   * Whether this player is using the Black or White tiles.
   * @return true if this player is using black, false otherwise.
   */
  @Override
  public boolean isBlack() {
    return isBlack;
  }

  /**
   * Add a listener to the list of subscribers that are notified when the machine picks a move.
   * @param s the subscriber to be added.
   */
  @Override
  public void addListener(PlayerActions s) {
    this.subscribers.add(s);
  }
}
