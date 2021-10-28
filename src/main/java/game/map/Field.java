package game.map;

public class Field {
  private final int id;
  private boolean occupied;
  private final boolean hole;

  public Field(int id, boolean occupied, boolean hole) {
    this.id = id;
    this.occupied = occupied;
    this.hole = hole;
  }

  public void setOccupied(boolean occupied) {
    this.occupied = occupied;
  }

  public boolean isOccupied() {
    return occupied;
  }

  public boolean isHole() {
    return hole;
  }

  public int getId() {
    return id;
  }
}
