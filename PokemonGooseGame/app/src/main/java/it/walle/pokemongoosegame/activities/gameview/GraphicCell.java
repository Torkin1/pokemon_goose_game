package it.walle.pokemongoosegame.activities.gameview;


public class GraphicCell {

    //set the position to the start, in case something fails
    private int cellImgX = 0, cellImgY = 0;
    private int cellIndex;

    //setters and getters

    public int getCellImgX(){
        return cellImgX;
    }


    public int getCellImgY(){
        return cellImgY;
    }

    public void setCellImgX(int x){
        this.cellImgX = x;
    }


    public void setCellImgY(int y){
        this.cellImgY = y;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }
}
