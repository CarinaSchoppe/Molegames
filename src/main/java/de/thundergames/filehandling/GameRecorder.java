package de.thundergames.filehandling;

import de.thundergames.play.game.Game;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GameRecorder {

  private final Game game;
  private final FileWriter writer;

  /**
   * @param game the game that will be used to record
   * @throws IOException
   * @author Carina
   * @use creates a new GameRecorder for the game
   */
  public GameRecorder(@NotNull final Game game) throws IOException {
    this.game = game;
    File file = new File("records/record" + game.getGameID() + ".json");
    writer = new FileWriter(file);
  }

  /**
   * @author Carina
   * @use Writes the current map with all field details and the player details to the file
   */
  public void record() {
    try {
      writer.write(game.getMap().toJSONString());
      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

