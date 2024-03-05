package cs3500.reversi.strategy;

import java.util.Optional;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;

/**
 * Fallible strategies can return Optional.empty, or an Optional of a coordinate for their move.
 */
public interface FallibleStrat {
  /**
   * Chooses a move according to the implemented strategy. Returns optional.empty if the strategy
   * could not find any valid moves.
   * @param m the board the move is being made for.
   * @return a move according to this strategy.
   */
  Optional<Coordinate> chooseMove(ObservableReversiModel m);
}
