package unical.master.computerscience.yellit.utiliies;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Lorenzo on 17/03/2017.
 */

public class YellitRestClient {

    private static AsyncHttpClient mClient = new AsyncHttpClient();

    public static void doGet(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mClient.get(BaseURL.URL + url, params, responseHandler);
    }

    public static void doPost(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mClient.post(BaseURL.URL + url, params, responseHandler);
    }

}
