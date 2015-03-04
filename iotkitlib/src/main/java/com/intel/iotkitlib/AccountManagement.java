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
import com.intel.iotkitlib.http.HttpDeleteTask;
import com.intel.iotkitlib.http.HttpGetTask;
import com.intel.iotkitlib.http.HttpPostTask;
import com.intel.iotkitlib.http.HttpPutTask;
import com.intel.iotkitlib.models.AuthorizationToken;
import com.intel.iotkitlib.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Account management features
 */
public class AccountManagement extends ParentModule {

    // Errors
    public static final String ERR_ACCOUNT_NAME = "account Name cannot be empty";
    public static final String ERR_INVALID_IDS = "userId or accountId of new user cannot be null";
    public static final String ERR_INVALID_BODY = "problem with Http body creation to add user to account";
    private static final String TAG = "AccountManagement";

    /**
     * Module that handles accounts and user related operations; use this to do sync operation
     */
    public AccountManagement() {
        super(null);
    }

    /**
     * Module that handles accounts and user related operations.
     * <p/>
     * For more information, please refer to @link{https://github.com/enableiot/iotkit-api/wiki/Account-Management}
     *
     * @param requestStatusHandler The handler for asynchronously request to return data and status
     *                             from the cloud.
     */
    public AccountManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    /**
     * Create an account with a name.
     *
     * @param accountName name of the account to be created.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse createAnAccount(String accountName) {
        if (accountName == null) {
            Log.d(TAG, ERR_ACCOUNT_NAME);
            return new CloudResponse(false, ERR_ACCOUNT_NAME);
        }
        String body = "{\"name\":" + "\"" + accountName + "\"" + "}";
        //initiating post for authorization
        HttpPostTask createAnAccount = new HttpPostTask();

        createAnAccount.setHeaders(basicHeaderList);
        createAnAccount.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.createAnAccount, null);
        RequestStatusHandler preProcessing = new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                Log.d(TAG, String.valueOf(response.getCode()));
                Log.d(TAG, response.getResponse());
                try {
                    // Store account and id in preferences
                    AuthorizationToken.parseAndStoreAccountIdAndName(response.getResponse());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        return super.invokeHttpExecuteOnURL(url, createAnAccount, preProcessing);
    }

    /**
     * Get the information about an account.
     *
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse getAccountInformation() {
        //initiating get for account info
        HttpGetTask getAccountInfo = new HttpGetTask();
        getAccountInfo.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.getAccountInfo, null);
        return super.invokeHttpExecuteOnURL(url, getAccountInfo);
    }

    /**
     * Get the account activation code which is the transient code that can be used to activate
     * devices for the account. It expires after one hour.
     *
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse getAccountActivationCode() {
        //initiating get for account activation code
        HttpGetTask getAccountActivationCode = new HttpGetTask();
        getAccountActivationCode.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.getActivationCode, null);
        RequestStatusHandler preProcessing = new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                Log.d(TAG, String.valueOf(response.getCode()));
                Log.d(TAG, response.getResponse());
                try {
                    // Store account and id in preferences
                    AuthorizationToken.parseAndStoreActivationCode(response.getResponse());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        return super.invokeHttpExecuteOnURL(url, getAccountActivationCode, preProcessing);
    }

    /**
     * Force a renewal of the account activation code.
     *
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse renewAccountActivationCode() {
        //initiating put for account activation code
        HttpPutTask renewActivationCode = new HttpPutTask();
        renewActivationCode.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.renewActivationCode, null);
        return super.invokeHttpExecuteOnURL(url, renewActivationCode);
    }

    /* TODO: Fix update account */
    public CloudResponse updateAnAccount(final String accountNameToUpdate) {
        //initiating put for update of account
        HttpPutTask updateAccount = new HttpPutTask();
        RequestStatusHandler preProcessing = new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                try {
                    AuthorizationToken.parseAndStoreAccountName(accountNameToUpdate, response.getCode());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        updateAccount.setHeaders(basicHeaderList);
        //populating the JSON body of account updation
        String body = null;
        try {
            body = Utilities.createBodyForUpdateAccount(accountNameToUpdate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (body != null) {
            updateAccount.setRequestBody(body);
        }
        String url = objIotKit.prepareUrl(objIotKit.updateAccount, null);
        return super.invokeHttpExecuteOnURL(url, updateAccount, preProcessing);
    }

    /**
     * Delete the current account.
     *
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse deleteAnAccount() {
        //initiating Delete of an account
        HttpDeleteTask deleteAccount = new HttpDeleteTask();
        deleteAccount.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.deleteAccount, null);
        return super.invokeHttpExecuteOnURL(url, deleteAccount);
    }

    /* TODO: Add change user privileges */

    /**
     * Add another user to your account.
     *
     * @param accountId     The account id of the other user.
     * @param inviteeUserId The user id of the other user.
     * @param isAdmin       The role for this user in the current account.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse addAnotherUserToYourAccount(String accountId, String inviteeUserId, Boolean isAdmin) throws JSONException {
        if (accountId == null || inviteeUserId == null) {
            Log.d(TAG, ERR_INVALID_IDS);
            return new CloudResponse(false, ERR_INVALID_IDS);
        }
        //initiating put for adding another user to account
        HttpPutTask addUser = new HttpPutTask();

        //populating the JSON body of adding user to account
        String body;
        if ((body = createBodyForAddingUserToAccount(accountId, inviteeUserId, isAdmin)) == null) {
            Log.d(TAG, ERR_INVALID_BODY);
            return new CloudResponse(false, ERR_INVALID_BODY);
        }
        addUser.setHeaders(basicHeaderList);
        addUser.setRequestBody(body);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("invitee_user_id", inviteeUserId);
        linkedHashMap.put("account_id", accountId);
        String url = objIotKit.prepareUrl(objIotKit.addUserToAccount, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, addUser);
    }

    private String createBodyForAddingUserToAccount(String accountId, String inviteeUserId, Boolean isAdmin) throws JSONException {
        JSONObject addUserJson = new JSONObject();
        addUserJson.put("id", inviteeUserId);
        JSONObject accountsJson = new JSONObject();
        if (isAdmin) {
            accountsJson.put(accountId, "admin");
        } else {
            accountsJson.put(accountId, "user");
        }
        addUserJson.put("accounts", accountsJson);
        return addUserJson.toString();
    }
}
