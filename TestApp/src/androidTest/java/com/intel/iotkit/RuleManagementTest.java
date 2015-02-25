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
package com.intel.iotkit;

import com.intel.iotkitlib.RequestStatusHandler;
import com.intel.iotkitlib.models.CreateRule;
import com.intel.iotkitlib.models.CreateRuleActions;
import com.intel.iotkitlib.models.CreateRuleConditionValues;
import com.intel.iotkitlib.RuleManagement;

import org.json.JSONException;


public class RuleManagementTest extends ApplicationTest {
    private boolean serverResponse = false;

    private void waitForServerResponse(Object object) {
        synchronized (object) {
            while (!serverResponse) {
            }
            serverResponse = false;
        }
    }

    public void testCreateARule() throws JSONException {
        RuleManagement ruleManagement = new RuleManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(201, responseCode);
                serverResponse = true;
            }
        });
        CreateRule createRuleObj = new CreateRule();
        CreateRuleActions createRuleActionObj = new CreateRuleActions();
        CreateRuleConditionValues createRuleConditionValuesObj =
                new CreateRuleConditionValues();

        createRuleObj.setRuleName("Test Rule2");
        createRuleObj.setRuleDescription("This is a iotkit_wrapper rule");
        createRuleObj.setRulePriority("Medium");
        createRuleObj.setRuleType("Regular");
        createRuleObj.setRuleStatus("Active");
        createRuleObj.setRuleResetType("Automatic");

        createRuleActionObj.setRuleActionType("mail");
        createRuleActionObj.addRuleActionTarget("intel.aricent.iot1@gmail.com");
        createRuleActionObj.addRuleActionTarget("intel.aricent.iot3@gmail.com");

        createRuleObj.setRuleActions(createRuleActionObj);
        createRuleObj.addRulePopulationId("685.1.1.1");

        createRuleConditionValuesObj.addConditionComponent("dataType", "Number");
        createRuleConditionValuesObj.addConditionComponent("name", "Temp.01.1");
        createRuleConditionValuesObj.setConditionType("basic");
        createRuleConditionValuesObj.addConditionValues("25");
        createRuleConditionValuesObj.setConditionOperator(">");

        createRuleObj.setRuleOperatorName("OR");
        createRuleObj.addRuleConditionValues(createRuleConditionValuesObj);

        assertEquals(true, ruleManagement.createARule(createRuleObj));
        waitForServerResponse(ruleManagement);
    }

    public void testUpdateRule() throws JSONException {
        RuleManagement ruleManagement = new RuleManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        CreateRule createRuleObj = new CreateRule();
        CreateRuleActions createRuleActionObj = new CreateRuleActions();
        CreateRuleConditionValues createRuleConditionValuesObj =
                new CreateRuleConditionValues();

        createRuleObj.setRuleName("Test Rule2");
        createRuleObj.setRuleDescription("This is a iotkit_wrapper rule");
        createRuleObj.setRulePriority("Medium");
        createRuleObj.setRuleType("Regular");
        createRuleObj.setRuleStatus("Active");
        createRuleObj.setRuleResetType("Automatic");

        createRuleActionObj.setRuleActionType("mail");
        createRuleActionObj.addRuleActionTarget("intel.aricent.iot3@gmail.com");
        createRuleActionObj.addRuleActionTarget("intel.aricent.iot1@gmail.com");

        createRuleObj.setRuleActions(createRuleActionObj);
        createRuleObj.addRulePopulationId("685.1.1.1");

        createRuleConditionValuesObj.addConditionComponent("dataType", "Number");
        createRuleConditionValuesObj.addConditionComponent("name", "Temp.01.1");
        createRuleConditionValuesObj.setConditionType("basic");
        createRuleConditionValuesObj.addConditionValues("25");
        createRuleConditionValuesObj.setConditionOperator(">");

        createRuleObj.setRuleOperatorName("OR");
        createRuleObj.addRuleConditionValues(createRuleConditionValuesObj);

        assertEquals(true, ruleManagement.updateARule(createRuleObj, "90205"));
        waitForServerResponse(ruleManagement);
    }

    public void testCreateRuleAsDraft() throws JSONException {
        RuleManagement ruleManagement = new RuleManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });

        assertEquals(true, ruleManagement.createRuleAsDraft("iotkit_wrapper draft rule 14/11/2014"));
        waitForServerResponse(ruleManagement);
    }

    public void testDeleteADraftRule() throws JSONException {
        RuleManagement ruleManagement = new RuleManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(204, responseCode);
                serverResponse = true;
            }
        });

        assertEquals(true, ruleManagement.deleteADraftRule("284fb6d048402e17b0cee60a24e426830328d316"));
        waitForServerResponse(ruleManagement);
    }

    public void testGetInformationOnRule() throws JSONException {
        RuleManagement ruleManagement = new RuleManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });

        assertEquals(true, ruleManagement.getInformationOnRule("90205"));
        waitForServerResponse(ruleManagement);
    }

    public void testGetListOfRules() throws JSONException {
        RuleManagement ruleManagement = new RuleManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });

        assertEquals(true, ruleManagement.getListOfRules());
        waitForServerResponse(ruleManagement);
    }

    public void testUpdateStatusOfRule() throws JSONException {
        RuleManagement ruleManagement = new RuleManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });

        assertEquals(true, ruleManagement.updateStatusOfRule("90205", "Archived"));
        waitForServerResponse(ruleManagement);
    }
}
