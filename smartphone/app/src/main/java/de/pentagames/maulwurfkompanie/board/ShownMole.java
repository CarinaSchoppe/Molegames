package de.pentagames.maulwurfkompanie.board;

import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.pentagames.maulwurfkompanie.R;
import de.pentagames.maulwurfkompanie.Utils;
import de.pentagames.maulwurfkompanie.client.Client;
import upb.maulwurfcompany.library.data.GameState;
import upb.maulwurfcompany.library.data.Mole;
import upb.maulwurfcompany.library.data.Position;

/**
 * A class used to visualize moles
 *
 * @author Thorben
 * @author Leo
 */
public class ShownMole {
  public Mole mole;

  private Position lastPosition;
  private boolean dead = false;
  private long startTime;
  private long endTime = 0;

  /**
   * The constructor of the ShownMole
   *
   * @param mole a mole object the mole the ShownMole is related to
   */
  public ShownMole(@NotNull final Mole mole) {
    this.mole = mole;
    this.lastPosition = Board.pClone(mole.position);
  }

  /**
   * Sets the moles death value to true and changes its {@link Position}
   */
  public void kill() {
    if (this.dead) return;
    this.lastPosition = this.mole.position;
    this.mole.position = new Position(-1, -1);
    this.dead = true;
    this.startTime = Utils.getTime();
    this.endTime = Utils.getTime() + Math.min(500, Client.messageHandler.game.visualisationTime);
  }

  /**
   * Animates to the {@link Position} p
   *
   * @param position {@link Position} to be an animated to.
   */
  public void animateTo(Position position) {
    if (this.dead) {
      this.dead = false;
      moveTo(position);
      return;
    }
    this.lastPosition = this.mole.position;
    this.mole.position = position;
    this.startTime = Utils.getTime();
    // 150ms per step, but not more than visualisationTime
    this.endTime = Utils.getTime() + Math.min(Math.max(Math.abs(mole.position.x - lastPosition.x), Math.abs(mole.position.y - lastPosition.y)) * 150L, Client.messageHandler.game.visualisationTime);
  }

  /**
   * Changes the moles {@link Position}.
   *
   * @param position {@link Position} to move the mole to.
   */
  public void moveTo(Position position) {
    this.dead = false;
    this.mole.position = position;
    this.lastPosition = Board.pClone(mole.position);
  }

  /**
   * Draws a mole-sprite using an svg at the {@link Position} of the mole.
   * A sprite of a mole in a hole is used should the mole be on top of a 'hole'-space
   * the mole uses the color of a player to its own color
   * the width is used to draw the mole offscreen if it is dead
   *
   * @param gameState the current {@link GameState}
   * @param canvas    the canvas object on which to draw the mole on
   * @param width     the canvas width
   */
  public void draw(@NotNull final GameState gameState, @NotNull final Canvas canvas, final int width) {
    var d = Pos.getDisplay(mole.position);
    if (Utils.getTime() < endTime) {
      var last = Pos.getDisplay(lastPosition);
      if (this.dead) {
        d[0] = (last[0] / width) < 0.5 ? -50 : width + 50; // if the mole is dead, it's animated next to the board
        d[1] = last[1];
      }
      var factor = 1f * (Utils.getTime() - startTime) / (endTime - startTime);
      d[0] = last[0] + (d[0] - last[0]) * factor;
      d[1] = last[1] + (d[1] - last[1]) * factor;
    } else if (this.dead) return;
    var drawableColor = (Drawable) null;
    var drawableShading = (Drawable) null;
    if (Board.getFieldType(gameState, Utils.getTime() < endTime ? lastPosition : mole.position) == Board.FieldType.HOLE) {
      drawableColor = ContextCompat.getDrawable(Utils.getContext(), R.drawable.ic_molegroundshade);
      drawableShading = ContextCompat.getDrawable(Utils.getContext(), R.drawable.ic_moleground);
    } else {
      drawableColor = ContextCompat.getDrawable(Utils.getContext(), R.drawable.ic_moleshade);
      drawableShading = ContextCompat.getDrawable(Utils.getContext(), R.drawable.ic_mole);
    }
    var size = Math.max(30, 240 / gameState.radius);
    var bounds = new Rect((int) d[0] - size, (int) d[1] - size, (int) d[0] + size, (int) d[1] + size);
    Objects.requireNonNull(drawableColor).setBounds(bounds);
    Objects.requireNonNull(drawableShading).setBounds(bounds);
    drawableColor.setColorFilter(GameView.getPlayerColor(gameState, mole.player), PorterDuff.Mode.MULTIPLY); // Sets drawableColor to the color of the mole
    drawableColor.draw(canvas);
    drawableShading.draw(canvas);
  }
}
