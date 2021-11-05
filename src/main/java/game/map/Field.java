package game.map;

public class Field {
  private int[] id = new int[2];
  private boolean occupied;
  private boolean hole;
  private boolean repeat;


  public Field(int[] id) {
    this.id = id;

  }

  public int[] getId() {
    return id;
  }

  public boolean isOccupied() {
    return occupied;
  }

  public boolean isHole() {
    return hole;
  }

  public boolean isRepeat() { return repeat; }

  public void setOccupied(boolean occupied) {
    this.occupied = occupied;
  }

  public void setHole(boolean hole) {
    this.hole = hole;
  }

  @Override
  public String toString() {
    return "field x: "+ id[0] + " y: " + id[1];
  }
}
