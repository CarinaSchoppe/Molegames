package de.thundergames.playmechanics.Board;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Player {
    private int id;
    private ArrayList<Mole> moles;
    private Mole activeMole;
    private Marker activeMoleMarker;
    private boolean isItMyTurn;

    public Player(int id, ArrayList<Mole> moles) {
        this.id = id;
        this.moles = moles;
        this.activeMole = this.moles.get(0); // By default first mole in the list
        this.activeMoleMarker = new Marker();
        this.isItMyTurn = false;
        this.attachMolesClickEvent();

    }

    public int getId() {
        return this.id;
    }

    public ArrayList<Mole> getMoles() {
        this.updateMoles();
        return this.moles;
    }

    public List<Integer> getOccupiedIds() {
        return this.moles.stream().map(mole -> mole.getMoleId()).collect(Collectors.toList());
    }

    public void updateMoles() {
        this.moles.forEach(mole -> mole.setDisable(!this.isItMyTurn));
        this.moles.forEach(mole -> mole.setOpacity(this.isItMyTurn ? 1 : 0.6));
    }

    public Marker getMarker() {
        this.updateMarker();
        return this.activeMoleMarker;
    }

    public void updateMarker() {
        this.activeMoleMarker.setLayoutX(this.activeMole.getLayoutX() + 16);
        this.activeMoleMarker.setLayoutY(this.activeMole.getLayoutY() - 16);
        this.activeMoleMarker.setDisable(!this.isItMyTurn);
        this.activeMoleMarker.setOpacity(this.isItMyTurn ? 1 : 0);
    }

    public void setMoles(ArrayList<Mole> moles) {
        this.moles = moles;
    }

    public void setActiveMole(Mole mole) {
        this.activeMole = mole;
        this.updateMarker();
    }

    public void setItMyTurn(boolean isItMyTurn) {
        this.isItMyTurn = isItMyTurn;
    }

    public void notifyNodeClick(Node n) {
        double moleCenter = this.activeMole.getSize() / 2;
        double x = n.getCenterX() - moleCenter ;
        double y = n.getCenterY() -  moleCenter;
        this.activeMole.updatePostion(x,y);
        this.updateMarker();
    }

    public void attachMolesClickEvent() {
        this.moles.forEach(mole -> {
            mole.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    setActiveMole(mole);
                }
            });
        });
    }
}
