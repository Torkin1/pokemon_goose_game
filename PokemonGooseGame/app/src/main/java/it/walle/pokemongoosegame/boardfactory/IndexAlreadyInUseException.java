package it.walle.pokemongoosegame.boardfactory;


import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.entity.board.cell.BlueCell;

public class IndexAlreadyInUseException extends IndexUnavailableException {
    public IndexAlreadyInUseException(Board board, BlueCell candidate) {
        super(candidate.getClass().getSimpleName() + "wants index " + candidate.getBoardIndex() + ", already in use by " + board.getCells().get(candidate.getBoardIndex()).getClass().getSimpleName() + ", skipping");
    }
}
