package it.walle.pokemongoosegame.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import it.walle.pokemongoosegame.entity.Pokemon;

@Dao
public interface DAOPokemon {

    @Query("SELECT * FROM pokemon")
    List<Pokemon> getAll();

    @Query("SELECT name FROM pokemon")
    List<String> getAllName();

    @Query("SELECT count(*) FROM pokemon")
    int size();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Pokemon... pokemon);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Pokemon> pokemon);

}
