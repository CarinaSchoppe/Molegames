package de.pentagames.maulwurfkompanie.board;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.pentagames.maulwurfkompanie.client.Client;
import upb.maulwurfcompany.library.data.GameState;
import upb.maulwurfcompany.library.data.GameStatus;
import upb.maulwurfcompany.library.data.Player;
import upb.maulwurfcompany.library.data.Position;
import upb.maulwurfcompany.library.messages.MakeMove;
import upb.maulwurfcompany.library.messages.PlaceMole;
import de.pentagames.maulwurfkompanie.R;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

  public static GameView instance;
  private GameThread thread;
  public ArrayList<ShownMole> shownMoles;
  private final Scoreboard scoreboard = new Scoreboard();
  public ArrayList<ShownDisc> shownDiscs = new ArrayList<>();
  public ArrayList<Position> lastTwoClicks = new ArrayList<>();
  public Integer selectedDisc;
  private Paint fillPaint;
  private Paint linePaint;
  public Drawable tile;
  public int currentLevel = -1;

  public GameView(@NotNull final Context context, @NotNull final AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public GameView(@NotNull final Context context, @NotNull final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public GameView(@NotNull final Context context) {
    super(context);
    init();
  }

  // Calculate the color of the given player
  public static int getPlayerColor(@NotNull final GameState gameState, @NotNull final Player player) {
    var index = 0;
    for (; index < gameState.score.players.length; index++) {
      if (gameState.score.players[index].clientID == player.clientID) break;
    }
    if(gameState.score.players.length == 0){
      return 0;}
    else {
      var hue = (360 / gameState.score.players.length) * index;
      return hsv_to_rgb(hue);
    }
  }

  /**
   * @param hue the hue as an int between 0 and 360
   */
  private static int hsv_to_rgb(int hue) {
    int h = (int) ((float) (hue % 360) / 60);
    float f = (float) hue / 60 - h;
    float p = (float) 0.9 * (1 - (float) 0.9);
    float q = (float) 0.9 * (1 - f * (float) 0.9);
    float t = (float) 0.9 * (1 - (1 - f) * (float) 0.9);
    int value2 = (int) ((float) 0.9 * 255);
    int t2 = (int) (t * 255);
    int p2 = (int) (p * 255);
    int q2 = (int) (q * 255);
    switch (h) {
      case 0:
        return Color.rgb(value2, t2, p2);
      case 1:
        return Color.rgb(q2, value2, p2);
      case 2:
        return Color.rgb(p2, value2, t2);
      case 3:
        return Color.rgb(p2, q2, value2);
      case 4:
        return Color.rgb(t2, p2, value2);
      case 5:
        return Color.rgb(value2, p2, q2);
      default:
        throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + (float) 0.9 + ", " + (float) 0.9);
    }
  }

  public void init() {
    setFocusable(true);
    this.getHolder().addCallback(this); // TODO move to created?
    fillPaint = new Paint();
    fillPaint.setAntiAlias(true);
    linePaint = new Paint();
    linePaint.setColor(Color.rgb(0, 0, 0));
    linePaint.setAntiAlias(true);
    linePaint.setStrokeWidth(8);
    //initiate the discs visually.
    for(int i = 0; i < Client.messageHandler.game.pullDiscs.length; i++) {
      shownDiscs.add(new ShownDisc(Client.messageHandler.game.pullDiscs[i], i,
              Client.messageHandler.game.pullDiscs.length,
              getResources().getDisplayMetrics().widthPixels,
              getResources().getDisplayMetrics().heightPixels));
    }
  }

  public void reset(@NotNull final GameState gameState) {
    Client.messageHandler.until = 0; // TODO read from gameState
    shownMoles = new ArrayList<>();
    for (var mole : gameState.placedMoles) shownMoles.add(new ShownMole(mole));
    scoreboard.currentPage = 1;
    resize();
  }

  @Override
  public void draw(@NotNull final Canvas canvas) {
    if (Client.messageHandler.currentGameState != null) {

      super.draw(canvas);
      if (shownMoles == null && Client.messageHandler.currentGameState != null)
        reset(Client.messageHandler.currentGameState);

      if (currentLevel != Client.messageHandler.currentGameState.levelNumber) {
        loadTile();
      }
      for (int tileX = 0; tileX < getWidth(); tileX += 256) {
        for (int tileY = 0; tileY < getHeight(); tileY += 256) {
          tile.setBounds(tileX, tileY, tileX + 256, tileY + 256);
          tile.draw(canvas);
        }
      }

      if (Client.messageHandler.currentGameState == null) return;
      var gameState = Client.messageHandler.currentGameState;
      var game = Client.messageHandler.game;
      if (gameState.status != GameStatus.OVER) // draw the scoreboard under the game board
        scoreboard.drawInGameScoreboard(game, gameState, canvas);
      // background
      var path = new Path();
      var start = Pos.getDisplay(-0.5f, -0.5f);
      path.moveTo(start[0], start[1]);
      for (var p : new float[][]{{game.radius, -0.5f}, {2 * game.radius + 0.5f, game.radius}, {2 * game.radius + 0.5f, 2 * game.radius + 0.5f}, {game.radius, 2 * game.radius + 0.5f}, {-0.5f, game.radius}}) {
        path.lineTo(Pos.getDisplayX(p), Pos.getDisplayY(p));
      }
      path.close();
      fillPaint.setColor(Color.argb(50, 255, 255, 255));
      canvas.drawPath(path, fillPaint);
      // lines
      for (var y = 0; y < 2 * game.radius + 1; y++) {
        for (var r = 0; r < 360; r += 120) {
          canvas.save();
          canvas.rotate(r, Pos.getDisplayX(game.radius, game.radius), Pos.getDisplayY(game.radius, game.radius));
          var left = Pos.getDisplay(Math.max(y - game.radius, 0), y);
          var right = Pos.getDisplay(game.radius + Math.min(y, game.radius), y);
          canvas.drawLine(left[0], left[1], right[0], right[1], linePaint);
          canvas.restore();
        }
      }
      // points
      for (var y = 0; y <= 2 * game.radius; y++) {
        for (var x = Math.max(0, y - game.radius); x <= game.radius + Math.min(y, game.radius); x++) {
          canvas.drawCircle(Pos.getDisplayX(x, y), Pos.getDisplayY(x, y), Math.max(5, 50f / game.radius), linePaint);
        }
      }
      fillPaint.setColor(Color.rgb(121, 85, 72));
      for (var hole : gameState.level.holes) {
        canvas.drawCircle(Pos.getDisplayX(hole), Pos.getDisplayY(hole), Math.max(10, 100f / game.radius), fillPaint);
      }
      fillPaint.setColor(Color.rgb(227, 217, 27));
      for (var again : gameState.level.drawAgainFields) {
        canvas.drawCircle(Pos.getDisplayX(again), Pos.getDisplayY(again), Math.max(10, 100f / game.radius), fillPaint);
      }
      for (var mole : new ArrayList<>(shownMoles))
        mole.draw(gameState, canvas, getWidth());
      if (gameState.status == GameStatus.OVER) // draw the winning scoreboard on top of the game board
        scoreboard.drawWinningScoreboard(gameState, canvas);
      if (gameState.status != GameStatus.OVER) {
        for (ShownDisc disc : shownDiscs)
          disc.draw(gameState, canvas);
      }
    }
  }

  private void resize() {
    var game = Client.messageHandler.game;
    var rotate = getHeight() > getWidth();
    var margin = (float) 40;
    // height of each triangle ist sqrt(3/4)*width of triangle , because w^2 = h^2 + (0.5w)^2
    var step = ((rotate ? Math.min(getHeight(), getWidth() / (float) Math.sqrt(0.75)) : Math.min(getHeight() / (float) Math.sqrt(0.75), getWidth())) - 2f * margin) / (2f * game.radius + 1);
    var matrix = new Matrix();
    matrix.postTranslate(-game.radius, -game.radius);
    matrix.postSkew(-0.5f, 0, 0, 0);
    matrix.postScale(step, step * (float) Math.sqrt(0.75));
    if (rotate) matrix.postRotate(30, 0, 0);
    matrix.postTranslate(getWidth() / 2f, getHeight() / 2f);
    Pos.setMatrix(matrix);
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public void surfaceCreated(@NotNull final SurfaceHolder holder) {
    instance = this;
    thread = new GameThread(getHolder(), this);
    thread.setRunning(true);
    thread.start();
    this.setOnTouchListener((v, e) -> {
      if (e.getAction() != MotionEvent.ACTION_DOWN) return false;
      if (Client.messageHandler.currentGameState == null) return false;
      Position p = Pos.fromDisplay(e.getX(), e.getY());

      if(Client.participant && Client.messageHandler.currentGameState.status == GameStatus.STARTED) {

        if (Board.onBoard(Client.messageHandler.currentGameState, p)) {
          if (e.getAction() == MotionEvent.ACTION_DOWN){
            //Placement phase.
            if(Client.messageHandler.currentGameState.placedMoles.length <
                    Client.messageHandler.currentGameState.activePlayers.length *
                            Client.messageHandler.currentGameState.moles &&
                    Client.messageHandler.currentGameState.levelNumber == 0) {
              Client.messageHandler.placeMole(new PlaceMole(p));
            }else{
              //Move phase.
              //Card needs to be selected first.

              if(selectedDisc != null && selectedDisc != -1) {
                //Reset at too many clicks
                if (lastTwoClicks.size() > 2) {
                  lastTwoClicks.clear();
                  return true;
                }
                //Save the first point.
                if(lastTwoClicks.size() == 0){
                  lastTwoClicks.add(p);
                  return true;
                }
                //Select the second point and send the move request.
                if(lastTwoClicks.size() == 1) {
                  lastTwoClicks.add(p);
                  Client.messageHandler.makeMove(new MakeMove(lastTwoClicks.get(0),
                          lastTwoClicks.get(1), selectedDisc));
                  lastTwoClicks.clear();
                  return true;
                }
              }
            }
            return true;
          }
        }else{
          //Select Disc
          selectedDisc = getDisc(e.getX(),e.getY(),shownDiscs);
          lastTwoClicks.clear();
          return true;
        }
      }
      if (e.getY() < 300) {
        scoreboard.nextPage();
        return true;
      }
      return true;
    });
  }

  /**
   * This method reads the touch position on screen and returns back the clicked pullDisc value.
   * @param x,y Touch coordinates
   * @author Dila, Alp
   */
  public Integer getDisc(float x, float y, ArrayList<ShownDisc> discArray) {
    for(ShownDisc disc: shownDiscs)
      if(disc.isItIn(x, y, discArray)){
        return disc.discNumber;
      }
    return null;
  }
  /**
   * This method loads a tile image according to the current level number.
   * @author Dila, Alp
   */
  public void loadTile(){

    if(Client.messageHandler.currentGameState.levelNumber == 0){
      tile = getResources().getDrawable(R.drawable.ic_ground, null);
    }
    if(Client.messageHandler.currentGameState.levelNumber > 0 &&
            Client.messageHandler.currentGameState.levelNumber
                    != Client.messageHandler.game.levelCount - 2) {
      switch (Client.messageHandler.currentGameState.levelNumber % 3) {
        case 1:
          tile = getResources().getDrawable(R.drawable.ic_dirt, null);
          break;
        case 2:
          tile = getResources().getDrawable(R.drawable.ic_rock, null);
          break;
        case 0:
          tile = getResources().getDrawable(R.drawable.ic_sand, null);
          break;
      }
    }
    if(Client.messageHandler.currentGameState.levelNumber
            == Client.messageHandler.game.levelCount - 2) {
      tile = getResources().getDrawable(R.drawable.ic_final, null);
    }

    currentLevel = Client.messageHandler.currentGameState.levelNumber;
  }

  @Override
  public void surfaceChanged(@NotNull final SurfaceHolder holder, final int format, final int width, final int height) {
    resize();
  }

  @Override
  public void surfaceDestroyed(@NotNull final SurfaceHolder holder) {
    var retry = true;
    while (retry) {
      try {
        thread.setRunning(false);
        thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      retry = false;
    }
  }
}
