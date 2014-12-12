/*
 * Copyright (c) 2014 Intel Corporation.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.intel.iotkitlib.LibUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class Utilities {
    private static final String TAG = "Utilities";
    public static WeakReference<Context> contextWeakReference;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public static List<NameValuePair> addHttpHeaders(List<NameValuePair> headers, String headerName, String headerValue) {
        //add header name-value pair
        headers.add(new BasicNameValuePair(headerName, headerValue));
        return headers;
    }

    public static List<NameValuePair> createEmptyListForHeaders() {
        //returns new NameValuePair array
        return (new ArrayList<NameValuePair>());
    }

    public static List<NameValuePair> createBasicHeadersWithBearerToken() {
        if (sharedPreferences == null) {
            Log.w(TAG, "cannot find shared preferences object, not able to take bearer token");
            return null;
        }
        //building header value(need persistent to store to take auth token at run time)
        String bearerToken = IotKit.HEADER_AUTHORIZATION_BEARER + " " + sharedPreferences.getString("auth_token", "");
        return Utilities.addHttpHeaders(
                Utilities.addHttpHeaders(Utilities.createEmptyListForHeaders(), IotKit.HEADER_CONTENT_TYPE_NAME, IotKit.HEADER_CONTENT_TYPE_JSON),
                IotKit.HEADER_AUTHORIZATION,
                bearerToken);
    }

    public static List<NameValuePair> createBasicHeadersWithDeviceToken() {
        if (sharedPreferences == null) {
            Log.w(TAG, "cannot find shared preferences object, not able to take device token");
            return null;
        }
        //building header value(need persistent to store to take auth token at run time)
        String deviceToken = IotKit.HEADER_AUTHORIZATION_BEARER + " " + sharedPreferences.getString("device_token", "");
        return Utilities.addHttpHeaders(
                Utilities.addHttpHeaders(Utilities.createEmptyListForHeaders(), IotKit.HEADER_CONTENT_TYPE_NAME, IotKit.HEADER_CONTENT_TYPE_JSON),
                IotKit.HEADER_AUTHORIZATION,
                deviceToken);
    }

    //function for testing update-account(values hard-coded)
    public static String createBodyForUpdateAccount(String accountName) throws JSONException {
        //json creation to update account name with default params and fetching account_id from prefs
        JSONObject updateAccountJson = new JSONObject();
        updateAccountJson.put("name", accountName);
        updateAccountJson.put("healthTimePeriod", 86400);
        updateAccountJson.put("exec_interval", 120);
        updateAccountJson.put("base_line_exec_interval", 86400);
        updateAccountJson.put("cd_model_frequency", 604800);
        updateAccountJson.put("cd_execution_frequency", 600);
        updateAccountJson.put("data_retention", 0);
        if (Utilities.sharedPreferences == null) {
            Log.d(TAG, "shared pref object is null,cannot create body for update account");
            return null;
        }
        updateAccountJson.put("id", Utilities.sharedPreferences.getString("account_id", ""));
        //adding attributes jsonObject
        JSONObject attributesJson = new JSONObject();
        List<NameValuePair> accountAttributeList = new LinkedList<NameValuePair>();
        accountAttributeList.add(new BasicNameValuePair("phone", "123456789"));
        accountAttributeList.add(new BasicNameValuePair("company", "Aricent"));
        accountAttributeList.add(new BasicNameValuePair("client", "Intel"));
        for (NameValuePair nameValuePair : accountAttributeList) {
            attributesJson.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        updateAccountJson.put("attributes", attributesJson);
        //stringify-ing json
        return updateAccountJson.toString();
    }

    public static void createSharedPreferences(WeakReference<Context> context) {

        if (context != null) {
            contextWeakReference = context;
            sharedPreferences = contextWeakReference.get().getSharedPreferences
                    ("com.intel.IOT.AndroidIOTLib.SharedPrefs.sharedPreferences",
                            contextWeakReference.get().getApplicationContext().MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.commit();

        }
    }

    public static Map.Entry<String, ?> getSensorMatch(String componentName) {
        Boolean found = false;
        Map.Entry<String, ?> sensorMatch = null;
        Map<String, ?> preferencesAll = Utilities.sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : preferencesAll.entrySet()) {
            Log.d(TAG, entry.getKey() + " :" + entry.getValue().toString());
            if (entry.getKey().contains("sensor")) {
                StringTokenizer tokenizer = new StringTokenizer(entry.getKey(), "-");
                while (tokenizer.hasMoreElements()) {
                    if (tokenizer.nextToken().equals(componentName)) {
                        Log.d(TAG, "sensor/component found in shared preferences");
                        found = true;
                        sensorMatch = entry;
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
        }
        return sensorMatch;
    }

    public static String getSensorId(String componentName) {
        Map.Entry<String, ?> sensorMatch = null;
        if ((sensorMatch = getSensorMatch(componentName)) != null) {
            return sensorMatch.getValue().toString();
        }
        return null;
    }
}
