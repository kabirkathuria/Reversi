package cs3500.reversi.view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cs3500.reversi.controller.PlayerActions;
import cs3500.reversi.model.ObservableReversiModel;
import cs3500.reversi.model.SquareReversiModel;

/**
 * Creates a frame containing the board's GUI, which is represented by a JReversiPanel.
 */
public class ReversiGuiView extends JFrame implements ReversiView {
  AbstractPanel panel;

  /**
   * Instantiates the frame and constructs the panel.
   * @param model the board the GUI is being made for.
   */
  public ReversiGuiView(ObservableReversiModel model) {
    setDefaultLookAndFeelDecorated(true);
    this.getContentPane().setBackground(Color.DARK_GRAY);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    if (model instanceof SquareReversiModel) {
      panel = new JSquareReversiPanel(model);
    } else {
      panel = new JOverlayPanel(model);
    }
    this.add(panel);
    this.pack();
  }

  /**
   * Renders the frame by setting it to visible.
   */
  @Override
  public void render() {
    this.setVisible(true);
  }

  /**
   * Updates the display, re-rendering the state of the game.
   */
  @Override
  public void update() {
    this.repaint();
  }

  /**
   * Indicates that a requested move was not valid.
   * To be displayed to the user!
   */
  @Override
  public void invalidMove() {
    JOptionPane.showMessageDialog(
            this,
            "Can't Move!",
            "Error!",
            JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void notYourTurn() {
    JOptionPane.showMessageDialog(
            this,
            "Not your turn yet!",
            "Error!",
            JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Adds a listener to player actions made through the view.
   *
   * @param s the listener being added to the list of subscribers.
   */
  @Override
  public void addListener(PlayerActions s) {
    this.panel.addListener(s);
  }
}
