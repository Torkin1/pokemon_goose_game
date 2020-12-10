package it.walle.pokemongoosegame.graphics;

public class Board {
    //declaring the new pawn res

    int boardImageX = 0, boardImageY = 0;

    public Board() {

//        System.out.println("creato il pokepawn");
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
