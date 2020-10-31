package it.walle.pokemongoosegame.boardfactory;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.entity.Board;
import it.walle.pokemongoosegame.entity.cell.BlueCell;
import it.walle.pokemongoosegame.entity.cell.Cell;
import it.walle.pokemongoosegame.settings.BoardProcedurallyGeneratedSettingsDAO;
import it.walle.pokemongoosegame.settings.json.BoardProcedurallyGeneratedSettingsDAO_JSON;

public class BoardFactoryProcedurallyGenerated extends BoardFactory{

    private static final String LOG_TAG = BoardProcedurallyGeneratedSettings.class.getName();

    public BoardFactoryProcedurallyGenerated(Context context) {
        super(context);
    }

    @Override
    public void createBoard(CreateBoardBean bean) {
        try {
            this.createBoardProcedurallyGenerated((CreateBoardProcedurallyGeneratedBean) bean);
        } catch (ClassCastException e){
            Log.w(LOG_TAG, e.getMessage(), e);
        }
    }

    private void createBoardProcedurallyGenerated(CreateBoardProcedurallyGeneratedBean bean){
        Board board = new Board();
        List<BlueCell> blueCells = new ArrayList<>();
        List<String> pokemonTypes = new ArrayList<>();

        // TODO: Populates pokemonTypes with types queried from pokeapi.

        // Populates BlueCells with blue cells.
        for (int i = 0; i < bean.getBlueCellSettings().length; i ++){
            BlueCell blueCell;
            try {
                blueCell = (BlueCell) Class.forName(bean.getBlueCellSettings()[i].getBlueCellClassName()).newInstance();
                blueCell.setBoardIndex(bean.getBlueCellSettings()[i].getBoardIndex());
                blueCells.add(blueCell);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                // If an error occurs the blue cell is skipped
                Log.w(LOG_TAG, "Unable to instantiate Blue Cell with class name: " + bean.getBlueCellSettings()[i].getBlueCellClassName(), e);
            }

        }

        // Adds blue cells to the board
        for (BlueCell c : blueCells){

            // Checks if the blue cell has a legal index
            if (bean.getBoardSettings().getNumCells() - c.getBoardIndex() > 0 && bean.getBoardSettings().getNumCells() - c.getBoardIndex() < bean.getBoardSettings().getNumCells()){

                // If the index is not already in use, adds the blue cell to the board at the provided index
                if (board.getCells().get(c.getBoardIndex()) == null){
                    board.getCells().add(c.getBoardIndex(), c);
                } else {
                    Log.w(LOG_TAG, c.getClass().getSimpleName() + "wants index " + c.getBoardIndex() + ", already in use by " + board.getCells().get(c.getBoardIndex()).getClass().getSimpleName() + ", skipping" );
                }
            } else {
                Log.w(LOG_TAG, c.getClass().getSimpleName() + " has an illegal index, skipping");
            }
        }

        // Generates all remaining cells and adds them to the board
        for (int i = 0; i < bean.getBoardSettings().getNumCells(); i ++){

            // If the index is already in use by a blue cell it ignores it
            if (board.getCells().get(i) != null){
                continue;
            }
            Cell cell = new Cell();

            // Sets a random type
            cell.setType(pokemonTypes.get((int) Math.floor(Math.random() * pokemonTypes.size())));

            // If it's a yellow cell adds a random yellow effect to it
            for (int j = 0; j < bean.getBoardSettings().getYellowCellStartingIndexes().length; j ++){
                if ((i + (Math.abs(bean.getBoardSettings().getYellowCellStartingIndexes()[j] - bean.getBoardSettings().getYellowCellDelta()))) % bean.getBoardSettings().getYellowCellDelta() == 0){
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
