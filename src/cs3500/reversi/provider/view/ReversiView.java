package cs3500.reversi.provider.view;

/**
 *  To represent a Reversi View. This interface contains all the methods that are needed
 *  to update the display of Reversi throughout the game.
 */
public interface ReversiView {

  /**
   * Registers the view to listen for view features.
   *
   * @param features the features.
   */
  void addFeatureListener(ViewFeatures features);

  /**
   * Shows a message dialog popup of the given message.
   *
   * @param message message.
   */
  void showMessageDialog(String message);

  /**
   * Enables key input, this can be mouse clicks or keyboard presses.
   */
  void enableInput();

  /**
   * Disables key input, this can be mouse clicks or keyboard presses.
   */
  void disableInput();

  /**
   * Repaints the Reversi panel.
   */
  void repaint();
}
