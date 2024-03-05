package cs3500.reversi.controller;

/**
 * Requirements to be a listener for the model's status.
 */
public interface ModelStatus {
  /**
   * Notification served by the model when the turn has changed.
   * @param isBlackTurn true iff it's now black's turn.
   */
  void notifyTurn(boolean isBlackTurn);
}
