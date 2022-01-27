package de.pentagames.maulwurfkompanie.board;


import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Objects;

import de.pentagames.maulwurfkompanie.R;
import de.pentagames.maulwurfkompanie.Utils;
import de.pentagames.maulwurfkompanie.client.Client;
import upb.maulwurfcompany.library.data.GameState;
import upb.maulwurfcompany.library.data.Player;
import upb.maulwurfcompany.library.messages.MoleMoved;
import upb.maulwurfcompany.library.data.GameState;

public class ShownDisc {

    public Integer discNumber;
    private final float posX;
    private final float posY;
    private final float radius;
    private boolean selected = false;
    private boolean played = false;



    public ShownDisc(Integer discNumber, Integer index, Integer discsSize, Integer width, Integer height) {
        this.discNumber = discNumber;
        this.radius = Math.min((float) (width * 0.11),(float) ((width * 0.7)/(discsSize * 2)));
        this.posX = radius + (index) * (2*radius) + (radius/3)*( index + 1);
        this.posY = height - 100 - radius ;

    }


    public void draw(GameState gameState, Canvas canvas) {

        Drawable drawableSelection;
        Drawable drawableDiscNumber;
        Drawable drawableShading;
        Rect bounds = new Rect((int)this.posX-(int)this.radius,
                (int)this.posY-(int)this.radius,
                (int)this.posX+(int)this.radius,
                (int)this.posY+(int)this.radius);


        drawableSelection = ContextCompat.getDrawable(Utils.getContext(), R.drawable.ic_discselected);
        drawableSelection.setBounds(bounds);

        if(this.selected) {
            drawableSelection.draw(canvas);
        }
        switch (this.discNumber) {
            case 1:
                drawableDiscNumber = ContextCompat.getDrawable(Utils.getContext(),
                        R.drawable.ic_disc1);
                break;
            case 2:
                drawableDiscNumber = ContextCompat.getDrawable(Utils.getContext(),
                        R.drawable.ic_disc2);
                break;
            case 3:
                drawableDiscNumber = ContextCompat.getDrawable(Utils.getContext(),
                        R.drawable.ic_disc3);
                break;
            case 4:
                drawableDiscNumber = ContextCompat.getDrawable(Utils.getContext(),
                        R.drawable.ic_disc4);
                break;
            case 5:
                drawableDiscNumber = ContextCompat.getDrawable(Utils.getContext(),
                        R.drawable.ic_disc5);
                break;
            default:
                drawableDiscNumber = ContextCompat.getDrawable(Utils.getContext(),
                        R.drawable.ic_discclosed);
                break;
        }

        //Hides the discs which are not playable anymore.
        if(this.played){
            drawableDiscNumber = ContextCompat.getDrawable(Utils.getContext(), R.drawable.ic_discclosed);
        }

        drawableShading = ContextCompat.getDrawable(Utils.getContext(), R.drawable.ic_discshade);
        drawableShading.setColorFilter(GameView.getPlayerColor(gameState, getPlayer(gameState)), PorterDuff.Mode.MULTIPLY);// Sets drawableShading to the color of the player

        drawableDiscNumber.setBounds(bounds);
        drawableShading.setBounds(bounds);

        drawableShading.draw(canvas);
        drawableDiscNumber.draw(canvas);
        //Reset played state of discs
        if(Objects.requireNonNull(gameState.pullDiscs.get(Client.clientId)).length
                == Client.messageHandler.game.pullDiscs.length){
            this.played = false;
        }
    }
    //Needed to enable getting player color.
    private Player getPlayer(GameState gameState) {
        for( Player player : gameState.activePlayers){
            if(player.clientID == Client.clientId ){
                return player;
            }
        }
        return null;
    }

    //Check if the touch coordinates is in the disc's surface.
    public boolean isItIn(float x, float y, ArrayList<ShownDisc> discArray) {
        if( Math.sqrt(Math.pow((x - this.posX),2) + Math.pow((y - this.posY),2)) < this.radius){
            //Reset selected condition of other cards.
            for(ShownDisc disc: discArray){
                if(disc.selected){
                    disc.selected = false;
                }
            }
            this.selected = true;
            return true;
        }
        return false;
    }

    public static void moveSuccessful(MoleMoved message) {
        for(ShownDisc disc: GameView.instance.shownDiscs){
            if(disc.selected && disc.discNumber == message.pullDisc){
                disc.played = true;
                disc.selected = false;
            }
        }

    }

}
