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
import java.util.HashMap;

/**
 * Created by hussein on 19/10/15.-
 */
public class SearchTask extends AsyncTask<String,Void,String> {
    private final String Log_Tag = myTask.class.getSimpleName();
    String urlresult = null;
    public static String quesName;

    private void getAnswerDataFromJson(String forecastJsonStr,String keyword)
            throws JSONException {
        if(SearchableActivity.answerResults != null)
            SearchableActivity.answerResults.clear();
        if(SearchableActivity.answerData != null)
            SearchableActivity.answerData.clear();
        JSONObject Answersjson = new JSONObject(forecastJsonStr);
        JSONArray AnswersjsonArray = Answersjson.getJSONArray("question_List");
        if(AnswersjsonArray.length() == 0){
            SearchableActivity.answerResults = new ArrayList<>();
            SearchableActivity.answerData = new ArrayList<>();
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
            HashMap<String, String> contact = new HashMap<String, String>();
            contact.put("questioner_name", questioner_name);
            contact.put("question", question);
            contact.put("Answer",Answer);
            contact.put("Answer_date",Answer_date);
            contact.put("question_date", question_date);
            JSONArray jsonTag = AnswerObject.getJSONArray("question_tag");
            String Tag = "";
            for (int j = 0; j < jsonTag.length(); j++) {
                Tag+= "#"+jsonTag.getString(j);
            }

            contact.put("question_tag",Tag);
            SearchableActivity.answerData.add(contact);
            SearchableActivity.answerResults.add("[Answer]\n\n" + questioner_name
                    + " asked: \n" + question + " " + "(" + question_date + ")\n\nAnswer: "
                    + Answer + " (" + Answer_date + ")\n");
        }
    }

    private void getUsersDataFromJson(String forecastJsonStr,String keyword)
            throws JSONException {
        if(SearchableActivity.usersResults != null)
            SearchableActivity.usersResults.clear();
        if(SearchableActivity.usersData != null)
            SearchableActivity.usersData.clear();
        JSONObject Usersjson = new JSONObject(forecastJsonStr);
        JSONArray UsersjsonArray = Usersjson.getJSONArray("question_List");
        if(UsersjsonArray.length() == 0){
            SearchableActivity.usersResults = new ArrayList<>();
            SearchableActivity.usersData = new ArrayList<>();
            return;
        }
        for(int i = 0; i < UsersjsonArray.length(); i++) {
            JSONObject AnswerObject = UsersjsonArray.getJSONObject(i);
            String questioner_name = AnswerObject.getString("questioner_name");
            String question = AnswerObject.getString("question");
            String question_date = AnswerObject.getString("question_date");
            String Answer = AnswerObject.getString("Answer");
            String Answer_date = AnswerObject.getString("Answer_date");
            String solved = AnswerObject.getString("solved");
            HashMap<String, String> contact = new HashMap<String, String>();
            contact.put("questioner_name", questioner_name);
            contact.put("question", question);
            contact.put("Answer",Answer);
            contact.put("Answer_date",Answer_date);
            contact.put("question_date", question_date);
            JSONArray jsonTag = AnswerObject.getJSONArray("question_tag");
            String Tag = "";
            for (int j = 0; j < jsonTag.length(); j++) {
                Tag+= "#"+jsonTag.getString(j);
            }

            contact.put("question_tag",Tag);
            SearchableActivity.usersData.add(contact);
            SearchableActivity.usersResults.add("[User]\n\n"+ questioner_name+"\n");
        }
    }

    private void getQuestionDataFromJson(String forecastJsonStr,String keyword)
            throws JSONException {
        if(SearchableActivity.questionResults != null)
            SearchableActivity.questionResults.clear();
        if(SearchableActivity.questionData != null)
            SearchableActivity.questionData.clear();
        JSONObject Questionsjson = new JSONObject(forecastJsonStr);
        JSONArray QuestionsjsonArray = Questionsjson.getJSONArray("question_List");
        if(QuestionsjsonArray.length() == 0){
            SearchableActivity.questionResults = new ArrayList<>();
            SearchableActivity.questionData = new ArrayList<>();
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
            HashMap<String, String> contact = new HashMap<String, String>();
            contact.put("questioner_name", questioner_name);
            contact.put("question", question);
            contact.put("Answer",Answer);
            contact.put("Answer_date",Answer_date);
            contact.put("question_date", question_date);
            JSONArray jsonTag = AnswerObject.getJSONArray("question_tag");
            String Tag = "";
            for (int j = 0; j < jsonTag.length(); j++) {
                Tag+= "#"+jsonTag.getString(j);
            }

            contact.put("question_tag",Tag);
            SearchableActivity.questionData.add(contact);
            SearchableActivity.questionResults.add("[Question]\n\n"+ questioner_name
                    + " asked: \n" + question + " " + "(" + question_date + ")\n\nAnswer: "
                    + Answer + " (" + Answer_date + ")\n");
        }
    }

