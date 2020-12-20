package it.walle.pokemongoosegame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.badge.BadgeDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import it.walle.pokemongoosegame.boardfactory.CreateBoardBean;
import it.walle.pokemongoosegame.boardfactory.procedurallygenerated.CreateBoardPGBean;
import it.walle.pokemongoosegame.database.local.LocalDatabase;
import it.walle.pokemongoosegame.database.pokeapi.DAOSprite;
import it.walle.pokemongoosegame.entity.Game;
import it.walle.pokemongoosegame.entity.board.pgsettings.BoardPGSettings;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.TypePointerPokemon;
import it.walle.pokemongoosegame.game.AddNewPlayerBean;
import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.CreateGameBean;
import it.walle.pokemongoosegame.game.GameFactory;
import it.walle.pokemongoosegame.graphics.ToastWithIcon;
import it.walle.pokemongoosegame.selectPokemon.ControllerSelectPokemon;
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

                    int oldSelectedIndex = AddPlayerActivity.this.currentlySelectedPokemonIndex;
                    int pokemonClickedIndex = PokemonHolder.this.getAdapterPosition();

                    // Updates currently selected pokemon
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
                            View oldSelectedPointingHand = oldSelectedHolder.findViewById(R.id.ivCurrentPokemonPointer);
                            endPointingHandAnimation(oldSelectedPointingHand);
                        }
                    }

                    // shows pointing hand on clicked pokemon and animates it
                    startPointingHandAnimation(PokemonHolder.this.ivCurrentPokemonPointer);

                    // Updates currently selected pokemon index
                    AddPlayerActivity.this.setCurrentlySelectedPokemonIndex(pokemonClickedIndex);
                }
            });
        }
    }

    private class Holder{
        private final AddPlayerActivity addPlayerActivity;
        private final EditText etPlayerName;
        private final EditText etPokemonName;
        private final RecyclerView rvPokemonList ;
        private final Button bAddPlayer;
        private final RecyclerView.Adapter<PokemonHolder> rvPokemonListAdapter;
        private final EditText etBoardType;
        private final EditText etBoardName;
        private final Button bChangeBoard;
        private final Button bStartGame;

        public Holder(AddPlayerActivity addPlayerActivity){

            // Stores reference of enclosing activity for further reference
            this.addPlayerActivity = addPlayerActivity;

            // Inflates text views for pokemon and player name and populates etPlayerName with default player name
            this.etPokemonName = addPlayerActivity.findViewById(R.id.etPokemonName);
            this.etPokemonName.setKeyListener(null);
            this.etPlayerName = addPlayerActivity.findViewById(R.id.etPlayerName);
            setDefaultPlayerName(etPlayerName);

            // Inflates list of available pokemons
            this.rvPokemonList = addPlayerActivity.findViewById(R.id.rvPokemonList);
            this.rvPokemonList.setLayoutManager(new LinearLayoutManager(addPlayerActivity, LinearLayoutManager.HORIZONTAL, false));
            this.rvPokemonListAdapter = new RecyclerView.Adapter<PokemonHolder>() {

                // Uses a different queue from the one used to query pokemon to speed up sprite loading
                private DAOSprite daoSprite = new DAOSprite(addPlayerActivity, Volley.newRequestQueue(addPlayerActivity));

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
                    /* FIXME:
                        If the holder is recycled before the sprite is loaded in the ImageView, multiple listeners will try to set the sprite in the same ImageView.
                        This will generate a race condition and the displayed sprite will be the last one loaded in the ImageView, not necessarily the one corresponding to the pokemon.
                        When the sprites are present in Volley cache they are generally loaded faster and this race condition is less likely (but not impossible) to occur.
                     */
                    String spritePointer = currentPokemon.getSprites().getOther().getOfficial_artwork().getFront_default();
                    if (spritePointer == null) {
                        spritePointer = currentPokemon.getSprites().getFront_default();
                    }

                    holder.ivSprite.setImageDrawable(ContextCompat.getDrawable(addPlayerActivity, R.drawable.pikpng_com_pokeball_png_589803));
                    daoSprite.loadSprite(spritePointer, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            holder.ivSprite.setImageBitmap(response.copy(Bitmap.Config.ARGB_8888, true));
                        }
                    }, null);


                    // Puts a pointing hand on it if it's the currently selected pokemon, removes it otherwise
                    if (position == AddPlayerActivity.this.getCurrentlySelectedPokemonIndex()){
                        startPointingHandAnimation(holder.ivCurrentPokemonPointer);
                    } else {
                        endPointingHandAnimation(holder.ivCurrentPokemonPointer);
                    }
                }

                @Override
                public int getItemCount() {
                    return (AddPlayerActivity.this.getAllPokemons() == null)? 0 : AddPlayerActivity.this.getAllPokemons().size();
                }
            };
            this.rvPokemonList.setAdapter(rvPokemonListAdapter);

            // Inflates button for adding a player and adds a click listener to it
            this.bAddPlayer = findViewById(R.id.bDone);
            this.bAddPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    *       Saves player and pokemon data and adds it to the GameFactory
                    * */

                    String currentPlayerName = etPlayerName.getText().toString();

                    // If the user has not chosen a pokemon, prompts them to select one before continuing
                    if (currentlySelectedPokemonIndex < 0){

                        (new ToastWithIcon(
                                addPlayerActivity,
                                ContextCompat
                                        .getDrawable(addPlayerActivity, R.drawable.psyduck_pixel_art_maker_198642),
                                getString(R.string.ALERT_NO_POKEMON_SELECTED_MSG)
                        ))
                                .show();
                    }
                    else {

                        // Adds the player with the selected pokemon to the game
                        AddNewPlayerBean addNewPlayerBean = new AddNewPlayerBean();
                        addNewPlayerBean.setPlayerUsername(currentPlayerName);
                        addNewPlayerBean.setPokemon(allPokemons.get(currentlySelectedPokemonIndex));
                        controllerSelectPokemon.getAddNewPlayerBeans().add(addNewPlayerBean);

                        // updates etPlayerName
                        setDefaultPlayerName(etPlayerName);

                        // Notifies user of success of operation
                        (new ToastWithIcon(
                                addPlayerActivity,
                                ContextCompat
                                        .getDrawable(addPlayerActivity, R.drawable.add_player),
                                String.format(getString(R.string.ADD_PLAYER_DIALOG_TITLE), currentPlayerName)
                        )).show();
                    }


                }
            });

            // Inflates editText displaying selected board name
            this.etBoardType = findViewById(R.id.etBoardType);
            this.etBoardType.setKeyListener(null);
            this.etBoardName = findViewById(R.id.etBoardName);
            this.etBoardName.setKeyListener(null);

            // Inflates button for changing board
            this.bChangeBoard = findViewById(R.id.bChangeBoard);
            bChangeBoard.setOnClickListener(new View.OnClickListener() {
                String type;
                String name;

                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(addPlayerActivity)
                            .setTitle(R.string.DIALOG_SELECT_BOARD_TITLE)
                            .setSingleChoiceItems(R.array.BOARD_CREATION_ALGORITHMS, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    type = (getResources().getStringArray(R.array.BOARD_CREATION_ALGORITHMS))[which];
                                }
                            })
                            .setPositiveButton(R.string.BUTTON_POSITIVE_SELECT_BOARD,new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id){
                                    if (type != null){
                                        if(type.equals(BoardTypeEnumeration.PG.getType())) {

                                            LocalDatabase localDatabase = (LocalDatabase) LocalDatabase.getReference(AddPlayerActivity.this);

                                            LiveData<List<String>> boardsNameLiveData =
                                                    localDatabase
                                                            .BoardPGParamsDAO()
                                                            .getBoardsPGName(); // Nomi delle board da visualizzare

                                            Observer<List<String>> observer = new Observer<List<String>>() {
                                                @Override
                                                public void onChanged(List<String> boardsName) {
                                                    boardsName.add(getString(R.string.CREATE_NEW_BOARD_SETTINGS));
                                                    new AlertDialog.Builder(addPlayerActivity)
                                                            .setTitle(R.string.DIALOG_SELECT_BOARD_TITLE)
                                                            .setSingleChoiceItems(boardsName.toArray(new String [boardsName.size()]), -1, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    name = boardsName.get(which);
                                                                }
                                                            })
                                                            .setPositiveButton(R.string.BUTTON_POSITIVE_SELECT_BOARD,new DialogInterface.OnClickListener(){
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int id){
                                                                    if (name != null){
                                                                        if(name.equals(getString(R.string.CREATE_NEW_BOARD_SETTINGS))){
                                                                            // TODO: remove toast and start activity for creating new board settings
                                                                            Toast.makeText(AddPlayerActivity.this, R.string.TOAST_SELECT_BOARD, Toast.LENGTH_LONG).show();
                                                                        }
                                                                        else{
                                                                            AddPlayerActivity.this
                                                                                    .holder
                                                                                    .etBoardName
                                                                                    .setText(name);
                                                                            AddPlayerActivity.this
                                                                                    .holder
                                                                                    .etBoardType
                                                                                    .setText(type);

                                                                            BoardPGSettings boardPGSettings =
                                                                                    ((LocalDatabase) LocalDatabase
                                                                                            .getReference(AddPlayerActivity.this))
                                                                                            .BoardPGParamsDAO()
                                                                                            .getBoardPGSettingsByName(name)
                                                                                            .getValue();

                                                                            CreateBoardPGBean createBoardPGBean = new CreateBoardPGBean();
                                                                            createBoardPGBean.setBoardSettings(boardPGSettings);
                                                                            AddPlayerActivity.this.createBoardPGBean = createBoardPGBean;
                                                                        }
                                                                    }

                                                                }
                                                            })
                                                            .setNegativeButton(R.string.BUTTON_NEGATIVE_SELECT_BOARD, new DialogInterface.OnClickListener(){
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int id){
                                                                    dialog.cancel();
                                                                }
                                                            })
                                                            .create()
                                                            .show();
                                                }};

                                            boardsNameLiveData.observe((LifecycleOwner) AddPlayerActivity.this, observer);
                                        }
                                        else{
                                            Toast.makeText(AddPlayerActivity.this, R.string.TOAST_SELECT_BOARD, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            })
                            .setNegativeButton(R.string.BUTTON_NEGATIVE_SELECT_BOARD, new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id){
                                    dialog.cancel();
                                }
                            })
                            .create()
                            .show();
                }
            });

            // Inflates button for starting game
            this.bStartGame = findViewById(R.id.bStartGame);
            bStartGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // If no player has been added, prompts user to add one
                    if (controllerSelectPokemon.getAddNewPlayerBeans().isEmpty()){
                        (new ToastWithIcon(
                                addPlayerActivity,
                                ContextCompat
                                        .getDrawable(addPlayerActivity, R.drawable.psyduck_pixel_art_maker_198642),
                                getString(R.string.ALERT_NO_PLAYER_ADDED_MSG),
                                Toast.LENGTH_LONG
                        )).show();
                    }

                    // If no board configuration has been added, prompts user to add one
                    else if (addPlayerActivity.getCreateBoardPGBean() == null){
                        (new ToastWithIcon(
                                addPlayerActivity,
                                ContextCompat
                                        .getDrawable(addPlayerActivity, R.drawable.psyduck_pixel_art_maker_198642),
                                getString(R.string.ALERT_NO_BOARD_SELECTED_MSG),
                                Toast.LENGTH_LONG
                        )).show();
                    }
                    else {

                        // Creates a new game
                        CreateGameBean createGameBean = new CreateGameBean();
                        createGameBean.setCreateBoardBean(addPlayerActivity.getCreateBoardPGBean());
                        createGameBean.setContext(addPlayerActivity);
                        createGameBean.setLifeCycleOwner(addPlayerActivity);
                        createGameBean.setPlayerBeans(controllerSelectPokemon.getAddNewPlayerBeans());
                        createGameBean.setGameObserver(new Observer<Game>() {
                            @Override
                            public void onChanged(Game game) {

                                // Sets up game controller with newly created game
                                CoreController.getReference().setGame(game);

                                // TODO: starts next activity
                            }
                        });
                        GameFactory.getReference().createGame(createGameBean);
                    }
                }
            });
        }

        public Button getbStartGame() {
            return bStartGame;
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
            return bAddPlayer;
        }

        public AddPlayerActivity getAddPlayerActivity() {
            return addPlayerActivity;
        }

        public RecyclerView.Adapter<PokemonHolder> getRvPokemonListAdapter() {
            return rvPokemonListAdapter;
        }

        public Button getbChangeBoard() {
            return bChangeBoard;
        }
    }

    private static final String TAG = AddPlayerActivity.class.getSimpleName();

    private Holder holder;                      // Holds all view elements
    private final List<Pokemon> allPokemons;    // List of all selectable pokemons
    private CreateBoardBean createBoardPGBean;  //bean for create a new board
    private int currentlySelectedPokemonIndex;    // Index of holder currently pointed by bouncing head
    private final ControllerSelectPokemon controllerSelectPokemon; // Reference to the controller


    public CreateBoardBean getCreateBoardPGBean() {
        return createBoardPGBean;
    }

    public Holder getHolder() {
        return holder;
    }

    public List<Pokemon> getAllPokemons() {
        return allPokemons;
    }

    public static String getTAG() {
        return TAG;
    }

    public int getCurrentlySelectedPokemonIndex() {
        return currentlySelectedPokemonIndex;
    }

    public void setCurrentlySelectedPokemonIndex(int currentlySelectedPokemonIndex) {
        this.currentlySelectedPokemonIndex = currentlySelectedPokemonIndex;
    }

    public AddPlayerActivity(){

        this.controllerSelectPokemon = new ControllerSelectPokemon(this);
        this.allPokemons = new Vector<>();
        this.currentlySelectedPokemonIndex = RecyclerView.NO_POSITION;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        // Initialize controller
        controllerSelectPokemon.setAddNewPlayerBeans(new ArrayList<>());

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
        controllerSelectPokemon.loadAllPokemons(loadPokemonBean);
    }

    private void startPointingHandAnimation(View pointingHandRef){

        pointingHandRef.setVisibility(View.VISIBLE);
        pointingHandRef.startAnimation(
                AnimationUtils
                        .loadAnimation(AddPlayerActivity.this, R.anim.bounce)
        );
        pointingHandRef.setHasTransientState(true);     // Needed to keep the animation running after the view goes off screen
    }

    private void endPointingHandAnimation(View pointingHandRef){
        pointingHandRef.clearAnimation();
        pointingHandRef.setVisibility(View.INVISIBLE);
        pointingHandRef.setHasTransientState(false);
    }

    private void setDefaultPlayerName(EditText etPlayerName){
        int numOfPlayers = controllerSelectPokemon.getAddNewPlayerBeans().size();
        String defaultPlayerName = getString(R.string.PLAYER_DEFAULT_NAME) + numOfPlayers;
        etPlayerName.setText(defaultPlayerName);
    }
}