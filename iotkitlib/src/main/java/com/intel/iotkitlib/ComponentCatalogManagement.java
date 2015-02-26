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
import com.intel.iotkitlib.http.HttpGetTask;
import com.intel.iotkitlib.http.HttpPostTask;
import com.intel.iotkitlib.http.HttpPutTask;
import com.intel.iotkitlib.http.HttpTaskHandler;
import com.intel.iotkitlib.models.ComponentCatalog;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Component Types Catalog functions
 */
public class ComponentCatalogManagement extends ParentModule {
    private static final String TAG = "ComponentTypesCatalog";

    // Errors
    public static final String ERR_INVALID_COMMAND = "Invalid command";
    public static final String ERR_INVALID_BODY = "Invalid body";

    /**
     * The component type catalog maintains a list of capabilities that component connected to the
     * enableiot cloud expose. There are two main types of components which are sensor and actutator.
     * The catalog comes with default (built-in @link{https://github.com/enableiot/iotkit-api/wiki/Component%20Types%20List})
     * component types which are available to all accounts.
     *
     * A Component-Type ID, which is account-level unique is a concatenation of its dimension and
     * its version, seperated by a single period: e.g. "temperature.v1.0" or "humidity.v2.0".
     *
     * For more information, please refer to @link{https://github.com/enableiot/iotkit-api/wiki/Component-Types-Catalog}
     *
     * @param requestStatusHandler The handler for asynchronously request to return data and status
     *                             from the cloud.
     */
    public ComponentCatalogManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    /**
     * Get a list of all component types with minimal information.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse listAllComponentTypesCatalog() {
        //initiating get for list all component types
        HttpGetTask listAllComponents = new HttpGetTask();
        listAllComponents.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.listAllComponentTypesCatalog, null);
        return super.invokeHttpExecuteOnURL(url, listAllComponents);
    }

    /**
     * Get a list of all component types with full detailed information.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse listAllDetailsOfComponentTypesCatalog() {
        //initiating get for list all component types detailed
        HttpGetTask listAllComponentsDetails = new HttpGetTask();
        listAllComponentsDetails.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.listAllComponentTypesCatalogDetailed, null);
        return super.invokeHttpExecuteOnURL(url, listAllComponentsDetails);
    }

    /**
     * Get a complete description of a component type for a specific component.
     * @param componentId the identifier for the component to get information for.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse listComponentTypeDetails(String componentId) {
        //initiating get for component type details
        HttpGetTask componentTypeDetails = new HttpGetTask();
        componentTypeDetails.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("cmp_catalog_id", componentId);
        String url = objIotKit.prepareUrl(objIotKit.componentTypeCatalogDetails, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, componentTypeDetails);
    }

    /**
     * Create a new custom component. The dimension and version attributes are used for determining
     * if the component exists. If not, a new component is created which auto-generated id results
     * the concatenation of dimension and version values.
     * @param componentCatalog the component type to be added to the catalog
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse createCustomComponent(ComponentCatalog componentCatalog) throws JSONException {
        if (!validateActuatorCommand(componentCatalog)) {
            return new CloudResponse(false, ERR_INVALID_COMMAND);
        }
        String body;
        if ((body = createBodyForComponentCreation(componentCatalog)) == null) {
            return new CloudResponse(false, ERR_INVALID_BODY);
        }
        //initiating post for component creation
        HttpPostTask createNewComponent = new HttpPostTask();
        createNewComponent.setHeaders(basicHeaderList);
        createNewComponent.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.createCustomComponent, null);
        return super.invokeHttpExecuteOnURL(url, createNewComponent);
    }

    /**
     * Update a component type definition by creating a brand new component which definition is
     * composed by the origin component data plus the requested changes having in mind that minor
     * version info (version attribute) is incremented by 1.
     * @param componentCatalog the component type to be updated in the catalog.
     * @param componentId the identifier for the component to be updated.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse updateAComponent(ComponentCatalog componentCatalog, String componentId) throws JSONException {
        if (!validateActuatorCommand(componentCatalog)) {
            return new CloudResponse(false, ERR_INVALID_COMMAND);
        }
        String body;
        if ((body = createBodyForComponentUpdate(componentCatalog)) == null) {
            return new CloudResponse(false, ERR_INVALID_BODY);
        }
        //initiating put for component updation
        HttpPutTask updateComponent = new HttpPutTask();
        updateComponent.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("cmp_catalog_id", componentId);
        updateComponent.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.updateComponent, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, updateComponent);
    }

    private boolean validateActuatorCommand(ComponentCatalog componentCatalog) {
        if (componentCatalog.getComponentType().contentEquals("actuator") &&
                componentCatalog.getActuatorCommandParams() == null) {
            Log.i(TAG, "Command Parameters are mandatory for component catalog type \"actuator\"");
            return false;
        }
        if (!componentCatalog.getComponentType().contentEquals("actuator") &&
                componentCatalog.getCommandString() != null) {
            Log.i(TAG, "Command Json(command string and params) not required  for catalog type \"" +
                    componentCatalog.getComponentType() + "\"");
            return false;
        }
        return true;
    }

    //method to handle http body formation for creating custom component
    private String createBodyForComponentCreation(ComponentCatalog componentCatalog) throws JSONException {
        if (componentCatalog.getComponentName() == null || componentCatalog.getComponentVersion() == null ||
                componentCatalog.getComponentType() == null || componentCatalog.getComponentDataType() == null ||
                componentCatalog.getComponentFormat() == null || componentCatalog.getComponentUnit() == null ||
                componentCatalog.getComponentDisplay() == null) {
            Log.i(TAG, "Mandatory field missing to create component catalog");
            return null;
        }
        JSONObject createComponentJson = new JSONObject();
        createComponentJson.put("dimension", componentCatalog.getComponentName());
        createComponentJson.put("version", componentCatalog.getComponentVersion());
        createComponentJson.put("type", componentCatalog.getComponentType());
        createComponentJson.put("dataType", componentCatalog.getComponentDataType());
        createComponentJson.put("format", componentCatalog.getComponentFormat());
        //add min,max values if exist
        createComponentJson = addMinMaxValuesToHttpBody(componentCatalog, createComponentJson);
        createComponentJson.put("measureunit", componentCatalog.getComponentUnit());
        createComponentJson.put("display", componentCatalog.getComponentDisplay());
        //add command param, if exist
        createComponentJson = addActuatorCommandParametersToHttpBody(componentCatalog, createComponentJson);
        return createComponentJson.toString();
    }

    private JSONObject addMinMaxValuesToHttpBody(ComponentCatalog componentCatalog,
                                                 JSONObject componentJson)
            throws JSONException {
        if (componentCatalog.isMinSet()) {
            componentJson.put("min", componentCatalog.getMinValue());
        }
        if (componentCatalog.isMaxSet()) {
            componentJson.put("max", componentCatalog.getMaxValue());
        }
        return componentJson;
    }

    private JSONObject addActuatorCommandParametersToHttpBody(ComponentCatalog componentCatalog,
                                                              JSONObject componentJson)
            throws JSONException {
        //handling actuator-type params
        if (componentCatalog.getCommandString() != null) {
            JSONObject commandJson = new JSONObject();
            commandJson.put("commandString", componentCatalog.getCommandString());
            JSONArray parametersArray = new JSONArray();
            for (NameValuePair nameValuePair : componentCatalog.getActuatorCommandParams()) {
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
    private String createBodyForComponentUpdate(ComponentCatalog componentCatalog) throws JSONException {
        JSONObject updateComponentJson = new JSONObject();
        if (componentCatalog.getComponentType() != null) {
            updateComponentJson.put("type", componentCatalog.getComponentType());
        }
        if (componentCatalog.getComponentDataType() != null) {
            updateComponentJson.put("dataType", componentCatalog.getComponentDataType());
        }
        if (componentCatalog.getComponentFormat() != null) {
            updateComponentJson.put("format", componentCatalog.getComponentFormat());
        }
        if (componentCatalog.getComponentUnit() != null) {
            updateComponentJson.put("measureunit", componentCatalog.getComponentUnit());
        }
        if (componentCatalog.getComponentDisplay() != null) {
            updateComponentJson.put("display", componentCatalog.getComponentDisplay());
        }
        updateComponentJson = addMinMaxValuesToHttpBody(componentCatalog, updateComponentJson);
        updateComponentJson = addActuatorCommandParametersToHttpBody(componentCatalog, updateComponentJson);
        return updateComponentJson.toString();
    }
}
