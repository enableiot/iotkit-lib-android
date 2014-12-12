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

import android.util.Log;

import com.intel.iotkitlib.LibHttp.HttpDeleteTask;
import com.intel.iotkitlib.LibHttp.HttpGetTask;
import com.intel.iotkitlib.LibHttp.HttpPostTask;
import com.intel.iotkitlib.LibHttp.HttpPutTask;
import com.intel.iotkitlib.LibHttp.HttpTaskHandler;
import com.intel.iotkitlib.LibModules.AuthorizationManagement.AuthorizationToken;
import com.intel.iotkitlib.LibUtils.IotKit;
import com.intel.iotkitlib.LibUtils.Utilities;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

public class UserManagement extends ParentModule {
    private final static String TAG = "UserManagement";

    public UserManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    public boolean createNewUser(String emailID, String password) throws JSONException {
        String body = validateAndCreateHttpBodyForNewUser(emailID, password);
        //adding header pair
        List<NameValuePair> headers = Utilities.addHttpHeaders(Utilities.createEmptyListForHeaders(),
                IotKit.HEADER_CONTENT_TYPE_NAME, IotKit.HEADER_CONTENT_TYPE_JSON);
        //initiating post for create new user
        HttpPostTask createUser = new HttpPostTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                //parse and store auth-token
                try {
                    AuthorizationToken.parseAndStoreUserId(response, responseCode);
                } catch (JSONException je) {
                    je.printStackTrace();
                }
                statusHandler.readResponse(responseCode, response);
            }
        });
        createUser.setHeaders(headers);
        createUser.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.createUser, null);
        return super.invokeHttpExecuteOnURL(url, createUser, "new auth token");

    }

    public boolean deleteAUser(String userId) {
        //initiating get for user deletion
        HttpDeleteTask deleteUser = new HttpDeleteTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                try {
                    AuthorizationToken.resetSharedPreferences(responseCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                statusHandler.readResponse(responseCode, response);
            }
        });
        String tempUserId = validateAndGetUserId(userId);
        if (tempUserId == null) {
            Log.d(TAG, "userId empty");
            return false;
        }
        deleteUser.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.deleteUser, createHashMapWithUserID(tempUserId));
        return super.invokeHttpExecuteOnURL(url, deleteUser, "delete a user");
    }

    public boolean getUserInfo(String userId) {
        //initiating get for user info
        HttpGetTask getUserInfo = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        String tempUserId = validateAndGetUserId(userId);
        if (tempUserId == null) {
            Log.d(TAG, "userId empty");
            return false;
        }
        getUserInfo.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.getUserInfo, createHashMapWithUserID(tempUserId));
        return super.invokeHttpExecuteOnURL(url, getUserInfo, "get user info");
    }

    public boolean updateUserAttributes(String userId, List<NameValuePair> userAttributes) throws JSONException {
        String tempUserId = validateAndGetUserId(userId);
        if (tempUserId == null) {
            Log.d(TAG, "userId empty");
            return false;
        }
        if (userAttributes == null) {
            Log.d(TAG, "attributes cannot be empty");
            return false;
        }
        String body;
        if ((body = createBodyForUserAttributesUpdation(tempUserId, userAttributes)) == null) {
            return false;
        }
        //initiating put for user attributes updation
        HttpPutTask updateUserAttributes = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        updateUserAttributes.setHeaders(basicHeaderList);
        updateUserAttributes.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.updateUserAttributes, createHashMapWithUserID(tempUserId));
        return super.invokeHttpExecuteOnURL(url, updateUserAttributes, "update user attributes");
    }

    public boolean acceptTermsAndConditions(String userId, boolean accept) throws JSONException {
        String tempUserId = validateAndGetUserId(userId);
        if (tempUserId == null) {
            Log.d(TAG, "userId empty");
            return false;
        }
        String body;
        if ((body = createBodyForTermsAndConditionsAcceptance(tempUserId, accept)) == null) {
            return false;
        }
        //initiating put for user acceptance for terms and conditions
        HttpPutTask acceptTermsAndConditions = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        acceptTermsAndConditions.setHeaders(basicHeaderList);
        acceptTermsAndConditions.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.acceptTermsAndConditions, createHashMapWithUserID(tempUserId));
        return super.invokeHttpExecuteOnURL(url, acceptTermsAndConditions, "terms and conditions acceptance");
    }

    public boolean requestChangePassword(String emailId) throws JSONException {
        if (emailId == null) {
            Log.d(TAG, "emailID cannot be empty");
            return false;
        }
        String body;
        if ((body = createBodyForRequestingChangePassword(emailId)) == null) {
            return false;
        }
        //initiating post for change password request
        HttpPostTask reqChangepassword = new HttpPostTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        reqChangepassword.setHeaders(basicHeaderList);
        reqChangepassword.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.requestChangePassword, null);
        return super.invokeHttpExecuteOnURL(url, reqChangepassword, "request change password");
    }

    public boolean updateForgotPassword(String token, String newPassword) throws JSONException {
        if (token == null || newPassword == null) {
            Log.d(TAG, "neither token nor newPassword cannot be empty");
            return false;
        }
        String body;
        if ((body = createBodyForUpdatingForgotPassword(token, newPassword)) == null) {
            return false;
        }
        //initiating put for update password request
        HttpPutTask updatePassword = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        updatePassword.setHeaders(basicHeaderList);
        updatePassword.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.requestChangePassword, null);
        return super.invokeHttpExecuteOnURL(url, updatePassword, "update forgotten password");
    }

    public boolean changePassword(String emailAddress, String currentPassword, String newPassword) throws JSONException {
        if (emailAddress == null || currentPassword == null || newPassword == null) {
            Log.d(TAG, "email or currentPassword or newPassword cannot be empty");
            return false;
        }
        String body;
        if ((body = createBodyForChangePassword(currentPassword, newPassword)) == null) {
            return false;
        }
        //initiating put for change password request
        HttpPutTask changePassword = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        changePassword.setHeaders(basicHeaderList);
        changePassword.setRequestBody(body);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("email", emailAddress);
        String url = objIotKit.prepareUrl(objIotKit.changePassword, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, changePassword, "change password");
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
