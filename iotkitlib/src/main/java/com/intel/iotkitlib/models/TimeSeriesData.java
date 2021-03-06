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
package com.intel.iotkitlib.models;

import java.util.LinkedList;
import java.util.List;


public class TimeSeriesData {
    Long fromTimeInMillis;
    Long toTimeInMillis;
    List<String> deviceList;
    List<String> componentIdList;

    public TimeSeriesData(Long fromTimeInMillis, Long toTimeInMillis) {
        this.fromTimeInMillis = fromTimeInMillis;
        this.toTimeInMillis = toTimeInMillis;
        this.deviceList = null;
        this.componentIdList = null;
    }

    public void addDeviceId(String deviceId) {
        if (deviceList == null) {
            deviceList = new LinkedList<String>();
        }
        deviceList.add(deviceId);
    }

    public void addComponentId(String componentId) {
        if (componentIdList == null) {
            componentIdList = new LinkedList<String>();
        }
        componentIdList.add(componentId);
    }

    public Long getFromTimeInMillis() { return fromTimeInMillis; }

    public Long getToTimeInMillis() { return toTimeInMillis; }

    public List<String> getDeviceList() { return deviceList; }

    public List<String> getComponentIdList() { return componentIdList; }
}
