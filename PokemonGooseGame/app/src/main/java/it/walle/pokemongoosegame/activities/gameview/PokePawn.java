package it.walle.pokemongoosegame.activities.gameview;

import android.graphics.Bitmap;

public class  PokePawn {

    //declaring the new pawn res

    private int pokePawnImageX = 0, pokePawnImageY = 0, pokePawnImgVelocity;
    private Bitmap sprite;

    public Bitmap getSprite() {
        return sprite;
    }

    public void setSprite(Bitmap sprite) {
        this.sprite = sprite;
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
