package it.walle.pokemongoosegame.boardfactory.procedurallygenerated;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.boardfactory.BoardFactory;
import it.walle.pokemongoosegame.boardfactory.IndexAlreadyInUseException;
import it.walle.pokemongoosegame.boardfactory.IndexUnavailableException;
import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.entity.board.cell.BlueCell;
import it.walle.pokemongoosegame.entity.board.cell.Cell;
import it.walle.pokemongoosegame.entity.board.pgsettings.BoardPGParams;
import it.walle.pokemongoosegame.entity.effect.YellowEffect;

public class BoardFactoryPG extends BoardFactory {

    private static final String LOG_TAG = BoardPGParams.class.getName();

    private BoardFactoryPG(Context context, CreateBoardPGBean bean) {
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
        if (bean.getBoardSettings().getBlueCellSettings() != null){
            Log.i(LOG_TAG, "burp");
            for (int i = 0; i < bean.getBoardSettings().getBlueCellSettings().size(); i ++){
                BlueCell blueCell;
                try {
                    blueCell = (BlueCell) Class.forName(bean.getBoardSettings().getBlueCellSettings().get(i).getBlueCellName()).newInstance();
                    blueCell.setBoardIndex(bean.getBoardSettings().getBlueCellSettings().get(i).getBoardIndex());
                    blueCells.add(blueCell);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    // If an error occurs the blue cell is skipped
                    Log.e(LOG_TAG, "Unable to instantiate Blue Cell with class name: " + bean.getBoardSettings().getBlueCellSettings().get(i).getBlueCellName() + " at position " + i, e);
                }
            }
        }

        // Adds blue cells to the board
        for (BlueCell c : blueCells){
            try{
                // If the index is available, adds the blue cell to the board at such index
                checkIndexAvailability(board, c);
                board.getCells().add(c.getBoardIndex(), c);
            } catch (IndexUnavailableException e){
                Log.e(LOG_TAG , e.getMessage(), e);
            }
        }

        // Generates all remaining cells and adds them to the board
        int numOfCells = bean.getBoardSettings().getBoardPGParams().getNumCells();
        for (int i = 0; i < numOfCells; i ++){

            // If the index is already in use by a blue cell it ignores it
            if (i < board.getCells().size() && board.getCells().get(i) != null){
                continue;
            }

            Cell cell = new Cell();

            // If list of types is available, sets a random type
            if (!pokemonTypes.isEmpty()){
                cell.setType(pokemonTypes.get((int) Math.floor(Math.random() * pokemonTypes.size())));
            }

            // Adds a random yellow effect if the cell is to be inserted at an any yellow cell position, unless it is the first or the last position on the board
            if (bean.getBoardSettings().getYellowCellStartingIndexes() != null){
                int currentStartingIndex;
                int yellowCellDelta = bean.getBoardSettings().getBoardPGParams().getYellowCellDelta();
                for (int j = 0; j < bean.getBoardSettings().getYellowCellStartingIndexes().size(); j ++){
                    currentStartingIndex = bean.getBoardSettings().getYellowCellStartingIndexes().get(j).getIndex();
                    if (i != 0 && i != numOfCells - 1 && (i + (Math.abs(currentStartingIndex - yellowCellDelta))) % yellowCellDelta == 0) {

                        // If there are available yellow effects, picks a random available yellow effect class name
                        String randomYellowEffectClassName = bean
                                .getBoardSettings()
                                .getYellowEffectNames()
                                .get((int) Math.floor(Math.random() * bean.getBoardSettings().getYellowEffectNames().size()))
                                .getYellowEffectClassName();

                        // Instantiate the YellowEffect class with randomYellowEffectClassName name, and binds it to the board cell
                        try {
                            cell.setEntryEffect((YellowEffect) Class.forName(randomYellowEffectClassName).newInstance());
                        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                            Log.e(LOG_TAG, "Can't instantiate effect with class name " + randomYellowEffectClassName + " at position " + i, e);
                        }
                    }
                }
            }

            // Adds the cell to the board
            board.getCells().add(cell);
        }

        // Stores reference to board in bean
        bean.setBoard(board);
    }
}
