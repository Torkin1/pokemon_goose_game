package it.walle.pokemongoosegame.entity.pokeapi;

import java.util.List;

import it.walle.pokemongoosegame.entity.pokeapi.type.Type;

public class Pokemon{

    private int id = 0;
    private String name = "";
    private int hp = 0;
    private List<Type> type = null;

    public Pokemon(){}

    public Pokemon(int id, String name, int hp, List<Type> type){
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.type = type;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setHp(int hp){
        this.hp = hp;
    }

    public int getHp(){
        return this.hp;
    }

    public void setType(List<Type> type){
        this.type = type;
    }

    public List<Type> getType(){
        return this.type;
    }
}
