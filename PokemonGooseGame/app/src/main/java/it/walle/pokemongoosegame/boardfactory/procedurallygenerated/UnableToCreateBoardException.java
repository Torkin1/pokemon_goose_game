package it.walle.pokemongoosegame.boardfactory.procedurallygenerated;

import androidx.annotation.Nullable;

public class UnableToCreateBoardException extends Exception {

    public UnableToCreateBoardException() {
    }

    public UnableToCreateBoardException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public UnableToCreateBoardException(Exception e) {
        super(e);
    }
}
