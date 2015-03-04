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
 * Interface for query aggregated metrics data in a single account.
 */
public class AggregatedReportInterface extends ParentModule {
    private final static String TAG = "AggregatedRpttInterface";

    // Errors
    public final static String ERR_INVALID_REQUEST = "Invalid body for request";

    Long startTimestamp;
    Long endTimestamp;
    List<String> aggregationMethods;
    List<String> dimensions;
    Integer offset;
    Integer limit;
    Boolean countOnly;
    String outputType;
    List<String> gatewayIds;
    List<String> deviceIds;
    List<String> componentIds;
    List<NameValuePair> sort;
    List<AttributeFilter> filters;

    /**
     * Interface for query aggregated metrics data in a single account; use this to do sync operation
     */
    public AggregatedReportInterface() { super(null); }

    /**
     * Interface for query aggregated metrics data in a single account.
     *
     * For more information, please refer to @link{https://github.com/enableiot/iotkit-api/wiki/Aggregated-Report-Interface}
     *
     * @param requestStatusHandler The handler for asynchronously request to return data and status
     *                             from the cloud.
     */
    public AggregatedReportInterface(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
        this.startTimestamp = 0L;
        this.endTimestamp = 0L;
        this.offset = 0;
        this.limit = 0;
        this.countOnly = false;
        this.outputType = "json";
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
     * Add the aggregation method to the query request.
     * @param aggregation The aggregation method from the list of acceptable values of "min", "max",
     *                    "average", "std", "count" and "sum". The default is "average".
     */
    public void addAggregationMethod(String aggregation) {
        if (this.aggregationMethods == null) {
            this.aggregationMethods = new LinkedList<String>();
        }
        this.aggregationMethods.add(aggregation);
    }

    /**
     * Add a dimension to a list of dimensions for measurements aggregation of the report.
     * @param dimension A dimension of either "timeHour", "componentName" or "deviceAttribute_any".
     */
    public void addDimension(String dimension) {
        if (this.dimensions == null) {
            this.dimensions = new LinkedList<String>();
        }
        this.dimensions.add(dimension);
    }

    /**
     * The first row of the data to return in the report. If this is set the limit must be set also.
     * @param offset The offset into the row of data to retrieve the report.
     */
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    /**
     * The maximum number of rows to include in the report.
     * @param limit the limit for the number of records return.
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * Setting to true will return the number of rows that would have returned from this report.
     * @param countOnly true will return number of rows
     */
    public void setCountOnly(Boolean countOnly) {
        this.countOnly = countOnly;
    }

    /**
     * Set the output type of the report to be returned.
     * @param outputType The desired output type of the data. The available options are "csv" or "json".
     *                   The default is "json".
     */
    public void setOutputType(String outputType) {
        this.outputType = outputType;
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
     * Add attribute filter.
     * @param attributeFilter the filter to be added.
     */
    public void addFilter(AttributeFilter attributeFilter) {
        if (this.filters == null) {
            this.filters = new LinkedList<AttributeFilter>();
        }
        this.filters.add(attributeFilter);
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
        if ((body = createBodyForAggregatedReportInterface()) == null) {
            return new CloudResponse(false, ERR_INVALID_REQUEST);
        }
        //initiating post for aggregated report interface
        HttpPostTask reportInterface = new HttpPostTask();
        reportInterface.setHeaders(basicHeaderList);
        reportInterface.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.aggregatedReportInterface, null);
        return super.invokeHttpExecuteOnURL(url, reportInterface);
    }

    private String createBodyForAggregatedReportInterface() throws JSONException {
        JSONObject reportInterfaceJson = new JSONObject();
        reportInterfaceJson.put("offset", this.offset);
        reportInterfaceJson.put("limit", this.limit);
        if (this.countOnly) {
            reportInterfaceJson.put("countOnly", this.countOnly);
        }
        if (this.outputType != null) {
            reportInterfaceJson.put("outputType", this.outputType);
        }
        if (this.aggregationMethods != null) {
            JSONArray aggregationArray = new JSONArray();
            for (String aggregationMethod : this.aggregationMethods) {
                aggregationArray.put(aggregationMethod);
            }
            reportInterfaceJson.put("aggregationMethods", aggregationArray);
        }
        if (this.dimensions != null) {
            JSONArray dimensionArray = new JSONArray();
            for (String dimension : this.dimensions) {
                dimensionArray.put(dimension);
            }
            reportInterfaceJson.put("dimensions", dimensionArray);
        }
        if (this.gatewayIds != null) {
            JSONArray gatewayArray = new JSONArray();
            for (String gatewayId : this.gatewayIds) {
                gatewayArray.put(gatewayId);
            }
            reportInterfaceJson.put("gatewayIds", gatewayArray);
        }
        if (this.deviceIds != null) {
            JSONArray deviceIdArray = new JSONArray();
            for (String deviceId : this.deviceIds) {
                deviceIdArray.put(deviceId);
            }
            reportInterfaceJson.put("deviceIds", deviceIdArray);
        }
        if (this.componentIds != null) {
            JSONArray componentIdArray = new JSONArray();
            for (String componentId : this.componentIds) {
                componentIdArray.put(componentId);
            }
            reportInterfaceJson.put("componentIds", componentIdArray);
        }
        //reportInterfaceJson.put("startTimestamp", this.startTimestamp);
        //reportInterfaceJson.put("endTimestamp", this.endTimestamp);
        reportInterfaceJson.put("from", this.startTimestamp);
        reportInterfaceJson.put("to", this.endTimestamp);
        //sort
        if (this.sort != null) {
            JSONArray sortArray = new JSONArray();
            for (NameValuePair nameValuePair : this.sort) {
                JSONObject nameValueJson = new JSONObject();
                nameValueJson.put(nameValuePair.getName(), nameValuePair.getValue());
                sortArray.put(nameValueJson);
            }
            reportInterfaceJson.put("sort", sortArray);
        }
        if (this.filters != null) {
            JSONObject filterJson = new JSONObject();
            for (AttributeFilter attributeFilter : this.filters) {
                JSONArray filterValuesArray = new JSONArray();
                for (String filterValue : attributeFilter.filterValues) {
                    filterValuesArray.put(filterValue);
                }
                filterJson.put(attributeFilter.filterName, filterValuesArray);
            }
            reportInterfaceJson.put("filters", filterJson);
        }
        return reportInterfaceJson.toString();
    }
}
