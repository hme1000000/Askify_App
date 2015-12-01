package app.com.example.hussein.askify_app;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by hussein on 01/12/15.
 */
public class LoginTask extends AsyncTask<String,Void,String>  {
    private final String Log_Tag = myTask.class.getSimpleName();
    public static int user_id = 0;


    private String getAnswerFromJson(String forecastJsonStr)
            throws JSONException {
        JSONObject result = new JSONObject(forecastJsonStr);

        int resultint = result.getInt("success");
        if(resultint == 1)
        {
            user_id = result.getInt("user_id");
            HomeActivity.userID = String.valueOf(result.getInt("user_id"));
            Log.e("ID","ID+++++++++++++"+HomeActivity.userID);
            return "success";
        }
        else {
            return "failure";
        }
    }



    @Override
    protected String doInBackground(String... params) {
        String urlresult = "";
        if(params.length == 0)
            return "Failure";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            String urlString = "http://askify-app.herokuapp.com/public/api/user/login";
            urlString += "?username="+params[0]+"&password="+params[1];
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
            String result =getAnswerFromJson(urlresult);
            if(result.equals("success"))
            {
                MainActivityFragment.success = 1;
            }
            else
            {
                MainActivityFragment.success = 0;
            }
            MainActivityFragment.finish = true;
            return result;
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return "Failure";



    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("success"))
        {
            MainActivityFragment.success = 1;
        }
        else
        {
            MainActivityFragment.success = 0;
        }
        MainActivityFragment.finish = true;
    }
}




