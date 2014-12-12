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
package com.intel.iotkitlib.LibModules.AuthorizationManagement;

import android.util.Log;

import com.intel.iotkitlib.LibHttp.HttpGetTask;
import com.intel.iotkitlib.LibHttp.HttpPostTask;
import com.intel.iotkitlib.LibHttp.HttpTaskHandler;
import com.intel.iotkitlib.LibModules.ParentModule;
import com.intel.iotkitlib.LibModules.RequestStatusHandler;
import com.intel.iotkitlib.LibUtils.IotKit;
import com.intel.iotkitlib.LibUtils.Utilities;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.util.List;


public class Authorization extends ParentModule {
    private static final String TAG = "Authorization";

    public Authorization(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    public boolean getNewAuthorizationToken(String username, String password) {
        if (username == null) {
            Log.d(TAG, "username empty");
            return false;
        }
        if (password == null) {
            Log.d(TAG, "password empty");
            return false;
        }
        //adding header pair
        List<NameValuePair> headers = Utilities.addHttpHeaders(Utilities.createEmptyListForHeaders(), IotKit.HEADER_CONTENT_TYPE_NAME, IotKit.HEADER_CONTENT_TYPE_JSON);
        //populating the JSON body
        String body = "{\"username\":" + "\"" + username + "\"," + "\"password\":" + "\"" + password + "\"}";
        //initiating post for authorization
        HttpPostTask getAuthToken = new HttpPostTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                //parse and store auth-token
                try {
                    AuthorizationToken.parseAndStoreAuthorizationToken(response, responseCode);
                } catch (JSONException je) {
                    je.printStackTrace();
                }
                statusHandler.readResponse(responseCode, response);
            }
        });
        getAuthToken.setHeaders(headers);
        getAuthToken.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.newAuthToken, null);
        return super.invokeHttpExecuteOnURL(url, getAuthToken, "new auth token");

    }

    public boolean getAuthorizationTokenInfo() {
        //building basic header contains content-type and bearer token
        List<NameValuePair> headers;
        if ((headers = Utilities.createBasicHeadersWithBearerToken()) == null) {
            Log.d(TAG, "bearer token cannot be empty");
            return false;
        }
        //initiating get for token info
        HttpGetTask getTokenInfo = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                //store auth-token related info(account-id,..)
                try {
                    AuthorizationToken.parseAndStoreAuthorizationTokenInfo(response, responseCode);
                } catch (JSONException je) {
                    je.printStackTrace();
                }
                statusHandler.readResponse(responseCode, response);
            }
        });
        getTokenInfo.setHeaders(headers);
        String url = objIotKit.prepareUrl(objIotKit.authTokenInfo, null);
        return super.invokeHttpExecuteOnURL(url, getTokenInfo, "auth token info");
    }

    public boolean validateAuthToken() {
        //building basic header contains content-type and bearer token
        List<NameValuePair> headers;
        if ((headers = Utilities.createBasicHeadersWithBearerToken()) == null) {
            Log.d(TAG, "bearer token cannot be empty");
            return false;
        }
        //initiating get for validating token info
        HttpGetTask validateToken = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        validateToken.setHeaders(headers);
        String url = objIotKit.prepareUrl(objIotKit.authTokenInfo, null);
        return super.invokeHttpExecuteOnURL(url, validateToken, "validate auth token");
    }
}

