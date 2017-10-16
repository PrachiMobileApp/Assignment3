package edu.neu.madcourse.prachisharma;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Prachi Sharma");
    }

    public void afterButtonClick(View view) {
        if (view.getId() == R.id.AboutButton) {
            Intent ActiveAbout = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(ActiveAbout);
        }
        if (view.getId() == R.id.GEbutton) {
            throw new java.lang.Error();
        }
        if (view.getId() == R.id.DictionaryButton) {
            Intent ActiveDictionary = new Intent(MainActivity.this, DictionaryActivity.class);
            startActivity(ActiveDictionary);
        }
        if (view.getId() == R.id.newGameButton) {
            Intent ActiveNewGame = new Intent(MainActivity.this, StartingNewGame.class);
            startActivity(ActiveNewGame);
        }
    }//end of getDictionary.
}
