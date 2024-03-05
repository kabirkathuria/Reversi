package cs3500.reversi.provider.model;

/**
 *  Represents the three discs/possible states a board tile can have.
 */
public enum Disc {
  BLACK,
  WHITE,
  NONE;

  /**
   * Gets the opposite disc of this disc.
   *
   * @return the opposite disc
   */
  public Disc getOppositeDisc() {
    if (this == BLACK) {
      return WHITE;
    } else if (this == WHITE) {
      return BLACK;
    } else {
      return NONE;
    }
  }

  /**
   * Gets the string equivalent of this disc.
   * Black discs are "X".
   * White discs are "O".
   * None discs are "_".
   *
   * @return string of a disc
   */
  public String toString() {
    if (this == BLACK) {
      return "X";
    } else if (this == WHITE) {
      return "O";
    } else {
      return "_";
    }
  }

  /**
   * The providers didn't give us javadoc for this, and we would lose points for style.
   * I don't know what this does.
   * @return ??
   */
  public String nameToString() {
    if (this == BLACK) {
      return "Black";
    } else if (this == WHITE) {
      return "White";
    } else {
      return "None";
    }
  }

}
