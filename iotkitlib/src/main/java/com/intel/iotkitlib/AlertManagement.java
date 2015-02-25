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

import com.intel.iotkitlib.http.HttpGetTask;
import com.intel.iotkitlib.http.HttpPostTask;
import com.intel.iotkitlib.http.HttpPutTask;
import com.intel.iotkitlib.http.HttpTaskHandler;
import com.intel.iotkitlib.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Alert management functions
 */
public class AlertManagement extends ParentModule {
    private final static String TAG = "AlertManagement";

    /**
     * The interface for handling alerts.
     *
     * For more information, please refer to @link{https://github.com/enableiot/iotkit-api/wiki/Alert-Management}
     *
     * @param requestStatusHandler The handler for asynchronously request to return data and status
     *                             from the cloud.
     */
    public AlertManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    /**
     * Get a list of all alerts for the specified account.
     * @return true if the request of REST call is valid; otherwise false. The actual result from.
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     */
    public boolean getListOfAlerts() {
        //initiating get for list of alerts
        HttpGetTask listOfAlerts = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        listOfAlerts.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.getListOfAlerts, null);
        return super.invokeHttpExecuteOnURL(url, listOfAlerts, "list of alerts");
    }

    /**
     * Get specific alert details connected with the account.
     * @param alertId the identifier for the alert to retrieve details.
     * @return true if the request of REST call is valid; otherwise false. The actual result from.
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     */
    public boolean getInfoOnAlert(String alertId) {
        if (alertId == null) {
            Log.d(TAG, "alert id cannot be null");
            return false;
        }
        //initiating get for alert info
        HttpGetTask infoOnAlert = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        infoOnAlert.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("alert_id", alertId);
        String url = objIotKit.prepareUrl(objIotKit.getAlertInformation, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, infoOnAlert, "info on one rule");
    }

    /**
     * Change the alert status to "Closed". Alert won't be active any more.
     * @param alertId the identifier for the alert to reset.
     * @return true if the request of REST call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     */
    public boolean resetAlert(String alertId) {
        if (alertId == null) {
            Log.d(TAG, "alert id cannot be null");
            return false;
        }
        //initiating put for resetting alert
        HttpPutTask alertReset = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        alertReset.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("alert_id", alertId);
        String url = objIotKit.prepareUrl(objIotKit.resetAlert, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, alertReset, "alert reset");
    }

    /**
     * Change the status of the alert.
     * @param alertId the identifier for the alert to change the status on.
     * @param status the status to change to. The value should be one of the following values:
     *               ['New', 'Open', 'Closed'].
     * @return
     */
    public boolean updateAlertStatus(String alertId, String status) {
        if (alertId == null || status == null) {
            Log.d(TAG, "alert id or status cannot be null");
            return false;
        }
        //initiating put for updating alert status
        HttpPutTask updateAlertStatus = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        updateAlertStatus.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("alert_id", alertId);
        linkedHashMap.put("status_name", status);
        String url = objIotKit.prepareUrl(objIotKit.resetAlert, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, updateAlertStatus, "alert status update");
    }

    /**
     * Add a comment to the alert.
     * @param alertId the identifier for the alert that the comment will be attached to.
     * @param user the user that made the comment.
     * @param timeStamp the timestamp when the comment was made.
     * @param comment the comment text.
     * @return true if the request of REST call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * @throws JSONException
     */
    public boolean addCommentsToTheAlert(String alertId, String user, Long timeStamp, String comment) throws JSONException {
        if (alertId == null || user == null || comment == null) {
            Log.d(TAG, "alert id or user or comment cannot be null");
            return false;
        }
        String body;
        if ((body = createBodyForAddingCommentToAlert(user, timeStamp, comment)) == null) {
            return false;
        }
        //initiating post for adding comment to alert
        HttpPostTask addCommentsTOAlert = new HttpPostTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        addCommentsTOAlert.setHeaders(basicHeaderList);
        addCommentsTOAlert.setRequestBody(body);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("alert_id", alertId);
        String url = objIotKit.prepareUrl(objIotKit.addCommentToAlert, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, addCommentsTOAlert, "add comments to alert");
    }

    private String createBodyForAddingCommentToAlert(String user, Long timeStamp, String comment) throws JSONException {
        JSONArray addCommentArray = new JSONArray();
        JSONObject addCommentJson = new JSONObject();
        addCommentJson.put("user", user);
        addCommentJson.put("timestamp", timeStamp);
        addCommentJson.put("text", comment);
        addCommentArray.put(addCommentJson);
        return addCommentArray.toString();
    }
}
