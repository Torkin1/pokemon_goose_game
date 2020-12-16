package it.walle.pokemongoosegame.boardfactory.procedurallygenerated;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import it.walle.pokemongoosegame.boardfactory.BoardFactory;
import it.walle.pokemongoosegame.boardfactory.CreateBoardBean;

public abstract class CreateBoardPGHandlerCallback implements Handler.Callback {
    @Override
    public final boolean handleMessage(@NonNull Message msg) {
        switch (msg.what){
            case BoardFactory.CREATE_BOARD_OK:
                this.handleBoard((CreateBoardBean) msg.obj);
                break;
            case BoardFactory.CREATE_BOARD_FAILURE:
                this.handleException((Exception) msg.obj);
                break;
        }
        return true;
    }


    public abstract void handleBoard(CreateBoardBean bean);     // What to do with the bean populated with the board
    public abstract void handleException(Exception e);          // Recovers from the generated exception
}
