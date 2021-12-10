package de.thundergames.playmechanics.board;

import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GameHandler {
  final public static long DEFAULT_TIMEOUT = 10000; // 10 seconds
  private final ArrayList<PlayerModel> players;
  private ArrayList<NodeType> nodeTypes;
  private final PlayerModel activePlayer;
  private Board board;
  private final long timeout;
  private final int boardRadius;

  /**
   * @param players
   * @param boardRadius
   * @param nodeTypes
   * @param timeout
   * @author Alp, Dila, Issam
   * @use constructor
   */
  public GameHandler(@NotNull final ArrayList<PlayerModel> players, final int boardRadius, @NotNull final List<NodeType> nodeTypes, final long timeout) {
    this.players = players;
    this.activePlayer = players.get(0);
    this.timeout = timeout;
    this.boardRadius = boardRadius;
    this.nodeTypes = new ArrayList<>(nodeTypes);
  }

  /**
   * @param players
   * @param boardRadius
   * @param nodeTypes
   * @author Alp, Dila, Issam
   * @use constructor
   */
  public GameHandler(@NotNull final ArrayList<PlayerModel> players, final int boardRadius, @NotNull final List<NodeType> nodeTypes) {
    this(players, boardRadius, nodeTypes, DEFAULT_TIMEOUT);
  }

  /**
   * @param container
   * @author Alp, Dila, Issam
   * @use starts the pane
   */
  public void start(@NotNull final Pane container) {
    this.board = new Board(this.boardRadius, container.getWidth(), container.getHeight());
    this.board.setContainerBackground(container, "background/ground.png"); // TODO: change depending on level
    this.board.setPlayers(this.players);
    this.board.setNodeTypes(this.nodeTypes);
    this.board.render();
    this.activePlayer.setItMyTurn(true);
  }

  public void loop() {
        // TODO: Implement game loop logic with timeout
    }

    public Board getBoard() {
        return this.board;
    }

    public void setNodeTypes(List<NodeType> nodeTypes) {
        this.nodeTypes = new ArrayList<>(nodeTypes);
    }

}
