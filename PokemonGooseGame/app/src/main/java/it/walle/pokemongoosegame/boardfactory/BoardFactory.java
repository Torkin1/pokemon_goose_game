package it.walle.pokemongoosegame.boardfactory;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;

public abstract class BoardFactory {

    // Base name for all instances of BoardFactory
    protected static final String baseName = BoardFactory.class.getSimpleName();
    protected final Context context;
    public static BoardFactory getInstance(Context context, BoardType type) throws ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {

        // return a reference to the desired BoardFactory
        return (BoardFactory) Class.forName(baseName + type.toString()).getConstructor(Context.class).newInstance(context);
    }

    public BoardFactory(Context context){
        this.context = context;
    }
    public abstract void createBoard(CreateBoardBean bean);
}
