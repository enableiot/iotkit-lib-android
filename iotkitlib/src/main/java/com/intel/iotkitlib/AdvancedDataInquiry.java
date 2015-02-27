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
import com.intel.iotkitlib.http.HttpPostTask;
import com.intel.iotkitlib.http.HttpTaskHandler;
import com.intel.iotkitlib.models.AttributeFilter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Advanced Data Inquiry module
 */
public class AdvancedDataInquiry extends ParentModule {
    private final static String TAG = "AdvancedDataEnquiry";

    // Errors
    public final static String ERR_INVALID_REQUEST = "Invalid body for request";

    String msgType;
    List<String> gatewayIds;
    List<String> deviceIds;
    List<String> componentIds;

    Long startTimestamp;
    Long endTimestamp;
    List<String> returnedMeasureAttributes;

    Boolean showMeasureLocation;

    List<AttributeFilter> devCompAttributeFilter;
    List<AttributeFilter> measurementAttributeFilter;
    AttributeFilter valueFilter;

    Integer componentRowLimit;
    Boolean countOnly;

    List<NameValuePair> sort;

    /**
     * Advanced Data Inquiry allows querying measurement data (values, location and attributes) for
     * a single account using advanced filtering and sorting; use this to do sync operation
     */
    public AdvancedDataInquiry() { super(null); }

    /**
     * Advanced Data Inquiry allows querying measurement data (values, location and attributes) for
     * a single account using advanced filtering and sorting.
     *
     * For more information, please refer to @link{https://github.com/enableiot/iotkit-api/wiki/Advanced-Data-Inquiry}
     *
     * @param requestStatusHandler The handler for asynchronously request to return data and status
     *                             from the cloud.
     */
    public AdvancedDataInquiry(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
        this.startTimestamp = 0L;
        this.endTimestamp = 0L;
        this.showMeasureLocation = false;
        this.componentRowLimit = 0;
        this.countOnly = false;
    }

    /**
     * Add a gateway id that is requested for the data in the report.
     * @param gatewayId The gateway identifier for gateway data to be included in the report.
     */
    public void addGatewayId(String gatewayId) {
        if (this.gatewayIds == null) {
            this.gatewayIds = new LinkedList<String>();
        }
        this.gatewayIds.add(gatewayId);
    }
    /**
     * Add a device id that is requested for the data in the report.
     * @param deviceId The device identifier for device data to be included in the report.
     */
    public void addDeviceId(String deviceId) {
        if (this.deviceIds == null) {
            this.deviceIds = new LinkedList<String>();
        }
        this.deviceIds.add(deviceId);
    }

    /**
     * Add a component id that is requested for the data in the report.
     * @param componentId The component identifier for component data to be included in the report.
     */
    public void addComponentId(String componentId) {
        if (this.componentIds == null) {
            this.componentIds = new LinkedList<String>();
        }
        this.componentIds.add(componentId);
    }

