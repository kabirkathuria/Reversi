package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;

/**
 * Tries N strategies progressively, returning the first move found, or empty.
 */
public class TryN implements FallibleStrat {
  private final List<FallibleStrat> strats;

  public TryN(FallibleStrat... strats) {
    this.strats = new ArrayList<>(List.of(strats));
  }

  /**
   * Combines a list of strategies together, first to last, and returns the first move found.
   *
   * @param m the game the strategies are trying to find a move for
   * @return the first strategy in the list to find a move, or Optional.empty if none have a move.
   */
  @Override
  public Optional<Coordinate> chooseMove(ObservableReversiModel m) {
    Optional<Coordinate> ans;
    int count = 0;
    do {
      ans = strats.get(count++).chooseMove(m);
    }
    while (!ans.isPresent() && count < strats.size());
    return ans;
  }
}
