package it.walle.pokemongoosegame.sound;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.widget.Toast;

import it.walle.pokemongoosegame.R;

public class SoundEffects {

    //TODO
//was a test with idea, doesnt work yet, commited just as reminder

    //for sound effects
    private SoundPool soundPool;

    //sound effect
    private int sound_back, sound_click;
    Context context;

    //    final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
    public SoundEffects(Activity context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build();
        //prendere da  file
        sound_back = soundPool.load(context, R.raw.back_sound_poke, 1);
        sound_click = soundPool.load(context, R.raw.beep_sound_poke, 1);
//        soundPool.play(sound_back, 1, 1, 0, 0, 1);
        this.context = context;

    }

    public SoundEffects(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build();
        //prendere da  file
        sound_back = soundPool.load(context, R.raw.back_sound_poke, 1);
        sound_click = soundPool.load(context, R.raw.beep_sound_poke, 1);
//        soundPool.play(sound_back, 1, 1, 0, 0, 1);
        this.context = context;
    }

    public void back_sound() {
        soundPool.play(sound_back, 1, 1, 0, 0, 1);
        Toast.makeText(context, "Hello dal sound", Toast.LENGTH_SHORT).show();
    }

    public void playClickSound(){

    }
}
