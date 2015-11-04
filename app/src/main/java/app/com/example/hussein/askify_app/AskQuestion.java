package app.com.example.hussein.askify_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class AskQuestion extends ActionBarActivity implements View.OnClickListener {

    private EditText AskQ;
    String AskQ_result;
    private Button AskButton;
    String Ask_URL = "http://askify-app.herokuapp.com/public/api/question/create";
    String user_id ; //user_id from login
    String Private; //private/public to be sent
    String success;
    String newQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        user_id = b.getString("user_id");
        AskQ = (EditText) findViewById(R.id.ask_question);


        AskButton = (Button) findViewById(R.id.button_ask);
        AskButton.setOnClickListener(this);

    }//end of onCreate

    ////////////////////////////////////////////*onClick*/////////////////////////////////////////
    @Override
    public void onClick (View v){
        if (v.getId() == R.id.button_ask) {
            newQuestion = AskQ.getText().toString();
            //if(newQuestion.length()>10 && newQuestion.length()<255){
            new AskAsync().execute();
            Intent j = new Intent(this,My_Questions.class);
            Bundle x = new Bundle();
            x.putString("user_id", user_id);
            j.putExtras(x);
            startActivity(j);
             //else{Toast.makeText(AskQuestion.this,"Error Enter yor Question",Toast.LENGTH_LONG).show();}

        }

    }//end of onClick

    ////////////////*for radiobuttons */////////////////
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.private_button:
                if (checked)
                    Private = "true";
                break;
            case R.id.public_button:
                if (checked)
                    Private = "false";
                break;
        }
    }// end of onRadioButtonClicked


    ////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////*AskAsync*////////////////////////////////////////////////
    private class AskAsync extends AsyncTask<String, String, String> {
        String forecastJsonStr;
        String postDataBytes;
        @Override
        protected String doInBackground(String... args) {


            Map<String,Object> params = new LinkedHashMap<>();
            params.put("user_id", "5");
            params.put("question", newQuestion );
            params.put("private", Private);
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                try {
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    postDataBytes = postData.toString();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }//end of for

            try {
                JSONParser jP = new JSONParser();
                forecastJsonStr = jP.excutePost(Ask_URL, postDataBytes);
                JSONObject forecast = new JSONObject(forecastJsonStr);
                success = forecast.getString("error");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return success;
        }//end of doInBackground

        @Override
        protected void onPostExecute(String success1) {
            if (success1.equals("false")) {
                Toast.makeText(AskQuestion.this,"Your Question Has Been Successfully Posted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AskQuestion.this, "Your Question failed. Please ask your question again", Toast.LENGTH_LONG).show();
            }

        }//end of onPostExecute
    }//end pf AskAsyn


}
