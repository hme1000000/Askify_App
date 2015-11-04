package app.com.example.hussein.askify_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;


public class QuestionsTask extends AsyncTask<String,Void,ArrayList<Map<String, String>>> {

    ListAdapter myAdapter;
    private ArrayList<Map<String, String>> getWeatherDataFromJson() {
        ArrayList<Map<String, String>> resultStrs = new ArrayList<Map<String, String>>();
        while (SearchableActivity.finished == false);
        if(SearchableActivity.questionData.size() == 0)
            return null;
        for (Map<String,String> element:
                SearchableActivity.questionData) {
            resultStrs.add(element);
        }
        return resultStrs;
    }



    @Override
    protected ArrayList<Map<String, String>> doInBackground(String... params) {

        try {
            ArrayList<Map<String, String>> result = getWeatherDataFromJson();
            return result;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(ArrayList<Map<String, String>> strings) {
        ListView listView = (ListView)QuestionFragment.rootView.findViewById(R.id.listView_question);
        if(strings != null){

            myAdapter = new SimpleAdapter(QuestionFragment.rootView.getContext(),strings,
                    R.layout.list_item_homequestion, new String[]{"question",
                    "questioner_name", "Answer"}, new int[]{R.id.question,
                    R.id.name,R.id.answer});
            listView.setAdapter(myAdapter);

            QuestionFragment.questionText.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            //AllFragment.myAdapter.clear();
            //for (String result:strings) {
            //AllFragment.myAdapter.add(result);
            // }
        }
        else {
            listView.setVisibility(View.INVISIBLE);
            QuestionFragment.questionText.setVisibility(View.VISIBLE);
            QuestionFragment.questionText.setText("No matched Question found");
        }
    }
}