    private void getUnsolvedDataFromJson(String forecastJsonStr,String keyword)
            throws JSONException {
        if(SearchableActivity.unsolvedResults != null)
            SearchableActivity.unsolvedResults.clear();
        if(SearchableActivity.unsolvedData != null)
            SearchableActivity.unsolvedData.clear();
        JSONObject Answersjson = new JSONObject(forecastJsonStr);
        JSONArray AnswersjsonArray = Answersjson.getJSONArray("question_List");
        if(AnswersjsonArray.length() == 0){
            SearchableActivity.unsolvedResults = new ArrayList<>();
            SearchableActivity.unsolvedData = new ArrayList<>();
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
            if(solved.equals("false")) {
                HashMap<String, String> contact = new HashMap<String, String>();
                contact.put("questioner_name", questioner_name);
                contact.put("question", question);
                contact.put("Answer",Answer);
                contact.put("Answer_date",Answer_date);
                contact.put("question_date", question_date);
                JSONArray jsonTag = AnswerObject.getJSONArray("question_tag");
                String Tag = "";
                for (int j = 0; j < jsonTag.length(); j++) {
                    Tag+= "#"+jsonTag.getString(j);
                }

                contact.put("question_tag",Tag);
                SearchableActivity.unsolvedData.add(contact);
                SearchableActivity.unsolvedResults.add("[Unsolved Questions]\n\n" + questioner_name
                        + " asked: \n" + question + " " + "(" + question_date + ")\n");
            }
        }
        if(SearchableActivity.unsolvedResults.size() == 0)
            SearchableActivity.unsolvedResults = new ArrayList<>();
        if(SearchableActivity.unsolvedData.size() == 0)
            SearchableActivity.unsolvedData = new ArrayList<>();

    }

    private void getAllDataFromJson(){
        if(SearchableActivity.allResults != null)
            SearchableActivity.allResults.clear();
        if(SearchableActivity.allData != null)
            SearchableActivity.allData.clear();


        for (int i =0;i<SearchableActivity.usersResults.size();i++) {
            SearchableActivity.allResults.add(SearchableActivity.usersResults.get(i));
            SearchableActivity.allData.add(SearchableActivity.usersData.get(i));
        }
        for (int i =0;i<SearchableActivity.questionResults.size();i++) {
            SearchableActivity.allResults.add(SearchableActivity.questionResults.get(i));
            SearchableActivity.allData.add(SearchableActivity.questionData.get(i));
        }

        for (int i =0;i<SearchableActivity.answerResults.size();i++) {
            SearchableActivity.allResults.add(SearchableActivity.answerResults.get(i));
            SearchableActivity.allData.add(SearchableActivity.answerData.get(i));
        }
        if(SearchableActivity.allResults.size() == 0)
            SearchableActivity.allResults = new ArrayList<>();
        if(SearchableActivity.allData.size() == 0)
            SearchableActivity.allData = new ArrayList<>();
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
            getAnswerDataFromJson(urlresult, params[0]);
        }
        catch (JSONException e){
            e.printStackTrace();
            return "Failure";
        }


        try {
            String urlString = "http://askify-app.herokuapp.com/public/api/search/user/"+params[0];
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
            getUsersDataFromJson(urlresult, params[0]);
        }
        catch (JSONException e){
            e.printStackTrace();
            return "Failure";
        }

        try {
            String urlString = "http://askify-app.herokuapp.com/public/api/search/question/"+params[0];
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
            getQuestionDataFromJson(urlresult,params[0]);
            getUnsolvedDataFromJson(urlresult,params[0]);
        }
        catch (JSONException e){
            e.printStackTrace();
            return "Failure";
        }

        try {
            getAllDataFromJson();
            return "Success";
        }
        catch (Exception e){
            e.printStackTrace();
            return "Failure";
        }


    }

    @Override
    protected void onPostExecute(String result) {
        SearchableActivity.finished = true;
    }
}


