package cs3500.reversi.model;

import java.util.Objects;

/**
 * Represents a coordinate in a row/column coordinate system.
 * Hexagonal rows are slanted from top left to bottom right!
 */
public class Coordinate {
  // these should be public, we want anyone to be able to see them.
  public final int c;
  public final int r;

  /**
   * Creates a new Coordinate.
   *
   * @param c The column
   * @param r The row
   */
  public Coordinate(int c, int r) {
    this.c = c;
    this.r = r;
  }

  /**
   * Allows for a quick de-aliased copy of a coordinate to be made.
   * Might be useful in the future.
   *
   * @param coordinate The coordinate being copied.
   */
  public Coordinate(Coordinate coordinate) {
    this.c = coordinate.c;
    this.r = coordinate.r;
  }

  /**
   * Checks if the given object is equal to this coordinate.
   * Two coordinates are equal if their c and r values are the same.
   *
   * @param obj the object to compare with
   * @return true iff the coordinates are the same.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Coordinate) {
      Coordinate other = (Coordinate) obj;
      return c == other.c && r == other.r;
    }
    return false;
  }

  /**
   * Computes a hash code for the coordinate based on its c and r values.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(c, r);
  }

  /**
   * Useful for debugging. Returns a clean String representation of the coordinate.
   * Provides a string representation of the coordinate.
   *
   * @return a string in the format "Coordinate: c: {@link #c}, r: {@link #r}"
   */
  @Override
  public String toString() {
    return "Coordinate: c: " + c + ", r: " + r;
  }
}