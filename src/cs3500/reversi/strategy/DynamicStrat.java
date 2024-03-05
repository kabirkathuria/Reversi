package cs3500.reversi.strategy;

import java.util.List;
import java.util.Optional;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;

/**
 * A dynamic strategy that produces a list of moves, in order of preference.
 * Useful for chaining together strategies, especially subtractive ones.
 */
public interface DynamicStrat extends FallibleStrat {
  /**
   * Returns a list of possible moves using this strategy, from best to worst.
   * @param m the model
   * @return a list of moves
   */
  Optional<List<Coordinate>> rankMoves(ObservableReversiModel m);
}
