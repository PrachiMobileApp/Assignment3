package edu.neu.madcourse.prachisharma;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StartingNewGame extends AppCompatActivity {

   // TextView instructions;
    EditText enterName;
    Button start;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_new_game);

        start = (Button) findViewById(R.id.StartGame);
        enterName = (EditText) findViewById(R.id.EnterName);

        name = enterName.getText().toString();

        enterName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                name = enterName.getText().toString();

            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ActiveGame = new Intent(StartingNewGame.this, NewGameActivity.class);
                ActiveGame.putExtra("NAME", name);
                startActivity(ActiveGame);
            }
        });


    }
}
