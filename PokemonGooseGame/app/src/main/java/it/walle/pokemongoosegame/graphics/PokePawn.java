package it.walle.pokemongoosegame.graphics;

import android.content.Context;

import it.walle.pokemongoosegame.graphics.AppConstants;

public class PokePawn {

    //declaring the new pawn res

    int pokePawnImageX = 0, pokePawnImageY = 0, pokePawnImgVelocity;
    public static int maxFrame; //pawns total movemnts (if possible implemet or delete it)

    public PokePawn(Context context) {

//        System.out.println("creato il pokepawn");
        pokePawnImageX = (AppConstants.getBitmapBank().getBoardWidth() - AppConstants.getInstance(context).LEFT_GAME_MENU_WIDTH) / 10;
        pokePawnImageY = AppConstants.getBitmapBank().getBoardHeight() - 120;
        maxFrame = 3;
        pokePawnImgVelocity = 0;
    }

    //getter method for getting the x-coordinated
    public int getX() {
        return pokePawnImageX;
    }

    //getter method for getting the y-coordinated
    public int getY() {
        return pokePawnImageY;
    }

    //setter for the x-coordinates
    public void setX(int pokePawnImageX) {
        this.pokePawnImageX = pokePawnImageX;
    }

    //setter for the y-coordinates
    public void setY(int pokePawnImageY) {
        this.pokePawnImageY = pokePawnImageY;
    }

    //getter velocity
    public int getBgImageVelocity() {
        return pokePawnImgVelocity;
    }

    public void setPokePawnImgVelocity(int velocity) {
        this.pokePawnImgVelocity = velocity;
    }

}
