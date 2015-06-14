package com.example.armando.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button playBtn = (Button) findViewById(R.id.playBtn);
        final Button highScoresBtn = (Button) findViewById(R.id.highScoresBtn);
        final Button settingsBtn = (Button) findViewById(R.id.settingsBtn);
        final Button exitBtn = (Button) findViewById(R.id.exitBtn);

        View.OnClickListener handler = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(v == playBtn) {
                    Intent i = new Intent(getApplicationContext(), PlayActivity.class);
                    startActivity(i);
                }
                else if(v == highScoresBtn){
                    Intent i = new Intent(getApplicationContext(), HighScoreActivity.class);
                    startActivity(i);
                }
                else if(v == settingsBtn){
                    Intent i = new Intent(getApplicationContext(), HighScoreActivity.class);
                    startActivity(i);
                }
                else if(v == exitBtn){
                    finish();
                    System.exit(0);
                }
            }
        };

        playBtn.setOnClickListener(handler);
        highScoresBtn.setOnClickListener(handler);
        settingsBtn.setOnClickListener(handler);
        exitBtn.setOnClickListener(handler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
