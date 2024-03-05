package cs3500.reversi.adapter;

import java.util.Optional;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;
import cs3500.reversi.provider.model.Disc;
import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.strategy.ReversiStrategy;
import cs3500.reversi.strategy.FallibleStrat;

/**
 * Adapts strategies to be used by our genericPlayer.
 */
public class StratAdapter implements FallibleStrat {
  ReversiStrategy strat;
  ObservableReversiModel model;
  boolean blackPlayer;

  /**
   * Constructs a strategy from a provider strategy.
   * @param s strategy
   * @param m model
   * @param blackPlayer the color of the player, true iff black
   */
  public StratAdapter(ReversiStrategy s, ObservableReversiModel m, boolean blackPlayer) {
    this.strat = s;
    this.model = m;
    this.blackPlayer = blackPlayer;
  }

  /**
   * Chooses a move according to the implemented strategy. Returns optional.empty if the strategy
   * could not find any valid moves.
   *
   * @param m the board the move is being made for.
   * @return a move according to this strategy.
   */
  @Override
  public Optional<Coordinate> chooseMove(ObservableReversiModel m) {
    Disc d = Disc.WHITE;
    if (blackPlayer) {
      d = Disc.BLACK;
    }
    Optional<Hex> c = strat.chooseMove(new ModelAdapter(model), d);
    if (c.isPresent()) {
      return Optional.of(convertCoord(c.get().getQ(), c.get().getR()));
    }
    return Optional.empty();
  }

  private Coordinate convertCoord(int q, int r) {
    int size = model.getBoardHeight() / 2;
    return new Coordinate(q + size, r + size);
  }
}
