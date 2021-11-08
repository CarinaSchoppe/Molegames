package game.map;

public class Hole   {
  int[] id = new int[2];
  boolean used;
  public Hole(int[] id, boolean used) {
    this.id = id;
    this.used = used;
  }

  public void setUsed(boolean used) {
    this.used = used;
  }
}
