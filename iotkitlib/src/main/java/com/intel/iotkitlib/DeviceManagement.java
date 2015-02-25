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
import com.intel.iotkitlib.models.CreateDevice;
import com.intel.iotkitlib.models.DeviceToken;
import com.intel.iotkitlib.utils.Utilities;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;


public class DeviceManagement extends ParentModule {
    private static final String TAG = "DeviceManagement";
    //public CreateDevice createDevice;

    public DeviceManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    public boolean getDeviceList() {
        //initiating get for device list
        HttpGetTask listDevices = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        listDevices.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.listDevices, null);
        return super.invokeHttpExecuteOnURL(url, listDevices, "list all devices");
    }

    public boolean createNewDevice(CreateDevice objCreateDevice) throws JSONException {
        String body;
        if ((body = createBodyForDeviceCreation(objCreateDevice)) == null) {
            return false;
        }
        //initiating post for device creation
        HttpPostTask createNewDevice = new HttpPostTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                try {
                    DeviceToken.parseAndStoreDeviceId(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                statusHandler.readResponse(responseCode, response);
            }
        });
        createNewDevice.setHeaders(basicHeaderList);
        createNewDevice.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.createDevice, null);
        return super.invokeHttpExecuteOnURL(url, createNewDevice, "create new device");
    }

    public boolean getInfoOnDevice(String deviceId) {
        //initiating get for device info
        HttpGetTask getDeviceDetails = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        getDeviceDetails.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("other_device_id", deviceId);
        String url = objIotKit.prepareUrl(objIotKit.getOneDeviceInfo, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, getDeviceDetails, "other device info");
    }

    public boolean getMyDeviceInfo() {
        //initiating get for device info
        HttpGetTask getDeviceDetails = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        getDeviceDetails.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.getMyDeviceInfo, null);
        return super.invokeHttpExecuteOnURL(url, getDeviceDetails, "my device info");
    }

    public boolean updateADevice(CreateDevice objUpdateDevice) throws JSONException {
        String body;
        if ((body = createBodyForDeviceUpdation(objUpdateDevice)) == null) {
            return false;
        }
        //initiating put for device updation
        HttpPutTask updateDevice = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        updateDevice.setHeaders(basicHeaderList);
        updateDevice.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.updateDevice, null);
        return super.invokeHttpExecuteOnURL(url, updateDevice, "update device");
    }

    public boolean deleteADevice(String deviceId) {
        //initiating delete of device
        HttpDeleteTask deleteADevice = new HttpDeleteTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        deleteADevice.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("other_device_id", deviceId);
        String url = objIotKit.prepareUrl(objIotKit.deleteDevice, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, deleteADevice, "delete device");
    }

    public boolean addComponentToDevice(String componentName, String componentType) throws JSONException {
        String body;
        if ((body = createBodyForAddComponent(componentName, componentType)) == null) {
            return false;
        }
        //initiating post for device creation
        HttpPostTask addComponent = new HttpPostTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                try {
                    DeviceToken.parseAndStoreComponent(response, responseCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                statusHandler.readResponse(responseCode, response);

            }
        });
        List<NameValuePair> submitDataHeaders = Utilities.createBasicHeadersWithDeviceToken();
        if (submitDataHeaders == null) {
            Log.d(TAG, "Cannot create request for add component");
            return false;
        }
        addComponent.setHeaders(submitDataHeaders);
        addComponent.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.addComponent, null);
        return super.invokeHttpExecuteOnURL(url, addComponent, "add component to device");
    }

    //To-DO,need to be tested
    public boolean deleteAComponent(final String componentName) {
        //initiating delete of component
        HttpDeleteTask deleteComponent = new HttpDeleteTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                DeviceToken.deleteTheComponentFromStorage(componentName, responseCode);
                statusHandler.readResponse(responseCode, response);
            }
        });
        deleteComponent.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("cname", componentName);
        String url = objIotKit.prepareUrl(objIotKit.deleteComponent, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, deleteComponent, "delete  a component");
    }

    public boolean activateADevice(String activationCode) throws JSONException {
        if (Utilities.sharedPreferences.contains("deviceToken") &&
                Utilities.sharedPreferences.getString("deviceToken", "") != null) {
            Log.i(TAG, "activateADevice::Device appears to be already activated. Could not proceed\n");
            return false;
        }
        if (activationCode == null) {
            Log.i(TAG, "activateADevice::Activation Code cannot be NULL");
            return false;
        }
        String body;
        if ((body = createBodyForDeviceActivation(activationCode)) == null) {
            return false;
        }
        //initiating put for device activation
        HttpPutTask activateDevice = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                try {
                    DeviceToken.parseAndStoreDeviceToken(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                statusHandler.readResponse(responseCode, response);
            }
        });
        activateDevice.setHeaders(basicHeaderList);
        activateDevice.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.activateDevice, null);
        return super.invokeHttpExecuteOnURL(url, activateDevice, "activate a device");
    }

    public boolean getAllAttributes() {
        //initiating get for all attributes
        HttpGetTask listAttributes = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        listAttributes.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.listAllAttributes, null);
        return super.invokeHttpExecuteOnURL(url, listAttributes, "list all attributes");
    }

    public boolean getAllTags() {
        //initiating get for all tags
        HttpGetTask listTags = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        listTags.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.listAllTags, null);
        return super.invokeHttpExecuteOnURL(url, listTags, "list all Tags");
    }

    //method to handle http body formation for updating or creating device
    private String createDeviceBody(JSONObject deviceBodyJson, CreateDevice deviceObjectForCreationOrUpdation) throws JSONException {
        if (deviceObjectForCreationOrUpdation.getGatewayId() == null) {
            Log.d(TAG, "gateway Id cannot be empty");
            return null;
        }
        if (deviceObjectForCreationOrUpdation.getDeviceName() == null) {
            Log.d(TAG, "Device name cannot be empty");
            return null;
        }
        deviceBodyJson.put("gatewayId", deviceObjectForCreationOrUpdation.getGatewayId());
        deviceBodyJson.put("name", deviceObjectForCreationOrUpdation.getDeviceName());
        //location details should contain lat,long,height details,adding location json array
        if (deviceObjectForCreationOrUpdation.getLocation().size() > 2) {
            JSONArray locationJsonArray = new JSONArray();
            for (Double location : deviceObjectForCreationOrUpdation.getLocation()) {
                locationJsonArray.put(location);
            }
            deviceBodyJson.put("loc", locationJsonArray);
        }
        //Adding tags json array
        if (deviceObjectForCreationOrUpdation.getTags().size() > 0) {
            JSONArray tagsJsonArray = new JSONArray();
            for (String tag : deviceObjectForCreationOrUpdation.getTags()) {
                tagsJsonArray.put(tag);
            }
            deviceBodyJson.put("tags", tagsJsonArray);
        }
        //adding attributes jsonObject
        if (deviceObjectForCreationOrUpdation.getAttributes().size() > 0) {
            JSONObject attributesJson = new JSONObject();
            for (NameValuePair nameValuePair : deviceObjectForCreationOrUpdation.getAttributes()) {
                attributesJson.put(nameValuePair.getName(), nameValuePair.getValue());
            }
            deviceBodyJson.put("attributes", attributesJson);
        }
        return deviceBodyJson.toString();
    }

    //method to handle http body formation for updating device
    private String createBodyForDeviceUpdation(CreateDevice objUpdateDevice) throws JSONException {
        JSONObject updateDeviceJson = new JSONObject();
        return createDeviceBody(updateDeviceJson, objUpdateDevice);
    }

    //method to handle http body formation for creating device
    private String createBodyForDeviceCreation(CreateDevice objCreateDevice) throws JSONException {
        JSONObject newDeviceJson = new JSONObject();
        if (objCreateDevice.getDeviceId() == null) {
            Log.d(TAG, "Device Id cannot be empty");
            return null;
        }
        newDeviceJson.put("deviceId", objCreateDevice.getDeviceId());
        return createDeviceBody(newDeviceJson, objCreateDevice);
    }

    //method to handle http body formation for adding component to device
    private String createBodyForAddComponent(String componentName, String componentType) throws JSONException {
        JSONObject addComponentJson = new JSONObject();
        addComponentJson.put("cid", UUID.randomUUID().toString());
        addComponentJson.put("name", componentName);
        addComponentJson.put("type", componentType);
        return addComponentJson.toString();
    }

    //method to handle http body formation for adding component to device
    private String createBodyForDeviceActivation(String activationCode) throws JSONException {
        JSONObject activateDeviceJson = new JSONObject();
        activateDeviceJson.put("activationCode", activationCode);
        return activateDeviceJson.toString();
    }
}
