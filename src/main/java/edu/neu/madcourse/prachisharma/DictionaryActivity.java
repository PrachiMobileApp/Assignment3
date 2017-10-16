package edu.neu.madcourse.prachisharma;

/**
 * Imports for android.
 */

import android.content.DialogInterface;
import android.media.Ringtone;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.DialogInterface.*;

/**
 * Imports for Java.
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;


public class DictionaryActivity extends AppCompatActivity {
    /**
     * Wordlist ->Dictionary.
     * displayList -> list of words searched and are available in dictionary.
     */
    ArrayList<String> displaylist = new ArrayList<>();
   ArrayList<String> ListOfWords = new ArrayList<>();
    //HashSet<String> ListOfWords = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        final Context context = this;
        getDictionary();
        Log.i("WORDLISTSIZE",String.valueOf(ListOfWords.size()));

        /**
         * editText -> represenst edit biox where word will be enter from user.
         * DisplayText -> represents a textbox, where list of wor will display.
         */
        final EditText editText = (EditText) findViewById(R.id.DictionaryText);
        final TextView DisplayText = (TextView) findViewById(R.id.DisplayWordTextView);

        //wordListClass.setWordlist();
        //final GlobalVariable g = new GlobalVariable();
        // final MainActivity m = new MainActivity();

        /**
         * Checkcing text from the editText
         */
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (ListOfWords.contains(editText.getText().toString())) {
                    if (!(displaylist.contains(editText.getText().toString()))) {
                        beep();
                        displayList(DisplayText, editText);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        /**
         * Clear Button Execution.
         * Click on button will clear the editboc and word list display.
         */
        Button Clear = (Button) findViewById(R.id.ClearButton);
        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                DisplayText.setText("");
                displaylist.clear();
            }
        });


        /**
         * Acknowledgments Button Execution.
         * Give detail of Ackowledge after being clicked.
         */
        Button Aknowledge = (Button) findViewById(R.id.AknowledgeButton);
        Aknowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder Ack = new AlertDialog.Builder(context);
                Ack.setTitle("ACKNOWLEDGMENT");
                // Ack.setMessage("Adrienne H. Slaughter \n Zhe Song");
                TextView message = new TextView(DictionaryActivity.this);
                SpannableString s = new SpannableString("\n \t \t https://developer.android.com/reference \n \n \t \t https://stackoverflow.com");
                Linkify.addLinks(s, Linkify.WEB_URLS);
                message.setText(s);
                message.setMovementMethod(LinkMovementMethod.getInstance());
                Ack.setMessage("\n Adrienne H. Slaughter \n \n Zhe Song");
                Ack.setView(message);
                Ack.setNeutralButton("Okay", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = Ack.create();
                Ack.show();
            }
        });
    }//end of Oncreate.

    /**
     * Reading words from dictionary and storing it into an ArrayList named ListOfWords;
     */

    /**
     * Displaying searched and available words from the dictionary.
     */
    public void displayList(TextView DisplayText, EditText DictionaryText1) {
        if ((!(displaylist.contains(DictionaryText1.getText().toString()))) || DisplayText.getText().equals("")) {
            displaylist.add(DictionaryText1.getText().toString());
            StringBuilder builder = new StringBuilder();
            for (String details : displaylist) {
                builder.append(details + "\n");
            }
            DisplayText.setText(builder.toString());
        }
    }//end of displayList.

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









