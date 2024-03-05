package cs3500.reversi.provider.strategy;

import java.util.Optional;

import cs3500.reversi.provider.model.Disc;
import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.ReversiReadOnlyModel;

/**
 *  Represents the strategy of capturing the most discs possible in Reversi.
 */
public class CaptureMostDiscs implements ReversiStrategy {
  @Override
  public Optional<Hex> chooseMove(ReversiReadOnlyModel model, Disc player) {
    Optional<Hex> bestMove = Optional.empty();
    int mostDiscsCaptured = 0;

    if (model.hasValidMove(player)) {

      for (Hex hex : model.getValidMoves(player)) {

        int discsCaptured = model.discsCaptured(hex.getQ(), hex.getR(), player);

        if (discsCaptured > mostDiscsCaptured) {
          bestMove = Optional.of(hex);
          mostDiscsCaptured = discsCaptured;

        } else if (discsCaptured == mostDiscsCaptured && bestMove.isPresent()) {

          Hex currentBestMove = bestMove.get();

          boolean moreUpLeft = hex.getR() < currentBestMove.getR()
                  || (hex.getR() == currentBestMove.getR() && hex.getQ() < currentBestMove.getQ());

          if (moreUpLeft) {
            bestMove = Optional.of(hex);
          }
        }

      }

    }

    return bestMove;
  }

}
