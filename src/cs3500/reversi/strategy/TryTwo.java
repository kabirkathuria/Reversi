package cs3500.reversi.strategy;

import java.util.Optional;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;

/**
 * Tries two strategies. If the first doesn't have a move, tries the second. If the second doesn't
 * have a move, returns empty.
 */
public class TryTwo implements FallibleStrat {
  // the strategies
  private final FallibleStrat first;
  private final FallibleStrat second;

  /**
   * Combines two strategies together, layering the first on top of the second.
   *
   * @param first  the first strategy to try.
   * @param second the second (backup) strategy to try.
   */
  public TryTwo(FallibleStrat first, FallibleStrat second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public Optional<Coordinate> chooseMove(ObservableReversiModel m) {
    Optional<Coordinate> ans = first.chooseMove(m);
    if (ans.isPresent()) {
      return ans;
    }
    ans = second.chooseMove(m);
    return ans;
  }
}
