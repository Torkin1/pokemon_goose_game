package it.walle.pokemongoosegame.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import it.walle.pokemongoosegame.DAO.Converters;
import it.walle.pokemongoosegame.DAO.DAOType;
import it.walle.pokemongoosegame.entity.Type;

@Database(entities= {Type.class}, version= 1)
@TypeConverters({Converters.class})
public abstract class DatabaseType extends RoomDatabase {
    public abstract DAOType DAOType();
}
