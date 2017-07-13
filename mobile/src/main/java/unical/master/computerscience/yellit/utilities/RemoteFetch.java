package unical.master.computerscience.yellit.utilities;

/**
 * Created by Altair07 on 17/05/2017.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;
import android.os.Debug;
import android.util.Log;

import unical.master.computerscience.yellit.R;

public class RemoteFetch {

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&units=metric";

    public static JSONObject getJSON(Context context, String city) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if (data.getInt("cod") != 200) {
                return null;
            }

            return data;
        } catch (Exception e) {
            return null;
        }
    }

    public static JSONObject getData(JSONObject json)
    {
        JSONObject data = new JSONObject();
        try {
            json.getJSONObject("city").getString("name").toUpperCase(Locale.US);
            json.getJSONObject("city").getString("country");
            for (int i = 0; i != json.getJSONArray("list").length(); i++) {
                JSONObject details = json.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0);
                JSONObject temperature = json.getJSONArray("list").getJSONObject(i).getJSONObject("temp");

                if (i == 0) {
                    data.accumulate("temperature", temperature.getDouble("day"));
                    data.accumulate("weather", details.get("description"));
                }
            }            /*
            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);*/

        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }

        return data;
    }

    public static double renderWeather(JSONObject json) {
        double t = 0;
        try {
            json.getJSONObject("city").getString("name").toUpperCase(Locale.US);
            json.getJSONObject("city").getString("country");
            for (int i = 0; i != json.getJSONArray("list").length(); i++) {
                JSONObject details = json.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0);
                JSONObject temperature = json.getJSONArray("list").getJSONObject(i).getJSONObject("temp");

                details.getString("description").toUpperCase(Locale.US);

                Log.e("varie",
                        details.getString("description").toUpperCase(Locale.US)/* +
                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
                            "\n" + "Pressure: " + main.getString("pressure") + " hPa"*/);

                Log.e("TEMPERATURA MEDIA: ", temperature.getDouble("day") + " ℃");
                if (i == 0)
                    t = temperature.getDouble("day");
            }            /*
            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);*/

        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
        return t;
    }
}