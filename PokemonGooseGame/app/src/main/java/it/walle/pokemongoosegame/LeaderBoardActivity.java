package it.walle.pokemongoosegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jinatonic.confetti.CommonConfetti;
import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettiView;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.Utils;
import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.WinnerBean;
import it.walle.pokemongoosegame.graphics.BitmapBank;

public class LeaderBoardActivity extends AppCompatActivity {


    private class PositionsAdapter extends RecyclerView.Adapter<PositionsAdapter.PositionsHolder> {
        private List<WinnerBean> winnerBeanList;

        PositionsAdapter(List<WinnerBean> winnerBeanList) {
            this.winnerBeanList = winnerBeanList;
        }

        @NonNull
        @Override
        public PositionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CardView cvPositionsCard = (CardView) LeaderBoardActivity.this.getLayoutInflater().inflate(R.layout.position_card, parent, false);
            return new PositionsHolder(cvPositionsCard);
        }

        @Override
        public void onBindViewHolder(@NonNull PositionsHolder holder, int position) {
            WinnerBean winnerBean = winnerBeanList.get(position);

            holder.tvPosition.setText(Integer.toString(position + 1));
            holder.tvPlayerName.setText(winnerBean.getWinnerUsername());
            holder.tvScore.setText(Integer.toString(winnerBean.getScore()));

            // If it's the first player it is the winner of the game
            if (position == 0){
                holder.ivWinner.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return winnerBeanList.size();
        }

        private class PositionsHolder extends RecyclerView.ViewHolder {

            private final TextView tvPosition;
            private final TextView tvPlayerName;
            private final TextView tvScore;
            private final ImageView ivWinner;

            public PositionsHolder(@NonNull View itemView) {
                super(itemView);
                this.tvPosition = itemView.findViewById(R.id.tvPosition);
                this.tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
                this.tvScore = itemView.findViewById(R.id.tvScore);
                ivWinner = itemView.findViewById(R.id.ivWinner);
            }
        }
    }

    private class Holder {
        private final LeaderBoardActivity leaderBoardActivity;
        private final Button btnHome;
        private final RecyclerView rvPositions;


        public Holder(LeaderBoardActivity leaderBoardActivity) {
            this.leaderBoardActivity = leaderBoardActivity;
            this.btnHome = leaderBoardActivity.findViewById(R.id.btnHome);

            this.btnHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            this.rvPositions = leaderBoardActivity.findViewById(R.id.rvPositions);
            this.rvPositions.setLayoutManager(new LinearLayoutManager(leaderBoardActivity));
            this.rvPositions.setAdapter(new PositionsAdapter(winnerBeanList));
        }

        public Button getBtnHome() {
            return btnHome;
        }

    }

    private Holder holder;
    private List<WinnerBean> winnerBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);


        //variables for the personalized confetti animation
        ConstraintLayout leaderBoard = findViewById(R.id.leader_board_layout);

        //decoding the bitmaps from the resources
        Bitmap pokeball = BitmapFactory.decodeResource(this.getResources(), R.drawable.pikpng_com_pokeball_png_589803);
        Bitmap pokeCoin = BitmapFactory.decodeResource(this.getResources(), R.drawable.poke_coin);
        Bitmap pokeStar1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.poke_star_1);
        Bitmap pokeStar2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.poke_star_2);
        Bitmap pokeStar3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.poke_star_2);

        //creating a list of Bitmaps
        ArrayList<Bitmap> bitmapArrayList = new ArrayList<Bitmap>();

        //adding Bitmaps to the list
        //not elegant, better ways to do it, no time have fun
        bitmapArrayList.add(pokeball);
        bitmapArrayList.add(pokeCoin);
        bitmapArrayList.add(pokeStar1);
        bitmapArrayList.add(pokeStar2);
        bitmapArrayList.add(pokeStar3);


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
                    }};

                final ConfettiSource confettiSource = new ConfettiSource(0, 20,  leaderBoard.getWidth(), 20);

                //now spark
                new ConfettiManager(LeaderBoardActivity.this, confettoGenerator, confettiSource, leaderBoard)

                        .setEmissionDuration(2000)
                        .setEmissionRate(200)
                        .setVelocityX(10, 10)
                        .setVelocityY(200)
                        .setRotationalVelocity(180, 180)
                        .enableFadeOut(Utils.getDefaultAlphaInterpolator())
                        .setTouchEnabled(true)
                        .animate();


                }
            });


            //Set full screen
            getWindow().

            setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            //Get the winners from the controller
            winnerBeanList =CoreController.getReference().

            endGame();

        this.holder =new

            Holder(this);
        }
    }