package app.com.example.hussein.askify_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.format.Time;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by hussein on 19/10/15.
 */
public class AllTask extends AsyncTask<String,Void,ArrayList<String>> {
    private final String Log_Tag = myTask.class.getSimpleName();

    String forecastJsonresult = null;

    Context context;


    /* The date/time conversion code is going to be moved outside the asynctask later,
 * so for convenience we're breaking it out into its own method now.
 */
    private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    public String formatHighLows(double high, double low,String type) {


        if(type.equals("imperial")){
            high = (high* 1.8)+32;
            low = (low * 1.8)+32;
        }

        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private ArrayList<String> getWeatherDataFromJson(String forecastJsonStr, int numDays,String city,String type,String keyword)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "main";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.

        Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        // now we work exclusively in UTC
        dayTime = new Time();

        String[] resultStrs = new String[numDays];
        ArrayList<String> searchresult = new ArrayList<>();
        for(int i = 0; i < weatherArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"
            String day;
            String description;
            String highAndLow;

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".
            long dateTime;
            // Cheating to convert this to UTC time, which is what we want anyhow
            dateTime = dayTime.setJulianDay(julianStartDay+i);
            day = getReadableDateString(dateTime);

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);

            highAndLow = formatHighLows(high, low,type);
            resultStrs[i] = city+" - "+day + " - " + description + " - " + highAndLow;
        }

        for (String element:resultStrs
                ) {if(element.contains(keyword))

            searchresult.add(element);

        }


        return searchresult;

    }


    @Override
    protected ArrayList<String> doInBackground(String... params) {
        if(params.length == 0)
            return null;
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.


        String format = "json";
        String units = "metric";
        int days = 14;
        String key = "1c0c481674277948e7d4187513d3bc5b";

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&APPID=1c0c481674277948e7d4187513d3bc5b&mode=json&units=metric&cnt=7");
//URL url = new URL("https://randomuser.me/api/");
            String urlString = "http://openweathermap.org/data/2.5/forecast/daily?";
            Uri urlBuild = Uri.parse(urlString).buildUpon()
                    .appendQueryParameter("q",params[0])
                    .appendQueryParameter("APPID",key)
                    .appendQueryParameter("mode",format)
                    .appendQueryParameter("units",units)
                    .appendQueryParameter("cnt",Integer.toString(days))
                    .build();
            URL url = new URL(urlBuild.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            int statusCode = urlConnection.getResponseCode();
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonresult = buffer.toString();

        } catch (IOException e) {
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
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
            ArrayList<String> result = getWeatherDataFromJson(forecastJsonresult,days,params[0],params[1],params[2]);
            return result;
        }
        catch (JSONException e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPreExecute() {
       // AllFragment.myAdapter.clear();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        //AllFragment.myAdapter.clear();
        //AllFragment.allText.setText("Loading...");
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        if(strings != null){
            SearchableActivity.allSearch.clear();
            for (String result:strings) {
                SearchableActivity.allSearch.add(result);
            }
        }
        else {
            AllFragment.myAdapter.clear();
            for(int i=0;i<10;i++)
            {
                AllFragment.myAdapter.add("Nothing");
            }
        }
    }
}

