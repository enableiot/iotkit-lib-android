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
    private final static Boolean ASYNC = true;

    protected RequestStatusHandler statusHandler;
    protected IotKit objIotKit;
    protected List<NameValuePair> basicHeaderList;

    protected ParentModule(RequestStatusHandler statusHandler) {
        this.statusHandler = statusHandler;
        objIotKit = IotKit.getInstance();
        basicHeaderList = Utilities.createBasicHeadersWithBearerToken();
    }

    protected boolean invokeHttpExecuteOnURL(String url, AsyncTask<String, Void, CloudResponse> httpTask, String taskDescription) {
        if (url == null || url.isEmpty()) {
            Log.e(TAG, "Http request Url Cannot be null");
            return false;
        }

        if (ASYNC) {
            httpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            return true;
        } else {
            try {
                httpTask.execute(url).get();
                return true;
            } catch (InterruptedException e) {
                Log.e(taskDescription, "InterruptedException", e);
                return false;
            } catch (ExecutionException e) {
                Log.e(taskDescription, "ExecutionException", e);
                return false;
            }
        }
    }
}
