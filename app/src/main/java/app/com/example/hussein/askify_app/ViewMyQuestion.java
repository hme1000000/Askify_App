package app.com.example.hussein.askify_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class ViewMyQuestion extends AppCompatActivity implements View.OnClickListener {

    TextView q,q_date,a,a_date,q_tag;
    Button q_edit, q_delete;
    String user_id;
    String ques_id;
    String ques;
    String solved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewMyQuestion.this, NotificationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", HomeActivity.userID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        q=(TextView)findViewById(R.id.Ques);
        q_date=(TextView)findViewById(R.id.q_date);
        a=(TextView)findViewById(R.id.Ans);
        a_date=(TextView)findViewById(R.id.a_date);
        q_tag=(TextView)findViewById(R.id.tag);

        Bundle bundle = getIntent().getExtras();
        ques = bundle.getString("question");
        String ques_tag = bundle.getString("question_tag");
        String ans = bundle.getString("answer");
        String ques_date = bundle.getString("question_date");
        String ans_date = bundle.getString("answer_date");
        ques_id = bundle.getString("question_id");
        user_id=HomeActivity.userID;
        solved=bundle.getString("solved");

        a.setText(ans);
        q_tag.setText(ques_tag);
        q.setText(ques);
        q_date.setText(ques_date);
        if(ans.equals("No Answer")){
        }
        else{a_date.setText(ans_date);}

        q_edit =(Button)findViewById(R.id.edit_ques);
        q_edit.setOnClickListener(this);

        q_delete =(Button)findViewById(R.id.delete_ques);
        q_delete.setOnClickListener(this);

    }//end of onCreate

    ////////////////////////////////////////////*onClick*/////////////////////////////////////////
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edit_ques) {
            Intent i = new Intent(this, EditQuestion.class);
            Bundle qbundle = new Bundle();
            qbundle.putString("question_id", ques_id);
            qbundle.putString("question", ques);
            qbundle.putString("user_id", user_id);
            qbundle.putString("solved",solved);
            i.putExtras(qbundle);
            startActivity(i);
        } else if (v.getId() == R.id.delete_ques) {
            new deleteAsync().execute();
            Intent j = new Intent(this, My_Questions.class);
            Bundle x = new Bundle();
            x.putString("user_id",user_id);
            j.putExtras(x);
            startActivity(j);

        }
    }//end of onClick


    ////////////////////////////////////////////*deleteAsync*////////////////////////////////////////////////
    private class deleteAsync extends AsyncTask<String, String, String> {
        String Delete_URL= "http://askify-app.herokuapp.com/public/api/question/delete";
        String deleteJsonStr;
        String postDataBytes;
        String failure;
        @Override
        protected String doInBackground(String... args) {
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("user_id", user_id);
            params.put("question_id", ques_id);
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                try {
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    postDataBytes = postData.toString();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            try {
                JSONParser jP = new JSONParser();
                deleteJsonStr = jP.excutePost(Delete_URL, postDataBytes);
                JSONObject x = new JSONObject(deleteJsonStr);
                failure = x.getString("error");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return failure;


        }//end of doInBackground

        @Override
        protected void onPostExecute(String error) {
            if (error.equals("false")) {
                Toast.makeText(ViewMyQuestion.this, "Your Question has been deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ViewMyQuestion.this, "Your Question has not been deleted . Please delete your question again", Toast.LENGTH_LONG).show();
            }
        }//end of onPostExecute
    }//end of deleteAsyn




}
