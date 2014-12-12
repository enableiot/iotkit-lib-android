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

import com.intel.iotkitlib.LibModules.RequestStatusHandler;
import com.intel.iotkitlib.LibUtils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AuthorizationToken {
    private static final String TAG = "AuthorizationToken";

    public static void parseAndStoreAuthorizationToken(String response, int responseCode) throws JSONException {
        if (responseCode != 200) {
            Log.d(TAG, "invalid response for token request");
            return;
        }
        final String token = AuthorizationToken.parseAuthorizationTokenJson(new JSONObject(response));
        //store token value to shared prefs
        storeToken(token);
        //IotKit.getInstance().authorizaionKey = token;
        //checks the validity of token
        Authorization authorization = new Authorization(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                try {
                    if (responseCode != 200) {
                        Log.w(TAG, "invalid token or responseCode");
                        return;
                    }
                    //store token associated details to shared prefs
                    storeTokenInfo(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        authorization.validateAuthToken();
    }

    //storing token string to shared prefs
    private static void storeToken(String token) {
        //validating shared prefs-editor
        if (Utilities.editor == null) {
            Log.w(TAG, "invalid shared preferences-editor object");
        }
        if (token == null) {
            Log.w(TAG, "Token cannot be empty");
        }
        Utilities.editor.putString("auth_token", token);
        Utilities.editor.commit();
    }

    public static void parseAndStoreUserId(String response, int responseCode) throws JSONException {
        if (responseCode != 201) {
            Log.d(TAG, "Invalid response for create user, not able to store userID");
            return;
        }
        JSONObject newUserJson = new JSONObject(response);
        AuthorizationToken.storeUserId(newUserJson.getString("id"));
    }

    public static void resetSharedPreferences(int responseCode) throws JSONException {
        if (responseCode != 204) {
            Log.d(TAG, "Invalid response for delete user, not able to delete userID");
            return;
        }
        //validating shared prefs-editor
        if (Utilities.editor == null) {
            Log.w(TAG, "invalid shared preferences-editor object");
        }
        Utilities.editor.clear().commit();
    }

    //storing user id to shared prefs
    private static void storeUserId(String userId) {
        //validating shared prefs-editor
        if (Utilities.editor == null) {
            Log.w(TAG, "invalid shared preferences-editor object");
        }
        Utilities.editor.putString("user_id", userId);
        Utilities.editor.commit();
    }

    //validate token-info and store in shared-prefs
    private static void storeTokenInfo(String response) throws JSONException {
        //token-info json extraction
        JSONObject authTokenInfoJson = new JSONObject(response);
        JSONObject payLoadJson = authTokenInfoJson.getJSONObject("payload");
        //validating shared pref editor
        if (Utilities.editor == null) {
            Log.e(TAG, "invalid shared preference editor");
            return;
        }
        Utilities.editor.putString("expiry", payLoadJson.getString("exp"));
        storeUserId(payLoadJson.getString("sub"));
        Utilities.editor.commit();
        JSONArray accountsArray = null;
        if (!payLoadJson.isNull("accounts")) {
            accountsArray = payLoadJson.getJSONArray("accounts");
        }
        //debugging purpose and TO-DO(need to store multiple account details)
        if (accountsArray != null && accountsArray.length() > 0) {
            JSONObject accountJson;
            //Set the first account details to shared prefs
            //accountJson = accountsArray.getJSONObject(0);
            accountJson = parseAndStoreAccountIdAndName(accountsArray.getJSONObject(0).toString());
            //printing all account details(id,name)
            for (int i = 0; i < accountsArray.length(); i++) {
                accountJson = accountsArray.getJSONObject(i);
                Log.d(TAG, accountJson.getString("name") + "=" + accountJson.getString("id"));
            }

        }
    }

    public static JSONObject parseAndStoreAccountIdAndName(String response) throws JSONException {
        //Set the first account details to shared prefs
        JSONObject accountJson = new JSONObject(response);
        Utilities.editor.putString("account_name", accountJson.getString("name"));
        Utilities.editor.putString("account_id", accountJson.getString("id"));
        Utilities.editor.commit();
        return accountJson;
    }

    public static void parseAndStoreAccountName(String accountName, int responseCode) throws JSONException {
        if (responseCode == 200) {
            Utilities.editor.putString("account_name", accountName);
            Utilities.editor.commit();
        }
    }

    private static String parseAuthorizationTokenJson(JSONObject tokenObject) throws JSONException {
        return tokenObject.getString("token");
    }

    public static void parseAndStoreAuthorizationTokenInfo(String response, int responseCode) throws JSONException {
        /*JSONObject tokenInfoJsonObject = new JSONObject(response);
        JSONObject payLoadJsonObject = tokenInfoJsonObject.getJSONObject("payload");
        JSONArray accountsJsonArray = payLoadJsonObject.getJSONArray("accounts");
        IotKit objIotKit = IotKit.getInstance();
        //extracting account name and account Id's if multiple exists
        if (accountsJsonArray != null && accountsJsonArray.length() > 0) {
            objIotKit.accountIds = new ArrayList<String>();
            objIotKit.accountName = accountsJsonArray.getJSONObject(0).getString("name");
        }
        for (int i = 0; accountsJsonArray != null && i < accountsJsonArray.length(); i++) {
            objIotKit.accountIds.add(accountsJsonArray.getJSONObject(i).getString("id"));
        }*/
        if (responseCode != 200) {
            Log.d(TAG, "invalid response for token info request");
            return;
        }
        storeTokenInfo(response);
    }
}
