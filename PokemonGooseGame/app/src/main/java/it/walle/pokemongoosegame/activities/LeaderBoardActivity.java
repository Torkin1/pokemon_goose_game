package it.walle.pokemongoosegame.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.Utils;
import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.walle.pokemongoosegame.sound.HomeWatcher;
import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.WinnerBean;
import it.walle.pokemongoosegame.activities.gameview.BitmapBank;
import it.walle.pokemongoosegame.sound.MusicService;

public class LeaderBoardActivity extends AppCompatActivity {


    private class PositionsAdapter extends RecyclerView.Adapter<PositionsAdapter.PositionsHolder> {
        private List<WinnerBean> winnerBeanList;//the list with all the finalists

        PositionsAdapter(List<WinnerBean> winnerBeanList) {
            this.winnerBeanList = winnerBeanList;
        }

        //create this holder for the card view, used by the RV to create the leader board
        @NonNull
        @Override
        public PositionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CardView cvPositionsCard = (CardView) LeaderBoardActivity.this.getLayoutInflater().inflate(R.layout.position_card, parent, false);
            return new PositionsHolder(cvPositionsCard);
        }

        @Override
        public void onBindViewHolder(@NonNull PositionsHolder holder, int position) {
            WinnerBean winnerBean = winnerBeanList.get(position);

            //sets the infos about the player
            holder.tvPosition.setText(Integer.toString(position + 1));
            holder.tvPlayerName.setText(winnerBean.getWinnerUsername());
            holder.tvScore.setText(Integer.toString(winnerBean.getScore()));

            // If it's the first player it is the winner of the game
            if (position == 0) {
                holder.ivWinner.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return winnerBeanList.size();
        }

        private class PositionsHolder extends RecyclerView.ViewHolder {
            //declare the RV's elements
            private final TextView tvPosition;
            private final TextView tvPlayerName;
            private final TextView tvScore;
            private final ImageView ivWinner;

            public PositionsHolder(@NonNull View itemView) {
                super(itemView);//initialize the RV's element
                this.tvPosition = itemView.findViewById(R.id.tvPosition);
                this.tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
                this.tvScore = itemView.findViewById(R.id.tvScore);
                ivWinner = itemView.findViewById(R.id.ivWinner);
            }
        }
    }

    private class Holder {//RV's holder
        private final LeaderBoardActivity leaderBoardActivity;
        private final Button btnHome;
        private final RecyclerView rvPositions;


        public Holder(LeaderBoardActivity leaderBoardActivity) {
            this.leaderBoardActivity = leaderBoardActivity;
            this.btnHome = leaderBoardActivity.findViewById(R.id.btnHome);//initiliaze the home button

            this.btnHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }//if clicked finish the activity
            });

            //check the position and put them in the correct order
            this.rvPositions = leaderBoardActivity.findViewById(R.id.rvPositions);
            this.rvPositions.setLayoutManager(new LinearLayoutManager(leaderBoardActivity));
            this.rvPositions.setAdapter(new PositionsAdapter(winnerBeanList));
        }

        public Button getBtnHome() {
            return btnHome;
        }

    }

    private Holder holder;//declare the holder
    private List<WinnerBean> winnerBeanList;//declare the win list
    //music related vars
    private SoundPool soundPool;
    HomeWatcher mHomeWatcher;
    private boolean mIsBound = false;
    private MusicService mServ;
    //for sound effects

    //sound effect
    private int sound_back, sound_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        final SharedPreferences prefs = getSharedPreferences(getString(R.string.game_flag), MODE_PRIVATE);
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
        sound_click = soundPool.load(this, R.raw.celebration_sound_effect, 1);

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

        //variables for the personalized confetti animation
        ConstraintLayout leaderBoard = findViewById(R.id.leader_board_layout);

        //decoding the bitmaps from the resources
        Bitmap pokeball = BitmapFactory.decodeResource(this.getResources(), R.drawable.pikpng_com_pokeball_png_589803);
        Bitmap pokeCoin = BitmapFactory.decodeResource(this.getResources(), R.drawable.poke_coin);
        Bitmap pokeStar1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.poke_star_1);
        Bitmap pokeStar2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.poke_star_2);
        Bitmap pokeStar3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.poke_star_2);
        Bitmap specialcoin = BitmapFactory.decodeResource(this.getResources(), R.drawable.special_coin);

        //creating a list of Bitmaps
        ArrayList<Bitmap> bitmapArrayList = new ArrayList<Bitmap>();

        //adding Bitmaps to the list
        //not elegant, better ways to do it, no time have fun
        bitmapArrayList.add(pokeball);
        bitmapArrayList.add(pokeCoin);
        bitmapArrayList.add(pokeStar1);
        bitmapArrayList.add(pokeStar2);
        bitmapArrayList.add(pokeStar3);
        bitmapArrayList.add(specialcoin);


        final List<Bitmap> allPossibleConfetti = bitmapArrayList;
// Alternatively, we provide some helper methods inside `Utils` to generate square, circle,
// and triangle bitmaps.
// Utils.generateConfettiBitmaps(new int[] { Color.BLACK }, 20 /* size */);

        final int numConfetti = allPossibleConfetti.size();
        BitmapBank bitmapBank = new BitmapBank(this.getResources(), this);


        //I put an Observer to w8 the layout creation
        leaderBoard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                leaderBoard.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                final ConfettoGenerator confettoGenerator = new ConfettoGenerator() {
                    @Override
                    public Confetto generateConfetto(Random random) {//random pick of the images
                        Bitmap confetti_img = allPossibleConfetti.get(random.nextInt(numConfetti));
                        confetti_img = bitmapBank.scaleConfetti(confetti_img);
                        return new BitmapConfetto(confetti_img);
                    }
                };

                final ConfettiSource confettiSource = new ConfettiSource(0, 20, leaderBoard.getWidth(), 20);

                //now spark, starts the animation with the new configured confetti
                new ConfettiManager(LeaderBoardActivity.this, confettoGenerator, confettiSource, leaderBoard)

                        .setEmissionDuration(2000)
                        .setEmissionRate(200)
                        .setVelocityX(10, 10)
                        .setVelocityY(200)
                        .setRotationalVelocity(180, 180)
                        .enableFadeOut(Utils.getDefaultAlphaInterpolator())
                        .setTouchEnabled(true)
                        .animate();

                //starts celebration music if sounds on
                if (prefs.getBoolean(getString(R.string.isSoundOn_flag), true))
                    soundPool.play(sound_click, 1, 1, 0, 0, 1);
            }
        });


        //Set full screen
        getWindow().
                setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Get the winners from the controller
        winnerBeanList = CoreController.getReference().

                endGame();

        this.holder = new

                Holder(this);
    }

    //Bind/Unbind music service

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
        //Controll this otherwise if you press back from an activity, will go back, but the onResume
        //will be called not the onCreate
        final SharedPreferences prefs = getSharedPreferences(getString(R.string.game_flag), MODE_PRIVATE);
        if (prefs.getBoolean(getString(R.string.isSoundOn_flag), true)) {
            if (mServ != null) {
                mServ.resumeMusic();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Detect idle screen
        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
    }


    @Override
    public void onBackPressed() {
        //if soundson, make back sound
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