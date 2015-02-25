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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

//inner class to hold device details
public class Device {
    private String deviceName;
    private String deviceId;
    private String gatewayId;
    private List<String> tags;
    private List<NameValuePair> attributes;
    private List<Double> location;

    public Device(String deviceName, String deviceId, String gatewayId) {
        this.deviceName = deviceName;
        this.deviceId = deviceId;
        this.gatewayId = gatewayId;
        this.location = new LinkedList<Double>();
        this.tags = new LinkedList<String>();
        this.attributes = new LinkedList<NameValuePair>();
    }

    public void setLocation(Double latitude, Double longitude, Double height) {
        location.add(latitude);
        location.add(longitude);
        location.add(height);
    }

    public void addTagName(String tagName) {
        tags.add(tagName);
    }

    public void addAttributeInfo(String name, String value) {
        attributes.add(new BasicNameValuePair(name, value));
    }

    public String getDeviceName() { return deviceName; }

    public String getDeviceId() { return deviceId; }

    public String getGatewayId() { return gatewayId; }

    public List<String> getTags() { return tags; }

    public List<NameValuePair> getAttributes() { return attributes; }

    public List<Double> getLocation() { return location; }
}
