/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 21:35 by Carina Latest changes made by Carina on 09.01.22, 21:35 All contents of "PlayerModel" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import de.thundergames.playmechanics.util.Player;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Getter
@Setter
public class PlayerModel {
  private final String playerColor;
  private int ID;
  private ArrayList<MoleModel> moles;
  private MoleModel activeMole;
  private boolean isItMyTurn;
  private Player player;

  public PlayerModel(@NotNull final Player player, @NotNull final ArrayList<MoleModel> moles, final boolean isItMyTurn, @NotNull final String playerColor) {
    this.player = player;
    this.moles = moles;
    this.isItMyTurn = isItMyTurn;
    this.playerColor = playerColor;
  }

  public void setMoles(@NotNull final ArrayList<MoleModel> moles) {
    this.moles = moles;
  }

  public void setItMyTurn(final boolean isItMyTurn) {
    this.isItMyTurn = isItMyTurn;
  }
}
