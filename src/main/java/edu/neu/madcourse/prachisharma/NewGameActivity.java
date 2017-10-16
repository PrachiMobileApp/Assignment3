package edu.neu.madcourse.prachisharma;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class NewGameActivity extends AppCompatActivity {

    ArrayList<String> ListOfWords = new ArrayList<>();
    ArrayList<String> gameWordList = new ArrayList<>();
    ArrayList<String> nineWords = new ArrayList<>();
    ArrayList<String> phaseTwoList = new ArrayList<>();
    ArrayList<Button[]> buttonList = new ArrayList<>();
    ArrayList<Button> selectedButtonsList = new ArrayList<>();
    ArrayList<String> Words = new ArrayList<>();
    ArrayList<String> finalNineWords = new ArrayList<>();
    ArrayList<CharSequence> Letters = new ArrayList<>();
    HashMap<Button, Button[]> mapLegalMove = new HashMap<>();
    Button[] legalButtons;

    MediaPlayer backgroundMusic;

    String word = "";

    int Score = 0;
    int totalScore = 0;
    long remainingTime = 0;

    boolean isPaused = false;


    GridLayout gridLayout;
    GridLayout large1;
    GridLayout large2;
    GridLayout large3;
    GridLayout large4;
    GridLayout large5;
    GridLayout large6;
    GridLayout large7;
    GridLayout large8;
    GridLayout large9;

    GridLayout[] large;

    Button small1;
    Button small2;
    Button small3;
    Button small4;
    Button small5;
    Button small6;
    Button small7;
    Button small8;
    Button small9;
    Button WordSelect;
    Button pauseButton;
    Button mute;

    Button[] button1;
    Button[] button2;
    Button[] button3;
    Button[] button4;
    Button[] button5;
    Button[] button6;
    Button[] button7;
    Button[] button8;
    Button[] button9;


    TextView timeCheck;
    TextView score;
    TextView userName;

    Button smallButtons[];

    CountDownTimer countDownTimer;

    char[] FirstWord;
    char[] SecondWord;
    char[] ThirdWord;
    char[] FourthWord;
    char[] FifthWord;
    char[] SixthWord;
    char[] SeventhWord;
    char[] EigthWord;
    char[] NinthWord;


    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.larger_board);
        setTitle("New Game");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("NAME");
            Log.i("NAME", name);
            userName = (TextView) findViewById(R.id.username);
            userName.setText(name);
        }


        backgroundMusic = MediaPlayer.create(NewGameActivity.this, R.raw.background);
        backgroundMusic.start();
        getDictionary();
        Log.i("LISTOFWORDSSIZE", String.valueOf(ListOfWords.size()));
        getNineWord();
        getNineWords();
        setUpLayout();
        setUpButtons();
        setTextView();
        //defineLegalMove();
        large = new GridLayout[]{large1, large2, large3, large4, large5, large6, large7, large8, large9};
        smallButtons = new Button[]{small1, small2, small3, small4, small5, small6, small7, small8, small9};
        timmer();
        getButtonValueForEachBlock();
        disableAllBlocks();
        EnableView(large1);
        onPauseButtonClick();
        selectButton();
        addButtonsToArray();
        setButtonsSelect();
        getTextOnButton();
        muteBackgroundMusic();
    }

    private void addButtonsToArray() {
        buttonList.add(button1);
        buttonList.add(button2);
        buttonList.add(button3);
        buttonList.add(button4);
        buttonList.add(button5);
        buttonList.add(button6);
        buttonList.add(button7);
        buttonList.add(button8);
        buttonList.add(button9);
    }

    public void getTextOnButton() {
        getValuesOnButton(button1, FirstWord);
        getValuesOnButton(button2, SecondWord);
        getValuesOnButton(button3, ThirdWord);
        getValuesOnButton(button4, FourthWord);
        getValuesOnButton(button5, FifthWord);
        getValuesOnButton(button6, SixthWord);
        getValuesOnButton(button7, SeventhWord);
        getValuesOnButton(button8, EigthWord);
        getValuesOnButton(button9, NinthWord);
    }

    public void selectButton() {
        WordSelect.setOnClickListener(new View.OnClickListener() {
            int i = 0;

            @Override
            public void onClick(View v) {
                if (i < 8) {
                    checkAndResetWord();
                    selectedButtonsList = new ArrayList<Button>();
                    EnableView(large[i + 1]);
                    DisableViewt(large[i]);
                    i++;
                } else if (i >= 8 || timeCheck.getText().equals("TIME-OUT !!")) {
                    checkAndResetWord();
                    selectedButtonsList = new ArrayList<Button>();
                    DisableViewt(large[i]);
                }

            }
        });
    }

    public void showAndResetScore() {
        totalScore = totalScore + Score;
        score.setText("Score" + totalScore);
        Score = 0;
    }

    public void checkAndResetWord() {
        if (ListOfWords.contains(word)) {
            Words.add(word);
            beep();
            for (Button v : selectedButtonsList) {
                v.setBackgroundColor(Color.GREEN);
            }
            showAndResetScore();
            word = "";

        } else {
            for (Button v : selectedButtonsList) {
                v.setBackgroundColor(Color.RED);
            }
            word = "";
        }
    }


    public void disableAllBlocks() {
        for (int i = 0; i < 9; i++) {
            DisableView(large[i]);
        }
    }

    public void onPauseButtonClick() {
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pauseButton.isSelected()) {
                    isPaused = true;
                    pauseButton.setText("Resume");
                    WordSelect.setEnabled(false);
                    pauseButton.setSelected(true);
                    for (int i = 0; i < 9; i++) {
                        large[i].setVisibility(View.GONE);
                    }

                } else {
                    isPaused = false;
                    pauseButton.setText("Pause");
                    pauseButton.setSelected(false);
                    WordSelect.setEnabled(true);
                    for (int i = 0; i < 9; i++) {
                        large[i].setVisibility(View.VISIBLE);
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
                    timeCheck.setText("" + String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    remainingTime = millisUntilFinished;
                    indicateTime(millisUntilFinished);

                }
            }

            public void onFinish() {
                timeCheck.setText("TIME-OUT !!");
                disableAllBlocks();
                backgroundMusic.stop();
                if (Letters.size() >= 9) {
                    Intent ActivePhase2 = new Intent(NewGameActivity.this, PhaseTwoActivity.class);
                    ActivePhase2.putExtra("phaseTwoList", phaseTwoList);
                    ActivePhase2.putExtra("WORDS", Words);
                    ActivePhase2.putExtra("Score", totalScore);
                    startActivity(ActivePhase2);
                } else {
                    final AlertDialog.Builder Ack = new AlertDialog.Builder(context);
                    Ack.setTitle("SCROGGLE");
                    TextView message = new TextView(NewGameActivity.this);
                    message.setMovementMethod(LinkMovementMethod.getInstance());
                    Ack.setMessage("\nYou Cannot move Forwards !!!");
                    Ack.setView(message);
                    Ack.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent ActiveMain = new Intent(NewGameActivity.this, MainActivity.class);
                            startActivity(ActiveMain);
                        }
                    });
                    AlertDialog alertDialog = Ack.create();
                    Ack.show();
                }
                onStop();
            }
        }.start();
    }

    public void indicateTime(long millisUntilFinished) {
        if (millisUntilFinished <= 30000) {
            timeCheck.setTextColor(Color.RED);
            timeCheck.setText("" + String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            remainingTime = millisUntilFinished;
        }
    }

    public void setButtonsSelect() {
        for (Button[] buttonArray : buttonList) {
            setButtonSelect(buttonArray);
        }
    }

    public void EnableView(GridLayout large) {
        for (int i = 0; i < large.getChildCount(); i++) {
            View child = large.getChildAt(i);
            child.setEnabled(true);
            selectLetter((Button) child);
            child.setBackgroundColor(Color.BLUE);
        }
    }


    public void setButtonSelect(Button[] buttons) {
        for (Button button : buttons) {
            button.setSelected(true);
        }
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

    public void selectLetter(final Button small) {

        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (small.isSelected()) {
                    if (checkLegalMove(small)) {
                        Letters.add(small.getText());
                        selectedButtonsList.add(small);
                        vibe();
                        small.setBackgroundColor(Color.LTGRAY);
                        small.setSelected(false);
                        word = word + small.getText();
                        getScore(small);
                        phaseTwoList.add((String) small.getText());
                    } else {
                        Toast.makeText(getApplicationContext(), "WRONG MOVE", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if (small.getId() == selectedButtonsList.get(selectedButtonsList.size() - 1).getId()) {
                        small.setBackgroundColor(Color.BLUE);
                        word = word.substring(0, word.length() - 1);
                        deductScore(small);
                        small.setSelected(true);
                        selectedButtonsList.remove(selectedButtonsList.size() - 1);
                        phaseTwoList.remove((String) small.getText());
                    } else {
                        Toast.makeText(getApplicationContext(), "Cannot select this", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public void vibe() {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);
    }

    public void setUpLayout() {

        gridLayout = (GridLayout) findViewById(R.id.gridlayoutphase1);

        large1 = (GridLayout) findViewById(R.id.large1);
        large2 = (GridLayout) findViewById(R.id.large2);
        large3 = (GridLayout) findViewById(R.id.large3);
        large4 = (GridLayout) findViewById(R.id.large4);
        large5 = (GridLayout) findViewById(R.id.large5);
        large6 = (GridLayout) findViewById(R.id.large6);
        large7 = (GridLayout) findViewById(R.id.large7);
        large8 = (GridLayout) findViewById(R.id.large8);
        large9 = (GridLayout) findViewById(R.id.large9);
    }

    private void setUpButtons() {
        small1 = (Button) findViewById(R.id.small1);
        small2 = (Button) findViewById(R.id.small2);
        small3 = (Button) findViewById(R.id.small3);
        small4 = (Button) findViewById(R.id.small4);
        small5 = (Button) findViewById(R.id.small5);
        small6 = (Button) findViewById(R.id.small6);
        small7 = (Button) findViewById(R.id.small7);
        small8 = (Button) findViewById(R.id.small8);
        small9 = (Button) findViewById(R.id.small9);
        WordSelect = (Button) findViewById(R.id.selectButton);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        mute = (Button) findViewById(R.id.MuteButton);
    }

    public void getNineWords() {
        FirstWord = nineWords.get(0).toCharArray();
        SecondWord = nineWords.get(1).toCharArray();
        ThirdWord = nineWords.get(2).toCharArray();
        FourthWord = nineWords.get(3).toCharArray();
        FifthWord = nineWords.get(4).toCharArray();
        SixthWord = nineWords.get(5).toCharArray();
        SeventhWord = nineWords.get(6).toCharArray();
        EigthWord = nineWords.get(7).toCharArray();
        NinthWord = nineWords.get(8).toCharArray();
        finalNineWords.add(FirstWord.toString());
        finalNineWords.add(SecondWord.toString());
        finalNineWords.add(ThirdWord.toString());
        finalNineWords.add(FourthWord.toString());
        finalNineWords.add(FifthWord.toString());
        finalNineWords.add(SixthWord.toString());
        finalNineWords.add(SeventhWord.toString());
        finalNineWords.add(EigthWord.toString());
        finalNineWords.add(NinthWord.toString());

    }

    public void getButtonValueForEachBlock() {
        HashMap<GridLayout, Button[]> map = new HashMap<>();
        for (GridLayout l : large) {
            map.put(l, getButtonValues(l));
        }

        button1 = map.get(large1);
        button2 = map.get(large2);
        button3 = map.get(large3);
        button4 = map.get(large4);
        button5 = map.get(large5);
        button6 = map.get(large6);
        button7 = map.get(large7);
        button8 = map.get(large8);
        button9 = map.get(large9);
    }

    public void setTextView() {
        score = (TextView) findViewById(R.id.ScoreText);
        score.setTextColor(Color.BLUE);
        timeCheck = (TextView) findViewById(R.id.timeCheck);
    }

    public void timmer() {
        countDownTimer = new CountDownTimer(75000, 1000) {
            public void onTick(final long millisUntilFinished) {
                if (isPaused) {
                    cancel();
                } else {
                    timeCheck.setText("" + String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    remainingTime = millisUntilFinished;
                    if (timeCheck.getText().equals("TIME-OUT !!")) {
                        for (int i = 0; i < 9; i++) {
                            smallButtons[i].setEnabled(false);
                            smallButtons[i].setBackgroundColor(Color.DKGRAY);
                        }
                    }
                    indicateTime(millisUntilFinished);
                }
            }

            public void onFinish() {
                timeCheck.setText("TIME-OUT !!");
                disableAllBlocks();
                backgroundMusic.stop();
                if (Letters.size() >= 9) {
                    Intent ActivePhase2 = new Intent(NewGameActivity.this, PhaseTwoActivity.class);
                    ActivePhase2.putExtra("phaseTwoList", phaseTwoList);
                    ActivePhase2.putExtra("WORDS", Words);
                    ActivePhase2.putExtra("Score", totalScore);
                    startActivity(ActivePhase2);
                } else {
                    final AlertDialog.Builder Ack = new AlertDialog.Builder(context);
                    Ack.setTitle("SCROGGLE");
                    TextView message = new TextView(NewGameActivity.this);
                    message.setMovementMethod(LinkMovementMethod.getInstance());
                    Ack.setMessage("\nYou Cannot move Forwards !!!");
                    Ack.setView(message);
                    Ack.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent ActiveMain = new Intent(NewGameActivity.this, MainActivity.class);
                            startActivity(ActiveMain);
                        }
                    });
                    AlertDialog alertDialog = Ack.create();
                    Ack.show();
                }

            }
        }.start();
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
        if (small.getText().equals("b") || small.getText().equals("c") || small.getText().equals("m") ||
                small.getText().equals("p")) {
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

    public void getNineWord() {
        int size = gameWordList.size();
        for (int i = 0; i < 9; i++) {
            int rand = (int) (Math.random() * size);
            nineWords.add(gameWordList.get(rand));
            for (String s : nineWords) {
                Log.i("NINE", s);
            }
        }
    }

    public void DisableView(GridLayout large) {
        for (int i = 0; i < large.getChildCount(); i++) {
            View child = large.getChildAt(i);
            child.setEnabled(false);
            child.setBackgroundColor(Color.DKGRAY);
        }
    }

    public void DisableViewt(GridLayout large) {
        for (int i = 0; i < large.getChildCount(); i++) {
            View child = large.getChildAt(i);
            child.setEnabled(false);
            if (child.isSelected()) {
                child.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }


    public Button[] getButtonValues(GridLayout large) {
        Button[] small = new Button[]{(Button) large.findViewById(R.id.small1),
                (Button) large.findViewById(R.id.small2),
                (Button) large.findViewById(R.id.small3),
                (Button) large.findViewById(R.id.small4),
                (Button) large.findViewById(R.id.small5),
                (Button) large.findViewById(R.id.small6),
                (Button) large.findViewById(R.id.small7),
                (Button) large.findViewById(R.id.small8),
                (Button) large.findViewById(R.id.small9)};
        return small;
    }

    public void getValuesOnButton(Button[] button, char[] word) {
        for (int i = 0; i < 9; i++) {
            button[i].setTextColor(Color.WHITE);
            button[i].setText(String.valueOf(word[i]));
        }
    }

    /**
     * Generate Beep if word is a match from dictionary.
     */
    public void beep() {
        try {
            Uri beep = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone b = RingtoneManager.getRingtone(getApplicationContext(), beep);
            b.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end of beep.


    public boolean checkButtonSmall1(Button small) {
        ArrayList<Integer> forSmall1 = new ArrayList<>();
        forSmall1.add(R.id.small2);
        forSmall1.add(R.id.small4);
        forSmall1.add(R.id.small5);

        return forSmall1.contains(small.getId());
    }

    public boolean checkLegalMove(Button small) {

        if (selectedButtonsList.isEmpty()) {
            return true;
        }

        Button prevButton = selectedButtonsList.get(selectedButtonsList.size() - 1);

        ArrayList<Integer> forSmall1 = new ArrayList<>();
        forSmall1.add(R.id.small2);
        forSmall1.add(R.id.small4);
        forSmall1.add(R.id.small5);


        ArrayList<Integer> forSmall2 = new ArrayList<>();
        forSmall2.add(R.id.small1);
        forSmall2.add(R.id.small4);
        forSmall2.add(R.id.small5);
        forSmall2.add(R.id.small6);
        forSmall2.add(R.id.small3);

        ArrayList<Integer> forSmall3 = new ArrayList<>();
        forSmall3.add(R.id.small2);
        forSmall3.add(R.id.small5);
        forSmall3.add(R.id.small6);

        ArrayList<Integer> forSmall4 = new ArrayList<>();
        forSmall4.add(R.id.small1);
        forSmall4.add(R.id.small2);
        forSmall4.add(R.id.small5);
        forSmall4.add(R.id.small8);
        forSmall4.add(R.id.small7);

        ArrayList<Integer> forSmall5 = new ArrayList<>();
        forSmall5.add(R.id.small1);
        forSmall5.add(R.id.small2);
        forSmall5.add(R.id.small3);
        forSmall5.add(R.id.small4);
        forSmall5.add(R.id.small6);
        forSmall5.add(R.id.small7);
        forSmall5.add(R.id.small8);
        forSmall5.add(R.id.small9);


        ArrayList<Integer> forSmall6 = new ArrayList<>();
        forSmall6.add(R.id.small2);
        forSmall6.add(R.id.small5);
        forSmall6.add(R.id.small3);
        forSmall6.add(R.id.small8);
        forSmall6.add(R.id.small9);


        ArrayList<Integer> forSmall7 = new ArrayList<>();
        forSmall7.add(R.id.small4);
        forSmall7.add(R.id.small5);
        forSmall7.add(R.id.small8);


        ArrayList<Integer> forSmall8 = new ArrayList<>();
        forSmall8.add(R.id.small5);
        forSmall8.add(R.id.small4);
        forSmall8.add(R.id.small6);
        forSmall8.add(R.id.small7);
        forSmall8.add(R.id.small9);

        ArrayList<Integer> forSmall9 = new ArrayList<>();
        forSmall9.add(R.id.small5);
        forSmall9.add(R.id.small6);
        forSmall9.add(R.id.small8);

        if (prevButton.getId() == R.id.small1) {
            return forSmall1.contains(small.getId());
        }

        if (prevButton.getId() == R.id.small2) {
            return forSmall2.contains(small.getId());
        }
        if (prevButton.getId() == R.id.small3) {
            return forSmall3.contains(small.getId());
        }
        if (prevButton.getId() == R.id.small4) {
            return forSmall4.contains(small.getId());
        }
        if (prevButton.getId() == R.id.small5) {
            return forSmall5.contains(small.getId());
        }
        if (prevButton.getId() == R.id.small6) {
            return forSmall6.contains(small.getId());
        }
        if (prevButton.getId() == R.id.small7) {
            return forSmall7.contains(small.getId());
        }
        if (prevButton.getId() == R.id.small8) {
            return forSmall8.contains(small.getId());
        }
        if (prevButton.getId() == R.id.small9) {
            return forSmall9.contains(small.getId());
        }
        return false;
    }

    public void getDictionary() {

        String checkString;
        InputStream file = null;
        try {
            file = getResources().openRawResource(R.raw.wordlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String line;
        try {
            InputStreamReader ISRfin = new InputStreamReader(file);
            BufferedReader txt = new BufferedReader(ISRfin);


            line = txt.readLine();
            while (line != null) {
                ListOfWords.add(line);
                if (line.length() == 9) {
                    gameWordList.add(line);
                }
                line = txt.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



