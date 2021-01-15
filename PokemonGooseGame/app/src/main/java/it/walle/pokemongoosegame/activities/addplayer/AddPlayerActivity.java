package it.walle.pokemongoosegame.activities.addplayer;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import it.walle.pokemongoosegame.sound.HomeWatcher;
import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.boardfactory.BlueCellSettings;
import it.walle.pokemongoosegame.boardfactory.CreateBoardBean;
import it.walle.pokemongoosegame.boardfactory.procedurallygenerated.CreateBoardPGBean;
import it.walle.pokemongoosegame.database.local.LocalDatabase;
import it.walle.pokemongoosegame.database.pokeapi.DAOSprite;
import it.walle.pokemongoosegame.entity.Game;
import it.walle.pokemongoosegame.entity.board.pgsettings.BoardPGParams;
import it.walle.pokemongoosegame.entity.board.pgsettings.BoardPGSettings;
import it.walle.pokemongoosegame.entity.board.pgsettings.WhatYellowCellStartingIndex;
import it.walle.pokemongoosegame.entity.board.pgsettings.WhatYellowEffectName;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.TypePointerPokemon;
import it.walle.pokemongoosegame.game.AddNewPlayerBean;
import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.CreateGameBean;
import it.walle.pokemongoosegame.game.GameFactory;
import it.walle.pokemongoosegame.activities.gameview.GameActivity;
import it.walle.pokemongoosegame.sound.MusicService;
import it.walle.pokemongoosegame.graphics.ToastWithIcon;
import it.walle.pokemongoosegame.activities.addplayer.selectpokemon.ControllerSelectPokemon;
import it.walle.pokemongoosegame.activities.addplayer.selectpokemon.LoadPokemonBean;
import it.walle.pokemongoosegame.utils.DrawableGetter;
import it.walle.pokemongoosegame.utils.DrawableNotFoundException;

public class AddPlayerActivity extends AppCompatActivity {

    private class PokemonHolder extends RecyclerView.ViewHolder {
        //This holder will update the view with all the contents regarding the pokemon

