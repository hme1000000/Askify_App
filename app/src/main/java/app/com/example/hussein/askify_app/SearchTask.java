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

/**
 * Created by hussein on 19/10/15.
 */
public class SearchTask extends AsyncTask<String,Void,String> {
    private final String Log_Tag = myTask.class.getSimpleName();
    String urlresult = null;

    private void getAnswerDataFromJson(String forecastJsonStr,String keyword)
            throws JSONException {
        SearchableActivity.answerResults.clear();
        JSONObject Answersjson = new JSONObject(forecastJsonStr);
        JSONArray AnswersjsonArray = Answersjson.getJSONArray("question_List");
        if(AnswersjsonArray.length() == 0){
            SearchableActivity.answerResults = null;
            return;
        }
        for(int i = 0; i < AnswersjsonArray.length(); i++) {
            JSONObject AnswerObject = AnswersjsonArray.getJSONObject(i);
            String questioner_name = AnswerObject.getString("questioner_name");
            String question = AnswerObject.getString("question");
            String question_date = AnswerObject.getString("question_date");
            String Answer = AnswerObject.getString("Answer");
            String Answer_date = AnswerObject.getString("Answer_date");
            String solved = AnswerObject.getString("solved");
            if(solved.equals("false"))
            {
                Answer = "No Answer yet";
            }
            SearchableActivity.answerResults.add("[Answer]\n\n" + questioner_name
                    + " asked: \n\n" + question + " " + "(" + question_date + ")\n\nAnswer: "
                    + Answer + " (" + Answer_date + ")");
        }
    }

    private void getUsersDataFromJson(String forecastJsonStr,String keyword)
            throws JSONException {
        SearchableActivity.usersResults.clear();
        JSONObject Usersjson = new JSONObject(forecastJsonStr);
        JSONArray UsersjsonArray = Usersjson.getJSONArray("question_List");
        if(UsersjsonArray.length() == 0){
            SearchableActivity.answerResults = null;
            return;
        }
        for(int i = 0; i < UsersjsonArray.length(); i++) {
            JSONObject AnswerObject = UsersjsonArray.getJSONObject(i);
            String questioner_name = AnswerObject.getString("questioner_name");
            SearchableActivity.usersResults.add("[User]\n\n"+ questioner_name);
        }
    }

    private void getQuestionDataFromJson(String forecastJsonStr,String keyword)
            throws JSONException {
        SearchableActivity.questionResults.clear();
        JSONObject Questionsjson = new JSONObject(forecastJsonStr);
        JSONArray QuestionsjsonArray = Questionsjson.getJSONArray("question_List");
        if(QuestionsjsonArray.length() == 0){
            SearchableActivity.questionResults = null;
            return;
        }
        for(int i = 0; i < QuestionsjsonArray.length(); i++) {
            JSONObject AnswerObject = QuestionsjsonArray.getJSONObject(i);
            String questioner_name = AnswerObject.getString("questioner_name");
            String question = AnswerObject.getString("question");
            String question_date = AnswerObject.getString("question_date");
            String Answer = AnswerObject.getString("Answer");
            String Answer_date = AnswerObject.getString("Answer_date");
            String solved = AnswerObject.getString("solved");
            if(solved.equals("false"))
            {
                Answer = "No Answer yet";
            }
            SearchableActivity.questionResults.add("[Question]\n\n"+ questioner_name
                    + " asked: \n\n" + question + " " + "(" + question_date + ")\n\nAnswer: "
                    + Answer + " (" + Answer_date + ")");
        }
    }

    private void getUnsolvedDataFromJson(String forecastJsonStr,String keyword)
            throws JSONException {
        SearchableActivity.unsolvedResults.clear();
        JSONObject Answersjson = new JSONObject(forecastJsonStr);
        JSONArray AnswersjsonArray = Answersjson.getJSONArray("question_List");
        if(AnswersjsonArray.length() == 0){
            SearchableActivity.unsolvedResults = null;
            return;
        }
        for(int i = 0; i < AnswersjsonArray.length(); i++) {
            JSONObject AnswerObject = AnswersjsonArray.getJSONObject(i);
            String questioner_name = AnswerObject.getString("questioner_name");
            String question = AnswerObject.getString("question");
            String question_date = AnswerObject.getString("question_date");
            String solved = AnswerObject.getString("solved");
            if(solved.equals("false")) {
                SearchableActivity.unsolvedResults.add("[Unsolved Questions]\n\n" + questioner_name
                        + " asked: \n" + question + " " + "(" + question_date + ")");
            }
        }
        if(SearchableActivity.unsolvedResults.size() == 0)
            SearchableActivity.unsolvedResults = null;
    }

    private void getAllDataFromJson(){
        SearchableActivity.allResults.clear();
        for (String element:
             SearchableActivity.usersResults) {
            SearchableActivity.allResults.equals(element);
        }
        for (String element:
                SearchableActivity.questionResults) {
            SearchableActivity.allResults.equals(element);
        }
        for (String element:
                SearchableActivity.answerResults) {
            SearchableActivity.allResults.equals(element);
        }
        if(SearchableActivity.allResults.size() == 0)
            SearchableActivity.allResults = null;
    }




    @Override
    protected String doInBackground(String... params) {
        if(params.length == 0)
            return "Failure";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            String urlString = "http://askify-app.herokuapp.com/public/api/search/answer/"+params[0];
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return "Failure";
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return "Failure";
            }
            urlresult = buffer.toString();

        } catch (IOException e) {
            return "Failure";
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(Log_Tag, "Error closing stream", e);
                }
            }
        }
        try {
            getAnswerDataFromJson(urlresult,params[0]);
            return "Success";
        }
        catch (JSONException e){
            e.printStackTrace();
            return "Failure";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        SearchableActivity.finished = true;
    }
}


