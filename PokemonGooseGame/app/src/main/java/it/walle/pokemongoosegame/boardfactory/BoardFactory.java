package it.walle.pokemongoosegame.boardfactory;

public abstract class BoardFactory {

    // Base name for all instances of BoardFactory
    protected static final String baseName = BoardFactory.class.getSimpleName();

    public static BoardFactory getInstance(BoardType type) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        // return a reference to the desired BoardFactory
        return (BoardFactory) Class.forName(baseName + type.toString()).newInstance();
    }
    public abstract void createBoard(CreateBoardBean bean);
}
