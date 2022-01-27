package de.pentagames.maulwurfkompanie.board;

import org.jetbrains.annotations.NotNull;

import upb.maulwurfcompany.library.data.GameState;
import upb.maulwurfcompany.library.data.Mole;
import upb.maulwurfcompany.library.data.Position;

/**
 * This class makes information concerning the game board available.
 * It is capable to search for objects on the board and calculate positions.
 */
public class Board {

  /**
   * This method determines whether a {@link Position} is on the board.
   *
   * @param state    The current {@link GameState}.
   * @param position The {@link Position} to be checked.
   * @return true if the {@link Position} is on the board.
   */
  public static boolean onBoard(@NotNull final GameState state, @NotNull final Position position) {
    return position.y >= 0 && position.y <= state.radius * 2
      && position.x >= Math.max(0, position.y - state.radius)
      && position.x <= state.radius + Math.min(position.y, state.radius);
  }

  /**
   * This method determines whether a given {@link Position} is empty and on the board.
   *
   * @param state    The current {@link GameState}.
   * @param position The {@link Position} to be checked.
   * @return true if the {@link Position} is on the board and is empty i.e. no mole
   */
  public static boolean isEmpty(@NotNull final GameState state, @NotNull final Position position) {
    if (!onBoard(state, position)) return false;
    for (var mole : state.placedMoles) {
      if (pEquals(mole.position, position)) return false;
    }
    return true;
  }

  /**
   * This method determines whether a mole exists at a given {@link Position}.
   * If found the mole is returned.
   *
   * @param state    The current {@link GameState}.
   * @param position The {@link Position} to be checked.
   * @return The Mole at the {@link Position} or 'null' if no mole found.
   */
  public static Mole getMole(@NotNull final GameState state, @NotNull final Position position) {
    if (!onBoard(state, position)) return null;
    for (var mole : state.placedMoles) {
      if (pEquals(mole.position, position)) return mole;
    }
    return null;
  }

  /**
   * This method determines whether a mole is shown at a given {@link Position}.
   * If found the mole is returned.
   *
   * @param state    The current {@link GameState}.
   * @param position The {@link Position} to be checked.
   * @return The Mole currently shown at the {@link Position} or 'null' if no mole found.
   */
  public static ShownMole getShownMole(@NotNull final GameState state, @NotNull final Position position) {
    if (!onBoard(state, position)) return null;
    for (var mole : GameView.instance.shownMoles) {
      if (pEquals(mole.mole.position, position)) return mole;
    }
    return null;
  }

  /**
   * This method determines what type of field exists at the given position.
   *
   * @param state    The current {@link GameState}.
   * @param position The {@link Position} to be checked.
   * @return A {@link FieldType} enumerate to differentiate between 'out of board' fields, holes,
   * 'draw again' fields and default ones.
   */
  public static FieldType getFieldType(@NotNull final GameState state, @NotNull final Position position) {
    if (!onBoard(state, position)) return FieldType.OUT_OF_BOARD;
    for (var hole : state.level.holes)
      if (pEquals(hole, position)) return FieldType.HOLE;
    for (var drawAgain : state.level.drawAgainFields)
      if (pEquals(drawAgain, position)) return FieldType.DRAW_AGAIN;
    return FieldType.DEFAULT;
  }

  /**
   * This method determines whether certain move is valid (valid direction, no moles in the way,
   * valid length; will be ignored if pullDisc is <= 0)
   *
   * @param state    The current {@link GameState}.
   * @param from     Start {@link Position} of the mole to be moved.
   * @param to       Target {@link Position} the mole should be moved to.
   * @param pullDisc The PullDisk the player intends to use.
   * @return true if the move is found to be valid.
   */
  public static boolean isValidMove(@NotNull final GameState state, @NotNull final Position from, @NotNull final Position to, final int pullDisc) {
    if (!isEmpty(state, to)) return false;
    var dif = new Position(to.x - from.x, to.y - from.y);
    if (dif.x * dif.y != 0 && dif.x != dif.y) return false; // no valid direction
    if (pullDisc > 0 && Math.abs(dif.x) != pullDisc && Math.abs(dif.y) != pullDisc)
      return false; // length != pullDisc
    // Go through all positions between from and to
    var dir = new Position((int) Math.signum(dif.x), (int) Math.signum(dif.y)); // eg (1,0) or (-1,-1)
    for (var p = pAdd(from, dir); !pEquals(p, to); p = pAdd(p, dir)) {
      if (!isEmpty(state, p)) return false;
    }
    return true;
  }

  /**
   * This method determines whether certain move is valid (not checking if it fits a pullDisk,
   * valid direction, no moles in the way, valid length)
   *
   * @param state The current {@link GameState}.
   * @param from  Start {@link Position} of the mole to be moved.
   * @param to    Target {@link Position} the mole should be moved to.
   * @return true if the move is found to be valid.
   */
  public static boolean isValidMove(@NotNull final GameState state, @NotNull final Position from, @NotNull final Position to) {
    return isValidMove(state, from, to, 0);
  }

  /**
   * This method clones a given {@link Position} object.
   *
   * @param position {@link Position} to be cloned.
   * @return The cloned {@link Position} object.
   */
  public static Position pClone(@NotNull final Position position) {
    return new Position(position.x, position.y);
  }

  /**
   * This method compares two {@link Position} objects.
   *
   * @param position1 First {@link Position}.
   * @param position2 {@link Position} the first is to be compared to.
   * @return true if the coordinates match.
   */
  public static boolean pEquals(@NotNull final Position position1, @NotNull final Position position2) {
    return position1.x == position2.x && position1.y == position2.y;
  }

  /**
   * This method adds the coordinates of two {@link Position} objects together.
   *
   * @param position1 First {@link Position}.
   * @param position2 {@link Position} to be added to the first one.
   * @return {@link Position} object having the sum of the given {@link Position} objects
   * coordinates as its own coordinates.
   */
  public static Position pAdd(@NotNull final Position position1, @NotNull final Position position2) {
    return new Position(position1.x + position2.x, position1.y + position2.y);
  }

  /**
   * Enumeration to differentiate different types of fields on the board.
   */
  public enum FieldType {OUT_OF_BOARD, DEFAULT, HOLE, DRAW_AGAIN}
}
