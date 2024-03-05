package cs3500.reversi.strategy;

import java.util.List;
import java.util.Optional;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;

/**
 * Gets a legal corner coordinate to play, or Optional.empty.
 */
public class PlayCorners implements FallibleStrat {
  /**
   * Returns an available corner move on the board.
   * @param m the board being strategized around
   * @return the best corner move, if there is one. Otherwise, optional.empty.
   */
  @Override
  public Optional<Coordinate> chooseMove(ObservableReversiModel m) {
    List<Coordinate> corners = m.getCorners();

    for (Coordinate c : corners) {
      if (m.isLegal(c)) {
        return Optional.of(c);
      }
    }
    return Optional.empty();
  }
}
