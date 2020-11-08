package it.walle.pokemongoosegame.boardfactory;

public class UnregisteredBoardFactoryTypeException extends ClassNotFoundException {
    public UnregisteredBoardFactoryTypeException(CreateBoardBean bean) {
        super(bean.getClass().getSimpleName() + " has no BoardFactory associated in any of BoardFactoryType instances");
    }
}
