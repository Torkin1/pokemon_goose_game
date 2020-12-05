package it.walle.pokemongoosegame.entity.pokeapi.type;

import it.walle.pokemongoosegame.entity.pokeapi.EntityPointer;

public class DamageRelations {
    private EntityPointer[] double_damage_from;
    private EntityPointer[] double_damage_to;
    private EntityPointer[] half_damage_from;
    private EntityPointer[] half_damage_to;
    private EntityPointer[] no_damage_from;
    private EntityPointer[] no_damage_to;

    public EntityPointer[] getDouble_damage_from() {
        return double_damage_from;
    }

    public void setDouble_damage_from(EntityPointer[] double_damage_from) {
        this.double_damage_from = double_damage_from;
    }

    public EntityPointer[] getDouble_damage_to() {
        return double_damage_to;
    }

    public void setDouble_damage_to(EntityPointer[] double_damage_to) {
        this.double_damage_to = double_damage_to;
    }

    public EntityPointer[] getHalf_damage_from() {
        return half_damage_from;
    }

    public void setHalf_damage_from(EntityPointer[] half_damage_from) {
        this.half_damage_from = half_damage_from;
    }

    public EntityPointer[] getHalf_damage_to() {
        return half_damage_to;
    }

    public void setHalf_damage_to(EntityPointer[] half_damage_to) {
        this.half_damage_to = half_damage_to;
    }

    public EntityPointer[] getNo_damage_from() {
        return no_damage_from;
    }

    public void setNo_damage_from(EntityPointer[] no_damage_from) {
        this.no_damage_from = no_damage_from;
    }

    public EntityPointer[] getNo_damage_to() {
        return no_damage_to;
    }

    public void setNo_damage_to(EntityPointer[] no_damage_to) {
        this.no_damage_to = no_damage_to;
    }
}