    /**
     * Set the start time for query data in be included in the report.
     * @param startTimestamp time in milliseconds since epoch time.
     */
    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    /**
     * Set the end time for query data in be included in the report.
     * @param endTimestamp time in milliseconds since epoch time.
     */
    public void setEndTimestamp(Long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    /**
     * Add a requested attribute to a list of attributes that will be return for each measurement.
     * @param attribute The attribute to add to the list of attributes that will be part of the request.
     */
    public void addReturnedMeasureAttribute(String attribute) {
        if (this.returnedMeasureAttributes == null) {
            this.returnedMeasureAttributes = new LinkedList<String>();
        }
        this.returnedMeasureAttributes.add(attribute);
    }

    /**
     * Request for location (lat, long, alt) as part of each returned measurement.
     * @param measureLocation if true returns location.
     */
    public void setShowMeasureLocation(Boolean measureLocation) {
        this.showMeasureLocation = measureLocation;
    }

    /**
     * Filter request based on device and/or component attributes.
     * @param attributeFilter is of name and values.
     */
    public void addDevCompAttributeFilter(AttributeFilter attributeFilter) {
        if (this.devCompAttributeFilter == null) {
            this.devCompAttributeFilter = new LinkedList<AttributeFilter>();
        }
        this.devCompAttributeFilter.add(attributeFilter);
    }

    /**
     * Filter request based on measurement attributes.
     * @param attributeFilter is of name and values.
     */
    public void addMeasurementAttributeFilter(AttributeFilter attributeFilter) {
        if (this.measurementAttributeFilter == null) {
            this.measurementAttributeFilter = new LinkedList<AttributeFilter>();
        }
        this.measurementAttributeFilter.add(attributeFilter);
    }

    /**
     * Filter request based on value attributes.
     * @param attributeFilter is of name and values.
     */
    public void addValueFilter(AttributeFilter attributeFilter) {
        this.valueFilter = attributeFilter;
    }

    /**
     * Limits the number of records returned for each component in the report.
     * @param componentRowLimit the number of row that will be returned.
     */
    public void setComponentRowLimit(int componentRowLimit) {
        this.componentRowLimit = componentRowLimit;
    }

    /**
     * Setting to true will return the number of rows that would have returned from this report.
     * @param countOnly true will return number of rows.
     */
    public void setCountOnly(Boolean countOnly) {
        this.countOnly = countOnly;
    }

    /**
     * Add the sort criteria to the query.
     * @param name The sort field name.
     * @param value The source value. It must be "Asc" or "Desc".
     */
    public void addSortInfo(String name, String value) {
        if (this.sort == null) {
            this.sort = new LinkedList<NameValuePair>();
        }
        this.sort.add(new BasicNameValuePair(name, value));
    }

    /**
     * Starts a request for the report.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse request() throws JSONException {
        String body;
        if ((body = createBodyForAdvancedDataInquiry()) == null) {
            return new CloudResponse(false, ERR_INVALID_REQUEST);
        }
        //initiating post for advanced data inquiry
        HttpPostTask advancedDataInquiry = new HttpPostTask();
        advancedDataInquiry.setHeaders(basicHeaderList);
        advancedDataInquiry.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.advancedEnquiryOfData, null);
        return super.invokeHttpExecuteOnURL(url, advancedDataInquiry);
    }

    private String createBodyForAdvancedDataInquiry() throws JSONException {
        JSONObject dataInquiryJson = new JSONObject();
        if (this.msgType == null) {
            dataInquiryJson.put("msgType", "advancedDataInquiryRequest");
        } else {
            dataInquiryJson.put("msgType", this.msgType);
        }
        if (this.gatewayIds != null) {
            JSONArray gatewayArray = new JSONArray();
            for (String gatewayId : this.gatewayIds) {
                gatewayArray.put(gatewayId);
            }
            dataInquiryJson.put("gatewayIds", gatewayArray);
        }
        if (this.deviceIds != null) {
            JSONArray deviceIdArray = new JSONArray();
            for (String deviceId : this.deviceIds) {
                deviceIdArray.put(deviceId);
            }
            dataInquiryJson.put("deviceIds", deviceIdArray);
        }
        if (this.componentIds != null) {
            JSONArray componentIdArray = new JSONArray();
            for (String componentId : this.componentIds) {
                componentIdArray.put(componentId);
            }
            dataInquiryJson.put("componentIds", componentIdArray);
        }
        //dataInquiryJson.put("startTimestamp", this.startTimestamp);
        //dataInquiryJson.put("endTimestamp", this.endTimestamp);
        dataInquiryJson.put("from", this.startTimestamp);
        dataInquiryJson.put("to", this.endTimestamp);
        //returnedMeasureAttributes
        if (this.returnedMeasureAttributes != null) {
            JSONArray returnedMeasureAttributesArray = new JSONArray();
            for (String attribute : this.returnedMeasureAttributes) {
                returnedMeasureAttributesArray.put(attribute);
            }
            dataInquiryJson.put("returnedMeasureAttributes", returnedMeasureAttributesArray);
        }
        if (this.showMeasureLocation) {
            dataInquiryJson.put("showMeasureLocation", this.showMeasureLocation);
        }
        if (this.componentRowLimit > 0) {
            dataInquiryJson.put("componentRowLimit", this.componentRowLimit);
        }
        //sort
        if (this.sort != null) {
            JSONArray sortArray = new JSONArray();
            for (NameValuePair nameValuePair : this.sort) {
                JSONObject nameValueJson = new JSONObject();
                nameValueJson.put(nameValuePair.getName(), nameValuePair.getValue());
                sortArray.put(nameValueJson);
            }
            dataInquiryJson.put("sort", sortArray);
        }
        if (this.countOnly) {
            dataInquiryJson.put("countOnly", this.countOnly);
        }
        if (this.devCompAttributeFilter != null) {
            JSONObject devCompAttributeJson = new JSONObject();
            for (AttributeFilter attributeFilter : this.devCompAttributeFilter) {
                JSONArray filterValuesArray = new JSONArray();
                for (String filterValue : attributeFilter.filterValues) {
                    filterValuesArray.put(filterValue);
                }
                devCompAttributeJson.put(attributeFilter.filterName, filterValuesArray);
            }
            dataInquiryJson.put("devCompAttributeFilter", devCompAttributeJson);
        }
        if (this.measurementAttributeFilter != null) {
            JSONObject measurementAttributeJson = new JSONObject();
            for (AttributeFilter attributeFilter : this.measurementAttributeFilter) {
                JSONArray filterValuesArray = new JSONArray();
                for (String filterValue : attributeFilter.filterValues) {
                    filterValuesArray.put(filterValue);
                }
                measurementAttributeJson.put(attributeFilter.filterName, filterValuesArray);
            }
            dataInquiryJson.put("measurementAttributeFilter", measurementAttributeJson);
        }
        if (this.valueFilter != null) {
            JSONObject valueFilterJson = new JSONObject();
            JSONArray filterValuesArray = new JSONArray();
            for (String filterValue : this.valueFilter.filterValues) {
                filterValuesArray.put(filterValue);
            }
            valueFilterJson.put(this.valueFilter.filterName, filterValuesArray);
            dataInquiryJson.put("valueFilter", valueFilterJson);
        }
        return dataInquiryJson.toString();
    }
}
