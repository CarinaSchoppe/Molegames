package de.thundergames.gameplay.player.board;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerTable {
        private String name;
        private int score;
        private int placement;
        private String pullDiscs;
}


