package app.com.example.hussein.askify_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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


public class UserTask extends AsyncTask<String,Void,ArrayList<Map<String, String>>> {

    //ArrayAdapter<String> myAdapter;
    ListAdapter allAdapter;
    private ArrayList<Map<String, String>> getWeatherDataFromJson() {
        ArrayList<Map<String, String>> resultStrs = new ArrayList<Map<String, String>>();
        while (SearchableActivity.finished == false);
        if(SearchableActivity.usersData.size() == 0)
            return null;
        for (Map<String,String> element:
                SearchableActivity.usersData) {
            resultStrs.add(element);
        }
        return resultStrs;
    }

    /*@Override
    protected void onPreExecute() {
        if(myAdapter != null)
            myAdapter.clear();
    }*/

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
        ListView listView = (ListView)UsersFragment.rootView.findViewById(R.id.listView_users);
        if(strings != null){
            //ListView listView = (ListView)AnswerFragment.rootView.findViewById(R.id.listView_answer);
            /*myAdapter = new ArrayAdapter<String>(AnswerFragment.rootView.getContext(),
                    R.layout.list_item_answer,
                    R.id.list_item_answer_textView,
                    strings);*/
            allAdapter = new SimpleAdapter(UsersFragment.rootView.getContext(),strings,
                    R.layout.list_item_users, new String[]{"question",
                    "questioner_name", "Answer"}, new int[]{R.id.questionUsers,
                    R.id.nameUsers,R.id.answerUsers});
            listView.setAdapter(allAdapter);
            UsersFragment.usersText.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            //listView.setVisibility(View.VISIBLE);
            //AllFragment.myAdapter.clear();
            //for (String result:strings) {
            //AllFragment.myAdapter.add(result);
            // }
        }
        else {
            listView.setVisibility(View.INVISIBLE);
            UsersFragment.usersText.setVisibility(View.VISIBLE);
            //listView.setVisibility(View.INVISIBLE);
            UsersFragment.usersText.setText("No matched Users found");
        }
    }
}

