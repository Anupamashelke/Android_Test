package app.test.com.myapplication.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import com.android.volley.toolbox.JsonObjectRequest;

/**
 * it is used to deal with webservice using volley
 */
public class WebServiceVolley {

    public static final String TAG = WebServiceVolley.class
            .getSimpleName();
    private Context context;
    private ArrayList<String> params;
    private String url;
    private String requestMethod="";
    private final Callback resultCallback;
    public static final int MY_SOCKET_TIMEOUT_MS = 7000;
    private int requestCode=1;




     public WebServiceVolley(Context context, Callback result) {
        super();
         this.context = context;
         this.resultCallback = result;
    }



    public void getProductDetailsApi(Context context, String url){
        this.context=context;
        this.requestMethod="";
     //send the normal jsonobjectrequst to volley
       volleyJsonObjectRequest(url);

    }





    public void volleyJsonObjectRequest(String URL){


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        System.out.println("response---------"+response.toString());
                        resultCallback.onSuccessJsonObject(requestCode,response);
                       // pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                /* handle the requests by volley */
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                if( error instanceof NoConnectionError) {
                    resultCallback.onError(20,error.toString());
                 }else
                if( error instanceof NetworkError) {
                     resultCallback.onError(21,error.toString());
                                  } else if( error instanceof ServerError) {
                    resultCallback.onError(22,error.toString());

                } else if( error instanceof AuthFailureError) {
                    resultCallback.onError(23,error.toString());
                } else if( error instanceof ParseError) {
                    resultCallback.onError(24,error.toString());
                } else  if( error instanceof TimeoutError) {
                    resultCallback.onError(25,error.toString());
                }

            }


        }


        );

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }






    public static interface Callback {

        public void onSuccessJsonObject(int reqestcode, JSONObject jsonResp);
        public void onError(int reqestcode, String error);

    }



}
