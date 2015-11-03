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
import android.widget.ListView;

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


public class UnsolvedTask extends AsyncTask<String,Void,ArrayList<String>> {

    ArrayAdapter<String> myAdapter;
    private ArrayList<String> getWeatherDataFromJson() {
        ArrayList<String> resultStrs = new ArrayList<>();
        while (SearchableActivity.finished == false);
        if(SearchableActivity.unsolvedResults.size() == 0)
            return null;
        for (String element:
                SearchableActivity.unsolvedResults) {
            resultStrs.add(element);
        }
        return resultStrs;
    }

    @Override
    protected void onPreExecute() {
        if(myAdapter != null)
            myAdapter.clear();
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {

        try {
            ArrayList<String> result = getWeatherDataFromJson();
            return result;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        if(strings != null){
            ListView listView = (ListView)UnsolvedFragment.rootView.findViewById(R.id.listView_unsolved);
            myAdapter = new ArrayAdapter<String>(UnsolvedFragment.rootView.getContext(),
                    R.layout.list_item_unsolved,
                    R.id.list_item_unsolved_textView,
                    strings);
            listView.setAdapter(myAdapter);

            UnsolvedFragment.unsolvedText.setVisibility(View.INVISIBLE);
            //AllFragment.myAdapter.clear();
            //for (String result:strings) {
            //AllFragment.myAdapter.add(result);
            // }
        }
        else {
            if(myAdapter != null)
                myAdapter.clear();
            UnsolvedFragment.unsolvedText.setText("No matched Answer found");
        }
    }
}

