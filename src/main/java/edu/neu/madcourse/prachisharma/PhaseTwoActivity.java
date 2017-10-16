package edu.neu.madcourse.prachisharma;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class PhaseTwoActivity extends AppCompatActivity {

    HashSet<String> ListOfWords = new HashSet<>();

    String word = "";

    Button firstButton;
    Button SecondButton;
    Button ThirdButton;
    Button FourthButton;
    Button FifthButton;
    Button SixthButton;
    Button SeventhButton;
    Button EigthButton;
    Button NinethButton;
    Button selectButton;
    Button pauseButton;
    Button mute;

    Button[] small;

    int Score;
    int totalScore;

    TextView scoreText;
    TextView timeText;

    CountDownTimer countDownTimer;

    long remainingTime;

    boolean isPaused = false;

    ArrayList<Button> selectedButtonsList = new ArrayList<>();
    ArrayList<String> Words = new ArrayList<>();

    MediaPlayer backgroundMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase_two);

        backgroundMusic = MediaPlayer.create(PhaseTwoActivity.this, R.raw.background);
        backgroundMusic.start();

        ArrayList<String> phaseTwoList = (ArrayList<String>) getIntent().getSerializableExtra("phaseTwoList");
        Words = (ArrayList<String>) getIntent().getSerializableExtra("WORDS");
        totalScore = (int) getIntent().getExtras().getSerializable("Score");

        getDictionary();
        timmer();

        scoreText = (TextView) findViewById(R.id.ScoreTextPhase2);
        timeText = (TextView) findViewById(R.id.timeCheckPhase2);
        scoreText.setText("Score:" + totalScore);


        firstButton = (Button) findViewById(R.id.Button1P2);
        SecondButton = (Button) findViewById(R.id.Button2P2);
        ThirdButton = (Button) findViewById(R.id.Button3P2);
        FourthButton = (Button) findViewById(R.id.Button4P2);
        FifthButton = (Button) findViewById(R.id.Button5P2);
        SixthButton = (Button) findViewById(R.id.Button6P2);
        SeventhButton = (Button) findViewById(R.id.Button7P2);
        EigthButton = (Button) findViewById(R.id.Button8P2);
        NinethButton = (Button) findViewById(R.id.Button9P2);

        selectButton = (Button) findViewById(R.id.selectButtonPhase);
        pauseButton = (Button) findViewById(R.id.pauseButtonPhase2);
        mute = (Button) findViewById(R.id.mutephase2);

        small = new Button[]{firstButton, SecondButton, ThirdButton, FourthButton, FifthButton,
                SixthButton, SeventhButton, EigthButton, NinethButton};

        for (int i = 0; i < 9; i++) {
            small[i].setBackgroundColor(Color.BLUE);
            small[i].setTextColor(Color.WHITE);
            small[i].setText(phaseTwoList.get(i));
        }

        FifthButton.setText("a");


        for (int i = 0; i < 9; i++) {
            small[i].setEnabled(true);
        }

        for (int i = 0; i < 9; i++) {
            selectLetter(small[i]);
        }

        muteBackgroundMusic();
        onSelectButtonClick();
        onPauseButtonClick();
    }

    public void selectLetter(final Button small) {
        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!small.isSelected()) {
                    selectedButtonsList.add(small);
                    vibe();
                    small.setBackgroundColor(Color.LTGRAY);
                    small.setSelected(true);
                    word = word + small.getText();
                    getScore(small);
                } else {
                    if (small.getId() == selectedButtonsList.get(selectedButtonsList.size() - 1).getId()) {
                        small.setBackgroundColor(Color.BLUE);
                        word = word.substring(0, word.length() - 1);
                        deductScore(small);
                        small.setSelected(false);
                        selectedButtonsList.remove(selectedButtonsList.size() - 1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Cannot select this", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void muteBackgroundMusic() {
        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mute.isSelected()) {
                    mute.setText("Unmute");
                    backgroundMusic.pause();
                    mute.setSelected(true);
                } else {
                    mute.setText("Mute");
                    backgroundMusic.start();
                    mute.setSelected(false);
                }

            }
        });
    }

    public void timmer() {
        countDownTimer = new CountDownTimer(75000, 1000) {
            public void onTick(final long millisUntilFinished) {
                if (isPaused) {
                    cancel();
                } else {
                    timeText.setText("" + String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    remainingTime = millisUntilFinished;
                    indicateTime(millisUntilFinished);
                }
            }

            public void onFinish() {
                timeText.setText("TIME-OUT !!");
                backgroundMusic.stop();
                Intent GameOver = new Intent(PhaseTwoActivity.this, GameOverActivity.class);
                GameOver.putExtra("WORDS", Words);
                GameOver.putExtra("Score", totalScore);
                startActivity(GameOver);
            }
        }.start();
    }

    public void onPauseButtonClick() {
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pauseButton.isSelected()) {
                    isPaused = true;
                    pauseButton.setText("Resume");
                    selectButton.setEnabled(false);
                    pauseButton.setSelected(true);
                    for (int i = 0; i < 9; i++) {
                        small[i].setVisibility(View.GONE);
                    }

                } else {
                    isPaused = false;
                    pauseButton.setText("Pause");
                    pauseButton.setSelected(false);
                    selectButton.setEnabled(true);
                    for (int i = 0; i < 9; i++) {
                        small[i].setVisibility(View.VISIBLE);
                    }
                    resumeTimmer();
                    ;
                }
            }
        });
    }

    public void resumeTimmer() {
        final CountDownTimer countDownTimer = new CountDownTimer(remainingTime, 1000) {
            public void onTick(long millisUntilFinished) {
                if (isPaused) {
                    cancel();
                } else {
                    timeText.setText("" + String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    remainingTime = millisUntilFinished;
                    indicateTime(millisUntilFinished);
                }
            }

            public void onFinish() {
                timeText.setText("TIME-OUT !!");
                backgroundMusic.stop();
                Intent GameOver = new Intent(PhaseTwoActivity.this, GameOverActivity.class);
                GameOver.putExtra("WORDS", Words);
                GameOver.putExtra("Score", totalScore);
                startActivity(GameOver);
                onStop();
            }
        }.start();
    }

    public void indicateTime(long millisUntilFinished) {
        if (millisUntilFinished <= 30000) {
            timeText.setTextColor(Color.RED);
            timeText.setText("" + String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            remainingTime = millisUntilFinished;
        }
    }

    public void getScore(Button small) {
        if (small.getText().equals("e") || small.getText().equals("a") || small.getText().equals("i")
                || small.getText().equals("o") || small.getText().equals("n") || small.getText().equals("r")
                || small.getText().equals("t") || small.getText().equals("l") || small.getText().equals("s")) {
            Score = Score + 1;
        }
        if (small.getText().equals("d") || small.getText().equals("g")) {
            Score = Score + 2;
        }
        if (small.getText().equals("b") || small.getText().equals("c") || small.getText().equals("m") || small.getText().equals("p")) {
            Score = Score + 4;
        }
        if (small.getText().equals("f") || small.getText().equals("h") || small.getText().equals("v") || small.getText().equals("w") ||
                small.getText().equals("y")) {
            Score = Score + 5;
        }
        if (small.getText().equals("k")) {
            Score = Score + 8;
        }
        if (small.getText().equals("j") || small.getText().equals("x")) {
            Score = Score + 10;
        }
        if (small.getText().equals("q") || small.getText().equals("z")) {
            Score = Score + 10;
        }
    }

    public void deductScore(Button small) {
        if (small.getText().equals("e") || small.getText().equals("a") || small.getText().equals("i") ||
                small.getText().equals("o") || small.getText().equals("n") || small.getText().equals("r") ||
                small.getText().equals("t") || small.getText().equals("l") || small.getText().equals("s")) {
            Score = Score - 1;
        }
        if (small.getText().equals("d") || small.getText().equals("g")) {
            Score = Score - 2;
        }
        if (small.getText().equals("b") || small.getText().equals("c") || small.getText().equals("m") ||
                small.getText().equals("p")) {
            Score = Score - 4;
        }
        if (small.getText().equals("f") || small.getText().equals("h") || small.getText().equals("v") ||
                small.getText().equals("w") ||
                small.getText().equals("y")) {
            Score = Score - 5;
        }
        if (small.getText().equals("k")) {
            Score = Score - 8;
        }
        if (small.getText().equals("j") || small.getText().equals("x")) {
            Score = Score - 10;
        }
        if (small.getText().equals("q") || small.getText().equals("z")) {
            Score = Score - 10;
        }
    }

    public void vibe() {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);
    }


    public void onSelectButtonClick() {
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ListOfWords.contains(word)) {
                    if (Words.contains(word)) {
                        Toast.makeText(getApplicationContext(), "You have Already selected that word!!", Toast.LENGTH_SHORT).show();
                        word = "";
                        for (int i = 0; i < 9; i++) {
                            small[i].setSelected(false);
                            small[i].setEnabled(true);
                            small[i].setBackgroundColor(Color.BLUE);
                        }
                    } else {
                        Words.add(word);
                        totalScore = totalScore + Score;
                        scoreText.setText("Score:" + totalScore);
                        Score = 0;
                        word = "";
                        selectedButtonsList.clear();
                        Toast.makeText(getApplicationContext(), "Word Present", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < 9; i++) {
                            small[i].setSelected(false);
                            small[i].setEnabled(true);
                            small[i].setBackgroundColor(Color.BLUE);
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Word Not Present", Toast.LENGTH_SHORT).show();
                    totalScore = totalScore + Score;
                    scoreText.setText("Score:" + totalScore);
                    Score = 0;
                    word = "";
                    selectedButtonsList.clear();
                    for (int i = 0; i < 9; i++) {
                        small[i].setSelected(false);
                        small[i].setEnabled(true);
                        small[i].setBackgroundColor(Color.BLUE);
                    }
                }
            }
        });
    }


    public void getDictionary() {
        InputStream file = null;
        try {
            file = getResources().openRawResource(R.raw.wordlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputStreamReader ISRfin = new InputStreamReader(file);
        BufferedReader txt = new BufferedReader(ISRfin);
        String line;

        try {
            line = txt.readLine();
            while (line != null) {
                ListOfWords.add(line);
                line = txt.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
