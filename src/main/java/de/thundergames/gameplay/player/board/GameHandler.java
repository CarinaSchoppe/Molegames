/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 19:20 by Carina Latest changes made by Carina on 15.12.21, 19:19 All contents of "GameHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import de.thundergames.playmechanics.util.Player;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameHandler {
  final public static long DEFAULT_TIMEOUT = 10000; // 10 seconds
  private ArrayList<PlayerModel> players;
  private final PlayerModel activePlayer;
  private final long timeout;
  private final int boardRadius;
  private HashMap<List<Integer>, NodeType> nodeTypes;
  private Board board;
  private BorderPane container;

  /**
   * @param players
   * @param boardRadius
   * @param nodeTypes
   * @param timeout
   * @author Alp, Dila, Issam
   * @use constructor
   */
  public GameHandler(@NotNull final ArrayList<PlayerModel> players, final int boardRadius, @NotNull final HashMap<List<Integer>, NodeType> nodeTypes, final long timeout, BorderPane container) {
    this.players = players;
    this.activePlayer = players.get(0);
    this.timeout = timeout;
    this.boardRadius = boardRadius;
    this.nodeTypes = new HashMap<>(nodeTypes);
    this.container = container;
  }

  /**
   * @param players
   * @param boardRadius
   * @param nodeTypes
   * @author Alp, Dila, Issam
   * @use constructor
   */
  public GameHandler(@NotNull final ArrayList<PlayerModel> players, final int boardRadius, @NotNull final HashMap<List<Integer>, NodeType> nodeTypes, BorderPane container) {
    this(players, boardRadius, nodeTypes, DEFAULT_TIMEOUT, container);
  }

  /**
   * @param container
   * @author Alp, Dila, Issam
   * @use starts the pane
   */
  public void start(ArrayList<PlayerModel> players) {
    this.board = new Board(this.boardRadius, container.getWidth(), container.getHeight(),nodeTypes,players);
    this.board.setContainerBackground(container, "background/ground.png"); // TODO: change depending on level
    this.board.render();
    this.activePlayer.setItMyTurn(true);
  }

  public void update(ArrayList<PlayerModel> players) {
    this.players = players;
    Platform.runLater(() -> {
      start(players);
      this.container.getChildren().clear();
      //this.container.getChildren().add(board);
      this.container.setCenter(this.board);
    });
  }

  public Board getBoard() {
    return this.board;
  }
}
