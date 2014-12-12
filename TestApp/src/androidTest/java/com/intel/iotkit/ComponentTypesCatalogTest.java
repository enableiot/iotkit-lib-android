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

import com.intel.iotkitlib.LibModules.ComponentTypesCatalog.ComponentTypesCatalog;
import com.intel.iotkitlib.LibModules.ComponentTypesCatalog.CreateOrUpdateComponentCatalog;
import com.intel.iotkitlib.LibModules.RequestStatusHandler;

import org.json.JSONException;


public class ComponentTypesCatalogTest extends ApplicationTest {
    private boolean serverResponse = false;

    private void waitForServerResponse(Object object) {
        synchronized (object) {
            while (!serverResponse) {
            }
            serverResponse = false;
        }
    }

    public void testListAllComponentTypesCatalog() {
        ComponentTypesCatalog objCatalog = new ComponentTypesCatalog(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                //setResponse(responseCode, response);
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, objCatalog.listAllComponentTypesCatalog());
        waitForServerResponse(objCatalog);
    }

    public void testListAllDetailsOfComponentTypesCatalog() {
        ComponentTypesCatalog objCatalog = new ComponentTypesCatalog(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                //setResponse(responseCode, response);
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, objCatalog.listAllDetailsOfComponentTypesCatalog());
        waitForServerResponse(objCatalog);
    }

    public void testListComponentTypeDetails() {
        ComponentTypesCatalog objCatalog = new ComponentTypesCatalog(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                //setResponse(responseCode, response);
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, objCatalog.listComponentTypeDetails("temperature.v1.0"));
        waitForServerResponse(objCatalog);
    }

    public void testCreateCustomComponent() throws JSONException {
        ComponentTypesCatalog objCatalog = new ComponentTypesCatalog(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                //setResponse(responseCode, response);
                assertEquals(201, responseCode);
                serverResponse = true;
            }
        });
        CreateOrUpdateComponentCatalog createComponentCatalog = new
                CreateOrUpdateComponentCatalog("IntelODCAC3", "1.0", "actuator", "Number", "float", "Degrees Celsius", "timeSeries");
        createComponentCatalog.setMinValue(5.0);
        createComponentCatalog.setMaxValue(100.0);
        createComponentCatalog.setCommandString("Intel actuator");
        createComponentCatalog.addCommandParameters("AC1", "30-40");
        createComponentCatalog.addCommandParameters("AC2", "35-48");
        createComponentCatalog.addCommandParameters("AC3", "32-38");
        createComponentCatalog.addCommandParameters("TrueAC", "20-30");
        createComponentCatalog.addCommandParameters("LiveAC", "10-20");
        assertEquals(true, objCatalog.createCustomComponent(createComponentCatalog));
        waitForServerResponse(objCatalog);
    }

    public void testUpdateAComponent() throws JSONException {
        ComponentTypesCatalog objCatalog = new ComponentTypesCatalog(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                //setResponse(responseCode, response);
                assertEquals(201, responseCode);
                serverResponse = true;
            }
        });
        CreateOrUpdateComponentCatalog updateComponentCatalog = new
                CreateOrUpdateComponentCatalog(null, null, "actuator", "Number", "float", "Degrees Celsius", "timeSeries");
        updateComponentCatalog.setMinValue(10.0);
        updateComponentCatalog.setMaxValue(200.0);
        updateComponentCatalog.setCommandString("Intel actuator");
        updateComponentCatalog.addCommandParameters("AC1", "25-40");
        updateComponentCatalog.addCommandParameters("AC2", "33-48");
        updateComponentCatalog.addCommandParameters("AC3", "31-38");
        updateComponentCatalog.addCommandParameters("TrueAC", "20-30");
        updateComponentCatalog.addCommandParameters("LiveAC", "10-20");

        assertEquals(true, objCatalog.updateAComponent(updateComponentCatalog, "IntelODCAC3.V1.0"));
        waitForServerResponse(objCatalog);
    }
}
