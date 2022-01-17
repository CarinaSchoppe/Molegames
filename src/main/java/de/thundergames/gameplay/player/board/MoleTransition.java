/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 17.01.22, 19:10 by Carina Latest changes made by Carina on 17.01.22, 19:10 All contents of "MoleTransition" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class MoleTransition {

  public static PathTransition transitionMole(MoleModel mole, Point2D moveDistance) {
    Path path = new Path();
    path.getElements().add(new MoveTo(Marker.DEFAULT_SIZE, Marker.DEFAULT_SIZE / 2));
    path.getElements().add(new LineTo(moveDistance.getX() + Marker.DEFAULT_SIZE, moveDistance.getY() + Marker.DEFAULT_SIZE / 2));
    PathTransition pathTransition = new PathTransition();
    pathTransition.setDuration(Duration.seconds(1));
    pathTransition.setNode(mole);
    pathTransition.setPath(path);
    pathTransition.play();
    return pathTransition;
  }

  public static void placeMole(MoleModel mole, Node nodeTo) {
    final KeyFrame keyframe1 = new KeyFrame(Duration.seconds(0), e -> {
      nodeTo.highlightNode(true);
      mole.showMarker(true);
    });
    final KeyFrame keyframe2 = new KeyFrame(Duration.seconds(2), e -> {
      nodeTo.highlightNode(false);
      mole.showMarker(false);
    });
    final Timeline timeline = new Timeline(keyframe1, keyframe2);
    timeline.play();
  }
}
