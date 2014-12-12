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
package com.intel.iotkitlib.LibModules;

import android.util.Log;

import com.intel.iotkitlib.LibHttp.HttpPostTask;
import com.intel.iotkitlib.LibHttp.HttpTaskHandler;
import com.intel.iotkitlib.LibUtils.AttributeFilters.AttributeFilter;
import com.intel.iotkitlib.LibUtils.AttributeFilters.AttributeFilterList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class AggregatedReportInterface extends ParentModule {
    private final static String TAG = "AggregatedReportInterface";
    String msgType;
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
    AttributeFilterList filters;

    public AggregatedReportInterface(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    public void assignMembersOfAggregatedReportInterfaceWithDefaults() {
        this.startTimestamp = 0L;
        this.endTimestamp = 0L;
        this.offset = 0;
        this.limit = 0;
        this.countOnly = false;
    }

    public void setReportMessageType(String msgType) {
        this.msgType = msgType;
    }

    public void setReportStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public void setReportEndTimestamp(Long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public void addAggregationMethods(String aggregation) {
        if (this.aggregationMethods == null) {
            this.aggregationMethods = new LinkedList<String>();
        }
        this.aggregationMethods.add(aggregation);
    }

    public void addDimensions(String dimension) {
        if (this.dimensions == null) {
            this.dimensions = new LinkedList<String>();
        }
        this.dimensions.add(dimension);
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setReportCountOnly(Boolean countOnly) {
        this.countOnly = countOnly;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public void addReportDeviceIds(String deviceId) {
        if (this.deviceIds == null) {
            this.deviceIds = new LinkedList<String>();
        }
        this.deviceIds.add(deviceId);
    }

    public void addReportGatewayIds(String gatewayId) {
        if (this.gatewayIds == null) {
            this.gatewayIds = new LinkedList<String>();
        }
        this.gatewayIds.add(gatewayId);
    }

    public void addReportComponentIds(String componentId) {
        if (this.componentIds == null) {
            this.componentIds = new LinkedList<String>();
        }
        this.componentIds.add(componentId);
    }

    public void addReportSortInfo(String name, String value) {
        if (this.sort == null) {
            this.sort = new LinkedList<NameValuePair>();
        }
        this.sort.add(new BasicNameValuePair(name, value));
    }

    public void addFilters(AttributeFilter attributeFilter) {
        if (this.filters == null) {
            this.filters = new AttributeFilterList();
            this.filters.filterData = new LinkedList<AttributeFilter>();
        }
        this.filters.filterData.add(attributeFilter);
    }

    public boolean aggregatedReportInterface() throws JSONException {
        String body;
        if ((body = createBodyForAggregatedReportInterface()) == null) {
            return false;
        }
        //initiating post for aggregated report interface
        HttpPostTask reportInterface = new HttpPostTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        reportInterface.setHeaders(basicHeaderList);
        reportInterface.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.aggregatedReportInterface, null);
        return super.invokeHttpExecuteOnURL(url, reportInterface, "aggregated report interface");
    }

    public String createBodyForAggregatedReportInterface() throws JSONException {
        JSONObject reportInterfaceJson = new JSONObject();
        if (this.msgType == null) {
            reportInterfaceJson.put("msgType", "aggregatedReportRequest");
        } else {
            reportInterfaceJson.put("msgType", this.msgType);
        }
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
        reportInterfaceJson.put("startTimestamp", this.startTimestamp);
        reportInterfaceJson.put("endTimestamp", this.endTimestamp);
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
            for (AttributeFilter attributeFilter : this.filters.filterData) {
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
