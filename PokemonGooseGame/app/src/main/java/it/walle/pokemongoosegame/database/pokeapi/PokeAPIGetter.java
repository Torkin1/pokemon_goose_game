package it.walle.pokemongoosegame.database.pokeapi;

public class PokeAPIGetter {

    private final String BASE;
    private final String TYPE_BY_NAME = "type/%s";//constants string used to reference the type
    private final String ALL_TYPE_POINTERS = "type/";//constant string used to reference all the types
    private final String POKEMON_BY_NAME = "pokemon/%s";//constant string used to reference the pokemon
    private final String ALL_POKEMON_POINTERS = "pokemon?limit=%d";
    private final String POKEMON = "pokemon";

    private static PokeAPIGetter ref = null;

    public static PokeAPIGetter getReference() {
        if (ref == null) {
            ref = new PokeAPIGetter(PokeAPIVersion.V2);
        }
        return ref;
    }

    private PokeAPIGetter(PokeAPIVersion version){
        String unformattedBase = "https://pokeapi.co/api/v%d/";           // Change this with the current accepted pokeapi base url
        this.BASE = String.format(unformattedBase, version.getVersion());
    }

    private String buildAPI(String... bricks){

        // Builds the api url and returns it
        StringBuilder builder = new StringBuilder();
        builder.append(BASE);
        for(String b : bricks){
            builder.append(b);
        }
        return builder.toString();
    }

    //the used getters, add more if needed
    public String getTypeByName(String name){
        return buildAPI(String.format(TYPE_BY_NAME, name));
    }

    public String getPokemonByName(String name){ return buildAPI(String.format(POKEMON_BY_NAME, name)); }

    public String getAllPokemonPointers(int limit){ return buildAPI(String.format(ALL_POKEMON_POINTERS, limit)); }

    public String getAllTypePointers(){
        return buildAPI(ALL_TYPE_POINTERS);
    }

    public String getNumOfPokemon(){ return buildAPI(POKEMON); }
}
