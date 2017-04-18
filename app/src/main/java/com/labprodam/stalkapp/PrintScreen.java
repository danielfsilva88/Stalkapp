package com.labprodam.stalkapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PrintScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_screen);

        Intent intentinfo = getIntent();
        String[] infos = intentinfo.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(infos[0] + "\n" + infos[1] + "\n" + infos[2] + "\n" + infos[3] + "\n");
    }

    // Called when user taps Return button
    public void returnScreen(View view){

        Intent intent_return = new Intent(this, MainActivity.class);
        startActivity(intent_return);

    }
}
