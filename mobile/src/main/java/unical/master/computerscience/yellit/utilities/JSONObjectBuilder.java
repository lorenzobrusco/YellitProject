package unical.master.computerscience.yellit.utilities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lorenzo on 21/03/2017.
 */

public class JSONObjectBuilder<T> {

    public static JSONObject jsonObject;

    public static JSONObject builder() {
        jsonObject = new JSONObject();
        return jsonObject;
    }

    public JSONObject put(String label, T data) {
        try {
            jsonObject.put(label, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
