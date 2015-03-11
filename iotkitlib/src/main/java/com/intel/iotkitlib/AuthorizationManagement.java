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

import android.util.Log;

import com.intel.iotkitlib.http.CloudResponse;
import com.intel.iotkitlib.http.HttpGetTask;
import com.intel.iotkitlib.http.HttpPostTask;
import com.intel.iotkitlib.http.HttpTaskHandler;
import com.intel.iotkitlib.models.AuthorizationToken;
import com.intel.iotkitlib.utils.IotKit;
import com.intel.iotkitlib.utils.Utilities;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.util.List;

/**
 * Authorization functions
 */
public class AuthorizationManagement extends ParentModule {
    private static final String TAG = "Authorization";

    // Errors
    public static final String ERR_INVALID_USERNAME = "Invalid user name";
    public static final String ERR_EMPTY_PASSWORD = "Empty password";
    public static final String ERR_INVALID_BEARER = "bearer token cannot be empty";

    /**
     * Module that handles authorization of user; use this to do sync operation.
     */
    public AuthorizationManagement() { super(null); }

    /**
     * Module that handles authorization of user.
     *
     * For more information, please refer to @link{https://github.com/enableiot/iotkit-api/wiki/Authorization}
     *
     * @param requestStatusHandler The handler for asynchronously request to return data and status
     *                             from the cloud.
     */
    public AuthorizationManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    /**
     * Get the JWT token for the user.
     * @param username the user name that identifies the user. This is usually an email address.
     * @param password the password for the user.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse getNewAuthorizationToken(String username, String password) {
        if (username == null) {
            Log.d(TAG, ERR_INVALID_USERNAME);
            return new CloudResponse(false, ERR_INVALID_USERNAME);
        }
        if (password == null) {
            Log.d(TAG, ERR_EMPTY_PASSWORD);
            return new CloudResponse(false, ERR_EMPTY_PASSWORD);
        }
        //adding header pair
        List<NameValuePair> headers = Utilities.addHttpHeaders(Utilities.createEmptyListForHeaders(),
                IotKit.HEADER_CONTENT_TYPE_NAME, IotKit.HEADER_CONTENT_TYPE_JSON);
        //populating the JSON body
        String body = "{\"username\":" + "\"" + username + "\"," + "\"password\":" + "\"" + password + "\"}";
        //initiating post for authorization
        HttpPostTask getAuthToken = new HttpPostTask();

        RequestStatusHandler preProcessing = new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                Log.d(TAG, String.valueOf(response.getCode()));
                Log.d(TAG, response.getResponse());
                //parse and store auth-token
                try {
                    AuthorizationToken.parseAndStoreAuthorizationToken(response.getResponse(),
                                                                       response.getCode());
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        };
        getAuthToken.setHeaders(headers);
        getAuthToken.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.newAuthToken, null);
        return super.invokeHttpExecuteOnURL(url, getAuthToken, preProcessing);

    }

    /**
     * Get user JWT token information.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse getAuthorizationTokenInfo() {
        //building basic header contains content-type and bearer token
        List<NameValuePair> headers;
        if ((headers = Utilities.createBasicHeadersWithBearerToken()) == null) {
            Log.d(TAG, ERR_INVALID_BEARER);
            return new CloudResponse(false, ERR_INVALID_BEARER);
        }
        //initiating get for token info
        HttpGetTask getTokenInfo = new HttpGetTask();
        RequestStatusHandler preProcessing = new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                Log.d(TAG, String.valueOf(response.getCode()));
                Log.d(TAG, response.getResponse());
                //parse and store auth-token
                try {
                    AuthorizationToken.parseAndStoreAuthorizationTokenInfo(response.getResponse(),
                            response.getCode());
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        };

        getTokenInfo.setHeaders(headers);
        String url = objIotKit.prepareUrl(objIotKit.authTokenInfo, null);
        return super.invokeHttpExecuteOnURL(url, getTokenInfo, preProcessing);
    }

    /**
     * Validate the token. This is basically the same call as getAuthorizationTokenInfo() to verify
     * that JWT token info can be retrieved.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse validateAuthToken() {
        //building basic header contains content-type and bearer token
        List<NameValuePair> headers;
        if ((headers = Utilities.createBasicHeadersWithBearerToken()) == null) {
            Log.d(TAG, ERR_INVALID_BEARER);
            return new CloudResponse(false, ERR_INVALID_BEARER);
        }
        //initiating get for validating token info
        HttpGetTask validateToken = new HttpGetTask();
        validateToken.setHeaders(headers);
        String url = objIotKit.prepareUrl(objIotKit.authTokenInfo, null);
        return super.invokeHttpExecuteOnURL(url, validateToken);
    }
}

