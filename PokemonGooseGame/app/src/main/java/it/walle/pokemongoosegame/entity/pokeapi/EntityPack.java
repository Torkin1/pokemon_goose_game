package it.walle.pokemongoosegame.entity.pokeapi;

import it.walle.pokemongoosegame.entity.pokeapi.EntityPointer;

public class EntityPack {
    int count;                  // Num of entities in all packs
    String next;                // url to query next pack
    String previous;            // url to query previous pack
    EntityPointer[] result;     // List of returned entities contained in this entity pack
}
