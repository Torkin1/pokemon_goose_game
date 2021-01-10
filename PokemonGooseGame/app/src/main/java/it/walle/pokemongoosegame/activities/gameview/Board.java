package it.walle.pokemongoosegame.activities.gameview;

public class Board {
    //declaring the new board res

    int boardImageX = 0, boardImageY = 0;

    //delcraing the initial position, in case
    public Board() {
        boardImageX = 70;
        boardImageY = 70;
    }

    public int getBoardImageX(){
        return boardImageX;
    }

    public int getBoardImageY(){
        return boardImageY;
    }

    public void setBoardImageX(int x){
        this.boardImageX = x;
    }

    public void setBoardImageY(int y){
        this.boardImageY = y;
    }

}
