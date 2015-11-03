package app.com.example.hussein.askify_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class EditQuestion extends AppCompatActivity implements View.OnClickListener{

    EditText ask_text;
    String ask_result;
    Button edit;
    String ques_id, ques;
    String user_id;
    String Private; //private/public to be sent
    String solved;
    String EditedQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
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

        Bundle bundle = getIntent().getExtras();
        ques_id = bundle.getString("question_id");
        ques = bundle.getString("question");
        user_id = bundle.getString("user_id");
        solved=bundle.getString("solved");


        ask_text = (EditText) findViewById(R.id.Edit_text);
        ask_text.setText(ques);
        ask_result = ask_text.getText().toString();

        edit = (Button) findViewById(R.id.Edit_button);
        edit.setOnClickListener(this);

    }//end of onCreate

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Edit_button) {

            new EditAsync().execute();
       /*   Intent j = new Intent(this, My_Questions.class);
            Bundle x = new Bundle();
            x.putString("user_id",user_id);
            j.putExtras(x);
            startActivity(j);
        */}
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

//////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////*AskAsync*////////////////////////////////////////////////
    private class EditAsync extends AsyncTask<String, String, String> {
        String EditJsonStr;
        String postDataBytes;
        String Edit_URL = "http://askify-app.herokuapp.com/public/api/question/edit";
        String error;
        @Override
        protected String doInBackground(String... args) {
            EditedQuestion = ask_result;
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("user_id", user_id);
            params.put("question", EditedQuestion);
            params.put("question_id", ques_id);
            params.put("private", Private);
            params.put("solved", solved);
            StringBuilder Data = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (Data.length() != 0) Data.append('&');
                try {
                    Data.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    Data.append('=');
                    Data.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    postDataBytes = Data.toString();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }//end of for
            try {
                JSONParser jP1 = new JSONParser();
                EditJsonStr = jP1.excutePost(Edit_URL, postDataBytes);
                JSONObject forecast = new JSONObject(EditJsonStr);
                error = forecast.getString("error");
            } catch (JSONException e) {e.printStackTrace();}

            return postDataBytes;
        }//end of doInBackground

        @Override
        protected void onPostExecute (String error){
            Toast.makeText(EditQuestion.this,error, Toast.LENGTH_LONG).show();
                /*if (error.equals("false")) {
                    Toast.makeText(EditQuestion.this,"Your Question has been edited successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditQuestion.this,"Your Question failed. Please edit your question again", Toast.LENGTH_LONG).show();
                }*/
        }//end of onPostExecute

    }//end of EditAsync



}
