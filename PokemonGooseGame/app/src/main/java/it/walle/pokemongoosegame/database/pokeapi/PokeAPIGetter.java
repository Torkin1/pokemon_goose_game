package it.walle.pokemongoosegame.database.pokeapi;

public class PokeAPIGetter {

    private final String BASE;
    private final String TYPE_BY_NAME = "type/%s";
    private final String POKEMON_BY_NAME = "pokemon/%s";
    private final String ALL_POKEMON_NAME = "pokemon?limit=%d";
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

    public String getTypeByName(String name){
        return buildAPI(String.format(TYPE_BY_NAME, name));
    }

    public String getPokemonByName(String name){ return buildAPI(String.format(POKEMON_BY_NAME, name)); }

    public String getAllPokemonName(int limit){ return buildAPI(String.format(ALL_POKEMON_NAME, limit)); }

    public String getNumOfPokemon(){ return buildAPI(POKEMON); }
}
