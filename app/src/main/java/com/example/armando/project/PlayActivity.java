package com.example.armando.project;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PlayActivity extends Activity {

    private int bet, balance, remaining_cards;
    private String deck_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        balance = 1000;

        final ImageButton decreaseBetButton = (ImageButton) findViewById(R.id.decreaseBetBtn);
        final ImageButton increaseBetButton = (ImageButton) findViewById(R.id.increaseBetBtn);

        final Button hitButton = (Button) findViewById(R.id.btnHit);
        final Button standButton = (Button) findViewById(R.id.btnStand);
        final Button surrenderButton = (Button) findViewById(R.id.btnSurrender);

        final Button placeBetButton = (Button) findViewById(R.id.placeBetButton);

        new getNewDeckTask().execute();

        View.OnClickListener handler = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(v == increaseBetButton) {
                    increaseBet(10);
                }
                else if(v == decreaseBetButton){
                    decreaseBet(10);
                }
                else if(v == placeBetButton){
                    hitButton.setVisibility(View.VISIBLE);
                    standButton.setVisibility(View.VISIBLE);
                    surrenderButton.setVisibility(View.VISIBLE);

                    placeBetButton.setVisibility(View.GONE);
                    decreaseBetButton.setVisibility(View.GONE);
                    increaseBetButton.setVisibility(View.GONE);

                    placeBet();
                }
            }
        };
        decreaseBetButton.setOnClickListener(handler);
        increaseBetButton.setOnClickListener(handler);
        placeBetButton.setOnClickListener(handler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play, menu);
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

    public void increaseBet(int amount){
        TextView betNumber = (TextView) findViewById(R.id.betNumberText);
        int bet = Integer.parseInt(betNumber.getText().toString());
        bet = bet + amount;
        betNumber.setText(""+bet);
    }

    public void decreaseBet(int amount){
        TextView betNumber = (TextView) findViewById(R.id.betNumberText);
        int bet = Integer.parseInt(betNumber.getText().toString());
        if(bet > 10) {
            bet = bet - amount;
            betNumber.setText("" + bet);
        }
    }

    public void placeBet(){
        TextView betNumber = (TextView) findViewById(R.id.betNumberText);
        bet = Integer.parseInt(betNumber.getText().toString());
        balance = balance - bet;
        TextView balanceTextView = (TextView) findViewById(R.id.balanceNumberTextView);
        balanceTextView.setText(""+balance);
    }

    private class getNewDeckTask extends AsyncTask<Void, Void, List<Object>> {
        private static final String URL = "http://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=6";
        AndroidHttpClient client = AndroidHttpClient.newInstance("");

        @Override
        protected List<Object> doInBackground(Void... params) {
            HttpGet request = new HttpGet(URL);
            JSONResponseHandler responseHandler = new JSONResponseHandler();
            try {
                return client.execute(request, responseHandler);
            } catch (ClientProtocolException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Object> result) {
            if (null != client) {
                client.close();
            }
            deck_id = (String) result.get(0);
            remaining_cards = (int) result.get(1);
            Log.d("Deck ID", deck_id);
            Log.d("Remaining cards", ""+remaining_cards);
        }

    }

    private class JSONResponseHandler implements ResponseHandler<List<Object>>{

        @Override
        public List<Object> handleResponse(HttpResponse response) throws ClientProtocolException, IOException{
            List<Object> result = new ArrayList<>();
            String JSONResponse = new BasicResponseHandler().handleResponse(response);

            try {
                JSONObject responseObject = (JSONObject) new JSONTokener(JSONResponse).nextValue();
                if(responseObject.getBoolean("success")){
                    Log.d("Create deck:", "success = true");
                    result.add(0, responseObject.getString("deck_id"));
                    result.add(1, responseObject.getInt("remaining"));
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return result;
        }
    }
}
