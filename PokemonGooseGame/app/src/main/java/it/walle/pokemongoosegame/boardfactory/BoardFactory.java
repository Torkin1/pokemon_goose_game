package it.walle.pokemongoosegame.boardfactory;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;

public abstract class BoardFactory {

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
            return (BoardFactory) Class.forName(type.getBoardFactoryTypeName()).getDeclaredConstructor(Context.class, type.getCreateBoardBeanType()).newInstance(context, createBoardBean);
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return null;
    }



    public BoardFactory(Context context, CreateBoardBean bean){
        this.context = context;
        this.bean = bean;
    }
    public abstract void createBoard();
}
