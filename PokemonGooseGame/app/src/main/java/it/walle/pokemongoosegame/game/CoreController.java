package it.walle.pokemongoosegame.game;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import it.walle.pokemongoosegame.entity.Game;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.entity.board.cell.Cell;
import it.walle.pokemongoosegame.entity.effect.InvocationContext;
import it.walle.pokemongoosegame.utils.MethodGetter;
import it.walle.pokemongoosegame.utils.NoSuchSetterException;

public class CoreController {
    // The game main engine.

    private final static String TAG = CoreController.class.getName();

    private static CoreController ref = null;
    public synchronized static CoreController getReference(){   // Call this only if a game has already been set
        if (ref == null || ref.game == null){
            throw new IllegalStateException("You can get a reference to CoreController only after a game has been set");
        }
        return ref;
    }

    public synchronized static CoreController getReference(Game game){  // Call this when you want to set the game while obtaining a ref to CoreController
        if (ref == null){
            ref = new CoreController();
        }
        ref.setGame(game);
        return ref;
    }

    // TODO: this should be moved in a game settings class, in a similar manner of board settngs
    public final static int MAX_HEALTH_POKEMON = 100;

    private Game game;   // Contains all infos about the game

    private Game getGame() {
        return game;
    }

    public void setGame(Game game) {

        this.game = game;

        // Adds all registered players to players currently in game
        game.getInGamePlayers().addAll(game.getAllPlayers());

        // Sets all pokemons health
        for (Player p : game.getInGamePlayers()){
            p.getPokemon().setMaxHp(MAX_HEALTH_POKEMON);
            p.getPokemon().setCurrentHp(MAX_HEALTH_POKEMON);
        }
    }

    public synchronized Board getBoard(){
        return this.game.getBoard();
    }

    public synchronized List<Player> getPlayers(){
        return this.game.getInGamePlayers();
    }

    public List<Player> getWinners(){
        return this.game.getWinners();
    }

    public List<Player> getLosers(){
        return this.game.getLosers();
    }

    public Integer getPlate(){
        return this.game.getPlate();
    }

    public void setPlate(Integer val){
        if(val < 0)
            val = 0;
        this.game.setPlate(val);
    }

    public synchronized void nextTurn(){

        // it's nonsense to call this method when there are no more players in game
        if (getPlayers().isEmpty()){
            throw new IllegalStateException("No players left in game");
        }

        // Changes the current player to the next player and updates next player index
        int newPlayerIndex = game.getNextPlayerIndex();
        this.game.setCurrentPlayerIndex(newPlayerIndex % this.game.getInGamePlayers().size());
        this.game.setNextPlayerIndex((newPlayerIndex + 1) % this.game.getInGamePlayers().size());
    }

    public synchronized Player getPlayerByUsername(String username){
        return game.getPlayerByUsername(username);
    }

    public void setNextPlayer(String username){
        this
                .game
                .setNextPlayerIndex(this
                        .game
                        .getInGamePlayers()
                        .indexOf(this
                                .game
                                .getPlayerByUsername(username)));
    }

    public void chooseLoser(LoserBean bean){

        // Removes player from in game players and adds it to losers list
        Player loser = getPlayerByUsername(bean.getPlayerUsername());
        game.getInGamePlayers().remove(loser);
        game.getLosers().add(loser);
    }

    public void chooseWinner(WinnerBean bean){

        // Removes the player from the game and adds it to the winner list, returning their score
        Player winner = this.game.getPlayerByUsername(bean.getWinnerUsername());

        this.game.getInGamePlayers().remove(winner);
        this.game.getWinners().add(winner);

        int score = this.calculateScore(winner);
        bean.setScore(score);
    }

    public void skipTurn(SkipTurnBean bean){

        // If player has turns to skip, make the player skip the turn and decrements player number of idle turns
        Player player = game.getPlayerByUsername(bean.getPlayerUsername());
        if (player.getNumOfIdleTurns() > 0){
            player.setIdleTurns(player.getNumOfIdleTurns() - 1);
            bean.setHasSkipped(true);
        } else {
            bean.setHasSkipped(false);
        }
    }

    private int calculateScore(Player player){

        //Check if the player has lost, if he has lost set the score to 0
        if(this.game.getLosers().contains(player)){
            return 0;
        }

        //if the player has not lost calculate score
        int scoreHp = player
                .getPokemon()
                .getCurrentHp();
        int scoreMoney = player.getMoney();
        int scorePlate = this.game.getPlate();

        // Resets plate
        this.setPlate(0);

        return scoreHp + scoreMoney + scorePlate;
    }

