package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;

/**
 * Avoids playing corner neighbors.
 */
public class AvoidCornerNeighbors implements FallibleStrat {
  /**
   * Chooses a move that avoids corner neighbors. Returns optional.empty if the strategy
   * could not find any valid moves. Uses MaximizeFlips as the base strategy to subtract moves from.
   * @param m the board the move is being made for.
   * @return a move according to this strategy.
   */
  @Override
  public Optional<Coordinate> chooseMove(ObservableReversiModel m) {
    DynamicStrat s = new MaximizeFlips();
    Optional<List<Coordinate>> moves = s.rankMoves(m);

    if (moves.isPresent()) {
      for (Coordinate c : moves.get()) {
        if (!isCornerNeighbor(c, m)) {
          return Optional.of(c);
        }
      }
    }
    return Optional.empty();
  }

  private boolean isCornerNeighbor(Coordinate c, ObservableReversiModel m) {
    int sideLen = (m.getBoardWidth() - 1) / 2;
    List<Coordinate> corners = m.getCorners();
    return getNeighbors(c, m).stream().anyMatch(nb -> corners.stream().anyMatch(nb::equals));
  }

  /**
   * Retrieves all of a hexagonal coordinate's neighbors.
   * @param c the coordinate
   * @return all of c's neighbors. Abstract of any board or bounds, it simply assumes
   *         the coordinate represents a hexagon tiling the plane.
   */
  private List<Coordinate> getNeighbors(Coordinate c, ObservableReversiModel m) {
    List<Coordinate> directions = m.getDirections();
    List<Coordinate> ans = new ArrayList<>();
    for (Coordinate coord : directions) {
      ans.add(new Coordinate(coord.c + c.c, coord.r + c.r));
    }
    return ans;
  }
}