        private final ImageView ivSprite; //the imageView that will contain the pokemon that the player can choose
        private final LinearLayout llTypes; //the linearlayout with the various images of the type
        private final ImageView ivCurrentPokemonPointer; // an image of a pointer, that will point the selcted pokemon

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
                    if (oldSelectedIndex != RecyclerView.NO_POSITION) {
                        View oldSelectedHolder = AddPlayerActivity
                                .this
                                .getHolder()
                                .getRvPokemonList()
                                .getLayoutManager()
                                .findViewByPosition(oldSelectedIndex);
                        if (oldSelectedHolder != null) {
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

    private class Holder {
        private final AddPlayerActivity addPlayerActivity;
        private final EditText etPlayerName;
        private final EditText etPokemonName;
        private final RecyclerView rvPokemonList;
        private final Button bAddPlayer;
        private final RecyclerView.Adapter<PokemonHolder> rvPokemonListAdapter;
        private final EditText etBoardType;
        private final EditText etBoardName;
        private final Button bChangeBoard;
        private final Button bStartGame;

        public Holder(AddPlayerActivity addPlayerActivity) {

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
                            int typeDrawableId = DrawableGetter.getReference().getTypeDrawableId(t.getType().getName());
                            ImageView ivType = new ImageView(addPlayerActivity);
                            ivType.setImageDrawable(ContextCompat.getDrawable(addPlayerActivity, typeDrawableId));
                            ivType.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
                            holder.llTypes.addView(ivType);
                        } catch (DrawableNotFoundException e) {
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
                    if (position == AddPlayerActivity.this.getCurrentlySelectedPokemonIndex()) {
                        startPointingHandAnimation(holder.ivCurrentPokemonPointer);
                    } else {
                        endPointingHandAnimation(holder.ivCurrentPokemonPointer);
                    }
                }

                //return the item on a recycler view,
                // also checks if is null and returns 0, otherwise returns the size of list with all the pokemons
                @Override
                public int getItemCount() {
                    return (AddPlayerActivity.this.getAllPokemons() == null) ? 0 : AddPlayerActivity.this.getAllPokemons().size();
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
                    if (currentlySelectedPokemonIndex < 0) {

                        (new ToastWithIcon(
                                addPlayerActivity,
                                ContextCompat
                                        .getDrawable(addPlayerActivity, R.drawable.psyduck_pixel_art_maker_198642),
                                getString(R.string.ALERT_NO_POKEMON_SELECTED_MSG)
                        ))
                                .show();
                    } else {

                        // Adds the player with the selected pokemon to the game
                        AddNewPlayerBean addNewPlayerBean = new AddNewPlayerBean();
                        addNewPlayerBean.setPlayerUsername(currentPlayerName);
                        Pokemon pokemonChoose = new Pokemon(allPokemons.get(currentlySelectedPokemonIndex));
                        addNewPlayerBean.setPokemon(pokemonChoose);
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
                    //A Dialog tha appear after clicking the change button, after you can chose the procedullary genereted type
                    //in future fixed boards can be implement or other things, just imagine. if pressed ok a new dialog will open
                    new AlertDialog.Builder(addPlayerActivity)
                            .setTitle(R.string.DIALOG_SELECT_BOARD_TITLE)
                            .setSingleChoiceItems(R.array.BOARD_CREATION_ALGORITHMS, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    type = (getResources().getStringArray(R.array.BOARD_CREATION_ALGORITHMS))[which];
                                }
                            })
                            .setPositiveButton(R.string.BUTTON_POSITIVE_SELECT_BOARD, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    if (type != null) {
                                        if (type.equals(getResources().getString(R.string.BOARD_CREATION_ALGORITHMS))) {

                                            LocalDatabase localDatabase = (LocalDatabase) LocalDatabase.getReference(AddPlayerActivity.this);

                                            LiveData<List<String>> boardsNameLiveData =
                                                    localDatabase
                                                            .BoardPGParamsDAO()
                                                            .getBoardsPGName(); // Nomi delle board da visualizzare

                                            //observer that monitor the list of possible board configurations,
                                            Observer<List<String>> observer = new Observer<List<String>>() {
                                                @Override
                                                public void onChanged(List<String> boardsName) {
                                                    boardsName.add(getString(R.string.CREATE_NEW_BOARD_SETTINGS));
                                                    new AlertDialog.Builder(addPlayerActivity)
                                                            .setTitle(R.string.DIALOG_SELECT_BOARD_TITLE)
                                                            .setSingleChoiceItems(boardsName.toArray(new String[boardsName.size()]), -1, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    name = boardsName.get(which);
                                                                }
                                                            })
                                                            .setPositiveButton(R.string.BUTTON_POSITIVE_SELECT_BOARD, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    if (name != null) {
                                                                        if (name.equals(getString(R.string.CREATE_NEW_BOARD_SETTINGS))) {
                                                                            // TODO: remove toast and start activity for creating new board settings
                                                                            Toast.makeText(AddPlayerActivity.this, R.string.TOAST_SELECT_BOARD, Toast.LENGTH_LONG).show();
                                                                        } else {//if clicked on the name choice, will load the board with that chosed name
                                                                            AddPlayerActivity.this
                                                                                    .holder
                                                                                    .etBoardName
                                                                                    .setText(name);
                                                                            AddPlayerActivity.this
                                                                                    .holder
                                                                                    .etBoardType
                                                                                    .setText(type);

                                                                            LiveData<BoardPGSettings> boardPGSettingsLiveData =
                                                                                    ((LocalDatabase) LocalDatabase
                                                                                            .getReference(AddPlayerActivity.this))
                                                                                            .BoardPGParamsDAO()
                                                                                            .getBoardPGSettingsByName(name);
                                                                            //waiting for the other data, the settings and params
                                                                            Observer<BoardPGSettings> boardPGSettingsObserver = new Observer<BoardPGSettings>() {
                                                                                @Override
                                                                                public void onChanged(BoardPGSettings boardPGSettings) {
                                                                                    LiveData<BoardPGParams> boardPGParamsLiveData =
                                                                                            ((LocalDatabase) LocalDatabase
                                                                                                    .getReference(AddPlayerActivity.this))
                                                                                                    .BoardPGParamsDAO()
                                                                                                    .getBoardPGParamByName(name);

                                                                                    Observer<BoardPGParams> boardPGParamsObserver = new Observer<BoardPGParams>() {
                                                                                        @Override
                                                                                        public void onChanged(BoardPGParams boardPGParams) {
                                                                                            boardPGSettings.setBoardPGParams(boardPGParams);
                                                                                        }
                                                                                    };

                                                                                    //Ovservers the changing on the blue cells settings and after the yellow one
                                                                                    //aftera that will pass the recieved data to the boardPGSettings
                                                                                    boardPGParamsLiveData.observe((LifecycleOwner) AddPlayerActivity.this, boardPGParamsObserver);

                                                                                    LiveData<List<BlueCellSettings>> blueCellSettingsLiveData =
                                                                                            ((LocalDatabase) LocalDatabase
                                                                                                    .getReference(AddPlayerActivity.this))
                                                                                                    .BlueCellSettingsDAO()
                                                                                                    .getBlueCellSettingsByBoardSettingsName(name);

                                                                                    Observer<List<BlueCellSettings>> blueCellSettingsObserver = new Observer<List<BlueCellSettings>>() {
                                                                                        @Override
                                                                                        public void onChanged(List<BlueCellSettings> blueCellSettingsList) {
                                                                                            boardPGSettings.setBlueCellSettings(blueCellSettingsList);
                                                                                        }
                                                                                    };

                                                                                    blueCellSettingsLiveData.observe((LifecycleOwner) AddPlayerActivity.this, blueCellSettingsObserver);


                                                                                    LiveData<List<WhatYellowCellStartingIndex>> whatYellowCellStartingIndexLiveData =
                                                                                            ((LocalDatabase) LocalDatabase
                                                                                                    .getReference(AddPlayerActivity.this))
                                                                                                    .WhatYellowCellStartingIndexDAO()
                                                                                                    .getAllYellowCellStartingIndexesByBoardSettingsName(name);

                                                                                    Observer<List<WhatYellowCellStartingIndex>> whatYellowCellStartingIndexObserver = new Observer<List<WhatYellowCellStartingIndex>>() {
                                                                                        @Override
                                                                                        public void onChanged(List<WhatYellowCellStartingIndex> whatYellowCellStartingIndexList) {
                                                                                            boardPGSettings.setYellowCellStartingIndexes(whatYellowCellStartingIndexList);
                                                                                        }
                                                                                    };

                                                                                    whatYellowCellStartingIndexLiveData.observe((LifecycleOwner) AddPlayerActivity.this, whatYellowCellStartingIndexObserver);

                                                                                    LiveData<List<WhatYellowEffectName>> whatYellowEffectNameLiveData =
                                                                                            ((LocalDatabase) LocalDatabase
                                                                                                    .getReference(AddPlayerActivity.this))
                                                                                                    .WhatYellowEffectNameDAO()
                                                                                                    .getYellowEffectNamesByBoardSettingsName(name);

                                                                                    Observer<List<WhatYellowEffectName>> whatYellowEffectNameObserver = new Observer<List<WhatYellowEffectName>>() {
                                                                                        @Override
                                                                                        public void onChanged(List<WhatYellowEffectName> whatYellowEffectNameList) {
                                                                                            boardPGSettings.setYellowEffectNames(whatYellowEffectNameList);
                                                                                        }
                                                                                    };

                                                                                    whatYellowEffectNameLiveData.observe((LifecycleOwner) AddPlayerActivity.this, whatYellowEffectNameObserver);

                                                                                    CreateBoardPGBean createBoardPGBean = new CreateBoardPGBean();
                                                                                    createBoardPGBean.setBoardSettings(boardPGSettings);
                                                                                    AddPlayerActivity.this.createBoardPGBean = createBoardPGBean;
                                                                                }
                                                                            };

                                                                            boardPGSettingsLiveData.observe((LifecycleOwner) AddPlayerActivity.this, boardPGSettingsObserver);
                                                                        }
                                                                    }

                                                                }
                                                            })
                                                            .setNegativeButton(R.string.BUTTON_NEGATIVE_SELECT_BOARD, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                }
                                                            })
                                                            .create()
                                                            .show();
                                                }
                                            };

