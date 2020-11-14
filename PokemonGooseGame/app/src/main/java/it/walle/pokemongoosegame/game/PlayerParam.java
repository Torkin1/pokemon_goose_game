package it.walle.pokemongoosegame.game;

import androidx.annotation.NonNull;

public enum PlayerParam implements EntityParam{

    /*
    TODO: (PLEASE NOTE CAREFULLY!)
        The Strings passed to the constructor must be the names of the corresponding Player field.
        If the Player fields name change, this enum must be updated as well!
        This is not ideal because if someone forgets to update this enum, errors would be visible only at runtime,
        so this has to be changed as soon as someone comes out with a better way.
    */
    MONEY ("money", PlayerParamGroup.PLAYER),
    IDLE_TURNS ("idleTurns", PlayerParamGroup.PLAYER),
    HP_CURRENT("currentHp", PlayerParamGroup.POKEMON)
    ;

    private final String paramName;
    private final PlayerParamGroup group;

    private PlayerParam(String paramName, PlayerParamGroup group){
        this.paramName = paramName;
        this.group = group;
    }

    public PlayerParamGroup getGroup() {
        return group;
    }

    @NonNull
    @Override
    public String toString() {
        return this.paramName;
    }
}
