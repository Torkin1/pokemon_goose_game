package it.walle.pokemongoosegame.graphics;

public class FindOccupiedDisplayedCellBean {

    private String playerUsername;      // Player who occupies the requested cell
    private int col, row;                   // Coordinates of the cell
    private GraphicCell cell;            // The occupied displayed cell

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public GraphicCell getCell() {
        return cell;
    }

    public void setCell(GraphicCell cell) {
        this.cell = cell;
    }
}