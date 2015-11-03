package app.com.example.hussein.askify_app;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class My_Questions extends ActionBarActivity {

    String user_id;
    String id; //question_id returned from json
    String ques_tag=""; //question_tag returned from json
    String ques; //question returned from json
    String ans; //answer returned form json
    String ques_date; //question_date returned from json
    String ans_date; //question_date returned from json
    String solved;//solved returned from json

    JSONArray jsonMainNode=null;
    JSONArray jsonTag =null; //question_tag may be returned array
    private ProgressDialog pDialog;
    ArrayList<Map<String, String>> qList;
    String My_Questions_URL = "http://askify-app.herokuapp.com/public/api/questionlist/view/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__questions);
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

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_custom_view_home);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setTitle(Html.fromHtml("<font color=\"#111111\">" + getString(R.string.app_name) + "</font>"));

        Bundle b = getIntent().getExtras();
        user_id = b.getString("user_id");
        qList = new ArrayList<Map<String, String>>();
        new QuesAsyn().execute();
    }



    private class QuesAsyn extends AsyncTask<String,Void,ArrayList<Map<String, String>>> {

        private final String LOG_TAG = QuesAsyn.class.getSimpleName();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(My_Questions.this);
            pDialog.setMessage("Loading Questions...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected ArrayList<Map<String, String>> doInBackground(String... args) {
            My_Questions_URL+=user_id;
            JSONParser jP = new JSONParser();
            String forecastJsonStr = jP.makeHttpRequest(My_Questions_URL);
            try {

                JSONObject forecast = new JSONObject(forecastJsonStr);
                jsonMainNode = forecast.getJSONArray("my_questions");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    ques = jsonChildNode.getString("question");
                    ans = jsonChildNode.getString("Answer");
                    id = jsonChildNode.getString("question_id");
                    ques_date=jsonChildNode.getString("question_date");
                    ans_date=jsonChildNode.getString("Answer_date");
                    solved= jsonChildNode.getString("solved");
                    HashMap<String, String> contact = new HashMap<String, String>();
                    contact.put("question_tag", ques_tag);
                    contact.put("question", ques);
                    contact.put("question_id",id);
                    contact.put("Answer",ans);
                    contact.put("solved",solved);
                    contact.put("question_date",ques_date);
                    contact.put("Answer_date",ans_date);
                    jsonTag = jsonChildNode.getJSONArray("question_tag");
                    for (int j = 0; j < jsonTag.length(); j++) {
                        ques_tag += "#"+jsonTag.getString(j);
                    }

                    contact.put("question_tag", ques_tag);
                    qList.add(contact);
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return qList;
        }

        @Override
        protected void onPostExecute(ArrayList<Map<String, String>> x){
            pDialog.dismiss();
            ListAdapter simpleAdapter = new SimpleAdapter(My_Questions.this, x, R.layout.my_list_item_question, new String[]{"question"
                    , "Answer"}, new int[]{R.id.myquestion
                    ,R.id.myanswer});
            ListView listView1 =(ListView) findViewById(R.id.my_ques_list);
            listView1.setAdapter(simpleAdapter);

            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long _id) {
                    Intent i = new Intent(My_Questions.this,ViewMyQuestion.class);
                    Map<String,String> mq = qList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("question_id",mq.get("question_id"));
                    bundle.putString("user_id",user_id);
                    bundle.putString("question_date",mq.get("question_date"));
                    bundle.putString("answer_date",mq.get("Answer_date"));
                    bundle.putString("question",mq.get("question"));
                    bundle.putString("question_tag",mq.get("question_tag"));
                    bundle.putString("answer",mq.get("Answer"));
                    bundle.putString("solved",mq.get("solved"));

                    i.putExtras(bundle);
                    startActivity(i);


                }
            });
        }


    }

}
