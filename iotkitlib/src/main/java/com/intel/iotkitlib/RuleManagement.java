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
import com.intel.iotkitlib.http.HttpDeleteTask;
import com.intel.iotkitlib.http.HttpGetTask;
import com.intel.iotkitlib.http.HttpPostTask;
import com.intel.iotkitlib.http.HttpPutTask;
import com.intel.iotkitlib.http.HttpTaskHandler;
import com.intel.iotkitlib.models.Rule;
import com.intel.iotkitlib.models.RuleAction;
import com.intel.iotkitlib.models.RuleConditionValues;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Rule management functions
 */
public class RuleManagement extends ParentModule {
    private final static String TAG = "RuleManagement";

    // Errors
    public final static String ERR_INVALID_ID =  "rule id cannot be null";
    public final static String ERR_INVALID_STATUS = "status cannot be null";
    public final static String ERR_INVALID_BODY = "Invalid body for rule";
    public final static String ERR_INVALID_NAME = "rule name cannot be null";
    public final static String ERR_INVALID_RULE = "Invalid rule object";

    /**
     * Module that manages rules. manage Rules. A rule is an association between one or more
     * device's components, a set of conditions for those components, and a number of actions
     * that have to be triggered in case those conditions are met.
     *
     * For more information, please refer to @link{https://github.com/enableiot/iotkit-api/wiki/Rule-Management}
     *
     * @param requestStatusHandler The handler for asynchronously request to return data and status
     *                             from the cloud
     */
    public RuleManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    /**
     * Get a list of all rules for the specified account.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse getListOfRules() {
        //initiating get for list of rules
        HttpGetTask listOfRules = new HttpGetTask();
        listOfRules.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.getListOfRules, null);
        return super.invokeHttpExecuteOnURL(url, listOfRules);
    }

    /**
     * Get specific rule details for the account
     * @param ruleId the identifier for the rule to retrieve info for.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse getInformationOnRule(String ruleId) {
        if (ruleId == null) {
            Log.d(TAG, ERR_INVALID_ID);
            return new CloudResponse(false, ERR_INVALID_ID);
        }
        //initiating get for rule info
        HttpGetTask infoOnRule = new HttpGetTask();
        infoOnRule.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("rule_id", ruleId);
        String url = objIotKit.prepareUrl(objIotKit.getInfoOfRule, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, infoOnRule);
    }

    /**
     * Delete a specific draft rule for account.
     * @param ruleId the identifier for the rule to delete.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse deleteADraftRule(String ruleId) {
        if (ruleId == null) {
            Log.d(TAG, ERR_INVALID_ID);
            return new CloudResponse(false, ERR_INVALID_ID);
        }
        //initiating delete for draft rule
        HttpDeleteTask deleteDraftRule = new HttpDeleteTask();
        deleteDraftRule.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("rule_id", ruleId);
        String url = objIotKit.prepareUrl(objIotKit.deleteDraftRule, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, deleteDraftRule);
    }

    /**
     * Update the status of the rule. Cannot be used for changing the status of draft rule.
     * Status value should be one of the following: ["Active", "Archived", "On-hold"]
     * @param ruleId the identifier for the rule to have the status updated.
     * @param status value should be one of the following: ["Active", "Archived", "On-hold"]
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse updateStatusOfRule(String ruleId, String status) throws JSONException {
        if (ruleId == null) {
            Log.d(TAG, ERR_INVALID_ID);
            return new CloudResponse(false, ERR_INVALID_ID);
        }
        if (status == null) {
            Log.d(TAG, ERR_INVALID_STATUS);
            return new CloudResponse(false, ERR_INVALID_STATUS);
        }
        String body;
        if ((body = createBodyForUpdateOfRuleStatus(status)) == null) {
            return new CloudResponse(false, ERR_INVALID_BODY);
        }
        //initiating put for rule status update
        HttpPutTask createInvitation = new HttpPutTask();
        createInvitation.setHeaders(basicHeaderList);
        createInvitation.setRequestBody(body);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("rule_id", ruleId);
        String url = objIotKit.prepareUrl(objIotKit.updateStatusOfRule, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, createInvitation);
    }

    /**
     * Create a rule with a status - "Draft" for the specified account.
     * @param ruleName the name of the rule to create a draft
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse createRuleAsDraft(String ruleName) throws JSONException {
        if (ruleName == null) {
            Log.d(TAG, ERR_INVALID_NAME);
            return new CloudResponse(false, ERR_INVALID_NAME);
        }
        String body;
        if ((body = createBodyForDraftRuleCreation(ruleName)) == null) {
            return new CloudResponse(false, ERR_INVALID_BODY);
        }
        //initiating put for draft rule creation
        HttpPutTask createDraftRule = new HttpPutTask();
        createDraftRule.setHeaders(basicHeaderList);
        createDraftRule.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.createRuleAsDraft, null);
        return super.invokeHttpExecuteOnURL(url, createDraftRule);
    }

    /**
     * Update the rule.
     * @param updateRuleObj the information that is used to update the rule with.
     * @param ruleId the identifier for the rule to be updated.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse updateARule(Rule updateRuleObj, String ruleId) throws JSONException {
        if (updateRuleObj == null) {
            Log.d(TAG, ERR_INVALID_RULE);
            return new CloudResponse(false, ERR_INVALID_RULE);
        }
        String body;
        if ((body = createBodyForRuleCreation(updateRuleObj)) == null) {
            return new CloudResponse(false, ERR_INVALID_BODY);
        }
        //initiating put for rule updation
        HttpPutTask updateRule = new HttpPutTask();
        updateRule.setHeaders(basicHeaderList);
        updateRule.setRequestBody(body);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("rule_id", ruleId);
        String url = objIotKit.prepareUrl(objIotKit.updateRule, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, updateRule);
    }

    /**
     * Create a rule.
     * @param ruleObj the information needed to create a new rule with.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse createARule(Rule ruleObj) throws JSONException {
        if (ruleObj == null) {
            Log.d(TAG, ERR_INVALID_RULE);
            return new CloudResponse(false, ERR_INVALID_RULE);
        }
        String body;
        if ((body = createBodyForRuleCreation(ruleObj)) == null) {
            return new CloudResponse(false, ERR_INVALID_BODY);
        }
        //initiating post for rule creation
        HttpPostTask createRule = new HttpPostTask();
        createRule.setHeaders(basicHeaderList);
        createRule.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.createRule, null);
        return super.invokeHttpExecuteOnURL(url, createRule);
    }

    private String createBodyForRuleCreation(Rule ruleObj) throws JSONException {
        JSONObject createRuleJson = new JSONObject();
        createRuleJson.put("name", ruleObj.getName());
        createRuleJson.put("description", ruleObj.getDescription());
        createRuleJson.put("priority", ruleObj.getPriority());
        createRuleJson.put("type", ruleObj.getRuleType());
        createRuleJson.put("status", ruleObj.getStatus());
        createRuleJson.put("resetType", ruleObj.getResetType());
        JSONArray actionsArray = new JSONArray();
        //adding actions
        for (RuleAction ruleActionObj : ruleObj.getRuleActionsList()) {
            JSONObject actionsJson = new JSONObject();
            JSONArray targetArray = new JSONArray();
            actionsJson.put("type", ruleActionObj.getRuleActionType());
            for (String targetName : ruleActionObj.getRuleActionTarget()) {
                targetArray.put(targetName);
            }
            actionsJson.put("target", targetArray);
            actionsArray.put(actionsJson);
        }
        createRuleJson.put("actions", actionsArray);
        //population
        JSONObject populationJson = new JSONObject();
        populationJson.put("attributes", ruleObj.getPopulationAttributes());
        JSONArray populationIdArray = new JSONArray();
        for (String id : ruleObj.getPopulationIds()) {
            populationIdArray.put(id);
        }
        populationJson.put("ids", populationIdArray);
        createRuleJson.put("population", populationJson);
        //conditions
        JSONObject conditionsJson = new JSONObject();
        conditionsJson.put("operator", ruleObj.getOperatorName());
        JSONArray conditionsValueArray = new JSONArray();
        for (RuleConditionValues ruleConditionValuesObj : ruleObj.getRuleConditionValuesList()) {
            JSONObject valuesJson = new JSONObject();
            valuesJson.put("type", ruleConditionValuesObj.getRuleConditionType());
            valuesJson.put("operator", ruleConditionValuesObj.getRuleConditionValuesOperatorName());
            JSONObject componentJson = new JSONObject();
            for (NameValuePair nameValuePair : ruleConditionValuesObj.getComponents()) {
                componentJson.put(nameValuePair.getName(), nameValuePair.getValue());
            }
            valuesJson.put("component", componentJson);
            JSONArray valuesArray = new JSONArray();
            for (String value : ruleConditionValuesObj.getValues()) {
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

