package it.walle.pokemongoosegame.game;

import it.walle.pokemongoosegame.boardfactory.BoardSettings;
import it.walle.pokemongoosegame.boardfactory.BoardFactoryType;
import it.walle.pokemongoosegame.boardfactory.CreateBoardBean;

public class AddNewBoardBean {
    private CreateBoardBean createBoardBean;    // Params used by BoardFactory

    public CreateBoardBean getCreateBoardBean() {
        return createBoardBean;
    }

    public void setCreateBoardBean(CreateBoardBean createBoardBean) {
        this.createBoardBean = createBoardBean;
    }
}
