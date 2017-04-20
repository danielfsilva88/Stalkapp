package com.labprodam.stalkapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PrintScreen extends AppCompatActivity {

    private static final String TAG = "com.labprodam.stalkapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_screen);
        Log.i(TAG, "Return: you're here!");

        // "pega" o intent da Main
        Intent intentinfo = getIntent();
        // armazena no array local (string[] infos) o array que foi passado pra ca
        // da Main, por meio da funcao putExtra (na main) - getStringArrayExtra (neste intent)
        String[] infos = intentinfo.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(infos[0] + "\n" + infos[1] + "\n" + infos[2] + "\n" + infos[3] + "\n"
                       + infos[19] + "\n" + infos[20] + "\n" + infos[21] + "\n" + infos[22] + "\n");

    }

    // Called when user taps Return button
    public void returnScreen(View view){

        Log.i(TAG, "Return: button_clicked");
        Intent intent_return = new Intent(this, MainActivity.class);
        startActivity(intent_return);

    }
}
