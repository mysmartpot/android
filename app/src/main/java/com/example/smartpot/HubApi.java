package com.example.smartpot;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public abstract class HubApi {

    public static String HUB_URL = "http://192.168.42.51:5000";
    public static TypeReference<Void> NO_CONTENT = new TypeReference<Void>() {};

    public interface SuccessCallback<T> {
        void onSuccess(T response);
    }

    public interface ErrorCallback {
        void onError();
    }

    public static void loadPots(SuccessCallback<List<Pot>> successCallback, ErrorCallback errorCallback) {
        sendGetRequest("/api/pots", new TypeReference<List<Pot>>() {}, successCallback, errorCallback);
    }

    public static void loadAvailablePots(SuccessCallback<List<AvailablePot>> successCallback, ErrorCallback errorCallback) {
        sendGetRequest("/api/pots/available", new TypeReference<List<AvailablePot>>() {}, successCallback, errorCallback);
    }

    public static void loadPotStatus(int id, SuccessCallback<Pot.PotStatus> successCallback, ErrorCallback errorCallback) {
        sendGetRequest("/api/pot/" + id, new TypeReference<Pot.PotStatus>() {}, successCallback, errorCallback);
    }

    public static void renamePot(int id, String newName, SuccessCallback<Void> successCallback, ErrorCallback errorCallback) {
        try {
            JSONObject body = new JSONObject();
            body.put("name", newName);
            sendPutRequest("/api/pot/" + id, body.toString(), NO_CONTENT, successCallback, errorCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void connectPot(String addr, String name, SuccessCallback<Void> successCallback, ErrorCallback errorCallback) {
        try {
            JSONObject body = new JSONObject();
            body.put("addr", addr);
            body.put("name", name);
            sendPostRequest("/api/pots", body.toString(), NO_CONTENT, successCallback, errorCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void waterPot(int id, int amount, SuccessCallback<Void> successCallback, ErrorCallback errorCallback) {
        try {
            JSONObject body = new JSONObject();
            body.put("amount", amount);
            sendPostRequest("/api/pot/" + id + "/water", body.toString(), NO_CONTENT, successCallback, errorCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void removePot(int id, SuccessCallback<Void> successCallback, ErrorCallback errorCallback) {
        sendDeleteRequest("/api/pot/" + id, NO_CONTENT, successCallback, errorCallback);
    }

    private static <T> void sendGetRequest(String path, TypeReference<T> typeRef, SuccessCallback<T> successCallback, ErrorCallback errorCallback) {
        sendRequest(Request.Method.GET, path, null, typeRef, successCallback, errorCallback);
    }

    private static <T> void sendPostRequest(String path, String body, TypeReference<T> typeRef, SuccessCallback<T> successCallback, ErrorCallback errorCallback) {
        sendRequest(Request.Method.POST, path, body, typeRef, successCallback, errorCallback);
    }

    private static <T> void sendPutRequest(String path, String body, TypeReference<T> typeRef, SuccessCallback<T> successCallback, ErrorCallback errorCallback) {
        sendRequest(Request.Method.PUT, path, body, typeRef, successCallback, errorCallback);
    }

    private static <T> void sendDeleteRequest(String path, TypeReference<T> typeRef, SuccessCallback<T> successCallback, ErrorCallback errorCallback) {
        sendRequest(Request.Method.DELETE, path, null, typeRef, successCallback, errorCallback);
    }

    private static <T> void sendRequest(int method, String path, String body, TypeReference<T> typeRef, SuccessCallback<T> successCallback, ErrorCallback errorCallback) {
        MainActivity.requestQueue.add(new StringRequest(
                method,
                HUB_URL + path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (typeRef == NO_CONTENT) {
                            successCallback.onSuccess(null);
                            return;
                        }
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            T jsonResponse = objectMapper.readValue(response, typeRef);
                            successCallback.onSuccess(jsonResponse);
                        } catch (IOException e) {
                            e.printStackTrace();
                            errorCallback.onError();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        errorCallback.onError();
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                if (body == null) return super.getBodyContentType();
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError  {
                try {
                    if (body != null) return body.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                }
                return super.getBody();
            }
        });
    }
}
