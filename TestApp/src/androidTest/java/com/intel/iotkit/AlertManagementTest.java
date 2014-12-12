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

import com.intel.iotkitlib.LibModules.AlertManagement.AlertManagement;
import com.intel.iotkitlib.LibModules.AlertManagement.CreateNewAlert;
import com.intel.iotkitlib.LibModules.AlertManagement.CreateNewAlertData;
import com.intel.iotkitlib.LibModules.AlertManagement.CreateNewAlertDataConditionComponents;
import com.intel.iotkitlib.LibModules.AlertManagement.CreateNewAlertDataConditions;
import com.intel.iotkitlib.LibModules.RequestStatusHandler;

import org.json.JSONException;


public class AlertManagementTest extends ApplicationTest {
    private boolean serverResponse = false;

    private void waitForServerResponse(Object object) {
        synchronized (object) {
            while (!serverResponse) {
            }
            serverResponse = false;
        }
    }

    public void testGetListOfAlerts() {
        AlertManagement alertManagement = new AlertManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, alertManagement.getListOfAlerts());
        waitForServerResponse(alertManagement);
    }

    public void testCreateAlert() throws JSONException {
        AlertManagement alertManagement = new AlertManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        CreateNewAlert newAlert = new CreateNewAlert();
        CreateNewAlertData newAlertData = new CreateNewAlertData();
        CreateNewAlertDataConditions newAlertDataConditions = new CreateNewAlertDataConditions();
        CreateNewAlertDataConditionComponents newAlertDataConditionComponents =
                new CreateNewAlertDataConditionComponents();

        //setting new alert props
        newAlertData.alertSetAlertId(1111);
        newAlertData.alertSetRuleId(101);
        newAlertData.alertSetDeviceId("dev1");
        newAlertData.alertSetAccountId("1e5f2301-b121-4933-8f32-1abb0af3d777");
        newAlertData.alertSetAlertStatus("Open");
        newAlertData.alertSetTimestamp(System.currentTimeMillis());
        newAlertData.alertSetResetTimestamp(System.currentTimeMillis());
        newAlertData.alertSetResetType("Automatic");
        newAlertData.alertSetLastUpdateDate(System.currentTimeMillis());
        newAlertData.alertSetRuleName("Test Rule2");
        newAlertData.alertSetRulePriority("Low");
        newAlertData.alertSetNaturalLangAlert("temperature > 0");
        newAlertData.alertSetRuleExecutionTimestamp(System.currentTimeMillis());

        //adding alert data to alert list
        newAlert.addNewAlertDataObject(newAlertData);

        //setting new alert condition props
        newAlertDataConditions.alertSetConditionSequence(1);
        newAlertDataConditions.alertSetNaturalLanguageCondition("temperature > 0");

        //adding conditions to alert condition list
        newAlertData.alertAddNewAlertConditions(newAlertDataConditions);

        //setting new alert condition component props
        newAlertDataConditionComponents.alertSetComponentId("5C09B9F0-E06B-404A-A882-EAC64675A63E");
        newAlertDataConditionComponents.alertSetDataType("Number");
        newAlertDataConditionComponents.alertSetComponentName("temper");
        newAlertDataConditionComponents.alertAddValuePoints(System.currentTimeMillis(), "25");

        //adding condition components to component list
        newAlertDataConditions.alertAddComponents(newAlertDataConditionComponents);

        assertEquals(true, alertManagement.createAlert(newAlert));
        waitForServerResponse(alertManagement);
    }

    public void testGetInfoOnAlert() {
        AlertManagement alertManagement = new AlertManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, alertManagement.getInfoOnAlert("1111"));
        waitForServerResponse(alertManagement);
    }

    public void testUpdateAlertStatus() {
        AlertManagement alertManagement = new AlertManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, alertManagement.updateAlertStatus("1111", "open"));
        waitForServerResponse(alertManagement);
    }

    public void testAddCommentsToTheAlert() throws JSONException {
        AlertManagement alertManagement = new AlertManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, alertManagement.addCommentsToTheAlert("1111", "raghu", System.currentTimeMillis(), "iotkit_wrapper comment"));
        waitForServerResponse(alertManagement);
    }

    public void testResetAlert() throws JSONException {
        AlertManagement alertManagement = new AlertManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, alertManagement.resetAlert("1111"));
        waitForServerResponse(alertManagement);
    }
}
