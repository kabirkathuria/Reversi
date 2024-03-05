package cs3500.reversi;

import javax.swing.JFrame;

import cs3500.reversi.adapter.ControllerAdapter;
import cs3500.reversi.adapter.ModelAdapter;
import cs3500.reversi.adapter.StratAdapter;
import cs3500.reversi.adapter.ViewAdapter;
import cs3500.reversi.controller.Controller;
import cs3500.reversi.controller.GenericPlayer;
import cs3500.reversi.controller.HumanPlayer;
import cs3500.reversi.controller.MachinePlayer;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.model.HexagonalReversiModel;
import cs3500.reversi.provider.model.ReversiReadOnlyModel;
import cs3500.reversi.provider.strategy.AvoidCellsNextToCorners;
import cs3500.reversi.provider.strategy.CaptureMostDiscs;
import cs3500.reversi.provider.strategy.PlayForCorners;
import cs3500.reversi.provider.strategy.ReversiStrategy;
import cs3500.reversi.provider.strategy.TryManyReversiStrategy;
import cs3500.reversi.provider.view.HexagonReversiView;
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
public final class Reversi {
  /**
   * Main method.
   * @param args ignored
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Must provide EXACTLY two arguments. For help, type '-h' or 'help'");
      helpmessage();
      System.exit(1);
    }
    if (args[0].equals("-h") || args[0].equalsIgnoreCase("help")) {
      helpmessage();
      System.exit(0);
    }
    // basic setup
    HexagonalReversiModel model = new HexagonalReversiModel(6);
    // this will make the window border look strange, but it makes resizing look better on windows.
    JFrame.setDefaultLookAndFeelDecorated(true);
    ReversiView view1 = new ReversiGuiView(model);

    GenericPlayer player1;
    GenericPlayer player2;

    if (args[0].equalsIgnoreCase("human")) {
      player1 = new HumanPlayer(true);
    } else {
      InFallibleStrat strat;
      switch (args[0]) {
        case "simple":
          strat = new Infallible(new MaximizeFlips());
          break;
        case "intermediate":
          strat = new Infallible(new AvoidCornerNeighbors());
          break;
        case "advanced":
          strat = new Infallible(new TryTwo(new PlayCorners(), new AvoidCornerNeighbors()));
          break;
        default:
          System.out.println("What are you trying to input??");
          helpmessage();
          throw new RuntimeException();
      }
      player1 = new MachinePlayer(true, strat, model);
    }

    // provider player setup.
    if (args[1].equalsIgnoreCase("human")) {
      player2 = new HumanPlayer(false);
    } else {
      ReversiStrategy strat;
      switch (args[1]) {
        case "simple":
          strat = new CaptureMostDiscs();
          break;
        case "intermediate":
          strat = new AvoidCellsNextToCorners();
          break;
        case "advanced":
          strat = new TryManyReversiStrategy(new PlayForCorners(), new CaptureMostDiscs());
          break;
        default:
          System.out.println("What are you trying to input??");
          helpmessage();
          throw new RuntimeException();
      }
      // adapter
      InFallibleStrat adaptedStrat =
              new Infallible(new StratAdapter(strat, model, false));
      player2 = new MachinePlayer(false, adaptedStrat, model);
    }

    // provider player
    ReversiReadOnlyModel providerModel = new ModelAdapter(model);
    cs3500.reversi.provider.view.ReversiView providerView = new HexagonReversiView(providerModel);
    Controller providerController =
            new ReversiController(model, player2, new ViewAdapter(providerView, model));
    new ControllerAdapter(providerController, model);

    // self
    new ReversiController(model, player1, view1);

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