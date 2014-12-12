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
package com.intel.iotkitlib.LibModules.DeviceManagement;

import android.util.Log;

import com.intel.iotkitlib.LibUtils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class DeviceToken {
    private static final String TAG = "DeviceToken";

    public static void parseAndStoreDeviceId(String response) throws JSONException {
        JSONObject deviceCreationJson = new JSONObject(response);
        if (Utilities.editor == null) {
            Log.d(TAG, "Shared prefs editor object empty, cannot store device ID");
            return;
        }
        if (!(deviceCreationJson.isNull("deviceId"))) {
            Utilities.editor.putString("deviceId", deviceCreationJson.getString("deviceId"));
            Utilities.editor.commit();
        }

    }

    public static void parseAndStoreDeviceToken(String response) throws JSONException {
        JSONObject deviceActivationJson = new JSONObject(response);
        if (Utilities.editor == null) {
            Log.d(TAG, "Shared prefs editor object empty, cannot store device Token");
            return;
        }
        if (!(deviceActivationJson.isNull("deviceToken"))) {
            Utilities.editor.putString("device_token", deviceActivationJson.getString("deviceToken"));
            Utilities.editor.commit();
        }
    }

    public static void parseAndStoreComponent(String response, int responseCode) throws JSONException {
        if (responseCode != 201) {
            Log.d(TAG, "problem in adding component to Device");
            return;
        }
        JSONObject addComponentJson = new JSONObject(response);
        if (Utilities.editor == null) {
            Log.d(TAG, "Not able to access shared pref editor object to store component values");
        }
        Utilities.editor.putString("sensor-" + addComponentJson.getString("name"), addComponentJson.getString("cid"));
        //Utilities.editor.putString("cid", addComponentJson.getString("cid"));
        //Utilities.editor.putString("cname", addComponentJson.getString("name"));
        //Utilities.editor.putString("ctype", addComponentJson.getString("type"));
        Utilities.editor.commit();
    }

    public static void deleteTheComponentFromStorage(String componentName, int responseCode) {
        if (responseCode != 204) {
            Log.d(TAG, "failure response for delete component on device");
            return;
        }
        Map.Entry<String, ?> sensorMatch = null;
        if ((sensorMatch = Utilities.getSensorMatch(componentName)) != null) {
            Utilities.editor.remove(sensorMatch.getKey()).commit();
        } else {
            Log.d(TAG, "component name not found in preferences");
        }
        /*Utilities.editor.remove("cid");
        Utilities.editor.remove("cname");
        //Utilities.editor.remove("ctype");
        Utilities.editor.commit();*/
    }
}
