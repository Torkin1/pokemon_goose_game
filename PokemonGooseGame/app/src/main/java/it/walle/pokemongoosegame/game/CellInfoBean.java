package it.walle.pokemongoosegame.game;

import it.walle.pokemongoosegame.entity.board.cell.Cell;

public class CellInfoBean {
    private int requestedIndex;     // The index of the requested cell
    private Cell cell;              // The requested cell

    public int getRequestedIndex() {
        return requestedIndex;
    }

    public void setRequestedIndex(int requestedIndex) {
        this.requestedIndex = requestedIndex;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public String getType() {
        return cell.getType();
    }

    public void setType(String type) {
        cell.setType(type);
    }
}
