package it.walle.pokemongoosegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.WinnerBean;

public class LeaderBoardActivity extends AppCompatActivity {


    private class PositionsAdapter extends RecyclerView.Adapter<PositionsAdapter.PositionsHolder>{
        private List<WinnerBean> winnerBeanList;

        PositionsAdapter(List<WinnerBean> winnerBeanList){
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
        }

        @Override
        public int getItemCount() {
            return winnerBeanList.size();
        }

        private class PositionsHolder extends RecyclerView.ViewHolder{

            private final TextView tvPosition;
            private final TextView tvPlayerName;
            private final TextView tvScore;

            public PositionsHolder(@NonNull View itemView){
                super(itemView);
                this.tvPosition = itemView.findViewById(R.id.tvPosition);
                this.tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
                this.tvScore = itemView.findViewById(R.id.tvScore);
            }
        }
    }

    private class Holder{
        private final LeaderBoardActivity leaderBoardActivity;
        private final Button btnHome;
        private final RecyclerView rvPositions;


        public Holder(LeaderBoardActivity leaderBoardActivity){
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

        //Set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Get the winners from the controller
        winnerBeanList = CoreController.getReference().endGame();

        this.holder = new Holder(this);
    }
}