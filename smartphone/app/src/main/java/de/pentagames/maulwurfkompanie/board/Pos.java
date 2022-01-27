package de.pentagames.maulwurfkompanie.board;

import android.graphics.Matrix;

import org.jetbrains.annotations.NotNull;

import upb.maulwurfcompany.library.data.Position;

public abstract class Pos {
  private static Matrix matrix;
  private static Matrix inverse;

  public static void setMatrix(@NotNull final Matrix matrix) {
    Pos.matrix = matrix;
    inverse = new Matrix();
    Pos.matrix.invert(inverse);
  }

  public static float[] getDisplay(@NotNull final float[] point) {
    var points = point.clone();
    matrix.mapPoints(points);
    return points;
  }

  public static float[] getDisplay(final float x, final float y) {
    return getDisplay(new float[]{x, y});
  }

  public static float[] getDisplay(@NotNull final Position position) {
    return getDisplay(new float[]{position.x, position.y});
  }

  public static Position fromDisplay(final float x, final float y) {
    var points = new float[]{x, y};
    inverse.mapPoints(points);
    return new Position(Math.round(points[0]), Math.round(points[1]));
  }

  public static float getDisplayX(final float[] point) {
    return getDisplay(point)[0];
  }

  public static float getDisplayX(final float x, final float y) {
    return getDisplayX(new float[]{x, y});
  }

  public static float getDisplayX(@NotNull final Position p) {
    return getDisplayX(p.x, p.y);
  }

  public static float getDisplayY(final float[] point) {
    return getDisplay(point)[1];
  }

  public static float getDisplayY(final float x, final float y) {
    return getDisplayY(new float[]{x, y});
  }

  public static float getDisplayY(@NotNull final Position p) {
    return getDisplayY(p.x, p.y);
  }
}
