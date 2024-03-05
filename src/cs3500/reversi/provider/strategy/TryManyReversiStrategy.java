package cs3500.reversi.provider.strategy;

import java.util.Optional;

import cs3500.reversi.provider.model.Disc;
import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.ReversiReadOnlyModel;

/**
 *  To represent a multiple Reversi Strategies composed to for a higher order strategy.
 */
public class TryManyReversiStrategy implements ReversiStrategy {
  private final ReversiStrategy first;
  private final ReversiStrategy second;

  public TryManyReversiStrategy(ReversiStrategy first, ReversiStrategy second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public Optional<Hex> chooseMove(ReversiReadOnlyModel model, Disc player) {
    Optional<Hex> move = this.first.chooseMove(model, player);

    if (move.isPresent()) {
      return move;
    }

    return this.second.chooseMove(model, player);
  }
}
