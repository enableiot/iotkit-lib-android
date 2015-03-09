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

import com.intel.iotkitlib.AlertManagement;
import com.intel.iotkitlib.RequestStatusHandler;
import com.intel.iotkitlib.http.CloudResponse;
import com.intel.iotkitlib.utils.Utilities;

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
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = alertManagement.getListOfAlerts();
        assertEquals(true, response.getStatus());
        waitForServerResponse(alertManagement);
    }

    public void testGetInfoOnAlert() {
        AlertManagement alertManagement = new AlertManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = alertManagement.getInfoOnAlert(alertId.toString());
        assertEquals(true, response.getStatus());
        waitForServerResponse(alertManagement);
    }

    public void testUpdateAlertStatus() {
        AlertManagement alertManagement = new AlertManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = alertManagement.updateAlertStatus(alertId.toString(), "Open");
        assertEquals(true, response.getStatus());
        waitForServerResponse(alertManagement);
    }

    public void testAddCommentsToTheAlert() throws JSONException {
        AlertManagement alertManagement = new AlertManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = alertManagement.addCommentsToTheAlert(alertId.toString(), Utilities.sharedPreferences.getString("email", ""),
                System.currentTimeMillis(), "iotkit_wrapper comment");
        assertEquals(true, response.getStatus());
        waitForServerResponse(alertManagement);
    }

    public void testResetAlert() throws JSONException {
        AlertManagement alertManagement = new AlertManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = alertManagement.resetAlert(alertId.toString());
        assertEquals(true, response.getStatus());
        waitForServerResponse(alertManagement);
    }
}
