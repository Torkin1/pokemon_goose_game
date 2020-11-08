package it.walle.pokemongoosegame.boardfactory;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;

public abstract class BoardFactory {

    protected final Context context;

    private static BoardFactoryType inferBoardFactoryType(CreateBoardBean bean) throws UnregisteredBoardFactoryTypeException {

        // Searches in BoardFactoryTypes for a match
        for (BoardFactoryType t : BoardFactoryType.values()){
            if (t.getCreateBoardBeanTypeName().equals(bean.getClass().getName())){
                return t;
            }
        }
        throw new UnregisteredBoardFactoryTypeException(bean);
    }


    public static BoardFactory getInstance(Context context, CreateBoardBean createBoardBean) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException {

        // Infers the desired BoardFactory type by looking at createBoardBean type
        BoardFactoryType type = inferBoardFactoryType(createBoardBean);

        // return a reference to the desired BoardFactory
        return (BoardFactory) Class.forName(type.toString()).getConstructor(Context.class).newInstance(context);
    }



    public BoardFactory(Context context){
        this.context = context;
    }
    public abstract void createBoard(CreateBoardBean bean);
}
