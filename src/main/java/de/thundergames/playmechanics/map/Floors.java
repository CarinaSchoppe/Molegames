/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 10:33 by Carina
 * Latest changes made by Carina on 15.11.21, 10:26
 * All contents of "Floors" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Floors {

  private int floorNumber;
  private final int doubleDrawFields;
  private final ArrayList<Hole> holes = new ArrayList<>();
  private final ArrayList<Field> fields = new ArrayList<>();
  private final HashMap<List<Integer>, Field> fieldMap = new HashMap<>();
  private final ArrayList<Field> occupied = new ArrayList<>();
  private final ArrayList<Field> doubleMoves = new ArrayList<>();
  private final Map map;
  private final int holeAmount;


  public Floors(int doubleDrawFields, Map map, int holeAmount) {
    this.doubleDrawFields = doubleDrawFields;
    this.map = map;
    this.holeAmount = holeAmount;
  }

  /**
   * @param floorNumber      the floornumber that we are on
   * @param holes            amount of holes on that floor
   * @param doubleDrawFields amount of doubleDrawFields on that floor
   * @param map              the map itself that this will be placed on
   * @author Carina
   */
  public Floors(
      final int floorNumber, final int holes, final int doubleDrawFields, @NotNull final Map map) {
    this.floorNumber = floorNumber;
    this.doubleDrawFields = doubleDrawFields;
    this.map = map;
    this.holeAmount = holes;
  }

  public Map getMap() {
    return map;
  }

  public ArrayList<Field> getOccupied() {
    return occupied;
  }

  public HashMap<List<Integer>, Field> getFieldMap() {
    return fieldMap;
  }

  public ArrayList<Field> getFields() {
    return fields;
  }
}
