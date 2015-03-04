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

import android.content.Context;
import android.test.ActivityUnitTestCase;

import com.intel.iotkitlib.utils.Utilities;

import java.lang.ref.WeakReference;
import java.util.Random;

public class ApplicationTest extends ActivityUnitTestCase<MyActivity> {
    final static Integer startingValue = 1;
    final static Integer endValue = 10000;
    static String accountName;
    static String deviceName;
    static String componentName;
    static String deviceComponentName;
    static String ruleName;
    static Integer alertId;

    public ApplicationTest() {
        super(MyActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Utilities.createSharedPreferences(new WeakReference<Context>(getInstrumentation().getTargetContext()));
    }

    public String getRandomValueString() {
        Random r = new Random();
        Integer randomValue = r.nextInt(endValue - startingValue) + startingValue;
        return randomValue.toString();
    }

    public Integer getRandomValueWithInFifty() {
        Random r = new Random();
        Integer randomValue = (r.nextInt(endValue - startingValue) + startingValue) % 50;
        return randomValue;
    }

    public String getAccountName() {
        accountName = "DemoIoT" + getRandomValueString();
        return accountName;
    }

    public String getDeviceName() {
        deviceName = "DemoDevice" + getRandomValueString();
        return deviceName;
    }

    public String getComponentName() {
        componentName = "DemoComponent" + getRandomValueString();
        return componentName;
    }

    public String getDeviceComponentName() {
        deviceComponentName = "DeviceDemoComponent" + getRandomValueString();
        return deviceComponentName;
    }

    public String getRuleName() {
        ruleName = "DemoRule" + getRandomValueString();
        return ruleName;
    }

    public Integer getAlertId() {
        alertId = Integer.parseInt(getRandomValueString());
        return alertId;
    }

    /*public void testSharedPreferences() {
        assertNotNull(IoTAppUtilities.sharedPreferences);
        assertNotNull(IoTAppUtilities.editor);
    }*/
}