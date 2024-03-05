package cs3500.reversi.strategy;

import java.util.Optional;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;

/**
 * Makes a fallible strategy infallible by throwing an exception if it's going to
 * return an empty optional.
 */
public class Infallible implements InFallibleStrat {
  private final FallibleStrat s;

  public Infallible(FallibleStrat s) {
    this.s = s;
  }

  @Override
  public Coordinate chooseMove(ObservableReversiModel m) {
    Optional<Coordinate> ans = s.chooseMove(m);
    if (ans.isPresent()) {
      return ans.get();
    }
    throw new IllegalArgumentException("No valid moves according to this strategy!");
  }
}
