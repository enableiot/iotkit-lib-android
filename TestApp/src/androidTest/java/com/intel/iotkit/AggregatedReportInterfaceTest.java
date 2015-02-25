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

import com.intel.iotkitlib.AggregatedReportInterface;
import com.intel.iotkitlib.RequestStatusHandler;
import com.intel.iotkitlib.utils.AttributeFilters.AttributeFilter;

import org.json.JSONException;

public class AggregatedReportInterfaceTest extends ApplicationTest {
    private boolean serverResponse = false;

    private void waitForServerResponse(Object object) {
        synchronized (object) {
            while (!serverResponse) {
            }
            serverResponse = false;
        }
    }

    public void testAggregatedReportInterface() throws JSONException {
        AggregatedReportInterface aggregatedReportInterface = new AggregatedReportInterface(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        aggregatedReportInterface.addGatewayId("qwertyPAD1");
        //aggregatedReportInterface.addGatewayId("xxxx");
        //advancedDataEnquiry.addGatewayId("02-00-86-81-77-15");

        aggregatedReportInterface.addDeviceId("qwertyPAD1");
        //aggregatedReportInterface.addDeviceId("xxxx");
        //advancedDataEnquiry.addDeviceId("02-00-86-81-77-19");

       /* aggregatedReportInterface.addComponentId("436e9e78-8772-4898-9957-26930f5eb7e1");
        aggregatedReportInterface.addComponentId("9d44c354-7252-4494-8ade-39508bfdbdaf");
        aggregatedReportInterface.addComponentId("c4d1f4c1-6fb6-4793-b85d-431c6cba647b");*/
        aggregatedReportInterface.addComponentId("5C09B9F0-E06B-404A-A882-EAC64675A63E");
        aggregatedReportInterface.addComponentId("BB9E347D-7895-437E-9ECA-8069879090B7");

        aggregatedReportInterface.setStartTimestamp(0L);
        aggregatedReportInterface.setEndTimestamp(System.currentTimeMillis());

        //report filters
        AttributeFilter filter1 = new AttributeFilter("Tags");
        filter1.addAttributeFilterValue("created from MAC book pro");
        /*filter1.addAttributeFilterValue("California");
        filter1.addAttributeFilterValue("Sacramento");*/

        aggregatedReportInterface.addFilter(filter1);

        aggregatedReportInterface.setOutputType("json");
        aggregatedReportInterface.setLimit(10);
        aggregatedReportInterface.setOffset(0);

        aggregatedReportInterface.addAggregationMethod("average");
        aggregatedReportInterface.addAggregationMethod("min");
        /*aggregatedReportInterface.addDimension("dim1");
        aggregatedReportInterface.addDimension("dim2");
        aggregatedReportInterface.setCountOnly(true);*/
        //sort
        aggregatedReportInterface.addSortInfo("timeHour", "Asc");
        //aggregatedReportInterface.addSortInfo("sortField2", "Desc");

        assertEquals(true, aggregatedReportInterface.request());
        waitForServerResponse(aggregatedReportInterface);
    }
}
