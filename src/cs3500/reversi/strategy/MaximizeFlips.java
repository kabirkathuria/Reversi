package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import cs3500.reversi.model.Coordinate;
import cs3500.reversi.model.ObservableReversiModel;

/**
 * A strategy that maximizes the number of enemy flipped tiles after one turn, breaking ties
 * by topmost row, or by leftmost tile in the row if the moves are in the same row.
 */
public class MaximizeFlips implements FallibleStrat, DynamicStrat {
  /**
   * Returns an available move according to the strategy.
   *
   * @param m the model, i.e. the current state of the board.
   * @return a coordinate, representing the best move this strategy can produce,
   *         or empty, if no moves are available that fit the strategy.
   */
  @Override
  public Optional<Coordinate> chooseMove(ObservableReversiModel m) {
    Optional<List<Coordinate>> rankedMoves = rankMoves(m);
    return rankedMoves.map(coordinates -> coordinates.get(0));
  }

  /**
   * returns all legal moves, ranked by number of flips.
   *
   * @param m the model
   * @return the list of moves, ordered by number of flips from most to least. ties are broken
   *         by the topmost-leftmost coordinate.
   */
  @Override
  public Optional<List<Coordinate>> rankMoves(ObservableReversiModel m) {
    // map of tiles flipped to moves. multiple moves can have the same # of flips, hence the list.
    Map<Integer, List<Coordinate>> moves = new TreeMap<>();
    // the move being tried. every position on the board will be checked for legality.
    Coordinate testMove;
    // the number of flips for a given move.
    int flips;
    // put scores of legal moves into map
    for (int r = 0; r < m.getBoardHeight(); r++) {
      for (int c = 0; c < m.getBoardWidth(); c++) {
        testMove = new Coordinate(c, r);
        if (m.isInBounds(testMove) && m.isLegal(testMove)) {
          flips = m.potentialScore(testMove);
          // opponent's score after the move
          if (!moves.containsKey(flips)) {
            moves.put(flips, new ArrayList<>());
          }
          moves.get(flips).add(new Coordinate(testMove));
        }
      }
    }
    List<Coordinate> rankedMoves = new ArrayList<>();
    // put them all in the arraylist, ordered best to worst.
    for (Integer i : moves.keySet()) {
      sortValues(moves.get(i));
      rankedMoves.addAll(0, moves.get(i));
    }

    if (rankedMoves.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(rankedMoves);
  }

  /**
   * Sorts a list of coordinates with topmost-leftmost coming first.
   * MUTATES THE LIST!!!
   * @param values the list of coordinates
   */
  private void sortValues(List<Coordinate> values) {
    values.sort((c1, c2) -> {
      if (c1.r != c2.r) {
        return c1.r - c2.r;
      }
      return c1.c - c2.c;
    });
  }
}