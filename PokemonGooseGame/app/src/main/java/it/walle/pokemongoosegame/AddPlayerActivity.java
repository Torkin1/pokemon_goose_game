package it.walle.pokemongoosegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import it.walle.pokemongoosegame.database.pokeapi.DAOPokemon;
import it.walle.pokemongoosegame.database.pokeapi.DAOSprite;
import it.walle.pokemongoosegame.entity.pokeapi.allpokemon.CountPokemon;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.TypePointerPokemon;
import it.walle.pokemongoosegame.game.GameFactory;
import it.walle.pokemongoosegame.selectPokemon.ControllerSelectPokemon;
import it.walle.pokemongoosegame.selectPokemon.GetNumOfPokemonBean;
import it.walle.pokemongoosegame.selectPokemon.LoadPokemonBean;
import it.walle.pokemongoosegame.utils.TypeDrawableGetter;
import it.walle.pokemongoosegame.utils.TypeDrawableNotFoundException;

public class AddPlayerActivity extends AppCompatActivity {


    private class PokemonHolder extends RecyclerView.ViewHolder{

        private final ImageView ivSprite;
        private final LinearLayout llTypes;
        private final ImageView ivCurrentPokemonPointer;

        public PokemonHolder(@NonNull View itemView) {
            super(itemView);
            this.ivSprite = itemView.findViewById(R.id.ivSprite);
            this.llTypes = itemView.findViewById(R.id.llTypes);
            this.ivCurrentPokemonPointer = itemView.findViewById(R.id.ivCurrentPokemonPointer);

            // Clears holder of pointing hand
            ivCurrentPokemonPointer.clearAnimation();
            ivCurrentPokemonPointer.setVisibility(View.INVISIBLE);

            // sets onclick listener to holder
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int oldSelectedIndex = AddPlayerActivity.this.currentlyPointedHolderIndex;
                    int pokemonClickedIndex = PokemonHolder.this.getAdapterPosition();

                    // Updates etPlayerName with name of clicked pokemon
                    String pokemonClickedName = AddPlayerActivity
                            .this
                            .getAllPokemons()
                            .get(pokemonClickedIndex)
                            .getName();
                    AddPlayerActivity
                            .this
                            .getHolder()
                            .getEtPokemonName()
                            .setText(pokemonClickedName);

                    // Hides pointing hand on old selected pokemon
                    if (oldSelectedIndex != RecyclerView.NO_POSITION){
                        View oldSelectedHolder = AddPlayerActivity
                                .this
                                .getHolder()
                                .getRvPokemonList()
                                .getLayoutManager()
                                .findViewByPosition(oldSelectedIndex);
                        if (oldSelectedHolder != null){
                            View oldSelected = oldSelectedHolder.findViewById(R.id.ivCurrentPokemonPointer);
                            oldSelected.clearAnimation();
                            oldSelected.setVisibility(View.INVISIBLE);
                        }


                    }

                    // shows pointing hand on clicked pokemon and animates it
                    PokemonHolder
                            .this
                            .ivCurrentPokemonPointer
                            .setVisibility(View.VISIBLE);
                    PokemonHolder
                            .this
                            .ivCurrentPokemonPointer
                            .startAnimation(
                                    AnimationUtils
                                            .loadAnimation(AddPlayerActivity.this, R.anim.bounce)
                            );

                    // Updates currently selected pokemon index
                    AddPlayerActivity.this.setCurrentlyPointedHolderIndex(pokemonClickedIndex);
                }
            });
        }
    }

    private class Holder{
        private final AddPlayerActivity addPlayerActivity;
        private final EditText etPlayerName;
        private final EditText etPokemonName;
        private final RecyclerView rvPokemonList ;
        private final Button bDone;
        private final RecyclerView.Adapter<PokemonHolder> rvPokemonListAdapter;
        private final EditText etBoardName;
        private final Button bChangeBoard;

        public Holder(AddPlayerActivity addPlayerActivity){

            // Stores reference of enclosing activity for further reference
            this.addPlayerActivity = addPlayerActivity;

            // Inflates text views for pokemon and player name and populates etPlayerName with default player name
            int numOfPlayers = addPlayerActivity.getGameFactory().getNumOfPlayers();
            this.etPokemonName = addPlayerActivity.findViewById(R.id.etPokemonName);
            this.etPokemonName.setKeyListener(null);
            this.etPlayerName = addPlayerActivity.findViewById(R.id.etPlayerName);
            String defaultPlayerName = getString(R.string.PLAYER_DEFAULT_NAME) + numOfPlayers;
            etPlayerName.setText(defaultPlayerName);

            // Inflates list of available pokemons
            this.rvPokemonList = addPlayerActivity.findViewById(R.id.rvPokemonList);
            this.rvPokemonList.setLayoutManager(new LinearLayoutManager(addPlayerActivity, LinearLayoutManager.HORIZONTAL, false));
            this.rvPokemonListAdapter = new RecyclerView.Adapter<PokemonHolder>() {

                @NonNull
                @Override
                public PokemonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    // Creates an instance of pokemon holder in an inflated pokemon card layout
                    CardView cvPokemonCard = (CardView) addPlayerActivity.getLayoutInflater().inflate(R.layout.pokemon_card, parent, false);
                    return new PokemonHolder(cvPokemonCard);
                }

                @Override
                public void onBindViewHolder(@NonNull PokemonHolder holder, int position) {

                    // Gets pokemon entity corresponding to the current holder
                    Pokemon currentPokemon = allPokemons.get(position);

                    // for every pokemon type, creates an ImageView with the type icon and injects it to llTypes
                    holder.llTypes.removeAllViews();
                    for (TypePointerPokemon t : currentPokemon.getTypes()) {
                        try {

                            int typeDrawableId = TypeDrawableGetter.getReference().getTypeDrawableId(t.getType().getName());
                            ImageView ivType = new ImageView(addPlayerActivity);
                            ivType.setImageDrawable(ContextCompat.getDrawable(addPlayerActivity, typeDrawableId));
                            ivType.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
                            holder.llTypes.addView(ivType);
                        } catch (TypeDrawableNotFoundException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }

                    }
                    // sets sprite image querying pokeapi
                    String spritePointer = currentPokemon.getSprites().getOther().getOfficial_artwork().getFront_default();
                    if (spritePointer == null) {
                        spritePointer = currentPokemon.getSprites().getFront_default();
                    }

                    DAOSprite.getReference(addPlayerActivity).loadSprite(spritePointer, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            holder.ivSprite.setImageBitmap(response.copy(Bitmap.Config.ARGB_8888, true));
                        }
                    }, null);


                    // Puts a pointing hand on it if it's the currently selected pokemon, removes it otherwise
                    if (position == AddPlayerActivity.this.getCurrentlyPointedHolderIndex()){

                        holder.ivCurrentPokemonPointer.setVisibility(View.VISIBLE);
                        holder.ivCurrentPokemonPointer.startAnimation(
                                AnimationUtils
                                        .loadAnimation(AddPlayerActivity.this, R.anim.bounce)
                        );
                    } else {
                        holder.ivCurrentPokemonPointer.clearAnimation();
                        holder.ivCurrentPokemonPointer.setVisibility(View.INVISIBLE);
                    }


                }

                @Override
                public int getItemCount() {
                    return (AddPlayerActivity.this.getAllPokemons() == null)? 0 : AddPlayerActivity.this.getAllPokemons().size();
                }
            };
            this.rvPokemonList.setAdapter(rvPokemonListAdapter);

            // Inflates button for ending activity and adds a click listener to it
            this.bDone = findViewById(R.id.bDone);
            this.bDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*  TODO:
                    *       Saves player and pokemon data and adds it to the GameFactory, then asks the user if he wants to add another player.
                    *       If yes, restarts the activity, else proceeds to the next activity
                    * */
                }
            });

            // Inflates editText displaying selected board name
            this.etBoardName = findViewById(R.id.etBoardName);
            this.etBoardName.setKeyListener(null);
            // TODO: set text to default board name

            // Inflates button for changing board
            this.bChangeBoard = findViewById(R.id.bChangeBoard);
            bChangeBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: displays dialog for choosing a board name, then updates etBoardName with selected text
                }
            });
        }

        public EditText getEtPlayerName() {
            return etPlayerName;
        }

        public EditText getEtPokemonName() {
            return etPokemonName;
        }

        public RecyclerView getRvPokemonList() {
            return rvPokemonList;
        }

        public Button getBDone() {
            return bDone;
        }

        public AddPlayerActivity getAddPlayerActivity() {
            return addPlayerActivity;
        }

        public RecyclerView.Adapter<PokemonHolder> getRvPokemonListAdapter() {
            return rvPokemonListAdapter;
        }
    }

    private static final String TAG = AddPlayerActivity.class.getSimpleName();

    private Holder holder;                      // Holds all view elements
    private final GameFactory gameFactory;      // Holds a gamefactory instance to build a new game
    private final List<Pokemon> allPokemons;    // List of all selectable pokemons
    private int currentlyPointedHolderIndex;    // Index of holder currently pointed by bouncing head

    public Holder getHolder() {
        return holder;
    }

    public GameFactory getGameFactory() {
        return gameFactory;
    }

    public List<Pokemon> getAllPokemons() {
        return allPokemons;
    }

    public static String getTAG() {
        return TAG;
    }

    public int getCurrentlyPointedHolderIndex() {
        return currentlyPointedHolderIndex;
    }

    public void setCurrentlyPointedHolderIndex(int currentlyPointedHolderIndex) {
        this.currentlyPointedHolderIndex = currentlyPointedHolderIndex;
    }

    public AddPlayerActivity(){
        this.gameFactory = new GameFactory(this);
        this.allPokemons = new Vector<>();
        this.currentlyPointedHolderIndex = RecyclerView.NO_POSITION;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        // Binds view elements
        this.holder = new Holder(this);

        // Populates allPokemons
        LoadPokemonBean loadPokemonBean = new LoadPokemonBean();
        loadPokemonBean.setListener(new Response.Listener<Pokemon>() {
            @Override
            public void onResponse(Pokemon response) {

                // Atomically adds the pokemon to allPokemons and notifies the recycler view adapter about it
                synchronized (allPokemons){
                    allPokemons.add(response);
                    int pos = allPokemons.indexOf(response);
                    holder.getRvPokemonListAdapter().notifyItemChanged(pos);
                }
            }
        });
        ControllerSelectPokemon.getReference(this).loadAllPokemons(loadPokemonBean);
    }
}