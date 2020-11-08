package it.walle.pokemongoosegame;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import it.walle.pokemongoosegame.Graphics.GameView;

public class GameActivity extends AppCompatActivity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //fare FullScreen l'activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Point point = new Point();//creo un oggetto punto
        getWindowManager().getDefaultDisplay().getSize(point);
        //ora l'oggeto point ha le misure x e y

        gameView = new GameView(this, point.x, point.y);

        //ora per mostrarare la gameview sullo schermo
        setContentView(gameView);//inoltre serve invocar onpause e onresume
        //così il gioco va in pausa se metto pausa l'activity anche il gioco
        //si ferma e se riparte uguale.

    }

    protected void onPause(){
        super.onPause();
        gameView.pause();
    }

    protected void onResume(){
        super.onResume();
        gameView.resume();
    }
}
