package it.walle.pokemongoosegame.activities.gameview;


public class GraphicCell {

    private int cellImgX = 0, cellImgY = 0;
    private int cellIndex;
/*
    public Cell(Context context) {
        cellImgX = AppConstants.getInstance(context).CELL_MARGIN + AppConstants.getInstance(context).LEFT_GAME_MENU_WIDTH;
        cellImgY = AppConstants.getInstance(context).SCREEN_HEIGHT - AppConstants.getInstance(context).LEFT_GAME_MENU_WIDTH - AppConstants.getInstance(context).CELL_MARGIN;

    }

 */

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