                                            boardsNameLiveData.observe((LifecycleOwner) AddPlayerActivity.this, observer);
                                        } else {//Soon the user can decide how the board is, and create one as they like
                                            Toast.makeText(AddPlayerActivity.this, R.string.TOAST_SELECT_BOARD, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            })
                            .setNegativeButton(R.string.BUTTON_NEGATIVE_SELECT_BOARD, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
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
                    if (controllerSelectPokemon.getAddNewPlayerBeans().isEmpty()) {
                        (new ToastWithIcon(
                                addPlayerActivity,
                                ContextCompat
                                        .getDrawable(addPlayerActivity, R.drawable.psyduck_pixel_art_maker_198642),
                                getString(R.string.ALERT_NO_PLAYER_ADDED_MSG),
                                Toast.LENGTH_LONG
                        )).show();
                    }

                    // If no board configuration has been added, prompts user to add one
                    else if (addPlayerActivity.getCreateBoardPGBean() == null) {
                        (new ToastWithIcon(
                                addPlayerActivity,
                                ContextCompat
                                        .getDrawable(addPlayerActivity, R.drawable.psyduck_pixel_art_maker_198642),
                                getString(R.string.ALERT_NO_BOARD_SELECTED_MSG),
                                Toast.LENGTH_LONG
                        )).show();
                    } else {
                        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);

                        if (prefs.getBoolean(getString(R.string.isSoundOn_flag), true))
                            soundPool.play(sound_click, 1, 1, 0, 0, 1);

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
                                CoreController.getReference(game);

                                // starts next activity
                                startActivity(new Intent(addPlayerActivity, GameActivity.class));
                                finish();
                            }
                        });//normally when the game starts for the first time
                        GameFactory gameFactory = new GameFactory();//new game factory istance
                        gameFactory.createGame(createGameBean);//create a bean with all the data
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
            return rvPokemonListAdapter;//returns the adapter of the recyclerView with all the pokemons
        }

        public Button getbChangeBoard() {
            return bChangeBoard;
        }
    }

    //TAG used on the logs, so they can print the class that create the error
    private static final String TAG = AddPlayerActivity.class.getSimpleName();

    private Holder holder;                      // Holds all view elements
    private List<Pokemon> allPokemons;    // List of all selectable pokemons
    private CreateBoardBean createBoardPGBean;  //bean for create a new board
    private int currentlySelectedPokemonIndex;    // Index of holder currently pointed by bouncing head
    private ControllerSelectPokemon controllerSelectPokemon; // Reference to the controller

    //istance of the class mHomeWatcher
    HomeWatcher mHomeWatcher;
    private boolean mIsBound = false; //variable to check if the music should bin with the activity
    private MusicService mServ;//istance of musicService
    //for sound effects
    private SoundPool soundPool;

    //sound effect
    private int sound_back, sound_click;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = getSharedPreferences(getString(R.string.game_flag), MODE_PRIVATE);
        setContentView(R.layout.activity_add_player);

        this.controllerSelectPokemon = new ControllerSelectPokemon();
        this.allPokemons = new Vector<>();
        this.currentlySelectedPokemonIndex = RecyclerView.NO_POSITION;

        //fare FullScreen l'activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize controller
        controllerSelectPokemon.setAddNewPlayerBeans(new ArrayList<>());

        // Binds view elements
        this.holder = new Holder(this);

        // Populates allPokemons
        LoadPokemonBean loadPokemonBean = new LoadPokemonBean();
        loadPokemonBean.setContext(this);
        loadPokemonBean.setPokemonRequestQueue(Volley.newRequestQueue(this));
        loadPokemonBean.setListener(new Response.Listener<Pokemon>() {
            @Override
            public void onResponse(Pokemon response) {

                // Atomically adds the pokemon to allPokemons and notifies the recycler view adapter about it
                synchronized (allPokemons) {
                    allPokemons.add(response);
                    int pos = allPokemons.indexOf(response);
                    holder.getRvPokemonListAdapter().notifyItemChanged(pos);
                }
            }
        });
        controllerSelectPokemon.loadAllPokemons(loadPokemonBean);

        //isSoundOn it's the bariable got from the shared prefs. where it's stored the information
        boolean isSoundOn = prefs.getBoolean(getString(R.string.isSoundOn_flag), true);

        //inizilizzo il suono
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build();


        //prendere da  file
        sound_back = soundPool.load(this, R.raw.back_sound_poke, 1);
        sound_click = soundPool.load(this, R.raw.start_sound, 1);

        //BIND Music Service
        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        if (isSoundOn)
            startService(music);

        //Start HomeWatcher
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }

            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();
    }

    //The start of the animation of the hand when a pokemon is selected from the RV
    private void startPointingHandAnimation(View pointingHandRef) {

        pointingHandRef.setVisibility(View.VISIBLE);
        pointingHandRef.startAnimation(
                AnimationUtils
                        .loadAnimation(AddPlayerActivity.this, R.anim.bounce)
        );
        pointingHandRef.setHasTransientState(true);     // Needed to keep the animation running after the view goes off screen
    }

    private void endPointingHandAnimation(View pointingHandRef) {
        pointingHandRef.clearAnimation();
        pointingHandRef.setVisibility(View.INVISIBLE);
        pointingHandRef.setHasTransientState(false);
    }

    private void setDefaultPlayerName(EditText etPlayerName) {
        int numOfPlayers = controllerSelectPokemon.getAddNewPlayerBeans().size();
        String defaultPlayerName = getString(R.string.PLAYER_DEFAULT_NAME) + numOfPlayers;
        etPlayerName.setText(defaultPlayerName);
    }

    //methods to Bind/Unbind music service

    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences prefs = getSharedPreferences(getString(R.string.game_flag), MODE_PRIVATE);
        if (prefs.getBoolean(getString(R.string.isSoundOn_flag), true)) {
            if (mServ != null) {
                mServ.resumeMusic();
            }
        }
    }

    @Override
    protected void onPause() {
        stopMusic();
        super.onPause();
    }

    @Override
    protected void onStop() {
        stopMusic();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Controll this otherwise if you press back from an activity, will go back, but the onResume
        //will be called not the onCreate
        final SharedPreferences prefs = getSharedPreferences(getString(R.string.game_flag), MODE_PRIVATE);
        if (prefs.getBoolean(getString(R.string.isSoundOn_flag), true)) {
            if (mServ != null) {
                mServ.resumeMusic();
            }
        }

    }

    private void stopMusic() {
        //Detect idle screen
        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);

        boolean isScreenOn = false;

        if (pm != null)
            isScreenOn = pm.isInteractive();//returns true if the device is read

        if (!isScreenOn && mServ != null)
            mServ.pauseMusic();
    }

    @Override
    public void onBackPressed() {
        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        if (prefs.getBoolean(getString(R.string.isSoundOn_flag), true))
            soundPool.play(sound_back, 1, 1, 0, 0, 1);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //UNBIND music service
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        mHomeWatcher.stopWatch();
        stopService(music);

    }
}