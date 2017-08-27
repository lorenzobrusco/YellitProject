package unical.master.computerscience.yellit.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

import unical.master.computerscience.yellit.R;

public class RemoteFetch {

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&units=metric";

    /**
     * Get json from open weather
     * @param context
     * @param city
     * @return
     */
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

            if (data.getInt("cod") != 200) {
                return null;
            }

            return data;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * get usefful data from above json
     * @param json
     * @return
     */
    public static JSONObject getData(JSONObject json) {
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
            }

        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }

        return data;
    }

}