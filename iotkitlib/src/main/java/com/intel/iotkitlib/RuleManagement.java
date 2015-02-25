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

import com.intel.iotkitlib.http.HttpDeleteTask;
import com.intel.iotkitlib.http.HttpGetTask;
import com.intel.iotkitlib.http.HttpPostTask;
import com.intel.iotkitlib.http.HttpPutTask;
import com.intel.iotkitlib.http.HttpTaskHandler;
import com.intel.iotkitlib.models.CreateRule;
import com.intel.iotkitlib.models.CreateRuleActions;
import com.intel.iotkitlib.models.CreateRuleConditionValues;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;


public class RuleManagement extends ParentModule {
    private final static String TAG = "RuleManagement";

    public RuleManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    public boolean getListOfRules() {
        //initiating get for list of rules
        HttpGetTask listOfRules = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        listOfRules.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.getListOfRules, null);
        return super.invokeHttpExecuteOnURL(url, listOfRules, "list of rules");
    }

    public boolean getInformationOnRule(String ruleId) {
        if (ruleId == null) {
            Log.d(TAG, "rule id cannot be null");
            return false;
        }
        //initiating get for rule info
        HttpGetTask infoOnRule = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        infoOnRule.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("rule_id", ruleId);
        String url = objIotKit.prepareUrl(objIotKit.getInfoOfRule, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, infoOnRule, "info on one rule");
    }

    public boolean deleteADraftRule(String ruleId) {
        if (ruleId == null) {
            Log.d(TAG, "rule id cannot be null");
            return false;
        }
        //initiating delete for draft rule
        HttpDeleteTask deleteDraftRule = new HttpDeleteTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        deleteDraftRule.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("rule_id", ruleId);
        String url = objIotKit.prepareUrl(objIotKit.deleteDraftRule, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, deleteDraftRule, "delete draft rule");
    }

    public boolean updateStatusOfRule(String ruleId, String status) throws JSONException {
        if (ruleId == null || status == null) {
            Log.d(TAG, "rule id or status cannot be null");
            return false;
        }
        String body;
        if ((body = createBodyForUpdateOfRuleStatus(status)) == null) {
            return false;
        }
        //initiating put for rule status update
        HttpPutTask createInvitation = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        createInvitation.setHeaders(basicHeaderList);
        createInvitation.setRequestBody(body);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("rule_id", ruleId);
        String url = objIotKit.prepareUrl(objIotKit.updateStatusOfRule, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, createInvitation, "update status of rule");
    }

    public boolean createRuleAsDraft(String ruleName) throws JSONException {
        if (ruleName == null) {
            Log.d(TAG, "rule name cannot be null");
            return false;
        }
        String body;
        if ((body = createBodyForDraftRuleCreation(ruleName)) == null) {
            return false;
        }
        //initiating put for draft rule creation
        HttpPutTask createDraftRule = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        createDraftRule.setHeaders(basicHeaderList);
        createDraftRule.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.createRuleAsDraft, null);
        return super.invokeHttpExecuteOnURL(url, createDraftRule, "create draft rule");
    }

    public boolean updateARule(CreateRule updateRuleObj, String ruleId) throws JSONException {
        if (updateRuleObj == null) {
            Log.d(TAG, "rule Object cannot be null");
            return false;
        }
        String body;
        if ((body = createBodyForRuleCreation(updateRuleObj)) == null) {
            return false;
        }
        //initiating put for rule updation
        HttpPutTask updateRule = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        updateRule.setHeaders(basicHeaderList);
        updateRule.setRequestBody(body);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("rule_id", ruleId);
        String url = objIotKit.prepareUrl(objIotKit.updateRule, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, updateRule, "update a rule");
    }

    public boolean createARule(CreateRule createRuleObj) throws JSONException {
        if (createRuleObj == null) {
            Log.d(TAG, "rule Object cannot be null");
            return false;
        }
        String body;
        if ((body = createBodyForRuleCreation(createRuleObj)) == null) {
            return false;
        }
        //initiating post for rule creation
        HttpPostTask createRule = new HttpPostTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        createRule.setHeaders(basicHeaderList);
        createRule.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.createRule, null);
        return super.invokeHttpExecuteOnURL(url, createRule, "create a rule");
    }

    private String createBodyForRuleCreation(CreateRule createRuleObj) throws JSONException {
        JSONObject createRuleJson = new JSONObject();
        createRuleJson.put("name", createRuleObj.getName());
        createRuleJson.put("description", createRuleObj.getDescription());
        createRuleJson.put("priority", createRuleObj.getPriority());
        createRuleJson.put("type", createRuleObj.getRuleType());
        createRuleJson.put("status", createRuleObj.getStatus());
        createRuleJson.put("resetType", createRuleObj.getResetType());
        JSONArray actionsArray = new JSONArray();
        //adding actions
        for (CreateRuleActions createRuleActionsObj : createRuleObj.getRuleActionsList()) {
            JSONObject actionsJson = new JSONObject();
            JSONArray targetArray = new JSONArray();
            actionsJson.put("type", createRuleActionsObj.getRuleActionType());
            for (String targetName : createRuleActionsObj.getRuleActionTarget()) {
                targetArray.put(targetName);
            }
            actionsJson.put("target", targetArray);
            actionsArray.put(actionsJson);
        }
        createRuleJson.put("actions", actionsArray);
        //population
        JSONObject populationJson = new JSONObject();
        populationJson.put("attributes", createRuleObj.getPopulationAttributes());
        JSONArray populationIdArray = new JSONArray();
        for (String id : createRuleObj.getPopulationIds()) {
            populationIdArray.put(id);
        }
        populationJson.put("ids", populationIdArray);
        createRuleJson.put("population", populationJson);
        //conditions
        JSONObject conditionsJson = new JSONObject();
        conditionsJson.put("operator", createRuleObj.getOperatorName());
        JSONArray conditionsValueArray = new JSONArray();
        for (CreateRuleConditionValues createRuleConditionValuesObj : createRuleObj.getRuleConditionValuesList()) {
            JSONObject valuesJson = new JSONObject();
            valuesJson.put("type", createRuleConditionValuesObj.getRuleConditionType());
            valuesJson.put("operator", createRuleConditionValuesObj.getRuleConditionValuesOperatorName());
            JSONObject componentJson = new JSONObject();
            for (NameValuePair nameValuePair : createRuleConditionValuesObj.getComponents()) {
                componentJson.put(nameValuePair.getName(), nameValuePair.getValue());
            }
            valuesJson.put("component", componentJson);
            JSONArray valuesArray = new JSONArray();
            for (String value : createRuleConditionValuesObj.getValues()) {
                valuesArray.put(value);
            }
            valuesJson.put("values", valuesArray);
            conditionsValueArray.put(valuesJson);
        }
        conditionsJson.put("values", conditionsValueArray);
        createRuleJson.put("conditions", conditionsJson);

        return createRuleJson.toString();
    }

    private String createBodyForDraftRuleCreation(String ruleName) throws JSONException {
        JSONObject draftRuleCreation = new JSONObject();
        draftRuleCreation.put("name", ruleName);
        return draftRuleCreation.toString();
    }

    private String createBodyForUpdateOfRuleStatus(String status) throws JSONException {
        JSONObject updateRuleStatusJson = new JSONObject();
        updateRuleStatusJson.put("status", status);
        return updateRuleStatusJson.toString();
    }

}

