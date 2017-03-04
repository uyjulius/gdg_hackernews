package hackernews.android.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by viki on 5/29/13.
 */
public class VolleyManager {

    private static final String TAG = "VolleyManager";
    private static RequestQueue volleyRequestQueue;
	private static Context context;

    public static void prepareVolley(Context contextParam){
		context = contextParam;
        volleyRequestQueue =  Volley.newRequestQueue(context);
    }

    public static void queueRequest(Context context, Request request){
        if (volleyRequestQueue == null){
            prepareVolley(context);
        }
        volleyRequestQueue.add(request);
    }

    /**
     *
     * @param url
     * @param responseListener
     * @param errorListener
     * @return
     * @throws Exception
     */
    public static Request makeVolleyStringRequest(String url, final Response.Listener<String> responseListener, final ErrorListener errorListener) throws Exception{
        Response.Listener<String> wrappedResponseListener = new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				responseListener.onResponse(response);
			}
		};

		ErrorListener wrappedErrorListener = new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, error.getMessage() );
				errorListener.onErrorResponse(error);
			}
		};

        StringRequest request = new StringRequest(Request.Method.GET, url, wrappedResponseListener, wrappedErrorListener);
        queueRequest(context , request);
        return request;
    }
}
