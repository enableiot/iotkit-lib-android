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
package com.intel.iotkitlib;

import android.util.Log;

import com.intel.iotkitlib.http.CloudResponse;
import com.intel.iotkitlib.http.HttpPostTask;
import com.intel.iotkitlib.http.HttpTaskHandler;
import com.intel.iotkitlib.models.TimeSeriesData;
import com.intel.iotkitlib.utils.Utilities;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Data management functions
 */
public class DataManagement extends ParentModule {
    private final static String TAG = "DataManagement";

    // Error strings
    public final static String ERR_SUBMIT_DATA = "Cannot submit data for device component";
    public final static String ERR_CREATE_DATA = "Cannot create request for submit data";
    public final static String ERR_INVALID_DATA = "device List or componentId List cannot be null";


    /**
     * Submit and retrieve data for a device. This is use to do sync operation.
     *
     * For more information, please refer to @link{https://github.com/enableiot/iotkit-api/wiki/Data-API}
     */
    public DataManagement() {
        super(null);
    }

    /**
     * Submit and retrieve data for a device.
     *
     * @param requestStatusHandler The handler for asynchronously request to return data and status
     *                             from the cloud.
     */
    public DataManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    /**
     * Submit data for specific device and it's component. Device and component have to be
     * registered in the cloud before sending observations. The device id
     * that is used will be the current device that is cached usually after a create new device.
     * @param componentName the name of the component to look up the component id.
     * @param componentValue the value to set for the component.
     * @param latitude lat location for the device in decimal
     * @param longitude lon location for the device in decimal
     * @param height altitude value in meters
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse submitData(String componentName, String componentValue,
                              Double latitude, Double longitude, Double height) throws JSONException {
        String componentId = validateRequestBodyParametersAndGetcomponentId(componentName, componentValue);
        if (componentId == null) {
            Log.d(TAG, ERR_SUBMIT_DATA);
            return new CloudResponse(false, ERR_SUBMIT_DATA);
        }
        String body = createHttpBodyToSubmitData(componentId, componentValue, latitude, longitude, height);
        //initiating post for data submission
        HttpPostTask submitDeviceData = new HttpPostTask();
        List<NameValuePair> submitDataHeaders = Utilities.createBasicHeadersWithDeviceToken();
        if (submitDataHeaders == null) {
            Log.d(TAG, ERR_CREATE_DATA);
            return new CloudResponse(false, ERR_CREATE_DATA);
        }

        submitDeviceData.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.submitData, null);
        return super.invokeHttpExecuteOnURL(url, submitDeviceData);
    }

    /**
     * Submit data for specific device and it's component. Device and component have to be
     * registered in the cloud before sending observations.
     * @param deviceId the identifier for the device to submit the data for.
     * @param componentName the name of the component to look up the component id.
     * @param componentValue the value to set for the component.
     * @param latitude lat location for the device in decimal
     * @param longitude lon location for the device in decimal
     * @param height altitude value in meters
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse submitData(String deviceId, String componentName, String componentValue,
                              Double latitude, Double longitude, Double height) throws JSONException {
        String componentId = validateRequestBodyParametersAndGetcomponentId(componentName, componentValue);
        if (componentId == null) {
            Log.d(TAG, ERR_SUBMIT_DATA);
            return new CloudResponse(false, ERR_SUBMIT_DATA);
        }
        String body = createHttpBodyToSubmitData(componentId, componentValue, latitude, longitude, height);
        //initiating post for data submission
        HttpPostTask submitDeviceData = new HttpPostTask();
        List<NameValuePair> submitDataHeaders = Utilities.createBasicHeadersWithDeviceToken();
        if (submitDataHeaders == null) {
            Log.d(TAG, ERR_CREATE_DATA);
            return new CloudResponse(false, ERR_CREATE_DATA);
        }

        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("device_id", deviceId);
        submitDeviceData.setHeaders(submitDataHeaders);
        submitDeviceData.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.submitData, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, submitDeviceData);

    }

    /**
     * Retrieve data for an account.
     * @param objTimeSeriesData time series data criteria for retrieve data from the cloud
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse retrieveData(TimeSeriesData objTimeSeriesData) throws JSONException {
        if (!validateRetrieveDataValues(objTimeSeriesData)) {
            return new CloudResponse(false, ERR_INVALID_DATA);
        }
        String body = createHttpBodyToRetrieveData(objTimeSeriesData);
        //initiating post for data retrieval
        HttpPostTask retrieveDataTask = new HttpPostTask();
        retrieveDataTask.setHeaders(basicHeaderList);
        retrieveDataTask.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.retrieveData, null);
        return super.invokeHttpExecuteOnURL(url, retrieveDataTask);

    }

    private String createHttpBodyToRetrieveData(TimeSeriesData objTimeSeriesData) throws JSONException {
        JSONObject retrieveDataJson = new JSONObject();
        retrieveDataJson.put("from", objTimeSeriesData.getFromTimeInMillis());
        retrieveDataJson.put("to", objTimeSeriesData.getToTimeInMillis());
        JSONObject targetFilterJson = new JSONObject();
        JSONArray deviceListJsonArray = new JSONArray();
        for (String deviceId : objTimeSeriesData.getDeviceList()) {
            deviceListJsonArray.put(deviceId);
        }
        targetFilterJson.put("deviceList", deviceListJsonArray);
        retrieveDataJson.put("targetFilter", targetFilterJson);
        JSONArray metricsJsonArray = new JSONArray();
        for (String componentId : objTimeSeriesData.getComponentIdList()) {
            JSONObject componentJson = new JSONObject();
            componentJson.put("id", componentId);
            componentJson.put("op", "none");
            metricsJsonArray.put(componentJson);
        }
        retrieveDataJson.put("metrics", metricsJsonArray);
        return retrieveDataJson.toString();
    }

    private boolean validateRetrieveDataValues(TimeSeriesData objTimeSeriesData) {
        if (objTimeSeriesData.getDeviceList() == null || objTimeSeriesData.getComponentIdList() == null) {
            Log.d(TAG, ERR_INVALID_DATA);
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
