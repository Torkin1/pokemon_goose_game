package it.walle.pokemongoosegame.boardfactory;

import androidx.annotation.NonNull;

import it.walle.pokemongoosegame.boardfactory.procedurallygenerated.BoardFactoryPG;
import it.walle.pokemongoosegame.boardfactory.procedurallygenerated.CreateBoardPGBean;

public enum BoardFactoryType {

    // Registers all associations between board types and board factories
    PROCEDURALLY_GENERATED (BoardFactoryPG.class, CreateBoardPGBean.class, "PG");

    private final Class<? extends CreateBoardBean> createBoardBeanType;
    private final Class<? extends BoardFactory> boardFactoryType;
    private final String root;

    private BoardFactoryType(Class<? extends BoardFactory> boardFactoryType, Class<? extends CreateBoardBean> createBoardBeanType, String root){
        this.boardFactoryType = boardFactoryType;
        this.createBoardBeanType = createBoardBeanType;
        this.root = root;
    }

    public String getCreateBoardBeanTypeName(){
        return this.createBoardBeanType.getName();
    }

    public String getBoardFactoryTypeName(){
        return this.boardFactoryType.getName();
    }

    public Class<? extends CreateBoardBean> getCreateBoardBeanType() {
        return createBoardBeanType;
    }

    public Class<? extends BoardFactory> getBoardFactoryType() {
        return boardFactoryType;
    }

    public String getRoot() {
        return root;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getRoot();
    }
}
