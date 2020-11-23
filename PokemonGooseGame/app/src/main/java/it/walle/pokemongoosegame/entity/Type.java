package it.walle.pokemongoosegame.entity;

import java.util.List;

public class Type{

    private String name = "";
    private List<String> double_damage_from = null;
    private List<String> double_damage_to = null;
    private List<String> half_damage_from = null;
    private List<String> half_damage_to = null;
    private List<String> no_damage_from = null;
    private List<String> no_damage_to = null;

    public Type(){}

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setHalf_damage_from(List<String> half_damage_from){
        this.half_damage_from = half_damage_from;
    }

    public List<String> getHalf_damage_from(){
        return half_damage_from;
    }

    public void setHalf_damage_to(List<String> half_damage_to){
        this.half_damage_to = half_damage_to;
    }

    public List<String> getHalf_damage_to(){
        return half_damage_to;
    }

    public void setDouble_damage_from(List<String> double_damage_from){
        this.double_damage_from = double_damage_from;
    }

    public List<String> getDouble_damage_from(){
        return double_damage_from;
    }

    public void setDouble_damage_to(List<String> double_damage_to){
        this.double_damage_to = double_damage_to;
    }

    public List<String> getDouble_damage_to(){
        return double_damage_to;
    }

    public void setNo_damage_from(List<String> no_damage_from){
        this.no_damage_from = no_damage_from;
    }

    public List<String> getNo_damage_from(){
        return no_damage_from;
    }

    public void setNo_damage_to(List<String> no_damage_to){
        this.no_damage_to = no_damage_to;
    }

    public List<String> getNo_damage_to(){
        return no_damage_to;
    }
}
