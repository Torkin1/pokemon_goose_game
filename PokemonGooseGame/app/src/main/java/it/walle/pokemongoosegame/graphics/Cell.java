package it.walle.pokemongoosegame.graphics;

public class Cell {

    int cellImgX = 0, cellImgY = 0;

    public void Cell() {
        cellImgX = AppConstants.CELL_MARGIN + AppConstants.LEFT_GAME_MENU_WIDTH;
        cellImgY = AppConstants.SCREEN_HEIGHT - AppConstants.LEFT_GAME_MENU_WIDTH - AppConstants.CELL_MARGIN;
    }

    //setters and getters

    public int getCellImgX(){
        return cellImgX;
    }


    public int getCellImgY(){
        return cellImgX;
    }

    public void setCellImgX(int x){
        this.cellImgX = x;
    }


    public void setCellImgY(int y){
        this.cellImgY = y;
    }
}
