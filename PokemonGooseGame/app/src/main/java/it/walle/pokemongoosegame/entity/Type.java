package it.walle.pokemongoosegame.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Type implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="id")
    private int id;
    @ColumnInfo(name="typeName")
    private String typeName;
    @ColumnInfo(name = "double_damage_from")
    private List<String> double_damage_from;
    @ColumnInfo(name = "double_damage_to")
    private List<String> double_damage_to;
    @ColumnInfo(name = "half_damage_from")
    private List<String> half_damage_from;
    @ColumnInfo(name = "half_damage_to")
    private List<String> half_damage_to;
    @ColumnInfo(name = "no_damage_from")
    private List<String> no_damage_from;
    @ColumnInfo(name = "no_damage_to")
    private List<String> no_damage_to;

    public static final Parcelable.Creator<Type> CREATOR = new Parcelable.Creator<Type>() {
        @Override
        public Type createFromParcel(Parcel in) {
            return new Type(in);
        }

        @Override
        public Type[] newArray(int size) {
            return new Type[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(typeName);
        dest.writeList(half_damage_from);
        dest.writeList(half_damage_to);
        dest.writeList(double_damage_from);
        dest.writeList(double_damage_to);
        dest.writeList(no_damage_from);
        dest.writeList(no_damage_to);
    }

    public Type(){}

    private Type(Parcel in) {
        id = in.readInt();
        typeName = in.readString();
        half_damage_from = new ArrayList<String>();
        in.readList(half_damage_from, String.class.getClassLoader());
        half_damage_to = new ArrayList<String>();
        in.readList(half_damage_to, String.class.getClassLoader());
        double_damage_from = new ArrayList<String>();
        in.readList(double_damage_from, String.class.getClassLoader());
        double_damage_to = new ArrayList<String>();
        in.readList(double_damage_to, String.class.getClassLoader());
        no_damage_from = new ArrayList<String>();
        in.readList(no_damage_from, String.class.getClassLoader());
        no_damage_to = new ArrayList<String>();
        in.readList(no_damage_to, String.class.getClassLoader());
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setTypeName(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName(){
        return this.typeName;
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
