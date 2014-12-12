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
import com.intel.iotkitlib.LibUtils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;


public class AccountManagement extends ParentModule {

    private static final String TAG = "AccountManagement";

    public AccountManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    public boolean createAnAccount(String accountName) {
        if (accountName == null) {
            Log.d(TAG, "account Name cannot be empty");
            return false;
        }
        String body = "{\"name\":" + "\"" + accountName + "\"" + "}";
        //initiating post for authorization
        HttpPostTask createAnAccount = new HttpPostTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                try {
                    AuthorizationToken.parseAndStoreAccountIdAndName(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                statusHandler.readResponse(responseCode, response);
            }
        });
        createAnAccount.setHeaders(basicHeaderList);
        createAnAccount.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.createAnAccount, null);
        return super.invokeHttpExecuteOnURL(url, createAnAccount, "create account");
    }

    public boolean getAccountInformation() {
        //initiating get for account info
        HttpGetTask getAccountInfo = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        getAccountInfo.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.getAccountInfo, null);
        return super.invokeHttpExecuteOnURL(url, getAccountInfo, "get account info");
    }

    public boolean getAccountActivationCode() {
        //initiating get for account activation code
        HttpGetTask getAccountActivationCode = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        getAccountActivationCode.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.getActivationCode, null);
        return super.invokeHttpExecuteOnURL(url, getAccountActivationCode, "get account activation code");
    }

    public boolean renewAccountActivationCode() {
//initiating put for account activation code
        HttpPutTask renewActivationCode = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        renewActivationCode.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.renewActivationCode, null);
        return super.invokeHttpExecuteOnURL(url, renewActivationCode, "renew activation code");
    }

    //To-DO
    public boolean updateAnAccount(final String accountNameToUpdate) {
        //initiating put for update of account
        HttpPutTask updateAccount = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
                try {
                    AuthorizationToken.parseAndStoreAccountName(accountNameToUpdate, responseCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
        return super.invokeHttpExecuteOnURL(url, updateAccount, "update account");
    }

    public boolean deleteAnAccount() {
        //initiating Delete of an account
        HttpDeleteTask deleteAccount = new HttpDeleteTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        deleteAccount.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.deleteAccount, null);
        return super.invokeHttpExecuteOnURL(url, deleteAccount, "delete account");
    }

    public boolean addAnotherUserToYourAccount(String accountId, String inviteeUserId, Boolean isAdmin) throws JSONException {
        if (accountId == null || inviteeUserId == null) {
            Log.d(TAG, "userId or accountId of new user cannot be null");
            return false;
        }
        //initiating put for adding another user to account
        HttpPutTask addUser = new HttpPutTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });

        //populating the JSON body of adding user to account
        String body = null;
        if ((body = createBodyForAddingUserToAccount(accountId, inviteeUserId, isAdmin)) == null) {
            Log.d(TAG, "problem with Http body creation to add user to account");
            return false;
        }
        addUser.setHeaders(basicHeaderList);
        addUser.setRequestBody(body);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("invitee_user_id", inviteeUserId);
        linkedHashMap.put("account_id", accountId);
        String url = objIotKit.prepareUrl(objIotKit.addUserToAccount, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, addUser, "add user to account");
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
