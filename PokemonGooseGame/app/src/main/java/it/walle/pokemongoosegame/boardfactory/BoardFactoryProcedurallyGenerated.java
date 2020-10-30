package it.walle.pokemongoosegame.boardfactory;

import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.entity.Board;
import it.walle.pokemongoosegame.entity.cell.BlueCell;
import it.walle.pokemongoosegame.entity.cell.Cell;

public class BoardFactoryProcedurallyGenerated extends BoardFactory{

    @Override
    public void createBoard(CreateBoardBean bean) {
        this.createBoard(bean);
    }

    private void createBoard(CreateBoardProcedurallyGeneratedBean bean){
        Board board = new Board();
        List<BlueCell> blueCells = new ArrayList<>();
        List<String> pokemonTypes = new ArrayList<>();

        // TODO: Populates pokemonTypes with types queried from pokeapi.

        // TODO: Populates BlueCells with blue cells. Name of BlueCells to instantiate are read from an asset file.

        // Adds blue cells to the board
        for (BlueCell c : blueCells){
            board.getCells().add(c.getBoardIndex(), c);
        }

        // generates all remaining cells and adds them to the board
        for (int i = 0; i < bean.getNumCells(); i ++){

            // If it's a blue cell it ignores it
            for (int j = 0; j < blueCells.size(); j ++){
                if (i == blueCells.get(j).getBoardIndex()){
                    i ++;
                    j = 0;
                }
            }
            Cell cell = new Cell();

            // Sets type of cell randomically
            cell.setType(pokemonTypes.get((int) Math.floor(Math.random() * pokemonTypes.size())));

            // If it's a yellow cell adds a random yellow effect to it
            for (int j = 0; j < bean.getYellowCellStartingIndexes().length; j ++){
                if ((i + (Math.abs(bean.getYellowCellStartingIndexes()[j] - bean.getYellowCellDelta()))) % bean.getYellowCellDelta() == 0){
                    // TODO: adds yellow effect. Names of YellowEffects to istantiate are read from an asset file.
                }
            }

            // Adds the cell to the board
            board.getCells().add(cell);
        }

        // Stores reference to board in bean
        bean.setBoard(board);
    }
}
