package it.walle.pokemongoosegame.boardfactory.procedurallygenerated;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.boardfactory.BoardFactory;
import it.walle.pokemongoosegame.boardfactory.CreateBoardBean;
import it.walle.pokemongoosegame.boardfactory.IndexAlreadyInUseException;
import it.walle.pokemongoosegame.boardfactory.IndexUnavailableException;
import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.entity.board.cell.BlueCell;
import it.walle.pokemongoosegame.entity.board.cell.Cell;

public class BoardFactoryPG extends BoardFactory {

    private static final String LOG_TAG = BoardPGParams.class.getName();

    private BoardFactoryPG(Context context, CreateBoardBean bean) {
        super(context, bean);
    }

    @Override
    public void createBoard() {
        try {
            this.createBoardProcedurallyGenerated((CreateBoardPGBean) this.bean);
        } catch (ClassCastException e){
            Log.w(LOG_TAG, e.getMessage(), e);
        }
    }

    private void checkIndexAvailability(Board board, BlueCell candidate) throws IndexUnavailableException {
        try {
            // Checks if index is in bounds and if there is no BlueCells with already such index
            if (board.getCells().get(candidate.getBoardIndex()) != null) {
                throw new IndexAlreadyInUseException(board, candidate);
            }
        } catch (IndexOutOfBoundsException e){
            throw new IndexUnavailableException(e);
        }
    }

    private void createBoardProcedurallyGenerated(CreateBoardPGBean bean){
        Board board = new Board();
        List<BlueCell> blueCells = new ArrayList<>();
        List<String> pokemonTypes = new ArrayList<>();

        // TODO: Populates pokemonTypes with types queried from pokeapi.

        // Populates BlueCells with blue cells.
        for (int i = 0; i < bean.getBoardSettings().getBlueCellSettings().size(); i ++){
            BlueCell blueCell;
            try {
                blueCell = (BlueCell) Class.forName(bean.getBoardSettings().getBlueCellSettings().get(i).getBlueCellName()).newInstance();
                blueCell.setBoardIndex(bean.getBoardSettings().getBlueCellSettings().get(i).getBoardIndex());
                blueCells.add(blueCell);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                // If an error occurs the blue cell is skipped
                Log.w(LOG_TAG, "Unable to instantiate Blue Cell with class name: " + bean.getBoardSettings().getBlueCellSettings().get(i).getBlueCellName(), e);
            }

        }

        // Adds blue cells to the board
        for (BlueCell c : blueCells){

            try{
                // If the index is available, adds the blue cell to the board at such index
                checkIndexAvailability(board, c);
                board.getCells().add(c.getBoardIndex(), c);
            } catch (IndexUnavailableException e){
                Log.w(LOG_TAG , e.getMessage(), e);
            }
        }

        // Generates all remaining cells and adds them to the board
        for (int i = 0; i < bean.getBoardSettings().getBoardPGParams().getNumCells(); i ++){

            // If the index is already in use by a blue cell it ignores it
            if (board.getCells().get(i) != null){
                continue;
            }
            Cell cell = new Cell();

            // Sets a random type
            cell.setType(pokemonTypes.get((int) Math.floor(Math.random() * pokemonTypes.size())));

            // If it's a yellow cell adds a random yellow effect to it
            for (int j = 0; j < bean.getBoardSettings().getYellowCellStartingIndexes().size(); j ++){
                if ((i + (Math.abs(
                        bean
                                .getBoardSettings()
                                .getYellowCellStartingIndexes()
                                .get(i)
                                .getIndex() - bean.getBoardSettings().getBoardPGParams().getYellowCellDelta())
                )) % bean
                        .getBoardSettings()
                        .getBoardPGParams()
                        .getYellowCellDelta() == 0) {
                    // TODO: adds yellow effect. Names of YellowEffects to istantiate are read from local DB.
                }
            }

            // Adds the cell to the board
            board.getCells().add(cell);
        }

        // Stores reference to board in bean
        bean.setBoard(board);
    }
}
