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

import com.intel.iotkitlib.AdvancedDataInquiry;
import com.intel.iotkitlib.RequestStatusHandler;
import com.intel.iotkitlib.utils.AttributeFilters.AttributeFilter;

import org.json.JSONException;

public class AdvancedDataInquiryTest extends ApplicationTest {
    private boolean serverResponse = false;

    private void waitForServerResponse(Object object) {
        synchronized (object) {
            while (!serverResponse) {
            }
            serverResponse = false;
        }
    }

    public void testAdvancedDataInquiry() throws JSONException {
        AdvancedDataInquiry objAdvancedDataInquiry = new AdvancedDataInquiry(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        objAdvancedDataInquiry.addGatewayId("qwertyPAD1");
        objAdvancedDataInquiry.addGatewayId("devTest");
        //objAdvancedDataInquiry.addGatewayId("02-00-86-81-77-15");

        objAdvancedDataInquiry.addDeviceId("qwertyPAD1");
        objAdvancedDataInquiry.addDeviceId("devTest");
        //objAdvancedDataInquiry.addDeviceId("02-00-86-81-77-19");

        objAdvancedDataInquiry.addComponentId("5C09B9F0-E06B-404A-A882-EAC64675A63E");
        objAdvancedDataInquiry.addComponentId("BB9E347D-7895-437E-9ECA-8069879090B7");
        objAdvancedDataInquiry.addComponentId("b780757a-4a45-40f3-9804-60eee953e8d2");
        //objAdvancedDataInquiry.addComponentId("c4d1f4c1-6fb6-4793-b85d-431c6cba647b");

        objAdvancedDataInquiry.setStartTimestamp(0L);
        objAdvancedDataInquiry.setEndTimestamp(System.currentTimeMillis());

        /*objAdvancedDataInquiry.addReturnedMeasureAttribute("attr_1");
        objAdvancedDataInquiry.addReturnedMeasureAttribute("attr_2");
        objAdvancedDataInquiry.addReturnedMeasureAttribute("attr_3");*/

        //objAdvancedDataInquiry.setShowMeasureLocation(true);
        //dev comp attrs
        AttributeFilter devCompAttributeFilter1 = new AttributeFilter("Tags");
        devCompAttributeFilter1.addAttributeFilterValue("created from MAC book pro");
        devCompAttributeFilter1.addAttributeFilterValue("intel");
        //devCompAttributeFilter1.addAttributeFilterValues("Sacramento");

        AttributeFilter devCompAttributeFilter2 = new AttributeFilter("componentType");
        devCompAttributeFilter2.addAttributeFilterValue("temperature");
        /*devCompAttributeFilter2.addAttributeFilterValue("value22");
        devCompAttributeFilter2.addAttributeFilterValue("value33");*/

        objAdvancedDataInquiry.addDevCompAttributeFilter(devCompAttributeFilter1);
        objAdvancedDataInquiry.addDevCompAttributeFilter(devCompAttributeFilter2);
        //measure comp attrs
        /*AttributeFilter measurementAttributeFilter1 = new AttributeFilter("mfilter_1");
        measurementAttributeFilter1.addAttributeFilterValue("mValue1");
        measurementAttributeFilter1.addAttributeFilterValue("mValue2");
        measurementAttributeFilter1.addAttributeFilterValue("mValue3");

        AttributeFilter measurementAttributeFilter2 = new AttributeFilter("mfilter_2");
        measurementAttributeFilter2.addAttributeFilterValue("mValue11");
        measurementAttributeFilter2.addAttributeFilterValue("mValue22");
        measurementAttributeFilter2.addAttributeFilterValue("mValue33");

        objAdvancedDataInquiry.addMeasurementAttributeFilter(measurementAttributeFilter1);
        objAdvancedDataInquiry.addMeasurementAttributeFilter(measurementAttributeFilter2);

        //value filter
        AttributeFilter valueFilter = new AttributeFilter("value");
        valueFilter.addAttributeFilterValue("filter_value1");
        valueFilter.addAttributeFilterValue("filter_value2");
        valueFilter.addAttributeFilterValue("filter_value3");
        objAdvancedDataInquiry.addValueFilter(valueFilter);*/

        /*objAdvancedDataInquiry.setComponentRowLimit(5);
        objAdvancedDataInquiry.setCountOnly(true);*/
        //sort
        objAdvancedDataInquiry.addSortInfo("Timestamp", "Asc");
        objAdvancedDataInquiry.addSortInfo("Value", "Desc");

        assertEquals(true, objAdvancedDataInquiry.request());
        waitForServerResponse(objAdvancedDataInquiry);
    }
}
