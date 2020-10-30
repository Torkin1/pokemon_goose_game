package it.walle.pokemongoosegame.entity;

import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.entity.cell.Cell;

public class Board {

    // models board
    private final List<Cell> cells = new ArrayList<>();

    public List<Cell> getCells() {
        return cells;
    }
}

