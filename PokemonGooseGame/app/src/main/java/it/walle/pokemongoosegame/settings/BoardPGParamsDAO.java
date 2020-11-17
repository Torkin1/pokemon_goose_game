package it.walle.pokemongoosegame.settings;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import it.walle.pokemongoosegame.boardfactory.procedurallygenerated.BoardPGParams;

@Dao
public interface BoardPGParamsDAO {
    @Insert
    public void storeBoardPGParams(BoardPGParams... boardPGParams);

    @Query("SELECT * FROM BoardPGParams where name = :name")
    public LiveData<BoardPGParams> getBoardPGParamByName(String name);

    @Transaction
    @Query("SELECT * FROM BoardPGParams where name = :name")
    public LiveData<BoardPGSettings> getBoardPGSettingsByName(String name);             // Use this method to load all settings needed by BoardFactoryPG
}
