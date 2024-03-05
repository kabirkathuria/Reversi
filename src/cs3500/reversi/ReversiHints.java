package cs3500.reversi;

import javax.swing.JFrame;

import cs3500.reversi.controller.GenericPlayer;
import cs3500.reversi.controller.HumanPlayer;
import cs3500.reversi.controller.MachinePlayer;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.model.HexagonalReversiModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.SquareReversiModel;
import cs3500.reversi.strategy.AvoidCornerNeighbors;
import cs3500.reversi.strategy.InFallibleStrat;
import cs3500.reversi.strategy.Infallible;
import cs3500.reversi.strategy.MaximizeFlips;
import cs3500.reversi.strategy.PlayCorners;
import cs3500.reversi.strategy.TryTwo;
import cs3500.reversi.view.ReversiGuiView;
import cs3500.reversi.view.ReversiView;

/**
 * Main method for drawing the gui of reversi in a window.
 */
public final class ReversiHints {
  /**
   * Main method.
   *
   * @param args ignored
   */
  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Must provide at least two arguments. For help, type '-h' or 'help'");
      helpmessage();
      System.exit(1);
    }
    if (args.length > 3) {
      System.out.println("You can specify the type of game with 'square' or 'hex' as the 3rd arg");
    }
    if (args[0].equals("-h") || args[0].equalsIgnoreCase("help")) {
      helpmessage();
      System.exit(0);
    }
    // basic setup
    ReversiModel model = new HexagonalReversiModel(6);
    // configure square or hexagonal model
    if (args.length > 2) {
      if (args[2].equalsIgnoreCase("square")) {
        model = new SquareReversiModel(8);
      }
      else if (!args[2].equalsIgnoreCase("hex")) {
        helpmessage();
        System.exit(0);
      }
    }

    // this will make the window border look strange, but it makes resizing look better on windows.
    JFrame.setDefaultLookAndFeelDecorated(true);

    GenericPlayer[] players = new GenericPlayer[] {new HumanPlayer(true), new HumanPlayer(false)};

    for (int i = 0; i <= 1; i++) {
      InFallibleStrat strat;
      switch (args[i]) {
        case "simple":
          strat = new Infallible(new MaximizeFlips());
          players[i] = new MachinePlayer(i == 0, strat, model);
          break;
        case "intermediate":
          strat = new Infallible(new AvoidCornerNeighbors());
          players[i] = new MachinePlayer(i == 0, strat, model);
          break;
        case "advanced":
          strat = new Infallible(new TryTwo(new PlayCorners(), new AvoidCornerNeighbors()));
          players[i] = new MachinePlayer(i == 0, strat, model);
          break;
        case "human":
          break;
        default:
          System.out.println("What are you trying to input??");
          helpmessage();
          throw new RuntimeException();
      }
    }

    ReversiView view1 = new ReversiGuiView(model);
    ReversiView view2 = new ReversiGuiView(model);

    new ReversiController(model, players[0], view1);
    new ReversiController(model, players[1], view2);

    model.startGame();
  }

  private static void helpmessage() {
    System.out.println("Format: '<player1> <player2>'");
    System.out.println("Where player1 is black, player2 is white,");
    System.out.println("and player1 and player2 are one of:");
    System.out.println("'human', 'simple', 'intermediate', 'advanced'");
    System.out.println("Note: the simple, intermediate, and advanced players are robots");
    System.out.println("that will automatically make moves. Their view can be discarded.");
  }
}