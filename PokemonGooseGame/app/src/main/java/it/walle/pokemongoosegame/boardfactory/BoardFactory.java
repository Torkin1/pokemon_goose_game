package it.walle.pokemongoosegame.boardfactory;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;

public abstract class BoardFactory {

    protected final Context context;
    protected final CreateBoardBean bean;

    private static BoardFactoryType infereBoardFactoryType(CreateBoardBean bean) throws UnregisteredBoardFactoryTypeException {

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
        BoardFactoryType type = infereBoardFactoryType(createBoardBean);

        // return a reference to the desired BoardFactory
        return (BoardFactory) Class.forName(type.getBoardFactoryTypeName()).getConstructor(Context.class, type.getCreateBoardBeanType()).newInstance(context, createBoardBean);
    }



    public BoardFactory(Context context, CreateBoardBean bean){
        this.context = context;
        this.bean = bean;
    }
    public abstract void createBoard();
}
