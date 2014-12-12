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

import com.intel.iotkitlib.LibModules.AdvancedDataEnquiry;
import com.intel.iotkitlib.LibModules.RequestStatusHandler;
import com.intel.iotkitlib.LibUtils.AttributeFilters.AttributeFilter;

import org.json.JSONException;

public class AdvancedDataEnquiryTest extends ApplicationTest {
    private boolean serverResponse = false;

    private void waitForServerResponse(Object object) {
        synchronized (object) {
            while (!serverResponse) {
            }
            serverResponse = false;
        }
    }

    public void testAdvancedDataEnquiry() throws JSONException {
        AdvancedDataEnquiry objAdvancedDataEnquiry = new AdvancedDataEnquiry(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        objAdvancedDataEnquiry.assignMembersOfAdvancedDataEnquiryWithDefaults();
        objAdvancedDataEnquiry.addGatewayIds("qwertyPAD1");
        objAdvancedDataEnquiry.addGatewayIds("devTest");
        //objAdvancedDataEnquiry.addGatewayIds("02-00-86-81-77-15");

        objAdvancedDataEnquiry.addDeviceIds("qwertyPAD1");
        objAdvancedDataEnquiry.addDeviceIds("devTest");
        //objAdvancedDataEnquiry.addDeviceIds("02-00-86-81-77-19");

        objAdvancedDataEnquiry.addComponentIds("5C09B9F0-E06B-404A-A882-EAC64675A63E");
        objAdvancedDataEnquiry.addComponentIds("BB9E347D-7895-437E-9ECA-8069879090B7");
        objAdvancedDataEnquiry.addComponentIds("b780757a-4a45-40f3-9804-60eee953e8d2");
        //objAdvancedDataEnquiry.addComponentIds("c4d1f4c1-6fb6-4793-b85d-431c6cba647b");

        objAdvancedDataEnquiry.setStartTimestamp(0L);
        objAdvancedDataEnquiry.setEndTimestamp(System.currentTimeMillis());

        /*objAdvancedDataEnquiry.addReturnedMeasureAttributes("attr_1");
        objAdvancedDataEnquiry.addReturnedMeasureAttributes("attr_2");
        objAdvancedDataEnquiry.addReturnedMeasureAttributes("attr_3");*/

        //objAdvancedDataEnquiry.setShowMeasureLocation(true);
        //dev comp attrs
        AttributeFilter devCompAttributeFilter1 = new AttributeFilter("Tags");
        devCompAttributeFilter1.addAttributeFilterValues("created from MAC book pro");
        devCompAttributeFilter1.addAttributeFilterValues("intel");
        //devCompAttributeFilter1.addAttributeFilterValues("Sacramento");

        AttributeFilter devCompAttributeFilter2 = new AttributeFilter("componentType");
        devCompAttributeFilter2.addAttributeFilterValues("temperature");
        /*devCompAttributeFilter2.addAttributeFilterValues("value22");
        devCompAttributeFilter2.addAttributeFilterValues("value33");*/

        objAdvancedDataEnquiry.addDevCompAttributeFilter(devCompAttributeFilter1);
        objAdvancedDataEnquiry.addDevCompAttributeFilter(devCompAttributeFilter2);
        //measure comp attrs
        /*AttributeFilter measurementAttributeFilter1 = new AttributeFilter("mfilter_1");
        measurementAttributeFilter1.addAttributeFilterValues("mValue1");
        measurementAttributeFilter1.addAttributeFilterValues("mValue2");
        measurementAttributeFilter1.addAttributeFilterValues("mValue3");

        AttributeFilter measurementAttributeFilter2 = new AttributeFilter("mfilter_2");
        measurementAttributeFilter2.addAttributeFilterValues("mValue11");
        measurementAttributeFilter2.addAttributeFilterValues("mValue22");
        measurementAttributeFilter2.addAttributeFilterValues("mValue33");

        objAdvancedDataEnquiry.addMeasurementAttributeFilter(measurementAttributeFilter1);
        objAdvancedDataEnquiry.addMeasurementAttributeFilter(measurementAttributeFilter2);

        //value filter
        AttributeFilter valueFilter = new AttributeFilter("value");
        valueFilter.addAttributeFilterValues("filter_value1");
        valueFilter.addAttributeFilterValues("filter_value2");
        valueFilter.addAttributeFilterValues("filter_value3");
        objAdvancedDataEnquiry.addValueFilter(valueFilter);*/

        /*objAdvancedDataEnquiry.setComponentRowLimit(5);
        objAdvancedDataEnquiry.setCountOnly(true);*/
        //sort
        objAdvancedDataEnquiry.addSortInfo("Timestamp", "Asc");
        objAdvancedDataEnquiry.addSortInfo("Value", "Desc");

        assertEquals(true, objAdvancedDataEnquiry.advancedDataInquiry());
        waitForServerResponse(objAdvancedDataEnquiry);
    }
}
