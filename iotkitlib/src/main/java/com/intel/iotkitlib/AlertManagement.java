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

    // Errors
    public final static String ERR_INVALID_ID = "alert id cannot be null";
    public final static String ERR_INVALID_BODY = "alert body cannot be null";

    /**
     * The interface for handling alerts; use this to do sync operation
     */
    public AlertManagement() { super(null); }

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
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse getListOfAlerts() {
        //initiating get for list of alerts
        HttpGetTask listOfAlerts = new HttpGetTask();
        listOfAlerts.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.getListOfAlerts, null);
        return super.invokeHttpExecuteOnURL(url, listOfAlerts);
    }

    /**
     * Get specific alert details connected with the account.
     * @param alertId the identifier for the alert to retrieve details.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse getInfoOnAlert(String alertId) {
        if (alertId == null) {
            Log.d(TAG, ERR_INVALID_ID);
            return new CloudResponse(false, ERR_INVALID_ID);
        }
        //initiating get for alert info
        HttpGetTask infoOnAlert = new HttpGetTask();
        infoOnAlert.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("alert_id", alertId);
        String url = objIotKit.prepareUrl(objIotKit.getAlertInformation, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, infoOnAlert);
    }

    /**
     * Change the alert status to "Closed". Alert won't be active any more.
     * @param alertId the identifier for the alert to reset.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse resetAlert(String alertId) {
        if (alertId == null) {
            Log.d(TAG, ERR_INVALID_ID);
            return new CloudResponse(false, ERR_INVALID_ID);
        }
        //initiating put for resetting alert
        HttpPutTask alertReset = new HttpPutTask();
        alertReset.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("alert_id", alertId);
        String url = objIotKit.prepareUrl(objIotKit.resetAlert, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, alertReset);
    }

    /**
     * Change the status of the alert.
     * @param alertId the identifier for the alert to change the status on.
     * @param status the status to change to. The value should be one of the following values:
     *               ['New', 'Open', 'Closed'].
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse updateAlertStatus(String alertId, String status) {
        if (alertId == null || status == null) {
            return new CloudResponse(false, ERR_INVALID_ID);
        }
        //initiating put for updating alert status
        HttpPutTask updateAlertStatus = new HttpPutTask();
        updateAlertStatus.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("alert_id", alertId);
        linkedHashMap.put("status_name", status);
        String url = objIotKit.prepareUrl(objIotKit.resetAlert, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, updateAlertStatus);
    }

    /**
     * Add a comment to the alert.
     * @param alertId the identifier for the alert that the comment will be attached to.
     * @param user the user that made the comment.
     * @param timeStamp the timestamp when the comment was made.
     * @param comment the comment text.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse addCommentsToTheAlert(String alertId, String user, Long timeStamp, String comment) throws JSONException {
        if (alertId == null || user == null || comment == null) {
            Log.d(TAG, ERR_INVALID_ID);
            return new CloudResponse(false, ERR_INVALID_ID);
        }
        String body;
        if ((body = createBodyForAddingCommentToAlert(user, timeStamp, comment)) == null) {
            return new CloudResponse(false, ERR_INVALID_BODY);
        }
        //initiating post for adding comment to alert
        HttpPostTask addCommentsTOAlert = new HttpPostTask();
        addCommentsTOAlert.setHeaders(basicHeaderList);
        addCommentsTOAlert.setRequestBody(body);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("alert_id", alertId);
        String url = objIotKit.prepareUrl(objIotKit.addCommentToAlert, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, addCommentsTOAlert);
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
