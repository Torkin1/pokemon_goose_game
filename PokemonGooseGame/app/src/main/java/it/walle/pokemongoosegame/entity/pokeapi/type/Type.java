package it.walle.pokemongoosegame.entity.pokeapi.type;

import it.walle.pokemongoosegame.entity.pokeapi.Name;

public class Type {

    //all the type attributes
    private int id;
    private String name = "";
    private DamageRelations damage_relations;
    private Name[] names;

    public Type() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //for the fight part
    public DamageRelations getDamage_relations() {
        return damage_relations;
    }

    public void setDamage_relations(DamageRelations damage_relations) {
        this.damage_relations = damage_relations;
    }

    public Name[] getNames() {
        return names;
    }

    public void setNames(Name[] names) {
        this.names = names;
    }
}
