package it.walle.pokemongoosegame.entity.effect;

import android.util.Log;

import java.util.List;

import it.walle.pokemongoosegame.entity.board.pgsettings.WhatYellowEffectName;

public class YellowEffectDrawer extends Effect {

    private static final String TAG = YellowEffectDrawer.class.getSimpleName();
    private final YellowEffectDeck deck = new YellowEffectDeck();

    public YellowEffectDrawer(List<WhatYellowEffectName> yellowEffectNames){
        deck.addToDeck(yellowEffectNames.toArray(new WhatYellowEffectName[0]));
    }

    @Override
    public void doEffect(InvocationContext invocationContext) {
        try {
            deck.drawYellowEffect().doEffect(invocationContext);
        } catch (IllegalStateException e){
            Log.e(TAG, e.getMessage() + ": cell index is " + invocationContext.getWhereTriggered(), e);
        }

    }
}
