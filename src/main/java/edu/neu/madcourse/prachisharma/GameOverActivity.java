package edu.neu.madcourse.prachisharma;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class GameOverActivity extends AppCompatActivity {

    ArrayList<String> Words = new ArrayList<>();
    int totalScore;
    TextView Score;
    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Words = (ArrayList<String>) getIntent().getSerializableExtra("WORDS");
        totalScore = (int) getIntent().getExtras().getSerializable("Score");

        Score =(TextView) findViewById(R.id.ScoreGameOver);
        Score.setText("SCORE :"+totalScore);

        home = (Button) findViewById(R.id.HOmeButton);

        TextView wordList = (TextView) findViewById(R.id.WordsListGameOver);
        wordList.setMovementMethod(new ScrollingMovementMethod());
        StringBuilder builder = new StringBuilder();
        for (String details : Words) {
            builder.append(details + "\n");
        }
        wordList.setText(builder.toString());

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ActiveMain = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(ActiveMain);
            }
        });




    }
}
