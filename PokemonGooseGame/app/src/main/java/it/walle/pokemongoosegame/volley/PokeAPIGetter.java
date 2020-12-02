package it.walle.pokemongoosegame.volley;

public class PokeAPIGetter {

    private final String BASE;
    private final String TYPE_BY_NAME = "type/%s";

    private static PokeAPIGetter ref = null;

    public static PokeAPIGetter getReference() {
        if (ref == null) {
            ref = new PokeAPIGetter(PokeAPIVersion.V2);
        }
        return ref;
    }

    public PokeAPIGetter(PokeAPIVersion version){
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
}
