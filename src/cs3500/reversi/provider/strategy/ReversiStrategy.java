package cs3500.reversi.provider.strategy;

import java.util.Optional;

import cs3500.reversi.provider.model.Disc;
import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.ReversiReadOnlyModel;

/**
 *  Represents the interface of Reversi Strategies.
 */
public interface ReversiStrategy {

  /**
   * Chooses a move based on the strategy type.
   *
   * @param model Mutator model.
   * @param player Player turn.
   * @return Optional hex if a move was found or not
   */
  Optional<Hex> chooseMove(ReversiReadOnlyModel model, Disc player);


}
