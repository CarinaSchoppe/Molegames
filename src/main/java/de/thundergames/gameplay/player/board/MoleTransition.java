package de.thundergames.gameplay.player.board;

import javafx.animation.PathTransition;
import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;


public class MoleTransition {


    public static PathTransition transitionMole(MoleModel mole, Point2D moveDistance) {

        Path path = new Path();
        path.getElements().add(new MoveTo(mole.getSize() / 2 , mole.getSize() / 2));
        path.getElements().add(new LineTo(moveDistance.getX(), moveDistance.getY()));

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(1));
        pathTransition.setNode(mole);
        pathTransition.setPath(path);
        pathTransition.play();

        return pathTransition;
    }



}
