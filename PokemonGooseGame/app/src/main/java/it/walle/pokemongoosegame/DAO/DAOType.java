package it.walle.pokemongoosegame.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import it.walle.pokemongoosegame.entity.Type;

@Dao
public interface DAOType {

    @Query("SELECT * FROM type")
    List<Type> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Type... type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Type> type);
}
