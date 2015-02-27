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

import com.intel.iotkitlib.ParentModule;
import com.intel.iotkitlib.RequestStatusHandler;
import com.intel.iotkitlib.http.CloudResponse;
import com.intel.iotkitlib.http.HttpDeleteTask;
import com.intel.iotkitlib.http.HttpGetTask;
import com.intel.iotkitlib.http.HttpPostTask;
import com.intel.iotkitlib.http.HttpPutTask;
import com.intel.iotkitlib.http.HttpTaskHandler;
import com.intel.iotkitlib.models.AuthorizationToken;
import com.intel.iotkitlib.utils.IotKit;
import com.intel.iotkitlib.utils.Utilities;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * User management module
 */
public class UserManagement extends ParentModule {
    private final static String TAG = "UserManagement";

    // Errors
    public final static String ERR_INVALID_ID = "invalid user id";
    public final static String ERR_INVALID_ATTRS = "attributes cannot be empty";
    public final static String ERR_INVALID_BODY = "invalid body";
    public final static String ERR_INVALID_EMAIL = "emailID cannot be empty";
    public final static String ERR_INVALID_TOKEN = "neither token nor newPassword cannot be empty";

    /**
     * User management features; use this to do sync operation.
     */
    public UserManagement() { super(null); }

    /**
     * User management features
     *
     * For more info, please refer to @link{https://github.com/enableiot/iotkit-api/wiki/User-Management}
     *
     * @param requestStatusHandler The handler for asynchronously request to return data and status
     *                             from the cloud
     */
    public UserManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    /**
     * Create a new user
     * @param emailID the email id for the user which is used as an identifier
     * @param password the password for the user
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse createNewUser(String emailID, String password) throws JSONException {
        String body = validateAndCreateHttpBodyForNewUser(emailID, password);
        //adding header pair
        List<NameValuePair> headers = Utilities.addHttpHeaders(Utilities.createEmptyListForHeaders(),
                IotKit.HEADER_CONTENT_TYPE_NAME, IotKit.HEADER_CONTENT_TYPE_JSON);
        //initiating post for create new user
        HttpPostTask createUser = new HttpPostTask();
        RequestStatusHandler preProcessing = new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                Log.d(TAG, String.valueOf(response.getCode()));
                Log.d(TAG, response.getResponse());
                try {
                    // Store user id in preferences
                    AuthorizationToken.parseAndStoreUserId(response.getResponse(), response.getCode());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        createUser.setHeaders(headers);
        createUser.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.createUser, null);
        return super.invokeHttpExecuteOnURL(url, createUser, preProcessing);
    }

    /**
     * Delete a user
     * @param userId the identifier for the user to be deleted from the cloud
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse deleteAUser(String userId) {
        //initiating get for user deletion
        HttpDeleteTask deleteUser = new HttpDeleteTask();
        RequestStatusHandler preProcessing = new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                Log.d(TAG, String.valueOf(response.getCode()));
                Log.d(TAG, response.getResponse());
                try {
                    AuthorizationToken.resetSharedPreferences(response.getCode());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String tempUserId = validateAndGetUserId(userId);
        if (tempUserId == null) {
            Log.d(TAG, ERR_INVALID_ID);
            return new CloudResponse(false, ERR_INVALID_ID);
        }
        deleteUser.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.deleteUser, createHashMapWithUserID(tempUserId));
        return super.invokeHttpExecuteOnURL(url, deleteUser, preProcessing);
    }

    /**
     * Get user information
     * @param userId the identifier for the user for retrieving user information for
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse getUserInfo(String userId) {
        //initiating get for user info
        HttpGetTask getUserInfo = new HttpGetTask();
        String tempUserId = validateAndGetUserId(userId);
        if (tempUserId == null) {
            Log.d(TAG, ERR_INVALID_ID);
            return new CloudResponse(false, ERR_INVALID_ID);
        }
        getUserInfo.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.getUserInfo, createHashMapWithUserID(tempUserId));
        return super.invokeHttpExecuteOnURL(url, getUserInfo);
    }

    /**
     * Update the user attributes for a given user
     * @param userId The identifier for the user to update the attributes for
     * @param userAttributes A list of name value pairs that specify the user attributes for the user
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse updateUserAttributes(String userId, List<NameValuePair> userAttributes) throws JSONException {
        String tempUserId = validateAndGetUserId(userId);
        if (tempUserId == null) {
            Log.d(TAG, ERR_INVALID_ID);
            return new CloudResponse(false, ERR_INVALID_ID);
        }
        if (userAttributes == null) {
            Log.d(TAG, ERR_INVALID_ATTRS);
            return new CloudResponse(false, ERR_INVALID_ATTRS);
        }
        String body;
        if ((body = createBodyForUserAttributesUpdation(tempUserId, userAttributes)) == null) {
            return new CloudResponse(false, ERR_INVALID_BODY);
        }
        //initiating put for user attributes updation
        HttpPutTask updateUserAttributes = new HttpPutTask();
        updateUserAttributes.setHeaders(basicHeaderList);
        updateUserAttributes.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.updateUserAttributes, createHashMapWithUserID(tempUserId));
        return super.invokeHttpExecuteOnURL(url, updateUserAttributes);
    }

    /**
     * Accept the terms and conditions for an user
     * @param userId The identifier for the user that either accepts or rejects the terms and conditions
     * @param accept true for accepting or fals for rejecting the terms and conditions
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse acceptTermsAndConditions(String userId, boolean accept) throws JSONException {
        String tempUserId = validateAndGetUserId(userId);
        if (tempUserId == null) {
            Log.d(TAG, ERR_INVALID_ID);
            return new CloudResponse(false, ERR_INVALID_ID);
        }
        String body;
        if ((body = createBodyForTermsAndConditionsAcceptance(tempUserId, accept)) == null) {
            return new CloudResponse(false, ERR_INVALID_BODY);
        }
        //initiating put for user acceptance for terms and conditions
        HttpPutTask acceptTermsAndConditions = new HttpPutTask();
        acceptTermsAndConditions.setHeaders(basicHeaderList);
        acceptTermsAndConditions.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.acceptTermsAndConditions, createHashMapWithUserID(tempUserId));
        return super.invokeHttpExecuteOnURL(url, acceptTermsAndConditions);
    }

    /**
     * Request for change of password
     * @param emailId The email address of the user that requests for a change of password
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse requestChangePassword(String emailId) throws JSONException {
        if (emailId == null) {
            Log.d(TAG, ERR_INVALID_EMAIL);
            return new CloudResponse(false, ERR_INVALID_EMAIL);
        }
        String body;
        if ((body = createBodyForRequestingChangePassword(emailId)) == null) {
            return new CloudResponse(false, ERR_INVALID_BODY);
        }
        //initiating post for change password request
        HttpPostTask reqChangepassword = new HttpPostTask();
        reqChangepassword.setHeaders(basicHeaderList);
        reqChangepassword.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.requestChangePassword, null);
        return super.invokeHttpExecuteOnURL(url, reqChangepassword);
    }

    /**
     * Update the password
     * @param token The token that is used access the cloud backend for updating the password
     * @param newPassword The new password for the user
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse updateForgotPassword(String token, String newPassword) throws JSONException {
        if (token == null || newPassword == null) {
            Log.d(TAG, ERR_INVALID_TOKEN);
            return new CloudResponse(false, ERR_INVALID_TOKEN);
        }
        String body;
        if ((body = createBodyForUpdatingForgotPassword(token, newPassword)) == null) {
            return new CloudResponse(false, ERR_INVALID_BODY);
        }
        //initiating put for update password request
        HttpPutTask updatePassword = new HttpPutTask();
        updatePassword.setHeaders(basicHeaderList);
        updatePassword.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.requestChangePassword, null);
        return super.invokeHttpExecuteOnURL(url, updatePassword);
    }

    /**
     * Change the password for the user
     * @param emailAddress The email address of the user
     * @param currentPassword The current password for the user
     * @param newPassword The new password for the user
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse changePassword(String emailAddress, String currentPassword, String newPassword) throws JSONException {
        if (emailAddress == null || currentPassword == null || newPassword == null) {
            Log.d(TAG, ERR_INVALID_EMAIL);
            return new CloudResponse(false, ERR_INVALID_EMAIL);
        }
        String body;
        if ((body = createBodyForChangePassword(currentPassword, newPassword)) == null) {
            return new CloudResponse(false, ERR_INVALID_BODY);
        }
        //initiating put for change password request
        HttpPutTask changePassword = new HttpPutTask();
        changePassword.setHeaders(basicHeaderList);
        changePassword.setRequestBody(body);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("email", emailAddress);
        String url = objIotKit.prepareUrl(objIotKit.changePassword, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, changePassword);
    }

    private String createBodyForChangePassword(String currentPassword, String newPassword) throws JSONException {
        JSONObject changePasswordJson = new JSONObject();
        changePasswordJson.put("currentpwd", currentPassword);
        changePasswordJson.put("password", newPassword);
        return changePasswordJson.toString();
    }

    private String createBodyForUpdatingForgotPassword(String token, String newPassword) throws JSONException {
        JSONObject updatePasswordJson = new JSONObject();
        updatePasswordJson.put("token", token);
        updatePasswordJson.put("password", newPassword);
        return updatePasswordJson.toString();
    }

    private String createBodyForRequestingChangePassword(String emailId) throws JSONException {
        JSONObject requestChangePwdJson = new JSONObject();
        requestChangePwdJson.put("email", emailId);
        return requestChangePwdJson.toString();
    }

    private String createBodyForTermsAndConditionsAcceptance(String userId, boolean accept) throws JSONException {
        JSONObject acceptTermsAndConditions = new JSONObject();
        acceptTermsAndConditions.put("id", userId);
        acceptTermsAndConditions.put("termsAndConditions", accept);
        return acceptTermsAndConditions.toString();
    }

    private String createBodyForUserAttributesUpdation(String userId, List<NameValuePair> userAttributes) throws JSONException {
        JSONObject updateAttributesJson = new JSONObject();
        updateAttributesJson.put("id", userId);
        JSONObject attributesJson = new JSONObject();
        for (NameValuePair nameValuePair : userAttributes) {
            attributesJson.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        updateAttributesJson.put("attributes", attributesJson);
        return updateAttributesJson.toString();
    }

    private String validateAndGetUserId(String userId) {
        String tempUserId = userId;
        if (tempUserId == null) {
            Log.d(TAG, "passed userId is NULL,trying to fetch the one from shared prefs.....");
            if (Utilities.sharedPreferences == null) {
                Log.d(TAG, "problem in getting user_id From shared prefs, as shared prefs is null");
                return null;
            }
            tempUserId = Utilities.sharedPreferences.getString("user_id", "");
            if (tempUserId == null) {
                Log.d(TAG, "problem in getting user_id From shared prefs, as user_id is nil");
                return null;
            }
        }
        return tempUserId;
    }

    private LinkedHashMap<String, String> createHashMapWithUserID(String userId) {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("user_id", userId);
        return linkedHashMap;
    }

    private String validateAndCreateHttpBodyForNewUser(String emailID, String password) throws JSONException {
        if (emailID == null) {
            Log.d(TAG, "emailID empty");
            return null;
        }
        if (password == null) {
            Log.d(TAG, "password empty");
            return null;
        }
        JSONObject createUserJson = new JSONObject();
        createUserJson.put("email", emailID);
        createUserJson.put("password", password);
        return createUserJson.toString();
    }
}