    public List<WinnerBean> endGame(){   // Call this when you want the game to normally end (for example, when all player have become winners or losers)
        List<WinnerBean> winnerBeans = new ArrayList<>();

        // Create a list of winners
        List<Player> winners = game.getWinners();

        // Now for all the players create winnerBean and set username and score
        for(int i = 0; i < winners.size(); i++){
            WinnerBean bean = new WinnerBean();
            bean.setWinnerUsername(winners.get(i).getUsername());
            bean.setScore(calculateScore(winners.get(i)));
            winnerBeans.add(bean);
        }

        // Sort winnerBeans list with the highest score in first position
        winnerBeans.sort(new Comparator<WinnerBean>() {
            @Override
            public int compare(WinnerBean o1, WinnerBean o2) {
                return o1.getScore() - o2.getScore();
            }
        });

        // Interrupts game and returns winners
        this.abortGame();
        return winnerBeans;
    }

    public void abortGame(){    // call this when you want to interrupt the game (pause game to resume another time, canceling game, ...)
        this.game = null;
    }

    public void throwDices(ThrowDicesBean bean){

        // Generates a number of casual values using params specified in bean
        ArrayList<Integer> exitNumbers = new ArrayList<>();
        for(int i = 0; i < bean.getNumOfDices(); i++){
            exitNumbers.add(i, (int) (Math.random() * bean.getNumOfFaces() + 1));
        }
        bean.setExitNumbers(exitNumbers);
    }

    public String[] getAllPlayersInACellUsernames(int index){
        // Returns the usernames who are  occupying the cell with the provided index
        List<String> usernames = new ArrayList<>();
        for (Player p : game.getInGamePlayers()){
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
                .getInGamePlayers()
                .get(this
                        .getGame()
                        .getCurrentPlayerIndex())
                .getUsername();
    }

    public int getCurrentPlayerPosition(){
        // Returns the index of the cell currently occupied by the player
        return this
                .getGame()
                .getInGamePlayers()
                .get(this
                        .getGame()
                        .getCurrentPlayerIndex())
                .getCurrentPosition();
    }

    public int getCurrentPlayerCoins(){
        return this
                .getGame()
                .getInGamePlayers()
                .get(this
                        .getGame()
                        .getCurrentPlayerIndex())
                .getMoney();
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

        int newPos = bean.getBoardIndex();

        // If target cell index exceeds index of last cell, player must go back
        int lastCellIndex = game.getBoard().getCells().size() - 1;
        int delta = bean.getBoardIndex() - lastCellIndex;
        if (delta > 0){
            newPos = lastCellIndex - delta;
        }

        //Set the new position of the player
        Player player = this.game
                        .getPlayerByUsername(bean.getPlayerUsername());

        player.setCurrentPosition(newPos);

        Cell cell = this.game
                .getBoard()
                .getCells()
                .get(newPos);

        //Check if in the cell there are an effect or not. If there are, do entry effect
        if(cell.getEntryEffect() != null){
            InvocationContext invocationContext =
                    this.setInvocationContext(
                            bean.getPlayerUsername(),
                            newPos,
                            bean.getContext()
                    );

            cell.getEntryEffect().doEffect(invocationContext);
        }
    }

    public void moveFromCell(MoveBean bean){
        Cell cell = this.game
                .getBoard()
                .getCells()
                .get(bean.getBoardIndex());

        //Check if in the cell there are an effect or not. If there are, do exit effect
        if(cell.getExitEffect() != null){
            InvocationContext invocationContext =
                    this.setInvocationContext(
                            bean.getPlayerUsername(),
                            bean.getBoardIndex(),
                            bean.getContext()
                    );

            cell.getExitEffect().doEffect(invocationContext);
        }
    }

    public void stayInCell(MoveBean bean){
        Cell cell = this.game
                .getBoard()
                .getCells()
                .get(bean.getBoardIndex());

        //Check if in the cell there are an effect or not. If there are, do stay effect
        if(cell.getStayEffect() != null){
            InvocationContext invocationContext =
                    this.setInvocationContext(
                            bean.getPlayerUsername(),
                            bean.getBoardIndex(),
                            bean.getContext()
                    );

            cell.getStayEffect().doEffect(invocationContext);
        }
    }

    private InvocationContext setInvocationContext(String playerUsername, int boardIndex, Context context) {
        InvocationContext invocationContext = new InvocationContext();
        invocationContext.setTriggerUsername(playerUsername);
        invocationContext.setWhereTriggered(boardIndex);
        invocationContext.setContext(context);

        return invocationContext;
    }

    public void observePlate(LifecycleOwner lifecycleOwner, Observer<Integer> observer){
        this.game.observePlate(lifecycleOwner, observer);
    }
}
