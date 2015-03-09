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
package com.intel.iotkitlib.http;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Handle the main part of HTTP commands
 */
public abstract class HttpTask {
    private static final String TAG = "HttpPostTask";
    private List<NameValuePair> headerList;
    private String httpBody;

    private AsyncTask<String, Void, CloudResponse> asyncTask;

    protected CloudResponse HTTPAsync(final String operation, String url, final HttpTaskHandler taskHandler) {
        asyncTask = new AsyncTask<String, Void, CloudResponse>() {
            @Override
            protected CloudResponse doInBackground(String... urls) {
                return HTTPSync(operation, urls[0]);
            }
            protected void onPostExecute(CloudResponse response) {
                // Done on UI Thread
                if (response != null && taskHandler != null) {
                    taskHandler.taskResponse(response.code, response.response);
                }
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        return new CloudResponse(true, "Successful execute " + url);
    }

    protected CloudResponse HTTPSync(final String operation, String url) {
        try {
            URL myURL = new URL(url);
            HttpURLConnection httpcon = (HttpURLConnection)myURL.openConnection();
            httpcon.setRequestMethod(operation);
            httpcon.setDoInput(true);

            //adding headers one by one
            for (NameValuePair nvp : headerList) {
                httpcon.setRequestProperty(nvp.getName(), nvp.getValue());
            }

            // Write data to the connection
            if (httpBody != null) {
                byte[] postData = httpBody.getBytes(Charset.forName("UTF-8"));
                httpcon.setRequestProperty("Charset", "utf-8");
                httpcon.setRequestProperty("Content-Length", Integer.toString(postData.length));
                httpcon.setRequestProperty("Content-Type", "application/json");
                httpcon.setRequestProperty("Accept", "application/json");
                httpcon.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(httpcon.getOutputStream());
                wr.write(postData);
            }

            // Force HttpURLConnection to run the request
            // Otherwise getErrorStream always returns null
            int code = httpcon.getResponseCode();
            int byteRead = 0;
            InputStream is = httpcon.getErrorStream();
            if (is == null) {
                is = httpcon.getInputStream();
            }
            StringBuffer sb = new StringBuffer();
            byte buffer[] = new byte[512];
            while ((byteRead = is.read(buffer)) >= 0) {
                String b = new String(buffer, 0, byteRead, "UTF-8");
                sb.append(b);
            }

            return new CloudResponse(code, sb.toString());
        } catch (java.net.ConnectException cEx) {
            Log.e(TAG, cEx.getMessage());
            return new CloudResponse(false, cEx.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return new CloudResponse(false, e.getMessage());
        }
    }

    public void setHeaders(List<NameValuePair> headers) {
        this.headerList = headers;
    }

    public void setRequestBody(String httpBody) {
        this.httpBody = httpBody;
    }

    abstract public CloudResponse doAsync(String url, final HttpTaskHandler taskHandler);

    abstract public CloudResponse doSync(String url);
}
