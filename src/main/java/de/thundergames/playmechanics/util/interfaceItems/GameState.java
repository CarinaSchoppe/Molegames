package de.thundergames.playmechanics.util.interfaceItems;

import de.thundergames.filehandling.Score;
import de.thundergames.playmechanics.util.Player;


public class GameState extends InterfaceObject{

  private NetworkPlayer[] players;
  private Player currentPlayer;
  private NetworkMole[] placedMoles;
  private int moles;
  private int radius;
  private NetworkFloor level;
  private boolean pullDiscsOrdered;
  private int[] pullDiscs; //TODO: hier
  private long visualizationTime;
  private String status;
  private Score score;





}
