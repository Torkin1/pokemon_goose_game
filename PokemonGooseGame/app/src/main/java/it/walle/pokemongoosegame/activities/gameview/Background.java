package it.walle.pokemongoosegame.activities.gameview;


public class Background {
    //Class that make help use teh iamge as a different object, can after be resized, or adding movement or other

    //the background starts from the start of the Screen
    int bgImageX = 0, bgImageY = 0, bgImgVelocity;

    public Background() {
        bgImageX = 0;
        bgImageY = 0;
        bgImgVelocity = 3;//Velocity, the variable of the movement speed
    }

    //getter method for getting the x-coordinated
    public int getX(){
        return bgImageX;
    }
    //getter method for getting the y-coordinated
    public int getY(){
        return bgImageY;
    }

    //setter for the x-coordinates
    public void setX(int bgImageX){
        this.bgImageX = bgImageX;
    }

    //setter for the y-coordinates
    public void setY(int bgImageY){
        this.bgImageY = bgImageY;
    }

    //getter velocity
    public int getBgImageVelocity(){
        return bgImgVelocity;
    }
}
