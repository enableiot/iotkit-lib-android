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
package com.intel.iotkitlib.LibModules.DataManagement;

import android.util.Log;

import com.intel.iotkitlib.LibHttp.HttpPostTask;
import com.intel.iotkitlib.LibHttp.HttpTaskHandler;
import com.intel.iotkitlib.LibModules.ParentModule;
import com.intel.iotkitlib.LibModules.RequestStatusHandler;
import com.intel.iotkitlib.LibUtils.Utilities;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DataManagement extends ParentModule {
    private final static String TAG = "DataManagement";

    public DataManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    public boolean submitData(String componentName, String componentValue,
                              Double latitude, Double longitude, Double height) throws JSONException {
        String componentId = validateRequestBodyParametersAndGetcomponentId(componentName, componentValue);
        if (componentId == null) {
            Log.d(TAG, "Cannot submit data for device component");
            return false;
        }
        String body = createHttpBodyToSubmitData(componentId, componentValue, latitude, longitude, height);
        //initiating post for data submission
        HttpPostTask submitDeviceData = new HttpPostTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);

            }
        });
        List<NameValuePair> submitDataHeaders = Utilities.createBasicHeadersWithDeviceToken();
        if (submitDataHeaders == null) {
            Log.d(TAG, "Cannot create request for submit data");
            return false;
        }
        submitDeviceData.setHeaders(submitDataHeaders);
        submitDeviceData.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.submitData, null);
        return super.invokeHttpExecuteOnURL(url, submitDeviceData, "submit data");

    }

    public boolean retrieveData(RetrieveData objRetrieveData) throws JSONException {
        if (!validateRetrieveDataValues(objRetrieveData)) {
            return false;
        }
        String body = createHttpBodyToRetrieveData(objRetrieveData);
        //initiating post for data retrieval
        HttpPostTask retrieveDataTask = new HttpPostTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);

            }
        });
        retrieveDataTask.setHeaders(basicHeaderList);
        retrieveDataTask.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.retrieveData, null);
        return super.invokeHttpExecuteOnURL(url, retrieveDataTask, "retrieve the data");

    }

    private String createHttpBodyToRetrieveData(RetrieveData objRetrieveData) throws JSONException {
        JSONObject retrieveDataJson = new JSONObject();
        retrieveDataJson.put("from", objRetrieveData.fromTimeInMillis);
        retrieveDataJson.put("to", objRetrieveData.toTimeInMillis);
        JSONObject targetFilterJson = new JSONObject();
        JSONArray deviceListJsonArray = new JSONArray();
        for (String deviceId : objRetrieveData.deviceList) {
            deviceListJsonArray.put(deviceId);
        }
        targetFilterJson.put("deviceList", deviceListJsonArray);
        retrieveDataJson.put("targetFilter", targetFilterJson);
        JSONArray metricsJsonArray = new JSONArray();
        for (String componentId : objRetrieveData.componentIdList) {
            JSONObject componentJson = new JSONObject();
            componentJson.put("id", componentId);
            componentJson.put("op", "none");
            metricsJsonArray.put(componentJson);
        }
        retrieveDataJson.put("metrics", metricsJsonArray);
        return retrieveDataJson.toString();
    }

    private boolean validateRetrieveDataValues(RetrieveData objRetrieveData) {
        if (objRetrieveData.deviceList == null || objRetrieveData.componentIdList == null) {
            Log.d(TAG, "device List or componentId List cannot be null");
            return false;
        }
        return true;
    }

    private String createHttpBodyToSubmitData(String componentId, String componentValue,
                                              Double latitude, Double longitude, Double height) throws JSONException {


        JSONObject submitDataJson = new JSONObject();
        submitDataJson.put("on", System.currentTimeMillis());
        submitDataJson.put("accountId", Utilities.sharedPreferences.getString("account_id", ""));
        JSONArray dataArray = new JSONArray();
        JSONObject dataJson = new JSONObject();
        dataJson.put("componentId", componentId);
        dataJson.put("value", componentValue);
        dataJson.put("on", System.currentTimeMillis());
        if (latitude != null && longitude != null) {
            JSONArray locationArray = new JSONArray();
            locationArray.put(latitude);
            locationArray.put(longitude);
            if (height != null) {
                locationArray.put(height);
            }
            dataJson.put("loc", locationArray);
        }
        dataArray.put(dataJson);
        submitDataJson.put("data", dataArray);
        return submitDataJson.toString();
    }

    private String validateRequestBodyParametersAndGetcomponentId(String componentName, String componentValue) {
        if (componentName == null) {
            Log.d(TAG, "submitData::Component Name cannot be NULL");
            return null;
        }
        if (componentValue == null) {
            Log.d(TAG, "submitData::Value cannot be NULL");
            return null;
        }
        if (Utilities.sharedPreferences == null) {
            Log.d(TAG, "Error in accessing accountID from shared prefs, shared prefs null");
            return null;
        }
        if (Utilities.sharedPreferences.getString("account_id", "") == null) {
            Log.d(TAG, "submitData::Account is NULL. Device appears to be unactivated");
            return null;
        }
        String cid;
        if ((cid = getSensorComponentId(componentName)) == null) {
            Log.d(TAG, "submitData::Component is not registered or problem with storage");
            return null;
        }
        return cid;
    }

    private String getSensorComponentId(String componentName) {
        if (Utilities.sharedPreferences == null) {
            Log.d(TAG, "shared prefs storage object not found, not abl eto get component ID");
            return null;
        }
        return Utilities.getSensorId(componentName);
        /*if (Utilities.sharedPreferences.getString("cname", "").contentEquals(componentName)) {
            return Utilities.sharedPreferences.getString("cid", "");
        }

        return null;*/
    }
}
