package de.thundergames.gameplay.player.board;

import de.thundergames.playmechanics.map.Field;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class PathSearch {
    private HashSet<Node> nodes;

    public PathSearch(HashSet<Node> nodes) {
        this.nodes = nodes;
    }

    private ArrayList<Node> findSimplePathLengthN(Node source, Node destination, int pathLength) {
        ArrayList<Node> path = new ArrayList<>();
        path.add(source);

        if(source == destination) {
            path.add(destination);
            return path;
        }


        var sourceField = source.getField();
        var destinationField = destination.getField();

        if(sourceField.getY() == destinationField.getY()) {
            var movementDirection = sourceField.getX() > destinationField.getX();
            for(int i = 1; i <= pathLength; i++) {
                var newFieldX = movementDirection ? sourceField.getX() - i : sourceField.getX() + i;
                path.add(getNodeFromField(new Field(newFieldX, sourceField.getY())));
            }
        } else if(source.getField().getX() == destinationField.getX()) {
            var movementDirection = sourceField.getY() > destinationField.getY();
            for(int i = 1; i <= pathLength; i++) {
                var newFieldY = movementDirection ? sourceField.getY() - i : sourceField.getY() + i;
                path.add(getNodeFromField(new Field(sourceField.getX(), newFieldY)));
            }
        } else {
            for(int i = 1; i <= pathLength; i++) {
                var newFieldX = sourceField.getX() > destinationField.getX() ? sourceField.getX() - i:  sourceField.getX() + i;
                var newFieldY = sourceField.getY() > destinationField.getY() ? sourceField.getY() - i:  sourceField.getY() + i;
                path.add(getNodeFromField(new Field(newFieldX , newFieldY)));
            }
        }
        return path;
    }

    private Node getNodeFromField(Field field) {
        return this.nodes.stream().filter(node -> node.getField().getX() == field.getX() && node.getField().getY() == field.getY()).findFirst().orElse(null);
    }


    public List<Node> getPathBetweenWithLength(Node source, Node target,  int length) {
        var path = findSimplePathLengthN(source, target, length);
        return new ArrayList<>(path);
    }

}
