package de.pentagames.maulwurfkompanie.board;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import org.jetbrains.annotations.NotNull;

/**
 * This class manages the thread used for operations concerning the gameplay.
 */
public class GameThread extends Thread {
  public static Canvas canvas;
  private final SurfaceHolder surfaceHolder;
  private final GameView gameView;
  private boolean running;

  public GameThread(@NotNull final SurfaceHolder surfaceHolder, @NotNull final GameView gameView) {
    super();
    this.surfaceHolder = surfaceHolder;
    this.gameView = gameView;
  }

  public void setRunning(boolean isRunning) {
    this.running = isRunning;
  }

  @Override
  public void run() {
    long startTime;
    long timeMillis;
    long waitTime;
    long totalTime = 0;
    int frameCount = 0;
    int targetFPS = 30;
    long averageFPS;
    long targetTime = 1000 / targetFPS;
    while (running) {
      startTime = System.nanoTime();
      canvas = null;
      try {
        canvas = this.surfaceHolder.lockCanvas();
        if (canvas != null) {
          synchronized (surfaceHolder) {
            this.gameView.draw(canvas);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (canvas != null) {
          try {
            surfaceHolder.unlockCanvasAndPost(canvas);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      timeMillis = (System.nanoTime() - startTime) / 1000000;
      waitTime = targetTime - timeMillis;
      try {
        sleep(waitTime);
      } catch (Exception ignored) {
      }
      totalTime += System.nanoTime() - startTime;
      frameCount++;
      if (frameCount == targetFPS) {
        averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
        frameCount = 0;
        totalTime = 0;
        //                System.out.println(averageFPS);
      }
    }
  }
}
