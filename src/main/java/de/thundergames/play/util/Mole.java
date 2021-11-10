package de.thundergames.play.util;

import de.thundergames.gameplay.player.Player;
import de.thundergames.play.map.Field;
import de.thundergames.play.map.Floors;
import org.jetbrains.annotations.NotNull;

public class Mole {

  private Floors floor;
  private Field field;
  private boolean inHole = false;
  private final int MoleID;
  private final Player player;

  public Mole(final int moleID, @NotNull final Player player) {
    this.player = player;
    MoleID = moleID;
  }

  public int getMoleID() {
    return MoleID;
  }

  /**
   * @return sais if a mole can be moved (important for AI)
   * @author Carina
   * @see Player
   * @see de.thundergames.gameplay.ai.AI
   */
  public boolean isMoveable() {
    //TODO hier logik einbauen
    return false;
  }

  public void setField(Field field) {
    this.field = field;
  }

  public Field getField() {
    return field;
  }

  public Player getPlayer() {
    return player;
  }
}


