package it.walle.pokemongoosegame.entity.effect;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import it.walle.pokemongoosegame.entity.board.pgsettings.WhatYellowEffectName;

public class YellowEffectDeck {

    private static final String TAG = YellowEffectDeck.class.getSimpleName();
    private final List<WhatYellowEffectName> effectNames = new ArrayList<>();

    public YellowEffect drawYellowEffect() {

        // If there are available yellow effects, picks a random available yellow effect class name, else returns null
        int yellowEffectRandomIndex;
        boolean chosen = false;
        String randomYellowEffectClassName = "";
        YellowEffect yellowEffect = null;
        while (!effectNames.isEmpty() && !chosen) {

            // gets a random yellow effect name from the available ones
            yellowEffectRandomIndex = new Random().nextInt(effectNames.size());
            randomYellowEffectClassName = effectNames
                    .get(yellowEffectRandomIndex)
                    .getYellowEffectClassName();
            try {

                // Instantiate the YellowEffect class with randomYellowEffectClassName name, and binds it to the board cell
                yellowEffect = (YellowEffect) Class.forName(randomYellowEffectClassName).newInstance();
                chosen = true;
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {

                // If the yellow effect name is invalid it pops it, then tries the next one until a valid one is available or all name are popped
                Log.e(TAG, "Can't instantiate effect with class name " + randomYellowEffectClassName, e);
                effectNames.remove(yellowEffectRandomIndex);

            }
        }

        // At this point, if no effect is picked means that the deck is empty
        if (yellowEffect == null) {
            throw new IllegalStateException("Can't draw yellow effects from an empty deck");
        }

        return yellowEffect;

    }

    public void addToDeck(WhatYellowEffectName... names) {

        // Adds input yellow names to deck
        effectNames.addAll(Arrays.asList(names));
    }
}
