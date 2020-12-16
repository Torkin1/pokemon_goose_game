package it.walle.pokemongoosegame.boardfactory.procedurallygenerated;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import it.walle.pokemongoosegame.boardfactory.BoardFactory;
import it.walle.pokemongoosegame.boardfactory.IndexAlreadyInUseException;
import it.walle.pokemongoosegame.boardfactory.IndexUnavailableException;
import it.walle.pokemongoosegame.database.pokeapi.DAOType;
import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.entity.board.cell.BlueCell;
import it.walle.pokemongoosegame.entity.board.cell.Cell;
import it.walle.pokemongoosegame.entity.board.cell.YellowCell;
import it.walle.pokemongoosegame.entity.board.pgsettings.BoardPGParams;
import it.walle.pokemongoosegame.entity.board.pgsettings.WhatYellowEffectName;
import it.walle.pokemongoosegame.entity.effect.YellowEffect;
import it.walle.pokemongoosegame.entity.pokeapi.EntityPack;
import it.walle.pokemongoosegame.entity.pokeapi.EntityPointer;

public class BoardFactoryPG extends BoardFactory {

    private static final String TAG = BoardPGParams.class.getName();

    private BoardFactoryPG(Context context, CreateBoardPGBean bean) {
        super(context, bean);
    }

    @Override
    public void createBoard() throws UnableToCreateBoardException {

                try {
                    BoardFactoryPG.this.createBoardProcedurallyGenerated((CreateBoardPGBean) BoardFactoryPG.this.bean);
                } catch (ClassCastException | UnableToSetTypesException e){
                    throw new UnableToCreateBoardException(e);
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

    private void createBoardProcedurallyGenerated(CreateBoardPGBean bean) throws UnableToSetTypesException {



        Board board = new Board();
        List<BlueCell> blueCells = new ArrayList<>();
        List<String> pokemonTypes = new ArrayList<>();
        Semaphore pokemonTypesSemaphore = new Semaphore(0);

        // Populates pokemonTypes with types queried from pokeapi.
        DAOType.getReference(context).loadAllTypePointers(
                new Response.Listener<EntityPack>() {
                    @Override
                    public void onResponse(EntityPack response) {

                        Log.d(TAG, "correctly received type pointers");

                        // Adds all type names to pokemonTypes
                        for (EntityPointer ep : response.getResults()){
                            pokemonTypes.add(ep.getName());
                        }

                        // adds a token to the semaphore
                        pokemonTypesSemaphore.release();
                        Log.d(TAG, "num of semaphore permits: " + pokemonTypesSemaphore.availablePermits());
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, error.getMessage(), error);

                        // adds a token to the semaphore
                        pokemonTypesSemaphore.release();
                    }
                });

        // Populates BlueCells with blue cells.
        if (bean.getBoardSettings().getBlueCellSettings() != null){
            for (int i = 0; i < bean.getBoardSettings().getBlueCellSettings().size(); i ++){
                BlueCell blueCell;
                try {
                    blueCell = (BlueCell) Class.forName(bean.getBoardSettings().getBlueCellSettings().get(i).getBlueCellName()).newInstance();
                    blueCell.setBoardIndex(bean.getBoardSettings().getBlueCellSettings().get(i).getBoardIndex());
                    blueCells.add(blueCell);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    // If an error occurs the blue cell is skipped
                    Log.e(TAG, "Unable to instantiate Blue Cell with class name: " + bean.getBoardSettings().getBlueCellSettings().get(i).getBlueCellName() + " at position " + i, e);
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
                Log.e(TAG, e.getMessage(), e);
            }
        }

        // Generates all remaining cells and adds them to the board
        int numOfCells = bean.getBoardSettings().getBoardPGParams().getNumCells();
        List<WhatYellowEffectName> availableYellowEffectNames = bean.getBoardSettings().getYellowEffectNames();
        for (int i = 0; i < numOfCells; i ++){

            // If the index is already in use by a blue cell it ignores it
            if (i < board.getCells().size() && board.getCells().get(i) != null){
                continue;
            }

            Cell cell = null;

            // Adds a random yellow effect if the cell is to be inserted at an any yellow cell position, unless it is the first or the last position on the board
            if (bean.getBoardSettings().getYellowCellStartingIndexes() != null){
                int currentStartingIndex;
                int yellowCellDelta = bean.getBoardSettings().getBoardPGParams().getYellowCellDelta();
                for (int j = 0; j < bean.getBoardSettings().getYellowCellStartingIndexes().size(); j ++){
                    currentStartingIndex = bean.getBoardSettings().getYellowCellStartingIndexes().get(j).getIndex();
                    if (i != 0 && i != numOfCells - 1 && (i + (Math.abs(currentStartingIndex - yellowCellDelta))) % yellowCellDelta == 0) {

                        // If there are available yellow effects, picks a random available yellow effect class name
                        int yellowEffectRandomIndex;
                        boolean chosen = false;
                        String randomYellowEffectClassName = "";
                        while (!bean.getBoardSettings().getYellowEffectNames().isEmpty() && !chosen) {

                            // gets a random yellow effect name from the available ones
                            yellowEffectRandomIndex  = (int) Math.floor(Math.random() * availableYellowEffectNames.size());
                            randomYellowEffectClassName = availableYellowEffectNames
                                    .get(yellowEffectRandomIndex)
                                    .getYellowEffectClassName();
                            try {

                                // Instantiate the YellowEffect class with randomYellowEffectClassName name, and binds it to the board cell
                                YellowEffect yellowEffect = (YellowEffect) Class.forName(randomYellowEffectClassName).newInstance();
                                cell = new YellowCell();
                                cell.setEntryEffect(yellowEffect);
                                chosen = true;
                            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {

                                // If the yellow effect name is invalid it pops it, then tries the next one until a valid one is available or all name are popped
                                Log.e(TAG, "Can't instantiate effect with class name " + randomYellowEffectClassName + " at position " + i, e);
                                availableYellowEffectNames.remove(yellowEffectRandomIndex);

                            }
                        }
                    }
                }

                // If cell is still null creates a normal cell
                if (cell == null){
                    cell = new Cell();
                }
            }

            // Adds the cell to the board
            board.getCells().add(cell);
        }

        // TODO: Adds winning entry effect to the last cell on the board

        // If list of types is available, sets a random type to every cell
        try {

            // Waits for pokeapi to return pokemon types
            pokemonTypesSemaphore.acquire();

            // If pokemonTypes is still empty, a problem occurred while fetching types from db
            if (pokemonTypes.isEmpty()){
                throw new NoTypesAvailableException();
            }

            // Adds a random type to every cell
            if (!pokemonTypes.isEmpty()){
                for (int i = 1; i < board.getCells().size() - 1; i ++){
                    board
                            .getCells()
                            .get(i)
                            .setType(
                                    pokemonTypes
                                            .get(
                                                    (int) Math.floor(
                                                            Math.random() * pokemonTypes.size()
                                                    )
                                            )
                            );
                }

            }

        } catch (InterruptedException | NoTypesAvailableException e) {

            // Types are needed by the game to work properly, we must inform the caller that something wrong occurred
            throw new UnableToSetTypesException(e);
        }

        // Stores reference to board in bean
        bean.setBoard(board);


    }
}
