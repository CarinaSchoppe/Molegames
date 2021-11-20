package de.thundergames.playmechanics.util.interfaceItems;

import com.google.gson.Gson;

public class InterfaceObject  {

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
