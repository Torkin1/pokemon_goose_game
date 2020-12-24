package it.walle.pokemongoosegame.boardfactory;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import it.walle.pokemongoosegame.boardfactory.procedurallygenerated.UnableToCreateBoardException;
import it.walle.pokemongoosegame.entity.board.Board;

public abstract class BoardFactory {

    public static final int CREATE_BOARD_OK = 0;
    public static final int CREATE_BOARD_FAILURE = - 1;

    protected final Context context;
    protected final CreateBoardBean bean;
    private static final String LOG_TAG = BoardFactory.class.getName();

    private static BoardFactoryType inferBoardFactoryType(CreateBoardBean bean) throws UnregisteredBoardFactoryTypeException {

        // Searches in BoardFactoryTypes for a match
        for (BoardFactoryType t : BoardFactoryType.values()){
            if (t.getCreateBoardBeanTypeName().equals(bean.getClass().getName())){
                return t;
            }
        }
        throw new UnregisteredBoardFactoryTypeException(bean);
    }


    public static BoardFactory getInstance(Context context, CreateBoardBean createBoardBean) throws ClassNotFoundException {

        // Infers the desired BoardFactory type by looking at createBoardBean type
        BoardFactoryType type = null;
        try {
            type = inferBoardFactoryType(createBoardBean);

            // makes temporarily accessible the  private constructor of actual board factory and calls it
            Constructor<? extends BoardFactory> boardFactoryConstructor = (Constructor<? extends BoardFactory>) Class.forName(type.getBoardFactoryTypeName()).getDeclaredConstructor(Context.class, type.getCreateBoardBeanType());
            boardFactoryConstructor.setAccessible(true);
            BoardFactory boardFactory = boardFactoryConstructor.newInstance(context, createBoardBean);
            boardFactoryConstructor.setAccessible(false);
            return boardFactory;
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return null;
    }



    public BoardFactory(Context context, CreateBoardBean bean){
        this.context = context;
        this.bean = bean;
    }
    public abstract void createBoard() throws UnableToCreateBoardException;
}
