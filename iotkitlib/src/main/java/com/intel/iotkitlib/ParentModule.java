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

import android.os.AsyncTask;
import android.util.Log;

import com.intel.iotkitlib.http.CloudResponse;
import com.intel.iotkitlib.http.HttpDeleteTask;
import com.intel.iotkitlib.http.HttpGetTask;
import com.intel.iotkitlib.http.HttpPostTask;
import com.intel.iotkitlib.http.HttpTask;
import com.intel.iotkitlib.http.HttpTaskHandler;
import com.intel.iotkitlib.utils.IotKit;
import com.intel.iotkitlib.utils.Utilities;

import org.apache.http.NameValuePair;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Base class for management module
 */
public class ParentModule {
    private final static String TAG = "ParentModule";

    // Errors
    private final static String ERR_INVALID_URL = "Http request Url Cannot be null";

    protected RequestStatusHandler statusHandler;
    protected IotKit objIotKit;
    protected List<NameValuePair> basicHeaderList;

    protected ParentModule(RequestStatusHandler statusHandler) {
        this.statusHandler = statusHandler;
        objIotKit = IotKit.getInstance();
        basicHeaderList = Utilities.createBasicHeadersWithBearerToken();
    }

    protected CloudResponse invokeHttpExecuteOnURL(String url, HttpTask httpTask) {
        if (url == null || url.isEmpty()) {
            Log.e(TAG, ERR_INVALID_URL);
            return new CloudResponse(false, ERR_INVALID_URL);
        }

        // Async mode
        if (this.statusHandler != null) {
            return httpTask.doAsync(url, new HttpTaskHandler() {
                @Override
                public void taskResponse(int responseCode, String response) {
                    statusHandler.readResponse(new CloudResponse(responseCode, response));
                }
            });
        } else {
            // Sync mode
            return httpTask.doSync(url);
        }
    }

    protected CloudResponse invokeHttpExecuteOnURL(String url, HttpTask httpTask,
                                                   final RequestStatusHandler preProcessing) {
        if (url == null || url.isEmpty()) {
            Log.e(TAG, ERR_INVALID_URL);
            return new CloudResponse(false, ERR_INVALID_URL);
        }

        // Async mode
        if (this.statusHandler != null) {
            return httpTask.doAsync(url, new HttpTaskHandler() {
                @Override
                public void taskResponse(int responseCode, String response) {
                    CloudResponse cloudResponse = new CloudResponse(responseCode, response);
                    preProcessing.readResponse(cloudResponse);
                    statusHandler.readResponse(cloudResponse);
                }
            });
        } else {
            // Sync mode
            CloudResponse response = httpTask.doSync(url);
            preProcessing.readResponse(response);
            return response;
        }
    }
}
