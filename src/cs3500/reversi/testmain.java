package cs3500.reversi;

import cs3500.reversi.controller.GenericPlayer;
import cs3500.reversi.controller.HumanPlayer;
import cs3500.reversi.controller.MachinePlayer;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.model.SquareReversiModel;
import cs3500.reversi.strategy.InFallibleStrat;
import cs3500.reversi.strategy.Infallible;
import cs3500.reversi.strategy.MaximizeFlips;
import cs3500.reversi.strategy.PlayCorners;
import cs3500.reversi.strategy.TryTwo;
import cs3500.reversi.view.ReversiGuiView;
import cs3500.reversi.view.ReversiSquareTextualView;
import cs3500.reversi.view.ReversiTextualView;

public class testmain {
  public static void main(String[] args) {
    SquareReversiModel m = new SquareReversiModel(8);
    ReversiGuiView v = new ReversiGuiView(m);
    ReversiGuiView v2 = new ReversiGuiView(m);
    InFallibleStrat s = new Infallible(new TryTwo(new PlayCorners(), new MaximizeFlips()));
    GenericPlayer p = new HumanPlayer(true);
    GenericPlayer p2 = new HumanPlayer(false);
    ReversiController c = new ReversiController(m, p, v);
    ReversiController c2 = new ReversiController(m, p2, v2);
    m.startGame();
  }
}
