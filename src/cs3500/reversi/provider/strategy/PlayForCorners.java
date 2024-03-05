package cs3500.reversi.provider.strategy;

import java.util.Optional;

import cs3500.reversi.provider.model.Disc;
import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.ReversiReadOnlyModel;

/**
 *  Represents the strategy of playing for corners in Reversi.
 */
public class PlayForCorners implements ReversiStrategy {
  @Override
  public Optional<Hex> chooseMove(ReversiReadOnlyModel model, Disc player) {
    Optional<Hex> cornerMove = Optional.empty();

    int size = model.getBoardSize();

    if (model.hasValidMove(player)) {

      for (Hex hex : model.getValidMoves(player)) {
        int qCoord = hex.getQ();
        int rCoord = hex.getR();

        if (this.isCorner(qCoord, rCoord, size)) {
          cornerMove = Optional.of(hex);
        }
      }
    }

    return cornerMove;
  }

  private boolean isCorner(int qCoord, int rCoord, int size) {
    return (qCoord == 0 && rCoord == -size)
            || (qCoord == size && rCoord == -size)
            || (qCoord == size && rCoord == 0)
            || (qCoord == 0 && rCoord == size)
            || (qCoord == -size && rCoord == 0)
            || (qCoord == -size && rCoord == size);
  }


}
