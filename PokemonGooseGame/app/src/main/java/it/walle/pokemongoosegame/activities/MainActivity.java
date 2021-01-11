package it.walle.pokemongoosegame.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import it.walle.pokemongoosegame.Bootstrap;
import it.walle.pokemongoosegame.sound.HomeWatcher;
import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.activities.addplayer.AddPlayerActivity;
import it.walle.pokemongoosegame.sound.MusicService;
import it.walle.pokemongoosegame.sound.SoundEffects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;//declare the drawer for the menu
    NavigationView navigationView;//navigation menu
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean isSoundOn;//variable for sound control

    //for sound effects
    private SoundPool soundPool;

    //sound effect
    private int sound_back, sound_click;

    //sounds
    SoundEffects soundEffects;

    //context
    Context context;

    HomeWatcher mHomeWatcher;//music controller

    private boolean mIsBound = false;//music controller
    private MusicService mServ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //declare a SP variable to check games preferences
        final SharedPreferences prefs = getSharedPreferences(getString(R.string.game_flag), MODE_PRIVATE);

        Bootstrap.getReference().doOnBoot(this);


        //The main should be fullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //set this local variable, for better usage, from the prefs
        isSoundOn = prefs.getBoolean(getString(R.string.isSoundOn_flag), true);

        //declare the volume control image, that will be turned on if sound is on, and down if muted
        final ImageView volumeCtrl = findViewById(R.id.volumeCtrl);
        if (!isSoundOn)
            volumeCtrl.setImageResource(R.drawable.sound_off);
        else
            volumeCtrl.setImageResource(R.drawable.sound_on);

        this.context = getApplicationContext();

        //music

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


        //hooks for the menu
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //has to be putted in front or will not be scrollable or clickable!! IMPORTANT
        navigationView.bringToFront();

        setUpToolbar();

        //The menu has to be clickable
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //all the cases that exist in the navigation menu
                //all have click sound or back sound, depends on the action
                switch (menuItem.getItemId()) {

                    case R.id.nav_home:
                        Intent home = new Intent(MainActivity.this, MainActivity.class);
                        onBackPressed();
                        break;

                    case R.id.nav_info:

                        Intent info = new Intent(MainActivity.this, InfoActivity.class);
                        if (prefs.getBoolean(getString(R.string.isSoundOn_flag), true))
                            soundPool.play(sound_click, 1, 1, 0, 0, 1);
                        startActivity(info);
                        break;

                    case R.id.nav_AboutUs:
                        Intent aboutUs = new Intent(MainActivity.this, AboutusActivity.class);
                        if (prefs.getBoolean(getString(R.string.isSoundOn_flag), true))
                            soundPool.play(sound_click, 1, 1, 0, 0, 1);
                        startActivity(aboutUs);
                        break;
//TODO

//                    case  R.id.nav_Policy:{
//
//                        Intent browserIntent  = new Intent(Intent.ACTION_VIEW , Uri.parse(""));
//                        startActivity(browserIntent);
//
//                    }
                    //       break;
                    case R.id.nav_share: {

                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        if (prefs.getBoolean(getString(R.string.isSoundOn_flag), true))
                            soundPool.play(sound_click, 1, 1, 0, 0, 1);
                        //creating a dummy share option
                        sharingIntent.setType("text/plain");
                        String shareBody = "http://play.google.com/store/apps/detail?id=" + getPackageName();
                        String shareSub = "Try now";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));

                    }
                    break;
                }
                return false;
            }
        });


        findViewById(R.id.play_button_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //the play image in the main activity
                if (prefs.getBoolean(getString(R.string.isSoundOn_flag), true))
                    soundPool.play(sound_click, 1, 1, 0, 0, 1);
                startActivity(new Intent(MainActivity.this, AddPlayerActivity.class));

            }
        });
        //adding effect tot the play button
        ImageView play_button_img = findViewById(R.id.play_button_img);
        Animation zoomAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        play_button_img.startAnimation(zoomAnim);

        //controlling the volume image in case there's a click on the icon, and make the chages on prefs too
        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSoundOn = !isSoundOn;
                if (!isSoundOn) {
                    volumeCtrl.setImageResource(R.drawable.sound_off);
                    if (mServ != null)
                        mServ.stopMusic();
                    Toast.makeText(getApplicationContext(), "Sound off", Toast.LENGTH_SHORT).show();

                } else {
                    volumeCtrl.setImageResource(R.drawable.sound_on);
                    if (mServ != null)
                        mServ.startMusic();
                    Toast.makeText(getApplicationContext(), "Sound on", Toast.LENGTH_SHORT).show();

                }
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(getString(R.string.isSoundOn_flag), isSoundOn);
                editor.apply();
            }
        });


        //inizilize the sound
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build();


        //bring sound from files
        sound_back = soundPool.load(this, R.raw.back_sound_poke, 1);
        sound_click = soundPool.load(this, R.raw.beep_sound_poke, 1);

        //if sound is on, pute the effects on
        if (prefs.getBoolean(getString(R.string.isSoundOn_flag), true))
            soundPool.play(sound_back, 1, 1, 0, 0, 1);
    }

    //control so don't close the activity after I press back and I'm in the menu
    @Override
    public void onBackPressed() {
        soundEffects = new SoundEffects(this);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            final SharedPreferences prefs = getSharedPreferences(getString(R.string.game_flag), MODE_PRIVATE);
            if (prefs.getBoolean(getString(R.string.isSoundOn_flag), true))
                soundPool.play(sound_back, 1, 1, 0, 0, 1);
            rotateConfigImg();
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    //creating the Toolbar
    public void setUpToolbar() {
        //I made a gimmick to make all the bar without title and transparent so looks like clicking on the engine
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.transparent));
        actionBarDrawerToggle.syncState();

    }

    public void rotateConfigImg() {
        //config img ruotates
        ImageView img = (ImageView) findViewById(R.id.config_btn);
        Animation aniRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        img.startAnimation(aniRotate);
    }

    //music
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
        stopMusic();
        super.onPause();


    }

    @Override
    protected void onStop() {
        stopMusic();
        super.onStop();
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
}