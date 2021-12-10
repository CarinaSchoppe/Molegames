package de.thundergames.playmechanics.Board;

import javafx.application.Application;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class TestWindow extends Application {

    final static int BOARD_RADIUS = 3;

    public static void main(String[] args) {
         launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        BorderPane borderPane = new BorderPane();

        // Set custom cursor
        Image cursor = new Image(Utils.getSprite("game/cursor.png"));
        borderPane.setCursor(new ImageCursor(cursor,
                cursor.getWidth() / 2,
                cursor.getHeight() /2));

        int maxPossibleId = 3 * (int) Math.pow(BOARD_RADIUS, 2) + 3 * BOARD_RADIUS + 1;

        // Create a game handler and add random players to it
        List<NodeType> nodeTypes = generateRandomNodeTypes( 50);
        ArrayList<Player> players = generateThreePlayers();
        GameHandler gameHandler = new GameHandler(players, BOARD_RADIUS, nodeTypes);

        gameHandler.start(borderPane);


        // Add resize event listener
        ChangeListener<Number> resizeObserver = (obs, newValue, oldValue) -> gameHandler.getBoard().onResize(borderPane.getWidth(), borderPane.getHeight());
        borderPane.widthProperty().addListener(resizeObserver);
        borderPane.heightProperty().addListener(resizeObserver);


        // Add board to center of borderPane
        borderPane.setCenter(gameHandler.getBoard());
        Scene s = new Scene(borderPane);
        primaryStage.setScene(s);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public ArrayList<Mole> generateRandomMoles(int numMoles, int minId, int maxId) {
        ArrayList<Mole> moles = new ArrayList<>();
        int[]  randomIntsArray = IntStream.generate(() -> new Random().nextInt(maxId - minId + 1) + minId).limit(numMoles).toArray();
        for(int id : randomIntsArray) {
            moles.add(new Mole(id, 40));
        }
        return moles;
    }


    public ArrayList<Player> generateThreePlayers() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(1, generateRandomMoles(3, 1, 7)));
        players.add(new Player(2, generateRandomMoles(3,8, 13)));
        players.add(new Player(3, generateRandomMoles(3, 14, 19)));
        return players;
    }

    public ArrayList<NodeType> generateRandomNodeTypes(int size) {
        ArrayList<NodeType> nodeTypes = new ArrayList<>();
        for(int i = 1; i <= size; i++) {
            nodeTypes.add(randomValueFromArray(NodeType.values()));
        }
        return nodeTypes;
    }


    <T> T randomValueFromArray(T[] values) {
        return values[new Random().nextInt(values.length)];
    }


}
