package it.walle.pokemongoosegame.game;

public class ChangePlayerValueBean extends ChangeEntityValueBean {

    private String username;        // The username of the target player
    private PlayerParam param;      // What param has to be changed

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PlayerParam getParam() {
        return param;
    }

    public void setParam(PlayerParam param) {
        this.param = param;
    }
}
