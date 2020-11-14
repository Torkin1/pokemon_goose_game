package it.walle.pokemongoosegame.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Pokemon implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="id")
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "hp")
    private int hp;                     // Max health value
    @ColumnInfo(name = "currentHp")
    private int currentHp;              // Current health value
    @ColumnInfo(name = "type")
    private List<String> type;

    public static final Parcelable.Creator<Pokemon> CREATOR = new Parcelable.Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel in) {
            return new Pokemon(in);
        }

        @Override
        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(hp);
        dest.writeList(type);
    }

    public Pokemon(){}

    private Pokemon(Parcel in) {
        id = in.readInt();
        name = in.readString();
        hp = in.readInt();
        type = new ArrayList<String>();
        in.readList(type, String.class.getClassLoader());
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
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

    public void setType(List<String> type){
        this.type = type;
    }

    public List<String> getType(){
        return this.type;
    }
}
