package it.walle.pokemongoosegame.graphics;


public class Background {
    //classe per dare effetti al background, come movimento o altro.

    int bgImageX = 0, bgImageY = 0, bgImgVelocity;

    public Background() {
//        System.out.println("Sono in background");
//
//        System.out.println("creato il background");
        bgImageX = 0;
        bgImageY = 0;
        bgImgVelocity = 3;

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
