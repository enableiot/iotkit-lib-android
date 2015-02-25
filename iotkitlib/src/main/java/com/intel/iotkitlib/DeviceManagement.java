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
import com.intel.iotkitlib.models.Device;
import com.intel.iotkitlib.models.DeviceToken;
import com.intel.iotkitlib.utils.Utilities;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * Device management functions
 */
public class DeviceManagement extends ParentModule {
    private static final String TAG = "DeviceManagement";

    /**
     * Management of devices
     *
     * For more information, please refer to @link{https://github.com/enableiot/iotkit-api/wiki/Device-Management}
     *
     * @param requestStatusHandler The handler for asynchronously request to return data and status
     *                             from the cloud.
     */
    public DeviceManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    /**
     * Get a list of all devices for the specified account, with minimal data for each device.
     * @return true if the request of REST call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link ParentModule#statusHandler}.
     */
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

    // TODO: getFilteredDevices

    /**
     * Create a new device with the device info.
     * @param objDevice the device info to create a new device with.
     * @return true if the request of REST call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * @throws JSONException
     */
    public boolean createNewDevice(Device objDevice) throws JSONException {
        String body;
        if ((body = createBodyForDeviceCreation(objDevice)) == null) {
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

    /**
     * Get full detail for specific device for the specified account.
     * @param deviceId the identifier for the device to get details for.
     * @return true if the request of REST call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     */
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

    /**
     * Get full detail for newly created device for the specified account. The device id
     * that is used will be the current device that is cached usually after a create new device.
     * @return true if the request of REST call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     */
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

    /**
     * Update a single device. The device ID (deviceId) cannot be updated as it is the key.
     * If the device id does not exist, an error will be returned. The device id
     * that is used will be the current device that is cached usually after a create new device.
     * @param objUpdateDevice the device info to update the device with.
     * @return true if the request of REST call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * @throws JSONException
     */
    public boolean updateADevice(Device objUpdateDevice) throws JSONException {
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

    /**
     * Delete a specific device for this account. All data from all time series associated with
     * the device will be deleted.
     * @param deviceId the identifier for the device that will be deleted.
     * @return true if the request of REST call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     */
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

    /**
     * Add component to an specific device. A component represents either a time series or an
     * actuator. The type must already existing in the Component Type catalog.
     * @param componentName the name that identifies the component.
     * @param componentType the type of the component
     * @return true if the request of REST call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * @throws JSONException
     */
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

    /* TODO: need to be tested */
    /**
     * Delete a specific component for a specific device. All data will be unavailable. The device id
     * that is used will be the current device that is cached usually after a create new device.
     * @param componentName the name that identifies the component.
     * @return true if the request of REST call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     */
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

    /**
     * Activates a specific device for the specified account.
     * @param activationCode the activation code to be used for activating the device.
     * @return true if the request of REST call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * @throws JSONException
     */
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

    /**
     * Get a list of all devices's attribute for the specified account.
     * @return true if the request of REST call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     */
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

    /**
     * Get a list of all tags from devices for the specified account.
     * @return true if the request of REST call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     */
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
    private String createDeviceBody(JSONObject deviceBodyJson, Device deviceObjectForCreationOrUpdation) throws JSONException {
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
    private String createBodyForDeviceUpdation(Device objUpdateDevice) throws JSONException {
        JSONObject updateDeviceJson = new JSONObject();
        return createDeviceBody(updateDeviceJson, objUpdateDevice);
    }

    //method to handle http body formation for creating device
    private String createBodyForDeviceCreation(Device objDevice) throws JSONException {
        JSONObject newDeviceJson = new JSONObject();
        if (objDevice.getDeviceId() == null) {
            Log.d(TAG, "Device Id cannot be empty");
            return null;
        }
        newDeviceJson.put("deviceId", objDevice.getDeviceId());
        return createDeviceBody(newDeviceJson, objDevice);
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
