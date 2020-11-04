package it.walle.pokemongoosegame.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import it.walle.pokemongoosegame.DAO.Converters;
import it.walle.pokemongoosegame.DAO.DAOPokemon;
import it.walle.pokemongoosegame.entity.Pokemon;


@Database(entities= {Pokemon.class}, version= 1)
@TypeConverters({Converters.class})
public abstract class DatabasePokemon extends RoomDatabase {
    public abstract DAOPokemon DAOPokemon();
}
