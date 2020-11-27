package it.walle.pokemongoosegame.game;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.entity.Game;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.utils.MethodGetter;
import it.walle.pokemongoosegame.utils.NoSuchSetterException;

public class CoreController {
    // The game main engine.

    private final static String TAG = CoreController.class.getName();

    private static CoreController ref = null;
    public static CoreController getReference(){
        if (ref == null){
            ref = new CoreController();
        }
        return ref;
    }

    private Game game;   // Contains all infos about the game

    private Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void nextTurn(NextTurnBean bean){

        // Initialize structures to count how may turns a player spent idle
        for (Player p : this.game.getGamers()){
           bean.getWhoWaited().put(p.getUsername(), 0);
       }

        // Saves current player index for later use
        int old = this.game.getCurrentPlayerIndex();

        // Updates next player index to the following player in the list with no idle turns, writing how many turns a player spent idle in the bean
        Player p;
        int i;
        for (i = this.game.getNextPlayerIndex(); (p = this.game.getGamers().get(i)).getNumOfIdleTurns() != 0; i = (i + 1) % this.game.getGamers().size()) {
            p.setIdleTurns(p.getNumOfIdleTurns() - 1);
            bean.getWhoWaited().replace(p.getUsername(), bean.getWhoWaited().get(p.getUsername()) + 1);
        }

        // Changes the current player to the next player and updates next player index
        this.game.setCurrentPlayerIndex(i);
        this.game.setNextPlayerIndex((old + 1) % this.game.getGamers().size());
    }

    public void setNextPlayer(String username){
        this
                .game
                .setNextPlayerIndex(this
                        .game
                        .getGamers()
                        .indexOf(this
                                .game
                                .getPlayerByUsername(username)));
    }

    public void chooseWinner(WinnerBean bean){
        // TODO: Removes the player from the game and returns their score
    }

    public List<WinnerBean> endGame(){
        List<WinnerBean> winnerBeans = new ArrayList<>();
        // TODO: Ends current game and returns the winners with their scores, with the highest score in first position.

        // Ends game and returns winner
        this.abortGame();
        return winnerBeans;
    }

    public void abortGame(){
        // TODO: Ends the game without declaring a winner
    }

    public void throwDices(ThrowDicesBean bean){
        ArrayList<Integer> exitNumbers = new ArrayList<>();
        for(int i = 0; i < bean.getNumOfDices(); i++){
            exitNumbers.add(i, (int) (Math.random() * bean.getNumOfFaces() + 1));
        }
        bean.setExitNumbers(exitNumbers);
    }

    public void getPlayerInfo(PlayerInfoBean bean){
        // TODO: populates the bean with infos of the player specified by the provided username, then returns it
    }

    public void getCellInfo(CellInfoBean bean){
        // TODO: populates the bean with infos of the cell specified by the provided board index, then returns it
    }

    public String[] getAllPlayersInACellUsernames(int index){
        // Returns the usernames who are  occupying the cell with the provided index
        List<String> usernames = new ArrayList<>();
        for (Player p : game.getGamers()){
            if (p.getCurrentPosition() == index){
                usernames.add(p.getUsername());
            }
        }
        return (String[]) usernames.toArray();
    }

    public String getCurrentPlayerUsername(){
        // Returns the username of the current player
        return this
                .getGame()
                .getGamers()
                .get(this
                        .getGame()
                        .getCurrentPlayerIndex())
                .getUsername();
    }

    public int getCurrentPlayerPosition(){
        // Returns the index of the cell currently occupied by the player
        return this
                .getGame()
                .getGamers()
                .get(this
                        .getGame()
                        .getCurrentPlayerIndex())
                .getCurrentPosition();
    }

    private void changeEntityValue(Object entity, EntityParam param, int value){
        // adds the specified value to the entity param and returns the old value
        try {
            MethodGetter.getSetter(param.toString(), entity.getClass()).invoke(entity, value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchSetterException e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    public void changePlayerValue(ChangePlayerValueBean bean) {
        // Changes values of player params, looking at the param group first
        switch (bean.getParam().getGroup()){
            case PLAYER:
                changeEntityValue(
                        game.getPlayerByUsername(bean.getUsername()),
                        bean.getParam(),
                        bean.getValue());
            case POKEMON:
                changeEntityValue(
                        game.getPlayerByUsername(bean.getUsername()).getPokemon(),
                        bean.getParam(),
                        bean.getValue());
        }

    }

    public void moveInCell(MoveBean bean){
        // TODO: called when the player enters a Cell
    }

    public void moveFromCell(MoveBean bean){
        // TODO: Called when the player gets out from a cell
    }

    public void stayInCell(MoveBean bean){
        /*
         TODO:
          Called when the player stays in a Cell, for example when it starts the turn in that cell.
          The boardIndex bean field it's ignored since a player can stay in a cell only if it's the currently occupied cell.
         */
    }

}
