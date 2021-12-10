package de.thundergames.playmechanics.Board;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class GameHandler  {
    final public  static  long DEFAULT_TIMEOUT = 10000; // 10 seconds
    private ArrayList<Player> players;
    private ArrayList<NodeType> nodeTypes;
    private Player activePlayer;
    private Board board;
    private long timeout;
    private int boardRadius;

    public GameHandler(ArrayList<Player> players, int boardRadius, List<NodeType> nodeTypes, long timeout) {
        this.players = players;
        this.activePlayer = players.get(0);
        this.timeout = timeout;
        this.boardRadius = boardRadius;
        this.nodeTypes = new ArrayList<>(nodeTypes);
    }

    public GameHandler(ArrayList<Player> players, int boardRadius, List<NodeType> nodeTypes) {
        this(players, boardRadius, nodeTypes, DEFAULT_TIMEOUT);
    }

    public void start(Pane container) {
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
