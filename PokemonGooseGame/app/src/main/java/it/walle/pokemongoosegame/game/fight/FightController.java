package it.walle.pokemongoosegame.game.fight;

public class FightController {

    private static FightController ref = null;
    public static FightController getReference(){
        if (ref == null){
            ref = new FightController();
        }
        return ref;
    }

    public void startFight(FightBean bean){
        // TODO: Starts a fight between provided players
    }
}
