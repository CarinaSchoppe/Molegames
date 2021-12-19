/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.12.21, 16:37 by Carina Latest changes made by Carina on 18.12.21, 16:35
 * All contents of "PlayerModel" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import de.thundergames.gameplay.player.Client;
import de.thundergames.networking.server.ServerThread;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import de.thundergames.playmechanics.util.Player;


@Getter
@Setter
public class PlayerModel{
  private int ID;
  private Marker activeMoleMarker;
  private ArrayList<MoleModel> moles;
  private MoleModel activeMole;
  private boolean isItMyTurn;
  private Player player;

  /**
   * @param ID
   * @param moles
   * @author Alp, Dila, Issam
   */
  public PlayerModel(@NotNull Client client, final int ID, @NotNull final ArrayList<MoleModel> moles,Player player) {
    this.ID = ID;
    this.moles = moles;
    this.activeMole = this.moles.get(0); // By default first mole in the list
    this.activeMoleMarker = new Marker();
    this.isItMyTurn = false;
    this.attachMolesClickEvent();
    this.player=player;
  }

  public PlayerModel() {

  }


  public PlayerModel(@NotNull ServerThread client,Player player) {
  }

  public PlayerModel(@NotNull Client client,Player player) {
  }

  public PlayerModel(Player player) {
    this.player = player;
    this.moles = this.player.getMoles().stream().map(mole -> new MoleModel(1, mole)).collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * @author Alp, Dila, Issam
   * @use updates the moles
   */
//  public void updateMoles() {
//    this.moles.forEach(mole -> mole.setDisable(!this.isItMyTurn));
//    this.moles.forEach(mole -> mole.setOpacity(this.isItMyTurn ? 1 : 0.6));
//  }

  /**
   * @author Alp, Dila, Issam
   * @use updates the marker
   */
  public void updateMarker() {
    this.activeMoleMarker.setLayoutX(this.activeMole.getLayoutX() + 16);
    this.activeMoleMarker.setLayoutY(this.activeMole.getLayoutY() - 16);
    this.activeMoleMarker.setDisable(!this.isItMyTurn);
    this.activeMoleMarker.setOpacity(this.isItMyTurn ? 1 : 0);
  }

  /**
   * @author Alp, Dila, Issam
   * @use notifies the node on the klick
   */
  public void notifyNodeClick(@NotNull final Node node) {
    var moleCenter = this.activeMole.getSize() / 2;
    var x = node.getCenterX() - moleCenter;
    var y = node.getCenterY() - moleCenter;
    this.activeMole.updatePostion(x, y);
    this.updateMarker();
  }

  public void attachMolesClickEvent() {
    this.moles.forEach(
        mole -> {
          mole.setOnAction(event -> setActiveMole(mole));
        });
  }

  public void setMoles(ArrayList<MoleModel> moles) {
    this.moles = moles;
  }

  public void setItMyTurn(boolean isItMyTurn) {
    this.isItMyTurn = isItMyTurn;
  }

  public Marker getMarker() {
    this.updateMarker();
    return this.activeMoleMarker;
  }

  public void setActiveMole(MoleModel mole) {
    this.activeMole = mole;
    this.updateMarker();
  }

  public List<Integer> getOccupiedIDs() {
    return this.moles.stream().map(mole -> mole.getMoleId()).collect(Collectors.toList());
  }
}
