package gameplay.ai;

import game.util.Mole;
import gameplay.player.PlayerHandler;

public class AI extends PlayerHandler implements Runnable {
    private boolean shouldRun = false;
    private final Thread AIThread;
    private boolean isMove = false;

    public AI(Thread AIThread, boolean shouldRun) {
        this.AIThread = AIThread;
        this.shouldRun = shouldRun;
    }


    public void start() {
        AIThread.start();
        isMove = true;
    }

    private void makeMove() {

    }

    @Override
    public void run() {
        boolean moveable = false;
        while (true) {
            if (isMove) {
                for (Mole mole : getPlayerMolesOnField()) {
                    if (mole.isMoveable() && moveable == false) {
                        moveable = true;
                        makeMove();
                    }
                }

                if (!moveable) {
                    for (Mole mole : getPlayerMolesInHoles()) {
                        if (mole.isMoveable() && moveable == false) {
                            moveable = true;
                            makeMove();

                        }
                    }
                }
                moveable = false;
                isMove = false;
            }
        }
    }


    public boolean isShouldRun() {
        return shouldRun;
    }

    public void setShouldRun(boolean shouldRun) {
        this.shouldRun = shouldRun;
    }

    public Thread getAIThread() {
        return AIThread;
    }
}
