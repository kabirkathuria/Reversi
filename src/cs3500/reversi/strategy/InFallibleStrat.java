package cs3500.reversi.strategy;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;

/**
 * Infallible strategies throw an exception if there are no moves. Otherwise,
 * they return the move.
 */
public interface InFallibleStrat {
  /**
   * Chooses a move according to the implemented strategy. Throws an exception if the strategy
   * could not find any valid moves.
   * @param m the board the move is being made for.
   * @return a move according to this strategy.
   */
  Coordinate chooseMove(ObservableReversiModel m);
}
