package it.walle.pokemongoosegame.boardfactory;

import androidx.annotation.NonNull;

import it.walle.pokemongoosegame.boardfactory.procedurallygenerated.BoardFactoryProcedurallyGenerated;
import it.walle.pokemongoosegame.boardfactory.procedurallygenerated.CreateBoardProcedurallyGeneratedBean;

public enum BoardFactoryType {

    // Registers all associations between board types and board factories
    PROCEDURALLY_GENERATED (BoardFactoryProcedurallyGenerated.class, CreateBoardProcedurallyGeneratedBean.class)
    ;

    private final Class<? extends CreateBoardBean> createBoardBeanType;
    private final Class<? extends BoardFactory> boardFactoryType;

    private BoardFactoryType(Class<? extends BoardFactory> boardFactoryType, Class<? extends CreateBoardBean> createBoardBeanType){
        this.boardFactoryType = boardFactoryType;
        this.createBoardBeanType = createBoardBeanType;
    }

    public String getCreateBoardBeanTypeName(){
        return this.createBoardBeanType.getName();
    }

    public String getBoardFactoryTypeName(){
        return this.boardFactoryType.getName();
    }

    @NonNull
    @Override
    public String toString() {
        return this.getBoardFactoryTypeName();
    }
}
