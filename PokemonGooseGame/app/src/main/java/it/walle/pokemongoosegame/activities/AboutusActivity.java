package it.walle.pokemongoosegame.activities;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Calendar;

import it.walle.pokemongoosegame.graphics.DialogManager;
import it.walle.pokemongoosegame.graphics.WIPDialog;
import it.walle.pokemongoosegame.sound.HomeWatcher;
import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.sound.MusicService;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutusActivity extends AppCompatActivity {

    //music watcher
    HomeWatcher mHomeWatcher;
    private boolean mIsBound = false;
    private MusicService mServ;
    //for sound effects
    private SoundPool soundPool;

    //sound effect
    private int sound_back, sound_click;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences prefs = getSharedPreferences(getString(R.string.game_flag), MODE_PRIVATE);

        //The main should be fullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_aboutus);

        //get preferences

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
        sound_click = soundPool.load(this, R.raw.beep_sound_poke, 1);


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
        //elemts to be displayed
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription(getString(R.string.ABOUT_US_DESC))
                .setImage(R.drawable.logo)
                .addItem(new Element().setTitle(getString(R.string.VERSION)))

                // TODO: add contact means (mail, instagram, github ...)

                .addGroup(getString(R.string.ABOUT_US_TEAM_HEADER))
                .addItem(new Element().setTitle("Jianu Mihai"))
                .addItem(new Element().setTitle("Mei Lorenzo"))
                .addItem(new Element().setTitle("Daniele La Prova"))
                .addItem(createCopyright())
                .create();
        setContentView(aboutPage);
    }

    //create an copyright element
    private Element createCopyright() {
        Element copyright = new Element();
        final String copyrightString = String.format(getString(R.string.COPYRIGHT), Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setIconDrawable(R.drawable.copyright_icon);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutusActivity.this, copyrightString, Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
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