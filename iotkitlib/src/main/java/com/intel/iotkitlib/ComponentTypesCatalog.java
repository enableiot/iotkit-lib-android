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

import com.intel.iotkitlib.http.HttpGetTask;
import com.intel.iotkitlib.http.HttpPostTask;
import com.intel.iotkitlib.http.HttpPutTask;
import com.intel.iotkitlib.http.HttpTaskHandler;
import com.intel.iotkitlib.models.CreateOrUpdateComponentCatalog;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;


public class ComponentTypesCatalog extends ParentModule {
    private static final String TAG = "ComponentTypesCatalog";

    //constructor
    public ComponentTypesCatalog(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    public boolean listAllComponentTypesCatalog() {
        //initiating get for list all component types
        HttpGetTask listAllComponents = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        listAllComponents.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.listAllComponentTypesCatalog, null);
        return super.invokeHttpExecuteOnURL(url, listAllComponents, "list all component types");
    }

    public boolean listAllDetailsOfComponentTypesCatalog() {
        //initiating get for list all component types detailed
        HttpGetTask listAllComponentsDetails = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        listAllComponentsDetails.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.listAllComponentTypesCatalogDetailed, null);
        return super.invokeHttpExecuteOnURL(url, listAllComponentsDetails, "list all component types detailed");
    }

    public boolean listComponentTypeDetails(String componentId) {
        //initiating get for component type details
        HttpGetTask componentTypeDetails = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        componentTypeDetails.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("cmp_catalog_id", componentId);
        String url = objIotKit.prepareUrl(objIotKit.componentTypeCatalogDetails, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, componentTypeDetails, "component type details");
    }

    private boolean validateActuatorCommand(CreateOrUpdateComponentCatalog createOrUpdateComponentCatalog) {
        if (createOrUpdateComponentCatalog.getComponentType().contentEquals("actuator") &&
                createOrUpdateComponentCatalog.getActuatorCommandParams() == null) {
            Log.i(TAG, "Command Parameters are mandatory for component catalog type \"actuator\"");
            return false;
        }
        if (!createOrUpdateComponentCatalog.getComponentType().contentEquals("actuator") &&
                createOrUpdateComponentCatalog.getCommandString() != null) {
            Log.i(TAG, "Command Json(command string and params) not required  for catalog type \"" +
                    createOrUpdateComponentCatalog.getComponentType() + "\"");
            return false;
        }
        return true;
    }

    public boolean createCustomComponent(CreateOrUpdateComponentCatalog createComponentCatalog) throws JSONException {
        if (!validateActuatorCommand(createComponentCatalog)) {
            return false;
        }
        String body;
        if ((body = createBodyForComponentCreation(createComponentCatalog)) == null) {
            return false;
        }
        //initiating post for component creation
        HttpPostTask createNewComponent = new HttpPostTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        createNewComponent.setHeaders(basicHeaderList);
        createNewComponent.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.createCustomComponent, null);
        return super.invokeHttpExecuteOnURL(url, createNewComponent, "create new custom component");
    }

    public boolean updateAComponent(CreateOrUpdateComponentCatalog updateComponentCatalog, String componentId) throws JSONException {
        if (!validateActuatorCommand(updateComponentCatalog)) {
            return false;
        }
        String body;
        if ((body = createBodyForComponentUpdation(updateComponentCatalog)) == null) {
            return false;
        }
        //initiating put for component updation
        HttpPutTask updateComponent = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        updateComponent.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("cmp_catalog_id", componentId);
        updateComponent.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.updateComponent, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, updateComponent, "update component");
    }

    //method to handle http body formation for creating custom component
    private String createBodyForComponentCreation(CreateOrUpdateComponentCatalog createComponentCatalog) throws JSONException {
        if (createComponentCatalog.getComponentName() == null || createComponentCatalog.getComponentVersion() == null ||
                createComponentCatalog.getComponentType() == null || createComponentCatalog.getComponentDataType() == null ||
                createComponentCatalog.getComponentFormat() == null || createComponentCatalog.getComponentUnit() == null ||
                createComponentCatalog.getComponentDisplay() == null) {
            Log.i(TAG, "Mandatory field missing to create component catalog");
            return null;
        }
        JSONObject createComponentJson = new JSONObject();
        createComponentJson.put("dimension", createComponentCatalog.getComponentName());
        createComponentJson.put("version", createComponentCatalog.getComponentVersion());
        createComponentJson.put("type", createComponentCatalog.getComponentType());
        createComponentJson.put("dataType", createComponentCatalog.getComponentDataType());
        createComponentJson.put("format", createComponentCatalog.getComponentFormat());
        //add min,max values if exist
        createComponentJson = addMinMaxValuesToHttpBody(createComponentCatalog, createComponentJson);
        createComponentJson.put("measureunit", createComponentCatalog.getComponentUnit());
        createComponentJson.put("display", createComponentCatalog.getComponentDisplay());
        //add command param, if exist
        createComponentJson = addActuatorCommandParametersToHttpBody(createComponentCatalog, createComponentJson);
        return createComponentJson.toString();
    }

    private JSONObject addMinMaxValuesToHttpBody(CreateOrUpdateComponentCatalog createOrUpdateComponentCatalog,
                                                 JSONObject componentJson)
            throws JSONException {
        if (createOrUpdateComponentCatalog.isMinSet()) {
            componentJson.put("min", createOrUpdateComponentCatalog.getMinValue());
        }
        if (createOrUpdateComponentCatalog.isMaxSet()) {
            componentJson.put("max", createOrUpdateComponentCatalog.getMaxValue());
        }
        return componentJson;
    }

    private JSONObject addActuatorCommandParametersToHttpBody(CreateOrUpdateComponentCatalog createOrUpdateComponentCatalog,
                                                              JSONObject componentJson)
            throws JSONException {
        //handling actuator-type params
        if (createOrUpdateComponentCatalog.getCommandString() != null) {
            JSONObject commandJson = new JSONObject();
            commandJson.put("commandString", createOrUpdateComponentCatalog.getCommandString());
            JSONArray parametersArray = new JSONArray();
            for (NameValuePair nameValuePair : createOrUpdateComponentCatalog.getActuatorCommandParams()) {
                JSONObject parametersJson = new JSONObject();
                parametersJson.put("name", nameValuePair.getName());
                parametersJson.put("values", nameValuePair.getValue());
                parametersArray.put(parametersJson);
            }
            commandJson.put("parameters", parametersArray);
            componentJson.put("command", commandJson);
        }
        return componentJson;
    }

    //method to handle http body formation for updating custom component
    private String createBodyForComponentUpdation(CreateOrUpdateComponentCatalog updateComponentCatalog) throws JSONException {
        JSONObject updateComponentJson = new JSONObject();
        if (updateComponentCatalog.getComponentType() != null) {
            updateComponentJson.put("type", updateComponentCatalog.getComponentType());
        }
        if (updateComponentCatalog.getComponentDataType() != null) {
            updateComponentJson.put("dataType", updateComponentCatalog.getComponentDataType());
        }
        if (updateComponentCatalog.getComponentFormat() != null) {
            updateComponentJson.put("format", updateComponentCatalog.getComponentFormat());
        }
        if (updateComponentCatalog.getComponentUnit() != null) {
            updateComponentJson.put("measureunit", updateComponentCatalog.getComponentUnit());
        }
        if (updateComponentCatalog.getComponentDisplay() != null) {
            updateComponentJson.put("display", updateComponentCatalog.getComponentDisplay());
        }
        updateComponentJson = addMinMaxValuesToHttpBody(updateComponentCatalog, updateComponentJson);
        updateComponentJson = addActuatorCommandParametersToHttpBody(updateComponentCatalog, updateComponentJson);
        return updateComponentJson.toString();
    }
}
