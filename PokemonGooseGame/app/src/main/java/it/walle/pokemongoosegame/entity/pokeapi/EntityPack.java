package it.walle.pokemongoosegame.entity.pokeapi;

public class EntityPack {
    private int count;                      // Num of entities in all packs
    private String next;                    // url to query next pack
    private String previous;                // url to query previous pack
    private EntityPointer[] results;        // List of returned entities contained in this entity pack

    public void setCount(int count) {
        this.count = count;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public void setResults(EntityPointer[] results) {
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public EntityPointer[] getResults() {
        return results;
    }
}
