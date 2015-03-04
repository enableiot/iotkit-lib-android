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

import com.intel.iotkitlib.DeviceManagement;
import com.intel.iotkitlib.RequestStatusHandler;
import com.intel.iotkitlib.http.CloudResponse;
import com.intel.iotkitlib.models.Device;
import com.intel.iotkitlib.utils.Utilities;

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
            public void readResponse(CloudResponse response) {
                assertEquals(201, response.getCode());
                serverResponse = true;
            }
        });
        String deviceNameRandom = getDeviceName();
        //create a device
        Device objDeviceDetails = new
                Device("iotkit Device" + getRandomValueString(), deviceNameRandom, deviceNameRandom);
        objDeviceDetails.setLocation(12.0, 16.0, 18.0);
        objDeviceDetails.addTagName("Intel ODC test dev");
        objDeviceDetails.addAttributeInfo("processor", "AMD");
        objDeviceDetails.addAttributeInfo("Camera", "8Mp with flash");
        objDeviceDetails.addAttributeInfo("Wifi", "Yes");
        objDeviceDetails.addAttributeInfo("Retina", "No");
        try {
            CloudResponse response = objDevice.createNewDevice(objDeviceDetails);
            assertEquals(true, response.getStatus());
        } catch (JSONException je) {
            je.printStackTrace();
        }
        waitForServerResponse(objDevice);
    }

    public void testUpdateADevice() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
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
        Device objDeviceDetails = new
                Device("iotkit Device" + getRandomValueString(), null, deviceName);
        objDeviceDetails.setLocation(10.0, 15.0, 15.0);
        objDeviceDetails.addTagName("intel");
        objDeviceDetails.addAttributeInfo("processor", "Intel");
        objDeviceDetails.addAttributeInfo("Camera", "5Mp with out flash");
        objDeviceDetails.addAttributeInfo("Wifi", "no");
        objDeviceDetails.addAttributeInfo("Retina", "Yes");
        try {
            CloudResponse response = objDevice.updateADevice(objDeviceDetails);
            assertEquals(true, response.getStatus());
        } catch (JSONException je) {
            je.printStackTrace();
        }
        waitForServerResponse(objDevice);
    }

    public void testGetDeviceList() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = objDevice.getDeviceList();
        assertEquals(true, response.getStatus());
        waitForServerResponse(objDevice);
    }

    public void testGetMyDeviceInfo() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = objDevice.getMyDeviceInfo();
        assertEquals(true, response.getStatus());
        waitForServerResponse(objDevice);
    }

    public void testGetInfoOnDevice() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = objDevice.getInfoOnDevice(deviceName);
        assertEquals(true, response.getStatus());
        waitForServerResponse(objDevice);
    }

    public void testActivateADevice() throws JSONException {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        //getting activation code from shared prefs
        CloudResponse response = objDevice.activateADevice(Utilities.sharedPreferences.getString("activationCode", ""));
        assertEquals(true, response.getStatus());
        waitForServerResponse(objDevice);
    }

    public void testAddComponentToDevice() throws JSONException {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(201, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = objDevice.addComponentToDevice(getDeviceComponentName(), "temperature.v1.0");
        assertEquals(true, response.getStatus());
        waitForServerResponse(objDevice);
    }

    public void testDeleteAComponent() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(204, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = objDevice.deleteAComponent(deviceComponentName);
        assertEquals(true, response.getStatus());
        waitForServerResponse(objDevice);
    }

    public void testDeleteADevice() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(204, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = objDevice.deleteADevice(deviceName);
        assertEquals(true, response.getStatus());
        waitForServerResponse(objDevice);
    }

    public void testGetAllAttributes() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = objDevice.getAllAttributes();
        assertEquals(true, response.getStatus());
        waitForServerResponse(objDevice);
    }

    public void testGetAllTags() {
        DeviceManagement objDevice = new DeviceManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = objDevice.getAllTags();
        assertEquals(true, response.getStatus());
        waitForServerResponse(objDevice);
    }
}
