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
package com.intel.iotkitlib.LibModules.AlertManagement;

import android.util.Log;

import com.intel.iotkitlib.LibHttp.HttpGetTask;
import com.intel.iotkitlib.LibHttp.HttpPostTask;
import com.intel.iotkitlib.LibHttp.HttpPutTask;
import com.intel.iotkitlib.LibHttp.HttpTaskHandler;
import com.intel.iotkitlib.LibModules.ParentModule;
import com.intel.iotkitlib.LibModules.RequestStatusHandler;
import com.intel.iotkitlib.LibUtils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;


public class AlertManagement extends ParentModule {
    private final static String TAG = "AlertManagement";

    public AlertManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

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

    public boolean createAlert(CreateNewAlert newAlert) throws JSONException {
        String body;
        if ((body = createBodyToCreateAlert(newAlert)) == null) {
            return false;
        }
        //initiating post for creating alert
        HttpPostTask createNewAlert = new HttpPostTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        createNewAlert.setHeaders(basicHeaderList);
        createNewAlert.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.createNewAlert, null);
        return super.invokeHttpExecuteOnURL(url, createNewAlert, "create new alert");
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

    private String createBodyToCreateAlert(CreateNewAlert newAlert) throws JSONException {
        JSONObject createAlertJson = new JSONObject();
        if (newAlert.msgType != null) {
            createAlertJson.put("msgType", newAlert.msgType);
        } else {
            createAlertJson.put("msgType", "alertsPush");
        }
        if (newAlert.alertDataList != null) {
            JSONArray alertDataArray = new JSONArray();
            for (CreateNewAlertData newAlertData : newAlert.alertDataList) {
                JSONObject newAlertDataJson = new JSONObject();
                if (newAlertData.accountId != null) {
                    newAlertDataJson.put("accountId", newAlertData.accountId);
                } else {
                    newAlertDataJson.put("accountId", Utilities.sharedPreferences.getString("account_id", ""));
                }
                if (newAlertData.deviceId != null) {
                    newAlertDataJson.put("deviceId", newAlertData.deviceId);
                } else {
                    newAlertDataJson.put("deviceId", Utilities.sharedPreferences.getString("deviceId", ""));
                }
                if (newAlertData.alertId != -1) {
                    newAlertDataJson.put("alertId", newAlertData.alertId);
                }
                if (newAlertData.ruleId != -1) {
                    newAlertDataJson.put("ruleId", newAlertData.ruleId);
                }
                if (newAlertData.alertStatus != null) {
                    newAlertDataJson.put("alertStatus", newAlertData.alertStatus);
                }
                if (newAlertData.timestamp > 0L) {
                    newAlertDataJson.put("timestamp", newAlertData.timestamp);
                }
                if (newAlertData.resetTimestamp > 0L) {
                    newAlertDataJson.put("resetTimestamp", newAlertData.resetTimestamp);
                }
                if (newAlertData.resetType != null) {
                    newAlertDataJson.put("resetType", newAlertData.resetType);
                }
                if (newAlertData.ruleName != null) {
                    newAlertDataJson.put("ruleName", newAlertData.ruleName);
                }
                if (newAlertData.lastUpdateDate > 0L) {
                    newAlertDataJson.put("lastUpdateDate", newAlertData.lastUpdateDate);
                }
                if (newAlertData.rulePriority != null) {
                    newAlertDataJson.put("rulePriority", newAlertData.rulePriority);
                }

                if (newAlertData.naturalLangAlert != null) {
                    newAlertDataJson.put("naturalLangAlert", newAlertData.naturalLangAlert);
                }
                if (newAlertData.ruleExecutionTimestamp > 0L) {
                    newAlertDataJson.put("ruleExecutionTimestamp", newAlertData.ruleExecutionTimestamp);
                }
                if (newAlertData.alertDataConditionsList != null) {
                    JSONArray conditionsArray = new JSONArray();
                    for (CreateNewAlertDataConditions newAlertDataConditions : newAlertData.alertDataConditionsList) {
                        JSONObject newAlertDataConditionsJson = new JSONObject();
                        if (newAlertDataConditions.conditionSequence > -1) {
                            newAlertDataConditionsJson.put("conditionSequence", newAlertDataConditions.conditionSequence);
                        }
                        if (newAlertDataConditions.naturalLangCondition != null) {
                            newAlertDataConditionsJson.put("naturalLangCondition", newAlertDataConditions.naturalLangCondition);
                        }
                        if (newAlertDataConditions.alertDataConditionCmpsList != null) {
                            JSONArray ConditionCmpsArray = new JSONArray();
                            for (CreateNewAlertDataConditionComponents newAlertDataConditionComponent : newAlertDataConditions.alertDataConditionCmpsList) {
                                JSONObject newAlertDataConditionComponentJson = new JSONObject();
                                if (newAlertDataConditionComponent.cmpComponentId != null) {
                                    newAlertDataConditionComponentJson.put("componentId", newAlertDataConditionComponent.cmpComponentId);
                                }
                                if (newAlertDataConditionComponent.cmpDataType != null) {
                                    newAlertDataConditionComponentJson.put("dataType", newAlertDataConditionComponent.cmpDataType);
                                }
                                if (newAlertDataConditionComponent.cmpComponentName != null) {
                                    newAlertDataConditionComponentJson.put("componentName", newAlertDataConditionComponent.cmpComponentName);
                                }
                                if (newAlertDataConditionComponent.conditionCmpsValuePointsList != null) {
                                    JSONArray valuePointsArray = new JSONArray();
                                    for (ConditionCmpsValuePoints valuePoint : newAlertDataConditionComponent.conditionCmpsValuePointsList) {
                                        JSONObject valuePointJson = new JSONObject();
                                        valuePointJson.put("timestamp", valuePoint.timeStamp);
                                        valuePointJson.put("value", valuePoint.value);
                                        valuePointsArray.put(valuePointJson);
                                    }
                                    newAlertDataConditionComponentJson.put("valuePoints", valuePointsArray);
                                }
                                ConditionCmpsArray.put(newAlertDataConditionComponentJson);
                            }
                            newAlertDataConditionsJson.put("components", ConditionCmpsArray);
                        }
                        conditionsArray.put(newAlertDataConditionsJson);
                    }
                    newAlertDataJson.put("conditions", conditionsArray);
                }

                alertDataArray.put(newAlertDataJson);
            }
            createAlertJson.put("data", alertDataArray);
        }
        return createAlertJson.toString();
    }
}
