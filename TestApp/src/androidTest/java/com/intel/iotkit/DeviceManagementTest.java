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

import com.intel.iotkitlib.models.CreateDevice;
import com.intel.iotkitlib.DeviceManagement;
import com.intel.iotkitlib.RequestStatusHandler;

import org.json.JSONException;


public class DeviceManagementTest extends ApplicationTest {

    private boolean serverResponse = false;

    private void waitForServerResponse(Object object) {
        synchronized (object) {
            while (!serverResponse) {
            }
            serverResponse = false;
        }
    }

    public void testCreateNewDevice() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(201, responseCode);
                serverResponse = true;
            }
        });
        //create a device
        CreateDevice objDeviceDetails = new
                CreateDevice("iotkit_wrapper-Device-retrieve-data2", "devTest", "devTest");
        objDeviceDetails.setLocation(12.0, 16.0, 18.0);
        objDeviceDetails.addTagName("Intel ODC test dev");
        objDeviceDetails.addAttributeInfo("processor", "Intel");
        objDeviceDetails.addAttributeInfo("Camera", "8Mp with flash");
        objDeviceDetails.addAttributeInfo("Wifi", "Yes");
        objDeviceDetails.addAttributeInfo("Retina", "Yes");
        try {
            assertEquals(true, objDevice.createNewDevice(objDeviceDetails));
        } catch (JSONException je) {
            je.printStackTrace();
        }
        waitForServerResponse(objDevice);
    }

    public void testUpdateADevice() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        //update device, device ID not needed as we get it from prefs for updating device
        /*CreateDevice objDeviceDetails = new
                CreateDevice("iotkit_wrapper-Device-retrieve-data", null, "dev");
        objDeviceDetails.addLocationInfo(10.0, 15.0, 15.0);
        objDeviceDetails.addTagName("Intel ODC test dev update");
        objDeviceDetails.addAttributeInfo("processor", "ARM");
        objDeviceDetails.addAttributeInfo("Camera", "5Mp with flash");
        objDeviceDetails.addAttributeInfo("Wifi", "Yes");*/
        CreateDevice objDeviceDetails = new
                CreateDevice("iot dev", null, "devTest");
        objDeviceDetails.setLocation(10.0, 15.0, 15.0);
        objDeviceDetails.addTagName("intel");
        objDeviceDetails.addAttributeInfo("processor", "AMD");
        objDeviceDetails.addAttributeInfo("Camera", "5Mp with out flash");
        objDeviceDetails.addAttributeInfo("Wifi", "no");
        try {
            assertEquals(true, objDevice.updateADevice(objDeviceDetails));
        } catch (JSONException je) {
            je.printStackTrace();
        }
        waitForServerResponse(objDevice);
    }

    public void testGetDeviceList() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, objDevice.getDeviceList());
        waitForServerResponse(objDevice);
    }

    public void testGetMyDeviceInfo() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, objDevice.getMyDeviceInfo());
        waitForServerResponse(objDevice);
    }

    public void testGetInfoOnDevice() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, objDevice.getInfoOnDevice("devTest"));
        waitForServerResponse(objDevice);
    }

    public void testActivateADevice() throws JSONException {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, objDevice.activateADevice("L6Nvfy8X"));
        waitForServerResponse(objDevice);
    }

    public void testAddComponentToDevice() throws JSONException {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(201, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, objDevice.addComponentToDevice("real", "temperature.v1.0"));
        waitForServerResponse(objDevice);
    }

    public void testDeleteAComponent() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(204, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, objDevice.deleteAComponent("real"));
        waitForServerResponse(objDevice);
    }

    public void testDeleteADevice() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(204, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, objDevice.deleteADevice("devTest"));
        waitForServerResponse(objDevice);
    }

    public void testGetAllAttributes() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, objDevice.getAllAttributes());
        waitForServerResponse(objDevice);
    }

    public void testGetAllTags() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, objDevice.getAllTags());
        waitForServerResponse(objDevice);
    }
}
