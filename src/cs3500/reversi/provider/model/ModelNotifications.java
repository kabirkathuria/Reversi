package cs3500.reversi.provider.model;

/**
 *  To represent the model notifications interface for reversi.
 *  This is everything the controller needs to be notified for.
 *  In the case of the assignment instructions, views should only be able to be interacted
 *  with when it is the players turn, hence the turnChanged listener.
 */
public interface ModelNotifications {

  /**
   * Notifies the subscriber of this listener that the turn has changed.
   */
  void turnChanged();

}

