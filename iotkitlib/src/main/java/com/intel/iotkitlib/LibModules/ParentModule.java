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

import android.os.AsyncTask;
import android.util.Log;

import com.intel.iotkitlib.LibHttp.CloudResponse;
import com.intel.iotkitlib.LibUtils.IotKit;
import com.intel.iotkitlib.LibUtils.Utilities;

import org.apache.http.NameValuePair;

import java.util.List;


public class ParentModule {
    private final static String TAG = "ParentModule";
    protected RequestStatusHandler statusHandler;
    protected IotKit objIotKit;
    protected List<NameValuePair> basicHeaderList;

    protected ParentModule(RequestStatusHandler statusHandler) {
        this.statusHandler = statusHandler;
        objIotKit = IotKit.getInstance();
        basicHeaderList = Utilities.createBasicHeadersWithBearerToken();
    }

    protected boolean invokeHttpExecuteOnURL(String url, AsyncTask<String, Void, CloudResponse> httpTask, String taskDescription) {
        //try {
        //httpTask.execute(url).get();
        if (url == null) {
            Log.i(TAG, "Http request Url Cannot be null");
        }
        httpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        return true;
        //}
         /*catch (InterruptedException e) {
            Log.e(taskDescription, "InterruptedException", e);
            return false;
        } catch (ExecutionException e) {
            Log.e(taskDescription, "ExecutionException", e);
            return false;
        }*/
    }
}
