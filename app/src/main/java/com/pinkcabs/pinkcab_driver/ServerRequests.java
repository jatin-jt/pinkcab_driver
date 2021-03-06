package com.pinkcabs.pinkcab_driver;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vishal on 15-Oct-16.
 */

public class ServerRequests {
    private ResponseCallback callback=null;

    private String NEW_DRIVER="http://198.199.120.41/pinkcabs/v1/register/driver";
    private String UPDATE_LOCATION="http://198.199.120.41/pinkcabs/v1/driver/location/_drv_fireb_id_";
    private String UPDATE_FCM_TOKEN="http://198.199.120.41/pinkcabs/v1/driver/_drv_fireb_id_/update-token";
    private String GET_LOCATION="http://198.199.120.41/pinkcabs/v1/location/user/_FID_";
    public void setCallback(ResponseCallback callback) {
        this.callback = callback;
    }

    void newDriver(Context ctx, final String driverFirebaseId, final String driverFcmId) {
        StringRequest request=new StringRequest(
                Request.Method.POST,
                NEW_DRIVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (callback!=null) callback.response(jsonObject.getString("error_msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback!=null) callback.response(null);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("drv_fireb_id",driverFirebaseId);
                map.put("drv_fcm_id",driverFcmId);
                return map;
            }
        };
        Volley.newRequestQueue(ctx).add(request);
    }

    void getUserLocationByFbId(Context ctx, String userFId) {
        JsonObjectRequest request=new JsonObjectRequest(
                Request.Method.PUT,
                GET_LOCATION.replace("_FID_", userFId),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (callback != null) callback.response(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback!=null) callback.response(null);
                    }
                }
        );
        Volley.newRequestQueue(ctx).add(request);
    }
    void updateLocation(Context ctx, final String driverFirebaseId, final double latitude, final double longitude) {
        StringRequest request=new StringRequest(
                Request.Method.PUT,
                UPDATE_LOCATION.replace("_drv_fireb_id_",driverFirebaseId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (callback!=null) callback.response(jsonObject.getString("result"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback!=null) callback.response(null);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("latitude", String.valueOf(latitude));
                map.put("longitude", String.valueOf(longitude));
                return map;
            }
        };
        Volley.newRequestQueue(ctx).add(request);
    }

    void updateFcmToken(Context ctx, String driverFirebaseId, final String fcmToken) {
        StringRequest request=new StringRequest(
                Request.Method.PUT,
                UPDATE_FCM_TOKEN.replace("_drv_fireb_id_",driverFirebaseId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (callback!=null) callback.response(jsonObject.getString("result"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback!=null) callback.response(null);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("fcm_token", String.valueOf(fcmToken));
                return map;
            }
        };
        Volley.newRequestQueue(ctx).add(request);
    }

    interface ResponseCallback {
        void response(Object data);
    }
}
