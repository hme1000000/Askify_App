package app.com.example.hussein.askify_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    String username;
    String user_id;
    String id ; //question_id returned from json
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
    String My_Questions_URL = "http://askify-app.herokuapp.com/public/api/notificationsToUser/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationActivity.this, NotificationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", HomeActivity.userID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Bundle b = getIntent().getExtras();
        if(b != null)
            username = b.getString("user_id");
        //else
            username= HomeActivity.userID;
        qList = new ArrayList<Map<String, String>>();
        new QuesAsyn().execute();
    }


    private class QuesAsyn extends AsyncTask<String,Void,ArrayList<Map<String, String>>> {

        private final String LOG_TAG = QuesAsyn.class.getSimpleName();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NotificationActivity.this);
            pDialog.setMessage("Loading Questions...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected ArrayList<Map<String, String>> doInBackground(String... args) {
            //My_Questions_URL+=username;
            String forecastJsonStr;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                String urlString = My_Questions_URL+"12";
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return new ArrayList<>();
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return new ArrayList<>();
                }
                forecastJsonStr = buffer.toString();

            } catch (IOException e) {
                return new ArrayList<>();
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            // = jP.makeHttpRequest(My_Questions_URL);
            try {

                if(forecastJsonStr == null)
                    return qList;

                JSONObject forecast = new JSONObject(forecastJsonStr);
                jsonMainNode = forecast.getJSONArray("question_List");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    ques = jsonChildNode.getString("question");
                    ans = jsonChildNode.getString("Answer");
                    id = jsonChildNode.getString("questioner_name");
                    ques_date=jsonChildNode.getString("question_date");
                    ans_date=jsonChildNode.getString("Answer_date");
                    solved= jsonChildNode.getString("solved");
                    HashMap<String, String> contact = new HashMap<String, String>();
                    contact.put("questioner_name", id);
                    contact.put("question", ques);
                    contact.put("Answer",ans);
                    contact.put("Answer_date",ans_date);
                    contact.put("question_date",ques_date);
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

            ArrayList<String> arrayList = new ArrayList<>();
            ListView listView1 =(ListView) findViewById(R.id.listView_notification);
            TextView notfound = (TextView)findViewById(R.id.notification_textView);
            if(x.size() != 0) {
                listView1.setVisibility(View.VISIBLE);
                notfound.setVisibility(View.INVISIBLE);
                ArrayAdapter simpleAdapter = new ArrayAdapter(NotificationActivity.this,
                        R.layout.list_item_nitification,
                        R.id.notification, arrayList);
                for (Map<String, String> element :
                        x) {
                    simpleAdapter.add("A New Question Answered :" + element.get("question"));
                }

                listView1.setAdapter(simpleAdapter);

                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long _id) {
                        Intent i = new Intent(NotificationActivity.this, ViewQuestion.class);
                        Map<String, String> mq = qList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putString("questioner_name",mq.get("questioner_name"));
                        bundle.putString("question",mq.get("question"));
                        bundle.putString("question_tag",mq.get("question_tag"));
                        bundle.putString("answer",mq.get("Answer"));
                        bundle.putString("user_id",username);
                        bundle.putString("question_date",mq.get("question_date"));
                        bundle.putString("answer_date",mq.get("Answer_date"));

                        i.putExtras(bundle);
                        startActivity(i);


                    }
                });
            }
            else {
                listView1.setVisibility(View.INVISIBLE);

                notfound.setText("No New Notifications");
                notfound.setVisibility(View.VISIBLE);
            }
        }


    }


}
