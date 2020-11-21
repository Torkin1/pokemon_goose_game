package it.walle.pokemongoosegame.database.boardpgdao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import it.walle.pokemongoosegame.entity.board.pgsettings.BoardPGParams;
import it.walle.pokemongoosegame.entity.board.pgsettings.BoardPGSettings;

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
